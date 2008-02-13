/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
package fr.paris.lutece.portal.business.group;

import fr.paris.lutece.test.LuteceTestCase;


public class GroupTest extends LuteceTestCase
{
    private final static String GROUPKEY1 = "GroupKey1";
    private final static String GROUPDESCRIPTION1 = "GroupDescription1";
    private final static String GROUPDESCRIPTION2 = "GroupDescription2";

    public void testBusiness(  )
    {
        // Initialize an group
        Group group = new Group(  );
        group.setGroupKey( GROUPKEY1 );
        group.setGroupDescription( GROUPDESCRIPTION1 );

        // Create test
        GroupHome.create( group );

        Group groupStored = GroupHome.findByPrimaryKey( group.getGroupKey(  ) );
        assertEquals( groupStored.getGroupKey(  ), group.getGroupKey(  ) );
        assertEquals( groupStored.getGroupDescription(  ), group.getGroupDescription(  ) );

        // Update test
        group.setGroupDescription( GROUPDESCRIPTION2 );
        GroupHome.update( group );
        groupStored = GroupHome.findByPrimaryKey( group.getGroupKey(  ) );
        assertEquals( groupStored.getGroupKey(  ), group.getGroupKey(  ) );
        assertEquals( groupStored.getGroupDescription(  ), group.getGroupDescription(  ) );

        // List test
        GroupHome.findAll(  );
        GroupHome.getGroupsList(  );

        // Delete test
        GroupHome.remove( group.getGroupKey(  ) );
        groupStored = GroupHome.findByPrimaryKey( group.getGroupKey(  ) );
        assertNull( groupStored );
    }
}
