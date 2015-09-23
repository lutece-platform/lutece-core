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
package fr.paris.lutece.portal.web.includes;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Page include that insert links into the head part of HTML pages
 */
public class LinksInclude implements PageInclude
{
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
    private static final String MARK_CSS_PREFIX = "css_prefix";

    // Templates
    private static final String TEMPLATE_PLUGIN_CSS_LINK = "skin/site/plugin_css_link.html";
    private static final String TEMPLATE_PLUGIN_JAVASCRIPT_LINK = "skin/site/plugin_javascript_link.html";
    private static final String PREFIX_PLUGINS_CSS = "css/plugins/";
    private static final String ABSOLUTE_URL = "http://";

    /**
     * Substitue specific bookmarks in the page template.
     * @param rootModel The data model
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        if ( request != null )
        {
            // Add links coming from the data object
            String strFavourite = ( data.getFavourite(  ) != null ) ? data.getFavourite(  )
                                                                    : PortalService.getSiteName(  );
            String strPortalName = PortalService.getSiteName(  );
            rootModel.put( MARK_FAVOURITE, strFavourite );
            rootModel.put( MARK_PORTAL_NAME, strPortalName );

            Locale locale = request.getLocale(  );

            // Add CSS links coming from plugins
            Collection<Plugin> listPlugins = PluginService.getPluginList(  );
            listPlugins.add( PluginService.getCore(  ) );

            StringBuilder sbCssLinks = new StringBuilder(  );
            StringBuilder sbJsLinks = new StringBuilder(  );

            for ( Plugin plugin : listPlugins )
            {
                if ( plugin.isInstalled(  ) )
                {
                    String strPage = request.getParameter( PARAMETER_PAGE );
                    Theme xpageTheme = plugin.getXPageTheme( request );

                    if ( ( strPage != null ) && ( xpageTheme != null ) )
                    {
                        for ( XPageApplicationEntry entry : plugin.getApplications(  ) )
                        {
                            if ( strPage.equals( entry.getId(  ) ) )
                            {
                                rootModel.put( MARK_PLUGIN_THEME_CSS, xpageTheme );
                            }
                        }
                    }

                    boolean bXPage = isPluginXPage( strPage, plugin );

                    if ( plugin.isCssStylesheetsScopePortal(  ) || ( bXPage && plugin.isCssStylesheetsScopeXPage(  ) ) )
                    {
                        for ( String strCssStyleSheet : plugin.getCssStyleSheets(  ) )
                        {
                            appendStyleSheet( sbCssLinks, strCssStyleSheet, locale );
                        }

                        for ( String strCssStyleSheet : plugin.getCssStyleSheets( nMode ) )
                        {
                            appendStyleSheet( sbCssLinks, strCssStyleSheet, locale );
                        }
                    }

                    if ( plugin.isJavascriptFilesScopePortal(  ) || ( bXPage && plugin.isJavascriptFilesScopeXPage(  ) ) )
                    {
                        for ( String strJavascriptFile : plugin.getJavascriptFiles(  ) )
                        {
                            appendJavascriptFile( sbJsLinks, strJavascriptFile, locale );
                        }

                        for ( String strJavascriptFile : plugin.getJavascriptFiles( nMode ) )
                        {
                            appendJavascriptFile( sbJsLinks, strJavascriptFile, locale );
                        }
                    }
                }
            }

            rootModel.put( MARK_PLUGINS_CSS_LINKS, sbCssLinks.toString(  ) );
            rootModel.put( MARK_PLUGINS_JAVASCRIPT_LINKS, sbJsLinks.toString(  ) );
        }
    }

    /**
     * Append a script to the links
     * @param sbJsLinks links in construction
     * @param strJavascriptFile the script to append
     * @param locale the locale
     */
    private void appendJavascriptFile( StringBuilder sbJsLinks, String strJavascriptFile, Locale locale )
    {
        Map<String, String> model = new HashMap<String, String>( 1 );
        model.put( MARK_PLUGIN_JAVASCRIPT_FILE, strJavascriptFile );

        HtmlTemplate tJs = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_JAVASCRIPT_LINK, locale, model );
        sbJsLinks.append( tJs.getHtml(  ) );
    }

    /**
     * Append a css to the stylesheets
     * @param sbCssLinks stylesheets in construction
     * @param strCssStyleSheet the stylesheet to append
     * @param locale the locale
     */
    private void appendStyleSheet( StringBuilder sbCssLinks, String strCssStyleSheet, Locale locale )
    {
        String strPrefix = ( strCssStyleSheet.startsWith( ABSOLUTE_URL ) ) ? "" : PREFIX_PLUGINS_CSS;

        Map<String, String> model = new HashMap<String, String>( 2 );
        model.put( MARK_PLUGIN_CSS_STYLESHEET, strCssStyleSheet );
        model.put( MARK_CSS_PREFIX, strPrefix );

        HtmlTemplate tCss = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_CSS_LINK, locale, model );
        sbCssLinks.append( tCss.getHtml(  ) );
    }

    /**
     * Check if the page is a valid plugin's page
     * @param strPage The page
     * @param plugin The plugin
     * @return true if valid otherwise false
     */
    private boolean isPluginXPage( String strPage, Plugin plugin )
    {
        if ( ( strPage != null ) )
        {
            for ( XPageApplicationEntry app : plugin.getApplications(  ) )
            {
                if ( strPage.equals( app.getId(  ) ) )
                {
                    return true;
                }
            }
        }

        return false;
    }
}
