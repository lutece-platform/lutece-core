/*
 * Copyright (c) 2002-2018, Mairie de Paris
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

import fr.paris.lutece.portal.business.daemon.DaemonTrigger;
import fr.paris.lutece.portal.business.daemon.DaemonTriggerHome;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeoutException;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.daemon.TestDaemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.scheduler.JobSchedulerService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
import static junit.framework.TestCase.assertTrue;
import org.quartz.CronTrigger;
import org.springframework.mock.web.MockHttpServletResponse;

public class TriggersJspBeanTest extends LuteceTestCase
{
    private static final String JUNIT_DAEMON = "JUNIT";
    private static final String JUNIT_DAEMON_2 = "JUNIT2";
    private static final String DAEMON_INTERVAL_DSKEY = "core.daemon." + JUNIT_DAEMON + ".interval";
    private TriggersJspBean bean;
    private DaemonEntry _entry;
    private TestDaemon _testDaemon;
    private DaemonTrigger _oldDaemonTrigger;

    // Views
    private static final String VIEW_MANAGE_TRIGGERS = "manageTriggers";
    private static final String VIEW_CREATE_TRIGGER = "createTrigger";
    private static final String VIEW_MODIFY_TRIGGER = "modifyTrigger";

    // Actions
    private static final String ACTION_CREATE_TRIGGER = "createTrigger";
    private static final String ACTION_MODIFY_TRIGGER = "modifyTrigger";
    private static final String ACTION_REMOVE_TRIGGER = "removeTrigger";
    private static final String ACTION_CONFIRM_REMOVE_TRIGGER = "confirmRemoveTrigger";

    // Templates
    private static final String TEMPLATE_CREATE_TRIGGER = "admin/trigger/create_trigger.html";
    private static final String TEMPLATE_MODIFY_TRIGGER = "admin/trigger/modify_trigger.html";

    // JSP
    private static final String JSP_MANAGE_TRIGGERS = "jsp/admin/system/ManageTriggers.jsp";

    // Parameters
    private static final String PARAMETER_DAEMON_TRIGGER_ID = "id";
    private static final String PARAMETER_TRIGGER_KEY = "key";
    private static final String PARAMETER_TRIGGER_GROUP = "group";
    private static final String PARAMETER_CRON_EXPRESSION = "cron_expression";
    private static final String PARAMETER_DAEMON_KEY = "daemon_key";

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        bean = new TriggersJspBean( );

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

        _oldDaemonTrigger = new DaemonTrigger( );
        _oldDaemonTrigger.setKey( "test2" );
        _oldDaemonTrigger.setGroup( "test2" );
        _oldDaemonTrigger.setCronExpression( "0 0 0 * * ?" );
        _oldDaemonTrigger.setDaemonKey( JUNIT_DAEMON_2 );
        JobSchedulerService.getInstance( ).createTrigger( _oldDaemonTrigger );
        DaemonTriggerHome.create( _oldDaemonTrigger );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        DatastoreService.removeInstanceData( DAEMON_INTERVAL_DSKEY );
        AppDaemonService.stopDaemon( JUNIT_DAEMON );
        AppDaemonService.unregisterDaemon( JUNIT_DAEMON );

        _oldDaemonTrigger = DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) );
        if ( _oldDaemonTrigger != null )
        {
            DaemonTriggerHome.remove( _oldDaemonTrigger.getId( ) );
            JobSchedulerService.getInstance().removeTrigger( _oldDaemonTrigger );
        }

        super.tearDown( );
    }

    public void testGetManageTriggers( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "view", VIEW_MANAGE_TRIGGERS );
        bean.init( request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        assertNotNull( bean.processController( request, response ) );
    }

    public void testGetCreateTrigger( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "view", VIEW_CREATE_TRIGGER );
        bean.init( request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        assertNotNull( bean.processController( request, response ) );
    }

    public void testGetModifyTrigger( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "view", VIEW_MODIFY_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        bean.init( request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        assertNotNull( bean.processController( request, response ) );
    }

    public void testGetConfirmRemoveTrigger( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_CONFIRM_REMOVE_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        bean.init( request, DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        assertNull( bean.processController( request, response ) );
    }

    public void testDoCreateTrigger( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        long lReadyTime;
        long lScheduledTime;

        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_CREATE_TRIGGER );
        request.setParameter( PARAMETER_TRIGGER_KEY, "test" );
        request.setParameter( PARAMETER_TRIGGER_GROUP, "test" );
        request.setParameter( PARAMETER_CRON_EXPRESSION, "0/1 * * * * ?" );
        request.setParameter( PARAMETER_DAEMON_KEY, JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_TRIGGER ) );

        try
        {
            lReadyTime = System.nanoTime( );
            assertNull( bean.processController( request, response ) );

            _testDaemon.go( );
            lScheduledTime = System.nanoTime( );
            AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );
            _testDaemon.waitForCompletion( ); // Complete first run without a timeout

            lReadyTime = System.nanoTime( );
            _testDaemon.go( );
            lScheduledTime = System.nanoTime( );
            AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );
            _testDaemon.waitForCompletion( ); // Complete second run without a timeout

            lReadyTime = System.nanoTime( );
            _testDaemon.go( );
            lScheduledTime = System.nanoTime( );
            long lRescheduleDurationMilliseconds = ( ( lScheduledTime - lReadyTime ) / 1000000 );
            AppLogService.info( "Daemon scheduled after " + ( ( lScheduledTime - lReadyTime ) / 1000000 ) + " ms" );
            _testDaemon.waitForCompletion( ); // Complete third run without a timeout. More runs would follow if we continued

            long lMarginMilliseconds = 250;
            assertTrue(
                    "Daemon should be re-scheduled approximately 1 second after the end of the previous run, but got " + lRescheduleDurationMilliseconds + "ms",
                    1000 - lMarginMilliseconds <= lRescheduleDurationMilliseconds && lRescheduleDurationMilliseconds <= 1000 + lMarginMilliseconds );

            DaemonTrigger newDaemonTrigger = getDaemonTrigger( "test", "test" );
            assertNotNull( newDaemonTrigger );
            assertEquals( "0/1 * * * * ?", newDaemonTrigger.getCronExpression( ) );
            assertEquals( JUNIT_DAEMON, newDaemonTrigger.getDaemonKey( ) );

            CronTrigger trigger = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( "test", "test");
            assertNotNull( trigger );
            assertEquals( "0/1 * * * * ?", trigger.getCronExpression( ) );
            assertEquals( JUNIT_DAEMON, trigger.getJobDataMap( ).get( "DaemonKey" ) );
        }
        finally
        {
            DaemonTrigger newDaemonTrigger = getDaemonTrigger( "test", "test" );
            DaemonTriggerHome.remove( newDaemonTrigger.getId( ) );
            JobSchedulerService.getInstance().removeTrigger( newDaemonTrigger );
        }
    }

    public void testDoCreateTriggerInvalidToken( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_CREATE_TRIGGER );
        request.setParameter( PARAMETER_TRIGGER_KEY, "test" );
        request.setParameter( PARAMETER_TRIGGER_GROUP, "test" );
        request.setParameter( PARAMETER_CRON_EXPRESSION, "0/1 * * * * ?" );
        request.setParameter( PARAMETER_DAEMON_KEY, JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_TRIGGER + 'b' ) );

        try
        {
            bean.processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            DaemonTrigger newDaemonTrigger = getDaemonTrigger( "test", "test" );
            assertNull( newDaemonTrigger );

            CronTrigger trigger = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( "test", "test");
            assertNull( trigger );
        }
    }

    public void testDoCreateTriggerNoToken( )
            throws InterruptedException, BrokenBarrierException, TimeoutException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_CREATE_TRIGGER );
        request.setParameter( PARAMETER_TRIGGER_KEY, "test" );
        request.setParameter( PARAMETER_TRIGGER_GROUP, "test" );
        request.setParameter( PARAMETER_CRON_EXPRESSION, "0/1 * * * * ?" );
        request.setParameter( PARAMETER_DAEMON_KEY, JUNIT_DAEMON );

        try
        {
            bean.processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            DaemonTrigger newDaemonTrigger = getDaemonTrigger( "test", "test" );
            assertNull( newDaemonTrigger );

            CronTrigger trigger = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( "test", "test");
            assertNull( trigger );
        }
    }

    public void testDoModifyTrigger( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_MODIFY_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        request.setParameter( PARAMETER_TRIGGER_KEY, "test" );
        request.setParameter( PARAMETER_TRIGGER_GROUP, "test" );
        request.setParameter( PARAMETER_CRON_EXPRESSION, "0/1 * * * * ?" );
        request.setParameter( PARAMETER_DAEMON_KEY, JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_TRIGGER ) );

        try
        {
            assertNull( bean.processController( request, response ) );
        } catch (Throwable t)
        {
            AppLogService.error( "Error with test ", t );
        }

        DaemonTrigger newDaemonTrigger = DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) );
        assertNotNull( newDaemonTrigger );
        assertEquals( "test", newDaemonTrigger.getKey( ) );
        assertEquals( "test", newDaemonTrigger.getGroup( ) );
        assertEquals( "0/1 * * * * ?", newDaemonTrigger.getCronExpression( ) );
        assertEquals( JUNIT_DAEMON, newDaemonTrigger.getDaemonKey( ) );

        CronTrigger trigger = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( "test", "test");
        assertNotNull( trigger );
        assertEquals( "0/1 * * * * ?", trigger.getCronExpression( ) );
        assertEquals( JUNIT_DAEMON, trigger.getJobDataMap( ).get( "DaemonKey" ) );
    }

    public void testDoModifyTriggerInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_MODIFY_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        request.setParameter( PARAMETER_TRIGGER_KEY, "test" );
        request.setParameter( PARAMETER_TRIGGER_GROUP, "test" );
        request.setParameter( PARAMETER_CRON_EXPRESSION, "0/1 * * * * ?" );
        request.setParameter( PARAMETER_DAEMON_KEY, JUNIT_DAEMON );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_TRIGGER + 'b' ) );

        try
        {
            bean.processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            DaemonTrigger newDaemonTrigger = DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) );
            assertNotNull( newDaemonTrigger );
            assertEquals( "test2", newDaemonTrigger.getKey( ) );
            assertEquals( "test2", newDaemonTrigger.getGroup( ) );
            assertEquals( "0 0 0 * * ?", newDaemonTrigger.getCronExpression( ) );
            assertEquals( JUNIT_DAEMON_2, newDaemonTrigger.getDaemonKey( ) );

            CronTrigger trigger = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( "test2", "test2");
            assertNotNull( trigger );
            assertEquals( "0 0 0 * * ?", trigger.getCronExpression( ) );
            assertEquals( JUNIT_DAEMON_2, trigger.getJobDataMap( ).get( "DaemonKey" ) );
        }
    }

    public void testDoModifyTriggerNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_MODIFY_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        request.setParameter( PARAMETER_TRIGGER_KEY, "test" );
        request.setParameter( PARAMETER_TRIGGER_GROUP, "test" );
        request.setParameter( PARAMETER_CRON_EXPRESSION, "0/1 * * * * ?" );
        request.setParameter( PARAMETER_DAEMON_KEY, JUNIT_DAEMON );

        try
        {
            bean.processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            DaemonTrigger newDaemonTrigger = DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) );
            assertNotNull( newDaemonTrigger );
            assertEquals( "test2", newDaemonTrigger.getKey( ) );
            assertEquals( "test2", newDaemonTrigger.getGroup( ) );
            assertEquals( "0 0 0 * * ?", newDaemonTrigger.getCronExpression( ) );
            assertEquals( JUNIT_DAEMON_2, newDaemonTrigger.getDaemonKey( ) );

            CronTrigger trigger = (CronTrigger) JobSchedulerService.getInstance( ).findTriggerByKey( "test2", "test2");
            assertNotNull( trigger );
            assertEquals( "0 0 0 * * ?", trigger.getCronExpression( ) );
            assertEquals( JUNIT_DAEMON_2, trigger.getJobDataMap( ).get( "DaemonKey" ) );
        }
    }

    public void testDoRemoveTrigger( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_REMOVE_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_MANAGE_TRIGGERS ) );
        assertNull( bean.processController( request, response ) );
        assertNull( DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) ) );
    }

    public void testDoRemoveTriggerInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_REMOVE_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_MANAGE_TRIGGERS + 'b' ) );

        try
        {
            bean.processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) ) );
        }
    }

    public void testDoRemoveTriggerNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), DaemonsJspBean.RIGHT_DAEMONS_MANAGEMENT );
        request.setParameter( "action", ACTION_REMOVE_TRIGGER );
        request.setParameter( PARAMETER_DAEMON_TRIGGER_ID, String.valueOf( _oldDaemonTrigger.getId( ) ) );

        try
        {
            bean.processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNotNull( DaemonTriggerHome.findByPrimaryKey( _oldDaemonTrigger.getId( ) ) );
        }
    }

    private DaemonTrigger getDaemonTrigger( String strTriggerKey, String strTriggerGroup )
    {
        DaemonTrigger daemonTrigger = null;
        for (DaemonTrigger dt : DaemonTriggerHome.getDaemonTriggersList( ) )
        {
            if ( dt.getKey( ).equals( strTriggerKey ) && dt.getGroup( ).equals( strTriggerGroup ) )
            {
                daemonTrigger = dt;
            }
        }

        return daemonTrigger;
    }
}
