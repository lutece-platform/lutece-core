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

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;


/**
 * Class to manage transactions
 */
public final class TransactionManager
{
    private static final String DEFAULT_POOL_NAME = "portal";
    private static ThreadLocal<Map<String, MultiPluginTransaction>> _tlTransactions = new ThreadLocal<Map<String, MultiPluginTransaction>>(  );

    /**
     * Default constructor
     */
    private TransactionManager(  )
    {
        // Do nothing
    }

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
        Map<String, MultiPluginTransaction> mapTransactions = _tlTransactions.get(  );
        MultiPluginTransaction transaction = null;

        if ( mapTransactions == null )
        {
            mapTransactions = new HashMap<String, MultiPluginTransaction>(  );
            _tlTransactions.set( mapTransactions );
        }
        else
        {
            transaction = mapTransactions.get( getPluginPool( plugin ) );
        }

        if ( ( transaction == null ) || ( transaction.getNbTransactionsOpened(  ) <= 0 ) )
        {
            transaction = new MultiPluginTransaction( plugin );

            try
            {
                transaction.getConnection(  ).setTransactionIsolation( nTransactionIsolation );
            }
            catch ( SQLException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }

            mapTransactions.put( getPluginPool( plugin ), transaction );
        }
        else
        {
            // A transaction has already been opened for this pool,
            // so we save that information to prevent the next call to the commit method to close the transaction.
            transaction.incrementNbTransactionsOpened(  );
        }
    }

    /**
     * Get the current transaction for the pool of a given plugin.
     * @param plugin The plugin to use the pool of, or null to use the default
     *            pool.
     * @return The transaction, or null if no transaction is currently running.
     */
    public static MultiPluginTransaction getCurrentTransaction( Plugin plugin )
    {
        Map<String, MultiPluginTransaction> mapTransactions = _tlTransactions.get(  );

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
        Map<String, MultiPluginTransaction> mapTransactions = _tlTransactions.get(  );

        if ( mapTransactions != null )
        {
            String strPoolName = getPluginPool( plugin );
            MultiPluginTransaction transaction = mapTransactions.get( strPoolName );

            if ( transaction != null )
            {
                // If the number of transactions opened is 1 or less, then we commit the transaction
                if ( transaction.getNbTransactionsOpened(  ) <= 1 )
                {
                    transaction.commit(  );
                    mapTransactions.remove( strPoolName );
                }
                else
                {
                    // Otherwise, we decrement the number
                    transaction.decrementNbTransactionsOpened(  );
                }
            }
        }
    }

    /**
     * Roll back a transaction associated to the pool of a given plugin. Note
     * that any plugin can roll a transaction back.
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
        Map<String, MultiPluginTransaction> mapTransactions = _tlTransactions.get(  );

        if ( mapTransactions != null )
        {
            String strPoolName = getPluginPool( plugin );
            MultiPluginTransaction transaction = mapTransactions.get( strPoolName );

            // We roll back the transaction, no matter how much transactions has been opened.
            if ( transaction != null )
            {
                transaction.rollback( e );
                mapTransactions.remove( strPoolName );
            }
        }
    }

    /**
     * Roll back every transactions opened by the current thread. This method
     * attempt to prevent connection leak.
     */
    public static void rollBackEveryTransaction(  )
    {
        rollBackEveryTransaction( null );
    }

    /**
     * Roll back every transactions opened by the current thread. This method
     * attempt to prevent connection leak.
     * @param e The exception that occurs and that may have prevent transaction
     *            from being properly closed (committed or roll backed)
     */
    public static void rollBackEveryTransaction( Throwable e )
    {
        Map<String, MultiPluginTransaction> mapTransactions = _tlTransactions.get(  );

        if ( ( mapTransactions != null ) && ( mapTransactions.size(  ) > 0 ) )
        {
            for ( MultiPluginTransaction transaction : mapTransactions.values(  ) )
            {
                transaction.rollback( null );
            }

            mapTransactions.clear(  );
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
        return ( plugin != null ) ? plugin.getDbPoolName(  ) : DEFAULT_POOL_NAME;
    }
}
