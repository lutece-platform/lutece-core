/*
 * Copyright (c) 2002-2025, City of Paris
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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * DashboardDAO
 *
 */
public class DashboardDAO implements IDashboardDAO
{
    private static final String LOG_ERROR_NOT_FOUND = "Dashboard named {} not found";
    private static final String SQL_QUERY_MAX_ORDER = "SELECT max(dashboard_order) FROM core_dashboard";
    private static final String SQL_QUERY_MAX_ORDER_COLUMN = SQL_QUERY_MAX_ORDER + " WHERE dashboard_column = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_dashboard ";
    private static final String SQL_QUERY_DELETE_BY_NAME = SQL_QUERY_DELETE + " WHERE dashboard_name = ? ";
    private static final String SQL_QUERY_SELECT = "SELECT dashboard_name, dashboard_order, dashboard_column  FROM core_dashboard ";
    private static final String SQL_QUERY_ORDER_BY_COLUMN_AND_ORDER = " ORDER BY dashboard_column, dashboard_order";
    private static final String SQL_QUERY_SELECT_ALL = SQL_QUERY_SELECT + " WHERE dashboard_column != - 1 " + SQL_QUERY_ORDER_BY_COLUMN_AND_ORDER;
    private static final String SQL_QUERY_SELECT_COLUMNS = "SELECT dashboard_column FROM core_dashboard GROUP BY dashboard_column";
    private static final String SQL_QUERY_FILTER_COLUMN = " dashboard_column = ? ";
    private static final String SQL_QUERY_FILTER_ORDER = " dashboard_order = ? ";
    private static final String SQL_QUERY_FILTER_NAME = " dashboard_name = ? ";
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = SQL_QUERY_SELECT + " WHERE " + SQL_QUERY_FILTER_NAME;
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_dashboard( dashboard_name, dashboard_order, dashboard_column ) " + " VALUES(?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_dashboard " + " SET dashboard_order = ?, dashboard_column = ? WHERE dashboard_name = ?";
    private static final String SQL_QUERY_KEYWORD_WHERE = "  WHERE ";
    private static final String SQL_QUERY_KEYWORD_AND = " AND ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strBeanName )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_NAME ) )
        {

            daoUtil.setString( 1, strBeanName );

            daoUtil.executeUpdate( );

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {

            daoUtil.executeUpdate( );

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( IDashboardComponent dashboardComponent )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {

            daoUtil.setString( 1, dashboardComponent.getName( ) );
            setInsertOrUpdateValues( 2, dashboardComponent, daoUtil );

            daoUtil.executeUpdate( );

        }
    }

    /**
     * Loads the dashboard from the factory
     * 
     * @param strBeanName
     *            the dashboard name
     * @return the dashboard found
     * @see DashboardFactory#getDashboardComponent(String)
     */
    private IDashboardComponent findDashboardFromFactory( String strBeanName )
    {
        return DashboardFactory.getDashboardComponent( strBeanName );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDashboardComponent load( String strClassName )
    {
        IDashboardComponent dashboardComponent = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY ) )
        {

            daoUtil.setString( 1, strClassName );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                String strBeanName = daoUtil.getString( 1 );

                dashboardComponent = findDashboardFromFactory( strBeanName );

                if ( dashboardComponent != null )
                {
                    load( dashboardComponent, daoUtil );
                }
                else
                {
                    AppLogService.error( LOG_ERROR_NOT_FOUND, strBeanName );
                }
            }

        }

        return dashboardComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDashboardComponent> selectAllDashboardComponents( )
    {
        List<IDashboardComponent> listDashboards = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL ) )
        {

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                IDashboardComponent dashboardComponent = null;

                String strBeanName = daoUtil.getString( 1 );

                dashboardComponent = findDashboardFromFactory( strBeanName );

                if ( dashboardComponent != null )
                {
                    load( dashboardComponent, daoUtil );
                    listDashboards.add( dashboardComponent );
                }
                else
                {
                    AppLogService.error( LOG_ERROR_NOT_FOUND, strBeanName );
                }
            }

        }

        return listDashboards;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectMaxOrder( )
    {
        int nMaxOrder = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nMaxOrder = daoUtil.getInt( 1 );
            }

        }

        return nMaxOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectMaxOrder( int nColumn )
    {
        int nMaxOrder = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER_COLUMN ) )
        {
            daoUtil.setInt( 1, nColumn );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nMaxOrder = daoUtil.getInt( 1 );
            }

        }

        return nMaxOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IDashboardComponent> selectDashboardComponents( DashboardFilter filter )
    {
        List<IDashboardComponent> listDashboards = new ArrayList<>( );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT );
        buildSQLFilter( sbSQL, filter );
        sbSQL.append( SQL_QUERY_ORDER_BY_COLUMN_AND_ORDER );

        try ( DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ) ) )
        {

            applySQLFilter( daoUtil, 1, filter );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                IDashboardComponent dashboardComponent = null;

                String strBeanName = daoUtil.getString( 1 );

                dashboardComponent = findDashboardFromFactory( strBeanName );

                if ( dashboardComponent != null )
                {
                    load( dashboardComponent, daoUtil );
                    listDashboards.add( dashboardComponent );
                }
                else
                {
                    AppLogService.error( LOG_ERROR_NOT_FOUND, strBeanName );
                }
            }

        }

        return listDashboards;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( IDashboardComponent dashboardComponent )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {

            int nIndex = setInsertOrUpdateValues( 1, dashboardComponent, daoUtil );
            daoUtil.setString( nIndex, dashboardComponent.getName( ) );

            daoUtil.executeUpdate( );

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> selectColumns( )
    {
        List<Integer> listColumns = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COLUMNS ) )
        {

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listColumns.add( daoUtil.getInt( 1 ) );
            }

        }

        return listColumns;
    }

    /**
     * Loads compenent data from daoUtil
     * 
     * @param component
     *            the component
     * @param daoUtil
     *            the daoutil
     */
    private void load( IDashboardComponent component, DAOUtil daoUtil )
    {
        int nIndex = 1;
        component.setName( daoUtil.getString( nIndex++ ) );
        component.setOrder( daoUtil.getInt( nIndex++ ) );
        component.setZone( daoUtil.getInt( nIndex++ ) );
    }

    /**
     * Sets daoUtil values from componnet
     * 
     * @param nStartIndex
     *            the start index
     * @param component
     *            the component
     * @param daoUtil
     *            daoutil
     * @return the end index
     */
    private int setInsertOrUpdateValues( int nStartIndex, IDashboardComponent component, DAOUtil daoUtil )
    {
        int nIndex = nStartIndex;
        daoUtil.setInt( nIndex++, component.getOrder( ) );
        daoUtil.setInt( nIndex++, component.getZone( ) );

        return nIndex;
    }

    /**
     * Builds sql filter
     * 
     * @param sbSQL
     *            the buffer
     * @param filter
     *            the filter
     */
    private void buildSQLFilter( StringBuilder sbSQL, DashboardFilter filter )
    {
        List<String> listFilters = new ArrayList<>( );

        if ( filter.containsFilterOrder( ) )
        {
            listFilters.add( SQL_QUERY_FILTER_ORDER );
        }

        if ( filter.containsFilterColumn( ) )
        {
            listFilters.add( SQL_QUERY_FILTER_COLUMN );
        }

        if ( !listFilters.isEmpty( ) )
        {
            sbSQL.append( SQL_QUERY_KEYWORD_WHERE );

            boolean bFirstFilter = true;

            for ( String strFilter : listFilters )
            {
                sbSQL.append( strFilter );

                if ( !bFirstFilter )
                {
                    sbSQL.append( SQL_QUERY_KEYWORD_AND );
                }
                else
                {
                    bFirstFilter = false;
                }
            }
        }
    }

    /**
     * Add daoUtil parameters
     * 
     * @param daoUtil
     *            daoUtil
     * @param nStartIndex
     *            start index
     * @param filter
     *            the filter to apply
     * @return end index
     */
    private int applySQLFilter( DAOUtil daoUtil, int nStartIndex, DashboardFilter filter )
    {
        int nIndex = nStartIndex;

        if ( filter.containsFilterOrder( ) )
        {
            daoUtil.setInt( nIndex++, filter.getFilterOrder( ) );
        }

        if ( filter.containsFilterColumn( ) )
        {
            daoUtil.setInt( nIndex++, filter.getFilterColumn( ) );
        }

        return nIndex;
    }
}
