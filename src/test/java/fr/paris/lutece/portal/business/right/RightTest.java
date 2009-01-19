/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Collection;


public class RightTest extends LuteceTestCase
{
    private static final String RIGHT_ID = "JUNIT_TEST_RIGHT";
    private final static String NAMEKEY1 = "NameKey 1";
    private final static String NAMEKEY2 = "NameKey 2";
    private final static String DESCRIPTIONKEY1 = "DescriptionKey 1";
    private final static String DESCRIPTIONKEY2 = "DescriptionKey 2";
    private final static int LEVEL1 = 1;
    private final static int LEVEL2 = 2;
    private final static String URL1 = "Url 1";
    private final static String URL2 = "Url 2";
    private final static String PLUGINNAME1 = "PluginName 1";
    private final static String PLUGINNAME2 = "PluginName 2";
    private final static String FEATUREGROUP1 = "FeatureGroup 1";
    private final static String FEATUREGROUP2 = "FeatureGroup 2";
    private final static String ICONURL1 = "IconUrl 1";
    private final static String ICONURL2 = "IconUrl 2";

    public void testBusinessRight(  )
    {
        // Initialize an object
        Right right = new Right(  );
        right.setId( RIGHT_ID );
        right.setNameKey( NAMEKEY1 );
        right.setDescriptionKey( DESCRIPTIONKEY1 );
        right.setLevel( LEVEL1 );
        right.setUrl( URL1 );
        right.setPluginName( PLUGINNAME1 );
        right.setFeatureGroup( FEATUREGROUP1 );
        right.setIconUrl( ICONURL1 );

        // Create test
        RightHome.create( right );

        Right rightStored = RightHome.findByPrimaryKey( right.getId(  ) );
        assertEquals( rightStored.getNameKey(  ), right.getNameKey(  ) );
        assertEquals( rightStored.getDescriptionKey(  ), right.getDescriptionKey(  ) );
        assertEquals( rightStored.getLevel(  ), right.getLevel(  ) );
        assertEquals( rightStored.getUrl(  ), right.getUrl(  ) );
        assertEquals( rightStored.getPluginName(  ), right.getPluginName(  ) );
        assertEquals( rightStored.getFeatureGroup(  ), right.getFeatureGroup(  ) );
        assertEquals( rightStored.getIconUrl(  ), right.getIconUrl(  ) );

        // Update test
        right.setNameKey( NAMEKEY2 );
        right.setDescriptionKey( DESCRIPTIONKEY2 );
        right.setLevel( LEVEL2 );
        right.setUrl( URL2 );
        right.setPluginName( PLUGINNAME2 );
        right.setFeatureGroup( FEATUREGROUP2 );
        right.setIconUrl( ICONURL2 );
        RightHome.update( right );
        rightStored = RightHome.findByPrimaryKey( right.getId(  ) );
        assertEquals( rightStored.getNameKey(  ), right.getNameKey(  ) );
        assertEquals( rightStored.getDescriptionKey(  ), right.getDescriptionKey(  ) );
        assertEquals( rightStored.getLevel(  ), right.getLevel(  ) );
        assertEquals( rightStored.getUrl(  ), right.getUrl(  ) );
        assertEquals( rightStored.getPluginName(  ), right.getPluginName(  ) );
        assertEquals( rightStored.getFeatureGroup(  ), right.getFeatureGroup(  ) );
        assertEquals( rightStored.getIconUrl(  ), right.getIconUrl(  ) );

        // List Test
        Collection listRights = RightHome.getRightsList(  );
        assertTrue( listRights.size(  ) > 0 );

        // Delete test
        RightHome.remove( right.getId(  ) );
        rightStored = RightHome.findByPrimaryKey( right.getId(  ) );
        assertNull( rightStored );
    }
}
