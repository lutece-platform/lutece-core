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
package fr.paris.lutece.portal.web.features;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.html.AbstractPaginator;
import jakarta.inject.Inject;

public class RightJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_SEARCH = "search";
    private static final String PARAMETER_LEVEL = "level";
    private static final String PARAMETER_PLUGIN = "plugin";
    private static final String FILTER_CORE_PLUGIN = "core";
    private static final String ALL_ITEMS_PER_PAGE = "1000";

    private Right right;
    private RightJspBean bean;
    private List<Right> listCreatedRights = new ArrayList<>( );
    private @Inject ISecurityTokenService _securityTokenService;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        right = new Right( );
        right.setId( getRandomName( ) );
        right.setNameKey( getRandomName( ) );
        right.setDescriptionKey( getRandomName( ) );
        right.setLevel( 0 );
        RightHome.create( right );
        bean = new RightJspBean( );
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        RightHome.remove( right.getId( ) );
        listCreatedRights.forEach( createdRight -> RightHome.remove( createdRight.getId( ) ) );
    }
    @Test
    public void testDoAssignUsers( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_right", right.getId( ) );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        request.setParameter( "available_users_list", Integer.toString( user.getUserId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/assign_users_right.html" ) );

        assertFalse( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        bean.doAssignUsers( request );
        assertTrue( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
    }
    @Test
    public void testDoAssignUsersInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_right", right.getId( ) );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        request.setParameter( "available_users_list", Integer.toString( user.getUserId( ) ) );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/assign_users_right.html" ) + "b" );

        assertFalse( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        try
        {
            bean.doAssignUsers( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        }
    }
    @Test
    public void testDoAssignUsersNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_right", right.getId( ) );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        request.setParameter( "available_users_list", Integer.toString( user.getUserId( ) ) );

        assertFalse( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        try
        {
            bean.doAssignUsers( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        }
    }
    @Test
    public void testDoUnAssignUser( ) throws AccessDeniedException
    {
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminUserHome.createRightForUser( user.getUserId( ), right.getId( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_right", right.getId( ) );
        request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
        request.setParameter( "anchor", "anchor" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/assign_users_right.html" ) );

        assertTrue( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        try
        {
            bean.doUnAssignUser( request );
            assertFalse( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        }
        finally
        {
            AdminUserHome.removeRightForUser( user.getUserId( ), right.getId( ) );
        }
    }
    @Test
    public void testDoUnAssignUserInvalidToken( ) throws AccessDeniedException
    {
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminUserHome.createRightForUser( user.getUserId( ), right.getId( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_right", right.getId( ) );
        request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
        request.setParameter( "anchor", "anchor" );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/features/assign_users_right.html" ) + "b" );

        assertTrue( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        try
        {
            bean.doUnAssignUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        }
        finally
        {
            AdminUserHome.removeRightForUser( user.getUserId( ), right.getId( ) );
        }
    }
    @Test
    public void testDoUnAssignUserNoToken( ) throws AccessDeniedException
    {
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        AdminUserHome.createRightForUser( user.getUserId( ), right.getId( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_right", right.getId( ) );
        request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
        request.setParameter( "anchor", "anchor" );

        assertTrue( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        try
        {
            bean.doUnAssignUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminUserHome.getRightsListForUser( user.getUserId( ) ).keySet( ).contains( right.getId( ) ) );
        }
        finally
        {
            AdminUserHome.removeRightForUser( user.getUserId( ), right.getId( ) );
        }
    }

    @Test
    public void testGetManageRights( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = initManageRightsRequest( );

        String strHtml = bean.getManageRights( request );

        assertTrue( strHtml.contains( getAssignLink( right ) ) );
    }

    @Test
    public void testGetManageRightsFilterByLevel( ) throws AccessDeniedException
    {
        Right rightLevel0 = createRight( getRandomName( ), 0, null );
        Right rightLevel3 = createRight( getRandomName( ), 3, null );
        MockHttpServletRequest request = initManageRightsRequest( );
        request.setParameter( PARAMETER_LEVEL, "3" );

        String strHtml = bean.getManageRights( request );

        assertTrue( strHtml.contains( getAssignLink( rightLevel3 ) ) );
        assertFalse( strHtml.contains( getAssignLink( rightLevel0 ) ) );
    }

    @Test
    public void testGetManageRightsFilterByPlugin( ) throws AccessDeniedException
    {
        String strPluginName = getRandomName( );
        Right rightPlugin = createRight( getRandomName( ), 0, strPluginName );
        Right rightCore = createRight( getRandomName( ), 0, null );
        MockHttpServletRequest request = initManageRightsRequest( );
        request.setParameter( PARAMETER_PLUGIN, strPluginName );

        String strHtml = bean.getManageRights( request );

        assertTrue( strHtml.contains( getAssignLink( rightPlugin ) ) );
        assertFalse( strHtml.contains( getAssignLink( rightCore ) ) );
    }

    @Test
    public void testGetManageRightsFilterByCorePlugin( ) throws AccessDeniedException
    {
        Right rightPlugin = createRight( getRandomName( ), 0, getRandomName( ) );
        Right rightCore = createRight( getRandomName( ), 0, null );
        MockHttpServletRequest request = initManageRightsRequest( );
        request.setParameter( PARAMETER_PLUGIN, FILTER_CORE_PLUGIN );

        String strHtml = bean.getManageRights( request );

        assertTrue( strHtml.contains( getAssignLink( rightCore ) ) );
        assertFalse( strHtml.contains( getAssignLink( rightPlugin ) ) );
    }

    @Test
    public void testGetManageRightsSearch( ) throws AccessDeniedException
    {
        String strPluginName = getRandomName( );
        Right rightPlugin = createRight( getRandomName( ), 0, strPluginName );
        MockHttpServletRequest request = initManageRightsRequest( );
        request.setParameter( PARAMETER_SEARCH, "  " + strPluginName.toUpperCase( ) + "  " );

        String strHtml = bean.getManageRights( request );

        assertTrue( strHtml.contains( getAssignLink( rightPlugin ) ) );
        assertFalse( strHtml.contains( getAssignLink( right ) ) );
    }

    @Test
    public void testGetManageRightsSort( ) throws AccessDeniedException
    {
        String strPrefix = getRandomName( );
        Right rightA = createRight( strPrefix + "a", 0, null );
        Right rightB = createRight( strPrefix + "b", 0, null );

        MockHttpServletRequest request = initManageRightsRequest( );
        request.setParameter( PARAMETER_SEARCH, strPrefix );
        request.setParameter( Parameters.SORTED_ATTRIBUTE_NAME, "name" );
        request.setParameter( Parameters.SORTED_ASC, "true" );
        String strHtml = bean.getManageRights( request );
        assertTrue( strHtml.indexOf( getAssignLink( rightA ) ) < strHtml.indexOf( getAssignLink( rightB ) ) );

        request = initManageRightsRequest( );
        request.setParameter( Parameters.SORTED_ATTRIBUTE_NAME, "name" );
        request.setParameter( Parameters.SORTED_ASC, "false" );
        strHtml = bean.getManageRights( request );
        assertTrue( strHtml.indexOf( getAssignLink( rightB ) ) < strHtml.indexOf( getAssignLink( rightA ) ) );
    }

    @Test
    public void testGetManageRightsFiltersKeptAndReset( ) throws AccessDeniedException
    {
        Right rightLevel0 = createRight( getRandomName( ), 0, null );
        Right rightLevel3 = createRight( getRandomName( ), 3, null );
        MockHttpServletRequest request = initManageRightsRequest( );
        request.setParameter( PARAMETER_LEVEL, "3" );
        bean.getManageRights( request );

        request = initManageRightsRequest( );
        request.setParameter( Parameters.SORTED_ATTRIBUTE_NAME, "name" );
        request.setParameter( Parameters.SORTED_ASC, "true" );
        String strHtml = bean.getManageRights( request );
        assertTrue( strHtml.contains( getAssignLink( rightLevel3 ) ) );
        assertFalse( strHtml.contains( getAssignLink( rightLevel0 ) ) );

        request = initManageRightsRequest( );
        request.setParameter( PARAMETER_SEARCH, "" );
        request.setParameter( PARAMETER_LEVEL, "" );
        request.setParameter( PARAMETER_PLUGIN, "" );
        strHtml = bean.getManageRights( request );
        assertTrue( strHtml.contains( getAssignLink( rightLevel3 ) ) );
        assertTrue( strHtml.contains( getAssignLink( rightLevel0 ) ) );
    }

    /**
     * Creates a request with an administrator allowed to manage rights, displaying all the rights on one page. The bean keeps its filters between the
     * requests, like in a user session.
     *
     * @return the request
     * @throws AccessDeniedException
     *             if the user is not allowed to manage rights
     */
    private MockHttpServletRequest initManageRightsRequest( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), RightJspBean.RIGHT_MANAGE_RIGHTS );
        bean.init( request, RightJspBean.RIGHT_MANAGE_RIGHTS );
        request.setParameter( AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, ALL_ITEMS_PER_PAGE );

        return request;
    }

    /**
     * Creates a right removed at the end of the test
     *
     * @param strNameKey
     *            The name key of the right, displayed as name when not found in the bundles
     * @param nLevel
     *            The level of the right
     * @param strPluginName
     *            The plugin of the right, null for a core right
     * @return the created right
     */
    private Right createRight( String strNameKey, int nLevel, String strPluginName )
    {
        Right newRight = new Right( );
        newRight.setId( getRandomName( ) );
        newRight.setNameKey( strNameKey );
        newRight.setDescriptionKey( getRandomName( ) );
        newRight.setLevel( nLevel );
        newRight.setPluginName( strPluginName );
        RightHome.create( newRight );
        listCreatedRights.add( newRight );

        return newRight;
    }

    /**
     * Returns the link to assign users to a right, rendered once per displayed right
     *
     * @param rightDisplayed
     *            The right
     * @return the link
     */
    private String getAssignLink( Right rightDisplayed )
    {
        return "id_right=" + rightDisplayed.getId( );
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }
}
