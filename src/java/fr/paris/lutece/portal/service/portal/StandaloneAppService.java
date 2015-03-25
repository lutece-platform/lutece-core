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
package fr.paris.lutece.portal.service.portal;

import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class delivers Extra pages (xpages) to web components. An XPage is a page where the content is provided by a
 * specific class, but should be integrated into the portal struture and design. XPageApps are identified by a key
 * name. To display an XPage into the portal just call the following url :<br><code>
 * RunStandaloneApp.jsp?page=<i>keyname</i>&amp;param1=value1&amp; ...&amp;paramN=valueN </code>
 *
 * @see fr.paris.lutece.portal.web.xpages.XPage
 */
public class StandaloneAppService extends ContentService
{
    public static final String PARAM_STANDALONE_APP = "page";
    private static final String PROPERTY_PATH_LUTECE_PLUGINS = "path.lutece.plugins";
    private static final String TEMPLATE_PAGE_FRAMESET = "page_frameset.html";
    private static final String TEMPLATE_STANDALONE_PAGE_FRAMESET = "skin/site/standalone_app_frameset.html";
    private static final String CONTENT_SERVICE_NAME = "StandaloneAppService";
    private static final String MESSAGE_ERROR_APP_BODY = "portal.util.message.errorXpageApp";

    /**
     * Returns the Content Service name
     *
     * @return The name as a String
     */
    public String getName(  )
    {
        return CONTENT_SERVICE_NAME;
    }

    /**
     * Analyzes request parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    public boolean isInvoked( HttpServletRequest request )
    {
        String strStandaloneApp = request.getParameter( PARAM_STANDALONE_APP );

        if ( ( strStandaloneApp != null ) && ( strStandaloneApp.length(  ) > 0 ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Enable or disable the cache feature.
     *
     * @param bCache true to enable the cache, false to disable
     */
    public void setCache( boolean bCache )
    {
    }

    /**
     * Gets the current cache status.
     *
     * @return true if enable, otherwise false
     */
    public boolean isCacheEnable(  )
    {
        return false;
    }

    /**
     * Reset the cache.
     */
    public void resetCache(  )
    {
    }

    /**
     * Gets the number of item currently in the cache.
     *
     * @return the number of item currently in the cache.
     */
    public int getCacheSize(  )
    {
        return 0;
    }

    /**
     * Build the XPage content.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @return The HTML code of the page.
     * @throws UserNotSignedException a userNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getPage( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException
    {
        // Gets XPage info from the lutece.properties
        String strName = request.getParameter( PARAM_STANDALONE_APP );

        if ( ( strName == null ) || ( strName.length(  ) <= 0 ) )
        {
            // Return the welcome page
            return null;
        }

        PageData data = new PageData(  );
        XPageApplicationEntry entry = XPageAppService.getApplicationEntry( strName );

        if ( ( entry != null ) && ( entry.isEnable(  ) ) )
        {
            XPageApplication app = XPageAppService.getApplicationInstance( entry );
            XPage page = app.getPage( request, nMode, entry.getPlugin(  ) );
            data.setContent( page.getContent(  ) );
            data.setName( page.getTitle(  ) );
        }
        else
        {
            AppLogService.error( "The specified Xpage '" + strName +
                "' cannot be retrieved. Check installation of your Xpage application." );
            SiteMessageService.setMessage( request, MESSAGE_ERROR_APP_BODY, SiteMessage.TYPE_ERROR );
        }

        return buildPageContent( data, nMode, strName, request );
    }

    /**
     * Returns the html code which represents the page content
     *
     * @param data The structure which contains the informations about the page
     * @param nMode The mode in which displaying the page : normal or administration
     * @param strPluginName The name of the plugin
     * @param request TheHttpServletRequest
     * @return The html code of a page
     */
    private static String buildPageContent( PageData data, int nMode, String strPluginName, HttpServletRequest request )
    {
        HtmlTemplate template;
        String strFileName = strPluginName + "/" + TEMPLATE_PAGE_FRAMESET;
        String strFilePath = AppPathService.getPath( PROPERTY_PATH_LUTECE_PLUGINS, strFileName );
        File file = new File( strFilePath );
        String strPluginPath = "skin/plugins/";

        // Load of the templates
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( Markers.BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( Markers.PAGE_NAME, data.getName(  ) );
        model.put( Markers.PAGE_CONTENT, data.getContent(  ) );

        Collection<PageInclude> colIncludes = PageIncludeService.getIncludes(  );

        for ( PageInclude pic : colIncludes )
        {
            pic.fillTemplate( model, data, nMode, request );
        }

        if ( file.exists(  ) )
        {
            template = AppTemplateService.getTemplate( strPluginPath + strFileName, request.getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_STANDALONE_PAGE_FRAMESET, request.getLocale(  ), model );
        }

        return template.getHtml(  );
    }
}
