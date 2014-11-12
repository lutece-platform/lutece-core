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

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;


/**
 * ServletService
 */
public final class ServletService
{
    private static ServletService _singleton = new ServletService(  );
    private static ServletContext _context;
    private List<LuteceServlet> _listServlets = new ArrayList<LuteceServlet>(  );

    /**
     * Private constructor
     */
    private ServletService(  )
    {
    }

    /**
     * Return the unique instance
     * @return The instance
     */
    public static ServletService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Register a servlet
     * @param entry The servlet entry defined in the plugin's XML file
     * @param plugin The plugin
     */
    public void registerServlet( ServletEntry entry, Plugin plugin )
    {
        try
        {
            HttpServlet servlet = (HttpServlet) Class.forName( entry.getServletClass(  ) ).newInstance(  );
            LuteceServlet s = new LuteceServlet( entry.getName(  ), servlet, entry.getMappingUrlPattern(  ), plugin,
                    entry.getInitParameters(  ) );
            _listServlets.add( s );
            AppLogService.info( "New Servlet registered : " + entry.getName(  ) );

            for ( String strKey : entry.getInitParameters(  ).keySet(  ) )
            {
                AppLogService.info( " * init parameter - name : '" + strKey + "' - value : '" +
                    entry.getInitParameters(  ).get( strKey ) + "'" );
            }
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( "Error registering a servlet : " + e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "Error registering a servlet : " + e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( "Error registering a servlet : " + e.getMessage(  ), e );
        }
    }

    /**
     * Defines the servlet context used by the ServletConfig given to the servlets
     * @param servletContext The context
     */
    public static void setServletContext( ServletContext servletContext )
    {
        _context = servletContext;
    }

    /**
     * Initializes servlets
     * @param context The context
     * @throws LuteceInitException If an error occurs
     */
    public static void init( ServletContext context ) throws LuteceInitException
    {
        AppLogService.info( "Initialize plugins servlets" );
        _context = context;

        for ( LuteceServlet servlet : ServletService.getInstance(  ).getServlets(  ) )
        {
            // Catch exception for each servlet to execute all chain
            try
            {
                if ( servlet.getPlugin(  ).isInstalled(  ) )
                {
                    // Create a ServletConfig wrapper to provide init parameters to the servlet
                    LuteceServletConfig servletConfig = new LuteceServletConfig( servlet.getName(  ), _context,
                            servlet.getInitParameters(  ) );
                    servlet.getServlet(  ).init( servletConfig );
                    AppLogService.info( " * servlet '" + servlet.getName(  ) + "' from plugin " +
                        servlet.getPlugin(  ).getName(  ) + " initialized." );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error execution init() method - Servlet " + servlet.getName(  ), e );
                throw new LuteceInitException( "Error execution init() method - Servlet " + servlet.getName(  ), e );
            }
        }
    }

    /**
     * Gives the servlets list
     * @return The list of servlets
     */
    public List<LuteceServlet> getServlets(  )
    {
        return _listServlets;
    }
}
