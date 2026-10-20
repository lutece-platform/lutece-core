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
package fr.paris.lutece.portal.web.editor;

import fr.paris.lutece.portal.business.editor.EditorProfile;
import fr.paris.lutece.portal.business.editor.EditorProfileHome;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardComponent;
import fr.paris.lutece.portal.service.editor.EditorProfileService;
import fr.paris.lutece.portal.service.editor.RichTextEditorService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

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
    private static final String MARK_PROFILES_BY_EDITOR = "editor_profiles_by_editor";
    private static final String MARK_EDITOR_LABELS = "editor_labels";
    private static final String MARK_ROLE_LABELS = "role_labels";
    private static final String MARK_WORKGROUP_LABELS = "workgroup_labels";
    private static final String MARK_MOVE_TOKEN = "move_profile_token";
    private static final String PROPERTY_DEFAULT_PROFILE_NAME = "portal.admindashboard.editorProfile.defaultProfileName";
    private static final String DEFAULT_PROFILE_NAME = "Default profile";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        ISecurityTokenService securityTokenService = CDI.current( ).select( ISecurityTokenService.class ).get( );    	
    	
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_LIST_EDITORS_BACK_OFFICE, RichTextEditorService.getListEditorsForBackOffice( AdminUserService.getLocale( request ) ) );
        model.put( MARK_CURRENT_EDITOR_BACK_OFFICE, RichTextEditorService.getBackOfficeDefaultEditor( ) );

        model.put( MARK_LIST_EDITORS_FRONT_OFFICE, RichTextEditorService.getListEditorsForFrontOffice( AdminUserService.getLocale( request ) ) );
        model.put( MARK_CURRENT_EDITOR_FRONT_OFFICE, RichTextEditorService.getFrontOfficeDefaultEditor( ) );

        model.put( SecurityTokenService.MARK_TOKEN, securityTokenService.getToken( request, TEMPLATE_EDITOR_CHOICE_PANEL ) );

        if ( user.checkRight( EditorChoiceLutecePanelJspBean.RIGHT_EDITORS_MANAGEMENT ) )
        {
            fillProfilesModel( model, request, AdminUserService.getLocale( request ), securityTokenService );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EDITOR_CHOICE_PANEL, user.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Puts the editor profiles in the model, by editor with the editor of the back office first, with the labels of their editors, roles and workgroups,
     * and the token of the forms moving them. The default profile of an editor is created when it is missing.
     *
     * @param model
     *            the model
     * @param request
     *            the HTTP request
     * @param locale
     *            the locale
     * @param securityTokenService
     *            the security token service
     */
    private static void fillProfilesModel( Map<String, Object> model, HttpServletRequest request, Locale locale, ISecurityTokenService securityTokenService )
    {
        ReferenceList listEditors = EditorProfileJspBean.getEditorsList( locale );
        List<String> listEditorNames = new ArrayList<>( );
        Map<String, String> mapEditorLabels = new HashMap<>( );

        for ( ReferenceItem item : listEditors )
        {
            listEditorNames.add( item.getCode( ) );
            mapEditorLabels.put( item.getCode( ), item.getName( ) );
        }

        EditorProfileService editorProfileService = CDI.current( ).select( EditorProfileService.class ).get( );
        // a missing key gives an empty label, and a profile needs a name
        String strDefaultName = StringUtils.defaultIfBlank( I18nService.getLocalizedString( PROPERTY_DEFAULT_PROFILE_NAME, locale ), DEFAULT_PROFILE_NAME );
        editorProfileService.createMissingDefaultProfiles( listEditorNames, strDefaultName );

        // the editor of the back office first, the others in the order of the list
        String strCurrentEditor = RichTextEditorService.getBackOfficeDefaultEditor( );
        listEditorNames.sort( ( e1, e2 ) -> Boolean.compare( !e1.equals( strCurrentEditor ), !e2.equals( strCurrentEditor ) ) );

        Map<String, List<EditorProfile>> mapProfiles = new LinkedHashMap<>( );

        for ( String strEditorName : listEditorNames )
        {
            mapProfiles.put( strEditorName, new ArrayList<>( ) );
        }

        for ( EditorProfile profile : EditorProfileHome.findAll( ) )
        {
            List<EditorProfile> listProfiles = mapProfiles.get( profile.getEditorName( ) );

            // the profiles of an editor no longer offered are kept, but not shown
            if ( listProfiles != null )
            {
                listProfiles.add( profile );
            }
        }

        Map<String, String> mapRoleLabels = new HashMap<>( );

        for ( ReferenceItem item : RBACRoleHome.getRolesList( ) )
        {
            mapRoleLabels.put( item.getCode( ), item.getName( ) );
        }

        Map<String, String> mapWorkgroupLabels = new HashMap<>( );

        for ( AdminWorkgroup workgroup : AdminWorkgroupHome.findAll( ) )
        {
            mapWorkgroupLabels.put( workgroup.getKey( ), workgroup.getDescription( ) );
        }

        model.put( MARK_PROFILES_BY_EDITOR, mapProfiles );
        model.put( MARK_EDITOR_LABELS, mapEditorLabels );
        model.put( MARK_ROLE_LABELS, mapRoleLabels );
        model.put( MARK_WORKGROUP_LABELS, mapWorkgroupLabels );
        model.put( MARK_MOVE_TOKEN, securityTokenService.getToken( request, EditorProfileJspBean.ACTION_MOVE_PROFILE ) );
    }
}
