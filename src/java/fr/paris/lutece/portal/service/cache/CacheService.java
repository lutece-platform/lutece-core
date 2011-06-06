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
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.management.ManagementService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.lang.management.ManagementFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.management.MBeanServer;


/**
 * Provides cache object for cacheable services
 */
public final class CacheService
{
    private static final String PROPERTY_PATH_CONF = "path.conf";
    private static final String PROPERTY_IS_ENABLED = ".enabled";
    private static final String FILE_CACHES_STATUS = "caches.dat";

    // Cache configuration properties
    private static final String PROPERTY_MAX_ELEMENTS = ".maxElementsInMemory";
    private static final String PROPERTY_ETERNAL = ".eternal";
    private static final String PROPERTY_TIME_TO_IDLE = ".timeToIdleSeconds";
    private static final String PROPERTY_TIME_TO_LIVE = ".timeToLiveSeconds";
    private static final String PROPERTY_OVERFLOW_TO_DISK = ".overflowToDisk";
    private static final String PROPERTY_DISK_PERSISTENT = ".diskPersistent";
    private static final String PROPERTY_DISK_EXPIRY = ".diskExpiryThreadIntervalSeconds";
    private static final String PROPERTY_MAX_ELEMENTS_DISK = ".maxElementsOnDisk";

    // JMX monitoring properties
    private static final String PROPERTY_JMX_MONITORING = "lutece.cache.jmx.monitoring.enabled";
    private static final String PROPERTY_MONITOR_CACHE_MANAGER = "lutece.cache.jmx.monitorCacheManager";
    private static final String PROPERTY_MONITOR_CACHES = "lutece.cache.jmx.monitorCaches";
    private static final String PROPERTY_MONITOR_CACHE_CONFIGURATIONS = "lutece.cache.jmx.monitorCacheConfiguration";
    private static final String PROPERTY_MONITOR_CACHE_STATISTICS = "lutece.cache.jmx.monitorCacheStatistics";
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    private static final String PREFIX_DEFAULT = "lutece.cache.default";
    private static CacheService _singleton;
    private static CacheManager _manager;
    private static Properties _propertiesCacheConfig;

    // Cacheable Services registry
    private static List<CacheableService> _listCacheableServicesRegistry = new ArrayList<CacheableService>(  );
    private int _nDefaultMaxElementsInMemory;
    private boolean _bDefaultEternal;
    private long _lDefaultTimeToIdle;
    private long _lDefaultTimeToLive;
    private boolean _bDefaultOverflowToDisk;
    private boolean _bDefaultDiskPersistent;
    private long _lDefaultDiskExpiry;
    private int _nDefaultMaxElementsOnDisk;

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
        _manager = CacheManager.create(  );
        loadDefaults(  );
        loadCachesConfig(  );

        boolean bJmxMonitoring = AppPropertiesService.getProperty( PROPERTY_JMX_MONITORING, FALSE ).equals( TRUE );

        if ( bJmxMonitoring )
        {
            initJmxMonitoring(  );
        }
    }

    /**
     * Init JMX monitoring configuration
     */
    private void initJmxMonitoring(  )
    {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer(  );

        boolean bRegisterCacheManager = AppPropertiesService.getProperty( PROPERTY_MONITOR_CACHE_MANAGER, FALSE )
                                                            .equals( TRUE );
        boolean bRegisterCaches = AppPropertiesService.getProperty( PROPERTY_MONITOR_CACHES, FALSE ).equals( TRUE );
        boolean bRegisterCacheConfigurations = AppPropertiesService.getProperty( PROPERTY_MONITOR_CACHE_CONFIGURATIONS,
                FALSE ).equals( TRUE );
        boolean bRegisterCacheStatistics = AppPropertiesService.getProperty( PROPERTY_MONITOR_CACHE_STATISTICS, FALSE )
                                                               .equals( TRUE );
        ManagementService.registerMBeans( _manager, mBeanServer, bRegisterCacheManager, bRegisterCaches,
            bRegisterCacheConfigurations, bRegisterCacheStatistics );
    }

    /**
     * Create a cache for a given Service
     * @param strCacheName The Cache/Service name
     * @return A cache object
     */
    public Cache createCache( String strCacheName )
    {
        Cache cache = new Cache( getCacheConfiguration( strCacheName ) );
        _manager.addCache( cache );

        return _manager.getCache( strCacheName );
    }

    /**
     * Reset all caches
     */
    public static void resetCaches(  )
    {
        // Reset cache
        for ( CacheableService cs : _listCacheableServicesRegistry )
        {
            cs.resetCache(  );
        }
    }

    /**
     * Shutdown the cache service and the cache manager. Should be called when the webapp is stopped.
     */
    public void shutdown(  )
    {
        CacheService.storeCachesStatus(  );
        _manager.shutdown(  );
    }

    /**
     * Registers a new CacheableService
     * @deprecated use registerCacheableService( CacheableService cs )
     * @param strName The name
     * @param cs The CacheableService
     */
    @Deprecated
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

        // read cache status from file "caches.dat"
        cs.enableCache( getStatus( cs ) );
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

    /**
     * Stores cache status
     */
    public static void storeCachesStatus(  )
    {
        for ( CacheableService cs : _listCacheableServicesRegistry )
        {
            _propertiesCacheConfig.setProperty( normalizeName( cs.getName(  ) ) + PROPERTY_IS_ENABLED,
                cs.isCacheEnable(  ) ? "1" : "0" );
        }

        try
        {
            String strCachesStatusFile = AppPathService.getPath( PROPERTY_PATH_CONF, FILE_CACHES_STATUS );
            File file = new File( strCachesStatusFile );
            FileOutputStream fos = new FileOutputStream( file );
            _propertiesCacheConfig.store( fos, "Caches status file" );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Error storing caches status file : " + e.getMessage(  ), e );
        }
    }

    /**
     * Returns cache config
     * @param cache The cache
     * @return Cache infos
     */
    static String getInfos( Cache cache )
    {
        StringBuilder sbInfos = new StringBuilder(  );
        sbInfos.append( PROPERTY_MAX_ELEMENTS ).append( "=" )
               .append( cache.getCacheConfiguration(  ).getMaxElementsInMemory(  ) ).append( "\n" );
        sbInfos.append( PROPERTY_ETERNAL ).append( "=" ).append( cache.getCacheConfiguration(  ).isEternal(  ) )
               .append( "\n" );
        sbInfos.append( PROPERTY_TIME_TO_IDLE ).append( "=" )
               .append( cache.getCacheConfiguration(  ).getTimeToIdleSeconds(  ) ).append( "\n" );
        sbInfos.append( PROPERTY_TIME_TO_LIVE ).append( "=" )
               .append( cache.getCacheConfiguration(  ).getTimeToLiveSeconds(  ) ).append( "\n" );
        sbInfos.append( PROPERTY_OVERFLOW_TO_DISK ).append( "=" )
               .append( cache.getCacheConfiguration(  ).isOverflowToDisk(  ) ).append( "\n" );
        sbInfos.append( PROPERTY_DISK_PERSISTENT ).append( "=" )
               .append( cache.getCacheConfiguration(  ).isDiskPersistent(  ) ).append( "\n" );
        sbInfos.append( PROPERTY_DISK_EXPIRY ).append( "=" )
               .append( cache.getCacheConfiguration(  ).getDiskExpiryThreadIntervalSeconds(  ) ).append( "\n" );
        sbInfos.append( PROPERTY_MAX_ELEMENTS_DISK ).append( "=" )
               .append( cache.getCacheConfiguration(  ).getMaxElementsOnDisk(  ) ).append( "\n" );

        return sbInfos.toString(  );
    }

    /**
     * Load defaults configuration parameters
     */
    private void loadDefaults(  )
    {
        _nDefaultMaxElementsInMemory = AppPropertiesService.getPropertyInt( PREFIX_DEFAULT + PROPERTY_MAX_ELEMENTS, 10000 );
        _bDefaultEternal = AppPropertiesService.getPropertyBoolean( PREFIX_DEFAULT + PROPERTY_ETERNAL, false );
        _lDefaultTimeToIdle = AppPropertiesService.getPropertyLong( PREFIX_DEFAULT + PROPERTY_TIME_TO_IDLE, 10000L );
        _lDefaultTimeToLive = AppPropertiesService.getPropertyLong( PREFIX_DEFAULT + PROPERTY_TIME_TO_LIVE, 10000L );
        _bDefaultOverflowToDisk = AppPropertiesService.getPropertyBoolean( PREFIX_DEFAULT + PROPERTY_OVERFLOW_TO_DISK,
                true );
        _bDefaultDiskPersistent = AppPropertiesService.getPropertyBoolean( PREFIX_DEFAULT + PROPERTY_DISK_PERSISTENT,
                true );
        _lDefaultDiskExpiry = AppPropertiesService.getPropertyLong( PREFIX_DEFAULT + PROPERTY_DISK_EXPIRY, 120L );
        _nDefaultMaxElementsOnDisk = AppPropertiesService.getPropertyInt( PREFIX_DEFAULT + PROPERTY_MAX_ELEMENTS_DISK,
                10000 );
    }

    /**
     * Load caches status
     */
    private void loadCachesConfig(  )
    {
        String strCachesStatusFile = AppPathService.getPath( PROPERTY_PATH_CONF, FILE_CACHES_STATUS );
        File file = new File( strCachesStatusFile );

        try
        {
            _propertiesCacheConfig = new Properties(  );

            FileInputStream fis = new FileInputStream( file );
            _propertiesCacheConfig.load( fis );
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( "No cache.dat file. Should be created at shutdown." );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Error loading caches status defined in file : " + file.getAbsolutePath(  ), e );
        }
    }

    /**
     * Returns the cache status
     * @param cs The cacheable service
     * @return The status
     */
    private static boolean getStatus( CacheableService cs )
    {
        String strEnabled = "1";

        if ( _propertiesCacheConfig != null )
        {
            String prop = normalizeName( cs.getName(  ) ) + PROPERTY_IS_ENABLED;
            strEnabled = _propertiesCacheConfig.getProperty( prop, "1" );
        }

        return strEnabled.equals( "1" );
    }

    /**
     * Normalize name (remove spaces)
     * @param strName The name to normalize
     * @return The normalized name
     */
    private static String normalizeName( String strName )
    {
        return strName.replace( " ", "" );
    }

    /**
     * Read cache config from the file caches.dat
     * @param strCacheName The cache name
     * @return The config
     */
    private CacheConfiguration getCacheConfiguration( String strCacheName )
    {
        CacheConfiguration config = new CacheConfiguration(  );
        config.setName( strCacheName );

        String strPrefix = normalizeName( strCacheName );
        config.setMaxElementsInMemory( getIntProperty( strPrefix, PROPERTY_MAX_ELEMENTS, _nDefaultMaxElementsInMemory ) );
        config.setEternal( getBooleanProperty( strPrefix, PROPERTY_ETERNAL, _bDefaultEternal ) );
        config.setTimeToIdleSeconds( getLongProperty( strPrefix, PROPERTY_TIME_TO_IDLE, _lDefaultTimeToIdle ) );
        config.setTimeToLiveSeconds( getLongProperty( strPrefix, PROPERTY_TIME_TO_LIVE, _lDefaultTimeToLive ) );
        config.setOverflowToDisk( getBooleanProperty( strPrefix, PROPERTY_OVERFLOW_TO_DISK, _bDefaultOverflowToDisk ) );
        config.setDiskPersistent( getBooleanProperty( strPrefix, PROPERTY_DISK_PERSISTENT, _bDefaultDiskPersistent ) );
        config.setDiskExpiryThreadIntervalSeconds( getLongProperty( strPrefix, PROPERTY_DISK_EXPIRY, _lDefaultDiskExpiry ) );
        config.setMaxElementsOnDisk( getIntProperty( strPrefix, PROPERTY_MAX_ELEMENTS_DISK, _nDefaultMaxElementsOnDisk ) );

        return config;
    }

    /**
     * Read an Integer property
     * @param strPrefix Property's prefix
     * @param strKey the key
     * @param nDefault the default value
     * @return The property's value
     */
    private int getIntProperty( String strPrefix, String strKey, int nDefault )
    {
        String strValue = _propertiesCacheConfig.getProperty( strPrefix + strKey );

        if ( strValue != null )
        {
            try
            {
                int nValue = Integer.parseInt( strValue );

                return nValue;
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( "Invalid numeric property : " + strPrefix + strKey + "=" + strValue , e);
            }
        }

        return nDefault;
    }

    /**
     * Read a Long property
     * @param strPrefix Property's prefix
     * @param strKey the key
     * @param lDefault the default value
     * @return The property's value
     */
    private long getLongProperty( String strPrefix, String strKey, long lDefault )
    {
        String strValue = _propertiesCacheConfig.getProperty( strPrefix + strKey );

        if ( strValue != null )
        {
            try
            {
                long lValue = Integer.parseInt( strValue );

                return lValue;
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( "Invalid numeric property : " + strPrefix + strKey + "=" + strValue , e);
            }
        }

        return lDefault;
    }

    /**
     * Read a Boolean property
     * @param strPrefix Property's prefix
     * @param strKey the key
     * @param bDefault the default value
     * @return The property's value
     */
    private boolean getBooleanProperty( String strPrefix, String strKey, boolean bDefault )
    {
        String strValue = _propertiesCacheConfig.getProperty( strPrefix + strKey );

        if ( strValue != null )
        {
            return ( strValue.equalsIgnoreCase( "true" ) );
        }

        return bDefault;
    }
}
