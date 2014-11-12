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
package fr.paris.lutece.portal.web.globalmanagement;

import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.panel.LutecePanelService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Abstract Lutece Panel JspBean
 */
public class GlobalManagementJspBean extends PluginAdminPageJspBean
{
    public static final String JSP_URL_GLOBAL_MANAGEMENT = "jsp/admin/globalmanagement/GetGlobalManagement.jsp";

    /**
     * Generated serial UID
     */
    private static final long serialVersionUID = 4377398578063850128L;
    private static final String MARK_PANELS_LIST = "panels_list";
    private static final String TEMPLATE_GLOBAL_MANAGEMENT = "/admin/globalmanagement/global_management.html";

    /**
     * Gets the global management.
     *
     * @param request the request
     * @return the global management
     */
    public String getGlobalManagement( HttpServletRequest request )
    {
        LutecePanelService<AbstractGMLutecePanel> gmLutecePanel = LutecePanelService.instance( AbstractGMLutecePanel.class );

        List<AbstractGMLutecePanel> listPanels = gmLutecePanel.getPanels(  );

        for ( AbstractGMLutecePanel panel : listPanels )
        {
            panel.setPanelLocale( AdminUserService.getLocale( request ) );
            panel.setRequest( request );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PANELS_LIST, listPanels );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_GLOBAL_MANAGEMENT,
                AdminUserService.getLocale( request ), model );

        return getAdminPage( template.getHtml(  ) );
    }
}
