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
package fr.paris.lutece.portal.web.admin;

import java.security.SecureRandom;
import java.util.Locale;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class AdminPageJspBeanTest extends LuteceTestCase
{
    private String _randomPageName;
    private Page _page;
    private AdminPageJspBean _bean;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        _randomPageName = "page" + new SecureRandom( ).nextLong( );
        IPageService pageService = ( IPageService ) SpringContextService.getBean( "pageService" );
        _page = new Page( );
        _page.setParentPageId( PortalService.getRootPageId( ) );
        _page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
        _page.setName( _randomPageName );
        pageService.createPage( _page );
        _bean = new AdminPageJspBean( );
    }

    @Override
    protected void tearDown( )
    {
        IPageService pageService = ( IPageService ) SpringContextService.getBean( "pageService" );
        if ( _page != null )
        {
            try
            {
                pageService.removePage( _page.getId( ) );
            }
            finally
            {
            }
        }
    }

    public void testGetRemovePageNoArgs( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testGetRemovePageNotANumber( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, "foo" );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testGetRemovePageNotExisting( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( 314159265 ) );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testGetRemovePage( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        _bean.getRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertNotNull( message.getRequestParameters( ).get( SecurityTokenService.PARAMETER_TOKEN ) );
        assertEquals( AdminMessage.TYPE_CONFIRMATION, message.getType( ) );
        ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
        for ( ReferenceItem lang : listLanguages )
        {
            assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( _randomPageName ) );
        }
    }

    public void testDoRemovePageNoArgs( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        _bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testDoRemovePageNotANumber( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, "foo" );
        _bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testDoRemovePageNotExisting( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( 314159265 ) );
        _bean.doRemovePage( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    public void testDoRemovePageWithChild( ) throws AccessDeniedException
    {
        String childPageName = _randomPageName + "-child";
        Page childPage = null;
        IPageService pageService = ( IPageService ) SpringContextService.getBean( "pageService" );
        try
        {
            childPage = new Page( );
            childPage.setParentPageId( _page.getId( ) );
            childPage.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
            childPage.setName( childPageName );
            pageService.createPage( childPage );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
            _bean.doRemovePage( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            ReferenceList listLanguages = I18nService.getAdminLocales( Locale.FRANCE );
            for ( ReferenceItem lang : listLanguages )
            {
                assertTrue( message.getText( new Locale( lang.getCode( ) ) ).contains( _randomPageName ) );
            }
        }
        finally
        {
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

    public void testDoRemovePage( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePage.jsp" ) );
        _bean.doRemovePage( request );
        assertFalse( PageHome.checkPageExist( _page.getId( ) ) );
    }

    public void testDoRemovePageNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        try
        {
            _bean.doRemovePage( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( PageHome.checkPageExist( _page.getId( ) ) );
        }
    }

    public void testDoRemovePageInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.PAGE_ID, Integer.toString( _page.getId( ) ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/site/DoRemovePage.jsp" ) + "b" );
        try
        {
            _bean.doRemovePage( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertTrue( PageHome.checkPageExist( _page.getId( ) ) );
        }
    }
}
