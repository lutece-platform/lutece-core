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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

/**
 * The transaction synchronization managers memorized by {@link DAOUtil} must be forgotten before the first code that
 * runs SQL when a container starts (AppInitListener observes the application scope with priority 1), and after the last
 * code that runs SQL when it stops (a pre-destroy method releasing a lock), so that a container started again in the same
 * class loader never uses the managers of a stopped container.
 * <p>
 * The Lutece test runner shares one container between all the test classes of the JVM: the test checks the memorized
 * state and not which container {@code CDI.current( )} returns.
 * </p>
 */
public class DAOUtilTransactionSynchronizationManagersTest
{
    private WeldContainer _container;
    private WeldContainer _containerPrevious;

    @AfterEach
    public void tearDown( )
    {
        for ( WeldContainer container : new WeldContainer [ ] { _container, _containerPrevious } )
        {
            if ( container != null && container.isRunning( ) )
            {
                container.shutdown( );
            }
        }
        DAOUtil.resetTransactionSynchronizationManagers( );
    }

    @Test
    public void testResolvedOnce( )
    {
        _container = start( );
        List<ITransactionSynchronizationManager> list = DAOUtil.getTransactionSynchronizationManagers( );
        assertNotNull( list );
        assertSame( list, DAOUtil.getTransactionSynchronizationManagers( ) );
    }

    @Test
    public void testForgottenBeforeTheFirstSqlOfANewContainer( )
    {
        // Managers memorized by another container of the same class loader
        _containerPrevious = start( );
        DAOUtil.getTransactionSynchronizationManagers( );
        assertTrue( DAOUtil.isTransactionSynchronizationManagersResolved( ) );

        StartupSqlObserver._bResolvedAtStartup = true;
        _container = start( );
        assertFalse( StartupSqlObserver._bResolvedAtStartup, "The managers of the previous container are still memorized when the first observer runs SQL" );
    }

    @Test
    public void testForgottenAfterTheLastSqlOfAStoppedContainer( )
    {
        _container = start( );
        _container.shutdown( );
        assertTrue( ShutdownSqlObserver._bCalled );
        assertFalse( DAOUtil.isTransactionSynchronizationManagersResolved( ),
                "The managers memorized by the shutdown code of the stopped container are still memorized" );
    }

    private static WeldContainer start( )
    {
        return new Weld( "dao-util-test-" + System.nanoTime( ) ).disableDiscovery( )
                .addBeanClasses( TestTransactionSynchronizationManager.class, StartupSqlObserver.class, ShutdownSqlObserver.class )
                .addExtension( new DAOUtilExtension( ) ).initialize( );
    }

    /**
     * A managed transaction support
     */
    @ApplicationScoped
    public static class TestTransactionSynchronizationManager implements ITransactionSynchronizationManager
    {
        @Override
        public boolean isSynchronizationActive( )
        {
            return false;
        }

        @Override
        public TransactionSynchronizationContext registerSynchronization( TransactionSynchronizationContext context ) throws SQLException
        {
            return context;
        }
    }

    /**
     * Runs SQL as soon as the application scope starts, like AppInitListener
     */
    @ApplicationScoped
    public static class StartupSqlObserver
    {
        static volatile boolean _bResolvedAtStartup;

        public void onStart( @Observes @Priority( 1 ) @Initialized( ApplicationScoped.class ) Object event )
        {
            _bResolvedAtStartup = DAOUtil.isTransactionSynchronizationManagersResolved( );
        }
    }

    /**
     * Runs SQL while the application scope stops, like the release of a lock in a pre-destroy method
     */
    @ApplicationScoped
    public static class ShutdownSqlObserver
    {
        static volatile boolean _bCalled;

        public void onStop( @Observes @Priority( 5000 ) @BeforeDestroyed( ApplicationScoped.class ) Object event )
        {
            DAOUtil.getTransactionSynchronizationManagers( );
            _bCalled = true;
        }
    }
}
