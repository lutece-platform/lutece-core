/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.web.dashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dashboard Service
 */
public class DashboardService
{
    private List<DashboardComponent> _listComponents = new ArrayList<DashboardComponent>();

    /**
     * Constructor
     */
    public DashboardService()
    {
    }

    public void setComponentsList( List<DashboardComponent> list )
    {
        _listComponents = list;
        Collections.sort( _listComponents );
    }

    /**
     * Register a component
     * @param dc The dashboard component to register
     */
    public void registerComponent( DashboardComponent dc )
    {
        _listComponents.add( dc );
        Collections.sort( _listComponents );
    }

    /**
     * Gets Data from all components of the zone
     * @param user The user
     * @param nZone The dasboard zone
     * @return Data of all components of the zone
     */
    public String getDashboardData( AdminUser user, int nZone )
    {
        StringBuffer sbDashboardData = new StringBuffer();
        for( DashboardComponent dc : _listComponents )
        {
            if( dc.getZone() == nZone && user.checkRight( dc.getRight() ))
            {
                sbDashboardData.append( dc.getDashboardData( user )  );
            }
        }
        return sbDashboardData.toString();
    }
}
