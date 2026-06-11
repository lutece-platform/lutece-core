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
package fr.paris.lutece.portal.web.admin;

import java.io.IOException;
import java.util.Optional;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.MvcControllerRegistry;
import fr.paris.lutece.portal.util.mvc.admin.PageFrameService;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Admin MVC front-controller servlet.
 * <p>
 * Single entry point mapped on {@code /jsp/admin/mvc/*}. It resolves the target controller bean from
 * its route name (the trailing path segment), delegates the request to {@link MVCAdminJspBean#processController},
 * and either lets the action commit its own redirect or wraps the view content into the full admin page.
 * Being mapped under {@code /jsp/admin/*}, it inherits the existing admin filter chain (authentication,
 * multipart, XSS, CSRF token, encoding); no security logic is duplicated here.
 * </p>
 */
public class AdminMvcServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_ACCESS_DENIED = "portal.util.message.accessDenied";
    private static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";

    /**
     * Handles HTTP GET requests, typically used to render views.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws IOException if writing the response fails
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        process( request, response );
    }

    /**
     * Handles HTTP POST requests, typically used to submit actions.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws IOException if writing the response fails
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        process( request, response );
    }

    /**
     * Resolves the controller from the request path, dispatches it and renders the result.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws IOException if writing the response fails
     */
    private void process( HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        String strName = extractRouteName( request );

        if ( strName.isEmpty( ) )
        {
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        Optional<Bean<?>> bean = CDI.current( ).select( MvcControllerRegistry.class ).get( ).resolve( strName );

        if ( bean.isEmpty( ) )
        {
            AppLogService.error( "No admin MVC controller registered under route name '{}'", strName );
            response.sendError( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

        dispatch( bean.get( ), request, response );
    }

    /**
     * Invokes the resolved controller and writes either nothing (action redirect already committed)
     * or the wrapped view page.
     *
     * @param bean the resolved controller bean
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws IOException if writing the response fails
     */
    private void dispatch( Bean<?> bean, HttpServletRequest request, HttpServletResponse response ) throws IOException
    {
        MVCAdminJspBean controller = (MVCAdminJspBean) CDI.current( ).select( bean.getBeanClass( ) ).get( );

        try
        {
            String strContent = controller.processController( request, response );

            if ( response.isCommitted( ) )
            {
                return;
            }

            String strPage = CDI.current( ).select( PageFrameService.class ).get( ).wrap( request, strContent );
            response.setContentType( CONTENT_TYPE_HTML );
            response.getWriter( ).write( strPage );
        }
        catch( AccessDeniedException e )
        {
            AppLogService.error( "Access denied on admin MVC controller {} : {}", bean.getBeanClass( ).getName( ), e.getMessage( ) );

            if ( !response.isCommitted( ) )
            {
                response.sendRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_ACCESS_DENIED, AdminMessage.TYPE_STOP ) );
            }
        }
    }

    /**
     * Extracts the controller route name from the request path info ({@code /name} becomes {@code name}).
     *
     * @param request the HTTP request
     * @return the route name, or an empty string when absent
     */
    private String extractRouteName( HttpServletRequest request )
    {
        String strPathInfo = request.getPathInfo( );

        if ( strPathInfo == null || strPathInfo.length( ) <= 1 )
        {
            return "";
        }

        String strName = strPathInfo.substring( 1 );
        int nSlash = strName.indexOf( '/' );

        return nSlash >= 0 ? strName.substring( 0, nSlash ) : strName;
    }
}
