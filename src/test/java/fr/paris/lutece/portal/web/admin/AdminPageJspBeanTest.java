/*
 * Copyright (c) 2015, Mairie de Paris
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

import java.security.SecureRandom;
import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class AdminPageJspBeanTest extends LuteceTestCase
{
    public void testGetRemovePage( )
    {
        AdminPageJspBean bean = new AdminPageJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        // no args
        bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
        // not a number
        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, "foo" );
        bean.getRemovePage( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
        // not an existing page
        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( 314159265 ) );
        bean.getRemovePage( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
        // valid page
        String randomPageName = "page" + new SecureRandom( ).nextLong( );
        Page page = null;
        IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
        try
        {
            page = new Page( );
            page.setParentPageId( PortalService.getRootPageId( ) );
            page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
            page.setName( randomPageName );
            pageService.createPage( page );
            request = new MockHttpServletRequest( );
            request.addParameter( Parameters.PAGE_ID, Integer.toString( page.getId( ) ) );
            bean.getRemovePage( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_CONFIRMATION, message.getType( ) );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( randomPageName ) );
            }
        }
        finally
        {
            if ( page != null )
            {
                try
                {
                    pageService.removePage( page.getId( ) );
                }
                finally
                {
                }
            }
        }
    }

    public void testDoRemovePage( )
    {
        AdminPageJspBean bean = new AdminPageJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        // no args
        bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
        // not a number
        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, "foo" );
        bean.doRemovePage( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
        // not an existing page
        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( 314159265 ) );
        bean.doRemovePage( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
        // valid page with child
        String randomPageName = "page" + new SecureRandom( ).nextLong( );
        String childPageName = randomPageName + "-child";
        Page page = null;
        Page childPage = null;
        IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
        try
        {
            page = new Page( );
            page.setParentPageId( PortalService.getRootPageId( ) );
            page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
            page.setName( randomPageName );
            pageService.createPage( page );
            childPage = new Page( );
            childPage.setParentPageId( page.getId( ) );
            childPage.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
            childPage.setName( childPageName );
            pageService.createPage( childPage );
            request = new MockHttpServletRequest( );
            request.addParameter( Parameters.PAGE_ID, Integer.toString( page.getId( ) ) );
            bean.doRemovePage( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( randomPageName ) );
            }
            // valid page without child
            request = new MockHttpServletRequest( );
            request.addParameter( Parameters.PAGE_ID, Integer.toString( childPage.getId( ) ) );
            bean.doRemovePage( request );
            assertFalse( PageHome.checkPageExist( childPage.getId( ) ) );
        }
        finally
        {
            if ( page != null )
            {
                try
                {
                    pageService.removePage( page.getId( ) );
                }
                finally
                {
                }
            }
            if ( childPage != null )
            {
                try
                {
                    pageService.removePage( childPage.getId( ) );
                }
                finally
                {
                }
            }
        }
    }
}
