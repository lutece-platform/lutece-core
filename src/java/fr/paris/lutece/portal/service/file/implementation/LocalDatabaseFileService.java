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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;
import fr.paris.lutece.portal.service.file.IFileStoreService;
import fr.paris.lutece.portal.service.upload.MultipartItem;
import fr.paris.lutece.portal.service.util.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * 
 * Database file Service.
 * 
 */
@ApplicationScoped
@Named( "localDatabaseFileService" )
public class LocalDatabaseFileService implements IFileStoreService
{
    private static final long serialVersionUID = 1L;

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
    public File getFile( String strKey, String strProviderName )
    {
        return getFile( strKey, true, strProviderName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getFileMetaData( String strKey, String strProviderName )
    {
        return getFile( strKey, false, strProviderName );
    }

    /**
     * get file from database
     * 
     * @param strKey
     * @param withPhysicalFile
     * 
     * @return the file with the physical file content if withPhysicalFile is true, null otherwise
     */
    public File getFile( String strKey, boolean withPhysicalFile, String strProviderName )
    {
        if ( StringUtils.isNotBlank( strKey ) )
        {
            int nfileId = Integer.parseInt( strKey );

            // get meta data
            File file = FileHome.findByPrimaryKey( nfileId );

            // check if the file exists and was inserted with this provider
            if ( file == null || ( file.getOrigin( ) == null && strProviderName != null )
                    || ( file.getOrigin( ) != null && !file.getOrigin( ).equals( strProviderName ) ) )
            {
                return null;
            }

            if ( withPhysicalFile )
            {
                // get file content
                file.setPhysicalFile( PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile( ).getIdPhysicalFile( ) ) );
            }

            return file;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeBytes( byte [ ] blob, String strProviderName )
    {
        File file = new File( );
        PhysicalFile physicalFile = new PhysicalFile( );
        physicalFile.setValue( blob );
        file.setPhysicalFile( physicalFile );
        file.setOrigin( strProviderName );

        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeInputStream( InputStream inputStream, String strProviderName )
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

        file.setOrigin( strProviderName );

        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String storeFileItem( MultipartItem fileItem, String strProviderName )
    {

        File file = new File( );
        file.setTitle( fileItem.getName( ) );
        file.setSize( (int) fileItem.getSize( ) );
        file.setMimeType( fileItem.getContentType( ) );

        file.setOrigin( strProviderName );

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
    public String storeFile( File file, String strProviderName )
    {
        file.setOrigin( strProviderName );

        int nFileId = FileHome.create( file );

        return String.valueOf( nFileId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream( String strKey, String strProviderName )
    {

        File file = getFile( strKey, strProviderName );

        return new ByteArrayInputStream( file.getPhysicalFile( ).getValue( ) );
    }
}
