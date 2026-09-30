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
package fr.paris.lutece.portal.web.style;

import fr.paris.lutece.portal.business.portlet.PortletTemplate;
import fr.paris.lutece.portal.business.portlet.PortletTemplateHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portlet.PortletTemplateResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.binding.BindingResult;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.util.IPager;
import fr.paris.lutece.portal.web.util.Pager;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Management of the portlet templates : the FreeMarker templates the portlets of each type can be rendered with. It replaces the XSL style management of
 * the former versions.
 */
@RequestScoped
@Named
@Controller( controllerJsp = PortletTemplateJspBean.CONTROLLER_JSP, controllerPath = PortletTemplateJspBean.CONTROLLER_PATH, right = PortletTemplateJspBean.RIGHT_MANAGE_PORTLET_TEMPLATES, securityTokenEnabled = true )
public class PortletTemplateJspBean extends MVCAdminJspBean
{
    /** The right of the feature */
    public static final String RIGHT_MANAGE_PORTLET_TEMPLATES = "CORE_PORTLET_TEMPLATE_MANAGEMENT";
    /** The controller JSP */
    public static final String CONTROLLER_JSP = "ManagePortletTemplates.jsp";
    /** The controller path */
    public static final String CONTROLLER_PATH = "jsp/admin/style/";

    private static final long serialVersionUID = 3927836215146725189L;

    // Templates
    private static final String TEMPLATE_MANAGE_TEMPLATES = "admin/style/manage_portlet_templates.html";
    private static final String TEMPLATE_CREATE_TEMPLATE = "admin/style/create_portlet_template.html";
    private static final String TEMPLATE_MODIFY_TEMPLATE = "admin/style/modify_portlet_template.html";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TEMPLATES = "portal.style.manage_portlet_templates.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TEMPLATE = "portal.style.create_portlet_template.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TEMPLATE = "portal.style.modify_portlet_template.pageTitle";
    private static final String PROPERTY_ITEMS_PER_PAGE = "portal.style.portletTemplates.itemsPerPage";
    private static final String PROPERTY_LABEL_ALL = "portal.util.labelAll";

    // Views
    private static final String VIEW_MANAGE_TEMPLATES = "managePortletTemplates";
    private static final String VIEW_CREATE_TEMPLATE = "createPortletTemplate";
    private static final String VIEW_MODIFY_TEMPLATE = "modifyPortletTemplate";
    private static final String VIEW_CONFIRM_REMOVE_TEMPLATE = "confirmRemovePortletTemplate";

    // Actions
    private static final String ACTION_CREATE_TEMPLATE = VIEW_CREATE_TEMPLATE;
    private static final String ACTION_MODIFY_TEMPLATE = VIEW_MODIFY_TEMPLATE;
    private static final String ACTION_REMOVE_TEMPLATE = "removePortletTemplate";

    // Parameters
    private static final String PARAMETER_TEMPLATE_ID = "template_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_TEMPLATE_DESCRIPTION = "template_description";
    private static final String PARAMETER_TEMPLATE_PATH = "template_path";

    // Marks
    private static final String MARK_TEMPLATE_LIST = "template_list";
    private static final String MARK_TEMPLATE = "template";
    private static final String MARK_PORTLET_TYPES_LIST = "portlet_types_list";
    private static final String MARK_PORTLET_TYPE_NAMES = "portlet_type_names";
    private static final String MARK_PORTLET_TYPE_FILTER_LIST = "portlet_type_filter_list";
    private static final String MARK_CURRENT_PORTLET_TYPE = "current_portlet_type";
    private static final String MARK_PERMISSION_CREATE = "permission_create";
    private static final String MARK_PERMISSIONS_MODIFY = "permissions_modify";
    private static final String MARK_PERMISSIONS_DELETE = "permissions_delete";
    private static final String MARK_TEMPLATES_USED = "templates_used";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_TEMPLATE = "portal.style.message.confirmRemovePortletTemplate";
    private static final String MESSAGE_TEMPLATE_IS_USED = "portal.style.message.portletTemplateIsUsed";
    private static final String MESSAGE_TEMPLATE_NOT_FOUND = "portal.style.message.portletTemplateNotFound";
    private static final String MESSAGE_TEMPLATE_FILE_NOT_FOUND = "portal.style.message.portletTemplateFileNotFound";
    private static final String MESSAGE_PORTLET_TYPE_NOT_FOUND = "portal.style.message.portletTemplateTypeNotFound";
    private static final String INFO_TEMPLATE_CREATED = "portal.style.info.portletTemplateCreated";
    private static final String INFO_TEMPLATE_UPDATED = "portal.style.info.portletTemplateUpdated";
    private static final String INFO_TEMPLATE_REMOVED = "portal.style.info.portletTemplateRemoved";

    @Inject
    @Pager( listBookmark = MARK_TEMPLATE_LIST, defaultItemsPerPage = PROPERTY_ITEMS_PER_PAGE, baseUrl = CONTROLLER_PATH + CONTROLLER_JSP )
    private IPager<Integer, PortletTemplate> _pager;

    /**
     * Displays the templates, grouped by portlet type, optionally restricted to one portlet type. The model tells, for each template, whether the user may
     * modify or delete it and whether a portlet uses it : a used template cannot be deleted.
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @return the page
     */
    @View( value = VIEW_MANAGE_TEMPLATES, defaultView = true )
    public String getManagePortletTemplates( HttpServletRequest request, Models model )
    {
        String strPortletTypeFilter = StringUtils.trimToEmpty( request.getParameter( PARAMETER_PORTLET_TYPE_ID ) );
        // the pager lives in the session : it keeps only the identifiers and loads the templates of the displayed page
        List<Integer> listIdTemplates = StringUtils.isNotEmpty( strPortletTypeFilter ) ? PortletTemplateHome.findIdsByPortletType( strPortletTypeFilter )
                : PortletTemplateHome.findAllIds( );

        // the pager links must keep the filter
        UrlItem urlPager = new UrlItem( CONTROLLER_PATH + CONTROLLER_JSP );
        urlPager.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MANAGE_TEMPLATES );

        if ( StringUtils.isNotEmpty( strPortletTypeFilter ) )
        {
            urlPager.addParameter( PARAMETER_PORTLET_TYPE_ID, strPortletTypeFilter );
        }

        _pager.withBaseUrl( urlPager.getUrl( ) ).withIdList( listIdTemplates ).populateModels( request, model, PortletTemplateHome::findByPrimaryKeyList,
                getLocale( ) );

        // the permissions and the usage are only needed for the templates of the displayed page
        @SuppressWarnings( "unchecked" )
        List<PortletTemplate> listPageTemplates = (List<PortletTemplate>) model.get( MARK_TEMPLATE_LIST );
        Map<String, Boolean> mapPermissionsModify = new HashMap<>( );
        Map<String, Boolean> mapPermissionsDelete = new HashMap<>( );
        Map<String, Boolean> mapTemplatesUsed = new HashMap<>( );

        for ( PortletTemplate template : listPageTemplates )
        {
            mapPermissionsModify.put( template.getResourceId( ), isAuthorized( template, PortletTemplateResourceIdService.PERMISSION_MODIFY ) );
            mapPermissionsDelete.put( template.getResourceId( ), isAuthorized( template, PortletTemplateResourceIdService.PERMISSION_DELETE ) );
            mapTemplatesUsed.put( template.getResourceId( ), PortletTemplateHome.isTemplateUsed( template.getId( ) ) );
        }

        model.put( MARK_PERMISSION_CREATE, isCreateAuthorized( ) );
        model.put( MARK_PERMISSIONS_MODIFY, mapPermissionsModify );
        model.put( MARK_PERMISSIONS_DELETE, mapPermissionsDelete );
        model.put( MARK_TEMPLATES_USED, mapTemplatesUsed );
        model.put( MARK_PORTLET_TYPE_NAMES, getPortletTypeNames( ) );
        model.put( MARK_PORTLET_TYPE_FILTER_LIST, getPortletTypeFilterList( ) );
        model.put( MARK_CURRENT_PORTLET_TYPE, strPortletTypeFilter );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_TEMPLATES, TEMPLATE_MANAGE_TEMPLATES, model );
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
    @View( value = VIEW_CREATE_TEMPLATE )
    public String getCreatePortletTemplate( HttpServletRequest request, Models model )
    {
        if ( !isCreateAuthorized( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP ) );
        }

        if ( model.get( MARK_TEMPLATE ) == null )
        {
            model.put( MARK_TEMPLATE, new PortletTemplate( ) );
        }

        model.put( MARK_PORTLET_TYPES_LIST, PortletTypeHome.getPortletsTypesList( getLocale( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_TEMPLATE, TEMPLATE_CREATE_TEMPLATE, model );
    }

    /**
     * Creates a template
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @param strPortletTypeId
     *            the portlet type identifier
     * @param strDescription
     *            the description
     * @param strTemplatePath
     *            the template path
     * @param bindingResult
     *            the binding result
     * @return the redirection
     */
    @Action( value = ACTION_CREATE_TEMPLATE )
    public String doCreatePortletTemplate( HttpServletRequest request, Models model,
            @RequestParam( value = PARAMETER_PORTLET_TYPE_ID, defaultValue = "" ) String strPortletTypeId,
            @RequestParam( value = PARAMETER_TEMPLATE_DESCRIPTION, defaultValue = "" ) String strDescription,
            @RequestParam( value = PARAMETER_TEMPLATE_PATH, defaultValue = "" ) String strTemplatePath, BindingResult bindingResult )
    {
        if ( !isCreateAuthorized( ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP ) );
        }

        PortletTemplate template = new PortletTemplate( );
        template.setPortletTypeId( strPortletTypeId.trim( ) );
        template.setDescription( strDescription.trim( ) );
        template.setTemplatePath( strTemplatePath.trim( ) );

        if ( bindingResult.isFailed( ) )
        {
            model.put( MARK_TEMPLATE, template );
            model.put( MVCUtils.MARK_ERRORS, bindingResult.getAllErrors( ) );

            return getCreatePortletTemplate( request, model );
        }

        String strErrorUrl = checkTemplateData( request, template, true );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        PortletTemplateHome.create( template );
        addInfo( INFO_TEMPLATE_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_TEMPLATES );
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
    @View( value = VIEW_MODIFY_TEMPLATE )
    public String getModifyPortletTemplate( HttpServletRequest request, Models model )
    {
        PortletTemplate template = getTemplateFromRequest( request );
        String strErrorUrl = checkTemplate( request, template, PortletTemplateResourceIdService.PERMISSION_MODIFY );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        if ( model.get( MARK_TEMPLATE ) == null )
        {
            model.put( MARK_TEMPLATE, template );
        }

        model.put( MARK_PORTLET_TYPES_LIST, PortletTypeHome.getPortletsTypesList( getLocale( ) ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_TEMPLATE, TEMPLATE_MODIFY_TEMPLATE, model );
    }

    /**
     * Modifies a template. The template path is never changed : a template is bound to its file, a portlet needing another file needs another
     * template.
     *
     * @param request
     *            the HTTP request
     * @param model
     *            the model
     * @param strPortletTypeId
     *            the portlet type identifier
     * @param strDescription
     *            the description
     * @param bindingResult
     *            the binding result
     * @return the redirection
     */
    @Action( value = ACTION_MODIFY_TEMPLATE )
    public String doModifyPortletTemplate( HttpServletRequest request, Models model,
            @RequestParam( value = PARAMETER_PORTLET_TYPE_ID, defaultValue = "" ) String strPortletTypeId,
            @RequestParam( value = PARAMETER_TEMPLATE_DESCRIPTION, defaultValue = "" ) String strDescription, BindingResult bindingResult )
    {
        PortletTemplate template = getTemplateFromRequest( request );
        String strErrorUrl = checkTemplate( request, template, PortletTemplateResourceIdService.PERMISSION_MODIFY );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        template.setPortletTypeId( strPortletTypeId.trim( ) );
        template.setDescription( strDescription.trim( ) );

        if ( bindingResult.isFailed( ) )
        {
            model.put( MARK_TEMPLATE, template );
            model.put( MVCUtils.MARK_ERRORS, bindingResult.getAllErrors( ) );

            return getModifyPortletTemplate( request, model );
        }

        strErrorUrl = checkTemplateData( request, template, false );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        PortletTemplateHome.update( template );
        addInfo( INFO_TEMPLATE_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_TEMPLATES );
    }

    /**
     * Displays the removal confirmation
     *
     * @param request
     *            the HTTP request
     * @return the redirection to the confirmation message
     */
    @View( value = VIEW_CONFIRM_REMOVE_TEMPLATE, securityTokenAction = ACTION_REMOVE_TEMPLATE )
    public String getConfirmRemovePortletTemplate( HttpServletRequest request )
    {
        PortletTemplate template = getTemplateFromRequest( request );
        String strErrorUrl = checkRemovableTemplate( request, template );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TEMPLATE ) );
        url.addParameter( PARAMETER_TEMPLATE_ID, template.getId( ) );

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TEMPLATE, new Object [ ] {
                template.getDescription( )
        }, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Removes a template. A template used by a portlet is never removed.
     *
     * @param request
     *            the HTTP request
     * @return the redirection
     */
    @Action( value = ACTION_REMOVE_TEMPLATE )
    public String doRemovePortletTemplate( HttpServletRequest request )
    {
        PortletTemplate template = getTemplateFromRequest( request );
        String strErrorUrl = checkRemovableTemplate( request, template );

        if ( strErrorUrl != null )
        {
            return redirect( request, strErrorUrl );
        }

        PortletTemplateHome.remove( template.getId( ) );
        addInfo( INFO_TEMPLATE_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_TEMPLATES );
    }

    /**
     * Loads the template designated by the request
     *
     * @param request
     *            the HTTP request
     * @return the template, null when the identifier is missing, malformed or unknown
     */
    private PortletTemplate getTemplateFromRequest( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_TEMPLATE_ID );

        if ( !StringUtils.isNumeric( strId ) )
        {
            return null;
        }

        return PortletTemplateHome.findByPrimaryKey( Integer.parseInt( strId ) );
    }

    /**
     * Checks that a template exists and that the user holds a permission on it
     *
     * @param request
     *            the HTTP request
     * @param template
     *            the template, may be null
     * @param strPermission
     *            the permission
     * @return the URL of the error message, null when everything is fine
     */
    private String checkTemplate( HttpServletRequest request, PortletTemplate template, String strPermission )
    {
        if ( template == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_TEMPLATE_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        if ( !isAuthorized( template, strPermission ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        return null;
    }

    /**
     * Checks that a template can be removed : it exists, the user may delete it and no portlet uses it
     *
     * @param request
     *            the HTTP request
     * @param template
     *            the template, may be null
     * @return the URL of the error message, null when the template can be removed
     */
    private String checkRemovableTemplate( HttpServletRequest request, PortletTemplate template )
    {
        String strErrorUrl = checkTemplate( request, template, PortletTemplateResourceIdService.PERMISSION_DELETE );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        if ( PortletTemplateHome.isTemplateUsed( template.getId( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_TEMPLATE_IS_USED, AdminMessage.TYPE_STOP );
        }

        return null;
    }

    /**
     * Checks the data of a template : every field is filled, the portlet type exists and, when asked, a template file exists at the given path
     *
     * @param request
     *            the HTTP request
     * @param template
     *            the template
     * @param bCheckTemplateFile
     *            true to check that the template file exists, false when the path is not being changed
     * @return the URL of the error message, null when the data is valid
     */
    private String checkTemplateData( HttpServletRequest request, PortletTemplate template, boolean bCheckTemplateFile )
    {
        if ( StringUtils.isAnyBlank( template.getPortletTypeId( ), template.getDescription( ), template.getTemplatePath( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ReferenceList listPortletTypes = PortletTypeHome.getPortletsTypesList( getLocale( ) );

        if ( listPortletTypes.stream( ).noneMatch( item -> item.getCode( ).equals( template.getPortletTypeId( ) ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_PORTLET_TYPE_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        if ( bCheckTemplateFile && !AppTemplateService.isTemplateExists( template.getTemplatePath( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_TEMPLATE_FILE_NOT_FOUND, new Object [ ] {
                    template.getTemplatePath( )
            }, AdminMessage.TYPE_STOP );
        }

        return null;
    }

    /**
     * Returns the localized names of the portlet types, by identifier
     *
     * @return the names
     */
    private Map<String, String> getPortletTypeNames( )
    {
        Map<String, String> mapNames = new HashMap<>( );

        for ( ReferenceItem item : PortletTypeHome.getPortletsTypesList( getLocale( ) ) )
        {
            mapNames.put( item.getCode( ), item.getName( ) );
        }

        return mapNames;
    }

    /**
     * Returns the portlet types offered by the filter of the list : an "all" entry with an empty code, then every portlet type
     *
     * @return the reference list
     */
    private ReferenceList getPortletTypeFilterList( )
    {
        ReferenceList list = new ReferenceList( );
        list.addItem( StringUtils.EMPTY, I18nService.getLocalizedString( PROPERTY_LABEL_ALL, getLocale( ) ) );
        list.addAll( PortletTypeHome.getPortletsTypesList( getLocale( ) ) );

        return list;
    }

    /**
     * Tells whether the current user may create templates : the CREATE permission is held on the resource type as a whole
     *
     * @return true when the user is authorized
     */
    private boolean isCreateAuthorized( )
    {
        return RBACService.isAuthorized( PortletTemplate.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID, PortletTemplateResourceIdService.PERMISSION_CREATE,
                getUser( ) );
    }

    /**
     * Tells whether the current user holds a permission on a template
     *
     * @param template
     *            the template
     * @param strPermission
     *            the permission
     * @return true when the user is authorized
     */
    private boolean isAuthorized( PortletTemplate template, String strPermission )
    {
        return RBACService.isAuthorized( template, strPermission, getUser( ) );
    }
}
