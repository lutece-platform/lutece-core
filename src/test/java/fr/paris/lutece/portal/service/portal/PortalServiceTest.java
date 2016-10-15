/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.service.portal;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.cache.IPathCacheService;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;

public class PortalServiceTest extends LuteceTestCase
{
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;

    IPathCacheService pathCacheService;
    boolean bPathCacheServiceEnabled;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        pathCacheService = null;
        List<CacheableService> serviceList = CacheService.getCacheableServicesList( );
        for ( CacheableService aService : serviceList )
        {
            if ( aService instanceof IPathCacheService )
            {
                pathCacheService = ( IPathCacheService ) aService;
                bPathCacheServiceEnabled = aService.isCacheEnable( );
                aService.enableCache( true );
                aService.resetCache( );
                break;
            }
        }
        assertNotNull( pathCacheService );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        List<CacheableService> serviceList = CacheService.getCacheableServicesList( );
        for ( CacheableService aService : serviceList )
        {
            if ( aService == pathCacheService )
            {
                aService.resetCache( );
                aService.enableCache( bPathCacheServiceEnabled );
                break;
            }
        }
        pathCacheService = null;
        super.tearDown( );
    }

    public void testGetDefaultPage( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );
        int nMode = 0;

        // PortalService.getDefaultPage( request, nMode );
    }

    public void testGetXPagePathContent( ) throws IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strPath_normal = PortalService.getXPagePathContent( "junit", MODE_NORMAL, request );
        assertEquals( loadExpected( "getXPagePathContent_1.txt" ), strPath_normal );
        assertSame( strPath_normal, PortalService.getXPagePathContent( "junit", MODE_NORMAL, request ) );

        String strPath_admin = PortalService.getXPagePathContent( "junit", MODE_ADMIN, request );
        assertEquals( loadExpected( "getXPagePathContent_1.txt" ), strPath_normal );
        assertNotSame( strPath_admin, strPath_normal );
        assertSame( strPath_admin, PortalService.getXPagePathContent( "junit", MODE_ADMIN, request ) );

        int nPageId = createPage( );
        try
        {
            MockHttpServletRequest request2 = new MockHttpServletRequest( );
            request2.setParameter( Parameters.PAGE_ID, Integer.toString( nPageId ) );
            String strPath_pageid = PortalService.getXPagePathContent( "junit", MODE_NORMAL, request2 );
            assertEquals( loadExpected( "getXPagePathContent_2.txt" ), strPath_pageid );
            assertNotSame( strPath_pageid, strPath_normal );
            assertNotSame( strPath_pageid, strPath_admin );
            assertSame( strPath_pageid, PortalService.getXPagePathContent( "junit", MODE_NORMAL, request2 ) );
        } finally
        {
            removePage( nPageId );
        }
    }

    public void testGetXPagePathContentWithTitleUrls( ) throws IOException
    {
        String strTitleUrls = "<page><page-id>junit</page-id><page-name>junit</page-name></page>";
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strPath_normal = PortalService.getXPagePathContent( "junit", MODE_NORMAL, strTitleUrls, request );
        assertEquals( loadExpected( "getXPagePathContentWithTitleUrls_1.txt" ), strPath_normal );
        assertSame( strPath_normal, PortalService.getXPagePathContent( "junit", MODE_NORMAL, strTitleUrls, request ) );

        String strPath_admin = PortalService.getXPagePathContent( "junit", MODE_ADMIN, strTitleUrls, request );
        assertEquals( loadExpected( "getXPagePathContentWithTitleUrls_1.txt" ), strPath_normal );
        assertNotSame( strPath_admin, strPath_normal );
        assertSame( strPath_admin, PortalService.getXPagePathContent( "junit", MODE_ADMIN, strTitleUrls, request ) );
    }

    private void removePage( int nPageId )
    {
        IPageService pageService = SpringContextService.getBean( "pageService" );
        pageService.removePage( nPageId );
    }

    private int createPage( )
    {
        IPageService pageService = SpringContextService.getBean( "pageService" );
        Page page = new Page( );
        page.setName( "junit2" );
        page.setDescription( "junit2" );
        page.setParentPageId( PortalService.getRootPageId( ) );
        pageService.createPage( page );
        return page.getId( );
    }

    private String loadExpected( String strExpectedFileName ) throws IOException
    {
        try ( Scanner s = new Scanner(
                this.getClass( ).getResourceAsStream( this.getClass( ).getSimpleName( ) + "_" + strExpectedFileName ),
                "UTF-8" ) )
        {
            Scanner delimited = s.useDelimiter( "\\A" );
            return delimited.hasNext( ) ? delimited.next( ) : "";
        }

    }
}
