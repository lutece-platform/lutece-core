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

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.cache.configuration.Configuration;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * Tests the creation of named caches: a disabled cache (the state of every cache the datastore does not enable) and
 * several requests creating the same cache at the same time.
 */
public class CacheCreationTest extends LuteceTestCase
{
    @Inject
    private ILuteceCacheManager _cacheManager;

    /**
     * A disabled cache stores nothing: reads miss, writes are ignored.
     */
    @Test
    public void testReadsAndWritesOnDisabledCache( )
    {
        Lutece107Cache<String, String> cache = _cacheManager.createCache( "junit-disabled-rw", String.class, String.class );

        assertFalse( cache.isCacheEnable( ) );
        assertTrue( cache.isClosed( ) );
        cache.put( "key", "value" );
        assertNull( cache.get( "key" ) );
        assertFalse( cache.containsKey( "key" ) );
        assertFalse( cache.putIfAbsent( "key", "value" ) );
        assertFalse( cache.remove( "key" ) );
        assertNull( cache.getAndPut( "key", "value" ) );
        assertTrue( cache.getAll( Set.of( "key" ) ).isEmpty( ) );
        assertFalse( cache.iterator( ).hasNext( ) );
        assertTrue( cache.getKeys( ).isEmpty( ) );
        cache.clear( );
        cache.resetCache( );
    }

    /**
     * The configuration of a disabled cache is still readable, so the cache manager can look it up by name.
     */
    @Test
    public void testLookupOfDisabledCache( )
    {
        _cacheManager.createCache( "junit-disabled-lookup", String.class, String.class );

        Lutece107Cache<String, String> found = _cacheManager.getCache( "junit-disabled-lookup", String.class, String.class );

        assertNotNull( found );
        assertEquals( String.class, found.getConfiguration( Configuration.class ).getKeyType( ) );
    }

    /**
     * Several requests creating the same disabled cache at the same time all get it.
     *
     * @throws Exception
     *             if a creation fails
     */
    @Test
    public void testConcurrentCreationOfDisabledCache( ) throws Exception
    {
        ExecutorService pool = Executors.newFixedThreadPool( 8 );
        try
        {
            Callable<Lutece107Cache<String, String>> create = ( ) -> _cacheManager.createCache( "junit-disabled-concurrent", String.class, String.class );
            for ( Future<Lutece107Cache<String, String>> future : pool.invokeAll( IntStream.range( 0, 32 ).mapToObj( i -> create ).collect( Collectors.toList( ) ) ) )
            {
                assertNotNull( future.get( 10, TimeUnit.SECONDS ) );
            }
        }
        finally
        {
            pool.shutdownNow( );
        }
    }

    /**
     * Several requests creating the same enabled cache at the same time all get the one cache.
     *
     * @throws Exception
     *             if a creation fails
     */
    @Test
    public void testConcurrentCreationOfEnabledCache( ) throws Exception
    {
        int nThreads = 8;
        ExecutorService pool = Executors.newFixedThreadPool( nThreads );
        try
        {
            for ( int nRound = 0; nRound < 10; nRound++ )
            {
                String strName = "junit-enabled-concurrent-" + nRound;
                CyclicBarrier barrier = new CyclicBarrier( nThreads );
                Callable<Lutece107Cache<String, String>> create = ( ) -> {
                    barrier.await( );
                    return _cacheManager.createCache( strName, String.class, String.class, true );
                };
                List<Future<Lutece107Cache<String, String>>> futures = pool.invokeAll( IntStream.range( 0, nThreads ).mapToObj( i -> create ).collect( Collectors.toList( ) ) );
                futures.get( 0 ).get( 10, TimeUnit.SECONDS ).put( "key", "value" );
                for ( Future<Lutece107Cache<String, String>> future : futures )
                {
                    assertEquals( "value", future.get( 10, TimeUnit.SECONDS ).get( "key" ) );
                }
            }
        }
        finally
        {
            pool.shutdownNow( );
        }
    }
}
