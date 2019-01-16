/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
 	
package fr.paris.lutece.portal.web.templates;

import fr.paris.lutece.portal.business.template.AutoInclude;
import fr.paris.lutece.portal.business.template.AutoIncludeHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;
import java.io.File;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage AutoInclude features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageAutoIncludes.jsp", controllerPath = "jsp/admin/templates/", right = "CORE_TEMPLATES_AUTO_INCLUDES_MANAGEMENT" )
public class AutoIncludeJspBean extends MVCAdminJspBean
{
    // Rights
    public static final String RIGHT_MANAGEAUTOINCLUDES = "CORE_TEMPLATES_AUTO_INCLUDES_MANAGEMENT";
    
    // Properties
    private static final String PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE = "admintheme.listItems.itemsPerPage";
    
    // Parameters
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    
    // Markers
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    
    private static final String PATH_TEMPLATES = "/WEB-INF/templates/";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    // Templates
    private static final String TEMPLATE_MANAGE_AUTOINCLUDES = "/admin/templates/manage_autoincludes.html";
    private static final String TEMPLATE_CREATE_AUTOINCLUDE = "/admin/templates/create_autoinclude.html";

    // Parameters
    private static final String PARAMETER_AUTOINCLUDE = "file_path";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_AUTOINCLUDES = "portal.templates.manage_autoincludes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_AUTOINCLUDE = "portal.templates..create_autoinclude.pageTitle";

    // Markers
    private static final String MARK_AUTOINCLUDE_LIST = "autoinclude_list";
    private static final String MARK_AUTOINCLUDE = "autoinclude";

    private static final String JSP_MANAGE_AUTOINCLUDES = "jsp/admin/templates/ManageAutoIncludes.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_AUTOINCLUDE = "portal.templates.message.confirmRemoveAutoInclude";

    // Views
    private static final String VIEW_MANAGE_AUTOINCLUDES = "manageAutoIncludes";
    private static final String VIEW_CREATE_AUTOINCLUDE = "createAutoInclude";

    // Actions
    private static final String ACTION_CREATE_AUTOINCLUDE = "createAutoInclude";
    private static final String ACTION_REMOVE_AUTOINCLUDE = "removeAutoInclude";
    private static final String ACTION_CONFIRM_REMOVE_AUTOINCLUDE = "confirmRemoveAutoInclude";

    // Infos
    private static final String INFO_AUTOINCLUDE_CREATED = "portal.templates.info.autoinclude.created";
    private static final String INFO_AUTOINCLUDE_REMOVED = "portal.templates.info.autoinclude.removed";
    
    // Session variable to store working values
    private AutoInclude _autoinclude;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_AUTOINCLUDES, defaultView = true )
    public String getManageAutoIncludes( HttpServletRequest request )
    {
        _autoinclude = null;
        List<AutoInclude> listAutoIncludes = AutoIncludeHome.getAutoIncludesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_AUTOINCLUDE_LIST, listAutoIncludes, JSP_MANAGE_AUTOINCLUDES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_AUTOINCLUDES, TEMPLATE_MANAGE_AUTOINCLUDES, model );
    }

    /**
     * Returns the form to create a autoinclude
     *
     * @param request The Http request
     * @return the html code of the autoinclude form
     */
    @View( VIEW_CREATE_AUTOINCLUDE )
    public String getCreateAutoInclude( HttpServletRequest request )
    {
        _autoinclude = ( _autoinclude != null ) ? _autoinclude : new AutoInclude(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_AUTOINCLUDE, _autoinclude );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_AUTOINCLUDE, TEMPLATE_CREATE_AUTOINCLUDE, model );
    }

    /**
     * Process the data capture form of a new autoinclude
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_AUTOINCLUDE )
    public String doCreateAutoInclude( HttpServletRequest request )
    {
        populate( _autoinclude, request, request.getLocale( ) );
        try
        {
            String strFilePath = AppPathService.getAbsolutePathFromRelativePath( PATH_TEMPLATES + _autoinclude.getFilePath() );
            File file = new File( strFilePath );
            if( ! file.exists() )
            {
                addError( "Template not found " + strFilePath );
                return redirectView( request, VIEW_CREATE_AUTOINCLUDE );
            }
            AutoIncludeHome.create( _autoinclude );
        }
        catch( RuntimeException e )
        {
            addError( "Template not found" );
            return redirectView( request, VIEW_CREATE_AUTOINCLUDE );
        }
        addInfo( INFO_AUTOINCLUDE_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_AUTOINCLUDES );
    }

    /**
     * Manages the removal form of a autoinclude whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_AUTOINCLUDE )
    public String getConfirmRemoveAutoInclude( HttpServletRequest request )
    {
        String strAutoInclude = request.getParameter( PARAMETER_AUTOINCLUDE );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_AUTOINCLUDE ) );
        url.addParameter( PARAMETER_AUTOINCLUDE, strAutoInclude );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_AUTOINCLUDE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a autoinclude
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage autoincludes
     */
    @Action( ACTION_REMOVE_AUTOINCLUDE )
    public String doRemoveAutoInclude( HttpServletRequest request )
    {
        String strAutoInclude = request.getParameter( PARAMETER_AUTOINCLUDE );
        AutoIncludeHome.remove( strAutoInclude );
        addInfo( INFO_AUTOINCLUDE_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_AUTOINCLUDES );
    }

    /**
     * Return a model that contains the list and paginator infos
     * @param request The HTTP request
     * @param strBookmark The bookmark
     * @param list The list of item
     * @param strManageJsp The JSP
     * @return The model
     */
    private <T> Map<String, Object> getPaginatedListModel( HttpServletRequest request, String strBookmark, List<T> list,
        String strManageJsp )
    {
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_ITEM_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage, _nDefaultItemsPerPage );

        UrlItem url = new UrlItem( strManageJsp );
        String strUrl = url.getUrl(  );

        // PAGINATOR
        LocalizedPaginator<T> paginator = new LocalizedPaginator<T>( list, _nItemsPerPage, strUrl, PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        Map<String, Object> model = getModel(  );

        model.put( MARK_NB_ITEMS_PER_PAGE, String.valueOf( _nItemsPerPage ) );
        model.put( MARK_PAGINATOR, paginator );
        model.put( strBookmark, paginator.getPageItems(  ) );

        return model;
    }

}