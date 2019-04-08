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
package fr.paris.lutece.portal.business.user.menu;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

public class AbstractAdminUserMenuItemProviderTest extends LuteceTestCase
{
    public void testIsInvokedDisabledPlugin( )
    {
        IAdminUserMenuItemProvider provider = new AbstractAdminUserMenuItemProvider( )
        {

            @Override
            public AdminUserMenuItem getItem( HttpServletRequest request )
            {
                return null;
            }

            @Override
            protected boolean isItemProviderInvoked( HttpServletRequest request )
            {
                request.setAttribute( "called", Boolean.TRUE );
                return true;
            }

        };
        final String inexistantPluginName = "inexistantPlugin";
        provider.setName( inexistantPluginName + ".provider" );
        assertFalse( PluginService.isPluginEnable( inexistantPluginName ) );

        HttpServletRequest request = new MockHttpServletRequest( );
        assertFalse( provider.isInvoked( request ) );
        assertNull( request.getAttribute( "called" ) );
    }
    
    public void testIsInvokedEnabledPlugin( )
    {
        IAdminUserMenuItemProvider provider = new AbstractAdminUserMenuItemProvider( )
        {

            @Override
            public AdminUserMenuItem getItem( HttpServletRequest request )
            {
                return null;
            }

            @Override
            protected boolean isItemProviderInvoked( HttpServletRequest request )
            {
                request.setAttribute( "called", Boolean.TRUE );
                return true;
            }

        };
        provider.setName( "core" );

        HttpServletRequest request = new MockHttpServletRequest( );
        assertTrue( provider.isInvoked( request ) );
        Boolean called = ( Boolean ) request.getAttribute( "called" );
        assertNotNull( called );
        assertTrue( called.booleanValue( ) );
    }
}
