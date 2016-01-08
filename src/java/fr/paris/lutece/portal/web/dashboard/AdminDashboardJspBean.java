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
package fr.paris.lutece.portal.web.dashboard;

import fr.paris.lutece.portal.business.dashboard.AdminDashboardFactory;
import fr.paris.lutece.portal.business.dashboard.AdminDashboardHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardService;
import fr.paris.lutece.portal.service.dashboard.admin.IAdminDashboardComponent;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Provides technical admin dashboard managements and display. Display is NOT managed as an admin feature (no right required).
 *
 */
public class AdminDashboardJspBean extends AdminFeaturesPageJspBean
{
    // Constants
    public static final String RIGHT_MANAGE_ADMINDASHBOARD = "CORE_ADMINDASHBOARD_MANAGEMENT";

    // Parameters
    private static final String PARAMETER_DASHBOARD_NAME = "dashboard_name";
    private static final String PARAMETER_DASHBOARD_COLUMN = "dashboard_column";
    private static final String PARAMETER_DASHBOARD_ORDER = "dashboard_order";
    private static final String PARAMETER_COLUMN = "column";

    // Messages
    private static final String MESSAGE_DASHBOARD_NOT_FOUND = "portal.admindashboard.message.dashboardNotFound";

    // Markers
    private static final String MARK_COLUMN_CONTENT_PREFIX = "column_content_";
    private static final String MARK_MAP_DASHBOARDS = "map_dashboards";
    private static final String MARK_NOT_SET_DASHBOARDS = "not_set_dashboards";
    private static final String MARK_COLUMN_COUNT = "column_count";
    private static final String MARK_LIST_AVAILABLE_COLUMNS = "list_available_columns";
    private static final String MARK_MAP_AVAILABLE_ORDERS = "map_available_orders";
    private static final String MARK_MAP_COLUMN_ORDER_STATUS = "map_column_order_status";

    // Templates
    private static final String TEMPLATE_VIEW_DASHBOARDS = "/admin/dashboard/admin/view_dashboards.html";
    private static final String TEMPLATE_MANAGE_DASHBOARDS = "/admin/dashboard/admin/manage_dashboards.html";

    // Jsp
    private static final String JSP_MANAGE_DASHBOARDS = "ManageAdminDashboards.jsp";
    private static final String EMPTY_STRING = "";
    private AdminDashboardService _service = AdminDashboardService.getInstance(  );

    /**
     * Displays admin dashboards
     * @param request the request
     * @return html code [NOT AS A FEATURE]
     */
    public String getAdminDashboards( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );
        Map<String, Object> model = new HashMap<String, Object>(  );

        // put each column data
        for ( int nColumn = 1; nColumn <= _service.getColumnCount(  ); nColumn++ )
        {
            String strColumnData = _service.getDashboardData( user, nColumn, request );

            model.put( MARK_COLUMN_CONTENT_PREFIX + nColumn, strColumnData );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_DASHBOARDS, user.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Manages dashboard
     * @param request the request
     * @return html code
     */
    public String getManageDashboards( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        Map<String, Object> model = new HashMap<String, Object>(  );

        Map<String, List<IAdminDashboardComponent>> mapAdminDashboards = _service.getAllSetDashboards(  );
        model.put( MARK_MAP_DASHBOARDS, mapAdminDashboards );

        List<IAdminDashboardComponent> listNotSetDashboards = _service.getNotSetDashboards(  );
        model.put( MARK_NOT_SET_DASHBOARDS, listNotSetDashboards );

        model.put( MARK_COLUMN_COUNT, _service.getColumnCount(  ) );
        model.put( MARK_MAP_AVAILABLE_ORDERS, getMapAvailableOrders(  ) );
        model.put( MARK_LIST_AVAILABLE_COLUMNS, getListAvailableColumns(  ) );
        model.put( MARK_MAP_COLUMN_ORDER_STATUS, _service.getOrderedColumnsStatus(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DASHBOARDS, user.getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Reorders columns
     * @param request the request
     * @return url
     */
    public String doReorderColumn( HttpServletRequest request )
    {
        String strColumnName = request.getParameter( PARAMETER_COLUMN );

        if ( StringUtils.isBlank( strColumnName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nColumn = 0;

        try
        {
            nColumn = Integer.parseInt( strColumnName );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( "AdminDashboardJspBean.doReorderColumn : " + nfe.getMessage(  ), nfe );

            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        _service.doReorderColumn( nColumn );

        return JSP_MANAGE_DASHBOARDS;
    }

    /**
     * Moves the dashboard
     * @param request the request
     * @return url
     */
    public String doMoveAdminDashboard( HttpServletRequest request )
    {
        String strDashboardName = request.getParameter( PARAMETER_DASHBOARD_NAME );

        if ( StringUtils.isBlank( strDashboardName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DASHBOARD_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        // retrieve dashboard from database. If not found, will use Spring.
        IAdminDashboardComponent dashboard = AdminDashboardHome.findByPrimaryKey( strDashboardName );
        int nOldOrder = 0;
        int nOldColumn = 0;
        boolean bCreate = false;

        if ( dashboard == null )
        {
            bCreate = true;

            if ( AppLogService.isDebugEnabled(  ) )
            {
                AppLogService.debug( "Dashboard " + strDashboardName +
                    " has no property set. Retrieving from SpringContext" );
            }

            dashboard = AdminDashboardFactory.getDashboardComponent( strDashboardName );

            if ( dashboard == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_DASHBOARD_NOT_FOUND, AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            nOldOrder = dashboard.getOrder(  );
            nOldColumn = dashboard.getZone(  );
        }

        // set order and column
        String strOrder = request.getParameter( PARAMETER_DASHBOARD_ORDER );
        String strColumn = request.getParameter( PARAMETER_DASHBOARD_COLUMN );

        int nOrder = StringUtil.getIntValue( strOrder, -1 );
        int nColumn = StringUtil.getIntValue( strColumn, -1 );

        dashboard.setOrder( nOrder );
        dashboard.setZone( nColumn );

        _service.doMoveDashboard( dashboard, nOldColumn, nOldOrder, bCreate );

        return JSP_MANAGE_DASHBOARDS;
    }

    /**
     * Returns list with available column
     * @return all available columns
     */
    private ReferenceList getListAvailableColumns(  )
    {
        ReferenceList refList = new ReferenceList(  );

        // add empty item
        refList.addItem( EMPTY_STRING, EMPTY_STRING );

        for ( int nColumnIndex = 1; nColumnIndex <= _service.getColumnCount(  ); nColumnIndex++ )
        {
            refList.addItem( nColumnIndex, Integer.toString( nColumnIndex ) );
        }

        return refList;
    }

    /**
     * Builds all refList order for all columns
     * @return the map with column id as key
     */
    private Map<String, ReferenceList> getMapAvailableOrders(  )
    {
        Map<String, ReferenceList> mapAvailableOrders = new HashMap<String, ReferenceList>(  );

        // get columns
        for ( Integer nColumn : AdminDashboardHome.findColumns(  ) )
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
    private ReferenceList getListAvailableOrders( int nColumn )
    {
        ReferenceList refList = new ReferenceList(  );

        // add empty item
        refList.addItem( EMPTY_STRING, EMPTY_STRING );

        int nMaxOrder = AdminDashboardHome.findMaxOrder( nColumn );

        for ( int nOrder = 1; nOrder <= nMaxOrder; nOrder++ )
        {
            refList.addItem( nOrder, Integer.toString( nOrder ) );
        }

        return refList;
    }
}
