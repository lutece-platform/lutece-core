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
package fr.paris.lutece.portal.web.style;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletImpl;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.binding.BindingResult;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ModelAttribute;
import fr.paris.lutece.portal.util.mvc.commons.annotations.RequestParam;
import fr.paris.lutece.portal.util.mvc.commons.annotations.ResponseBody;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.IPager;
import fr.paris.lutece.portal.web.util.Pager;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.sort.AttributeComparator;

/**
 * This class provides the user interface to manage Styles features
 */
@RequestScoped
@Named
@Controller( controllerJsp = "ManageStyles.jsp", controllerPath = "jsp/admin/style/", right = "CORE_STYLES_MANAGEMENT", securityTokenEnabled=true )
public class StylesJspBean extends MVCAdminJspBean
{
    // ////////////////////////////////////////////////////////////////////////////////
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

 // Views
    private static final String VIEW_MANAGE_STYLES = "manageStyles";
    private static final String VIEW_CREATE_STYLE = "getCreateStyle";
    private static final String VIEW_MODIFY_STYLE = "getModifyStyle";
    private static final String VIEW_CONFIRM_REMOVE_STYLE = "getConfirmRemoveStyle";
    
    // Actions
    private static final String ACTION_GET_STYLE_ITEMS = "getStyleItems";
    private static final String ACTION_CREATE_STYLE = "createStyle";
    private static final String ACTION_MODIFY_STYLE = "modifyStyle";
    private static final String ACTION_REMOVE_STYLE = "removeStyle";


    // Markers
    private static final String MARK_STYLE_LIST = "style_list";
    private static final String MARK_PORTLET_TYPE_LIST = "portlet_type_list";
    private static final String MARK_PORTAL_COMPONENT_LIST = "portal_component_list";
    private static final String MARK_STYLE = "style";

    // Properties
    private static final String PROPERTY_STYLES_PER_PAGE = "paginator.style.itemsPerPage";

    public static final String PORTLET_TYPE = "portletTypeId";
    public static final String STYLE_ID = "id";
    public static final String STYLE_NAME = "description";
    public static final String STYLES = "styles";
    public static final String PORTAL_COMPONENT = "portalComponentId";
    
    // Templates files path
    private static final String TEMPLATE_MANAGE_STYLES = "admin/style/manage_styles.html";
    private static final String TEMPLATE_CREATE_STYLE = "admin/style/create_style.html";
    private static final String TEMPLATE_MODIFY_STYLE = "admin/style/modify_style.html";

    // Portal Component definition
    private static final int PORTAL_COMPONENT_ID_PORTLET = 0;

    // Jsp Definition
    private static final String JSP_DO_REMOVE_STYLE = "jsp/admin/style/ManageStyles.jsp";
    private static final String JSP_DO_REMOVE_STYLESHEET = "jsp/admin/style/DoRemoveStyleSheet.jsp";
    private static final String JSP_MANAGE_STYLESHEETS = "jsp/admin/style/ManageStyles.jsp";

    // Message keys
    private static final String MESSAGE_CANT_DELETE_STYLE_PORTLETS = "portal.style.message.cannotDeleteStylePorlets";

    private static final String MESSAGE_CONFIRM_DELETE_STYLE = "portal.style.message.confirmDeleteStyle";
    private static final String MESSAGE_CREATE_STYLE_INVALID_FORMAT_ID = "portal.style.message.createStyle.InvalidIdFormat";
    private static final String MESSAGE_CREATE_STYLE_ID_ALREADY_EXISTS = "portal.style.message.createStyle.idAlreadyExists";
    private static final String MESSAGE_CREATE_STYLE_COMPONENT_EXISTS = "portal.style.message.createStyle.componentHasAlreadyAStyle";
    private static final String MESSAGE_CONFIRM_DELETE_STYLESHEET = "portal.style.message.stylesheetConfirmDelete";
    
    // Infos
    private static final String INFO_STYLE_CREATED = "portal.style.info.style.created";
    private static final String INFO_STYLE_UPDATED = "portal.style.info.style.updated";
    private static final String INFO_STYLE_REMOVED = "portal.style.info.style.removed";
    
    @Inject
    @Pager( listBookmark = MARK_STYLE_LIST, defaultItemsPerPage = PROPERTY_STYLES_PER_PAGE)
    private IPager<Style, Void> pager;
    
    /**
    * Displays the list of styles in the management view.
    *
    * @param model
    *            the model used to pass attributes to the view
    * @param request
    *            the HTTP request
    * @return the HTML code of the styles management page
    */
    @View( value = VIEW_MANAGE_STYLES, defaultView = true )
    public String getStylesManagement(  Models model, HttpServletRequest request )
    {
        List<Style> listStyles = (List<Style>) StyleHome.getStylesList( );

        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listStyles, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        String strURL = getHomeUrl( request );

        if ( strSortedAttributeName != null )
        {
            strURL += ( "?" + Parameters.SORTED_ATTRIBUTE_NAME + "=" + strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            strURL += ( "&" + Parameters.SORTED_ASC + "=" + strAscSort );
        }
        pager.withBaseUrl(strURL)
        .withListItem(listStyles)
        .populateModels(request, model, getLocale());
                
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_STYLES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Retrieves a paginated list of style items for the specified page number.
     *
     * @param numPage The page number to retrieve.
     * @return A list of style items for the specified page.
     */
    @Action( value = ACTION_GET_STYLE_ITEMS )
	@ResponseBody
    public List<Style> getStyleItems( @RequestParam("page") int numPage )
    {
    	return pager.getPaginator().get().getPageItems(numPage);
    }
    /**
     * Returns the create form of a new style
     * 
     * @param model
     *            The Models
     * @return The html code for the create form of a new style
     */
    @View( value = VIEW_CREATE_STYLE, securityTokenAction = ACTION_CREATE_STYLE )
    public String getCreateStyle( Models model)
    {
        model.put( MARK_PORTLET_TYPE_LIST, PortletTypeHome.getPortletsTypesList( getLocale( ) ) );
        model.put( MARK_PORTAL_COMPONENT_LIST, StyleHome.getPortalComponentList( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_STYLE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Handles the creation of a new style by validating the form inputs, 
     * checking for conflicts, and persisting the style if valid.
     *
     * @param style
     *            the style object bound from the creation form
     * @param bindingResult
     *            the result of the validation of the style object
     * @param model
     *            the model used to pass attributes to the view
     * @param request
     *            the HTTP request
     * @return the JSP URL or redirect URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Action( value = ACTION_CREATE_STYLE )
    public String doCreateStyle(@Valid @ModelAttribute Style style, BindingResult bindingResult, Models model, HttpServletRequest request ) throws AccessDeniedException
    {
        if(bindingResult.isFailed( )) {
        	model.put(MVCUtils.MARK_ERRORS, bindingResult.getAllErrors( ));
        	return getCreateStyle( model);
        }
        Style styleExisting = StyleHome.findByPrimaryKey( style.getId( ) );

        if ( styleExisting != null )
        {
        	String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_ID_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }

        if ( StyleHome.checkStylePortalComponent( style.getPortalComponentId( ) ) && ( style.getPortalComponentId( ) != PORTAL_COMPONENT_ID_PORTLET ) )
        {
        	String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_COMPONENT_EXISTS, AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }

        StyleHome.create( style );

        return redirectView(request, VIEW_MANAGE_STYLES );
    }

    /**
     * Returns the form to update a style identified by its id.
     *
     * @param nStyleId
     *            the identifier of the style to modify
     * @param model
     *            the model used to pass attributes to the view
     * @return the HTML code of the update form
     */
    @View( value = VIEW_MODIFY_STYLE, securityTokenAction = ACTION_MODIFY_STYLE  )
    public String getModifyStyle(  @RequestParam(STYLE_ID) int nStyleId, Models model )
    {
        model.put( MARK_STYLE, StyleHome.findByPrimaryKey( nStyleId ) );
        model.put( MARK_PORTLET_TYPE_LIST, PortletTypeHome.getPortletsTypesList( getLocale( ) ) );
        model.put( MARK_PORTAL_COMPONENT_LIST, StyleHome.getPortalComponentList( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_STYLE, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Processes the update of a style by validating the form inputs, 
     * checking for conflicts, and persisting the modifications if valid.
     *
     * @param style
     *            the style object bound from the update form
     * @param nStyleId
     *            the identifier of the style being updated
     * @param bindingResult
     *            the result of the validation of the style object
     * @param model
     *            the model used to pass attributes to the view
     * @param request
     *            the HTTP request
     * @return the JSP URL or redirect URL of the process result
     */
    @Action( value = ACTION_MODIFY_STYLE )
    public String doModifyStyle( @Valid @ModelAttribute Style style, @RequestParam(STYLE_ID) int nStyleId,  BindingResult bindingResult, Models model, HttpServletRequest request )
    {
    	if(bindingResult.isFailed( )) {
        	model.put(MVCUtils.MARK_ERRORS, bindingResult.getAllErrors( ));
        	return getModifyStyle( nStyleId, model);
        }
        Style styleOld = StyleHome.findByPrimaryKey( nStyleId );
        int nPortalComponentOld = styleOld.getPortalComponentId( );

        if ( StyleHome.checkStylePortalComponent( style.getPortalComponentId() ) && ( style.getPortalComponentId() != PORTAL_COMPONENT_ID_PORTLET )
                && ( style.getPortalComponentId() != nPortalComponentOld ) )
        {
            String strMessageUrl= AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_STYLE_COMPONENT_EXISTS, AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }
        StyleHome.update( style );
        return redirectView(request, VIEW_MANAGE_STYLES );
    }

    /**
     * Returns the confirmation page for removing a style identified by its id.
     *
     * @param nId
     *            the identifier of the style to remove
     * @param request
     *            the HTTP request
     * @return the HTML code of the remove confirmation page
     */
    @View( value = VIEW_CONFIRM_REMOVE_STYLE, securityTokenAction = ACTION_REMOVE_STYLE )
    public String getConfirmRemoveStyle( @RequestParam(STYLE_ID) int nId, HttpServletRequest request )
    {
        Collection<PortletImpl> listPortlets = PortletHome.getPortletListByStyle( nId );
        Collection<StyleSheet> listStyleSheets = StyleHome.getStyleSheetList( nId );

        if ( CollectionUtils.isNotEmpty( listPortlets ) )
        {
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CANT_DELETE_STYLE_PORTLETS, JSP_MANAGE_STYLESHEETS, AdminMessage.TYPE_STOP );
            return redirect( request, strMessageUrl );
        }

        if ( CollectionUtils.isNotEmpty( listStyleSheets ) )
        {
            for ( StyleSheet styleSheet : listStyleSheets )
            {
                Object [ ] args = {
                        styleSheet.getDescription( )
                };

                Map<String, Object> parameters = new HashMap<>( );
                parameters.put( Parameters.STYLESHEET_ID, Integer.toString( styleSheet.getId( ) ) );
                parameters.put( Parameters.STYLE_ID, nId );
                parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, JSP_DO_REMOVE_STYLESHEET ) );

                String strMessageUrl= AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_STYLESHEET, args, null, JSP_DO_REMOVE_STYLESHEET, null,
                        AdminMessage.TYPE_CONFIRMATION, parameters, JSP_MANAGE_STYLESHEETS );        
                return redirect( request, strMessageUrl );
            }
        }

        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( STYLE_ID, Integer.toString( nId ) );
        parameters.put(MVCUtils.PARAMETER_ACTION, ACTION_REMOVE_STYLE);
        
        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_STYLE, null, null, JSP_DO_REMOVE_STYLE, null, AdminMessage.TYPE_CONFIRMATION, parameters, JSP_MANAGE_STYLESHEETS );
        return redirect( request, strMessageUrl );
    }

    /**
     * Processes the deletion of a style identified by its id.
     *
     * @param nId
     *            the identifier of the style to remove
     * @param request
     *            the HTTP request
     * @return the JSP URL or redirect URL of the process result
     */
    @Action( value = ACTION_REMOVE_STYLE )
    public String doRemoveStyle( @RequestParam(STYLE_ID) int nId, HttpServletRequest request )
    {
        StyleHome.remove( nId );
        return redirectView(request, VIEW_MANAGE_STYLES );
    }
}
