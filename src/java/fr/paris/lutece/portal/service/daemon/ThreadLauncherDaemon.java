/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

/**
 * Daemon that manage a pool of threads to launch runnables.
 */
public class ThreadLauncherDaemon extends Daemon
{
    private static final String THREAD_LAUNCHER_DAEMON_ID = "threadLauncherDaemon";

    /**
     * Runnable wrapper to signal the daemon on processing end
     */
    private class RunnableWrapper implements Runnable
    {
        private final Runnable _wrapped;

        /**
         * Constructor
         * 
         * @param wrapped
         *            the wrapped Runnable
         */
        public RunnableWrapper( Runnable wrapped )
        {
            _wrapped = wrapped;
        }

        @Override
        public void run( )
        {
            try
            {
                _wrapped.run( );
            }
            finally
            {
                // will clean up the dead thread
                // and permit execution of blocked tasks
                AppDaemonService.signalDaemon( THREAD_LAUNCHER_DAEMON_ID );
            }
        }

    }

    private static final String PROPERTY_MAX_NUMBER_THREAD = "daemon.threadLauncherDaemon.maxNumberOfThread";
    private static Deque<RunnableQueueItem> _stackItems = new ArrayDeque<>( );
    private Map<String, Thread> _mapThreadByKey = new HashMap<>( );
    private List<Thread> _listThread = new ArrayList<>( );

    /**
     * {@inheritDoc}
     */
    @Override
    public void run( )
    {
        int nMaxNumberThread = AppPropertiesService.getPropertyInt( PROPERTY_MAX_NUMBER_THREAD, 5 );

        // We remove dead threads from running thread collections
        String logs = removeDeadThreads( );

        RunnableQueueItem item = null;
        int nCurrentNumberRunningThreads = _mapThreadByKey.size( ) + _listThread.size( );

        List<RunnableQueueItem> listLockedItems = new ArrayList<>( );

        while ( ( nCurrentNumberRunningThreads < nMaxNumberThread ) && ( ( item = popItemFromQueue( ) ) != null ) )
        {
            String key = item.computeKey( );
            // If the item has a key, then we must make sure that another thread with the same key and plugin is not running
            if ( key != null )
            {
                Thread thread = _mapThreadByKey.get( key );

                if ( thread != null )
                {
                    if ( thread.isAlive( ) )
                    {
                        // The thread is already running. We declare this item as locked for this run of the daemon.
                        listLockedItems.add( item );
                    }
                    else
                    {
                        // Dead threads are removed from collections at the beginning of the run of the daemon
                        // We still check again that the thread is alive just in case it died during the run
                        thread = getThread( item );
                        _mapThreadByKey.put( key, thread );

                        // We do not increase the number of running threads, because we removed and add one
                    }
                }
                else
                {
                    thread = getThread( item );
                    _mapThreadByKey.put( key, thread );
                    nCurrentNumberRunningThreads++;
                }
            }
            else
            {
                // If it has no key, or if the plugin has not been set, we create a thread in the keyless collection
                Thread thread = getThread( item );
                _mapThreadByKey.put( key, thread );
                nCurrentNumberRunningThreads++;
            }
        }

        // We replace in the queue items that was locked
        listLockedItems.stream( ).forEach( ThreadLauncherDaemon::addItemToQueue );

        // If the maximum number of running threads has been reached, we end this run
        if ( nCurrentNumberRunningThreads >= nMaxNumberThread )
        {
            setLastRunLogs( logs + "Every threads are running. Daemon execution ending." );
            return;
        }

        setLastRunLogs( logs + "There is no more runnable to launch." );
    }

    private Thread getThread( RunnableQueueItem item )
    {
        Thread thread = new Thread( new RunnableWrapper( item.getRunnable( ) ) );
        thread.start( );
        return thread;
    }

    private String removeDeadThreads( )
    {
        List<String> listDeadThreadKeys = new ArrayList<>( );
        for ( Entry<String, Thread> threadEntry : _mapThreadByKey.entrySet( ) )
        {
            if ( !threadEntry.getValue( ).isAlive( ) )
            {
                listDeadThreadKeys.add( threadEntry.getKey( ) );
            }
        }

        for ( String strThreadKey : listDeadThreadKeys )
        {
            _mapThreadByKey.remove( strThreadKey );
        }

        List<Thread> listDeadThreads = new ArrayList<>( );

        for ( Thread thread : _listThread )
        {
            if ( !thread.isAlive( ) )
            {
                listDeadThreads.add( thread );
            }
        }

        String logs = "";
        if ( !listDeadThreads.isEmpty( ) )
        {
            logs = "Releasing " + listDeadThreads.size( ) + " dead threads.\n";
        }
        for ( Thread thread : listDeadThreads )
        {
            _listThread.remove( thread );
        }
        return logs;
    }

    /**
     * Add a runnable to the launch queue. It will be launched as soon as a thread is available.
     * 
     * @param runnable
     *            The runnable to execute
     * @param strKey
     *            The key of the runnable. Runnables of a given plugin are ensured that they will not be executed at the same time if they have the same key.
     * @param plugin
     *            The plugin the runnable is associated with
     */
    public static void addItemToQueue( Runnable runnable, String strKey, Plugin plugin )
    {
        RunnableQueueItem runnableItem = new RunnableQueueItem( runnable, strKey, plugin );

        synchronized( ThreadLauncherDaemon.class )
        {
            _stackItems.addLast( runnableItem );
        }

        AppDaemonService.signalDaemon( THREAD_LAUNCHER_DAEMON_ID );
    }

    /**
     * Add a runnable item to the queue
     * 
     * @param runnableItem
     *            The runnable item to add to the queue
     */
    private static synchronized void addItemToQueue( RunnableQueueItem runnableItem )
    {
        _stackItems.addLast( runnableItem );
    }

    /**
     * Pop the first item of the queue. The item is removed from the queue.
     * 
     * @return The first item of the queue, or null if the queue is empty
     */
    private static synchronized RunnableQueueItem popItemFromQueue( )
    {
        if ( CollectionUtils.isEmpty( _stackItems ) )
        {
            return null;
        }

        return _stackItems.pop( );
    }

    /**
     * Count the number of items in the queue.
     * 
     * @return The current number of items in the queue
     */
    public static synchronized Integer countItemsInQueue( )
    {
        return _stackItems.size( );
    }
}
