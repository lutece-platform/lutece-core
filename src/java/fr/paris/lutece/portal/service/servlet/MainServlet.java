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
package fr.paris.lutece.portal.service.servlet;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * MainServlet
 */
public class MainServlet implements Servlet
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void init( ServletConfig config ) throws ServletException
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void service( ServletRequest requestServlet, ServletResponse responseServlet )
        throws ServletException, IOException
    {
        AppLogService.debug( "MainServlet : service()" );

        HttpServletRequest request = (HttpServletRequest) requestServlet;
        HttpServletResponse response = (HttpServletResponse) responseServlet;

        for ( LuteceServlet servlet : ServletService.getInstance(  ).getServlets(  ) )
        {
            AppLogService.debug( "PluginServlet : " + servlet.getName(  ) + " - url pattern : " +
                servlet.getMappingUrlPattern(  ) );

            try
            {
                // Checks mapping and plugin status
                if ( matchMapping( servlet, request ) && servlet.getPlugin(  ).isInstalled(  ) )
                {
                    servlet.getServlet(  ).service( request, response );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error execution 'service' method - Servlet " + servlet.getName(  ), e );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy(  )
    {
        for ( LuteceServlet servlet : ServletService.getInstance(  ).getServlets(  ) )
        {
            // Catch exception for each servlet to execute all chain
            try
            {
                // Checks mapping and plugin status
                if ( servlet.getPlugin(  ).isInstalled(  ) )
                {
                    servlet.getServlet(  ).destroy(  );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error execution destroy() method - Servlet " + servlet.getName(  ), e );
            }
        }
    }

    /**
     * Check the mapping of the request with an url pattern
     * @param servlet The servlet
     * @param request The request
     * @return True if the request match the url pattern
     */
    private boolean matchMapping( LuteceServlet servlet, HttpServletRequest request )
    {
        return matchUrl( servlet.getMappingUrlPattern(  ), request.getServletPath(  ) + request.getPathInfo(  ) );
    }

    /**
     * Check the mapping of the request with an url pattern according servlet
     * specifications 2.3 rules
     * @param strUrlPattern The servlet url pattern
     * @param strRequestUrl The request Url
     * @return True if the request match the url pattern
     *
     * Algorithm comming from tomcat6
     */
    private boolean matchUrl( String strUrlPattern, String strRequestUrl )
    {
        return strRequestUrl.startsWith( strUrlPattern );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServletConfig getServletConfig(  )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo(  )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
