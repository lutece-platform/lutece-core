/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.cache.CacheConfigUtil.CacheConfig;

/**
 * Tests the generation of the cache settings: cache names, inheritance of the default settings, Ehcache file.
 */
public class CacheConfigUtilTest
{
    private static CacheConfig defaults( )
    {
        return new CacheConfig.ConfigBuilder( "luteceCache" ).heapEntries( 10000 ).offheapMB( 0 ).diskMB( 0 ).timeToLiveSeconds( 1800 )
                .timeToIdleSeconds( 0 ).eternal( false ).statistics( false ).enableManagement( false ).persistenceDirectory( "java.io.tmpdir/ehcache/" )
                .build( );
    }

    /**
     * A cache name keeps its dots: the name ends where a known setting starts.
     */
    @Test
    public void testCacheNameWithDots( )
    {
        Set<String> names = CacheConfigUtil.determineCaheName( Set.of( "core.cache.status.forms.formsCacheService.timeToLiveSeconds",
                "core.cache.status.LuteceUserCacheService.maxElementsInMemory", "core.cache.status.SiteMapService.enabled" ) );

        assertEquals( Set.of( "forms.formsCacheService", "LuteceUserCacheService" ), names );
    }

    /**
     * A cache that sets its size only keeps the default expiry, a cache that sets its expiry only keeps the default size.
     */
    @Test
    public void testSettingsInheritTheDefaults( )
    {
        List<CacheConfig> configs = CacheConfigUtil.buildCacheConfigs( Map.of( "core.cache.status.sizeOnly.maxElementsInMemory", "500",
                "core.cache.status.ttlOnly.timeToLiveSeconds", "60" ) );
        CacheConfig sizeOnly = configs.stream( ).filter( c -> c.getName( ).equals( "sizeOnly" ) ).findFirst( ).get( ).completedBy( defaults( ) );
        CacheConfig ttlOnly = configs.stream( ).filter( c -> c.getName( ).equals( "ttlOnly" ) ).findFirst( ).get( ).completedBy( defaults( ) );

        assertEquals( 500, sizeOnly.getHeapEntries( ) );
        assertEquals( 1800, sizeOnly.getTimeToLiveSeconds( ) );
        assertFalse( sizeOnly.isEternal( ) );
        assertEquals( 10000, ttlOnly.getHeapEntries( ) );
        assertEquals( 60, ttlOnly.getTimeToLiveSeconds( ) );
    }

    /**
     * An eternal cache does not inherit the default time to live.
     */
    @Test
    public void testEternalIsKept( )
    {
        CacheConfig eternal = CacheConfigUtil.buildCacheConfigs( Map.of( "core.cache.status.pluginCache.eternal", "true" ) ).get( 0 )
                .completedBy( defaults( ) );

        assertTrue( eternal.isEternal( ) );
        assertEquals( 10000, eternal.getHeapEntries( ) );
    }

    /**
     * The Ehcache file bounds every template by entries, without an offheap tier nor a persistence directory when no
     * cache uses a disk.
     *
     * @throws Exception
     *             if the file cannot be written
     */
    @Test
    public void testEhcacheFile( ) throws Exception
    {
        List<CacheConfig> configs = CacheConfigUtil.buildCacheConfigs( Map.of( "core.cache.status.forms.formsCacheService.timeToLiveSeconds", "86400" ) );
        String strXml = Files.readString( Path.of( CacheConfigUtil.createTempEhcacheConfigFile( configs, defaults( ) ) ) );

        assertTrue( strXml.contains( "<jsr107:cache name=\"forms.formsCacheService\"" ) );
        assertTrue( strXml.contains( "<ttl unit=\"seconds\">86400</ttl>" ) );
        assertEquals( 2, strXml.split( "<heap unit=\"entries\">10000</heap>", -1 ).length - 1 );
        assertFalse( strXml.contains( "offheap" ) );
        assertFalse( strXml.contains( "<persistence" ) );
    }

    /**
     * The java.io.tmpdir prefix is the temporary directory of the JVM, not a literal folder.
     */
    @Test
    public void testPersistenceDirectory( )
    {
        assertEquals( Path.of( System.getProperty( "java.io.tmpdir" ), "ehcache" ).toString( ),
                Path.of( CacheConfigUtil.resolveDirectory( "java.io.tmpdir/ehcache/" ) ).toString( ) );
    }

    /**
     * The settings given to the configuration customizers are the completed settings of the cache.
     */
    @Test
    public void testSettingsForCustomizers( )
    {
        CacheSettings settings = CacheConfigUtil.buildCacheConfigs( Map.of( "core.cache.status.forms.formsCacheService.maxElementsInMemory", "20000" ) )
                .get( 0 ).completedBy( defaults( ) ).toSettings( );

        assertEquals( 20000, settings.maxElementsInMemory( ) );
        assertEquals( 1800, settings.timeToLiveSeconds( ) );
        assertFalse( settings.eternal( ) );
    }

    /**
     * A path starting with java.io.tmpdir is located in the temporary directory of the JVM (search index, export).
     */
    @Test
    public void testTemporaryDirectoryPath( )
    {
        assertEquals( Path.of( System.getProperty( "java.io.tmpdir" ), "index" ).toString( ),
                fr.paris.lutece.portal.service.util.AppPathService.resolveTemporaryDirectory( "java.io.tmpdir/index" ) );
        assertEquals( "/data/index", fr.paris.lutece.portal.service.util.AppPathService.resolveTemporaryDirectory( "/data/index" ) );
    }
}
