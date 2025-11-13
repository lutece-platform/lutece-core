package fr.paris.lutece.portal.service.cache;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;

public interface ILuteceCacheManager {

	/**
	   * Creates a named {@link Cache} at runtime.
	   * <p>
	   * If a {@link Cache} with the specified name is known to the {@link
	   * CacheManager}, a CacheException is thrown.
	   * <p>
	   * If a {@link Cache} with the specified name is unknown the {@link
	   * CacheManager}, one is created according to the provided {@link Configuration}
	   * after which it becomes managed by the {@link CacheManager}.
	   * <p>
	   * Prior to a {@link Cache} being created, the provided {@link Configuration}s is
	   * validated within the context of the {@link CacheManager} properties and
	   * implementation.
	   * <p>
	   * Implementers should be aware that the {@link Configuration} may be used to
	   * configure other {@link Cache}s.
	   * <p>
	   * There's no requirement on the part of a developer to call this method for
	   * each {@link Cache} an application may use.  Implementations may support
	   * the use of declarative mechanisms to pre-configure {@link Cache}s, thus
	   * removing the requirement to configure them in an application.  In such
	   * circumstances a developer may simply call either the
	   * {@link #getCache(String)} or {@link #getCache(String, Class, Class)}
	   * methods to acquire a previously established or pre-configured {@link Cache}.
	   *
	   * @param <K> the type of key
	   * @param <V> the type of value
	   * @param <C> the type of the Configuration
	   * @param cacheName     the name of the {@link Cache}. Names should not use
	   *                      forward slashes(/) or colons(:), or start with
	   *                      java. or javax. These prefixes are reserved.
	   * @param enable If enable is set to true, the cache will be initialized upon its creation.
	   * @param configuration a {@link Configuration} for the {@link Cache}
	   * @throws IllegalStateException         if the {@link CacheManager}
	   *                                       {@link #isClosed()}
	   * @throws CacheException                if there was an error configuring the
	   *                                       {@link Cache}, which includes trying
	   *                                       to create a cache that already exists.
	   * @throws IllegalArgumentException      if the configuration is invalid
	   * @throws UnsupportedOperationException if the configuration specifies
	   *                                       an unsupported feature
	   * @throws NullPointerException          if the cache configuration or name
	   *                                       is null
	   * @throws SecurityException             when the operation could not be performed
	   *                                       due to the current security settings
	   */
	   <K, V, C extends Configuration<K, V> > Lutece107Cache<K, V> createCache(String cacheName, C configuration, boolean enable)
	      throws IllegalArgumentException;
	  
	  /**
	   * Creates a named {@link Cache} at runtime.
	   * <p>
	   * If a {@link Cache} with the specified name is known to the {@link
	   * CacheManager}, a CacheException is thrown.
	   * <p>
	   * If a {@link Cache} with the specified name is unknown the {@link
	   * CacheManager}, one is created according to the provided {@link Configuration}
	   * after which it becomes managed by the {@link CacheManager}.
	   * <p>
	   * Prior to a {@link Cache} being created, the provided {@link Configuration}s is
	   * validated within the context of the {@link CacheManager} properties and
	   * implementation.
	   * <p>
	   * Implementers should be aware that the {@link Configuration} may be used to
	   * configure other {@link Cache}s.
	   * <p>
	   * There's no requirement on the part of a developer to call this method for
	   * each {@link Cache} an application may use.  Implementations may support
	   * the use of declarative mechanisms to pre-configure {@link Cache}s, thus
	   * removing the requirement to configure them in an application.  In such
	   * circumstances a developer may simply call either the
	   * {@link #getCache(String)} or {@link #getCache(String, Class, Class)}
	   * methods to acquire a previously established or pre-configured {@link Cache}.
	   *
	   * @param <K> the type of key
	   * @param <V> the type of value
	   * @param cacheName     the name of the {@link Cache}. Names should not use
	   *                      forward slashes(/) or colons(:), or start with
	   *                      java. or javax. These prefixes are reserved.
	   * @throws IllegalStateException         if the {@link CacheManager}
	   *                                       {@link #isClosed()}
	   * @throws CacheException                if there was an error configuring the
	   *                                       {@link Cache}, which includes trying
	   *                                       to create a cache that already exists.
	   * @throws IllegalArgumentException      if the configuration is invalid
	   * @throws UnsupportedOperationException if the configuration specifies
	   *                                       an unsupported feature
	   * @throws NullPointerException          if the cache configuration or name
	   *                                       is null
	   * @throws SecurityException             when the operation could not be performed
	   *                                       due to the current security settings
	   */
	   <K, V > Lutece107Cache<K, V> createCache(String cacheName)
	      throws IllegalArgumentException;
	  
	  /**
	   * Creates a named {@link Cache} at runtime.
	   * <p>
	   * If a {@link Cache} with the specified name is known to the {@link
	   * CacheManager}, a CacheException is thrown.
	   * <p>
	   * If a {@link Cache} with the specified name is unknown the {@link
	   * CacheManager}, one is created according to the provided {@link Configuration}
	   * after which it becomes managed by the {@link CacheManager}.
	   * <p>
	   * Prior to a {@link Cache} being created, the provided {@link Configuration}s is
	   * validated within the context of the {@link CacheManager} properties and
	   * implementation.
	   * <p>
	   * Implementers should be aware that the {@link Configuration} may be used to
	   * configure other {@link Cache}s.
	   * <p>
	   * There's no requirement on the part of a developer to call this method for
	   * each {@link Cache} an application may use.  Implementations may support
	   * the use of declarative mechanisms to pre-configure {@link Cache}s, thus
	   * removing the requirement to configure them in an application.  In such
	   * circumstances a developer may simply call either the
	   * {@link #getCache(String)} or {@link #getCache(String, Class, Class)}
	   * methods to acquire a previously established or pre-configured {@link Cache}.
	   *
	   * @param <K> the type of key
	   * @param <V> the type of value
	   * @param <C> the type of the Configuration
	   * @param cacheName     the name of the {@link Cache}. Names should not use
	   *                      forward slashes(/) or colons(:), or start with
	   *                      java. or javax. These prefixes are reserved.
	   * @param configuration a {@link Configuration} for the {@link Cache}
	   * @throws IllegalStateException         if the {@link CacheManager}
	   *                                       {@link #isClosed()}
	   * @throws CacheException                if there was an error configuring the
	   *                                       {@link Cache}, which includes trying
	   *                                       to create a cache that already exists.
	   * @throws IllegalArgumentException      if the configuration is invalid
	   * @throws UnsupportedOperationException if the configuration specifies
	   *                                       an unsupported feature
	   * @throws NullPointerException          if the cache configuration or name
	   *                                       is null
	   * @throws SecurityException             when the operation could not be performed
	   *                                       due to the current security settings
	   */
	   <K, V, C extends Configuration<K, V> > Lutece107Cache<K, V> createCache(String cacheName,  Class<K> k, Class<V> v)
	      throws IllegalArgumentException;
	   /**
		 * Creates a named {@link Cache} at runtime.
		 * <p>
		 * If a {@link Cache} with the specified name is known to the {@link
		 * CacheManager}, a CacheException is thrown.
		 * <p>
		 * If a {@link Cache} with the specified name is unknown the {@link
		 * CacheManager}, one is created according to the provided {@link Configuration}
		 * after which it becomes managed by the {@link CacheManager}.
		 * <p>
		 * Prior to a {@link Cache} being created, the provided {@link Configuration}s is
		 * validated within the context of the {@link CacheManager} properties and
		 * implementation.
		 * <p>
		 * Implementers should be aware that the {@link Configuration} may be used to
		 * configure other {@link Cache}s.
		 * <p>
		 * There's no requirement on the part of a developer to call this method for
		 * each {@link Cache} an application may use.  Implementations may support
		 * the use of declarative mechanisms to pre-configure {@link Cache}s, thus
		 * removing the requirement to configure them in an application.  In such
		 * circumstances a developer may simply call either the
		 * {@link #getCache(String)} or {@link #getCache(String, Class, Class)}
		 * methods to acquire a previously established or pre-configured {@link Cache}.
		 *
		 * @param <K> the type of key
		 * @param <V> the type of value
		 * @param <C> the type of the Configuration
		 * @param cacheName     the name of the {@link Cache}. Names should not use
		 *                      forward slashes(/) or colons(:), or start with
		 *                      java. or javax. These prefixes are reserved.
		 * @param enable If enable is set to true, the cache will be initialized upon its creation.
		 * @param configuration a {@link Configuration} for the {@link Cache}
		 * @throws IllegalStateException         if the {@link CacheManager}
		 *                                       {@link #isClosed()}
		 * @throws CacheException                if there was an error configuring the
		 *                                       {@link Cache}, which includes trying
		 *                                       to create a cache that already exists.
		 * @throws IllegalArgumentException      if the configuration is invalid
		 * @throws UnsupportedOperationException if the configuration specifies
		 *                                       an unsupported feature
		 * @throws NullPointerException          if the cache configuration or name
		 *                                       is null
		 * @throws SecurityException             when the operation could not be performed
		 *                                       due to the current security settings
		 */
	   <K, V, C extends Configuration<K, V>> Lutece107Cache<K, V> createCache(String strCacheName,  Class<K> k, Class<V> v, boolean enable) throws IllegalArgumentException; 

       /**
        * Creates a named {@link Cache} at runtime.
        * <p>
        * If a {@link Cache} with the specified name is known to the {@link
        * CacheManager}, a CacheException is thrown.
        * <p>
        * If a {@link Cache} with the specified name is unknown the {@link
        * CacheManager}, one is created according to the provided {@link Configuration}
        * after which it becomes managed by the {@link CacheManager}.
        * <p>
        * Prior to a {@link Cache} being created, the provided {@link Configuration}s is
        * validated within the context of the {@link CacheManager} properties and
        * implementation.
        * <p>
        * Implementers should be aware that the {@link Configuration} may be used to
        * configure other {@link Cache}s.
        * <p>
        * There's no requirement on the part of a developer to call this method for
        * each {@link Cache} an application may use.  Implementations may support
        * the use of declarative mechanisms to pre-configure {@link Cache}s, thus
        * removing the requirement to configure them in an application.  In such
        * circumstances a developer may simply call either the
        * {@link #getCache(String)} or {@link #getCache(String, Class, Class)}
        * methods to acquire a previously established or pre-configured {@link Cache}.
        *
        * @param <K> the type of key
        * @param <V> the type of value
        * @param <C> the type of the Configuration
        * @param cacheName     the name of the {@link Cache}. Names should not use
        *                      forward slashes(/) or colons(:), or start with
        *                      java. or javax. These prefixes are reserved.
        * @param enable If enable is set to true, the cache will be initialized upon its creation.
        * @param preventGlobalReset If set to true, the cache will not be reset by global cache resets, like resetAll
        * @param configuration a {@link Configuration} for the {@link Cache}
        * @throws IllegalStateException         if the {@link CacheManager}
        *                                       {@link #isClosed()}
        * @throws CacheException                if there was an error configuring the
        *                                       {@link Cache}, which includes trying
        *                                       to create a cache that already exists.
        * @throws IllegalArgumentException      if the configuration is invalid
        * @throws UnsupportedOperationException if the configuration specifies
        *                                       an unsupported feature
        * @throws NullPointerException          if the cache configuration or name
        *                                       is null
        * @throws SecurityException             when the operation could not be performed
        *                                       due to the current security settings
        */
	   <K, V, C extends Configuration<K, V>> Lutece107Cache<K, V> createCache(String strCacheName,  Class<K> k, Class<V> v, boolean enable, boolean preventGlobalReset) throws IllegalArgumentException;;

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
	  <K, V> Lutece107Cache<K, V> getCache(String cacheName, Class<K> keyType,
	                              Class<V> valueType);
	  /**
	   * Looks up a managed {@link Cache} given its name.
	   * @param <K> the type of key
	   * @param <V> the type of value
	   * @param cacheName the name of the managed {@link Cache} to acquire
	   * @return the Cache or null if it does exist or can't be pre-configured
	   * @throws IllegalStateException    if the {@link CacheManager}
	   *                                  is {@link #isClosed()}
	   * @throws ClassCastException       if the specified key and/or value types are
	   *                                  incompatible with the configured cache.
	   * @throws NullPointerException     if either keyType or classType is null.
	   * @throws SecurityException        when the operation could not be performed
	   *                                  due to the current security settings
	   */
	  <K, V> Lutece107Cache<K, V> getCache(String cacheName);
}
