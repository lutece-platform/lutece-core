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

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.daemon.TestDaemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

public class DaemonsJspBeanTest extends LuteceTestCase
{
    private static final String TEMPLATE_MANAGE_DAEMONS = "admin/system/manage_daemons.html";
    private static final String JUNIT_DAEMON = "JUNIT";
    private static final String DAEMON_INTERVAL_DSKEY = "core.daemon." + JUNIT_DAEMON + ".interval";
    private DaemonsJspBean bean;
    private DaemonEntry _entry;
    private String origMaxInitialStartDelay;
    private TestDaemon _testDaemon;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        origMaxInitialStartDelay = setInitialStartDelay( );
        assertEquals( "Failed to adjust daemon initial start delay", 1, AppPropertiesService.getPropertyInt( "daemon.maxInitialStartDelay", 3000 ) );
        bean = new DaemonsJspBean( );
        _entry = new DaemonEntry( );
        _entry.setId( JUNIT_DAEMON );
        _entry.setNameKey( JUNIT_DAEMON );
        _entry.setDescriptionKey( JUNIT_DAEMON );
        _entry.setClassName( TestDaemon.class.getName( ) );
        _entry.setPluginName( "core" );
        // AppDaemonService.registerDaemon will copy this datastore value in the entry.
        DatastoreService.setInstanceDataValue( DAEMON_INTERVAL_DSKEY, "1" );
        AppDaemonService.registerDaemon( _entry );
        _testDaemon = (TestDaemon) AppDaemonService.getDaemon( JUNIT_DAEMON );
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
        // Attempt to unlock test daemon threads that would be blocked on the barriers, for
        // example if the test throws an unexpected exception, causing it to not call go() and waitforcompletion() to unlock the barriers.
        // A stuck thread would make the next test start with the daemon already running
        // (even though it is unregistered, because the logic to remove running daemons is done at the end of the thread),
        // so the calls the go() and waitcompletion() could be imbalanced. And it's highly
        // confusing that the tests keep this kind of state between them.
        // Note that the thread would die off eventually because the barriers have a timeout, but tests
        // continue to execute in the mean time and fail with obscure reasons.
        _testDaemon.resetGo( );
        _testDaemon.resetCompletion( );

        DatastoreService.removeInstanceData( DAEMON_INTERVAL_DSKEY );
        AppDaemonService.stopDaemon( JUNIT_DAEMON );
        AppDaemonService.unregisterDaemon( JUNIT_DAEMON );
        restoreInitialStartDelay( origMaxInitialStartDelay );
        assertEquals( "Failed to restore daemon initial start delay", origMaxInitialStartDelay,
                AppPropertiesService.getProperty( "daemon.maxInitialStartDelay" ) );
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

    public void testDoDaemonActionStart( ) throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        long lReadyTime;
        long lScheduledTime;
        assertFalse( _entry.isRunning( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "START" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) );
        lReadyTime = System.nanoTime( );
        bean.doDaemonAction( request ); // Daemon should run periodically with interval of 1s

        assertTrue( _entry.isRunning( ) );
        _testDaemon.go( );
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        _testDaemon.waitForCompletion( ); // Complete first run without a timeout

        lReadyTime = System.nanoTime( );
        _testDaemon.go( );
        lScheduledTime = System.nanoTime( );
        long lRescheduleDurationMilliseconds = ( ( lScheduledTime - lReadyTime ) / 1000000 );
        long lMarginMilliseconds = 250;
        AppLogService.info( "Daemon scheduled after " + lRescheduleDurationMilliseconds + " ms" );

        _testDaemon.waitForCompletion( ); // Complete second run without a timeout. More runs would follow if we continued

        assertTrue(
                "Daemon should be re-scheduled approximately 1 second after the end of the previous run, but got " + lRescheduleDurationMilliseconds + "ms",
                1000 - lMarginMilliseconds <= lRescheduleDurationMilliseconds && lRescheduleDurationMilliseconds <= 1000 + lMarginMilliseconds );
    }

    public void testDoDaemonActionStartInvalidToken( ) throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        assertFalse( _entry.isRunning( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "START" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) + "b" );
        try
        {
            bean.doDaemonAction( request ); // Daemon should run periodically with interval of 1s
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( _entry.isRunning( ) );
            try
            {
                // Here the daemon should not be launched 
                _testDaemon.go( 2500, TimeUnit.MILLISECONDS );
                fail( "Daemon running be should not" );
            }
            catch( TimeoutException te )
            {
                // ok
            }
        }
    }

    public void testDoDaemonActionStartNoToken( ) throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        assertFalse( _entry.isRunning( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "START" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        try
        {
            bean.doDaemonAction( request ); // Daemon should run periodically with interval of 1s
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( _entry.isRunning( ) );
            try
            {
                // Here the daemon should not be launched 
                _testDaemon.go( 2500, TimeUnit.MILLISECONDS );
                fail( "Daemon running be should not" );
            }
            catch ( TimeoutException te )
            {
                // ok
            }
        }
    }

    public void testDoDaemonActionStop( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        assertFalse( _entry.isRunning( ) );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run
                                                      // periodically with
                                                      // interval of 1s
        _testDaemon.go( );
        _testDaemon.waitForCompletion( );
        // We have about 1 second to stop the daemon
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "STOP" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) );
        bean.doDaemonAction( request );
        assertFalse( _entry.isRunning( ) );
        try
        {
            // Here the daemon should not be relaunched after a 1s interval. So
            // wait 2.5 seconds until a timeout.
            _testDaemon.go( 2500, TimeUnit.MILLISECONDS );
            fail( "Daemon still running after stop" );
        }
        catch ( TimeoutException e )
        {
            // ok
        }
    }

    public void testDoDaemonActionStopInvalidToken( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        assertFalse( _entry.isRunning( ) );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run
                                                      // periodically with
                                                      // interval of 1s
        _testDaemon.go( );
        _testDaemon.waitForCompletion( );
        // We have about 1 second to stop the daemon
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "STOP" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) + "b" );
        try
        {
            bean.doDaemonAction( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( _entry.isRunning( ) );
            _testDaemon.go( );
            _testDaemon.waitForCompletion( );
        }
    }

    public void testDoDaemonActionStopNoToken( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        assertFalse( _entry.isRunning( ) );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run
                                                      // periodically with
                                                      // interval of 1s
        _testDaemon.go( );
        _testDaemon.waitForCompletion( );
        // We have about 1 second to stop the daemon
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "STOP" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        try
        {
            bean.doDaemonAction( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( _entry.isRunning( ) );
            _testDaemon.go( );
            _testDaemon.waitForCompletion( );
        }
    }

    public void testDoDaemonActionRun( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        long lReadyTime;
        long lScheduledTime;
        assertFalse( _entry.isRunning( ) );
        _entry.setInterval( 1000 );

        lReadyTime = System.nanoTime( );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run
                                                      // periodically with
                                                      // interval of 1000s
        _testDaemon.go( );
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        _testDaemon.waitForCompletion( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "RUN" ); // Manually do 1 run of the
                                                 // daemon now
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) );
        lReadyTime = System.nanoTime( );
        bean.doDaemonAction( request );

        _testDaemon.go( ); // It should run in less than 1000 seconds !
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        _testDaemon.waitForCompletion( );
    }

    public void testDoDaemonActionRunInvalidToken( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        long lReadyTime;
        long lScheduledTime;
        assertFalse( _entry.isRunning( ) );
        _entry.setInterval( 1000 );

        lReadyTime = System.nanoTime( );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run
                                                      // periodically with
                                                      // interval of 1000s
        _testDaemon.go( );
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        _testDaemon.waitForCompletion( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "RUN" ); // Manually do 1 run of the
                                                 // daemon now
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) + "b" );

        try
        {
            bean.doDaemonAction( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            try
            {
                // Here the daemon should not be run
                _testDaemon.go( 1, TimeUnit.SECONDS );
                fail( "Daemon should not have run" );
            }
            catch ( TimeoutException te )
            {
                // ok
            }
        }
    }

    public void testDoDaemonActionRunNoToken( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        long lReadyTime;
        long lScheduledTime;
        assertFalse( _entry.isRunning( ) );
        _entry.setInterval( 1000 );

        lReadyTime = System.nanoTime( );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run
                                                      // periodically with
                                                      // interval of 1000s
        _testDaemon.go( );
        lScheduledTime = System.nanoTime( );
        AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );

        _testDaemon.waitForCompletion( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "RUN" ); // Manually do 1 run of the
                                                 // daemon now
        request.setParameter( "daemon", JUNIT_DAEMON );

        try
        {
            bean.doDaemonAction( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            try
            {
                // Here the daemon should not be run
                _testDaemon.go( 1, TimeUnit.SECONDS );
                fail( "Daemon should not have run" );
            }
            catch ( TimeoutException te )
            {
                // ok
            }
        }
    }

    public void testDoDaemonActionUpdateInterval( ) throws AccessDeniedException
    {
        final long lTestInterval = 314159L;
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "UPDATE_INTERVAL" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( "interval", Long.toString( lTestInterval ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) );
        bean.doDaemonAction( request );
        assertEquals( lTestInterval, _entry.getInterval( ) );
    }

    public void testDoDaemonActionUpdateIntervalInvalidToken( ) throws AccessDeniedException
    {
        final long lTestInterval = 314159L;
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "UPDATE_INTERVAL" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( "interval", Long.toString( lTestInterval ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DAEMONS ) + "b" );
        try
        {
            bean.doDaemonAction( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( 1, _entry.getInterval( ) );
        }
    }

    public void testDoDaemonActionUpdateIntervalNoToken( ) throws AccessDeniedException
    {
        final long lTestInterval = 314159L;
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "UPDATE_INTERVAL" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        request.setParameter( "interval", Long.toString( lTestInterval ) );

        try
        {
            bean.doDaemonAction( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( 1, _entry.getInterval( ) );
        }
    }

    public void testDoDaemonActionUnknown( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "UNKNOWN" );
        bean.doDaemonAction( request ); // does not throw
    }

    public void testGetManageDaemons( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        bean.init( request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        assertNotNull( bean.getManageDaemons( request ) );
    }
}
