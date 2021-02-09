/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.editor.RichTextEditorService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * EditorAdminDashboardComponent
 */
public class EditorAdminDashboardComponent extends AdminDashboardComponent
{
    private static final String MARK_LIST_EDITORS_BACK_OFFICE = "listEditorsBackOffice";
    private static final String MARK_LIST_EDITORS_FRONT_OFFICE = "listEditorsFrontOffice";
    private static final String MARK_CURRENT_EDITOR_BACK_OFFICE = "current_editor_back_office";
    private static final String MARK_CURRENT_EDITOR_FRONT_OFFICE = "current_editor_front_office";
    private static final String TEMPLATE_EDITOR_CHOICE_PANEL = "admin/dashboard/admin/editor_dashboard.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_EDITORS_BACK_OFFICE, RichTextEditorService.getListEditorsForBackOffice( AdminUserService.getLocale( request ) ) );
        model.put( MARK_CURRENT_EDITOR_BACK_OFFICE, RichTextEditorService.getBackOfficeDefaultEditor( ) );

        model.put( MARK_LIST_EDITORS_FRONT_OFFICE, RichTextEditorService.getListEditorsForFrontOffice( AdminUserService.getLocale( request ) ) );
        model.put( MARK_CURRENT_EDITOR_FRONT_OFFICE, RichTextEditorService.getFrontOfficeDefaultEditor( ) );

        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_EDITOR_CHOICE_PANEL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EDITOR_CHOICE_PANEL, user.getLocale( ), model );

        return template.getHtml( );

    }
}
