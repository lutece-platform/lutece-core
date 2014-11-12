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
package fr.paris.lutece.util.pool.service;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Hashtable;

import javax.sql.DataSource;


/**
 * Lutece Connection Service
 */
public class LuteceConnectionService implements ConnectionService
{
    private String _strPoolName;
    private Logger _logger;
    private ConnectionPool _connPool;

    /**
     * Sets the pool name
     * @param strPoolName The pool name
     */
    public void setPoolName( String strPoolName )
    {
        _strPoolName = strPoolName;
    }

    /**
     * Returns the pool name
     * @return The pool name
     */
    public String getPoolName(  )
    {
        return _strPoolName;
    }

    /**
     * Sets the logger
     * @param logger The logger
     */
    public void setLogger( Logger logger )
    {
        _logger = logger;
    }

    /**
     * Gets the pool logger
     * @return The logger
     */
    public Logger getLogger(  )
    {
        return _logger;
    }

    /**
     * Initializes the connection pool
     * @param htParamsConnectionPool Pool parameters
     */
    public void init( Hashtable<String, String> htParamsConnectionPool )
    {
        String url = htParamsConnectionPool.get( getPoolName(  ) + ".url" );

        if ( url == null )
        {
            _logger.error( "No URL specified for the pool " + getPoolName(  ) );
        }

        String user = htParamsConnectionPool.get( getPoolName(  ) + ".user" );

        if ( user == null )
        {
            _logger.error( "No user specified for the pool " + getPoolName(  ) );
        }

        String password = htParamsConnectionPool.get( getPoolName(  ) + ".password" );

        if ( password == null )
        {
            _logger.error( "No password specified for the pool " + getPoolName(  ) );
        }

        //load of the driver
        String strDiverClassName = htParamsConnectionPool.get( getPoolName(  ) + ".driver" );

        try
        {
            Driver driver = (Driver) Class.forName( strDiverClassName ).newInstance(  );
            DriverManager.registerDriver( driver );
            _logger.info( "Registered JDBC driver " + strDiverClassName );
        }
        catch ( NullPointerException e )
        {
            _logger.error( "Can't register JDBC driver: " + strDiverClassName +
                " because the property driver is not defined", e );
        }
        catch ( Exception e )
        {
            _logger.error( "Can't register JDBC driver: " + strDiverClassName, e );
        }

        int maxConns = ( htParamsConnectionPool.get( getPoolName(  ) + ".maxconns" ) == null ) ? 0
                                                                                               : Integer.parseInt( htParamsConnectionPool.get( getPoolName(  ) +
                    ".maxconns" ) );

        int initConns = ( htParamsConnectionPool.get( getPoolName(  ) + ".initconns" ) == null ) ? 0
                                                                                                 : Integer.parseInt( htParamsConnectionPool.get( getPoolName(  ) +
                    ".initconns" ) );

        int timeOut = ( htParamsConnectionPool.get( getPoolName(  ) + ".logintimeout" ) == null ) ? 5
                                                                                                  : Integer.parseInt( htParamsConnectionPool.get( getPoolName(  ) +
                    ".logintimeout" ) );

        String checkValidConnectionSql = ( htParamsConnectionPool.get( getPoolName(  ) + ".checkvalidconnectionsql" ) == null )
            ? "" : htParamsConnectionPool.get( getPoolName(  ) + ".checkvalidconnectionsql" );

        _connPool = new ConnectionPool( getPoolName(  ), url, user, password, maxConns, initConns, timeOut, _logger,
                checkValidConnectionSql );
    }

    /**
     * Get a connection
     * @return A connection
     */
    public Connection getConnection(  )
    {
        try
        {
            Connection connection = _connPool.getConnection(  );

            return connection;
        }
        catch ( SQLException e )
        {
            _logger.error( e.getMessage(  ), e );

            return null;
        }
    }

    /**
     * Free the connection
     * @param conn The connection to release
     */
    public void freeConnection( Connection conn )
    {
        _connPool.freeConnection( conn );
    }

    /**
     * Release the pool
     */
    public void release(  )
    {
        _connPool.release(  );
    }

    /**
     * Returns the connection pool
     * @return the connection pool
     */
    public ConnectionPool getConnectionPool(  )
    {
        return _connPool;
    }

    /**
     * {@inheritDoc}
     */
    public int getCurrentConnections(  )
    {
        return _connPool.getConnectionCount(  );
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxConnections(  )
    {
        return _connPool.getMaxConnectionCount(  );
    }

    /**
     * {@inheritDoc }
     */
    public String getPoolProvider(  )
    {
        return "Lutece";
    }

    /**
     * {@inheritDoc }
     */
    public DataSource getDataSource(  )
    {
        return _connPool;
    }
}
