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
package fr.paris.lutece.portal.web.role;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.BeanUtils;
import fr.paris.lutece.portal.service.util.RemovalListenerService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * JspBean for page Role management through the admin MVC front-controller.
 */
@RequestScoped
@Named
@Controller( name = "pagerole", right = "CORE_ROLES_MANAGEMENT" )
public class RoleJspBean extends MVCAdminJspBean
{
    // Right
    /**
     * Right to manage roles
     */
    public static final String RIGHT_ROLES_MANAGEMENT = "CORE_ROLES_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -764350969432795013L;

    // Markers
    private static final String MARK_ROLES_LIST = "roles_list";
    private static final String MARK_EXIST_RBAC_MAP = "exist_rbac_map";

    private static final String MARK_ROLE = "role";
    private static final String MARK_DEFAULT_VALUE_WORKGROUP_KEY = "workgroup_key_default_value";
    private static final String MARK_WORKGROUP_KEY_LIST = "workgroup_key_list";

    // Parameters
    private static final String PARAMETER_PAGE_ROLE = "role";
    private static final String PARAMETER_PAGE_ROLE_DESCRIPTION = "role_description";
    private static final String PARAMETER_PAGE_WORKGROUP = "workgroup_key";

    // Templates
    private static final String TEMPLATE_MANAGE_ROLES = "admin/role/manage_roles.html";
    private static final String TEMPLATE_PAGE_ROLE_MODIFY = "admin/role/modify_page_role.html";
    private static final String TEMPLATE_CREATE_PAGE_ROLE = "admin/role/create_page_role.html";

    // Security token key for the removal action
    private static final String TOKEN_REMOVE_ROLE = "DoRemovePageRole.jsp";

    // Views
    private static final String VIEW_MANAGE_ROLES = "managePageRole";
    private static final String VIEW_CREATE_PAGE_ROLE = "createPageRole";
    private static final String VIEW_MODIFY_PAGE_ROLE = "modifyPageRole";
    private static final String VIEW_REMOVE_PAGE_ROLE = "removePageRole";

    // Actions
    private static final String ACTION_CREATE_PAGE_ROLE = "createPageRole";
    private static final String ACTION_MODIFY_PAGE_ROLE = "modifyPageRole";
    private static final String ACTION_REMOVE_PAGE_ROLE = "removePageRole";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_CREATE_ROLE = "portal.role.create_role.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ROLE = "portal.role.modify_role.pageTitle";

    // Message
    private static final String MESSAGE_ROLE_EXIST = "portal.role.message.roleexist";
    private static final String MESSAGE_ROLE_FORMAT = "portal.role.message.roleformat";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.role.message.confirmRemoveRole";
    private static final String MESSAGE_CANNOT_REMOVE_ROLE = "portal.role.message.cannotRemoveRole";

    @Inject
    @Named( BeanUtils.BEAN_ROLE_REMOVAL_SERVICE )
    private RemovalListenerService _removalListenerService;

    @Inject
    private Models _models;

    /**
     * Returns Page Role management form.
     *
     * @param request
     *            The Http request
     * @return Html form
     */
    @View( value = VIEW_MANAGE_ROLES, defaultView = true )
    public String getManagePageRole( HttpServletRequest request )
    {
        setPageTitleProperty( null );

        Collection<Role> listRoles = RoleHome.findAll( );
        User user = getUser( );
        listRoles = AdminWorkgroupService.getAuthorizedCollection( listRoles, user );
        Map<String, Boolean> mapExistRbac = listRoles.stream( ).collect( Collectors.toMap( Role::getRole, x -> RBACRoleHome.checkExistRole( x.getRole( ) ) ) );

        _models.put( MARK_ROLES_LIST, listRoles );
        _models.put( MARK_EXIST_RBAC_MAP, mapExistRbac );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ROLES, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Insert a new PageRole.
     *
     * @param request
     *            The HTTP request
     * @return String The html code page
     */
    @View( VIEW_CREATE_PAGE_ROLE )
    public String getCreatePageRole( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ROLE );

        _models.put( MARK_DEFAULT_VALUE_WORKGROUP_KEY, AdminWorkgroupService.ALL_GROUPS );
        _models.put( MARK_WORKGROUP_KEY_LIST, AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
        _models.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_CREATE_PAGE_ROLE ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PAGE_ROLE, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Create PageRole.
     *
     * @param request
     *            The HTTP request
     * @return String The url page
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_CREATE_PAGE_ROLE )
    public String doCreatePageRole( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        String strPageRoleDescription = request.getParameter( PARAMETER_PAGE_ROLE_DESCRIPTION );
        String strPageWorkgroup = request.getParameter( PARAMETER_PAGE_WORKGROUP );

        if ( StringUtil.isAnyEmpty( strPageRole, strPageRoleDescription ) || ( strPageWorkgroup == null ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        if ( !StringUtil.checkCodeKey( strPageRole ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ROLE_FORMAT, AdminMessage.TYPE_STOP ) );
        }

        if ( RoleHome.findExistRole( strPageRole ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ROLE_EXIST, AdminMessage.TYPE_STOP ) );
        }

        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_CREATE_PAGE_ROLE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        Role role = new Role( );
        role.setRole( strPageRole );
        role.setRoleDescription( strPageRoleDescription );
        role.setWorkgroup( strPageWorkgroup );
        RoleHome.create( role );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Returns the page role modification form.
     *
     * @param request
     *            The HTTP request
     * @return String The html code page
     */
    @View( VIEW_MODIFY_PAGE_ROLE )
    public String getModifyPageRole( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ROLE );

        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );

        Role role = RoleHome.findByPrimaryKey( strPageRole );

        if ( role == null )
        {
            return getManagePageRole( request );
        }

        _models.put( MARK_ROLE, role );
        _models.put( MARK_WORKGROUP_KEY_LIST, AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
        _models.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_PAGE_ROLE_MODIFY ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_ROLE_MODIFY, getLocale( ), _models );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Modify PageRole.
     *
     * @param request
     *            The HTTP request
     * @return String The url page
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_MODIFY_PAGE_ROLE )
    public String doModifyPageRole( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        String strPageRoleDescription = request.getParameter( PARAMETER_PAGE_ROLE_DESCRIPTION );
        String strPageWorkgroup = request.getParameter( PARAMETER_PAGE_WORKGROUP );

        if ( ( strPageRoleDescription == null ) || strPageRoleDescription.equals( "" ) || ( strPageWorkgroup == null ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_PAGE_ROLE_MODIFY ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        Role role = new Role( );
        role.setRole( strPageRole );
        role.setRoleDescription( strPageRoleDescription );
        role.setWorkgroup( strPageWorkgroup );
        RoleHome.update( role );

        return redirect( request, getHomeUrl( request ) );
    }

    /**
     * Confirm Delete PageRole.
     *
     * @param request
     *            The HTTP request
     * @return String The html code page
     */
    @View( VIEW_REMOVE_PAGE_ROLE )
    public String getRemovePageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        if ( StringUtils.isBlank( strPageRole ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object [ ] {
                    PARAMETER_PAGE_ROLE
            }, AdminMessage.TYPE_STOP ) );
        }
        Role role = RoleHome.findByPrimaryKey( strPageRole );
        if ( role == null || !strPageRole.equals( role.getRole( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY, new Object [ ] {
                    strPageRole
            }, AdminMessage.TYPE_STOP ) );
        }
        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( PARAMETER_PAGE_ROLE, request.getParameter( PARAMETER_PAGE_ROLE ) );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, TOKEN_REMOVE_ROLE ) );

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, new Object [ ] {
                strPageRole
        }, null, getActionUrl( ACTION_REMOVE_PAGE_ROLE ), null, AdminMessage.TYPE_CONFIRMATION, parameters ) );
    }

    /**
     * Delete PageRole.
     *
     * @param request
     *            The HTTP request
     * @return String The url page
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( ACTION_REMOVE_PAGE_ROLE )
    public String doRemovePageRole( HttpServletRequest request ) throws AccessDeniedException
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        ArrayList<String> listErrors = new ArrayList<>( );

        if ( !_removalListenerService.checkForRemoval( strPageRole, listErrors, getLocale( ) ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, getLocale( ) );
            Object [ ] args = {
                    strPageRole, strCause
            };

            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_ROLE, args, AdminMessage.TYPE_STOP ) );
        }
        if ( !getSecurityTokenService( ).validate( request, TOKEN_REMOVE_ROLE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        RoleHome.remove( strPageRole );

        return redirect( request, getHomeUrl( request ) );
    }
}
