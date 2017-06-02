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
package fr.paris.lutece.portal.web.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.daemon.TestDaemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;

public class DaemonsJspBeanTest extends LuteceTestCase
{
    private static final String JUNIT_DAEMON = "JUNIT";
    private static final String DAEMON_INTERVAL_DSKEY = "core.daemon." + JUNIT_DAEMON + ".interval";
    private DaemonsJspBean bean;
    private DaemonEntry _entry;
    private String origMaxInitialStartDelay;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        origMaxInitialStartDelay = setInitialStartDelay( );
        bean = new DaemonsJspBean( );
        _entry = new DaemonEntry( );
        _entry.setId( JUNIT_DAEMON );
        _entry.setClassName( TestDaemon.class.getName( ) );
        _entry.setPluginName( "core" );
        // AppDaemonService.registerDaemon will copy this datastore value in the entry.
        DatastoreService.setInstanceDataValue( DAEMON_INTERVAL_DSKEY, "1" );
        AppDaemonService.registerDaemon( _entry );
    }

    private String setInitialStartDelay( ) throws FileNotFoundException, IOException
    {
        File propertiesFile = new File( getResourcesDir( ) + "/WEB-INF/conf/daemons.properties" );
        Properties props = new Properties( );
        try( FileInputStream is = new FileInputStream( propertiesFile ) )
        {
            props.load( is );
        }
        String orig = props.getProperty( "daemon.maxInitialStartDelay" );
        props.setProperty( "daemon.maxInitialStartDelay", "1" );
        try( FileOutputStream out = new FileOutputStream( propertiesFile ) )
        {
            props.store( out, "junit" );
        }
        AppPropertiesService.reloadAll( );
        return orig;
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        DatastoreService.removeInstanceData( DAEMON_INTERVAL_DSKEY );
        AppDaemonService.stopDaemon( JUNIT_DAEMON );
        AppDaemonService.unregisterDaemon( JUNIT_DAEMON );
        restoreInitialStartDelay( origMaxInitialStartDelay );
        super.tearDown( );
    }

    private void restoreInitialStartDelay( String orig ) throws FileNotFoundException, IOException
    {
        File propertiesFile = new File( getResourcesDir( ) + "/WEB-INF/conf/daemons.properties" );
        Properties props = new Properties( );
        try( FileInputStream is = new FileInputStream( propertiesFile ) )
        {
            props.load( is );
        }
        if ( orig == null )
        {
            props.remove( "daemon.maxInitialStartDelay" );
        }
        else
        {
            props.setProperty( "daemon.maxInitialStartDelay", orig );
        }
        try( FileOutputStream out = new FileOutputStream( propertiesFile ) )
        {
            props.store( out, "junit" );
        }
        AppPropertiesService.reloadAll( );
    }

    public void testDoDaemonActionStart( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        long lReadyTime;
        long lScheduledTime;
        assertFalse( _entry.isRunning( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "START" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        lReadyTime = System.nanoTime( );
        bean.doDaemonAction( request ); // Daemon should run periodically with interval of 1s

        assertTrue( _entry.isRunning( ) );
        TestDaemon daemon = (TestDaemon) AppDaemonService.getDaemon( JUNIT_DAEMON );
        daemon.go( );
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        daemon.waitForCompletion( ); // Complete first run without a timeout

        lReadyTime = System.nanoTime( );
        daemon.go( );
        lScheduledTime = System.nanoTime( );
        long lRescheduleDurationMilliseconds = ( ( lScheduledTime - lReadyTime ) / 1000000 );
        long lMarginMilliseconds = 250;
        AppLogService.info( "Daemon scheduled after " + lRescheduleDurationMilliseconds + " ms" );

        daemon.waitForCompletion( ); // Complete second run without a timeout. More runs would follow if we continued

        assertTrue(
                "Daemon should be re-scheduled approximately 1 second after the end of the previous run, but got " + lRescheduleDurationMilliseconds + "ms",
                1000 - lMarginMilliseconds <= lRescheduleDurationMilliseconds && lRescheduleDurationMilliseconds <= 1000 + lMarginMilliseconds );
    }

    public void testDoDaemonActionStop( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        assertFalse( _entry.isRunning( ) );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run periodically with interval of 1s
        TestDaemon daemon = (TestDaemon) AppDaemonService.getDaemon( JUNIT_DAEMON );
        daemon.go( );
        daemon.waitForCompletion( );
        // We have about 1 second to stop the daemon
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "STOP" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        bean.doDaemonAction( request );
        assertFalse( _entry.isRunning( ) );
        try
        {
            // Here the daemon should not be relaunched after a 1s interval. So wait 2.5 seconds until a timeout.
            daemon.go( 2500, TimeUnit.MILLISECONDS );
            fail( "Daemon still running after stop" );
        }
        catch( TimeoutException e )
        {
            // ok
        }
    }

    public void testDoDaemonActionRun( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        long lReadyTime;
        long lScheduledTime;
        assertFalse( _entry.isRunning( ) );
        _entry.setInterval( 1000 );

        lReadyTime = System.nanoTime( );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run periodically with interval of 1000s
        TestDaemon daemon = (TestDaemon) AppDaemonService.getDaemon( JUNIT_DAEMON );
        daemon.go( );
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        daemon.waitForCompletion( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "RUN" ); // Manually do 1 run of the daemon now
        request.setParameter( "daemon", JUNIT_DAEMON );
        lReadyTime = System.nanoTime( );
        bean.doDaemonAction( request );

        daemon.go( ); // It should run in less than 1000 seconds !
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        daemon.waitForCompletion( );
    }

    public void testDoDaemonActionUpdateInterval( )
    {
        final long lTestInterval = 314159L;
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "UPDATE_INTERVAL" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( "interval", Long.toString( lTestInterval ) );
        bean.doDaemonAction( request );
        assertEquals( lTestInterval, _entry.getInterval( ) );
    }

    public void testDoDaemonActionUnknown( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "UNKNOWN" );
        bean.doDaemonAction( request ); // does not throw
    }
}
