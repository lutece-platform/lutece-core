/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.portal.service.user.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.menu.AdminUserMenuItem;
import fr.paris.lutece.portal.business.user.menu.IAdminUserMenuItemProvider;
import fr.paris.lutece.test.LuteceTestCase;

public class AdminUserMenuServiceTest extends LuteceTestCase
{
    private static final int NUM_ITEMS = 5;
    private AdminUserMenuService _service;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _service = new AdminUserMenuService( );
        for ( int i = 0; i < NUM_ITEMS; i++ )
        {
            IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( Integer.toString( i ) );
            item.setName( Integer.toString( i ) );
            _service.addItemProvider( item, null, null );
        }
    }

    public void testAddItemProvider( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, null, null );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( NUM_ITEMS ).getItemClass( ) );
    }

    public void testAddItemProviderAfterFirst( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, "0", null );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( 1 ).getItemClass( ) );
    }

    public void testAddItemProviderAfterLast( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, Integer.toString( NUM_ITEMS - 1 ), null );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( NUM_ITEMS ).getItemClass( ) );
    }

    public void testAddItemProviderAfterUnknown( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, "unknown", null );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( NUM_ITEMS ).getItemClass( ) );
    }

    public void testAddItemProviderBeforeFirst( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, null, "0" );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( 0 ).getItemClass( ) );
    }

    public void testAddItemProviderBeforeLast( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, null, Integer.toString( NUM_ITEMS - 1 ) );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( NUM_ITEMS - 1 ).getItemClass( ) );
    }

    public void testAddItemProviderBeforeUnknown( )
    {
        final String strClass = "testAddItemProvider";
        IAdminUserMenuItemProvider item = new TestAdminUserMenuItemProvider( strClass );
        item.setName( strClass );
        _service.addItemProvider( item, null, "unknown" );

        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS + 1, items.size( ) );
        assertEquals( strClass, items.get( NUM_ITEMS ).getItemClass( ) );
    }

    public void testGetItems( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( NUM_ITEMS, items.size( ) );
        for ( int i = 0; i < NUM_ITEMS; i++ )
        {
            assertEquals( Integer.toString( i ), items.get( i ).getItemClass( ) );
        }
    }

    public void testGetItemsInvoked( )
    {
        assertTrue( NUM_ITEMS >= 2 );
        HttpServletRequest request = new MockHttpServletRequest( );
        request.setAttribute( TestAdminUserMenuItemProvider.INVOKED_NAME, Integer.toString( NUM_ITEMS - 2 ) );
        List<AdminUserMenuItem> items = _service.getItems( request );

        assertNotNull( items );
        assertEquals( 1, items.size( ) );
        assertEquals( Integer.toString( NUM_ITEMS - 2 ), items.get( 0 ).getItemClass( ) );
    }
}
