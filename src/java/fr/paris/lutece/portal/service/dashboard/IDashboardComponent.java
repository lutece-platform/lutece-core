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
package fr.paris.lutece.portal.service.dashboard;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * IDashboardComponent
 *
 */
public interface IDashboardComponent extends Comparable<IDashboardComponent>
{
    /**
     * Gets dashboard data for a given user
     * @param user The user
     * @param request HttpServletRequest
     * @return HTML content to insert into a dashboard zone
     */
    String getDashboardData( AdminUser user, HttpServletRequest request );

    /**
     * Returns the Name
     * @return The Name
     */
    String getName(  );

    /**
     * Sets the Name
     * @param strName The Name
     */
    void setName( String strName );

    /**
     * Returns the Right
     * @return The Right
     */
    String getRight(  );

    /**
     * Sets the Right
     * @param strRight The Right
     */
    void setRight( String strRight );

    /**
     * Returns the Zone
     * @return The Zone
     */
    int getZone(  );

    /**
     * Sets the Zone
     * @param nZone The Zone
     */
    void setZone( int nZone );

    /**
     * Returns the Order
     * @return The Order
     */
    int getOrder(  );

    /**
     * Sets the Order
     * @param nOrder The Order
     */
    void setOrder( int nOrder );

    /**
     * Returns the Plugin
     * @return The Plugin
     */
    Plugin getPlugin(  );

    /**
     * Sets the Plugin
     * @param plugin The plugin
     */
    void setPlugin( Plugin plugin );

    /**
     * Tells if the component is enabled
     * @return true if enabled
     */
    boolean isEnabled(  );
}
