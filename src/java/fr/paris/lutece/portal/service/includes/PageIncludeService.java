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
package fr.paris.lutece.portal.service.includes;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This service provides the registration and the list of all Portal Content Includes
 * declared in lutece.properties or in plugin XML files.
 * @since 1.2.4
 */
public final class PageIncludeService
{
    private static Map<String, PageIncludeEntry> _mapPageIncludes = new HashMap<String, PageIncludeEntry>(  );

    /**
     * Contructeur private (singleton pattern)
     */
    private PageIncludeService(  )
    {
    }

    /**
     * Register a Portal Content Include
     *
     * @param entry The PageInclude entry
     * @throws LuteceInitException If an error occured
     */
    public static void registerPageInclude( PageIncludeEntry entry )
        throws LuteceInitException
    {
        try
        {
            PageInclude pageInclude = (PageInclude) Class.forName( entry.getClassName(  ) ).newInstance(  );
            entry.setPageInclude( pageInclude );
            _mapPageIncludes.put( entry.getId(  ), entry );
            AppLogService.info( "New Page Include Service registered : " + entry.getId(  ) +
                ( ( !entry.isEnabled(  ) ) ? " (disabled)" : "" ) );
        }
        catch ( ClassNotFoundException e )
        {
            throw new LuteceInitException( e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new LuteceInitException( e.getMessage(  ), e );
        }
        catch ( InstantiationException e )
        {
            throw new LuteceInitException( e.getMessage(  ), e );
        }
    }

    /**
     * Returns a collection of Portal Content Includes
     * @return _mapIncludes
     */
    public static List<PageInclude> getIncludes(  )
    {
        List<PageInclude> listIncludes = new ArrayList<PageInclude>(  );

        for ( PageIncludeEntry entry : _mapPageIncludes.values(  ) )
        {
            if ( entry.isEnable(  ) )
            {
                listIncludes.add( entry.getPageInclude(  ) );
            }
        }

        return listIncludes;
    }
}
