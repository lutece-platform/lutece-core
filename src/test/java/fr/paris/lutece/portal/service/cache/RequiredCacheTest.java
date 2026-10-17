/*
 * Copyright (c) 2002-2025, City of Paris
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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * Tests the caches the webapp cannot run without: a required cache stays enabled whatever the saved status, a cache
 * whose reset is prevented keeps its entries.
 */
public class RequiredCacheTest extends LuteceTestCase
{
    private static final String DISABLED = "0";

    @Inject
    private ManageCacheService _manageCacheService;

    /**
     * A disabled status left in the database does not disable a required cache, and is replaced by the enabled one.
     */
    @Test
    public void testRequiredCacheIgnoresSavedDisabledStatus( )
    {
        String strName = "junit-required-saved";
        DatastoreService.setInstanceDataValue( CacheConfigUtil.getDSKey( strName, ".enabled" ), DISABLED );

        Default107Cache<String, String> cache = new Default107Cache<>( strName, String.class, String.class, true, false, true, false );

        assertTrue( cache.isRequired( ) );
        assertTrue( cache.isCacheEnable( ) );
        cache.put( "key", "value" );
        assertEquals( "value", cache.get( "key" ) );
        assertTrue( CacheConfigUtil.getStatusFromDataBase( strName ) );
    }

    /**
     * Neither the back office nor the refresh of the statuses saved by the other instances disable a required cache.
     */
    @Test
    public void testRequiredCacheCannotBeDisabled( )
    {
        String strName = "junit-required-disable";
        Default107Cache<String, String> cache = new Default107Cache<>( strName, String.class, String.class, true, false, true, false );
        cache.put( "key", "value" );

        cache.enableCache( false );
        assertTrue( cache.isCacheEnable( ) );
        assertEquals( "value", cache.get( "key" ) );

        DatastoreService.setInstanceDataValue( CacheConfigUtil.getDSKey( strName, ".enabled" ), DISABLED );
        _manageCacheService.refreshStatuses( );
        assertTrue( cache.isCacheEnable( ) );
        assertEquals( "value", cache.get( "key" ) );
    }

    /**
     * A cache whose reset is prevented keeps its entries on its own reset and on a global reset.
     */
    @Test
    public void testPreventResetKeepsEntries( )
    {
        Default107Cache<String, String> cache = new Default107Cache<>( "junit-prevent-reset", String.class, String.class, true, false, true, true );
        cache.put( "key", "value" );

        assertTrue( cache.isPreventGlobalReset( ) );
        cache.resetCache( );
        CacheService.resetCaches( );
        assertEquals( "value", cache.get( "key" ) );
    }

    /**
     * A cache that is not required keeps the status saved in the database (back office choice).
     */
    @Test
    public void testNotRequiredCacheKeepsSavedStatus( )
    {
        String strName = "junit-not-required-saved";
        DatastoreService.setInstanceDataValue( CacheConfigUtil.getDSKey( strName, ".enabled" ), DISABLED );

        Default107Cache<String, String> cache = new Default107Cache<>( strName, String.class, String.class, true, false );

        assertFalse( cache.isRequired( ) );
        assertFalse( cache.isCacheEnable( ) );
    }

    /**
     * The cache of the loaded plugins is required and never reset.
     */
    @Test
    public void testPluginCacheIsRequired( )
    {
        CacheableService<?, ?> pluginCache = CacheService.getCacheableServicesList( ).stream( )
                .filter( cs -> "pluginCache".equals( cs.getName( ) ) ).findFirst( ).orElseThrow( );

        assertTrue( pluginCache.isRequired( ) );
        assertTrue( pluginCache.isPreventReset( ) );
        pluginCache.enableCache( false );
        pluginCache.resetCache( );
        assertTrue( pluginCache.isCacheEnable( ) );
        assertNotNull( PluginService.getCore( ) );
    }
}
