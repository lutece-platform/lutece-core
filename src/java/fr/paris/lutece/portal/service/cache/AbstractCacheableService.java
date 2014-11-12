/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppLogService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Base implementation for a cacheable service
 */
public abstract class AbstractCacheableService implements CacheableService, CacheEventListener
{
    private Cache _cache;
    private boolean _bEnable;
    private Logger _logger = Logger.getLogger( "lutece.cache" );

    /**
     * Init the cache. Should be called by the service at its initialization.
     */
    public void initCache(  )
    {
        initCache( getName(  ) );
    }

    /**
     * Init the cache. Should be called by the service at its initialization.
     * @param strCacheName The cache name
     */
    public void initCache( String strCacheName )
    {
        createCache( strCacheName );
        _bEnable = true;
        CacheService.registerCacheableService( this );
    }

    /**
     * Create a cache
     * @param strCacheName The cache name
     */
    private void createCache( String strCacheName )
    {
        _cache = CacheService.getInstance(  ).createCache( strCacheName );
        _cache.getCacheEventNotificationService(  ).registerListener( this );
    }

    /**
     * Put an object into the cache
     * @param strKey The key of the object to put into the cache
     * @param object The object to put into the cache
     */
    public void putInCache( String strKey, Object object )
    {
        if ( ( _cache != null ) && isCacheEnable(  ) )
        {
            Element element = new Element( strKey, object );
            _cache.put( element );
        }
    }

    /**
     * Gets an object from the cache
     * @param strKey The key of the object to retrieve from the cache
     * @return The object from the cache
     */
    public Object getFromCache( String strKey )
    {
        Object object = null;

        if ( ( _cache != null ) && isCacheEnable(  ) )
        {
            Element element = _cache.get( strKey );

            if ( element != null )
            {
                object = element.getObjectValue(  );
            }
        }

        return object;
    }

    /**
     * Gets the current cache status.
     *
     * @return true if enable, otherwise false
     */
    @Override
    public boolean isCacheEnable(  )
    {
        return _bEnable;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void enableCache( boolean bEnable )
    {
        _bEnable = bEnable;

        if ( ( !_bEnable ) && ( _cache != null ) )
        {
            _cache.removeAll(  );
        }

        if ( ( _bEnable ) && ( _cache == null ) )
        {
            createCache( getName(  ) );
        }

        CacheService.updateCacheStatus( this );
    }

    /**
     * Reset the cache.
     */
    @Override
    public void resetCache(  )
    {
        try
        {
            if ( _cache != null )
            {
                _cache.removeAll(  );
            }
        }
        catch ( IllegalStateException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( CacheException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Gets the number of item currently in the cache.
     *
     * @return the number of item currently in the cache.
     */
    @Override
    public int getCacheSize(  )
    {
        return ( _cache != null ) ? _cache.getSize(  ) : 0;
    }

    /**
     * Return a cache object
     * @return cache object
     */
    public Cache getCache(  )
    {
        return _cache;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getKeys(  )
    {
        if ( _cache != null )
        {
            return _cache.getKeys(  );
        }

        return new ArrayList<String>(  );
    }

    /**
    *  {@inheritDoc }
    */
    @Override
    public int getMaxElements(  )
    {
        return _cache.getCacheConfiguration(  ).getMaxElementsInMemory(  );
    }

    /**
     *  {@inheritDoc }
     */
    @Override
    public long getTimeToLive(  )
    {
        return _cache.getCacheConfiguration(  ).getTimeToLiveSeconds(  );
    }

    /**
     *  {@inheritDoc }
     */
    @Override
    public long getMemorySize(  )
    {
        return _cache.calculateInMemorySize(  );
    }

    /**
     *  {@inheritDoc }
     */
    @Override
    public String getInfos(  )
    {
        return CacheService.getInfos( _cache );
    }

    // CacheEventListener implementation

    /**
     * @see java.lang.Object#clone()
     * @return the instance
     */
    @Override
    public Object clone(  )
    {
        throw new RuntimeException( "This class shouldn't be cloned" );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void notifyElementExpired( Ehcache cache, Element element )
    {
        // Remove the element from the cache
        _cache.remove( element.getKey(  ) );
        _logger.debug( "Object removed from the cache : " + cache.getName(  ) + " - key : " + element.getKey(  ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void notifyElementRemoved( Ehcache ehch, Element elmnt )
        throws CacheException
    {
        // Do nothing
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void notifyElementEvicted( Ehcache ehch, Element elmnt )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void notifyRemoveAll( Ehcache ehch )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void notifyElementPut( Ehcache ehch, Element elmnt )
        throws CacheException
    {
        // Do nothing
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void notifyElementUpdated( Ehcache ehch, Element elmnt )
        throws CacheException
    {
        // Do nothing
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void dispose(  )
    {
        // Do nothing
    }

    /**
     * Remove a key from the cache
     * @param strKey The key to remove
     */
    public void removeKey( String strKey )
    {
        getCache(  ).remove( strKey );
    }
}
