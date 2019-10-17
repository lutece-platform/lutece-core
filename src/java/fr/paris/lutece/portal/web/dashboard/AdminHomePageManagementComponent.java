/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * AdminHomePageManagementComponent
 */
public class AdminHomePageManagementComponent extends AdminDashboardComponent
{
    // Templates
    private static final String TEMPLATE_MANAGE_DASHBOARDS = "/admin/dashboard/manage_dashboards.html";

    // MARKS
    private static final String MARK_MAP_DASHBOARDS = "map_dashboards";
    private static final String MARK_NOT_SET_DASHBOARDS = "not_set_dashboards";
    private static final String MARK_COLUMN_COUNT = "column_count";
    private static final String MARK_LIST_AVAILABLE_COLUMNS = "list_available_columns";
    private static final String MARK_MAP_AVAILABLE_ORDERS = "map_available_orders";
    private static final String MARK_MAP_COLUMN_ORDER_STATUS = "map_column_order_status";

    private DashboardService _service = DashboardService.getInstance( );

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        Map<String, List<IDashboardComponent>> mapDashboards = _service.getAllSetDashboards( user );

        for ( List<IDashboardComponent> listComponents : mapDashboards.values( ) )
        {
            listComponents = I18nService.localizeCollection( listComponents, user.getLocale( ) );
        }

        model.put( MARK_MAP_DASHBOARDS, mapDashboards );

        List<IDashboardComponent> listNotSetDashboards = _service.getNotSetDashboards( );
        model.put( MARK_NOT_SET_DASHBOARDS, listNotSetDashboards );

        model.put( MARK_COLUMN_COUNT, _service.getColumnCount( ) );
        model.put( MARK_MAP_AVAILABLE_ORDERS, _service.getMapAvailableOrders( ) );
        model.put( MARK_LIST_AVAILABLE_COLUMNS, _service.getListAvailableColumns( ) );
        model.put( MARK_MAP_COLUMN_ORDER_STATUS, _service.getOrderedColumnsStatus( ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_DASHBOARDS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DASHBOARDS, user.getLocale( ), model );

        return template.getHtml( );

    }

}
