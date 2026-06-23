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
package fr.paris.lutece.portal.web.workgroup;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;

public class AdminWorkgroupJspBeanTest extends LuteceTestCase
{
    private static final String RIGHT = "CORE_WORKGROUPS_MANAGEMENT";
    private static final String TOKEN_ASSIGN = "admin/workgroup/assign_users_workgroup.html";
    private static final String TOKEN_CREATE = "admin/workgroup/create_workgroup.html";
    private static final String TOKEN_MODIFY = "admin/workgroup/modify_workgroup.html";
    private static final String TOKEN_REMOVE = "jsp/admin/workgroup/DoRemoveWorkgroup.jsp";

    private AdminWorkgroup adminWorkgroup;
    private @Inject AdminWorkgroupJspBean bean;
    private @Inject ISecurityTokenService _securityTokenService;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        adminWorkgroup = new AdminWorkgroup( );
        adminWorkgroup.setKey( getRandomName( ) );
        adminWorkgroup.setDescription( adminWorkgroup.getKey( ) );
        AdminWorkgroupHome.create( adminWorkgroup );
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        AdminWorkgroupHome.removeAllUsersForWorkgroup( adminWorkgroup.getKey( ) );
        AdminWorkgroupHome.remove( adminWorkgroup.getKey( ) );
    }

    /**
     * Builds a request with an authenticated admin user holding the workgroup management permission.
     *
     * @return the request
     */
    private MockHttpServletRequest authedRequest( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), RIGHT );
        return request;
    }

    @Test
    public void testDoAssignUsers( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "assignUsers" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        request.setParameter( "list_users", Integer.toString( user.getUserId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_ASSIGN ) );

        assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        bean.processController( request, new MockHttpServletResponse( ) );
        assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
    }

    @Test
    public void testDoAssignUsersInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "assignUsers" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        request.setParameter( "list_users", Integer.toString( user.getUserId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_ASSIGN ) + "b" );

        assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        }
    }

    @Test
    public void testDoAssignUsersNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "assignUsers" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        request.setParameter( "list_users", Integer.toString( user.getUserId( ) ) );

        assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        }
    }

    @Test
    public void testGetAssignUsers( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_VIEW, "assignUsers" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );

        assertNotNull( bean.processController( request, new MockHttpServletResponse( ) ) );
    }

    @Test
    public void testDoCreateWorkgroup( ) throws AccessDeniedException
    {
        final String key = getRandomName( );
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "createWorkgroup" );
        request.setParameter( "workgroup_key", key );
        request.setParameter( "workgroup_description", key );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_CREATE ) );

        assertFalse( AdminWorkgroupHome.checkExistWorkgroup( key ) );
        bean.processController( request, new MockHttpServletResponse( ) );
        assertTrue( AdminWorkgroupHome.checkExistWorkgroup( key ) );

        AdminWorkgroupHome.remove( key );
    }

    @Test
    public void testDoCreateWorkgroupInvalidToken( ) throws AccessDeniedException
    {
        final String key = getRandomName( );
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "createWorkgroup" );
        request.setParameter( "workgroup_key", key );
        request.setParameter( "workgroup_description", key );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_CREATE ) + "b" );

        assertFalse( AdminWorkgroupHome.checkExistWorkgroup( key ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminWorkgroupHome.checkExistWorkgroup( key ) );
        }

        AdminWorkgroupHome.remove( key );
    }

    @Test
    public void testDoCreateWorkgroupNoToken( ) throws AccessDeniedException
    {
        final String key = getRandomName( );
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "createWorkgroup" );
        request.setParameter( "workgroup_key", key );
        request.setParameter( "workgroup_description", key );

        assertFalse( AdminWorkgroupHome.checkExistWorkgroup( key ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminWorkgroupHome.checkExistWorkgroup( key ) );
        }

        AdminWorkgroupHome.remove( key );
    }

    @Test
    public void testDoModifyWorkgroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "modifyWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( "workgroup_description", adminWorkgroup.getDescription( ) + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_MODIFY ) );

        assertEquals( adminWorkgroup.getKey( ), adminWorkgroup.getDescription( ) );
        bean.processController( request, new MockHttpServletResponse( ) );
        assertEquals( adminWorkgroup.getDescription( ) + "_mod", AdminWorkgroupHome.findByPrimaryKey( adminWorkgroup.getKey( ) ).getDescription( ) );
    }

    @Test
    public void testDoModifyWorkgroupInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "modifyWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( "workgroup_description", adminWorkgroup.getDescription( ) + "_mod" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_MODIFY ) + "b" );

        assertEquals( adminWorkgroup.getKey( ), adminWorkgroup.getDescription( ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( adminWorkgroup.getDescription( ), AdminWorkgroupHome.findByPrimaryKey( adminWorkgroup.getKey( ) ).getDescription( ) );
        }
    }

    @Test
    public void testDoModifyWorkgroupNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "modifyWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( "workgroup_description", adminWorkgroup.getDescription( ) + "_mod" );

        assertEquals( adminWorkgroup.getKey( ), adminWorkgroup.getDescription( ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( adminWorkgroup.getDescription( ), AdminWorkgroupHome.findByPrimaryKey( adminWorkgroup.getKey( ) ).getDescription( ) );
        }
    }

    @Test
    public void testGetConfirmRemoveWorkgroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_VIEW, "confirmRemoveWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );

        bean.processController( request, new MockHttpServletResponse( ) );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        assertTrue( message.getRequestParameters( ).containsKey( "workgroup_key" ) );
        assertEquals( adminWorkgroup.getKey( ), message.getRequestParameters( ).get( "workgroup_key" ) );
    }

    @Test
    public void testDoRemoveWorkgroup( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "removeWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_REMOVE ) );

        assertTrue( AdminWorkgroupHome.checkExistWorkgroup( adminWorkgroup.getKey( ) ) );
        bean.processController( request, new MockHttpServletResponse( ) );
        assertFalse( AdminWorkgroupHome.checkExistWorkgroup( adminWorkgroup.getKey( ) ) );
    }

    @Test
    public void testDoRemoveWorkgroupInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "removeWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_REMOVE ) + "b" );

        assertTrue( AdminWorkgroupHome.checkExistWorkgroup( adminWorkgroup.getKey( ) ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminWorkgroupHome.checkExistWorkgroup( adminWorkgroup.getKey( ) ) );
        }
    }

    @Test
    public void testDoRemoveWorkgroupNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "removeWorkgroup" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );

        assertTrue( AdminWorkgroupHome.checkExistWorkgroup( adminWorkgroup.getKey( ) ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminWorkgroupHome.checkExistWorkgroup( adminWorkgroup.getKey( ) ) );
        }
    }

    @Test
    public void testDoUnAssignUser( ) throws AccessDeniedException
    {
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminWorkgroupHome.addUserForWorkgroup( user, adminWorkgroup.getKey( ) );

        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "unassignUser" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_ASSIGN ) );

        assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        bean.processController( request, new MockHttpServletResponse( ) );
        assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
    }

    @Test
    public void testDoUnAssignUserInvalidToken( ) throws AccessDeniedException
    {
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminWorkgroupHome.addUserForWorkgroup( user, adminWorkgroup.getKey( ) );

        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "unassignUser" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_ASSIGN ) + "b" );

        assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have Thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        }
    }

    @Test
    public void testDoUnAssignUserNoToken( ) throws AccessDeniedException
    {
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminWorkgroupHome.addUserForWorkgroup( user, adminWorkgroup.getKey( ) );

        MockHttpServletRequest request = authedRequest( );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "unassignUser" );
        request.setParameter( "workgroup_key", adminWorkgroup.getKey( ) );
        request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );

        assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        try
        {
            bean.processController( request, new MockHttpServletResponse( ) );
            fail( "Should have Thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, adminWorkgroup.getKey( ) ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
