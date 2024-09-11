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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppPathService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * Provides cache object for cacheable services
 */
@ApplicationScoped
public class Lutece107CacheManager implements ILutece107CacheManager
{
    private static final String PROPERTY_PATH_CONF = "path.conf";
    private static final String FILE_CACHES_STATUS = "caches.dat";
    
    // Datastore
    private static final String KEY_PREFIX = "core.cache.status.";
  
    private final static String DEFAULT_JSR107_CACHING_PROVIDER="org.ehcache.jsr107.EhcacheCachingProvider";
   
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);

    private CacheManager _manager;
 
    @Inject
    @ConfigProperty(name="lutece.cache.jcache.cachingprovider", defaultValue=DEFAULT_JSR107_CACHING_PROVIDER)
    private  String cachingProviderClass;    
    
    @Inject
    @ConfigProperty(name="lutece.cache.jcache.config.uri", defaultValue="null" )
    private  String uriValue;


    /**
     * Requests a {@link CacheManager} configured according to the implementation
     * specific {@link URI} be made available that uses the provided
     * {@link ClassLoader} for loading underlying classes. 
     * @throws CacheException    when a {@link CacheManager} for the
     *                           specified arguments could not be produced
     * @throws SecurityException when the operation could not be performed
     *                           due to the current security settings
     */
    @PostConstruct
    public void initCacheService() {
    		logger.info("start init Lutece107CacheManager");
    		loadCachesConfig( );
            CachingProvider cachingProvider = Caching.getCachingProvider(cachingProviderClass);                       
            Properties properties = new Properties();
            uriValue= "null".equals(uriValue)?null:uriValue;
            URI uri = null;
			try {
				uri = CacheConfigUtil.preConfigureCacheManager(uriValue, cachingProvider, properties);
			} catch (IOException e) {
				logger.error(" Error Uri CacheManager", e);
			}
            _manager = cachingProvider.getCacheManager(uri, null, properties); 
    		logger.info(" Lutece107CacheManager initialized ");

    }
    /**
     * Closing the cache manager when the web application stops
     */
    public void cacheShutDownEvent( @Observes CacheService.ShutDownEvent event )
    {
    	_manager.close();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache( String strCacheName, C configuration ) throws IllegalArgumentException 
    {
        return _manager.createCache(strCacheName, configuration);
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public void close( )
    {
        _manager.close();
    }
    /**
     * {@inheritDoc }
     */
	@Override
	public CachingProvider getCachingProvider() {
		return _manager.getCachingProvider( );
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public URI getURI() {
		return _manager.getURI( );
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public ClassLoader getClassLoader() {
		return _manager.getClassLoader( );
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public Properties getProperties() {
		return _manager.getProperties( );
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public <K, V> Cache<K, V> getCache(String cacheName) {
		return _manager.getCache(cacheName);
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public Iterable<String> getCacheNames() {
		return _manager.getCacheNames( );
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public void destroyCache(String cacheName) {
		_manager.destroyCache(cacheName);
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public void enableManagement(String cacheName, boolean enabled) {
		_manager.enableManagement(cacheName, enabled);
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public void enableStatistics(String cacheName, boolean enabled) {
		_manager.enableStatistics(cacheName, enabled);
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public boolean isClosed() {
		return _manager.isClosed( );
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public <T> T unwrap(Class<T> clazz) {
		return _manager.unwrap(clazz);
	}
	/**
     * {@inheritDoc }
     */
	@Override
	public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
		return _manager.getCache(cacheName, keyType, valueType);
	}
	 /**
     * Load caches status
     */
    private void loadCachesConfig( )
    {
    	logger.info("Load caches status");
        String strCachesStatusFile = AppPathService.getPath( PROPERTY_PATH_CONF, FILE_CACHES_STATUS );
        File file = new File( strCachesStatusFile );

        try ( FileInputStream fis = new FileInputStream( file ) )
        {
            Properties properties = new Properties( );
            properties.load( fis );

            // If the keys aren't found in the datastore then create a key in it
            for ( String strKey : properties.stringPropertyNames( ) )
            {
                String strDSKey = KEY_PREFIX + strKey;

                if ( !DatastoreService.existsInstanceKey( strDSKey ) )
                {
                    String strValue = properties.getProperty( strKey );
                    DatastoreService.setInstanceDataValue( strDSKey, strValue );
                	logger.debug("create a key '{}' with value '{}' in datastore ", strKey, strValue);
                }
            }
        }
        catch( FileNotFoundException e )
        {
        	logger.error( "No cache.dat file. Should be created at shutdown." );
        }
        catch( Exception e )
        {
        	logger.error( "Error loading caches status defined in file : {}", file.getAbsolutePath( ), e );
        }
    }
}

