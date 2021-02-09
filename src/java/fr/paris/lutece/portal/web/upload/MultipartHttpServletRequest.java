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
package fr.paris.lutece.portal.web.upload;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;

/**
 * This class provides a Wrapper of an HTTP request that handle multipart content
 */
public class MultipartHttpServletRequest extends HttpServletRequestWrapper
{
    private final Map<String, List<FileItem>> _multipartFiles;
    private final Map<String, String [ ]> _stringParameters;

    /**
     * Constructor
     * 
     * @param request
     *            The HTTP request
     * @param multipartFiles
     *            Files
     * @param parameters
     *            Request parameters
     */
    public MultipartHttpServletRequest( HttpServletRequest request, Map<String, List<FileItem>> multipartFiles, Map<String, String [ ]> parameters )
    {
        super( request );
        _multipartFiles = Collections.unmodifiableMap( multipartFiles );
        _stringParameters = Collections.unmodifiableMap( parameters );
    }

    /**
     * Gets parameters names
     * 
     * @return An enumeration of parameters names
     */
    @Override
    public Enumeration getParameterNames( )
    {
        return Collections.enumeration( _stringParameters.keySet( ) );
    }

    /**
     * Gets a parameter value
     * 
     * @param strName
     *            The parameter name
     * @return The value
     */
    @Override
    public String getParameter( String strName )
    {
        String [ ] values = getParameterValues( strName );

        return ( ( ( values != null ) && ( values.length > 0 ) ) ? values [0] : null );
    }

    /**
     * Gets parameter values
     * 
     * @param strName
     *            The parameter name
     * @return An array of values
     */
    @Override
    public String [ ] getParameterValues( String strName )
    {
        return _stringParameters.get( strName );
    }

    /**
     * Gets the parameter map
     * 
     * @return A map containing all request parameters
     */
    @Override
    public Map getParameterMap( )
    {
        return _stringParameters;
    }

    /**
     * Gets the list of filenames attached to the request
     * 
     * @return The list as an enumeration
     */
    public Enumeration<String> getFileNames( )
    {
        return Collections.enumeration( _multipartFiles.keySet( ) );
    }

    /**
     * Gets a map of all files attached to the request
     * 
     * @return The map
     */
    public Map<String, List<FileItem>> getFileListMap( )
    {
        return _multipartFiles;
    }

    /**
     * Gets a file. If several files are available for a given name, then only the first one is returned
     * 
     * @param strName
     *            The file name
     * @return The file as a FileItem
     */
    public FileItem getFile( String strName )
    {
        List<FileItem> listFileItem = _multipartFiles.get( strName );

        return CollectionUtils.isNotEmpty( listFileItem ) ? listFileItem.get( 0 ) : null;
    }

    /**
     * Gets a file
     * 
     * @param strName
     *            The file name
     * @return The file as a FileItem
     */
    public List<FileItem> getFileList( String strName )
    {
        return _multipartFiles.get( strName );
    }
}
