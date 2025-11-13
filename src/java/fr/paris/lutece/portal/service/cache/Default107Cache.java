package fr.paris.lutece.portal.service.cache;
import javax.cache.CacheException;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;


/**
 * Default Lutece implementation of the Cache using JCache (JSR-107).
 * This class implements the JCache (JSR-107) cache API and provides the service for enabling and disabling caches
 * from an administration interface.
 * Retrieves a cache instance using this {@link ILuteceCacheManager} 
 * or {@link LuteceCache} annotation.
 * <p>Example:</p>
 * <pre>
 * {@code @Inject @LuteceCache(cacheName = "cacheName", keyType = Integer.class, valueType = String.class)
 * Lutece107Cache<Integer, String> cacheInstance;}
 * </pre>
 * 
 * @param <K> the type of keys maintained by the cache
 * @param <V> the type of cached values
 */
public class Default107Cache<K, V> extends AbstractCacheableService<K, V> {

    private final String name;

    /**
     * Validates the cache configuration.
     *
     * @throws IllegalStateException if the 'name' property is not initialized
     * @throws CacheException if a cache with the given name already exists
     */
    private void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("The 'name' property must be initialized.");
        }
        if (CacheService.isCacheExist(name)) {
            throw new CacheException("A cache named [" + name + "] already exists.");
        }
    }

    /**
     * Constructor that initializes the cache with the given name.
     *
     * @param name the name of the cache
     */
    public Default107Cache(String name) {
        this.name = name;
        validate();
        this.initCache(name);
    }

    /**
     * Constructor that initializes the cache with the given name and types for keys and values.
     *
     * @param name the name of the cache
     * @param k the class type of the keys
     * @param v the class type of the values
     */
    public Default107Cache(String name, Class<K> k, Class<V> v) {
        this.name = name;
        this.configuration = new MutableConfiguration<K, V>().setTypes(k, v);
        validate();
        this.initCache(name);
    }
    /**
     * Constructor that initializes the cache with the given name and types for keys and values.
     *
     * @param name the name of the cache
     * @param k the class type of the keys
     * @param v the class type of the values
     * @param enable the enable the cache
     * @param preventGlobalReset prevent the cache to be reset by global cache resets
     */
    public Default107Cache(String name, Class<K> k, Class<V> v, boolean enable, boolean preventGlobalReset) {
    	this.name = name;
        this._bEnable= enable;
        this.configuration = new MutableConfiguration<K, V>().setTypes(k, v);
        this._bPreventGlobalReset = preventGlobalReset;
        validate();
        this.initCache(name);
    }

    /**
     * Constructor that initializes the cache with the given name and configuration.
     *
     * @param name the name of the cache
     * @param configuration the configuration of the cache
     * @param <C> the type of the configuration
     * @param enable the enable cache
     * @param preventGlobalReset prevent the cache to be reset by global cache resets
     */
    public <C extends Configuration<K, V>> Default107Cache(String name, C configuration, boolean enable, boolean preventGlobalReset) {
        this.name = name;
        this.configuration = configuration;
        this._bEnable= enable;
        this._bPreventGlobalReset = preventGlobalReset;
        validate();
        this.initCache(name);
    }

    /**
     * Returns the name of the cache.
     *
     * @return the name of the cache
     */
    @Override
    public String getName() {
        return this.name;
    }
}
