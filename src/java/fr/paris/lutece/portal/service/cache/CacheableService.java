/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
 * Base interface of Lutece services that provide a cache
 */
public interface CacheableService extends LuteceService
{
    /**
     * Gets the current cache status.
     *
     * @return true if enable, otherwise false
     */
    boolean isCacheEnable(  );

    /**
     * Gets the number of item currently in the cache.
     *
     * @return the number of item currently in the cache.
     */
    int getCacheSize(  );

    /**
     * Reset the cache.
     */
    void resetCache(  );

    /**
     * Enable the cache
     * @param bEnable true to enable, false to disable
     */
    void enableCache( boolean bEnable );

    /**
     * Gets all keys in the cache
     * @return The List
     */
    List<String> getKeys(  );

    /**
     * Returns maximum elements accepted into the cache
     * @return The max elements
     */
    int getMaxElements(  );

    /**
     * Returns the time to live for objects in the cache
     * @return The time to live in seconds
     */
    long getTimeToLive(  );

    /**
     * Return the memory size
     * @return the memory size
     */
    long getMemorySize(  );

    /**
     * Returns cache infos
     * @return cache infos
     */
    String getInfos(  );
}
