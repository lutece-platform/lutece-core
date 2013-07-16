package fr.paris.lutece.portal.service.daemon;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;


/**
 * Represents a runnable waiting to be launched by the
 * {@link ThreadLauncherDaemon}
 */
public class RunnableQueueItem implements Runnable
{
    private Runnable _runnable;
    private String _strKey;
    private Plugin _plugin;

    /**
     * Creates a new {@link RunnableQueueItem}
     * @param runnable The runnable waiting to bet launched
     * @param strKey The key associated with the runnable. Runnables of a given
     *            plugin are ensured that they will not be executed at the same
     *            time if they have the same key.
     * @param plugin The plugin associated with the runnable queue item
     */
    public RunnableQueueItem( Runnable runnable, String strKey, Plugin plugin )
    {
        this._runnable = runnable;
        this._strKey = strKey;
        this._plugin = plugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run( )
    {
        try
        {
            _runnable.run( );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
        }
    }

    /**
     * Get the runnable that is waiting for its execution
     * @return The runnable
     */
    public Runnable getRunnable( )
    {
        return _runnable;
    }

    /**
     * Get the key that identifies the runnable.
     * @return the key of the runnable
     */
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * Get the plugin that created the runnable
     * @return The plugin that created the runnable
     */
    public Plugin getPlugin( )
    {
        return _plugin;
    }

    /**
     * Compute the key of the item from its plugin name and the declared key.
     * The difference between the computed key and the defined key is that the
     * computed key is unique, whereas the defined key is unique within a
     * plugin.
     * @return The computed key of this runnable item
     */
    protected String computeKey( )
    {
        if ( _strKey != null && _plugin != null )
        {
            return _plugin.getName( ) + _strKey;
        }
        return null;
    }
}
