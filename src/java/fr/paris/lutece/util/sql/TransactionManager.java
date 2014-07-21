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
package fr.paris.lutece.util.sql;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.Connection;


/**
 * Class to manage transactions
 */
public class TransactionManager
{
    private static final String DEFAULT_MODULE_NAME = "core";

    private static ThreadLocal<Map<String, Transaction>> _tlTransactions = new ThreadLocal<Map<String, Transaction>>( );

    /**
     * Begin a transaction for the current thread using the pool of a specific
     * plugin with the default transaction isolation. The default transaction
     * isolation is {@link Connection#TRANSACTION_READ_COMMITTED }.<br />
     * Note that only one transaction may be active at a time by pool for each
     * thread.
     * @param plugin The plugin to use the pool of, or null to use the default
     *            pool.
     */
    public static void beginTransaction( Plugin plugin )
    {
        beginTransaction( plugin, Connection.TRANSACTION_READ_COMMITTED );
    }

    /**
     * Begin a transaction for the current thread using the pool of a specific
     * plugin
     * @param plugin The plugin to use the pool of, or null to use the default
     *            pool.
     * @param nTransactionIsolation The transaction isolation. View
     *            {@link Connection#setTransactionIsolation(int) } to view the
     *            different available transaction isolations.
     */
    public static void beginTransaction( Plugin plugin, int nTransactionIsolation )
    {
        Map<String, Transaction> mapTransactions = _tlTransactions.get( );
        Transaction transaction = null;
        if ( mapTransactions == null )
        {
            mapTransactions = new HashMap<String, Transaction>( );
            _tlTransactions.set( mapTransactions );
        }
        else
        {
            transaction = mapTransactions.get( getPluginPool( plugin ) );
        }
        if ( transaction == null )
        {
            transaction = new Transaction( plugin );
            try
            {
                transaction.getConnection( ).setTransactionIsolation( nTransactionIsolation );
            }
            catch ( SQLException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
            mapTransactions.put( getPluginPool( plugin ), transaction );
        }
    }

    /**
     * Get the current transaction for the pool of a given plugin.
     * @param plugin The plugin to use the pool of, or null to use the default
     *            pool.
     * @return The transaction, or null if no transaction is currently running.
     */
    public static Transaction getCurrentTransaction( Plugin plugin )
    {
        Map<String, Transaction> mapTransactions = _tlTransactions.get( );
        if ( mapTransactions != null )
        {
            return mapTransactions.get( getPluginPool( plugin ) );
        }
        return null;
    }

    /**
     * Commit the transaction associated to the pool of a given plugin.
     * @param plugin The plugin associated to the pool to commit the transaction
     *            of, or null to use the default pool.
     */
    public static void commitTransaction( Plugin plugin )
    {
        Map<String, Transaction> mapTransactions = _tlTransactions.get( );
        if ( mapTransactions != null )
        {
            String strPoolName = getPluginPool( plugin );
            Transaction transaction = mapTransactions.get( strPoolName );
            if ( transaction != null )
            {
                transaction.commit( );
                mapTransactions.remove( strPoolName );
            }
        }
    }

    /**
     * Roll back a transaction associated to the pool of a given plugin.
     * @param plugin The plugin associated to the pool to roll back the
     *            transaction of, or null to use the default pool.
     */
    public static void rollBack( Plugin plugin )
    {
        rollBack( plugin, null );
    }

    /**
     * Roll back a transaction associated to the pool of a given plugin with an
     * exception.
     * @param plugin The plugin associated to the pool to roll back the
     *            transaction of, or null to use the default pool.
     * @param e The exception to associates with the roll back.
     */
    public static void rollBack( Plugin plugin, Exception e )
    {
        Map<String, Transaction> mapTransactions = _tlTransactions.get( );
        if ( mapTransactions != null )
        {
            String strPoolName = getPluginPool( plugin );
            Transaction transaction = mapTransactions.get( strPoolName );
            if ( transaction != null )
            {
                transaction.rollback( e );
                mapTransactions.remove( strPoolName );
            }
        }
    }

    /**
     * Get the name of the pool of a given plugin
     * @param plugin The plugin to get the name of the pool, or null to get the
     *            name of the default pool.
     * @return The name of the pool
     */
    private static String getPluginPool( Plugin plugin )
    {
        return plugin != null ? plugin.getDbPoolName( ) : DEFAULT_MODULE_NAME;
    }
}
