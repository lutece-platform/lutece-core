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
package fr.paris.lutece.util.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * A Lutece transaction begun inside a transaction managed by the container, on a pool managed by the container, joins it
 */
public class TransactionManagerManagedTransactionTest
{
    @BeforeEach
    public void setUp( )
    {
        ActiveManagedTransaction._nRollbackOnly = 0;
        ActiveManagedTransaction._bActive = true;
        DAOUtil.setTransactionSynchronizationManagers( List.of( new ActiveManagedTransaction( ) ) );
    }

    @AfterEach
    public void tearDown( )
    {
        ActiveManagedTransaction._bActive = false;
        TransactionManager.clear( );
        DAOUtil.resetTransactionSynchronizationManagers( );
    }

    /**
     * Begin and commit join the managed transaction: no Lutece transaction, so no local commit nor auto-commit change on the
     * enlisted connection; nested begins are counted
     */
    @Test
    public void testCommitJoinsTheManagedTransaction( )
    {
        Plugin plugin = new ManagedPoolPlugin( );

        TransactionManager.beginTransaction( plugin );
        TransactionManager.beginTransaction( plugin );
        assertNull( TransactionManager.getCurrentTransaction( plugin ) );
        TransactionManager.commitTransaction( plugin );
        TransactionManager.commitTransaction( plugin );
        assertNull( TransactionManager.getCurrentTransaction( plugin ) );
        assertEquals( 0, ActiveManagedTransaction._nRollbackOnly );
    }

    /**
     * A roll back of the joined code marks the managed transaction rollback only, and ends the join
     */
    @Test
    public void testRollBackMarksTheManagedTransaction( )
    {
        Plugin plugin = new ManagedPoolPlugin( );

        TransactionManager.beginTransaction( plugin );
        TransactionManager.beginTransaction( plugin );
        TransactionManager.rollBack( plugin );
        assertEquals( 1, ActiveManagedTransaction._nRollbackOnly );
        TransactionManager.commitTransaction( plugin );
        assertEquals( 1, ActiveManagedTransaction._nRollbackOnly );
        assertNull( TransactionManager.getCurrentTransaction( plugin ) );
    }

    /**
     * A transaction managed by the container, active only during these tests (the class is also discovered by the container of the
     * other tests of the module)
     */
    public static class ActiveManagedTransaction implements ITransactionSynchronizationManager
    {
        static volatile boolean _bActive;
        static int _nRollbackOnly;

        @Override
        public boolean isSynchronizationActive( )
        {
            return _bActive;
        }

        @Override
        public TransactionSynchronizationContext registerSynchronization( TransactionSynchronizationContext context ) throws SQLException
        {
            return context;
        }

        @Override
        public void setRollbackOnly( )
        {
            _nRollbackOnly++;
        }
    }

    /**
     * A plugin whose pool is managed by the container
     */
    private static class ManagedPoolPlugin extends Plugin
    {
        private static final long serialVersionUID = 1L;

        @Override
        public void init( )
        {
            // nothing to initialize
        }

        @Override
        public String getDbPoolName( )
        {
            return "managed-test";
        }

        @Override
        public PluginConnectionService getConnectionService( )
        {
            return new PluginConnectionService( "managed-test" )
            {
                @Override
                public String getPoolProvider( )
                {
                    return TransactionSynchronizationManager.MANAGED_CONNECTION_POOL_PROVIDER;
                }
            };
        }
    }
}
