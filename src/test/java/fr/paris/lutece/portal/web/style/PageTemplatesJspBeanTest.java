/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.style.PageTemplate;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * PageTemplatesJspBeanTest Test Class
 *
 */
public class PageTemplatesJspBeanTest extends LuteceTestCase
{
    private static final String TEST_PAGE_TEMPLATE_ID = "1"; // Page template one column
    private MockHttpServletRequest request;
    private PageTemplatesJspBean instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
        instance = new PageTemplatesJspBean( );
        instance.init( request, PageTemplatesJspBean.RIGHT_MANAGE_PAGE_TEMPLATES );
    }
    /**
     * Test of getManagePageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testGetManagePageTemplate( ) throws AccessDeniedException
    {
        instance.getManagePageTemplate( request );
    }

    /**
     * Test of getCreatePageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testGetCreatePageTemplate( ) throws AccessDeniedException
    {
        instance.getCreatePageTemplate( request );
    }

    /**
     * Test of doCreatePageTemplate method, of fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     * @throws AccessDeniedException 
     * @throws IOException 
     */
    public void testDoCreatePageTemplate( ) throws AccessDeniedException, IOException
    {
        final String desc = getRandomName( );
        Map<String, String[ ]> parameters = new HashMap<String, String[]>( );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String[] { desc } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[] { SecurityTokenService.getInstance( ).getToken( request, "admin/style/create_page_template.html" ) } );
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory( );
        Map<String, List<FileItem>> files = new HashMap<>( );
        List<FileItem> pageTemplateFiles = new ArrayList<>( );
        FileItem pageTemplateFile = fileItemFactory.createItem( "page_template_file", "text/html", false, "junit.html" );
        pageTemplateFile.getOutputStream( ).write( new byte[1] );
        pageTemplateFiles.add( pageTemplateFile );
        files.put( "page_template_file", pageTemplateFiles );
        List<FileItem> pageTemplatePictures = new ArrayList<>( );
        FileItem pageTemplatePicture = fileItemFactory.createItem( "page_template_picture", "image/jpg", false, "junit.jpg" );
        pageTemplatePicture.getOutputStream( ).write( new byte[1] );
        pageTemplatePictures.add( pageTemplatePicture );
        files.put( "page_template_picture", pageTemplatePictures );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, files, parameters );
        try
        {
            instance.doCreatePageTemplate( multipartRequest );
            assertTrue( PageTemplateHome.getPageTemplatesList( ).stream( ).anyMatch( t -> t.getDescription( ).equals( desc ) ) );
        }
        finally
        {
            PageTemplateHome.getPageTemplatesList( ).stream( ).filter( t -> t.getDescription( ).equals( desc ) ).forEach( t -> PageTemplateHome.remove( t.getId( ) ) );
        }
    }

    public void testDoCreatePageTemplateInvalidToken( ) throws AccessDeniedException, IOException
    {
        final String desc = getRandomName( );
        Map<String, String[ ]> parameters = new HashMap<String, String[]>( );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String[] { desc } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[] { SecurityTokenService.getInstance( ).getToken( request, "admin/style/create_page_template.html" ) + "b" } );
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory( );
        Map<String, List<FileItem>> files = new HashMap<>( );
        List<FileItem> pageTemplateFiles = new ArrayList<>( );
        FileItem pageTemplateFile = fileItemFactory.createItem( "page_template_file", "text/html", false, "junit.html" );
        pageTemplateFile.getOutputStream( ).write( new byte[1] );
        pageTemplateFiles.add( pageTemplateFile );
        files.put( "page_template_file", pageTemplateFiles );
        List<FileItem> pageTemplatePictures = new ArrayList<>( );
        FileItem pageTemplatePicture = fileItemFactory.createItem( "page_template_picture", "image/jpg", false, "junit.jpg" );
        pageTemplatePicture.getOutputStream( ).write( new byte[1] );
        pageTemplatePictures.add( pageTemplatePicture );
        files.put( "page_template_picture", pageTemplatePictures );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, files, parameters );
        try
        {
            instance.doCreatePageTemplate( multipartRequest );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( PageTemplateHome.getPageTemplatesList( ).stream( ).anyMatch( t -> t.getDescription( ).equals( desc ) ) );
        }
        finally
        {
            PageTemplateHome.getPageTemplatesList( ).stream( ).filter( t -> t.getDescription( ).equals( desc ) ).forEach( t -> PageTemplateHome.remove( t.getId( ) ) );
        }
    }

    public void testDoCreatePageTemplateNoToken( ) throws AccessDeniedException, IOException
    {
        final String desc = getRandomName( );
        Map<String, String[ ]> parameters = new HashMap<String, String[]>( );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String[] { desc } );
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory( );
        Map<String, List<FileItem>> files = new HashMap<>( );
        List<FileItem> pageTemplateFiles = new ArrayList<>( );
        FileItem pageTemplateFile = fileItemFactory.createItem( "page_template_file", "text/html", false, "junit.html" );
        pageTemplateFile.getOutputStream( ).write( new byte[1] );
        pageTemplateFiles.add( pageTemplateFile );
        files.put( "page_template_file", pageTemplateFiles );
        List<FileItem> pageTemplatePictures = new ArrayList<>( );
        FileItem pageTemplatePicture = fileItemFactory.createItem( "page_template_picture", "image/jpg", false, "junit.jpg" );
        pageTemplatePicture.getOutputStream( ).write( new byte[1] );
        pageTemplatePictures.add( pageTemplatePicture );
        files.put( "page_template_picture", pageTemplatePictures );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, files, parameters );
        try
        {
            instance.doCreatePageTemplate( multipartRequest );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( PageTemplateHome.getPageTemplatesList( ).stream( ).anyMatch( t -> t.getDescription( ).equals( desc ) ) );
        }
        finally
        {
            PageTemplateHome.getPageTemplatesList( ).stream( ).filter( t -> t.getDescription( ).equals( desc ) ).forEach( t -> PageTemplateHome.remove( t.getId( ) ) );
        }
    }

    /**
     * Test of getModifyPageTemplate method, of class fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     */
    public void testGetModifyPageTemplate( ) throws AccessDeniedException
    {
        request.addParameter( Parameters.PAGE_TEMPLATE_ID, TEST_PAGE_TEMPLATE_ID );
        instance.getModifyPageTemplate( request );
    }

    /**
     * Test of doModifyPageTemplate method, of fr.paris.lutece.portal.web.style.PageTemplatesJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoModifyPageTemplate( ) throws AccessDeniedException
    {
        final String desc = getRandomName();
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        PageTemplateHome.create( pageTemplate );

        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[] { Integer.toString( pageTemplate.getId( ) ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String[] { desc + "mod" } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[] { SecurityTokenService.getInstance( ).getToken( request, "admin/style/modify_page_template.html" ) } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
        try
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
            instance.doModifyPageTemplate( multipartRequest );
            assertEquals( desc + "mod", PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    public void testDoModifyPageTemplateInvalidToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName();
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        PageTemplateHome.create( pageTemplate );

        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[] { Integer.toString( pageTemplate.getId( ) ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String[] { desc + "mod" } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String[] { SecurityTokenService.getInstance( ).getToken( request, "admin/style/modify_page_template.html" ) + "b" } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
        try
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
            instance.doModifyPageTemplate( multipartRequest );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    public void testDoModifyPageTemplateNoToken( ) throws AccessDeniedException
    {
        final String desc = getRandomName();
        PageTemplate pageTemplate = new PageTemplate( );
        pageTemplate.setDescription( desc );
        PageTemplateHome.create( pageTemplate );

        Map<String, String[ ]> parameters = new HashMap<>( );
        parameters.put( Parameters.PAGE_TEMPLATE_ID, new String[] { Integer.toString( pageTemplate.getId( ) ) } );
        parameters.put( Parameters.PAGE_TEMPLATE_DESCRIPTION, new String[] { desc + "mod" } );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, Collections.emptyMap( ), parameters );
        try
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
            instance.doModifyPageTemplate( multipartRequest );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( desc, PageTemplateHome.findByPrimaryKey( pageTemplate.getId( ) ).getDescription( ) );
        }
        finally
        {
            PageTemplateHome.remove( pageTemplate.getId( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
