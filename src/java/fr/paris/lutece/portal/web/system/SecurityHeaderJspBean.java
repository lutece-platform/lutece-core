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
package fr.paris.lutece.portal.web.system;

import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderHome;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderType;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.securityheader.SecurityHeaderService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage security headers features ( manage, create, modify, remove, activate/deactivate ).
 */
@SessionScoped
@Named
@Controller( controllerJsp = "ManageSecurityHeaders.jsp", controllerPath = "jsp/admin/system/", right = "CORE_SECURITY_HEADER_MANAGEMENT" )
public class SecurityHeaderJspBean extends MVCAdminJspBean
{
    // Templates
    private static final String TEMPLATE_CREATE_SECURITYHEADER = "admin/system/create_securityheader.html";
    private static final String TEMPLATE_MODIFY_SECURITYHEADER = "admin/system/modify_securityheader.html";
    
    // Markers
    private static final String MARK_SECURITY_HEADERS_LIST = "security_headers_list";
    private static final String MARK_SECURITY_HEADER = "securityheader";
    private static final String MARK_TYPES_LIST = "types_list";
    private static final String MARK_TYPE_SELECTED = "selected_type";
    private static final String MARK_PAGE_CATEGORY_LIST = "page_category_list";
    private static final String MARK_PAGE_CATEGORY_SELECTED = "selected_pageCategory";
    private static final String MARK_ERRORS = "errors";
    
    // Properties
    private static final String PROPERTY_CREATE_SECURITYHEADER_PAGETITLE = "portal.securityheader.create_securityheader.pageTitle";
    private static final String PROPERTY_MODIFY_SECURITYHEADER_PAGETITLE = "portal.securityheader.modify_securityheader.pageTitle";   
    private static final String MESSAGE_ACTIVE_HEADER_NOT_EDITABLE = "portal.securityheader.message.activeHeaderNotEditable";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.securityheader.message.confirmRemoveSecurityHeader";
    private static final String MESSAGE_HEADER_ALREADY_EXIST = "portal.securityheader.message.securityHeadersAlreadyexists";    
    private static final String MESSAGE_PAGE_CATEGORY_REQUIRED_WHEN_PAGE_IS_TYPE = "portal.securityheader.message.pageCategoryRequiredTypePage";
    private static final String MESSAGE_TYPE_UNKNOWN = "portal.securityheader.message.typeUnknown";
    private static final String MESSAGE_PAGE_CATEGORY_UNKNOWN = "portal.securityheader.message.pageCategoryUnknown";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "portal.securityheader.model.entity.securityheader.attribute.";
    
    // Views
    private static final String VIEW_MANAGE_SECURITYHEADERS = "manageSecurityHeaders";
    private static final String VIEW_CREATE_SECURITYHEADER = "createSecurityHeader";
    private static final String VIEW_MODIFY_SECURITYHEADER = "modifySecurityHeader";
    private static final String VIEW_CONFIRM_REMOVE_SECURITYHEADER = "confirmRemoveSecurityHeader";
    private static final String VIEW_MESSAGE_NOT_EDITABLE_SECURITYHEADER = "messageNotEditableSecurityHeader";
    
    // Actions
    private static final String ACTION_CREATE_SECURITYHEADER = "createSecurityHeader";
    private static final String ACTION_MODIFY_SECURITYHEADER = "modifySecurityHeader";
    private static final String ACTION_REMOVE_SECURITYHEADER = "removeSecurityHeader";
    private static final String ACTION_ENABLE_SECURITYHEADER = "enableSecurityHeader";
    private static final String ACTION_DISABLE_SECURITYHEADER = "disableSecurityHeader";
    
    // Template Files path
    private static final String TEMPLATE_MANAGE_SECURITY_HEADERS = "admin/system/manage_security_headers.html";
    
    // Parameters
    private static final String PARAMETER_SECURITY_HEADER_ID = "id_securityheader";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_VALUE = "value";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_TYPE = "type";
    private static final String PARAMETER_PAGE_CATEGORY  = "pageCategory";
    
    // Jsp definition
    public static final String JSP_MANAGE_SECURITY_HEADERS = "ManageSecurityHeaders.jsp";
    public static final String JSP_REMOVE_SECURITY_HEADERS = "jsp/admin/system/DoRemoveSecurityHeader.jsp";
  
    private static final long serialVersionUID = 7010476999488231065L;
    
    @Inject
    private SecurityHeaderService _securityHeaderService;
    
    /**
     * Returns the page to manage security headers.
     * 
     * @param request
     *            The HttpServletRequest
     * @return The HTML code.
     */
    @View( value = VIEW_MANAGE_SECURITYHEADERS, defaultView = true )
    public String getManageSecurityHeaders( HttpServletRequest request )
    {
    	Map<String, Object> model = createModelForHeadersList( request );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_SECURITY_HEADERS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }
    
    /**
     * 
     * Creates the model used for displaying security headers list in manage security headers page.
     * 
     * @param request
     *            The HttpServletRequest
     * @return model map
     */
    private Map<String, Object> createModelForHeadersList( HttpServletRequest request )
    {
    	Map<String, Object> model = getModel( );
        model.put( MARK_SECURITY_HEADERS_LIST, _securityHeaderService.findAllSorted( getLocale( ) ) );
        
        return model;
    }
    
    /**
     * Returns the security header creation page.
     * 
     * @param request
     *            the http request
     * @return the html code for the securityheader creation page
     */
    @View( value = VIEW_CREATE_SECURITYHEADER )
    public String getCreateSecurityHeader( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_SECURITYHEADER_PAGETITLE );
        Map<String, Object> model = createModelForHeaderCreation( request );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_SECURITYHEADER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }
    
    /**
     * 
     * Creates the model used for adding a new security header.
     * 
     * @param request
     *            The HttpServletRequest
     * @return model map
     */
    private Map<String, Object> createModelForHeaderCreation( HttpServletRequest request )
    {
    	ReferenceList listTypes = _securityHeaderService.getTypeList( );
        ReferenceList listPageCategories = _securityHeaderService.getPageCategoryList( );
   
        Map<String, Object> model = getModel( );
        model.put( MARK_TYPES_LIST, listTypes );
        model.put( MARK_PAGE_CATEGORY_LIST, listPageCategories );
        if ( !listTypes.isEmpty( ) )
        {
            model.put( MARK_TYPE_SELECTED, listTypes.get( 0 ).getCode( ) );
        }
        
        if ( !listPageCategories.isEmpty( ) )
        {
            model.put( MARK_PAGE_CATEGORY_SELECTED, listPageCategories.get( 0 ).getCode( ) );
        }
        
        return model;
    }
    
    /**
     * Process the data capture form for create a security header
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_SECURITYHEADER )
    public String doCreateSecurityHeader( HttpServletRequest request )
    {
        SecurityHeader securityHeader = new SecurityHeader( );        
        String strErrors = processCreationFormData( request, securityHeader );
        
        if ( strErrors != null )
        {
        	String strMessageUrl = AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
        	return redirect( request, strMessageUrl );
        }
        
        _securityHeaderService.create( securityHeader );

        return redirectView( request, VIEW_MANAGE_SECURITYHEADERS );
    }
    
    /**
     * Process Creation Form Data.
     * 
     * @param request
     *            The HTTP request
     * @param securityheader
     *            The security header
     * @return An Error message or null if no error
     */
    private String processCreationFormData( HttpServletRequest request, SecurityHeader securityHeader )
    {
        securityHeader.setName( request.getParameter( PARAMETER_NAME ) );
        securityHeader.setValue( request.getParameter( PARAMETER_VALUE ) );
        securityHeader.setDescription( request.getParameter( PARAMETER_DESCRIPTION ) );
        securityHeader.setType( request.getParameter( PARAMETER_TYPE ) );
        securityHeader.setPageCategory( getPageCategory( request.getParameter( PARAMETER_PAGE_CATEGORY ), securityHeader.getType( ) ) );
        
        String strErrors = processCommonControls( securityHeader );
        if( strErrors != null )
        {
        	return strErrors;
        }

        if( !isSecurityHeaderToCreateUnique( securityHeader ) )
        {
        	return MESSAGE_HEADER_ALREADY_EXIST;
        }
        
        return null;
    }
    
    
    /**
     * Executes common controls between creation and modification actions on request parameters
     * 
     * @param securityHeader
     *           Security header to control
     * @return null if controls are passed, an string containing an error message otherwise
     */
    private String processCommonControls( SecurityHeader securityHeader )
    {
        
    	if( !validateBean( securityHeader, VALIDATION_ATTRIBUTES_PREFIX ) )
    	{
    		List<ErrorMessage> listErrors = ( List<ErrorMessage> ) getModel( ).get( MARK_ERRORS );
    		return listErrors.get( 0 ).getMessage( );
    	}

    	if( !isTypeBelongsToReferenceList ( securityHeader.getType( ) ) )
    	{
    		return MESSAGE_TYPE_UNKNOWN;
    	}
    	
    	if( securityHeader.getPageCategory( ) != null && !isPageCategoryBelongsToReferenceList ( securityHeader.getPageCategory( ) ) )
    	{
    		return MESSAGE_PAGE_CATEGORY_UNKNOWN;
    	}
    	
        if( SecurityHeaderType.PAGE.getCode( ).equals( securityHeader.getType( ) ) && StringUtils.isEmpty( securityHeader.getPageCategory( ) ) )
        {
        	return MESSAGE_PAGE_CATEGORY_REQUIRED_WHEN_PAGE_IS_TYPE;
        }
        
        return null;
    }
    
    /**
     * Checks if specified type is found in the reference list of security headers types 
     * 
     * @param strType
     *         THe security header type
     * @return true if type belongs to the reference types list, false otherwise
     */
    private boolean isTypeBelongsToReferenceList( String strType )
    {
    	for (ReferenceItem referenceType : _securityHeaderService.getTypeList( ) )
    	{
    		if ( strType.equals( referenceType.getCode() ) )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks if specified page category is found in the reference list of security headers page categories 
     * 
     * @param strPageCategory
     *         The security header page category
     * @return true if page category belongs to the reference types list, false otherwise
     */
    private boolean isPageCategoryBelongsToReferenceList( String strPageCategory )
    {
    	for (ReferenceItem referencePageCategory : _securityHeaderService.getPageCategoryList( ) )
    	{
    		if ( strPageCategory.equals( referencePageCategory.getCode() ) )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Checks if the security header to create doesn't already exist to prevent duplicates.
     * The criteria used to determine if a security header is unique are name, type and page category (in case of security header type is equals to page).
     * 
     * @param securityHeaderToCreate
     *            The security header to create
     * @return true if the security header doesn't exist, false otherwise
     */
    private boolean isSecurityHeaderToCreateUnique( SecurityHeader securityHeaderToCreate )
    {	
    	return _securityHeaderService.find( securityHeaderToCreate.getName( ), securityHeaderToCreate.getType( ) , securityHeaderToCreate.getPageCategory( ) ).isEmpty( );
    }
    
    /**
     * Returns the security header modification page.
     * 
     * @param request
     *            the http request
     * @return the html code for the securityheader modification page
     */
    @View( value = VIEW_MODIFY_SECURITYHEADER )
    public String getModifySecurityHeader( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_SECURITYHEADER_PAGETITLE );

        String strSecurityHeaderId = request.getParameter( PARAMETER_SECURITY_HEADER_ID );

        if ( !StringUtils.isNumeric( strSecurityHeaderId ) )
        {
            AppLogService.error( " {} is not a valid security header id.", ( ) -> SecurityUtil.logForgingProtect( strSecurityHeaderId ) );

            return getManageSecurityHeaders( request );
        }
        
        SecurityHeader securityHeader = getSecurityHeaderForModification( strSecurityHeaderId );
        if ( securityHeader == null )
        {
            AppLogService.error( "{} is not a valid security header id.", ( ) -> SecurityUtil.logForgingProtect( strSecurityHeaderId ) );

            return getManageSecurityHeaders( request );
        }

        Map<String, Object> model = createModelForHeaderModification( request, securityHeader );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_SECURITYHEADER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * This method returns the security header to modify. Value field is escaped because some headers values can have double quotes 
     * (Clear-site-data header for instance) and in this case, the value is not displayed on the modification screen
     * 
     * @param strSecurityHeaderId
     *          The security header id
     * @return security header to modify
     */
    private SecurityHeader getSecurityHeaderForModification( String strSecurityHeaderId )
    {
    	SecurityHeader securityHeader = SecurityHeaderHome.findByPrimaryKey( Integer.parseInt( strSecurityHeaderId ) );
    	if( securityHeader != null )
    	{
            securityHeader.setValue( StringEscapeUtils.escapeHtml4( securityHeader.getValue( ) ) );
    	}
        
        return securityHeader;
    }
    
    /**
     * Returns the message page that informs that a security header is not editable.
     *
     * @param request
     *            The Http Request
     * @return the confirmation url
     */
    @View( value = VIEW_MESSAGE_NOT_EDITABLE_SECURITYHEADER )
    public String getMessageNotEditableSecurityHeader( HttpServletRequest request )
    {  	 
    	String strMessageUrl =  AdminMessageService.getMessageUrl( request, MESSAGE_ACTIVE_HEADER_NOT_EDITABLE, AdminMessage.TYPE_INFO );
    	return redirect( request, strMessageUrl );   	
    }
      
    /**
     * 
     * Creates the model used for modifying a security header.
     * 
     * @param request
     *            The HttpServletRequest
     * @return model map
     */
	private Map<String, Object> createModelForHeaderModification(HttpServletRequest request, SecurityHeader securityHeader) 
	{
		ReferenceList listTypes = _securityHeaderService.getTypeList( );
        ReferenceList listPageCategories = _securityHeaderService.getPageCategoryList( );

        Map<String, Object> model = getModel( );
        model.put( MARK_TYPES_LIST, listTypes );
        model.put( MARK_PAGE_CATEGORY_LIST, listPageCategories );        
        model.put( MARK_TYPE_SELECTED, securityHeader.getType() );
        String selectedCategory = null;
        if(securityHeader.getType().equals(SecurityHeaderType.PAGE.getCode()))
        {
        	selectedCategory = securityHeader.getPageCategory();
        }
        else
        {
        	selectedCategory = listPageCategories.get( 0 ).getCode( );
        }
        model.put( MARK_PAGE_CATEGORY_SELECTED, selectedCategory );
        model.put( MARK_SECURITY_HEADER, securityHeader );

        return model;
	}
    
    /**
     * Processes the data capture form for modifying a security header.
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     */
	@Action( value = ACTION_MODIFY_SECURITYHEADER )
    public String doModifySecurityHeader( HttpServletRequest request )
    {
		int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
	    SecurityHeader securityHeader = SecurityHeaderHome.findByPrimaryKey( nId );

	    String strErrors = processModifyFormData( request, securityHeader );

	    if ( strErrors != null )
	    {
	        String strMessageUrl = AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
	        return redirect( request, strMessageUrl );
	    }

	    _securityHeaderService.update( securityHeader );		

        return redirectView( request, VIEW_MANAGE_SECURITYHEADERS );
    }
    
    /**
     * Processes Modify Form Data.
     * 
     * @param request
     *            The HTTP request
     * @param securityheader
     *            The security header
     * @return An Error message or null if no error
     */
    private String processModifyFormData( HttpServletRequest request, SecurityHeader securityHeader )
    {
    	securityHeader.setName( request.getParameter( PARAMETER_NAME ) );
    	securityHeader.setValue( request.getParameter( PARAMETER_VALUE ) );
    	securityHeader.setDescription( request.getParameter( PARAMETER_DESCRIPTION ) );
    	securityHeader.setType( request.getParameter( PARAMETER_TYPE ) );
    	securityHeader.setPageCategory( getPageCategory( request.getParameter( PARAMETER_PAGE_CATEGORY ), securityHeader.getType( ) ) );
        
        String strErrors = processCommonControls( securityHeader );
        if( strErrors != null )
        {
        	return strErrors;
        }
        
        if( !isSecurityHeaderToModifyUnique( securityHeader, securityHeader.getName( ), securityHeader.getType( ), securityHeader.getPageCategory( )  ) )
        {
        	return MESSAGE_HEADER_ALREADY_EXIST;
        }
        
        return null;
    }
    
    /**
     * Returns security header page category from request if security header type is page. 
     * If type is REST api, page category is not relevant and should not be filled so the method returns null.
     * 
     * @param strType
     * @param strPageCategoryFromRequest
     * @return
     */
    private String getPageCategory( String strPageCategoryFromRequest, String strType )
    { 	
    	if( SecurityHeaderType.PAGE.getCode( ).equals( strType ) )
    	{
    		return strPageCategoryFromRequest ;
    	}
    	
    	return null;
    }
    
    /**
     * Checks if the security header to modify will be always unique after modifications are applied to prevent duplicates.
     * The criteria used to determine if a security header is unique are name, type and page category (in case of security header type is equals to page).
     * 
     * @param securityHeaderToModify
     *            The security header to modify
     * @param strName
     *            The name to set  
     * @param strType
     *            The type to set
     * @param strPageCategory
     *            The page category to set
     * @return true if the security header after modification will be still unique, false otherwise
     */
    private boolean isSecurityHeaderToModifyUnique( SecurityHeader securityHeaderToModify, String strName, String strType, String strPageCategory )
    {	
    	for( SecurityHeader securityHeader : _securityHeaderService.find( strName, strType, strPageCategory ) )
    	{
    		if( securityHeaderToModify.getId( ) != securityHeader.getId( ) )
    		{
    			return false;
    		}   			
    	}
    	
    	return true;
    }
    
    /**
     * Returns the page of confirmation for deleting a security header.
     *
     * @param request
     *            The Http Request
     * @return the confirmation url
     */
    @View( value = VIEW_CONFIRM_REMOVE_SECURITYHEADER, securityTokenAction = ACTION_REMOVE_SECURITYHEADER )
    public String getConfirmRemoveSecurityHeader( HttpServletRequest request )
    {  	
    	int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_SECURITYHEADER ) );
        url.addParameter( PARAMETER_SECURITY_HEADER_ID, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }
    
    /**
     * Processes the data capture form for removing a security header.
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_REMOVE_SECURITYHEADER )
    public String doRemoveSecurityHeader( HttpServletRequest request )
    {
    	String strId = request.getParameter( PARAMETER_SECURITY_HEADER_ID );
    	_securityHeaderService.remove( Integer.parseInt( strId ) );
    	
    	return redirectView( request, VIEW_MANAGE_SECURITYHEADERS );
    }
    
    /**
     * Enable a security header.
     * 
     * @param request
     *            The HTTP request
     * @return The forward URL
     */
    @Action( value = ACTION_ENABLE_SECURITYHEADER, securityTokenDisabled = true )
    public String doEnableSecurityHeaderAction( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        _securityHeaderService.enable( nId );

        return redirectView( request, VIEW_MANAGE_SECURITYHEADERS );
    }
    
    /**
     * Disable a security header.
     * 
     * @param request
     *            The HTTP request
     * @return The forward URL
     */
    @Action( value = ACTION_DISABLE_SECURITYHEADER, securityTokenDisabled = true )
    public String doDisableSecurityHeaderAction( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        _securityHeaderService.disable( nId );

        return redirectView( request, VIEW_MANAGE_SECURITYHEADERS );
    }
}