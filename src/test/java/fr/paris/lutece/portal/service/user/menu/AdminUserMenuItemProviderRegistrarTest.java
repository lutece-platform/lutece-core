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

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.menu.AdminUserMenuItem;
import fr.paris.lutece.test.LuteceTestCase;

public class AdminUserMenuItemProviderRegistrarTest extends LuteceTestCase
{
    private TestAdminUserMenuService _adminUserMenuService;
    private AdminUserMenuItemProviderRegistrar _instance;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _adminUserMenuService = new TestAdminUserMenuService( );
        _instance = new AdminUserMenuItemProviderRegistrar( _adminUserMenuService );
        _instance.setBeanName( "junit" );
    }

    public void testSetClassName( )
            throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        _instance.setClassName( TestAdminUserMenuItemProvider.class.getName( ) );
        _instance.registerAdminUserMenuItemProvider( );
        List<AdminUserMenuItem> items = _adminUserMenuService.getItems( new MockHttpServletRequest( ) );

        assertNotNull( items );
        assertEquals( 1, items.size( ) );
        assertEquals( TestAdminUserMenuItemProvider.ITEM, items.get( 0 ) );
    }

    public void testSetClassNameProviderAlreadySetDirectly( )
            throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        _instance.setProvider( new TestAdminUserMenuItemProvider( ) );
        try
        {
            _instance.setClassName( TestAdminUserMenuItemProvider.class.getName( ) );
            fail( "Should have thrown" );
        }
        catch ( IllegalStateException e )
        {

        }
    }

    public void testSetClassNameProviderAlreadySetByClassName( )
            throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        _instance.setClassName( TestAdminUserMenuItemProvider.class.getName( ) );
        try
        {
            _instance.setClassName( TestAdminUserMenuItemProvider.class.getName( ) );
            fail( "Should have thrown" );
        }
        catch ( IllegalStateException e )
        {

        }
    }

    public void testSetProvider( )
            throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        _instance.setProvider( new TestAdminUserMenuItemProvider( ) );
        _instance.registerAdminUserMenuItemProvider( );
        List<AdminUserMenuItem> items = _adminUserMenuService.getItems( new MockHttpServletRequest( ) );

        assertNotNull( items );
        assertEquals( 1, items.size( ) );
        assertEquals( TestAdminUserMenuItemProvider.ITEM, items.get( 0 ) );
    }

    public void testSetProviderProviderAlreadySetDirectly( )
            throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        _instance.setProvider( new TestAdminUserMenuItemProvider( ) );
        try
        {
            _instance.setProvider( new TestAdminUserMenuItemProvider( ) );
            fail( "Should have thrown" );
        }
        catch ( IllegalStateException e )
        {

        }
    }

    public void testSetProviderProviderAlreadySetByClassName( )
            throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        _instance.setClassName( TestAdminUserMenuItemProvider.class.getName( ) );
        try
        {
            _instance.setProvider( new TestAdminUserMenuItemProvider( ) );
            fail( "Should have thrown" );
        }
        catch ( IllegalStateException e )
        {

        }
    }

    public void testRegisterAdminUserMenuItemProviderNullProvider( )
    {
        try
        {
            _instance.registerAdminUserMenuItemProvider( );
            fail( "Should have thrown" );
        }
        catch ( IllegalStateException e )
        {

        }
    }
}
