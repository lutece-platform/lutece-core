/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;

/**
 * Utility class for files
 */
public final class FileUtil
{
    public static final String CONSTANT_MIME_TYPE_ZIP = "application/zip";
    public static final String CONSTANT_MIME_TYPE_CSV = "application/csv";
    public static final String EXTENSION_ZIP = ".zip";
    public static final String EXTENSION_CSV = ".csv";
    private static final String PROPERTY_ALLOWED_IMAGES_EXTENSIONS = "portal.files.allowedImagesExtentions";
    private static final String PROPERTY_ALLOWED_HTML_EXTENSIONS = "portal.files.allowedHtmlExtentions";
    private static final String DEFAULT_IMAGES_EXTENSION = "gif,png,jpg,jpeg,bmp";
    private static final String DEFAULT_HTML_EXTENSION = "html,htm,xhtml";
    private static final String FREEMARKER_EXTENSION = "ftl";
    private static final String CONSTANT_POINT = ".";
    private static final String CONSTANT_COMMA = ",";

    /**
     * Private constructor
     */
    private FileUtil( )
    {
    }

    /**
     * Check if the extension of the file is an image extension
     * 
     * @param strImageFileName
     *            The file name to check
     * @return True if the extension is correct, false otherwise
     */
    public static boolean hasImageExtension( String strImageFileName )
    {
        String strImagesExtentions = AppPropertiesService.getProperty( PROPERTY_ALLOWED_IMAGES_EXTENSIONS, DEFAULT_IMAGES_EXTENSION );

        return hasExtension( strImageFileName, strImagesExtentions );
    }

    /**
     * Check if a file has a valid html extension
     * 
     * @param strFileName
     *            The file name to check
     * @return True if the file name is valid, false otherwise
     */
    public static boolean hasHtmlExtension( String strFileName )
    {
        String strImagesExtentions = AppPropertiesService.getProperty( PROPERTY_ALLOWED_HTML_EXTENSIONS, DEFAULT_HTML_EXTENSION );

        return hasExtension( strFileName, strImagesExtentions );
    }

    /**
     * Check if a file has a valid Freemarker extension
     * 
     * @param strFileName
     *            The file name to check
     * @return True if the file name is valid, false otherwise
     */
    public static boolean hasFreemarkerExtension( String strFileName )
    {
        return hasExtension( strFileName, FREEMARKER_EXTENSION );
    }

    /**
     * Check if a file name match extensions in a given list
     * 
     * @param strFileName
     *            The file name to check
     * @param strAllowedExtensions
     *            The comma separated list of allowed extensions
     * @return True of the extension of the file exists in the extension list
     */
    private static boolean hasExtension( String strFileName, String strAllowedExtensions )
    {
        int nIndex = strFileName.lastIndexOf( CONSTANT_POINT );

        if ( ( nIndex >= 0 ) && ( strFileName.length( ) > ( nIndex + 2 ) ) )
        {
            String strExtension = strFileName.substring( nIndex + 1 );

            if ( StringUtils.isNotEmpty( strExtension ) )
            {
                for ( String strAllowedExtention : strAllowedExtensions.split( CONSTANT_COMMA ) )
                {
                    if ( StringUtils.equalsIgnoreCase( strExtension, strAllowedExtention ) )
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * creates a zip file
     * 
     * @param zipFile
     *            the zipfile to create
     * @param files
     *            to add to the Zip
     * @throws IOException
     */
    public static void zipFiles( Path zipFile, Path... paths ) throws IOException
    {
        if ( zipFile.toFile( ).exists( ) )
        {
            deleteFile( zipFile.toFile( ) );
        }

        try ( ZipOutputStream zos = new ZipOutputStream( Files.newOutputStream( zipFile ) ) )
        {
            for ( Path file : paths )
            {
                addEntryToZip( zos, file );
            }
        }
    }

    private static void addEntryToZip( ZipOutputStream zos, Path file ) throws IOException
    {
        try ( InputStream fis = Files.newInputStream( file ) )
        {
            ZipEntry zipEntry = new ZipEntry( file.toFile( ).getName( ) );
            zos.putNextEntry( zipEntry );

            byte [ ] bytes = new byte [ 1024];
            int length;
            while ( ( length = fis.read( bytes ) ) >= 0 )
            {
                zos.write( bytes, 0, length );
            }
            zos.closeEntry( );
        }
    }

    /**
     * Converts French diacritics characters into non diacritics. <br />
     * Replace whitespaces by underscores <br />
     * Keep only underscores and alphanum characters <br />
     * Transform to lowercase
     * 
     * @param string
     * @return
     */
    public static final String normalizeFileName( String string )
    {
        return StringUtil.replaceAccent( string ).replace( ' ', '_' ).replaceAll( "[^a-zA-Z0-9_]+", "" ).toLowerCase( );
    }

    /**
     * Delete the file. <br />
     * Logs an error if the delete is not succesful.
     * 
     * @param pathname
     */
    public static void deleteFile( File file )
    {
        if ( file == null )
        {
            AppLogService.error( "Error deleting file, file null" );
            return;
        }
        try
        {
            if ( file.exists( ) )
            {
                Files.delete( file.toPath( ) );
            }
        }
        catch( IOException e )
        {
            AppLogService.error( "Error deleting file", e );
        }
    }

}
