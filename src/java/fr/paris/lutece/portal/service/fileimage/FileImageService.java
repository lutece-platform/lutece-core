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
package fr.paris.lutece.portal.service.fileimage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.FileServiceException;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.util.file.FileUtil;

/**
 * Service for AdminUser image attributes. Provide ImageResource management
 */
@ApplicationScoped
public class FileImageService implements ImageResourceProvider
{
    private static final String IMAGE_RESOURCE_TYPE_ID = "core_attribute_img";
    @Inject
    private FileService _fileService;
    
    /**
     * Creates a new instance of FileImgService
     */
    FileImageService( )
    {
    }

    /**
     * Init
     *
     * @throws LuteceInitException
     *             if an error occurs
     */
    public static synchronized void init( )
    {
        getInstance( ).register( );
    }

    /**
     * Initializes the service
     */
    public void register( )
    {
        ImageResourceManager.registerProvider( this );
    }

    /**
     * Returns the unique instance of the {@link FileImageService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link FileImageService} instance instead.</p>
     * 
     * @return The unique instance of {@link FileImageService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link FileImageService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static FileImageService getInstance( )
    {
        return CDI.current().select(FileImageService.class).get();
    }

    /**
     * Return the Resource id
     *
     * @param nIdResource
     *            The resource identifier
     * @return The Resource Image
     */
    public ImageResource getImageResource( int nIdResource )
    {
        
        if ( !isAuthorized( nIdResource ) ) return null;
        
        File file = FileHome.findByPrimaryKey( nIdResource );

        if ( ( file != null ) && ( file.getPhysicalFile( ) != null ) && FileUtil.hasImageExtension( file.getTitle( ) ) )
        {
            PhysicalFile physicalFile = PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) );
            ImageResource imageResource = new ImageResource( );
            imageResource.setImage( physicalFile.getValue( ) );
            imageResource.setMimeType( file.getMimeType( ) );

            return imageResource;
        }
        

        return null;
    }

    /**
     * Return the Resource Type id
     *
     * @return The Resource Type Id
     */
    public String getResourceTypeId( )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }

    /**
     * check rights
     *
     * @param nIdResource
     * @return true if authorized
     */
    private static boolean isAuthorized(  int nIdResource  )
    {
        HttpServletRequest request = LocalVariables.getRequest( );
        AdminUser user = AdminUserService.getAdminUser( request );

        return ( user == null || !AdminUserFieldHome.existsWithFile( nIdResource ) );
    }
    
    /**
     * Add Image Resource
     * @param fileItem
     * @return the Image File Key
     */
    public String addImageResource( MultipartItem fileItem )
	{
    	try 
    	{
    		return _fileService.getFileStoreServiceProvider( ).storeFileItem( fileItem );
    	}
		catch (FileServiceException e )
    	{
			AppLogService.error(e);
			return null;
    	}
	}
}