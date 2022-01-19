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

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.date.DateUtil;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.NameValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 *
 * @author seboo
 */
public class FileServiceTest extends LuteceTestCase
{
    /**
     * test store Lutece File
     * 
     * @throws java.io.UnsupportedEncodingException
     */
    public void testStoreFile( ) throws UnsupportedEncodingException
    {
        File file = getOneLuteceFile( );

        String strFileId = FileService.getInstance( ).getFileStoreServiceProvider( ).storeFile( file );

        File storedFile = FileService.getInstance( ).getFileStoreServiceProvider( ).getFile( strFileId );

        assertEquals( file.getTitle( ), storedFile.getTitle( ) );

        // test delete
        FileService.getInstance( ).getFileStoreServiceProvider( ).delete( strFileId );
        storedFile = FileService.getInstance( ).getFileStoreServiceProvider( ).getFile( strFileId );

        assertNull( storedFile );

    }

    /**
     * test store
     * 
     * @throws IOException
     */
    public void testStoreBytes( ) throws IOException
    {
        java.io.File file = getOneFile( );
        byte [ ] fileInBytes = FileUtils.readFileToByteArray( file );

        String strFileId = FileService.getInstance( ).getFileStoreServiceProvider( ).storeBytes( fileInBytes );

        File storedFile = FileService.getInstance( ).getFileStoreServiceProvider( ).getFile( strFileId );

        assertEquals( fileInBytes.length, storedFile.getPhysicalFile( ).getValue( ).length );
    }

    /**
     * test store fileitem
     * 
     * @throws IOException
     */
    public void testStoreFileItem( ) throws IOException
    {
        java.io.File file = getOneFile( );
        FileItem fileItem = getOneFileItem( file );

        byte [ ] fileInBytes = FileUtils.readFileToByteArray( file );
        InputStream inputStream = new FileInputStream( file );

        String strFileId = FileService.getInstance( ).getFileStoreServiceProvider( ).storeFileItem( fileItem );

        File storedFile = FileService.getInstance( ).getFileStoreServiceProvider( ).getFile( strFileId );

        assertEquals( fileInBytes.length, storedFile.getPhysicalFile( ).getValue( ).length );
    }

    /**
     * test store fileitem
     * 
     * @throws IOException
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.file.ExpiredLinkException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    public void testDownloadFileBO( ) throws IOException, AccessDeniedException, ExpiredLinkException, UserNotSignedException
    {
        File file = getOneLuteceFile( );

        String strFileId = FileService.getInstance( ).getFileStoreServiceProvider( ).storeFile( file );

        Map<String, String> data = new HashMap<>( );
        data.put( FileService.PARAMETER_RESOURCE_ID, "123" );
        data.put( FileService.PARAMETER_RESOURCE_TYPE, "TEST" );

        String strUrl = FileService.getInstance( ).getFileStoreServiceProvider( ).getFileDownloadUrlBO( strFileId, data );
        assertNotNull( strUrl );

        List<NameValuePair> params = URLEncodedUtils.parse( strUrl, Charset.forName( "UTF-8" ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        for ( NameValuePair param : params )
        {
            request.addParameter( param.getName( ), param.getValue( ) );
        }

        // add mock BO authentication
        registerAdminUserAdmin( request );

        File storedFile = FileService.getInstance( ).getFileStoreServiceProvider( ).getFileFromRequestBO( request );

        assertEquals( file.getPhysicalFile( ).getValue( ).length, storedFile.getPhysicalFile( ).getValue( ).length );

    }

    /**
     * test store fileitem
     * 
     * @throws IOException
     * @throws fr.paris.lutece.portal.service.admin.AccessDeniedException
     * @throws fr.paris.lutece.portal.service.file.ExpiredLinkException
     * @throws fr.paris.lutece.portal.service.security.UserNotSignedException
     */
    public void testDownloadFileFO( ) throws IOException, AccessDeniedException, ExpiredLinkException, UserNotSignedException
    {
        File file = getOneLuteceFile( );

        String strFileId = FileService.getInstance( ).getFileStoreServiceProvider( ).storeFile( file );

        Map<String, String> data = new HashMap<>( );
        data.put( FileService.PARAMETER_RESOURCE_ID, "123" );
        data.put( FileService.PARAMETER_RESOURCE_TYPE, "TEST" );

        String strUrl = FileService.getInstance( ).getFileStoreServiceProvider( ).getFileDownloadUrlFO( strFileId, data );
        assertNotNull( strUrl );

        List<NameValuePair> params = URLEncodedUtils.parse( strUrl, Charset.forName( "UTF-8" ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        for ( NameValuePair param : params )
        {
            request.addParameter( param.getName( ), param.getValue( ) );
        }

        // no authentication

        File storedFile = FileService.getInstance( ).getFileStoreServiceProvider( ).getFileFromRequestFO( request );

        assertEquals( file.getPhysicalFile( ).getValue( ).length, storedFile.getPhysicalFile( ).getValue( ).length );

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
    private FileItem getOneFileItem( java.io.File file ) throws IOException
    {
        DiskFileItemFactory factory = new DiskFileItemFactory( );
        FileItem fileItem = factory.createItem( file.getName( ), "text/plain", false, file.getPath( ) );

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
