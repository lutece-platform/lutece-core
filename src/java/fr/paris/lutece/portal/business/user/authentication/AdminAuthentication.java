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
package fr.paris.lutece.portal.business.user.authentication;

import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.Collection;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;


/**
 * This Interface defines all methods required by an authentication service password is not valid
 */
public interface AdminAuthentication
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
     * @param strAccessCode The username
     * @param strUserPassword The user's password
     * @param request The HttpServletRequest
     * @return The login
     * @throws LoginException The Login Exception
     */
    AdminUser login( final String strAccessCode, final String strUserPassword, HttpServletRequest request )
        throws LoginException;

    /**
     * logout the user
     * @param user The user
     */
    void logout( AdminUser user );

    /**
     * This method create an anonymous user
     *
     * @return A AdminUser object corresponding to an anonymous user
     */
    AdminUser getAnonymousUser(  );

    /**
     * Indicates that the user should be already authenticated by an external authentication service (ex : Web Server authentication).
     * @return true if the authentication is external, false if the authentication is provided by the Lutece portal.
     */
    boolean isExternalAuthentication(  );

    /**
     * Returns a Lutece user object if the user is already authenticated in the Http request. This method should return null if the user is not authenticated or if the authentication service is not
     * based on Http authentication.
     * @param request The HTTP request
     * @return Returns A Lutece User
     */
    AdminUser getHttpAuthenticatedUser( HttpServletRequest request );

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    String getLoginPageUrl(  );

    /**
     * Returns the password modification page URL of the Authentication Service
     * @return The URL
     */
    String getChangePasswordPageUrl(  );

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
     * get the list of user to display a list for selection in the main user management page
     *
     * @param strLastName The last name
     * @param strFirstName The first name
     * @param strEmail The email
     * @return the collection of available users
     */
    Collection<AdminUser> getUserList( String strLastName, String strFirstName, String strEmail );

    /**
     * Get user data
     * @param strAccessCode The access code (login)
     * @return The admin User
     */
    AdminUser getUserPublicData( String strAccessCode );
}
