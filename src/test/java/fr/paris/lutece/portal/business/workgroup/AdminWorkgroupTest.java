/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.business.workgroup;

import fr.paris.lutece.test.LuteceTestCase;

public class AdminWorkgroupTest extends LuteceTestCase
{
    private final static String GROUPKEY1 = "AdminWorkgroupKey1";
    private final static String GROUPDESCRIPTION1 = "AdminWorkgroupDescription1";
    private final static String GROUPDESCRIPTION2 = "AdminWorkgroupDescription2";

    public void testBusiness( )
    {
        // Initialize an group
        AdminWorkgroup group = new AdminWorkgroup( );
        group.setKey( GROUPKEY1 );
        group.setDescription( GROUPDESCRIPTION1 );

        // Create test
        AdminWorkgroupHome.create( group );

        AdminWorkgroup groupStored = AdminWorkgroupHome.findByPrimaryKey( group.getKey( ) );
        assertEquals( groupStored.getKey( ), group.getKey( ) );
        assertEquals( groupStored.getDescription( ), group.getDescription( ) );

        // Update test
        group.setDescription( GROUPDESCRIPTION2 );
        AdminWorkgroupHome.update( group );
        groupStored = AdminWorkgroupHome.findByPrimaryKey( group.getKey( ) );
        assertEquals( groupStored.getKey( ), group.getKey( ) );
        assertEquals( groupStored.getDescription( ), group.getDescription( ) );

        // List test
        AdminWorkgroupHome.findAll( );
        AdminWorkgroupHome.getUserListForWorkgroup( GROUPKEY1 );

        // Delete test
        AdminWorkgroupHome.remove( group.getKey( ) );
        groupStored = AdminWorkgroupHome.findByPrimaryKey( group.getKey( ) );
        assertNull( groupStored );
    }
}
