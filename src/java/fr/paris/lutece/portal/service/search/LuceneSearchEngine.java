/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * LuceneSearchEngine
 */
public class LuceneSearchEngine implements SearchEngine
{
    public static final int MAX_RESPONSES = 1000000;

    /**
    * Return search results
    *
    * @param strQuery The search query
    * @param request The HTTP request
    * @return Results as a collection of SearchResult
    */
    public List<SearchResult> getSearchResults( String strQuery, HttpServletRequest request )
    {
        ArrayList<SearchItem> listResults = new ArrayList<SearchItem>(  );
        Searcher searcher = null;
        Filter filterRole = null;
        boolean bFilterResult = false;
        LuteceUser user = null;

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            user = SecurityService.getInstance(  ).getRegisteredUser( request );

            Filter[] filtersRole = null;

            if ( user != null )
            {
                String[] userRoles = SecurityService.getInstance(  ).getRolesByUser( user );

                if ( userRoles != null )
                {
                    filtersRole = new Filter[userRoles.length + 1];

                    for ( int i = 0; i < userRoles.length; i++ )
                    {
                        Query queryRole = new TermQuery( new Term( SearchItem.FIELD_ROLE, userRoles[i] ) );
                        filtersRole[i] = new CachingWrapperFilter( new QueryWrapperFilter( queryRole ) );
                    }
                }
                else
                {
                    bFilterResult = true;
                }
            }
            else
            {
                filtersRole = new Filter[1];
            }

            if ( !bFilterResult )
            {
                Query queryRole = new TermQuery( new Term( SearchItem.FIELD_ROLE, Page.ROLE_NONE ) );
                filtersRole[filtersRole.length - 1] = new CachingWrapperFilter( new QueryWrapperFilter( queryRole ) );
                filterRole = new ChainedFilter( filtersRole, ChainedFilter.OR );
            }
        }

        try
        {
            searcher = new IndexSearcher( IndexationService.getDirectoryIndex(  ), true );

            Query query = null;
            QueryParser parser = new QueryParser( IndexationService.LUCENE_INDEX_VERSION, SearchItem.FIELD_CONTENTS,
                    IndexationService.getAnalyser(  ) );
            query = parser.parse( ( strQuery != null ) ? strQuery : "" );

            // Get results documents
            TopDocs topDocs = searcher.search( query, filterRole, MAX_RESPONSES );
            ScoreDoc[] hits = topDocs.scoreDocs;

            for ( int i = 0; i < hits.length; i++ )
            {
                int docId = hits[i].doc;
                Document document = searcher.doc( docId );
                SearchItem si = new SearchItem( document );

                if ( ( !bFilterResult ) || ( bFilterResult && si.getRole(  ).equals( Page.ROLE_NONE ) ) ||
                        ( bFilterResult && SecurityService.getInstance(  ).isUserInRole( request, si.getRole(  ) ) ) )
                {
                    listResults.add( si );
                }
            }

            searcher.close(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return convertList( listResults );
    }

    /**
    * Convert a list of Lucene items into a list of generic search items
    * @param listSource The list of Lucene items
    * @return A list of generic search items
    */
    private List<SearchResult> convertList( List<SearchItem> listSource )
    {
        List<SearchResult> listDest = new ArrayList<SearchResult>(  );

        for ( SearchItem item : listSource )
        {
            SearchResult result = new SearchResult(  );
            result.setId( item.getId(  ) );

            try
            {
                result.setDate( DateTools.stringToDate( item.getDate(  ) ) );
            }
            catch ( ParseException e )
            {
                AppLogService.error( "Bad Date Format for indexed item \"" + item.getTitle(  ) + "\" : " +
                    e.getMessage(  ) );
            }

            result.setUrl( item.getUrl(  ) );
            result.setTitle( item.getTitle(  ) );
            result.setSummary( item.getSummary(  ) );
            result.setType( item.getType(  ) );
            listDest.add( result );
        }

        return listDest;
    }
}
