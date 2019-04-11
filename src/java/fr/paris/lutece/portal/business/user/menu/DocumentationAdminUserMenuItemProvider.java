/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.portal.business.user.menu;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * Documentation admin user menu item provider
 */
public class DocumentationAdminUserMenuItemProvider extends AbstractAdminUserMenuItemProvider
{
    private static final String PROPERTY_DOCUMENTATION_SUMMARY_URL = "lutece.documentation.summary.url";
    private static final String TEMPLATE = "admin/user/menu/documentation.html";
    private static final String MARK_ADMIN_SUMMARY_DOCUMENTATION_URL = "admin_summary_documentation_url";

    @Override
    protected boolean isItemProviderInvoked( HttpServletRequest request )
    {
        return AppPropertiesService.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL ) != null;
    }

    @Override
    public AdminUserMenuItem getItem( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_ADMIN_SUMMARY_DOCUMENTATION_URL, AppPropertiesService.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE, user.getLocale( ), model );

        return new AdminUserMenuItem( template.getHtml( ) );
    }

}
