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

import fr.paris.lutece.util.pool.PoolManager;

import java.sql.Connection;


/**
 * Class for Plugins connection services.
 */
public class PluginConnectionService
{
    private String _strPoolName;

    /**
     * Creates a new PluginConnectionService object.
     *
     * @param strPoolName Rhe name of the pool
     */
    public PluginConnectionService( String strPoolName )
    {
        setPool( strPoolName );
    }

    /**
     * Sets the pool to be used by this Connection Service
     *
     * @param strPoolName The name of the pool
     */
    public final void setPool( String strPoolName )
    {
        _strPoolName = strPoolName;
    }

    /**
     * Returns a connection to database from the pool name
     *
     * @return a connection to database from the pool
     */
    public Connection getConnection(  )
    {
        PoolManager poolManager = AppConnectionService.getPoolManager(  );
        Connection conn = poolManager.getConnection( _strPoolName );

        return conn;
    }

    /**
     * Releases a connection and replaces it in the pool
     *
     * @param conn The connection to realease
     */
    public void freeConnection( Connection conn )
    {
        PoolManager poolManager = AppConnectionService.getPoolManager(  );
        poolManager.freeConnection( _strPoolName, conn );
    }

    /**
     * Gets the pool name
     * @return the pool name
     */
    public String getPoolName(  )
    {
        return _strPoolName;
    }
}
