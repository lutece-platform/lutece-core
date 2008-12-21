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
package fr.paris.lutece.portal.business.user.authentication;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.log.UserLog;
import fr.paris.lutece.portal.business.user.log.UserLogHome;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Collection;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;


/**
 * Default authentication module for admin authentication
 */
public class LuteceDefaultAdminAuthentication implements AdminAuthentication
{
    private static final String CONSTANT_LOST_PASSWORD_URL = "jsp/admin/AdminForgotPassword.jsp";
    private static final String PROPERTY_MAX_ACCESS_FAILED = "access.failures.max";
    private static final String PROPERTY_INTERVAL_MINUTES = "access.failures.interval.minutes";
    private ILuteceDefaultAdminUserDAO _dao;

    /**
     * Setter used by Spring IoC
     * @param dao The DAO (defined in the Spring context)
     */
    public void setDao( ILuteceDefaultAdminUserDAO dao )
    {
        _dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthServiceName(  )
    {
        return "LUTECE DEFAULT AUTHENTICATION";
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthType( HttpServletRequest request )
    {
        return HttpServletRequest.BASIC_AUTH;
    }

    /**
     * {@inheritDoc}
     */
    public AdminUser login( String strAccessCode, String strUserPassword, HttpServletRequest request )
        throws LoginException
    {
        // Creating a record of connections log
        UserLog userLog = new UserLog(  );
        userLog.setAccessCode( strAccessCode );
        userLog.setIpAddress( request.getRemoteAddr(  ) );
        userLog.setDateLogin( new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );

        //      Test the number of errors during an interval of minutes
        int nMaxFailed = AppPropertiesService.getPropertyInt( PROPERTY_MAX_ACCESS_FAILED, 3 );
        int nIntervalMinutes = AppPropertiesService.getPropertyInt( PROPERTY_INTERVAL_MINUTES, 10 );
        int nNbFailed = UserLogHome.getLoginErrors( userLog, nIntervalMinutes );

        if ( nNbFailed > nMaxFailed )
        {
            throw new FailedLoginException(  );
        }

        int nUserCode = _dao.checkPassword( strAccessCode, strUserPassword );

        if ( nUserCode != LuteceDefaultAdminUserDAO.USER_OK )
        {
            throw new FailedLoginException(  );
        }

        AdminUser user = _dao.load( strAccessCode, this );

        return user;
    }

    /**
     * {@inheritDoc}
     */
    public void logout( AdminUser user )
    {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public AdminUser getAnonymousUser(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isExternalAuthentication(  )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public AdminUser getHttpAuthenticatedUser( HttpServletRequest request )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getLoginPageUrl(  )
    {
        return "jsp/admin/AdminLogin.jsp";
    }

    /**
     * {@inheritDoc}
     */
    public String getChangePasswordPageUrl(  )
    {
        return "jsp/admin/user/ModifyDefaultUserPassword.jsp";
    }

    /**
     * {@inheritDoc}
     */
    public String getDoLoginUrl(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getDoLogoutUrl(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getNewAccountPageUrl(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getViewAccountPageUrl(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getLostPasswordPageUrl(  )
    {
        return CONSTANT_LOST_PASSWORD_URL;
    }

    /**
     * Not used - Return null always for this module
     * @param strLastName The last name
     * @param strFirstName The first name
     * @param strEmail The email
     * @see fr.paris.lutece.portal.business.user.authentication.AdminAuthentication#getUserList()
     * @return null
     */
    public Collection<AdminUser> getUserList( String strLastName, String strFirstName, String strEmail )
    {
        return null;
    }

    /**
     * Not used - Return null always for this module
     * @param strLogin The login
     * @see fr.paris.lutece.portal.business.user.authentication.AdminAuthentication#getUserPublicData(java.lang.String)
     * @return null
     */
    public AdminUser getUserPublicData( String strLogin )
    {
        return null;
    }
}
