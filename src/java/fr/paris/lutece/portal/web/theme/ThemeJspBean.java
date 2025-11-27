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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.service.theme.ThemeService;
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
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.CDI;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import fr.paris.lutece.portal.util.mvc.binding.BindingResult;


/**
 * ThemeJspBean
 */
@RequestScoped
@Named
@Controller( controllerJsp = "ManageThemes.jsp", controllerPath = "jsp/admin/templates/", right = "CORE_THEME_MANAGEMENT" )
public class ThemeJspBean extends MVCAdminJspBean
{
    private static final long serialVersionUID = 3959917474602825328L;

    // Templates files path
    private static final String TEMPLATE_MANAGE_THEMES = "admin/theme/manage_themes.html";

    // Controller
    private static final String JSP_MANAGE_THEMES = "jsp/admin/plugins/filestoragetransfer/ManageRequests.jsp";

    // Views
    private static final String VIEW_MANAGE_THEMES = "manageThemes";

    // Actions
    private static final String ACTION_MODIFY_GLOBAL_THEME = "modifyGlobalTheme";

    // Messages
    private static final String MESSAGE_OBJECT_NOT_FOUND = "theme.message.object_not_found";
    private static final String MESSAGE_BINDING_RESULT_FAILED = "theme.message.binding_result_failed";

    // Parameters
    public static final String PARAMETER_THEME = "theme";
    public static final String PARAMETER_URL = "url";

    // Properties
    public static final String PROPERTY_MANAGE_THEMES_PAGE_TITLE = "theme.manage_themes.page_title";

    // Marks
    public static final String MARK_THEMES_LIST = "themes_list";
    public static final String MARK_THEME_DEFAULT = "theme_default";
    public static final String MARK_BASE_URL = "base_url";
    
    @Inject
    private ThemeService _themeService;

    /**
     * Returns the list of Themes
     *
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    @View( value = VIEW_MANAGE_THEMES, defaultView = true )
    public String getManageThemes( HttpServletRequest request, Models model )
    {
        Collection<Theme> _listThemes = _themeService.getThemesList(  );
        
        model.put( MARK_THEMES_LIST, _listThemes );
        model.put( MARK_THEME_DEFAULT, _themeService.getGlobalTheme(  ) );
        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        
        setPageTitleProperty( PROPERTY_MANAGE_THEMES_PAGE_TITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_THEMES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify the global theme
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    @Action( ACTION_MODIFY_GLOBAL_THEME )
    public String doModifyGlobalTheme( HttpServletRequest request, 
    @RequestParam( value="theme", defaultValue="") String strTheme,
    @RequestParam( value="theme_version", defaultValue="") String strVersion, 
    BindingResult result)
    {
        if( result.isFailed( ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_BINDING_RESULT_FAILED, AdminMessage.TYPE_STOP );
        }
        else {
            if ( !strTheme.isBlank() )
            {
                _themeService.setGlobalTheme( strTheme, strVersion );
                return redirectView( request, VIEW_MANAGE_THEMES );
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_FOUND, AdminMessage.TYPE_STOP );
            }
        }
    }

}