/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
import fr.paris.lutece.portal.service.util.AppPathService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Provides cache object for cacheable services
 */
public final class CacheService
{
    private static CacheService _singleton;
    private static final String PATH_CONF = "/WEB-INF/conf/";
    private static final String FILE_EHCACHE_CONFIG = "ehcache.xml";
    private static CacheManager _manager;

    // Cacheable Services registry
    private static List<CacheableService> _listCacheableServicesRegistry = new ArrayList<CacheableService>();

    /** Creates a new instance of CacheService */
    private CacheService(  )
    {
    }

    /**
     * Gets the unique instance of the CacheService
     * @return The unique instance of the CacheService
     */
    public static synchronized CacheService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new CacheService(  );
            _singleton.init(  );
        }

        return _singleton;
    }

    /**
     * Itializes the service by creating a manager object with a given configuration file.
     */
    private void init(  )
    {
        try
        {
            FileInputStream fis = AppPathService.getResourceAsStream( PATH_CONF, FILE_EHCACHE_CONFIG );
            _manager = CacheManager.create( fis );
            fis.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Create a cache for a given Service
     * @param strCacheName The Cache/Service name
     * @return A cache object
     */
    public Cache createCache( String strCacheName )
    {
        _manager.addCache( strCacheName );

        return _manager.getCache( strCacheName );
    }

    /**
     * Shutdown the cache service and the cache manager. Should be called when the webapp is stopped.
     */
    public void shutdown(  )
    {
        _manager.shutdown(  );
    }

    /**
     * Registers a new CacheableService
     * @deprecated ated
     * @param strName The name
     * @param cs The CacheableService
     */
    public static void registerCacheableService( String strName, CacheableService cs )
    {
        registerCacheableService( cs );
    }

    /**
     * Registers a new CacheableService
     * @param cs The CacheableService
     */
    public static void registerCacheableService( CacheableService cs )
    {
        _listCacheableServicesRegistry.add( cs );
    }

    /**
     * Returns all registered Cacheable services
     *
     * @return A collection containing all registered Cacheable services
     */
    public static List<CacheableService> getCacheableServicesList(  )
    {
        return _listCacheableServicesRegistry;
    }


}
