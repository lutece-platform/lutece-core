/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.service.admin;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.AdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Collection;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides a security service to register and check user authentication
 */
public final class AdminAuthenticationService
{
    /**
     * Session attribute that stores the AdminUser object attached to the session
     */
    private static final String ATTRIBUTE_ADMIN_USER = "lutece_admin_user";
    private static final String BEAN_ADMIN_AUTHENTICATION_MODULE = "adminAuthenticationModule";
    private static AdminAuthenticationService _singleton = new AdminAuthenticationService(  );
    private static AdminAuthentication _authentication;
    private static boolean _bUseDefaultModule;

    /**
     * Private constructor
     */
    private AdminAuthenticationService(  )
    {
    }

    /**
     * Initialize service
     * @throws LuteceInitException If error while initialization
     */
    public static synchronized void init(  ) throws LuteceInitException
    {
        _authentication = (AdminAuthentication) SpringContextService.getBean( BEAN_ADMIN_AUTHENTICATION_MODULE );
        AppLogService.info( "Authentication module loaded : " + _authentication.getAuthServiceName(  ) );

        if ( _authentication.getClass(  ).equals( LuteceDefaultAdminAuthentication.class ) )
        {
            _bUseDefaultModule = true;
        }
    }

    /**
     * Get the unique instance of the Security Service
     * @return The instance
     */
    public static AdminAuthenticationService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Check  whether the authentication service is configurer on the default module.
     * @return true if the default module is in use, false if another module is used
     */
    public boolean isDefaultModuleUsed(  )
    {
        return _bUseDefaultModule;
    }

    /**
     * Gets the AdminUser attached to the current Http session
     *
     * @return A valid AdminUser object if found
     * @param request The Http request
     * @throws AccessDeniedException If the user cannot have access
     * @throws UserNotSignedException If the user is not signed
     */
    public AdminUser getRemoteUser( HttpServletRequest request )
        throws UserNotSignedException, AccessDeniedException
    {
        AdminUser user = getRegisteredUser( request );

        if ( _authentication.isExternalAuthentication(  ) )
        {
            if ( user == null )
            {
                // User is not registered by Lutece, but it may be authenticated by another system
                user = _authentication.getHttpAuthenticatedUser( request );
                registerUser( request, user );

                // Start a new session
                throw new UserNotSignedException(  );
            }
            else
            {
                // user is already in session. for external auth, we check that the user hasn't changed in the external module .
                AdminUser newUser = _authentication.getHttpAuthenticatedUser( request );

                if ( newUser == null )
                {
                    throw new AccessDeniedException(  );
                }
                else if ( !newUser.getAccessCode(  ).equals( user.getAccessCode(  ) ) )
                {
                    unregisterUser( request );
                    registerUser( request, newUser );

                    // Start a new session
                    throw new UserNotSignedException(  );
                }
            }
        }
        else // if not external authentication, just check if user is null or not
        {
            if ( user == null )
            {
                // user is not signed
                throw new UserNotSignedException(  );
            }
        }

        return user;
    }

    /**
     * Checks user's login with the Authentication service.
     * @param request The Http request
     * @param strAccessCode The user's login
     * @param strPassword The user's password
     * @throws LoginException The LoginException
     */
    public void loginUser( HttpServletRequest request, final String strAccessCode, final String strPassword )
        throws LoginException
    {
        AdminUser user = _authentication.login( strAccessCode, strPassword, request );

        try
        {
            registerUser( request, user );
        }
        catch ( AccessDeniedException e )
        {
            throw new LoginException(  );
        }
        catch ( UserNotSignedException e )
        {
            throw new LoginException(  );
        }
    }

    /**
     * Logout the user
     * @param request The HTTP request
     */
    public void logoutUser( HttpServletRequest request )
    {
        AdminUser user;

        try
        {
            user = getRemoteUser( request );
        }
        catch ( UserNotSignedException e )
        {
            return;
        }
        catch ( AccessDeniedException e )
        {
            return;
        }

        _authentication.logout( user );
        unregisterUser( request );
    }

    /**
     * Bind user : complete module user with local settings (roles, etc)
     *
     * @param user The current user
     * @throws AccessDeniedException If the user cannot have access
     * @throws UserNotSignedException If the user is not signed
     * @return The AdminUser
     */
    private AdminUser bindUser( AdminUser user ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser bindUser = user;

        if ( user == null )
        {
            throw new UserNotSignedException(  );
        }

        // retrieve the user in local system from the access code
        bindUser = AdminUserHome.findUserByLogin( user.getAccessCode(  ) );

        // only allow a user that is marked active
        if ( ( bindUser == null ) || ( !bindUser.isStatusActive(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        // set the rights for this user
        bindUser.setRights( AdminUserHome.getRightsListForUser( bindUser.getUserId(  ) ) );

        // set the rights for this user
        bindUser.setRoles( AdminUserHome.getRolesListForUser( bindUser.getUserId(  ) ) );

        return bindUser;
    }

    /**
     * Register the user in the Http session
     *
     * @param request The Http request
     * @param user The current user
     * @throws AccessDeniedException If the user cannot have access
     * @throws UserNotSignedException If the user is not signed
     */
    private void registerUser( HttpServletRequest request, AdminUser user )
        throws AccessDeniedException, UserNotSignedException
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_ADMIN_USER, bindUser( user ) );
    }

    /**
     * Unregister the user in the Http session
     * @param request The Http request
     */
    private void unregisterUser( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );
        session.removeAttribute( ATTRIBUTE_ADMIN_USER );
    }

    /**
     * Gets the Lutece user registered in the Http session
     * @param request The HTTP request
     * @return The User registered or null if the user has not been registered
     */
    public AdminUser getRegisteredUser( HttpServletRequest request )
    {
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            return (AdminUser) session.getAttribute( ATTRIBUTE_ADMIN_USER );
        }

        return null;
    }

    /**
     * Returns the authentication type : External or Lutece portal based
     * @return true if the user is already authenticated or false if it needs to login.
     */
    public boolean isExternalAuthentication(  )
    {
        return _authentication.isExternalAuthentication(  );
    }

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    public String getLoginPageUrl(  )
    {
        return _authentication.getLoginPageUrl(  );
    }

    /**
     * Returns the modification password page URL of the Authentication Service
     * @return The URL
     */
    public String getChangePasswordPageUrl(  )
    {
        return _authentication.getChangePasswordPageUrl(  );
    }

    /**
     * Returns the DoLogin URL of the Authentication Service
     * @return The URL
     */
    public String getDoLoginUrl(  )
    {
        return _authentication.getDoLoginUrl(  );
    }

    /**
     * Returns the DoLogout URL of the Authentication Service
     * @return The URL
     */
    public String getDoLogoutUrl(  )
    {
        return _authentication.getDoLogoutUrl(  );
    }

    /**
     * Returns the new account page URL of the Authentication Service
     * @return The URL
     */
    public String getNewAccountPageUrl(  )
    {
        return _authentication.getNewAccountPageUrl(  );
    }

    /**
     * Returns the view account page URL of the Authentication Service
     * @return The URL
     */
    public String getViewAccountPageUrl(  )
    {
        return _authentication.getViewAccountPageUrl(  );
    }

    /**
     * Returns the lost password URL of the Authentication Service
     * @return The URL
     */
    public String getLostPasswordPageUrl(  )
    {
        return _authentication.getLostPasswordPageUrl(  );
    }

    /**
     * Returns the user list
     *
     * @return the collection of all users from the module
     * @param strLastName The last name
     * @param strFirstName The first name
     * @param strEmail The email
     */
    public Collection getUserListFromModule( String strLastName, String strFirstName, String strEmail )
    {
        return _authentication.getUserList( strLastName, strFirstName, strEmail );
    }

    /**
     *
     * @param strAccessCode The login
     * @return The AdminUser
     */
    public AdminUser getUserPublicDataFromModule( String strAccessCode )
    {
        return _authentication.getUserPublicData( strAccessCode );
    }
}
