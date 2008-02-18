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

import fr.paris.lutece.portal.service.html.EncodingService;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * A rewrite of the multipart filter from the com.oreilly.servlet package. The
 * rewrite allows us to use initialization parameters specified in the Lutece
 * configuration files.
 * Abstract method for upload filters (front and back office)
 */
public abstract class UploadFilter implements Filter
{
    private FilterConfig _filterConfig;
    private int _nSizeThreshold = -1;
    private long _nRequestSizeMax = -1;
    
    /**
     * Forward the error message url when file is bigger than the max size authorized
     * @param request The http request
     * @return Message
     */
    protected abstract String getMessageRelativeUrl( HttpServletRequest request );
    
    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @param config The FilterConfig
     * @throws ServletException The ServletException
     */
    public void init( FilterConfig config ) throws ServletException
    {
        _filterConfig = config;
        
        try
        {
            String paramValue = _filterConfig.getInitParameter( "sizeThreshold" );
            
            if ( paramValue != null )
            {
                _nSizeThreshold = Integer.parseInt( paramValue );
            }
            
            paramValue = _filterConfig.getInitParameter( "requestSizeMax" );
            
            if ( paramValue != null )
            {
                _nRequestSizeMax = Long.parseLong( paramValue );
            }
        }
        catch ( NumberFormatException ex )
        {
            ServletException servletEx = new ServletException( ex.getMessage(  ) );
            servletEx.initCause( ex );
            throw servletEx;
        }
    }
    
    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy(  )
    {
        // Do nothing
    }
    
    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     * @param request The ServletRequest
     * @param response The ServletResponse
     * @param chain The FilterChain
     * @throws IOException The IOException
     * @throws ServletException The SerletException
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
    throws IOException, ServletException
    {
        if ( !( request instanceof HttpServletRequest ) )
        {
            chain.doFilter( request, response );
            
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        boolean isMultipartContent = ServletFileUpload.isMultipartContent( httpRequest );
        
        if ( !isMultipartContent )
        {
            chain.doFilter( request, response );
        }
        else
        {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory(  );
            
            // Set factory constraints
            factory.setSizeThreshold( _nSizeThreshold );
            
            /* TODO
            if ( _repositoryPath != null )
            {
                factory.setRepository( _repositoryPath );
            }
             */
            
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload( factory );
            
            // Set overall request size constraint
            upload.setSizeMax( _nRequestSizeMax );
            
            // get encoding to be used
            String strEncoding = httpRequest.getCharacterEncoding(  );
            
            if ( strEncoding == null )
            {
                strEncoding = EncodingService.getEncoding(  );
            }
            
            Map<String, FileItem> mapFiles = new HashMap<String, FileItem>(  );
            Map<String, String[]> mapParameters = new HashMap<String, String[]>(  );
            
            try
            {
                List<FileItem> listItems = upload.parseRequest( httpRequest );
                
                // Process the uploaded items
                for ( FileItem item : listItems )
                {
                    if ( item.isFormField(  ) )
                    {
                        String strValue = null;
                        
                        try
                        {
                            strValue = item.getString( strEncoding );
                        }
                        catch ( UnsupportedEncodingException ex )
                        {
                            // if encoding problem, try with system encoding
                            strValue = item.getString(  );
                        }
                        
                        // check if item of same name already in map
                        String[] curParam = mapParameters.get( item.getFieldName(  ) );
                        
                        if ( curParam == null )
                        {
                            // simple form field
                            mapParameters.put( item.getFieldName(  ), new String[] { strValue } );
                        }
                        else
                        {
                            // array of simple form fields
                            String[] newArray = new String[curParam.length + 1];
                            System.arraycopy( curParam, 0, newArray, 0, curParam.length );
                            newArray[curParam.length] = strValue;
                            mapParameters.put( item.getFieldName(  ), newArray );
                        }
                    }
                    else
                    {
                        // multipart file field
                        mapFiles.put( item.getFieldName(  ), item );
                    }
                }
                
                MultipartHttpServletRequest multiHtppRequest = new MultipartHttpServletRequest( httpRequest, mapFiles,
                    mapParameters );
                chain.doFilter( multiHtppRequest, response );
            }
            catch ( FileUploadException e )
            {
                if ( e instanceof SizeLimitExceededException )
                {
                    request.getRequestDispatcher( "/" + getMessageRelativeUrl( httpRequest ) ).forward( request,
                        response );
                }
                else
                {
                    throw new ServletException( "Unkown error occured during the upload" );
                }
            }
        }
    }
    
    /**
     * Get the max size of upload file
     * @return The max size
     */
    public long getRequestSizeMax(  )
    {
        return _nRequestSizeMax;
    }
}
