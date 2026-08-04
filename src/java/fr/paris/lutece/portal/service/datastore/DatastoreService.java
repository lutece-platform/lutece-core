/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.service.datastore;

import fr.paris.lutece.portal.business.datastore.DataEntity;
import fr.paris.lutece.portal.business.datastore.DataEntityHome;
import fr.paris.lutece.portal.service.cache.ILuteceCacheManager;
import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.template.FreeMarkerTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.NoDatabaseException;
import fr.paris.lutece.util.ReferenceList;
import jakarta.enterprise.inject.spi.CDI;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;

/**
 * Datastore Service
 */

public final class DatastoreService
{
    private static final String CACHE_SERVICE_NAME = "Datastore Cache Service";
    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";
    private static final String DATASTORE_KEY = "dskey";
    private static final Pattern PATTERN_DATASTORE_KEY = Pattern.compile( "#" + DATASTORE_KEY + "\\{(.*?)\\}" );
    static final String VALUE_MISSING = "DS Value Missing";
    private static final DataEntity ENTITY_MISSING = new DataEntity( null, null );
    public static Lutece107Cache<String,DataEntity> _cache;
    private static boolean _bDatabase = true;

    /**
     * Tells whether the cache can be used
     *
     * @return true when the cache exists and is open
     */
    private static boolean isCacheUsable( )
    {
        return _cache != null && _cache.isCacheEnable( ) && !_cache.isClosed( );
    }

    /**
     * Get an entity from the cache
     *
     * @param strKey
     *            The entity's key
     * @return The cached entity, the missing marker for a key known not to exist, null when the key
     *         has not been looked up yet
     */
    private static DataEntity getCachedEntity( String strKey )
    {
        return isCacheUsable( ) ? _cache.get( strKey ) : null;
    }

    /**
     * Put an entity in the cache
     *
     * @param strKey
     *            The entity's key
     * @param entity
     *            The entity to cache, or the missing marker
     */
    private static void cacheEntity( String strKey, DataEntity entity )
    {
        if ( isCacheUsable( ) )
        {
            _cache.put( strKey, entity );
        }
    }

    /**
     * Drop a key from the cache, whether it held a value or the missing marker
     *
     * Called by DataEntityHome on every write so a caller that stores an entity through the home
     * rather than through this service does not leave a stale entry behind.
     *
     * @param strKey
     *            The entity's key
     */
    public static void evictCachedKey( String strKey )
    {
        if ( isCacheUsable( ) )
        {
            _cache.remove( strKey );
        }
    }

    /**
     * Tells whether a cached entity is the marker of a key known not to exist
     *
     * @param entity
     *            The cached entity
     * @return true for the missing marker, whose key is null while a stored row always has one
     */
    private static boolean isMissing( DataEntity entity )
    {
        return entity != null && entity.getKey( ) == null;
    }
    

    /**
     * Private constructor
     */
    private DatastoreService( )
    {
    }

    /**
     * initialize the service
     */
    public static void init( )
    {
        FreeMarkerTemplateService.getInstance( ).setSharedVariable( DATASTORE_KEY, new DatastoreTemplateMethod( ) );
    }

    /**
     * Get entity
     *
     * @param strKey
     *            The entity's key
     * @param strDefault
     *            The default value
     * @return The value
     */
    public static String getDataValue( String strKey, String strDefault )
    {
        try
        {
            if ( _bDatabase )
            {
                DataEntity entity = getCachedEntity( strKey );

                if ( entity == null )
                {
                    entity = DataEntityHome.findByPrimaryKey( strKey );

                    if ( entity == null )
                    {
                        entity = ENTITY_MISSING;
                    }

                    cacheEntity( strKey, entity );
                }

                return isMissing( entity ) ? strDefault : entity.getValue( );
            }
        }
        catch( NoDatabaseException e )
        {
            disableDatastore( e );
        }

        return strDefault;
    }

    /**
     * Get entity depending the current web app instance
     *
     * @param strKey
     *            The entity's key
     * @param strDefault
     *            The default value
     * @return The value
     */
    public static String getInstanceDataValue( String strKey, String strDefault )
    {
        String strInstanceKey = getInstanceKey( strKey );

        return getDataValue( strInstanceKey, strDefault );
    }

    /**
     * Set entity
     *
     * @param strKey
     *            The entity's key
     * @param strValue
     *            The value
     */
    public static void setDataValue( String strKey, String strValue )
    {
        try
        {
            if ( _bDatabase )
            {
                DataEntity p = new DataEntity( strKey, strValue );
                DataEntity entity = DataEntityHome.findByPrimaryKey( strKey );

                if ( entity != null )
                {
                    DataEntityHome.update( p );
                }
                else
                {
                    DataEntityHome.create( p );
                }
            }
        }
        catch( NoDatabaseException e )
        {
            disableDatastore( e );
        }
    }

    /**
     * Set entity depending the current web app instance
     *
     * @param strKey
     *            The entity's key
     * @param strValue
     *            The value
     */
    public static void setInstanceDataValue( String strKey, String strValue )
    {
        String strInstanceKey = getInstanceKey( strKey );
        setDataValue( strInstanceKey, strValue );
    }

    /**
     * Atomically sets a value only if the key does not already exist in the
     * datastore. Safe under concurrent calls from multiple application
     * instances (multi-instance deployment) — relies on the database-level
     * primary key uniqueness of {@code core_datastore.entity_key}.
     * <p>
     * Unlike {@link #setDataValue(String, String)} which performs an upsert
     * (and therefore overwrites the existing value), this method never
     * overwrites. Use it for one-shot initialization of cluster-shared values
     * (RSA keys, cluster tokens, ...).
     * </p>
     *
     * @param strKey
     *            The entity's key
     * @param strValue
     *            The value to insert if the key is absent
     * @return {@code true} if the value was stored, {@code false} if the key
     *         already existed (in which case the stored value is left
     *         untouched)
     * @since 8.0
     */
    public static boolean insertDataValueIfAbsent( String strKey, String strValue )
    {
        try
        {
            if ( _bDatabase )
            {
                return DataEntityHome.createIfAbsent( new DataEntity( strKey, strValue ) );
            }
        }
        catch ( NoDatabaseException e )
        {
            disableDatastore( e );
        }
        return false;
    }

    /**
     * Remove a give key
     *
     * @param strKey
     *            The key
     */
    public static void removeData( String strKey )
    {
        try
        {
            if ( _bDatabase )
            {
                DataEntityHome.remove( strKey );
            }
        }
        catch( NoDatabaseException e )
        {
            disableDatastore( e );
        }
    }

    /**
     * Remove a give key depending the current web app instance
     *
     * @param strKey
     *            The key
     */
    public static void removeInstanceData( String strKey )
    {
        String strInstanceKey = getInstanceKey( strKey );
        removeData( strInstanceKey );
    }

    /**
     * Remove all data where keys begin with a given prefix
     *
     * @param strPrefix
     *            The prefix
     */
    public static void removeDataByPrefix( String strPrefix )
    {
        try
        {
            if ( _bDatabase )
            {
                List<DataEntity> listEntities = DataEntityHome.findAll( );

                for ( DataEntity entity : listEntities )
                {
                    if ( entity.getKey( ).startsWith( strPrefix ) )
                    {
                        removeData( entity.getKey( ) );
                    }
                }
            }
        }
        catch( NoDatabaseException e )
        {
            disableDatastore( e );
        }
    }

    /**
     * Remove all data where keys begin with a given prefix depending the current web app instance
     *
     * @param strPrefix
     *            The prefix
     */
    public static void removeInstanceDataByPrefix( String strPrefix )
    {
        String strInstancePrefix = getInstanceKey( strPrefix );
        removeDataByPrefix( strInstancePrefix );
    }

    /**
     * Gets a list of key/value where keys are matching a given prefix
     *
     * @param strPrefix
     *            The prefix
     * @return The list
     */
    public static ReferenceList getDataByPrefix( String strPrefix )
    {
        ReferenceList list = new ReferenceList( );

        try
        {
            if ( _bDatabase )
            {
                DataEntityHome.findByPrefix( strPrefix )
                        .forEach( ( DataEntity entity ) -> list.addItem( entity.getKey( ), entity.getValue( ) ) );

            }
        }
        catch ( NoDatabaseException e )
        {
            disableDatastore( e );
        }

        return list;
    }

    /**
     * Gets a list of key/value where keys are matching a given prefix depending the current web app instance
     *
     * @param strPrefix
     *            The prefix
     * @return The list
     */
    public static ReferenceList getInstanceDataByPrefix( String strPrefix )
    {
        String strInstancePrefix = getInstanceKey( strPrefix );

        return getDataByPrefix( strInstancePrefix );
    }

    /**
     * This method replace keys by their value into a given content
     *
     * @param strSource
     *            The string that contains datastore keys
     * @return The string with keys replaced
     */
    public static String replaceKeys( String strSource )
    {
        String result = strSource;

        if ( strSource != null )
        {
            Matcher matcher = PATTERN_DATASTORE_KEY.matcher( strSource );

            if ( matcher.find( ) )
            {
                StringBuffer sb = new StringBuffer( );

                do
                {
                    String strKey = matcher.group( 1 );
                    String strValue = DatastoreService.getDataValue( strKey, VALUE_MISSING );

                    if ( VALUE_MISSING.equals( strValue ) )
                    {
                        AppLogService.getLogger().warn( "Datastore Key missing : {} - Please fix to avoid performance issues.", strKey );
                    }

                    matcher.appendReplacement( sb, strValue );
                }
                while ( matcher.find( ) );

                matcher.appendTail( sb );
                result = sb.toString( );
            }
        }

        return result;
    }

    /**
     * Check if a key is available in the datastore
     *
     * @param strKey
     *            The key
     * @return True if the key is found otherwise false
     */
    public static boolean existsKey( String strKey )
    {
        try
        {
            if ( _bDatabase )
            {
                DataEntity entity = getCachedEntity( strKey );

                if ( entity == null )
                {
                    entity = DataEntityHome.findByPrimaryKey( strKey );

                    if ( entity == null )
                    {
                        entity = ENTITY_MISSING;
                    }

                    cacheEntity( strKey, entity );
                }

                return !isMissing( entity );
            }
        }
        catch( NoDatabaseException e )
        {
            disableDatastore( e );
        }

        return false;
    }

    /**
     * Check if a key is available in the datastore depending the current web app instance
     *
     * @param strKey
     *            The key
     * @return True if the key is found otherwise false
     */
    public static boolean existsInstanceKey( String strKey )
    {
        String strInstanceKey = getInstanceKey( strKey );

        return existsKey( strInstanceKey );
    }
    /**
     * Start cache. NB : Cache can't be created at DataStore creation because CacheService uses DatastoreService (Circular reference)
     */
    public static void startCache( )
    {
    	ILuteceCacheManager luteceCacheManager= CDI.current( ).select(ILuteceCacheManager.class).get( );
       Configuration<String,DataEntity> con= new MutableConfiguration<String, DataEntity>().setTypes(String.class, DataEntity.class);
    	_cache = luteceCacheManager.createCache(CACHE_SERVICE_NAME,con, true );      		
        AppLogService.info( "Datastore's cache started." );
    }

    /**
     * Disable the Datastore if a NoDatabaseException is catched
     * 
     * @param e
     *            The NoDatabaseException
     */
    private static void disableDatastore( NoDatabaseException e )
    {
        _bDatabase = false;
        AppLogService.error( "##### CRITICAL ERROR ##### : Datastore has been disabled due to a NoDatabaseException catched", e );
    }

    /**
     * Return a datastore key for the current webapp instance
     * 
     * @param strKey
     *            The key
     * @return The key for the current instance
     */
    private static String getInstanceKey( String strKey )
    {
        if ( !AppPathService.isDefaultWebappInstance( ) )
        {
            StringBuilder sbInstanceKey = new StringBuilder( );
            sbInstanceKey.append( AppPathService.getWebappInstance( ) ).append( "." ).append( strKey );

            return sbInstanceKey.toString( );
        }

        return strKey;
    }
}
