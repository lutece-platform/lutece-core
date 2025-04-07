/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.service.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public abstract class AbstractMultipartHandler implements MultipartHandler
{

    private static final int DEFAULT_BUFFER_SIZE = 10240;
    private static final String DEFAULT_ENCODING = "UTF-8";

    public MultipartHttpServletRequest handle( HttpServletRequest request, boolean bActivateNormalizeFileName ) throws IOException, ServletException
    {
        String encoding = null != request.getCharacterEncoding( ) ? request.getCharacterEncoding( ) : DEFAULT_ENCODING;

        Map<String, List<MultipartItem>> mapFiles = new HashMap<>( );
        Map<String, String [ ]> mapParameters = new HashMap<>( );
        for ( Part part : request.getParts( ) )
        {
            if ( null != part.getSubmittedFileName( ) )
            {
                processFilePart( part, mapFiles, part.getSubmittedFileName( ), bActivateNormalizeFileName );
            }
            else
            {
                processTextPart( part, mapParameters, encoding );
            }

        }

        return new MultipartHttpServletRequest( request, mapFiles, mapParameters );
    }

    protected abstract void processFilePart( Part part, Map<String, List<MultipartItem>> mapFiles, String filename, boolean bActivateNormalizeFileName )
            throws IOException;

    protected void processTextPart( Part part, Map<String, String [ ]> mapParameters, String encoding ) throws IOException
    {
        String strName = part.getName( );
        String [ ] values = mapParameters.get( strName );

        if ( values == null )
        {
            // Not in parameter map yet, so add as new value.
            mapParameters.put( strName, new String [ ] {
                    getValue( part, encoding )
            } );
        }
        else
        {
            // Multiple field values, so add new value to existing array.
            int length = values.length;
            String [ ] newValues = new String [ length + 1];
            System.arraycopy( values, 0, newValues, 0, length );
            newValues [length] = getValue( part, encoding );
            mapParameters.put( strName, newValues );
        }
    }

    private String getValue( Part part, String encoding ) throws IOException
    {
        BufferedReader reader = new BufferedReader( new InputStreamReader( part.getInputStream( ), encoding ) );
        StringBuilder value = new StringBuilder( );
        char [ ] buffer = new char [ DEFAULT_BUFFER_SIZE];
        for ( int length = 0; ( length = reader.read( buffer ) ) > 0; )
        {
            value.append( buffer, 0, length );
        }
        return value.toString( );
    }
}
