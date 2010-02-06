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
package fr.paris.lutece.portal.business.features;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Collection;


public class FeaturesTest extends LuteceTestCase
{
    private final static String NAME1 = "Level 1 JUnit";
    private final static String NAME2 = "Level 2 JUnit";

    public void testBusinessLevel(  )
    {
        // Initialize an object
        Level level = new Level(  );
        level.setName( NAME1 );

        // Create test
        LevelHome.create( level );

        Level levelStored = LevelHome.findByPrimaryKey( level.getId(  ) );
        assertEquals( levelStored.getName(  ), level.getName(  ) );

        // Update test
        level.setName( NAME2 );

        LevelHome.update( level );
        levelStored = LevelHome.findByPrimaryKey( level.getId(  ) );
        assertEquals( levelStored.getName(  ), level.getName(  ) );

        // Delete test
        LevelHome.remove( level.getId(  ) );
        levelStored = LevelHome.findByPrimaryKey( level.getId(  ) );
        assertNull( levelStored );

        // List Test
        Collection list = LevelHome.getLevelsList(  );
        assertTrue( list.size(  ) > 0 );
    }
}
