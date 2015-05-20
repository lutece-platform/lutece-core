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
package fr.paris.lutece.portal.web.user;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.user.log.UserLog;
import fr.paris.lutece.portal.business.user.log.UserLogHome;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppHTTPSService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides the user interface to manage login features ( login,
 * logout, ... )
 */
public class AdminLoginJspBean implements Serializable
{
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1437296329596757569L;

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String CONSTANT_EMAIL_DELIMITER = ";";
    private static final String CONSTANT_EMPTY_STRING = "";
    private static final String CONSTANT_SLASH = "/";
    private static final String CONSTANT_HTTP = "http";
    private static final String REGEX_ID = "^[\\d]+$";

    // Jsp
    private static final String JSP_URL_MODIFY_DEFAULT_USER_PASSOWRD = "jsp/admin/user/ModifyDefaultUserPassword.jsp";
    private static final String JSP_URL_FORM_CONTACT = "AdminFormContact.jsp";
    private static final String JSP_URL_DO_ADMIN_LOGIN = "jsp/admin/DoAdminLogin.jsp";
    private static final String JSP_URL_ADMIN_LOGIN = "jsp/admin/AdminLogin.jsp";

    // Templates
    private static final String TEMPLATE_ADMIN_LOGIN = "admin/admin_login.html";
    private static final String TEMPLATE_ADMIN_FORGOT_PASSWORD = "admin/admin_forgot_password.html";
    private static final String TEMPLATE_ADMIN_FORGOT_LOGIN = "admin/admin_forgot_login.html";
    private static final String TEMPLATE_ADMIN_FORM_CONTACT = "admin/admin_form_contact.html";
    private static final String TEMPLATE_ADMIN_EMAIL_FORGOT_PASSWORD = "admin/admin_email_forgot_password.html";
    private static final String TEMPLATE_ADMIN_EMAIL_FORGOT_LOGIN = "admin/admin_email_forgot_login.html";

    // Markers
    private static final String MARK_PARAMS_LIST = "params_list";
    private static final String MARK_FORGOT_PASSWORD_URL = "forgot_password_url";
    private static final String MARK_FORGOT_LOGIN_URL = "forgot_login_url";
    private static final String MARK_PARAM_VERSION = "version";
    private static final String MARK_SITE_NAME = "site_name";
    private static final String MARK_NEW_PASSWORD = "new_password";
    private static final String MARK_LOGIN_URL = "login_url";
    private static final String MARK_DO_ADMIN_LOGIN_URL = "do_admin_login_url";
    private static final String MARK_SITE_LINK = "site_link";
    private static final String MARK_LOGIN = "login";
    private static final String SESSION_ATTRIBUTE_USER = "lutece_admin_user"; // Used by all JSP

    // parameters
    private static final String PARAMETER_MESSAGE = "message_contact";
    private static final String PARAMETER_FORCE_CHANGE_PASSWORD_REINIT = "force_change_password_reinit";

    // I18n message keys
    private static final String MESSAGE_SENDING_SUCCESS = "portal.admin.message.admin_forgot_password.sendingSuccess";
    private static final String MESSAGE_ADMIN_SENDING_SUCCESS = "portal.admin.message.admin_form_contact.sendingSuccess";
    private static final String MESSAGE_EMAIL_SUBJECT = "portal.admin.admin_forgot_password.email.subject";
    private static final String MESSAGE_FORGOT_LOGIN_EMAIL_SUBJECT = "portal.admin.admin_forgot_login.email.subject";
    private static final String MESSAGE_FORGOT_LOGIN_SENDING_SUCCESS = "portal.admin.message.admin_forgot_login.sendingSuccess";
    private static final String MESSAGE_EMAIL_ADMIN_SUBJECT = "portal.admin.admin_form_contact.email.subject";
    private static final String MESSAGE_WRONG_EMAIL_FORMAT = "portal.admin.message.admin_forgot_login.wrongEmailFormat";

    // Properties
    private static final String PROPERTY_LEVEL = "askPasswordReinitialization.admin.level";

    /**
     * Returns the view of login form
     *
     * @param request The request
     * @return The HTML form
     */
    public String getLogin( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        // Invalidate a previous session
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            session.removeAttribute( SESSION_ATTRIBUTE_USER );
            // Put real base url in session
            request.getSession(  ).setAttribute( AppPathService.SESSION_BASE_URL, AppPathService.getBaseUrl( request ) );
        }

        Locale locale = AdminUserService.getLocale( request );

        Enumeration<String> enumParams = request.getParameterNames(  );
        ReferenceList listParams = new ReferenceList(  );
        String strParamName;

        while ( enumParams.hasMoreElements(  ) )
        {
            strParamName = enumParams.nextElement(  );

            String strParamValue = request.getParameter( strParamName );
            listParams.addItem( strParamName, strParamValue );
        }

        StringBuilder sbUrl = new StringBuilder(  );

        if ( AppHTTPSService.isHTTPSSupportEnabled(  ) )
        {
            sbUrl.append( AppHTTPSService.getHTTPSUrl( request ) );
        }
        else
        {
            sbUrl.append( AppPathService.getBaseUrl( request ) );
        }

        if ( !sbUrl.toString(  ).endsWith( CONSTANT_SLASH ) )
        {
            sbUrl.append( CONSTANT_SLASH );
        }

        sbUrl.append( JSP_URL_DO_ADMIN_LOGIN );

        model.put( MARK_PARAM_VERSION, AppInfo.getVersion(  ) );
        model.put( MARK_SITE_NAME, PortalService.getSiteName(  ) );
        model.put( MARK_PARAMS_LIST, listParams );
        model.put( MARK_FORGOT_PASSWORD_URL, AdminAuthenticationService.getInstance(  ).getLostPasswordPageUrl(  ) );
        model.put( MARK_FORGOT_LOGIN_URL, AdminAuthenticationService.getInstance(  ).getLostLoginPageUrl(  ) );
        model.put( MARK_DO_ADMIN_LOGIN_URL, sbUrl.toString(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_LOGIN, locale, model );

        return template.getHtml(  );
    }

    /**
     * Returns the view of forgot password form
     *
     * @param request The request
     * @return The HTML form
     */
    public String getForgotPassword( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        // Invalidate a previous session
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            session.removeAttribute( SESSION_ATTRIBUTE_USER );
        }

        Locale locale = AdminUserService.getLocale( request );

        Enumeration<String> enumParams = request.getParameterNames(  );
        ReferenceList listParams = new ReferenceList(  );
        String strParamName;

        while ( enumParams.hasMoreElements(  ) )
        {
            strParamName = enumParams.nextElement(  );

            String strParamValue = request.getParameter( strParamName );
            listParams.addItem( strParamName, strParamValue );
        }

        model.put( MARK_PARAM_VERSION, AppInfo.getVersion(  ) );
        model.put( MARK_PARAMS_LIST, listParams );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_FORGOT_PASSWORD, locale, model );

        return template.getHtml(  );
    }

    /**
     * Returns the view of forgot password form
     *
     * @param request The request
     * @return The HTML form
     */
    public String getForgotLogin( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        // Invalidate a previous session
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            session.removeAttribute( SESSION_ATTRIBUTE_USER );
        }

        Locale locale = AdminUserService.getLocale( request );

        Enumeration<String> enumParams = request.getParameterNames(  );
        ReferenceList listParams = new ReferenceList(  );
        String strParamName;

        while ( enumParams.hasMoreElements(  ) )
        {
            strParamName = enumParams.nextElement(  );

            String strParamValue = request.getParameter( strParamName );
            listParams.addItem( strParamName, strParamValue );
        }

        model.put( MARK_PARAM_VERSION, AppInfo.getVersion(  ) );
        model.put( MARK_PARAMS_LIST, listParams );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_FORGOT_LOGIN, locale, model );

        return template.getHtml(  );
    }

    /**
     * Get the admin contact form
     * @param request The Http request
     * @return The HTML form
     */
    public String getFormContact( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        // Invalidate a previous session
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            session.removeAttribute( SESSION_ATTRIBUTE_USER );
        }

        Locale locale = AdminUserService.getLocale( request );

        model.put( MARK_PARAM_VERSION, AppInfo.getVersion(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_FORM_CONTACT, locale, model );

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
        // recovery of the login attributes
        String strAccessCode = request.getParameter( Parameters.ACCESS_CODE );
        String strPassword = request.getParameter( Parameters.PASSWORD );

        if ( request.getScheme(  ).equals( CONSTANT_HTTP ) && AppHTTPSService.isHTTPSSupportEnabled(  ) )
        {
            return JSP_URL_ADMIN_LOGIN;
        }

        // Encryption password
        strPassword = AdminUserService.encryptPassword( strPassword );

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
            userLog.setIpAddress( SecurityUtil.getRealIp( request ) );
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

        UrlItem url;

        AdminUser user = AdminUserHome.findUserByLogin( strAccessCode );

        if ( user.isPasswordReset(  ) )
        {
            String strRedirectUrl = AdminMessageService.getMessageUrl( request,
                    Messages.MESSAGE_USER_MUST_CHANGE_PASSWORD, JSP_URL_MODIFY_DEFAULT_USER_PASSOWRD,
                    AdminMessage.TYPE_ERROR );
            url = new UrlItem( strRedirectUrl );
        }
        else
        {
            String strNextUrl = AdminAuthenticationService.getInstance(  ).getLoginNextUrl( request );

            if ( StringUtils.isNotBlank( strNextUrl ) )
            {
                url = new UrlItem( strNextUrl );
            }
            else
            {
                url = AppPathService.resolveRedirectUrl( request, AppPathService.getAdminMenuUrl(  ) );
            }
        }

        return url.getUrl(  );
    }

    /**
     * Process the sending to user password
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     * @throws Exception The exception
     */
    public String doForgotPassword( HttpServletRequest request )
        throws Exception
    {
        // get mail from user
        String strAccessCode = request.getParameter( Parameters.ACCESS_CODE );
        Locale locale = AdminUserService.getLocale( request );

        if ( ( strAccessCode == null ) || strAccessCode.equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( locale == null )
        {
            locale = LocaleService.getDefault(  );
        }

        // if user or mail not found, send admin message
        AdminUser user = AdminUserHome.findUserByLogin( strAccessCode );

        if ( ( user == null ) || ( user.getEmail(  ) == null ) || user.getEmail(  ).equals( CONSTANT_EMPTY_STRING ) )
        {
            return JSP_URL_FORM_CONTACT;
        }

        // make password
        String strPassword = AdminUserService.makePassword(  );

        // update password
        if ( ( strPassword != null ) && !strPassword.equals( CONSTANT_EMPTY_STRING ) )
        {
            // Encrypted password
            String strEncryptedPassword = AdminUserService.encryptPassword( strPassword );

            LuteceDefaultAdminUser userStored = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId(  ) );
            userStored.setPasswordMaxValidDate( AdminUserService.getPasswordMaxValidDate(  ) );
            userStored.setPassword( strEncryptedPassword );

            if ( Boolean.valueOf( DefaultUserParameterHome.findByKey( PARAMETER_FORCE_CHANGE_PASSWORD_REINIT ) ) )
            {
                userStored.setPasswordReset( Boolean.TRUE );
            }

            AdminUserHome.update( userStored );

            // AdminUserHome.insertNewPasswordInHistory( strEncryptedPassword, userStored.getUserId( ) );
        }

        // send password by e-mail
        String strSenderEmail = MailService.getNoReplyEmail(  );
        String strEmailSubject = I18nService.getLocalizedString( MESSAGE_EMAIL_SUBJECT, locale );
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_NEW_PASSWORD, strPassword );
        model.put( MARK_LOGIN_URL,
            AppPathService.getBaseUrl( request ) + AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ) );
        model.put( MARK_SITE_LINK, MailService.getSiteLink( AppPathService.getBaseUrl( request ), false ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_EMAIL_FORGOT_PASSWORD, locale, model );

        MailService.sendMailHtml( user.getEmail(  ), strSenderEmail, strSenderEmail, strEmailSubject,
            template.getHtml(  ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_SENDING_SUCCESS, JSP_URL_ADMIN_LOGIN,
            AdminMessage.TYPE_INFO );
    }

    /**
     * Process the sending of the login
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     * @throws Exception The exception
     */
    public String doForgotLogin( HttpServletRequest request )
        throws Exception
    {
        String strEmail = request.getParameter( Parameters.EMAIL );
        Locale locale = AdminUserService.getLocale( request );

        if ( ( strEmail == null ) || strEmail.equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !AdminUserService.checkEmail( strEmail ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_EMAIL_FORMAT, AdminMessage.TYPE_STOP );
        }

        if ( locale == null )
        {
            locale = LocaleService.getDefault(  );
        }

        // if access code not found, send admin message
        String strAccessCode = AdminUserHome.findUserByEmail( strEmail );

        if ( StringUtils.isEmpty( strAccessCode ) )
        {
            return JSP_URL_FORM_CONTACT;
        }

        // send access code by e-mail
        String strSenderEmail = MailService.getNoReplyEmail(  );
        String strEmailSubject = I18nService.getLocalizedString( MESSAGE_FORGOT_LOGIN_EMAIL_SUBJECT, locale );
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LOGIN, strAccessCode );
        model.put( MARK_LOGIN_URL,
            AppPathService.getBaseUrl( request ) + AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ) );
        model.put( MARK_SITE_LINK, MailService.getSiteLink( AppPathService.getBaseUrl( request ), false ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_EMAIL_FORGOT_LOGIN, locale, model );

        MailService.sendMailHtml( strEmail, strSenderEmail, strSenderEmail, strEmailSubject, template.getHtml(  ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_FORGOT_LOGIN_SENDING_SUCCESS, AdminMessage.TYPE_INFO );
    }

    /**
     * Send the message to the adminsitrator(s)
     * @param request The {@link HttpServletRequest}
     * @return an adminMessage
     */
    public String doFormContact( HttpServletRequest request )
    {
        // Get message, check if empty
        String strMessage = request.getParameter( PARAMETER_MESSAGE );

        if ( ( strMessage == null ) || strMessage.equals( CONSTANT_EMPTY_STRING ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Locale locale = AdminUserService.getLocale( request );

        if ( locale == null )
        {
            locale = LocaleService.getDefault(  );
        }

        // send mail to admin wich have level
        int nIdLevel = 0;
        String strLevelId = AppPropertiesService.getProperty( PROPERTY_LEVEL, "0" );

        if ( ( strLevelId != null ) && strLevelId.matches( REGEX_ID ) )
        {
            nIdLevel = Integer.parseInt( strLevelId );
        }

        Collection<AdminUser> adminUserList = AdminUserHome.findByLevel( nIdLevel );
        StringBuilder sbMailsTo = new StringBuilder( CONSTANT_EMPTY_STRING );

        for ( AdminUser adminUser : adminUserList )
        {
            if ( StringUtil.checkEmail( adminUser.getEmail(  ) ) )
            {
                sbMailsTo.append( adminUser.getEmail(  ) ).append( CONSTANT_EMAIL_DELIMITER );
            }
        }

        String strMailsTo = sbMailsTo.toString(  );

        if ( !strMailsTo.equals( CONSTANT_EMPTY_STRING ) )
        {
            String strSenderEmail = MailService.getNoReplyEmail(  );
            String strEmailSubject = I18nService.getLocalizedString( MESSAGE_EMAIL_ADMIN_SUBJECT, locale );

            MailService.sendMailHtml( strMailsTo, strSenderEmail, strSenderEmail, strEmailSubject, strMessage );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_ADMIN_SENDING_SUCCESS,
            AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ), AdminMessage.TYPE_INFO );
    }

    /**
     * Process the logout of user
     *
     * @param request Http request
     * @return The Jsp URL of the process result
     */
    public String doLogout( HttpServletRequest request )
    {
        // Invalidation of the session
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            session.invalidate(  );
        }

        String strLoginUrl = AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  );

        return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_LOGOUT, strLoginUrl, AdminMessage.TYPE_INFO );
    }
}
