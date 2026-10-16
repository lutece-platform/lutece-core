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

import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

/**
 * Adapts the JCache configuration of a Lutece cache to a caching provider, for the settings that JCache cannot carry
 * (the size of a Hazelcast cache, for instance). Implementations are CDI beans, discovered by the core when a cache is
 * created: a module bringing the API of a provider can implement it without making the core depend on that provider.
 */
public interface ICacheConfigurationCustomizer
{
    /**
     * Tells whether this customizer applies to a caching provider
     *
     * @param provider
     *            the caching provider of the Lutece cache manager
     * @return true when {@link #customize(String, Configuration, CacheSettings)} must be called for this provider
     */
    boolean supports( CachingProvider provider );

    /**
     * Returns the configuration to create a cache with
     *
     * @param <K>
     *            the type of the keys
     * @param <V>
     *            the type of the values
     * @param strCacheName
     *            the JCache name of the cache
     * @param configuration
     *            the JCache configuration built by the core (types, store by value or reference, expiry)
     * @param settings
     *            the Lutece settings of the cache
     * @return the configuration to use, the given one or a provider specific one that keeps its settings
     */
    <K, V> Configuration<K, V> customize( String strCacheName, Configuration<K, V> configuration, CacheSettings settings );
}
