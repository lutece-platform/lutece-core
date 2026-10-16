package fr.paris.lutece.portal.service.cache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManageCacheService {

	private static final Logger logger = LogManager.getLogger( CacheConfigUtil.CACHE_LOGGER_NAME );
	
	/**
     * A concurrent map storing registered cacheable services.
     * <p>
     * The map associates cache names with their respective cacheable service
     * instances. This ensures thread-safe operations for adding and retrieving
     * cacheable services.
     * </p>
     */
    private final ConcurrentMap<String, CacheableService<?, ?>> _listCacheableServicesRegistry = new ConcurrentHashMap<>();


    /**
     * Registers a cacheable service in the registry.
     * <p>
     * This method adds the provided cacheable service to the registry if a
     * service with the same name is not already present. The cache status is
     * determined and applied to the service based on its current configuration
     * and status read from a file.
     * </p>
     * 
     * @param <K> the type of cache keys
     * @param <V> the type of cache values
     * @param cs the cacheable service to register
     */
	public <K, V> void registerCacheableService(CacheableService<K, V> cs) {
		
		_listCacheableServicesRegistry.putIfAbsent(cs.getName(), cs);
		// read cache status from file "caches.dat"
		cs.enableCache( getStatus( cs ) || cs.isCacheEnable( ) );
	 }
	/**
     * Checks if a cache with the specified name exists in the registry.
     * <p>
     * This method checks whether a cacheable service with the given name is
     * present in the registry. This helps determine if a cache can be accessed
     * or managed.
     * </p>
     * 
     * @param cacheName the name of the cache
     * @return {@code true} if a cache with the specified name exists; {@code false} otherwise
     */
	public <K, V> boolean isCacheExist(String cacheName) {
		return _listCacheableServicesRegistry.containsKey(cacheName);
	 }
	 /**
     * Retrieves a cacheable service by its name from the registry.
     * <p>
     * This method returns the cacheable service associated with the specified
     * name. The returned service is cast to the appropriate type based on
     * the generic parameters.
     * </p>
     * 
     * @param <K> the type of cache keys
     * @param <V> the type of cache values
     * @param cacheName the name of the cache
     * @return the cacheable service associated with the name, or {@code null}
     *         if no such service exists
     */
	@SuppressWarnings("unchecked")
	public <K, V> CacheableService<K,V> getCache(String cacheName) {
		return (CacheableService<K, V>) _listCacheableServicesRegistry.get(cacheName);
	 }
	/**
     * Returns all registered Cacheable services
     *
     * @return A collection containing all registered Cacheable services
     */
	public List<CacheableService<?, ?>> getCacheableServicesList( ){
		return _listCacheableServicesRegistry.values().stream().collect(Collectors.toList());
    }
	 /**
     * Reset all caches
     */
    public void resetCaches( )
    {
    	 // Reset cache
        _listCacheableServicesRegistry.values( ).stream( ).filter( Predicate.not( CacheableService::isPreventGlobalReset ) )
                .forEach( cs -> cs.resetCache( ) );
    }
    /**
     * Applies to this instance the status of each cache saved in the database. With several instances sharing their
     * caches, a cache enabled or disabled from the back office of one instance is then enabled or disabled on the
     * others too. A cache whose status is unchanged is not touched; a failure on a cache does not stop the others.
     */
    public void refreshStatuses( )
    {
        for ( CacheableService<?, ?> cs : _listCacheableServicesRegistry.values( ) )
        {
            try
            {
                boolean bEnabled = getStatus( cs );
                if ( bEnabled != cs.isCacheEnable( ) )
                {
                    logger.info( "Cache {} {} by another instance", cs.getName( ), bEnabled ? "enabled" : "disabled" );
                    cs.enableCache( bEnabled );
                }
            }
            catch( RuntimeException e )
            {
                logger.warn( "The status of the cache {} could not be refreshed", cs.getName( ), e );
            }
        }
    }

    /**
     * Returns the cache status froms database
     *
     * @param cs
     *            The cacheable service
     * @return The status
     */
    private static boolean getStatus( CacheableService<?,?> cs )
    {
    	return CacheConfigUtil.getStatusFromDataBase(cs.getName( ));
      
    }  
}