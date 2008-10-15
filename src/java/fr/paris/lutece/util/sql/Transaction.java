/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Transaction
 */
public class Transaction
{

    public static final int OPENED = -1;
    public static final int COMMITED = 0;
    public static final int ROLLEDBACK = 1;
    private static final String DEFAULT_MODULE_NAME = "core";
    private static final String LOGGER_DEBUG_SQL = "lutece.debug.sql.";
    /** Connection Service providing connection from a defined pool */
    private PluginConnectionService _connectionService;
    /** JDBC Connection */
    private Connection _connection;
    /** Plugin name */
    private String _strPluginName;
    /** The debug logger */
    private Logger _logger;
    private PreparedStatement _statement;
    private int _nStatus = OPENED;
    private boolean _bAutoCommit;
    private String _strSQL;

    /**
     * Constructor
     */
    public Transaction()
    {
        beginTransaction( null );
    }

    /**
     * Constructor
     * @param plugin The plugin ownner of the transaction
     */
    public Transaction( Plugin plugin )
    {
        beginTransaction( plugin );
    }

    /**
     * Gets a prepared statement
     * @param strSQL The SQL statement
     * @throws SQLException
     */
    public void prepareStatement( String strSQL ) throws SQLException
    {
        // Close the previous statement if exists
        if( _statement != null )
        {
            _statement.close();
        }

        // Get a new statement 
        _strSQL = strSQL;
        _statement = _connection.prepareStatement( _strSQL );
    }
    
    /**
     * The current prepared statement
     * @return The current statement
     */
    public PreparedStatement getStatement()
    {
        return _statement;
    }
    
    /**
     * Execute the current statement
     * @throws SQLException
     */
    public void executeStatement( ) throws SQLException
    {
        _logger.debug( "Plugin : '" + _strPluginName + "' - EXECUTE STATEMENT : " + _strSQL );
        _statement.executeUpdate();
    }

    /**
     * Commit the transaction
     */
    public void commit()
    {
        try
        {
            _connection.commit();
            _logger.debug( "Plugin : '" + _strPluginName + "' - COMMIT TRANSACTION" );
            closeTransaction( COMMITED );
        }
        catch( SQLException e )
        {
            rollback( e );
        }
    }

    /**
     * Rollback the transaction
     */
    public void rollback()
    {
        rollback( null );
    }

    /**
     * Rollback the transaction
     * @param e The exception that cause the rollback
     */
    public void rollback( Exception e )
    {
        if( e != null )
        {
            _logger.error( "Transaction Error - Rollback in progress " + e.getMessage(), e.getCause() );
        }
        try
        {
            _connection.rollback();
            _logger.debug( "Plugin : '" + _strPluginName + "' - ROLLBACK TRANSACTION" );
            closeTransaction( ROLLEDBACK );
        }
        catch( SQLException ex )
        {
            _logger.error( "Transaction Error - Rollback error : " + ex.getMessage(), ex.getCause() );
        }
    }

    /**
     * Return the transaction status
     * @return The transaction status
     */
    public int getStatus()
    {
        return _nStatus;
    }

    /**
     * Begin a transaction
     * @param plugin The plugin owner of the transaction
     */
    private void beginTransaction( Plugin plugin )
    {
        if( plugin != null )
        {
            _strPluginName = plugin.getName();
            _connectionService = plugin.getConnectionService();
        }
        else
        {
            _strPluginName = DEFAULT_MODULE_NAME;
            _connectionService = AppConnectionService.getDefaultConnectionService();
        }

        if( _connectionService == null )
        {
            throw new AppException( "Database access error. Please check component installations and db.properties." );
        }

        _logger = Logger.getLogger( LOGGER_DEBUG_SQL + _strPluginName );
        _logger.debug( "Plugin : '" + _strPluginName + "' - BEGIN TRANSACTION" );

        try
        {
            _connection = _connectionService.getConnection();

            // Save the autocommit configuration of the connection
            _bAutoCommit = _connection.getAutoCommit();
            _connection.setAutoCommit( false );
        }
        catch( SQLException e )
        {
            rollback( e );
        }
    }

    /**
     * Close the transaction
     * @param nStatus The status of the transaction
     */
    private void closeTransaction( int nStatus )
    {
        _nStatus = nStatus;
        try
        {
            if( _statement != null )
            {
                _statement.close();
            }
            // Restore the autocommit configuration of the connection
            _connection.setAutoCommit( _bAutoCommit );
        }
        catch( SQLException ex )
        {
            _logger.error( "Transaction Error - Unable to close transaction " + ex.getMessage(), ex.getCause() );
        }
        _connectionService.freeConnection( _connection );

    }

    /**
     * Checks that the transaction has been commited (or rolled back) before being destroyed
     * and release all transaction resources (statement, connection, ...) if not.
     * @throws java.lang.Throwable
     */
    @Override
    protected void finalize() throws Throwable
    {
        if ( _nStatus == OPENED )
        {
            _logger.error( "The transaction has not been commited" );
            closeTransaction( OPENED );
        }

        super.finalize(  );
    }
    
    

}
