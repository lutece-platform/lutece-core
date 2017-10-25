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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * PluginJspBean Test Class
 *
 */
public class PluginJspBeanTest extends LuteceTestCase
{
    private static final String PLUGIN_NAME = "junitplugin";
    private static final String PARAM_PLUGIN_TYPE = "plugin_type";
    private static final String PARAM_PLUGIN_TYPE_ALL = "all";
    private static final String PARAM_DB_POOL_NAME = "db_pool_name";
    private static final String PATH_PLUGIN = "path.plugins";
    private PluginJspBean instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new PluginJspBean( );
        try ( InputStream in = this.getClass( ).getResourceAsStream( "junit_plugin.xml" ) )
        {
            try ( OutputStream out = new FileOutputStream(
                    new File( AppPathService.getPath( PATH_PLUGIN ), "junit_plugin.xml" ) ) )
            {
                IOUtils.copy( in, out );
            }
        }
        PluginService.init( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        PluginService.getPlugin( PLUGIN_NAME ).uninstall( );
        new File( AppPathService.getPath( PATH_PLUGIN ), "junit_plugin.xml" ).delete( );
        PluginService.init( );
        super.tearDown( );
    }

    /**
     * Test of getManagePlugins method, of class
     * fr.paris.lutece.portal.web.system.PluginJspBean.
     */
    public void testGetManagePlugins( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), PluginJspBean.RIGHT_MANAGE_PLUGINS );
        request.addParameter( PARAM_PLUGIN_TYPE, PARAM_PLUGIN_TYPE_ALL );

        instance.init( request, PluginJspBean.RIGHT_MANAGE_PLUGINS );
        assertNotNull( instance.getManagePlugins( request ) );
    }

    public void testDoInstallPlugin( ) throws AccessDeniedException
    {
        assertFalse( PluginService.isPluginEnable( PLUGIN_NAME ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_plugins.html" ) );
        instance.doInstallPlugin( request, request.getServletContext( ) );
        assertTrue( PluginService.isPluginEnable( PLUGIN_NAME ) );
    }

    public void testDoInstallPluginInvalidToken( ) throws AccessDeniedException
    {
        assertFalse( PluginService.isPluginEnable( PLUGIN_NAME ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_plugins.html" ) + "b" );
        try
        {
            instance.doInstallPlugin( request, request.getServletContext( ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( PluginService.isPluginEnable( PLUGIN_NAME ) );
        }
    }

    public void testDoInstallPluginNoToken( ) throws AccessDeniedException
    {
        assertFalse( PluginService.isPluginEnable( PLUGIN_NAME ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        try
        {
            instance.doInstallPlugin( request, request.getServletContext( ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( PluginService.isPluginEnable( PLUGIN_NAME ) );
        }
    }

    public void testDoModifyPluginPool( ) throws AccessDeniedException
    {
        assertNull( PluginService.getPlugin( PLUGIN_NAME ).getDbPoolName( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( PARAM_DB_POOL_NAME, "junit" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_plugins.html" ) );
        instance.doModifyPluginPool( request );
        assertEquals( "junit", PluginService.getPlugin( PLUGIN_NAME ).getDbPoolName( ) );
    }

    public void testDoModifyPluginPoolInvalidToken( ) throws AccessDeniedException
    {
        assertNull( PluginService.getPlugin( PLUGIN_NAME ).getDbPoolName( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( PARAM_DB_POOL_NAME, "junit" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_plugins.html" ) + "b" );
        try
        {
            instance.doModifyPluginPool( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNull( PluginService.getPlugin( PLUGIN_NAME ).getDbPoolName( ) );
        }
    }

    public void testDoModifyPluginPoolNoToken( ) throws AccessDeniedException
    {
        assertNull( PluginService.getPlugin( PLUGIN_NAME ).getDbPoolName( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( PARAM_DB_POOL_NAME, "junit" );

        try
        {
            instance.doModifyPluginPool( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNull( PluginService.getPlugin( PLUGIN_NAME ).getDbPoolName( ) );
        }
    }

    public void testGetConfirmUninstallPlugin( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        instance.getConfirmUninstallPlugin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    public void testDoUninstallPlugin( ) throws AccessDeniedException
    {
        PluginService.getPlugin( PLUGIN_NAME ).install( );
        assertTrue( PluginService.isPluginEnable( PLUGIN_NAME ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/system/DoUninstallPlugin.jsp" ) );
        instance.doUninstallPlugin( request, request.getServletContext( ) );
        assertFalse( PluginService.isPluginEnable( PLUGIN_NAME ) );
    }

    public void testDoUninstallPluginInvalidToken( ) throws AccessDeniedException
    {
        PluginService.getPlugin( PLUGIN_NAME ).install( );
        assertTrue( PluginService.isPluginEnable( PLUGIN_NAME ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/system/DoUninstallPlugin.jsp" )
                        + "b" );
        try
        {
            instance.doUninstallPlugin( request, request.getServletContext( ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( PluginService.isPluginEnable( PLUGIN_NAME ) );
        }
    }

    public void testDoUninstallPluginNoToken( ) throws AccessDeniedException
    {
        PluginService.getPlugin( PLUGIN_NAME ).install( );
        assertTrue( PluginService.isPluginEnable( PLUGIN_NAME ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "plugin_name", PLUGIN_NAME );

        try
        {
            instance.doUninstallPlugin( request, request.getServletContext( ) );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( PluginService.isPluginEnable( PLUGIN_NAME ) );
        }
    }
}
