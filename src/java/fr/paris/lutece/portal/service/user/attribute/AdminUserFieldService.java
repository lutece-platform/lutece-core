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
package fr.paris.lutece.portal.service.user.attribute;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AbstractAttribute;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Messages;

/**
 * 
 * AdminUserFieldService
 *
 */
public class AdminUserFieldService 
{
	// CONSTANTS
	private static final String CONSTANT_EMPTY_STRING = "";
	
	// PARAMETERS
	private static final String PARAMETER_ATTRIBUTE = "attribute_";
	
	/**
	 * Check if the user fields are correctly filled
	 * @param request HttpServletRequest
	 * @param locale locale
	 * @return null if there are no problem
	 */
	public static String checkUserFields( HttpServletRequest request, Locale locale )
	{
		// Specific attributes
        List<AbstractAttribute> listAttributes = AttributeHome.findAll( locale );
        for ( AbstractAttribute attribute : listAttributes )
        {
        	String value = request.getParameter( PARAMETER_ATTRIBUTE +  attribute.getIdAttribute(  ) );
        	if ( attribute.isMandatory(  ) && ( value == null || value.equals( CONSTANT_EMPTY_STRING ) ) )
        	{
        		return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        	}
        }
        
        return null;
	}
	
	/**
	 * Create the user fields
	 * @param user Adminuser
	 * @param request HttpServletRequest
	 * @param locale locale
	 */
	public static void doCreateUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
		// Attributes created in the Back-Office
		List<AbstractAttribute> listAttributes = AttributeHome.findCoreAttributes( locale );
        for ( AbstractAttribute attribute : listAttributes )
        {
        	List<AdminUserField> listUserFields = attribute.getUserFieldsData( request, user );
        	for ( AdminUserField userField : listUserFields )
        	{
        		if ( userField != null )
        		{
        			AdminUserFieldHome.create( userField );
        		}
        	}
        }
        
        // Attributes associated to the plugins
        for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService.getBeansOfType( AdminUserFieldListenerService.class ) )
        {
        	adminUserFieldListenerService.doCreateUserFields( user, request, locale );
        }
    }

	/**
	 * Modify the user fields
	 * @param user AdminUser
	 * @param request HttpServletRequest
	 * @param locale locale
	 * @param currentUser current user
	 */
	public static void doModifyUserFields( AdminUser user, HttpServletRequest request, Locale locale, AdminUser currentUser )
	{
		// Remove all user fields
		AdminUserFieldHome.removeUserFieldsFromIdUser( user.getUserId(  ) );
		
		// Attributes created in the Back-Office
		List<AbstractAttribute> listAttributes = AttributeHome.findCoreAttributes( locale );
        for ( AbstractAttribute attribute : listAttributes )
        {
        	List<AdminUserField> listUserFields = attribute.getUserFieldsData( request, user );
        	for ( AdminUserField userField : listUserFields )
        	{
        		if ( userField != null )
            	{
            		AdminUserFieldHome.create( userField );
            	}
        	}
        }
        
        // Attributes associated to the plugins
        for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService.getBeansOfType( AdminUserFieldListenerService.class ) )
        {
        	adminUserFieldListenerService.doModifyUserFields( user, request, locale, currentUser );
        }
	}

	/**
	 * Remove the user fields
	 * @param user Adminuser
	 * @param request HttpServletRequest
	 * @param locale locale
	 */
	public static void doRemoveUserFields( AdminUser user, HttpServletRequest request, Locale locale )
	{
		AdminUserFieldHome.removeUserFieldsFromIdUser( user.getUserId(  ) );
		
		// Attributes associated to the plugins
        for ( AdminUserFieldListenerService adminUserFieldListenerService : SpringContextService.getBeansOfType( AdminUserFieldListenerService.class ) )
        {
        	adminUserFieldListenerService.doRemoveUserFields( user, request, locale );
        }
	}
}
