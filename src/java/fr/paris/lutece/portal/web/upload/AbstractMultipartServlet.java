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
package fr.paris.lutece.portal.web.upload;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import fr.paris.lutece.portal.service.upload.MultipartHandler;
import fr.paris.lutece.portal.service.upload.MultipartUploadHandler;
import fr.paris.lutece.portal.service.util.AppLogService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractMultipartServlet extends HttpServlet
{
    private static final String PROPERTY_TITLE_FILE_SIZE_LIMIT_EXCEEDED = "portal.util.message.titleDefault";
    private static final String PROPERTY_MESSAGE_FILE_SIZE_LIMIT_EXCEEDED = "portal.util.message.fileSizeLimitExceeded";
    private static final int KILO_BYTE = 1024;
    private static final String ACTIVATE_NORMALIZE_FILE_NAME = "activateNormalizeFileName";
    private boolean _bActivateNormalizeFileName;

    @Inject
    @MultipartUploadHandler
    protected MultipartHandler _multipartHandler;

    @Override
    public void init( ServletConfig config ) throws ServletException
    {
        String paramValue = config.getInitParameter( ACTIVATE_NORMALIZE_FILE_NAME );

        if ( paramValue != null )
        {
            _bActivateNormalizeFileName = Boolean.valueOf( paramValue );
        }
    }

    protected MultipartHttpServletRequest handleRequest( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        return handleRequest( request, response, _multipartHandler );
    }

    protected MultipartHttpServletRequest handleRequest( HttpServletRequest request, HttpServletResponse response, MultipartHandler multipartHandler )
            throws ServletException, IOException
    {
        try
        {
            return multipartHandler.handle( request, _bActivateNormalizeFileName );
        }
        catch( IllegalStateException e )
        {
            AppLogService.error( e.getMessage( ), e );

            Object [ ] args = {
                    getDisplaySize( )
            };
            ( (HttpServletResponse) response )
                    .sendRedirect( getMessageRelativeUrl( request, PROPERTY_MESSAGE_FILE_SIZE_LIMIT_EXCEEDED, args, PROPERTY_TITLE_FILE_SIZE_LIMIT_EXCEEDED ) );

        }
        return null;
    }

    /**
     * Forward the error message url depends site or admin implementation.
     *
     * @param request
     *            The http request
     * @param strMessageKey
     *            the str message key
     * @param messageArgs
     *            the message args
     * @param strTitleKey
     *            the str title key
     * @return Message
     */
    protected abstract String getMessageRelativeUrl( HttpServletRequest request, String strMessageKey, Object [ ] messageArgs, String strTitleKey );

    /**
     *
     * @return the size of the request to display in the error message
     */
    private String getDisplaySize( )
    {
        long lSizeMax = 1024;
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance( );
        decimalFormat.applyPattern( "#" );

        return ( lSizeMax >= KILO_BYTE ) ? ( String.valueOf( lSizeMax / KILO_BYTE ) ) : ( decimalFormat.format( lSizeMax / KILO_BYTE ) );
    }

}
