/*
 * Copyright (c) 2002-2022, City of Paris
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

import java.util.Collection;
import java.util.HashMap;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.plugins.resource.loader.ResourceNotFoundException;
import fr.paris.lutece.portal.service.cache.CacheConfigUtil;
import fr.paris.lutece.portal.service.cache.Default107Cache;
import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import jakarta.enterprise.inject.spi.CDI;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * This class provides services and utilities for plugins management
 */
public final class PluginService
{
    // Constantes
    private static final String PATH_CONF = "path.conf";
    private static final String DEFAULT_PATH_CONF = "/WEB-INF/conf/";
    private static final String CORE_XML = "core.xml";
    private static final String CORE = "core";
    private static final String PATH_PLUGIN = "path.plugins";
    private static final String DEFAULT_PLUGINS_PATH_CONF = "/WEB-INF/plugins/";
    private static final String FILE_PLUGINS_STATUS = "plugins.dat";
    private static final String EXTENSION_FILE = ".xml";
    private static final String PROPERTY_IS_INSTALLED = ".installed";
    private static final String PROPERTY_DB_POOL_NAME = ".pool";
    private static final String KEY_PLUGINS_STATUS = "core.plugins.status.";
    private static final String KEY_UNINSTALLED_PLUGIN = "plugins.uninstalled.";
    private static final Logger logger = LogManager.getLogger(CacheConfigUtil.CACHE_LOGGER_NAME);

    // This map holds information associated only with the current instance and not shared across other instances.
    private static Map<String, PluginData> _mapPluginData = new HashMap<>( );

    private static Lutece107Cache<String, Plugin> _pluginCache;

    
    /**
     * Creates a new PluginService object.
     */
    private PluginService( )
    {
    }

    /**
     * Initialize the service
     * 
     * @throws LuteceInitException
     *             If an error occured
     */
    public static void init( ) throws LuteceInitException
    {
    	loadPluginsStatus( );

    	if( _pluginCache == null || _pluginCache.isClosed( )) {
    		
    		_pluginCache = new Default107Cache<String, Plugin>( "pluginCache", String.class, Plugin.class, true, true ){
    			 @Override
    		      public void enableCache(boolean bEnable) {
    				 logger.info( "pluginCache cache cannot be disabled because it contains information required for Lutece to operate correctly." );    			       
    		      }
    			 @Override
    			 public void resetCache() { 
    				 logger.info( "pluginCache cache cannot be reset because it contains information required for Lutece to operate correctly." );    			       
    			 }
    		};
    	
    	}else {
            _pluginCache.clear( );
    	}
    	
        //_mapPlugins.clear( );
        loadCoreComponents( );
        loadPlugins( );
    }

    /**
     * Returns the plugins file list
     *
     * @return the plugins file list as a File[]
     */
    public static Collection<Plugin> getPluginList( )
    {
    	return StreamSupport.stream(_pluginCache.spliterator(), false)
    		    .filter(entry -> !CORE.equals(entry.getKey()))
    		    .map(Lutece107Cache.Entry::getValue)
    		    .collect(Collectors.toList());
    }

    /**
     * Returns a Plugin object from its name
     *
     * @param strPluginName
     *            The name of the plugin
     * @return The Plugin object corresponding to the name
     */
    public static Plugin getPlugin( String strPluginName )
    {
        Plugin plugin = null;
        if ( null != strPluginName )
        {
            plugin = _pluginCache.get( strPluginName );
        }
        return plugin;
    }

    /**
     * Load components of the core.
     * 
     * @throws LuteceInitException
     *             If an error occured
     */
    private static void loadCoreComponents( ) throws LuteceInitException
    {
      loadPluginFromFile( AppPropertiesService.getProperty( PATH_CONF, DEFAULT_PATH_CONF ) + CORE_XML, false );   
    }

    /**
     * Load all plugins installed on the system.
     * 
     * @throws LuteceInitException
     *             If an error occured
     */
    private static void loadPlugins( ) throws LuteceInitException
    {
        Set<String> listFile= new HashSet<>( );
		try {
			listFile = AppPathService.getResourcesPathsFromRelativePath(AppPropertiesService.getProperty( PATH_PLUGIN, DEFAULT_PLUGINS_PATH_CONF ));
		} catch (ResourceNotFoundException e) {
			AppLogService.getLogger().warn("No XML plugins file found for installation in the directory defined by {} properties: {}", PATH_PLUGIN, e.getMessage( ));
			AppLogService.getLogger().debug("No XML plugins file found for installation in the directory {}", PATH_PLUGIN, e);			
		}
        for ( String pathFile : listFile )
        {
           if(pathFile.endsWith( EXTENSION_FILE )) {
        	   loadPluginFromFile( pathFile, true );
           }
        }
    }

    /**
     * Load a plugin from a file definition
     * 
     * @param file
     *            The plugin file
     * @param bRegisterAsPlugin
     *            Register it as a plugin : true for plugins, false for core components file
     * @throws LuteceInitException
     *             If an error occured
     */
    private static void loadPluginFromFile( String path, boolean bRegisterAsPlugin ) throws LuteceInitException
    {
        PluginFile pluginFile = new PluginFile( );
        pluginFile.load( path );

        // Check if plugin is uninstalled. If the plugin in uninstalled then the plugin is skipped, the plugin file is not read at all and no resources are
        // loaded. If the uninstalled key is not provided or the value is different from 1, the plugin is considered as installed.
        Config config = ConfigProvider.getConfig( );
        if ( 1 == config.getOptionalValue( KEY_UNINSTALLED_PLUGIN + pluginFile.getName( ), Integer.class ).orElse( 0 ) )
        {
            AppLogService.debug("Plugin uninstalled: {} ", pluginFile.getName( ));
            return;
        }
        
        String strPluginClass = pluginFile.getPluginClass( );

        if ( strPluginClass != null )
        {
            try
            {
                Plugin plugin = (Plugin) Class.forName( strPluginClass ).getDeclaredConstructor().newInstance();;
                plugin.load( pluginFile );

                if ( bRegisterAsPlugin )
                {
                    plugin.setStatus( getPluginStatus( plugin ) );
                    registerPlugin( plugin );
                }
                else
                {
                    plugin.setStatus( true );
                }

                // If the plugin requires a database connection pool then
                // get the pool name and initialize its ConnectionService
                if ( plugin.isDbPoolRequired( ) )
                {
                    String strPoolName = getPluginPoolName( plugin );
                    plugin.setPoolName( strPoolName );
                    plugin.initConnectionService( strPoolName );
                }
                
                plugin.init( );
                _pluginCache.put(plugin.getName( ), plugin);

                // plugin installed event
                if( plugin.isInstalled()) {
                	PluginEvent event = new PluginEvent( plugin, PluginEvent.PLUGIN_INSTALLED );
                	CDI.current( ).getBeanManager( ).getEvent( ).fire( event );
                }
            }
            catch( Exception e )
            {
                throw new LuteceInitException( "Error instantiating plugin defined in file : " + path, e );
            }
        }
        else
        {
            AppLogService.error( "No plugin class defined in file : {}", path );
        }
    }

    /**
     * Register the plugin as a plugin loaded in the system
     *
     * @param plugin
     *            The plugin object
     */
    private static void registerPlugin( Plugin plugin )
    {
        //_mapPlugins.put( plugin.getName( ), plugin );
        String strStatusWarning = ( plugin.isInstalled( ) ) ? "" : " *** Warning : current status is 'disabled' ***";
        AppLogService.info( "New Plugin registered : {} {}", plugin.getName( ), strStatusWarning );
    }
    /**
     * Gets the core.
     *
     * @return the core
     */
    public static Plugin getCore( )
    {
    	return _pluginCache.get(CORE);
        //return _pluginCore;
    }

    /**
     * Update plugins data.
     *
     * @param plugin
     *            The plugin object
     */
    public static void updatePluginData( Plugin plugin )
    {
        String strKey = getInstalledKey( plugin.getName( ) );
        String strValue = plugin.isInstalled( ) ? DatastoreService.VALUE_TRUE : DatastoreService.VALUE_FALSE;
        DatastoreService.setInstanceDataValue( strKey, strValue );

        if ( plugin.isDbPoolRequired( ) )
        {
            DatastoreService.setInstanceDataValue( getPoolNameKey( plugin.getName( ) ), plugin.getDbPoolName( ) );
        }
        _pluginCache.put(plugin.getName( ), plugin);
    }

    /**
     * Build the datastore key for a given plugin
     * 
     * @param strPluginName
     *            The plugin name
     * @return The key
     */
    private static String getInstalledKey( String strPluginName )
    {
        return KEY_PLUGINS_STATUS + strPluginName + PROPERTY_IS_INSTALLED;
    }

    /**
     * Build the datastore key for a given plugin
     * 
     * @param strPluginName
     *            The plugin name
     * @return The key
     */
    private static String getPoolNameKey( String strPluginName )
    {
        return KEY_PLUGINS_STATUS + strPluginName + PROPERTY_DB_POOL_NAME;
    }

    /**
     * Load plugins status
     */
    private static void loadPluginsStatus( )
    {
        // Load default values from the plugins.dat file
            Properties props = new Properties( );

        try 
        {
            props.load( AppPathService.getResourceStream(AppPropertiesService.getProperty( PATH_PLUGIN, DEFAULT_PATH_CONF ), FILE_PLUGINS_STATUS) );
        }
        catch( IOException | ResourceNotFoundException e )
        {
            AppLogService.error( "Error loading plugin defined in file : {} defined in the directory property conf {} ", FILE_PLUGINS_STATUS, PATH_PLUGIN, e );
        }

        // If the keys aren't found in the datastore then create a key in it
        for ( String strKey : props.stringPropertyNames( ) )
        {
            // Initialize plugins status into Datastore
            int nPos = strKey.indexOf( PROPERTY_IS_INSTALLED );

            if ( nPos > 0 )
            {
                String strPluginName = strKey.substring( 0, nPos );
                String strDSKey = getInstalledKey( strPluginName );

                if ( !DatastoreService.existsInstanceKey( strDSKey ) )
                {
                    String strValue = String.valueOf( props.getProperty( strKey ).equals( "1" ) );
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
     * @param plugin
     *            The plugin object
     * @return true if installed, otherwise false
     */
    private static boolean getPluginStatus( Plugin plugin )
    {
        String strValue = DatastoreService.getInstanceDataValue( getInstalledKey( plugin.getName( ) ), DatastoreService.VALUE_FALSE );

        return strValue.equals( DatastoreService.VALUE_TRUE );
    }

    /**
     * Gets the pool that should be used by the plugin
     *
     * @param plugin
     *            The plugin Object
     * @return the pool name
     */
    private static String getPluginPoolName( Plugin plugin )
    {
        String strPoolname = DatastoreService.getInstanceDataValue( getPoolNameKey( plugin.getName( ) ), AppConnectionService.NO_POOL_DEFINED );

        if ( strPoolname.equals( AppConnectionService.NO_POOL_DEFINED ) && plugin.isDbPoolRequired( ) && !plugin.getName( ).equals( CORE ) )
        {
            AppLogService.info( " *** WARNING *** - The plugin '{}' has no pool defined in db.properties or datastore. Using the default pool '{}' instead.",
                    plugin, AppConnectionService.DEFAULT_POOL_NAME );
            strPoolname = AppConnectionService.DEFAULT_POOL_NAME;
        }

        return strPoolname;
    }

    /**
     * Gets the plugin status enable / disable
     * 
     * @param strPluginName
     *            The plugin name
     * @return True if the plugin is enable, otherwise false
     */
    public static boolean isPluginEnable( String strPluginName )
    {
        if ( strPluginName.equals( CORE ) )
        {
            return true;
        }

        Plugin plugin = getPlugin( strPluginName );

        return ( ( plugin != null ) && ( plugin.isInstalled( ) ) );
    }
    /**
     * Add plginData object
     * @param pluginData
     */
    public static void addPluginData(PluginData pluginData) {
    	_mapPluginData.put(pluginData.getPluginName(), pluginData);
    }
    /**
     * Get pluginData object
     * @param pluginName the plugin name
     * @return the pluginData object
     */
    public static PluginData getPluginData(String pluginName) {
    	return _mapPluginData.get(pluginName);
    }
}
