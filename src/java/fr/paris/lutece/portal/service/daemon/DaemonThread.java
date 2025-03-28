/*
 * Copyright (c) 2002-2025, City of Paris
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

import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Date;

/**
 * This class performs methods to manage threads of execution for a given daemon instance
 */
public class DaemonThread implements Runnable
{
    static final String DEAMON_THREAD_BEAN_NAME = "daemonThread";

    private DaemonEntry _entry;
    private String _strDaemonName;

    /**
     * Sets the daemon entry
     * 
     * @param entry
     *            The entry
     */
    public void setDaemonEntry( DaemonEntry entry )
    {
        _entry = entry;
    }

    /**
     * Get the daemon name
     * 
     * @return the daemon name
     */
    public String getDaemonName( )
    {
        return _strDaemonName;
    }

    /**
     * Execution process of the daemon associated with the thread This methods is called for regular interval
     */
    @Override
    public void run( )
    {
        if ( _entry.isRunning( ) )
        {
            if ( _strDaemonName == null )
            {
                _strDaemonName = "Daemon " + _entry.getId( );
            }

            Thread currentThread = Thread.currentThread( );
            String strPooledThreadName = currentThread.getName( );
            currentThread.setName( "Lutece-Daemon-" + _entry.getId( ) );

            Daemon daemon = _entry.getDaemon( );
            AppLogService.info( "{} - starts processing.", _strDaemonName );

            try
            {
                _entry.setLastRunDate( new Date( ) );

                if ( PluginService.isPluginEnable( daemon.getPluginName( ) ) )
                {
                    daemon.run( );
                    _entry.setLastRunLogs( daemon.getLastRunLogs( ) );
                }
                else
                {
                    _entry.setLastRunLogs( "Plugin not enabled" );
                }
            }
            catch( Throwable t )
            {
                AppLogService.error( "Could not process Daemon: {}", _entry.getId( ), t );
            }

            AppLogService.info( "{} - end of process.", _strDaemonName );
            currentThread.setName( strPooledThreadName );
        }
        else
        {
            AppLogService.error( "Daemon's {} run not processed because it has been stopped. Will unschedule.", _entry.getId( ) );
            AppDaemonService.cancelScheduledThread( _entry.getId( ) );
        }
    }
}
