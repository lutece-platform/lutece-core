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
import fr.paris.lutece.test.LuteceTestCase;

public class DaemonsJspBeanTest extends LuteceTestCase
{
    private static final String JUNIT_DAEMON = "JUNIT";
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
        //AppDaemonService.registerDaemon will copy this datastore value in the entry.
        DatastoreService.setInstanceDataValue( "core.daemon." + JUNIT_DAEMON + ".interval", "1");
        AppDaemonService.registerDaemon( _entry );
    }

    private String setInitialStartDelay( ) throws FileNotFoundException, IOException
    {
        File propertiesFile = new File( getResourcesDir( ) + "/WEB-INF/conf/daemons.properties" );
        Properties props = new Properties( );
        try ( FileInputStream is = new FileInputStream( propertiesFile ) )
        {
            props.load( is );
        }
        String orig = props.getProperty( "daemon.maxInitialStartDelay" );
        props.setProperty( "daemon.maxInitialStartDelay", "1" );
        try ( FileOutputStream out = new FileOutputStream( propertiesFile ) )
        {
            props.store( out, "junit" );
        }
        AppPropertiesService.reloadAll( );
        return orig;
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        AppDaemonService.stopDaemon( JUNIT_DAEMON );
        AppDaemonService.unregisterDaemon( JUNIT_DAEMON );
        restoreInitialStartDelay( origMaxInitialStartDelay );
        super.tearDown( );
    }

    private void restoreInitialStartDelay( String orig ) throws FileNotFoundException, IOException
    {
        File propertiesFile = new File( getResourcesDir( ) + "/WEB-INF/conf/daemons.properties" );
        Properties props = new Properties( );
        try ( FileInputStream is = new FileInputStream( propertiesFile ) )
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
        try ( FileOutputStream out = new FileOutputStream( propertiesFile ) )
        {
            props.store( out, "junit" );
        }
        AppPropertiesService.reloadAll( );
    }

    public void testDoDaemonActionStart( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        assertFalse( _entry.isRunning( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "START" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        bean.doDaemonAction( request ); // Daemon should run periodically with interval of 1s
        assertTrue( _entry.isRunning( ) );
        TestDaemon daemon = ( TestDaemon ) AppDaemonService.getDaemon( JUNIT_DAEMON );
        daemon.go( );
        daemon.waitForCompletion( ); // Complete first run without a timeout
        daemon.go( );
        daemon.waitForCompletion( ); // Complete second run without a timeout. More runs would follow if we continued
    }

    public void testDoDaemonActionStop( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        assertFalse( _entry.isRunning( ) );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run periodically with interval of 1s
        TestDaemon daemon = ( TestDaemon ) AppDaemonService.getDaemon( JUNIT_DAEMON );
        daemon.go( );
        daemon.waitForCompletion( );
        //We have about 1 second to stop the daemon
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "STOP" );
        request.setParameter( "daemon", JUNIT_DAEMON );
        bean.doDaemonAction( request );
        assertFalse( _entry.isRunning( ) );
        try
        {
            //Here the daemon should not be relaunched after a 1s interval. So wait 2.5 seconds until a timeout.
            daemon.go( 2500, TimeUnit.MILLISECONDS );
            fail( "Daemon still running after stop" );
        }
        catch ( TimeoutException e )
        {
            // ok
        }
    }

    public void testDoDaemonActionRun( ) throws InterruptedException, BrokenBarrierException, TimeoutException
    {
        assertFalse( _entry.isRunning( ) );
        _entry.setInterval( 1000 );
        AppDaemonService.startDaemon( JUNIT_DAEMON ); // Daemon should run periodically with interval of 1000s
        TestDaemon daemon = ( TestDaemon ) AppDaemonService.getDaemon( JUNIT_DAEMON );
        daemon.go( );
        daemon.waitForCompletion( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "action", "RUN" ); // Manually do 1 run of the daemon now
        request.setParameter( "daemon", JUNIT_DAEMON );
        bean.doDaemonAction( request );
        daemon.go( ); // It should run in less than 1000 seconds !
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
