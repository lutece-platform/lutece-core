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
package fr.paris.lutece.portal.service.message;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Site message pages provider service.
 *
 */
public class SiteMessageHandler implements ISiteMessageHandler
{
    private static final String TEMPLATE_MESSAGE = "skin/site/site_message.html";
    private static final String MARK_TITLE = "title";
    private static final String MARK_TEXT = "text";
    private static final String MARK_URL = "url";
    private static final String MARK_TARGET = "target";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_CANCEL_BUTTON = "cancel_button";
    private static final String MARK_REQUEST_PARAMETERS = "list_parameters";
    private static final String PROPERTY_TITLE_ERROR = "portal.util.message.titleError";
    private static final String TEMPLATE_PAGE_SITE_MESSAGE = "skin/site/page_site_message.html";
    private static final String BOOKMARK_BASE_URL = "@base_url@";
    private static final String MARK_BACK_URL = "back_url";

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean hasMessage( HttpServletRequest request )
    {
        SiteMessage message = SiteMessageService.getMessage( request );

        if ( message != null )
        {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getPage( HttpServletRequest request, int nMode )
    {
        Locale locale = request.getLocale(  );
        Map<String, Object> model = new HashMap<String, Object>(  );

        SiteMessage message = SiteMessageService.getMessage( request );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) );

        if ( message == null )
        {
            message = new SiteMessage( Messages.MESSAGE_ERROR_SESSION, null, PROPERTY_TITLE_ERROR, url.getUrl(  ), "",
                    SiteMessage.TYPE_ERROR, SiteMessage.TYPE_BUTTON_HIDDEN, null, null );
        }

        model.put( MARK_MESSAGE, message );
        model.put( MARK_TEXT, message.getText( locale ) );
        model.put( MARK_TITLE, message.getTitle( locale ) );
        model.put( MARK_URL, message.getUrl(  ) );
        model.put( MARK_TARGET, message.getTarget(  ) );
        model.put( MARK_CANCEL_BUTTON, message.getTypeButton(  ) );
        model.put( MARK_REQUEST_PARAMETERS, message.getRequestParameters(  ) );
        model.put( MARK_BACK_URL, message.getBackUrl(  ) );

        // Delete message in session
        SiteMessageService.cleanMessageSession( request );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE, locale, model );

        // Fill a PageData structure for those elements
        PageData data = new PageData(  );
        data.setName( message.getTitle( locale ) );
        // FIXME Cannot set the page path when app run in standalone mode (cannot connect to database). The page path is now not set.
        //        data.setPagePath( PortalService.getXPagePathContent( message.getTitle( locale ), nMode, request ) );
        data.setContent( template.getHtml(  ) );

        return buildPageContent( data, nMode, request );
    }

    /**
     * Returns the html code which represents the page content
     *
     * @param data The structure which contains the informations about the page
     * @param nMode The mode in which displaying the page : normal or administration
     * @param request The request
     * @return The html code of a page
     */
    private static String buildPageContent( PageData data, int nMode, HttpServletRequest request )
    {
        Locale locale = null;
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        if ( request != null )
        {
            locale = request.getLocale(  );
        }

        List<PageInclude> listIncludes = PageIncludeService.getIncludes(  );

        for ( PageInclude pic : listIncludes )
        {
            pic.fillTemplate( model, data, nMode, request );
        }

        model.put( Markers.PAGE_NAME, ( data.getName(  ) == null ) ? "" : data.getName(  ) );
        model.put( Markers.PAGE_TITLE, ( data.getName(  ) == null ) ? "" : data.getName(  ) );
        model.put( Markers.PAGE_CONTENT, ( data.getContent(  ) == null ) ? "" : data.getContent(  ) );

        String strBaseUrl = ( request != null ) ? AppPathService.getBaseUrl( request ) : ""; // request could be null (method called by daemons or batch)

        // for link service
        model.put( Markers.BASE_URL, strBaseUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_SITE_MESSAGE, locale, model );

        template.substitute( BOOKMARK_BASE_URL, ( request != null ) ? AppPathService.getBaseUrl( request ) : "" ); // request could be null (method called by daemons or batch)

        return template.getHtml(  );
    }
}
