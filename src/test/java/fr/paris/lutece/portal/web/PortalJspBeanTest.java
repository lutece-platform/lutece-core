/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * PortalJspBean Test Class
 *
 */
public class PortalJspBeanTest extends LuteceTestCase
{
    /**
     * Test of getContent method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetContent( ) throws Exception
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( new MockServletConfig( ), request, new MockHttpServletResponse( ) );
        PortalJspBean instance = new PortalJspBean( );

        String result = instance.getContent( request );
        assertTrue( StringUtils.isNotEmpty( result ) );
    }

    /**
     * Test of getStartUpFailurePage method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetStartUpFailurePage( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        PortalJspBean instance = new PortalJspBean( );
        String result = instance.getStartUpFailurePage( request );
        assertTrue( StringUtils.isNotEmpty( result ) );
    }

    /**
     * Test of getError500Page method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetError500Page( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        PortalJspBean instance = new PortalJspBean( );
        String result = instance.getError500Page( request, "Cause" );
        assertTrue( StringUtils.isNotEmpty( result ) );
    }

    /**
     * Test of getError404Page method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetError404Page( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        PortalJspBean instance = new PortalJspBean( );
        String result = instance.getError404Page( request );
        assertTrue( StringUtils.isNotEmpty( result ) );
    }

    /**
     * Test of getCredits method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetCredits( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        PortalJspBean instance = new PortalJspBean( );
        String result = instance.getCredits( request );
        assertTrue( StringUtils.isNotEmpty( result ) );
    }

    /**
     * Test of getLegalInfos method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetLegalInfos( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        PortalJspBean instance = new PortalJspBean( );
        String result = instance.getLegalInfos( request );
        assertTrue( StringUtils.isNotEmpty( result ) );
    }

    /**
     * Test of redirectLogin method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testRedirectLogin( )
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            HttpServletRequest request = new MockHttpServletRequest( );
            PortalJspBean instance = new PortalJspBean( );
            String result = instance.redirectLogin( request );
            assertTrue( StringUtils.isNotEmpty( result ) );
        }
        else
        {
            fail( );
        }
    }

    /**
     * Test of getLoginNextUrl method, of class fr.paris.lutece.portal.web.PortalJspBean.
     */
    public void testGetLoginNextUrl( )
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            HttpServletRequest request = new MockHttpServletRequest( );
            PortalJspBean.getLoginNextUrl( request );
        }
        else
        {
            fail( );
        }
    }

    @Override
    public void tearDown( ) throws Exception
    {
        LocalVariables.setLocal( null, null, null );
        super.tearDown( );
    }
}
