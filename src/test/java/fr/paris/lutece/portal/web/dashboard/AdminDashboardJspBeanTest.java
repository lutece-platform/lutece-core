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
package fr.paris.lutece.portal.web.dashboard;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.dashboard.AdminDashboardFactory;
import fr.paris.lutece.portal.business.dashboard.AdminDashboardHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.dashboard.admin.IAdminDashboardComponent;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * AdminDashboardJspBean Test Class
 *
 */
public class AdminDashboardJspBeanTest extends LuteceTestCase
{

    private AdminDashboardJspBean instance;
    private IAdminDashboardComponent _dashboard;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new AdminDashboardJspBean( );
        _dashboard = new TestAdminDashboardCompoent( );
        _dashboard.setName( getRandomName( ) );
        AdminDashboardFactory.registerDashboardComponent( _dashboard );
        AdminDashboardHome.create( _dashboard );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        AdminDashboardHome.remove( _dashboard.getName( ) );
        // TODO : dashboard should be unregistered
        super.tearDown( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    /**
     * Test of getAdminDashboards method, of class
     * fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean.
     * 
     * @throws Exception
     *             e
     */
    public void testGetAdminDashboards( ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );

        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );

        instance = new AdminDashboardJspBean( );

        // should not throw on freemarker templating
        String adminDashboards = instance.getAdminDashboards( request );
        assertNotNull( adminDashboards );
        assertFalse( "".equals( adminDashboards ) );
    }

    public void testGetManageDashboards( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ),
                AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD );

        instance.init( request, AdminDashboardJspBean.RIGHT_MANAGE_ADMINDASHBOARD );

        assertNotNull( instance.getManageDashboards( request ) );
    }

    public void testDoMoveAdminDashboard( ) throws AccessDeniedException
    {
        IAdminDashboardComponent stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( 0, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "dashboard_name", _dashboard.getName( ) );
        request.setParameter( "dashboard_order", "-1" );
        request.setParameter( "dashboard_column", "-1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( )
                .getToken( request, "/admin/dashboard/admin/manage_dashboards.html" ) );

        instance.doMoveAdminDashboard( request );

        stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 1, stored.getOrder( ) );
        assertEquals( -1, stored.getZone( ) );
    }

    public void testDoMoveAdminDashboardInvalidToken( ) throws AccessDeniedException
    {
        IAdminDashboardComponent stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( 0, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "dashboard_name", _dashboard.getName( ) );
        request.setParameter( "dashboard_order", "-1" );
        request.setParameter( "dashboard_column", "-1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "/admin/dashboard/admin/manage_dashboards.html" )
                        + "b" );

        try
        {
            instance.doMoveAdminDashboard( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
            assertNotNull( stored );
            assertEquals( 0, stored.getOrder( ) );
            assertEquals( 0, stored.getZone( ) );
        }
    }

    public void testDoMoveAdminDashboardNoToken( ) throws AccessDeniedException
    {
        IAdminDashboardComponent stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( 0, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "dashboard_name", _dashboard.getName( ) );
        request.setParameter( "dashboard_order", "-1" );
        request.setParameter( "dashboard_column", "-1" );

        try
        {
            instance.doMoveAdminDashboard( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
            assertNotNull( stored );
            assertEquals( 0, stored.getOrder( ) );
            assertEquals( 0, stored.getZone( ) );
        }
    }

    public void testDoReorderColumn( ) throws AccessDeniedException
    {
        IAdminDashboardComponent stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( 0, stored.getZone( ) );
        int nZone = AdminDashboardHome.findColumns( ).stream( ).max( Integer::compare ).orElse( 1 );
        stored.setZone( nZone );
        stored.setOrder( -1 );
        AdminDashboardHome.update( stored );
        stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertEquals( -1, stored.getOrder( ) );
        assertEquals( nZone, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "column", Integer.toString( nZone ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( )
                .getToken( request, "/admin/dashboard/admin/manage_dashboards.html" ) );

        instance.doReorderColumn( request );

        stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertEquals( 1, stored.getOrder( ) );
        assertEquals( nZone, stored.getZone( ) );
    }

    public void testDoReorderColumnInvalidToken( ) throws AccessDeniedException
    {
        IAdminDashboardComponent stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( 0, stored.getZone( ) );
        int nZone = AdminDashboardHome.findColumns( ).stream( ).max( Integer::compare ).orElse( 0 ) + 1;
        stored.setZone( nZone );
        stored.setOrder( -1 );
        AdminDashboardHome.update( stored );
        stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertEquals( -1, stored.getOrder( ) );
        assertEquals( nZone, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "column", Integer.toString( nZone ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "/admin/dashboard/admin/manage_dashboards.html" )
                        + "b" );

        try
        {
            instance.doReorderColumn( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
            assertEquals( -1, stored.getOrder( ) );
            assertEquals( nZone, stored.getZone( ) );
        }
    }

    public void testDoReorderColumnNoToken( ) throws AccessDeniedException
    {
        IAdminDashboardComponent stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( 0, stored.getZone( ) );
        int nZone = AdminDashboardHome.findColumns( ).stream( ).max( Integer::compare ).orElse( 0 ) + 1;
        stored.setZone( nZone );
        stored.setOrder( -1 );
        AdminDashboardHome.update( stored );
        stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertEquals( -1, stored.getOrder( ) );
        assertEquals( nZone, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "column", Integer.toString( nZone ) );

        try
        {
            instance.doReorderColumn( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            stored = AdminDashboardHome.findByPrimaryKey( _dashboard.getName( ) );
            assertEquals( -1, stored.getOrder( ) );
            assertEquals( nZone, stored.getZone( ) );
        }
    }
}
