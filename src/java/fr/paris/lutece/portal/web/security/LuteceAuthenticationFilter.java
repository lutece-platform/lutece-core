/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.portal.web.security;

import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.PublicUrlService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.PortalJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.url.UrlItem;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Filter to prevent unauthenticated access to site if site authentication is enabled
 */
public class LuteceAuthenticationFilter implements Filter
{
    private static final String URL_INTERROGATIVE = "?";
    private static final String URL_AMPERSAND = "&";
    private static final String URL_EQUAL = "=";
    private static final String URL_STAR = "*";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig config ) throws ServletException
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy(  )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if ( SecurityService.isAuthenticationEnable(  ) &&
                SecurityService.getInstance(  ).isPortalAuthenticationRequired(  ) && isPrivateUrl( req ) )
        {
            try
            {
                filterAccess( req );
            }
            catch ( UserNotSignedException e )
            {
                if ( SecurityService.getInstance(  ).isExternalAuthentication(  ) &&
                        !SecurityService.getInstance(  ).isMultiAuthenticationSupported(  ) )
                {
                    try
                    {
                        SiteMessageService.setMessage( req, Messages.MESSAGE_USER_NOT_AUTHENTICATED, null,
                            Messages.MESSAGE_USER_NOT_AUTHENTICATED, null, "", SiteMessage.TYPE_STOP );
                    }
                    catch ( SiteMessageException lme )
                    {
                        resp.sendRedirect( AppPathService.getSiteMessageUrl( req ) );
                    }
                }
                else
                {
                    resp.sendRedirect( PortalJspBean.redirectLogin( req ) );
                }

                return;
            }
        }

        chain.doFilter( request, response );
    }

    /**
     * Check wether a given url is to be considered as private (ie that needs a
     * successful authentication to be accessed) or public (ie that can be
     * access without being authenticated)
     *
     * @param request
     *            the http request
     * @return true if the url needs to be authenticated, false otherwise
     *
     * */
    private boolean isPrivateUrl( HttpServletRequest request )
    {
        return !( ( isInSiteMessageUrl( request ) || ( isInPublicUrlList( request ) ) ) );
    }

    /**
     * check that the access is granted
     *
     * @param request
     *            The HTTP request
     *
     * @throws UserNotSignedException
     *             If the user is not signed
     *
     **/
    private static void filterAccess( HttpServletRequest request )
        throws UserNotSignedException
    {
        // Try to register the user in case of external authentication
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
                // if multiauthentication is supported, then when have to
                // check remote user before other check
                if ( SecurityService.getInstance(  ).isMultiAuthenticationSupported(  ) )
                {
                    // getRemoteUser needs to be checked before any check so
                    // the user is registered
                    // getRemoteUser throws an exception if no user found,
                    // but here we have to bypass this exception to display
                    // login page.
                    user = SecurityService.getInstance(  ).getRemoteUser( request );
                }

                // If portal authentication is enabled and user is null and
                // the requested URL
                // is not the login URL, user cannot access to Portal
                if ( user == null )
                {
                    // Authentication is required to access to the portal
                    throw new UserNotSignedException(  );
                }
            }
        }
    }

    /**
     * Checks if the requested is the url of site message
     * @param request The HTTP request
     * @return true if the requested is the url of site message
     */
    private boolean isInSiteMessageUrl( HttpServletRequest request )
    {
        return matchUrl( request, AppPathService.getSiteMessageUrl( request ) );
    }

    /**
     * Checks if the requested is in the list of urls defined in  Security service
     * that shouldn't be protected
     *
     * @param request
     *            the http request
    
     * @return true if the url is in the list, false otherwise
     *
     * */
    private boolean isInPublicUrlList( HttpServletRequest request )
    {
        for ( String strPubliUrl : PublicUrlService.getInstance(  ).getPublicUrls(  ) )
        {
            if ( matchUrl( request, strPubliUrl ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * method to test if the URL matches the pattern
    
     * @param request the request
     * @param strUrlPatern the pattern
     * @return true if the URL matches the pattern
     */
    private boolean matchUrl( HttpServletRequest request, String strUrlPatern )
    {
        boolean bMatch = false;

        if ( strUrlPatern != null )
        {
            UrlItem url = new UrlItem( getResquestedUrl( request ) );

            if ( strUrlPatern.contains( URL_INTERROGATIVE ) )
            {
                for ( String strParamPatternValue : strUrlPatern.substring( strUrlPatern.indexOf( URL_INTERROGATIVE ) +
                        1 ).split( URL_AMPERSAND ) )
                {
                    String[] arrayPatternParamValue = strParamPatternValue.split( URL_EQUAL );

                    if ( ( arrayPatternParamValue != null ) &&
                            ( request.getParameter( arrayPatternParamValue[0] ) != null ) )
                    {
                        url.addParameter( arrayPatternParamValue[0], request.getParameter( arrayPatternParamValue[0] ) );
                    }
                }
            }

            if ( strUrlPatern.contains( URL_STAR ) )
            {
                String strUrlPaternLeftEnd = strUrlPatern.substring( 0, strUrlPatern.indexOf( URL_STAR ) );
                String strAbsoluteUrlPattern = getAbsoluteUrl( request, strUrlPaternLeftEnd );
                bMatch = url.getUrl(  ).startsWith( strAbsoluteUrlPattern );
            }
            else
            {
                String strAbsoluteUrlPattern = getAbsoluteUrl( request, strUrlPatern );
                bMatch = url.getUrl(  ).equals( strAbsoluteUrlPattern );
            }
        }

        return bMatch;
    }

    /**
     * Returns the absolute url corresponding to the given one, if the later was
     * found to be relative. An url starting with "http://" is absolute. A
     * relative url should be given relatively to the webapp root.
     *
     * @param request
     *            the http request (provides the base path if needed)
     * @param strUrl
     *            the url to transform
     * @return the corresonding absolute url
     *
     * */
    private String getAbsoluteUrl( HttpServletRequest request, String strUrl )
    {
        if ( ( strUrl != null ) && !strUrl.startsWith( "http://" ) && !strUrl.startsWith( "https://" ) )
        {
            return AppPathService.getBaseUrl( request ) + strUrl;
        }
        else
        {
            return strUrl;
        }
    }

    /**
     * Return the absolute representation of the requested url
     *
     * @param request
     *            the http request (provides the base path if needed)
     * @return the requested url has a string
     *
     * */
    private String getResquestedUrl( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + request.getServletPath(  ).substring( 1 );
    }
}
