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
package fr.paris.lutece.portal.service.cache;


import javax.cache.configuration.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

/**
 * Manages the creation and retrieval of cache instances (Lutece107Cache) in the Lutece application.
 * <p>
 * This class uses CDI to manage cache instances, providing methods to create and retrieve caches
 * based on their name, key type, value type, and configuration settings.
 * </p>
 */
@ApplicationScoped
public class LuteceCacheManager implements ILuteceCacheManager
{
	@Inject
    private LuteceCacheParameterHolder parameterHolder;
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);
   
    /**
     * {@inheritDoc }
     */
    @Override
    public <K, V, C extends Configuration<K, V>> Lutece107Cache<K, V> createCache( String strCacheName, C configuration, boolean enable ) 
    		throws IllegalArgumentException 
    {
    	logger.debug("Start creating a named cache: '{}', key type: '{}', value type: '{}', enable: '{}'", 
                strCacheName, configuration.getKeyType(), configuration.getValueType(), enable);
    	parameterHolder.setCacheName(strCacheName);
    	parameterHolder.setConfig(configuration);
    	parameterHolder.setEnable( enable );
    	 // Obtain the cache using CDI        
    	return CDI.current().select(new TypeLiteral<Lutece107Cache<K, V>>() {}, new LuteceCacheWithConfigurationLiteral()).get();

    }
    /**
     * {@inheritDoc }
     */
    @Override
    public <K, V> Lutece107Cache<K, V> createCache( String strCacheName ) 
    		throws IllegalArgumentException 
    {
        logger.debug("Start creating a named cache: '{}'", strCacheName);
        return CDI.current().select(
                new TypeLiteral<Lutece107Cache<K, V>>() {},
                new LuteceCacheLiteral(strCacheName, (Class<K>)Object.class, (Class<V>)Object.class, false, false)
            ).get(); 

    }
    /**
     * {@inheritDoc }
     */
    @Override
	public <K, V, C extends Configuration<K, V>> Lutece107Cache<K, V> createCache(String strCacheName,  Class<K> k, Class<V> v) 
			throws IllegalArgumentException {
    	logger.debug("Start creating a named cache: '{}', key type: '{}', value type: '{}'", 
                strCacheName, k, v);
    	return CDI.current().select(
                new TypeLiteral<Lutece107Cache<K, V>>() {},
                new LuteceCacheLiteral(strCacheName, k, v, false, false)
            ).get();    	
	}
    /**
     * {@inheritDoc }
     */
    @Override
	public <K, V, C extends Configuration<K, V>> Lutece107Cache<K, V> createCache(String strCacheName,  Class<K> k, Class<V> v, boolean enable) 
			throws IllegalArgumentException {
    	logger.debug("Start creating a named cache: '{}', key type: '{}', value type: '{}', enable: '{}'", 
                strCacheName, k, v, enable);
    	return CDI.current().select(
                new TypeLiteral<Lutece107Cache<K, V>>() {},
                new LuteceCacheLiteral(strCacheName, k, v, enable, false)
            ).get();    	
	}
    @Override
    public <K, V, C extends Configuration<K, V>> Lutece107Cache<K, V> createCache( String strCacheName, Class<K> k, Class<V> v, boolean enable,
            boolean automaticResetDisabled ) throws IllegalArgumentException
    {
        logger.debug("Start creating a named cache: '{}', key type: '{}', value type: '{}', enable: '{}', automaticResetDisabled: '{}'", 
                strCacheName, k, v, enable, automaticResetDisabled);
        return CDI.current().select(
                new TypeLiteral<Lutece107Cache<K, V>>() {},
                new LuteceCacheLiteral(strCacheName, k, v, enable, automaticResetDisabled)
            ).get();        
    }
    /**
     * {@inheritDoc }
     */
	@Override
	public <K, V> Lutece107Cache<K, V> getCache(String cacheName, Class<K> k, Class<V> v) {
		
		logger.debug("Looking up a managed cache with name '{}'", cacheName);
	    logger.debug("Get named cache: '{}', key type: '{}', value type: '{}'", cacheName, k, v);
	    if (cacheName == null) {
    	    throw new NullPointerException("The cacheName is null");
    	}
    	if (k == null) {
    	    throw new NullPointerException("The keyType is null");
    	}
    	if (v == null) {
    	    throw new NullPointerException("The valueType is null");
    	}
		CacheableService<K, V> cacheableService= CacheService.getCache(cacheName);
		if (cacheableService == null) {
            logger.debug("The cache '{}' does not exist in the _listCacheableServicesRegistry called by the CacheService", cacheName);		    return null;
		}		    
		if(!Lutece107Cache.class.isAssignableFrom(cacheableService.getClass())) {
		    	throw new ClassCastException("Cache type: " + cacheableService.getName() +" is not Lutece107Cache instance");
		}
		Lutece107Cache<K, V> cache= (Lutece107Cache<K, V>) cacheableService;
		Class<?> actualKeyType = cache.getConfiguration(Configuration.class).getKeyType();
		Class<?> actualValueType = cache.getConfiguration(Configuration.class).getValueType();

		if (k != actualKeyType) {
		     throw new ClassCastException("Cache has key type " + actualKeyType.getName()
		          + ", but getCache() called with key type " + k.getName());
		}
		if (v != actualValueType) {
		     throw new ClassCastException("Cache has value type " + actualValueType.getName()
		          + ", but getCache() called with value type " + v.getName());
		}
        logger.debug("The cache '{}' is found and its type is '{}'", cacheableService.getName(), cacheableService.getClass());
        return cache;
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public <K, V> Lutece107Cache<K, V> getCache(String cacheName) {
		logger.debug("Looking up a managed cache with name '{}'", cacheName);
        logger.debug("Get named cache: '{}'", cacheName);

		if (cacheName == null) {
    	    throw new NullPointerException("The cacheName is null");
		 }
		 CacheableService<K, V> cacheableService= CacheService.getCache(cacheName);
		 if (cacheableService == null) {
	            logger.debug("The cache '{}' does not exist in the _listCacheableServicesRegistry called by the CacheService", cacheName);		     return null;
		 }
		if(!Lutece107Cache.class.isAssignableFrom(cacheableService.getClass())) {
		   	throw new ClassCastException("Cache type: " + cacheableService.getName() +" is not Lutece107Cache instance");
		 }
        logger.debug("The cache '{}' is found and its type is '{}'", cacheableService.getName(), cacheableService.getClass());		return (Lutece107Cache<K, V>) cacheableService;
	}
   
}

