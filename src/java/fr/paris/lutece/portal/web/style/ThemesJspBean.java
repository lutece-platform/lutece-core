/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.includes.ThemesInclude;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class ThemesJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_MANAGE_THEMES = "CORE_THEMES_MANAGEMENT";

    // Templates files path
    private static final String TEMPLATE_MANAGE_THEMES = "admin/style/manage_themes.html";

    // Markers
    private static final String MARK_THEMES_LIST = "themes_list";
    private static final String MARK_THEME = "theme";

    // Parameters
    private static final String PARAMETER_THEME = "theme";
    private static final String PARAMETER_URL = "url";

    /**
     * Returns the list of Themes
     *
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    public String getManageThemes( HttpServletRequest request )
    {

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_THEMES_LIST, ThemesInclude.getThemesList() );
        model.put( MARK_THEME, ThemesInclude.getGlobalTheme() );

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
        ThemesInclude.setGlobalTheme( strTheme );
        return this.getHomeUrl( request );
    }


    /**
     * Modify the User theme
     *
     * @param request The Http request
     * @return the html code for display the manage themes page
     */
    public String doModifyUserTheme( HttpServletRequest request , HttpServletResponse response )
    {
        String strTheme = request.getParameter( PARAMETER_THEME );
        String strForwardUrl = request.getParameter( PARAMETER_URL );
        ThemesInclude.setUserTheme( request , response , strTheme );
        return strForwardUrl;
    }


}
