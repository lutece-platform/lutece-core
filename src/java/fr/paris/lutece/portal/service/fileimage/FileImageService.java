/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.init.LuteceInitException;


/**
 * Service for Url entry types. Provide ImageResource management
 */
public final class FileImageService implements ImageResourceProvider
{
    private static FileImageService _singleton = new FileImageService(  );
    private static final String IMAGE_RESOURCE_TYPE_ID = "core_attribute_img";

    /**
     * Creates a new instance of FileImgService
     */
    private FileImageService(  )
    {
    }

    /**
     * Init
     * @throws LuteceInitException if an error occurs
     */
    public static synchronized void init(  ) throws LuteceInitException
    {
        getInstance(  ).register(  );
    }

    /**
     * Initializes the service
     */
    public void register(  )
    {
        ImageResourceManager.registerProvider( this );
    }

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static FileImageService getInstance(  )
    {
        return _singleton;
    }

    /**
    * Return the Resource id
    * @param nIdResource The resource identifier
    * @return The Resource Image
    */
    public ImageResource getImageResource( int nIdResource )
    {
        File file = FileHome.findByPrimaryKey( nIdResource );
        PhysicalFile physicalFile = ( file.getPhysicalFile(  ) != null )
            ? PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile(  ).getIdPhysicalFile(  ) ) : null;

        if ( physicalFile != null )
        {
            ImageResource imageResource = new ImageResource(  );
            imageResource.setImage( physicalFile.getValue(  ) );
            imageResource.setMimeType( file.getMimeType(  ) );

            return imageResource;
        }

        return null;
    }

    /**
     * Return the Resource Type id
     * @return The Resource Type Id
     */
    public String getResourceTypeId(  )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }
}
