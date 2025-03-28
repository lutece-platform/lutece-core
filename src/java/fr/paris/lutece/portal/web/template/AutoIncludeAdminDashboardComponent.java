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
package fr.paris.lutece.portal.web.template;

import fr.paris.lutece.portal.business.template.AutoInclude;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.portal.service.template.FreeMarkerTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * AutoIncludeAdminDashboardComponent
 */
public class AutoIncludeAdminDashboardComponent extends AdminDashboardComponent
{
    // Templates
    private static final String TEMPLATE_MANAGE_AUTOINCLUDES = "/admin/dashboard/admin/autoincludes_dashboard.html";
    // Markers
    private static final String MARK_AUTOINCLUDE_LIST = "autoinclude_list";
    private static final String MARK_AUTOIMPORT_MAP = "autoimport_map";
    private static final String MARK_COMMONS_LIST = "commons_list";
    private static final String MARK_CURRENT_COMMONS = "current_commons";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        FreeMarkerTemplateService fmtService = FreeMarkerTemplateService.getInstance( );
        model.put( MARK_AUTOINCLUDE_LIST, fmtService.getAutoIncludesList( ) );
        model.put( MARK_AUTOIMPORT_MAP, fmtService.getAutoImportsMap( ) );
        model.put( MARK_COMMONS_LIST, CommonsService.getCommonsList( ) );
        model.put( MARK_CURRENT_COMMONS, CommonsService.getCurrentCommonsKey( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_AUTOINCLUDES, user.getLocale( ), model );
        return template.getHtml( );
    }

}
