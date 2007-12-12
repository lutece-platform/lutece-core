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


public class RBACTest extends LuteceTestCase
{
    private final static String ROLEKEY1 = "RoleKey 1";
    private final static String ROLEKEY2 = "RoleKey 2";
    private final static String RESOURCETYPE1 = "ResourceType 1";
    private final static String RESOURCETYPE2 = "ResourceType 2";
    private final static String RESOURCEID1 = "ResourceId 1";
    private final static String RESOURCEID2 = "ResourceId 2";
    private final static String PERMISSION1 = "Permission 1";
    private final static String PERMISSION2 = "Permission 2";

    public void testBusiness(  )
    {
        // Initialize an object 1
        RBAC rbac1 = new RBAC(  );
        rbac1.setRoleKey( ROLEKEY1 );
        rbac1.setResourceTypeKey( RESOURCETYPE1 );
        rbac1.setResourceId( RESOURCEID1 );
        rbac1.setPermissionKey( PERMISSION1 );

        // Initialize an object 2
        RBAC rbac2 = new RBAC(  );
        rbac2.setRoleKey( ROLEKEY2 );
        rbac2.setResourceTypeKey( RESOURCETYPE2 );
        rbac2.setResourceId( RESOURCEID2 );
        rbac2.setPermissionKey( PERMISSION2 );

        // Create test
        RBACHome.create( rbac1 );

        RBAC rbacStored = RBACHome.findByPrimaryKey( rbac1.getRBACId(  ) );
        assertEquals( rbacStored.getRoleKey(  ), rbac1.getRoleKey(  ) );
        assertEquals( rbacStored.getResourceTypeKey(  ), rbac1.getResourceTypeKey(  ) );
        assertEquals( rbacStored.getResourceId(  ), rbac1.getResourceId(  ) );
        assertEquals( rbacStored.getPermissionKey(  ), rbac1.getPermissionKey(  ) );

        // Update test
        rbac1.setRoleKey( ROLEKEY2 );
        rbac1.setResourceTypeKey( RESOURCETYPE2 );
        rbac1.setResourceId( RESOURCEID2 );
        rbac1.setPermissionKey( PERMISSION2 );

        RBACHome.update( rbac1 );
        rbacStored = RBACHome.findByPrimaryKey( rbac1.getRBACId(  ) );
        assertEquals( rbacStored.getRoleKey(  ), rbac1.getRoleKey(  ) );
        assertEquals( rbacStored.getResourceTypeKey(  ), rbac1.getResourceTypeKey(  ) );
        assertEquals( rbacStored.getResourceId(  ), rbac1.getResourceId(  ) );
        assertEquals( rbacStored.getPermissionKey(  ), rbac1.getPermissionKey(  ) );

        // Delete test
        RBACHome.remove( rbac1.getRBACId(  ) );
        rbacStored = RBACHome.findByPrimaryKey( rbac1.getRBACId(  ) );
        assertNull( rbacStored );

        // add elements element for the lists (both rbac1 and rbac2 values are ...2 values at this point)
        RBACHome.create( rbac1 );
        RBACHome.create( rbac2 );

        // List Test
        Collection list = RBACHome.findAll(  );
        assertTrue( list.size(  ) > 0 );

        // list by role Test
        Collection<RBAC> listByRole = RBACHome.findResourcesByCode( ROLEKEY2 );
        assertTrue( listByRole.size(  ) > 0 );

        for ( RBAC rbacListItem : listByRole )
        {
            assertEquals( ROLEKEY2, rbacListItem.getRoleKey(  ) );
        }

        // find roles from resource and permission Test
        Collection<String> listOfRoles = RBACHome.findRoleKeys( RESOURCETYPE2, RESOURCEID2, PERMISSION2 );
        assertTrue( list.size(  ) > 0 );

        boolean bRoleFound = false;

        for ( String strRoleKey : listOfRoles )
        {
            if ( strRoleKey.equals( ROLEKEY2 ) )
            {
                bRoleFound = true;
            }
        }

        assertTrue( bRoleFound );

        // update role keys for all Test
        RBACHome.updateRoleKey( ROLEKEY2, ROLEKEY1 );

        Collection listByRoleUpdated = RBACHome.findResourcesByCode( ROLEKEY1 );
        assertTrue( listByRoleUpdated.size(  ) > 0 );

        // both rbac1 and rbac2  have ROLEKEY1 values at this point     
        RBACHome.removeForRoleKey( ROLEKEY1 );
        rbacStored = RBACHome.findByPrimaryKey( rbac1.getRBACId(  ) );
        assertNull( rbacStored );
        rbacStored = RBACHome.findByPrimaryKey( rbac2.getRBACId(  ) );
        assertNull( rbacStored );
    }
}
