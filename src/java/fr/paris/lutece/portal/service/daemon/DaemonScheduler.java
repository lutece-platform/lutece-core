/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Daemon scheduler.
 * <p>
 * Responsible for ensuring on demand or timely daemon execution. Starts a thread which monitor the queue for daemons to execute. A {@link Timer} handles
 * repeating daemons runs.
 * 
 * <p>
 * Daemon run requests are coalesced. If a daemon is already running when a request comes, a new run is scheduled right after the current run ends.
 */
@ApplicationScoped
class DaemonScheduler implements Runnable, IDaemonScheduler
{
    private static final String PROPERTY_MAX_AWAIT_TERMINATION_DELAY = "daemon.maxAwaitTerminationDelay";

    private BlockingQueue<DaemonEntry> _queue;
    @Inject
    @DaemonExecutor
    private ExecutorService _executor;
    @Resource
    private ManagedThreadFactory _managedThreadFactory;
    private Thread _coordinatorThread;
    private ScheduledExecutorService _scheduledExecutorService;
    private Map<String, RunnableWrapper> _executingDaemons;
    private Map<String, DaemonManagedTask> _scheduledDaemons;
    private Map<String, ScheduledFuture<DaemonManagedTask>> _scheduledFutures;
    private volatile boolean _bShuttingDown;
    private int _maxAwaitTerminationDelay;


    /**
     * Constructor
     * 
     * @param queue
     *            the queue where daemon execution requests are stored
     * @param executor
     *            the executor service handling the execution of daemons
     */
    public DaemonScheduler( )
    {
       
    }

    @PostConstruct
    public void initDaemonScheduler( ){  	
    	_queue = new LinkedBlockingQueue<DaemonEntry>();
    	_scheduledExecutorService =  Executors.newScheduledThreadPool( 1 , _managedThreadFactory);
        _executingDaemons = new HashMap<>( );
        _scheduledDaemons = new HashMap<>( );
        _scheduledFutures = new HashMap<>( );
        _coordinatorThread = _managedThreadFactory.newThread( this);
        _coordinatorThread.setName(  "Lutece-Daemons-Coordinator"  );
        _coordinatorThread.setDaemon( true );
        _coordinatorThread.start( );
        _bShuttingDown = false;
        _maxAwaitTerminationDelay = AppPropertiesService.getPropertyInt( PROPERTY_MAX_AWAIT_TERMINATION_DELAY, 15 );
    }
    
    void initDaemonScheduler(BlockingQueue<DaemonEntry> queue, ExecutorService executor, ManagedThreadFactory managedThreadFactory) {
        _queue = queue;
        _managedThreadFactory = managedThreadFactory;
        _executor = executor;
        _scheduledExecutorService =  Executors.newScheduledThreadPool( 1 , _managedThreadFactory);
        _executingDaemons = new HashMap<>( );
        _scheduledDaemons = new HashMap<>( );
        _scheduledFutures = new HashMap<>( );
        _coordinatorThread = _managedThreadFactory.newThread( this);
        _coordinatorThread.setName(  "Lutece-Daemons-Coordinator"  );
        _coordinatorThread.setDaemon( true );
        _coordinatorThread.start( );
        _bShuttingDown = false;
    }
    
    @Override
    public boolean enqueue( DaemonEntry entry, long nDelay, TimeUnit unit )
    {
        assertNotShuttingDown( );
        if ( nDelay == 0L )
        {
            boolean queued = _queue.offer( entry );
            if ( !queued )
            {
                AppLogService.error( "Failed to enqueue a run of daemon {}", entry.getId( ) );
            }
            return queued;
        }
        try
        {
            _scheduledExecutorService.schedule( entry.getDaemon( ), nDelay, unit );
            return true;
        }
        catch( IllegalStateException e )
        {
            return false;
        }
    }

    private void assertNotShuttingDown( )
    {
        if ( _bShuttingDown )
        {
            throw new IllegalStateException( "DaemonScheduler is shutting down. Enqueing tasks or scheduling tasks is not possible anymore." );
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
        assertNotShuttingDown( );
        synchronized( _scheduledDaemons )
        {
            if ( _scheduledDaemons.containsKey( entry.getId( ) ) )
            {
                AppLogService.error( "Daemon " + entry.getId( ) + " already scheduled, not scheduling again" );
            }
            else
            {
                DaemonManagedTask daemonManagedTask = new DaemonManagedTask( entry );
                ScheduledFuture sf = _scheduledExecutorService.scheduleAtFixedRate( daemonManagedTask ,  nInitialDelay , unit.convert(entry.getInterval( ), TimeUnit.SECONDS) , unit );
                _scheduledFutures.put( entry.getId( ), sf );
                _scheduledDaemons.put( entry.getId( ), daemonManagedTask );
            }
        }

    }

    @Override
    public void unSchedule( DaemonEntry entry )
    {
        synchronized( _scheduledDaemons )
        {
            DaemonManagedTask daemonTimerTask = _scheduledDaemons.get( entry.getId( ) );
            if ( daemonTimerTask == null )
            {
                AppLogService.error( "Could not unschedule daemon " + entry.getId( ) + " which was not scheduled" );
            }
            else
            {
                _scheduledFutures.get( entry.getId( ) ).cancel( true );
                _scheduledFutures.remove( entry.getId( ) );
                _scheduledDaemons.remove( entry.getId( ) );
            }
        }
        boolean bStopScheduled = false;
        synchronized( _executingDaemons )
        {
            RunnableWrapper runnable = _executingDaemons.get( entry.getId( ) );
            if ( runnable != null )
            {
                runnable.stopDaemonAfterExecution( );
                bStopScheduled = true;
            }
        }
        if ( !bStopScheduled )
        {
            try
            {
                entry.getDaemon( ).stop( );
            }
            catch( Throwable t )
            {
                AppLogService.error( "Failed to stop daemon {}", entry.getId( ), t );
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
        _bShuttingDown = true; // prevent future scheduling of daemons
        AppLogService
                .info( "Lutece daemons scheduler stop requested : trying to terminate gracefully daemons list (max wait " + _maxAwaitTerminationDelay + " s)." );
        _scheduledExecutorService.shutdown( );
        _coordinatorThread.interrupt( );
        _executor.shutdown( );

        // make a copy of scheduled daemons so that the list can be modified by
        // #unSchedule
        ArrayList<DaemonManagedTask> scheduled = new ArrayList<>( _scheduledDaemons.values( ) );
        scheduled.forEach( task -> unSchedule( task.getDaemonEntry( ) ) );

        try
        {
            if ( _executor.awaitTermination( _maxAwaitTerminationDelay, TimeUnit.SECONDS ) )
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
        private volatile boolean _bstopAfterExecution;

        /**
         * @param entry
         *            the wrapped DaemonEntry
         */
        public RunnableWrapper( DaemonEntry entry )
        {
            _entry = entry;
        }

        public void stopDaemonAfterExecution( )
        {
            _bstopAfterExecution = true;
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
                if ( _bstopAfterExecution )
                {
                    _entry.getDaemon( ).stop( );
                }
                else
                    if ( _bShouldEnqueueAgain )
                    {
                        enqueue( _entry );
                    }
            }
        }

    }

    /**
     * Managed task to enqueue daemon runs
     */
    private final class DaemonManagedTask implements Runnable
    {

        private final DaemonEntry _entry;

        /**
         * @param entry
         *            the daemon
         */
        public DaemonManagedTask( DaemonEntry entry )
        {
            _entry = entry;
        }

        @Override
        public void run( )
        {
            enqueue( _entry );
        }

        /**
         * Access the daemon entry
         * 
         * @return the daemon
         */
        public DaemonEntry getDaemonEntry( )
        {
            return _entry;
        }

    }

}
