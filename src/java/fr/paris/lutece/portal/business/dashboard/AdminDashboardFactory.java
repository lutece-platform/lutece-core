/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.business.dashboard;

import fr.paris.lutece.portal.service.dashboard.admin.IAdminDashboardComponent;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Factory for {@link IAdminDashboardComponent}
 *
 */
public final class AdminDashboardFactory
{
    private static Map<String, IAdminDashboardComponent> _mapAdminDashboardComponents = new HashMap<String, IAdminDashboardComponent>(  );

    /**
     * Empty constructor
     */
    private AdminDashboardFactory(  )
    {
        // nothing
    }

    /**
     * Registers the new component. Name must be unique
     * @param adminDashboardComponent the component to register
     * @return <code>true</code> if registered, <code>false</code> otherwise.
     */
    public static boolean registerDashboardComponent( IAdminDashboardComponent adminDashboardComponent )
    {
        String strName = adminDashboardComponent.getName(  );

        if ( _mapAdminDashboardComponents.containsKey( strName ) )
        {
            AppLogService.error( "Error while registering admin dashboard for " + strName + " class=" +
                adminDashboardComponent.getClass(  ) + ", factory already contains class=" +
                _mapAdminDashboardComponents.get( strName ) );

            return false;
        }

        _mapAdminDashboardComponents.put( strName, adminDashboardComponent );

        return true;
    }

    /**
     * Gets the {@link IAdminDashboardComponent} for the given name
     * @param strName the name
     * @return The component found, <code>null</code> otherwise.
     */
    public static IAdminDashboardComponent getDashboardComponent( String strName )
    {
        return _mapAdminDashboardComponents.get( strName );
    }

    /**
     * Finds all registered admin dashboards
     * @return the list
     */
    public static List<IAdminDashboardComponent> getAllAdminDashboardComponents(  )
    {
        return new ArrayList<IAdminDashboardComponent>( _mapAdminDashboardComponents.values(  ) );
    }
}
