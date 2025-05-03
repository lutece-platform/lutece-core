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

public class DefaultFileStoreServiceProvider implements IFileStoreServiceProvider
{

    private static final long serialVersionUID = 1L;

    private static final Object PARAMETER_FILE_ID = "file_id";

    protected String _name;
    protected boolean _isDefault = false;

    IFileStoreService _fsService;
    IFileDownloadUrlService _dlService;
    IFileRBACService _rbacService;

    public DefaultFileStoreServiceProvider( String _name, IFileStoreService _fsService, IFileDownloadUrlService _dlService, IFileRBACService _rbacService,
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
        return _fsService.storeFile( file );
    }

    @Override
    public File getFile( String strKey ) throws FileServiceException
    {

        return _fsService.getFile( strKey );
    }

    @Override
    public String storeFileItem( MultipartItem fileItem ) throws FileServiceException
    {
        return _fsService.storeFileItem( fileItem );
    }

    @Override
    public String storeInputStream( InputStream inputStream ) throws FileServiceException
    {
        return _fsService.storeInputStream( inputStream );
    }

    @Override
    public String storeBytes( byte [ ] blob ) throws FileServiceException
    {
        return _fsService.storeBytes( blob );
    }

    @Override
    public fr.paris.lutece.portal.business.file.File getFileMetaData( String strKey ) throws FileServiceException
    {
        return _fsService.getFileMetaData( strKey );
    }

    @Override
    public InputStream getInputStream( String strKey ) throws FileServiceException
    {
        return _fsService.getInputStream( strKey );
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
