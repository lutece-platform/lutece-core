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
package fr.paris.lutece.portal.web;

import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.message.ISiteMessageHandler;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.StandaloneAppService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Class of the StandaloneAppJspBean object.
 */
public class StandaloneAppJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final int MODE_HTML = 0;
    private static final String TEMPLATE_STANDALONE = "skin/site/standalone_app.html";
    private static final String MARK_ENTRY_LIST = "entry_list";
    private static final String MARK_BASE_URL = "base_url";
    private static final String MARK_CORE_PLUGIN = "core_plugin";
    private static final String BEAN_SITE_MESSAGE_HANDLER = "siteMessageHandler";

    /**
     * Returns the content of a page according to the parameters found in the http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getContent( HttpServletRequest request )
        throws UserNotSignedException, SiteMessageException
    {
        return getContent( request, MODE_HTML );
    }

    /**
     * Returns the content of a page according to the parameters found in the http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @param nMode The mode (normal or administration)
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getContent( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException
    {
        // Handle site messages first
        ISiteMessageHandler handlerSiteMessage = (ISiteMessageHandler) SpringContextService.getBean( BEAN_SITE_MESSAGE_HANDLER );

        if ( handlerSiteMessage.hasMessage( request ) )
        {
            return handlerSiteMessage.getPage( request, nMode );
        }

        ContentService csStandalone = new StandaloneAppService(  );
        String htmlPage = csStandalone.getPage( request, nMode );

        if ( htmlPage == null )
        {
            // Return the welcome page
            return getPluginList( request );
        }

        return htmlPage;
    }

    /**
     * Display the list of plugins app installed on the instance of lutece
     *
     * @param request The HTTP request
     * @return the list
     */
    public String getPluginList( HttpServletRequest request )
    {
        HashMap<String, Object> modelList = new HashMap<String, Object>(  );
        Collection<XPageApplicationEntry> entryList = new ArrayList<XPageApplicationEntry>(  );
        Locale locale = ( request == null ) ? null : request.getLocale(  );

        Collection<XPageApplicationEntry> applications = XPageAppService.getXPageApplicationsList(  );
        Comparator<XPageApplicationEntry> comparator = new Comparator<XPageApplicationEntry>(  )
            {
                public int compare( XPageApplicationEntry c1, XPageApplicationEntry c2 )
                {
                    Plugin p1 = ( c1.getPlugin(  ) == null ) ? PluginService.getCore(  ) : c1.getPlugin(  );
                    Plugin p2 = ( c2.getPlugin(  ) == null ) ? PluginService.getCore(  ) : c2.getPlugin(  );

                    return p1.getName(  ).compareTo( p2.getName(  ) );
                }
            };

        List<XPageApplicationEntry> applicationsSorted = new ArrayList<XPageApplicationEntry>( applications );
        Collections.sort( applicationsSorted, comparator );

        // Scan of the list
        for ( XPageApplicationEntry entry : applicationsSorted )
        {
            if ( entry.isEnable(  ) )
            {
                entryList.add( entry );
            }
        }

        // Insert the rows in the list
        modelList.put( MARK_ENTRY_LIST, entryList );
        modelList.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        modelList.put( MARK_CORE_PLUGIN, PluginService.getCore(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_STANDALONE, locale, modelList );

        return templateList.getHtml(  );
    }
}
