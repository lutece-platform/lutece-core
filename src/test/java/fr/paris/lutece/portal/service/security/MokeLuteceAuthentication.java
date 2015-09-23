/*
 * Copyright (c) 2002-2015, Mairie de Paris
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

import java.util.Arrays;
import java.util.Collection;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class MokeLuteceAuthentication implements LuteceAuthentication
{
    /** Creates a new instance of MokeLuteceAuthentication */
    public MokeLuteceAuthentication(  )
    {
    }

    public String getAuthServiceName(  )
    {
        return "MOKE AUTHENTICATION SERVICE";
    }

    public String getAuthType( HttpServletRequest request )
    {
        return null;
    }

    public LuteceUser login( final String strUserName, final String strUserPassword, HttpServletRequest request )
        throws LoginException
    {
        return null;
    }

    public void logout( LuteceUser user )
    {
    }

    public boolean findResetPassword( HttpServletRequest request, String strLogin )
    {
        return false;
    }

    public LuteceUser getAnonymousUser(  )
    {
        return null;
    }

    public boolean isUserInRole( LuteceUser user, HttpServletRequest request, String strRole )
    {
        return Arrays.asList( user.getRoles(  ) ).contains( strRole );
    }

    public String[] getRolesByUser( LuteceUser user )
    {
        return null;
    }

    public boolean isExternalAuthentication(  )
    {
        return false;
    }

    public LuteceUser getHttpAuthenticatedUser( HttpServletRequest request )
    {
        return null;
    }

    public String getLoginPageUrl(  )
    {
        return null;
    }

    public String getDoLoginUrl(  )
    {
        return null;
    }

    public String getDoLogoutUrl(  )
    {
        return null;
    }

    public String getNewAccountPageUrl(  )
    {
        return null;
    }

    public String getViewAccountPageUrl(  )
    {
        return null;
    }

    public String getLostPasswordPageUrl(  )
    {
        return null;
    }

    public String getResetPasswordPageUrl( HttpServletRequest request )
    {
        return null;
    }

    public String getAccessDeniedTemplate(  )
    {
        return null;
    }

    public String getAccessControledTemplate(  )
    {
        return null;
    }

    public boolean isUsersListAvailable(  )
    {
        return false;
    }

    public Collection<LuteceUser> getUsers(  )
    {
        return null;
    }

    public LuteceUser getUser( String strUserLogin )
    {
        return null;
    }

    public boolean isDelegatedAuthentication(  )
    {
        return false;
    }

    public boolean isMultiAuthenticationSupported(  )
    {
        return false;
    }

    public String getIconUrl(  )
    {
        return null;
    }

    public String getName(  )
    {
        return null;
    }

    public String getPluginName(  )
    {
        return null;
    }

    public void updateDateLastLogin( LuteceUser user, HttpServletRequest request )
    {
    }

    @Override
    public String getLostLoginPageUrl(  )
    {
        return null;
    }
}
