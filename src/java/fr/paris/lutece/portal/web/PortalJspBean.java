/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.init.AppInit;
import fr.paris.lutece.portal.service.message.ISiteMessageHandler;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides the methods to display the page of the site
 */
public class PortalJspBean
{
    public static final int MODE_HTML = 0;
    public static final int MODE_ADMIN = 1;
    private static final String TEMPLATE_POPUP_CREDITS = "skin/site/popup_credits.html";
    private static final String TEMPLATE_POPUP_LEGAL_INFO = "skin/site/popup_legal_info.html";
    private static final String TEMPLATE_STARTUP_FAILURE = "skin/site/startup_failure.html";
    private static final String MARK_PORTAL_DOMAIN = "portal_domain";
    private static final String MARK_ADDRESS_INFOS_CNIL = "confidentiality_info";
    private static final String MARK_APP_VERSION = "app_version";
    private static final String PROPERTY_INFOS_CNIL = "lutece.legal.infos";
    private static final String PROPERTY_PORTAL_DOMAIN = "lutece.name";
    private static final String ATTRIBUTE_LOGIN_NEXT_URL = "luteceLoginNextUrl";
    private static final String ATTRIBUTE_UPLOAD_FILTER_SITE_NEXT_URL = "uploadFilterSiteNextUrl";
    private static final String MARK_FAILURE_MESSAGE = "failure_message";
    private static final String MARK_FAILURE_DETAILS = "failure_details";
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
        if ( !AppInit.isWebappSuccessfullyLoaded(  ) )
        {
            return getStartUpFailurePage(  );
        }

        // Try to register the user in case of external authentication
        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            if ( SecurityService.getInstance(  ).isExternalAuthentication(  ) &&
                    !SecurityService.getInstance(  ).isMultiAuthenticationSupported(  ) )
            {
                // The authentication is external
                // Should register the user if it's not already done
                if ( SecurityService.getInstance(  ).getRegisteredUser( request ) == null )
                {
                    if ( ( SecurityService.getInstance(  ).getRemoteUser( request ) == null ) &&
                            ( SecurityService.getInstance(  ).isPortalAuthenticationRequired(  ) ) )
                    {
                        // Authentication is required to access to the portal
                        throw new UserNotSignedException(  );
                    }
                }
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
                        try
                        {
                            user = SecurityService.getInstance(  ).getRemoteUser( request );
                        }
                        catch ( UserNotSignedException unse )
                        {
                            // nothing to do, there might be another authentication provider or login page to display
                        }
                    }

                    //If portal authentication is enabled and user is null and the requested URL 
                    //is not the login URL, user cannot access to Portal
                    if ( SecurityService.getInstance(  ).isPortalAuthenticationRequired(  ) && ( user == null ) &&
                            !SecurityService.getInstance(  ).isLoginUrl( request ) )
                    {
                        // Authentication is required to access to the portal
                        throw new UserNotSignedException(  );
                    }
                }
            }
        }

        ISiteMessageHandler handler = (ISiteMessageHandler) SpringContextService.getBean( BEAN_SITE_MESSAGE_HANDLER );

        if ( handler.hasMessage( request ) )
        {
            return handler.getPage( request, nMode );
        }

        // Search the content service invoked and call its getPage method
        ContentService cs = PortalService.getInvokedContentService( request );

        if ( cs != null )
        {
            return cs.getPage( request, nMode );
        }

        return PortalService.getDefaultPage( request, nMode );
    }

    /**
     * Returns the code for the popup of the credits
     *
     * @return the html code for the popup credits
     */
    public String getStartUpFailurePage(  )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_FAILURE_MESSAGE, AppInit.getLoadingFailureCause(  ) );
        model.put( MARK_FAILURE_DETAILS, AppInit.getLoadingFailureDetails(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_STARTUP_FAILURE, null, model );

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
        model.put( MARK_APP_VERSION, AppInfo.getVersion(  ) );
        model.put( MARK_PORTAL_DOMAIN, AppPropertiesService.getProperty( PROPERTY_PORTAL_DOMAIN ) );

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
        model.put( MARK_ADDRESS_INFOS_CNIL, AppPropertiesService.getProperty( PROPERTY_INFOS_CNIL ) );
        model.put( MARK_PORTAL_DOMAIN, AppPropertiesService.getProperty( PROPERTY_PORTAL_DOMAIN ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_POPUP_LEGAL_INFO, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * This method is called by Portal.jsp when it caught an UserNotSignedException.
     * It gives the login url and stores in the session the url asked
     * @param request The HTTP request
     * @return The login page URL
     * @since v1.1
     */
    public static String redirectLogin( HttpServletRequest request )
    {
        String strNextUrl = request.getRequestURI(  );
        UrlItem url = new UrlItem( strNextUrl );
        Enumeration enumParams = request.getParameterNames(  );

        while ( enumParams.hasMoreElements(  ) )
        {
            String strParamName = (String) enumParams.nextElement(  );
            url.addParameter( strParamName, request.getParameter( strParamName ) );
        }

        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_LOGIN_NEXT_URL, url.getUrl(  ) );

        String strRedirect = SecurityService.getInstance(  ).getLoginPageUrl(  );

        return strRedirect;
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
        Enumeration enumParams = request.getParameterNames(  );

        while ( enumParams.hasMoreElements(  ) )
        {
            String strParamName = (String) enumParams.nextElement(  );
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
}
