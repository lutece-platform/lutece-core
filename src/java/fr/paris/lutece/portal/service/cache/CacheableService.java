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

import fr.paris.lutece.portal.service.util.LuteceService;

import java.util.List;

/**
 * Base interface of Lutece services that provide a cache.
 * Uses the JCache API to make cache statistics available via the {@link javax.cache.management.CacheStatisticsMXBean}.
 *
 * @param <K> the type of keys maintained by the cache
 * @param <V> the type of cached values
 */
public interface CacheableService<K, V> extends LuteceService {
    
    /**
     * Gets the current cache status.
     *
     * @return true if the cache is enabled, otherwise false
     */
    boolean isCacheEnable();

    /**
     * Gets the number of items currently in the cache.
     *
     * @return the number of items currently in the cache
     */
    int getCacheSize();

    /**
     * Resets the cache.
     */
    void resetCache();

    /**
     * Enables or disables the cache.
     * 
     * @param bEnable true to enable, false to disable
     */
    void enableCache(boolean bEnable);

    /**
     * Gets all keys in the cache.
     * 
     * @return a list of all keys in the cache
     */
    List<K> getKeys();

    /**
     * Returns the maximum number of elements accepted into the cache.
     * This method will be removed in the next version: Use the JCache API to make cache statistics available via the {@link javax.cache.management.CacheStatisticsMXBean}.
     *
     * @return the maximum number of elements
     */
    @Deprecated
    default int getMaxElements() {
        throw new UnsupportedOperationException("Unimplemented method 'getMaxElements'");
    }

    /**
     * Returns the time to live for objects in the cache.
     * This method will be removed in the next version: Use the JCache API to make cache statistics available via the {@link javax.cache.management.CacheStatisticsMXBean}.
     *
     * @return the time to live in seconds
     */
    @Deprecated
    default long getTimeToLive() {
        throw new UnsupportedOperationException("Unimplemented method 'getTimeToLive'");
    }

    /**
     * Returns the memory size of the cache.
     * This method will be removed in the next version: Use the JCache API to make cache statistics available via the {@link javax.cache.management.CacheStatisticsMXBean}.
     *
     * @return the memory size
     */
    @Deprecated
    default long getMemorySize() {
        throw new UnsupportedOperationException("Unimplemented method 'getMemorySize'");
    }

    /**
     * Returns cache information.
     * 
     * @return cache information
     */
    String getInfos();

    /**
     * Can the cache be reset by automatic caches resets
     * 
     * @return true is the cache can be reset by automatic / massive caches resets, false otherwise
     */
    boolean isPreventGlobalReset( );
}
