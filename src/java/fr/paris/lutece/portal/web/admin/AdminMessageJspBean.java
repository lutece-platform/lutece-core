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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class AdminMessageJspBean
{
    private static final String TEMPLATE_MESSAGE = "admin/util/message.html";
    private static final String MARK_TITLE = "title";
    private static final String MARK_TEXT = "text";
    private static final String MARK_URL = "url";
    private static final String MARK_TARGET = "target";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_REQUEST_PARAMETERS = "list_parameters";
    private static final String MARK_BACK_URL = "back_url";
    private static final String MARK_ADMIN_URL = "admin_url";
    private static final String PROPERTY_TITLE_ERROR = "portal.util.message.titleError";

    /**
     * Retrieve a message stored into a request
     * @param request The HTTP request
     * @return The HTML code of the message
     */
    public String getMessage( HttpServletRequest request )
    {
        Locale locale = AdminUserService.getLocale( request );
        Map<String, Object> model = new HashMap<String, Object>(  );
        AdminMessage message = AdminMessageService.getMessage( request );

        if ( message == null )
        {
            message = new AdminMessage( Messages.MESSAGE_ERROR_SESSION, null, PROPERTY_TITLE_ERROR,
                    AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ), "", AdminMessage.TYPE_ERROR, false,
                    null );
        }

        model.put( MARK_MESSAGE, message );
        model.put( MARK_TEXT, message.getText( locale ) );
        model.put( MARK_TITLE, message.getTitle( locale ) );
        model.put( MARK_URL, message.getUrl(  ) );
        model.put( MARK_TARGET, message.getTarget(  ) );
        model.put( MARK_REQUEST_PARAMETERS, message.getRequestParameters(  ) );
        model.put( MARK_BACK_URL, message.getBackUrl(  ) );
        model.put( MARK_ADMIN_URL, AppPathService.getAdminMenuUrl(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE, locale, model );

        return template.getHtml(  );
    }
}
