/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * Base class for admin user menu item providers.
 * 
 * Considers the item provider origin plugin state to decide if it is invoked
 * 
 * @since 6.2.0
 */
public abstract class AbstractAdminUserMenuItemProvider implements IAdminUserMenuItemProvider
{

    private String _strName;

    @Override
    public final void setName( String strName )
    {
        _strName = strName;
    }

    @Override
    public final String getName( )
    {
        return _strName;
    }

    @Override
    public final boolean isInvoked( HttpServletRequest request )
    {
        return SpringContextService.isBeanEnabled( getName( ) ) && isItemProviderInvoked( request );
    }

    /**
     * Should this item provider be included for the request.
     * 
     * This is only called if the plugin declaring this item provider is active.
     * 
     * @param request
     *            the request
     * @return <code>true</code> if this item provider should be included, <code>false</code> otherwise
     */
    protected abstract boolean isItemProviderInvoked( HttpServletRequest request );

}
