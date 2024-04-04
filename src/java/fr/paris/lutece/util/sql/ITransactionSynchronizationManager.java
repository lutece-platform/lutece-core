package fr.paris.lutece.util.sql;

import java.sql.SQLException;

public interface ITransactionSynchronizationManager
{

    /**
     * Determines if a managed transaction is currently active
     * 
     * @return The synchronization availability
     */
    boolean isSynchronizationActive( );

    /**
     * Registers a synchronization to the current managed transaction
     * 
     * @param context
     *            The synchronization context
     */
    public TransactionSynchronizationContext registerSynchronization(TransactionSynchronizationContext context) throws SQLException;

}
