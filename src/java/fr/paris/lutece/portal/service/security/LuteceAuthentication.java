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

import java.util.Collection;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;


/**
 * This Interface defines all methods required by an authentication service password is not valid
 */
public interface LuteceAuthentication
{
    /**
     * Gets the Authentification service name
     * @return The Service Name
     */
    String getAuthServiceName(  );

    /**
     * Gets the Authentification type
     * @param request The HTTP request
     * @return The type of authentication
     */
    String getAuthType( HttpServletRequest request );

    /**
     * Checks the login
     *
     * @param strUserName The username
    * @param strUserPassword The user's password
    * @param request The HttpServletRequest
    * @return The login
    * @throws LoginException If a Login error occured
    * @throws LoginRedirectException If the the login process should be redirected
    */
    LuteceUser login( final String strUserName, final String strUserPassword, HttpServletRequest request )
        throws LoginException, LoginRedirectException;

    /**
         * logout the user
         * @param user The user
         */
    void logout( LuteceUser user );

    /**
     * Checks if a user's password has been reset
     * @param request The request
     * @param strLogin the login
     * @return True if the password has been reset, false otherwise
     */
    boolean findResetPassword( HttpServletRequest request, String strLogin );

    /**
     * This method creates an anonymous user
     *
     * @return A LuteceUser object corresponding to an anonymous user
     */
    LuteceUser getAnonymousUser(  );

    /**
     * Checks that the current user is associated to a given role
     * @param user The user
     * @param request The HTTP request
     * @param strRole The role name
     * @return Returns true if the user is associated to the role, otherwise false
     */
    boolean isUserInRole( LuteceUser user, HttpServletRequest request, String strRole );

    /**
     * get all roles for this user :
     *    - user's roles
     *    - user's groups roles
     *
     * @param user The user
     * @return Array of roles
     */
    String[] getRolesByUser( LuteceUser user );

    /**
     * Indicates that the user should be already authenticated by an external
     * authentication service (ex : Web Server authentication).
     * @return true if the authentication is external, false if the authentication
     * is provided by the Lutece portal.
     */
    boolean isExternalAuthentication(  );

    /**
     * Authentication is done by remote system (like OAuth)
     * @return <code>true</code> if part of the authentication if done by external website,
     * <code>false</code> otherwise.
     */
    boolean isDelegatedAuthentication(  );

    /**
     * Returns a Lutece user object if the user is already authenticated in the Http request.
     * This method should return null if the user is not authenticated or if
     * the authentication service is not based on Http authentication.
     * @param request The HTTP request
     * @return Returns A Lutece User
         */
    LuteceUser getHttpAuthenticatedUser( HttpServletRequest request );

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    String getLoginPageUrl(  );

    /**
     * Returns the DoLogin URL of the Authentication Service
     * @return The URL
     */
    String getDoLoginUrl(  );

    /**
     * Returns the DoLogout URL of the Authentication Service
     * @return The URL
     */
    String getDoLogoutUrl(  );

    /**
     * Returns the new account page URL of the Authentication Service
     * @return The URL
     */
    String getNewAccountPageUrl(  );

    /**
     * Returns the view account page URL of the Authentication Service
     * @return The URL
     */
    String getViewAccountPageUrl(  );

    /**
     * Returns the lost password URL of the Authentication Service
     * @return The URL
     */
    String getLostPasswordPageUrl(  );

    /**
     * Returns the lost login URL of the Authentication Service
     * @return The URL
     */
    String getLostLoginPageUrl(  );

    /**
     * Returns the reset password URL of the Authentication Service
     * @param request the HTTP request
     * @return The URL
     */
    String getResetPasswordPageUrl( HttpServletRequest request );

    /**
     * Returns the template that contains the Access Denied message.
     * @return The template path
     */
    String getAccessDeniedTemplate(  );

    /**
     * Returns the template that contains the Access Controled message.
     * @return The template path
     */
    String getAccessControledTemplate(  );

    /**
     * Tells whether or not the authentication service can provide a list of all its users
     * @return true if the service can return a users list
     */
    boolean isUsersListAvailable(  );

    /**
     * Returns all users managed by the authentication service if this feature is
     * available.
     * @return A collection of Lutece users or null if the service doesn't provide a users list
     */
    Collection<LuteceUser> getUsers(  );

    /**
     * Returns user managed by the authentication service if this feature is available.
     * @param strUserLogin the user login
     * @return A Lutece user or null if the service doesn't provide LuteceUser
     */
    LuteceUser getUser( String strUserLogin );

    /**
     * <b>true</b> when the service provides multi authentication support
     * @return <code>true</code> if multi authentication is supported, <code>false</code> otherwise.
     */
    boolean isMultiAuthenticationSupported(  );

    /**
     * Icon url
     * @return icon url
     */
    String getIconUrl(  );

    /**
     * Used as Identifier. <b>MUST </b>be unique.
     * @return the identifier
     */
    String getName(  );

    /**
     * Returns the plugin name.
     * <br>
     * Used to show (or not) this authentication depending on the plugin status .
     * @return the plugin name
         */
    String getPluginName(  );

    /**
     * Update the last login date of a user
     * @param user User to update
     * @param request The request
     */
    void updateDateLastLogin( LuteceUser user, HttpServletRequest request );
}
