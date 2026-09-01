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
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
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
import fr.paris.lutece.test.mocks.MockMultipartItem;
import fr.paris.lutece.test.mocks.TemporaryMultipartItemFactory;
import fr.paris.lutece.util.date.DateUtil;
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

            MockHttpServletRequest request = new MockHttpServletRequest( );
            addUrlParameters( request, strUrl );

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

            MockHttpServletRequest request = new MockHttpServletRequest( );
            addUrlParameters( request, strUrl );

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
    /**
     * Adds to the request the parameters of a download URL, the way
     * URLEncodedUtils.parse( strUrl, UTF-8 ) used to before that class was dropped along with
     * the httpclient dependency it came from.
     *
     * <p>
     * The parsing looks odd because the string is not a query string : it is a whole URL, whose
     * parameters UrlItem.getUrlWithEntity joins with the numeric entity <code>&amp;#38;</code>.
     * Splitting it on both <code>&amp;</code> and <code>;</code>, the two separators
     * URLEncodedUtils defaulted to, therefore yields three tokens for
     * <code>&lt;url&gt;?provider=X&amp;#38;data=Y</code> : a first name carrying the path, a stray
     * <code>#38</code>, and <code>data=Y</code>. Only the last one matters, the code under test
     * reading everything it needs from the encrypted <code>data</code>. Splitting on
     * <code>&amp;</code> alone would leave <code>#38;data</code> as the name and the test would
     * fail.
     * </p>
     *
     * @param request
     *            The request to fill
     * @param strUrl
     *            The download URL
     */
    private static void addUrlParameters( MockHttpServletRequest request, String strUrl )
    {
        for ( String strToken : strUrl.split( "[&;]" ) )
        {
            if ( strToken.isEmpty( ) )
            {
                continue;
            }

            int nEquals = strToken.indexOf( '=' );

            if ( nEquals < 0 )
            {
                // a token without '=' had a null value, which the request would reject
                continue;
            }

            request.addParameter( decode( strToken.substring( 0, nEquals ) ), decode( strToken.substring( nEquals + 1 ) ) );
        }
    }

    /**
     * URL decodes a parameter name or value in UTF-8.
     *
     * @param strValue
     *            The value to decode
     * @return The decoded value
     */
    private static String decode( String strValue )
    {
        try
        {
            return URLDecoder.decode( strValue, "UTF-8" );
        }
        catch( UnsupportedEncodingException e )
        {
            throw new IllegalStateException( e );
        }
    }

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
