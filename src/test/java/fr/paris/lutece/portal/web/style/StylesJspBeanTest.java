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

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new StylesJspBean( );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
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
        int id = StyleHome.getStylesList( ).stream( ).map( style -> style.getId( ) ).max( Integer::max ).get( ) + 1;
        request.addParameter( Parameters.STYLE_ID, Integer.toString( id ) );
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
            Style style = StyleHome.findByPrimaryKey( id );
            assertNotNull( style );
            assertEquals( id, style.getId( ) );
        }
        finally
        {
            StyleHome.remove( id );
        }
    }

    public void testDoCreateStyleInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int id = StyleHome.getStylesList( ).stream( ).map( style -> style.getId( ) ).max( Integer::max ).get( ) + 1;
        request.addParameter( Parameters.STYLE_ID, Integer.toString( id ) );
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
            Style style = StyleHome.findByPrimaryKey( id );
            assertNull( style );
        }
        finally
        {
            StyleHome.remove( id );
        }
    }

    public void testDoCreateStyleNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        int id = StyleHome.getStylesList( ).stream( ).map( style -> style.getId( ) ).max( Integer::max ).get( ) + 1;
        request.addParameter( Parameters.STYLE_ID, Integer.toString( id ) );
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
            Style style = StyleHome.findByPrimaryKey( id );
            assertNull( style );
        }
        finally
        {
            StyleHome.remove( id );
        }
    }

    /**
     * Test of getModifyStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetModifyStyle( ) throws AccessDeniedException
    {
        System.out.println( "getModifyStyle" );

        if ( StyleHome.getStylesList( ).size( ) > 0 )
        {
            int nStyleId = StyleHome.getStylesList( ).iterator( ).next( ).getId( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( Parameters.STYLE_ID, "" + nStyleId );
            Utils.registerAdminUserWithRigth( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

            instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
            instance.getModifyStyle( request );
        }
    }

    /**
     * Test of doModifyStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testDoModifyStyle( )
    {
        System.out.println( "doModifyStyle" );

        // Not implemented yet
    }

    /**
     * Test of getConfirmRemoveStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testGetConfirmRemoveStyle( ) throws AccessDeniedException
    {
        System.out.println( "getConfirmRemoveStyle" );

        if ( StyleHome.getStylesList( ).size( ) > 0 )
        {
            int nStyleId = StyleHome.getStylesList( ).iterator( ).next( ).getId( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( Parameters.STYLE_ID, "" + nStyleId );
            Utils.registerAdminUserWithRigth( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

            instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
            instance.getConfirmRemoveStyle( request );
        }
    }

    /**
     * Test of doRemoveStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    public void testDoRemoveStyle( )
    {
        System.out.println( "doRemoveStyle" );

        // Not implemented yet
    }
}
