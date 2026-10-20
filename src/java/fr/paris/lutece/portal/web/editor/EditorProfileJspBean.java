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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import fr.paris.lutece.portal.business.editor.EditorProfile;
import fr.paris.lutece.portal.business.editor.EditorProfileHome;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.editor.EditorProfileService;
import fr.paris.lutece.portal.service.editor.RichTextEditorService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Management of the profiles of the back office rich text editors. The profiles are listed in the "Text editor" tab of the technical settings
 * (EditorAdminDashboardComponent): this controller only holds their forms and actions, and brings the user back to that tab. A profile belongs to the editor
 * chosen at its creation, the editor of the back office at that time by default, and keeps it.
 */
@RequestScoped
@Named
@Controller( controllerJsp = EditorProfileJspBean.CONTROLLER_JSP, controllerPath = EditorProfileJspBean.CONTROLLER_PATH, right = EditorChoiceLutecePanelJspBean.RIGHT_EDITORS_MANAGEMENT, securityTokenEnabled = true )
public class EditorProfileJspBean extends MVCAdminJspBean
{
    /** The controller JSP */
    public static final String CONTROLLER_JSP = "ManageEditorProfiles.jsp";
    /** The controller path */
    public static final String CONTROLLER_PATH = "jsp/admin/admindashboard/";
    /** The action moving a profile, whose token the dashboard tab puts in its forms */
    public static final String ACTION_MOVE_PROFILE = "moveEditorProfile";

    private static final long serialVersionUID = 6135420986547812054L;

    // Templates
    private static final String TEMPLATE_CREATE_PROFILE = "admin/dashboard/admin/create_editor_profile.html";
    private static final String TEMPLATE_MODIFY_PROFILE = "admin/dashboard/admin/modify_editor_profile.html";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_CREATE_PROFILE = "portal.admindashboard.create_editor_profile.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PROFILE = "portal.admindashboard.modify_editor_profile.pageTitle";

    // Views
    private static final String VIEW_BACK_TO_DASHBOARD = "backToDashboard";
    private static final String VIEW_CREATE_PROFILE = "createEditorProfile";
    private static final String VIEW_MODIFY_PROFILE = "modifyEditorProfile";
    private static final String VIEW_CONFIRM_REMOVE_PROFILE = "confirmRemoveEditorProfile";

    // Actions
    private static final String ACTION_CREATE_PROFILE = VIEW_CREATE_PROFILE;
    private static final String ACTION_MODIFY_PROFILE = VIEW_MODIFY_PROFILE;
    private static final String ACTION_REMOVE_PROFILE = "removeEditorProfile";

    // Parameters
    private static final String PARAMETER_PROFILE_ID = "profile_id";
    private static final String PARAMETER_NAME = "profile_name";
    private static final String PARAMETER_DESCRIPTION = "profile_description";
    private static final String PARAMETER_EDITOR = "profile_editor";
    private static final String PARAMETER_CONFIG = "profile_config";
    private static final String PARAMETER_PLUGINS = "profile_plugins";
    private static final String PARAMETER_ROLES = "profile_roles";
    private static final String PARAMETER_WORKGROUPS = "profile_workgroups";
    private static final String PARAMETER_DIRECTION = "direction";
    private static final String DIRECTION_UP = "up";

    // Marks
    private static final String MARK_PROFILE = "profile";
    private static final String MARK_EDITORS_LIST = "editors_list";
    private static final String MARK_PLUGINS_LIST = "plugins_list";
    private static final String MARK_ROLES_LIST = "roles_list";
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    private static final String MARK_EDITOR_LABEL = "editor_label";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_PROFILE = "portal.admindashboard.message.confirmRemoveEditorProfile";
    private static final String MESSAGE_PROFILE_NOT_FOUND = "portal.admindashboard.message.editorProfileNotFound";
    private static final String MESSAGE_DEFAULT_PROFILE_NOT_REMOVABLE = "portal.admindashboard.message.defaultEditorProfileNotRemovable";
    private static final String ERROR_NAME_MANDATORY = "portal.admindashboard.message.editorProfileNameMandatory";
    private static final String ERROR_PLUGIN_MANDATORY = "portal.admindashboard.message.editorProfilePluginMandatory";
    private static final String ERROR_INVALID_CONFIG = "portal.admindashboard.message.editorProfileInvalidConfig";
    private static final String ERROR_EDITOR_MANDATORY = "portal.admindashboard.message.editorProfileEditorMandatory";

    private static final String ANCHOR_EDITORS = "editors";

    @Inject
    private EditorProfileService _editorProfileService;

    /**
     * The profiles are listed in the technical settings: the default view goes back there
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the tab of the editors
     */
    @View( value = VIEW_BACK_TO_DASHBOARD, defaultView = true )
    public String getBackToDashboard( HttpServletRequest request )
    {
        return redirect( request, getAdminDashboardsUrl( request, ANCHOR_EDITORS ) );
    }

    /**
     * Displays the creation form
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @return the page
     */
    @View( value = VIEW_CREATE_PROFILE )
    public String getCreateEditorProfile( HttpServletRequest request, Models model )
    {
        if ( model.get( MARK_PROFILE ) == null )
        {
            // a new profile belongs by default to the editor of the back office
            EditorProfile profile = new EditorProfile( );
            profile.setEditorName( RichTextEditorService.getBackOfficeDefaultEditor( ) );
            model.put( MARK_PROFILE, profile );
        }
        fillFormModel( model );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PROFILE, TEMPLATE_CREATE_PROFILE, model );
    }

    /**
     * Creates a profile, after the existing ones in the order of priority
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @return the redirection to the tab of the editors, or the form with its errors
     */
    @Action( value = ACTION_CREATE_PROFILE )
    public String doCreateEditorProfile( HttpServletRequest request, Models model )
    {
        EditorProfile profile = new EditorProfile( );
        profile.setEditorName( request.getParameter( PARAMETER_EDITOR ) );
        boolean bValidEditor = getEditorsList( getLocale( ) ).stream( ).anyMatch( editor -> editor.getCode( ).equals( profile.getEditorName( ) ) );

        if ( !bValidEditor )
        {
            addError( ERROR_EDITOR_MANDATORY, getLocale( ) );
        }

        if ( !populate( request, profile ) || !bValidEditor )
        {
            model.put( MARK_PROFILE, profile );

            return getCreateEditorProfile( request, model );
        }

        EditorProfileHome.create( profile );

        return getBackToDashboard( request );
    }

    /**
     * Displays the modification form
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @return the page
     */
    @View( value = VIEW_MODIFY_PROFILE )
    public String getModifyEditorProfile( HttpServletRequest request, Models model )
    {
        EditorProfile profile = getProfileFromRequest( request );

        if ( profile == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_PROFILE_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        if ( model.get( MARK_PROFILE ) == null )
        {
            model.put( MARK_PROFILE, profile );
        }
        fillFormModel( model );
        model.put( MARK_EDITOR_LABEL, getEditorsList( getLocale( ) ).stream( ).filter( editor -> editor.getCode( ).equals( profile.getEditorName( ) ) )
                .map( ReferenceItem::getName ).findFirst( ).orElse( profile.getEditorName( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PROFILE, TEMPLATE_MODIFY_PROFILE, model );
    }

    /**
     * Modifies a profile. Its editor is never changed, nor the targets of a default profile: it applies when no other profile of its editor does.
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @return the redirection to the tab of the editors, or the form with its errors
     */
    @Action( value = ACTION_MODIFY_PROFILE )
    public String doModifyEditorProfile( HttpServletRequest request, Models model )
    {
        EditorProfile profile = getProfileFromRequest( request );

        if ( profile == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_PROFILE_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        if ( !populate( request, profile ) )
        {
            model.put( MARK_PROFILE, profile );

            return getModifyEditorProfile( request, model );
        }

        EditorProfileHome.update( profile );

        return getBackToDashboard( request );
    }

    /**
     * Displays the removal confirmation
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the confirmation message
     */
    @View( value = VIEW_CONFIRM_REMOVE_PROFILE, securityTokenAction = ACTION_REMOVE_PROFILE )
    public String getConfirmRemoveEditorProfile( HttpServletRequest request )
    {
        EditorProfile profile = getProfileFromRequest( request );
        String strErrorUrl = checkRemovable( request, profile );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PROFILE ) );
        url.addParameter( PARAMETER_PROFILE_ID, profile.getId( ) );

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PROFILE, new Object [ ] {
                profile.getName( )
        }, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Removes a profile. The default profile is never removed.
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the tab of the editors
     */
    @Action( value = ACTION_REMOVE_PROFILE )
    public String doRemoveEditorProfile( HttpServletRequest request )
    {
        EditorProfile profile = getProfileFromRequest( request );
        String strErrorUrl = checkRemovable( request, profile );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        EditorProfileHome.remove( profile.getId( ) );

        return getBackToDashboard( request );
    }

    /**
     * Moves a profile one step before or after in the order of priority
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the tab of the editors
     */
    @Action( value = ACTION_MOVE_PROFILE )
    public String doMoveEditorProfile( HttpServletRequest request )
    {
        EditorProfile profile = getProfileFromRequest( request );

        if ( profile != null && !profile.isDefault( ) )
        {
            _editorProfileService.move( profile, DIRECTION_UP.equals( request.getParameter( PARAMETER_DIRECTION ) ) );
        }

        return getBackToDashboard( request );
    }

    /**
     * Sets the values of the form on a profile, except its editor, and checks them
     *
     * @param request
     *            the HTTP request
     * @param profile
     *            the profile
     * @return true when the values are valid, false after adding the errors
     */
    private boolean populate( HttpServletRequest request, EditorProfile profile )
    {
        Locale locale = getLocale( );
        boolean bValid = true;

        profile.setName( StringUtils.trimToEmpty( request.getParameter( PARAMETER_NAME ) ) );
        profile.setDescription( StringUtils.trimToEmpty( request.getParameter( PARAMETER_DESCRIPTION ) ) );

        // The XSS filter of the back office turns the quotes of the JSON into entities
        String strConfig = StringUtils.trimToEmpty( StringEscapeUtils.unescapeHtml4( request.getParameter( PARAMETER_CONFIG ) ) );
        profile.setConfig( strConfig.isEmpty( ) ? EditorProfile.EMPTY_CONFIG : strConfig );

        if ( !profile.isDefault( ) )
        {
            profile.setPlugins( getValues( request, PARAMETER_PLUGINS ) );
            profile.setRoles( getValues( request, PARAMETER_ROLES ) );
            profile.setWorkgroups( getValues( request, PARAMETER_WORKGROUPS ) );

            if ( profile.getPlugins( ).isEmpty( ) )
            {
                addError( ERROR_PLUGIN_MANDATORY, locale );
                bValid = false;
            }
        }

        if ( profile.getName( ).isEmpty( ) )
        {
            addError( ERROR_NAME_MANDATORY, locale );
            bValid = false;
        }

        if ( !EditorProfileService.isValidConfig( profile.getConfig( ) ) )
        {
            addError( ERROR_INVALID_CONFIG, locale );
            bValid = false;
        }

        return bValid;
    }

    /**
     * Puts the lists of the form in the model
     *
     * @param model
     *            the model
     */
    private void fillFormModel( Models model )
    {
        ReferenceList listPlugins = new ReferenceList( );
        listPlugins.addItem( EditorProfileService.PLUGIN_CORE, EditorProfileService.PLUGIN_CORE );

        List<Plugin> listInstalledPlugins = new ArrayList<>( PluginService.getPluginList( ) );
        listInstalledPlugins.removeIf( plugin -> !plugin.isInstalled( ) || EditorProfileService.PLUGIN_CORE.equals( plugin.getName( ) ) );
        listInstalledPlugins.sort( ( p1, p2 ) -> p1.getName( ).compareTo( p2.getName( ) ) );

        for ( Plugin plugin : listInstalledPlugins )
        {
            listPlugins.addItem( plugin.getName( ), plugin.getName( ) );
        }

        ReferenceList listWorkgroups = new ReferenceList( );

        for ( AdminWorkgroup workgroup : AdminWorkgroupHome.findAll( ) )
        {
            listWorkgroups.addItem( workgroup.getKey( ), workgroup.getDescription( ) );
        }

        model.put( MARK_EDITORS_LIST, getEditorsList( getLocale( ) ) );
        model.put( MARK_PLUGINS_LIST, listPlugins );
        model.put( MARK_ROLES_LIST, RBACRoleHome.getRolesList( ) );
        model.put( MARK_WORKGROUPS_LIST, listWorkgroups );
    }

    /**
     * @param locale
     *            the locale
     * @return the editors of the back office a profile can belong to: all but "no editor"
     */
    public static ReferenceList getEditorsList( Locale locale )
    {
        ReferenceList listEditors = new ReferenceList( );

        for ( ReferenceItem editor : RichTextEditorService.getListEditorsForBackOffice( locale ) )
        {
            if ( StringUtils.isNotEmpty( editor.getCode( ) ) )
            {
                listEditors.add( editor );
            }
        }

        return listEditors;
    }

    /**
     * @param request
     *            the HTTP request
     * @param strParameter
     *            the name of a multiple parameter
     * @return its non blank values, without duplicates
     */
    private static List<String> getValues( HttpServletRequest request, String strParameter )
    {
        String [ ] values = request.getParameterValues( strParameter );

        if ( values == null )
        {
            return new ArrayList<>( );
        }

        return new ArrayList<>( Arrays.stream( values ).filter( StringUtils::isNotBlank ).map( String::trim ).distinct( ).toList( ) );
    }

    /**
     * Loads the profile designated by the request
     *
     * @param request
     *            the HTTP request
     * @return the profile, null when the identifier is missing, malformed or unknown
     */
    private static EditorProfile getProfileFromRequest( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_PROFILE_ID );

        if ( !StringUtils.isNumeric( strId ) )
        {
            return null;
        }

        return EditorProfileHome.findByPrimaryKey( Integer.parseInt( strId ) );
    }

    /**
     * Checks that a profile exists and is not the default one
     *
     * @param request
     *            the HTTP request
     * @param profile
     *            the profile
     * @return the URL of the error message, null when the profile can be removed
     */
    private static String checkRemovable( HttpServletRequest request, EditorProfile profile )
    {
        if ( profile == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_PROFILE_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        if ( profile.isDefault( ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DEFAULT_PROFILE_NOT_REMOVABLE, AdminMessage.TYPE_STOP );
        }

        return null;
    }
}
