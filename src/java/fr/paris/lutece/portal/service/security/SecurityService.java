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
package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import java.security.Principal;

import java.util.Collection;
import java.util.Enumeration;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides a security service to register and check user
 * authentication
 */
public final class SecurityService
{
    /**
     * Session attribute that stores the LuteceUser object attached to the
     * session
     */
    private static final String ATTRIBUTE_LUTECE_USER = "lutece_user";
    private static final String PROPERTY_AUTHENTICATION_CLASS = "mylutece.authentication.class";
    private static final String PROPERTY_AUTHENTICATION_ENABLE = "mylutece.authentication.enable";
    private static final String PROPERTY_PORTAL_AUTHENTICATION_REQUIRED = "mylutece.portal.authentication.required";
    private static final String URL_INTERROGATIVE = "?";
    private static final String URL_AMPERSAND = "&";
    private static final String URL_EQUAL = "=";
    private static SecurityService _singleton = new SecurityService(  );
    private static LuteceAuthentication _authenticationService;
    private static boolean _bEnable;

    /**
     * Private constructor
     */
    private SecurityService(  )
    {
    }

    /**
     * Initialize service
     * @throws LuteceInitException if an error occurs
     */
    public static synchronized void init(  ) throws LuteceInitException
    {
        _bEnable = false;

        String strEnable = AppPropertiesService.getProperty( PROPERTY_AUTHENTICATION_ENABLE, "false" );

        if ( strEnable.equalsIgnoreCase( "true" ) )
        {
            _authenticationService = getPortalAuthentication(  );

            if ( _authenticationService != null )
            {
                _bEnable = true;
            }
        } else
        {
            // in case authentication is disabled after having been enabled
            _authenticationService = null;
        }
    }

    /**
     * Get the unique instance of the Security Service
     * @return The instance
     */
    public static SecurityService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the authentication's activation : enable or disable
     * @return true if the authentication is active, false otherwise
     */
    public static boolean isAuthenticationEnable(  )
    {
        return _bEnable;
    }

    /**
     * Gets the LuteceUser attached to the current Http session
     * @param request The Http request
     * @return A LuteceUser object if found
     * @throws UserNotSignedException If there is no current user
     */
    public LuteceUser getRemoteUser( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser user = getRegisteredUser( request );

        if ( user == null )
        {
            // User is not registered by Lutece, but it may be authenticated by another system
            if ( _authenticationService.isExternalAuthentication(  ) ||
                    _authenticationService.isMultiAuthenticationSupported(  ) )
            {
                user = _authenticationService.getHttpAuthenticatedUser( request );

                if ( ( user == null ) && isPortalAuthenticationRequired(  ) )
                {
                    throw new UserNotSignedException(  );
                }

                registerUser( request, user );
            }
            else
            {
                throw new UserNotSignedException(  );
            }
        }

        return user;
    }

    /**
     * Returns the user's principal
     * @param request The HTTP request
     * @return The user's principal
     * @throws UserNotSignedException The UserNotSignedException
     */
    public Principal getUserPrincipal( HttpServletRequest request )
        throws UserNotSignedException
    {
        return getRemoteUser( request );
    }

    /**
     * Checks if the user is associated to a given role
     * @param request The Http request
     * @param strRole The Role name
     * @return Returns true if the user is associated to the given role
     */
    public boolean isUserInRole( HttpServletRequest request, String strRole )
    {
        LuteceUser user;

        try
        {
            user = getRemoteUser( request );
        }
        catch ( UserNotSignedException e )
        {
            return false;
        }

        return _authenticationService.isUserInRole( user, request, strRole );
    }

    /**
     * get all roles for this user : - user's roles - user's groups roles
     *
     * @param user The user
     * @return Array of roles
     */
    public String[] getRolesByUser( LuteceUser user )
    {
        return _authenticationService.getRolesByUser( user );
    }

    /**
     * Checks user's login with the Authentication service.
     * @param request The Http request
     * @param strUserName The user's login
     * @param strPassword The user's password
     * @throws LoginException The LoginException
     * @throws LoginRedirectException if redirect exception
     */
    public void loginUser( HttpServletRequest request, final String strUserName, final String strPassword )
        throws LoginException, LoginRedirectException
    {
        LuteceUser user = _authenticationService.login( strUserName, strPassword, request );
        _authenticationService.updateDateLastLogin( user, request );

        if ( _authenticationService.findResetPassword( request, strUserName ) )
        {
            String redirect = _authenticationService.getResetPasswordPageUrl( request );
            registerUser( request, user );
            throw new LoginRedirectException( redirect );
        }

        registerUser( request, user );
    }

    /**
     * Logout the user
     * @param request The HTTP request
     */
    public void logoutUser( HttpServletRequest request )
    {
        LuteceUser user;

        try
        {
            user = getRemoteUser( request );
        }
        catch ( UserNotSignedException e )
        {
            return;
        }

        _authenticationService.logout( user );
        unregisterUser( request );
    }

    /**
     * Retrieves the portal authentication service configured in the
     * config.properties
     * @return A PortalAuthentication object
     * @throws LuteceInitException If an error occurred
     */
    private static LuteceAuthentication getPortalAuthentication(  )
        throws LuteceInitException
    {
        String strAuthenticationClass = AppPropertiesService.getProperty( PROPERTY_AUTHENTICATION_CLASS );
        LuteceAuthentication authentication = null;

        if ( ( strAuthenticationClass != null ) && !strAuthenticationClass.equals( "" ) )
        {
            try
            {
                authentication = (LuteceAuthentication) Class.forName( strAuthenticationClass ).newInstance(  );
                AppLogService.info( "Authentication service loaded : " + authentication.getAuthServiceName(  ) );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( "Error instantiating Authentication Class", e );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( "Error instantiating Authentication Class", e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( "Error instantiating Authentication Class", e );
            }
        }

        return authentication;
    }

    /**
     * Register the user in the Http session
     * @param request The Http request
     * @param user The current user
     */
    public void registerUser( HttpServletRequest request, LuteceUser user )
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_LUTECE_USER, user );
    }

    /**
     * Unregister the user in the Http session
     * @param request The Http request
     */
    public void unregisterUser( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );
        session.removeAttribute( ATTRIBUTE_LUTECE_USER );
    }

    /**
     * Gets the Lutece user registered in the Http session
     * @param request The HTTP request
     * @return The User registered or null if the user has not been registered
     */
    public LuteceUser getRegisteredUser( HttpServletRequest request )
    {
        HttpSession session = ( request != null ) ? request.getSession( false ) : null;

        if ( session != null )
        {
            return (LuteceUser) session.getAttribute( ATTRIBUTE_LUTECE_USER );
        }

        return null;
    }

    /**
     * Returns the authentication type : External or Lutece portal based
     * @return true if the user is already authenticated or false if it needs to
     *         login.
     */
    public boolean isExternalAuthentication(  )
    {
        return _authenticationService.isExternalAuthentication(  );
    }

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    public String getLoginPageUrl(  )
    {
        return _authenticationService.getLoginPageUrl(  );
    }

    /**
     * Returns the DoLogin URL of the Authentication Service
     * @return The URL
     */
    public String getDoLoginUrl(  )
    {
        return _authenticationService.getDoLoginUrl(  );
    }

    /**
     * Returns the DoLogout URL of the Authentication Service
     * @return The URL
     */
    public String getDoLogoutUrl(  )
    {
        return _authenticationService.getDoLogoutUrl(  );
    }

    /**
     * Returns the new account page URL of the Authentication Service
     * @return The URL
     */
    public String getNewAccountPageUrl(  )
    {
        return _authenticationService.getNewAccountPageUrl(  );
    }

    /**
     * Returns the view account page URL of the Authentication Service
     * @return The URL
     */
    public String getViewAccountPageUrl(  )
    {
        return _authenticationService.getViewAccountPageUrl(  );
    }

    /**
     * Returns the lost password URL of the Authentication Service
     * @return The URL
     */
    public String getLostPasswordPageUrl(  )
    {
        return _authenticationService.getLostPasswordPageUrl(  );
    }

    // Added in v1.3

    /**
     * Returns the access denied template
     * @return The template
     */
    public String getAccessDeniedTemplate(  )
    {
        return _authenticationService.getAccessDeniedTemplate(  );
    }

    /**
     * Returns the access controled template
     * @return The template
     */
    public String getAccessControledTemplate(  )
    {
        return _authenticationService.getAccessControledTemplate(  );
    }

    /**
     * Returns whether or not the portal needs authentication
     * @return true if the access needs authentication, otherwise
     * @since 1.3.1
     */
    public boolean isPortalAuthenticationRequired(  )
    {
        String strAuthenticationRequired = DatastoreService.getDataValue( PROPERTY_PORTAL_AUTHENTICATION_REQUIRED,
                "false" );

        return strAuthenticationRequired.equals( "true" );
    }

    /**
     * Checks user's login with the Authentication service. Used during remote
     * authentication validation We don't have to put user informations in
     * session, since it is only used in external
     * applications
     * @param request the request
     * @param strUserName The user's login
     * @param strPassword The user's password
     * @return user's informations
     * @throws LoginException The LoginException
     * @throws LoginRedirectException The redirect exception
     */
    public LuteceUser remoteLoginUser( final HttpServletRequest request, final String strUserName,
        final String strPassword ) throws LoginException, LoginRedirectException
    {
        LuteceUser user = _authenticationService.login( strUserName, strPassword, request );

        return user;
    }

    /**
     * Return true if the requested url is equal to LoginUrl
     * @param request The Http servlet request
     * @return True if the requested url is equal to LoginUrl, false else.
     */
    public boolean isLoginUrl( HttpServletRequest request )
    {
        if ( ( getLoginPageUrl(  ) == null ) || ( request == null ) )
        {
            return false;
        }

        String strRequestUrl = request.getRequestURI(  );
        UrlItem url = new UrlItem( strRequestUrl );

        for ( String strParamValueLoginPageUrl : getLoginPageUrl(  )
                                                     .substring( getLoginPageUrl(  ).indexOf( URL_INTERROGATIVE ) + 1 )
                                                     .split( URL_AMPERSAND ) )
        {
            String[] arrayParamValueLoginPageUrl = strParamValueLoginPageUrl.split( URL_EQUAL );
            Enumeration<String> enumParams = request.getParameterNames(  );

            while ( enumParams.hasMoreElements(  ) )
            {
                String strRequestParameter = (String) enumParams.nextElement(  );

                if ( arrayParamValueLoginPageUrl[0].equals( strRequestParameter ) &&
                        arrayParamValueLoginPageUrl[1].equals( request.getParameter( strRequestParameter ) ) )
                {
                    url.addParameter( strRequestParameter, request.getParameter( strRequestParameter ) );
                }
            }
        }

        if ( url.getUrl(  ).endsWith( getLoginPageUrl(  ) ) && !getLoginPageUrl(  ).equals( "" ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Tells whether or not the authentication service can provide a list of all
     * its users
     * @return true if the service can return a users list
     */
    boolean isUsersListAvailable(  )
    {
        return _authenticationService.isUsersListAvailable(  );
    }

    /**
     * Returns all users managed by the authentication service if this feature
     * is available.
     * @return A collection of Lutece users or null if the service doesn't
     *         provide a users list
     */
    public Collection<LuteceUser> getUsers(  )
    {
        return _authenticationService.getUsers(  );
    }

    /**
     * Returns user managed by the authentication service if this feature is
     * available.
     * @param strUserLogin the user login
     * @return A Lutece user or null if the service doesn't provide LuteceUser
     */
    public LuteceUser getUser( String strUserLogin )
    {
        return _authenticationService.getUser( strUserLogin );
    }

    /**
     * <b>true</b> when the service provides multi authentication support
     * @return <code>true</code> if multi authentication is supported,
     *         <code>false</code> otherwise.
     */
    public boolean isMultiAuthenticationSupported(  )
    {
        return _authenticationService.isMultiAuthenticationSupported(  );
    }

    /**
     * Gets the actual authentication implementation
     * @return {@link LuteceAuthentication} implementation
     */
    public LuteceAuthentication getAuthenticationService(  )
    {
        return _authenticationService;
    }
}
