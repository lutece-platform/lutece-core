/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.web.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.PublicUrlResourceIdService;
import fr.paris.lutece.portal.service.security.PublicUrlService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;


/**
 *
 * PublicUrlJspBean used for managing Public Url
 *
 */
public class PublicUrlJspBean extends AdminFeaturesPageJspBean
{
    /**
    *
    */
    public static final String RIGHT_MANAGE = "CORE_PUBLIC_URL_MANAGEMENT";
    private static final long serialVersionUID = -669562727518395523L;

    // Parameters
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_PUBLIC_URL_CODE = "public_url_code";
    private static final String PARAMETER_PUBLIC_URL_VALUE = "public_url_value";

    // Jsp url
    private static final String JSP_MANAGE_PUBLIC_URL = "ManagePublicUrl.jsp";
    private static final String JSP_DO_REMOVE_PUBLIC_URL = "jsp/admin/security/DoRemovePublicUrl.jsp";
    

    // Properties
    private static final String PROPERTY_MANAGE_PUBLIC_URL_PAGETITLE = "portal.security.manage_public_url.pageTitle";

    // Template
    private static final String TEMPLATE_MANAGE_PUBLIC_URL = "admin/security/manage_public_url.html";
    //Message
    private static final String MESSAGE_PUBLIC_URL_CODE_ALREADY_EXIST = "portal.security.messagePublicUrlCodeAlreadyExist";
    private static final String MESSAGE_PUBLIC_URL_CONFIRM_REMOVE= "portal.security.messagePublicUrlConfirmRemove";
    
    

    /**
     * Builds the advanced parameters management page
     * @param request the HTTP request
     * @return the built page
     */
    public String getManageAdvancedParameters( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( PublicUrlService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    PublicUrlResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        setPageTitleProperty( PROPERTY_MANAGE_PUBLIC_URL_PAGETITLE );

        Map<String, Object> model = PublicUrlService.getInstance(  ).getManageAdvancedParameters( getUser(  ), request );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PUBLIC_URL, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

   
    
    /**
     * Create public Url
     * @param request the HTTP request
     * @return the jsp URL of the process result
     * @throws AccessDeniedException if permission to create Public Url
     * on security service has not been granted to the user
     */
    public String doCreatePublicUrl( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( PublicUrlService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    PublicUrlResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
        {
            throw new AccessDeniedException( "User " + getUser(  ) + " is not authorized to permission " +
                PublicUrlResourceIdService.PERMISSION_MANAGE );
        }
       
        if ( 	request.getParameter( PARAMETER_CANCEL ) == null 	 )
        {
           
        	
        	ReferenceItem publicUrlData=getPublicUrlData(request);
        	normalizedPublicUrlCode(publicUrlData);
        	
        	String strError = StringUtils.EMPTY;
        	if ( StringUtils.isBlank( publicUrlData.getCode() ) || StringUtils.isBlank( publicUrlData.getName() ))
             {
                 strError = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
             }
        	else if (DatastoreService.getDataValue(publicUrlData.getCode(), null )!=null)
             {
            		strError = AdminMessageService.getMessageUrl( request, MESSAGE_PUBLIC_URL_CODE_ALREADY_EXIST, AdminMessage.TYPE_STOP );
             }
            

            if (!StringUtils.isBlank(strError))
            {
            	
            	return strError;
        	}
          
            //create public url
           DatastoreService.setDataValue(publicUrlData.getCode(), publicUrlData.getName());
         
            	
        }

        return JSP_MANAGE_PUBLIC_URL;
    }
    
    /**
     * Do Modify Public Url
     * @param request the HTTP request
     * @return the jsp URL of the process result
     * @throws AccessDeniedException if permission to Manage Public Url
     * on security service has not been granted to the user
     */
    public String doModifyPublicUrl( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( PublicUrlService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    PublicUrlResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
        {
            throw new AccessDeniedException( "User " + getUser(  ) + " is not authorized to permission " +
                PublicUrlResourceIdService.PERMISSION_MANAGE );
        }
       
        if ( 	request.getParameter( PARAMETER_CANCEL ) == null )
        {
           
        	
        	ReferenceItem publicUrlData=getPublicUrlData(request);
        	normalizedPublicUrlCode(publicUrlData);
        	
        	String strError = StringUtils.EMPTY;
        	if ( StringUtils.isBlank( publicUrlData.getCode() ) || StringUtils.isBlank( publicUrlData.getName() ) ||DatastoreService.getDataValue(publicUrlData.getCode(), null ) == null)
             {
                 strError = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
             }
        
            

        	 if (!StringUtils.isBlank(strError))
             
            {
            	
            	return strError;
        	}
          
            //updateParameter
        	 DatastoreService.setDataValue(publicUrlData.getCode(), publicUrlData.getName());
            	
        }

        return JSP_MANAGE_PUBLIC_URL;
    }
    
    
    /**
     * Remove Public Url
     * @param request the HTTP request
     * @return the jsp URL of the process result
     * @throws AccessDeniedException if permission manage Public Url
     * on security service has not been granted to the user
     */
    public String doRemovePublicUrl( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( PublicUrlService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    PublicUrlResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
        {
            throw new AccessDeniedException( "User " + getUser(  ) + " is not authorized to permission " +
                PublicUrlResourceIdService.PERMISSION_MANAGE );
        }
       
        	ReferenceItem publicUrlData=getPublicUrlData(request);
        	if(publicUrlData!=null)
        	{
        		normalizedPublicUrlCode(publicUrlData);
        		DatastoreService.removeData(publicUrlData.getCode());
        	} 
            return JSP_MANAGE_PUBLIC_URL;
            	
     }


    

    /**
     * Get the Public Url Data
     * @param request The HTTP request
     * @return ReferenceItem
     */
	private ReferenceItem getPublicUrlData(HttpServletRequest request)
	
	{
		
		ReferenceItem publicUrlData=new ReferenceItem();
		String strPublicUrlCode=request.getParameter(PARAMETER_PUBLIC_URL_CODE)!=null ?request.getParameter(PARAMETER_PUBLIC_URL_CODE).trim():null;
    	String strPublicUrlValue=request.getParameter(PARAMETER_PUBLIC_URL_VALUE)!=null ?request.getParameter(PARAMETER_PUBLIC_URL_VALUE).trim():null;
    	publicUrlData.setCode(strPublicUrlCode);
        publicUrlData.setName(strPublicUrlValue);
		return publicUrlData;
		
	}
	
	
    /**
     * Gets the confirmation page of delete Public Url
     * @param request The HTTP request
     * @throws AccessDeniedException the {@link AccessDeniedException}
     * @return the confirmation page of Remove Public Url
     */
    public String getConfirmRemovePublicUrl( HttpServletRequest request )
        throws AccessDeniedException
    {
    	 if ( !RBACService.isAuthorized( PublicUrlService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                 PublicUrlResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
     {
         throw new AccessDeniedException( "User " + getUser(  ) + " is not authorized to permission " +
             PublicUrlResourceIdService.PERMISSION_MANAGE );
     }
    

        UrlItem url = new UrlItem( JSP_DO_REMOVE_PUBLIC_URL );
        url.addParameter( PARAMETER_PUBLIC_URL_CODE, request.getParameter(PARAMETER_PUBLIC_URL_CODE) );
        
        return AdminMessageService.getMessageUrl( request, MESSAGE_PUBLIC_URL_CONFIRM_REMOVE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
    }
    
    
    /**
     * normalized public url code
     * @param publicUrl publicUrlCode
     */
    private void normalizedPublicUrlCode(ReferenceItem publicUrl)
    {
    	
    	if(!StringUtils.isBlank(publicUrl.getCode()))
    	{
    		String strCode=publicUrl.getCode();
    		strCode=strCode.replaceAll(" ", "_");
    		publicUrl.setCode(PublicUrlService.PUBLIC_URL_PREFIX+strCode);
    	}
    }

	
	
}



