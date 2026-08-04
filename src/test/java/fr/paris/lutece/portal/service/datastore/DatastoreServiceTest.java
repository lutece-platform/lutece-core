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
package fr.paris.lutece.portal.service.datastore;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.datastore.DataEntity;
import fr.paris.lutece.portal.business.datastore.DataEntityHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * DatastoreService Test
 */
public class DatastoreServiceTest extends LuteceTestCase
{
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final String VALUE_DEFAULT = "default";
    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";
    @Test
    public void test( )
    {
        String strValue = DatastoreService.getDataValue( KEY1, VALUE_DEFAULT );
        assertEquals( strValue, VALUE_DEFAULT );
        DatastoreService.setDataValue( KEY1, VALUE1 );
        strValue = DatastoreService.getDataValue( KEY1, VALUE_DEFAULT );
        assertEquals( strValue, VALUE1 );
        DatastoreService.removeData( KEY1 );
    }

    /**
     * A key looked up while absent must still be seen once it is created, then once it is removed :
     * the absent state is cached, so every write has to drop it.
     */
    @Test
    public void testAbsentThenCreatedThenRemoved( )
    {
        assertEquals( VALUE_DEFAULT, DatastoreService.getDataValue( KEY2, VALUE_DEFAULT ) );
        assertFalse( DatastoreService.existsKey( KEY2 ) );

        DatastoreService.setDataValue( KEY2, VALUE2 );
        assertTrue( DatastoreService.existsKey( KEY2 ) );
        assertEquals( VALUE2, DatastoreService.getDataValue( KEY2, VALUE_DEFAULT ) );

        DatastoreService.removeData( KEY2 );
        assertFalse( DatastoreService.existsKey( KEY2 ) );
        assertEquals( VALUE_DEFAULT, DatastoreService.getDataValue( KEY2, VALUE_DEFAULT ) );
    }

    /**
     * A key stored through the home rather than through this service must still become visible,
     * even when it was looked up while missing beforehand : the home drops the cached marker.
     */
    @Test
    public void testCreatedThroughHomeAfterMiss( )
    {
        String strKey = "key4";
        assertEquals( VALUE_DEFAULT, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );

        DataEntityHome.create( new DataEntity( strKey, VALUE1 ) );
        assertEquals( VALUE1, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );

        DataEntityHome.update( new DataEntity( strKey, VALUE2 ) );
        assertEquals( VALUE2, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );

        DataEntityHome.remove( strKey );
        assertEquals( VALUE_DEFAULT, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );
    }

    /**
     * insertDataValueIfAbsent must expose the value it stored, even when the key was looked up and
     * cached as absent beforehand.
     */
    @Test
    public void testInsertIfAbsentAfterMiss( )
    {
        String strKey = "key3";
        assertEquals( VALUE_DEFAULT, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );
        assertTrue( DatastoreService.insertDataValueIfAbsent( strKey, VALUE1 ) );
        assertEquals( VALUE1, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );
        assertFalse( DatastoreService.insertDataValueIfAbsent( strKey, VALUE2 ) );
        assertEquals( VALUE1, DatastoreService.getDataValue( strKey, VALUE_DEFAULT ) );
        DatastoreService.removeData( strKey );
    }
}
