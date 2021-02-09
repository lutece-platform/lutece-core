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
package fr.paris.lutece.portal.web.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage rights features ( manage, create, modify )
 */
public class RightJspBean extends AdminFeaturesPageJspBean
{
    private static final long serialVersionUID = 8074788265698162631L;

    // Right
    public static final String RIGHT_MANAGE_RIGHTS = "CORE_RIGHT_MANAGEMENT";

    // Properties
    private static final String PROPERTY_MANAGE_RIGHTS_PAGETITLE = "portal.features.manage_rights.pageTitle";
    private static final String PROPERTY_ASSIGN_USERS_PAGETITLE = "portal.features.assign_users.pageTitle";
    private static final String PROPERTY_USERS_PER_PAGE = "paginator.user.itemsPerPage";

    // Markers
    private static final String MARK_RIGHTS_LIST = "rights_list";
    private static final String MARK_RIGHT = "right";
    private static final String MARK_AVAILABLE_USERS_LIST = "available_users_list";
    private static final String MARK_ASSIGNED_USERS_LIST = "assigned_users_list";
    private static final String MARK_ASSIGNED_USERS_NUMBER = "assigned_users_number";
    private static final String MARK_USER_LEVELS_LIST = "user_levels";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // Parameters
    private static final String PARAMETER_ID_RIGHT = "id_right";
    private static final String PARAMETER_AVAILABLE_USER_LIST = "available_users_list";
    private static final String PARAMETER_ID_USER = "id_user";
    private static final String PARAMETER_ANCHOR = "anchor";

    // Templates files path
    private static final String TEMPLATE_MANAGE_RIGHTS = "admin/features/manage_rights.html";
    private static final String TEMPLATE_ASSIGN_USERS = "admin/features/assign_users_right.html";

    // JSP
    private static final String JSP_URL_ASSIGN_USERS_TO_RIGHT = "jsp/admin/features/AssignUsersRight.jsp";
    private static final String JSP_ASSIGN_USERS_TO_RIGHT = "AssignUsersRight.jsp";
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;
    private ItemNavigator _itemNavigator;

    /**
     * Returns the list of rights
     *
     * @param request
     *            The Http request
     * @return the html code for display the rights list
     */
    public String getManageRights( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_RIGHTS_PAGETITLE );

        // Reinit session
        reinitItemNavigator( );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_RIGHTS_LIST, I18nService.localizeCollection( RightHome.getRightsList( ), getLocale( ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RIGHTS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
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

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_USERS_TO_RIGHT;
        UrlItem url = new UrlItem( strBaseUrl );

        // RIGHT
        String strIdRight = request.getParameter( PARAMETER_ID_RIGHT );
        Right right = RightHome.findByPrimaryKey( strIdRight );

        if ( right == null )
        {
            return getManageRights( request );
        }

        right.setLocale( getLocale( ) );

        // ASSIGNED USERS
        // Add users with higher level then connected user or add all users if connected
        // user is administrator
        List<AdminUser> listAssignedUsers = AdminUserHome.findByRight( strIdRight ).stream( ).filter( this::isUserHigherThanConnectedUser )
                .collect( Collectors.toList( ) );

        List<AdminUser> listFilteredUsers = AdminUserService.getFilteredUsersInterface( listAssignedUsers, request, model, url );

        // AVAILABLE USERS
        ReferenceList listAvailableUsers = new ReferenceList( );

        for ( AdminUser user : AdminUserHome.findUserList( ) )
        {
            final ReferenceItem itemUser = new ReferenceItem( );
            itemUser.setCode( Integer.toString( user.getUserId( ) ) );
            itemUser.setName( user.getAccessCode( ) + "(" + user.getFirstName( ) + " " + user.getLastName( ) + ")" );

            boolean bAssigned = listAssignedUsers.stream( )
                    .anyMatch( assignedUser -> Integer.toString( assignedUser.getUserId( ) ).equals( itemUser.getCode( ) ) );

            // Add users with higher level then connected user or add all users if connected
            // user is administrator
            if ( !bAssigned && isUserHigherThanConnectedUser( user ) && ( user.getUserLevel( ) <= right.getLevel( ) ) )
            {
                listAvailableUsers.add( itemUser );
            }
        }

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listFilteredUsers, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        int defaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, defaultItemsPerPage );

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        // ITEM NAVITATOR
        setItemNavigator( strIdRight, url.getUrl( ) );

        // PAGINATOR
        url.addParameter( PARAMETER_ID_RIGHT, right.getId( ) );

        LocalizedPaginator<AdminUser> paginator = new LocalizedPaginator<>( listFilteredUsers, _nItemsPerPage, url.getUrl( ),
                AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        // USER LEVEL
        Collection<Level> filteredLevels = LevelHome.getLevelsList( ).stream( )
                .filter( level -> getUser( ).isAdmin( ) || getUser( ).hasRights( level.getId( ) ) ).collect( Collectors.toList( ) );

        model.put( MARK_RIGHT, right );
        model.put( MARK_USER_LEVELS_LIST, filteredLevels );
        model.put( MARK_AVAILABLE_USERS_LIST, listAvailableUsers );
        model.put( MARK_ASSIGNED_USERS_LIST, paginator.getPageItems( ) );
        model.put( MARK_ASSIGNED_USERS_NUMBER, listAssignedUsers.size( ) );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( _nItemsPerPage ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_ASSIGN_USERS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_USERS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the data capture form for assign users to a role
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
        String strIdRight = request.getParameter( PARAMETER_ID_RIGHT );

        // retrieve the selected portlets ids
        String [ ] arrayUsersIds = request.getParameterValues( PARAMETER_AVAILABLE_USER_LIST );

        if ( ( arrayUsersIds != null ) )
        {
            for ( String arrayUsersId : arrayUsersIds )
            {
                int nUserId = Integer.parseInt( arrayUsersId );
                AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
                if ( !AdminUserHome.hasRight( user, strIdRight ) )
                {
                    AdminUserHome.createRightForUser( nUserId, strIdRight );
                }
            }
        }

        return JSP_ASSIGN_USERS_TO_RIGHT + "?" + PARAMETER_ID_RIGHT + "=" + strIdRight;
    }

    /**
     * unassigns user from role
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
        String strIdRight = request.getParameter( PARAMETER_ID_RIGHT );
        int nIdUser = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
        String strAnchor = request.getParameter( PARAMETER_ANCHOR );

        AdminUser adminUser = AdminUserHome.findByPrimaryKey( nIdUser );

        if ( adminUser != null )
        {
            AdminUserHome.removeRightForUser( nIdUser, strIdRight );
        }

        return JSP_ASSIGN_USERS_TO_RIGHT + "?" + PARAMETER_ID_RIGHT + "=" + strIdRight + "#" + strAnchor;
    }

    /**
     * Get the item navigator
     * 
     * @param strIdRight
     *            the id right
     * @param strUrl
     *            the url
     */
    private void setItemNavigator( String strIdRight, String strUrl )
    {
        if ( _itemNavigator == null )
        {
            List<String> listIdsRight = new ArrayList<>( );
            int nCurrentItemId = 0;
            int nIndex = 0;

            for ( Right right : RightHome.getRightsList( ) )
            {
                if ( ( right != null ) && StringUtils.isNotBlank( right.getId( ) ) )
                {
                    listIdsRight.add( right.getId( ) );

                    if ( right.getId( ).equals( strIdRight ) )
                    {
                        nCurrentItemId = nIndex;
                    }

                    nIndex++;
                }
            }

            _itemNavigator = new ItemNavigator( listIdsRight, nCurrentItemId, strUrl, PARAMETER_ID_RIGHT );
        }
        else
        {
            _itemNavigator.setCurrentItemId( strIdRight );
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
