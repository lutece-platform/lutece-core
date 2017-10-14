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
package fr.paris.lutece.portal.web.system;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.event.CacheEventListenerAdapter;

/**
 * SystemJspBean Test Class
 *
 */
public class CacheJspBeanTest extends LuteceTestCase
{

    /**
     * Test of getManageCaches method, of class
     * fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testGetManageCaches( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );

        CacheJspBean instance = new CacheJspBean( );
        instance.init( request, CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        assertNotNull( instance.getManageCaches( request ) );
    }

    /**
     * Test of doResetCaches method, of class
     * fr.paris.lutece.portal.web.system.SystemJspBean.
     * 
     * @throws AccessDeniedException
     */
    public void testDoResetCaches( ) throws AccessDeniedException
    {
        CacheListener cacheEventListener = new CacheListener( );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) )
                .map( service -> ( ( AbstractCacheableService ) service ).getCache( )
                        .getCacheEventNotificationService( ) )
                .peek( rel -> rel.registerListener( cacheEventListener ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_caches.html" ) );
        CacheJspBean.doResetCaches( request );
        assertEquals( "Not all AbstractCacheableService were reset", registeredListener,
                cacheEventListener.getCallCount( ) );
    }

    public void testDoResetCachesInvalidToken( ) throws AccessDeniedException
    {
        CacheListener cacheEventListener = new CacheListener( );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) )
                .map( service -> ( ( AbstractCacheableService ) service ).getCache( )
                        .getCacheEventNotificationService( ) )
                .peek( rel -> rel.registerListener( cacheEventListener ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_caches.html" ) + "b" );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, cacheEventListener.getCallCount( ) );
        }
    }

    public void testDoResetCachesNoToken( ) throws AccessDeniedException
    {
        CacheListener cacheEventListener = new CacheListener( );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) )
                .map( service -> ( ( AbstractCacheableService ) service ).getCache( )
                        .getCacheEventNotificationService( ) )
                .peek( rel -> rel.registerListener( cacheEventListener ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, cacheEventListener.getCallCount( ) );
        }
    }

    public void testDoResetCachesOneCache( ) throws AccessDeniedException
    {
        CacheListener cacheEventListener = new CacheListener( );
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService )
            {
                ( ( AbstractCacheableService ) service ).getCache( ).getCacheEventNotificationService( )
                        .registerListener( cacheEventListener );
                break;
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        CacheListener allCacheEventListener = new CacheListener( );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) )
                .map( service -> ( ( AbstractCacheableService ) service ).getCache( )
                        .getCacheEventNotificationService( ) )
                .peek( rel -> rel.registerListener( allCacheEventListener ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_caches.html" ) );
        CacheJspBean.doResetCaches( request );
        assertEquals( "Only one cache should have been reset", 1, allCacheEventListener.getCallCount( ) );
        assertEquals( "The target cache should have been reset", 1, cacheEventListener.getCallCount( ) );
    }

    public void testDoResetCachesOneCacheInvalidToken( ) throws AccessDeniedException
    {
        CacheListener cacheEventListener = new CacheListener( );
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService )
            {
                ( ( AbstractCacheableService ) service ).getCache( ).getCacheEventNotificationService( )
                        .registerListener( cacheEventListener );
                break;
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        CacheListener allCacheEventListener = new CacheListener( );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) )
                .map( service -> ( ( AbstractCacheableService ) service ).getCache( )
                        .getCacheEventNotificationService( ) )
                .peek( rel -> rel.registerListener( allCacheEventListener ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/system/manage_caches.html" ) + "b" );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, allCacheEventListener.getCallCount( ) );
            assertEquals( "No cache should have been reset", 0, cacheEventListener.getCallCount( ) );
        }
    }

    public void testDoResetCachesOneCacheNoToken( ) throws AccessDeniedException
    {
        CacheListener cacheEventListener = new CacheListener( );
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService )
            {
                ( ( AbstractCacheableService ) service ).getCache( ).getCacheEventNotificationService( )
                        .registerListener( cacheEventListener );
                break;
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        CacheListener allCacheEventListener = new CacheListener( );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) )
                .map( service -> ( ( AbstractCacheableService ) service ).getCache( )
                        .getCacheEventNotificationService( ) )
                .peek( rel -> rel.registerListener( allCacheEventListener ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, allCacheEventListener.getCallCount( ) );
            assertEquals( "No cache should have been reset", 0, cacheEventListener.getCallCount( ) );
        }
    }

    /**
     * Test of doReloadProperties method, of class
     * fr.paris.lutece.portal.web.system.SystemJspBean.
     */
    public void testDoReloadProperties( )
    {
        System.out.println( "doReloadProperties" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        Utils.registerAdminUserWithRigth( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );

        CacheJspBean instance = new CacheJspBean( );
        instance.doReloadProperties( );
    }

    private static final class CacheListener extends CacheEventListenerAdapter
    {
        private long _nCallCount = 0;

        @Override
        public void notifyRemoveAll( Ehcache cache )
        {
            _nCallCount++;
        }

        public long getCallCount( )
        {
            return _nCallCount;
        }
    }
}
