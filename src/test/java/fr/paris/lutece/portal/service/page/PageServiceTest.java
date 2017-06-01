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
package fr.paris.lutece.portal.service.page;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;

public class PageServiceTest extends LuteceTestCase
{
    private boolean _pageCacheStatus;

    @Override
    public void setUp( ) throws Exception
    {
        super.setUp( );

        // activate the page cache
        List<CacheableService> services = CacheService.getCacheableServicesList( );

        for ( CacheableService cs : services )
        {
            if ( cs instanceof PageCacheService )
            {
                _pageCacheStatus = cs.isCacheEnable( );
                cs.enableCache( true );

                break;
            }
        }
    }

    @Override
    public void tearDown( ) throws Exception
    {
        // restore the previous page cache status
        List<CacheableService> services = CacheService.getCacheableServicesList( );

        for ( CacheableService cs : services )
        {
            if ( cs instanceof PageCacheService )
            {
                cs.enableCache( _pageCacheStatus );

                break;
            }
        }

        super.tearDown( );
    }

    /**
     * Tests that the base URL is correct when the page is cached and the URL used to access the site changes
     * 
     * @throws SiteMessageException
     */
    @Test
    public void testBaseUrl( ) throws SiteMessageException
    {
        IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
        HttpServletResponse response = new MockHttpServletResponse( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        LocalVariables.setLocal( null, request, response );
        request.addParameter( Parameters.PAGE_ID, "1" );

        String pageContent = pageService.getPage( request, 0 );
        assertTrue( pageContent.contains( "<base href=\"http://localhost/\">" ) );

        // change server name and thus base_url
        request = new MockHttpServletRequest( );
        request.setServerName( "junit" );

        LocalVariables.setLocal( null, request, response );
        request.addParameter( Parameters.PAGE_ID, "1" );
        pageContent = pageService.getPage( request, 0 );
        assertTrue( pageContent.contains( "<base href=\"http://junit/\">" ) );
    }
}
