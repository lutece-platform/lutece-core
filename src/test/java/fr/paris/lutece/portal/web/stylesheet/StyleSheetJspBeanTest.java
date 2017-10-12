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
package fr.paris.lutece.portal.web.stylesheet;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
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
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * StyleSheetJspBean Test Class
 *
 */
public class StyleSheetJspBeanTest extends LuteceTestCase
{

    private StyleSheetJspBean instance;
    private Style style;
    private StyleSheet stylesheet;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        instance = new StyleSheetJspBean( );
        style = new Style( );
        int nId = StyleHome.getStylesList( ).stream( ).map( Style::getId ).max( Integer::compare ).get( ) + 1;
        style.setId( nId );
        style.setDescription( getRandomName( ) );
        style.setPortalComponentId( 2 );
        StyleHome.create( style );
        stylesheet = new StyleSheet( );
        stylesheet.setDescription( getRandomName() );
        stylesheet.setModeId( 1 );
        stylesheet.setStyleId( style.getId( ) );
        stylesheet.setFile( "file" );
        stylesheet.setSource( "<a/>".getBytes( ) );
        StyleSheetHome.create( stylesheet );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        StyleSheetHome.remove( stylesheet.getId( ) );
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
     * Test of getManageStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetStyleSheetManagement( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

        instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
        instance.getManageStyleSheet( request );
    }

    /**
     * Test of getCreateStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetCreateStyleSheet( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.MODE_ID, "0" );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

        instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
        String html = instance.getCreateStyleSheet( request );
        assertNotNull( html );
    }

    /**
     * Test of doCreateStyleSheet method, of class
     * fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     * 
     * @throws IOException
     * @throws AccessDeniedException
     */
    public void testDoCreateStyleSheet( ) throws IOException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        final String randomName = getRandomName( );
        parameters.put( Parameters.STYLESHEET_NAME, new String[ ] { randomName } );
        parameters.put( Parameters.STYLES, new String[ ] { Integer.toString( style.getId( ) ) } );
        parameters.put( Parameters.MODE_STYLESHEET, new String[ ] { "0" } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] {
                SecurityTokenService.getInstance( ).getToken( request, "admin/stylesheet/create_stylesheet.html" ) } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem source = new DiskFileItemFactory( ).createItem( Parameters.STYLESHEET_SOURCE, "application/xml", true,
                randomName );
        source.getOutputStream( ).write( "<a/>".getBytes( ) );
        items.add( source );
        multipartFiles.put( Parameters.STYLESHEET_SOURCE, items );
        MultipartHttpServletRequest multipart = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        try
        {
            instance.doCreateStyleSheet( multipart );
            assertTrue( StyleSheetHome.getStyleSheetList( 0 ).stream( )
                    .anyMatch( stylesheet -> stylesheet.getDescription( ).equals( randomName ) ) );
        }
        finally
        {
            StyleSheetHome.getStyleSheetList( 0 ).stream( )
                    .filter( stylesheet -> stylesheet.getDescription( ).equals( randomName ) )
                    .forEach( stylesheet -> StyleSheetHome.remove( stylesheet.getId( ) ) );
        }
    }

    public void testDoCreateStyleSheetInvalidToken( ) throws IOException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        final String randomName = getRandomName( );
        parameters.put( Parameters.STYLESHEET_NAME, new String[ ] { randomName } );
        parameters.put( Parameters.STYLES, new String[ ] { Integer.toString( style.getId( ) ) } );
        parameters.put( Parameters.MODE_STYLESHEET, new String[ ] { "0" } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] {
                SecurityTokenService.getInstance( ).getToken( request, "admin/stylesheet/create_stylesheet.html" ) + "b" } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem source = new DiskFileItemFactory( ).createItem( Parameters.STYLESHEET_SOURCE, "application/xml", true,
                randomName );
        source.getOutputStream( ).write( "<a/>".getBytes( ) );
        items.add( source );
        multipartFiles.put( Parameters.STYLESHEET_SOURCE, items );
        MultipartHttpServletRequest multipart = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        try
        {
            instance.doCreateStyleSheet( multipart );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( StyleSheetHome.getStyleSheetList( 0 ).stream( )
                    .noneMatch( stylesheet -> stylesheet.getDescription( ).equals( randomName ) ) );
        }
        finally
        {
            StyleSheetHome.getStyleSheetList( 0 ).stream( )
                    .filter( stylesheet -> stylesheet.getDescription( ).equals( randomName ) )
                    .forEach( stylesheet -> StyleSheetHome.remove( stylesheet.getId( ) ) );
        }
    }

    public void testDoCreateStyleSheetNoToken( ) throws IOException, AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        final String randomName = getRandomName( );
        parameters.put( Parameters.STYLESHEET_NAME, new String[ ] { randomName } );
        parameters.put( Parameters.STYLES, new String[ ] { Integer.toString( style.getId( ) ) } );
        parameters.put( Parameters.MODE_STYLESHEET, new String[ ] { "0" } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem source = new DiskFileItemFactory( ).createItem( Parameters.STYLESHEET_SOURCE, "application/xml", true,
                randomName );
        source.getOutputStream( ).write( "<a/>".getBytes( ) );
        items.add( source );
        multipartFiles.put( Parameters.STYLESHEET_SOURCE, items );
        MultipartHttpServletRequest multipart = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        try
        {
            instance.doCreateStyleSheet( multipart );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( StyleSheetHome.getStyleSheetList( 0 ).stream( )
                    .noneMatch( stylesheet -> stylesheet.getDescription( ).equals( randomName ) ) );
        }
        finally
        {
            StyleSheetHome.getStyleSheetList( 0 ).stream( )
                    .filter( stylesheet -> stylesheet.getDescription( ).equals( randomName ) )
                    .forEach( stylesheet -> StyleSheetHome.remove( stylesheet.getId( ) ) );
        }
    }

    /**
     * Test of getModifyStyleSheet method, of class
     * fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetModifyStyleSheet( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.STYLESHEET_ID, Integer.toString( stylesheet.getId( ) ) );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

        instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
        assertNotNull( instance.getModifyStyleSheet( request ) );
    }

    /**
     * Test of doModifyStyleSheet method, of class
     * fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     * 
     * @throws AccessDeniedException
     * @throws IOException
     */
    public void testDoModifyStyleSheet( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.STYLESHEET_ID, new String[ ] { Integer.toString( stylesheet.getId( ) ) } );
        parameters.put( Parameters.STYLESHEET_NAME, new String[ ] { stylesheet.getDescription( ) + "_mod" } );
        parameters.put( Parameters.STYLES, new String[ ] { Integer.toString( stylesheet.getStyleId( ) ) } );
        parameters.put( Parameters.MODE_STYLESHEET, new String[ ] { Integer.toString( stylesheet.getModeId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] {
                SecurityTokenService.getInstance( ).getToken( request, "admin/stylesheet/modify_stylesheet.html" ) } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem source = new DiskFileItemFactory( ).createItem( Parameters.STYLESHEET_SOURCE, "application/xml", true,
                stylesheet.getDescription( ) );
        source.getOutputStream( ).write( "<a/>".getBytes( ) );
        items.add( source );
        multipartFiles.put( Parameters.STYLESHEET_SOURCE, items );
        MultipartHttpServletRequest multipart = new MultipartHttpServletRequest( request, multipartFiles, parameters );

        instance.doModifyStyleSheet( multipart );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNull( message );
        StyleSheet stored = StyleSheetHome.findByPrimaryKey( stylesheet.getId( ) );
        assertNotNull( stored );
        assertEquals( stylesheet.getDescription( ) + "_mod", stored.getDescription( ) );
    }

    public void testDoModifyStyleSheetInvalidToken( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.STYLESHEET_ID, new String[ ] { Integer.toString( stylesheet.getId( ) ) } );
        parameters.put( Parameters.STYLESHEET_NAME, new String[ ] { stylesheet.getDescription( ) + "_mod" } );
        parameters.put( Parameters.STYLES, new String[ ] { Integer.toString( stylesheet.getStyleId( ) ) } );
        parameters.put( Parameters.MODE_STYLESHEET, new String[ ] { Integer.toString( stylesheet.getModeId( ) ) } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[ ] {
                SecurityTokenService.getInstance( ).getToken( request, "admin/stylesheet/modify_stylesheet.html" ) + "b" } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem source = new DiskFileItemFactory( ).createItem( Parameters.STYLESHEET_SOURCE, "application/xml", true,
                stylesheet.getDescription( ) );
        source.getOutputStream( ).write( "<a/>".getBytes( ) );
        items.add( source );
        multipartFiles.put( Parameters.STYLESHEET_SOURCE, items );
        MultipartHttpServletRequest multipart = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        try
        {
            instance.doModifyStyleSheet( multipart );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            StyleSheet stored = StyleSheetHome.findByPrimaryKey( stylesheet.getId( ) );
            assertNotNull( stored );
            assertEquals( stylesheet.getDescription( ), stored.getDescription( ) );
        }
    }

    public void testDoModifyStyleSheetNoToken( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.STYLESHEET_ID, new String[ ] { Integer.toString( stylesheet.getId( ) ) } );
        parameters.put( Parameters.STYLESHEET_NAME, new String[ ] { stylesheet.getDescription( ) + "_mod" } );
        parameters.put( Parameters.STYLES, new String[ ] { Integer.toString( stylesheet.getStyleId( ) ) } );
        parameters.put( Parameters.MODE_STYLESHEET, new String[ ] { Integer.toString( stylesheet.getModeId( ) ) } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> items = new ArrayList<>( );
        FileItem source = new DiskFileItemFactory( ).createItem( Parameters.STYLESHEET_SOURCE, "application/xml", true,
                stylesheet.getDescription( ) );
        source.getOutputStream( ).write( "<a/>".getBytes( ) );
        items.add( source );
        multipartFiles.put( Parameters.STYLESHEET_SOURCE, items );
        MultipartHttpServletRequest multipart = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        try
        {
            instance.doModifyStyleSheet( multipart );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            StyleSheet stored = StyleSheetHome.findByPrimaryKey( stylesheet.getId( ) );
            assertNotNull( stored );
            assertEquals( stylesheet.getDescription( ), stored.getDescription( ) );
        }
    }

    /**
     * Test of getConfirmRemoveStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testGetConfirmRemoveStyleSheet( ) throws AccessDeniedException
    {
        if ( StyleSheetHome.getStyleSheetList( 0 ).size( ) > 0 )
        {
            int nStyleSheetId = StyleSheetHome.getStyleSheetList( 0 ).iterator( ).next( ).getId( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( Parameters.STYLESHEET_ID, "" + nStyleSheetId );
            System.out.println( "-> using stylesheet ID : " + nStyleSheetId );
            Utils.registerAdminUserWithRigth( request, new AdminUser( ), StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );

            instance.init( request, StyleSheetJspBean.RIGHT_MANAGE_STYLESHEET );
            instance.getRemoveStyleSheet( request );
        }
    }

    /**
     * Test of doRemoveStyleSheet method, of class fr.paris.lutece.portal.web.stylesheet.StyleSheetJspBean.
     */
    public void testDoRemoveStyleSheet( )
    {
        // Not implemented yet
    }
}
