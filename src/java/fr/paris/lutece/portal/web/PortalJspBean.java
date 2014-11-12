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

import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.content.ContentPostProcessorService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.editor.EditorBbcodeService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.init.AppInit;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.ISiteMessageHandler;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portal.ThemesService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides the methods to display the page of the site
 */
public class PortalJspBean
{
    public static final int MODE_HTML = 0;
    public static final int MODE_ADMIN = 1;
    private static final String TEMPLATE_PAGE_ERROR404 = "skin/site/page_error404.html";
    private static final String TEMPLATE_PAGE_ERROR500 = "skin/site/page_error500.html";
    private static final String TEMPLATE_POPUP_CREDITS = "skin/site/popup_credits.html";
    private static final String TEMPLATE_POPUP_LEGAL_INFO = "skin/site/popup_legal_info.html";
    private static final String TEMPLATE_STARTUP_FAILURE = "skin/site/startup_failure.html";
    private static final String TEMPLATE_SEND_RESOURCE = "skin/site/popup_send_resource.html";
    private static final String TEMPLATE_EMAIL_SEND_RESOURCE = "skin/site/mail_send_resource.html";
    private static final String PROPERTY_INFOS_CNIL = "lutece.legal.infos";
    private static final String ATTRIBUTE_LOGIN_NEXT_URL = "luteceLoginNextUrl";
    private static final String ATTRIBUTE_UPLOAD_FILTER_SITE_NEXT_URL = "uploadFilterSiteNextUrl";
    private static final String MARK_PORTAL_DOMAIN = "portal_domain";
    private static final String MARK_ADDRESS_INFOS_CNIL = "confidentiality_info";
    private static final String MARK_APP_VERSION = "app_version";
    private static final String MARK_FAILURE_MESSAGE = "failure_message";
    private static final String MARK_FAILURE_DETAILS = "failure_details";
    private static final String MARK_RESOURCE_URL = "resource_url";
    private static final String MARK_RESOURCE = "resource";
    private static final String MARK_ERROR = "error";
    private static final String MARK_SUCCESS = "success";
    private static final String MARK_ERROR_CAUSE = "error_cause";
    private static final String MARK_PLUGIN_THEME = "plugin_theme";
    private static final String MARK_THEME = "theme";
    private static final String BEAN_SITE_MESSAGE_HANDLER = "siteMessageHandler";
    private static final String PARAMETER_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    private static final String PARAMETER_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    private static final String PARAMETER_SENDER_NAME = "senderName";
    private static final String PARAMETER_SENDER_FIRST_NAME = "senderFirstname";
    private static final String PARAMETER_SENDER_EMAIL = "senderEmail";
    private static final String PARAMETER_CONTENT = "content";
    private static final String PARAMETER_SEND = "send";
    private static final String MESSAGE_ERROR_WRONG_SENDER_EMAIL = "portal.site.error.wrongEmailFormat";
    private static final String MESSAGE_ERROR_MANDATORY_FIELDS = "portal.util.message.mandatoryFields";
    private static final String MESSAGE_NO_RESOURCE_FOUND = "portal.site.error.noResourceFound";
    private static final String PROPERTY_PAGE_TITLE_ERROR404 = "portal.util.error404.title";
    private static final String PROPERTY_PAGE_TITLE_CREDITS = "portal.site.popup_credits.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_LEGAL_INFO = "portal.site.popup_legal_info.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_STARTUP_FAILURE = "portal.util.startup.failure.title";
    private static final String PROPERTY_PAGE_TITLE_ERROR500 = "portal.util.error500.title";
    private static final String PROPERTY_DEBUG = "error.page.debug";
    private static final String PROPERTY_DEBUG_DEFAULT = "true";
    private static final String CONSTANT_SPACE = " ";
    private static final String KEY_WEBMASTER_EMAIL = "portal.site.site_property.noreply_email";

    /**
     * Returns the content of a page according to the parameters found in the
     * http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public String getContent( HttpServletRequest request )
        throws UserNotSignedException, SiteMessageException
    {
        return getContent( request, MODE_HTML );
    }

    /**
     * Returns the content of a page according to the parameters found in the
     * http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @param nMode The mode (normal or administration)
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be
     *             displayed
     */
    public String getContent( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException
    {
        if ( !AppInit.isWebappSuccessfullyLoaded(  ) )
        {
            return getStartUpFailurePage( request );
        }

        // Try to register the user in case of external authentication
        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            try
            {
                if ( SecurityService.getInstance(  ).isExternalAuthentication(  ) &&
                        !SecurityService.getInstance(  ).isMultiAuthenticationSupported(  ) )
                {
                    SecurityService.getInstance(  ).getRemoteUser( request );
                }
                else
                {
                    LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

                    // no checks are needed if the user is already registered
                    if ( user == null )
                    {
                        // if multiauthentication is supported, then when have to check remote user before other check
                        if ( SecurityService.getInstance(  ).isMultiAuthenticationSupported(  ) )
                        {
                            // getRemoteUser needs to be checked before any check so the user is registered
                            // getRemoteUser throws an exception if no user found, but here we have to bypass this exception to display login page.
                            SecurityService.getInstance(  ).getRemoteUser( request );
                        }
                    }
                }
            }
            catch ( UserNotSignedException unse )
            {
                // nothing to do,Leave LuteceAuthenticationFilter testing if the access to the content requires authentication
            }
        }

        // Search the content service invoked and call its getPage method
        ContentService cs = PortalService.getInvokedContentService( request );

        String strContent = ( cs != null ) ? cs.getPage( request, nMode ) : PortalService.getDefaultPage( request, nMode );

        if ( ContentPostProcessorService.hasProcessor(  ) )
        {
            strContent = ContentPostProcessorService.process( request, strContent );
        }

        return strContent;
    }

    /**
     * Returns the content of a page according to the parameters found in the
     * http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @return the html code for the display of a page of a site
     *
     */
    public String getSiteMessageContent( HttpServletRequest request )
    {
        return getSiteMessageContent( request, MODE_HTML );
    }

    /**
     * Returns the content of a page according to the parameters found in the
     * http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @param nMode The mode (normal or administration)
     * @return the html code for the display of a page of a site
     *
     */
    public String getSiteMessageContent( HttpServletRequest request, int nMode )
    {
        String strContent = null;

        if ( !AppInit.isWebappSuccessfullyLoaded(  ) )
        {
            return getStartUpFailurePage( request );
        }

        ISiteMessageHandler handler = (ISiteMessageHandler) SpringContextService.getBean( BEAN_SITE_MESSAGE_HANDLER );

        if ( handler.hasMessage( request ) )
        {
            strContent = handler.getPage( request, nMode );
        }

        return strContent;
    }

    /**
     * Returns the code for the popup of the credits
     * @param request The HTTP request
     * @return the html code for the popup credits
     */
    public String getStartUpFailurePage( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        fillPageModel( request, model );
        model.put( MARK_FAILURE_MESSAGE, AppInit.getLoadingFailureCause(  ) );
        model.put( MARK_FAILURE_DETAILS, AppInit.getLoadingFailureDetails(  ) );
        model.put( Markers.PAGE_TITLE,
            I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_STARTUP_FAILURE, request.getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_STARTUP_FAILURE, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the code for the popup of the credits
     *
     * @param request The Http Request
     * @return the html code for the popup credits
     */
    public String getCredits( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        fillPageModel( request, model );
        model.put( MARK_APP_VERSION, AppInfo.getVersion(  ) );
        model.put( MARK_PORTAL_DOMAIN, PortalService.getSiteName(  ) );
        model.put( Markers.PAGE_TITLE,
            I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_CREDITS, request.getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_POPUP_CREDITS, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the code for the popup of the legal infos
     *
     * @param request The Http Request
     * @return the html code for the legal infos
     */
    public String getLegalInfos( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        fillPageModel( request, model );
        model.put( MARK_ADDRESS_INFOS_CNIL, AppPropertiesService.getProperty( PROPERTY_INFOS_CNIL ) );
        model.put( MARK_PORTAL_DOMAIN, PortalService.getSiteName(  ) );
        model.put( Markers.PAGE_TITLE,
            I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_LEGAL_INFO, request.getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_POPUP_LEGAL_INFO, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the 404 Error page
     * @param request The HTTP request
     * @return The page
     */
    public String getError404Page( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        fillPageModel( request, model );
        model.put( Markers.PAGE_TITLE,
            I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_ERROR404, request.getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_ERROR404, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the 500 Error page
     * @param request The HTTP request
     * @param exception The Exception
     * @return The page
     */
    public String getError500Page( HttpServletRequest request, Throwable exception )
    {
        AppLogService.error( "Error 500 : " + exception.getMessage(  ), exception );

        String strCause = null;

        if ( AppPropertiesService.getProperty( PROPERTY_DEBUG, PROPERTY_DEBUG_DEFAULT ).equalsIgnoreCase( "true" ) )
        {
            strCause = exception.getMessage(  );

            if ( exception.getCause(  ) != null )
            {
                strCause += exception.getCause(  ).getMessage(  );
            }
        }

        return getError500Page( request, strCause );
    }

    /**
     * Returns the 500 Error page
     * @param request The HTTP request
     * @param strCause The message
     * @return The page
     */
    public String getError500Page( HttpServletRequest request, String strCause )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        fillPageModel( request, model );
        model.put( Markers.PAGE_TITLE,
            I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_ERROR500, request.getLocale(  ) ) );
        model.put( MARK_ERROR_CAUSE, strCause );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_ERROR500, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Fill the model'map with commons values
     * @param request The HTTP request
     * @param model The map containing the model
     */
    private static void fillPageModel( HttpServletRequest request, HashMap<String, Object> model )
    {
        model.put( Markers.BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_PLUGIN_THEME, null );
        model.put( MARK_THEME, ThemesService.getGlobalThemeObject(  ) );
    }

    /**
     * This method is called by Portal.jsp when it caught an
     * UserNotSignedException.
     * It gives the login url and stores in the session the url asked
     * @param request The HTTP request
     * @return The login page URL
     * @since v1.1
     */
    public static String redirectLogin( HttpServletRequest request )
    {
        String strNextUrl = request.getRequestURI(  );
        UrlItem url = new UrlItem( strNextUrl );
        Enumeration<String> enumParams = request.getParameterNames(  );

        while ( enumParams.hasMoreElements(  ) )
        {
            String strParamName = enumParams.nextElement(  );

            try
            {
                url.addParameter( strParamName, URLEncoder.encode( request.getParameter( strParamName ), "UTF-8" ) );
            }
            catch ( UnsupportedEncodingException ex )
            {
                AppLogService.error( "Redirection error while encoding URL : " + ex.getMessage(  ), ex );
            }
        }

        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_LOGIN_NEXT_URL, url.getUrl(  ) );

        String strRedirect = SecurityService.getInstance(  ).getLoginPageUrl(  );

        return AppPathService.getAbsoluteUrl( request, strRedirect );
    }

    /**
     * Returns the url (asked before login) to redirect after login
     * @param request The Http request
     * @return The url asked before login
     * @since v1.1
     */
    public static String getLoginNextUrl( HttpServletRequest request )
    {
        HttpSession session = request.getSession(  );
        String strNextUrl = (String) session.getAttribute( ATTRIBUTE_LOGIN_NEXT_URL );

        return strNextUrl;
    }

    /**
     * Set the upload filter site next url
     * @param request the HTTP request
     */
    public static void setUploadFilterSiteNextUrl( HttpServletRequest request )
    {
        String strNextUrl = request.getRequestURI(  );
        UrlItem url = new UrlItem( strNextUrl );
        Enumeration<String> enumParams = request.getParameterNames(  );

        while ( enumParams.hasMoreElements(  ) )
        {
            String strParamName = enumParams.nextElement(  );
            url.addParameter( strParamName, request.getParameter( strParamName ) );
        }

        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_UPLOAD_FILTER_SITE_NEXT_URL, url.getUrl(  ) );
    }

    /**
     * Get the upload filter site next url
     * @param request the HTTP request
     * @return the next url
     */
    public static String getUploadFilterSiteNextUrl( HttpServletRequest request )
    {
        HttpSession session = request.getSession(  );
        String strNextUrl = (String) session.getAttribute( ATTRIBUTE_UPLOAD_FILTER_SITE_NEXT_URL );

        return strNextUrl;
    }

    /**
     * Remove the upload filter next url from the session
     * @param request the HTTP request
     */
    public static void removeUploadFilterSiteNextUrl( HttpServletRequest request )
    {
        HttpSession session = request.getSession(  );
        session.removeAttribute( ATTRIBUTE_UPLOAD_FILTER_SITE_NEXT_URL );
    }

    /**
     * Do send a resource
     * @param request The request
     * @return The HTML content to display
     * @throws SiteMessageException If the resource or its associated service is
     *             not found
     */
    public static String sendResource( HttpServletRequest request )
        throws SiteMessageException
    {
        String strSenderEmail = DatastoreService.getDataValue( KEY_WEBMASTER_EMAIL, "no-reply@mydomain.com" );
        String strSenderName = request.getParameter( PARAMETER_SENDER_NAME );
        String strSenderFirstName = request.getParameter( PARAMETER_SENDER_FIRST_NAME );
        String strReceipientEmail = request.getParameter( Parameters.EMAIL );
        String strContent = request.getParameter( PARAMETER_CONTENT );
        String strExtendableResourceType = request.getParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE );
        String strIdExtendableResource = request.getParameter( PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strSend = request.getParameter( PARAMETER_SEND );
        IExtendableResource resource = null;

        String strError = null;

        // If the form was submited, we check data
        if ( strSend != null )
        {
            if ( StringUtils.isBlank( strSenderEmail ) || StringUtils.isBlank( strSenderName ) ||
                    StringUtils.isBlank( strSenderFirstName ) || StringUtils.isBlank( strReceipientEmail ) ||
                    StringUtils.isBlank( strContent ) )
            {
                strError = I18nService.getLocalizedString( MESSAGE_ERROR_MANDATORY_FIELDS, request.getLocale(  ) );
            }

            if ( ( strError != null ) &&
                    ( !AdminUserService.checkEmail( strSenderEmail ) ||
                    !AdminUserService.checkEmail( strReceipientEmail ) ) )
            {
                strError = I18nService.getLocalizedString( MESSAGE_ERROR_WRONG_SENDER_EMAIL, request.getLocale(  ) );
            }
        }

        // We get the resource from its resource service
        IExtendableResourceService resourceService = null;
        List<IExtendableResourceService> listExtendableResourceService = SpringContextService.getBeansOfType( IExtendableResourceService.class );

        for ( IExtendableResourceService extendableResourceService : listExtendableResourceService )
        {
            if ( extendableResourceService.isInvoked( strExtendableResourceType ) )
            {
                resourceService = extendableResourceService;
                resource = extendableResourceService.getResource( strIdExtendableResource, strExtendableResourceType );
            }
        }

        if ( ( resourceService == null ) || ( resource == null ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_NO_RESOURCE_FOUND, SiteMessage.TYPE_ERROR );
            throw new SiteMessageException(  );
        }

        String strResourceUrl = resourceService.getResourceUrl( strIdExtendableResource, strExtendableResourceType );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_RESOURCE, resource );
        model.put( MARK_RESOURCE_URL, strResourceUrl );
        model.put( Markers.BASE_URL, AppPathService.getBaseUrl( request ) );

        if ( ( strSend != null ) && ( strError == null ) )
        {
            Map<String, Object> mailModel = new HashMap<String, Object>(  );
            mailModel.put( Markers.BASE_URL, AppPathService.getBaseUrl( request ) );
            mailModel.put( MARK_RESOURCE, resource );
            mailModel.put( PARAMETER_SENDER_EMAIL, strSenderEmail );
            mailModel.put( PARAMETER_SENDER_NAME, strSenderName );
            mailModel.put( PARAMETER_SENDER_FIRST_NAME, strSenderFirstName );
            mailModel.put( Parameters.EMAIL, strReceipientEmail );
            mailModel.put( PARAMETER_CONTENT, EditorBbcodeService.getInstance(  ).parse( strContent ) );
            mailModel.put( MARK_RESOURCE_URL,
                resourceService.getResourceUrl( strIdExtendableResource, strExtendableResourceType ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EMAIL_SEND_RESOURCE,
                    request.getLocale(  ), mailModel );
            MailService.sendMailHtml( strReceipientEmail, strSenderFirstName + CONSTANT_SPACE + strSenderName,
                strSenderEmail, resource.getExtendableResourceName(  ), template.getHtml(  ) );
            model.put( MARK_SUCCESS, MARK_SUCCESS );
        }
        else
        {
            model.put( PARAMETER_SENDER_NAME, strSenderName );
            model.put( PARAMETER_SENDER_FIRST_NAME, strSenderFirstName );
            model.put( Parameters.EMAIL, strReceipientEmail );
            model.put( PARAMETER_CONTENT, strContent );
            model.put( MARK_ERROR, strError );
        }

        model.put( Markers.PAGE_MAIN_MENU, StringUtils.EMPTY );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SEND_RESOURCE, request.getLocale(  ), model );

        return template.getHtml(  );
    }
}
