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
package fr.paris.lutece.portal.web.theme;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.service.theme.ThemeResourceIdService;
import fr.paris.lutece.portal.service.theme.ThemeService;
import fr.paris.lutece.portal.service.theme.ThemeUtil;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portal.ThemesService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * ThemeJspBean
 */
public class ThemeJspBean extends MVCAdminJspBean
{
    // Right
    public static final String RIGHT_MANAGE_THEMES = "CORE_THEME_MANAGEMENT";

    // Templates files path
    private static final String TEMPLATE_MANAGE_THEMES = "admin/theme/manage_themes.html";

    /**
     * Returns the list of Themes
     *
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    public String getManageThemes( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        Collection<Theme> listThemes = ThemeService.getInstance(  ).getThemesList(  );
        
        model.put( ThemeUtil.MARK_THEMES_LIST, listThemes );
        model.put( ThemeUtil.MARK_THEME_DEFAULT, ThemeService.getInstance(  ).getGlobalTheme(  ) );
        model.put( ThemeUtil.MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( ThemeUtil.MARK_PERMISSION_MODIFY_GLOBAL_THEME,
                RBACService.isAuthorized( Theme.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    ThemeResourceIdService.PERMISSION_MODIFY_GLOBAL_THEME, getUser(  ) ) );
        
        setPageTitleProperty( ThemeUtil.PROPERTY_MANAGE_THEMES_PAGE_TITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_THEMES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify the global theme
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    public String doModifyGlobalTheme( HttpServletRequest request )
    {
        String strUrl = "";
        String strTheme = request.getParameter( ThemeUtil.PARAMETER_THEME );
        String strVersion = request.getParameter( ThemeUtil.PARAMETER_THEME_VERSION );

        if ( !strTheme.isBlank() )
        {
            ThemeService.getInstance(  ).setGlobalTheme( strTheme, strVersion );
            strUrl = getHomeUrl( request );
        }
        else
        {
            strUrl = AdminMessageService.getMessageUrl( request, ThemeUtil.MESSAGE_OBJECT_NOT_FOUND,
                    AdminMessage.TYPE_STOP );
        }

        return strUrl;
    }

}
