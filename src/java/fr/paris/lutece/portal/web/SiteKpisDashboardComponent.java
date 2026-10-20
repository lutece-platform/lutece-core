/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.web;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.portal.business.page.SiteStatisticsHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The figures of the site on the admin home: its pages, its published portlets, its pages restricted to a role and those changed lately
 */
public class SiteKpisDashboardComponent extends DashboardComponent
{
    // Marks
    private static final String MARK_URL = "url";
    private static final String MARK_PAGES_COUNT = "pages_count";
    private static final String MARK_PORTLETS_COUNT = "portlets_count";
    private static final String MARK_PUBLISHED_PORTLETS_COUNT = "published_portlets_count";
    private static final String MARK_RESTRICTED_PAGES_COUNT = "restricted_pages_count";
    private static final String MARK_RECENT_PAGES_COUNT = "recent_pages_count";
    private static final String MARK_RECENT_DAYS = "recent_days";

    // Properties
    private static final String PROPERTY_RECENT_DAYS = "lutece.dashboard.site.recentDays";
    private static final int DEFAULT_RECENT_DAYS = 30;

    // Templates
    private static final String TEMPLATE_DASHBOARD = "/admin/site/site_kpis_dashboard.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Right right = RightHome.findByPrimaryKey( getRight( ) );
        int nRecentDays = AppPropertiesService.getPropertyInt( PROPERTY_RECENT_DAYS, DEFAULT_RECENT_DAYS );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_URL, ( right != null ) ? right.getUrl( ) : "" );
        model.put( MARK_PAGES_COUNT, SiteStatisticsHome.countPages( ) );
        model.put( MARK_PORTLETS_COUNT, SiteStatisticsHome.countPortlets( ) );
        model.put( MARK_PUBLISHED_PORTLETS_COUNT, SiteStatisticsHome.countPortletsWithStatus( Portlet.STATUS_PUBLISHED ) );
        model.put( MARK_RESTRICTED_PAGES_COUNT, SiteStatisticsHome.countPagesRestrictedToRole( ) );
        model.put( MARK_RECENT_PAGES_COUNT,
                SiteStatisticsHome.countPagesUpdatedSince( new Timestamp( System.currentTimeMillis( ) - Duration.ofDays( nRecentDays ).toMillis( ) ) ) );
        model.put( MARK_RECENT_DAYS, nRecentDays );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD, user.getLocale( ), model );

        return template.getHtml( );
    }
}
