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

import java.util.HashMap;
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
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.securityheader.SecurityHeaderService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.util.ErrorMessage;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;

/**
 * This class provides the user interface to manage security headers features ( manage, create, modify, remove, activate/deactivate ).
 */
@SessionScoped
@Named
public class SecurityHeaderJspBean extends MVCAdminJspBean
{
    // Rights
    public static final String RIGHT_SECURITY_HEADER_MANAGEMENT = "CORE_SECURITY_HEADER_MANAGEMENT";

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
    
    // Template Files path
    private static final String TEMPLATE_MANAGE_SECURITY_HEADERS = "admin/system/manage_security_headers.html";
    
    // Parameters
    private static final String PARAMETER_SECURITY_HEADER_ID = "id_securityheader";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_VALUE = "value";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_TYPE = "type";
    private static final String PARAMETER_PAGE_CATEGORY  = "pageCategory";
    private static final String PARAMETER_ACTION = "action";
    
    // Jsp definition
    public static final String JSP_MANAGE_SECURITY_HEADERS = "ManageSecurityHeaders.jsp";
    public static final String JSP_REMOVE_SECURITY_HEADERS = "jsp/admin/system/DoRemoveSecurityHeader.jsp";
    
    // Actions
    private static final String ACTION_ENABLE = "ENABLE";
    private static final String ACTION_DISABLE = "DISABLE";
  
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
    public String getManageSecurityHeaders( HttpServletRequest request )
    {
    	HashMap<String, Object> model = createModelForHeadersList( request );
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
    private HashMap<String, Object> createModelForHeadersList( HttpServletRequest request )
    {
    	HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_SECURITY_HEADERS_LIST, _securityHeaderService.findAllSorted( getLocale( ) ) );
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_MANAGE_SECURITY_HEADERS ) );
        
        return model;
    }
    
    /**
     * Returns the security header creation page.
     * 
     * @param request
     *            the http request
     * @return the html code for the securityheader creation page
     */
    public String getCreateSecurityHeader( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_SECURITYHEADER_PAGETITLE );
        HashMap<String, Object> model = createModelForHeaderCreation( request );
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
    private HashMap<String, Object> createModelForHeaderCreation( HttpServletRequest request )
    {
    	ReferenceList listTypes = _securityHeaderService.getTypeList( );
        ReferenceList listPageCategories = _securityHeaderService.getPageCategoryList( );
   
        HashMap<String, Object> model = new HashMap<>( );
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
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_CREATE_SECURITYHEADER ) );
        
        return model;
    }
    
    /**
     * Process the data capture form for create a security header
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             If the security token is invalid
     */
    public String doCreateSecurityHeader( HttpServletRequest request ) throws AccessDeniedException
    {
        SecurityHeader securityHeader = new SecurityHeader( );        
        String strErrors = processCreationFormData( request, securityHeader );
        
        if ( strErrors != null )
        {
            return AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
        }

        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_CREATE_SECURITYHEADER ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        
        _securityHeaderService.create( securityHeader );

        return JSP_MANAGE_SECURITY_HEADERS;
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

        HashMap<String, Object> model = createModelForHeaderModification( request, securityHeader );
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
    public String getMessageNotEditableSecurityHeader( HttpServletRequest request )
    {  	 
        return AdminMessageService.getMessageUrl( request, MESSAGE_ACTIVE_HEADER_NOT_EDITABLE, AdminMessage.TYPE_INFO );
    }
      
    /**
     * 
     * Creates the model used for modifying a security header.
     * 
     * @param request
     *            The HttpServletRequest
     * @return model map
     */
	private HashMap<String, Object> createModelForHeaderModification(HttpServletRequest request, SecurityHeader securityHeader) 
	{
		ReferenceList listTypes = _securityHeaderService.getTypeList( );
        ReferenceList listPageCategories = _securityHeaderService.getPageCategoryList( );

        HashMap<String, Object> model = new HashMap<>( );
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
        model.put( SecurityTokenService.MARK_TOKEN, getSecurityTokenService( ).getToken( request, TEMPLATE_MODIFY_SECURITYHEADER ) );
		return model;
	}
    
    /**
     * Processes the data capture form for modifying a security header.
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifySecurityHeader( HttpServletRequest request ) throws AccessDeniedException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        SecurityHeader securityHeader = SecurityHeaderHome.findByPrimaryKey( nId );

        String strErrors = processModifyFormData( request, securityHeader );

        if ( strErrors != null )
        {
            return AdminMessageService.getMessageUrl( request, strErrors, AdminMessage.TYPE_STOP );
        }
        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_MODIFY_SECURITYHEADER ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        _securityHeaderService.update( securityHeader );

        return getHomeUrl( request );
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
    public String getConfirmRemoveSecurityHeader( HttpServletRequest request )
    {  	
    	Map<String, String> parameters = new HashMap<>( );
        parameters.put( PARAMETER_SECURITY_HEADER_ID, request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN, getSecurityTokenService( ).getToken( request, JSP_REMOVE_SECURITY_HEADERS ) );
        
        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, JSP_REMOVE_SECURITY_HEADERS, AdminMessage.TYPE_CONFIRMATION, parameters );
    }
    
    /**
     * Processes the data capture form for removing a security header.
     *
     * @param request
     *            The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doRemoveSecurityHeader( HttpServletRequest request ) throws AccessDeniedException
    {
    	if ( !getSecurityTokenService( ).validate( request, JSP_REMOVE_SECURITY_HEADERS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
    	String strId = request.getParameter( PARAMETER_SECURITY_HEADER_ID );
    	_securityHeaderService.remove( Integer.parseInt( strId ) );
    	
    	return JSP_MANAGE_SECURITY_HEADERS;
    }
    
    /**
     * Processes the security headers actions (enable and disable).
     * 
     * @param request
     *            The HTTP request
     * @return The forward URL
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doSecurityHeaderAction( HttpServletRequest request ) throws AccessDeniedException
    {
        String strAction = request.getParameter( PARAMETER_ACTION );
        int nId = Integer.parseInt( request.getParameter( PARAMETER_SECURITY_HEADER_ID ) );
        
        if ( !getSecurityTokenService( ).validate( request, TEMPLATE_MANAGE_SECURITY_HEADERS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        switch( strAction )
        {
            case ACTION_ENABLE:
            	_securityHeaderService.enable( nId );
                break;
            case ACTION_DISABLE:
            	_securityHeaderService.disable( nId );
                break;
            default:
                AppLogService.error( "Unknown security header action : {}", strAction );
        }

        return getHomeUrl( request );
    }
    
}