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
import java.sql.SQLException;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;


/**
 * This class provides a ConnectionService based on Tomcat
 */
public class TomcatConnectionService implements ConnectionService
{
    private DataSource _ds;
    private String _strPoolName;
    private Logger _logger;

    /**
     * {@inheritDoc}
     */
    public Connection getConnection(  )
    {
        Connection conn = null;

        try
        {
            if ( _ds != null )
            {
                conn = _ds.getConnection(  );

                if ( conn != null )
                {
                    _logger.debug( "The connexion is get" );
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
     * {@inheritDoc}
     */
    public void freeConnection( Connection conn )
    {
        try
        {
            conn.close(  );
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
     * {@inheritDoc}
     */
    public void init( Hashtable<String, String> htParamsConnectionPool )
    {
        try
        {
            String strDs = htParamsConnectionPool.get( getPoolName(  ) + ".ds" );
            Context ctx = new InitialContext(  );

            _ds = (DataSource) ctx.lookup( "java:comp/env/" + strDs );
        }
        catch ( Exception e )
        {
            _logger.error( "Error while initializing the pool " + getPoolName(  ), e );
        }

        _logger.info( "Initialization of the pool " + getPoolName(  ) );
    }

    /**
     * {@inheritDoc}
     */
    public void setPoolName( String strPoolName )
    {
        _strPoolName = strPoolName;
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public Logger getLogger(  )
    {
        return _logger;
    }

    /**
     * {@inheritDoc}
     */
    public void release(  )
    {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     */
    public int getCurrentConnections(  )
    {
        return ConnectionService.INFO_NOT_AVAILABLE;
    }

    /**
     * {@inheritDoc}
     */
    public int getMaxConnections(  )
    {
        return ConnectionService.INFO_NOT_AVAILABLE;
    }

    /**
     * {@inheritDoc }
     */
    public String getPoolProvider(  )
    {
        return "Tomcat";
    }

    /**
     * {@inheritDoc }
     */
    public DataSource getDataSource(  )
    {
        return _ds;
    }
}
