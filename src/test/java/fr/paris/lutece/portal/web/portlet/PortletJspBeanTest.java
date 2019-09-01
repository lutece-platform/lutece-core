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
package fr.paris.lutece.portal.web.portlet;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

public class PortletJspBeanTest extends LuteceTestCase
{
    public void testGetCreateTemplate( ) throws PasswordResetException, AccessDeniedException
    {
        PortletJspBean instance = new TestPortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), PortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        instance.init( request, PortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        RequestContextHolder.setRequestAttributes( new ServletRequestAttributes( request ) );

        Map<String, Object> model = new HashMap<>( );
        assertNotNull( instance.getCreateTemplate( "1", "ALIAS_PORTLET", model ) );
        assertTrue( model.containsKey( SecurityTokenService.MARK_TOKEN ) );
    }

    public void testGetModifyTemplateTemplate( ) throws PasswordResetException, AccessDeniedException
    {
        PortletJspBean instance = new TestPortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), PortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        instance.init( request, PortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        RequestContextHolder.setRequestAttributes( new ServletRequestAttributes( request ) );

        Map<String, Object> model = new HashMap<>( );
        model.put( "alias_portlet", "1" );
        Portlet portlet = new TestPortlet( "ALIAS_PORTLET" );
        assertNotNull( instance.getModifyTemplate( portlet, model ) );
        assertTrue( model.containsKey( SecurityTokenService.MARK_TOKEN ) );
    }

    public void testSetPortletCommonData( )
    {
        PortletJspBean instance = new TestPortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "1" );
        request.setParameter( Parameters.COLUMN, "1" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "1" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.STYLE, "1" );
        request.setParameter( "page_id", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/portlet/create_portlet.html" ) );
        Portlet portlet = new TestPortlet( "ALIAS_PORTLET" );
        instance.setPortletCommonData( request, portlet );

        assertEquals( strName, portlet.getName( ) );
        assertEquals( 1, portlet.getOrder( ) );
        assertEquals( 1, portlet.getColumn( ) );
        assertEquals( 1, portlet.getAcceptAlias( ) );
        assertEquals( 1, portlet.getDisplayPortletTitle( ) );
        assertEquals( 1, portlet.getStyleId( ) );
        assertEquals( 1, portlet.getPageId( ) );
    }

    public void testSetPortletCommonDataInvalidToken( )
    {
        PortletJspBean instance = new TestPortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "1" );
        request.setParameter( Parameters.COLUMN, "1" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "1" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.STYLE, "1" );
        request.setParameter( "page_id", "1" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/portlet/create_portlet.html" ) + "b" );
        Portlet portlet = new TestPortlet( "ALIAS_PORTLET" );
        try
        {
            instance.setPortletCommonData( request, portlet );
            fail( "Should have thrown" );
        }
        catch ( AppException e )
        {
            assertNotNull( e.getCause( ) );
            assertTrue( e.getCause( ) instanceof AccessDeniedException );
            assertEquals( "ALIAS_PORTLET", portlet.getName( ) );
            assertEquals( 0, portlet.getOrder( ) );
            assertEquals( 0, portlet.getColumn( ) );
            assertEquals( 0, portlet.getAcceptAlias( ) );
            assertEquals( 0, portlet.getDisplayPortletTitle( ) );
            assertEquals( 0, portlet.getStyleId( ) );
            assertEquals( 0, portlet.getPageId( ) );
        }
    }

    public void testSetPortletCommonDataNoToken( )
    {
        PortletJspBean instance = new TestPortletJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strName = getRandomName( );
        request.setParameter( Parameters.PORTLET_NAME, strName );
        request.setParameter( Parameters.ORDER, "1" );
        request.setParameter( Parameters.COLUMN, "1" );
        request.setParameter( Parameters.ACCEPT_ALIAS, "1" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "1" );
        request.setParameter( Parameters.STYLE, "1" );
        request.setParameter( "page_id", "1" );

        Portlet portlet = new TestPortlet( "ALIAS_PORTLET" );
        try
        {
            instance.setPortletCommonData( request, portlet );
            fail( "Should have thrown" );
        }
        catch ( AppException e )
        {
            assertNotNull( e.getCause( ) );
            assertTrue( e.getCause( ) instanceof AccessDeniedException );
            assertEquals( "ALIAS_PORTLET", portlet.getName( ) );
            assertEquals( 0, portlet.getOrder( ) );
            assertEquals( 0, portlet.getColumn( ) );
            assertEquals( 0, portlet.getAcceptAlias( ) );
            assertEquals( 0, portlet.getDisplayPortletTitle( ) );
            assertEquals( 0, portlet.getStyleId( ) );
            assertEquals( 0, portlet.getPageId( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
