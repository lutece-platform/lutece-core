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

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.NoDatabaseException;

import org.apache.log4j.Logger;

import org.springframework.jdbc.datasource.DataSourceUtils;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;


/**
 * Prepared statement util class
 *
 * @since version 1.3
 */
public class DAOUtil
{
    public static final String MSG_EXCEPTION_SELECT_ERROR = "Error selecting row id : ";
    private static final String DEFAULT_MODULE_NAME = "lutece";
    private static final String LOGGER_DEBUG_SQL = "lutece.debug.sql.";

    /** Connection Service providing connection from a defined pool */
    private PluginConnectionService _connectionService;

    /** JDBC Connection */
    private Connection _connection;
    private List<Array> _arrays;

    /** Plugin name */
    private String _strPluginName;

    /** Prepared statement */
    private PreparedStatement _statement;

    /** result set */
    private ResultSet _resultSet;

    /** True if SQL request are logged */
    private boolean _bReleased;
    private String _strSQL;
    private boolean _bTransactionnal;

    /** The debug logger */
    private Logger _logger;
    private StringBuffer _sbLogs = new StringBuffer(  );

    /**
     * Creates a new DAOUtil object.
     *
     * @param sql Sql Query for prepared Statement
     */
    public DAOUtil( String sql )
    {
        this( sql, null );
    }

    /**
     * Creates a new DAOUtil object.
     *
     * @param strSQL sql query for prepared Statement
     * @param plugin The plugin using this database access
     */
    public DAOUtil( String strSQL, Plugin plugin )
    {
        _bReleased = false;
        _strSQL = strSQL;

        if ( plugin != null )
        {
            _strPluginName = plugin.getName(  );
            _connectionService = plugin.getConnectionService(  );
        }
        else
        {
            _strPluginName = DEFAULT_MODULE_NAME;
            _connectionService = AppConnectionService.getDefaultConnectionService(  );
        }

        if ( _connectionService == null )
        {
            throw new NoDatabaseException( 
                "Database access error. Please check component installations and db.properties." );
        }

        // Use the logger name "lutece.debug.sql.<plugin_name>" to filter logs by plugins
        _logger = Logger.getLogger( LOGGER_DEBUG_SQL + _strPluginName );

        if ( _logger.isDebugEnabled(  ) )
        {
            log( "Module : '" + _strPluginName + "' - SQL Statement : " + _strSQL );
        }

        try
        {
            MultiPluginTransaction transaction = TransactionManager.getCurrentTransaction( plugin );

            if ( transaction != null )
            {
                _bTransactionnal = true;
            }
            else
            {
                // We check if there is a managed transaction to get the transactionnal connection
                if ( TransactionSynchronizationManager.isSynchronizationActive(  ) )
                {
                    _bTransactionnal = true;

                    DataSource ds = AppConnectionService.getPoolManager(  )
                                                        .getDataSource( _connectionService.getPoolName(  ) );
                    _connection = DataSourceUtils.getConnection( ds );

                    if ( _logger.isDebugEnabled(  ) )
                    {
                        _logger.debug( "Transactionnal context is used for pool " + _connectionService.getPoolName(  ) );
                    }
                }
                else
                {
                    // no transaction found, use the connection service directly
                    _connection = _connectionService.getConnection(  );
                }
            }

            if ( transaction != null )
            {
                _statement = transaction.prepareStatement( _strSQL );
            }
            else if ( _connection != null )
            {
                _statement = _connection.prepareStatement( _strSQL );
            }
            else
            {
                throw new AppException( "Database access error for component '" + _strPluginName +
                    "'. Please check plugin installation and db.properties." );
            }
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Build a formatted error message for a given exception
     * @param e The exception
     * @return The error message
     */
    private String getErrorMessage( Exception e )
    {
        free(  );

        StringBuilder sbError = new StringBuilder( "DAOUtil error : " );
        sbError.append( e.getMessage(  ) );
        sbError.append( " - SQL statement : " );
        sbError.append( " - Plugin : " );
        sbError.append( _strPluginName );

        return sbError.toString(  );
    }

    /**
     * Executes the update request and throws an error if the result is not 1
     */
    public void executeUpdate(  )
    {
        try
        {
            _statement.executeUpdate(  );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Executes a query
     */
    public void executeQuery(  )
    {
        try
        {
            _resultSet = _statement.executeQuery(  );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Log a message
     * @param strMessage The message to log
     */
    private void log( String strMessage )
    {
        if ( _logger.isDebugEnabled(  ) )
        {
            _sbLogs.append( strMessage );
        }
    }

    /**
     * Log a parameter
     * @param oName The parameter name
     * @param oValue The parameter value
     */
    private void logParameter( Object oName, Object oValue )
    {
        Object[] args = { oName, oValue };
        log( MessageFormat.format( "\n               Index : ''{0}''       Value : ''{1}'' ", args ) );
    }

    /**
     * Writes logs
     */
    private void writeLogs(  )
    {
        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( _sbLogs.toString(  ) );
        }
    }

    /**
     * Free connection
     */
    public final void free(  )
    {
        writeLogs(  );

        try
        {
            // Close statement if necessary
            if ( _statement != null )
            {
                _statement.close(  );
            }
            if ( _arrays != null )
            {
                for ( Array array : _arrays )
                {
                    array.free( );
                }
            }
        }
        catch ( SQLException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
        finally
        {
            // Free the connection - the connection is freed some other way in transactionnal context.
            if ( ( _connectionService != null ) && !_bTransactionnal )
            {
                _connectionService.freeConnection( _connection );
                _connectionService = null;
            }

            _bReleased = true;
        }
    }

    /**
     * Moves the cursor to the first row in this ResultSet object.
     * @deprecated Use {@link #next()} instead
     * @return true if the cursor is on a valid row; false if there are no rows
     *         in the result set
     */
    @Deprecated
    public boolean first(  )
    {
        try
        {
            return _resultSet.first(  );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Indicates whether the cursor is on the last row of this ResultSet object.
     * @return true if the cursor is on the last row; false otherwise
     */
    public boolean isLast(  )
    {
        try
        {
            return _resultSet.isLast(  );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a date value
     *
     * @param nIndex parameter index
     * @param date date value
     */
    public void setDate( int nIndex, Date date )
    {
        try
        {
            _statement.setDate( nIndex, date );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a date value
     *
     * @see PreparedStatement#setDate(int, Date, Calendar)
     * @since 5.1.1
     * @param nIndex parameter index
     * @param date date value
     * @param calendar the calendar
     */
    public void setDate( int nIndex, Date date, Calendar calendar )
    {
        try
        {
            _statement.setDate( nIndex, date, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a time value
     *
     * @param nIndex parameter index
     * @param time time value
     */
    public void setTime( int nIndex, Time time )
    {
        try
        {
            _statement.setTime( nIndex, time );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a time value
     *
     * @see PreparedStatement#setTime(int, Time, Calendar)
     * @since 5.1.1
     * @param nIndex parameter index
     * @param time time value
     * @param calendar the calendat
     */
    public void setTime( int nIndex, Time time, Calendar calendar )
    {
        try
        {
            _statement.setTime( nIndex, time, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a binary value stream
     * @param nIndex parameter index
     * @param iStream the java input stream which contains the binary parameter
     *            value
     * @param nBlength the number of bytes in the stream
     */
    public void setBinaryStream( int nIndex, InputStream iStream, int nBlength )
    {
        try
        {
            _statement.setBinaryStream( nIndex, iStream, nBlength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a binary value stream
     * @see PreparedStatement#setBinaryStream(int, InputStream, long)
     * @since 5.1.1
     * @param nIndex parameter index
     * @param iStream the java input stream which contains the binary parameter
     *            value
     * @param nBlength the number of bytes in the stream
     */
    public void setBinaryStream( int nIndex, InputStream iStream, long nBlength )
    {
        try
        {
            _statement.setBinaryStream( nIndex, iStream, nBlength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a binary value stream
     * @see PreparedStatement#setBinaryStream(int, InputStream)
     * @since 5.1.1
     * @param nIndex parameter index
     * @param iStream the java input stream which contains the binary parameter
     *            value
     */
    public void setBinaryStream( int nIndex, InputStream iStream )
    {
        try
        {
            _statement.setBinaryStream( nIndex, iStream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a binary stream from a resultSet
     *
     * @param nIndex column index
     * @return InputStream instance
     */
    public InputStream getBinaryStream( int nIndex )
    {
        try
        {
            return _resultSet.getBinaryStream( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a blob from a resultset
     *
     * @param nIndex column index
     * @return Blob instance
     */
    public Blob getBlob( int nIndex )
    {
        try
        {
            return _resultSet.getBlob( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a blob from a resultset
     *
     * @param strColumnName column name
     *
     * @return Blob instance
     */
    public Blob getBlob( String strColumnName )
    {
        try
        {
            return _resultSet.getBlob( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a blob value from a stream
     * @see PreparedStatement#setBlob(int, InputStream)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the value
     */
    public void setBlob( int nIndex, InputStream stream )
    {
        try
        {
            _statement.setBlob( nIndex, stream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a blob value from a stream
     * @see PreparedStatement#setBlob(int, InputStream, long)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the value
     * @param nLength the number of bytes to read
     */
    public void setBlob( int nIndex, InputStream stream, long nLength )
    {
        try
        {
            _statement.setBlob( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a blob value
     * @see PreparedStatement#setBlob(int, InputStream)
     * @since 5.1.1
     * @param nIndex the index
     * @param blob the value
     */
    public void setBlob( int nIndex, Blob blob )
    {
        try
        {
            _statement.setBlob( nIndex, blob );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a byte array from a resultset
     *
     * @param nIndex column index
     *
     * @return byte[] instance
     */
    public byte[] getBytes( int nIndex )
    {
        try
        {
            return _resultSet.getBytes( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a byte array from a resultset
     *
     * @param strColumnName column name
     *
     * @return byte[] instance
     */
    public byte[] getBytes( String strColumnName )
    {
        try
        {
            return _resultSet.getBytes( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a byte from a resultset
     *
     * @see ResultSet#getByte(int)
     * @since 5.1.1
     * @param nIndex column index
     * @return byte instance
     */
    public byte getByte( int nIndex )
    {
        try
        {
            return _resultSet.getByte( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets a byte from a resultset
     *
     * @see ResultSet#getByte(int)
     * @since 5.1.1
     * @param strColumnName column name
     * @return byte instance
     */
    public byte getByte( String strColumnName )
    {
        try
        {
            return _resultSet.getByte( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a int value
     *
     * @param nIndex parameter index in the prepared statement
     * @param nValue int value
     */
    public void setInt( int nIndex, int nValue )
    {
        try
        {
            _statement.setInt( nIndex, nValue );

            if ( _logger.isDebugEnabled(  ) )
            {
                logParameter( nIndex, nValue );
            }
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a Boolean value
     *
     * @param nIndex parameter index in the prepared statement
     * @param bValue Boolean value
     */
    public void setBoolean( int nIndex, boolean bValue )
    {
        try
        {
            // _statement.setBoolean( nIndex, bValue.booleanValue(  ) );
            _statement.setInt( nIndex, ( bValue ) ? 1 : 0 );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a byte value
     * @see PreparedStatement#setByte(int, byte)
     * @since 5.1.1
     * @param nIndex parameter index in the prepared statement
     * @param bValue byte value
     */
    public void setByte( int nIndex, byte bValue )
    {
        try
        {
            _statement.setByte( nIndex, bValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a byte array value
     *
     * @param nIndex parameter index in the prepared statement
     * @param tbValue byte array value
     */
    public void setBytes( int nIndex, byte[] tbValue )
    {
        try
        {
            _statement.setBytes( nIndex, tbValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a string value
     *
     * @param nIndex parameter index in the prepared statement
     * @param strValue string value
     */
    public void setString( int nIndex, String strValue )
    {
        try
        {
            _statement.setString( nIndex, strValue );

            if ( _logger.isDebugEnabled(  ) )
            {
                logParameter( nIndex, strValue );
            }
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a timestamp value
     *
     * @param nIndex parameter index in the prepared statement
     * @param ts timestamp value
     */
    public void setTimestamp( int nIndex, Timestamp ts )
    {
        try
        {
            _statement.setTimestamp( nIndex, ts );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a timestamp value
     *
     * @see PreparedStatement#setTimestamp(int, Timestamp, Calendar)
     * @since 5.1.1
     * @param nIndex parameter index in the prepared statement
     * @param ts timestamp value
     * @param calendar the calendar
     */
    public void setTimestamp( int nIndex, Timestamp ts, Calendar calendar )
    {
        try
        {
            _statement.setTimestamp( nIndex, ts, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a double value
     * @param nIndex parameter index in the prepared statement
     * @param dValue The value
     */
    public void setDouble( int nIndex, double dValue )
    {
        try
        {
            _statement.setDouble( nIndex, dValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Sets null value for a "double" column
     * @param nIndex the index
     */
    public void setDoubleNull( int nIndex )
    {
        try
        {
            _statement.setNull( nIndex, Types.DOUBLE );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Date object in the Java programming language.
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Date getDate( int nIndex )
    {
        try
        {
            return _resultSet.getDate( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Date object in the Java programming language.
     *
     * @param strColumnName name of the column, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Date getDate( String strColumnName )
    {
        try
        {
            return _resultSet.getDate( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Date object in the Java programming language.
     *
     * @see ResultSet#getDate(int, Calendar)
     * @since 5.1.1
     * @param nIndex the first column is 1, the second is 2, ...
     * @param calendar the calendar
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Date getDate( int nIndex, Calendar calendar )
    {
        try
        {
            return _resultSet.getDate( nIndex, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Date object in the Java programming language.
     *
     * @see ResultSet#getDate(String, Calendar)
     * @since 5.1.1
     * @param strColumnName name of the column, ...
     * @param calendar the calendar
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Date getDate( String strColumnName, Calendar calendar )
    {
        try
        {
            return _resultSet.getDate( strColumnName, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Time object in the Java programming language.
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Time getTime( int nIndex )
    {
        try
        {
            return _resultSet.getTime( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Time object in the Java programming language.
     *
     * @param strColumnName name of the column, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Time getTime( String strColumnName )
    {
        try
        {
            return _resultSet.getTime( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Time object in the Java programming language.
     *
     * @see ResultSet#getTime(int, Calendar)
     * @since 5.1.1
     * @param nIndex the first column is 1, the second is 2, ...
     * @param calendar the calendar
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Time getTime( int nIndex, Calendar calendar )
    {
        try
        {
            return _resultSet.getTime( nIndex, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a java.sql.Time object in the Java programming language.
     *
     * @see ResultSet#getTime(String, Calendar)
     * @since 5.1.1
     * @param strColumnName name of the column, ...
     * @param calendar the calendar
     * @return the column value; if the value is SQL NULL, the value returned is
     *         null
     */
    public Time getTime( String strColumnName, Calendar calendar )
    {
        try
        {
            return _resultSet.getTime( strColumnName, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a int
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         0
     */
    public int getInt( int nIndex )
    {
        try
        {
            return _resultSet.getInt( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a int
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         0
     */
    public int getInt( String strColumnName )
    {
        try
        {
            return _resultSet.getInt( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Boolean
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         FALSE
     */
    public boolean getBoolean( int nIndex )
    {
        try
        {
            return ( _resultSet.getInt( nIndex ) == 0 ) ? false : true;
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Boolean
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         FALSE
     */
    public boolean getBoolean( String strColumnName )
    {
        try
        {
            return ( _resultSet.getInt( strColumnName ) == 0 ) ? false : true;
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a string
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public String getString( int nIndex )
    {
        try
        {
            return _resultSet.getString( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a string
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public String getString( String strColumnName )
    {
        try
        {
            return _resultSet.getString( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a timestamp
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Timestamp getTimestamp( int nIndex )
    {
        try
        {
            return _resultSet.getTimestamp( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Timestamp
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Timestamp getTimestamp( String strColumnName )
    {
        try
        {
            return _resultSet.getTimestamp( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a timestamp
     *
     * @see ResultSet#getTimestamp(int, Calendar)
     * @since 5.1.1
     * @param nIndex the first column is 1, the second is 2, ...
     * @param calendar the calendar
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Timestamp getTimestamp( int nIndex, Calendar calendar )
    {
        try
        {
            return _resultSet.getTimestamp( nIndex, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Timestamp
     *
     * @see ResultSet#getTimestamp(String, Calendar)
     * @since 5.1.1
     * @param strColumnName column name
     * @param calendar the calendar
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Timestamp getTimestamp( String strColumnName, Calendar calendar )
    {
        try
        {
            return _resultSet.getTimestamp( strColumnName, calendar );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a double
     * @param strColumnName column name
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public double getDouble( String strColumnName )
    {
        try
        {
            return _resultSet.getDouble( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a double
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         0
     */
    public double getDouble( int nIndex )
    {
        try
        {
            return _resultSet.getDouble( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Object
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Object getObject( int nIndex )
    {
        try
        {
            return _resultSet.getObject( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Object
     *
     * @see ResultSet#getObject(int, Class)
     * @since 5.1.1
     * @param nIndex the first column is 1, the second is 2, ...
     * @param type the type
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public <T> T getObject( int nIndex, Class<T> type )
    {
        try
        {
            return _resultSet.getObject( nIndex, type );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as a Object
     *
     * @see ResultSet#getObject(int, java.util.Map<String,Class<?>>)
     * @since 5.1.1
     * @param nIndex the first column is 1, the second is 2, ...
     * @param map the type map
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Object getObject( int nIndex, java.util.Map<String,Class<?>> map )
    {
        try
        {
            return _resultSet.getObject( nIndex, map );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get the ResultSet
     * @return the resultSet
     */
    public ResultSet getResultSet(  )
    {
        return _resultSet;
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as an Object
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Object getObject( String strColumnName )
    {
        try
        {
            return _resultSet.getObject( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as an Object
     *
     * @see ResultSet#getObject(String, Class)
     * @since 5.1.1
     * @param strColumnName column name
     * @param type the type
     *
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public <T> T getObject( String strColumnName, Class<T> type )
    {
        try
        {
            return _resultSet.getObject( strColumnName, type );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the value of the designated column in the current row of this
     * ResultSet
     * object as an Object
     *
     * @see ResultSet#getObject(String, java.util.Map)
     * @since 5.1.1
     * @param strColumnName column name
     * @param map type map
     * @return the column value; if the value is SQL NULL, the value returned is
     *         NULL
     */
    public Object getObject( String strColumnName, java.util.Map<String,Class<?>> map )
    {
        try
        {
            return _resultSet.getObject( strColumnName, map );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Moves the cursor down one row from its current position in the ResultSet.
     *
     * @return true if the new current row is valid; false if there are no more
     *         rows
     */
    public boolean next(  )
    {
        try
        {
            return _resultSet.next(  );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a int null value
     * @param nIndex parameter index
     */
    public void setIntNull( int nIndex )
    {
        try
        {
            _statement.setNull( nIndex, Types.INTEGER );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Sets null value for a "long" column
     * @param nIndex the index
     */
    public void setLongNull( int nIndex )
    {
        try
        {
            _statement.setNull( nIndex, Types.BIGINT );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Sets a long value
     * @param nIndex the index
     * @param lValue the value
     */
    public void setLong( int nIndex, long lValue )
    {
        try
        {
            _statement.setLong( nIndex, lValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the long value
     * @param nIndex the index
     * @return the long value
     */
    public long getLong( int nIndex )
    {
        try
        {
            return _resultSet.getLong( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Gets the long value
     * @param strColumnName the column name
     * @return the long value
     */
    public long getLong( String strColumnName )
    {
        try
        {
            return _resultSet.getLong( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Set an Array value
     *
     * @see Connection#createArrayOf(String, Object[])
     * @see PreparedStatement#setArray(int, Array)
     * @since 5.1.1
     * @param nIndex the index
     * @param strTypename the type SQL name
     * @param elements the array value
     */
    public void setArray(int nIndex, String strTypename, Object[] elements)
    {
        try
        {
            Array x = _connection.createArrayOf( strTypename, elements );
            registerArray( x );
            _statement.setArray( nIndex, x );
        } catch ( SQLException e)
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Register an SQL Array to be able to free it later
     * @param array the array to register
     */
    private void registerArray( Array array )
    {
        if ( _arrays == null )
        {
            _arrays = new ArrayList<>( );
        }
        _arrays.add( array );
    }

    /**
     * Get an Array value
     * @see ResultSet#getArray(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the array value
     */
    public Array getArray( int nIndex )
    {
        try
        {
            Array res = _resultSet.getArray( nIndex );
            registerArray( res );
            return res;
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get an Array value
     * @see ResultSet#getArray(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the array value
     */
    public Array getArray( String strColumnName )
    {
        try
        {
            Array res = _resultSet.getArray( strColumnName );
            registerArray( res );
            return res;
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an ascii value stream
     * @see PreparedStatement#setAsciiStream(int, InputStream)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     */
    public void setAsciiStream( int nIndex, InputStream stream )
    {
        try
        {
            _statement.setAsciiStream( nIndex, stream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an ascii value stream
     * @see PreparedStatement#setAsciiStream(int, InputStream, int)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     * @param nLength the number of bytes to read
     */
    public void setAsciiStream( int nIndex, InputStream stream, int nLength)
    {
        try
        {
            _statement.setAsciiStream( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an ascii value stream
     * @see PreparedStatement#setAsciiStream(int, InputStream, long)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     * @param lLength the number of bytes to read
     */
    public void setAsciiStream( int nIndex, InputStream stream, long lLength)
    {
        try
        {
            _statement.setAsciiStream( nIndex, stream, lLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get an ascii value stream
     * @see ResultSet#getAsciiStream(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the ascii stream
     */
    public InputStream getAsciiString( int nIndex )
    {
        try
        {
            return _resultSet.getAsciiStream( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get an ascii value stream
     * @see ResultSet#getAsciiStream(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the ascii stream
     */
    public InputStream getAsciiString( String strColumnName )
    {
        try
        {
            return _resultSet.getAsciiStream( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared Statement with a BigDecimal value
     * @see PreparedStatement#setBigDecimal(int, BigDecimal)
     * @since 5.1.1
     * @param nIndex the index
     * @param value the value
     */
    public void setBigDecimal( int nIndex, BigDecimal value )
    {
        try
        {
            _statement.setBigDecimal( nIndex, value );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a BigDecimal value
     * @see ResultSet#getBigDecimal(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the value
     */
    public BigDecimal getBigDecimal( int nIndex )
    {
        try
        {
            return _resultSet.getBigDecimal( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a BigDecimal value
     * @see ResultSet#getBigDecimal(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the value
     */
    public BigDecimal getBigDecimal( String strColumnName )
    {
        try
        {
            return _resultSet.getBigDecimal( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a character stream
     *
     * @see PreparedStatement#setCharacterStream(int, Reader)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     */
    public void setCharacterStream( int nIndex, Reader stream )
    {
        try
        {
            _statement.setCharacterStream( nIndex, stream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a character stream
     *
     * @see PreparedStatement#setCharacterStream(int, Reader, int)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     * @param nLength the number of bytes to read
     */
    public void setCharacterStream( int nIndex, Reader stream, int nLength )
    {
        try
        {
            _statement.setCharacterStream( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a character stream
     *
     * @see PreparedStatement#setCharacterStream(int, Reader, long)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     * @param nLength the number of bytes to read
     */
    public void setCharacterStream( int nIndex, Reader stream, long nLength )
    {
        try
        {
            _statement.setCharacterStream( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a character stream value
     *
     * @see ResultSet#getCharacterStream(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the stream value
     */
    public Reader getCharacterStream( int nIndex )
    {
        try
        {
            return _resultSet.getCharacterStream( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a character stream value
     *
     * @see ResultSet#getCharacterStream(String)
     * @since 5.1.1
     * @param strColumnName the column index
     * @return the stream value
     */
    public Reader getCharacterStream( String strColumnName )
    {
        try
        {
            return _resultSet.getCharacterStream( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a Clob stream
     *
     * @see PreparedStatement#setClob(int, Reader)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the value
     */
    public void setClob( int nIndex, Reader stream )
    {
        try
        {
            _statement.setClob( nIndex, stream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a Clob stream
     *
     * @see PreparedStatement#setClob(int, Reader, long)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the value
     * @param nLength the number of bytes to read
     */
    public void setClob( int nIndex, Reader stream, long nLength )
    {
        try
        {
            _statement.setClob( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a Clob
     *
     * @see PreparedStatement#setClob(int, Clob)
     * @since 5.1.1
     * @param nIndex the index
     * @param clob the value
     */
    public void setClob( int nIndex, Clob clob )
    {
        try
        {
            _statement.setClob( nIndex, clob );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a Clob value
     *
     * @see ResultSet#getClob(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public Clob getClob( int nIndex )
    {
        try
        {
            return _resultSet.getClob( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a Clob value
     *
     * @see ResultSet#getClob(String)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public Clob getClob( String strColumnName )
    {
        try
        {
            return _resultSet.getClob( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a NClob stream
     *
     * @see PreparedStatement#setNClob(int, Reader)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the value
     */
    public void setNClob( int nIndex, Reader stream )
    {
        try
        {
            _statement.setNClob( nIndex, stream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a NClob stream
     *
     * @see PreparedStatement#setNClob(int, Reader, long)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the value
     * @param nLength the number of bytes to read
     */
    public void setNClob( int nIndex, Reader stream, long nLength )
    {
        try
        {
            _statement.setNClob( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a NClob
     *
     * @see PreparedStatement#setNClob(int, NClob)
     * @since 5.1.1
     * @param nIndex the index
     * @param clob the value
     */
    public void setClob( int nIndex, NClob clob )
    {
        try
        {
            _statement.setNClob( nIndex, clob );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a NClob value
     *
     * @see ResultSet#getNClob(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public NClob getNClob( int nIndex )
    {
        try
        {
            return _resultSet.getNClob( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a Clob value
     *
     * @see ResultSet#getClob(String)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public NClob getNClob( String strColumnName )
    {
        try
        {
            return _resultSet.getNClob( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a float value
     *
     * @see PreparedStatement#setFloat(int, float)
     * @since 5.1.1
     * @param nIndex the index
     * @param fValue the value
     */
    public void setFloat( int nIndex, float fValue )
    {
        try
        {
            _statement.setFloat( nIndex, fValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a float value
     *
     * @see ResultSet#getFloat(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the value
     */
    public float getFloat( String strColumnName )
    {
        try
        {
            return _resultSet.getFloat( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a float value
     *
     * @see ResultSet#getFloat(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public float getFloat( int nIndex )
    {
        try
        {
            return _resultSet.getFloat( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a character stream value
     *
     * @see PreparedStatement#setNCharacterStream(int, Reader)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     */
    public void setNCharacterStream(int nIndex, Reader stream )
    {
        try
        {
            _statement.setNCharacterStream( nIndex, stream );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a character stream value
     *
     * @see PreparedStatement#setNCharacterStream(int, Reader, long)
     * @since 5.1.1
     * @param nIndex the index
     * @param stream the stream
     * @param nLength the number of characters to read
     */
    public void setNCharacterStream(int nIndex, Reader stream, long nLength )
    {
        try
        {
            _statement.setNCharacterStream( nIndex, stream, nLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a character stream value
     *
     * @see ResultSet#getNCharacterStream(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the stream
     */
    public Reader getNCharacterStream( int nIndex )
    {
        try
        {
            return _resultSet.getNCharacterStream( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a character stream value
     *
     * @see ResultSet#getNCharacterStream(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the stream
     */
    public Reader getNCharacterStream( String strColumnName )
    {
        try
        {
            return _resultSet.getNCharacterStream( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a String value
     *
     * @see PreparedStatement#setNString(int, String)
     * @since 5.1.1
     * @param nIndex the index
     * @param strValue the value
     */
    public void setNString(int nIndex, String strValue )
    {
        try
        {
            _statement.setNString( nIndex, strValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a String value
     *
     * @see ResultSet#getNString(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the String
     */
    public String getNString( int nIndex )
    {
        try
        {
            return _resultSet.getNString( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a Stringvalue
     *
     * @see ResultSet#getNString(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the stream
     */
    public String getNString( String strColumnName )
    {
        try
        {
            return _resultSet.getNString( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a null value
     *
     * @see PreparedStatement#setNull(int, int)
     * @since 5.1.1
     * @param nIndex the index
     * @param nType the SQL type code defined in java.sql.Types
     */
    public void setNull( int nIndex, int nType )
    {
        try
        {
            _statement.setNull( nIndex, nType );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a null value
     *
     * @see PreparedStatement#setNull(int, int, String)
     * @since 5.1.1
     * @param nIndex the index
     * @param nType the SQL type code defined in java.sql.Types
     * @param strTypeName the type name
     */
    public void setNull( int nIndex, int nType, String strTypeName)
    {
        try
        {
            _statement.setNull( nIndex, nType, strTypeName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an object value
     *
     * @see PreparedStatement#setObject(int, Object)
     * @since 5.1.1
     * @param nIndex the index
     * @param value the value
     */
    public void setObject( int nIndex, Object value )
    {
        try
        {
            _statement.setObject( nIndex, value );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an object value
     *
     * @see PreparedStatement#setObject(int, Object, int)
     * @since 5.1.1
     * @param nIndex the index
     * @param value the value
     * @param nType the target SQL type (as defined in java.sql.Types)
     */
    public void setObject( int nIndex, Object value, int nType )
    {
        try
        {
            _statement.setObject( nIndex, value, nType );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an object value
     *
     * @see PreparedStatement#setObject(int, Object, int, int)
     * @since 5.1.1
     * @param nIndex the index
     * @param value the value
     * @param nType the target SQL type (as defined in java.sql.Types)
     * @param nScaleOrLength the scale or length
     */
    public void setObject( int nIndex, Object value, int nType, int nScaleOrLength )
    {
        try
        {
            _statement.setObject( nIndex, value, nType, nScaleOrLength );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a Ref value
     *
     * @see ResultSet#getRef(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the ref
     */
    public Ref getRef( int nIndex )
    {
        try
        {
            return _resultSet.getRef( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a Ref value
     *
     * @see ResultSet#getRef(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the ref
     */
    public Ref getRef( String strColumnName )
    {
        try
        {
            return _resultSet.getRef( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with an Ref value
     *
     * @see PreparedStatement#setRef(int, Ref)
     * @since 5.1.1
     * @param ref the Ref
     */
    public void setRef( int nIndex, Ref ref )
    {
        try
        {
            _statement.setRef( nIndex, ref );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a RowId value
     *
     * @see PreparedStatement#setRowId(int, RowId)
     * @since 5.1.1
     * @param nIndex the index
     * @param rowId the value
     */
    public void setRowId( int nIndex, RowId rowId )
    {
        try
        {
            _statement.setRowId( nIndex, rowId );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a RowId value
     *
     * @see ResultSet#getRowId(int)
     * @since 5.1.1
     * @param nIndex the index
     * @return the rowId
     */
    public RowId getRowId( int nIndex )
    {
        try
        {
            return _resultSet.getRowId( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a RowId value
     *
     * @see ResultSet#getRowId(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the rowId
     */
    public RowId getRowId( String strColumnName )
    {
        try
        {
            return _resultSet.getRowId( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a short value
     *
     * @see PreparedStatement#setShort(int, short)
     * @since 5.1.1
     * @param nIndex the index
     * @param shortValue the value
     */
    public void setShort( int nIndex, short shortValue )
    {
        try
        {
            _statement.setShort( nIndex, shortValue );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a short value
     *
     * @see ResultSet#getShort(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the value
     */
    public short getShort( String strColumnName )
    {
        try
        {
            return _resultSet.getShort( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a short value
     *
     * @see ResultSet#getShort(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public short getShort( int nIndex )
    {
        try
        {
            return _resultSet.getShort( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a SQLXML value
     *
     * @see PreparedStatement#setSQLXML(int, SQLXML)
     * @since 5.1.1
     * @param nIndex the index
     * @param value the value
     */
    public void setSQLXML( int nIndex, SQLXML value )
    {
        try
        {
            _statement.setSQLXML( nIndex, value );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a SQLXML value
     *
     * @see ResultSet#getSQLXML(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the value
     */
    public SQLXML getSQLXML( String strColumnName )
    {
        try
        {
            return _resultSet.getSQLXML( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a SQLXML value
     *
     * @see ResultSet#getSQLXML(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public SQLXML getSQLXML( int nIndex )
    {
        try
        {
            return _resultSet.getSQLXML( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Fills the prepared statement with a URL value
     *
     * @see PreparedStatement#setURL(int, URL)
     * @since 5.1.1
     * @param nIndex the index
     * @param url the URL
     */
    public void setURL( int nIndex, URL url )
    {
        try
        {
            _statement.setURL( nIndex, url );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a URL value
     *
     * @see ResultSet#getURL(String)
     * @since 5.1.1
     * @param strColumnName the column name
     * @return the value
     */
    public URL getURL( String strColumnName )
    {
        try
        {
            return _resultSet.getURL( strColumnName );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * Get a URL value
     *
     * @see ResultSet#getURL(int)
     * @since 5.1.1
     * @param nIndex the column index
     * @return the value
     */
    public URL getURL( int nIndex )
    {
        try
        {
            return _resultSet.getURL( nIndex );
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize(  ) throws Throwable
    {
        if ( !_bReleased )
        {
            free(  );
            AppLogService.error( 
                "A call to DAOUtil.free() seems to be missing or an unexpected exception has occured during the use of a DAOUtil object - plugin : " +
                _strPluginName + " - SQL statement : " + _strSQL );
        }

        super.finalize(  );
    }
}
