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
package fr.paris.lutece.portal.web;

import fr.paris.lutece.portal.service.security.SecurityService;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.LuteceTestCase;
import fr.paris.lutece.MokeHttpServletRequest;

/**
 * PortalJspBean Test Class
 *
 */
public class PortalJspBeanTest extends LuteceTestCase
{
    
    /**
     * Test of getContent method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetContent() throws Exception
    {
        System.out.println("getContent");
        
        HttpServletRequest request = new MokeHttpServletRequest();
        PortalJspBean instance = new PortalJspBean();
        
        String result = instance.getContent(request);
    }
    
    /**
     * Test of getStartUpFailurePage method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetStartUpFailurePage()
    {
        System.out.println("getStartUpFailurePage");
        
        PortalJspBean instance = new PortalJspBean();
        String result = instance.getStartUpFailurePage();
    }
    
    /**
     * Test of getCredits method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetCredits()
    {
        System.out.println("getCredits");
        
        HttpServletRequest request = new MokeHttpServletRequest();
        PortalJspBean instance = new PortalJspBean();
        String result = instance.getCredits( request );
    }
    
    /**
     * Test of getLegalInfos method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetLegalInfos()
    {
        System.out.println("getLegalInfos");
        
        HttpServletRequest request = new MokeHttpServletRequest();
        PortalJspBean instance = new PortalJspBean();
        String result = instance.getLegalInfos( request );
    }
    
    /**
     * Test of redirectLogin method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testRedirectLogin()
    {
        System.out.println("redirectLogin");
        
        if( SecurityService.isAuthenticationEnable() )
        {
            HttpServletRequest request = new MokeHttpServletRequest();
            PortalJspBean instance = new PortalJspBean();
            String result = instance.redirectLogin(request);
        }
    }
    
    /**
     * Test of getLoginNextUrl method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetLoginNextUrl()
    {
        System.out.println("getLoginNextUrl");
        
        if( SecurityService.isAuthenticationEnable() )
        {
            HttpServletRequest request = new MokeHttpServletRequest();
            PortalJspBean.getLoginNextUrl(request);
        }
    }
    
}
