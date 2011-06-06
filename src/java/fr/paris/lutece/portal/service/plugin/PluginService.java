/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.filesystem.FileListFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

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
    private static final String PATH_PLUGIN = "path.plugins";
    private static final String FILE_PLUGINS_STATUS = "plugins.dat";
    private static final String EXTENSION_FILE = "xml";
    private static final String PROPERTY_IS_INSTALLED = ".installed";
    private static final String PROPERTY_DB_POOL_NAME = ".pool";

    // Variables
    private static Map<String, Plugin> _mapPlugins = new HashMap<String, Plugin>(  );
    private static Properties _propPluginsStatus = new Properties(  );
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
        storePluginsStatus(  );
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

        if ( ( file != null ) && ( file.exists(  ) ) )
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

        if ( ( dirPlugin != null ) && dirPlugin.exists(  ) )
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
     * @param bRegisterAsPlugin Register it as a plugin : true for plugins, false for core components file
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
                plugin.setStatus( getPluginStatus( plugin ) );

                if ( bRegisterAsPlugin )
                {
                    registerPlugin( plugin );
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
        AppLogService.info( "New Plugin registered : " + plugin.getName(  ) );
    }

    /**
     * Update plugins data.
     *
     * @param plugin The plugin object
     */
    public static void updatePluginData( Plugin plugin )
    {
        String strKeyInstalled = plugin.getName(  ) + PROPERTY_IS_INSTALLED;
        _propPluginsStatus.setProperty( strKeyInstalled, ( plugin.isInstalled(  ) ) ? "1" : "0" );

        if ( plugin.isDbPoolRequired(  ) )
        {
            String strKeyPool = plugin.getName(  ) + PROPERTY_DB_POOL_NAME;
            _propPluginsStatus.setProperty( strKeyPool, plugin.getDbPoolName(  ) );
        }

        storePluginsStatus(  );
    }

    /**
     * Stores the plugins info
     */
    private static void storePluginsStatus(  )
    {
        for ( Plugin plugin : getPluginList(  ) )
        {
            _propPluginsStatus.setProperty( plugin.getName(  ) + PROPERTY_IS_INSTALLED,
                plugin.isInstalled(  ) ? "1" : "0" );
        }

        try
        {
            String strPluginStatusFile = AppPathService.getPath( PATH_PLUGIN, FILE_PLUGINS_STATUS );
            File file = new File( strPluginStatusFile );
            FileOutputStream fos = new FileOutputStream( file );
            _propPluginsStatus.store( fos, "Plugins status file" );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Error storing plugins status file : " + e.getMessage(  ), e );
        }
    }

    /**
     * Load plugins status
     */
    private static void loadPluginsStatus(  )
    {
        String strPluginStatusFile = AppPathService.getPath( PATH_PLUGIN, FILE_PLUGINS_STATUS );
        File file = new File( strPluginStatusFile );

        try
        {
            FileInputStream fis = new FileInputStream( file );
            _propPluginsStatus.load( fis );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Error loading plugin defined in file : " + file.getAbsolutePath(  ), e );
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
        String strStatus = _propPluginsStatus.getProperty( plugin.getName(  ) + PROPERTY_IS_INSTALLED, "0" );

        return ( strStatus.equals( "1" ) ? true : false );
    }

    /**
     * Gets the pool that should be used by the plugin
     *
     * @param plugin The plugin Object
     * @return the pool name
     */
    private static String getPluginPoolName( Plugin plugin )
    {
        String strPoolName = _propPluginsStatus.getProperty( plugin.getName(  ) + PROPERTY_DB_POOL_NAME,
                AppConnectionService.NO_POOL_DEFINED );

        return strPoolName;
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
