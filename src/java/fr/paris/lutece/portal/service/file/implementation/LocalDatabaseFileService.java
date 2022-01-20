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
package fr.paris.lutece.portal.service.file.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.file.ExpiredLinkException;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileDownloadUrlService;
import fr.paris.lutece.portal.service.file.IFileRBACService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppException;

/**
 * 
 * DatabaseBlobStoreService.
 * 
 */
public class LocalDatabaseFileService implements IFileStoreServiceProvider
{
    private static final long serialVersionUID = 1L;

    private IFileDownloadUrlService _fileDownloadUrlService;
    private IFileRBACService _fileRBACService;
    private String _strName;
    private boolean _bDefault;

    /**
     * init
     * 
     * @param _fileDownloadUrlService
     * @param _fileRBACService
     */
    public LocalDatabaseFileService( IFileDownloadUrlService _fileDownloadUrlService, IFileRBACService _fileRBACService )
    {
        this._fileDownloadUrlService = _fileDownloadUrlService;
        this._fileRBACService = _fileRBACService;
    }

    /**
     * get the FileRBACService
     * 
     * @return the FileRBACService
     */
    public IFileRBACService getFileRBACService( )
    {
        return _fileRBACService;
    }

    /**
     * set the FileRBACService
     * 
     * @param fileRBACService
     */
    public void setFileRBACService( IFileRBACService fileRBACService )
    {
        this._fileRBACService = fileRBACService;
    }

    /**
     * Get the downloadService
     * 
     * @return the downloadService
     */
    public IFileDownloadUrlService getDownloadUrlService( )
    {
        return _fileDownloadUrlService;
    }

    /**
     * Sets the downloadService
     * 
     * @param downloadUrlService
     *            downloadService
     */
    public void setDownloadUrlService( IFileDownloadUrlService downloadUrlService )
    {
        _fileDownloadUrlService = downloadUrlService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return _strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strKey )
    {
        int nfileId = Integer.parseInt( strKey );
        FileHome.remove( nfileId );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getFile( String strKey )
    {
        return getFile( strKey, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getFileMetaData( String strKey )
    {
        return getFile( strKey, false );
    }

    /**
     * get file from database
     * 
     * @param strKey
     * @param withPhysicalFile
     * 
     * @return the file with the physical file content if withPhysicalFile is true
     */
    public File getFile( String strKey, boolean withPhysicalFile )
    {
        if ( StringUtils.isNotBlank( strKey ) )
        {
            int nfileId = Integer.parseInt( strKey );
            
            // get meta data
            File file = FileHome.findByPrimaryKey( nfileId );

            if ( file != null )
            {
                if ( withPhysicalFile )
                {
                    // get file content
                    file.setPhysicalFile( PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) ) );
                }
                
                return file;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeBytes( byte [ ] blob )
    {
        File file = new File( );
        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( blob );
        file.setPhysicalFile( physicalFile );

        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeInputStream( InputStream inputStream )
    {
        File file = new File( );
        PhysicalFile physicalFile = new PhysicalFile( );

        byte [ ] buffer;

        try
        {
            buffer = new byte [ inputStream.available( )];
        }
        catch( IOException ex )
        {
            throw new AppException( ex.getMessage( ), ex );
        }

        physicalFile.setValue( buffer );
        file.setPhysicalFile( physicalFile );

        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeFileItem( FileItem fileItem )
    {

        File file = new File( );
        file.setTitle( fileItem.getName( ) );
        file.setSize( (int) fileItem.getSize( ) );
        file.setMimeType( fileItem.getContentType( ) );

        PhysicalFile physicalFile = new PhysicalFile( );

        byte [ ] byteArray;

        try
        {
            byteArray = IOUtils.toByteArray( fileItem.getInputStream( ) );
        }
        catch( IOException ex )
        {
            throw new AppException( ex.getMessage( ), ex );
        }

        physicalFile.setValue( byteArray );
        file.setPhysicalFile( physicalFile );

        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeFile( File file )
    {
        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    public void setDefault( boolean bDefault )
    {
        this._bDefault = bDefault;
    }

    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefault( )
    {
        return _bDefault;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream( String strKey )
    {

        File file = getFile( strKey );

        return new ByteArrayInputStream( file.getPhysicalFile( ).getValue( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlFO( String strKey )
    {
        return _fileDownloadUrlService.getFileDownloadUrlFO( strKey, getName( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlFO( String strKey, Map<String, String> additionnalData )
    {
        return _fileDownloadUrlService.getFileDownloadUrlFO( strKey, additionnalData, getName( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlBO( String strKey )
    {
        return _fileDownloadUrlService.getFileDownloadUrlBO( strKey, getName( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileDownloadUrlBO( String strKey, Map<String, String> additionnalData )
    {
        return _fileDownloadUrlService.getFileDownloadUrlBO( strKey, additionnalData, getName( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkAccessRights( Map<String, String> fileData, User user ) throws AccessDeniedException, UserNotSignedException
    {
        if ( _fileRBACService != null )
        {
            _fileRBACService.checkAccessRights( fileData, user );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkLinkValidity( Map<String, String> fileData ) throws ExpiredLinkException
    {
        _fileDownloadUrlService.checkLinkValidity( fileData );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getFileFromRequestBO( HttpServletRequest request ) throws AccessDeniedException, ExpiredLinkException, UserNotSignedException
    {
        Map<String, String> fileData = _fileDownloadUrlService.getRequestDataBO( request );

        // check access rights
        checkAccessRights( fileData, AdminAuthenticationService.getInstance( ).getRegisteredUser( request ) );

        // check validity
        checkLinkValidity( fileData );

        String strFileId = fileData.get( FileService.PARAMETER_FILE_ID );

        return getFile( strFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getFileFromRequestFO( HttpServletRequest request ) throws AccessDeniedException, ExpiredLinkException, UserNotSignedException
    {

        Map<String, String> fileData = _fileDownloadUrlService.getRequestDataFO( request );

        // check access rights
        checkAccessRights( fileData, SecurityService.getInstance( ).getRegisteredUser( request ) );

        // check validity
        checkLinkValidity( fileData );

        String strFileId = fileData.get( FileService.PARAMETER_FILE_ID );

        return getFile( strFileId );
    }
}
