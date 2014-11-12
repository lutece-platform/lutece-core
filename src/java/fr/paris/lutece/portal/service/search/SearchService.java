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
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.business.search.SearchParameterHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.util.AppPathService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * The Class SearchService.
 */
public final class SearchService
{
    public static final String RESOURCE_TYPE = "SEARCH_SERVICE";
    public static final String TYPE_FILTER_NONE = "none";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_TYPE = "type";
    private static final String MARK_LINK = "link";

    /**
     * Instantiates a new search service.
     */
    private SearchService(  )
    {
    }

    /**
     * Build the advanced parameters management.
     *
     * @param user the current admin user
     * @param request the request
     * @return the model for the advanced parameters
     */
    public static Map<String, Object> getManageAdvancedParameters( AdminUser user, HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.putAll( SearchParameterHome.findAll(  ) );
        model.put( MARK_LOCALE, user.getLocale(  ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return model;
    }

    /**
     * Find types managed by each registered indexer and the link to their specific search page
     * @return the model for this list of types and links
     */
    public static List<Map<String, Object>> getSearchTypesAndLinks(  )
    {
        List<Map<String, Object>> listTypesAndLinks = new ArrayList<Map<String, Object>>(  );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_TYPE, TYPE_FILTER_NONE );
        model.put( MARK_LINK, null );
        listTypesAndLinks.add( model );

        List<SearchIndexer> listIndexer = new ArrayList<SearchIndexer>( IndexationService.getIndexers(  ) );

        for ( SearchIndexer indexer : listIndexer )
        {
            String strLink = indexer.getSpecificSearchAppUrl(  );

            for ( String strType : indexer.getListType(  ) )
            {
                model = new HashMap<String, Object>(  );
                model.put( MARK_TYPE, strType );
                model.put( MARK_LINK, strLink );
                listTypesAndLinks.add( model );
            }
        }

        return listTypesAndLinks;
    }
}
