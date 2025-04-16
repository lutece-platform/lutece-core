package fr.paris.lutece.portal.service.file;

import java.io.InputStream;
import fr.paris.lutece.portal.business.file.File;

import fr.paris.lutece.portal.service.upload.MultipartItem;
import jakarta.servlet.http.HttpServletRequest;

public interface IFileStoreService
{

    public String getName( );

    public default boolean healthCheck( )
    {
        // default
        return true;
    }

    /**
     * {@inheritDoc}
     */
    String storeFile( File file ) throws FileServiceException;

    /**
     * {@inheritDoc}
     */
    File getFile( String strKey ) throws FileServiceException;

    /**
     * {@inheritDoc}
     */
    void delete( String strKey );

    /**
     * {@inheritDoc}
     */
    fr.paris.lutece.portal.business.file.File getFileMetaData( String strKey );

    /**
     * {@inheritDoc}
     */
    String storeBytes( byte [ ] blob );

    /**
     * {@inheritDoc}
     */
    String storeInputStream( InputStream inputStream );

    /**
     * {@inheritDoc}
     */
    String storeFileItem( MultipartItem fileItem );

    /**
     * {@inheritDoc}
     */
    InputStream getInputStream( String strKey );
}
