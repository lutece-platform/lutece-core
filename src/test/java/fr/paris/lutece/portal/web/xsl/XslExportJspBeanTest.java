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
package fr.paris.lutece.portal.web.xsl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

public class XslExportJspBeanTest extends LuteceTestCase
{
    private XslExportJspBean _instance;
    private XslExport _xslExport;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _instance = new XslExportJspBean( );
        String strName = getRandomName( );
        _xslExport = new XslExport( );
        _xslExport.setTitle( strName );
        _xslExport.setDescription( strName );
        _xslExport.setExtension( strName );
        _xslExport.setPlugin( "" );
        File file = new File( );
        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( new byte [ 1] );
        file.setTitle( strName );
        file.setSize( 1 );
        file.setPhysicalFile( physicalFile );
        file.setMimeType( "application/xml" );
        _xslExport.setFile( file );
        _xslExport.getFile( ).setIdFile( FileHome.create( _xslExport.getFile( ) ) );
        XslExportHome.create( _xslExport );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        XslExportHome.remove( _xslExport.getIdXslExport( ) );
        File file = FileHome.findByPrimaryKey( _xslExport.getFile( ).getIdFile( ) );
        if ( file != null )
        {
            FileHome.remove( file.getIdFile( ) );
        }
        super.tearDown( );
    }

    public void testGetCreateXslExport( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        assertNotNull( _instance.getCreateXslExport( request ) );
    }

    public void testDoCreateXslExport( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        String randomName = getRandomName( );
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "title", new String [ ] {
            randomName
        } );
        parameters.put( "description", new String [ ] {
            randomName
        } );
        parameters.put( "extension", new String [ ] {
            randomName
        } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
            SecurityTokenService.getInstance( ).getToken( request, "admin/xsl/create_xsl_export.html" )
        } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem item = new DiskFileItemFactory( ).createItem( "id_file", "", false, "xsl" );
        item.getOutputStream( ).write( "<?xml version='1.0'?><a/>".getBytes( ) );
        fileItems.add( item );
        multipartFiles.put( "id_file", fileItems );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        try
        {
            _instance.doCreateXslExport( new MultipartHttpServletRequest( request, multipartFiles, parameters ) );
            assertTrue( XslExportHome.getList( ).stream( )
                    .anyMatch( e -> randomName.equals( e.getTitle( ) ) && randomName.equals( e.getDescription( ) ) && randomName.equals( e.getExtension( ) ) ) );
        }
        finally
        {
            XslExportHome.getList( ).stream( ).filter( e -> randomName.equals( e.getTitle( ) ) ).forEach( e -> XslExportHome.remove( e.getIdXslExport( ) ) );
        }
    }

    public void testDoCreateXslExportInvalidToken( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        String randomName = getRandomName( );
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "title", new String [ ] {
            randomName
        } );
        parameters.put( "description", new String [ ] {
            randomName
        } );
        parameters.put( "extension", new String [ ] {
            randomName
        } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
            SecurityTokenService.getInstance( ).getToken( request, "admin/xsl/create_xsl_export.html" ) + "b"
        } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem item = new DiskFileItemFactory( ).createItem( "id_file", "", false, "xsl" );
        item.getOutputStream( ).write( "<?xml version='1.0'?><a/>".getBytes( ) );
        fileItems.add( item );
        multipartFiles.put( "id_file", fileItems );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        try
        {
            _instance.doCreateXslExport( new MultipartHttpServletRequest( request, multipartFiles, parameters ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException ade )
        {
            assertFalse( XslExportHome.getList( ).stream( )
                    .anyMatch( e -> randomName.equals( e.getTitle( ) ) && randomName.equals( e.getDescription( ) ) && randomName.equals( e.getExtension( ) ) ) );
        }
        finally
        {
            XslExportHome.getList( ).stream( ).filter( e -> randomName.equals( e.getTitle( ) ) ).forEach( e -> XslExportHome.remove( e.getIdXslExport( ) ) );
        }
    }

    public void testDoCreateXslExportNoToken( ) throws AccessDeniedException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        String randomName = getRandomName( );
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "title", new String [ ] {
            randomName
        } );
        parameters.put( "description", new String [ ] {
            randomName
        } );
        parameters.put( "extension", new String [ ] {
            randomName
        } );

        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem item = new DiskFileItemFactory( ).createItem( "id_file", "", false, "xsl" );
        item.getOutputStream( ).write( "<?xml version='1.0'?><a/>".getBytes( ) );
        fileItems.add( item );
        multipartFiles.put( "id_file", fileItems );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        try
        {
            _instance.doCreateXslExport( new MultipartHttpServletRequest( request, multipartFiles, parameters ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException ade )
        {
            assertFalse( XslExportHome.getList( ).stream( )
                    .anyMatch( e -> randomName.equals( e.getTitle( ) ) && randomName.equals( e.getDescription( ) ) && randomName.equals( e.getExtension( ) ) ) );
        }
        finally
        {
            XslExportHome.getList( ).stream( ).filter( e -> randomName.equals( e.getTitle( ) ) ).forEach( e -> XslExportHome.remove( e.getIdXslExport( ) ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    public void testGetModifyXslExport( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        request.setParameter( "id_xsl_export", Integer.toString( _xslExport.getIdXslExport( ) ) );
        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        assertNotNull( _instance.getModifyXslExport( request ) );
    }

    public void testDoModifyXslExport( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        String randomName = getRandomName( );
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "title", new String [ ] {
            randomName
        } );
        parameters.put( "description", new String [ ] {
            randomName
        } );
        parameters.put( "extension", new String [ ] {
            randomName
        } );
        parameters.put( "id_xsl_export", new String [ ] {
            Integer.toString( _xslExport.getIdXslExport( ) )
        } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
            SecurityTokenService.getInstance( ).getToken( request, "admin/xsl/modify_xsl_export.html" )
        } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        _instance.doModifyXslExport( new MultipartHttpServletRequest( request, multipartFiles, parameters ) );

        XslExport stored = XslExportHome.findByPrimaryKey( _xslExport.getIdXslExport( ) );
        assertNotNull( stored );
        assertEquals( randomName, stored.getTitle( ) );
        assertEquals( randomName, stored.getDescription( ) );
        assertEquals( randomName, stored.getExtension( ) );
    }

    public void testDoModifyXslExportInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        String randomName = getRandomName( );
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "title", new String [ ] {
            randomName
        } );
        parameters.put( "description", new String [ ] {
            randomName
        } );
        parameters.put( "extension", new String [ ] {
            randomName
        } );
        parameters.put( "id_xsl_export", new String [ ] {
            Integer.toString( _xslExport.getIdXslExport( ) )
        } );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, new String [ ] {
            SecurityTokenService.getInstance( ).getToken( request, "admin/xsl/modify_xsl_export.html" ) + "b"
        } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        try
        {
            _instance.doModifyXslExport( new MultipartHttpServletRequest( request, multipartFiles, parameters ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            XslExport stored = XslExportHome.findByPrimaryKey( _xslExport.getIdXslExport( ) );
            assertNotNull( stored );
            assertEquals( _xslExport.getTitle( ), stored.getTitle( ) );
            assertEquals( _xslExport.getDescription( ), stored.getDescription( ) );
            assertEquals( _xslExport.getExtension( ), stored.getExtension( ) );
        }
    }

    public void testDoModifyXslExportNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );
        String randomName = getRandomName( );
        Map<String, String [ ]> parameters = new HashMap<>( );
        parameters.put( "title", new String [ ] {
            randomName
        } );
        parameters.put( "description", new String [ ] {
            randomName
        } );
        parameters.put( "extension", new String [ ] {
            randomName
        } );
        parameters.put( "id_xsl_export", new String [ ] {
            Integer.toString( _xslExport.getIdXslExport( ) )
        } );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );

        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        try
        {
            _instance.doModifyXslExport( new MultipartHttpServletRequest( request, multipartFiles, parameters ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            XslExport stored = XslExportHome.findByPrimaryKey( _xslExport.getIdXslExport( ) );
            assertNotNull( stored );
            assertEquals( _xslExport.getTitle( ), stored.getTitle( ) );
            assertEquals( _xslExport.getDescription( ), stored.getDescription( ) );
            assertEquals( _xslExport.getExtension( ), stored.getExtension( ) );
        }
    }

    public void testGetConfirmRemoveXslExport( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        request.setParameter( "id_xsl_export", Integer.toString( _xslExport.getIdXslExport( ) ) );
        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        _instance.getConfirmRemoveXslExport( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    public void testDoRemoveXslExport( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        request.setParameter( "id_xsl_export", Integer.toString( _xslExport.getIdXslExport( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/xsl/DoRemoveXslExport.jsp" ) );
        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        _instance.doRemoveXslExport( request );

        XslExport stored = XslExportHome.findByPrimaryKey( _xslExport.getIdXslExport( ) );
        assertNull( stored );
    }

    public void testDoRemoveXslExportInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        request.setParameter( "id_xsl_export", Integer.toString( _xslExport.getIdXslExport( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/xsl/DoRemoveXslExport.jsp" ) + "b" );
        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        try
        {
            _instance.doRemoveXslExport( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            XslExport stored = XslExportHome.findByPrimaryKey( _xslExport.getIdXslExport( ) );
            assertNotNull( stored );
        }
    }

    public void testDoRemoveXslExportNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( AdminRoleHome.findAll( ).stream( ).collect( Collectors.toMap( AdminRole::getKey, Function.identity( ) ) ) );
        Utils.registerAdminUserWithRigth( request, user, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        request.setParameter( "id_xsl_export", Integer.toString( _xslExport.getIdXslExport( ) ) );
        _instance.init( request, XslExportJspBean.RIGHT_MANAGE_XSL_EXPORT );

        try
        {
            _instance.doRemoveXslExport( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            XslExport stored = XslExportHome.findByPrimaryKey( _xslExport.getIdXslExport( ) );
            assertNotNull( stored );
        }
    }
}
