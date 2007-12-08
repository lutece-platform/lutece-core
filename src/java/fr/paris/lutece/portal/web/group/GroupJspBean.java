/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
package fr.paris.lutece.portal.web.group;

import fr.paris.lutece.portal.business.group.Group;
import fr.paris.lutece.portal.business.group.GroupHome;
import fr.paris.lutece.portal.business.group.GroupRoleHome;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Lutece group features ( manage, create, modify, remove )
 */
public class GroupJspBean extends AdminFeaturesPageJspBean
{
    //Constants
    private static final String MANAGE_GROUPS = "ManageGroups.jsp";

    // JSP
    private static final String JSP_DO_REMOVE_GROUP = "jsp/admin/group/DoRemoveGroup.jsp";

    //Markers
    private static final String MARK_GROUPS_LIST = "groups_list";
    private static final String MARK_GROUP = "group";
    private static final String MARK_ROLES_LIST = "role_list";
    private static final String MARK_ROLES_LIST_FOR_GROUP = "group_role_list";

    // Parameters
    private static final String PARAMETER_GROUP_KEY = "group_key";
    private static final String PARAMETER_GROUP_DESCRIPTION = "group_description";
    private static final String PARAMETER_ROLE_KEY = "role_key";

    // Templates
    private static final String TEMPLATE_MANAGE_GROUPS = "admin/group/manage_groups.html";
    private static final String TEMPLATE_CREATE_GROUP = "admin/group/create_group.html";
    private static final String TEMPLATE_GROUP_MODIFY = "admin/group/modify_group.html";
    private static final String TEMPLATE_MANAGE_ROLES_GROUP = "admin/group/manage_roles_group.html";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_CREATE_GROUP = "portal.group.create_group.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_GROUP = "portal.group.modify_group.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ROLES_GROUP = "portal.group.manage_roles_group.pageTitle";

    // Message
    private static final String MESSAGE_GROUP_EXIST = "portal.group.message.groupExist";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.group.message.confirmRemoveGroup";
    private static final String MESSAGE_ERROR_MODIFY = "portal.group.message.errorModifyGroup";
    private static final String MESSAGE_ERROR_REMOVE = "portal.group.message.errorRemoveGroup";
    private static final String MESSAGE_ERROR_MANAGE_GROUPS = "portal.group.message.errorManageGroups";

    // Right
    public final String RIGHT_GROUPS_MANAGEMENT = "CORE_GROUPS_MANAGEMENT";

    /**
     * Creates a new GroupJspBean object.
     */
    public GroupJspBean(  )
    {
    }

    /**
     * Returns Group management form
     * @param request The Http request
     * @return Html form
     */
    public String getManageGroups( HttpServletRequest request )
    {
        setPageTitleProperty( null );

        HashMap<String, Collection<Group>> model = new HashMap<String, Collection<Group>>(  );
        model.put( MARK_GROUPS_LIST, GroupHome.findAll(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_GROUPS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Insert a new group
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getCreateGroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_GROUP );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_GROUP, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Create Group
     * @param request The HTTP request
     * @return String The url page
     */
    public String doCreateGroup( HttpServletRequest request )
    {
        String strGroupKey = request.getParameter( PARAMETER_GROUP_KEY );
        String strGroupDescription = request.getParameter( PARAMETER_GROUP_DESCRIPTION );

        // Mandatory field
        if ( strGroupKey.length(  ) == 0 )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // check if group exist
        if ( GroupHome.findByPrimaryKey( strGroupKey ) != null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_GROUP_EXIST, AdminMessage.TYPE_STOP );
        }

        Group group = new Group(  );
        group.setGroupKey( strGroupKey );
        group.setGroupDescription( strGroupDescription );
        GroupHome.create( group );

        return getHomeUrl( request );
    }

    /**
    *
    * @param request The HTTP request
    * @return String The html code page
    */
    public String getModifyGroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_GROUP );

        HashMap<String, Group> model = new HashMap<String, Group>(  );

        Group group = getGroupFromRequest( request );

        if ( group == null )
        {
            return getCreateGroup( request );
        }

        model.put( MARK_GROUP, group );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_GROUP_MODIFY, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify group
     * @param request The HTTP request
     * @return String The url page
     */
    public String doModifyGroup( HttpServletRequest request )
    {
        String strGroupKey = request.getParameter( PARAMETER_GROUP_KEY );
        String strGroupDescription = request.getParameter( PARAMETER_GROUP_DESCRIPTION );

        Group group = getGroupFromRequest( request );

        if ( group == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_MODIFY, AdminMessage.TYPE_ERROR );
        }

        // Mandatory field
        if ( strGroupKey.length(  ) == 0 )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        group.setGroupKey( strGroupKey );
        group.setGroupDescription( strGroupDescription );
        GroupHome.update( group );

        return getHomeUrl( request );
    }

    /**
     * confirm Delete Group
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getRemoveGroup( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_GROUP );
        url.addParameter( PARAMETER_GROUP_KEY, request.getParameter( PARAMETER_GROUP_KEY ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete Group
     * @param request The HTTP request
     * @return String The url page
     */
    public String doRemoveGroup( HttpServletRequest request )
    {
        Group group = getGroupFromRequest( request );

        if ( group == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REMOVE, AdminMessage.TYPE_ERROR );
        }

        GroupHome.remove( group.getGroupKey(  ) );

        return getHomeUrl( request );
    }

    /**
     * Returns roles management form for a specified group
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageRolesGroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_ROLES_GROUP );

        Group group = getGroupFromRequest( request );

        if ( group == null )
        {
            return getManageGroups( request );
        }

        Collection<Role> allRoleList = RoleHome.findAll(  );
        List<String> groupRoleKeyList = GroupRoleHome.findGroupRoles( group.getGroupKey(  ) );
        Collection<Role> groupRoleList = new ArrayList<Role>(  );

        for ( String strRoleKey : groupRoleKeyList )
        {
            for ( Role role : allRoleList )
            {
                if ( role.getRole(  ).equals( strRoleKey ) )
                {
                    groupRoleList.add( RoleHome.findByPrimaryKey( strRoleKey ) );
                }
            }
        }

        HashMap model = new HashMap(  );
        model.put( MARK_ROLES_LIST, allRoleList );
        model.put( MARK_ROLES_LIST_FOR_GROUP, groupRoleList );
        model.put( MARK_GROUP, group );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ROLES_GROUP, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process assignation roles for a specified group
     *
     * @param request The Http request
     * @return Html form
     */
    public String doAssignRoleGroup( HttpServletRequest request )
    {
        //get group
        Group group = getGroupFromRequest( request );

        if ( group == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_MANAGE_GROUPS, AdminMessage.TYPE_ERROR );
        }

        String[] roleArray = request.getParameterValues( PARAMETER_ROLE_KEY );

        GroupRoleHome.removeRoles( group.getGroupKey(  ) );

        if ( roleArray != null )
        {
            for ( int i = 0; i < roleArray.length; i++ )
            {
                GroupRoleHome.addRole( group.getGroupKey(  ), roleArray[i] );
            }
        }

        return MANAGE_GROUPS;
    }

    /**
    *
    * @param request The HTTP request
    * @return the group
    */
    private Group getGroupFromRequest( HttpServletRequest request )
    {
        String strGroupKey = request.getParameter( PARAMETER_GROUP_KEY );

        if ( ( strGroupKey == null ) || ( strGroupKey.length(  ) == 0 ) )
        {
            return null;
        }

        Group group = GroupHome.findByPrimaryKey( strGroupKey );

        return group;
    }
}
