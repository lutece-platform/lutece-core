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
package fr.paris.lutece.portal.service.filter;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;


/**
 * FilterService
 */
public class FilterService
{
    private static FilterService _singleton = new FilterService(  );
    List<LuteceFilter> _listFilters = new ArrayList<LuteceFilter>(  );

    /**
     * Private constructor
     */
    private FilterService(  )
    {
    }

    public static FilterService getInstance(  )
    {
        return _singleton;
    }

    void registerFilter( String strFilterName, String strFilterClass, String strFilterMapping, Plugin plugin )
    {
        try
        {
            Filter filter = (Filter) Class.forName( strFilterClass ).newInstance(  );
            LuteceFilter f = new LuteceFilter( strFilterName, filter, strFilterMapping, plugin );
            _listFilters.add( f );
            AppLogService.info( "New plugin filter registered : " + strFilterName );
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

    List<LuteceFilter> getFilters(  )
    {
        return _listFilters;
    }
}
