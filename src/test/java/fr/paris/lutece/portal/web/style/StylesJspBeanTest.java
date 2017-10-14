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
package fr.paris.lutece.portal.web.style;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.business.stylesheet.StyleSheetHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * StylesJspBeanTest Test Class
 *
 */
public class StylesJspBeanTest extends LuteceTestCase
{
    private StylesJspBean instance;
    private Style style;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new StylesJspBean( );
        style = new Style( );
        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        style.setId( nId );
        style.setDescription( getRandomName() );
        style.setPortalComponentId( 2 );
        StyleHome.create( style );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        StyleHome.remove( style.getId( ) );
        super.tearDown( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    /**
     * Test of getStylesManagement method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetStylesManagement( ) throws AccessDeniedException
    {
        System.out.println( "getStylesManagement" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        instance.getStylesManagement( request );
    }

    /**
     * Test of getCreateStyle method, of class
     * fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetCreateStyle( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        String html = instance.getCreateStyle( request );
        assertNotNull( html );
    }

    /**
     * Test of doCreateStyle method, of
     * fr.paris.lutece.portal.web.style.StylesJspBean.
     * 
     * @throws AccessDeniedException
     */
    public void testDoCreateStyle( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nId ) );
        String name = getRandomName( );
        request.addParameter( Parameters.STYLE_NAME, name );
        String portalComponantId = "1";
        request.addParameter( Parameters.PORTAL_COMPONENT, portalComponantId );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/style/create_style.html" ) );
        try
        {
            instance.doCreateStyle( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            Style stored = StyleHome.findByPrimaryKey( nId );
            assertNotNull( stored );
            assertEquals( nId, stored.getId( ) );
        }
        finally
        {
            StyleHome.remove( nId );
        }
    }

    public void testDoCreateStyleInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nId ) );
        String name = getRandomName( );
        request.addParameter( Parameters.STYLE_NAME, name );
        String portalComponantId = "1";
        request.addParameter( Parameters.PORTAL_COMPONENT, portalComponantId );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/style/create_style.html" ) + "b" );
        try
        {
            instance.doCreateStyle( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Style stored = StyleHome.findByPrimaryKey( nId );
            assertNull( stored );
        }
        finally
        {
            StyleHome.remove( nId );
        }
    }

    public void testDoCreateStyleNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nId ) );
        String name = getRandomName( );
        request.addParameter( Parameters.STYLE_NAME, name );
        String portalComponantId = "1";
        request.addParameter( Parameters.PORTAL_COMPONENT, portalComponantId );
        try
        {
            instance.doCreateStyle( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Style stored = StyleHome.findByPrimaryKey( nId );
            assertNull( stored );
        }
        finally
        {
            StyleHome.remove( nId );
        }
    }

    /**
     * Test of getModifyStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetModifyStyle( ) throws AccessDeniedException
    {
        int nStyleId = style.getId( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nStyleId ) );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        String html = instance.getModifyStyle( request );
        assertNotNull( html );
    }

    /**
     * Test of doModifyStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoModifyStyle( ) throws AccessDeniedException
    {
        int nStyleId = style.getId( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nStyleId ) );
        request.addParameter( Parameters.PORTAL_COMPONENT, Integer.toString( style.getPortalComponentId( ) ) );
        request.addParameter( Parameters.STYLE_NAME, style.getDescription( ) + "_mod" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/style/modify_style.html" ) );
        instance.doModifyStyle( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNull( message );
        Style stored = StyleHome.findByPrimaryKey( style.getId( ) );
        assertNotNull( stored );
        assertEquals( style.getId( ), stored.getId( ) );
        assertEquals( style.getDescription( ) + "_mod", stored.getDescription( ) );
    }

    public void testDoModifyStyleInvalidToken( ) throws AccessDeniedException
    {
        int nStyleId = style.getId( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nStyleId ) );
        request.addParameter( Parameters.PORTAL_COMPONENT, Integer.toString( style.getPortalComponentId( ) ) );
        request.addParameter( Parameters.STYLE_NAME, style.getDescription( ) + "_mod" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/style/modify_style.html" )  + "b" );
        try
        {
            instance.doModifyStyle( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Style stored = StyleHome.findByPrimaryKey( style.getId( ) );
            assertNotNull( stored );
            assertEquals( style.getId( ), stored.getId( ) );
            assertEquals( style.getDescription( ), stored.getDescription( ) );
        }
    }

    public void testDoModifyStyleNoToken( ) throws AccessDeniedException
    {
        int nStyleId = style.getId( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( nStyleId ) );
        request.addParameter( Parameters.PORTAL_COMPONENT, Integer.toString( style.getPortalComponentId( ) ) );
        request.addParameter( Parameters.STYLE_NAME, style.getDescription( ) + "_mod" );
        try
        {
            instance.doModifyStyle( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Style stored = StyleHome.findByPrimaryKey( style.getId( ) );
            assertNotNull( stored );
            assertEquals( style.getId( ), stored.getId( ) );
            assertEquals( style.getDescription( ), stored.getDescription( ) );
        }
    }

    /**
     * Test of getConfirmRemoveStyle method, of class
     * fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetConfirmRemoveStyle( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( style.getId( ) ) );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        instance.getConfirmRemoveStyle( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    public void testGetConfirmRemoveStyleWithStyleSheet( ) throws AccessDeniedException
    {
        StyleSheet stylesheet = new StyleSheet( );
        String randomName = getRandomName();
        stylesheet.setDescription( randomName );
        stylesheet.setModeId( 1 );
        stylesheet.setStyleId( style.getId( ) );
        stylesheet.setFile( "file" );
        stylesheet.setSource( "<a/>".getBytes( ) );
        StyleSheetHome.create( stylesheet );
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( Parameters.STYLE_ID, Integer.toString( style.getId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/style/DoRemoveStyle.jsp" ) );
            instance.getConfirmRemoveStyle( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
            assertTrue( message.getRequestParameters( ).containsKey( Parameters.STYLESHEET_ID ) );
            assertEquals( Integer.toString( stylesheet.getId( ) ), message.getRequestParameters( ).get( Parameters.STYLESHEET_ID ) );
        }
        finally
        {
            StyleSheetHome.remove( stylesheet.getId( ) );
        }
    }

    /**
     * Test of doRemoveStyle method, of
     * fr.paris.lutece.portal.web.style.StylesJspBean.
     * 
     * @throws AccessDeniedException
     */
    public void testDoRemoveStyle( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( style.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/style/DoRemoveStyle.jsp" ) );
        instance.doRemoveStyle( request );
        assertNull( StyleHome.findByPrimaryKey( style.getId( ) ) );
    }

    public void testDoRemoveStyleInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( style.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/style/DoRemoveStyle.jsp" ) + "b" );
        try
        {
            instance.doRemoveStyle( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNotNull( StyleHome.findByPrimaryKey( style.getId( ) ) );
        }
    }

    public void testDoRemoveStyleNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( style.getId( ) ) );
        try
        {
            instance.doRemoveStyle( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNotNull( StyleHome.findByPrimaryKey( style.getId( ) ) );
        }
    }
}
