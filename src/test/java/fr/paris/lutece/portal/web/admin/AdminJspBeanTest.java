/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.LuteceTestCase;
import fr.paris.lutece.MokeHttpServletRequest;

/**
 * AdminJspBeanTest Test Class
 * 
 */
public class AdminJspBeanTest extends LuteceTestCase
{
	  private static final String PARAMETER_PAGE_ID = "page_id"; // home page	
	  private static final String TEST_PAGE_ID = "1"; // home page

	/**
	 * Test of getAdminPage method, of class fr.paris.lutece.portal.web.admin.AdminJspBean.
	 */
	public void testGetAdminPage() throws AccessDeniedException
	{
		System.out.println("getAdminPage");
	        
	    MokeHttpServletRequest request = new MokeHttpServletRequest();	 
	    request.addMokeParameters( PARAMETER_PAGE_ID, TEST_PAGE_ID );
        request.registerAdminUserWithRigth( new AdminUser() , AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
     
	    AdminPageJspBean instance = new AdminPageJspBean();        
        instance.init( request , AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );	    
	    instance.getAdminPage( request );
	}
	
	/**
	 * Test of getAdminPagePreview method, of class fr.paris.lutece.portal.web.admin.AdminJspBean.
	 * @throws UserNotSignedException 
	 */
	public void testGetAdminPagePreview() throws AccessDeniedException, UserNotSignedException
	{
		System.out.println("getAdminPagePreview");
	        
	    MokeHttpServletRequest request = new MokeHttpServletRequest();	 
        request.registerAdminUserWithRigth( new AdminUser() , AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        
	    AdminPageJspBean instance = new AdminPageJspBean();        
        instance.init( request , AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );	    
	    try 
	    {
			instance.getAdminPagePreview( request );
		} catch (SiteMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
