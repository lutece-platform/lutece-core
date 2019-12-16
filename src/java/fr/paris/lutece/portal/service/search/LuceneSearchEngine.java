/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import fr.paris.lutece.util.date.DateUtil;

import org.apache.commons.lang.StringUtils;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * LuceneSearchEngine
 */
public class LuceneSearchEngine implements SearchEngine
{
    public static final int MAX_RESPONSES = 1000000;
    private static final String PARAMETER_TYPE_FILTER = "type_filter";
    private static final String PARAMETER_DATE_AFTER = "date_after";
    private static final String PARAMETER_DATE_BEFORE = "date_before";
    private static final String PARAMETER_TAG_FILTER = "tag_filter";
    private static final String PARAMETER_DEFAULT_OPERATOR = "default_operator";
    private static final String PARAMETER_OPERATOR_AND = "AND";

    /**
     * Return search results
     *
     * @param strQuery
     *            The search query
     * @param request
     *            The HTTP request
     * @return Results as a collection of SearchResult
     */
    public List<SearchResult> getSearchResults( String strQuery, HttpServletRequest request )
    {
        ArrayList<SearchItem> listResults = new ArrayList<SearchItem>( );
        ArrayList<Query> listFilter = new ArrayList<Query>( );
        IndexSearcher searcher = null;
        boolean bFilterResult = false;
        LuteceUser user = null;
        String [ ] typeFilter = request.getParameterValues( PARAMETER_TYPE_FILTER );
        String strDateAfter = request.getParameter( PARAMETER_DATE_AFTER );
        String strDateBefore = request.getParameter( PARAMETER_DATE_BEFORE );
        boolean bDateAfter = false;
        boolean bDateBefore = false;
        Query allFilter = null;
        String strTagFilter = request.getParameter( PARAMETER_TAG_FILTER );

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRegisteredUser( request );

            Query [ ] filtersRole = null;

            if ( user != null )
            {
                String [ ] userRoles = SecurityService.getInstance( ).getRolesByUser( user );

                if ( userRoles != null )
                {
                    filtersRole = new Query [ userRoles.length + 1];

                    for ( int i = 0; i < userRoles.length; i++ )
                    {
                        Query queryRole = new TermQuery( new Term( SearchItem.FIELD_ROLE, userRoles [i] ) );
                        filtersRole [i] = queryRole;
                    }
                }
                else
                {
                    bFilterResult = true;
                }
            }
            else
            {
                filtersRole = new Query [ 1];
            }

            if ( !bFilterResult )
            {
                Query queryRole = new TermQuery( new Term( SearchItem.FIELD_ROLE, Page.ROLE_NONE ) );
                filtersRole [filtersRole.length - 1] = queryRole;
                BooleanQuery.Builder booleanQueryBuilderRole = new BooleanQuery.Builder( );
                for ( Query filterRole : filtersRole )
                {
                    booleanQueryBuilderRole.add( filterRole, BooleanClause.Occur.SHOULD );
                }
                listFilter.add( booleanQueryBuilderRole.build( ) );
            }
        }

        if ( StringUtils.isNotBlank( strDateAfter ) || StringUtils.isNotBlank( strDateBefore ) )
        {
            BytesRef strAfter = null;
            BytesRef strBefore = null;

            if ( StringUtils.isNotBlank( strDateAfter ) )
            {
                Date dateAfter = DateUtil.formatDate( strDateAfter, request.getLocale( ) );
                strAfter = new BytesRef( DateTools.dateToString( dateAfter, Resolution.DAY ) );
                bDateAfter = true;
            }

            if ( StringUtils.isNotBlank( strDateBefore ) )
            {
                Date dateBefore = DateUtil.formatDate( strDateBefore, request.getLocale( ) );
                strBefore = new BytesRef( DateTools.dateToString( dateBefore, Resolution.DAY ) );
                bDateBefore = true;
            }

            Query queryDate = new TermRangeQuery( SearchItem.FIELD_DATE, strAfter, strBefore, bDateAfter, bDateBefore );
            listFilter.add( queryDate );
        }

        if ( ( typeFilter != null ) && ( typeFilter.length > 0 ) && !typeFilter [0].equals( SearchService.TYPE_FILTER_NONE ) )
        {
            Query [ ] filtersType = new Query [ typeFilter.length];

            for ( int i = 0; i < typeFilter.length; i++ )
            {
                Query queryType = new TermQuery( new Term( SearchItem.FIELD_TYPE, typeFilter [i] ) );
                filtersType [i] = queryType;
            }

            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder( );
            for ( Query filterType : filtersType )
            {
                booleanQueryBuilder.add( filterType, BooleanClause.Occur.SHOULD );
            }
            listFilter.add( booleanQueryBuilder.build( ) );
        }

        if ( !listFilter.isEmpty( ) )
        {
            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder( );
            for ( Query filter : listFilter )
            {
                booleanQueryBuilder.add( filter, BooleanClause.Occur.MUST );
            }
            allFilter = booleanQueryBuilder.build( );
        }

        try( Directory directory = IndexationService.getDirectoryIndex( ) ; IndexReader ir = DirectoryReader.open( directory ) ; )
        {
            searcher = new IndexSearcher( ir );

            BooleanQuery.Builder bQueryBuilder = new BooleanQuery.Builder( );

            if ( StringUtils.isNotBlank( strTagFilter ) )
            {
                QueryParser parser = new QueryParser( SearchItem.FIELD_METADATA, IndexationService.getAnalyser( ) );

                Query queryMetaData = parser.parse( ( strQuery != null ) ? strQuery : "" );
                bQueryBuilder.add( queryMetaData, BooleanClause.Occur.SHOULD );

                parser = new QueryParser( SearchItem.FIELD_SUMMARY, IndexationService.getAnalyser( ) );

                Query querySummary = parser.parse( ( strQuery != null ) ? strQuery : "" );
                bQueryBuilder.add( querySummary, BooleanClause.Occur.SHOULD );
            }
            else
            {
                QueryParser parser = new QueryParser( SearchItem.FIELD_CONTENTS, IndexationService.getAnalyser( ) );

                String operator = request.getParameter( PARAMETER_DEFAULT_OPERATOR );

                if ( StringUtils.isNotEmpty( operator ) && operator.equals( PARAMETER_OPERATOR_AND ) )
                {
                    parser.setDefaultOperator( QueryParser.AND_OPERATOR );
                }

                Query queryContent = parser.parse( ( StringUtils.isNotBlank( strQuery ) ) ? strQuery : "" );
                bQueryBuilder.add( queryContent, BooleanClause.Occur.SHOULD );
            }

            Query query = bQueryBuilder.build( );

            if ( allFilter != null )
            {
                BooleanQuery.Builder bQueryBuilderWithFilter = new BooleanQuery.Builder( );
                bQueryBuilderWithFilter.add( allFilter, BooleanClause.Occur.FILTER );
                bQueryBuilderWithFilter.add( query, BooleanClause.Occur.MUST );
                query = bQueryBuilderWithFilter.build( );
            }

            // Get results documents
            TopDocs topDocs = searcher.search( query, MAX_RESPONSES );
            ScoreDoc [ ] hits = topDocs.scoreDocs;

            for ( int i = 0; i < hits.length; i++ )
            {
                int docId = hits [i].doc;
                Document document = searcher.doc( docId );
                SearchItem si = new SearchItem( document );

                if ( ( !bFilterResult ) || ( si.getRole( ).equals( Page.ROLE_NONE ) )
                        || ( SecurityService.getInstance( ).isUserInRole( request, si.getRole( ) ) ) )
                {
                    listResults.add( si );
                }
            }
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        return convertList( listResults );
    }

    /**
     * Convert a list of Lucene items into a list of generic search items
     * 
     * @param listSource
     *            The list of Lucene items
     * @return A list of generic search items
     */
    private List<SearchResult> convertList( List<SearchItem> listSource )
    {
        List<SearchResult> listDest = new ArrayList<SearchResult>( );

        for ( SearchItem item : listSource )
        {
            SearchResult result = new SearchResult( );
            result.setId( item.getId( ) );

            try
            {
                result.setDate( DateTools.stringToDate( item.getDate( ) ) );
            }
            catch( ParseException e )
            {
                AppLogService.error( "Bad Date Format for indexed item \"" + item.getTitle( ) + "\" : " + e.getMessage( ) );
            }

            result.setUrl( item.getUrl( ) );
            result.setTitle( item.getTitle( ) );
            result.setSummary( item.getSummary( ) );
            result.setType( item.getType( ) );
            listDest.add( result );
        }

        return listDest;
    }
}
