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
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.dashboard.DashboardFactory;
import fr.paris.lutece.portal.business.dashboard.DashboardHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

public class DashboardJspBeanTest extends LuteceTestCase
{
    private static final class TestDashboardComponent extends DashboardComponent
    {

        @Override
        public String getDashboardData( AdminUser user, HttpServletRequest request )
        {
            return "<p>JUNIT</p>";
        }

    }

    private DashboardJspBean _instance;
    private IDashboardComponent _dashboard;
    private int _nZone;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _instance = new DashboardJspBean( );
        _dashboard = new TestDashboardComponent( );
        _dashboard.setName( getRandomName( ) );
        _dashboard.setRight( "ALL" );
        _nZone = DashboardService.getInstance( ).getColumnCount( );
        _dashboard.setZone( _nZone );
        DashboardFactory.registerDashboardComponent( _dashboard );
        DashboardHome.create( _dashboard );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        DashboardHome.remove( _dashboard.getName( ) );
        // TODO : dashboard should be unregistered
        super.tearDown( );
    }

    public void testGetManageDashboards( ) throws PasswordResetException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        // set all rights to have all dashboards
        user.setRights( RightHome.getRightsList( ).stream( )
                .collect( Collectors.toMap( Right::getId, Function.identity( ) ) ) );
        Utils.registerAdminUser( request, user );
        _instance.init( request, DashboardJspBean.RIGHT_MANAGE_DASHBOARD );

        String html = _instance.getManageDashboards( request );
        assertNotNull( html );
        assertTrue( html.contains( _dashboard.getName( ) ) );
    }

    public void testdoMoveDashboard( ) throws AccessDeniedException
    {
        IDashboardComponent stored = DashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( _nZone, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "dashboard_name", _dashboard.getName( ) );
        request.setParameter( "dashboard_order", "-1" );
        request.setParameter( "dashboard_column", "-1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "/admin/dashboard/manage_dashboards.html" ) );

        _instance.doMoveDashboard( request );

        stored = DashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 1, stored.getOrder( ) );
        assertEquals( -1, stored.getZone( ) );
    }

    public void testdoMoveDashboardInvalidToken( ) throws AccessDeniedException
    {
        IDashboardComponent stored = DashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( _nZone, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "dashboard_name", _dashboard.getName( ) );
        request.setParameter( "dashboard_order", "-1" );
        request.setParameter( "dashboard_column", "-1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "/admin/dashboard/manage_dashboards.html" )
                        + "b" );

        try
        {
            _instance.doMoveDashboard( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            stored = DashboardHome.findByPrimaryKey( _dashboard.getName( ) );
            assertNotNull( stored );
            assertEquals( 0, stored.getOrder( ) );
            assertEquals( _nZone, stored.getZone( ) );
        }
    }

    public void testdoMoveDashboardNoToken( ) throws AccessDeniedException
    {
        IDashboardComponent stored = DashboardHome.findByPrimaryKey( _dashboard.getName( ) );
        assertNotNull( stored );
        assertEquals( 0, stored.getOrder( ) );
        assertEquals( _nZone, stored.getZone( ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "dashboard_name", _dashboard.getName( ) );
        request.setParameter( "dashboard_order", "-1" );
        request.setParameter( "dashboard_column", "-1" );

        try
        {
            _instance.doMoveDashboard( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            stored = DashboardHome.findByPrimaryKey( _dashboard.getName( ) );
            assertNotNull( stored );
            assertEquals( 0, stored.getOrder( ) );
            assertEquals( _nZone, stored.getZone( ) );
        }
    }
}
