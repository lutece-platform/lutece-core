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

/** Contibution AtosWorldline / Meteo France - mlux */
package fr.paris.lutece.util.pool.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Hashtable;

import javax.sql.DataSource;


/**
 * C3P0 connection service
 */
public class C3p0ConnectionService implements ConnectionService
{
    /**
     * C3P0 connection pool
     */
    private ComboPooledDataSource _dataSource;

    /**
     * Pool name
     */
    private String _strPoolName;

    /**
     * Log4j logger
     */
    private Logger _logger = Logger.getLogger( this.getClass(  ) );

    /**
     * {@inheritDoc }
     */
    public void init( Hashtable<String, String> htParamsConnectionPool )
    {
        try
        {
            _dataSource = new ComboPooledDataSource( _strPoolName );

            String strDriver = htParamsConnectionPool.get( getPoolName(  ) + ".driver" );
            _dataSource.setDriverClass( strDriver );

            String strUrl = htParamsConnectionPool.get( getPoolName(  ) + ".url" );
            _dataSource.setJdbcUrl( strUrl );

            String strUser = htParamsConnectionPool.get( getPoolName(  ) + ".user" );
            _dataSource.setUser( strUser );

            String strPassword = htParamsConnectionPool.get( getPoolName(  ) + ".password" );
            _dataSource.setPassword( strPassword );

            String strMaxConns = htParamsConnectionPool.get( getPoolName(  ) + ".maxconns" );
            int nMaxConns = ( strMaxConns == null ) ? 0 : Integer.parseInt( strMaxConns );
            _dataSource.setMaxPoolSize( nMaxConns );

            String strMinConns = htParamsConnectionPool.get( getPoolName(  ) + ".initconns" );
            int nInitConns = ( strMinConns == null ) ? 0 : Integer.parseInt( strMinConns );
            _dataSource.setInitialPoolSize( nInitConns );
            _dataSource.setMinPoolSize( nInitConns );
        }
        catch ( Exception e )
        {
            _logger.error( "Error while initializing the pool " + getPoolName(  ), e );
        }

        _logger.info( "Initialization of the C3P0 pool named '" + getPoolName(  ) + "', Min/Max pool size : " +
            _dataSource.getMinPoolSize(  ) + "/" + _dataSource.getMaxPoolSize(  ) );
    }

    /**
     * {@inheritDoc }
     */
    public Connection getConnection(  )
    {
        Connection conn = null;

        try
        {
            if ( _dataSource != null )
            {
                conn = _dataSource.getConnection(  );

                if ( conn != null )
                {
                    _logger.debug( "The connexion is get, Current/Max pool : " +
                        _dataSource.getNumConnectionsAllUsers(  ) + "/" + _dataSource.getMaxPoolSize(  ) );
                }
            }
        }
        catch ( Exception e )
        {
            _logger.error( "Erreur when getting the connexion with the pool : " + getPoolName(  ), e );
        }

        return conn;
    }

    /**
     * {@inheritDoc }
     */
    public void freeConnection( Connection conn )
    {
        try
        {
            conn.close(  );

            _logger.debug( "The connexion is released, Current/Max pool : " +
                _dataSource.getNumConnectionsAllUsers(  ) + "/" + _dataSource.getMaxPoolSize(  ) );
        }
        catch ( SQLException e )
        {
            _logger.error( "SQL error when releasing the connexion with the pool : " + getPoolName(  ), e );
        }
        catch ( Exception e )
        {
            _logger.error( "Error while releasing the connexion with the pool : " + getPoolName(  ), e );
        }
    }

    /**
     * {@inheritDoc }
     */
    public void setPoolName( String poolName )
    {
        this._strPoolName = poolName;
    }

    /**
     * {@inheritDoc }
     */
    public String getPoolName(  )
    {
        return _strPoolName;
    }

    /**
     * {@inheritDoc }
     */
    public void setLogger( Logger log )
    {
        this._logger = log;
    }

    /**
     * {@inheritDoc }
     */
    public Logger getLogger(  )
    {
        return _logger;
    }

    /**
     * {@inheritDoc }
     */
    public void release(  )
    {
    }

    /**
     * {@inheritDoc }
     */
    public int getCurrentConnections(  )
    {
        int nCurrentConnections = -1;

        try
        {
            nCurrentConnections = _dataSource.getNumConnections(  );
        }
        catch ( SQLException ex )
        {
            _logger.error( "GetCurrentConnections error : " + ex.getMessage(  ), ex );
        }

        return nCurrentConnections;
    }

    /**
     * {@inheritDoc }
     */
    public int getMaxConnections(  )
    {
        return _dataSource.getMaxPoolSize(  );
    }

    /**
     * {@inheritDoc }
     */
    public String getPoolProvider(  )
    {
        return "C3P0";
    }

    /**
     * {@inheritDoc }
     */
    public DataSource getDataSource(  )
    {
        return _dataSource;
    }
}
