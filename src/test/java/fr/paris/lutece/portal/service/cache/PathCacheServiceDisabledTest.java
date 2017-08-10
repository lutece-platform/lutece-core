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
package fr.paris.lutece.portal.service.cache;

import java.util.List;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.test.LuteceTestCase;

public class PathCacheServiceDisabledTest extends LuteceTestCase
{
    IPathCacheService service;
    boolean bEnabled;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        service = null;
        List<CacheableService> serviceList = CacheService.getCacheableServicesList( );
        for ( CacheableService aService : serviceList )
        {
            if ( aService instanceof IPathCacheService )
            {
                service = (IPathCacheService) aService;
                bEnabled = aService.isCacheEnable( );
                aService.enableCache( false );
                break;
            }
        }
        assertNotNull( service );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        List<CacheableService> serviceList = CacheService.getCacheableServicesList( );
        for ( CacheableService aService : serviceList )
        {
            if ( aService == service )
            {
                aService.enableCache( bEnabled );
                break;
            }
        }
        service = null;
        super.tearDown( );
    }

    public void testGetKey( )
    {
        assertNull( service.getKey( "junit", 0, new MockHttpServletRequest( ) ) );
        assertNull( service.getKey( "junit", 0, "junit", new MockHttpServletRequest( ) ) );
    }

    public void testPutAndGetFromCache( )
    {
        service.putInCache( null, "junit" );
        String key = service.getKey( "junit", 0, null );
        service.putInCache( key, "junit" );
        assertNull( service.getFromCache( key ) );
        assertNull( service.getFromCache( null ) );
        assertNull( service.getFromCache( "NotInCache" ) );
    }

    public void testProcessPageEvent( )
    {
        String key = service.getKey( "junit", 0, null );
        for ( int nEventType : new int [ ] {
                PageEvent.PAGE_CONTENT_MODIFIED, PageEvent.PAGE_CREATED, PageEvent.PAGE_DELETED, PageEvent.PAGE_MOVED, PageEvent.PAGE_STATE_CHANGED
        } )
        {
            service.putInCache( key, "junit" );
            PageEvent event = new PageEvent( new Page( ), nEventType );
            ( (PathCacheService) service ).processPageEvent( event );
            assertNull( service.getFromCache( key ) );
        }
    }

}
