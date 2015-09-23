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
package fr.paris.lutece.portal.service.filter;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;


/**
 * FilterService
 */
public final class FilterService
{
    private static FilterService _singleton = new FilterService(  );
    private static ServletContext _context;
    private List<LuteceFilter> _listFilters = new ArrayList<LuteceFilter>(  );

    /**
     * Private constructor
     */
    private FilterService(  )
    {
    }

    /**
     * Return the unique instance
     * @return The instance
     */
    public static FilterService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Register a filter
     * @param entry The filter entry defined in the plugin's XML file
     * @param plugin The plugin
     */
    public void registerFilter( FilterEntry entry, Plugin plugin )
    {
        try
        {
            Filter filter = (Filter) Class.forName( entry.getFilterClass(  ) ).newInstance(  );
            LuteceFilter f = new LuteceFilter( entry.getName(  ), filter, entry.getMappingUrlPattern(  ), plugin,
                    entry.getInitParameters(  ) );
            f.setOrder( entry.getOrder(  ) );
            _listFilters.add( f );
            AppLogService.info( "New Filter registered : " + entry.getName(  ) );

            for ( String strKey : entry.getInitParameters(  ).keySet(  ) )
            {
                AppLogService.info( " * init parameter - name : '" + strKey + "' - value : '" +
                    entry.getInitParameters(  ).get( strKey ) + "'" );
            }
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( "Error registering a filter : " + e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "Error registering a filter : " + e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( "Error registering a filter : " + e.getMessage(  ), e );
        }
    }

    /**
     * Defines the servlet context used by the FilterConfig given to the filters
     * @param servletContext The context
     */
    public static void setServletContext( ServletContext servletContext )
    {
        _context = servletContext;
    }

    /**
     * Initializes filters
     * @param context The context
     * @throws LuteceInitException If an error occurs
     */
    public static void init( ServletContext context ) throws LuteceInitException
    {
        _context = context;
        AppLogService.info( "Initialize plugins filters" );

        for ( LuteceFilter filter : FilterService.getInstance(  ).getFilters(  ) )
        {
            // Catch exception for each filter to execute all chain
            try
            {
                if ( filter.getPlugin(  ).isInstalled(  ) )
                {
                    // Create a FilterConfig wrapper to provide init parameters to the filter
                    LuteceFilterConfig filterConfig = new LuteceFilterConfig( filter.getName(  ), _context,
                            filter.getInitParameters(  ) );
                    filter.getFilter(  ).init( filterConfig );
                    AppLogService.info( " * filter '" + filter.getName(  ) + "' from plugin " +
                        filter.getPlugin(  ).getName(  ) + " initialized." );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error execution init() method - Filter " + filter.getName(  ), e );
                throw new LuteceInitException( "Error execution init() method - Filter " + filter.getName(  ), e );
            }
        }

        sortFilters(  );

        if ( AppLogService.isDebugEnabled(  ) )
        {
            AppLogService.debug( "Displaying filters order" );

            for ( LuteceFilter filter : FilterService.getInstance(  ).getFilters(  ) )
            {
                AppLogService.debug( filter.getName(  ) + " - order = " + filter.getOrder(  ) );
            }
        }
    }

    /**
     * Sort filters.
     */
    public static void sortFilters(  )
    {
        // sort the filter's list
        Collections.sort( FilterService.getInstance(  ).getFilters(  ) );
    }

    /**
     * Gives the filters list
     * @return The list of filters
     */
    public List<LuteceFilter> getFilters(  )
    {
        return _listFilters;
    }
}
