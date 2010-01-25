/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.web.workgroup;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.service.workgroup.WorkgroupRemovalListenerService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *  AdminWorkgroup Jsp Bean
 */
public class AdminWorkgroupJspBean extends AdminFeaturesPageJspBean
{
    //Rights
    public static final String RIGHT_MANAGE_WORKGROUPS = "CORE_WORKGROUPS_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_MANAGE_WORGROUPS = "admin/workgroup/manage_workgroups.html";
    private static final String TEMPLATE_CREATE_WORKGROUP = "admin/workgroup/create_workgroup.html";
    private static final String TEMPLATE_MODIFY_WORKGROUP = "admin/workgroup/modify_workgroup.html";
    private static final String TEMPLATE_ASSIGN_USERS = "admin/workgroup/assign_users_workgroup.html";

    // Markers  Freemarker
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    private static final String MARK_WORKGROUP = "workgroup";
    private static final String MARK_USERS_LIST = "users_list";
    private static final String MARK_ASSIGNED_USERS_LIST = "assigned_users_list";
    private static final String MARK_ASSIGNED_USERS_NUMBER = "assigned_users_number";

    // Properties
    private static final String PROPERTY_CREATE_WORKGROUP_PAGETITLE = "portal.workgroup.create_workgroup.pageTitle";
    private static final String PROPERTY_MODIFY_WORKGROUP_PAGETITLE = "portal.workgroup.modify_workgroup.pageTitle";
    private static final String PROPERTY_ASSIGN_USERS_PAGE_TITLE = "portal.workgroup.assign_users.pageTitle";
    private static final String PROPERTY_MANAGE_WORKGROUPS_PAGETITLE = "portal.workgroup.manage_workgroups.pageTitle";

    // Parameters
    private static final String PARAMETER_WORKGROUP_KEY = "workgroup_key";
    private static final String PARAMETER_WORKGROUP_DESCRIPTION = "workgroup_description";
    private static final String PARAMETER_USERS_LIST = "list_users";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_ID_USER = "id_user";

    // JSP
    private static final String JSP_MANAGE_WORKGROUPS = "ManageWorkgroups.jsp";
    private static final String JSP_ASSIGN_USERS_TO_WORKGROUPS = "AssignUsersWorkgroup.jsp";
    private static final String JSP_URL_REMOVE_WORKGROUP = "jsp/admin/workgroup/DoRemoveWorkgroup.jsp";

    // Messages
    private static final String MESSAGE_WORKGROUP_ALREADY_EXIST = "portal.workgroup.message.workgroupAlreadyExist";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.workgroup.message.confirmRemove";
    private static final String MESSAGE_WORKGROUP_ALREADY_USED = "portal.workgroup.message.workgroupAlreadyUsed";
    private static final String MESSAGE_CANNOT_REMOVE_WORKGROUP = "portal.workgroup.message.cannotRemoveWorkgroup";
    private static final String MESSAGE_WORKGROUP_ACCENTUATED_CHARACTER = "portal.workgroup.message.accentuatedCharacter";

    /**
     * Get the workgroups management page.
     * This page provides the list of all existing workgroups.
     * @param request the http request
     * @return the html code for the workgroup management page
     */
    public String getManageWorkgroups( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_WORKGROUPS_PAGETITLE );

        HashMap<String, Collection<AdminWorkgroup>> model = new HashMap<String, Collection<AdminWorkgroup>>(  );
        Collection<AdminWorkgroup> listWorkGroups = AdminWorkgroupHome.findAll(  );

        if ( !getUser(  ).isAdmin(  ) )
        {
            listWorkGroups = AdminWorkgroupService.getAuthorizedCollection( (Collection<?extends AdminWorkgroupResource>) listWorkGroups,
                    getUser(  ) );
        }

        model.put( MARK_WORKGROUPS_LIST, listWorkGroups );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_WORGROUPS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the data capture form of a new Workgroup
     *
     * @param request The HTTP Request
     * @return The HTML form
     */
    public String getCreateWorkgroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_WORKGROUP_PAGETITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_WORKGROUP, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new workgroup
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doCreateWorkgroup( HttpServletRequest request )
    {
        String strKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strDescription = request.getParameter( PARAMETER_WORKGROUP_DESCRIPTION );

        if ( ( strKey == null ) || ( strKey.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strDescription == null ) || ( strDescription.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check if workgroup already exist
        if ( AdminWorkgroupHome.checkExistWorkgroup( strKey ) == Boolean.TRUE )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WORKGROUP_ALREADY_EXIST, AdminMessage.TYPE_STOP );
        }

        // Check if strKey contains accentuated caracters
        if( !StringUtil.checkCodeKey( strKey ) )
        {
        	return AdminMessageService.getMessageUrl( request, MESSAGE_WORKGROUP_ACCENTUATED_CHARACTER, AdminMessage.TYPE_STOP );
        }
        
        AdminWorkgroup adminWorkgroup = new AdminWorkgroup(  );
        adminWorkgroup.setKey( strKey.trim(  ) );
        adminWorkgroup.setDescription( strDescription );
        AdminWorkgroupHome.create( adminWorkgroup );
        AdminWorkgroupHome.addUserForWorkgroup( getUser(  ), strKey );

        return JSP_MANAGE_WORKGROUPS;
    }

    /**
     * Returns the page of confirmation for deleting a workgroup
     *
     * @param request The Http Request
     * @return the confirmation url
     */
    public String getConfirmRemoveWorkgroup( HttpServletRequest request )
    {
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strUrlRemove = JSP_URL_REMOVE_WORKGROUP + "?" + PARAMETER_WORKGROUP_KEY + "=" + strWorkgroupKey;

        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, strUrlRemove,
                AdminMessage.TYPE_CONFIRMATION );

        return strUrl;
    }

    /**
     * Process to the confirmation of deleting a workgroup
     *
     * @param request The Http Request
     * @return the HTML page
     */
    public String doRemoveWorkgroup( HttpServletRequest request )
    {
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        ArrayList<String> listErrors = new ArrayList<String>(  );

        if ( AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroupKey ).size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WORKGROUP_ALREADY_USED, AdminMessage.TYPE_STOP );
        }

        if ( !WorkgroupRemovalListenerService.getService(  ).checkForRemoval( strWorkgroupKey, listErrors, getLocale(  ) ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, getLocale(  ) );
            Object[] args = { strCause };

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_WORKGROUP, args,
                AdminMessage.TYPE_STOP );
        }
        else
        {
            AdminWorkgroupHome.remove( strWorkgroupKey );
        }

        return JSP_MANAGE_WORKGROUPS;
    }

    /**
     * Returns the form to update info about a Workgroup
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyWorkgroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_WORKGROUP_PAGETITLE );

        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );

        HashMap<String, AdminWorkgroup> model = new HashMap<String, AdminWorkgroup>(  );
        HtmlTemplate template;

        AdminWorkgroup workgroup = null;

        workgroup = AdminWorkgroupHome.findByPrimaryKey( strWorkgroupKey );
        model.put( MARK_WORKGROUP, workgroup );
        template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_WORKGROUP, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for modify a workgroup
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doModifyWorkgroup( HttpServletRequest request )
    {
        String strWorgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strDescription = request.getParameter( PARAMETER_WORKGROUP_DESCRIPTION );

        if ( ( strDescription == null ) || ( strDescription.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        AdminWorkgroup adminWorkgroup = new AdminWorkgroup(  );
        adminWorkgroup.setKey( strWorgroupKey );
        adminWorkgroup.setDescription( strDescription );
        AdminWorkgroupHome.update( adminWorkgroup );

        return JSP_MANAGE_WORKGROUPS;
    }

    /**
     * Returns the users assignation form
     *
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getAssignUsers( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        setPageTitleProperty( PROPERTY_ASSIGN_USERS_PAGE_TITLE );

        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );

        AdminWorkgroup adminWorkgroup = AdminWorkgroupHome.findByPrimaryKey( strWorkgroupKey );
        List<AdminUser> listAssignedUsers = new ArrayList<AdminUser>(  );

        for ( AdminUser user : AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroupKey ) )
        {
            //Add users with higher level then connected user or add all users if connected user is administrator
            if ( ( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) )
            {
                listAssignedUsers.add( user );
            }
        }

        ReferenceList listUsers = new ReferenceList(  );
        ReferenceItem itemUser = null;
        boolean bAssigned;

        for ( AdminUser user : AdminUserHome.findUserList(  ) )
        {
            itemUser = new ReferenceItem(  );
            itemUser.setCode( Integer.toString( user.getUserId(  ) ) );
            itemUser.setName( user.getAccessCode(  ) + "(" + user.getFirstName(  ) + " " + user.getLastName(  ) + ")" );
            bAssigned = Boolean.FALSE;

            for ( AdminUser assignedUser : listAssignedUsers )
            {
                if ( Integer.toString( assignedUser.getUserId(  ) ).equals( itemUser.getCode(  ) ) )
                {
                    bAssigned = Boolean.TRUE;
                }
            }

            //Add users with higher level then connected user or add all users if connected user is administrator
            if ( !bAssigned &&
                    ( ( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) ) )
            {
                listUsers.add( itemUser );
            }
        }

        model.put( MARK_WORKGROUP, adminWorkgroup );
        model.put( MARK_USERS_LIST, listUsers );
        model.put( MARK_ASSIGNED_USERS_LIST, listAssignedUsers );
        model.put( MARK_ASSIGNED_USERS_NUMBER, listAssignedUsers.size(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign users to a workgroup
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doAssignUsers( HttpServletRequest request )
    {
        String strReturn;

        String strActionCancel = request.getParameter( PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_WORKGROUPS;
        }
        else
        {
            String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
            Collection<AdminUser> listAdminUsers = AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroupKey );

            //retrieve the selected portlets ids
            String[] arrayUsersIds = request.getParameterValues( PARAMETER_USERS_LIST );

            if ( ( arrayUsersIds != null ) )
            {
                for ( int i = 0; i < arrayUsersIds.length; i++ )
                {
                    int nUserId = Integer.parseInt( arrayUsersIds[i] );
                    AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

                    if ( !AdminWorkgroupHome.isUserInWorkgroup( user, strWorkgroupKey ) )
                    {
                        AdminWorkgroupHome.addUserForWorkgroup( user, strWorkgroupKey );
                    }
                }
            }

            // Add user with high or equal level  or  do not add users if connected user is administrator
            for ( AdminUser user : listAdminUsers )
            {
                if ( ( user.getUserLevel(  ) <= getUser(  ).getUserLevel(  ) ) && ( !getUser(  ).isAdmin(  ) ) )
                {
                    AdminWorkgroupHome.addUserForWorkgroup( user, strWorkgroupKey );
                }
            }

            strReturn = JSP_ASSIGN_USERS_TO_WORKGROUPS + "?" + PARAMETER_WORKGROUP_KEY + "=" + strWorkgroupKey;
        }

        return strReturn;
    }

    /**
     * unassigns user from workgroup
     * @param request The HttpRequest
     * @return the HTML code of list assignations
     */
    public String doUnAssignUser( HttpServletRequest request )
    {
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        int nIdUser = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );

        AdminUser adminUser = AdminUserHome.findByPrimaryKey( nIdUser );

        if ( adminUser != null )
        {
            AdminWorkgroupHome.removeUserFromWorkgroup( adminUser, strWorkgroupKey );
        }

        return JSP_ASSIGN_USERS_TO_WORKGROUPS + "?" + PARAMETER_WORKGROUP_KEY + "=" + strWorkgroupKey;
    }
}
