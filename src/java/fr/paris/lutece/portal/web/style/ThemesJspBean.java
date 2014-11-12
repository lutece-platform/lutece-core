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
package fr.paris.lutece.portal.web.style;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.business.style.ThemeHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portal.ThemesService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @deprecated As of 2.4.3. Use plugin-theme instead.
 */
public class ThemesJspBean extends AdminFeaturesPageJspBean
{
    // Right
    /**
     * Right to manage themes
     */
    public static final String RIGHT_MANAGE_THEMES = "CORE_THEMES_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -8201123021058332937L;

    // Templates files path
    private static final String TEMPLATE_MANAGE_THEMES = "admin/style/manage_themes.html";
    private static final String TEMPLATE_CREATE_THEME = "admin/style/create_theme.html";
    private static final String TEMPLATE_MODIFY_THEME = "admin/style/modify_theme.html";

    // Markers
    private static final String MARK_THEMES_LIST = "themes_list";
    private static final String MARK_THEME = "theme_default";

    // Parameters
    private static final String PARAMETER_THEME = "theme";
    private static final String PARAMETER_URL = "url";
    private static final String BASE_URL = "base_url";
    private static final String THEME_LICENCE = "theme_licence";
    private static final String THEME_VERSION = "theme_version";
    private static final String THEME_AUTHOR_URL = "theme_author_url";
    private static final String THEME_AUTHOR = "theme_author";
    private static final String PATH_CSS = "path_css";
    private static final String PATH_JS = "path_js";
    private static final String PATH_IMAGES = "path_images";
    private static final String THEME_DESCRIPTION = "theme_description";
    private static final String CODE_THEME = "code_theme";

    /**
     * Returns the list of Themes
     *
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    public String getManageThemes( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_THEMES_LIST, ThemeHome.getThemesList(  ) );
        model.put( MARK_THEME, ThemesService.getGlobalThemeObject(  ) );
        model.put( BASE_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_THEMES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify the global theme
     *
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    public String doModifyGlobalTheme( HttpServletRequest request )
    {
        String strTheme = request.getParameter( PARAMETER_THEME );
        ThemesService.setGlobalTheme( strTheme );

        return this.getHomeUrl( request );
    }

    /**
     * Modify the User theme
     *
     * @param request The Http request
     * @param response The Http response
     * @return the html code for display the manage themes page
     */
    public String doModifyUserTheme( HttpServletRequest request, HttpServletResponse response )
    {
        String strTheme = request.getParameter( PARAMETER_THEME );
        String strForwardUrl = request.getParameter( PARAMETER_URL );

        if ( !SecurityUtil.containsCleanParameters( request ) )
        {
            return AppPathService.getBaseUrl( request );
        }

        ThemesService.setUserTheme( request, response, strTheme );

        return strForwardUrl;
    }

    /**
     *
     * @param request The HttpServletRequest
     * @return the html code to create a theme
     */
    public String getCreateTheme( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( BASE_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_THEME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     *
     * @param request The HttpServletRequest
     * @return the html page to modifiy a theme
     */
    public String getModifyTheme( HttpServletRequest request )
    {
        Theme themeToModify = ThemeHome.findByPrimaryKey( request.getParameter( CODE_THEME ) );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( PARAMETER_THEME, themeToModify );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_THEME, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     *
     * @param request The HttpServletRequest
     * @return the html code for the theme list
     */
    public String doModifyTheme( HttpServletRequest request )
    {
        Theme theme = getThemeFromRequest( request );

        // Mandatory fields
        if ( isMissingFields( request ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ThemeHome.update( theme );

        return this.getHomeUrl( request );
    }

    /**
     *
     * @param request The HttpServletRequest
     * @return the html code for the theme list
     */
    public String doCreateTheme( HttpServletRequest request )
    {
        Theme theme = getThemeFromRequest( request );

        // Mandatory fields
        if ( isMissingFields( request ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ThemeHome.create( theme );

        return this.getHomeUrl( request );
    }

    /**
     *
     * @param request The HttpServletRequest
     * @return the theme object from request parameter
     */
    private Theme getThemeFromRequest( HttpServletRequest request )
    {
        Theme theme = new Theme(  );

        theme.setCodeTheme( request.getParameter( CODE_THEME ) );
        theme.setThemeDescription( request.getParameter( THEME_DESCRIPTION ) );
        theme.setPathImages( request.getParameter( PATH_IMAGES ) );
        theme.setPathCss( request.getParameter( PATH_CSS ) );
        theme.setPathJs( request.getParameter( PATH_JS ) );
        theme.setThemeAuthor( request.getParameter( THEME_AUTHOR ) );
        theme.setThemeAuthorUrl( request.getParameter( THEME_AUTHOR_URL ) );
        theme.setThemeVersion( request.getParameter( THEME_VERSION ) );
        theme.setThemeLicence( request.getParameter( THEME_LICENCE ) );

        return theme;
    }

    /**
     * @param request The HttpServletRequest
     * @return true if 1 field is missing
     */
    private boolean isMissingFields( HttpServletRequest request )
    {
        return request.getParameter( CODE_THEME ).equals( "" ) ||
        request.getParameter( THEME_DESCRIPTION ).equals( "" ) || request.getParameter( PATH_IMAGES ).equals( "" ) ||
        request.getParameter( PATH_CSS ).equals( "" ) || request.getParameter( PATH_JS ).equals( "" );
    }
}
