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
package fr.paris.lutece.portal.service.cache;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

public class PathCacheServiceEnabledTest extends LuteceTestCase
{

    private @Inject PathCacheService service;
    boolean bEnabled;
    
    @BeforeEach
    protected void setUp( ) throws Exception
    {
        bEnabled = service.isCacheEnable( );
        service.enableCache( true );
        assertNotNull( service );
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        service.enableCache( bEnabled );
    }
    
    @Test
    public void testGetKey( )
    {
        Set<String> keys = new HashSet<>( );
        for ( String xpageName : new String [ ] {
                "name", "name2"
        } )
        {
            for ( int mode : new int [ ] {
                    0, 1, 2
            } )
            {
                String key = service.getKey( xpageName, mode, null );
                checkKey( keys, key );
                MockHttpServletRequest request = new MockHttpServletRequest( );
                request.addPreferredLocale( Locale.FRANCE );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addPreferredLocale( Locale.ITALY );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PAGE_ID, "1" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PAGE_ID, "2" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PORTLET_ID, "1" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
                request = new MockHttpServletRequest( );
                request.addParameter( Parameters.PORTLET_ID, "2" );
                key = service.getKey( xpageName, mode, request );
                checkKey( keys, key );
            }
        }
    }

    @Test
    public void testGetKeyWithTitleUrls( )
    {
        Set<String> keys = new HashSet<>( );
        for ( String xpageName : new String [ ] {
                "name", "name2"
        } )
        {
            for ( int mode : new int [ ] {
                    0, 1, 2
            } )
            {
                for ( String titleUrls : new String [ ] {
                        null, "title1", "title2"
                } )
                {
                    String key = service.getKey( xpageName, mode, titleUrls, null );
                    checkKey( keys, key );
                    MockHttpServletRequest request = new MockHttpServletRequest( );
                    request.addPreferredLocale( Locale.FRANCE );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addPreferredLocale( Locale.ITALY );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PAGE_ID, "1" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PAGE_ID, "2" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PORTLET_ID, "1" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                    request = new MockHttpServletRequest( );
                    request.addParameter( Parameters.PORTLET_ID, "2" );
                    key = service.getKey( xpageName, mode, titleUrls, request );
                    checkKey( keys, key );
                }
            }
        }
    }

    @Test
    public void testPutAndGetFromCache( )
    {
        String key = service.getKey( "junit", 0, null );
        service.put( key, "junit" );
        assertEquals( "junit", service.get( key ) );
        assertNull( service.get( "NotInCache" ) );
    }

    @Test
    public void testProcessPageEvent( )
    {
        String key = service.getKey( "junit", 0, null );
        service.put( key, "junit" );
        PageEvent event = new PageEvent( new Page( ), PageEvent.PAGE_CREATED );
        ( (PathCacheService) service ).processPageEvent( event );
        assertEquals( "junit", service.get( key ) );
        for ( int nEventType : new int [ ] {
                PageEvent.PAGE_CONTENT_MODIFIED, PageEvent.PAGE_DELETED, PageEvent.PAGE_MOVED, PageEvent.PAGE_STATE_CHANGED
        } )
        {
            service.put( key, "junit" );
            event = new PageEvent( new Page( ), nEventType );
            ( (PathCacheService) service ).processPageEvent( event );
            assertNull( service.get( key ) );
        }
    }

    @Test
    public void testRegisteredPageEventListener( )
    {
        String key = service.getKey( "junit", 0, null );
        service.put( key, "junit" );
        IPageService pageService = CDI.current( ).select( IPageService.class ).get( );
        Page page = new Page( );
        page.setName( getRandomName( ) );
        page.setDescription( page.getName( ) );
        page.setParentPageId( PortalService.getRootPageId( ) );
        pageService.createPage( page );
        try
        {
            // FIXME : change when LUTECE-1834 Adding or removing a page blows up all caches
            // is fixed
            // assertEquals( "junit", service.getFromCache( key ) );
            assertNull( service.get( key ) );
            service.put( key, "junit" );
            page.setDescription( page.getName( ) + page.getName( ) );
            pageService.updatePage( page );
            assertNull( service.get( key ) );
            service.put( key, "junit" );
        }
        finally
        {
            pageService.removePage( page.getId( ) );
            assertNull( service.get( key ) );
        }
    }

    private String getRandomName( )
    {
        Random rand = new SecureRandom( );
        BigInteger bigInt = new BigInteger( 128, rand );
        return "junit" + bigInt.toString( 36 );
    }

    private void checkKey( Set<String> keys, String key )
    {
        assertNotNull( key );
        assertFalse( keys.contains( key ) );
        keys.add( key );
    }
}
