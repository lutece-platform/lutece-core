package fr.paris.lutece.portal.service.cache;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

/**
 * Producer of Lutece107Cache instance
 * Retrieves a cache instance using @LuteceCache(cacheName = "cacheName", keyType = keyType, ValueType = keyType, enable = enable).
 * <p>Example:</p>
 * <pre>
 * {@code @Inject @LuteceCache(cacheName = "cacheName", keyType = Integer.class, valueType = String.class, enable = true)
 * Lutece107Cache<Integer, String> cacheInstance;}
 * </pre> 
 **/
@ApplicationScoped
public class LuteceCacheProducer {

	@Inject
    protected ILuteceCacheManager luteceCacheManager;
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);	

    /**
	 * Retrieves a cache instance using @LuteceCache(cacheName = "cacheName", keyType = keyType, ValueType = keyType, enable=enable).
	 * <p>Example:</p>
	 * <pre>
	 * {@code @Inject @LuteceCache(cacheName = "cacheName", keyType = Integer.class, valueType = String.class, enable=true)
	 * Lutece107Cache<Integer, String> cacheInstance;}
	 * </pre>
	 * @param <K> the type of keys maintained the cache
     * @param <V> the type of cached values
	 * @param injectionPoint the injectionPoint
	 * @return The Lutece107Cache instance
	 * @throws IllegalArgumentException
	 */
	@Produces
    @LuteceCache(cacheName = "", keyType = Object.class, valueType = Object.class, enable = false, preventGlobalReset = false )
    public <K, V> Lutece107Cache<K, V> produceLuteceCache(InjectionPoint injectionPoint) throws IllegalArgumentException{
		LuteceCache qualifier = injectionPoint.getQualifiers().stream()
                .filter(LuteceCache.class::isInstance)
                .map(LuteceCache.class::cast)
                .findFirst()
                .orElse(null);
        if (qualifier == null || qualifier.cacheName() == null || qualifier.cacheName().isEmpty( )) {
            throw new IllegalStateException("The LuteceCache annotation is missing.");
        }
        Lutece107Cache<K, V> cache= (Lutece107Cache<K, V>) luteceCacheManager.getCache(qualifier.cacheName(), qualifier.keyType(), qualifier.valueType( ));
        if( cache == null ) {
        	cache= new Default107Cache<>(qualifier.cacheName(), (Class<K>) qualifier.keyType(), (Class<V>) qualifier.valueType( ), qualifier.enable( ), qualifier.preventGlobalReset( ) );
    		logger.info("The new cache '{}' is created", cache.getName() );
        	return cache;
        }
		logger.info("The cache '{}' is retrieved from the luteceCacheManager", cache.getName() );

        return cache;
	}
	/**
	 * Retrieves a cache instance using luteceCacheManager.createcache(cacheName cacheName, Configuration c ,enable enable).
	 * @param <K> the type of keys maintained the cache
     * @param <V> the type of cached values
	 * @param parameterHolder the luteceCacheParameterHolder
	 * @return The Lutece107Cache instance
	 * @throws IllegalArgumentException
	 */
	@Produces
	@LuteceCacheWithConfiguration
    public <K, V> Lutece107Cache<K, V> produceLuteceCache(LuteceCacheParameterHolder parameterHolder ) throws IllegalArgumentException{	
        if (parameterHolder.getCacheName() == null || parameterHolder.getCacheName( ).isEmpty( )) {
            throw new IllegalStateException("The LuteceCacheWithConfiguration annotation is missing.");
        }
        Lutece107Cache<K, V> cache= (Lutece107Cache<K, V>) luteceCacheManager.getCache(parameterHolder.getCacheName( ), parameterHolder.getConfig().getKeyType(), parameterHolder.getConfig().getValueType( ));
        if( cache == null ) {
        	cache= new Default107Cache<>( parameterHolder.getCacheName(), parameterHolder.getConfig(), parameterHolder.isEnable( ), parameterHolder.isPreventGlobalReset( ) );
    		logger.info("The new cache '{}' is created", cache.getName() );
    		return cache;
        }
		logger.info("The cache '{}' is retrieved from the luteceCacheManager", cache.getName() );
        return cache;
	}
}
