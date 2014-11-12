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

import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * DashboardHome
 *
 */
public final class DashboardHome
{
    // Static variable pointed at the DAO instance
    private static IDashboardDAO _dao = (IDashboardDAO) SpringContextService.getBean( "dashboardDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DashboardHome(  )
    {
    }

    /**
     * Creation of an instance of DashboardComponent
     *
     * @param dashboardComponent The instance of the DashboardComponent which contains the informations to store
     *
     */
    public static void create( IDashboardComponent dashboardComponent )
    {
        _dao.insert( dashboardComponent );
    }

    /**
     * Update of the DashboardComponent which is specified in parameter
     *
     * @param dashboardComponent The instance of the DashboardComponent which contains the informations to update
     *
     */
    public static void update( IDashboardComponent dashboardComponent )
    {
        _dao.store( dashboardComponent );
    }

    /**
     * Remove the DashboardComponent whose identifier is specified in parameter
     *
     * @param strBeanName The DashboardComponent id
     */
    public static void remove( String strBeanName )
    {
        _dao.delete( strBeanName );
    }

    /**
     * Remove the DashboardComponent whose identifier is specified in parameter
     *
     */
    public static void removeAll(  )
    {
        _dao.deleteAll(  );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a DashboardComponent whose identifier is specified in parameter
     *
     * @param strBeanName The DashboardComponent primary key
     * @return an instance of DashboardComponent
     */
    public static IDashboardComponent findByPrimaryKey( String strBeanName )
    {
        return _dao.load( strBeanName );
    }

    /**
     * Loads the data of all the IDashboardComponent
     *
     * @return the list which contains the data of all the IDashboardComponent
     */
    public static List<IDashboardComponent> findAll(  )
    {
        return _dao.selectAllDashboardComponents(  );
    }

    /**
     * Loads the data of all the IDashboardComponent
     * @param filter the filter
     * @return the list which contains the data of all the IDashboardComponent
     */
    public static List<IDashboardComponent> findByFilter( DashboardFilter filter )
    {
        return _dao.selectDashboardComponents( filter );
    }

    /**
     * Finds the max order for all columns.
     * @return the max order
     */
    public static int findMaxOrder(  )
    {
        return _dao.selectMaxOrder(  );
    }

    /**
     * Finds the max order for the column.
     * @param nColumn the column
     * @return the max order
     */
    public static int findMaxOrder( int nColumn )
    {
        return _dao.selectMaxOrder( nColumn );
    }

    /**
     * Finds all columns
     * @return the list of columns
     */
    public static List<Integer> findColumns(  )
    {
        return _dao.selectColumns(  );
    }
}
