/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.service.daemon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Daemon scheduler.
 * <p>
 * Responsible for ensuring on demand or timely daemon execution. Starts a thread which monitor the queue for daemons to execute. A {@link Timer} handles
 * repeating daemons runs.
 * 
 * <p>
 * Daemon run requests are coalesced. If a daemon is already running when a request comes, a new run is scheduled right after the current run ends.
 */
class DaemonScheduler implements Runnable, IDaemonScheduler
{
    private static final String PROPERTY_MAX_AWAIT_TERMINATION_DELAY = "daemon.maxAwaitTerminationDelay";

    private final BlockingQueue<DaemonEntry> _queue;
    private final ExecutorService _executor;
    private final Thread _coordinatorThread;
    private final Timer _scheduledDaemonsTimer;
    private final Map<String, RunnableWrapper> _executingDaemons;
    private final Map<String, DaemonTimerTask> _scheduledDaemons;

    /**
     * Constructor
     * 
     * @param queue
     *            the queue where daemon execution requests are stored
     * @param executor
     *            the executor service handling the execution of daemons
     */
    public DaemonScheduler( BlockingQueue<DaemonEntry> queue, ExecutorService executor )
    {
        _queue = queue;
        _executor = executor;
        _scheduledDaemonsTimer = new Timer( "Lutece-Daemons-Scheduled-Timer-Thread", true );
        _executingDaemons = new HashMap<>( );
        _scheduledDaemons = new HashMap<>( );
        _coordinatorThread = new Thread( this, "Lutece-Daemons-Coordinator" );
        _coordinatorThread.setDaemon( true );
        _coordinatorThread.start( );
    }

    @Override
    public boolean enqueue( DaemonEntry entry, long nDelay, TimeUnit unit )
    {
        if ( nDelay == 0L )
        {
            boolean queued = _queue.offer( entry );
            if ( !queued )
            {
                AppLogService.error( "Failed to enqueue a run of daemon " + entry.getId( ) );
            }
            return queued;
        }
        try
        {
            _scheduledDaemonsTimer.schedule( new DaemonTimerTask( entry ), unit.toMillis( nDelay ) );
            return true;
        }
        catch( IllegalStateException e )
        {
            return false;
        }
    }

    /**
     * Enqueue without delay
     * 
     * @param entry
     *            the daemon entry
     */
    private void enqueue( DaemonEntry entry )
    {
        enqueue( entry, 0L, TimeUnit.MILLISECONDS );
    }

    @Override
    public void schedule( DaemonEntry entry, long nInitialDelay, TimeUnit unit )
    {
        synchronized( _scheduledDaemons )
        {
            if ( _scheduledDaemons.containsKey( entry.getId( ) ) )
            {
                AppLogService.error( "Daemon " + entry.getId( ) + " already scheduled, not scheduling again" );
            }
            else
            {

                DaemonTimerTask daemonTimerTask = new DaemonTimerTask( entry );
                _scheduledDaemonsTimer.scheduleAtFixedRate( daemonTimerTask, unit.toMillis( nInitialDelay ), entry.getInterval( ) * 1000 );
                _scheduledDaemons.put( entry.getId( ), daemonTimerTask );
            }
        }

    }

    @Override
    public void unSchedule( DaemonEntry entry )
    {
        synchronized( _scheduledDaemons )
        {
            DaemonTimerTask daemonTimerTask = _scheduledDaemons.get( entry.getId( ) );
            if ( daemonTimerTask == null )
            {
                AppLogService.error( "Could not unschedule daemon " + entry.getId( ) + " which was not scheduled" );
            }
            else
            {
                daemonTimerTask.cancel( );
                _scheduledDaemonsTimer.purge( );
                _scheduledDaemons.remove( entry.getId( ) );
            }
        }
    }

    @Override
    public void run( )
    {
        // use a set to coalesce daemon signaling
        Set<DaemonEntry> queued = new HashSet<>( );
        do
        {
            try
            {
                // collect signaled daemons
                queued.add( _queue.take( ) );
                _queue.drainTo( queued );
            }
            catch( InterruptedException e )
            {
                // We were asked to stop
                break;
            }
            // execute them
            for ( DaemonEntry entry : queued )
            {
                RunnableWrapper runnable = null;
                synchronized( _executingDaemons )
                {
                    runnable = _executingDaemons.get( entry.getId( ) );
                    if ( runnable != null )
                    {
                        // already executing; schedule a new run after this one
                        runnable.shouldEnqueueAgain( );
                        runnable = null;
                    }
                    else
                    {
                        runnable = new RunnableWrapper( entry );
                        _executingDaemons.put( entry.getId( ), runnable );
                    }
                }
                if ( runnable != null )
                {
                    _executor.execute( runnable );
                }
            }
            // prepare next iteration
            queued.clear( );
        }
        while ( !Thread.interrupted( ) );
    }

    @Override
    public void shutdown( )
    {
        int maxAwaitTerminationDelay = AppPropertiesService.getPropertyInt( PROPERTY_MAX_AWAIT_TERMINATION_DELAY, 15 );
        AppLogService.info( "Lutece daemons scheduler stop requested : trying to terminate gracefully daemons list (max wait " + maxAwaitTerminationDelay
                + " s)." );
        _scheduledDaemonsTimer.cancel( );
        _scheduledDaemonsTimer.purge( );
        _coordinatorThread.interrupt( );
        _executor.shutdown( );
        try
        {
            if ( _executor.awaitTermination( maxAwaitTerminationDelay, TimeUnit.SECONDS ) )
            {
                AppLogService.info( "All daemons shutdown successfully." );
            }
            else
            {
                AppLogService.info( "Some daemons are still running, trying to interrupt them..." );
                _executor.shutdownNow( );

                if ( _executor.awaitTermination( 1, TimeUnit.SECONDS ) )
                {
                    AppLogService.info( "All running daemons successfully interrupted." );
                }
                else
                {
                    AppLogService.error( "Interrupt failed; daemons still running." );
                }
            }
        }
        catch( InterruptedException e )
        {
            AppLogService.error( "Interruped while waiting for daemons termination", e );
        }
    }

    /**
     * Wrapper for the daemon Runnable which can enqueue a new run when execution completes
     */
    private final class RunnableWrapper implements Runnable
    {

        private final DaemonEntry _entry;
        private volatile boolean _bShouldEnqueueAgain;

        /**
         * @param entry
         *            the wrapped DaemonEntry
         */
        public RunnableWrapper( DaemonEntry entry )
        {
            _entry = entry;
        }

        /**
         * Signals that an execution of the daemon should be enqueued on completion
         */
        public void shouldEnqueueAgain( )
        {
            _bShouldEnqueueAgain = true;
        }

        @Override
        public void run( )
        {
            try
            {
                _entry.getDaemonThread( ).run( );
            }
            finally
            {
                synchronized( _executingDaemons )
                {
                    _executingDaemons.remove( _entry.getId( ) );
                }
                if ( _bShouldEnqueueAgain )
                {
                    enqueue( _entry );
                }
            }
        }

    }

    /**
     * Timer task to enqueue daemon runs
     */
    private final class DaemonTimerTask extends TimerTask
    {

        private final DaemonEntry _entry;

        /**
         * @param entry
         *            the daemon
         */
        public DaemonTimerTask( DaemonEntry entry )
        {
            _entry = entry;
        }

        @Override
        public void run( )
        {
            enqueue( _entry );
        }

    }
}
