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

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;
import fr.paris.lutece.portal.service.file.FileServiceException;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.file.FileUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

@ApplicationScoped
public class FileImagePublicService implements ImageResourceProvider 
{
	@Inject
	@LuteceCache(cacheName = IMAGE_RESOURCE_TYPE_ID, keyType = String.class, valueType = ImageResource.class, enable = false)
	private Lutece107Cache<String, ImageResource> _cacheFileImage;

	public static final String IMAGE_RESOURCE_TYPE_ID = "public_image_resource";
	
	@Inject
	@Named( "defaultDatabaseFileStoreProvider" )
	private transient IFileStoreServiceProvider _fileStoreService;

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
     * Returns the unique instance of the {@link FileImagePublicService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link FileImagePublicService} instance instead.</p>
     * 
     * @return The unique instance of {@link FileImagePublicService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link FileImagePublicService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static FileImagePublicService getInstance( )
    {
        return CDI.current().select(FileImagePublicService.class).get();
    }

	@Override
	/**
     * Return the Resource Type id
     *
     * @return The Resource Type Id
     */
    public String getResourceTypeId( )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }

	@Override
	public ImageResource getImageResource( int nIdResource )
	{
        /*get from cache*/
		String cacheKey = getCacheKey( nIdResource );
		ImageResource imageresource = null;
		if(_cacheFileImage.isCacheEnable() && !_cacheFileImage.isClosed( )) {
			
			 imageresource = _cacheFileImage.get(cacheKey);
		}
		if ( imageresource != null )
		{
			return imageresource;
		}
		else
		{
			/*if no cache*/
			try 
			{
	        File file = _fileStoreService.getFile( String.valueOf( nIdResource ) );
	
	        if ( ( file != null ) && ( file.getPhysicalFile( ) != null ) && FileUtil.hasImageExtension( file.getTitle( ) ) )
	        {
	        	ImageResource imageResource = new ImageResource( file );
	        	if(_cacheFileImage.isCacheEnable() && !_cacheFileImage.isClosed( )) {
	        		_cacheFileImage.put(cacheKey, imageResource);
	        	}
	            return imageResource;
	        }
			}
	        catch ( FileServiceException e )
			{
				AppLogService.error(e);
			}
		}

        return null;
	}
	
	/**
     * Return the Resource id
     *
     * @param iImageResource
     *            The resource identifier
     * @return The New Resource Id
     */
	public String addImageResource( MultipartItem fileItem )
    {
		try 
		{
			return _fileStoreService.storeFileItem( fileItem ) ;
		}
		catch ( FileServiceException e )
		{
			AppLogService.error(e);
			return null;
		}
    }

	public String getName( )
	{
		return IMAGE_RESOURCE_TYPE_ID;
	}
	
	/**
     * get the cache key
     * 
     * @param nId
     * @param user
     * @return the key
     */
    private static String getCacheKey( int nId )
    {
        StringBuilder sbKey = new StringBuilder( );
        return sbKey.append( "[" ).append( IMAGE_RESOURCE_TYPE_ID ).append( ":" ).append( nId )
        		.append( "]" ).toString( );
    }
    /**
     * This method observes the initialization of the {@link ApplicationScoped} context.
     * It ensures that this CDI beans are instantiated at the application startup.
     *
     * <p>This method is triggered automatically by CDI when the {@link ApplicationScoped} context is initialized,
     * which typically occurs during the startup of the application server.</p>
     *
     * @param context the {@link ServletContext} that is initialized. This parameter is observed
     *                and injected automatically by CDI when the {@link ApplicationScoped} context is initialized.
     */
    public void initializedService(@Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        // This method is intentionally left empty to trigger CDI bean instantiation
    }
}