/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
public class DaemonThread extends Thread
{
    private DaemonEntry _entry;

    /**
     * Constructor Creates the thread of execution from informations contained
     * in DaemonEntry structure
     * @param entry The entry
     */
    public DaemonThread( DaemonEntry entry )
    {
        super( "DaemonThread:" + entry.getId(  ) );
        _entry = entry;
        setPriority( Thread.MIN_PRIORITY );
    }

    /**
     * the execution method of the daemon thread
     */
    public void run(  )
    {
        Daemon daemon = _entry.getDaemon(  );

        //at the very minimum this daemon is processing...
        AppLogService.info( "Daemon '" + _entry.getId(  ) + "' - first process." );

        // Execution loop of the thread (in which the thread is put in sleep during a given interval)
        while ( true )
        {
            //move the seconds to milliseconds
            try
            {
                synchronized ( this )
                {
                    // Puts the thread in sleep
                    Thread.sleep( _entry.getInterval(  ) * 1000 );
                }
            }
            catch ( InterruptedException e )
            {
                break;

                //the DaemonFactory may want to stop this thread from
                //sleeping and call interrupt() on this thread.
            }

            // Runs the execution process of the daemon
            runDaemon( daemon );
        }

        AppLogService.info( "Daemon '" + _entry.getId(  ) + "' - stopped." );
    }

    /**
     * Execution process of the daemon associated with the thread This methods
     * is called for regular interval
     *
     * @param daemon the daemon instance to process
     */
    private void runDaemon( Daemon daemon )
    {
        //       daemon.setStatus( Daemon.STATUS_RUNNING );
        AppLogService.info( "Daemon '" + _entry.getId(  ) + "' - starts processing." );

        try
        {
            _entry.setLastRunDate( new Date(  ) );

            if ( PluginService.isPluginEnable( daemon.getPluginName(  ) ) )
            {
                daemon.run(  );
                _entry.setLastRunLogs( daemon.getLastRunLogs(  ) );
            }
            else
            {
                _entry.setLastRunLogs( "Plugin not enabled" );
            }
        }
        catch ( Throwable t )
        {
            AppLogService.error( "Could not process Daemon: " + _entry.getId(  ), t );
        }

        AppLogService.info( "Daemon '" + _entry.getId(  ) + "' - end of process." );
    }
}
