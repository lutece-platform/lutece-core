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
package fr.paris.lutece.portal.util.mvc.utils;

import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Utils for MVC components
 */
public final class MVCUtils
{
    public static final String PARAMETER_VIEW = "view";
    public static final String PARAMETER_ACTION = "action";
    public static final String PARAMETER_PAGE = "page";
    private static final String PREFIX_VIEW = "view_";
    private static final String PREFIX_ACTION = "action_";
    private static Logger _logger = Logger.getLogger( "lutece.mvc" );

    /**
     * Private constructor
     */
    private MVCUtils(  )
    {
    }

    /**
     * Get the view parameter
     * @param request The HTTP request
     * @return The view parameter or null if not found
     */
    public static String getView( HttpServletRequest request )
    {
        String strView = request.getParameter( PARAMETER_VIEW );

        if ( strView != null )
        {
            return strView;
        }

        Enumeration<String> parameters = request.getParameterNames(  );

        while ( parameters.hasMoreElements(  ) )
        {
            String strParameter = parameters.nextElement(  );

            if ( strParameter.startsWith( PREFIX_VIEW ) )
            {
                strView = strParameter.substring( PREFIX_VIEW.length(  ) );

                break;
            }
        }

        return strView;
    }

    /**
     * Get the action parameter or any parameter with the prefix 'action_'
     * @param request The HTTP request
     * @return The action parameter or null if not found
     */
    public static String getAction( HttpServletRequest request )
    {
        String strAction = request.getParameter( PARAMETER_ACTION );

        if ( strAction != null )
        {
            return strAction;
        }

        Enumeration<String> parameters = request.getParameterNames(  );

        while ( parameters.hasMoreElements(  ) )
        {
            String strParameter = parameters.nextElement(  );

            if ( strParameter.startsWith( PREFIX_ACTION ) )
            {
                strAction = strParameter.substring( PREFIX_ACTION.length(  ) );

                break;
            }
        }

        return strAction;
    }

    /**
     * Find the method that provide a given view
     * @param request The HTTP request
     * @param methods The methods list
     * @return The method
     */
    public static Method findViewAnnotedMethod( HttpServletRequest request, Method[] methods )
    {
        String strView = getView( request );

        if ( strView != null )
        {
            for ( Method m : methods )
            {
                if ( m.isAnnotationPresent( View.class ) && strView.equals( m.getAnnotation( View.class ).value(  ) ) )
                {
                    _logger.debug( "MVC controller - process view : '" + strView + "'" );

                    return m;
                }
            }

            _logger.warn( "MVC controller - No method found to process view : '" + strView + "'" );
        }

        return null;
    }

    /**
     * Find the method that provide a given action
     * @param request The HTTP request
     * @param methods The methods list
     * @return The method
     */
    public static Method findActionAnnotedMethod( HttpServletRequest request, Method[] methods )
    {
        String strAction = getAction( request );

        if ( strAction != null )
        {
            for ( Method m : methods )
            {
                if ( m.isAnnotationPresent( Action.class ) &&
                        strAction.equals( m.getAnnotation( Action.class ).value(  ) ) )
                {
                    _logger.debug( "MVC controller - process action : '" + strAction + "'" );

                    return m;
                }
            }

            _logger.warn( "MVC controller - No method found to process action : '" + strAction + "'" );
        }

        return null;
    }

    /**
     * Find the method that provide the default view
     * @param methods The methods list
     * @return The method
     */
    public static Method findDefaultViewMethod( Method[] methods )
    {
        for ( Method m : methods )
        {
            if ( m.isAnnotationPresent( View.class ) && m.getAnnotation( View.class ).defaultView(  ) )
            {
                _logger.debug( "MVC controller - process default view" );

                return m;
            }
        }

        _logger.error( "MVC controller - No default view found" );

        return null;
    }

    /**
     * Add download headers to the response
     * @param response The response
     * @param strFilename The name of the file to download
     * @param strContentType The content type of the downloaded file
     */
    public static void addDownloadHeaderToResponse( HttpServletResponse response, String strFilename,
        String strContentType )
    {
        response.setHeader( "Content-Disposition", "attachment; filename=\"" + strFilename + "\";" );
        response.setHeader( "Content-type", strContentType );
        response.addHeader( "Content-Encoding", "UTF-8" );
        response.addHeader( "Pragma", "public" );
        response.addHeader( "Expires", "0" );
        response.addHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
    }

    /**
     * Return the MVC logger
     * @return THe logger
     */
    public static Logger getLogger(  )
    {
        return _logger;
    }
}
