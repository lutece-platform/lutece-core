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
package fr.paris.lutece.portal.service.database;

import fr.paris.lutece.portal.service.plugin.PluginEvent;
import fr.paris.lutece.portal.service.plugin.PluginEventListener;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.sql.TransactionManager;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;


/**
 * DataSource transaction manager. This transaction manager use Spring
 * transaction manager, and do not use {@link TransactionManager}. This
 * transaction manager allow to use transaction in a given plugin, but does not
 * influence other plugins. To create transactions throw multiple plugins, use
 * {@link LuteceTransactionManager}
 */
public class DAOUtilTransactionManager extends DataSourceTransactionManager implements PluginEventListener
{
    private static final long serialVersionUID = -654531540978261621L;
    private Logger _logger = Logger.getLogger( "lutece.debug.sql.tx" );
    private String _strPluginName;
    private boolean _bInit;

    /**
     * Registers the listener to {@link PluginService}.
     */
    public DAOUtilTransactionManager(  )
    {
        PluginService.registerPluginEventListener( this );
    }

    /**
     * Gets the plugin name
     * @return the plugin name
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the plugin name
     * @param strPluginName the plugin name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processPluginEvent( PluginEvent event )
    {
        if ( getPluginName(  ).equals( event.getPlugin(  ).getName(  ) ) )
        {
            if ( ( event.getEventType(  ) == PluginEvent.PLUGIN_INSTALLED ) ||
                    ( event.getEventType(  ) == PluginEvent.PLUGIN_POOL_CHANGED ) )
            {
                if ( StringUtils.isNotBlank( event.getPlugin(  ).getDbPoolName(  ) ) &&
                        !AppConnectionService.NO_POOL_DEFINED.equals( event.getPlugin(  ).getDbPoolName(  ) ) )
                {
                    try
                    {
                        _logger.debug( "DAOUtilTransactionManager changed datasource status..." );
                        setDataSource( AppConnectionService.getPoolManager(  )
                                                           .getDataSource( event.getPlugin(  ).getDbPoolName(  ) ) );
                        _bInit = true;
                    }
                    catch ( Exception ex )
                    {
                        _bInit = false;
                        _logger.error( "An error occured getting pool for DAOUtilTransactionManager for plugin " +
                            event.getPlugin(  ).getName(  ) +
                            ", please check plugin is activated and pool is correctly set : " + ex.getMessage(  ), ex );
                    }
                }
                else
                {
                    _logger.debug( "Pool for plugin " + event.getPlugin(  ).getName(  ) +
                        " is set to null, clearing transaction manager" );
                    setDataSource( null );
                    _bInit = false;
                }
            }
            else if ( event.getEventType(  ) == PluginEvent.PLUGIN_UNINSTALLED )
            {
                setDataSource( null );
                _bInit = false;
            }
        }
    }

    /**
     * Returns a "fake" datasource to avoid spring checks failure when pool are
     * not initialized.
     * Returns the current datasource otherwise.
     *
     * @return the data source
     */
    @Override
    public DataSource getDataSource(  )
    {
        if ( _bInit )
        {
            return super.getDataSource(  );
        }

        /**
         * Empty datasource
         */
        return new EmptyDataSource(  );
    }

    /**
     * Empty datasource
     */
    private static class EmptyDataSource implements DataSource
    {
        @Override
        public <T> T unwrap( Class<T> iface ) throws SQLException
        {
            return null;
        }

        @Override
        public boolean isWrapperFor( Class<?> iface ) throws SQLException
        {
            return false;
        }

        @Override
        public void setLoginTimeout( int seconds ) throws SQLException
        {
        }

        @Override
        public void setLogWriter( PrintWriter out ) throws SQLException
        {
        }

        @Override
        public int getLoginTimeout(  ) throws SQLException
        {
            return 0;
        }

        @Override
        public PrintWriter getLogWriter(  ) throws SQLException
        {
            return null;
        }

        @Override
        public Connection getConnection( String username, String password )
            throws SQLException
        {
            return null;
        }

        @Override
        public Connection getConnection(  ) throws SQLException
        {
            return null;
        }

        /**
         * Get the parent logger
         * @return the logger
         * @throws SQLFeatureNotSupportedException if this method is not
         *             supported
         */
        public java.util.logging.Logger getParentLogger(  )
            throws SQLFeatureNotSupportedException
        {
            throw new SQLFeatureNotSupportedException(  );
        }
    }
}
