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
package fr.paris.lutece.portal.web.editor;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.editor.RichTextEditorService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class EditorChoiceLutecePanelJspBean.
 */
public class EditorChoiceLutecePanelJspBean extends PluginAdminPageJspBean implements Serializable
{
    public static final String RIGHT_EDITORS_MANAGEMENT = "CORE_EDITORS_MANAGEMENT";
    private static final String ANCHOR_ADMIN_DASHBOARDS = "editors";
    
    /**
     * Generated servial UID
     */
    private static final long serialVersionUID = -3558930087612105420L;
    private static final String PARAM_EDITOR_BACK_OFFICE = "editor_back_office";
    private static final String PARAM_EDITOR_FRONT_OFFICE = "editor_front_office";
    private static final String TEMPLATE_EDITOR_CHOICE_PANEL = "admin/dashboard/admin/editor_dashboard.html";


    /**
     * Do update back office editor.
     *
     * @param request
     *            the request
     * @return the string
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doUpdateBackOfficeEditor( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_EDITOR_CHOICE_PANEL ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        String strEditorName = request.getParameter( PARAM_EDITOR_BACK_OFFICE );
        RichTextEditorService.updateBackOfficeDefaultEditor( strEditorName );

        return getAdminDashboardsUrl( request , ANCHOR_ADMIN_DASHBOARDS );
    }

    /**
     * Do update front office editor.
     *
     * @param request
     *            the request
     * @return the string
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doUpdateFrontOfficeEditor( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_EDITOR_CHOICE_PANEL ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }
        String strEditorName = request.getParameter( PARAM_EDITOR_FRONT_OFFICE );
        RichTextEditorService.updateFrontOfficeDefaultEditor( strEditorName );

        return getAdminDashboardsUrl( request , ANCHOR_ADMIN_DASHBOARDS );
    }
}
