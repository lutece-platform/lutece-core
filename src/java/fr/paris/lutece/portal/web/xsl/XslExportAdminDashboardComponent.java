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


package fr.paris.lutece.portal.web.xsl;

import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.xsl.XslExportResourceIdService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * XslExportAdminDashboardComponent
 */
public class XslExportAdminDashboardComponent  extends AdminDashboardComponent
{
    private static final String TEMPLATE_MANAGE_XSL_EXPORT = "admin/xsl/xsl_export_admin_dashboard.html";
    private static final String MARK_XSL_EXPORT_LIST = "xsl_export_list";
    private static final String MARK_PERMISSION_CREATE = "right_create";
    private static final String MARK_PERMISSION_MODIFY = "right_modify";
    private static final String MARK_PERMISSION_DELETE = "right_delete";

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<>( );
        List<XslExport> listXslExport = XslExportHome.getList( );

        model.put( MARK_PERMISSION_CREATE,
                RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, XslExportResourceIdService.PERMISSION_CREATE, user ) );
        model.put( MARK_PERMISSION_MODIFY,
                RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, XslExportResourceIdService.PERMISSION_MODIFY, user ) );
        model.put( MARK_PERMISSION_DELETE,
                RBACService.isAuthorized( XslExport.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, XslExportResourceIdService.PERMISSION_DELETE, user ) );


        model.put( MARK_XSL_EXPORT_LIST, listXslExport );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_XSL_EXPORT, user.getLocale( ), model );

        return templateList.getHtml( );
    }


}
