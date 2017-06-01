/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.portal.business.user;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class AdminUserTest extends LuteceTestCase
{
    private final static String ACCESSCODE1 = "AccessCode 1";
    private final static String ACCESSCODE2 = "AccessCode 2";
    private final static String LASTNAME1 = "LastName 1";
    private final static String LASTNAME2 = "LastName 2";
    private final static String FIRSTNAME1 = "FirstName 1";
    private final static String FIRSTNAME2 = "FirstName 2";
    private final static String EMAIL1 = "Email 1";
    private final static String EMAIL2 = "Email 2";
    private final static int STATUS1 = 1;
    private final static int STATUS2 = 2;
    private static final String RIGHT1 = "Right 1";
    private static final String ROLE1 = "Role 1";
    private final static int LEVEL = 0;

    public void testBusinessUser( )
    {
        // Initialize an object
        AdminUser user = new AdminUser( );
        user.setAccessCode( ACCESSCODE1 );
        user.setLastName( LASTNAME1 );
        user.setFirstName( FIRSTNAME1 );
        user.setEmail( EMAIL1 );
        user.setStatus( STATUS1 );
        user.setLocale( Locale.ENGLISH );
        user.setUserLevel( LEVEL );

        // Create test
        AdminUserHome.create( user );

        AdminUser userStored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
        assertEquals( userStored.getAccessCode( ), user.getAccessCode( ) );
        assertEquals( userStored.getLastName( ), user.getLastName( ) );
        assertEquals( userStored.getFirstName( ), user.getFirstName( ) );
        assertEquals( userStored.getEmail( ), user.getEmail( ) );
        assertEquals( userStored.getStatus( ), user.getStatus( ) );
        assertEquals( userStored.getLocale( ), user.getLocale( ) );
        assertEquals( userStored.getUserLevel( ), user.getUserLevel( ) );

        AdminUserHome.createRightForUser( user.getUserId( ), RIGHT1 );
        AdminUserHome.createRoleForUser( user.getUserId( ), ROLE1 );

        // List Test
        Collection listUsers = AdminUserHome.findUserList( );
        assertTrue( listUsers.size( ) > 0 );

        Map listRights = AdminUserHome.getRightsListForUser( user.getUserId( ) );

        // assertTrue( listRights.size() > 0 );
        Map listRoles = AdminUserHome.getRolesListForUser( user.getUserId( ) );
        // assertTrue( listRoles.size() > 0 );

        // Update test
        user.setAccessCode( ACCESSCODE2 );
        user.setLastName( LASTNAME2 );
        user.setFirstName( FIRSTNAME2 );
        user.setEmail( EMAIL2 );
        user.setStatus( STATUS2 );
        user.setLocale( Locale.FRANCE );

        AdminUserHome.update( user );
        userStored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
        assertEquals( userStored.getAccessCode( ), user.getAccessCode( ) );
        assertEquals( userStored.getLastName( ), user.getLastName( ) );
        assertEquals( userStored.getFirstName( ), user.getFirstName( ) );
        assertEquals( userStored.getEmail( ), user.getEmail( ) );
        assertEquals( userStored.getStatus( ), user.getStatus( ) );
        // assertEquals( userStored.getLocale( ), user.getLocale( ) );

        // Delete test
        AdminUserHome.removeAllRightsForUser( user.getUserId( ) );
        AdminUserHome.removeAllRolesForUser( user.getUserId( ) );
        AdminUserHome.remove( user.getUserId( ) );
        userStored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
        assertNull( userStored );
    }
}
