/*
 * Copyright (c) 2002-2022, City of Paris
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

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.business.stylesheet.StyleSheetHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

/**
 * StylesJspBeanTest Test Class
 *
 */
public class StylesJspBeanTest extends LuteceTestCase
{
    @Inject
    private StylesJspBean instance;
    private Style style;
    @Inject
    private Models models;  
        
    
    @BeforeEach
    protected void setUp( ) throws Exception
    {
        style = new Style( );
        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        style.setId( nId );
        style.setDescription( getRandomName( ) );
        style.setPortalComponentId( 2 );
        StyleHome.create( style );
       
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        StyleHome.remove( style.getId( ) );
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
    @Test
    public void testGetStylesManagement( ) throws AccessDeniedException
    {
        System.out.println( "getStylesManagement" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );       
        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );        	

        assertTrue( StringUtils.isNotEmpty( instance.getStylesManagement( models, request ) ) );
    }

    /**
     * Test of getCreateStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    @Test
    public void testGetCreateStyle( ) throws AccessDeniedException
    {
        String html = instance.getCreateStyle( models );
        assertNotNull( html );
    }

    /**
     * Test of doCreateStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoCreateStyle( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );       
        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );        	

        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        request.addParameter( "id", Integer.toString( nId ) );
        String name = getRandomName( );
        request.addParameter( "description", name );
        String portalComponantId = "1";
        request.addParameter( "PortalComponentId", portalComponantId );
        request.addParameter( "action", "createStyle" );

        try
        {    	
            instance.processController(request, new MockHttpServletResponse( ));
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
    /**
     * Test of getModifyStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    @Test
    public void testGetModifyStyle( ) throws AccessDeniedException
    {
        int nStyleId = style.getId( );
        String html = instance.getModifyStyle(nStyleId , models );
        assertNotNull( html );
    }

    /**
     * Test of doModifyStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoModifyStyle( ) throws AccessDeniedException
    {
        int nStyleId = style.getId( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );       
        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );        	

        request.addParameter( "id", Integer.toString( nStyleId ) );
        request.addParameter( "portalComponentId", Integer.toString( style.getPortalComponentId( ) ) );
        request.addParameter( "description", style.getDescription( ) + "_mod" );       
        
        request.addParameter( "action", "modifyStyle" );

        instance.processController(request, new MockHttpServletResponse( ));
     
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNull( message );
        Style stored = StyleHome.findByPrimaryKey( style.getId( ) );
        assertNotNull( stored );
        assertEquals( style.getId( ), stored.getId( ) );
        assertEquals( style.getDescription( ) + "_mod", stored.getDescription( ) );
    }
    /**
     * Test of getConfirmRemoveStyle method, of class fr.paris.lutece.portal.web.style.StylesJspBean.
     */
    @Test
    public void testGetConfirmRemoveStyle( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLE_ID, Integer.toString( style.getId( ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );

        instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );
        
        instance.getConfirmRemoveStyle( style.getId( ), request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
    }
    @Test
    public void testGetConfirmRemoveStyleWithStyleSheet( ) throws AccessDeniedException
    {
        StyleSheet stylesheet = new StyleSheet( );
        String randomName = getRandomName( );
        stylesheet.setDescription( randomName );
        stylesheet.setModeId( 1 );
        stylesheet.setStyleId( style.getId( ) );
        stylesheet.setFile( "file" );
        stylesheet.setSource( "<a/>".getBytes( ) );
        StyleSheetHome.create( stylesheet );
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), StylesJspBean.RIGHT_MANAGE_STYLE );       
            instance.init( request, StylesJspBean.RIGHT_MANAGE_STYLE );        	
            request.addParameter( "id", Integer.toString( style.getId( ) ) );
            request.addParameter( "view", "getConfirmRemoveStyle" );
            instance.processController(request,  new MockHttpServletResponse( ));
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertTrue( message.getRequestParameters( ).containsKey( Parameters.STYLESHEET_ID ) );
            assertEquals( Integer.toString( stylesheet.getId( ) ), message.getRequestParameters( ).get( Parameters.STYLESHEET_ID ) );
        }
        finally
        {
            StyleSheetHome.remove( stylesheet.getId( ) );
        }
    }

    /**
     * Test of doRemoveStyle method, of fr.paris.lutece.portal.web.style.StylesJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoRemoveStyle( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id", Integer.toString( style.getId( ) ) );
        instance.doRemoveStyle( style.getId( ), request );
        assertNull( StyleHome.findByPrimaryKey( style.getId( ) ) );
    }
}
