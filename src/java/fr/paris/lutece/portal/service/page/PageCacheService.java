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
package fr.paris.lutece.portal.service.page;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import javax.cache.Cache;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.util.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.ServletContext;

/**
 * Page Cache Service
 */
@ApplicationScoped
public class PageCacheService extends AbstractCacheableService<String,String>
{
    private static final String SERVICE_NAME = "PageCacheService";

    // Performance patch
    private static ConcurrentHashMap<String, String> _keyMemory = new ConcurrentHashMap<>( );
    
    /**
     * Init the cache. Should be called by the service at its initialization.
     * 
     * @param strCacheName
     *            The cache name
     */
    @Override
    public void initCache( String strCacheName )
    {
    	Cache<String, String> cache= createCache( strCacheName, new MutableConfiguration<String,String>().setTypes(String.class, String.class)  );
        if ( null != cache )
        {
            cache.registerCacheEntryListener( new MutableCacheEntryListenerConfiguration<String, String>(
                    FactoryBuilder.factoryOf( new PageCacheEntryListener<String, String>( ) ), null, false, false ) );
            CacheService.registerCacheableService( this );
        }
    }

    /**
     * {@inheritDoc }
     */
    public String getName( )
    {
        return SERVICE_NAME;
    }

    /**
     * Get a memory key
     * 
     * @param strKey
     *            The key
     * @return The key
     */
    String getKey( String strKey )
    {
        String keyInMemory = _keyMemory.putIfAbsent( strKey, strKey );

        if ( keyInMemory != null )
        {
            return keyInMemory;
        }

        return strKey;
    }

    /**
     * @see java.lang.Object#clone()
     * @return the instance
     */
    @Override
    public Object clone( )
    {
        throw new AppException( "This class shouldn't be cloned" );
    }

	static class PageCacheEntryListener<K, V> implements CacheEntryRemovedListener<K, V>, CacheEntryExpiredListener<K, V>, Serializable
	{

		@Override
		public void onExpired(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
				throws CacheEntryListenerException {
			for (CacheEntryEvent<? extends K, ? extends V> event : events)
	        {
				_keyMemory.remove( (String) event.getKey( ) );
	        }
		}

		@Override
		public void onRemoved(Iterable<CacheEntryEvent<? extends K, ? extends V>> events)
				throws CacheEntryListenerException {
            for ( CacheEntryEvent<? extends K, ? extends V> event : events )
            {
                _keyMemory.remove( (String) event.getKey( ) );
            }
        }

	}

	/**
     * This method observes the initialization of the {@link ApplicationScoped} context.
     * It ensures that this CDI beans are instantiated at the application startup.
     *
     * <p>This method is triggered automatically by CDI when the {@link ApplicationScoped} context is initialized,
     * which typically occurs during the startup of the application server.</p>
     *
     * @param context the {@link ServletContext} that is initialized. This parameter is observed
     *                and injected automatically by CDI when the {@link ApplicationScoped} context is initialized.
     */
    public void initializedService(@Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        // This method is intentionally left empty to trigger CDI bean instantiation
    }
}
