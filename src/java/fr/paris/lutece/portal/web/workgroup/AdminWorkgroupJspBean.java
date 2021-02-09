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
package fr.paris.lutece.portal.web.workgroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupFilter;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.service.workgroup.WorkgroupRemovalListenerService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * AdminWorkgroup Jsp Bean
 */
public class AdminWorkgroupJspBean extends AdminFeaturesPageJspBean
{
    // Rights
    /**
     * Right to manage workgroups
     */
    public static final String RIGHT_MANAGE_WORKGROUPS = "CORE_WORKGROUPS_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5945178935890410656L;

    // Templates
    private static final String TEMPLATE_MANAGE_WORGROUPS = "admin/workgroup/manage_workgroups.html";
    private static final String TEMPLATE_CREATE_WORKGROUP = "admin/workgroup/create_workgroup.html";
    private static final String TEMPLATE_MODIFY_WORKGROUP = "admin/workgroup/modify_workgroup.html";
    private static final String TEMPLATE_ASSIGN_USERS = "admin/workgroup/assign_users_workgroup.html";

    // Markers Freemarker
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    private static final String MARK_WORKGROUP = "workgroup";
    private static final String MARK_USERS_LIST = "users_list";
    private static final String MARK_ASSIGNED_USERS_LIST = "assigned_users_list";
    private static final String MARK_ASSIGNED_USERS_NUMBER = "assigned_users_number";
    private static final String MARK_SEARCH_IS_SEARCH = "search_is_search";
    private static final String MARK_SEARCH_ADMIN_WORKGROUP_FILTER = "search_admin_workgroup_filter";
    private static final String MARK_USER_LEVELS_LIST = "user_levels";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_SORT_SEARCH_ATTRIBUTE = "sort_search_attribute";

    // Properties
    private static final String PROPERTY_CREATE_WORKGROUP_PAGETITLE = "portal.workgroup.create_workgroup.pageTitle";
    private static final String PROPERTY_MODIFY_WORKGROUP_PAGETITLE = "portal.workgroup.modify_workgroup.pageTitle";
    private static final String PROPERTY_ASSIGN_USERS_PAGETITLE = "portal.workgroup.assign_users.pageTitle";
    private static final String PROPERTY_MANAGE_WORKGROUPS_PAGETITLE = "portal.workgroup.manage_workgroups.pageTitle";
    private static final String PROPERTY_USERS_PER_PAGE = "paginator.user.itemsPerPage";

    // Parameters
    private static final String PARAMETER_WORKGROUP_KEY = "workgroup_key";
    private static final String PARAMETER_WORKGROUP_DESCRIPTION = "workgroup_description";
    private static final String PARAMETER_USERS_LIST = "list_users";
    private static final String PARAMETER_ID_USER = "id_user";
    private static final String PARAMETER_ANCHOR = "anchor";

    // JSP
    private static final String JSP_MANAGE_WORKGROUPS = "ManageWorkgroups.jsp";
    private static final String JSP_ASSIGN_USERS_TO_WORKGROUPS = "AssignUsersWorkgroup.jsp";
    private static final String JSP_URL_REMOVE_WORKGROUP = "jsp/admin/workgroup/DoRemoveWorkgroup.jsp";
    private static final String JSP_URL_ASSIGN_USERS_TO_WORKGROUPS = "jsp/admin/workgroup/AssignUsersWorkgroup.jsp";

    // Messages
    private static final String MESSAGE_WORKGROUP_ALREADY_EXIST = "portal.workgroup.message.workgroupAlreadyExist";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.workgroup.message.confirmRemove";
    private static final String MESSAGE_WORKGROUP_ALREADY_USED = "portal.workgroup.message.workgroupAlreadyUsed";
    private static final String MESSAGE_CANNOT_REMOVE_WORKGROUP = "portal.workgroup.message.cannotRemoveWorkgroup";
    private static final String MESSAGE_WORKGROUP_ACCENTUATED_CHARACTER = "portal.workgroup.message.accentuatedCharacter";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private ItemNavigator _itemNavigator;

    /**
     * Get the workgroups management page. This page provides the list of all existing workgroups.
     * 
     * @param request
     *            the http request
     * @return the html code for the workgroup management page
     */
    public String getManageWorkgroups( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_WORKGROUPS_PAGETITLE );

        // Reinit session
        reinitItemNavigator( );

        // FILTER
        AdminWorkgroupFilter awFilter = new AdminWorkgroupFilter( );
        boolean bIsSearch = awFilter.setAdminWorkgroupFilter( request );

        List<AdminWorkgroup> listFilteredWorkgroups = (List<AdminWorkgroup>) AdminWorkgroupHome.findByFilter( awFilter );

        HashMap<String, Object> model = new HashMap<>( );

        if ( !getUser( ).isAdmin( ) )
        {
            listFilteredWorkgroups = (List<AdminWorkgroup>) AdminWorkgroupService.getAuthorizedCollection( listFilteredWorkgroups, getUser( ) );
        }

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listFilteredWorkgroups, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        String strURL = getHomeUrl( request );
        UrlItem url = new UrlItem( strURL );

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        String strSortSearchAttribute = "";

        if ( bIsSearch )
        {
            awFilter.setUrlAttributes( url );

            if ( !awFilter.getUrlAttributes( ).equals( "" ) )
            {
                strSortSearchAttribute = "&" + awFilter.getUrlAttributes( );
            }
        }

        // PAGINATOR
        LocalizedPaginator<AdminWorkgroup> paginator = new LocalizedPaginator<>( listFilteredWorkgroups, _nItemsPerPage, url.getUrl( ),
                AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_WORKGROUPS_LIST, paginator.getPageItems( ) );
        model.put( MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( MARK_SEARCH_ADMIN_WORKGROUP_FILTER, awFilter );
        model.put( MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_WORGROUPS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Returns the data capture form of a new Workgroup
     *
     * @param request
     *            The HTTP Request
     * @return The HTML form
     */
    public String getCreateWorkgroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_WORKGROUP_PAGETITLE );

        Map<String, Object> model = new HashMap<>( 1 );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_WORKGROUP ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_WORKGROUP, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the data capture form of a new workgroup
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doCreateWorkgroup( HttpServletRequest request ) throws AccessDeniedException
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
        if ( Boolean.TRUE.equals( AdminWorkgroupHome.checkExistWorkgroup( strKey ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WORKGROUP_ALREADY_EXIST, AdminMessage.TYPE_STOP );
        }

        // Check if strKey contains accentuated caracters
        if ( !StringUtil.checkCodeKey( strKey ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WORKGROUP_ACCENTUATED_CHARACTER, AdminMessage.TYPE_STOP );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_CREATE_WORKGROUP ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        AdminWorkgroup adminWorkgroup = new AdminWorkgroup( );
        adminWorkgroup.setKey( strKey.trim( ) );
        adminWorkgroup.setDescription( strDescription );
        AdminWorkgroupHome.create( adminWorkgroup );
        AdminWorkgroupHome.addUserForWorkgroup( getUser( ), strKey );

        return JSP_MANAGE_WORKGROUPS;
    }

    /**
     * Returns the page of confirmation for deleting a workgroup
     *
     * @param request
     *            The Http Request
     * @return the confirmation url
     */
    public String getConfirmRemoveWorkgroup( HttpServletRequest request )
    {
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strUrlRemove = JSP_URL_REMOVE_WORKGROUP;
        Map<String, String> parameters = new HashMap<>( );
        parameters.put( PARAMETER_WORKGROUP_KEY, strWorkgroupKey );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, JSP_URL_REMOVE_WORKGROUP ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, strUrlRemove, AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Process to the confirmation of deleting a workgroup
     *
     * @param request
     *            The Http Request
     * @return the HTML page
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemoveWorkgroup( HttpServletRequest request ) throws AccessDeniedException
    {
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        ArrayList<String> listErrors = new ArrayList<>( );

        if ( CollectionUtils.isNotEmpty( AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroupKey ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_WORKGROUP_ALREADY_USED, AdminMessage.TYPE_STOP );
        }

        if ( !WorkgroupRemovalListenerService.getService( ).checkForRemoval( strWorkgroupKey, listErrors, getLocale( ) ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, getLocale( ) );
            Object [ ] args = {
                    strCause
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE_WORKGROUP, args, AdminMessage.TYPE_STOP );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_REMOVE_WORKGROUP ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        AdminWorkgroupHome.remove( strWorkgroupKey );

        return JSP_MANAGE_WORKGROUPS;
    }

    /**
     * Returns the form to update info about a Workgroup
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    public String getModifyWorkgroup( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_WORKGROUP_PAGETITLE );

        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );

        AdminWorkgroup workgroup = AdminWorkgroupHome.findByPrimaryKey( strWorkgroupKey );

        if ( workgroup == null )
        {
            return getManageWorkgroups( request );
        }

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_WORKGROUP, workgroup );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_WORKGROUP ) );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_WORKGROUP, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the data capture form for modify a workgroup
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyWorkgroup( HttpServletRequest request ) throws AccessDeniedException
    {
        String strWorgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strDescription = request.getParameter( PARAMETER_WORKGROUP_DESCRIPTION );

        if ( StringUtils.isEmpty( strDescription ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MODIFY_WORKGROUP ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        AdminWorkgroup adminWorkgroup = new AdminWorkgroup( );
        adminWorkgroup.setKey( strWorgroupKey );
        adminWorkgroup.setDescription( strDescription );
        AdminWorkgroupHome.update( adminWorkgroup );

        return JSP_MANAGE_WORKGROUPS;
    }

    /**
     * Returns the users assignation form
     *
     * @param request
     *            The Http request
     * @return the html code for display the modes list
     */
    public String getAssignUsers( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        setPageTitleProperty( PROPERTY_ASSIGN_USERS_PAGETITLE );

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_USERS_TO_WORKGROUPS;
        UrlItem url = new UrlItem( strBaseUrl );

        // WORKGROUP
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        AdminWorkgroup adminWorkgroup = AdminWorkgroupHome.findByPrimaryKey( strWorkgroupKey );

        if ( adminWorkgroup == null )
        {
            return getManageWorkgroups( request );
        }

        // ASSIGNED USERS
        // Add users with higher level then connected user or add all users if connected
        // user is administrator
        List<AdminUser> listAssignedUsers = AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroupKey ).stream( )
                .filter( this::isUserHigherThanConnectedUser ).collect( Collectors.toList( ) );

        List<AdminUser> listFilteredUsers = AdminUserService.getFilteredUsersInterface( listAssignedUsers, request, model, url );

        // AVAILABLE USERS
        ReferenceList listUsers = new ReferenceList( );

        for ( AdminUser user : AdminUserHome.findUserList( ) )
        {
            final ReferenceItem itemUser = new ReferenceItem( );
            itemUser.setCode( Integer.toString( user.getUserId( ) ) );
            itemUser.setName( user.getLastName( ) + " " + user.getFirstName( ) + " (" + user.getAccessCode( ) + ")" );

            boolean bAssigned = listAssignedUsers.stream( )
                    .anyMatch( assignedUser -> Integer.toString( assignedUser.getUserId( ) ).equals( itemUser.getCode( ) ) );

            // Add users with higher level then connected user or add all users if connected
            // user is administrator
            if ( !bAssigned && isUserHigherThanConnectedUser( user ) )
            {
                listUsers.add( itemUser );
            }
        }

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );

            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listFilteredUsers, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        // ITEM NAVIGATION
        setItemNavigator( strWorkgroupKey, url.getUrl( ) );

        // PAGINATOR
        url.addParameter( PARAMETER_WORKGROUP_KEY, adminWorkgroup.getKey( ) );

        LocalizedPaginator<AdminUser> paginator = new LocalizedPaginator<>( listFilteredUsers, _nItemsPerPage, url.getUrl( ),
                AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        // USER LEVEL
        Collection<Level> filteredLevels = new ArrayList<>( );

        for ( Level level : LevelHome.getLevelsList( ) )
        {
            if ( getUser( ).isAdmin( ) || getUser( ).hasRights( level.getId( ) ) )
            {
                filteredLevels.add( level );
            }
        }

        model.put( MARK_WORKGROUP, adminWorkgroup );
        model.put( MARK_USERS_LIST, listUsers );
        model.put( MARK_ASSIGNED_USERS_LIST, paginator.getPageItems( ) );
        model.put( MARK_ASSIGNED_USERS_NUMBER, listAssignedUsers.size( ) );
        model.put( MARK_USER_LEVELS_LIST, filteredLevels );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( _nItemsPerPage ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_ASSIGN_USERS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_USERS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the data capture form for assign users to a workgroup
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doAssignUsers( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_ASSIGN_USERS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );

        // retrieve the selected portlets ids
        String [ ] arrayUsersIds = request.getParameterValues( PARAMETER_USERS_LIST );

        if ( ( arrayUsersIds != null ) )
        {
            for ( int i = 0; i < arrayUsersIds.length; i++ )
            {
                int nUserId = Integer.parseInt( arrayUsersIds [i] );
                AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

                if ( !AdminWorkgroupHome.isUserInWorkgroup( user, strWorkgroupKey ) )
                {
                    AdminWorkgroupHome.addUserForWorkgroup( user, strWorkgroupKey );
                }
            }
        }

        return JSP_ASSIGN_USERS_TO_WORKGROUPS + "?" + PARAMETER_WORKGROUP_KEY + "=" + strWorkgroupKey;
    }

    /**
     * unassigns user from workgroup
     * 
     * @param request
     *            The HttpRequest
     * @return the HTML code of list assignations
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doUnAssignUser( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_ASSIGN_USERS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );
        int nIdUser = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
        String strAnchor = request.getParameter( PARAMETER_ANCHOR );

        AdminUser adminUser = AdminUserHome.findByPrimaryKey( nIdUser );

        if ( adminUser != null )
        {
            AdminWorkgroupHome.removeUserFromWorkgroup( adminUser, strWorkgroupKey );
        }

        return JSP_ASSIGN_USERS_TO_WORKGROUPS + "?" + PARAMETER_WORKGROUP_KEY + "=" + strWorkgroupKey + "#" + strAnchor;
    }

    /**
     * Get the item navigator
     * 
     * @param strWorkgroupKey
     *            the workgroup key
     * @param strUrl
     *            the url
     */
    private void setItemNavigator( String strWorkgroupKey, String strUrl )
    {
        if ( _itemNavigator == null )
        {
            List<String> listWorkgroupKeys = new ArrayList<>( );
            int nCurrentItemId = 0;
            int nIndex = 0;

            for ( AdminWorkgroup workgroup : AdminWorkgroupHome.findAll( ) )
            {
                if ( ( workgroup != null ) && StringUtils.isNotBlank( workgroup.getKey( ) ) )
                {
                    listWorkgroupKeys.add( workgroup.getKey( ) );

                    if ( workgroup.getKey( ).equals( strWorkgroupKey ) )
                    {
                        nCurrentItemId = nIndex;
                    }

                    nIndex++;
                }
            }

            _itemNavigator = new ItemNavigator( listWorkgroupKeys, nCurrentItemId, strUrl, PARAMETER_WORKGROUP_KEY );
        }
        else
        {
            _itemNavigator.setCurrentItemId( strWorkgroupKey );
        }
    }

    /**
     * Reinit the item navigator
     */
    private void reinitItemNavigator( )
    {
        _itemNavigator = null;
    }
}
