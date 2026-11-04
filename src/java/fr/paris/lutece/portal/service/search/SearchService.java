/*
 * Copyright (c) 2002-2022, City of Paris
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
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPathService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

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
    private static final String MARK_LABEL = "label";
    private static final String PREFIX_TYPE_LABEL_KEY = "portal.search.type.";

    // Labels of the types already resolved, by locale and type: a key no bundle answers is logged as a warning on each lookup
    private static final Map<String, String> _mapTypeLabels = new ConcurrentHashMap<>( );

    /**
     * Instantiates a new search service.
     */
    private SearchService( )
    {
    }

    /**
     * Build the advanced parameters management.
     *
     * @param user
     *            the current admin user
     * @param request
     *            the request
     * @return the model for the advanced parameters
     */
    public static Map<String, Object> getManageAdvancedParameters( AdminUser user, HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.putAll( SearchParameterHome.findAll( ) );
        model.put( MARK_LOCALE, user.getLocale( ) );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );

        return model;
    }

    /**
     * Find types managed by each registered indexer and the link to their specific search page
     * 
     * @return the model for this list of types and links
     */
    public static List<Map<String, Object>> getSearchTypesAndLinks( )
    {
        return getSearchTypesAndLinks( I18nService.getDefaultLocale( ) );
    }

    /**
     * Find types managed by each registered indexer, their label and the link to their specific search page
     * 
     * @param locale
     *            the locale of the labels
     * @return the model for this list of types, labels and links
     */
    public static List<Map<String, Object>> getSearchTypesAndLinks( Locale locale )
    {
        List<Map<String, Object>> listTypesAndLinks = new ArrayList<>( );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_TYPE, TYPE_FILTER_NONE );
        model.put( MARK_LINK, null );
        listTypesAndLinks.add( model );

        List<SearchIndexer> listIndexer = new ArrayList<>( IndexationService.getIndexers( ) );

        for ( SearchIndexer indexer : listIndexer )
        {
            String strLink = indexer.getSpecificSearchAppUrl( );

            for ( String strType : indexer.getListType( ) )
            {
                model = new HashMap<>( );
                model.put( MARK_TYPE, strType );
                model.put( MARK_LABEL, indexer.getTypeLabel( strType, locale ) );
                model.put( MARK_LINK, strLink );
                listTypesAndLinks.add( model );
            }
        }

        return listTypesAndLinks;
    }

    /**
     * Labels of the types of every registered indexer, to show the type of a search result
     * 
     * @param locale
     *            the locale of the labels
     * @return the labels by type, in the order of the indexers
     */
    public static Map<String, String> getSearchTypeLabels( Locale locale )
    {
        Map<String, String> mapLabels = new LinkedHashMap<>( );

        for ( SearchIndexer indexer : IndexationService.getIndexers( ) )
        {
            for ( String strType : indexer.getListType( ) )
            {
                mapLabels.putIfAbsent( strType, indexer.getTypeLabel( strType, locale ) );
            }
        }

        return mapLabels;
    }

    /**
     * Default label of a search type: the key <code>portal.search.type.&lt;type&gt;</code>, the type lower-cased and every character other than a
     * letter or a digit replaced by an underscore (e.g. <code>portal.search.type.page</code>). When no bundle answers, the type itself with its first
     * letter capitalized.
     * 
     * @param strType
     *            the type
     * @param locale
     *            the locale of the label
     * @return the label of the type, never null
     */
    public static String getDefaultTypeLabel( String strType, Locale locale )
    {
        if ( StringUtils.isBlank( strType ) )
        {
            return StringUtils.defaultString( strType );
        }

        Locale theLocale = ( locale != null ) ? locale : I18nService.getDefaultLocale( );

        return _mapTypeLabels.computeIfAbsent( theLocale + "|" + strType, k -> {
            String strKey = PREFIX_TYPE_LABEL_KEY + strType.toLowerCase( Locale.ROOT ).replaceAll( "[^a-z0-9]+", "_" );
            String strLabel = I18nService.getLocalizedString( strKey, theLocale );

            return StringUtils.isNotBlank( strLabel ) ? strLabel : StringUtils.capitalize( strType );
        } );
    }

    /**
     * Forget the labels of the types already resolved, e.g. after a change of the i18n bundles
     */
    public static void resetTypeLabels( )
    {
        _mapTypeLabels.clear( );
    }
}
