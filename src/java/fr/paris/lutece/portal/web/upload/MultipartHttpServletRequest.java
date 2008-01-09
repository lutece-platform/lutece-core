/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import org.apache.commons.fileupload.FileItem;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class MultipartHttpServletRequest extends HttpServletRequestWrapper
{
    private final Map<String, FileItem> _multipartFiles;
    private final Map<String, String[]> _stringParameters;

    public MultipartHttpServletRequest( HttpServletRequest request, Map<String, FileItem> multipartFiles,
        Map<String, String[]> parameters )
    {
        super( request );
        _multipartFiles = Collections.unmodifiableMap( multipartFiles );
        _stringParameters = Collections.unmodifiableMap( parameters );
    }

    public Enumeration getParameterNames(  )
    {
        return Collections.enumeration( _stringParameters.keySet(  ) );
    }

    public String getParameter( String strName )
    {
        String[] values = getParameterValues( strName );

        return ( ( ( values != null ) && ( values.length > 0 ) ) ? values[0] : null );
    }

    public String[] getParameterValues( String strName )
    {
        return (String[]) _stringParameters.get( strName );
    }

    public Map getParameterMap(  )
    {
        return _stringParameters;
    }

    public Enumeration getFileNames(  )
    {
        return Collections.enumeration( _multipartFiles.keySet(  ) );
    }

    public Map getFileMap(  )
    {
        return _multipartFiles;
    }

    public FileItem getFile( String strName )
    {
        return _multipartFiles.get( strName );
    }
}
