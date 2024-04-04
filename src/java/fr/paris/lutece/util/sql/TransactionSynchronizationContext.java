package fr.paris.lutece.util.sql;

import java.sql.Connection;

import javax.sql.DataSource;

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.plugin.Plugin;

public class TransactionSynchronizationContext
{

    private final Plugin _plugin;
    private final PluginConnectionService _connectionService;
    private Connection _connection;
    private boolean _useTransactionManager;

    public TransactionSynchronizationContext( Plugin _plugin, PluginConnectionService _connectionService )
    {
        this._plugin = _plugin;
        this._connectionService = _connectionService;
    }

    public Plugin getPlugin( )
    {
        return _plugin;
    }

    public PluginConnectionService getConnectionService( )
    {
        return _connectionService;
    }

    public DataSource getDataSource( )
    {
        return AppConnectionService.getPoolManager( ).getDataSource( _connectionService.getPoolName( ) );
    }

    public void complete( Connection connection, boolean useTransactionManager )
    {
        this._connection = connection;
        this._useTransactionManager = useTransactionManager;
    }

    public Connection getConnection( )
    {
        return _connection;
    }

    public boolean useTransactionManager( )
    {
        return _useTransactionManager;
    }

}
