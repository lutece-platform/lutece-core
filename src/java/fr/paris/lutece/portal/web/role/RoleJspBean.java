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
package fr.paris.lutece.portal.web.role;

import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.role.RoleRemovalListenerService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * JspBean for Role management
 */
public class RoleJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////////
    // Constant

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

    // Jsp
    private static final String PATH_JSP = "jsp/admin/role/";
    private static final String JSP_REMOVE_ROLE = "DoRemovePageRole.jsp";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_CREATE_ROLE = "portal.role.create_role.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ROLE = "portal.role.modify_role.pageTitle";

    // Message
    private static final String MESSAGE_ROLE_EXIST = "portal.role.message.roleexist";
    private static final String MESSAGE_ROLE_FORMAT = "portal.role.message.roleformat";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.role.message.confirmRemoveRole";
    private static final String MESSAGE_CANNOT_REMOVE_ROLE = "portal.role.message.cannotRemoveRole";

    /**
     * Creates a new RoleJspBean object.
     */
    public RoleJspBean(  )
    {
    }

    /**
     * Returns Page Role management form
     * @param request The Http request
     * @return Html form
     */
    public String getManagePageRole( HttpServletRequest request )
    {
        setPageTitleProperty( null );

        Map<String, Object> model = new HashMap<String, Object>(  );
        Collection<Role> listRoles = RoleHome.findAll(  );
        listRoles = AdminWorkgroupService.getAuthorizedCollection( listRoles, getUser(  ) );
        model.put( MARK_ROLES_LIST, listRoles );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ROLES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Insert a new PageRole
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getCreatePageRole( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ROLE );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_DEFAULT_VALUE_WORKGROUP_KEY, AdminWorkgroupService.ALL_GROUPS );
        model.put( MARK_WORKGROUP_KEY_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PAGE_ROLE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Create PageRole
     * @param request The HTTP request
     * @return String The url page
     */
    public String doCreatePageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        String strPageRoleDescription = request.getParameter( PARAMETER_PAGE_ROLE_DESCRIPTION );
        String strPageWorkgroup = request.getParameter( PARAMETER_PAGE_WORKGROUP );

        // Mandatory field
        if ( ( strPageRole == null ) || strPageRole.equals( "" ) || ( strPageRoleDescription == null ) ||
                strPageRoleDescription.equals( "" ) || ( strPageWorkgroup == null ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check if code is valid
        if ( !StringUtil.checkCodeKey( strPageRole ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ROLE_FORMAT, AdminMessage.TYPE_STOP );
        }

        // Check if role exist
        if ( RoleHome.findExistRole( strPageRole ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ROLE_EXIST, AdminMessage.TYPE_STOP );
        }

        Role role = new Role(  );
        role.setRole( strPageRole );
        role.setRoleDescription( strPageRoleDescription );
        role.setWorkgroup( strPageWorkgroup );
        RoleHome.create( role );

        return getHomeUrl( request );
    }

    /**
     *
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getModifyPageRole( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ROLE );

        Map<String, Object> model = new HashMap<String, Object>(  );

        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );

        Role role = RoleHome.findByPrimaryKey( strPageRole );

        if ( role == null )
        {
            return getManagePageRole( request );
        }

        model.put( MARK_ROLE, role );
        model.put( MARK_WORKGROUP_KEY_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_ROLE_MODIFY, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify PageRole
     * @param request The HTTP request
     * @return String The url page
     */
    public String doModifyPageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        String strPageRoleDescription = request.getParameter( PARAMETER_PAGE_ROLE_DESCRIPTION );
        String strPageWorkgroup = request.getParameter( PARAMETER_PAGE_WORKGROUP );

        // Mandatory field
        if ( ( strPageRoleDescription == null ) || strPageRoleDescription.equals( "" ) || ( strPageWorkgroup == null ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Role role = new Role(  );
        role.setRole( strPageRole );
        role.setRoleDescription( strPageRoleDescription );
        role.setWorkgroup( strPageWorkgroup );
        RoleHome.update( role );

        return getHomeUrl( request );
    }

    /**
     * confirm Delete PageRole
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getRemovePageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        if ( StringUtils.isBlank( strPageRole ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY,
                    new Object[] { PARAMETER_PAGE_ROLE }, AdminMessage.TYPE_STOP );
        }
        Role role = RoleHome.findByPrimaryKey( strPageRole );
        if ( role == null || !strPageRole.equals( role.getRole( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MESSAGE_INVALID_ENTRY,
                    new Object[] { strPageRole }, AdminMessage.TYPE_STOP );
        }
        UrlItem url = new UrlItem( PATH_JSP + JSP_REMOVE_ROLE + "?role=" + request.getParameter( PARAMETER_PAGE_ROLE ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, new Object[] { strPageRole }, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete PageRole
     * @param request The HTTP request
     * @return String The url page
     */
    public String doRemovePageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        ArrayList<String> listErrors = new ArrayList<String>(  );

        if ( !RoleRemovalListenerService.getService(  ).checkForRemoval( strPageRole, listErrors, getLocale(  ) ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, getLocale(  ) );
            Object[] args = { strPageRole, strCause };

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_ROLE, args, AdminMessage.TYPE_STOP );
        }

        RoleHome.remove( strPageRole );

        return getHomeUrl( request );
    }
}
