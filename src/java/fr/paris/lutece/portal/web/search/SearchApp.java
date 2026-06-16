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
package fr.paris.lutece.portal.web.search;

import fr.paris.lutece.portal.business.search.SearchParameterHome;
import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.search.ISponsoredLinksService;
import fr.paris.lutece.portal.service.search.QueryEvent;
import fr.paris.lutece.portal.service.search.SearchEngine;
import fr.paris.lutece.portal.service.search.SearchResult;
import fr.paris.lutece.portal.service.search.SearchService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides search results pages.
 */
@RequestScoped
@Named("core.xpage.search")
public class SearchApp implements XPageApplication
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -9101183157272256639L;

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String BEAN_SEARCH_ENGINE = "searchEngine";
    private static final String TEMPLATE_RESULTS = "skin/search/search_results.html";
    private static final String PROPERTY_SEARCH_PAGE_URL = "search.pageSearch.baseUrl";
    private static final String PROPERTY_RESULTS_PER_PAGE = "search.nb.docs.per.page";
    private static final String PROPERTY_PATH_LABEL = "portal.search.search_results.pathLabel";
    private static final String PROPERTY_PAGE_TITLE = "portal.search.search_results.pageTitle";
    private static final String MESSAGE_INVALID_SEARCH_TERMS = "portal.search.message.invalidSearchTerms";
    private static final String MESSAGE_INVALID_DATE = "portal.search.message.invalidDate";
    private static final String MESSAGE_ENCODING_ERROR = "portal.search.message.encodingError";
    private static final String DEFAULT_RESULTS_PER_PAGE = "10";
    private static final String DEFAULT_PAGE_INDEX = "1";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_NB_ITEMS_PER_PAGE = "items_per_page";
    private static final String PARAMETER_QUERY = "query";
    private static final String PARAMETER_TAG_FILTER = "tag_filter";
    private static final String PARAMETER_DEFAULT_OPERATOR = "default_operator";
    private static final String PARAMETER_DATE_AFTER = "date_after";
    private static final String PARAMETER_DATE_BEFORE = "date_before";
    private static final String PARAMETER_TYPE_FILTER = "type_filter";
    private static final String PARAMETER_DATE_FILTER_PRESET = "datefilter";
    private static final String DATE_PICKER_INIT_PATTERN = "yyyy-MM-dd";
    private static final String MARK_RESULTS_LIST = "results_list";
    private static final String MARK_QUERY = "query";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_ERROR = "error";
    private static final String MARK_SPONSOREDLINKS_SET = "sponsoredlinks_set";
    private static final String MARK_LIST_TYPE_AND_LINK = "list_type_and_link";
    private static final String MARK_DATE_PICKER_FORMAT = "date_picker_format";
    private static final String MARK_SELECTED_TYPES = "selected_types";
    private static final String MARK_SELECTED_DATE_PRESET = "selected_datefilter";
    private static final String MARK_DATE_AFTER_VALUE = "date_after_value";
    private static final String MARK_DATE_BEFORE_VALUE = "date_before_value";
    private static final String PROPERTY_ENCODE_URI = "search.encode.uri";
    private static final String PROPERTY_ENCODE_URI_ENCODING = "search.encode.uri.encoding";
    private static final String CONSTANT_HTTP_METHOD_GET = "GET";
    private static final String DEFAULT_DATE_PICKER_FORMAT = "dd/mm/yyyy";
    private static final boolean DEFAULT_ENCODE_URI = false;
    @Inject
    @Named( BEAN_SEARCH_ENGINE )
    private SearchEngine _engine;
    @Inject
    private Event<QueryEvent> _queryEvent;
    @Inject
    @Named("sponsoredlinks.sponsoredLinksService")
    private Instance<ISponsoredLinksService> _sponsoredLinksService;

    /**
     * Returns search results
     *
     * @param request
     *            The HTTP request.
     * @param nMode
     *            The current mode.
     * @param plugin
     *            The plugin
     * @return The HTML code of the page.
     * @throws SiteMessageException
     *             If an error occurs
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws SiteMessageException
    {
        XPage page = new XPage( );
        String strQuery = request.getParameter( PARAMETER_QUERY );
        String strTagFilter = request.getParameter( PARAMETER_TAG_FILTER );

        String strEncoding = AppPropertiesService.getProperty( PROPERTY_ENCODE_URI_ENCODING, StandardCharsets.ISO_8859_1.name( ) );

        if ( StringUtils.equalsIgnoreCase( CONSTANT_HTTP_METHOD_GET, request.getMethod( ) )
                && !StringUtils.equalsIgnoreCase( strEncoding, StandardCharsets.UTF_8.name( ) ) )
        {
            try
            {
                if ( StringUtils.isNotBlank( strQuery ) )
                {
                    strQuery = new String( strQuery.getBytes( strEncoding ), StandardCharsets.UTF_8 );
                }

                if ( StringUtils.isNotBlank( strTagFilter ) )
                {
                    strTagFilter = new String( strTagFilter.getBytes( strEncoding ), StandardCharsets.UTF_8 );
                }
            }
            catch( UnsupportedEncodingException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }

        if ( StringUtils.isNotEmpty( strTagFilter ) )
        {
            strQuery = strTagFilter;
        }

        boolean bEncodeUri = Boolean.parseBoolean( AppPropertiesService.getProperty( PROPERTY_ENCODE_URI, Boolean.toString( DEFAULT_ENCODE_URI ) ) );

        String strSearchPageUrl = AppPropertiesService.getProperty( PROPERTY_SEARCH_PAGE_URL );
        String strError = "";
        Locale locale = request.getLocale( );

        // Check XSS characters
        if ( ( strQuery != null ) && ( SecurityUtil.containsXssCharacters( request, strQuery ) ) )
        {
            strError = I18nService.getLocalizedString( MESSAGE_INVALID_SEARCH_TERMS, locale );
            strQuery = "";
        }

        // Check date filters
        if ( !areDatesValid( request, locale ) )
        {
            strError = I18nService.getLocalizedString( MESSAGE_INVALID_DATE, locale );
        }

        String strDefaultNbItemPerPage = AppPropertiesService.getProperty( PROPERTY_RESULTS_PER_PAGE, DEFAULT_RESULTS_PER_PAGE );
        String strNbItemPerPage = Optional.ofNullable( request.getParameter( PARAMETER_NB_ITEMS_PER_PAGE ) ).orElse( strDefaultNbItemPerPage );
        int nNbItemsPerPage = Integer.parseInt( strNbItemPerPage );

        String strCurrentPageIndex = Optional.ofNullable( request.getParameter( PARAMETER_PAGE_INDEX ) ).orElse( DEFAULT_PAGE_INDEX );

        List<SearchResult> listResults = _engine.getSearchResults( strQuery, request );

        // The page should not be added to the cache

        // Notify results infos to QueryEventListeners
        notifyQueryListeners( strQuery, listResults.size( ), request );

        UrlItem url = new UrlItem( strSearchPageUrl );
        String strQueryForPaginator = strQuery;

        if ( bEncodeUri )
        {
            strQueryForPaginator = encodeUrl( request, strQuery );
        }

        if ( StringUtils.isNotBlank( strTagFilter ) )
        {
            strQuery = "";
        }

        url.addParameter( PARAMETER_QUERY, strQueryForPaginator );
        url.addParameter( PARAMETER_NB_ITEMS_PER_PAGE, nNbItemsPerPage );

        StringBuilder sbUrl = new StringBuilder( );
        sbUrl = sbUrl.append( url.getUrl( ) );

        Map<String, Object> model = new HashMap<>( );
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_DEFAULT_OPERATOR ) ) )
        {
            sbUrl = sbUrl.append( "&default_operator=" + request.getParameter( PARAMETER_DEFAULT_OPERATOR ) );
        }

        // Keep the active filters in the pagination links so they are not lost when browsing result pages
        appendUrlParameter( sbUrl, PARAMETER_DATE_AFTER, request.getParameter( PARAMETER_DATE_AFTER ) );
        appendUrlParameter( sbUrl, PARAMETER_DATE_BEFORE, request.getParameter( PARAMETER_DATE_BEFORE ) );
        appendUrlParameter( sbUrl, PARAMETER_DATE_FILTER_PRESET, request.getParameter( PARAMETER_DATE_FILTER_PRESET ) );

        String [ ] selectedTypes = request.getParameterValues( PARAMETER_TYPE_FILTER );
        if ( selectedTypes != null )
        {
            for ( String strType : selectedTypes )
            {
                appendUrlParameter( sbUrl, PARAMETER_TYPE_FILTER, strType );
            }
        }

        Paginator<SearchResult> paginator = new Paginator<>( listResults, nNbItemsPerPage, sbUrl.toString( ), PARAMETER_PAGE_INDEX, strCurrentPageIndex );

        model.put( MARK_RESULTS_LIST, paginator.getPageItems( ) );
        model.put( MARK_QUERY, strQuery );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, strNbItemPerPage );
        model.put( MARK_ERROR, strError );

        if ( _sponsoredLinksService.isResolvable( ) )
        {
            model.put( MARK_SPONSOREDLINKS_SET, _sponsoredLinksService.get( ).getHtmlCode( strQuery, locale ) );
        }

        model.put( MARK_LIST_TYPE_AND_LINK, SearchService.getSearchTypesAndLinks( ) );
        model.put( MARK_DATE_PICKER_FORMAT, getDatePickerFormat( locale ) );

        // Re-expose the submitted filters so the form reflects the current selection after a search
        model.put( MARK_SELECTED_TYPES, getSelectedTypes( request ) );
        model.put( MARK_SELECTED_DATE_PRESET, StringUtils.defaultString( request.getParameter( PARAMETER_DATE_FILTER_PRESET ) ) );
        model.put( MARK_DATE_AFTER_VALUE, toDatePickerInitValue( request.getParameter( PARAMETER_DATE_AFTER ), locale ) );
        model.put( MARK_DATE_BEFORE_VALUE, toDatePickerInitValue( request.getParameter( PARAMETER_DATE_BEFORE ), locale ) );

        model.putAll( SearchParameterHome.findAll( ) );

        // Override the configured default operator with the one submitted, so the form reflects the current selection (must be done after findAll)
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_DEFAULT_OPERATOR ) ) )
        {
            model.put( PARAMETER_DEFAULT_OPERATOR, request.getParameter( PARAMETER_DEFAULT_OPERATOR ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESULTS, locale, model );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATH_LABEL, locale ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale ) );
        page.setContent( template.getHtml( ) );

        return page;
    }

    /**
     * Appends a URL-encoded query parameter to the given URL builder, if the value is not blank. {@link UrlItem} does not encode parameter values, so the
     * encoding is done here to handle values containing spaces (type names) or slashes (dates).
     *
     * @param sbUrl
     *            The URL being built (already containing a query string)
     * @param strName
     *            The parameter name
     * @param strValue
     *            The parameter value
     */
    private void appendUrlParameter( StringBuilder sbUrl, String strName, String strValue )
    {
        if ( StringUtils.isNotBlank( strValue ) )
        {
            sbUrl.append( '&' ).append( strName ).append( '=' ).append( URLEncoder.encode( strValue, StandardCharsets.UTF_8 ) );
        }
    }

    /**
     * Returns the content types currently selected in the type filter, so they can be re-checked in the form after a search.
     *
     * @param request
     *            The HTTP request
     * @return The selected type values, or an empty list if none
     */
    private List<String> getSelectedTypes( HttpServletRequest request )
    {
        String [ ] typeFilter = request.getParameterValues( PARAMETER_TYPE_FILTER );

        return ( typeFilter != null ) ? Arrays.asList( typeFilter ) : Collections.emptyList( );
    }

    /**
     * Converts a submitted date filter value into the ISO value (<code>yyyy-MM-dd</code>) expected by the datepicker to pre-fill the input after a search.
     * The datepicker parses its initial value with {@link java.util.Date}, so a locale-formatted value (e.g. <code>11/06/2026</code>) cannot be used directly.
     *
     * @param strDate
     *            The submitted date string, in the locale short format
     * @param locale
     *            The request locale
     * @return The ISO date string, or an empty string if the value is blank or unparseable
     */
    private String toDatePickerInitValue( String strDate, Locale locale )
    {
        if ( StringUtils.isBlank( strDate ) )
        {
            return StringUtils.EMPTY;
        }

        Date date = DateUtil.formatDate( strDate, locale );

        return ( date != null ) ? new SimpleDateFormat( DATE_PICKER_INIT_PATTERN ).format( date ) : StringUtils.EMPTY;
    }

    /**
     * Builds the datepicker submit format (dataFormat) for the given locale, so that the value posted by the date filter inputs matches the pattern
     * {@link fr.paris.lutece.util.date.DateUtil#formatDate} uses to parse them server-side.
     *
     * The server pattern comes from <code>lutece.format.date.short</code> in Java {@link java.text.SimpleDateFormat} tokens (e.g. <code>dd'/'MM'/'yyyy</code>).
     * It is converted to vanillajs-datepicker tokens by stripping the quotes that escape the separators and lowercasing the month token (Java <code>MM</code>
     * numeric month becomes vanillajs <code>mm</code>; <code>MM</code> would mean the month name in vanillajs).
     *
     * @param locale
     *            The request locale
     * @return The datepicker format, e.g. <code>dd/mm/yyyy</code>
     */
    private String getDatePickerFormat( Locale locale )
    {
        String strServerPattern = I18nService.getDateFormatShortPattern( locale );

        if ( StringUtils.isBlank( strServerPattern ) )
        {
            return DEFAULT_DATE_PICKER_FORMAT;
        }

        return strServerPattern.replace( "'", "" ).replace( 'M', 'm' );
    }

    /**
     * Checks that the date filter parameters, when provided, hold valid dates.
     *
     * @param request
     *            The HTTP request
     * @param locale
     *            The locale used to parse the dates
     * @return <code>true</code> if both date parameters are either blank or parseable, <code>false</code> otherwise
     */
    private boolean areDatesValid( HttpServletRequest request, Locale locale )
    {
        return isDateValid( request.getParameter( PARAMETER_DATE_AFTER ), locale )
                && isDateValid( request.getParameter( PARAMETER_DATE_BEFORE ), locale );
    }

    /**
     * Checks that a date string, when provided, can be parsed for the given locale.
     *
     * @param strDate
     *            The date string to check
     * @param locale
     *            The locale used to parse the date
     * @return <code>true</code> if the string is blank or parseable, <code>false</code> otherwise
     */
    private boolean isDateValid( String strDate, Locale locale )
    {
        return StringUtils.isBlank( strDate ) || ( DateUtil.formatDate( strDate, locale ) != null );
    }

    /**
     * Encode an url string
     * 
     * @param request
     *            The HTTP request
     * @param strSource
     *            The string to encode
     * @return The encoded string
     * @throws SiteMessageException
     *             If an error occurs
     */
    public static String encodeUrl( HttpServletRequest request, String strSource ) throws SiteMessageException
    {
        String strSourceUrl = ( strSource != null ) ? strSource : StringUtils.EMPTY;

        String strEncoded = EncodingService.encodeUrl( strSourceUrl, PROPERTY_ENCODE_URI_ENCODING, StandardCharsets.ISO_8859_1.name( ) );

        if ( StringUtils.isBlank( strEncoded ) && StringUtils.isNotBlank( strSourceUrl ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_ENCODING_ERROR, SiteMessage.TYPE_ERROR );
        }

        return strEncoded;
    }

    /**
     * Notify all query Listeners
     * 
     * @param strQuery
     *            The query
     * @param nResultsCount
     *            The results count
     * @param request
     *            The request
     */
    private void notifyQueryListeners( String strQuery, int nResultsCount, HttpServletRequest request )
    {
        QueryEvent event = new QueryEvent( );
        event.setQuery( strQuery );
        event.setResultsCount( nResultsCount );
        event.setRequest( request );
        _queryEvent.fire( event );
    }
}
