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

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeoutException;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;

public class AppDaemonServiceConcurrentTest extends LuteceTestCase
{
    private static final String INTERVAL_VALUE = "10000";
    private static final String JUNIT_DAEMON = "JUNITAppDaemonServiceConcurrentTest";
    private static final String JUNIT_OTHERDAEMON = "OTHERJUNITAppDaemonServiceConcurrentTest";
    private static final String DAEMON_INTERVAL_DSKEY = "core.daemon." + JUNIT_DAEMON + ".interval";
    private static final String OTHERDAEMON_INTERVAL_DSKEY = "core.daemon." + JUNIT_OTHERDAEMON + ".interval";

    private DaemonEntry _entry;
    private DaemonEntry _otherEntry;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        log( "Creating daemon " + JUNIT_DAEMON );
        _entry = new DaemonEntry( );
        _entry.setId( JUNIT_DAEMON );
        _entry.setNameKey( JUNIT_DAEMON );
        _entry.setDescriptionKey( JUNIT_DAEMON );
        _entry.setClassName( TestDaemon.class.getName( ) );
        _entry.setPluginName( "core" );
        // AppDaemonService.registerDaemon will copy this datastore value in the
        // entry.
        DatastoreService.setInstanceDataValue( DAEMON_INTERVAL_DSKEY, INTERVAL_VALUE );
        AppDaemonService.registerDaemon( _entry );

        log( "Creating daemon " + JUNIT_OTHERDAEMON );
        _otherEntry = new DaemonEntry( );
        _otherEntry.setId( JUNIT_OTHERDAEMON );
        _otherEntry.setNameKey( JUNIT_OTHERDAEMON );
        _otherEntry.setDescriptionKey( JUNIT_OTHERDAEMON );
        _otherEntry.setClassName( TestConcurrentDaemon.class.getName( ) );
        _otherEntry.setPluginName( "core" );
        // AppDaemonService.registerDaemon will copy this datastore value in the
        // entry.
        DatastoreService.setInstanceDataValue( OTHERDAEMON_INTERVAL_DSKEY, INTERVAL_VALUE );
        AppDaemonService.registerDaemon( _otherEntry );
    }

    private void log( String message )
    {
        AppLogService.info( "{} : {}", this.getClass( ).getName( ), message );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        DatastoreService.removeInstanceData( DAEMON_INTERVAL_DSKEY );
        AppDaemonService.unregisterDaemon( JUNIT_DAEMON );
        DatastoreService.removeInstanceData( OTHERDAEMON_INTERVAL_DSKEY );
        AppDaemonService.unregisterDaemon( JUNIT_OTHERDAEMON );
        super.tearDown( );
    }

    public void testConcurrentDaemonRun( )
    {
        assertTrue( AppDaemonService.getDaemonEntries( ).contains( _entry ) );
        assertTrue( AppDaemonService.getDaemonEntries( ).contains( _otherEntry ) );

        AppDaemonService.startDaemon( JUNIT_DAEMON );
        AppDaemonService.startDaemon( JUNIT_OTHERDAEMON );

        final TestDaemon daemon = (TestDaemon) _entry.getDaemon( );
        ( (TestConcurrentDaemon) _otherEntry.getDaemon( ) ).setOther( daemon );

        AppDaemonService.signalDaemon( JUNIT_DAEMON );
        AppDaemonService.signalDaemon( JUNIT_OTHERDAEMON );
        try
        {
            // daemon only passes the "go" barrier if otherDeamon runs
            daemon.waitForCompletion( );
        }
        catch( InterruptedException | BrokenBarrierException | TimeoutException e )
        {
            fail( "The timeout indicates that the two daemons could not run simultaneously" );
        }

    }
}
