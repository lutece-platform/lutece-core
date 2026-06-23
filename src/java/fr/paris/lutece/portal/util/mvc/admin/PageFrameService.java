/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.util.mvc.admin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminThemeService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminMenuJspBean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Composes the full admin HTML page (chrome plus view content) for the admin MVC front-controller.
 * <p>
 * This service is the programmatic replacement for the {@code AdminHeader.jsp} / {@code AdminFooter.jsp}
 * includes. It reuses the very same fragments rendered today by {@link AdminMenuJspBean}
 * (stylesheets, menu header, menu footer) so the produced markup is equivalent to the legacy JSP chrome.
 * </p>
 */
@ApplicationScoped
public class PageFrameService
{
    private static final String TEMPLATE_PAGE_FRAME = "admin/admin_page_layout.html";
    private static final String MARK_LANG = "lang";
    private static final String MARK_BASE_URL = "base_url";
    private static final String MARK_SITE_NAME = "site_name";
    private static final String MARK_ACCESSIBLE = "accessible_mode";
    private static final String MARK_STYLESHEETS = "admin_stylesheets";
    private static final String MARK_MENU_HEADER = "admin_menu_header";
    private static final String MARK_MENU_FOOTER = "admin_menu_footer";
    private static final String MARK_CONTENT = "content";

    @Inject
    private AdminMenuJspBean _adminMenuJspBean;

    /**
     * Wraps the view content produced by a controller into the complete admin page.
     *
     * @param request the current HTTP request, used to resolve the admin user, base URL and theme
     * @param strContent the HTML body content returned by the controller view
     * @return the full HTML page ready to be written to the response
     */
    public String wrap( HttpServletRequest request, String strContent )
    {
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = user != null ? user.getLocale( ) : Locale.getDefault( );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LANG, locale.getLanguage( ) );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_SITE_NAME, PortalService.getSiteName( ) );
        model.put( MARK_ACCESSIBLE, AdminThemeService.isModeAccessible( request ) );
        model.put( MARK_STYLESHEETS, _adminMenuJspBean.getAdminStyleSheets( ) );
        model.put( MARK_MENU_HEADER, _adminMenuJspBean.getAdminMenuHeader( request ) );
        model.put( MARK_MENU_FOOTER, _adminMenuJspBean.getAdminMenuFooter( request ) );
        model.put( MARK_CONTENT, strContent );

        return AppTemplateService.getTemplate( TEMPLATE_PAGE_FRAME, locale, model ).getHtml( );
    }
}
