/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.portal.service.prefs;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;


/**
 * User Preferences Service Test
 */
public class UserPreferencesServiceTest extends LuteceTestCase
{
    private final static String USER_ID_1 = "1";
    private final static String KEY1 = "KEY1";
    private final static String KEY2 = "KEY2";
    private final static String KEY3 = "KEY3";
    private final static String DEFAULT = "default";
    private final static String VALUE1 = "Value 1";
    private static final int INT_DEFAULT = 10;
    private static final int INT_VALUE = 20;
    private static final boolean BOOL_DEFAULT = true;
    private static final boolean BOOL_VALUE = false;
    private static final String NICKNAME = "nickname";

    public void testBusinessLevel(  )
    {
        IPortalUserPreferencesService service = UserPreferencesService.instance(  );

        service.clear( USER_ID_1 );

        String strValue = service.get( USER_ID_1, KEY1, DEFAULT );
        assertEquals( strValue, DEFAULT );
        service.put( USER_ID_1, KEY1, VALUE1 );
        strValue = service.get( USER_ID_1, KEY1, DEFAULT );
        assertEquals( strValue, VALUE1 );

        int nValue = service.getInt( USER_ID_1, KEY2, INT_DEFAULT );
        assertEquals( nValue, INT_DEFAULT );
        service.putInt( USER_ID_1, KEY2, INT_VALUE );
        nValue = service.getInt( USER_ID_1, KEY2, INT_DEFAULT );
        assertEquals( nValue, INT_VALUE );

        boolean bValue = service.getBoolean( USER_ID_1, KEY3, BOOL_DEFAULT );
        assertEquals( bValue, BOOL_DEFAULT );
        service.putBoolean( USER_ID_1, KEY3, BOOL_VALUE );
        bValue = service.getBoolean( USER_ID_1, KEY3, BOOL_DEFAULT );
        assertEquals( bValue, BOOL_VALUE );

        service.setNickname( USER_ID_1, NICKNAME );
        assertTrue( NICKNAME.equals( service.getNickname( USER_ID_1 ) ) );

        // List Test
        List list = service.keys( USER_ID_1 );
        assertTrue( list.size(  ) > 0 );
    }
}
