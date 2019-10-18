/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.util.http;

import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.portal.web.upload.NormalizeFileItem;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * MultipartUtil
 *
 */
public final class MultipartUtil
{
    /**
     * Private constructor
     */
    private MultipartUtil( )
    {
    }

    /**
     * Check if the given HTTP request has multipart content
     * 
     * @param request the HTTP request
     * @return true if it has multipart content, false otherwise
     */
    public static boolean isMultipart( HttpServletRequest request )
    {
        return ServletFileUpload.isMultipartContent( request );
    }

    /**
     * Convert a HTTP request to a {@link MultipartHttpServletRequest}
     * 
     * @param nSizeThreshold             the size threshold
     * @param nRequestSizeMax            the request size max
     * @param bActivateNormalizeFileName true if the file name must be normalized,
     *                                   false otherwise
     * @param request                    the HTTP request
     * @return a {@link MultipartHttpServletRequest}, null if the request does not
     *         have a multipart content
     * @throws SizeLimitExceededException exception if the file size is too big
     * @throws FileUploadException        exception if an unknown error has occurred
     */
    public static MultipartHttpServletRequest convert( int nSizeThreshold, long nRequestSizeMax,
            boolean bActivateNormalizeFileName, HttpServletRequest request ) throws FileUploadException
    {
        if ( !isMultipart( request ) )
        {
            return null;
        }

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory( );

        // Set factory constraints
        factory.setSizeThreshold( nSizeThreshold );

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload( factory );

        // Set overall request size constraint
        upload.setSizeMax( nRequestSizeMax );

        // get encoding to be used
        String strEncoding = Optional.ofNullable( request.getCharacterEncoding( ) )
                .orElse( EncodingService.getEncoding( ) );

        Map<String, List<FileItem>> mapFiles = new HashMap<>( );
        Map<String, String[]> mapParameters = new HashMap<>( );

        List<FileItem> listItems = upload.parseRequest( request );

        // Process the uploaded items
        for ( FileItem item : listItems )
        {
            processItem( item, strEncoding, bActivateNormalizeFileName, mapFiles, mapParameters );
        }

        return new MultipartHttpServletRequest( request, mapFiles, mapParameters );
    }

    private static void processItem( FileItem item, String strEncoding, boolean bActivateNormalizeFileName,
            Map<String, List<FileItem>> mapFiles, Map<String, String[]> mapParameters )
    {
        if ( item.isFormField( ) )
        {
            String strValue = StringUtils.EMPTY;

            if ( item.getSize( ) > 0 )
            {
                try
                {
                    strValue = item.getString( strEncoding );
                }
                catch ( UnsupportedEncodingException ex )
                {
                    // if encoding problem, try with system encoding
                    strValue = item.getString( );
                }
            }

            // check if item of same name already in map
            String[] curParam = mapParameters.get( item.getFieldName( ) );

            if ( curParam == null )
            {
                // simple form field
                mapParameters.put( item.getFieldName( ), new String[]
                { strValue } );
            }
            else
            {
                // array of simple form fields
                String[] newArray = new String[curParam.length + 1];
                System.arraycopy( curParam, 0, newArray, 0, curParam.length );
                newArray[curParam.length] = strValue;
                mapParameters.put( item.getFieldName( ), newArray );
            }
        }
        else
        {
            // multipart file field, if the parameter filter ActivateNormalizeFileName is
            // set to true
            // all file name will be normalize
            FileItem fileItem = bActivateNormalizeFileName ? new NormalizeFileItem( item ) : item;
            List<FileItem> listFileItem = mapFiles.get( fileItem.getFieldName( ) );

            if ( listFileItem != null )
            {
                listFileItem.add( fileItem );
            }
            else
            {
                listFileItem = new ArrayList<>( 1 );
                listFileItem.add( fileItem );
                mapFiles.put( fileItem.getFieldName( ), listFileItem );
            }
        }
    }
}
