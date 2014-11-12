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

import org.apache.commons.lang.NotImplementedException;

import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Class to represents transactions that are shared between plugins. Such
 * transactions will be shared by each plugin using the same pool than the one
 * that created the transaction.
 */
public class MultiPluginTransaction extends Transaction
{
    private int _nNbTransactionsOpened;

    /**
     * Constructor
     */
    public MultiPluginTransaction(  )
    {
        super(  );
        _nNbTransactionsOpened = 1;
    }

    /**
     * Constructor
     * @param plugin The plugin owner of the transaction
     */
    public MultiPluginTransaction( Plugin plugin )
    {
        super( plugin );
        _nNbTransactionsOpened = 1;
    }

    /**
     * Get the number of transactions that plugins tried to open simultaneously
     * on the same pool. If the number if 1, then the transaction can be safely
     * committed. If it is more than 1, then this number should be decremented
     * and the transaction should not be committed.
     * @return The number of transactions that was required
     */
    public int getNbTransactionsOpened(  )
    {
        return _nNbTransactionsOpened;
    }

    /**
     * Increment the number of transactions by one. See
     * {@link #getNbTransactionsOpened()} for more details.
     */
    public void incrementNbTransactionsOpened(  )
    {
        _nNbTransactionsOpened++;
    }

    /**
     * Decrement the number of transactions by one. See
     * {@link #getNbTransactionsOpened()} for more details.
     */
    public void decrementNbTransactionsOpened(  )
    {
        _nNbTransactionsOpened--;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement prepareStatement( String strSQL )
        throws SQLException
    {
        // Get a new statement 
        if ( getConnection(  ) == null )
        {
            throw new SQLException( 
                "MultiPluginTransaction - Connection has been closed. The new prepared statement can not be created : " +
                strSQL );
        }

        return getConnection(  ).prepareStatement( strSQL );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeStatement(  ) throws SQLException
    {
        throw new NotImplementedException( 
            "The method executeStatement( ) of class MultiPluginTransaction must not be called. Use manually PreparedStatement.executeQuery() instead." );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit(  )
    {
        _nNbTransactionsOpened = 0;
        super.commit(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback( Exception e )
    {
        _nNbTransactionsOpened = 0;
        super.rollback( e );
    }
}
