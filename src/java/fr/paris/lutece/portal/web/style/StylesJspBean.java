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
package fr.paris.lutece.portal.web.style;

import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletImpl;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Styles features
 */
public class StylesJspBean extends AdminFeaturesPageJspBean
{
    //////////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    /**
     * Right to manage styles
     */
    public static final String RIGHT_MANAGE_STYLE = "CORE_STYLES_MANAGEMENT";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 7138319350433775587L;

    // Markers
    private static final String MARK_STYLE_LIST = "style_list";
    private static final String MARK_PORTLET_TYPE_LIST = "portlet_type_list";
    private static final String MARK_PORTAL_COMPONENT_LIST = "portal_component_list";
    private static final String MARK_STYLE = "style";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // Properties
    private static final String PROPERTY_STYLES_PER_PAGE = "paginator.style.itemsPerPage";

    // Templates files path
    private static final String TEMPLATE_MANAGE_STYLES = "admin/style/manage_styles.html";
    private static final String TEMPLATE_CREATE_STYLE = "admin/style/create_style.html";
    private static final String TEMPLATE_MODIFY_STYLE = "admin/style/modify_style.html";

    // Portal Component definition
    private static final int PORTAL_COMPONENT_ID_PORTLET = 0;

    // Jsp Definition
    private static final String JSP_DO_REMOVE_STYLE = "jsp/admin/style/DoRemoveStyle.jsp";
    private static final String JSP_DO_REMOVE_STYLESHEET = "jsp/admin/style/DoRemoveStyleSheet.jsp";

    // Message keys
    private static final String MESSAGE_CANT_DELETE_STYLE_PORTLETS = "portal.style.message.cannotDeleteStylePorlets";

    //    private static final String MESSAGE_CANT_DELETE_STYLE_XSL = "portal.style.message.cannotDeleteStyleXsl";
    private static final String MESSAGE_CONFIRM_DELETE_STYLE = "portal.style.message.confirmDeleteStyle";
    private static final String MESSAGE_CREATE_STYLE_INVALID_FORMAT_ID = "portal.style.message.createStyle.InvalidIdFormat";
    private static final String MESSAGE_CREATE_STYLE_ID_ALREADY_EXISTS = "portal.style.message.createStyle.idAlreadyExists";
    private static final String MESSAGE_CREATE_STYLE_COMPONENT_EXISTS = "portal.style.message.createStyle.componentHasAlreadyAStyle";
    private static final String MESSAGE_CONFIRM_DELETE_STYLESHEET = "portal.style.message.stylesheetConfirmDelete";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Displays the styles list
     * @param request The HTTP request
     * @return the html code for displaying the styles list
     */
    public String getStylesManagement( HttpServletRequest request )
    {
        List<Style> listStyles = (List<Style>) StyleHome.getStylesList(  );

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listStyles, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLES_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        String strURL = getHomeUrl( request );

        if ( strSortedAttributeName != null )
        {
            strURL += ( "?" + Parameters.SORTED_ATTRIBUTE_NAME + "=" + strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            strURL += ( "&" + Parameters.SORTED_ASC + "=" + strAscSort );
        }

        LocalizedPaginator<Style> paginator = new LocalizedPaginator<Style>( listStyles, _nItemsPerPage, strURL,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_STYLE_LIST, paginator.getPageItems(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_STYLES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the create form of a new style
     * @param request The http request
     * @return The html code for the create form of a new style
     */
    public String getCreateStyle( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PORTLET_TYPE_LIST, PortletTypeHome.getPortletsTypesList( getLocale(  ) ) );
        model.put( MARK_PORTAL_COMPONENT_LIST, StyleHome.getPortalComponentList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_STYLE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation form of a new style by recovering the parameters in the http request
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doCreateStyle( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.STYLE_ID );

        //Mandatory fields
        if ( request.getParameter( Parameters.STYLE_ID ).equals( "" ) ||
                request.getParameter( Parameters.STYLE_NAME ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nId;

        try
        {
            nId = Integer.parseInt( strId );
        }
        catch ( NumberFormatException nb )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_INVALID_FORMAT_ID,
                AdminMessage.TYPE_STOP );
        }

        Style styleExisting = StyleHome.findByPrimaryKey( nId );

        if ( styleExisting != null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_ID_ALREADY_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        int nPortalComponentId = Integer.parseInt( request.getParameter( Parameters.PORTAL_COMPONENT ) );

        if ( StyleHome.checkStylePortalComponent( nPortalComponentId ) &&
                ( nPortalComponentId != PORTAL_COMPONENT_ID_PORTLET ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_COMPONENT_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        //The style doesn't exist in the database, we can create it
        Style style = new Style(  );
        style.setId( nId );
        style.setDescription( request.getParameter( Parameters.STYLE_NAME ) );
        style.setPortalComponentId( nPortalComponentId );

        String strPortletTypeId = request.getParameter( Parameters.PORTLET_TYPE );
        strPortletTypeId = ( strPortletTypeId != null ) ? strPortletTypeId : "";
        style.setPortletTypeId( strPortletTypeId );
        StyleHome.create( style );

        return getHomeUrl( request );
    }

    /**
     * Returns the form to update a style whose identifer is stored in the http request
     * @param request The http request
     * @return The html code
     */
    public String getModifyStyle( HttpServletRequest request )
    {
        String strIdStyles = request.getParameter( Parameters.STYLE_ID );
        int nStyleId = Integer.parseInt( strIdStyles );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_STYLE, StyleHome.findByPrimaryKey( nStyleId ) );
        model.put( MARK_PORTLET_TYPE_LIST, PortletTypeHome.getPortletsTypesList( getLocale(  ) ) );
        model.put( MARK_PORTAL_COMPONENT_LIST, StyleHome.getPortalComponentList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_STYLE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the updating form of a style whose new parameters are stored in the
     * http request
     * @param request The http request
     * @return The Jsp URL of the process result
     */
    public String doModifyStyle( HttpServletRequest request )
    {
        int nStyleId = Integer.parseInt( request.getParameter( Parameters.STYLE_ID ) );

        //the portlet type can be not present  the request if the portal component is not a portlet
        String strPortletTypeId = request.getParameter( Parameters.PORTLET_TYPE );
        strPortletTypeId = ( strPortletTypeId != null ) ? strPortletTypeId : "";

        int nPortalComponentId = Integer.parseInt( request.getParameter( Parameters.PORTAL_COMPONENT ) );
        String strStyleDescription = request.getParameter( Parameters.STYLE_NAME );

        if ( strStyleDescription.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Style style = StyleHome.findByPrimaryKey( nStyleId );
        int nPortalComponentOld = style.getPortalComponentId(  );

        if ( StyleHome.checkStylePortalComponent( nPortalComponentId ) &&
                ( nPortalComponentId != PORTAL_COMPONENT_ID_PORTLET ) && ( nPortalComponentId != nPortalComponentOld ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_COMPONENT_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        style.setPortletTypeId( strPortletTypeId );
        style.setPortalComponentId( nPortalComponentId );
        style.setDescription( strStyleDescription );
        StyleHome.update( style );

        return getHomeUrl( request );
    }

    /**
     * Returns the confirm of removing the style whose identifier is in
     * the http request
     *
     * @param request The Http request
     * @return the html code for the remove confirmation page
     */
    public String getConfirmRemoveStyle( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.STYLE_ID );
        int nId = Integer.parseInt( strId );
        Collection<PortletImpl> listPortlets = PortletHome.getPortletListByStyle( nId );
        Collection<StyleSheet> listStyleSheets = StyleHome.getStyleSheetList( nId );

        if ( listPortlets.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANT_DELETE_STYLE_PORTLETS,
                AdminMessage.TYPE_STOP );
        }

        if ( listStyleSheets.size(  ) > 0 )
        {
            for ( StyleSheet styleSheet : listStyleSheets )
            {
                int nIdStyleSheet = styleSheet.getId(  );
                UrlItem urlStylesheet = new UrlItem( JSP_DO_REMOVE_STYLESHEET );
                urlStylesheet.addParameter( Parameters.STYLESHEET_ID, nIdStyleSheet );
                urlStylesheet.addParameter( Parameters.STYLE_ID, nId );

                Object[] args = { styleSheet.getDescription(  ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_STYLESHEET, args,
                    urlStylesheet.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
            }
        }

        UrlItem url = new UrlItem( JSP_DO_REMOVE_STYLE );
        url.addParameter( Parameters.STYLE_ID, nId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_STYLE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the deletion of a style
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doRemoveStyle( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.STYLE_ID );
        int nId = Integer.parseInt( strId );
        StyleHome.remove( nId );

        return getHomeUrl( request );
    }
}
