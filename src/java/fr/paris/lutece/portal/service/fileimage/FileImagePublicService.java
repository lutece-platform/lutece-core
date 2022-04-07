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

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.file.FileService;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.util.file.FileUtil;

public class FileImagePublicService extends AbstractCacheableService implements ImageResourceProvider 
{
	private static FileImagePublicService _singleton = new FileImagePublicService( );
	public static final String IMAGE_RESOURCE_TYPE_ID = "public_image_resource";
	private static final IFileStoreServiceProvider _fileStoreService = FileService.getInstance( ).getFileStoreServiceProvider( );
	
	/**
     * Creates a new instance of FileImgService
     */
    private FileImagePublicService( )
    {
    	initCache();
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
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static FileImagePublicService getInstance( )
    {
        return _singleton;
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
		ImageResource imageresource = (ImageResource) getFromCache( cacheKey );
		if ( imageresource != null )
		{
			return imageresource;
		}
		else
		{
			/*if no cache*/
	        File file = _fileStoreService.getFile( String.valueOf( nIdResource ) );
	
	        if ( ( file != null ) && ( file.getPhysicalFile( ) != null ) && FileUtil.hasImageExtension( file.getTitle( ) ) )
	        {
	        	ImageResource imageResource = new ImageResource( file );
	        	putInCache( cacheKey, imageResource);
	            return imageResource;
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
	public String addImageResource( FileItem fileItem )
    {
        return _fileStoreService.storeFileItem( fileItem ) ;
    }

	@Override
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
}