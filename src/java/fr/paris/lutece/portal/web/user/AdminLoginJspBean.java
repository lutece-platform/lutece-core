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
package fr.paris.lutece.portal.web.user;

import fr.paris.lutece.portal.business.user.log.UserLog;
import fr.paris.lutece.portal.business.user.log.UserLogHome;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides the user interface to manage login features ( login, logout, ... )
 */
public class AdminLoginJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String JSP_ADMIN_MENU = "jsp/admin/AdminMenu.jsp";
    private static final String TEMPLATE_ADMIN_LOGIN = "admin/admin_login.html";
    private static final String MARK_PARAMS_LIST = "params_list";
    private static final String MARK_PARAM_VERSION = "version";
    private static final String SESSION_ATTRIBUTE_USER = "lutece_admin_user"; // Used by all JSP

    /**
     * Returns the view of login form
     *
     * @param request The request
     * @return The HTML form
     */
    public String getLogin( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        // Invalidate a previous session
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            session.removeAttribute( SESSION_ATTRIBUTE_USER );
        }

        Locale locale = AdminUserService.getLocale( request );

        Enumeration enumParams = request.getParameterNames(  );
        ReferenceList listParams = new ReferenceList(  );
        String strParamName;

        while ( enumParams.hasMoreElements(  ) )
        {
            strParamName = (String) enumParams.nextElement(  );

            String strParamValue = request.getParameter( strParamName );
            listParams.addItem( strParamName, strParamValue );
        }

        model.put( MARK_PARAM_VERSION, AppInfo.getVersion(  ) );
        model.put( MARK_PARAMS_LIST, listParams );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_LOGIN, locale, model );

        return template.getHtml(  );
    }

    /**
     * Process the login of user
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     * @throws Exception The exception
     */
    public String doLogin( HttpServletRequest request )
        throws Exception
    {
        //recovery of the login attributes
        String strAccessCode = request.getParameter( Parameters.ACCESS_CODE );
        String strPassword = request.getParameter( Parameters.PASSWORD );
        String strLoginUrl = AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  );

        try
        {
            AdminAuthenticationService.getInstance(  ).loginUser( request, strAccessCode, strPassword );
        }
        catch ( FailedLoginException ex )
        {
            // Creating a record of connections log
            UserLog userLog = new UserLog(  );
            userLog.setAccessCode( strAccessCode );
            userLog.setIpAddress( request.getRemoteAddr(  ) );
            userLog.setDateLogin( new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
            userLog.setLoginStatus( UserLog.LOGIN_DENIED ); // will be inserted only if access denied
            UserLogHome.addUserLog( userLog );

            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_AUTH_FAILURE, strLoginUrl,
                AdminMessage.TYPE_STOP );
        }
        catch ( LoginException ex )
        {
            AppLogService.error( "Error during connection for user access code :" + strAccessCode, ex );

            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_AUTH_FAILURE, strLoginUrl,
                AdminMessage.TYPE_STOP );
        }

        UrlItem url = AppPathService.resolveRedirectUrl( request, JSP_ADMIN_MENU );

        return url.getUrl(  );
    }

    /**
     * Process the logout of user
     *
     * @param request  Http request
     * @return The Jsp URL of the process result
     */
    public String doLogout( HttpServletRequest request )
    {
        // Invalidation of the session
        HttpSession session = request.getSession(  );
        session.removeAttribute( SESSION_ATTRIBUTE_USER );

        String strLoginUrl = AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  );

        return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_LOGOUT, strLoginUrl, AdminMessage.TYPE_INFO );
    }
}
