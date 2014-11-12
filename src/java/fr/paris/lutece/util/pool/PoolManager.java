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
package fr.paris.lutece.util.pool;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.pool.service.ConnectionService;
import fr.paris.lutece.util.pool.service.LuteceConnectionService;

import org.apache.log4j.Logger;

import java.io.InputStream;

import java.sql.Connection;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;


/**
 * This class can manages a set of database connections pools. It's implemented as a singleton. It provides methods to
 * get or release a connection from a given pool.
 */
public final class PoolManager
{
    private static final String LOGGER_NAME = "lutece.pool";
    private static PoolManager _instance;
    private Logger _logger;
    private Map<String, ConnectionService> _pools = new HashMap<String, ConnectionService>(  );

    /**
     * Creates a new PoolManager object.
     *
     *
     * @param isDbProperties A properties file containing pools parameters.
     * @throws LuteceInitException If any error occured
     */
    private PoolManager( InputStream isDbProperties ) throws LuteceInitException
    {
        init( isDbProperties );
    }

    /**
     * This method returns the unique instance of the PoolManager.
     *
     * @return The unique instance of Poolmanager.
     * @param isDbProperties An InputStream on a db.properties File to initialiaze the pool if it's not already created.
     * @throws LuteceInitException  If any error occured
     */
    public static synchronized PoolManager getInstance( InputStream isDbProperties )
        throws LuteceInitException
    {
        if ( _instance == null )
        {
            _instance = new PoolManager( isDbProperties );
        }

        return _instance;
    }

    /**
     * Initializes pools with parameters defined in a db.properties File.
     *
     * @param is An InputStream on a db.properties File.
     * @throws LuteceInitException If any error occured
     */
    private void init( InputStream is ) throws LuteceInitException
    {
        _logger = Logger.getLogger( LOGGER_NAME );

        Properties dbProps = new Properties(  );

        try
        {
            dbProps.load( is );
        }
        catch ( Exception e )
        {
            throw new LuteceInitException( "Can't read the properties file. Make sure db.properties is in the CLASSPATH",
                e );
        }

        createPools( dbProps );
    }

    /**
     * Creates all pools defined in a properties file.
     *
     * @param props A properties file containing pools parameters.
     * @throws LuteceInitException  If any error occured
     */
    private void createPools( Properties props ) throws LuteceInitException
    {
        Enumeration propNames = props.propertyNames(  );
        String strPoolName = "";

        Hashtable<String, Hashtable<String, String>> htPools = new Hashtable<String, Hashtable<String, String>>(  );

        while ( propNames.hasMoreElements(  ) )
        {
            String name = (String) propNames.nextElement(  );

            try
            {
                strPoolName = name.substring( 0, name.lastIndexOf( '.' ) );

                //tests if the pool has yet somme of its porperties stored in the hatsable
                Hashtable<String, String> htParamsPool;

                //if the pool has not yet property
                if ( htPools.get( strPoolName ) == null )
                {
                    htParamsPool = new Hashtable<String, String>(  );
                }
                else
                {
                    htParamsPool = htPools.get( strPoolName );
                }

                htParamsPool.put( name, props.getProperty( name ) );
                htPools.put( strPoolName, htParamsPool );

                _logger.debug( "property " + name );
                _logger.debug( "pool name " + strPoolName );
            }
            catch ( Exception e )
            {
                throw new LuteceInitException( 
                    "Invalid initialization of the pools. Problem encoutered with the property :  " + name, e );
            }
        }

        Enumeration enumKeys = htPools.keys(  );

        while ( enumKeys.hasMoreElements(  ) )
        {
            String key = "";

            try
            {
                key = (String) enumKeys.nextElement(  );

                Hashtable<String, String> htParamsPool = htPools.get( key );
                ConnectionService cs = null;

                try
                {
                    String strConnectionService = htParamsPool.get( key + ".poolservice" );

                    cs = (ConnectionService) Class.forName( strConnectionService ).newInstance(  );
                }
                catch ( NullPointerException nullEx )
                {
                    cs = new LuteceConnectionService(  );
                }
                catch ( Exception e )
                {
                    throw new LuteceInitException( "Exception when getting the property poolservice", e );
                }

                if ( cs != null )
                {
                    cs.setPoolName( key );
                    cs.setLogger( _logger );
                    cs.init( htParamsPool );
                    _pools.put( key, cs );
                }
            }
            catch ( Exception e )
            {
                throw new LuteceInitException( "Exception when getting the pool '" + key +
                    "'. Please check your '/WEB-INF/conf/db.properties' file.", e );
            }
        }
    }

    /**
     * Returns an available connection from the pool.
     *
     * @param strPoolName The pool name
     * @return A connection
     */
    public Connection getConnection( String strPoolName )
    {
        Connection conn = null;
        ConnectionService pool = (ConnectionService) _pools.get( strPoolName );

        if ( pool != null )
        {
            conn = pool.getConnection(  );
        }

        return conn;
    }

    /**
     * Returns a connection to pool.
     *
     * @param strPoolName Pool's name
     * @param con A released connection
     */
    public void freeConnection( String strPoolName, Connection con )
    {
        ConnectionService cs = (ConnectionService) _pools.get( strPoolName );

        if ( cs != null )
        {
            cs.freeConnection( con );
        }
    }

    /**
     * Releases all connections from all the pool.
     */
    public synchronized void release(  )
    {
        for ( ConnectionService pool : _pools.values(  ) )
        {
            pool.release(  );
        }
    }

    /**
     * Returns all pools available
     * @return The list of available pools
     */
    public Collection<ConnectionService> getPools(  )
    {
        return _pools.values(  );
    }

    /**
     * Returns pool's infos (currently opened connections)
     * @return The pool's infos
     */
    public ReferenceList getPoolsInfos(  )
    {
        ReferenceList listPoolsInfos = new ReferenceList(  );
        Collection<ConnectionService> listPools = getPools(  );

        for ( ConnectionService cs : listPools )
        {
            String strCurrentConnections = ( cs.getCurrentConnections(  ) == cs.INFO_NOT_AVAILABLE ) ? "-"
                                                                                                     : ( "" +
                cs.getCurrentConnections(  ) );
            String strMaxConnections = ( cs.getMaxConnections(  ) == cs.INFO_NOT_AVAILABLE ) ? "-"
                                                                                             : ( "" +
                cs.getMaxConnections(  ) );
            listPoolsInfos.addItem( cs.getPoolName(  ),
                strCurrentConnections + " / " + strMaxConnections + " (" + cs.getPoolProvider(  ) + ")" );
        }

        return listPoolsInfos;
    }

    /**
     * Returns the datasource for a given pool name
     * @param strPoolName The Pool name
     * @return A data source object
     */
    public DataSource getDataSource( String strPoolName )
    {
        return _pools.get( strPoolName ).getDataSource(  );
    }
}
