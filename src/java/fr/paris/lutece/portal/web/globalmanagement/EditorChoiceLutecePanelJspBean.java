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
import fr.paris.lutece.portal.service.globalmanagement.RichTextEditorService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * The Class EditorChoiceLutecePanelJspBean.
 */
public class EditorChoiceLutecePanelJspBean extends AbstractGMLutecePanel
{
    /**
     * Generated servial UID
     */
    private static final long serialVersionUID = -3558930087612105420L;
    private static final String LABEL_TITLE_EDITOR_CHOICE = "portal.globalmanagement.editorChoice.labelEditorChoice";
    private static final String PARAM_EDITOR_BACK_OFFICE = "editor_back_office";
    private static final String PARAM_EDITOR_FRONT_OFFICE = "editor_front_office";
    private static final String MARK_LIST_EDITORS_BACK_OFFICE = "listEditorsBackOffice";
    private static final String MARK_LIST_EDITORS_FRONT_OFFICE = "listEditorsFrontOffice";
    private static final String MARK_CURRENT_EDITOR_BACK_OFFICE = "current_editor_back_office";
    private static final String MARK_CURRENT_EDITOR_FRONT_OFFICE = "current_editor_front_office";
    private static final String TEMPLATE_EDITOR_CHOICE_PANEL = "admin/globalmanagement/panel/editor_choice_panel.html";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPanelContent(  )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LIST_EDITORS_BACK_OFFICE,
            RichTextEditorService.getListEditorsForBackOffice( AdminUserService.getLocale( getRequest(  ) ) ) );
        model.put( MARK_CURRENT_EDITOR_BACK_OFFICE, RichTextEditorService.getBackOfficeDefaultEditor(  ) );

        model.put( MARK_LIST_EDITORS_FRONT_OFFICE,
            RichTextEditorService.getListEditorsForFrontOffice( AdminUserService.getLocale( getRequest(  ) ) ) );
        model.put( MARK_CURRENT_EDITOR_FRONT_OFFICE, RichTextEditorService.getFrontOfficeDefaultEditor(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EDITOR_CHOICE_PANEL, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPanelKey(  )
    {
        return LABEL_TITLE_EDITOR_CHOICE;
    }

    /**
     * Returns the panel's order. This panel is a first panel.
     * @return 1
     */
    @Override
    public int getPanelOrder(  )
    {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPanelTitle(  )
    {
        return I18nService.getLocalizedString( LABEL_TITLE_EDITOR_CHOICE, AdminUserService.getLocale( getRequest(  ) ) );
    }

    /**
     * Do update back office editor.
     *
     * @param request the request
     * @return the string
     */
    public String doUpdateBackOfficeEditor( HttpServletRequest request )
    {
        String strEditorName = request.getParameter( PARAM_EDITOR_BACK_OFFICE );
        RichTextEditorService.updateBackOfficeDefaultEditor( strEditorName );

        return AppPathService.getBaseUrl( request ) + GlobalManagementJspBean.JSP_URL_GLOBAL_MANAGEMENT;
    }

    /**
     * Do update front office editor.
     *
     * @param request the request
     * @return the string
     */
    public String doUpdateFrontOfficeEditor( HttpServletRequest request )
    {
        String strEditorName = request.getParameter( PARAM_EDITOR_FRONT_OFFICE );
        RichTextEditorService.updateFrontOfficeDefaultEditor( strEditorName );

        return AppPathService.getBaseUrl( request ) + GlobalManagementJspBean.JSP_URL_GLOBAL_MANAGEMENT;
    }
}
