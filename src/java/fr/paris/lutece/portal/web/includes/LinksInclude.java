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
package fr.paris.lutece.portal.web.includes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.CryptoService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Page include that insert links into the head part of HTML pages
 */
public class LinksInclude implements PageInclude
{
    private static final String ALGORITHM = "SHA-256";

    // Parameters
    private static final String PARAMETER_PAGE = "page";

    // Markers
    private static final String MARK_FAVOURITE = "favourite";
    private static final String MARK_PORTAL_NAME = "lutece_name";
    private static final String MARK_PLUGIN_THEME_CSS = "plugin_theme";
    private static final String MARK_PLUGINS_CSS_LINKS = "plugins_css_links";
    private static final String MARK_PLUGINS_JAVASCRIPT_LINKS = "plugins_javascript_links";
    private static final String MARK_PLUGIN_CSS_STYLESHEET = "plugin_css_stylesheet";
    private static final String MARK_PLUGIN_JAVASCRIPT_FILE = "plugin_javascript_file";

    // Templates
    private static final String TEMPLATE_PLUGIN_CSS_LINK = "skin/site/plugin_css_link.html";
    private static final String TEMPLATE_PLUGIN_JAVASCRIPT_LINK = "skin/site/plugin_javascript_link.html";
    private static final String PREFIX_PLUGINS_CSS = "css/plugins/";
    private static final String PREFIX_PLUGINS_JAVASCRIPT = "js/plugins/";

    /**
     * Substitue specific bookmarks in the page template.
     * 
     * @param rootModel
     *            The data model
     * @param data
     *            A PageData object containing applications data
     * @param nMode
     *            The current mode
     * @param request
     *            The HTTP request
     */
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        if ( request == null )
        {
            return;
        }
        // Add links coming from the data object
        String strFavourite = ( data.getFavourite( ) != null ) ? data.getFavourite( ) : PortalService.getSiteName( );
        String strPortalName = PortalService.getSiteName( );
        rootModel.put( MARK_FAVOURITE, strFavourite );
        rootModel.put( MARK_PORTAL_NAME, strPortalName );

        Locale locale = request.getLocale( );

        // Add CSS links coming from plugins
        Collection<Plugin> listPlugins = PluginService.getPluginList( );
        listPlugins.add( PluginService.getCore( ) );

        String strPage = request.getParameter( PARAMETER_PAGE );

        for ( Plugin plugin : listPlugins )
        {
            if ( plugin.isInstalled( ) )
            {
                Theme xpageTheme = plugin.getXPageTheme( request );

                if ( ( strPage != null ) && ( xpageTheme != null ) )
                {
                    for ( XPageApplicationEntry entry : plugin.getApplications( ) )
                    {
                        if ( strPage.equals( entry.getId( ) ) )
                        {
                            rootModel.put( MARK_PLUGIN_THEME_CSS, xpageTheme );
                        }
                    }
                }
            }
        }

        LinksIncludeCacheService cacheService = SpringContextService.getBean( LinksIncludeCacheService.SERVICE_NAME );
        String strKey = cacheService.getCacheKey( nMode, strPage, locale );
        @SuppressWarnings( "unchecked" )
        Map<String, Object> links = (Map<String, Object>) cacheService.getFromCache( strKey );
        if ( links == null )
        {
            StringBuilder sbCssLinks = new StringBuilder( );
            StringBuilder sbJsLinks = new StringBuilder( );

            for ( Plugin plugin : listPlugins )
            {
                if ( plugin.isInstalled( ) )
                {
                    boolean bXPage = isPluginXPage( strPage, plugin );

                    if ( plugin.isCssStylesheetsScopePortal( ) || ( bXPage && plugin.isCssStylesheetsScopeXPage( ) ) )
                    {
                        for ( String strCssStyleSheet : plugin.getCssStyleSheets( ) )
                        {
                            appendStyleSheet( request.getServletContext( ), sbCssLinks, strCssStyleSheet, locale );
                        }

                        for ( String strCssStyleSheet : plugin.getCssStyleSheets( nMode ) )
                        {
                            appendStyleSheet( request.getServletContext( ), sbCssLinks, strCssStyleSheet, locale );
                        }
                    }

                    if ( plugin.isJavascriptFilesScopePortal( ) || ( bXPage && plugin.isJavascriptFilesScopeXPage( ) ) )
                    {
                        for ( String strJavascriptFile : plugin.getJavascriptFiles( ) )
                        {
                            appendJavascriptFile( request.getServletContext( ), sbJsLinks, strJavascriptFile, locale );
                        }

                        for ( String strJavascriptFile : plugin.getJavascriptFiles( nMode ) )
                        {
                            appendJavascriptFile( request.getServletContext( ), sbJsLinks, strJavascriptFile, locale );
                        }
                    }
                }
            }

            links = new HashMap<>( 2 );
            links.put( MARK_PLUGINS_CSS_LINKS, sbCssLinks.toString( ) );
            links.put( MARK_PLUGINS_JAVASCRIPT_LINKS, sbJsLinks.toString( ) );
            cacheService.putInCache( strKey, links );
        }
        rootModel.putAll( links );
    }

    /**
     * Append a script to the links
     * 
     * @param servletContext
     *            servlet context
     * @param sbJsLinks
     *            links in construction
     * @param strJavascriptFile
     *            the script to append
     * @param locale
     *            the locale
     */
    private void appendJavascriptFile( ServletContext servletContext, StringBuilder sbJsLinks, String strJavascriptFile, Locale locale )
    {
        URI javascripFileURI = getURI( servletContext, strJavascriptFile, PREFIX_PLUGINS_JAVASCRIPT );

        if ( javascripFileURI == null )
        {
            return;
        }

        Map<String, String> model = new HashMap<>( 1 );
        model.put( MARK_PLUGIN_JAVASCRIPT_FILE, javascripFileURI.toString( ) );

        HtmlTemplate tJs = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_JAVASCRIPT_LINK, locale, model );
        sbJsLinks.append( tJs.getHtml( ) );
    }

    /**
     * Append a css to the stylesheets
     * 
     * @param servletContext
     *            servlet context
     * @param sbCssLinks
     *            stylesheets in construction
     * @param strCssStyleSheet
     *            the stylesheet to append
     * @param locale
     *            the locale
     */
    private void appendStyleSheet( ServletContext servletContext, StringBuilder sbCssLinks, String strCssStyleSheet, Locale locale )
    {
        URI styleSheetURI = getURI( servletContext, strCssStyleSheet, PREFIX_PLUGINS_CSS );

        if ( styleSheetURI == null )
        {
            return;
        }

        Map<String, String> model = new HashMap<>( 2 );
        model.put( MARK_PLUGIN_CSS_STYLESHEET, styleSheetURI.toString( ) );

        HtmlTemplate tCss = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_CSS_LINK, locale, model );
        sbCssLinks.append( tCss.getHtml( ) );
    }

    /**
     * Get a URI for a resource. If the resource is provided by this site, a hash of its content is added as query parameter so that changes to the content are
     * picked up by browsers.
     * 
     * @param servletContext
     *            the servlet context
     * @param strResourceURI
     *            the resource URI as string
     * @param strURIPrefix
     *            a prefix to add to the URI if it is not absolute
     * @return the URI or <code>null</code> if it cannot be parsed
     */
    private URI getURI( ServletContext servletContext, String strResourceURI, String strURIPrefix )
    {
        try
        {
            URI resourceURI = new URI( strResourceURI );
            if ( !resourceURI.isAbsolute( ) && resourceURI.getHost( ) == null )
            {
                if ( strURIPrefix != null )
                {
                    resourceURI = new URI( strURIPrefix + strResourceURI );
                }
                try( InputStream inputStream = servletContext.getResourceAsStream( resourceURI.getPath( ) ) )
                {
                    if ( inputStream != null )
                    {
                        String hash = CryptoService.digest( inputStream, ALGORITHM );
                        if ( hash != null )
                        {
                            char separator = '?';
                            if ( resourceURI.getQuery( ) != null )
                            {
                                separator = '&';
                            }
                            resourceURI = new URI( resourceURI.toString( ) + separator + "lutece_h=" + hash );
                        }
                    }
                }
                catch( IOException e )
                {
                    AppLogService.error( "Error while closing stream for " + strResourceURI, e );
                }
            }
            return resourceURI;
        }
        catch( URISyntaxException e )
        {
            AppLogService.error( "Invalid cssStyleSheetURI : " + strResourceURI, e );
            return null;
        }
    }

    /**
     * Check if the page is a valid plugin's page
     * 
     * @param strPage
     *            The page
     * @param plugin
     *            The plugin
     * @return true if valid otherwise false
     */
    private boolean isPluginXPage( String strPage, Plugin plugin )
    {
        if ( ( strPage != null ) )
        {
            for ( XPageApplicationEntry app : plugin.getApplications( ) )
            {
                if ( strPage.equals( app.getId( ) ) )
                {
                    return true;
                }
            }
        }

        return false;
    }
}
