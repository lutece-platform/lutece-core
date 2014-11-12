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

import fr.paris.lutece.portal.business.dashboard.DashboardFactory;
import fr.paris.lutece.portal.business.dashboard.DashboardFilter;
import fr.paris.lutece.portal.business.dashboard.DashboardHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sort.AttributeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Dashboard Service
 */
public final class DashboardService
{
    // Properties
    private static final String PROPERTY_COLUMN_COUNT = "dashboard.columnCount";

    // Constants
    private static final String ALL = "ALL";
    private static final String EMPTY_STRING = "";
    private static final String ORDER = "order";
    private static final int CONSTANTE_FIRST_ORDER = 1;
    private static final int CONSTANTE_DEFAULT_COLUMN_COUNT = 3;
    private static DashboardService _singleton = new DashboardService(  );

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
         * Returns the column count, with {@link DashboardService#PROPERTY_COLUMN_COUNT}. Default is {@link DashboardService#CONSTANTE_DEFAULT_COLUMN_COUNT}
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
    public List<IDashboardComponent> getAllDashboardComponents(  )
    {
        return DashboardFactory.getAllDashboardComponents(  );
    }

    /**
     *
     * @param nColumn the column id
     * @return all dashboards for this column
     */
    public List<IDashboardComponent> getDashboardComponents( int nColumn )
    {
        DashboardFilter filter = new DashboardFilter(  );
        filter.setFilterColumn( nColumn );

        List<IDashboardComponent> dashboardComponents = DashboardHome.findByFilter( filter );

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
            DashboardComponent dc = (DashboardComponent) Class.forName( entry.getComponentClass(  ) ).newInstance(  );

            dc.setName( entry.getName(  ) );
            dc.setRight( entry.getRight(  ) );
            dc.setPlugin( plugin );

            boolean bRegistered = DashboardFactory.registerDashboardComponent( dc );

            if ( bRegistered )
            {
                AppLogService.info( "New Dashboard Component registered : " + entry.getName(  ) );
            }
            else
            {
                AppLogService.error( " Dashboard Component not registered : " + entry.getName(  ) + " : " +
                    entry.getComponentClass(  ) );
            }
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
         * Moves the dashboard.
         * @param dashboard to move, with new values
         * @param nOldColumn previous column id
         * @param nOldOrder previous order
         * @param bCreate <code>true</code> if this is a new dashboard, <code>false</code> otherwise.
         */
    public void doMoveDashboard( IDashboardComponent dashboard, int nOldColumn, int nOldOrder, boolean bCreate )
    {
        int nColumn = dashboard.getZone(  );
        int nOrder = dashboard.getOrder(  );

        // find the dashboard already with this order and column
        DashboardFilter filter = new DashboardFilter(  );
        filter.setFilterColumn( nColumn );

        List<IDashboardComponent> listColumnDashboards = DashboardHome.findByFilter( filter );

        if ( ( listColumnDashboards != null ) && !listColumnDashboards.isEmpty(  ) )
        {
            if ( AppLogService.isDebugEnabled(  ) )
            {
                AppLogService.debug( "Reordering  dashboard column " + dashboard.getZone(  ) );
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
                    for ( IDashboardComponent dc : listColumnDashboards )
                    {
                        if ( !dc.equals( dashboard ) )
                        {
                            int nCurrentOrder = dc.getOrder(  );

                            if ( ( nCurrentOrder >= nOrder ) && ( nCurrentOrder < nOldOrder ) )
                            {
                                dc.setOrder( nCurrentOrder + 1 );
                                DashboardHome.update( dc );
                            }
                        }
                    }
                }
                else if ( nOrder > nOldOrder )
                {
                    for ( IDashboardComponent dc : listColumnDashboards )
                    {
                        if ( !dc.equals( dashboard ) )
                        {
                            int nCurrentOrder = dc.getOrder(  );

                            if ( ( nCurrentOrder <= nOrder ) && ( nCurrentOrder > nOldOrder ) )
                            {
                                dc.setOrder( nCurrentOrder - 1 );
                                DashboardHome.update( dc );
                            }
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
            DashboardHome.create( dashboard );
        }
        else
        {
            // update dashboard
            DashboardHome.update( dashboard );
        }
    }

    /**
     * Returns all dashboards with no column/order set
     * @return all dashboards with no column/order set
     */
    public List<IDashboardComponent> getNotSetDashboards(  )
    {
        List<IDashboardComponent> listDashboards = DashboardHome.findAll(  );
        List<IDashboardComponent> listSpringDashboards = getAllDashboardComponents(  );

        List<IDashboardComponent> listUnsetDashboards = new ArrayList<IDashboardComponent>(  );

        for ( IDashboardComponent dashboard : listSpringDashboards )
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
     * @param user the current user
     * @return a map where key is the column id, and value is the column's dashboard list.
     */
    public Map<String, List<IDashboardComponent>> getAllSetDashboards( AdminUser user )
    {
        Map<String, List<IDashboardComponent>> mapDashboardComponents = new HashMap<String, List<IDashboardComponent>>(  );

        List<IDashboardComponent> listDashboards = DashboardHome.findAll(  );

        for ( IDashboardComponent dashboard : listDashboards )
        {
            int nColumn = dashboard.getZone(  );
            boolean bRight = user.checkRight( dashboard.getRight(  ) ) ||
                dashboard.getRight(  ).equalsIgnoreCase( ALL );

            if ( !bRight )
            {
                continue;
            }

            String strColumn = Integer.toString( nColumn );

            // find this column list
            List<IDashboardComponent> listDashboardsColumn = mapDashboardComponents.get( strColumn );

            if ( listDashboardsColumn == null )
            {
                // the list does not exist, create it
                listDashboardsColumn = new ArrayList<IDashboardComponent>(  );
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
     * @param nZone The dasboard zone
     * @param request HttpServletRequest
     * @return Data of all components of the zone
     */
    public String getDashboardData( AdminUser user, int nZone, HttpServletRequest request )
    {
        StringBuffer sbDashboardData = new StringBuffer(  );

        for ( IDashboardComponent dc : getDashboardComponents( nZone ) )
        {
            boolean bRight = user.checkRight( dc.getRight(  ) ) || dc.getRight(  ).equalsIgnoreCase( ALL );

            if ( ( dc.getZone(  ) == nZone ) && dc.isEnabled(  ) && bRight )
            {
                sbDashboardData.append( dc.getDashboardData( user, request ) );
            }
        }

        return sbDashboardData.toString(  );
    }

    /**
     * Get the list of dashboard from plugins
     * @param user the current user
     * @param request HttpServletRequest
     * @return the list of dashboards
     */
    public List<IDashboardComponent> getDashboards( AdminUser user, HttpServletRequest request )
    {
        List<IDashboardComponent> listDashboards = new ArrayList<IDashboardComponent>(  );

        // Attributes associated to the plugins
        for ( DashboardListenerService dashboardListenerService : SpringContextService.getBeansOfType( 
                DashboardListenerService.class ) )
        {
            dashboardListenerService.getDashboardComponents( listDashboards, user, request );
        }

        return listDashboards;
    }

    /**
     * Gets Data from all components of the zone
     * @param listDashboards the list of dashboards
     * @param user The user
     * @param nZone The dasboard zone
     * @param request HttpServletRequest
     * @return Data of all components of the zone
     */
    public String getDashboardData( List<IDashboardComponent> listDashboards, AdminUser user, int nZone,
        HttpServletRequest request )
    {
        List<IDashboardComponent> listDashboardComponents = new ArrayList<IDashboardComponent>(  );

        for ( IDashboardComponent dc : listDashboards )
        {
            if ( dc.getZone(  ) == nZone )
            {
                listDashboardComponents.add( dc );
            }
        }

        Collections.sort( listDashboardComponents, new AttributeComparator( ORDER, true ) );

        StringBuffer sbDashboardData = new StringBuffer(  );

        for ( IDashboardComponent dc : listDashboardComponents )
        {
            boolean bRight = user.checkRight( dc.getRight(  ) ) || dc.getRight(  ).equalsIgnoreCase( ALL );

            if ( ( dc.getZone(  ) == nZone ) && dc.isEnabled(  ) && bRight )
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

        for ( IDashboardComponent dc : getDashboardComponents( nColumn ) )
        {
            dc.setOrder( nOrder++ );
            DashboardHome.update( dc );
        }
    }

    /**
     * Builds the map to with column id as key, and <code>true</code> as value if column is well ordered, <code>false</code> otherwise.
     * @return the map
     */
    public Map<String, Boolean> getOrderedColumnsStatus(  )
    {
        Map<String, Boolean> mapOrderedStatus = new HashMap<String, Boolean>(  );
        List<Integer> listColumns = DashboardHome.findColumns(  );

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

        for ( IDashboardComponent dc : getDashboardComponents( nColumn ) )
        {
            if ( nOrder != dc.getOrder(  ) )
            {
                return false;
            }

            nOrder++;
        }

        return true;
    }

    /**
     * Returns list with available column
     * @return all available columns
     */
    public ReferenceList getListAvailableColumns(  )
    {
        ReferenceList refList = new ReferenceList(  );

        // add empty item
        refList.addItem( EMPTY_STRING, EMPTY_STRING );

        for ( int nColumnIndex = 1; nColumnIndex <= getColumnCount(  ); nColumnIndex++ )
        {
            refList.addItem( nColumnIndex, Integer.toString( nColumnIndex ) );
        }

        return refList;
    }

    /**
     * Builds all refList order for all columns
     * @return the map with column id as key
     */
    public Map<String, ReferenceList> getMapAvailableOrders(  )
    {
        Map<String, ReferenceList> mapAvailableOrders = new HashMap<String, ReferenceList>(  );

        // get columns
        for ( Integer nColumn : DashboardHome.findColumns(  ) )
        {
            // get orders
            mapAvailableOrders.put( nColumn.toString(  ), getListAvailableOrders( nColumn ) );
        }

        return mapAvailableOrders;
    }

    /**
     * Orders reference list for the given column
     * @param nColumn column
     * @return the refList
     */
    public ReferenceList getListAvailableOrders( int nColumn )
    {
        ReferenceList refList = new ReferenceList(  );

        // add empty item
        refList.addItem( EMPTY_STRING, EMPTY_STRING );

        int nMaxOrder = DashboardHome.findMaxOrder( nColumn );

        for ( int nOrder = 1; nOrder <= nMaxOrder; nOrder++ )
        {
            refList.addItem( nOrder, Integer.toString( nOrder ) );
        }

        return refList;
    }
}
