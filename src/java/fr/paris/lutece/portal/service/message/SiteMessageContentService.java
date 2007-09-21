/*
 * Copyright (c) 2002-2007, Mairie de Paris
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

import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Site message pages provider service.
 *
 */
public class SiteMessageContentService extends ContentService
{
    private static final String CONTENT_SERVICE_NAME = "Site Message Content Service";
    private static final String TEMPLATE_MESSAGE = "skin/site/site_message.html";
    private static final String MARK_TITLE = "title";
    private static final String MARK_TEXT = "text";
    private static final String MARK_URL = "url";
    private static final String MARK_TARGET = "target";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_CANCEL_BUTTON = "cancel_button";
    private static final String MARK_REQUEST_PARAMETERS = "list_parameters";
    private static final String PROPERTY_TITLE_ERROR = "portal.util.message.titleError";

    /**
     * Returns the content of a page according to the parameters found in the http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @param nMode The mode
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     */
    @Override
    public String getPage( HttpServletRequest request, int nMode )
        throws UserNotSignedException
    {
        Locale locale = request.getLocale(  );
        HashMap model = new HashMap(  );

        SiteMessage message = SiteMessageService.getMessage( request );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) );

        if ( message == null )
        {
            message = new SiteMessage( Messages.MESSAGE_ERROR_SESSION, null, PROPERTY_TITLE_ERROR, url.getUrl(  ), "",
                    SiteMessage.TYPE_ERROR, SiteMessage.TYPE_BUTTON_HIDDEN, null );
        }

        model.put( MARK_MESSAGE, message );
        model.put( MARK_TEXT, message.getText( locale ) );
        model.put( MARK_TITLE, message.getTitle( locale ) );
        model.put( MARK_URL, message.getUrl(  ) );
        model.put( MARK_TARGET, message.getTarget(  ) );
        model.put( MARK_CANCEL_BUTTON, message.getTypeButton(  ) );
        model.put( MARK_REQUEST_PARAMETERS, message.getRequestParameters(  ) );

        // Delete message in session
        SiteMessageService.cleanMessageSession( request );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE, locale, model );

        // Fill a PageData structure for those elements
        PageData data = new PageData(  );
        data.setName( message.getTitle( locale ) );
        data.setPagePath( PortalService.getXPagePathContent( message.getTitle( locale ), nMode, request ) );
        data.setContent( template.getHtml(  ) );

        return PortalService.buildPageContent( data, nMode, request );
    }

    /**
     * Analyzes request parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        SiteMessage message = SiteMessageService.getMessage( request );

        if ( message != null )
        {
            return true;
        }

        return false;
    }

    /**
     * Returns the Content Service name
     *
     * @return The name as a String
     */
    public String getName(  )
    {
        return CONTENT_SERVICE_NAME;
    }
}
