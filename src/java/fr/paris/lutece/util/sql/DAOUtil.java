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
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.log4j.Logger;

import java.io.InputStream;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import java.text.MessageFormat;


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

    /** Plugin name */
    private String _strPluginName;

    /** Prepared statement */
    private PreparedStatement _statement;

    /** result set */
    private ResultSet _resultSet;

    /** True if SQL request are logged */
    private boolean _bReleased;
    private String _strSQL;

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
            throw new AppException( "Database access error. Please check component installations and db.properties." );
        }

        // Use the logger name "lutece.debug.sql.<plugin_name>" to filter logs by plugins
        _logger = Logger.getLogger( LOGGER_DEBUG_SQL + _strPluginName );
        log( "Module : '" + _strPluginName + "' - SQL Statement : " + _strSQL );

        try
        {
            _connection = _connectionService.getConnection(  );

            if ( _connection != null )
            {
                _statement = _connection.prepareStatement( strSQL );
            }
            else
            {
                throw new AppException( "Database access error for component '" + _strPluginName + "' on pool '" +
                    _connectionService.getConnection(  ) + "'. Please check plugin installation and db.properties." );
            }
        }
        catch ( SQLException e )
        {
            free(  );
            throw new AppException( getErrorMessage( e ), e );
        }
    }

    private String getErrorMessage( Exception e )
    {
        free(  );

        StringBuffer sbError = new StringBuffer( "DAOUtil error : " );
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

    private void log( String strMessage )
    {
        if ( _logger.isDebugEnabled(  ) )
        {
            _sbLogs.append( strMessage );
        }
    }

    private void logParameter( Object oName, Object oValue )
    {
        Object[] args = { oName, oValue };
        log( MessageFormat.format( "\n               Index : ''{0}''       Value : ''{1}'' ", args ) );
    }

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
    public void free(  )
    {
        writeLogs(  );

        try
        {
            // Close statement if necessary
            if ( _statement != null )
            {
                _statement.close(  );
            }
        }
        catch ( SQLException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
        finally
        {
            // Free the connection
            if ( _connectionService != null )
            {
                _connectionService.freeConnection( _connection );
                _connectionService = null;
            }

            _bReleased = true;
        }
    }

    /**
     * @deprecated
     * Moves the cursor to the first row in this ResultSet object.
     * @return true if the cursor is on a valid row; false if there are no rows in the result set
     */
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
     * Fills the prepared statement with a binary value stream
     * @param nIndex parameter index
     * @param iStream the java input stream which contains the binary parameter value
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
     * Gets a binary stream from a resultSet
     *
     * @param nIndex column index
     *
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
     *
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
            logParameter( nIndex, nValue );
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
            logParameter( nIndex, strValue );
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
     * Fills the prepared statement with a double value
     * @param nIndex parameter index in the prepared statement
     * @param nValue int value
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a java.sql.Date object in the Java programming language.
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is null
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a java.sql.Date object in the Java programming language.
     *
     * @param strColumnName name of the column, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is null
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a java.sql.Time object in the Java programming language.
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is null
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a java.sql.Time object in the Java programming language.
     *
     * @param strColumnName name of the column, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is null
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a int
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is 0
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a int
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is 0
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a Boolean
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is FALSE
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a Boolean
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is FALSE
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a string
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a string
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a timestamp
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a Timestamp
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a double
     * @param strColumnName column name
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a double
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is 0
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
     * Gets the value of the designated column in the current row of this ResultSet
     * object as a Object
     *
     * @param nIndex the first column is 1, the second is 2, ...
     *
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Get the ResultSet
     * @return the resultSet
     */
    public ResultSet getResultSet(  )
    {
        return _resultSet;
    }

    /**
     * Gets the value of the designated column in the current row of this ResultSet
     * object as an Object
     *
     * @param strColumnName column name
     *
     * @return the column value; if the value is SQL NULL, the value returned is NULL
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
     * Moves the cursor down one row from its current position in the ResultSet.
     *
     * @return true if the new current row is valid; false if there are no more rows
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
     * finalize implementation
     * @see java.lang.Object#finalize()
     * @throws Throwable
     */
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
