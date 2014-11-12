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
package fr.paris.lutece.portal.service.database;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.pool.PoolManager;
import fr.paris.lutece.util.pool.service.ConnectionService;

import java.io.InputStream;

import java.sql.Connection;


/**
 * This class provides a Database Connection Service based on pooled connections.
 */
public final class AppConnectionService
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String NO_POOL_DEFINED = "none";
    public static final String DEFAULT_POOL_NAME = "portal";
    private static PoolManager _poolManager;
    private static PluginConnectionService _connectionService;

    /**
     * Creates a new AppConnectionService object.
     */
    private AppConnectionService(  )
    {
    }

    /**
     * Initializes the connection pool and sets the pool manager instance
     *
     * @param strConfigPath The relative Config path
     * @param strConfigFilename The config file name
     * @param strPoolName The pool name
     * @throws LuteceInitException If an error occured
     */
    public static void init( String strConfigPath, String strConfigFilename, String strPoolName )
        throws LuteceInitException
    {
        try
        {
            InputStream is = AppPathService.getResourceAsStream( strConfigPath, strConfigFilename );
            _poolManager = PoolManager.getInstance( is );
            _connectionService = new PluginConnectionService( strPoolName );
            is.close(  );
        }
        catch ( Exception e )
        {
            throw new LuteceInitException( "Error initializing pool : " + e.getMessage(  ), e );
        }
    }

    /**
     * Returns a connection to database from the pool name
     *
     * @return a connection to database from the pool
     */
    public static Connection getConnection(  )
    {
        if ( _poolManager == null )
        {
            throw new RuntimeException( "* Erreur * getConnection : Le pool de connexion n'est pas initialise !" );
        }

        Connection conn = _connectionService.getConnection(  );

        return conn;
    }

    /**
     * Releases a connection and replaces it in the pool
     *
     * @param conn The connection to realease
     */
    public static void freeConnection( Connection conn )
    {
        _connectionService.freeConnection( conn );
    }

    /**
     * Releases all the connections on all the pools
     */
    public static void releasePool(  )
    {
        _poolManager.release(  );
    }

    /**
     * Gets a reference on the current Pool Manager
     *
     * @return The current Pool Manager
     */
    public static PoolManager getPoolManager(  )
    {
        if ( _poolManager == null )
        {
            throw new AppException( "PoolManager was not initialized !" );
        }

        return _poolManager;
    }

    /**
     * Gets the list of all pools defined in the db.properties file
     *
     * @param list The Reference List
     */
    public static void getPoolList( ReferenceList list )
    {
        for ( ConnectionService cs : _poolManager.getPools(  ) )
        {
            list.addItem( cs.getPoolName(  ), cs.getPoolName(  ) );
        }
    }

    /**
     * Returns a default Plugin Connection Service
     * @return  The connection Service
     */
    public static PluginConnectionService getDefaultConnectionService(  )
    {
        return _connectionService;
    }
}
