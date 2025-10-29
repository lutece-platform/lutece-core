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

import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.ContentServiceEntry;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.dashboard.DashboardComponentEntry;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.admin.AdminDashboardService;
import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.filter.FilterEntry;
import fr.paris.lutece.portal.service.filter.FilterService;
import fr.paris.lutece.portal.service.includes.PageIncludeEntry;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.insert.InsertService;
import fr.paris.lutece.portal.service.insert.InsertServiceManager;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.rbac.RBACResourceTypeEntry;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.search.SearchIndexerEntry;
import fr.paris.lutece.portal.service.servlet.ServletEntry;
import fr.paris.lutece.portal.service.servlet.ServletService;
import fr.paris.lutece.portal.service.sessionlistener.HttpSessionListenerEntry;
import fr.paris.lutece.portal.service.sessionlistener.HttpSessionListenerService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

/**
 * This class is the general plugin element
 */
public abstract class Plugin implements Comparable<Plugin>, Serializable
{
    // Constantes
    public static final int PLUGIN_TYPE_FEATURE = 0x01;
    public static final int PLUGIN_TYPE_PORTLET = 0x02;
    public static final int PLUGIN_TYPE_APPLICATION = 0x04;
    public static final int PLUGIN_TYPE_INSERTSERVICE = 0x08;
    public static final int PLUGIN_TYPE_CONTENTSERVICE = 0x10;
    public static final int PLUGIN_TYPE_DAEMON = 0x20;
    private static final String PROPERTY_DEFAULT_ICON_URL = "plugin.image.defaultIconUrl";
    private static final String SCOPE_PORTAL = "portal";
    private static final String SCOPE_XPAGE = "xpage";

    // Variables
    private String _strName;
    private String _strVersion;
    private String _strDescription;
    private String _strProvider;
    private String _strProviderUrl;
    private String _strCopyright;
    private String _strPluginClass;
    private String _strDbPoolName;
    private String _strIconUrl;
    private String _strDocumentationUrl;
    private String _strMinCoreVersion;
    private String _strMaxCoreVersion;
    private boolean _bIsInstalled;
    private boolean _bDbPoolRequired;
    private String _strCssStylesheetsScope;
    private String _strJavascriptFilesScope;

    private boolean _bHasContentServices;
    private boolean _bhasDaemons;

    /**
     * Initializes the plugin at the first load
     *
     */
    public abstract void init( );

    /**
     * Load plugin's data from the plugin's xml file.
     *
     * @param pluginFile
     *            The plugin file object
     * @throws LuteceInitException
     *             If an error occured
     */
    void load( PluginFile pluginFile ) throws LuteceInitException
    {
        try
        {
            _strName = pluginFile.getName( );
            _strVersion = pluginFile.getVersion( );
            _strDescription = pluginFile.getDescription( );
            _strProvider = pluginFile.getProvider( );
            _strProviderUrl = pluginFile.getProviderUrl( );

            String strDefaultIconUrl = AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL );
            _strIconUrl = pluginFile.getIconUrl( ).equals( "" ) ? strDefaultIconUrl : pluginFile.getIconUrl( );
            _strDocumentationUrl = pluginFile.getDocumentationUrl( );
            _strCopyright = pluginFile.getCopyright( );
            _strPluginClass = pluginFile.getPluginClass( );
            _strMinCoreVersion = pluginFile.getMinCoreVersion( );
            _strMaxCoreVersion = pluginFile.getMaxCoreVersion( );
            _bDbPoolRequired = pluginFile.isDbPoolRequired( );
            _strCssStylesheetsScope = ( pluginFile.getCssStylesheetsScope( ) != null ) ? pluginFile.getCssStylesheetsScope( ) : SCOPE_XPAGE;
            _strJavascriptFilesScope = ( pluginFile.getJavascriptFilesScope( ) != null ) ? pluginFile.getJavascriptFilesScope( ) : SCOPE_XPAGE;

        	PluginData pluginData= new PluginData( pluginFile.getName( ) );
            pluginData.setXPageApplications(pluginFile.getXPageApplications( ));
            pluginData.setRights(pluginFile.getRights( ));
            pluginData.setInsertServices(pluginFile.getInsertServices( ));
            pluginData.setParams(pluginFile.getParams( ));
            pluginData.setPortletTypes(pluginFile.getPortletTypes( ));
            pluginData.setCssStyleSheets(pluginFile.getCssStyleSheetsForAllModes( ));
            pluginData.setJavascriptFiles(pluginFile.getJavascriptFilesForAllModes( ));
            pluginData.setFreemarkerAutoImports(pluginFile.getFreemarkerAutoImports( ));
            pluginData.setFreemarkerAutoIncludes(pluginFile.getFreemarkerAutoIncludes( ));
            pluginData.setAdminCssStyleSheets(pluginFile.getAdminCssStyleSheets( ));
            pluginData.setAdminJavascriptFiles(pluginFile.getAdminJavascriptFiles( ));
            
            registerPluginData( pluginData );            
            // Register plugin components
            registerXPageApplications( );
            registerFilters( pluginFile.getFilters( ) );
            registerServlets( pluginFile.getServlets( ) );
            registerListeners( pluginFile.getListeners( ) );
            registerContentServices( pluginFile.getContentServices( ) );
            registerInsertServices( );
            registerSearchIndexers(pluginFile.getSearchIndexers( ) );
            registerPageIncludes( pluginFile.getPageIncludes( ) );
            registerDashboardComponents( pluginFile.getDashboardComponents( ) );
            registerAdminDashboardComponents(pluginFile.getAdminDashboardComponents( ) );
            registerRBACResourceTypes( pluginFile.getRBACResourceTypes( ) );
            registerDaemons( pluginFile.getDaemons( ) );
        }
        catch( Exception e )
        {
            throw new LuteceInitException( "Error loading plugin : " + e.getMessage( ), e );
        }
    }

    /**
     * Returns weither or not plugin has portlet.
     *
     * @return true if the plugin contains one or more portlet
     */
    public boolean hasPortlets( )
    {
        return CollectionUtils.isNotEmpty( PluginService.getPluginData( this._strName ).getPortletTypes( ) );
    }

    /**
     * Returns weither or not plugin has daemon.
     *
     * @return true if the plugin contains one or more daemon
     */
    public boolean hasDaemons( )
    {
        return _bhasDaemons;
    }

    /**
     * Updates the plg file
     */
    protected void update( )
    {
        PluginService.updatePluginData( this );
    }

    /**
     * Modify the plugin status
     *
     * @param bStatus
     *            true installed, false uninstalled
     */
    public void setStatus( boolean bStatus )
    {
        _bIsInstalled = bStatus;
    }

    /**
     * Updates a database connection pool associated to the plugin and stores it
     * 
     * @param strPoolName
     *            the name of the pool
     */
    public void updatePoolName( String strPoolName )
    {
        _strDbPoolName = strPoolName;
        PluginService.getPluginData( this._strName ).getConnectionService( ).setPool(strPoolName);
        update( );

        notifyListeners( PluginEvent.PLUGIN_POOL_CHANGED );
        AppLogService.debug( "Database connection pool associated to plugin {} has been switched to {}", _strName, _strDbPoolName );
    }

    /**
     * Updates a database connection pool associated to the plugin and stores it
     * 
     * @param strPoolName
     *            The name of the pool
     */
    public void setPoolName( String strPoolName )
    {
        _strDbPoolName = strPoolName;
    }

    /**
     * Gets the current database connection pool associated to the plugin
     * 
     * @return The name of the database for the pool checked
     */
    public String getDbPoolName( )
    {
        return _strDbPoolName;
    }

    /**
     * Creates a new right in the rights set
     */
    protected void registerRights( )
    {
        for ( Right right : PluginService.getPluginData( this._strName ).getRights( ) )
        {
            RightHome.remove( right.getId( ) );

            if ( !( right.getId( ).equals( "" ) ) )
            {
                RightHome.create( right );
            }
        }
    }

    /**
     * Remove a right from the rights set.
     */
    protected void unregisterRights( )
    {
        for ( Right right : PluginService.getPluginData( this._strName ).getRights( ) )
        {
            if ( ( right != null ) && ( !( right.getId( ).equals( "" ) ) ) )
            {
                RightHome.remove( right.getId( ) );
            }
        }
    }

    /**
     * Creates a new portlet in the portlets type set
     */
    protected void registerPortlets( )
    {
        for ( PortletType portletType : PluginService.getPluginData( this._strName ).getPortletTypes( ) )
        {
            PortletTypeHome.remove( portletType.getId( ) );

            if ( ( portletType.getHomeClass( ) != null ) && ( !( portletType.getHomeClass( ).equals( "" ) ) ) )
            {
                PortletTypeHome.create( portletType );
            }
        }
    }

    /**
     * Remove a portlet from the portlets type set.
     */
    protected void unregisterPortlets( )
    {
        for ( PortletType portletType : PluginService.getPluginData( this._strName ).getPortletTypes( ) )
        {
            PortletTypeHome.remove( portletType.getId( ) );
        }
    }

    /**
     * Register XPage applications
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerXPageApplications( ) throws LuteceInitException
    {
        for ( XPageApplicationEntry entry : PluginService.getPluginData( this._strName ).getXPageApplications( ) )
        {
            entry.setPluginName( getName( ) );
            XPageAppService.registerXPageApplication( entry );
        }
    }

    /**
     * Register Filters
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerFilters( List<FilterEntry> listFilters ) throws LuteceInitException
    {
    	FilterService filterService = CDI.current( ).select( FilterService.class ).get( );
        for ( FilterEntry entry : listFilters )
        {
            filterService.registerFilter( entry, this );
        }
    }

    /**
     * Register Servlets
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerServlets( List<ServletEntry> listServlets) throws LuteceInitException
    {
    	ServletService servletService = CDI.current( ).select( ServletService.class ).get( );
        for ( ServletEntry entry : listServlets )
        {
            servletService.registerServlet( entry, this );
        }
    }

    /**
     * Register listeners
     * 
     * @throws LuteceInitException
     *             if an error occurs
     */
    protected void registerListeners( List<HttpSessionListenerEntry> listListeners) throws LuteceInitException
    {
        for ( HttpSessionListenerEntry entry : listListeners )
        {
            HttpSessionListenerService.registerListener( entry );
        }
    }

    /**
     * Register Content Services
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerContentServices(List<ContentServiceEntry> listContentServices) throws LuteceInitException
    {
    	_bHasContentServices= CollectionUtils.isNotEmpty( listContentServices );
        for ( ContentServiceEntry entry : listContentServices )
        {
            try
            {
                ContentService cs = (ContentService) Class.forName( entry.getClassName( ) ).getDeclaredConstructor( ).newInstance( );

                cs.setPluginName( getName( ) );

                PortalService.registerContentService( cs.getName( ), cs );
            }
            catch( InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e )
            {
                throw new LuteceInitException( e.getMessage( ), e );
            }
        }
    }

    /**
     * Register Insert Services
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerInsertServices( ) throws LuteceInitException
    {
        for ( InsertService is : PluginService.getPluginData( this._strName ).getInsertServices( ) )
        {
            is.setPluginName( getName( ) );
            InsertServiceManager.registerInsertService( is );
        }
    }

    /**
     * Register Search Indexers
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerSearchIndexers( List<SearchIndexerEntry> listSearchIndexers ) throws LuteceInitException
    {
        for ( SearchIndexerEntry entry : listSearchIndexers )
        {
            try
            {
                SearchIndexer indexer = (SearchIndexer) Class.forName( entry.getClassName( ) ).getDeclaredConstructor( ).newInstance( );
                IndexationService.registerIndexer( indexer );
            }
            catch( IllegalAccessException | ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException e )
            {
                throw new LuteceInitException( e.getMessage( ), e );
            }
        }
    }

    /**
     * Register Page Includes
     * 
     * @throws LuteceInitException
     *             If an error occured
     */
    protected void registerPageIncludes( List<PageIncludeEntry> listPageIncludes) throws LuteceInitException
    {
        for ( PageIncludeEntry entry : listPageIncludes )
        {
            entry.setPluginName( getName( ) );
            PageIncludeService.registerPageInclude( entry );
        }
    }

    /**
     * Register Dashboard Components
     * 
     * @throws LuteceInitException
     *             If an error occured
     */
    protected void registerDashboardComponents( List<DashboardComponentEntry>  listDashboardComponents) throws LuteceInitException
    {
    	DashboardService dashboardService = CDI.current( ).select( DashboardService.class ).get( );    	
        for ( DashboardComponentEntry entry : listDashboardComponents )
        {
            dashboardService.registerDashboardComponent( entry, this );
        }
    }

    /**
     * Register Admin Dashboard Components
     * 
     * @throws LuteceInitException
     *             If an error occured
     */
    protected void registerAdminDashboardComponents( List<DashboardComponentEntry> listAdminDashboardComponents ) throws LuteceInitException
    {
    	AdminDashboardService adminDashboardService = CDI.current( ).select( AdminDashboardService.class ).get( );    	
        for ( DashboardComponentEntry entry : listAdminDashboardComponents )
        {
            adminDashboardService.registerDashboardComponent( entry, this );
        }
    }

    /**
     * Register RBAC Resource Types
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerRBACResourceTypes( List<RBACResourceTypeEntry>  listRBACResourceTypes ) throws LuteceInitException
    {
        for ( RBACResourceTypeEntry entry : listRBACResourceTypes )
        {
            ResourceIdService ris;

            try
            {
                ris = (ResourceIdService) Class.forName( entry.getClassName( ) ).getDeclaredConstructor( ).newInstance( );
                // Each resource id service should register itself and its permissions
                ris.register( );
            }
            catch( InstantiationException | ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e )
            {
                throw new LuteceInitException( e.getMessage( ), e );
            }
        }
    }

    /**
     * Register Daemons
     * 
     * @throws LuteceInitException
     *             If an error occurs
     */
    protected void registerDaemons( List<DaemonEntry> listDaemons ) throws LuteceInitException
    {
    	_bhasDaemons = CollectionUtils.isNotEmpty( listDaemons );
        for ( DaemonEntry entry : listDaemons )
        {
            entry.setPluginName( getName( ) );
            AppDaemonService.registerDaemon( entry );
        }
    }

    /**
     * Installs a Plugin
     */
    public void install( )
    {
    	AppLogService.debug( "Installing plugin {} ...", ( ) -> this.getName( ) );
        // Register a new right for the plugin
    	AppLogService.debug( "Registering rights for {} plugin...", ( ) -> this.getName( ) );
        registerRights( );
        AppLogService.debug( "Rights for {} plugin registration completed", ( ) -> this.getName( ) );

        // Register a new portlets as plugin
        AppLogService.debug( "Registering portlets for {} plugin...", ( ) -> this.getName( ) );
        registerPortlets( );
        AppLogService.debug( "Portlets registration for {} plugin completed", ( ) -> this.getName( ) );

        _bIsInstalled = true;
        update( );
        AppLogService.debug( "Plugin {} installed", ( ) -> this.getName( ) );
        
        notifyListeners( PluginEvent.PLUGIN_INSTALLED );       
    }

    /**
     * Uninstalls a Plugin
     */
    public void uninstall( )
    {
    	AppLogService.debug( "Uninstalling plugin {}...", ( ) -> this.getName() );
        // Unregister a new right for the plugin
    	AppLogService.debug( "Unregistering rights for {} plugin...", ( ) -> this.getName( ) );
        unregisterRights( );
        AppLogService.debug( "Rights for {} plugin unregistration completed", ( ) -> this.getName( ) );

        // Unregister a new portlets as plugin
        AppLogService.debug( "Unregistering portlets for {} plugin...", ( ) -> this.getName( ) );
        unregisterPortlets( );
        AppLogService.debug( "Portlets unregistration for {} plugin completed", ( ) -> this.getName( ) );
        _bIsInstalled = false;
        update( );
        AppLogService.debug( "Plugin {} uninstalled", ( ) -> this.getName( ) );
        
        notifyListeners( PluginEvent.PLUGIN_UNINSTALLED );      
    }

    /**
     * Notifiy Listener
     * 
     * @param nEventType
     *            The event type
     */
    private void notifyListeners( int nEventType )
    {
        PluginEvent event = new PluginEvent( this, nEventType );
        CDI.current( ).getBeanManager( ).getEvent( ).fire( event );
    }

    /**
     * Returns the type of the plugin
     *
     * @return the plugin type as a int
     */
    public int getType( )
    {
        // Load the Type
        int nPluginTypeFlags = 0;

        if ( CollectionUtils.isNotEmpty( PluginService.getPluginData( this._strName ).getXPageApplications( ) ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_APPLICATION;
        }

        if ( CollectionUtils.isNotEmpty( PluginService.getPluginData( this._strName ).getPortletTypes( ) ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_PORTLET;
        }

        if ( CollectionUtils.isNotEmpty( PluginService.getPluginData( this._strName ).getRights( ) ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_FEATURE;
        }

        if ( CollectionUtils.isNotEmpty( PluginService.getPluginData( this._strName ).getInsertServices( ) ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_INSERTSERVICE;
        }

        if ( _bHasContentServices )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_CONTENTSERVICE;
        }

        if ( _bhasDaemons )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_DAEMON;
        }

        return nPluginTypeFlags;
    }

    /**
     * Returns the list of insert services of the plugin
     *
     * @return the plugin list of ContentServiceEntry
     */
    public List<InsertService> getInsertServices( )
    {
        return PluginService.getPluginData( this._strName ).getInsertServices( );
    }


    /**
     * Returns the list of XPage Applications of the plugin
     *
     * @return the plugin list of XPageApplicationEntry
     */
    public List<XPageApplicationEntry> getApplications( )
    {
        return PluginService.getPluginData( this._strName ).getXPageApplications( );
    }

    /**
     * Returns the list of portlet type of the plugin
     *
     * @return the plugin list of portlet type
     */
    public List<PortletType> getPortletTypes( )
    {
        return PluginService.getPluginData( this._strName ).getPortletTypes( );
    }

    /**
     * Returns the list of portlet type of the plugin
     *
     * @return the plugin list of rights
     */
    public List<Right> getRights( )
    {
        return PluginService.getPluginData( this._strName ).getRights( );
    }


    /**
     * Returns the name of the plugin
     *
     * @return the plugin name as a String
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * Sets the name of the plugin
     *
     * @param strName
     *            The plugin name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the version of the plugin
     *
     * @return the plugin version as a String
     */
    public String getVersion( )
    {
        return _strVersion;
    }

    /**
     * Sets the version plugin name
     * 
     * @param strVersion
     *            The version
     */
    public void setVersion( String strVersion )
    {
        _strVersion = strVersion;
    }

    /**
     * Returns the description of the plugin
     *
     * @return the plugin description as a String
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the description of the plugin
     *
     * @param strDescription
     *            The description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the Provider of the plugin
     *
     * @return the plugin Provider as a String
     */
    public String getProvider( )
    {
        return _strProvider;
    }

    /**
     * Sets the provider name
     *
     * @param strProvider
     *            The provider name
     */
    public void setProvider( String strProvider )
    {
        _strProvider = strProvider;
    }

    /**
     * Returns the Provider's URL of the plugin
     *
     * @return the plugin Provider's URL as a String
     */
    public String getProviderUrl( )
    {
        return _strProviderUrl;
    }

    /**
     * Sets the provider url
     *
     * @param strProviderUrl
     *            the name of the provider
     */
    public void setProviderUrl( String strProviderUrl )
    {
        _strProviderUrl = strProviderUrl;
    }

    /**
     * Returns the Icon's URL of the plugin
     *
     * @return the plugin Icon's URL as a String
     */
    public String getIconUrl( )
    {
        return _strIconUrl;
    }

    /**
     * Sets the url of the plugin's icon
     *
     * @param strIconUrl
     *            The url of icon
     */
    public void setIconUrl( String strIconUrl )
    {
        _strIconUrl = strIconUrl;
    }

    /**
     * Returns the Documentation's URL of the plugin
     *
     * @return the plugin Documentation's URL as a String
     */
    public String getDocumentationUrl( )
    {
        return _strDocumentationUrl;
    }

    /**
     * Sets the url of the plugin's Documentation
     *
     * @param strDocumentationUrl
     *            The documentation Url
     */
    public void setDocumentationUrl( String strDocumentationUrl )
    {
        _strDocumentationUrl = strDocumentationUrl;
    }

    /**
     * Returns the Copyright of the plugin
     *
     * @return the plugin Copyright as a String
     */
    public String getCopyright( )
    {
        return _strCopyright;
    }

    /**
     * Sets the copyright
     *
     * @param strCopyright
     *            The copyright
     */
    public void setCopyright( String strCopyright )
    {
        _strCopyright = strCopyright;
    }

    /**
     * Returns the main Class of the plugin
     *
     * @return the Class as a String
     */
    public String getServiceClass( )
    {
        return _strPluginClass;
    }

    /**
     * Sets the class service of plugin
     *
     * @param strPluginClass
     *            The plugin class
     */
    public void setServiceClass( String strPluginClass )
    {
        _strPluginClass = strPluginClass;
    }

    /**
     * Returns the installation status of the plugin
     *
     * @return the installation status as an int
     */
    public boolean isInstalled( )
    {
        return _bIsInstalled;
    }

    /**
     * Sets the boolean which shows if the plugin is installed
     *
     * @param bIsInstalled
     *            The installed boolean
     */
    public void setIsInstalled( boolean bIsInstalled )
    {
        _bIsInstalled = bIsInstalled;
    }

    /**
     * Returns the min core version compatibility for the plugin
     *
     * @return the min core version as a String
     */
    public String getMinCoreVersion( )
    {
        return _strMinCoreVersion;
    }

    /**
     * Sets the the min core version compatibility for the plugin
     *
     * @param strMinCoreVersion
     *            The min core version
     */
    public void setMinCoreVersion( String strMinCoreVersion )
    {
        _strMinCoreVersion = strMinCoreVersion;
    }

    /**
     * Returns the max core version compatibility for the plugin
     *
     * @return the max core version as a String
     */
    public String getMaxCoreVersion( )
    {
        return _strMaxCoreVersion;
    }

    /**
     * Sets the the max core version compatibility for the plugin
     *
     * @param strMaxCoreVersion
     *            The max core version
     */
    public void setMaxCoreVersion( String strMaxCoreVersion )
    {
        _strMaxCoreVersion = strMaxCoreVersion;
    }

    /**
     * Returns if the plugin needs a database connection pool
     *
     * @return <b>true</b> if the plugin needs a database connection pool, otherwise <b>false</b>
     */
    public boolean isDbPoolRequired( )
    {
        return _bDbPoolRequired;
    }

    /**
     * Sets the boolean which shows if a pool is required
     *
     * @param bDbPoolRequired
     *            The dbpool boolean
     */
    public void setIsDbPoolRequired( boolean bDbPoolRequired )
    {
        _bDbPoolRequired = bDbPoolRequired;
    }

    /**
     * Returns a Connection Service associated to the plugin
     * 
     * @return _connectionService The connection service
     */
    public PluginConnectionService getConnectionService( )
    {
        return PluginService.getPluginData( this._strName ).getConnectionService( );
    }

    /**
     * Sets the connection service
     *
     * @param connectionService
     *            The connection Service object
     */
    public void setConnectionService( PluginConnectionService connectionService )
    {
    	PluginService.getPluginData( this._strName ).setConnectionService(connectionService);
    }

    /**
     * Initializes the plugin's ConnectionService
     *
     * @param strPoolName
     *            The pool name
     */
    public void initConnectionService( String strPoolName )
    {
    	PluginService.getPluginData( this._strName ).setConnectionService(new PluginConnectionService( strPoolName ));
    }

    /**
     * Gets plugin's parameters
     * 
     * @return _mapParams The hastable of parameters
     */
    public Map<String, String> getParams( )
    {
        return PluginService.getPluginData( this._strName ).getParams( );
    }

    /**
     * Gets a parameter value for a given parameter name
     * 
     * @param strParamName
     *            The name of the parameter
     * @return null
     */
    public String getParamValue( String strParamName )
    {
        if ( !PluginService.getPluginData( this._strName ).getParams( ).containsKey( strParamName ) )
        {
            return null;
        }

        return PluginService.getPluginData( this._strName ).getParams( ).get( strParamName );
    }
    /**
     * Implementation of the Comparable interface.
     * 
     * @param plugin
     *            A plugin Object
     * @return 1, 0 ou -1 according the plugin name
     */
    @Override
    public int compareTo( Plugin plugin )
    {
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

        return comparator.compare( getName( ), plugin.getName( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o )
    {
        if ( !( o instanceof Plugin ) )
        {
            return false;
        }

        return compareTo( (Plugin) o ) == 0;
    }

    /**
     * Returns all CSS Style Sheets of the plugin with no mode associated
     * 
     * @return The list of CSS Style Sheets with no mode associated
     */
    public List<String> getCssStyleSheets( )
    {
        List<String> res = PluginService.getPluginData( this._strName ).getCssStyleSheets().get( null );        		
        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    /**
     * Returns all CSS Style Sheets of the plugin associated with a mode
     * 
     * @param mode
     *            the mode
     * @return The list of CSS Style Sheets associated with the mode
     * @since 5.1.0
     */
    public List<String> getCssStyleSheets( int mode )
    {
        List<String> res = PluginService.getPluginData( this._strName ).getCssStyleSheets( ).get( mode );
        		
        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    /**
     * Returns the theme the plugin use for rendering a Xpage
     * 
     * @param request
     *            The HttpServletRequest
     * @return The theme
     */
    public Theme getXPageTheme( HttpServletRequest request )
    {
        return null;
    }

    /**
     * Returns all Javascript File of the plugin with no mode associated
     * 
     * @return The list of Javascript File with no mode associated
     */
    public List<String> getJavascriptFiles( )
    {
        List<String> res = PluginService.getPluginData( this._strName ).getJavascriptFiles( ).get( null );        		
        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    /**
     * Returns all Javascript File of the plugin associated with a mode
     * 
     * @param mode
     *            the mode
     * @return The list of Javascript File with associated with the mode
     * @since 5.1.0
     */
    public List<String> getJavascriptFiles( int mode )
    {
        List<String> res = PluginService.getPluginData( this._strName ).getJavascriptFiles().get( mode );
        if ( res == null )
        {
            return Collections.emptyList( );
        }

        return res;
    }

    /**
     * Give the scope of css stylesheets
     * 
     * @return true if scope is portal otherwise false
     */
    public boolean isCssStylesheetsScopePortal( )
    {
        return ( ( _strCssStylesheetsScope != null ) && _strCssStylesheetsScope.equalsIgnoreCase( SCOPE_PORTAL ) );
    }

    /**
     * Give the scope of css stylesheets
     * 
     * @return true if scope is portal otherwise false
     */
    public boolean isCssStylesheetsScopeXPage( )
    {
        return ( ( _strCssStylesheetsScope != null ) && _strCssStylesheetsScope.equalsIgnoreCase( SCOPE_XPAGE ) );
    }

    /**
     * Give the scope of javascripts
     * 
     * @return true if scope is portal otherwise false
     */
    public boolean isJavascriptFilesScopePortal( )
    {
        return ( ( _strJavascriptFilesScope != null ) && _strJavascriptFilesScope.equalsIgnoreCase( SCOPE_PORTAL ) );
    }

    /**
     * Give the scope of javascripts
     * 
     * @return true if scope is portal otherwise false
     */
    public boolean isJavascriptFilesScopeXPage( )
    {
        return ( ( _strJavascriptFilesScope != null ) && _strJavascriptFilesScope.equalsIgnoreCase( SCOPE_XPAGE ) );
    }

    /**
     * Return the list of stylesheets that should be used in the Back Office
     * 
     * @return the list of stylesheets that should be used in the Back Office
     * @since 5.1
     */
    public List<String> getAdminCssStyleSheets( )
    {
    	return PluginService.getPluginData( this._strName ).getAdminCssStyleSheets( );
    }

    /**
     * Return the list of Javascript Files that should be used in the Back Office
     * 
     * @return the list of Javascript Files that should be used in the Back Office
     * @since 5.1
     */
    public List<String> getAdminJavascriptFiles( )
    {
    	return PluginService.getPluginData( this._strName ).getAdminJavascriptFiles( );
    }

    /**
     * Gets the freemarker auto-includes.
     *
     * @return the freemarker auto-includes
     */
    public List<String> getFreeMarkerAutoIncludes( )
    {
    	return PluginService.getPluginData( this._strName ).getFreemarkerAutoIncludes( );
    }


    /**
     * Gets the freemarker auto-imports.
     *
     * @return the freemarker auto-imports
     */
    public Map<String,String> getFreeMarkerAutoImports( )
    {
    	return PluginService.getPluginData( this._strName ).getFreemarkerAutoImports( );
    }
    
    private void registerPluginData( PluginData pluginData ) 
    {
    	PluginService.addPluginData(pluginData);
    }

    /**
     * Useful for debugging
     *
     * @return The plugin object in String format
     */
    @Override
    public String toString( )
    {
        return getName( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode( )
    {
        if ( getName( ) == null )
        {
            return 0;
        }

        return getName( ).toLowerCase( ).hashCode( );
    }
    
}
