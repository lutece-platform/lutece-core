/*
 * Copyright (c) 2002-2023, City of Paris
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
package fr.paris.lutece.portal.service.rbac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

public class RBACServiceTest extends LuteceTestCase
{
    private final static class TestResource implements RBACResource
    {

        @Override
        public int hashCode( )
        {
            return Objects.hash( _strResourceId, _strResourceType );
        }

        @Override
        public boolean equals( Object obj )
        {
            if ( this == obj )
                return true;
            if ( obj == null )
                return false;
            if ( getClass( ) != obj.getClass( ) )
                return false;
            TestResource other = ( TestResource ) obj;
            return Objects.equals( _strResourceId, other._strResourceId )
                    && Objects.equals( _strResourceType, other._strResourceType );
        }

        private final String _strResourceType;
        private final String _strResourceId;

        TestResource( String strResourcetype, String strResourceId )
        {
            _strResourceType = strResourcetype;
            _strResourceId = strResourceId;
        }

        @Override
        public String getResourceTypeCode( )
        {
            return _strResourceType;
        }

        @Override
        public String getResourceId( )
        {
            return _strResourceId;
        }

    }

    private static final String[ ][ ] data = { { "JUNITROLE1", "JUNITTYPE1", "JUNITID1", "JUNITPERM1" },
            { "JUNITROLE2", "JUNITTYPE2", "*", "JUNITPERM2" }, { "JUNITROLE3", "JUNITTYPE3", "JUNITID3", "*" },
            { "JUNITROLE4", "JUNITTYPE4", "JUNITID4", "*" }, { "JUNITROLE5", "JUNITTYPE4", "JUNITID5", "*" },
            { "JUNITROLE6", "JUNITTYPE6", "*", "JUNITPERM6" }, { "JUNITROLE6", "JUNITTYPE6", "*", "JUNITPERM6_BIS" },
            { "JUNITROLE7", "JUNITTYPE6", "*", "JUNITPERM6_TER" }, };

    private Collection<RBAC> rbacs;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        rbacs = new ArrayList<>( );
        for ( String[ ] rbacData : data )
        {
            RBAC rbac = new RBAC( );
            rbac.setRoleKey( rbacData[ 0 ] );
            rbac.setResourceTypeKey( rbacData[ 1 ] );
            rbac.setResourceId( rbacData[ 2 ] );
            rbac.setPermissionKey( rbacData[ 3 ] );
            RBACHome.create( rbac );
            rbacs.add( rbac );
        }
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        for ( RBAC rbac : rbacs )
        {
            try
            {
                RBACHome.remove( rbac.getRBACId( ) );
            }
            catch ( Exception e )
            {
                System.err.println( "Failed to teardown RBAC " + rbac.getRBACId( ) + " ( " + e.getMessage( ) + ")" );
            }
        }
    }

    @Test
    public void testGetAuthorizedCollectionEmpty( )
    {
        List<TestResource> resources = Collections.emptyList( );
        User user = new TestUser( "JUNITROLE1" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollectionNoUser( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        List<TestResource> resources = Arrays.asList( testResource );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1",
                ( User ) null );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollection( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( "JUNITROLE1" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 1, authorized.size( ) );
        assertTrue( authorized.contains( testResource ) );
    }

    @Test
    public void testGetAuthorizedCollectionNoRole( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollectionWrongType( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1_WRONG", "JUNITID1" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( "JUNITROLE1" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollectionWrongPerm( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( "JUNITROLE1" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1_WRONG",
                user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollectionWrongRole( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( "JUNITROLE1_WRONG" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollectionWrongResourceId( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1_WRONG" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( "JUNITROLE1" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedCollectionWildcardId( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE2", "JUNITID2" );
        TestResource testResource2 = new TestResource( "JUNITTYPE2", "JUNITID2_BIS" );
        List<TestResource> resources = Arrays.asList( testResource, testResource2 );
        User user = new TestUser( "JUNITROLE2" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM2", user );
        assertEquals( 2, authorized.size( ) );
        assertTrue( authorized.contains( testResource ) );
        assertTrue( authorized.contains( testResource2 ) );
    }

    @Test
    public void testGetAuthorizedCollectionWildcardPerm( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE3", "JUNITID3" );
        List<TestResource> resources = Arrays.asList( testResource );
        User user = new TestUser( "JUNITROLE3" );
        Arrays.asList( "JUNITPERM3", "JUNITPERM3BIS", "JUNITPERM3TER" ).forEach( perm -> {
            Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, perm, user );
            assertEquals( 1, authorized.size( ) );
            assertTrue( authorized.contains( testResource ) );
        } );
    }

    @Test
    public void testGetAuthorizedCollectionMultipleRoles( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        TestResource testResource2 = new TestResource( "JUNITTYPE2", "JUNITID3" );
        TestResource testResource3 = new TestResource( "JUNITTYPE3", "JUNITID3" );
        List<TestResource> resources = Arrays.asList( testResource, testResource2, testResource3 );
        User user = new TestUser( "JUNITROLE1", "JUNITROLE3" );
        Collection<TestResource> authorized = RBACService.getAuthorizedCollection( resources, "JUNITPERM1", user );
        assertEquals( 2, authorized.size( ) );
        assertTrue( authorized.contains( testResource ) );
        assertTrue( authorized.contains( testResource3 ) );
    }

    @Test
    public void testGetAuthorizedReferenceListEmpty( )
    {
        ReferenceList refList = new ReferenceList( );
        User user = new TestUser( "JUNITROLE1" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListNoUser( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1", "JUNITID1" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1",
                ( User ) null );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceList( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1", user );
        assertEquals( 1, authorized.size( ) );
        assertEquals( "JUNITID1", authorized.get( 0 ).getCode( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListNoRole( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1", "JUNITID1" );
        User user = new TestUser( );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListWrongType( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1_WRONG", "JUNITPERM1",
                user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListWrongPerm( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1_WRONG",
                user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListWrongRole( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1_WRONG" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListWrongResourceId( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID1_WRONG", "JUNITID1_WRONG" );
        User user = new TestUser( "JUNITROLE1" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE1", "JUNITPERM1", user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedReferenceListWildcardId( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID2", "JUNITID2" );
        refList.addItem( "JUNITID2_BIS", "JUNITID2_BIS" );
        User user = new TestUser( "JUNITROLE2" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE2", "JUNITPERM2", user );
        assertEquals( 2, authorized.size( ) );
        assertTrue( authorized.stream( ).anyMatch( refItem -> "JUNITID2".equals( refItem.getCode( ) ) ) );
        assertTrue( authorized.stream( ).anyMatch( refItem -> "JUNITID2_BIS".equals( refItem.getCode( ) ) ) );
    }

    @Test
    public void testGetAuthorizedReferenceListWildcardPerm( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID3", "JUNITID3" );
        User user = new TestUser( "JUNITROLE3" );
        Arrays.asList( "JUNITPERM3", "JUNITPERM3BIS", "JUNITPERM3TER" ).forEach( perm -> {
            ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE3", perm, user );
            assertEquals( 1, authorized.size( ) );
            assertEquals( "JUNITID3", authorized.get( 0 ).getCode( ) );
        } );
    }

    @Test
    public void testGetAuthorizedReferenceListMultipleRoles( )
    {
        ReferenceList refList = new ReferenceList( );
        refList.addItem( "JUNITID4", "JUNITID4" );
        refList.addItem( "JUNITID5", "JUNITID5" );
        refList.addItem( "JUNITID6", "JUNITID6" );
        User user = new TestUser( "JUNITROLE4", "JUNITROLE5" );
        ReferenceList authorized = RBACService.getAuthorizedReferenceList( refList, "JUNITTYPE4", "JUNITPERM4", user );
        assertEquals( 2, authorized.size( ) );
        assertTrue( authorized.stream( ).anyMatch( refItem -> "JUNITID4".equals( refItem.getCode( ) ) ) );
        assertTrue( authorized.stream( ).anyMatch( refItem -> "JUNITID5".equals( refItem.getCode( ) ) ) );
    }

    @Test
    public void testGetAuthorizedActionsCollection( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM1" );
        List<RBACAction> actions = Arrays.asList( testAction );
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 1, authorized.size( ) );
        assertTrue(
                authorized.stream( ).map( RBACAction::getPermission ).allMatch( perm -> "JUNITPERM1".equals( perm ) ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionWrondResourceId( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM1" );
        List<RBACAction> actions = Arrays.asList( testAction );
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1_WRONG" );
        User user = new TestUser( "JUNITROLE1" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionWrondResourceType( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM1" );
        List<RBACAction> actions = Arrays.asList( testAction );
        TestResource testResource = new TestResource( "JUNITTYPE1_WRONG", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionEmptyCollection( )
    {
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        User user = new TestUser( "JUNITROLE1" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( Collections.emptyList( ),
                testResource, user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionNoPermOnResource( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM1" );
        List<RBACAction> actions = Arrays.asList( testAction );
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID2" );
        User user = new TestUser( "JUNITROLE1" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionNoRole( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM1" );
        List<RBACAction> actions = Arrays.asList( testAction );
        TestResource testResource = new TestResource( "JUNITTYPE1", "JUNITID1" );
        User user = new TestUser( );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 0, authorized.size( ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionWildcardPermission( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM1" );
        TestRBACAction testAction2 = new TestRBACAction( "JUNITPERM1_BIS" );
        List<RBACAction> actions = Arrays.asList( testAction, testAction2 );
        TestResource testResource = new TestResource( "JUNITTYPE3", "JUNITID3" );
        User user = new TestUser( "JUNITROLE3" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 2, authorized.size( ) );
        assertTrue(
                authorized.stream( ).map( RBACAction::getPermission ).anyMatch( perm -> "JUNITPERM1".equals( perm ) ) );
        assertTrue( authorized.stream( ).map( RBACAction::getPermission )
                .anyMatch( perm -> "JUNITPERM1_BIS".equals( perm ) ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionMultiplePermission( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM6" );
        TestRBACAction testAction2 = new TestRBACAction( "JUNITPERM6_BIS" );
        TestRBACAction testAction3 = new TestRBACAction( "JUNITPERM6_TER" );
        List<RBACAction> actions = Arrays.asList( testAction, testAction2, testAction3 );
        TestResource testResource = new TestResource( "JUNITTYPE6", "JUNITID6" );
        User user = new TestUser( "JUNITROLE6" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 2, authorized.size( ) );
        assertTrue(
                authorized.stream( ).map( RBACAction::getPermission ).anyMatch( perm -> "JUNITPERM6".equals( perm ) ) );
        assertTrue( authorized.stream( ).map( RBACAction::getPermission )
                .anyMatch( perm -> "JUNITPERM6_BIS".equals( perm ) ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionMultipleRoles( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM6" );
        TestRBACAction testAction2 = new TestRBACAction( "JUNITPERM6_BIS" );
        TestRBACAction testAction3 = new TestRBACAction( "JUNITPERM6_TER" );
        TestRBACAction testAction4 = new TestRBACAction( "JUNITPERM6_QUATER" );
        List<RBACAction> actions = Arrays.asList( testAction, testAction2, testAction3, testAction4 );
        TestResource testResource = new TestResource( "JUNITTYPE6", "JUNITID6" );
        User user = new TestUser( "JUNITROLE6", "JUNITROLE7" );
        Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource, user );
        assertEquals( 3, authorized.size( ) );
        assertTrue(
                authorized.stream( ).map( RBACAction::getPermission ).anyMatch( perm -> "JUNITPERM6".equals( perm ) ) );
        assertTrue( authorized.stream( ).map( RBACAction::getPermission )
                .anyMatch( perm -> "JUNITPERM6_BIS".equals( perm ) ) );
        assertTrue( authorized.stream( ).map( RBACAction::getPermission )
                .anyMatch( perm -> "JUNITPERM6_TER".equals( perm ) ) );
    }

    @Test
    public void testGetAuthorizedActionsCollectionWildcardId( )
    {
        TestRBACAction testAction = new TestRBACAction( "JUNITPERM2" );
        List<RBACAction> actions = Arrays.asList( testAction );
        Arrays.asList( "JUNITID2", "JUNITID2_BIS", "JUNITID2_TER" ).forEach( id -> {
            TestResource testResource = new TestResource( "JUNITTYPE2", id );
            User user = new TestUser( "JUNITROLE2" );
            Collection<RBACAction> authorized = RBACService.getAuthorizedActionsCollection( actions, testResource,
                    user );
            assertEquals( 1, authorized.size( ) );
            assertTrue( authorized.stream( ).map( RBACAction::getPermission )
                    .allMatch( perm -> "JUNITPERM2".equals( perm ) ) );
        } );
    }

}
