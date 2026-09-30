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

import fr.paris.lutece.portal.service.cache.LuteceCacheEvent.LuteceCacheEventType;

import jakarta.enterprise.inject.spi.CDI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base Lutece implementation of the Cache using JCache (JSR-107) and cacheable service.
 * This class implements the JCache (JSR-107) cache API and provides the service for enabling and disabling caches
 * from an administration interface.
 * 
 * @param <K> the type of keys maintained by the cache
 * @param <V> the type of cached values
 */
public abstract class AbstractCacheableService<K, V> implements Lutece107Cache<K, V> {
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);

    protected volatile Cache<K, V> _cache;
    protected Configuration<K, V> configuration; 
    protected boolean _bPreventGlobalReset;

    /**
     * Init & create the cache. Should be called by the class (cache) that extends AbstractCacheableService during its initialization.
     */
    public void initCache() {
        initCache(getName());
    }

    /**
     * Init & create the cache. Should be called by the class (cache) that extends AbstractCacheableService during its initialization.
     * 
     * @param strCacheName The cache name
     */
    public void initCache(String strCacheName) {
        createCache(strCacheName);
    }
    /**
     * Init & create the cache. Should be called by the class (cache) that extends AbstractCacheableService during its initialization.
     * 
     * @param strCacheName The cache name
     * @param bEnable enable cache
     */
    public void initCache(String strCacheName, boolean bEnable) {
    	 Configuration<K, V> config = (this.configuration != null) ? this.configuration : new MutableConfiguration<>();
         createCache(strCacheName, config, bEnable);
    }

    /**
     * Init & create the cache. Should be called by the class (cache) that extends AbstractCacheableService during its initialization.
     * 
     * @param strCacheName The cache name
     * @param k The type of keys maintained by the cache
     * @param v The type of cached values
     */
    protected void initCache(String strCacheName, Class<K> k, Class<V> v) {
        createCache(strCacheName, new MutableConfiguration<K, V>().setTypes(k, v));
    }

    /**
     * Init & create the cache.
     * 
     * @param strCacheName The cache name
     * @param configuration a {@link Configuration} for the {@link Cache}
     */
    protected <C extends Configuration<K, V>> void initCache(String strCacheName, C configuration) {
        createCache(strCacheName, configuration);
    }

    /**
     * Create a cache {@link Cache}.
     * 
     * @param strCacheName The cache name
     * @return A cache object
     */
    protected Cache<K, V> createCache(String strCacheName) {
        Configuration<K, V> config = (this.configuration != null) ? this.configuration : new MutableConfiguration<>();
        return createCache(strCacheName, config);
    }

    /**
     * Create a cache for a given Service.
     *
     * @param strCacheName The Cache/Service name
     * @param configuration a {@link Configuration} for the {@link Cache}
     * @return A cache object
     */
    protected <C extends Configuration<K, V>> Cache<K, V> createCache(String strCacheName, C configuration) {
    	return createCache(strCacheName, configuration, false);
    }
    /**
     * Create a cache for a given Service.
     *
     * @param strCacheName The Cache/Service name
     * @param configuration a {@link Configuration} for the {@link Cache}
     * @param bEnable the enbale param
     * @return A cache object
     */
    protected <C extends Configuration<K, V>> Cache<K, V> createCache(String strCacheName, C configuration, boolean enable) {
       
    	ILutece107CacheManager luteceCacheManager = CDI.current().select(ILutece107CacheManager.class).get();
    	Cache<K, V> cache = luteceCacheManager.getCache(strCacheName);
    	if (cache == null && (CacheConfigUtil.getStatusFromDataBase(strCacheName) || enable)) {
    		cache = createOrGetCache(luteceCacheManager, strCacheName, configuration);
        }
    	this.configuration = (cache != null && !cache.isClosed()) ? cache.getConfiguration(Configuration.class) : configuration;
    	_cache = cache;
        CacheService.registerCacheableService(this);
        return cache;
    }

    /**
     * Creates the named cache, or returns it when another request created it in the meantime.
     *
     * @param luteceCacheManager the cache manager
     * @param strCacheName the cache name
     * @param configuration the configuration of the cache to create
     * @return the cache
     * @throws CacheException if the cache cannot be created and does not exist
     */
    private <C extends Configuration<K, V>> Cache<K, V> createOrGetCache(ILutece107CacheManager luteceCacheManager, String strCacheName, C configuration) {
        try {
            return luteceCacheManager.createCache(strCacheName, configuration);
        } catch (CacheException e) {
            Cache<K, V> cache = luteceCacheManager.getCache(strCacheName);
            if (cache == null) {
                throw e;
            }
            return cache;
        }
    }

    /**
     * Put an object into the cache.
     * This method will be removed in the next version. Use {@link #put(K, V)} instead.
     * 
     * @param strKey The key of the object to put into the cache
     * @param object The object to put into the cache
     */
    @Deprecated
    public void putInCache(K strKey, V object) {
        put(strKey, object);
    }

    /**
     * Gets an object from the cache.
     * This method will be removed in the next version. Use {@link #get(K)} instead.
     * 
     * @param strKey The key of the object to retrieve from the cache
     * @return The object from the cache
     */
    @Deprecated
    public V getFromCache(K strKey) {
        return get(strKey);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isCacheEnable() {
        return activeCache() != null;
    }

    /**
     * Returns the underlying cache when it is enabled. A disabled cache stores nothing: reads miss and writes are ignored.
     *
     * @return the open cache, or null when the cache is disabled
     */
    private Cache<K, V> activeCache() {
        Cache<K, V> cache = _cache;
        return (cache != null && !cache.isClosed()) ? cache : null;
    }

    /**
     * Returns the underlying cache for an operation that has no meaning on a disabled cache.
     *
     * @return the open cache
     * @throws IllegalStateException if the cache is disabled
     */
    private Cache<K, V> requireCache() {
        Cache<K, V> cache = activeCache();
        if (cache == null) {
            throw new IllegalStateException("The cache '" + getName() + "' is disabled");
        }
        return cache;
    }
    /**
     * {@inheritDoc }
     */
    public int getCacheSize() {
        int cacheSize = 0;
        Cache<K, V> cache = activeCache();
        if (cache != null) {
	        for (Cache.Entry<K, V> entry : cache) {
	            if (entry != null) {
	            	cacheSize++;
	            }
	        }
        }
        return cacheSize;          
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public void enableCache(boolean bEnable) {
        CacheService.updateCacheStatus(this);
        Cache<K, V> cache = activeCache();
        if (!bEnable && cache != null) {
            cache.clear();
            cache.close();
        }

        if (bEnable && cache == null) {
            this.initCache(getName(), bEnable );;
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void resetCache() {
        try {
            Cache<K, V> cache = activeCache();
            if (cache != null) {
                cache.removeAll();
                CDI.current().getBeanManager().getEvent( ).fire(new LuteceCacheEvent( cache, LuteceCacheEventType.RESET ));
            }
        } catch (CacheException | IllegalStateException e) {
        	logger.error(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        Cache<K, V> cache = activeCache();
        if (cache != null) {
	        for (Cache.Entry<K, V> entry : cache) {
	            if (entry != null) {
	                keys.add(entry.getKey());
	            }
	        }
        }
        return keys;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getInfos() {
        return CacheConfigUtil.getInfos(this.configuration);
    }

    /**
     * Remove a key from the cache.
     * This method will be removed in the next version. Use {@link #remove(K)} instead.
     *
     * @param strKey The key to remove
     */
    @Deprecated
    public void removeKey(K strKey) {
        remove(strKey);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public V get(K key) {
        Cache<K, V> cache = activeCache();
        return cache != null ? cache.get(key) : null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        Cache<K, V> cache = activeCache();
        return cache != null ? cache.getAll(keys) : Collections.emptyMap();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean containsKey(K key) {
        Cache<K, V> cache = activeCache();
        return cache != null && cache.containsKey(key);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, CompletionListener completionListener) {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.loadAll(keys, replaceExistingValues, completionListener);
        } else if (completionListener != null) {
            completionListener.onCompletion();
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void put(K key, V value) {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.put(key, value);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public V getAndPut(K key, V value) {
        Cache<K, V> cache = activeCache();
        return cache != null ? cache.getAndPut(key, value) : null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.putAll(map);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean putIfAbsent(K key, V value) {
        Cache<K, V> cache = activeCache();
        return cache != null && cache.putIfAbsent(key, value);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean remove(K key) {
        Cache<K, V> cache = activeCache();
        return cache != null && cache.remove(key);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean remove(K key, V oldValue) {
        Cache<K, V> cache = activeCache();
        return cache != null && cache.remove(key, oldValue);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public V getAndRemove(K key) {
        Cache<K, V> cache = activeCache();
        return cache != null ? cache.getAndRemove(key) : null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Cache<K, V> cache = activeCache();
        return cache != null && cache.replace(key, oldValue, newValue);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean replace(K key, V value) {
        Cache<K, V> cache = activeCache();
        return cache != null && cache.replace(key, value);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public V getAndReplace(K key, V value) {
        Cache<K, V> cache = activeCache();
        return cache != null ? cache.getAndReplace(key, value) : null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeAll(Set<? extends K> keys) {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.removeAll(keys);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeAll() {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.removeAll();
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clear() {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.clear();
            CDI.current().getBeanManager().getEvent( ).fire(new LuteceCacheEvent( cache, LuteceCacheEventType.CLEAR ));
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <C extends Configuration<K, V>> C getConfiguration(Class<C> clazz) {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            return cache.getConfiguration(clazz);
        }
        if (clazz.isInstance(this.configuration)) {
            return clazz.cast(this.configuration);
        }
        throw new IllegalArgumentException("The configuration of the cache '" + getName() + "' is not a " + clazz.getName());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> T invoke(K key, EntryProcessor<K, V, T> entryProcessor, Object... arguments) {
        return requireCache().invoke(key, entryProcessor, arguments);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> keys, EntryProcessor<K, V, T> entryProcessor, Object... arguments) {
        return requireCache().invokeAll(keys, entryProcessor, arguments);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CacheManager getCacheManager() {
        return requireCache().getCacheManager();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void close() {
        Cache<K, V> cache = activeCache();
        if (cache != null) {
            cache.close();
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isClosed() {
        return activeCache() == null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> T unwrap(Class<T> clazz) {
        return requireCache().unwrap(clazz);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        requireCache().registerCacheEntryListener(cacheEntryListenerConfiguration);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
        requireCache().deregisterCacheEntryListener(cacheEntryListenerConfiguration);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        Cache<K, V> cache = activeCache();
        return cache != null ? cache.iterator() : Collections.emptyIterator();
    }

    /**
     * Set cache.
     * 
     * @param cache the cache to set
     */
    public void setCache(Cache<K, V> cache) {
        _cache = cache;
    }

    /**
     * Return a cache object.
     * 
     * @return cache object
     */
    public Cache<K, V> getCache() {
        return _cache;
    }

    @Override
    public boolean isPreventGlobalReset( )
    {
        return _bPreventGlobalReset;
    }
}