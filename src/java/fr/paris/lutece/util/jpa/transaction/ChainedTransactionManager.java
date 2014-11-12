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
package fr.paris.lutece.util.jpa.transaction;

import fr.paris.lutece.util.jpa.JPAConstants;

import org.apache.log4j.Logger;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;


/**
 *
 * Manages multi transaction
 * @see {@link http://www.javaworld.com/javaworld/jw-01-2009/jw-01-spring-transactions.html?page=6}
 *
 */
public class ChainedTransactionManager implements PlatformTransactionManager
{
    private static final Logger _log = Logger.getLogger( JPAConstants.JPA_LOGGER );
    private List<PlatformTransactionManager> _transactionManagers;

    /**
     * Builds a new ChainedTransactionManager
     */
    public ChainedTransactionManager(  )
    {
    }

    /**
     * Begin a transaction for all transaction managers {@inheritDoc}
     */
    public TransactionStatus getTransaction( TransactionDefinition definition )
        throws TransactionException
    {
        if ( _transactionManagers.isEmpty(  ) )
        {
            return null;
        }

        MultiTransactionStatus mts = new MultiTransactionStatus( _transactionManagers.get( 0 ) );

        if ( !TransactionSynchronizationManager.isSynchronizationActive(  ) )
        {
            TransactionSynchronizationManager.initSynchronization(  );
            mts.setNewSynchonization(  );

            if ( _log.isDebugEnabled(  ) )
            {
                _log.debug( "Begin transaction : " + mts.toString(  ) );
            }
        }

        for ( PlatformTransactionManager transactionManager : _transactionManagers )
        {
            mts.getTransactionStatuses(  ).put( transactionManager, transactionManager.getTransaction( definition ) );
        }

        return mts;
    }

    /**
     *
     * {@inheritDoc}
     */
    public void commit( TransactionStatus status ) throws TransactionException
    {
        for ( PlatformTransactionManager transactionManager : _transactionManagers )
        {
            TransactionStatus transactionStatus = null;

            try
            {
                transactionStatus = ( (MultiTransactionStatus) status ).getTransactionStatus( transactionManager );
                transactionManager.commit( transactionStatus );
            }
            catch ( Exception e )
            {
                _log.error( e.getMessage(  ), e );

                // transactionManager.rollback( transactionStatus );
            }
        }

        if ( _log.isDebugEnabled(  ) )
        {
            _log.debug( "Ending transaction : " + status.toString(  ) );
        }

        if ( ( (MultiTransactionStatus) status ).isNewSynchonization(  ) )
        {
            TransactionSynchronizationManager.clear(  );
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    public void rollback( TransactionStatus status ) throws TransactionException
    {
        for ( PlatformTransactionManager dataSourceManager : _transactionManagers )
        {
            try
            {
                dataSourceManager.rollback( ( ( (MultiTransactionStatus) status ).getTransactionStatus( 
                        dataSourceManager ) ) );
            }
            catch ( Exception ex )
            {
                _log.error( ex.getMessage(  ), ex );
            }
        }

        if ( ( (MultiTransactionStatus) status ).isNewSynchonization(  ) )
        {
            TransactionSynchronizationManager.clear(  );
        }
    }

    /**
     * "Getter method" pour la variable {@link #_transactionManagers}
     * @return La variable {@link #_transactionManagers}
     */
    public List<PlatformTransactionManager> getTransactionManagers(  )
    {
        return _transactionManagers;
    }

    /**
     * "Setter method" pour la variable {@link #_transactionManagers}
     * @param managers La nouvelle valeur de la variable {@link #_transactionManagers}
     */
    public void setTransactionManagers( List<PlatformTransactionManager> managers )
    {
        if ( ( managers == null ) || managers.isEmpty(  ) )
        {
            _transactionManagers = Collections.emptyList(  );
        }
        else
        {
            _transactionManagers = managers;

            if ( _log.isDebugEnabled(  ) )
            {
                _log.debug( "Transaction Managers : " + _transactionManagers.toString(  ) );
            }
        }
    }
}
