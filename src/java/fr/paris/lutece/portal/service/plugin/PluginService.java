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
package fr.paris.lutece.portal.service.plugin;

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.filesystem.FileListFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;


/**
 * This class provides services and utilities for plugins management
 */
public final class PluginService
{
    // Constantes
    private static final String PATH_CONF = "path.conf";
    private static final String CORE_XML = "core.xml";
    private static final String CORE = "core";
    private static Plugin _pluginCore;
    private static final String PATH_PLUGIN = "path.plugins";
    private static final String FILE_PLUGINS_STATUS = "plugins.dat";
    private static final String EXTENSION_FILE = "xml";
    private static final String PROPERTY_IS_INSTALLED = ".installed";
    private static final String PROPERTY_DB_POOL_NAME = ".pool";
    private static final String KEY_PLUGINS_STATUS = "core.plugins.status.";

    // Variables
    private static Map<String, Plugin> _mapPlugins = new HashMap<String, Plugin>(  );
    private static List<PluginEventListener> _listPluginEventListeners = new ArrayList<PluginEventListener>(  );

    /**
     * Creates a new PluginService object.
     */
    private PluginService(  )
    {
    }

    /**
     * Initialize the service
     * @throws LuteceInitException If an error occured
     */
    public static void init(  ) throws LuteceInitException
    {
        loadPluginsStatus(  );
        _mapPlugins.clear(  );
        loadCoreComponents(  );
        loadPlugins(  );
    }

    /**
     * Returns the plugins file list
     *
     * @return the plugins file list as a File[]
     */
    public static Collection<Plugin> getPluginList(  )
    {
        TreeSet<Plugin> setSorted = new TreeSet<Plugin>( _mapPlugins.values(  ) );

        return setSorted;
    }

    /**
     * Returns a Plugin object from its name
     *
     * @param strPluginName The name of the plugin
     * @return The Plugin object corresponding to the name
     */
    public static Plugin getPlugin( String strPluginName )
    {
        return _mapPlugins.get( strPluginName );
    }

    /**
     * Load components of the core.
     * @throws LuteceInitException If an error occured
     */
    private static void loadCoreComponents(  ) throws LuteceInitException
    {
        File file = new File( AppPathService.getPath( PATH_CONF, CORE_XML ) );

        if ( file.exists(  ) )
        {
            loadPluginFromFile( file, false );
        }
    }

    /**
     * Load all plugins installed on the system.
     * @throws LuteceInitException If an error occured
     */
    private static void loadPlugins(  ) throws LuteceInitException
    {
        File dirPlugin = new File( AppPathService.getPath( PATH_PLUGIN ) );

        if ( dirPlugin.exists(  ) )
        {
            FilenameFilter select = new FileListFilter( "", EXTENSION_FILE );
            File[] listFile = dirPlugin.listFiles( select );

            for ( File file : listFile )
            {
                loadPluginFromFile( file, true );
            }
        }
    }

    /**
     * Load a plugin from a file definition
     * @param file The plugin file
     * @param bRegisterAsPlugin Register it as a plugin : true for plugins,
     *            false for core components file
     * @throws LuteceInitException If an error occured
     */
    private static void loadPluginFromFile( File file, boolean bRegisterAsPlugin )
        throws LuteceInitException
    {
        PluginFile pluginFile = new PluginFile(  );
        pluginFile.load( file.getAbsolutePath(  ) );

        String strPluginClass = pluginFile.getPluginClass(  );

        if ( strPluginClass != null )
        {
            try
            {
                Plugin plugin = (Plugin) Class.forName( strPluginClass ).newInstance(  );
                plugin.load( pluginFile );

                if ( bRegisterAsPlugin )
                {
                    plugin.setStatus( getPluginStatus( plugin ) );
                    registerPlugin( plugin );
                }
                else
                {
                    plugin.setStatus( true );
                    registerCore( plugin );
                }

                // If the plugin requires a database connection pool then
                // get the pool name and initialize its ConnectionService
                if ( plugin.isDbPoolRequired(  ) )
                {
                    String strPoolName = getPluginPoolName( plugin );
                    plugin.setPoolName( strPoolName );
                    plugin.initConnectionService( strPoolName );
                }

                plugin.init(  );

                // plugin installed event
                PluginEvent event = new PluginEvent( plugin, PluginEvent.PLUGIN_INSTALLED );
                notifyListeners( event );
            }
            catch ( Exception e )
            {
                throw new LuteceInitException( "Error instantiating plugin defined in file : " +
                    file.getAbsolutePath(  ), e );
            }
        }
        else
        {
            AppLogService.error( "No plugin class defined in file : " + file.getAbsolutePath(  ) );
        }
    }

    /**
     * Register the plugin as a plugin loaded in the system
     *
     * @param plugin The plugin object
     */
    private static void registerPlugin( Plugin plugin )
    {
        _mapPlugins.put( plugin.getName(  ), plugin );

        String strStatusWarning = ( plugin.isInstalled(  ) ) ? "" : " *** Warning : current status is 'disabled' ***";
        AppLogService.info( "New Plugin registered : " + plugin.getName(  ) + strStatusWarning );
    }

    /**
     * Gets the core plugin
     * @param plugin the plugin
     */
    private static synchronized void registerCore( Plugin plugin )
    {
        _pluginCore = plugin;
    }

    /**
     * Gets the core.
     *
     * @return the core
     */
    public static Plugin getCore(  )
    {
        return _pluginCore;
    }

    /**
     * Update plugins data.
     *
     * @param plugin The plugin object
     */
    public static void updatePluginData( Plugin plugin )
    {
        String strKey = getInstalledKey( plugin.getName(  ) );
        String strValue = plugin.isInstalled(  ) ? DatastoreService.VALUE_TRUE : DatastoreService.VALUE_FALSE;
        DatastoreService.setInstanceDataValue( strKey, strValue );

        if ( plugin.isDbPoolRequired(  ) )
        {
            DatastoreService.setInstanceDataValue( getPoolNameKey( plugin.getName(  ) ), plugin.getDbPoolName(  ) );
        }
    }

    /**
     * Build the datastore key for a given plugin
     * @param strPluginName The plugin name
     * @return The key
     */
    private static String getInstalledKey( String strPluginName )
    {
        return KEY_PLUGINS_STATUS + strPluginName + PROPERTY_IS_INSTALLED;
    }

    /**
     * Build the datastore key for a given plugin
     * @param strPluginName The plugin name
     * @return The key
     */
    private static String getPoolNameKey( String strPluginName )
    {
        return KEY_PLUGINS_STATUS + strPluginName + PROPERTY_DB_POOL_NAME;
    }

    /**
     * Load plugins status
     */
    private static void loadPluginsStatus(  )
    {
        // Load default values from the plugins.dat file
        String strPluginStatusFile = AppPathService.getPath( PATH_PLUGIN, FILE_PLUGINS_STATUS );
        File file = new File( strPluginStatusFile );
        Properties props = new Properties(  );
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream( file );
            props.load( fis );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Error loading plugin defined in file : " + file.getAbsolutePath(  ), e );
        }
        finally
        {
            if ( fis != null )
            {
                try
                {
                    fis.close(  );
                }
                catch ( IOException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
            }
        }

        // If the keys aren't found in the datastore then create a key in it
        for ( String strKey : props.stringPropertyNames(  ) )
        {
            // Initialize plugins status into Datastore
            int nPos = strKey.indexOf( PROPERTY_IS_INSTALLED );

            if ( nPos > 0 )
            {
                String strPluginName = strKey.substring( 0, nPos );
                String strDSKey = getInstalledKey( strPluginName );

                if ( !DatastoreService.existsInstanceKey( strDSKey ) )
                {
                    String strValue = props.getProperty( strKey ).equals( "1" ) ? DatastoreService.VALUE_TRUE
                                                                                : DatastoreService.VALUE_FALSE;
                    DatastoreService.setInstanceDataValue( strDSKey, strValue );
                }
            }

            // Initialize plugins connection pool into Datastore
            nPos = strKey.indexOf( PROPERTY_DB_POOL_NAME );

            if ( nPos > 0 )
            {
                String strPluginName = strKey.substring( 0, nPos );
                String strDSKey = getPoolNameKey( strPluginName );

                if ( !DatastoreService.existsInstanceKey( strDSKey ) )
                {
                    String strValue = props.getProperty( strKey );
                    DatastoreService.setInstanceDataValue( strDSKey, strValue );
                }
            }
        }
    }

    /**
     * Gets the plugin status
     *
     * @param plugin The plugin object
     * @return true if installed, otherwise false
     */
    private static boolean getPluginStatus( Plugin plugin )
    {
        String strValue = DatastoreService.getInstanceDataValue( getInstalledKey( plugin.getName(  ) ),
                DatastoreService.VALUE_FALSE );

        return strValue.equals( DatastoreService.VALUE_TRUE );
    }

    /**
     * Gets the pool that should be used by the plugin
     *
     * @param plugin The plugin Object
     * @return the pool name
     */
    private static String getPluginPoolName( Plugin plugin )
    {
        String strPoolname = DatastoreService.getInstanceDataValue( getPoolNameKey( plugin.getName(  ) ),
                AppConnectionService.NO_POOL_DEFINED );

        if ( strPoolname.equals( AppConnectionService.NO_POOL_DEFINED ) && plugin.isDbPoolRequired(  ) &&
                !plugin.getName(  ).equals( CORE ) )
        {
            AppLogService.info( " *** WARNING *** - The plugin '" + plugin +
                "' has no pool defined in db.properties or datastore. Using the default pool '" +
                AppConnectionService.DEFAULT_POOL_NAME + "' instead." );
            strPoolname = AppConnectionService.DEFAULT_POOL_NAME;
        }

        return strPoolname;
    }

    /**
     * Gets the plugin status enable / disable
     * @param strPluginName The plugin name
     * @return True if the plugin is enable, otherwise false
     */
    public static boolean isPluginEnable( String strPluginName )
    {
        if ( strPluginName.equals( CORE ) )
        {
            return true;
        }

        Plugin plugin = getPlugin( strPluginName );

        return ( ( plugin != null ) && ( plugin.isInstalled(  ) ) );
    }

    /**
     * Register a Plugin Event Listener
     * @param listener The listener
     */
    public static void registerPluginEventListener( PluginEventListener listener )
    {
        _listPluginEventListeners.add( listener );
    }

    /**
     * Notify an event to all Plugin Event Listeners
     * @param event The event
     */
    public static void notifyListeners( PluginEvent event )
    {
        for ( PluginEventListener listener : _listPluginEventListeners )
        {
            listener.processPluginEvent( event );
        }
    }
}
