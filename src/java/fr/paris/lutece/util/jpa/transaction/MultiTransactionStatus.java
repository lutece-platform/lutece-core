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

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.util.HashMap;
import java.util.Map;


/**
 * This transaction status wraps several {@link TransactionStatus}
 */
public class MultiTransactionStatus implements TransactionStatus
{
    private Map<PlatformTransactionManager, TransactionStatus> _transactionStatuses;
    private PlatformTransactionManager _mainPTM;
    private boolean _bNewSynchonization;

    /**
     * Creates a TransactionStatus that handles several TransactionStatus
     * @param mainPTM will be default {@link PlatformTransactionManager} for status informations (is*, has* methods)
     */
    public MultiTransactionStatus( PlatformTransactionManager mainPTM )
    {
        _mainPTM = mainPTM;
        _transactionStatuses = new HashMap<PlatformTransactionManager, TransactionStatus>(  );
    }

    /**
     * Sets new synchronization to true
     */
    public void setNewSynchonization(  )
    {
        this._bNewSynchonization = true;
    }

    /**
     * true if new synchronization
     * @return true if new synchronization
     */
    public boolean isNewSynchonization(  )
    {
        return this._bNewSynchonization;
    }

    /**
     * Gets the transaction status for the ptm
     * @param ptm the {@link PlatformTransactionManager}
     * @return transaction status found
     */
    public TransactionStatus getTransactionStatus( PlatformTransactionManager ptm )
    {
        return _transactionStatuses.get( ptm );
    }

    /**
     *
     * {@inheritDoc}
     */
    public void flush(  )
    {
        for ( TransactionStatus ts : _transactionStatuses.values(  ) )
        {
            ts.flush(  );
        }
    }

    /**
     * Gets the main transaction status
     * @return the transaction status
     */
    private TransactionStatus getMainTransactionStatus(  )
    {
        return _transactionStatuses.get( _mainPTM );
    }

    /**
     *
     * {@inheritDoc}
     */
    public boolean hasSavepoint(  )
    {
        return getMainTransactionStatus(  ).hasSavepoint(  );
    }

    /**
     *
     * {@inheritDoc}
     */
    public boolean isCompleted(  )
    {
        return getMainTransactionStatus(  ).isCompleted(  );
    }

    /**
     *
     * {@inheritDoc}
     */
    public boolean isNewTransaction(  )
    {
        return getMainTransactionStatus(  ).isNewTransaction(  );
    }

    /**
     *
     * {@inheritDoc}
     */
    public boolean isRollbackOnly(  )
    {
        return getMainTransactionStatus(  ).isRollbackOnly(  );
    }

    /**
     *
     * {@inheritDoc}
     */
    public void setRollbackOnly(  )
    {
        for ( TransactionStatus ts : _transactionStatuses.values(  ) )
        {
            ts.setRollbackOnly(  );
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    public Object createSavepoint(  ) throws TransactionException
    {
        return null;
    }

    /**
     *
     * {@inheritDoc}
     */
    public void releaseSavepoint( Object savepoint ) throws TransactionException
    {
        for ( TransactionStatus ts : _transactionStatuses.values(  ) )
        {
            ts.releaseSavepoint( savepoint );
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    public void rollbackToSavepoint( Object savepoint )
        throws TransactionException
    {
        for ( TransactionStatus ts : _transactionStatuses.values(  ) )
        {
            ts.rollbackToSavepoint( savepoint );
        }
    }

    /**
     * "Getter method" for {@link #_transactionStatuses}
     * @return value of {@link #_transactionStatuses}
     */
    public Map<PlatformTransactionManager, TransactionStatus> getTransactionStatuses(  )
    {
        return _transactionStatuses;
    }

    /**
     * "Setter method" for {@link #_transactionStatuses}
     * @param statuses value of {@link #_transactionStatuses}
     */
    public void setTransactionStatuses( Map<PlatformTransactionManager, TransactionStatus> statuses )
    {
        _transactionStatuses = statuses;
    }
}
