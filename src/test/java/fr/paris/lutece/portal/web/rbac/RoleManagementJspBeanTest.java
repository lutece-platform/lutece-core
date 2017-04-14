/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.web.rbac;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Random;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;

/**
 * StyleSheetJspBean Test Class
 *
 */
public class RoleManagementJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_ROLE_KEY = "role_key";
    private static final String PARAMETER_RBAC_ID = "rbac_id";

    /**
     * Test of getManageRoles method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testGetManageRoles( ) throws AccessDeniedException
    {
        System.out.println( "getManageRoles" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), RoleManagementJspBean.RIGHT_MANAGE_ROLES );

        RoleManagementJspBean instance = new RoleManagementJspBean( );
        instance.init( request, RoleManagementJspBean.RIGHT_MANAGE_ROLES );
        instance.getManageRoles( request );
    }

    /**
     * Test of getCreateRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testGetCreateRole( ) throws AccessDeniedException
    {
        System.out.println( "getCreateRole" );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        Utils.registerAdminUserWithRigth( request, new AdminUser( ), RoleManagementJspBean.RIGHT_MANAGE_ROLES );

        RoleManagementJspBean instance = new RoleManagementJspBean( );
        instance.init( request, RoleManagementJspBean.RIGHT_MANAGE_ROLES );
        instance.getCreateRole( request );
    }

    /**
     * Test of doCreateRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoCreateRole( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String roleName = getRandomName( );
        request.setParameter( "role_key", roleName );
        request.setParameter( "role_description", roleName );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/rbac/create_role.html" ) );
        try
        {
            assertFalse( AdminRoleHome.checkExistRole( roleName ) );
            bean.doCreateRole( request );
            assertTrue( AdminRoleHome.checkExistRole( roleName ) );
        }
        finally
        {
            AdminRoleHome.remove( roleName );
        }
    }

    public void testDoCreateRoleInvalidToken( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String roleName = getRandomName( );
        request.setParameter( "role_key", roleName );
        request.setParameter( "role_description", roleName );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/rbac/create_role.html" ) + "b" );
        try
        {
            assertFalse( AdminRoleHome.checkExistRole( roleName ) );
            bean.doCreateRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( AdminRoleHome.checkExistRole( roleName ) );
        }
        finally
        {
            AdminRoleHome.remove( roleName );
        }
    }

    public void testDoCreateRoleNoToken( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        final String roleName = getRandomName( );
        request.setParameter( "role_key", roleName );
        request.setParameter( "role_description", roleName );
        try
        {
            assertFalse( AdminRoleHome.checkExistRole( roleName ) );
            bean.doCreateRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertFalse( AdminRoleHome.checkExistRole( roleName ) );
        }
        finally
        {
            AdminRoleHome.remove( roleName );
        }
    }

    /**
     * Test of getModifyRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testGetModifyRole( ) throws AccessDeniedException
    {
        System.out.println( "getModifyRole" );

        // Not implemented yet
    }

    /**
     * Test of doModifyRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoModifyRole( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "role_key_previous", role.getKey( ) );
        request.setParameter( "role_description", role.getKey( ) + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/rbac/view_role_description.html" ) );
        try
        {
            AdminRole stored = AdminRoleHome.findByPrimaryKey( role.getKey( ) );
            assertEquals( role.getDescription( ), stored.getDescription( ) );
            bean.doModifyRole( request );
            stored = AdminRoleHome.findByPrimaryKey( role.getKey( ) );
            assertEquals( role.getDescription( ) + "_mod", stored.getDescription( ) );
        }
        finally
        {
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoModifyRoleInvalidToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "role_key_previous", role.getKey( ) );
        request.setParameter( "role_description", role.getKey( ) + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/rbac/view_role_description.html" ) + "b" );
        try
        {
            AdminRole stored = AdminRoleHome.findByPrimaryKey( role.getKey( ) );
            assertEquals( role.getDescription( ), stored.getDescription( ) );
            bean.doModifyRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            AdminRole stored = AdminRoleHome.findByPrimaryKey( role.getKey( ) );
            assertEquals( role.getDescription( ), stored.getDescription( ) );
        }
        finally
        {
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoModifyRoleNoToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "role_key_previous", role.getKey( ) );
        request.setParameter( "role_description", role.getKey( ) + "_mod" );
        try
        {
            AdminRole stored = AdminRoleHome.findByPrimaryKey( role.getKey( ) );
            assertEquals( role.getDescription( ), stored.getDescription( ) );
            bean.doModifyRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            AdminRole stored = AdminRoleHome.findByPrimaryKey( role.getKey( ) );
            assertEquals( role.getDescription( ), stored.getDescription( ) );
        }
        finally
        {
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    /**
     * Test of doConfirmRemoveRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testDoConfirmRemoveRole( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", "role" );
        bean.doConfirmRemoveRole( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    /**
     * Test of doRemoveRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoRemoveRole( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/rbac/DoRemoveRole.jsp" ) );
        try
        {
            assertTrue( AdminRoleHome.checkExistRole( role.getKey( ) ) );
            bean.doRemoveRole( request );
            assertFalse( AdminRoleHome.checkExistRole( role.getKey( ) ) );
        }
        finally
        {
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoRemoveRoleInvalidToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/rbac/DoRemoveRole.jsp" ) + "b" );
        try
        {
            assertTrue( AdminRoleHome.checkExistRole( role.getKey( ) ) );
            bean.doRemoveRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( AdminRoleHome.checkExistRole( role.getKey( ) ) );
        }
        finally
        {
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoRemoveRoleNoToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        try
        {
            assertTrue( AdminRoleHome.checkExistRole( role.getKey( ) ) );
            bean.doRemoveRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( AdminRoleHome.checkExistRole( role.getKey( ) ) );
        }
        finally
        {
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    /**
     * Test of getViewRoleDescription method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     */
    public void testgetViewRoleDescription( ) throws AccessDeniedException
    {
        System.out.println( "getViewRoleDescription" );

        // Not implemented yet
    }

    public void testDoConfirmRemoveControlFromRole( )
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "rbac_id", "1" );
        bean.doConfirmRemoveControlFromRole( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
    }

    /**
     * Test of doRemoveControlFromRole method, of class fr.paris.lutece.portal.web.rbac.RoleManagementJspBean.
     * @throws AccessDeniedException 
     */
    public void testDoRemoveControlFromRole( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RBAC rBAC = new RBAC( );
        rBAC.setRoleKey( role.getKey( ) );
        rBAC.setResourceId( "*" );
        rBAC.setPermissionKey( "*" );
        rBAC.setResourceTypeKey( "*" );
        RBACHome.create( rBAC );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "rbac_id", Integer.toString( rBAC.getRBACId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/rbac/DoRemoveControlFromRole.jsp" ) );
        try
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            boolean found = false;
            for ( RBAC aRBAC : rbacs )
            {
                if ( aRBAC.getRBACId( ) == rBAC.getRBACId( ) )
                {
                    found = true;
                    break;
                }
            }
            assertTrue( found );
            bean.doRemoveControlFromRole( request );
            rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            found = false;
            for ( RBAC aRBAC : rbacs )
            {
                if ( aRBAC.getRBACId( ) == rBAC.getRBACId( ) )
                {
                    found = true;
                    break;
                }
            }
            assertFalse( found );
        }
        finally
        {
            RBACHome.remove( rBAC.getRBACId( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoRemoveControlFromRoleInvalidToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RBAC rBAC = new RBAC( );
        rBAC.setRoleKey( role.getKey( ) );
        rBAC.setResourceId( "*" );
        rBAC.setPermissionKey( "*" );
        rBAC.setResourceTypeKey( "*" );
        RBACHome.create( rBAC );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "rbac_id", Integer.toString( rBAC.getRBACId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/rbac/DoRemoveControlFromRole.jsp" ) + "b" );
        try
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            boolean found = false;
            for ( RBAC aRBAC : rbacs )
            {
                if ( aRBAC.getRBACId( ) == rBAC.getRBACId( ) )
                {
                    found = true;
                    break;
                }
            }
            assertTrue( found );
            bean.doRemoveControlFromRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            boolean found = false;
            for ( RBAC aRBAC : rbacs )
            {
                if ( aRBAC.getRBACId( ) == rBAC.getRBACId( ) )
                {
                    found = true;
                    break;
                }
            }
            assertTrue( found );
        }
        finally
        {
            RBACHome.remove( rBAC.getRBACId( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoRemoveControlFromRoleNoToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RBAC rBAC = new RBAC( );
        rBAC.setRoleKey( role.getKey( ) );
        rBAC.setResourceId( "*" );
        rBAC.setPermissionKey( "*" );
        rBAC.setResourceTypeKey( "*" );
        RBACHome.create( rBAC );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "rbac_id", Integer.toString( rBAC.getRBACId( ) ) );
        try
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            boolean found = false;
            for ( RBAC aRBAC : rbacs )
            {
                if ( aRBAC.getRBACId( ) == rBAC.getRBACId( ) )
                {
                    found = true;
                    break;
                }
            }
            assertTrue( found );
            bean.doRemoveControlFromRole( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            boolean found = false;
            for ( RBAC aRBAC : rbacs )
            {
                if ( aRBAC.getRBACId( ) == rBAC.getRBACId( ) )
                {
                    found = true;
                    break;
                }
            }
            assertTrue( found );
        }
        finally
        {
            RBACHome.remove( rBAC.getRBACId( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoAssignUsers( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "AssignUsersRole.jsp" ) );
        Collection<AdminUser> users = AdminUserHome.findUserList( );
        for ( AdminUser user : users )
        {
            request.addParameter( "available_users_list", Integer.toString( user.getUserId( ) ) );
        }
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        request.setParameter( "role_key", role.getKey( ) );
        try
        {
            bean.doAssignUsers( request );
            users = AdminUserHome.findUserList( );
            for ( AdminUser user : users )
            {
                assertTrue( AdminUserHome.hasRole( user, role.getKey( ) ) );
            }
        }
        finally
        {
            users = AdminUserHome.findUserList( );
            for ( AdminUser user : users )
            {
                AdminUserHome.removeRoleForUser( user.getUserId( ), role.getKey( ) );
            }
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoAssignUsersInvalidToken( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "AssignUsersRole.jsp" ) + "b" );
        Collection<AdminUser> users = AdminUserHome.findUserList( );
        for ( AdminUser user : users )
        {
            request.addParameter( "available_users_list", Integer.toString( user.getUserId( ) ) );
        }
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        request.setParameter( "role_key", role.getKey( ) );
        try
        {
            bean.doAssignUsers( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            users = AdminUserHome.findUserList( );
            for ( AdminUser user : users )
            {
                assertFalse( AdminUserHome.hasRole( user, role.getKey( ) ) );
            }
        }
        finally
        {
            users = AdminUserHome.findUserList( );
            for ( AdminUser user : users )
            {
                AdminUserHome.removeRoleForUser( user.getUserId( ), role.getKey( ) );
            }
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoAssignUsersNoToken( ) throws AccessDeniedException
    {
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Collection<AdminUser> users = AdminUserHome.findUserList( );
        for ( AdminUser user : users )
        {
            request.addParameter( "available_users_list", Integer.toString( user.getUserId( ) ) );
        }
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        request.setParameter( "role_key", role.getKey( ) );
        try
        {
            bean.doAssignUsers( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            users = AdminUserHome.findUserList( );
            for ( AdminUser user : users )
            {
                assertFalse( AdminUserHome.hasRole( user, role.getKey( ) ) );
            }
        }
        finally
        {
            users = AdminUserHome.findUserList( );
            for ( AdminUser user : users )
            {
                AdminUserHome.removeRoleForUser( user.getUserId( ), role.getKey( ) );
            }
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoSelectPermissions( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "select_resources", "all" );
        request.setParameter( "select_permissions", "all" );
        request.setParameter( "resource_type", Page.RESOURCE_TYPE );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/rbac/select_permissions.html" ) );
        try
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            assertTrue( rbacs.isEmpty( ) );
            bean.doSelectPermissions( request );
            rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            assertEquals( 1, rbacs.size( ) );
            RBAC rbac = rbacs.iterator( ).next( );
            assertEquals( Page.RESOURCE_TYPE, rbac.getResourceTypeKey( ) );
            assertEquals( RBAC.WILDCARD_RESOURCES_ID, rbac.getResourceId( ) );
            assertEquals( RBAC.WILDCARD_PERMISSIONS_KEY, rbac.getPermissionKey( ) );
        }
        finally
        {
            RBACHome.removeForRoleKey( role.getKey( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoSelectPermissionsInvalidToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "select_resources", "all" );
        request.setParameter( "select_permissions", "all" );
        request.setParameter( "resource_type", Page.RESOURCE_TYPE );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/rbac/select_permissions.html" ) + "b" );
        try
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            assertTrue( rbacs.isEmpty( ) );
            bean.doSelectPermissions( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            assertTrue( rbacs.isEmpty( ) );
        }
        finally
        {
            RBACHome.removeForRoleKey( role.getKey( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoSelectPermissionsNoToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "select_resources", "all" );
        request.setParameter( "select_permissions", "all" );
        request.setParameter( "resource_type", Page.RESOURCE_TYPE );
        try
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            assertTrue( rbacs.isEmpty( ) );
            bean.doSelectPermissions( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            Collection<RBAC> rbacs = RBACHome.findResourcesByCode( role.getKey( ) );
            assertTrue( rbacs.isEmpty( ) );
        }
        finally
        {
            RBACHome.removeForRoleKey( role.getKey( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoUnAssignUser( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        int userId = user.getUserId( );
        AdminUserHome.createRoleForUser( userId, role.getKey( ) );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "id_user", Integer.toString( userId ) );
        request.setParameter( "anchor", "anchor" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "AssignUsersRole.jsp" ) );
        try
        {
            assertTrue( AdminUserHome.hasRole( user, role.getKey( ) ) );
            bean.doUnAssignUser( request );
            assertFalse( AdminUserHome.hasRole( user, role.getKey( ) ) );
        }
        finally
        {
            AdminUserHome.removeRoleForUser( userId, role.getKey( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoUnAssignUserInvalidToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        int userId = user.getUserId( );
        AdminUserHome.createRoleForUser( userId, role.getKey( ) );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "id_user", Integer.toString( userId ) );
        request.setParameter( "anchor", "anchor" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "AssignUsersRole.jsp" ) + "b" );
        try
        {
            assertTrue( AdminUserHome.hasRole( user, role.getKey( ) ) );
            bean.doUnAssignUser( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( AdminUserHome.hasRole( user, role.getKey( ) ) );
        }
        finally
        {
            AdminUserHome.removeRoleForUser( userId, role.getKey( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    public void testDoUnAssignUserNoToken( ) throws AccessDeniedException
    {
        AdminRole role = new AdminRole( );
        role.setKey( getRandomName( ) );
        role.setDescription( role.getKey( ) );
        AdminRoleHome.create( role );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        int userId = user.getUserId( );
        AdminUserHome.createRoleForUser( userId, role.getKey( ) );
        RoleManagementJspBean bean = new RoleManagementJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "role_key", role.getKey( ) );
        request.setParameter( "id_user", Integer.toString( userId ) );
        request.setParameter( "anchor", "anchor" );
        try
        {
            assertTrue( AdminUserHome.hasRole( user, role.getKey( ) ) );
            bean.doUnAssignUser( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( AdminUserHome.hasRole( user, role.getKey( ) ) );
        }
        finally
        {
            AdminUserHome.removeRoleForUser( userId, role.getKey( ) );
            AdminRoleHome.remove( role.getKey( ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
