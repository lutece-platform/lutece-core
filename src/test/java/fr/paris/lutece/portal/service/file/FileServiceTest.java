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
package fr.paris.lutece.portal.service.file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.file.implementation.DefaultFileDownloadService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.http.MockMultipartItem;
import fr.paris.lutece.util.http.TemporaryMultipartItemFactory;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author seboo
 */
public class FileServiceTest extends LuteceTestCase
{
    @Inject
    @Named( "defaultDatabaseFileStoreProvider" )
    private IFileStoreServiceProvider _fileServiceProvider;

    @Inject
    private FileService _fileService;
    
    /**
     * test store Lutece File with deprecated FileService
     * 
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testStoreFileWithFileService( ) throws UnsupportedEncodingException
    {
        File file = getOneLuteceFile( );
        
        try
        {
            String strFileId = _fileService.getFileStoreServiceProvider( ).storeFile( file );

            File storedFile = _fileService.getFileStoreServiceProvider( ).getFile( strFileId );

            assertEquals( file.getTitle( ), storedFile.getTitle( ) );

            // test delete
            _fileService.getFileStoreServiceProvider( ).delete( strFileId );
            storedFile = _fileService.getFileStoreServiceProvider( ).getFile( strFileId );

            assertNull( storedFile );

        }
        catch( FileServiceException e )
        {
            fail( e.getMessage( ) );
        }

    }

    /**
     * test store Lutece File
     * 
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testStoreFile( ) throws UnsupportedEncodingException
    {
        File file = getOneLuteceFile( );

        try
        {
            String strFileId = _fileServiceProvider.storeFile( file );

            File storedFile = _fileServiceProvider.getFile( strFileId );

            assertEquals( file.getTitle( ), storedFile.getTitle( ) );

            // test delete
            _fileServiceProvider.delete( strFileId );
            storedFile = _fileServiceProvider.getFile( strFileId );

            assertNull( storedFile );

        }
        catch( FileServiceException e )
        {
            fail( e.getMessage( ) );
        }

    }

    /**
     * test store
     * 
     * @throws IOException
     */
    @Test
    public void testStoreBytes( ) throws IOException
    {
        try
        {
            java.io.File file = getOneFile( );
            byte [ ] fileInBytes = FileUtils.readFileToByteArray( file );

            String strFileId = _fileServiceProvider.storeBytes( fileInBytes );

            File storedFile = _fileServiceProvider.getFile( strFileId );

            assertEquals( fileInBytes.length, storedFile.getPhysicalFile( ).getValue( ).length );
        }
        catch( FileServiceException e )
        {
            fail( e.getMessage( ) );
        }
    }

    /**
     * test store fileitem
     * 
     * @throws IOException
     */
    @Test
    public void testStoreFileItem( ) throws IOException
    {

        try
        {
            java.io.File file = getOneFile( );
            MultipartItem fileItem = getOneFileItem( file );

            byte [ ] fileInBytes = FileUtils.readFileToByteArray( file );

            String strFileId = _fileServiceProvider.storeFileItem( fileItem );

            File storedFile = _fileServiceProvider.getFile( strFileId );

            assertEquals( fileInBytes.length, storedFile.getPhysicalFile( ).getValue( ).length );
        }
        catch( FileServiceException e )
        {
            fail( e.getMessage( ) );
        }
    }

    /**
     * test store fileitem
     * 
     * @throws IOException
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.file.ExpiredLinkException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    @Test
    public void testDownloadFileBO( ) throws IOException, AccessDeniedException, ExpiredLinkException, UserNotSignedException
    {
        File file = getOneLuteceFile( );

        try
        {
            String strFileId = _fileServiceProvider.storeFile( file );

            Map<String, String> data = new HashMap<>( );
            data.put( DefaultFileDownloadService.PARAMETER_RESOURCE_ID, "123" );
            data.put( DefaultFileDownloadService.PARAMETER_RESOURCE_TYPE, "TEST" );

            String strUrl = _fileServiceProvider.getFileDownloadUrlBO( strFileId, data );
            assertNotNull( strUrl );

            List<NameValuePair> params = URLEncodedUtils.parse( strUrl, Charset.forName( "UTF-8" ) );

            MockHttpServletRequest request = new MockHttpServletRequest( );
            for ( NameValuePair param : params )
            {
                request.addParameter( param.getName( ), param.getValue( ) );
            }

            // add mock BO authentication
            registerAdminUserAdmin( request );

            File storedFile = _fileServiceProvider.getFileFromRequestBO( request );

            assertEquals( file.getPhysicalFile( ).getValue( ).length, storedFile.getPhysicalFile( ).getValue( ).length );
        }
        catch( FileServiceException e )
        {
            fail( e.getMessage( ) );
        }

    }

    /**
     * test store fileitem
     * 
     * @throws IOException
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.file.ExpiredLinkException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    @Test
    public void testDownloadFileFO( ) throws IOException, AccessDeniedException, ExpiredLinkException, UserNotSignedException
    {
        File file = getOneLuteceFile( );

        try
        {
            String strFileId = _fileServiceProvider.storeFile( file );

            Map<String, String> data = new HashMap<>( );
            data.put( DefaultFileDownloadService.PARAMETER_RESOURCE_ID, "123" );
            data.put( DefaultFileDownloadService.PARAMETER_RESOURCE_TYPE, "TEST" );

            String strUrl = _fileServiceProvider.getFileDownloadUrlFO( strFileId, data );
            assertNotNull( strUrl );

            List<NameValuePair> params = URLEncodedUtils.parse( strUrl, Charset.forName( "UTF-8" ) );

            MockHttpServletRequest request = new MockHttpServletRequest( );
            for ( NameValuePair param : params )
            {
                request.addParameter( param.getName( ), param.getValue( ) );
            }

            // no authentication

            File storedFile = _fileServiceProvider.getFileFromRequestFO( request );

            assertEquals( file.getPhysicalFile( ).getValue( ).length, storedFile.getPhysicalFile( ).getValue( ).length );
        }
        catch( FileServiceException e )
        {
            fail( e.getMessage( ) );
        }

    }

    /**
     * get lutece test file
     * 
     * @return a file
     */
    private File getOneLuteceFile( ) throws UnsupportedEncodingException
    {
        File file = new File( );
        file.setTitle( "test" );
        file.setDateCreation( DateUtil.formatTimestamp( "1/1/2000", Locale.FRANCE ) );
        file.setExtension( "txt" );
        file.setMimeType( "text/plain" );
        file.setSize( 12 );

        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setIdPhysicalFile( 1 );
        physicalFile.setValue( "some content".getBytes( "UTF-8" ) );

        file.setPhysicalFile( physicalFile );

        return file;
    }

    /**
     * get java.io.file
     * 
     * @return the file
     * @throws IOException
     */
    private java.io.File getOneFile( ) throws IOException
    {
        java.io.File file = java.io.File.createTempFile( "test", "txt" );

        FileWriter fileWriter = new FileWriter( file.getPath( ), true );
        PrintWriter printWriter = new PrintWriter( fileWriter );

        printWriter.print( "some content" );
        printWriter.close( );

        return file;
    }

    /**
     * get test FileItem
     * 
     * @return the file
     */
    private MultipartItem getOneFileItem( java.io.File file ) throws IOException
    {
        MockMultipartItem fileItem = TemporaryMultipartItemFactory.create( file.getName( ), "text/plain", file.getPath( ) );

        fileItem.getOutputStream( ).write( FileUtils.readFileToByteArray( file ) );

        return fileItem;
    }

    /**
     * register admin adminUser for tests
     * 
     * @param request
     * @throws AccessDeniedException
     * @throws UserNotSignedException
     */
    private void registerAdminUserAdmin( HttpServletRequest request ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );
        adminUser.setLastName( "test" );
        adminUser.setStatus( 0 );
        adminUser.setUserLevel( 0 );

        AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
    }
}
