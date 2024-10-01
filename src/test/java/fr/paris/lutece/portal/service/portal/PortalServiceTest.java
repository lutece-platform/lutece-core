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
package fr.paris.lutece.portal.service.portal;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.cache.PathCacheService;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

public class PortalServiceTest extends LuteceTestCase
{
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;

    private @Inject PathCacheService pathCacheService;
    boolean bPathCacheServiceEnabled;
    private @Inject IPageService pageService;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        bPathCacheServiceEnabled = pathCacheService.isCacheEnable( );
        pathCacheService.enableCache( true );
        assertNotNull( pathCacheService );
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        pathCacheService.enableCache( bPathCacheServiceEnabled );
    }

    @Test
    public void testGetXPagePathContent( ) throws IOException
    {
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            String strPath_normal = PortalService.getXPagePathContent( "junit", MODE_NORMAL, request );

            assertSame( strPath_normal, PortalService.getXPagePathContent( "junit", MODE_NORMAL, request ) );

            String strPath_admin = PortalService.getXPagePathContent( "junit", MODE_ADMIN, request );
            assertNotSame( strPath_admin, strPath_normal );
            assertSame( strPath_admin, PortalService.getXPagePathContent( "junit", MODE_ADMIN, request ) );

            int nPageId = createPage( );
            try
            {
                MockHttpServletRequest request2 = new MockHttpServletRequest( );
                request2.setParameter( Parameters.PAGE_ID, Integer.toString( nPageId ) );
                String strPath_pageid = PortalService.getXPagePathContent( "junit", MODE_NORMAL, request2 );
                assertNotSame( strPath_pageid, strPath_normal );
                assertNotSame( strPath_pageid, strPath_admin );
                assertSame( strPath_pageid, PortalService.getXPagePathContent( "junit", MODE_NORMAL, request2 ) );
            }
            finally
            {
                removePage( nPageId );
            }
        }
        catch( IllegalStateException e )
        {
            fail( "Cache exception, check cache implementation", e );
        }
    }

    @Test
    public void testGetXPagePathContentWithTitleUrls( ) throws IOException
    {
        try
        {
            String strTitleUrls = "<page><page-id>junit</page-id><page-name>junit</page-name></page>";
            MockHttpServletRequest request = new MockHttpServletRequest( );
            String strPath_normal = PortalService.getXPagePathContent( "junit", MODE_NORMAL, strTitleUrls, request );
            assertSame( strPath_normal, PortalService.getXPagePathContent( "junit", MODE_NORMAL, strTitleUrls, request ) );

            String strPath_admin = PortalService.getXPagePathContent( "junit", MODE_ADMIN, strTitleUrls, request );
            assertNotSame( strPath_admin, strPath_normal );
            assertSame( strPath_admin, PortalService.getXPagePathContent( "junit", MODE_ADMIN, strTitleUrls, request ) );
        }
        catch( IllegalStateException e )
        {
            fail("Cache exception, check cache implementation" , e);
        }
    }

    private void removePage( int nPageId )
    {
        pageService.removePage( nPageId );
    }

    private int createPage( )
    {
        Page page = new Page( );
        page.setName( "junit2" );
        page.setDescription( "junit2" );
        page.setParentPageId( PortalService.getRootPageId( ) );
        pageService.createPage( page );
        return page.getId( );
    }
}
