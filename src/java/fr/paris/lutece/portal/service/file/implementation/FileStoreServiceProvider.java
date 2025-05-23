/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.io.InputStream;
import java.util.Map;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.file.ExpiredLinkException;
import fr.paris.lutece.portal.service.file.FileServiceException;
import fr.paris.lutece.portal.service.file.IFileDownloadUrlService;
import fr.paris.lutece.portal.service.file.IFileRBACService;
import fr.paris.lutece.portal.service.file.IFileStoreService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import jakarta.servlet.http.HttpServletRequest;

public class FileStoreServiceProvider implements IFileStoreServiceProvider
{

    private static final long serialVersionUID = 1L;

    private static final Object PARAMETER_FILE_ID = "file_id";

    protected String _name;
    protected boolean _isDefault = false;

    private IFileStoreService _fsService;
    private IFileDownloadUrlService _dlService;
    private IFileRBACService _rbacService;

    public FileStoreServiceProvider( String _name, IFileStoreService _fsService, IFileDownloadUrlService _dlService, IFileRBACService _rbacService,
            boolean _isDefault )
    {
        super( );
        this._name = _name;
        this._dlService = _dlService;
        this._rbacService = _rbacService;
        this._fsService = _fsService;
        this._isDefault = _isDefault;
    }

    @Override
    public String getName( )
    {
        return _name;
    }

    @Override
    public boolean isDefault( )
    {
        return _isDefault;
    }

    @Override
    public String storeFile( File file ) throws FileServiceException
    {
        return _fsService.storeFile( file, getName( ) );
    }

    @Override
    public File getFile( String strKey ) throws FileServiceException
    {

        return _fsService.getFile( strKey, getName( ) );
    }

    @Override
    public String storeFileItem( MultipartItem fileItem ) throws FileServiceException
    {
        return _fsService.storeFileItem( fileItem, getName( ) );
    }

    @Override
    public String storeInputStream( InputStream inputStream ) throws FileServiceException
    {
        return _fsService.storeInputStream( inputStream, getName( ) );
    }

    @Override
    public String storeBytes( byte [ ] blob ) throws FileServiceException
    {
        return _fsService.storeBytes( blob, getName( ) );
    }

    @Override
    public fr.paris.lutece.portal.business.file.File getFileMetaData( String strKey ) throws FileServiceException
    {
        return _fsService.getFileMetaData( strKey, getName( ) );
    }

    @Override
    public InputStream getInputStream( String strKey ) throws FileServiceException
    {
        return _fsService.getInputStream( strKey, getName( ) );
    }

    @Override
    public void delete( String strKey ) throws FileServiceException
    {
        _fsService.delete( strKey );
    }

    @Override
    public File getFileFromRequestBO( HttpServletRequest request )
            throws AccessDeniedException, ExpiredLinkException, UserNotSignedException, FileServiceException
    {
        Map<String, String> fileData = _dlService.getRequestDataBO( request );

        // check access rights
        checkAccessRights( fileData, AdminAuthenticationService.getInstance( ).getRegisteredUser( request ) );

        // check validity
        checkLinkValidity( fileData );

        String strFileId = fileData.get( PARAMETER_FILE_ID );

        return getFile( strFileId );
    }

    @Override
    public File getFileFromRequestFO( HttpServletRequest request )
            throws AccessDeniedException, ExpiredLinkException, UserNotSignedException, FileServiceException
    {
        Map<String, String> fileData = _dlService.getRequestDataFO( request );

        // check access rights
        checkAccessRights( fileData, SecurityService.getInstance( ).getRegisteredUser( request ) );

        // check validity
        checkLinkValidity( fileData );

        String strFileId = fileData.get( PARAMETER_FILE_ID );

        return getFile( strFileId );
    }

    @Override
    public String getFileDownloadUrlFO( String strKey )
    {
        return _dlService.getFileDownloadUrlFO( strKey, this.getName( ) );
    }

    @Override
    public void checkAccessRights( Map<String, String> fileData, User user ) throws AccessDeniedException, UserNotSignedException
    {
        if ( _rbacService != null )
        {
            _rbacService.checkAccessRights( fileData, user );
        }
    }

    @Override
    public String getFileDownloadUrlFO( String strKey, Map<String, String> additionnalData )
    {
        return _dlService.getFileDownloadUrlFO( strKey, additionnalData, this.getName( ) );
    }

    @Override
    public String getFileDownloadUrlBO( String strKey )
    {
        return _dlService.getFileDownloadUrlBO( strKey, this.getName( ) );
    }

    @Override
    public String getFileDownloadUrlBO( String strKey, Map<String, String> additionnalData )
    {
        return _dlService.getFileDownloadUrlBO( strKey, additionnalData, this.getName( ) );
    }

    @Override
    public void checkLinkValidity( Map<String, String> fileData ) throws ExpiredLinkException
    {
        _dlService.checkLinkValidity( fileData );
    }

    /**
     * get FileService implementations
     * 
     * @return a string
     */
    public String getDetail( )
    {

        Class<?> fsClass = this.getClass( );
        if ( fsClass.getName( ).contains( "$" ) )
        {
            fsClass = fsClass.getSuperclass( );
        }

        StringBuffer sb = new StringBuffer( );
        sb.append( "FileStoreServiceProvider : " + fsClass.getName( ) ).append( "\n" );
        sb.append( " - default : " + _isDefault ).append( "\n" );
        sb.append( " - fs : " + _fsService.getClass( ).getName( ) ).append( "\n" );
        sb.append( " - dwnld : " + _dlService.getClass( ).getName( ) ).append( "\n" );
        sb.append( " - rbac : " + _rbacService.getClass( ).getName( ) ).append( "\n" );

        return sb.toString( );
    }
}
