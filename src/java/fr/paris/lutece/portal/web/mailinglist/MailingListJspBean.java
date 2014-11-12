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
package fr.paris.lutece.portal.web.mailinglist;

import fr.paris.lutece.portal.business.mailinglist.MailingList;
import fr.paris.lutece.portal.business.mailinglist.MailingListFilter;
import fr.paris.lutece.portal.business.mailinglist.MailingListHome;
import fr.paris.lutece.portal.business.mailinglist.MailingListUsersFilter;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.mailinglist.MailingListRemovalListenerService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Mailing ListJspBean
 */
public class MailingListJspBean extends AdminFeaturesPageJspBean
{
    //Rights
    public static final String RIGHT_MANAGE_MAILINGLISTS = "CORE_MAILINGLISTS_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_MANAGE_MAILINGLISTS = "admin/mailinglist/manage_mailinglists.html";
    private static final String TEMPLATE_CREATE_MAILINGLIST = "admin/mailinglist/create_mailinglist.html";
    private static final String TEMPLATE_MODIFY_MAILINGLIST = "admin/mailinglist/modify_mailinglist.html";
    private static final String TEMPLATE_ADD_USERS = "admin/mailinglist/add_users.html";
    private static final String TEMPLATE_VIEW_USERS = "admin/mailinglist/view_users.html";

    // Bookmarks
    private static final String MARK_MAILINGLISTS_LIST = "mailinglists_list";
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    private static final String MARK_WORKGROUP_SELECTED = "selected_workgroup";
    private static final String MARK_ROLES_LIST = "roles_list";
    private static final String MARK_RECIPIENTS_LIST = "recipients_list";
    private static final String MARK_MAILINGLIST = "mailinglist";
    private static final String MARK_MAILINGLIST_FILTER = "mailinglistFilter";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // Properties
    private static final String PROPERTY_CREATE_MAILINGLIST_PAGETITLE = "portal.mailinglist.create_mailinglist.pageTitle";
    private static final String PROPERTY_MODIFY_MAILINGLIST_PAGETITLE = "portal.mailinglist.modify_mailinglist.pageTitle";
    private static final String PROPERTY_VIEW_USERS_PAGETITLE = "portal.mailinglist.view_users.pageTitle";
    private static final String PROPERTY_ADD_USERS_PAGETITLE = "portal.mailinglist.add_users.pageTitle";
    private static final String PROPERTY_MAILINGLIST_PER_PAGE = "paginator.mailinglist.itemsPerPage";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.mailinglist.message.confirmRemoveMailingList";
    private static final String MESSAGE_CANNOT_REMOVE = "portal.mailinglist.message.cannotRemoveMailingList";
    private static final String MESSAGE_FILTER_ALREADY_EXISTS = "portal.mailinglist.message.filterAlreadyExists";

    // Parameters
    private static final String PARAMETER_WORKGROUP = "workgroup";
    private static final String PARAMETER_ROLE = "role";
    private static final String PARAMETER_MAILINGLIST_ID = "id_mailinglist";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_SESSION = "session";

    // JSP
    private static final String JSP_MODIFY_MAILINGLIST = "ModifyMailingList.jsp";
    private static final String JSP_URL_REMOVE_MAILINGLIST = "jsp/admin/mailinglist/DoRemoveMailingList.jsp";
    private static final String JSP_URL_MANAGE_MAILINGLISTS = "jsp/admin/mailinglist/ManageMailingLists.jsp";
    private MailingListFilter _mailingListFilter;
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Get the mailinglists management page.
     * This page provides the list of all existing mailinglists.
     * @param request the http request
     * @return the html code for the mailinglist management page
     */
    public String getManageMailinglists( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        // Build filter
        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SESSION ) ) )
        {
            _mailingListFilter = new MailingListFilter(  );
            populate( _mailingListFilter, request );
        }

        List<MailingList> listMailinglists = AdminMailingListService.getUserMailingListsByFilter( getUser(  ),
                _mailingListFilter );

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = request.getParameter( Parameters.SORTED_ASC );
        boolean bIsAscSort = true;

        if ( StringUtils.isBlank( strSortedAttributeName ) )
        {
            strSortedAttributeName = PARAMETER_NAME;
        }

        if ( StringUtils.isNotBlank( strAscSort ) )
        {
            bIsAscSort = Boolean.parseBoolean( strAscSort );
        }

        Collections.sort( listMailinglists, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );

        // Paginator
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_MAILINGLIST_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_MAILINGLISTS );
        url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        url.addParameter( Parameters.SORTED_ASC, Boolean.toString( bIsAscSort ) );
        url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );

        LocalizedPaginator<MailingList> paginator = new LocalizedPaginator<MailingList>( listMailinglists,
                _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex,
                request.getLocale(  ) );

        model.put( MARK_MAILINGLISTS_LIST, paginator.getPageItems(  ) );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( paginator.getItemsPerPage(  ) ) );
        model.put( MARK_MAILINGLIST_FILTER, _mailingListFilter );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_MAILINGLISTS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get the mailinglist create page.
     * @param request the http request
     * @return the html code for the mailinglist create page
     */
    public String getCreateMailinglist( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_MAILINGLIST_PAGETITLE );

        ReferenceList listWorkgroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_WORKGROUPS_LIST, listWorkgroups );

        //LUTECE-890 : the first workgroup will be selected by default
        if ( !listWorkgroups.isEmpty(  ) )
        {
            model.put( MARK_WORKGROUP_SELECTED, listWorkgroups.get( 0 ).getCode(  ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_MAILINGLIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for create a mailing list
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doCreateMailingList( HttpServletRequest request )
    {
        MailingList mailinglist = new MailingList(  );
        String strErrors = processFormData( request, mailinglist );

        if ( strErrors != null )
        {
            return AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
        }

        MailingListHome.create( mailinglist );

        // Forward to modify page to enter users filters
        UrlItem urlModify = new UrlItem( JSP_MODIFY_MAILINGLIST );
        urlModify.addParameter( PARAMETER_MAILINGLIST_ID, mailinglist.getId(  ) );

        return urlModify.getUrl(  );
    }

    /**
     * Get the mailinglist modify page.
     * @param request the http request
     * @return the html code for the mailinglist modify page
     */
    public String getModifyMailinglist( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_MAILINGLIST_PAGETITLE );

        ReferenceList listWorkgroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );

        String strMailingListId = request.getParameter( PARAMETER_MAILINGLIST_ID );

        if ( !StringUtils.isNumeric( strMailingListId ) )
        {
            AppLogService.error( strMailingListId + " is not a valid mailing list id." );

            return getManageMailinglists( request );
        }

        int nMailingListId = Integer.parseInt( strMailingListId );
        MailingList mailinglist = MailingListHome.findByPrimaryKey( nMailingListId );

        if ( mailinglist == null )
        {
            AppLogService.error( strMailingListId + " is not a valid mailing list id." );

            return getManageMailinglists( request );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_WORKGROUPS_LIST, listWorkgroups );
        model.put( MARK_MAILINGLIST, mailinglist );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_MAILINGLIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for modify a mailing list
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doModifyMailingList( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );
        int nId = Integer.parseInt( strId );
        MailingList mailinglist = MailingListHome.findByPrimaryKey( nId );

        String strErrors = processFormData( request, mailinglist );

        if ( strErrors != null )
        {
            return AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
        }

        MailingListHome.update( mailinglist );

        return getHomeUrl( request );
    }

    /**
     * Returns the page of confirmation for deleting a mailinglist
     *
     * @param request The Http Request
     * @return the confirmation url
     */
    public String getConfirmRemoveMailingList( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );
        String strUrlRemove = JSP_URL_REMOVE_MAILINGLIST + "?" + PARAMETER_MAILINGLIST_ID + "=" + strId;

        ArrayList<String> listErrors = new ArrayList<String>(  );
        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, strUrlRemove,
                AdminMessage.TYPE_CONFIRMATION );

        if ( !MailingListRemovalListenerService.getService(  ).checkForRemoval( strId, listErrors, getLocale(  ) ) )
        {
            String strCause = AdminMessageService.getFormattedList( listErrors, getLocale(  ) );
            Object[] args = { strCause };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_REMOVE, args, AdminMessage.TYPE_STOP );
        }

        return strUrl;
    }

    /**
     * Process the data capture form for modify a mailing list
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doRemoveMailingList( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );
        int nId = Integer.parseInt( strId );

        MailingListHome.remove( nId );

        return getHomeUrl( request );
    }

    /**
     * Get the view users page.
     * @param request the http request
     * @return the html code for the mailinglist modify page
     */
    public String getViewUsers( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_VIEW_USERS_PAGETITLE );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        Collection<Recipient> listRecipients;
        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );

        if ( strId != null )
        {
            int nIdMailingList = Integer.parseInt( strId );
            listRecipients = AdminMailingListService.getRecipients( nIdMailingList );
        }
        else
        {
            String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP );
            String strRole = request.getParameter( PARAMETER_ROLE );
            listRecipients = AdminMailingListService.getRecipients( strWorkgroup, strRole );
        }

        model.put( MARK_RECIPIENTS_LIST, listRecipients );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VIEW_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Get the add users page.
     * @param request the http request
     * @return the html code for the mailinglist modify page
     */
    public String getAddUsers( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_ADD_USERS_PAGETITLE );

        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );
        int nId = Integer.parseInt( strId );
        MailingList mailinglist = MailingListHome.findByPrimaryKey( nId );

        if ( mailinglist == null )
        {
            return getManageMailinglists( request );
        }

        ReferenceList listWorkgroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );
        ReferenceList listRoles = AdminRoleHome.getRolesList(  );
        listRoles.addItem( AdminMailingListService.ALL_ROLES, AdminMailingListService.ALL_ROLES );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_WORKGROUPS_LIST, listWorkgroups );
        model.put( MARK_ROLES_LIST, listRoles );
        model.put( MARK_MAILINGLIST, mailinglist );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADD_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form for adding users filters
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doAddUsers( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP );
        String strRole = request.getParameter( PARAMETER_ROLE );

        int nId = Integer.parseInt( strId );
        MailingListUsersFilter filter = new MailingListUsersFilter(  );
        filter.setWorkgroup( strWorkgroup );
        filter.setRole( strRole );

        if ( !AdminMailingListService.checkFilter( filter, nId ) )
        {
            MailingListHome.addFilterToMailingList( filter, nId );

            // Forward to modify page to enter users filters
            UrlItem urlModify = new UrlItem( JSP_MODIFY_MAILINGLIST );
            urlModify.addParameter( PARAMETER_MAILINGLIST_ID, nId );

            return urlModify.getUrl(  );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_FILTER_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
    }

    /**
     * Process the data capture form to remove users filters
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doDeleteFilter( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_MAILINGLIST_ID );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP );
        String strRole = request.getParameter( PARAMETER_ROLE );

        int nId = Integer.parseInt( strId );
        MailingListUsersFilter filter = new MailingListUsersFilter(  );
        filter.setWorkgroup( strWorkgroup );
        filter.setRole( strRole );
        MailingListHome.deleteFilterToMailingList( filter, nId );

        // Forward to modify page to enter users filters
        UrlItem urlModify = new UrlItem( JSP_MODIFY_MAILINGLIST );
        urlModify.addParameter( PARAMETER_MAILINGLIST_ID, nId );

        return urlModify.getUrl(  );
    }

    /**
     * Process Form Data
     * @param request The HTTP request
     * @param mailinglist The mailing list
     * @return An Error message or null if no error
     */
    private String processFormData( HttpServletRequest request, MailingList mailinglist )
    {
        String strErrors = null;
        String strName = request.getParameter( PARAMETER_NAME );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP );

        if ( ( strName == null ) || ( strName.equals( "" ) ) || ( strDescription == null ) ||
                ( strDescription.equals( "" ) ) )
        {
            return Messages.MANDATORY_FIELDS;
        }

        mailinglist.setName( strName );
        mailinglist.setDescription( strDescription );
        mailinglist.setWorkgroup( strWorkgroup );

        return strErrors;
    }
}
