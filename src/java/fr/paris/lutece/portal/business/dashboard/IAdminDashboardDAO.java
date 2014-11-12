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

import java.util.List;


/**
 *
 * IAdminDashboardDAO
 *
 */
public interface IAdminDashboardDAO
{
    /**
     * Insert a new record in the table.
     *
     * @param dashboardComponent instance of the dashboardComponent object to insert
     */
    void insert( IAdminDashboardComponent dashboardComponent );

    /**
     * Load the data of the IndexerAction  from the table
     *
     * @param strBeanName The identifier of the action
     * @return the instance of the  IndexerAction
     */
    IAdminDashboardComponent load( String strBeanName );

    /**
     * Delete a record from the table
     *
     * @param strBeanName The identifier of the dashboard
     */
    void delete( String strBeanName );

    /**
     * Delete all record from the table
     *
     */
    void deleteAll(  );

    /**
     * Update the dashboardComponent in the table
     *
     * @param dashboardComponent instance of the IAdminDashboardComponent object to update
     */
    void store( IAdminDashboardComponent dashboardComponent );

    /**
     * Finds all AdminDashboardComponent
     * @return all AdminDashboardComponent
     */
    List<IAdminDashboardComponent> selectAllDashboardComponents(  );

    /**
     * Finds all dashboard components matching filter
     * @param filter the filter
     * @return all dashboard components matching filter
     */
    List<IAdminDashboardComponent> selectDashboardComponents( AdminDashboardFilter filter );

    /**
     * Returns the max order value, for all columns
     * @return the max order
     */
    int selectMaxOrder(  );

    /**
     * Returns the max order value, for the given column
     * @param nColumn the column
     * @return the max order
     */
    int selectMaxOrder( int nColumn );

    /**
     * Returns the columns list
     * @return the columns list
     */
    List<Integer> selectColumns(  );
}
