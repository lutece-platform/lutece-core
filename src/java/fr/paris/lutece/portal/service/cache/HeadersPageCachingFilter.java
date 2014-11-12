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

import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.portal.service.page.PageEventListener;
import fr.paris.lutece.portal.service.page.PageService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimpleCachingHeadersPageCachingFilter;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Headers Page Caching Filter
 * based on EHCACHE WEB
 */
public class HeadersPageCachingFilter extends SimpleCachingHeadersPageCachingFilter implements CacheableService,
    PageEventListener
{
    private static final String BLOCKING_TIMEOUT_MILLIS = "blockingTimeoutMillis";
    private static final String INIT_PARAM_CACHE_NAME = "cacheName";
    private Cache _cache;
    private Logger _logger = Logger.getLogger( "lutece.cache" );
    private boolean _bInit;
    private boolean _bEnable = true;
    private String _strCacheName;

    /**
     * {@inheritDoc }
     */
    @Override
    public void doInit( FilterConfig filterConfig ) throws CacheException
    {
        // Override to inhibate the startup call made too early
        // The original treatment is done at the first doFilter call
        // through the init method below
    }

    /**
     * Initialization of the filter
     */
    private void init(  )
    {
        // Execute the doInit
        synchronized ( HeadersPageCachingFilter.class )
        {
            if ( blockingCache == null )
            {
                _strCacheName = filterConfig.getInitParameter( INIT_PARAM_CACHE_NAME );
                CacheService.getInstance(  ).createCache( _strCacheName );
                _cache = CacheManager.getInstance(  ).getCache( _strCacheName );
                CacheService.registerCacheableService( this );
                _logger.debug( "Initializing cache : " + _strCacheName );

                setCacheNameIfAnyConfigured( filterConfig );

                final String localCacheName = getCacheName(  );
                Ehcache cache = getCacheManager(  ).getEhcache( localCacheName );

                if ( cache == null )
                {
                    throw new CacheException( "cache '" + localCacheName + "' not found in configuration" );
                }

                if ( !( cache instanceof BlockingCache ) )
                {
                    // decorate and substitute
                    BlockingCache newBlockingCache = new BlockingCache( cache );
                    getCacheManager(  ).replaceCacheWithDecoratedCache( cache, newBlockingCache );
                }

                blockingCache = (BlockingCache) getCacheManager(  ).getEhcache( localCacheName );

                Integer blockingTimeoutMillis = parseBlockingCacheTimeoutMillis( filterConfig );

                if ( ( blockingTimeoutMillis != null ) && ( blockingTimeoutMillis > 0 ) )
                {
                    blockingCache.setTimeoutMillis( blockingTimeoutMillis );
                }
            }

            PageService.addPageEventListener( this );
        }

        _bInit = true;
    }

    /**
     * Reads the filterConfig for the parameter "blockingTimeoutMillis", and if
     * found, set the blocking timeout. If there is a parsing exception, no
     * timeout is set.
     *
     * @param filterConfig The filter config
     * @return The timeout value
     */
    private Integer parseBlockingCacheTimeoutMillis( FilterConfig filterConfig )
    {
        String timeout = filterConfig.getInitParameter( BLOCKING_TIMEOUT_MILLIS );

        try
        {
            return Integer.parseInt( timeout );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    /**
     * {@inheritDoc } This method is overriden to provide the cache name
     */
    @Override
    protected String getCacheName(  )
    {
        return _strCacheName;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void doFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain )
        throws AlreadyGzippedException, AlreadyCommittedException, FilterNonReentrantException, LockTimeoutException,
            Exception
    {
        if ( !_bInit )
        {
            init(  );
        }

        if ( _bEnable )
        {
            super.doFilter( request, response, chain );
            _logger.debug( "URI served from cache : " + request.getRequestURI(  ) );
        }
        else
        {
            chain.doFilter( request, response );
        }
    }

    // Cacheable Service implementation
    /**
     * {@inheritDoc }
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
    public int getCacheSize(  )
    {
        return _cache.getSize(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void resetCache(  )
    {
        _cache.removeAll(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName(  )
    {
        return _strCacheName;
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

        CacheService.updateCacheStatus( this );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getKeys(  )
    {
        return _cache.getKeys(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getMaxElements(  )
    {
        return _cache.getCacheConfiguration(  ).getMaxElementsInMemory(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public long getTimeToLive(  )
    {
        return _cache.getCacheConfiguration(  ).getTimeToLiveSeconds(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public long getMemorySize(  )
    {
        return _cache.calculateInMemorySize(  );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getInfos(  )
    {
        return CacheService.getInfos( _cache );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void processPageEvent( PageEvent event )
    {
        String strPattern = "page_id=" + event.getPage(  ).getId(  );

        for ( String strKey : (List<String>) blockingCache.getKeys(  ) )
        {
            if ( strKey.contains( strPattern ) && ( event.getEventType(  ) != PageEvent.PAGE_CREATED ) )
            {
                blockingCache.remove( strKey );
            }
        }
    }
}
