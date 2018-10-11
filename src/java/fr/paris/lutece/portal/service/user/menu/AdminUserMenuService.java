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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.menu.AdminUserMenuItem;
import fr.paris.lutece.portal.business.user.menu.IAdminUserMenuItemProvider;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Registry of admin user menu item providers
 * 
 * @since 6.2.0
 */
public class AdminUserMenuService
{
    public static final String BEAN_NAME = "adminUserMenuService";

    private final List<IAdminUserMenuItemProvider> _itemProviders = new ArrayList<>( );

    /**
     * Add an item provider to the registry
     * 
     * Placement of the item can be altered with strAfterName or strBeforeName
     * 
     * @param itemProvider
     *            admin user menu item provider
     * @param strBeforeName
     *            add the item provider before the provider with this name
     * @param strAfterName
     *            add the item provider after the provider with this name
     */
    public void addItemProvider( IAdminUserMenuItemProvider itemProvider, String strAfterName, String strBeforeName )
    {
        if ( strAfterName != null )
        {
            addItemProviderInPosition( itemProvider, strAfterName, i -> {
                i.add( itemProvider );
            } );
        }
        else if ( strBeforeName != null )
        {
            addItemProviderInPosition( itemProvider, strBeforeName, i -> {
                if ( i.hasPrevious( ) )
                {
                    i.previous( );
                    i.add( itemProvider );
                }
                else
                {
                    _itemProviders.add( 0, itemProvider );
                }
            } );
        }
        else
        {
            _itemProviders.add( itemProvider );
        }
        AppLogService.info( "New admin user menu item provider registered : " + itemProvider.getName( ) );
    }

    private void addItemProviderInPosition( IAdminUserMenuItemProvider itemProvider, String strRefName,
            Consumer<ListIterator<IAdminUserMenuItemProvider>> adder )
    {
        ListIterator<IAdminUserMenuItemProvider> iterator = _itemProviders.listIterator( );
        boolean bFound = false;
        while ( iterator.hasNext( ) )
        {
            IAdminUserMenuItemProvider anItem = iterator.next( );
            if ( anItem.getName( ).equals( strRefName ) )
            {
                bFound = true;
                adder.accept( iterator );
                break;
            }
        }
        if ( !bFound )
        {
            AppLogService.error( "Did not find admin user menu item provider named <" + strRefName
                    + "> when registering <" + itemProvider.getName( ) );
            _itemProviders.add( itemProvider );
        }
    }

    /**
     * Get all invoked admin user menu items
     * 
     * @param request
     *            the request
     * @return invoked admin user menu items
     */
    public List<AdminUserMenuItem> getItems( final HttpServletRequest request )
    {
        return _itemProviders.stream( ).filter( p -> p.isInvoked( request ) ).map( p -> p.getItem( request ) )
                .collect( Collectors.toList( ) );
    }
}
