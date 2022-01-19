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
package fr.paris.lutece.portal.web.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Handles asynchronous uploads.
 */
public class UploadServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final String JSON_FILE_SIZE = "fileSize";
    private static final String JSON_FILE_NAME = "fileName";
    private static final String JSON_FILES = "files";
    private static final String JSON_UTF8_CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final ObjectMapper objectMapper = new ObjectMapper( );

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse response ) throws IOException
    {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req;

        List<FileItem> listFileItems = new ArrayList<>( );
        Map<String, Object> mapJson = new HashMap<>( );
        List<Map<String, Object>> listJsonFileMap = new ArrayList<>( );
        mapJson.put( JSON_FILES, listJsonFileMap );

        for ( Entry<String, List<FileItem>> entry : ( request.getFileListMap( ) ).entrySet( ) )
        {
            for ( FileItem fileItem : entry.getValue( ) )
            {
                Map<String, Object> jsonFileMap = new HashMap<>( );
                jsonFileMap.put( JSON_FILE_NAME, fileItem.getName( ) );
                jsonFileMap.put( JSON_FILE_SIZE, fileItem.getSize( ) );

                // add to existing array
                listJsonFileMap.add( jsonFileMap );

                listFileItems.add( fileItem );
            }
        }

        IAsynchronousUploadHandler2 handler2 = getHandler2( request );
        if ( handler2 != null )
        {
            handler2.process( request, response, mapJson, listFileItems );
        }
        else
        {
            AppLogService.error( "No handler found, removing temporary files" );

            for ( FileItem fileItem : listFileItems )
            {
                fileItem.delete( );
            }
        }

        String strResultJson = objectMapper.writeValueAsString( mapJson );
        if ( AppLogService.isDebugEnabled( ) )
        {
            AppLogService.debug( "Aysnchronous upload : " + strResultJson );
        }

        response.setContentType( JSON_UTF8_CONTENT_TYPE );
        response.getWriter( ).print( strResultJson );
    }

    /**
     * Gets the handler implementing the new version IAsynchronousUploadHandler2
     *
     * @param request
     *            the request
     * @return the handler found, <code>null</code> otherwise.
     * @see IAsynchronousUploadHandler2#isInvoked(HttpServletRequest)
     */
    private IAsynchronousUploadHandler2 getHandler2( HttpServletRequest request )
    {
        for ( IAsynchronousUploadHandler2 handler : SpringContextService.getBeansOfType( IAsynchronousUploadHandler2.class ) )
        {
            if ( handler.isInvoked( request ) )
            {
                return handler;
            }
        }

        return null;
    }
}
