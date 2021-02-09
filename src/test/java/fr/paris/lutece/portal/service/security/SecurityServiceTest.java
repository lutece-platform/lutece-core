/*
 * Copyright (c) 2002-2021, City of Paris
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

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * This class tests the class fr.paris.lutece.portal.service.SecurityService
 */
public class SecurityServiceTest extends LuteceTestCase
{
    /**
     * Login User fail test
     * 
     * @throws LoginException
     */
    public void testLoginUserFail( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );

        String strUserName = "";
        String strPassword = "";

        try
        {
            SecurityService securityService = SecurityService.getInstance( );
            MokeLuteceAuthentication mockLuteceAuthentication = new MokeLuteceAuthentication( );  
            ReflectionTestUtils.setField( securityService, "_authenticationService", mockLuteceAuthentication );

            securityService.loginUser( request, strUserName, strPassword );

            // FailedLoginException should be thrown before this assertion
            assertTrue( false );
        }
        catch( LoginException e )
        {
            assertEquals( e.getClass( ), FailedLoginException.class );
        }
        catch( NullPointerException e )
        {
            assertTrue( false );
        }
        catch( LoginRedirectException e )
        {
            assertTrue( false );
        }
    }

    /**
     * Login User success test
     *
     * @throws LoginException
     */
    public void testLoginUserSuccess( ) throws LoginException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strUserName = "admin";
        String strPassword = "adminadmin";
        String sessionId = request.getSession( true ).getId( );

        try
        {
            SecurityService securityService = SecurityService.getInstance( );
            MokeLuteceAuthentication mockLuteceAuthentication = new MokeLuteceAuthentication( );
            ReflectionTestUtils.setField( securityService, "_authenticationService", mockLuteceAuthentication );

            securityService.loginUser( request, strUserName, strPassword );

            String newSessionId = request.getSession( true ).getId( );

            // session Id should not changed during authentication process
            assertTrue( sessionId.contentEquals( newSessionId ) );
        }
        catch( Exception e )
        {
            assertTrue( false );
        }
    }
}
