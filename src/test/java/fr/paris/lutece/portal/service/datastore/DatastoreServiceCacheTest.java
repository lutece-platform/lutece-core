/*
 * Copyright (c) 2002-2024, City of Paris
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
package fr.paris.lutece.portal.service.datastore;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

public abstract class DatastoreServiceCacheTest extends LuteceTestCase
{

    private static final String PREFIX_A = "a.";
    private static final String PREFIX_B = "b.";
    private static final int NUM_VALUES = 5;
    private static final String CACHE_SERVICE_NAME = "Datastore Cache Service";
    private boolean _bStatus;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        setDatastoreCacheStatus( );
        for ( int i = 1; i <= NUM_VALUES; i++ )
        {
            DatastoreService.setDataValue( PREFIX_A + i, PREFIX_A + i );
            DatastoreService.setDataValue( PREFIX_B + i, PREFIX_B + i );
        }

    }

    private void setDatastoreCacheStatus( )
    {
        CacheableService dataStoreCache = CacheService.getCacheableServicesList( ).stream( )
                .filter( c -> CACHE_SERVICE_NAME.equals( c.getName( ) ) ).findFirst( ).get( );
        _bStatus = dataStoreCache.isCacheEnable( );
        dataStoreCache.enableCache( getTargetDatastoreCacheStatus( ) );
    }

    protected abstract boolean getTargetDatastoreCacheStatus( );

    public void testGetDataByPrefix( )
    {
        ReferenceList entities = DatastoreService.getDataByPrefix( PREFIX_A );
        assertEquals( NUM_VALUES, entities.size( ) );
        for ( int i = 1; i <= NUM_VALUES; i++ )
        {
            final int idx = i;
            assertEquals( 1, entities.stream( ).filter( e -> ( PREFIX_A + idx ).equals( e.getCode( ) ) ).count( ) );
        }
        assertFalse( entities.stream( ).anyMatch( e -> e.getCode( ).startsWith( PREFIX_B ) ) );

        for ( String newEntity : new String[ ] { PREFIX_A, PREFIX_A + "new" } )
        {
            DatastoreService.setDataValue( newEntity, newEntity );
            entities = DatastoreService.getDataByPrefix( PREFIX_A );
            assertEquals( "New value not found when searching by prefix", 1,
                    entities.stream( ).filter( e -> ( newEntity ).equals( e.getCode( ) ) ).count( ) );
            DatastoreService.removeData( newEntity );
            entities = DatastoreService.getDataByPrefix( PREFIX_A );
            assertEquals( "Old value still present when searching by prefix", 0,
                    entities.stream( ).filter( e -> ( newEntity ).equals( e.getCode( ) ) ).count( ) );
        }
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        DatastoreService.removeDataByPrefix( PREFIX_A );
        DatastoreService.removeDataByPrefix( PREFIX_B );
        restoreDatastoreCacheStatus( );
        super.tearDown( );
    }

    private void restoreDatastoreCacheStatus( )
    {
        CacheService.getCacheableServicesList( ).stream( ).filter( c -> CACHE_SERVICE_NAME.equals( c.getName( ) ) )
                .findFirst( ).get( ).enableCache( _bStatus );
    }

}
