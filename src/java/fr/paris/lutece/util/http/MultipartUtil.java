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
package fr.paris.lutece.util.http;

import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.html.XSSSanitizerException;
import fr.paris.lutece.portal.service.html.XSSSanitizerService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.filesystem.UploadUtil;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.core.FileUploadFileCountLimitException;
import org.apache.commons.fileupload2.core.RequestContext;
import org.apache.commons.fileupload2.jakarta.JakartaFileCleaner;
import org.apache.commons.fileupload2.jakarta.JakartaServletDiskFileUpload;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import org.apache.commons.fileupload2.jakarta.JakartaServletRequestContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

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
     * @param request
     *            the HTTP request
     * @return true if it has multipart content, false otherwise
     */
    public static boolean isMultipart( HttpServletRequest request )
    {
        return JakartaServletFileUpload.isMultipartContent( request );
    }

    /**
     * Convert a HTTP request to a {@link MultipartHttpServletRequest}
     *
     * @param nSizeThreshold
     *            the size threshold
     * @param nRequestSizeMax
     *            the request size max
     * @param bActivateNormalizeFileName
     *            true if the file name must be normalized, false otherwise
     * @param request
     *            the HTTP request
     * @return a {@link MultipartHttpServletRequest}, null if the request does not have a multipart content
     * @throws FileUploadException
     *             exception if an unknown error has occurred
     */
    public static MultipartHttpServletRequest convert( int nSizeThreshold, long nRequestSizeMax, boolean bActivateNormalizeFileName,

            HttpServletRequest request, boolean isXssSanitize ) throws FileUploadException
    {
        if ( !isMultipart( request ) )
        {
            return null;
        }

        // Create a factory for disk-based file items     
          DiskFileItemFactory fileItemFactory = DiskFileItemFactory.builder()
                .setBufferSize(nSizeThreshold)
                .setCharset(Optional.ofNullable( request.getCharacterEncoding( ) ).orElse( EncodingService.getEncoding( ) ))
                .setFileCleaningTracker(JakartaFileCleaner.getFileCleaningTracker(request.getServletContext( )))
                .get();
        final var servletFileUpload = new JakartaServletDiskFileUpload(fileItemFactory);

        // Set overall request size constraint
        servletFileUpload.setSizeMax( nRequestSizeMax );

        Map<String, List<FileItem<DiskFileItem>>> mapFiles = new HashMap<>( );
        Map<String, String [ ]> mapParameters = new HashMap<>( );

        List<DiskFileItem> listItems = parseRequest( new JakartaServletRequestContext(request), fileItemFactory, servletFileUpload, bActivateNormalizeFileName);
        // Process the uploaded items
        for ( DiskFileItem item : listItems )
        {
        	 processItem( item, mapFiles, mapParameters, isXssSanitize );
        }

        return new MultipartHttpServletRequest( request, mapFiles, mapParameters );
    }

    private static void processItem( DiskFileItem item, Map<String, List<FileItem<DiskFileItem>>> mapFiles,
            Map<String, String [ ]> mapParameters, boolean isXssSanitize ) throws FileUploadException
        {
        if ( item.isFormField( ) )
        {
            String strValue = StringUtils.EMPTY;

            if ( item.getSize( ) > 0 )
            {
                    strValue = item.getString(  );                
            }


            if ( isXssSanitize ) 
            {
        	try
				{
				    strValue = XSSSanitizerService.sanitize( strValue );
				} 
		        	catch ( XSSSanitizerException e )
				{
				    AppLogService.error( "XSS Sanitize Service Error", e );
				} 
            }
            
            // check if item of same name already in map
            String [ ] curParam = mapParameters.get( item.getFieldName( ) );

            if ( curParam == null )
            {
                // simple form field
                mapParameters.put( item.getFieldName( ), new String [ ] {
                        strValue
                } );
            }
            else
            {
                // array of simple form fields
                String [ ] newArray = new String [ curParam.length + 1];
                System.arraycopy( curParam, 0, newArray, 0, curParam.length );
                newArray [curParam.length] = strValue;
                mapParameters.put( item.getFieldName( ), newArray );
            }
        }
        else
        {
            var listFileItem = mapFiles.get( item.getFieldName( ) );

            if ( listFileItem != null )
            {
                listFileItem.add( item );
            }
            else
            {
                listFileItem = new ArrayList<>( 1 );
                listFileItem.add( item );
                mapFiles.put( item.getFieldName( ), listFileItem );
            }
        }
    }    
    /**
     * Parses an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a> compliant {@code multipart/form-data} stream.
     *
     * @param requestContext The context for the request to be parsed.
     * @return A list of {@code FileItem} instances parsed from the request, in the order that they were transmitted.
     * @throws FileUploadException if there are problems reading/parsing the request or storing files.
     */
    private static List<DiskFileItem> parseRequest(final RequestContext requestContext, final DiskFileItemFactory fileItemFactory, final JakartaServletFileUpload<DiskFileItem,DiskFileItemFactory> servletUpload, boolean bActivateNormalizeFileName) throws FileUploadException {
        final  List<DiskFileItem> itemList = new ArrayList<>();
        var successful = false;
        try {
            final var buffer = new byte[IOUtils.DEFAULT_BUFFER_SIZE];
            servletUpload.getItemIterator(requestContext).forEachRemaining(fileItemInput -> {
                if (itemList.size() == servletUpload.getFileCountMax( )) {
                    // The next item will exceed the limit.
                    throw new FileUploadFileCountLimitException("attachment", servletUpload.getFileCountMax( ), itemList.size());
                }
                // Don't use getName() here to prevent an InvalidFileNameException.
                // @formatter:off
                final String fileName= fileItemInput.getName();
                final var fileItem = fileItemFactory.fileItemBuilder()
                    .setFieldName(fileItemInput.getFieldName())
                    .setContentType(fileItemInput.getContentType())
                    .setFormField(fileItemInput.isFormField())
                    //if the parameter filter ActivateNormalizeFileName is set to true all file name will be normalize
                    .setFileName( bActivateNormalizeFileName && fileName != null ? UploadUtil.cleanFileName( FilenameUtils.getName( fileName ) ):fileName)
                    .setFileItemHeaders(fileItemInput.getHeaders())
                    .get();
                // @formatter:on
                itemList.add(fileItem);
                try (var inputStream = fileItemInput.getInputStream();
                        var outputStream = fileItem.getOutputStream()) {
                    IOUtils.copyLarge(inputStream, outputStream, buffer);
                } catch (final FileUploadException e) {
                    throw e;
                } catch (final IOException e) {
                    throw new FileUploadException(String.format("Processing of multipart/\form request failed. %s", e.getMessage()), e);
                }
            });
            successful = true;
            return itemList;
        } catch (final FileUploadException e) {
            throw e;
        } catch (final IOException e) {
            throw new FileUploadException(e.getMessage(), e);
        } finally {
            if (!successful) {
                for (final  DiskFileItem fileItem : itemList) {
                    try {
                        fileItem.delete();
                    } catch (final Exception ignored) {
                        AppLogService.error( ignored.getMessage(), ignored );
                    }
                }
            }
        }
    }
}
