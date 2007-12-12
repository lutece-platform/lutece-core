/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.portal.business.rbac;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Collection;


public class AdminRoleTest extends LuteceTestCase
{
    private final static String ROLEKEY1 = "RoleKey1";
    private final static String ROLEKEY2 = "RoleKey2";
    private final static String ROLEDESCRIPTION1 = "RoleDescription 1";
    private final static String ROLEDESCRIPTION2 = "RoleDescription 2";

    public void testBusiness(  )
    {
        // Initialize an object
        AdminRole adminRole = new AdminRole(  );
        adminRole.setKey( ROLEKEY1 );
        adminRole.setDescription( ROLEDESCRIPTION1 );

        // Create test
        AdminRoleHome.create( adminRole );

        AdminRole adminRoleStored = AdminRoleHome.findByPrimaryKey( adminRole.getKey(  ) );
        assertEquals( adminRoleStored.getKey(  ), adminRole.getKey(  ) );
        assertEquals( adminRoleStored.getDescription(  ), adminRole.getDescription(  ) );

        // Update test
        adminRole.setKey( ROLEKEY2 );
        adminRole.setDescription( ROLEDESCRIPTION2 );

        AdminRoleHome.update( adminRoleStored.getKey(  ), adminRole );
        adminRoleStored = AdminRoleHome.findByPrimaryKey( adminRole.getKey(  ) );
        assertEquals( adminRoleStored.getKey(  ), adminRole.getKey(  ) );
        assertEquals( adminRoleStored.getDescription(  ), adminRole.getDescription(  ) );

        //list test
        Collection list = AdminRoleHome.findAll(  );
        assertTrue( list.size(  ) > 0 );

        // check exist Test
        boolean bCheck = AdminRoleHome.checkExistRole( ROLEKEY2 );
        assertTrue( bCheck );
        bCheck = AdminRoleHome.checkExistRole( ROLEKEY1 );
        assertFalse( bCheck );

        // Delete test
        AdminRoleHome.remove( adminRole.getKey(  ) );
        adminRoleStored = AdminRoleHome.findByPrimaryKey( adminRole.getKey(  ) );
        assertNull( adminRoleStored );
    }
}
