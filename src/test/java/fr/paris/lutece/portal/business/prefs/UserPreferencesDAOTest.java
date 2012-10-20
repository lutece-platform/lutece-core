/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.business.prefs;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;


/**
 * UserPreferencesDAOTest
 */
public class UserPreferencesDAOTest extends LuteceTestCase
{
    private final static String USER_ID_1 = "1";
    private final static String KEY1 = "KEY1";
    private final static String DEFAULT = "default";
    private final static String VALUE1 = "Value 1";

    public void testBusinessLevel(  )
    {
        UserPreferencesDAO dao = new UserPreferencesDAO(  );

        dao.remove( USER_ID_1 );

        String strValue = dao.load( USER_ID_1, KEY1, DEFAULT );
        assertEquals( strValue, DEFAULT );

        dao.store( USER_ID_1, KEY1, VALUE1 );
        strValue = dao.load( USER_ID_1, KEY1, DEFAULT );
        assertEquals( strValue, VALUE1 );

        // List Test
        List list = dao.keys( USER_ID_1 );
        assertTrue( list.size(  ) > 0 );
    }
}
