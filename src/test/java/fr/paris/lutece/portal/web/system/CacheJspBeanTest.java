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
package fr.paris.lutece.portal.web.system;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * CacheJspBeanTest Test Class
 *
 */
public class CacheJspBeanTest extends LuteceTestCase
{
    private @Inject TestResetCacheObserver resetCacheObserver;
    private @Inject ISecurityTokenService _securityTokenService;
    
    @BeforeEach
    private void resetObserver() {
        resetCacheObserver.reset( );
    }
    
    /**
     * Test of getManageCaches method, of class fr.paris.lutece.portal.web.system.CacheJspBean.
     */
    @Test
    public void testGetManageCaches( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );

        CacheJspBean instance = new CacheJspBean( );
        instance.init( request, CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        assertNotNull( instance.getManageCaches( request ) );
    }

    /**
     * Test of doResetCaches method, of class fr.paris.lutece.portal.web.system.CacheJspBean.
     * 
     * @throws AccessDeniedException
     */
    @Test
    public void testDoResetCaches( ) throws AccessDeniedException
    {
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/system/manage_caches.html" ) );
        CacheJspBean.doResetCaches( request );
        assertEquals( "Not all AbstractCacheableService were reset", registeredListener, resetCacheObserver.getCallCount( ) );
    }
    
    @Test
    public void testDoResetCachesInvalidToken( ) throws AccessDeniedException
    {
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/system/manage_caches.html" ) + "b" );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, resetCacheObserver.getCallCount( ) );
        }
    }

    @Test
    public void testDoResetCachesNoToken( ) throws AccessDeniedException
    {
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, resetCacheObserver.getCallCount( ) );
        }
    }

    @Test
    public void testDoResetCachesOneCache( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        String cacheName = null;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService && service.isCacheEnable( ) && !service.isPreventGlobalReset( ) ) 
            {
                cacheName = ((AbstractCacheableService) service ).getCache( ).getName( );
                break;
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/system/manage_caches.html" ) );
        CacheJspBean.doResetCaches( request );
        assertEquals( "Only one cache should have been reset", 1, resetCacheObserver.getCallCount( ) );
        assertEquals( "The target cache should have been reset", 1, resetCacheObserver.getCallCount( cacheName ) );
    }

    @Test
    public void testDoResetCachesOneCacheInvalidToken( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        String cacheName = null;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService && service.isCacheEnable( ) && !service.isPreventGlobalReset( ) ) 
            {
                cacheName = ((AbstractCacheableService) service ).getCache( ).getName( );
                break;
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "admin/system/manage_caches.html" ) + "b" );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, resetCacheObserver.getCallCount( ) );
            assertEquals( "No cache should have been reset", 0, resetCacheObserver.getCallCount( cacheName ) );
        }
    }

    @Test
    public void testDoResetCachesOneCacheNoToken( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        String cacheName = null;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService && service.isCacheEnable( ) && !service.isPreventGlobalReset( ) )
            {
                cacheName = ((AbstractCacheableService) service ).getCache( ).getName( );
                break;
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( "There should be at least one active AbstractCacheableService", 0 == registeredListener );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        try
        {
            CacheJspBean.doResetCaches( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( "No cache should have been reset", 0, resetCacheObserver.getCallCount( ) );
            assertEquals( "No cache should have been reset", 0, resetCacheObserver.getCallCount( cacheName ) );
        }
    }

    @Test
    public void testDoToggleCache( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService )
            {
                if (service.isCacheEnable( )) { break; }
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/system/DoToggleCache.jsp" ) );
        try
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
            CacheJspBean.doToggleCache( request );
            assertFalse( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
        }
        finally
        {
            CacheService.getCacheableServicesList( ).get( cacheIndex ).enableCache( true );
        }
    }

    @Test
    public void testDoToggleCacheInvalidToken( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService )
            {
                if (service.isCacheEnable( )) { break; }
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, "jsp/admin/system/DoToggleCache.jsp" ) + "b" );
        try
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
            CacheJspBean.doToggleCache( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
        }
        finally
        {
            CacheService.getCacheableServicesList( ).get( cacheIndex ).enableCache( true );
        }
    }

    @Test
    public void testDoToggleCacheNoToken( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService )
            {
                if (service.isCacheEnable( )) { break; }
            }
        }
        assertFalse( "There should be at least one active AbstractCacheableService", cacheIndex == -1 );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        try
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
            CacheJspBean.doToggleCache( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
        }
        finally
        {
            CacheService.getCacheableServicesList( ).get( cacheIndex ).enableCache( true );
        }
    }

    @Test
    public void testGetConfirmToggleCache( )
    {
        CacheJspBean instance = new CacheJspBean( );
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
            instance.getConfirmToggleCache( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_CONFIRMATION, message.getType( ) );
            for ( Locale locale : I18nService.getAdminAvailableLocales( ) )
            {
                assertTrue( message.getText( locale ).contains( service.getName( ) ) );
            }
            assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        }
    }

    @Test
    public void testGetConfirmToggleCacheNoParam( )
    {
        CacheJspBean instance = new CacheJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        instance.getConfirmToggleCache( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

}
