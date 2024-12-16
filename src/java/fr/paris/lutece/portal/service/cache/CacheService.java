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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Provides cache management services for cacheable components.
 */
public class CacheService
{	
    private static final String PROPERTY_IS_ENABLED = ".enabled";
    private static final String ENABLED = "1";
    private static final String DISABLED = "0";
    
    private static final String LABEL_ENABLED = "enabled";
    private static final String LABEL_DISABLED = "disabled";

    private static ManageCacheService manageCacheService = CDI.current().select(ManageCacheService.class).get();
    
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);

    /**
     * Resets all caches managed by the service.
     */
    public static void resetCaches()
    {
        manageCacheService.resetCaches();
        logger.debug( "All caches have been reset" );
    }

    /**
     * Shuts down the cache service and the cache manager.
     * This method should be called when the web application is stopped.
     */
    public static void shutdown()
    {
        CDI.current().getBeanManager().getEvent().fire(new ShutDownEvent());
        logger.debug( "Cache service shut down" );
    }

    /**
     * Registers a new cacheable service.
     *
     * @param cs The cacheable service to register
     */
    public static void registerCacheableService(CacheableService<?, ?> cs)
    {
        manageCacheService.registerCacheableService(cs);
        logger.debug( "Cacheable service {} registered", ( ) -> cs.getName() );
    }

    /**
     * Checks if a cache with the given name exists.
     *
     * @param cacheName The name of the cache to check
     * @return true if the cache exists, false otherwise
     */
    public static boolean isCacheExist(String cacheName)
    {
        return manageCacheService.isCacheExist(cacheName);
    }

    /**
     * Returns all registered cacheable services.
     *
     * @return A list containing all registered cacheable services
     */
    public static List<CacheableService<?, ?>> getCacheableServicesList()
    {
        return manageCacheService.getCacheableServicesList();
    }

    /**
     * Retrieves a cacheable service by its cache name.
     *
     * @param <K> The type of keys maintained by the cache
     * @param <V> The type of cached values
     * @param cacheName The name of the cache to retrieve
     * @return The cacheable service associated with the specified cache name
     */
    public static <K, V> CacheableService<K, V> getCache(String cacheName)
    {
        return manageCacheService.getCache(cacheName);
    }

    /**
     * Updates the cache status in the database.
     *
     * @param cs The cacheable service whose status is to be updated
     */
    public static void updateCacheStatus(CacheableService<?, ?> cs)
    {
        String strKey = CacheConfigUtil.getDSKey(cs.getName(), PROPERTY_IS_ENABLED);
        DatastoreService.setInstanceDataValue(strKey, (cs.isCacheEnable() ? ENABLED : DISABLED));
        logger.debug( "Cache {} {}",  ( ) -> cs.getName( ),  ( ) -> ( cs.isCacheEnable( ) ? LABEL_ENABLED : LABEL_DISABLED ) );
    }

    /**
     * Event to be fired during the shutdown process.
     */
    public static record ShutDownEvent()
    {
    }
}

