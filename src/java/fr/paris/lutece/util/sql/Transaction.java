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

    private static final String DEFAULT_MODULE_NAME = "lutece";
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

    public Transaction()
    {
        beginTransaction( null );
    }
    
    public Transaction(Plugin plugin)
    {
        beginTransaction( plugin );
    }

    public void commit()
    {
        try
        {
            _connection.commit();
            _logger.debug("Plugin : '" + _strPluginName + "' - COMMIT TRANSACTION");
            closeTransaction();
        }
        catch (Exception e)
        {
            rollback(e);
        }
    }

    public void rollback()
    {
        rollback(null);
    }

    public PreparedStatement prepareStatement(String strSQL) throws SQLException
    {
        // Close the previous statement if exists
        if( _statement != null )
        {
            _statement.close();
        }

        // Get a new statement 
        try
        {
            _statement = _connection.prepareStatement(strSQL);
        }
        catch (SQLException e)
        {
            rollback(e);
        }
        return _statement;
    }
    
    private void beginTransaction( Plugin plugin )
    {
        if (plugin != null)
        {
            _strPluginName = plugin.getName();
            _connectionService = plugin.getConnectionService();
        }
        else
        {
            _strPluginName = DEFAULT_MODULE_NAME;
            _connectionService = AppConnectionService.getDefaultConnectionService();
        }

        if (_connectionService == null)
        {
            throw new AppException("Database access error. Please check component installations and db.properties.");
        }

        _logger = Logger.getLogger(LOGGER_DEBUG_SQL + _strPluginName);
        _logger.debug("Module : '" + _strPluginName + "' - BEGIN TRANSACTION");

        try
        {
            _connection = _connectionService.getConnection();
            _connection.setAutoCommit(false);
        }
        catch (Exception e)
        {
            rollback(e);
        }
    }

    private void closeTransaction()
    {
        try
        {
            if (_statement != null)
            {
                _statement.close();
            }
        }
        catch (SQLException ex)
        {
            _logger.error("Transaction Error - Unable to close statement " + ex.getMessage(), ex.getCause());
        }
        _connectionService.freeConnection(_connection);

    }

    private void rollback(Exception e)
    {
        if (e != null)
        {
            _logger.error("Transaction Error - Rollback in progress " + e.getMessage(), e.getCause());
        }
        try
        {
            _connection.rollback();
            _logger.debug("Plugin : '" + _strPluginName + "' - ROLLBACK TRANSACTION");
            closeTransaction();
        }
        catch (SQLException ex)
        {
            _logger.error("Transaction Error - Rollback error : " + ex.getMessage(), ex.getCause());
        }
    }
}
