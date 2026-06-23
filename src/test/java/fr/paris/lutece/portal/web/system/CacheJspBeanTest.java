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
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * CacheJspBeanTest Test Class
 *
 */
public class CacheJspBeanTest extends LuteceTestCase
{
    private static final String TOKEN_KEY_RESET = "resetCaches";
    private static final String TOKEN_KEY_TOGGLE = "toggleCache";

    private @Inject TestResetCacheObserver resetCacheObserver;
    private @Inject ISecurityTokenService _securityTokenService;

    @BeforeEach
    public void resetObserver( )
    {
        resetCacheObserver.reset( );
    }

    /**
     * Gets a CDI-managed instance of the controller under test.
     *
     * @return the CacheJspBean instance
     */
    private CacheJspBean getInstance( )
    {
        return CDI.current( ).select( CacheJspBean.class ).get( );
    }

    /**
     * Test of getManageCaches view, of class CacheJspBean.
     *
     * @throws AccessDeniedException
     *             if the controller denies access
     */
    @Test
    public void testGetManageCaches( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );

        assertNotNull( getInstance( ).processController( request, response ) );
    }

    /**
     * Test of resetCaches action, of class CacheJspBean.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoResetCaches( ) throws AccessDeniedException
    {
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) && !service.isPreventGlobalReset( ) ).count( );
        assertFalse( 0 == registeredListener, "There should be at least one active AbstractCacheableService" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_RESET );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_KEY_RESET ) );
        getInstance( ).processController( request, response );
        assertEquals( registeredListener, resetCacheObserver.getCallCount( ), "Not all AbstractCacheableService were reset" );
    }

    /**
     * Test that an invalid token prevents the reset.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoResetCachesInvalidToken( ) throws AccessDeniedException
    {
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( 0 == registeredListener, "There should be at least one active AbstractCacheableService" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_RESET );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_KEY_RESET ) + "b" );
        try
        {
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( 0, resetCacheObserver.getCallCount( ), "No cache should have been reset" );
        }
    }

    /**
     * Test that a missing token prevents the reset.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoResetCachesNoToken( ) throws AccessDeniedException
    {
        long registeredListener = CacheService.getCacheableServicesList( ).stream( )
                .filter( service -> service instanceof AbstractCacheableService && service.isCacheEnable( ) ).count( );
        assertFalse( 0 == registeredListener, "There should be at least one active AbstractCacheableService" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_RESET );
        try
        {
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( 0, resetCacheObserver.getCallCount( ), "No cache should have been reset" );
        }
    }

    /**
     * Test of resetCaches action on a single cache, of class CacheJspBean.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoResetCachesOneCache( ) throws AccessDeniedException
    {
        int cacheIndex = findFirstResettableCacheIndex( );
        assertFalse( cacheIndex == -1, "There should be at least one active AbstractCacheableService" );
        String cacheName = ( (AbstractCacheableService) CacheService.getCacheableServicesList( ).get( cacheIndex ) ).getCache( ).getName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_RESET );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_KEY_RESET ) );
        getInstance( ).processController( request, response );
        assertEquals( 1, resetCacheObserver.getCallCount( ), "Only one cache should have been reset" );
        assertEquals( 1, resetCacheObserver.getCallCount( cacheName ), "The target cache should have been reset" );
    }

    /**
     * Test that an invalid token prevents the single cache reset.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoResetCachesOneCacheInvalidToken( ) throws AccessDeniedException
    {
        int cacheIndex = findFirstResettableCacheIndex( );
        assertFalse( cacheIndex == -1, "There should be at least one active AbstractCacheableService" );
        String cacheName = ( (AbstractCacheableService) CacheService.getCacheableServicesList( ).get( cacheIndex ) ).getCache( ).getName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_RESET );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_KEY_RESET ) + "b" );
        try
        {
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( 0, resetCacheObserver.getCallCount( ), "No cache should have been reset" );
            assertEquals( 0, resetCacheObserver.getCallCount( cacheName ), "No cache should have been reset" );
        }
    }

    /**
     * Test that a missing token prevents the single cache reset.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoResetCachesOneCacheNoToken( ) throws AccessDeniedException
    {
        int cacheIndex = findFirstResettableCacheIndex( );
        assertFalse( cacheIndex == -1, "There should be at least one active AbstractCacheableService" );
        String cacheName = ( (AbstractCacheableService) CacheService.getCacheableServicesList( ).get( cacheIndex ) ).getCache( ).getName( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_RESET );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        try
        {
            getInstance( ).processController( request, response );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( 0, resetCacheObserver.getCallCount( ), "No cache should have been reset" );
            assertEquals( 0, resetCacheObserver.getCallCount( cacheName ), "No cache should have been reset" );
        }
    }

    /**
     * Test of toggleCache action, of class CacheJspBean.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoToggleCache( ) throws AccessDeniedException
    {
        int cacheIndex = findFirstResettableCacheIndex( );
        assertFalse( cacheIndex == -1, "There should be at least one active AbstractCacheableService" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_TOGGLE );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_KEY_TOGGLE ) );
        try
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
            getInstance( ).processController( request, response );
            assertFalse( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
        }
        finally
        {
            CacheService.getCacheableServicesList( ).get( cacheIndex ).enableCache( true );
        }
    }

    /**
     * Test that an invalid token prevents the toggle.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoToggleCacheInvalidToken( ) throws AccessDeniedException
    {
        int cacheIndex = findFirstActiveCacheIndex( );
        assertFalse( cacheIndex == -1, "There should be at least one active AbstractCacheableService" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_TOGGLE );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, TOKEN_KEY_TOGGLE ) + "b" );
        try
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
            getInstance( ).processController( request, response );
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

    /**
     * Test that a missing token prevents the toggle.
     *
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    @Test
    public void testDoToggleCacheNoToken( ) throws AccessDeniedException
    {
        int cacheIndex = findFirstActiveCacheIndex( );
        assertFalse( cacheIndex == -1, "There should be at least one active AbstractCacheableService" );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, TOKEN_KEY_TOGGLE );
        request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
        try
        {
            assertTrue( CacheService.getCacheableServicesList( ).get( cacheIndex ).isCacheEnable( ) );
            getInstance( ).processController( request, response );
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

    /**
     * Test of confirmToggleCache view, of class CacheJspBean.
     *
     * @throws AccessDeniedException
     *             if the controller denies access
     */
    @Test
    public void testGetConfirmToggleCache( ) throws AccessDeniedException
    {
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            MockHttpServletRequest request = new MockHttpServletRequest( );
            MockHttpServletResponse response = new MockHttpServletResponse( );
            AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
            request.addParameter( MVCUtils.PARAMETER_VIEW, "confirmToggleCache" );
            request.addParameter( "id_cache", Integer.toString( cacheIndex ) );
            getInstance( ).processController( request, response );
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

    /**
     * Test of confirmToggleCache view without the id_cache parameter, of class CacheJspBean.
     *
     * @throws AccessDeniedException
     *             if the controller denies access
     */
    @Test
    public void testGetConfirmToggleCacheNoParam( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), CacheJspBean.RIGHT_CACHE_MANAGEMENT );
        request.addParameter( MVCUtils.PARAMETER_VIEW, "confirmToggleCache" );
        getInstance( ).processController( request, response );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_ERROR, message.getType( ) );
    }

    /**
     * Finds the index of the first resettable cache (active, not prevented from global reset).
     *
     * @return the index, or -1 if none
     */
    private int findFirstResettableCacheIndex( )
    {
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService && service.isCacheEnable( ) && !service.isPreventGlobalReset( ) )
            {
                return cacheIndex;
            }
        }
        return -1;
    }

    /**
     * Finds the index of the first active cache.
     *
     * @return the index, or -1 if none
     */
    private int findFirstActiveCacheIndex( )
    {
        int cacheIndex = -1;
        for ( CacheableService service : CacheService.getCacheableServicesList( ) )
        {
            cacheIndex++;
            if ( service instanceof AbstractCacheableService && service.isCacheEnable( ) )
            {
                return cacheIndex;
            }
        }
        return -1;
    }

}
