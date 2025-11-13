package fr.paris.lutece.portal.service.cache;

import javax.cache.configuration.Configuration;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Configuration class for LuteceCache.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */

@ApplicationScoped
public class  LuteceCacheParameterHolder {

    private String cacheName;
    private Configuration<?,?> config;
    protected boolean _bEnable;
    protected boolean _bPreventGlobalReset;

    /**
     * Gets the name of the cache.
     *
     * @return the name of the cache
     */
    public String getCacheName() {
        return cacheName;
    }

    /**
     * Sets the name of the cache.
     *
     * @param cacheName the name of the cache
     */
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * Gets the configuration of the cache.
     *
     * @return the configuration of the cache
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * Sets the configuration of the cache.
     *
     * @param conf the configuration of the cache
     */
    public void setConfig(Configuration<?,?> conf) {
        this.config = conf;
    }
    /**
     * Gets the value of the `_bEnable` field.
     *
     * @return {@code true} if the feature is enabled, {@code false} otherwise.
     */
    public boolean isEnable() {
        return _bEnable;
    }

    /**
     * Sets the value of the `_bEnable` field.
     *
     * @param enable {@code true} to enable the feature, {@code false} to disable it.
     */
    public void setEnable(boolean enable) {
        this._bEnable = enable;
    }

    public boolean isPreventGlobalReset() {
        return _bPreventGlobalReset;
    }
}
