/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the copy of a user read from a cache
 */
public class LuteceUserCloneTest
{
    /**
     * A clone of a user owns its infos, roles and workgroups: changing them leaves the original unchanged
     *
     * @throws CloneNotSupportedException
     *             never
     */
    @Test
    public void testCloneIsDeep( ) throws CloneNotSupportedException
    {
        MokeLuteceUser user = new MokeLuteceUser( "name", new MokeLuteceAuthentication( ) );
        user.setUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL, "email" );
        user.setRoles( List.of( "role1" ) );
        user.getUserWorkgroups( ).add( "wg1" );

        LuteceUser copy = (LuteceUser) user.clone( );
        copy.setUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL, "changed" );
        copy.getUserWorkgroups( ).add( "wg2" );
        copy.getRoles( ) [0] = "role2";

        assertEquals( "email", user.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ) );
        assertEquals( 1, user.getUserWorkgroups( ).size( ) );
        assertEquals( "role1", user.getRoles( ) [0] );
    }
}
