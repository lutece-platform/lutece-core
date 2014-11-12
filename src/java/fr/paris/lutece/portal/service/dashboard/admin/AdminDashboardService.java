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
package fr.paris.lutece.portal.service.dashboard.admin;

import fr.paris.lutece.portal.business.dashboard.AdminDashboardFactory;
import fr.paris.lutece.portal.business.dashboard.AdminDashboardFilter;
import fr.paris.lutece.portal.business.dashboard.AdminDashboardHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponentEntry;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * AdminDashboardService
 *
 */
public final class AdminDashboardService
{
    /**
     * admindashboard.columnCount
     */
    private static final String PROPERTY_COLUMN_COUNT = "admindashboard.columnCount";
    private static final String ALL = "ALL";
    private static final int CONSTANTE_FIRST_ORDER = 1;
    private static final int CONSTANTE_DEFAULT_COLUMN_COUNT = 2;
    private static AdminDashboardService _singleton = new AdminDashboardService(  );

    /**
     * Private Constructor
     */
    private AdminDashboardService(  )
    {
        // nothing
    }

    /**
     * Return the unique instance
     * @return The instance
     */
    public static AdminDashboardService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the column count, with {@link AdminDashboardService#PROPERTY_COLUMN_COUNT}. Default is {@link AdminDashboardService#CONSTANTE_DEFAULT_COLUMN_COUNT}
     * @return the column count
     */
    public int getColumnCount(  )
    {
        return AppPropertiesService.getPropertyInt( PROPERTY_COLUMN_COUNT, CONSTANTE_DEFAULT_COLUMN_COUNT );
    }

    /**
     * All known dashboards as declared in SpringContext
     * @return dashboards list
     */
    public List<IAdminDashboardComponent> getAllAdminDashboardComponents(  )
    {
        return AdminDashboardFactory.getAllAdminDashboardComponents(  );
    }

    /**
     *
     * @param nColumn the column id
     * @return all dashboards for this column
     */
    public List<IAdminDashboardComponent> getAdminDashboardComponents( int nColumn )
    {
        AdminDashboardFilter filter = new AdminDashboardFilter(  );
        filter.setFilterColumn( nColumn );

        List<IAdminDashboardComponent> dashboardComponents = AdminDashboardHome.findByFilter( filter );

        return dashboardComponents;
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
            IAdminDashboardComponent dc = (IAdminDashboardComponent) Class.forName( entry.getComponentClass(  ) )
                                                                          .newInstance(  );

            dc.setName( entry.getName(  ) );
            dc.setPlugin( plugin );

            boolean bRegistered = AdminDashboardFactory.registerDashboardComponent( dc );

            if ( bRegistered )
            {
                AppLogService.info( "New Admin Dashboard Component registered : " + entry.getName(  ) );
            }
            else
            {
                AppLogService.error( " Admin Dashboard Component not registered : " + entry.getName(  ) + " : " +
                    entry.getComponentClass(  ) );
            }
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( "Error registering an Admin DashboardComponent : " + e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "Error registering an Admin DashboardComponent : " + e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( "Error registering an Admin DashboardComponent : " + e.getMessage(  ), e );
        }
    }

    /**
     * Moves the dashboard.
     * @param dashboard to move, with new values
     * @param nOldColumn previous column id
     * @param nOldOrder previous order
     * @param bCreate <code>true</code> if this is a new dashboard, <code>false</code> otherwise.
     */
    public void doMoveDashboard( IAdminDashboardComponent dashboard, int nOldColumn, int nOldOrder, boolean bCreate )
    {
        int nColumn = dashboard.getZone(  );
        int nOrder = dashboard.getOrder(  );

        // find the dashboard already with this order and column
        AdminDashboardFilter filter = new AdminDashboardFilter(  );
        filter.setFilterColumn( nColumn );

        List<IAdminDashboardComponent> listColumnDashboards = AdminDashboardHome.findByFilter( filter );

        if ( ( listColumnDashboards != null ) && !listColumnDashboards.isEmpty(  ) )
        {
            if ( AppLogService.isDebugEnabled(  ) )
            {
                AppLogService.debug( "Reordering admin dashboard column " + dashboard.getZone(  ) );
            }

            // sort by order
            Collections.sort( listColumnDashboards );

            int nMaxOrder = listColumnDashboards.get( listColumnDashboards.size(  ) - 1 ).getOrder(  );

            if ( ( nOldColumn == 0 ) || ( nOldColumn != nColumn ) )
            {
                // was not in this column before, put to the end
                dashboard.setOrder( nMaxOrder + 1 );
            }
            else
            {
                if ( nOrder < nOldOrder )
                {
                    for ( IAdminDashboardComponent dc : listColumnDashboards )
                    {
                        int nCurrentOrder = dc.getOrder(  );

                        if ( !dc.equals( dashboard ) && ( nCurrentOrder >= nOrder ) && ( nCurrentOrder < nOldOrder ) )
                        {
                            dc.setOrder( nCurrentOrder + 1 );
                            AdminDashboardHome.update( dc );
                        }
                    }
                }
                else if ( nOrder > nOldOrder )
                {
                    for ( IAdminDashboardComponent dc : listColumnDashboards )
                    {
                        int nCurrentOrder = dc.getOrder(  );

                        if ( !dc.equals( dashboard ) && ( nCurrentOrder <= nOrder ) && ( nCurrentOrder > nOldOrder ) )
                        {
                            dc.setOrder( nCurrentOrder - 1 );
                            AdminDashboardHome.update( dc );
                        }
                    }
                }

                // dashboard are singletons, values are modified by getting it from database
                dashboard.setOrder( nOrder );
                dashboard.setZone( nColumn );
            }
        }
        else
        {
            dashboard.setOrder( 1 );
        }

        if ( bCreate )
        {
            // create dashboard
            AdminDashboardHome.create( dashboard );
        }
        else
        {
            // update dashboard
            AdminDashboardHome.update( dashboard );
        }
    }

    /**
     * Returns all dashboards with no column/order set
     * @return all dashboards with no column/order set
     */
    public List<IAdminDashboardComponent> getNotSetDashboards(  )
    {
        List<IAdminDashboardComponent> listDashboards = AdminDashboardHome.findAll(  );
        List<IAdminDashboardComponent> listSpringDashboards = getAllAdminDashboardComponents(  );

        List<IAdminDashboardComponent> listUnsetDashboards = new ArrayList<IAdminDashboardComponent>(  );

        for ( IAdminDashboardComponent dashboard : listSpringDashboards )
        {
            if ( !listDashboards.contains( dashboard ) )
            {
                listUnsetDashboards.add( dashboard );
            }
        }

        return listUnsetDashboards;
    }

    /**
     * Finds all dashboard with column and order set.
     * @return a map where key is the column id, and value is the column's dashboard list.
     */
    public Map<String, List<IAdminDashboardComponent>> getAllSetDashboards(  )
    {
        Map<String, List<IAdminDashboardComponent>> mapDashboardComponents = new HashMap<String, List<IAdminDashboardComponent>>(  );

        List<IAdminDashboardComponent> listDashboards = AdminDashboardHome.findAll(  );

        for ( IAdminDashboardComponent dashboard : listDashboards )
        {
            int nColumn = dashboard.getZone(  );

            String strColumn = Integer.toString( nColumn );

            // find this column list
            List<IAdminDashboardComponent> listDashboardsColumn = mapDashboardComponents.get( strColumn );

            if ( listDashboardsColumn == null )
            {
                // the list does not exist, create it
                listDashboardsColumn = new ArrayList<IAdminDashboardComponent>(  );
                mapDashboardComponents.put( strColumn, listDashboardsColumn );
            }

            // add dashboard to the list
            listDashboardsColumn.add( dashboard );
        }

        return mapDashboardComponents;
    }

    /**
     * Gets Data from all components of the zone
     * @param user The user
     * @param nColumn The dasboard column
     * @param request HttpServletRequest
     * @return Data of all components of the zone
     */
    public String getDashboardData( AdminUser user, int nColumn, HttpServletRequest request )
    {
        StringBuilder sbDashboardData = new StringBuilder(  );

        for ( IAdminDashboardComponent dc : getAdminDashboardComponents( nColumn ) )
        {
            boolean bRight = ( dc.getRight(  ) == null ) || user.checkRight( dc.getRight(  ) ) ||
                dc.getRight(  ).equalsIgnoreCase( ALL );

            if ( dc.isEnabled(  ) && bRight )
            {
                sbDashboardData.append( dc.getDashboardData( user, request ) );
            }
        }

        return sbDashboardData.toString(  );
    }

    /**
     * Reorders column's dashboard
     * @param nColumn the column to reorder
     */
    public void doReorderColumn( int nColumn )
    {
        int nOrder = CONSTANTE_FIRST_ORDER;

        for ( IAdminDashboardComponent dc : getAdminDashboardComponents( nColumn ) )
        {
            dc.setOrder( nOrder++ );
            AdminDashboardHome.update( dc );
        }
    }

    /**
     * Builds the map to with column id as key, and <code>true</code> as value if column is well ordered, <code>false</code> otherwise.
     * @return the map
     */
    public Map<String, Boolean> getOrderedColumnsStatus(  )
    {
        Map<String, Boolean> mapOrderedStatus = new HashMap<String, Boolean>(  );
        List<Integer> listColumns = AdminDashboardHome.findColumns(  );

        for ( Integer nIdColumn : listColumns )
        {
            mapOrderedStatus.put( nIdColumn.toString(  ), isWellOrdered( nIdColumn ) );
        }

        return mapOrderedStatus;
    }

    /**
     * Determines if the column is well ordered
     * @param nColumn the column id
     * @return true if well ordered, <code>false</code> otherwise.
     */
    private boolean isWellOrdered( int nColumn )
    {
        int nOrder = CONSTANTE_FIRST_ORDER;

        for ( IAdminDashboardComponent dc : getAdminDashboardComponents( nColumn ) )
        {
            if ( nOrder != dc.getOrder(  ) )
            {
                return false;
            }

            nOrder++;
        }

        return true;
    }
}
