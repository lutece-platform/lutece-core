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
package fr.paris.lutece.portal.web.search;

import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage the launching of the indexing of the site pages
 */
public class SearchIndexationJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constantes
    /**
     * Right to manage indexation
     */
    public static final String RIGHT_INDEXER = "CORE_SEARCH_INDEXATION";
    private static final long serialVersionUID = 2585709013740037568L;
    private static final String TEMPLATE_MANAGE_INDEXER = "admin/search/manage_search_indexation.html";
    private static final String TEMPLATE_INDEXER_LOGS = "admin/search/search_indexation_logs.html";
    private static final String MARK_LOGS = "logs";
    private static final String MARK_INDEXERS_LIST = "indexers_list";

    /**
     * Displays the indexing parameters
     *
     * @param request the http request
     * @return the html code which displays the parameters page
     */
    public String getIndexingProperties( HttpServletRequest request )
    {
        HashMap<String, Collection<SearchIndexer>> model = new HashMap<String, Collection<SearchIndexer>>(  );
        Collection<SearchIndexer> listIndexers = IndexationService.getIndexers(  );
        model.put( MARK_INDEXERS_LIST, listIndexers );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_INDEXER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Calls the indexing process
     *
     * @param request the http request
     * @return the result of the indexing process
     */
    public String doIndexing( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        String strLogs;

        if ( request.getParameter( "incremental" ) != null )
        {
            strLogs = IndexationService.processIndexing( false );
        }
        else
        {
            strLogs = IndexationService.processIndexing( true );
        }

        model.put( MARK_LOGS, strLogs );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_INDEXER_LOGS, null, model );

        return getAdminPage( template.getHtml(  ) );
    }
}
