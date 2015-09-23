/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 *  this class provides methods to manage daemons services
 * */
public final class AppDaemonService
{
    private static final String PROPERTY_MAX_INITIAL_START_DELAY = "daemon.maxInitialStartDelay";
    private static final String PROPERTY_MAX_AWAIT_TERMINATION_DELAY = "daemon.maxAwaitTerminationDelay";
    private static final String PROPERTY_SCHEDULED_THREAD_CORE_POOL_SIZE = "daemon.ScheduledThreadCorePoolSize";
    private static final String PROPERTY_DAEMON_ON_STARTUP = ".onStartUp";
    private static final String PROPERTY_DAEMON_INTERVAL = ".interval";
    private static final String KEY_DAEMON_PREFIX = "core.daemon.";
    private static final int MAX_INITIAL_START_DELAY = AppPropertiesService.getPropertyInt( PROPERTY_MAX_INITIAL_START_DELAY,
            30 );
    private static final int MAX_AWAIT_TERMINATION_DELAY = AppPropertiesService.getPropertyInt( PROPERTY_MAX_AWAIT_TERMINATION_DELAY,
            15 );
    private static final int DAEMON_CORE_POOL_SIZE = AppPropertiesService.getPropertyInt( PROPERTY_SCHEDULED_THREAD_CORE_POOL_SIZE,
            30 );
    private static final Map<String, DaemonEntry> _mapDaemonEntries = new HashMap<String, DaemonEntry>(  );
    private static final ScheduledThreadPoolExecutor _scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool( DAEMON_CORE_POOL_SIZE,
            new DaemonThreadFactory(  ) );
    private static ConcurrentHashMap<String, ScheduledFuture<?>> _lRunningThread = new ConcurrentHashMap<String, ScheduledFuture<?>>(  );
    private static final Random _random = new Random(  );
    private static boolean _bInit;

    /** Private constructor */
    private AppDaemonService(  )
    {
    }

    /**
     * Performs initialization of the DaemonFactory.  Note that this should
     * return right away so that processing can continue (IE thread off
     * everything)
     * @throws LuteceInitException If an error occurred
     */
    public static synchronized void init(  ) throws LuteceInitException
    {
        // already initialized
        if ( _bInit )
        {
            return;
        }

        if ( _mapDaemonEntries.size(  ) > 0 )
        {
            // Unsynchronized daemon start
            int nInitialDaemon = 0;

            for ( DaemonEntry entry : _mapDaemonEntries.values(  ) )
            {
                if ( entry.onStartup(  ) )
                {
                    nInitialDaemon++;
                }
            }

            int nDelay = MAX_INITIAL_START_DELAY;

            if ( nInitialDaemon > 0 )
            {
                nDelay = MAX_INITIAL_START_DELAY / nInitialDaemon;
            }

            int nInitialDelay = 0;

            // Register daemons
            for ( DaemonEntry entry : _mapDaemonEntries.values(  ) )
            {
                // starts any daemon declared as startup daemons
                if ( entry.onStartup(  ) )
                {
                    nInitialDelay += nDelay;

                    scheduleThread( entry, nInitialDelay );
                }
            }
        }

        _bInit = true;
    }

    /**
     * Register a daemon by its entry
     * @param entry The daemon entry
     * @throws LuteceInitException If an error occurred
     */
    public static void registerDaemon( DaemonEntry entry )
        throws LuteceInitException
    {
        String strIntervalKey = getIntervalKey( entry.getId(  ) );
        String strIntervalKeyDefaultValue = null;

        //init interval value if no exists
        if ( !DatastoreService.existsInstanceKey( strIntervalKey ) )
        {
            strIntervalKeyDefaultValue = AppPropertiesService.getProperty( "daemon." + entry.getId(  ) + ".interval",
                    "10" );
            DatastoreService.setInstanceDataValue( strIntervalKey, strIntervalKeyDefaultValue );
        }

        String strIntervalKeyValue = DatastoreService.getInstanceDataValue( strIntervalKey, strIntervalKeyDefaultValue );

        long lInterval = Long.valueOf( strIntervalKeyValue );

        String strOnStartupKey = getOnStartupKey( entry.getId(  ) );
        String strOnStartupDefaultValue = null;

        //init onStartup value if no exists
        if ( !DatastoreService.existsInstanceKey( strOnStartupKey ) )
        {
            strOnStartupDefaultValue = AppPropertiesService.getProperty( "daemon." + entry.getId(  ) + ".onstartup", "0" )
                                                           .equals( "1" ) ? DatastoreService.VALUE_TRUE
                                                                          : DatastoreService.VALUE_FALSE;
            DatastoreService.setInstanceDataValue( strOnStartupKey, strOnStartupDefaultValue );
        }

        String strOnStarupvalue = DatastoreService.getInstanceDataValue( strOnStartupKey, strOnStartupDefaultValue );
        boolean bOnStartup = Boolean.valueOf( strOnStarupvalue );

        entry.setInterval( lInterval );
        entry.setOnStartUp( bOnStartup );

        try
        {
            entry.loadDaemon(  );
        }
        catch ( ClassNotFoundException e )
        {
            throw new LuteceInitException( "Couldn't instantiate daemon: " + entry.getId(  ), e );
        }
        catch ( InstantiationException e )
        {
            throw new LuteceInitException( "Couldn't instantiate daemon: " + entry.getId(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new LuteceInitException( "Couldn't instantiate daemon: " + entry.getId(  ), e );
        }

        // Add plugin name to Daemon class
        if ( entry.getPluginName(  ) != null )
        {
            entry.getDaemon(  ).setPluginName( entry.getPluginName(  ) );
        }

        _mapDaemonEntries.put( entry.getId(  ), entry );

        AppLogService.info( "New Daemon registered : " + entry.getId(  ) );
    }

    /**
     * Unregister a daemon
     * @param strDaemonKey The daemon key
     */
    public static void unregisterDaemon( String strDaemonKey )
    {
        unScheduleThread( _mapDaemonEntries.get( strDaemonKey ) );
        _mapDaemonEntries.remove( strDaemonKey );
    }

    /**
     * Starts a daemon
     * @param strDaemonKey The daemon key
     */
    public static void startDaemon( String strDaemonKey )
    {
        scheduleThread( _mapDaemonEntries.get( strDaemonKey ) );
    }

    /**
     * Stops a daemon
     * @param strDaemonKey The daemon key
     */
    public static void stopDaemon( String strDaemonKey )
    {
        unScheduleThread( _mapDaemonEntries.get( strDaemonKey ) );
    }

    /**
     * modify daemon interval
     * @param strDaemonKey The daemon key
     * @param strDaemonInterval the daemon interval
     */
    public static void modifyDaemonInterval( String strDaemonKey, String strDaemonInterval )
    {
        DaemonEntry entry = _mapDaemonEntries.get( strDaemonKey );

        if ( entry != null )
        {
            entry.setInterval( new Long( strDaemonInterval ) );
            DatastoreService.setInstanceDataValue( getIntervalKey( entry.getId(  ) ), strDaemonInterval );
        }
    }

    /**
     * Add daemon to schedule's queue
     * @param entry The DaemonEntry
     */
    private static void scheduleThread( DaemonEntry entry )
    {
        scheduleThread( entry, _random.nextInt( MAX_INITIAL_START_DELAY ) );
    }

    /**
     *  Add daemon to schedule's queue
     * @param entry The DaemonEntry
     * @param nInitialDelay Initial start delay
     */
    private static void scheduleThread( DaemonEntry entry, int nInitialDelay )
    {
        ScheduledFuture<?> result = _lRunningThread.get( entry.getId(  ) );

        if ( result == null )
        {
            ScheduledFuture<?> task = _scheduler.scheduleAtFixedRate( entry.getDaemonThread(  ), nInitialDelay,
                    entry.getInterval(  ), TimeUnit.SECONDS );

            _lRunningThread.putIfAbsent( entry.getId(  ), task );
            AppLogService.info( "Starting daemon '" + entry.getId(  ) + "'" );
        }

        entry.setIsRunning( true );
        //update onStartup property
        DatastoreService.setInstanceDataValue( getOnStartupKey( entry.getId(  ) ), DatastoreService.VALUE_TRUE );
    }

    /**
     * Remove daemon from schedule's queue
     * @param entry The DaemonEntry
     */
    private static void unScheduleThread( DaemonEntry entry )
    {
        cancelScheduledThread( entry.getId(  ) );
        entry.setIsRunning( false );
        //update onStartup property
        DatastoreService.setInstanceDataValue( getOnStartupKey( entry.getId(  ) ), DatastoreService.VALUE_FALSE );
        AppLogService.info( "Stopping daemon '" + entry.getId(  ) + "'" );
    }

    /**
     * Cancel scheduled thread (don't interrupt if it is running )
     * @param strEntryId The DaemonEntry Id
     */
    protected static void cancelScheduledThread( String strEntryId )
    {
        ScheduledFuture<?> task = _lRunningThread.get( strEntryId );

        if ( task != null )
        {
            task.cancel( false );
            _scheduler.remove( (Runnable) task );
            _scheduler.purge(  );
            _lRunningThread.remove( strEntryId );
        }
    }

    /**
     * Get the current known DaemonEntries within the DaemonFactory
     *
     * @return the entries list of daemons declaration
     */
    public static Collection<DaemonEntry> getDaemonEntries(  )
    {
        return _mapDaemonEntries.values(  );
    }

    /**
     * Performs the shutdown of the DaemonFactory.
     */
    public static void shutdown(  )
    {
        AppLogService.info( 
            "Lutece daemons scheduler stop requested : trying to terminate gracefully daemons list (max wait " +
            MAX_AWAIT_TERMINATION_DELAY + " s)." );
        _scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy( false );
        _scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy( false );
        _scheduler.shutdown(  );

        try
        {
            if ( _scheduler.awaitTermination( MAX_AWAIT_TERMINATION_DELAY, TimeUnit.SECONDS ) )
            {
                AppLogService.info( "All daemons shutdown successfully." );
            }
            else
            {
                AppLogService.info( _scheduler.getActiveCount(  ) +
                    " daemons still running, trying to interrupt them..." );
                _scheduler.shutdownNow(  );

                if ( _scheduler.awaitTermination( 1, TimeUnit.SECONDS ) )
                {
                    AppLogService.info( "All running daemons successfully interrupted." );
                }
                else
                {
                    AppLogService.error( "Interrupt failed : " + _scheduler.getActiveCount(  ) +
                        " daemons still running." );
                }
            }
        }
        catch ( InterruptedException e )
        {
            AppLogService.error( "Error during waiting for daemons termination", e );
        }
        finally
        {
            _scheduler.purge(  );
        }
    }

    /**
     * Gets a daemon object from its key name
     *
     * @param strDaemonKey The daemon key
     * @return The daemon
     */
    public static Daemon getDaemon( String strDaemonKey )
    {
        DaemonEntry entry = _mapDaemonEntries.get( strDaemonKey );

        return entry.getDaemon(  );
    }

    /**
     * return the OnStartup key link to the daemon
     * @param strDaemonKey The daemon key
     * @return The key
     */
    private static String getOnStartupKey( String strDaemonKey )
    {
        return KEY_DAEMON_PREFIX + strDaemonKey + PROPERTY_DAEMON_ON_STARTUP;
    }

    /**
     * return the Interval key link to the daemon
     * @param strDaemonKey The daemon key
     * @return The key
     */
    private static String getIntervalKey( String strDaemonKey )
    {
        return KEY_DAEMON_PREFIX + strDaemonKey + PROPERTY_DAEMON_INTERVAL;
    }
}
