/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.security.SecureRandom;

import javax.security.auth.login.LoginException;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.IAdminUserDAO;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.password.IPassword;
import jakarta.inject.Inject;

public class LuteceDefaultAdminAuthenticationTest extends LuteceTestCase
{
    private static final String PASSWORD = "PASSWORD";
    @Inject
    private IAdminUserDAO adminUserDAO;
    @Inject
    private AdminAuthentication adminAuth;

    public void testLoginUpgradePassword( )
    {
        String randomUsername = "user" + new SecureRandom( ).nextLong( );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( new IPassword( )
        {

            @Override
            public boolean isLegacy( )
            {
                return true;
            }

            @Override
            public String getStorableRepresentation( )
            {
                return "PLAINTEXT:" + PASSWORD;
            }

            @Override
            public boolean check( String strCleartextPassword )
            {
                return PASSWORD.equals( strCleartextPassword );
            }
        } );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );
        try
        {
            // check that password is legacy
            LuteceDefaultAdminUser defaultAdminUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            assertNotNull( defaultAdminUser );
            assertTrue( defaultAdminUser.getPassword( ).isLegacy( ) );
            // login
            AdminUser authenticated = adminAuth.login( randomUsername, PASSWORD, new MockHttpServletRequest( ) );
            assertNotNull( authenticated );
            // check that password is not legacy anymore
            LuteceDefaultAdminUser defaultAdminUserAthenticated = adminUserDAO.loadDefaultAdminUser( authenticated.getUserId( ) );
            assertNotNull( defaultAdminUserAthenticated );
            assertFalse( defaultAdminUserAthenticated.getPassword( ).isLegacy( ) );
            // retry login to check that password has not changed
            authenticated = adminAuth.login( randomUsername, PASSWORD, new MockHttpServletRequest( ) );
            assertNotNull( authenticated );
        }
        catch( LoginException e )
        {
            fail( e.getMessage( ) );
        }
        finally
        {
            adminUserDAO.delete( user.getUserId( ) );
        }
    }
}
