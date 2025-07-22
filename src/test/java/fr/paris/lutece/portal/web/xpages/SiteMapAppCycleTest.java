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
package fr.paris.lutece.portal.web.xpages;

import java.io.IOException;
import java.security.SecureRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.style.IPageTemplateRepository;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * Tests that a cycle in the page hierarchy is handled by the site map
 */
public class SiteMapAppCycleTest extends LuteceTestCase
{

    private Page _top;
    private Page _middle;
    private Page _bottom;
    private int _nInitialRootId;
    @Inject
    private IPageService _pageService;
    @Inject
    private SiteMapApp _app;
    @Inject
    private IPageTemplateRepository _pageTemplateRepository;
    
    @BeforeEach
    protected void setUp( ) throws Exception
    {
        // create pages
        _top = getPage( null );
        _middle = getPage( _top.getId( ) );
        _bottom = getPage( _middle.getId( ) );
        // set up the cyle
        _top.setParentPageId( _bottom.getId( ) );
        _pageService.updatePage( _top );
        // change root id
        _nInitialRootId = PortalService.getRootPageId( );
        setRootPageId( _top.getId( ) );
    }

    private Page getPage( Integer parentId )
    {
        Page page = new Page( );
        if ( parentId != null )
        {
            page.setParentPageId( parentId );
        }
        page.setPageTemplateId( _pageTemplateRepository.findAll( ).stream( ).findFirst( ).get( ).getId( ) );
        page.setName( "page" + new SecureRandom( ).nextLong( ) );

        _pageService.createPage( page );

        return page;
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        removePageQuietly( _bottom.getId( ) );
        removePageQuietly( _middle.getId( ) );
        removePageQuietly( _top.getId( ) );
        setRootPageId( _nInitialRootId );
    }

    private void setRootPageId( int nRootPageId ) throws IOException
    {
        AppLogService.info( "Setting root page id to {}", nRootPageId );
        System.setProperty( "lutece.page.root", Integer.toString( nRootPageId ) );
    }

    private void removePageQuietly( int nPageId )
    {
        try
        {
            _pageService.removePage( nPageId );
        }
        catch( Throwable t )
        {
            AppLogService.error( "Could not remove page {}", nPageId, t );
        }
    }

    @Test
    public void testGetPage( )
    {
        XPage res = _app.getPage( new MockHttpServletRequest( ), 0, null );

        assertNotNull( res );
        assertTrue( "SiteMap should contain reference to page", res.getContent( ).contains( _middle.getName( ) ) );
        assertTrue( "SiteMap should contain reference to page", res.getContent( ).contains( _bottom.getName( ) ) );
    }

}
