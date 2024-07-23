package fr.paris.lutece.portal.service.cache;

import javax.cache.Cache;
import javax.cache.CacheManager;

public interface ICacheService{
	  /**
	   * Looks up a managed {@link Cache} given its name.
	   * <p>
	   * Use this method to check runtime key and value types.
	   * <p>
	   * Use {@link #getCache(String)} where this check is not required.
	   * <p>
	   * Implementations must ensure that the key and value types are the same as
	   * those configured for the {@link Cache} prior to returning from this method.
	   * <p>
	   * Implementations may further perform type checking on mutative cache operations
	   * and throw a {@link ClassCastException} if these checks fail.
	   * <p>
	   * Implementations that support declarative mechanisms for pre-configuring
	   * {@link Cache}s may return a pre-configured {@link Cache} instead of
	   * <code>null</code>.
	   *
	   * @param <K> the type of key
	   * @param <V> the type of value
	   * @param cacheName the name of the managed {@link Cache} to acquire
	   * @param keyType   the expected {@link Class} of the key
	   * @param valueType the expected {@link Class} of the value
	   * @return the Cache or null if it does exist or can't be pre-configured
	   * @throws IllegalStateException    if the {@link CacheManager}
	   *                                  is {@link #isClosed()}
	   * @throws ClassCastException       if the specified key and/or value types are
	   *                                  incompatible with the configured cache.
	   * @throws NullPointerException     if either keyType or classType is null.
	   * @throws SecurityException        when the operation could not be performed
	   *                                  due to the current security settings
	   */
	  <K, V> Cache<K, V> getCache( String cacheName );
	  
	 /**
	  * Shutdown the cache service and the cache manager. Should be called when the webapp is stopped.
	  */
	  public void shutdown( );
	  
	  /**
	   * Registers a new CacheableService
	   *
	   * @param cs
	   *            The CacheableService
	   */
	   
	  public  void registerCacheableService( CacheableService<?,?> cs );
}
