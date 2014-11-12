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

import java.util.Hashtable;

import javax.sql.DataSource;


/**
 * Database Connection Service Interface
 */
public interface ConnectionService
{
    int INFO_NOT_AVAILABLE = -1;

    /**
     * Get a connection
     * @return A database connection
     */
    Connection getConnection(  );

    /**
     * Release the connection
     * @param conn The connection to release
     */
    void freeConnection( Connection conn );

    /**
     * Free all connections
     */
    void release(  );

    /**
     * Initialize pool parameters
     * @param htParamsConnectionPool Parameters
     */
    void init( Hashtable<String, String> htParamsConnectionPool );

    /**
     * Define the pool name
     * @param strPoolName The pool name
     */
    void setPoolName( String strPoolName );

    /**
     * Gets the pool name
     * @return The pool name
     */
    String getPoolName(  );

    /**
     * Define the logger
     * @param logger The logger
     */
    void setLogger( Logger logger );

    /**Gets the logger
     *
     * @return  The logger
     */
    Logger getLogger(  );

    /**
     * Gets the number of opened connections
     * @return The current connections count
     */
    int getCurrentConnections(  );

    /**
     * Gets the max connections
     * @return The max connections count
     */
    int getMaxConnections(  );

    /**
     * Gets the pool manager provider
     * @return The pool manager
     */
    String getPoolProvider(  );

    /**
     * Get datasource
     * @return A data source object
     */
    DataSource getDataSource(  );
}
