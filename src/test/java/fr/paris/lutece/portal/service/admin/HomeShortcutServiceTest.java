/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.admin;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * Tests of the shortcuts of the home page
 */
public class HomeShortcutServiceTest extends LuteceTestCase
{
    private static final String DATASTORE_KEY_DEFAULT = "core.home.shortcuts.default";

    @Inject
    private HomeShortcutService _service;
    private AdminUser _user;
    private List<FeatureGroup> _listFeatureGroups;
    private String _strDefaultBefore;

    @BeforeEach
    protected void setUp( )
    {
        // A user nobody else has: its preferences are its own
        _user = new AdminUser( );
        _user.setUserId( 900000 + new SecureRandom( ).nextInt( 99999 ) );
        _listFeatureGroups = Collections.singletonList( getFeatureGroup( IntStream.rangeClosed( 1, 10 ).mapToObj( n -> "R" + n ).toArray( String [ ]::new ) ) );
        _strDefaultBefore = DatastoreService.getDataValue( DATASTORE_KEY_DEFAULT, null );
        _service.saveDefaultShortcuts( Arrays.asList( "R1", "R2" ), _listFeatureGroups );
    }

    @AfterEach
    protected void tearDown( )
    {
        _service.resetUserShortcuts( _user );

        if ( _strDefaultBefore != null )
        {
            DatastoreService.setDataValue( DATASTORE_KEY_DEFAULT, _strDefaultBefore );
        }
        else
        {
            DatastoreService.removeData( DATASTORE_KEY_DEFAULT );
        }
    }

    @Test
    public void testDefaultWithoutOwnList( )
    {
        assertFalse( _service.hasOwnShortcuts( _user ) );
        assertEquals( Arrays.asList( "R1", "R2" ), getIds( _service.getShortcuts( _user, _listFeatureGroups ) ) );
    }

    @Test
    public void testOwnListThenReset( )
    {
        _service.saveUserShortcuts( _user, Arrays.asList( "R3", "R3", "UNKNOWN", "R1" ), _listFeatureGroups );

        assertTrue( _service.hasOwnShortcuts( _user ) );
        assertEquals( Arrays.asList( "R3", "R1" ), getIds( _service.getShortcuts( _user, _listFeatureGroups ) ), "repeated and unknown ids are dropped" );

        _service.resetUserShortcuts( _user );

        assertFalse( _service.hasOwnShortcuts( _user ) );
        assertEquals( Arrays.asList( "R1", "R2" ), getIds( _service.getShortcuts( _user, _listFeatureGroups ) ) );
    }

    @Test
    public void testEmptyOwnList( )
    {
        _service.saveUserShortcuts( _user, Collections.emptyList( ), _listFeatureGroups );

        assertTrue( _service.hasOwnShortcuts( _user ) );
        assertTrue( _service.getShortcuts( _user, _listFeatureGroups ).isEmpty( ), "a user who removed every shortcut does not get the default back" );
    }

    @Test
    public void testHighestNumber( )
    {
        List<String> listIds = IntStream.rangeClosed( 1, 10 ).mapToObj( n -> "R" + n ).collect( Collectors.toList( ) );
        _service.saveUserShortcuts( _user, listIds, _listFeatureGroups );

        assertEquals( listIds.subList( 0, HomeShortcutService.MAX_SHORTCUTS ), getIds( _service.getShortcuts( _user, _listFeatureGroups ) ) );
    }

    @Test
    public void testRightLost( )
    {
        _service.saveUserShortcuts( _user, Arrays.asList( "R3", "R1" ), _listFeatureGroups );

        // The user no longer has R3: it leaves the shortcuts, its place in the list is kept should the right come back
        List<FeatureGroup> listWithoutR3 = Collections.singletonList( getFeatureGroup( "R1", "R2" ) );
        assertEquals( Collections.singletonList( "R1" ), getIds( _service.getShortcuts( _user, listWithoutR3 ) ) );
    }

    @Test
    public void testUserShortcutsDisabled( )
    {
        String strEnabledBefore = DatastoreService.getDataValue( HomeShortcutService.DATASTORE_KEY_USER_SHORTCUTS_ENABLED, null );
        _service.saveUserShortcuts( _user, Arrays.asList( "R3" ), _listFeatureGroups );

        try
        {
            // The site no longer lets the users choose: everyone has the default, the saved list is kept for later
            DatastoreService.setDataValue( HomeShortcutService.DATASTORE_KEY_USER_SHORTCUTS_ENABLED, "0" );
            assertFalse( _service.isUserShortcutsEnabled( ) );
            assertFalse( _service.hasOwnShortcuts( _user ) );
            assertEquals( Arrays.asList( "R1", "R2" ), getIds( _service.getShortcuts( _user, _listFeatureGroups ) ) );

            DatastoreService.setDataValue( HomeShortcutService.DATASTORE_KEY_USER_SHORTCUTS_ENABLED, "1" );
            assertEquals( Collections.singletonList( "R3" ), getIds( _service.getShortcuts( _user, _listFeatureGroups ) ) );
        }
        finally
        {
            restore( HomeShortcutService.DATASTORE_KEY_USER_SHORTCUTS_ENABLED, strEnabledBefore );
        }
    }

    private static void restore( String strKey, String strValue )
    {
        if ( strValue != null )
        {
            DatastoreService.setDataValue( strKey, strValue );
        }
        else
        {
            DatastoreService.removeData( strKey );
        }
    }

    private static FeatureGroup getFeatureGroup( String... strIds )
    {
        FeatureGroup featureGroup = new FeatureGroup( );
        featureGroup.setId( "TEST_GROUP" );

        for ( String strId : strIds )
        {
            Right right = new Right( );
            right.setId( strId );
            right.setUrl( "jsp/admin/Test" + strId + ".jsp" );
            featureGroup.addFeature( right );
        }

        return featureGroup;
    }

    private static List<String> getIds( List<Right> listRights )
    {
        return listRights.stream( ).map( Right::getId ).collect( Collectors.toList( ) );
    }
}
