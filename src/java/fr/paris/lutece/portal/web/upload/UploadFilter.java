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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.http.MultipartUtil;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;

import java.io.IOException;

import java.text.DecimalFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Upload filter
 */
public abstract class UploadFilter implements Filter
{
    private static final String PROPERTY_TITLE_FILE_SIZE_LIMIT_EXCEEDED = "portal.util.message.titleDefault";
    private static final String PROPERTY_MESSAGE_FILE_SIZE_LIMIT_EXCEEDED = "portal.util.message.fileSizeLimitExceeded";
    private static final int KILO_BYTE = 1024;
    private static final String SIZE_THRESHOLD = "sizeThreshold";
    private static final String REQUEST_SIZE_MAX = "requestSizeMax";
    private static final String ACTIVATE_NORMALIZE_FILE_NAME = "activateNormalizeFileName";
    private FilterConfig _filterConfig;
    private int _nSizeThreshold = -1;
    private long _nRequestSizeMax = -1;
    private boolean _bActivateNormalizeFileName;

    /**
     * Forward the error message url depends site or admin implementation.
     *
     * @param request The http request
     * @param strMessageKey the str message key
     * @param messageArgs the message args
     * @param strTitleKey the str title key
     * @return Message
     */
    protected abstract String getMessageRelativeUrl( HttpServletRequest request, String strMessageKey,
        Object[] messageArgs, String strTitleKey );

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @param config
     *            The FilterConfig
     * @throws ServletException
     *             The ServletException
     */
    @Override
    public void init( FilterConfig config ) throws ServletException
    {
        _filterConfig = config;

        try
        {
            String paramValue = _filterConfig.getInitParameter( SIZE_THRESHOLD );

            if ( paramValue != null )
            {
                _nSizeThreshold = Integer.parseInt( paramValue );
            }

            paramValue = _filterConfig.getInitParameter( REQUEST_SIZE_MAX );

            if ( paramValue != null )
            {
                _nRequestSizeMax = Long.parseLong( paramValue );
            }

            paramValue = _filterConfig.getInitParameter( ACTIVATE_NORMALIZE_FILE_NAME );

            if ( paramValue != null )
            {
                _bActivateNormalizeFileName = Boolean.valueOf( paramValue );
            }
        }
        catch ( NumberFormatException ex )
        {
            AppLogService.error( ex.getMessage(  ), ex );
            throw new ServletException( ex.getMessage(  ), ex );
        }
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     * @param request
     *            The ServletRequest
     * @param response
     *            The ServletResponse
     * @param chain
     *            The FilterChain
     * @throws IOException
     *             The IOException
     * @throws ServletException
     *             The SerletException
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ( !MultipartUtil.isMultipart( httpRequest ) )
        {
            chain.doFilter( request, response );
        }
        else
        {
            try
            {
                MultipartHttpServletRequest multiHtppRequest = MultipartUtil.convert( _nSizeThreshold,
                        _nRequestSizeMax, _bActivateNormalizeFileName, httpRequest );
                chain.doFilter( multiHtppRequest, response );
            }
            catch ( SizeLimitExceededException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                Object[] args = { getDisplaySize(  ) };
                ( (HttpServletResponse) response ).sendRedirect( getMessageRelativeUrl( httpRequest,
                        PROPERTY_MESSAGE_FILE_SIZE_LIMIT_EXCEEDED, args, PROPERTY_TITLE_FILE_SIZE_LIMIT_EXCEEDED ) );
            }
            catch ( FileUploadException e )
            {
                AppLogService.error( e.getMessage(  ), e );
                throw new ServletException( "Unkown error occured during the upload", e );
            }
        }
    }

    /**
     * Get the max size of upload file
     *
     * @return The max size
     */
    public long getRequestSizeMax(  )
    {
        return _nRequestSizeMax;
    }

    /**
     * Default implementation for subclasses
     */
    @Override
    public void destroy(  )
    {
        // Do nothing
    }

    /**
     *
     * @return the size of the request to display in the error message
     */
    private String getDisplaySize(  )
    {
        long lSizeMax = getRequestSizeMax(  );
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(  );
        decimalFormat.applyPattern( "#" );

        String strMessage = ( lSizeMax >= KILO_BYTE ) ? ( String.valueOf( lSizeMax / KILO_BYTE ) )
                                                      : ( decimalFormat.format( lSizeMax / KILO_BYTE ) );

        return strMessage;
    }
}
