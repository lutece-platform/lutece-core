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
package fr.paris.lutece.portal.web.upload;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse response )
        throws ServletException, IOException
    {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req;

        List<FileItem> listFileItems = new ArrayList<FileItem>(  );
        JSONObject json = new JSONObject(  );
        json.element( JSON_FILES, new JSONArray(  ) );

        for ( Entry<String, List<FileItem>> entry : ( request.getFileListMap(  ) ).entrySet(  ) )
        {
            for ( FileItem fileItem : entry.getValue(  ) )
            {
                JSONObject jsonFile = new JSONObject(  );
                jsonFile.element( JSON_FILE_NAME, fileItem.getName(  ) );
                jsonFile.element( JSON_FILE_SIZE, fileItem.getSize(  ) );

                // add to existing array
                json.accumulate( JSON_FILES, jsonFile );

                listFileItems.add( fileItem );
            }
        }

        IAsynchronousUploadHandler handler = getHandler( request );

        if ( handler == null )
        {
            AppLogService.error( "No handler found, removing temporary files" );

            for ( FileItem fileItem : listFileItems )
            {
                fileItem.delete(  );
            }
        }
        else
        {
            handler.process( request, response, json, listFileItems );
        }

        if ( AppLogService.isDebugEnabled(  ) )
        {
            AppLogService.debug( "Aysnchronous upload : " + json.toString(  ) );
        }

        response.setContentType(JSON_UTF8_CONTENT_TYPE);
        response.getWriter().print( json.toString(  ) );
    }

    /**
     * Gets the handler
     * @param request the reques
     * @return the handler found, <code>null</code> otherwise.
     * @see IAsynchronousUploadHandler#isInvoked(HttpServletRequest)
     */
    private IAsynchronousUploadHandler getHandler( HttpServletRequest request )
    {
        for ( IAsynchronousUploadHandler handler : SpringContextService.getBeansOfType( 
                IAsynchronousUploadHandler.class ) )
        {
            if ( handler.isInvoked( request ) )
            {
                return handler;
            }
        }

        return null;
    }
}
