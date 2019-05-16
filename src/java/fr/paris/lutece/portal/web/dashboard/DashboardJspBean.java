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

import fr.paris.lutece.portal.business.dashboard.DashboardFactory;
import fr.paris.lutece.portal.business.dashboard.DashboardHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;


import javax.servlet.http.HttpServletRequest;

/**
 * Provides technical admin dashboard managements and display. Display is NOT managed as an admin feature (no right required).
 *
 */
public class DashboardJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_MANAGE_DASHBOARD = "CORE_DASHBOARD_MANAGEMENT";

    // Parameters
    private static final String PARAMETER_DASHBOARD_NAME = "dashboard_name";
    private static final String PARAMETER_DASHBOARD_COLUMN = "dashboard_column";
    private static final String PARAMETER_DASHBOARD_ORDER = "dashboard_order";
    private static final String PARAMETER_COLUMN = "column";

    // Messages
    private static final String MESSAGE_DASHBOARD_NOT_FOUND = "portal.dashboard.message.dashboardNotFound";


    // Templates
    private static final String TEMPLATE_MANAGE_DASHBOARDS = "/admin/dashboard/manage_dashboards.html";

    // JSP
    private static final String JSP_MANAGE_DASHBOARDS = JSP_TECHNICAL_ADMINISTRATION + "?#adminHomePageManagement";
    private DashboardService _service = DashboardService.getInstance( );


    /**
     * Reorders columns
     * 
     * @param request
     *            the request
     * @return url
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doReorderColumn( HttpServletRequest request ) throws AccessDeniedException
    {
        String strColumnName = request.getParameter( PARAMETER_COLUMN );

        if ( StringUtils.isBlank( strColumnName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nColumn;

        try
        {
            nColumn = Integer.parseInt( strColumnName );
        }
        catch( NumberFormatException nfe )
        {
            AppLogService.error( "DashboardJspBean.doReorderColumn : " + nfe.getMessage( ), nfe );

            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MANAGE_DASHBOARDS ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        _service.doReorderColumn( nColumn );

        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_DASHBOARDS;
    }

    /**
     * Moves the dashboard
     * 
     * @param request
     *            the request
     * @return url
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doMoveDashboard( HttpServletRequest request ) throws AccessDeniedException
    {
        String strDashboardName = request.getParameter( PARAMETER_DASHBOARD_NAME );

        if ( StringUtils.isBlank( strDashboardName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DASHBOARD_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        // retrieve dashboard from database. If not found, will use Spring.
        IDashboardComponent dashboard = DashboardHome.findByPrimaryKey( strDashboardName );
        int nOldOrder = 0;
        int nOldColumn = 0;
        boolean bCreate = false;

        if ( dashboard == null )
        {
            bCreate = true;

            if ( AppLogService.isDebugEnabled( ) )
            {
                AppLogService.debug( "Dashboard " + strDashboardName + " has no property set. Retrieving from SpringContext" );
            }

            dashboard = DashboardFactory.getDashboardComponent( strDashboardName );

            if ( dashboard == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_DASHBOARD_NOT_FOUND, AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            nOldOrder = dashboard.getOrder( );
            nOldColumn = dashboard.getZone( );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MANAGE_DASHBOARDS ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        // set order and column
        String strOrder = request.getParameter( PARAMETER_DASHBOARD_ORDER );
        String strColumn = request.getParameter( PARAMETER_DASHBOARD_COLUMN );

        int nOrder = StringUtil.getIntValue( strOrder, -1 );
        int nColumn = StringUtil.getIntValue( strColumn, -1 );

        dashboard.setOrder( nOrder );
        dashboard.setZone( nColumn );

        _service.doMoveDashboard( dashboard, nOldColumn, nOldOrder, bCreate );

        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_DASHBOARDS;
    }
}
