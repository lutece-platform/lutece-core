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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * AdminDashboardHome
 *
 */
public final class AdminDashboardHome
{
    // Static variable pointed at the DAO instance
    private static IAdminDashboardDAO _dao = (IAdminDashboardDAO) SpringContextService.getBean( "adminDashboardDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AdminDashboardHome(  )
    {
    }

    /**
     * Creation of an instance of adminDashboardComponent
     *
     * @param adminDashboardComponent The instance of the adminDashboardComponent which contains the informations to store
     *
     */
    public static void create( IAdminDashboardComponent adminDashboardComponent )
    {
        _dao.insert( adminDashboardComponent );
    }

    /**
     * Update of the adminDashboardComponent which is specified in parameter
     *
     * @param adminDashboardComponent The instance of the adminDashboardComponent which contains the informations to update
     *
     */
    public static void update( IAdminDashboardComponent adminDashboardComponent )
    {
        _dao.store( adminDashboardComponent );
    }

    /**
     * Remove the adminDashboardComponent whose identifier is specified in parameter
     *
     * @param strBeanName The adminDashboardComponent id
     */
    public static void remove( String strBeanName )
    {
        _dao.delete( strBeanName );
    }

    /**
     * Remove the adminDashboardComponent whose identifier is specified in parameter
     *
     */
    public static void removeAll(  )
    {
        _dao.deleteAll(  );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a adminDashboardComponent whose identifier is specified in parameter
     *
     * @param strBeanName The adminDashboardComponent primary key
     * @return an instance of adminDashboardComponent
     */
    public static IAdminDashboardComponent findByPrimaryKey( String strBeanName )
    {
        return _dao.load( strBeanName );
    }

    /**
     * Loads the data of all the IAdminDashboardComponent
     *
     * @return the list which contains the data of all the IAdminDashboardComponent
     */
    public static List<IAdminDashboardComponent> findAll(  )
    {
        return _dao.selectAllDashboardComponents(  );
    }

    /**
     * Loads the data of all the IAdminDashboardComponent
     * @param filter a search by criteria
     * @return the list which contains the data of all the IAdminDashboardComponent
     */
    public static List<IAdminDashboardComponent> findByFilter( AdminDashboardFilter filter )
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
     * Find columns.
     *
     * @return the list
     */
    public static List<Integer> findColumns(  )
    {
        return _dao.selectColumns(  );
    }
}
