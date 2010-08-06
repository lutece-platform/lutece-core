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
package fr.paris.lutece.portal.web.features;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserFilter;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage rights features ( manage, create, modify )
 */
public class RightJspBean  extends AdminFeaturesPageJspBean
{
	// Right 
    public static final String RIGHT_MANAGE_RIGHTS = "CORE_RIGHT_MANAGEMENT";
    
    // Properties
    private static final String PROPERTY_PAGE_TITLE_RIGHT_LIST = "portal.features.manage_rights.pageTitle";
    private static final String PROPERTY_ASSIGN_USERS_PAGE_TITLE = "portal.features.assign_users.pageTitle";
    private static final String PROPERTY_USERS_PER_PAGE = "paginator.user.itemsPerPage";
    
    // Markers            
    private static final String MARK_RIGHTS_LIST = "rights_list";
    private static final String MARK_RIGHT = "right";
    private static final String MARK_AVAILABLE_USERS_LIST = "available_users_list";
    private static final String MARK_ASSIGNED_USERS_LIST = "assigned_users_list";
    private static final String MARK_ASSIGNED_USERS_NUMBER = "assigned_users_number";
    private static final String MARK_SEARCH_IS_SEARCH = "search_is_search";
    private static final String MARK_USER_LEVELS_LIST = "user_levels";
    private static final String MARK_SEARCH_ADMIN_USER_FILTER = "search_admin_user_filter";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_SORT_SEARCH_ATTRIBUTE = "sort_search_attribute";
    
    // Parameters
    private static final String PARAMETER_ID_RIGHT = "id_right";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_AVAILABLE_USER_LIST = "available_users_list";
    private static final String PARAMETER_ID_USER = "id_user";
    private static final String PARAMETER_ANCHOR = "anchor";
    
    // Templates files path    
    private static final String TEMPLATE_MANAGE_RIGHTS = "admin/features/manage_rights.html";
    private static final String TEMPLATE_ASSIGN_USERS = "admin/features/assign_users_right.html";
    
    // JSP
    private static final String JSP_URL_ASSIGN_USERS_TO_RIGHT_COMPLETE = "jsp/admin/features/AssignUsersRight.jsp";
    private static final String JSP_URL_ASSIGN_USERS_TO_RIGHT = "AssignUsersRight.jsp";
    private static final String JSP_URL_RIGHTS_MANAGEMENT = "ManageRights.jsp";
    
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    
    /**
     * Returns the list of rights
     *
     * @param request The Http request
     * @return the html code for display the rights list
     */
    public String getManageRights( HttpServletRequest request )
    {
    	setPageTitleProperty( PROPERTY_PAGE_TITLE_RIGHT_LIST );
    	
    	HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_RIGHTS_LIST, I18nService.localizeCollection( RightHome.getRightsList(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RIGHTS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
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

        // RIGHT
        String strIdRight = request.getParameter( PARAMETER_ID_RIGHT );
        Right right = RightHome.findByPrimaryKey( strIdRight );
        right.setLocale( getLocale(  ) );
        
        // ASSIGNED USERS
        List<AdminUser> listAssignedUsers = new ArrayList<AdminUser>(  );
        for ( AdminUser user : AdminUserHome.findByRight( strIdRight ) )
        {
            //Add users with higher level then connected user or add all users if connected user is administrator
            if ( ( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) )
            {
                listAssignedUsers.add( user );
            }
        }
        
        // FILTER
        AdminUserFilter auFilter = new AdminUserFilter(  );
        List<AdminUser> listFilteredUsers = new ArrayList<AdminUser>(  );
        boolean bIsSearch = auFilter.setAdminUserFilter( request );
        boolean bIsFiltered;
        
        for ( AdminUser userFiltered : AdminUserHome.findUserByFilter( auFilter ) )
        {
        	bIsFiltered = Boolean.FALSE;
        	
        	for( AdminUser assignedUser : listAssignedUsers )
        	{
        		if ( assignedUser.getUserId(  ) == userFiltered.getUserId(  ) )
                {
            		bIsFiltered = Boolean.TRUE;
            		break;
                }
        	}
        	
        	if ( bIsFiltered &&
                    ( ( userFiltered.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) ) )
            {
        		listFilteredUsers.add( userFiltered );
            }
        }
        
        // AVAILABLE USERS
        ReferenceList listAvailableUsers = new ReferenceList(  );
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
                    break;
                }
            }

            //Add users with higher level then connected user or add all users if connected user is administrator
            if ( !bAssigned &&
                    ( ( user.getUserLevel(  ) > getUser(  ).getUserLevel(  ) ) || ( getUser(  ).isAdmin(  ) ) ) && 
                    user.getUserLevel(  ) <= right.getLevel(  ) )
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

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_ASSIGN_USERS_TO_RIGHT_COMPLETE;
        UrlItem url = new UrlItem( strBaseUrl );
        
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
        	auFilter.setUrlAttributes( url );
        	strSortSearchAttribute = "&" + auFilter.getUrlAttributes(  );
        }
        
        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<Right> listAllRight = RightHome.getRightsList(  );
        // Sort by name
        Map<String, String> listRightNames = new TreeMap<String, String>(  );
        for ( Right allRight : listAllRight )
        {
        	allRight.setLocale( getLocale(  ) );
        	listRightNames.put( allRight.getName(  ), allRight.getId(  ));
        }
        int nMapKey = 1;
        int nCurrentItemId = 1;
        for ( Iterator<String> it = listRightNames.values(  ).iterator(  ); it.hasNext(  ); )
        {
        	String strRightId = it.next(  );
        	listItem.put( nMapKey, strRightId );
        	if( strRightId.equals( right.getId(  ) ) )
        	{
        		nCurrentItemId = nMapKey;
        	}
        	nMapKey++;
        }
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_ID_RIGHT );

        // PAGINATOR
        url.addParameter( PARAMETER_ID_RIGHT, right.getId(  ) );
        LocalizedPaginator paginator = new LocalizedPaginator( listFilteredUsers, _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX,
        		_strCurrentPageIndex, getLocale(  ) );        
        
        // USER LEVEL
        Collection<Level> filteredLevels = new ArrayList<Level>(  );
        for ( Level level : LevelHome.getLevelsList(  ) )
        {
            if ( getUser(  ).isAdmin(  ) || getUser(  ).hasRights( level.getId(  ) ) )
            {
                filteredLevels.add( level );
            }
        }

        model.put( MARK_RIGHT, right );
        model.put( MARK_USER_LEVELS_LIST, filteredLevels );
        model.put( MARK_AVAILABLE_USERS_LIST, listAvailableUsers );
        model.put( MARK_ASSIGNED_USERS_LIST, paginator.getPageItems(  ) );
        model.put( MARK_ASSIGNED_USERS_NUMBER, listAssignedUsers.size(  ) );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( MARK_SEARCH_ADMIN_USER_FILTER, auFilter );
        model.put( MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGN_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for assign users to a role
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
            strReturn = JSP_URL_RIGHTS_MANAGEMENT;
        }
        else
        {
            String strIdRight = request.getParameter( PARAMETER_ID_RIGHT );

            //retrieve the selected portlets ids
            String[] arrayUsersIds = request.getParameterValues( PARAMETER_AVAILABLE_USER_LIST );

            if ( ( arrayUsersIds != null ) )
            {
                for ( int i = 0; i < arrayUsersIds.length; i++ )
                {
                    int nUserId = Integer.parseInt( arrayUsersIds[i] );
                    AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

                    if ( !AdminUserHome.hasRight( user, strIdRight ) )
                    {
                    	AdminUserHome.createRightForUser( nUserId, strIdRight );
                    }
                }
            }

            strReturn = JSP_URL_ASSIGN_USERS_TO_RIGHT + "?" + PARAMETER_ID_RIGHT + "=" + strIdRight;
        }

        return strReturn;
    }

    /**
     * unassigns user from role
     * @param request The HttpRequest
     * @return the HTML code of list assignations
     */
    public String doUnAssignUser( HttpServletRequest request )
    {
        String strIdRight = request.getParameter( PARAMETER_ID_RIGHT );
        int nIdUser = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
        String strAnchor = request.getParameter( PARAMETER_ANCHOR );

        AdminUser adminUser = AdminUserHome.findByPrimaryKey( nIdUser );

        if ( adminUser != null )
        {
        	AdminUserHome.removeRightForUser( nIdUser, strIdRight );
        }

        return JSP_URL_ASSIGN_USERS_TO_RIGHT + "?" + PARAMETER_ID_RIGHT + "=" + strIdRight
        		+ "#" + strAnchor;
    }
}
