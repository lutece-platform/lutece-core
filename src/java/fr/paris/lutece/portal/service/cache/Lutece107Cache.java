package fr.paris.lutece.portal.service.cache;

import java.io.Serializable;

import javax.cache.Cache;

public interface Lutece107Cache<K, V> extends CacheableService<K,V>, Cache<K, V>, Serializable {

}
