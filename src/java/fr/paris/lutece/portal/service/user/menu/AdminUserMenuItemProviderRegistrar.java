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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.beans.factory.BeanNameAware;

import fr.paris.lutece.portal.business.user.menu.IAdminUserMenuItemProvider;

/**
 * Admin user menu provider registrar.
 * 
 * Used to add item providers to the admin user menu provider registry. One of <code>className</code> or <code>provider</code> must be set. Ordering can be
 * altered by using <code>insertAfter</code> or <code>insertBefore</code> properties.
 * 
 * Normally used from Spring.
 * 
 * @since 6.2.0
 */
public class AdminUserMenuItemProviderRegistrar implements BeanNameAware
{
    private final AdminUserMenuService _service;
    private IAdminUserMenuItemProvider _provider;
    private String _strAfterName;
    private String _strBeforeName;
    private String _strName;

    /**
     * Constructor
     * 
     * @param service
     *            the admin user item service
     */
    @Inject
    public AdminUserMenuItemProviderRegistrar( AdminUserMenuService service )
    {
        _service = service;
    }

    /**
     * Class name of the admin user menu item provider
     * 
     * @param strClassName
     *            class name
     * @throws InstantiationException
     *             if the class cannot be instantiated
     * @throws IllegalAccessException
     *             if the class cannot be instantiated
     * @throws IllegalStateException
     *             if the item provider is already set
     * @throws ClassNotFoundException
     *             if the class cannot be found
     */
    public void setClassName( String strClassName ) throws InstantiationException, IllegalAccessException, IllegalStateException, ClassNotFoundException
    {
        assertItemProviderNotSet( );
        _provider = (IAdminUserMenuItemProvider) Class.forName( strClassName ).newInstance( );
    }

    /**
     * Provider
     * 
     * @param provider
     *            the provider
     * @throws IllegalStateException
     *             if the item provider is already set
     */
    public void setProvider( IAdminUserMenuItemProvider provider ) throws IllegalStateException
    {
        assertItemProviderNotSet( );
        _provider = provider;
    }

    /**
     * Name of the item provider after which to insert this one
     * 
     * @param strAfterName
     *            item provider name
     */
    public void setInsertAfter( String strAfterName )
    {
        _strAfterName = strAfterName;
    }

    /**
     * Name of the item provider before which to insert this one
     * 
     * @param strBeforeName
     *            item provider name
     */
    public void setInsertBefore( String strBeforeName )
    {
        _strBeforeName = strBeforeName;
    }

    /**
     * Assert the item provider is not already set
     */
    private void assertItemProviderNotSet( )
    {
        if ( _provider != null )
        {
            throw new IllegalStateException( "Only one of className or provider must be specifed" );
        }
    }

    /**
     * Registers the item provider
     */
    @PostConstruct
    public void registerAdminUserMenuItemProvider( )
    {
        if ( _provider == null )
        {
            throw new IllegalStateException( "Either className or provider must be specifed" );
        }
        _provider.setName( _strName );
        _service.addItemProvider( _provider, _strAfterName, _strBeforeName );
    }

    /**
     * Set the name under which to register the admin user menu item provider
     * 
     * @param strName
     *            name of the admin user menu item provider
     */
    @Override
    public void setBeanName( String strName )
    {
        _strName = strName;
    }
}
