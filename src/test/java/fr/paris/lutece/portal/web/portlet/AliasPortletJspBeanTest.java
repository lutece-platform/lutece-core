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
package fr.paris.lutece.portal.web.portlet;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.admin.AdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.LuteceTestCase;
import fr.paris.lutece.MokeHttpServletRequest;

/**
 * AliasPortletJspBeanTest Test Class
 * 
 */
public class AliasPortletJspBeanTest extends LuteceTestCase
{
	   private static final String TEST_PAGE_ID = "1"; // home page 
	   private static final String TEST_PORTLET_TYPE_ID = "DOCUMENT_PORTLET"; // portlet type document
	   
    /**
     * Test of doCreate method, of fr.paris.lutece.portal.web.portlet.AliasPortletJspBean.
     */
    public void testDoCreate()
    {
        System.out.println("doCreate");

        // Not implemented yet
    }    
    
    /**
     * Test of doModify method, of fr.paris.lutece.portal.web.portlet.AliasPortletJspBean.
     */
    public void testDoModify()
    {
        System.out.println("doModify");

        // Not implemented yet
    }       
    
    /**
     * Test of testGetCreate method, of class fr.paris.lutece.portal.web.portlet.AliasPortletJspBean.
     */
    public void testGetCreate() throws AccessDeniedException
    {
        System.out.println("testGetCreate");
        
        MokeHttpServletRequest request = new MokeHttpServletRequest();   
        request.addMokeParameters( Parameters.PAGE_ID , TEST_PAGE_ID );
        request.addMokeParameters( Parameters.PORTLET_TYPE_ID, TEST_PORTLET_TYPE_ID );
        request.registerAdminUserWithRigth( new AdminUser() , AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        AliasPortletJspBean instance = new AliasPortletJspBean();
        instance.init( request , AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        instance.getCreate( request );
    }          
     
}
