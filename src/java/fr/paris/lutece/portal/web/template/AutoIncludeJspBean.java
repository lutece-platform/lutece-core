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
 	
package fr.paris.lutece.portal.web.template;

import fr.paris.lutece.portal.business.template.AutoInclude;
import fr.paris.lutece.portal.service.template.CommonsService;
import fr.paris.lutece.portal.service.template.FreeMarkerTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.admin.AdminMenuJspBean;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage AutoInclude features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageAutoIncludes.jsp", controllerPath = "jsp/admin/templates/", right = "CORE_TEMPLATES_AUTO_INCLUDES_MANAGEMENT" )
public class AutoIncludeJspBean extends MVCAdminJspBean
{
    // Rights
    public static final String RIGHT_MANAGEAUTOINCLUDES = "CORE_TEMPLATES_AUTO_INCLUDES_MANAGEMENT";
    
    // Templates
    private static final String TEMPLATE_MANAGE_AUTOINCLUDES = "/admin/templates/manage_autoincludes.html";

    // Parameters
    private static final String PARAMETER_COMMONS_KEY = "commons_key";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_AUTOINCLUDES = "portal.templates.manage_autoincludes.pageTitle";

    // Markers
    private static final String MARK_AUTOINCLUDE_LIST = "autoinclude_list";
    private static final String MARK_COMMONS_LIST = "commons_list";
    private static final String MARK_CURRENT_COMMONS = "current_commons";

    // Views
    private static final String VIEW_MANAGE_AUTOINCLUDES = "manageAutoIncludes";

    // Actions
    private static final String ACTION_ACTIVATE = "activate";

    // Infos
    private static final String INFO_COMMONS_ACTIVATE = "portal.templates.info.commons.activated";
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_AUTOINCLUDES, defaultView = true )
    public String getManageAutoIncludes( HttpServletRequest request )
    {
        List<AutoInclude> listAutoIncludes = FreeMarkerTemplateService.getInstance().getAutoIncludesList( );
        Map<String, Object> model = getModel();
        model.put( MARK_AUTOINCLUDE_LIST , listAutoIncludes );
        
        model.put( MARK_COMMONS_LIST, CommonsService.getCommonsIncludeList() );
        model.put( MARK_CURRENT_COMMONS, CommonsService.getCurrentCommonsKey() );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_AUTOINCLUDES, TEMPLATE_MANAGE_AUTOINCLUDES, model );
    }

    
    /**
     * Activate a commons include
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage autoincludes
     */
    @Action( ACTION_ACTIVATE )
    public String doActivateCommonsInclude( HttpServletRequest request )
    {
        String strCommonsIncludeKey = request.getParameter( PARAMETER_COMMONS_KEY );
        CommonsService.activateCommons( strCommonsIncludeKey );
        AdminMenuJspBean.resetAdminStylesheets();
        addInfo( INFO_COMMONS_ACTIVATE, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_AUTOINCLUDES );
    }

}