/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.web.system;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import fr.paris.lutece.portal.business.securityheader.SecurityHeaderConfigHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderConfigItem;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.securityheader.SecurityHeaderConfigService;
import fr.paris.lutece.portal.service.securityheader.SecurityHeaderService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.url.UrlItem;

@RequestScoped
@Named
@Controller( controllerJsp = "ModifySecurityHeaderConfig.jsp", controllerPath = "jsp/admin/system/", right = "CORE_SECURITY_HEADER_MANAGEMENT", securityTokenEnabled = true )
public class SecurityHeaderConfigJspBean extends MVCAdminJspBean
{
	// Rights
    public static final String RIGHT_SECURITY_HEADER_MANAGEMENT = "CORE_SECURITY_HEADER_MANAGEMENT";
	
    // Templates
    private static final String TEMPLATE_CREATE_SECURITYHEADER_CONFIG_ITEM = "admin/system/create_securityheader_config_item.html";
    private static final String TEMPLATE_MODIFY_SECURITYHEADER_CONFIG_ITEM = "admin/system/modify_securityheader_config_item.html";
    
	// Markers
    private static final String MARK_SECURITY_HEADERS_LIST = "security_headers_list";
    private static final String MARK_SECURITY_HEADER_ID = "id_securityheader";
    private static final String MARK_SECURITY_HEADER_CONFIG_ITEM = "securityheaderConfigItem";
    private static final String MARK_ERRORS = "errors";
    
    // Properties
    private static final String PROPERTY_CREATE_SECURITYHEADER_CONFIG_ITEM_PAGETITLE = "portal.securityheader.create_securityheader_config_item.pageTitle";
    private static final String PROPERTY_MODIFY_SECURITYHEADER_CONFIG_ITEM_PAGETITLE = "portal.securityheader.modify_securityheader_config_item.pageTitle";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.securityheader.message.confirmRemoveSecurityHeaderConfigItem";
    private static final String MESSAGE_CUSTOM_VALUE_ALREADY_EXISTS_FOR_URL_PATTERN = "portal.securityheader.message.custom_value_already_exists_for_url_pattern";
	
    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "model.entity.security_header_config_item.attribute.";
	
    // Views
    private static final String VIEW_CREATE_SECURITYHEADER_CONFIG_ITEM = "createSecurityHeaderConfigItem";
    private static final String VIEW_MODIFY_SECURITYHEADER_CONFIG_ITEM = "modifySecurityHeaderConfigItem";
    private static final String VIEW_CONFIRM_REMOVE_SECURITYHEADER_CONFIG_ITEM = "confirmRemoveSecurityHeaderConfigItem";
    
    // Actions
    private static final String VIEW_MODIFY_SECURITYHEADER_CONFIG = "modifySecurityHeaderConfig";
    private static final String ACTION_CREATE_SECURITYHEADER_CONFIG_ITEM = "createSecurityHeaderConfigItem";
    private static final String ACTION_MODIFY_SECURITYHEADER_CONFIG_ITEM = "modifySecurityHeaderConfigItem";
    private static final String ACTION_REMOVE_SECURITYHEADER_CONFIG_ITEM = "removeSecurityHeaderConfigItem";
        
    // Parameters
    private static final String PARAMETER_SECURITY_HEADER_ID = "id_securityheader";
    private static final String PARAMETER_SECURITY_HEADER_CONFIG_ITEM_ID = "id_config_item";
    private static final String PARAMETER_URL_PATTERN = "urlPattern";
    private static final String PARAMETER_HEADER_CUSTOM_VALUE = "headerCustomValue";
    private static final String PARAMETER_VIEW = "view";

    // Jsp definition
    public static final String JSP_MANAGE_SECURITY_HEADERS = "jsp/admin/system/ManageSecurityHeaders.jsp";

	private static final long serialVersionUID = -1017233262963948739L;
    
    @Inject
    private SecurityHeaderConfigService _securityHeaderConfigService;
    
    @Inject
    private SecurityHeaderService _securityHeaderService;
    
    @Inject
    private Models _model;
    
    /**
     * Returns the security header config item creation page.
     * 
     * @param request
     *            the http request
     * @return the html code for the security header config item creation page that allows to add an exception to default security header value on certain Urls
     */
    @View( VIEW_CREATE_SECURITYHEADER_CONFIG_ITEM )
	public String getCreateSecurityHeaderConfigItem( HttpServletRequest request )
	{
		setPageTitleProperty( PROPERTY_CREATE_SECURITYHEADER_CONFIG_ITEM_PAGETITLE );
		
		_model.put( MARK_SECURITY_HEADER_ID, request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_SECURITYHEADER_CONFIG_ITEM, getLocale( ), _model );

        return getAdminPage( template.getHtml( ) );
	}
	
	/**
     * Process the data capture form for create a security header config item that allows to add an exception to default security header value on certain Urls
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_SECURITYHEADER_CONFIG_ITEM )
	public String doCreateSecurityHeaderConfigItem( HttpServletRequest request )
    {
		SecurityHeaderConfigItem securityHeaderConfigItem = new SecurityHeaderConfigItem( );
        String strErrors = processCreationFormData( request, securityHeaderConfigItem );
        
        if ( strErrors != null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP ) );
        }
        
        _securityHeaderConfigService.create( securityHeaderConfigItem );
        
        return redirect( request, getModifySecurityHeaderConfigUrl( request ) );
    }

    /**
     * Returns modify security header config url
     * 
     * @param request
     * @return The modify security header config url
     */
    private String getModifySecurityHeaderConfigUrl( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_MANAGE_SECURITY_HEADERS );
        url.addParameter( PARAMETER_VIEW, VIEW_MODIFY_SECURITYHEADER_CONFIG );
        url.addParameter( PARAMETER_SECURITY_HEADER_ID, Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) ) );
        
        return url.getUrl( );

    }
    
	/**
     * Process Creation Form Data.
     * 
     * @param request
     *            The HTTP request
     * @param securityheader
     *            The security header config item
     * @return An Error message or null if no error
     */
	private String processCreationFormData( HttpServletRequest request, SecurityHeaderConfigItem securityHeaderConfigItem )
    {
		securityHeaderConfigItem.setIdSecurityHeader( Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) ) );
		securityHeaderConfigItem.setUrlPattern( StringEscapeUtils.unescapeHtml4( request.getParameter( PARAMETER_URL_PATTERN ) ) );
		securityHeaderConfigItem.setHeaderCustomValue( StringEscapeUtils.unescapeHtml4( request.getParameter( PARAMETER_HEADER_CUSTOM_VALUE ) ) );
        
        return processCreationControls( securityHeaderConfigItem );
    }
	
	/**
     * Executes creation controls action on request parameters
     * 
     * @param securityHeaderConfigItem
     *           Security header config item to control
     * @return null if controls are passed, an string containing an error message otherwise
     */
    private String processCreationControls( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
    	if( !validateBean( securityHeaderConfigItem, VALIDATION_ATTRIBUTES_PREFIX ) )
    	{
    		List<ErrorMessage> listErrors = ( List<ErrorMessage> ) _model.get( MARK_ERRORS );
    		return listErrors.get( 0 ).getMessage( );
    	}
    	
    	if( !isUrlPatternUniqueOnCreation( securityHeaderConfigItem ) )
    	{
    		return MESSAGE_CUSTOM_VALUE_ALREADY_EXISTS_FOR_URL_PATTERN;
    	}
        
        return null;
    }
    
    /**
     * Executes modification controls action on request parameters
     * 
     * @param securityHeaderConfigItem
     *           Security header config item to control
     * @return null if controls are passed, an string containing an error message otherwise
     */
    private String processModificationControls( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
    	if( !validateBean( securityHeaderConfigItem, VALIDATION_ATTRIBUTES_PREFIX ) )
    	{
    		List<ErrorMessage> listErrors = ( List<ErrorMessage> ) _model.get( MARK_ERRORS );
    		return listErrors.get( 0 ).getMessage( );
    	}
    	
    	if( !isUrlPatternUniqueOnModification( securityHeaderConfigItem ) )
    	{
    		return MESSAGE_CUSTOM_VALUE_ALREADY_EXISTS_FOR_URL_PATTERN;
    	}
        
        return null;
    }
    
    /**
     * Checks if the url pattern of security header config item already exists for the security header on config item creation.
     * It is used to prevent multiple values for the same URL pattern.
     * 
     * @param securityHeaderConfigItem
     * @return true if the url pattern of security header config item already exists for the security header, false otherwise
     */
    private boolean isUrlPatternUniqueOnCreation( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
    	return _securityHeaderConfigService.find( securityHeaderConfigItem.getIdSecurityHeader( ), securityHeaderConfigItem.getUrlPattern( ) ).isEmpty( );
    }
    
    /**
     * Checks if the url pattern of security header config item already exists for the security header on config item modification.
     * It is used to prevent multiple values for the same URL pattern.
     * 
     * @param securityHeaderConfigItem
     *           the security header config item
     * @return true if the url pattern of security header config item already exists for the security header, false otherwise
     */
    private boolean isUrlPatternUniqueOnModification( SecurityHeaderConfigItem securityHeaderConfigItemToModify )
    {   	
    	for( SecurityHeaderConfigItem securityHeaderConfigItem : _securityHeaderConfigService.find( securityHeaderConfigItemToModify.getIdSecurityHeader( ), securityHeaderConfigItemToModify.getUrlPattern( ) ) )
    	{
    		if( securityHeaderConfigItemToModify.getIdConfigItem( ) != securityHeaderConfigItem.getIdConfigItem( ) )
    		{
    			return false;
    		}   			
    	}
    	
    	return true;
    }

    /**
     * Returns the security header config item modification page that allows to modify an exception to default security header value on certain Urls
     * 
     * @param request
     *            the http request
     * @return the html code for the security header config item creation page
     */
    @View( VIEW_MODIFY_SECURITYHEADER_CONFIG_ITEM )
	public String getModifySecurityHeaderConfigItem( HttpServletRequest request )
	{
		setPageTitleProperty( PROPERTY_MODIFY_SECURITYHEADER_CONFIG_ITEM_PAGETITLE );
		
		String securityHeaderConfigItemId = request.getParameter( PARAMETER_SECURITY_HEADER_CONFIG_ITEM_ID );
		
		if ( !StringUtils.isNumeric( securityHeaderConfigItemId ) )
        {
            AppLogService.error( " {} is not a valid security header config item id.", ( ) -> SecurityUtil.logForgingProtect( securityHeaderConfigItemId ) );

            return getManageSecurityHeaders( request );
        }		
		
		SecurityHeaderConfigItem securityHeaderConfigItem = SecurityHeaderConfigHome.findByPrimaryKey( Integer.parseInt( securityHeaderConfigItemId ) );
		
		if ( securityHeaderConfigItem == null )
        {
            AppLogService.error( " {} is not a valid security header config item id.", ( ) -> SecurityUtil.logForgingProtect( securityHeaderConfigItemId ) );
        
            return getManageSecurityHeaders( request );
        }
		
		_model.put( MARK_SECURITY_HEADER_CONFIG_ITEM, securityHeaderConfigItem );
		_model.put( MARK_SECURITY_HEADER_ID, request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_SECURITYHEADER_CONFIG_ITEM, getLocale( ), _model );

        return getAdminPage( template.getHtml( ) );
	}
	
	/**
     * Process the data capture form for update a security header config item
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_SECURITYHEADER_CONFIG_ITEM )
	public String doModifySecurityHeaderConfigItem( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_CONFIG_ITEM_ID ) );
        SecurityHeaderConfigItem securityHeaderConfigItem = SecurityHeaderConfigHome.findByPrimaryKey( nId );

        String strErrors = processModifyFormData( request, securityHeaderConfigItem );

        if ( strErrors != null )
        {
            return AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
        }

        _securityHeaderConfigService.update( securityHeaderConfigItem );
        
        return redirect( request, getModifySecurityHeaderConfigUrl( request ) );
    }
	
	/**
     * Process Modification Form Data.
     * 
     * @param request
     *            The HTTP request
     * @param securityheader
     *            The security header config item
     * @return An Error message or null if no error
     */
	private String processModifyFormData( HttpServletRequest request, SecurityHeaderConfigItem securityHeaderConfigItem )
    {
		securityHeaderConfigItem.setUrlPattern( StringEscapeUtils.unescapeHtml4( request.getParameter( PARAMETER_URL_PATTERN ) ) );
		securityHeaderConfigItem.setHeaderCustomValue( StringEscapeUtils.unescapeHtml4( request.getParameter( PARAMETER_HEADER_CUSTOM_VALUE ) ) );
        
        return processModificationControls( securityHeaderConfigItem );
    }
	
	/**
     * Returns the page of confirmation for deleting a security header config item.
     *
     * @param request
     *            The Http Request
     * @return the confirmation url
     */
	@View( value = VIEW_CONFIRM_REMOVE_SECURITYHEADER_CONFIG_ITEM, securityTokenAction = ACTION_REMOVE_SECURITYHEADER_CONFIG_ITEM )
	public String getConfirmRemoveSecurityHeaderConfigItem( HttpServletRequest request )
	{
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_SECURITYHEADER_CONFIG_ITEM ) );
        url.addParameter( PARAMETER_SECURITY_HEADER_CONFIG_ITEM_ID, Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_CONFIG_ITEM_ID ) ) );
        url.addParameter( PARAMETER_SECURITY_HEADER_ID, request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
		        
        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
	}
	
	/**
     * Processes the data capture form for removing a security header config item.
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     */
	@Action( ACTION_REMOVE_SECURITYHEADER_CONFIG_ITEM )
	public String doRemoveSecurityHeaderConfigItem( HttpServletRequest request )
    {
    	_securityHeaderConfigService.remove( Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_CONFIG_ITEM_ID ) ) );
        
        return redirect( request, getModifySecurityHeaderConfigUrl( request ) );
    }
	
	/**
     * Returns the page to manage security headers.
     * 
     * @param request
     *            The HttpServletRequest
     * @return The HTML code.
     */
    public String getManageSecurityHeaders( HttpServletRequest request )
    {
    	_model.put( MARK_SECURITY_HEADERS_LIST, _securityHeaderService.findAllSorted( getLocale( ) ) );
        HtmlTemplate template = AppTemplateService.getTemplate( SecurityHeaderJspBean.TEMPLATE_MANAGE_SECURITY_HEADERS, getLocale( ), _model );

        return getAdminPage( template.getHtml( ) );
    }
}