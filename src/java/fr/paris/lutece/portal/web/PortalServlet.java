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
package fr.paris.lutece.portal.web;

import java.io.IOException;

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.PageNotFoundException;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.upload.AbstractSiteMultipartServlet;
import fr.paris.lutece.util.http.MultipartUtil;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PortalServlet extends AbstractSiteMultipartServlet
{
    private static final String EXTERNAL_AUTHENTICATION_ERROR_MESSAGE = "<center>"
            + "<br />"
            + "<br />"
            + "<h3>"
            + "Error : This page requires an authenticated user identified by an external service but no user is available."
            + "</h3>"
            + "</center>";

    @Inject
    private PortalJspBean _portalJspBean;

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if ( MultipartUtil.isMultipart( httpRequest ) )
        {
            httpRequest = handleRequest( httpRequest, response ); // _multipartHandler.handle( httpRequest, _bActivateNormalizeFileName );
            if ( null == httpRequest )
            {
                return;
            }
        }

        LocalVariables.setLocal( getServletConfig( ), request, response );
        try
        {
            response.setHeader( "Cache-Control", "no-cache" ); // HTTP 1.1
            response.setDateHeader( "Expires", 0 ); // prevents caching at the proxy server
            response.setContentType( "text/html" );
            response.getWriter( ).println( _portalJspBean.getContent( httpRequest ) );
            response.flushBuffer( );
        }
        catch( UserNotSignedException e )
        {
            if ( SecurityService.getInstance( ).isExternalAuthentication( ) &&
                    !SecurityService.getInstance( ).isMultiAuthenticationSupported( ) )
            {
                response.getWriter( ).println( EXTERNAL_AUTHENTICATION_ERROR_MESSAGE );
                response.flushBuffer( );
            }
            else
            {
                response.sendRedirect( PortalJspBean.redirectLogin( httpRequest ) );
            }
        }
        catch( SiteMessageException e )
        {
            response.sendRedirect( AppPathService.getSiteMessageUrl( httpRequest ) );
        }
        catch( PageNotFoundException e )
        {
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
        finally
        {
            LocalVariables.setLocal( null, null, null );
        }
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        doPost( request, response );
    }

}
