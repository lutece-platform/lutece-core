/*
 * Copyright (c) 2002-2007, Mairie de Paris
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

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 *  this class provides methods to manage daemons services
 * */
public class AppDaemonService
{
    private static Map<String, DaemonEntry> _mapDaemonEntries = new HashMap<String, DaemonEntry>(  );
    private static boolean _bInit;

    /**
     * Performs initialization of the DaemonFactory.  Note that this should
     * return right away so that processing can continue (IE thread off
     * everything)
     */
    public static synchronized void init(  ) throws LuteceInitException
    {
        // already initialized
        if ( _bInit )
        {
            return;
        }

        // Register core default daemons
        DaemonEntry entryIndexer = new DaemonEntry(  );
        entryIndexer.setId( "indexer" );

        //        registerDaemon( CORE_DAEMON_INDEXER );
        for ( DaemonEntry entry : _mapDaemonEntries.values(  ) )
        {
            // starts any daemon declared as startup daemons
            if ( entry.onStartup(  ) )
            {
                entry.startThread(  );
            }
        }

        _bInit = true;
    }

    public static void registerDaemon( DaemonEntry entry )
        throws LuteceInitException
    {
        long lInterval = (long) AppPropertiesService.getPropertyInt( "daemon." + entry.getId(  ) + ".interval", 10 );
        boolean bOnStartup = ( 1 == AppPropertiesService.getPropertyInt( "daemon." + entry.getId(  ) + ".onstartup", 0 ) );
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

        AppLogService.info( "New daemon registered : '" + entry.getId(  ) + "'" );
    }

    public static void unregisterDaemon( String strDaemonKey )
    {
    }

    public static void startDaemon( String strDaemonKey )
    {
        DaemonEntry entry = _mapDaemonEntries.get( strDaemonKey );
        entry.startThread(  );
    }

    public static void stopDaemon( String strDaemonKey )
    {
        DaemonEntry entry = _mapDaemonEntries.get( strDaemonKey );
        entry.stopThread(  );
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
        for ( DaemonEntry entry : _mapDaemonEntries.values(  ) )
        {
            entry.stopThread(  );
        }
    }

    /**
     * Gets a daemon object from its key name
     * @return The daemon
     */
    public static Daemon getDaemon( String strDaemonKey )
    {
        DaemonEntry entry = _mapDaemonEntries.get( strDaemonKey );

        return entry.getDaemon(  );
    }
}
