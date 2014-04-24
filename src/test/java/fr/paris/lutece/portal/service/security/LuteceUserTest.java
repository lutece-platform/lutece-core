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

import fr.paris.lutece.test.LuteceTestCase;

import java.util.ArrayList;


/**
 * LuteceUserTest
 */
public class LuteceUserTest extends LuteceTestCase
{
    private static final String NAME = "NAME";
    private static final String ROLE1 = "ROLE1";
    private static final String ROLE2 = "ROLE2";
    private static final String EMAIL = "EMAIL";

    public void testLuteceUser(  )
    {
        MokeLuteceAuthentication auth = new MokeLuteceAuthentication(  );

        MokeLuteceUser user = new MokeLuteceUser( NAME, auth );
        user.setName( NAME );

        ArrayList<String> listRoles = new ArrayList(  );
        listRoles.add( ROLE1 );
        listRoles.add( ROLE2 );
        user.setRoles( listRoles );
        user.setUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL, EMAIL );
        assertEquals( user.getName(  ), NAME );

        String[] roles = user.getRoles(  );
        assertTrue( roles.length == 2 );

        String strEmail = user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL );
        assertEquals( strEmail, EMAIL );
        user.getUserInfos(  );
        user.getAuthenticationService(  );
        user.getAuthenticationType(  );
    }
}
