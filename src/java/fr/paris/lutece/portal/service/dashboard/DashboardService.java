/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.service.dashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.List;


/**
 * Dashboard Service
 */
public final class DashboardService
{
    private static DashboardService _singleton = new DashboardService(  );
    private List<DashboardComponent> _listComponents = new ArrayList<DashboardComponent>(  );

    /**
     * Private Constructor
     */
    private DashboardService(  )
    {
    }

    /**
    * Return the unique instance
    * @return The instance
    */
    public static DashboardService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Register a Dashboard Component
     * @param entry The DashboardComponent entry defined in the plugin's XML file
     * @param plugin The plugin
     */
    public void registerDashboardComponent( DashboardComponentEntry entry, Plugin plugin )
    {
        try
        {
            DashboardComponent dc = (DashboardComponent) Class.forName( entry.getComponentClass(  ) ).newInstance(  );

            dc.setName( entry.getName(  ) );
            dc.setRight( entry.getRight(  ) );
            dc.setZone( entry.getZone(  ) );
            dc.setOrder( entry.getOrder(  ) );
            dc.setPlugin( plugin );
            _listComponents.add( dc );
            AppLogService.info( "New Dashboard Component registered : " + entry.getName(  ) );
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( "Error registering a DashboardComponent : " + e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "Error registering a DashboardComponent : " + e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( "Error registering a DashboardComponent : " + e.getMessage(  ), e );
        }
    }

    /**
     * Gets Data from all components of the zone
     * @param user The user
     * @param nZone The dasboard zone
     * @return Data of all components of the zone
     */
    public String getDashboardData( AdminUser user, int nZone )
    {
        StringBuffer sbDashboardData = new StringBuffer(  );

        for ( DashboardComponent dc : _listComponents )
        {
            if ( ( dc.getZone(  ) == nZone ) && dc.isEnabled(  ) && user.checkRight( dc.getRight(  ) ) )
            {
                sbDashboardData.append( dc.getDashboardData( user ) );
            }
        }

        return sbDashboardData.toString(  );
    }
}
