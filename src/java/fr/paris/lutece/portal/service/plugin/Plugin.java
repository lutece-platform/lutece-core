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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class is the general plugin element
 */
public abstract class Plugin implements Comparable<Plugin>
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
    private ContentService _contentService;
    private String _strCssStylesheetsScope;
    private String _strJavascriptFilesScope;

    // Lists of rights and portlets of the plugin
    private List<XPageApplicationEntry> _listXPageApplications;
    private List<FilterEntry> _listFilters;
    private List<ServletEntry> _listServlets;
    private List<HttpSessionListenerEntry> _listListeners;
    private Map<Integer, List<String>> _listCssStyleSheets;
    private Map<Integer, List<String>> _listJavascriptFiles;
    private List<String> _listAdminCssStyleSheets;
    private List<String> _listAdminJavascriptFiles;
    private List<Right> _listRights;
    private List<PortletType> _listPortletTypes;
    private List<ContentServiceEntry> _listContentServices;
    private List<SearchIndexerEntry> _listSearchIndexers;
    private List<InsertService> _listInsertServices;
    private List<PageIncludeEntry> _listPageIncludes;
    private List<DashboardComponentEntry> _listDashboardComponents;
    private List<DashboardComponentEntry> _listAdminDashboardComponents;
    private List<RBACResourceTypeEntry> _listRBACResourceTypes;
    private List<DaemonEntry> _listDaemons;
    private List<String> _listFreemarkerMacrosFiles;

    //hashtable which contains all the params described in the xml plugin file
    private Map<String, String> _mapParams = new HashMap<String, String>(  );
    private PluginConnectionService _connectionService;

    /**
     * Initializes the plugin at the first load
     *
     */
    public abstract void init(  );

    /**
     * Load plugin's data from the plugin's xml file.
     *
     * @param pluginFile The plugin file object
     * @throws LuteceInitException If an error occured
     */
    void load( PluginFile pluginFile ) throws LuteceInitException
    {
        try
        {
            _strName = pluginFile.getName(  );
            _strVersion = pluginFile.getVersion(  );
            _strDescription = pluginFile.getDescription(  );
            _strProvider = pluginFile.getProvider(  );
            _strProviderUrl = pluginFile.getProviderUrl(  );

            String strDefaultIconUrl = AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL );
            _strIconUrl = pluginFile.getIconUrl(  ).equals( "" ) ? strDefaultIconUrl : pluginFile.getIconUrl(  );
            _strDocumentationUrl = pluginFile.getDocumentationUrl(  );
            _strCopyright = pluginFile.getCopyright(  );
            _strPluginClass = pluginFile.getPluginClass(  );
            _strMinCoreVersion = pluginFile.getMinCoreVersion(  );
            _strMaxCoreVersion = pluginFile.getMaxCoreVersion(  );
            _listXPageApplications = pluginFile.getXPageApplications(  );
            _listFilters = pluginFile.getFilters(  );
            _listServlets = pluginFile.getServlets(  );
            _listListeners = pluginFile.getListeners(  );
            _listRights = pluginFile.getRights(  );
            _listPortletTypes = pluginFile.getPortletTypes(  );
            _listContentServices = pluginFile.getContentServices(  );
            _listInsertServices = pluginFile.getInsertServices(  );
            _listSearchIndexers = pluginFile.getSearchIndexers(  );
            _listPageIncludes = pluginFile.getPageIncludes(  );
            _listDashboardComponents = pluginFile.getDashboardComponents(  );
            _listAdminDashboardComponents = pluginFile.getAdminDashboardComponents(  );
            _listRBACResourceTypes = pluginFile.getRBACResourceTypes(  );
            _listDaemons = pluginFile.getDaemons(  );
            _mapParams = pluginFile.getParams(  );
            _bDbPoolRequired = pluginFile.isDbPoolRequired(  );

            _listCssStyleSheets = pluginFile.getCssStyleSheetsForAllModes(  );
            _strCssStylesheetsScope = ( pluginFile.getCssStylesheetsScope(  ) != null )
                ? pluginFile.getCssStylesheetsScope(  ) : SCOPE_XPAGE;
            _listJavascriptFiles = pluginFile.getJavascriptFilesForAllModes(  );
            _strJavascriptFilesScope = ( pluginFile.getJavascriptFilesScope(  ) != null )
                ? pluginFile.getJavascriptFilesScope(  ) : SCOPE_XPAGE;
            _listFreemarkerMacrosFiles = pluginFile.getFreemarkerMacrosFiles(  );
            _listAdminCssStyleSheets = pluginFile.getAdminCssStyleSheets(  );
            _listAdminJavascriptFiles = pluginFile.getAdminJavascriptFiles(  );
            // Register plugin components
            registerXPageApplications(  );
            registerFilters(  );
            registerServlets(  );
            registerListeners(  );
            registerContentServices(  );
            registerInsertServices(  );
            registerSearchIndexers(  );
            registerPageIncludes(  );
            registerDashboardComponents(  );
            registerAdminDashboardComponents(  );
            registerRBACResourceTypes(  );
            registerDaemons(  );
        }
        catch ( Exception e )
        {
            throw new LuteceInitException( "Error loading plugin : " + e.getMessage(  ), e );
        }
    }

    /**
     *
     * @return the content service
     */
    public ContentService getContentService(  )
    {
        return _contentService;
    }

    /**
     * Returns weither or not plugin has portlet.
     *
     * @return true if the plugin contains one or more portlet
     */
    public boolean hasPortlets(  )
    {
        return ( _listPortletTypes.size(  ) > 0 );
    }

    /**
     * Returns weither or not plugin has daemon.
     *
     * @return true if the plugin contains one or more daemon
     */
    public boolean hasDaemons(  )
    {
        return ( _listDaemons.size(  ) > 0 );
    }

    /**
     * Returns The daemons list of the plugin.
     *
     * @return The daemons list of the plugin
     */
    public List<DaemonEntry> getDaemons(  )
    {
        return _listDaemons;
    }

    /**
     * Updates the plg file
     */
    protected void update(  )
    {
        PluginService.updatePluginData( this );
    }

    /**
     * Modify the plugin status
     *
     * @param bStatus true installed, false uninstalled
     */
    public void setStatus( boolean bStatus )
    {
        _bIsInstalled = bStatus;
    }

    /**
     * Updates a database connection pool associated to the plugin and stores it
     * @param strPoolName the name of the pool
     */
    public void updatePoolName( String strPoolName )
    {
        _strDbPoolName = strPoolName;
        _connectionService.setPool( strPoolName );
        update(  );

        notifyListeners( PluginEvent.PLUGIN_POOL_CHANGED );
    }

    /**
     * Updates a database connection pool associated to the plugin and stores it
     * @param strPoolName The name of the pool
     */
    public void setPoolName( String strPoolName )
    {
        _strDbPoolName = strPoolName;
    }

    /**
     * Gets the current database connection pool associated to the plugin
     * @return The name of the database for the pool checked
     */
    public String getDbPoolName(  )
    {
        return _strDbPoolName;
    }

    /**
     * Creates a new right in the rights set
     */
    protected void registerRights(  )
    {
        for ( Right right : _listRights )
        {
            RightHome.remove( right.getId(  ) );

            if ( !( right.getId(  ).equals( "" ) ) )
            {
                RightHome.create( right );
            }
        }
    }

    /**
     * Remove a right from the rights set.
     */
    protected void unregisterRights(  )
    {
        for ( Right right : _listRights )
        {
            if ( ( right != null ) && ( !( right.getId(  ).equals( "" ) ) ) )
            {
                RightHome.remove( right.getId(  ) );
            }
        }
    }

    /**
     * Creates a new portlet in the portlets type set
     */
    protected void registerPortlets(  )
    {
        for ( PortletType portletType : _listPortletTypes )
        {
            PortletTypeHome.remove( portletType.getId(  ) );

            if ( ( portletType.getHomeClass(  ) != null ) && ( !( portletType.getHomeClass(  ).equals( "" ) ) ) )
            {
                PortletTypeHome.create( portletType );
            }
        }
    }

    /**
     * Remove a portlet from the portlets type set.
     */
    protected void unregisterPortlets(  )
    {
        for ( PortletType portletType : _listPortletTypes )
        {
            PortletTypeHome.remove( portletType.getId(  ) );
        }
    }

    /**
     * Register XPage applications
     * @throws LuteceInitException If an error occurs
     */
    protected void registerXPageApplications(  ) throws LuteceInitException
    {
        for ( XPageApplicationEntry entry : _listXPageApplications )
        {
            entry.setPluginName( getName(  ) );
            XPageAppService.registerXPageApplication( entry );
        }
    }

    /**
     * Register Filters
     * @throws LuteceInitException If an error occurs
     */
    protected void registerFilters(  ) throws LuteceInitException
    {
        for ( FilterEntry entry : _listFilters )
        {
            FilterService.getInstance(  ).registerFilter( entry, this );
        }
    }

    /**
     * Register Servlets
     * @throws LuteceInitException If an error occurs
     */
    protected void registerServlets(  ) throws LuteceInitException
    {
        for ( ServletEntry entry : _listServlets )
        {
            ServletService.getInstance(  ).registerServlet( entry, this );
        }
    }

    /**
     * Register listeners
     * @throws LuteceInitException if an error occurs
     */
    protected void registerListeners(  ) throws LuteceInitException
    {
        for ( HttpSessionListenerEntry entry : _listListeners )
        {
            HttpSessionListenerService.registerListener( entry );
        }
    }

    /**
     * Register Content Services
     * @throws LuteceInitException If an error occurs
     */
    protected void registerContentServices(  ) throws LuteceInitException
    {
        for ( ContentServiceEntry entry : _listContentServices )
        {
            try
            {
                ContentService cs = (ContentService) Class.forName( entry.getClassName(  ) ).newInstance(  );

                cs.setPluginName( getName(  ) );

                PortalService.registerContentService( cs.getName(  ), cs );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Register Insert Services
     * @throws LuteceInitException If an error occurs
     */
    protected void registerInsertServices(  ) throws LuteceInitException
    {
        for ( InsertService is : _listInsertServices )
        {
            is.setPluginName( getName(  ) );
            InsertServiceManager.registerInsertService( is );
        }
    }

    /**
     * Register Search Indexers
     * @throws LuteceInitException If an error occurs
     */
    protected void registerSearchIndexers(  ) throws LuteceInitException
    {
        for ( SearchIndexerEntry entry : _listSearchIndexers )
        {
            try
            {
                SearchIndexer indexer = (SearchIndexer) Class.forName( entry.getClassName(  ) ).newInstance(  );
                IndexationService.registerIndexer( indexer );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Register Page Includes
     * @throws LuteceInitException If an error occured
     */
    protected void registerPageIncludes(  ) throws LuteceInitException
    {
        for ( PageIncludeEntry entry : _listPageIncludes )
        {
            entry.setPluginName( getName(  ) );
            PageIncludeService.registerPageInclude( entry );
        }
    }

    /**
     * Register Dashboard Components
     * @throws LuteceInitException If an error occured
     */
    protected void registerDashboardComponents(  ) throws LuteceInitException
    {
        for ( DashboardComponentEntry entry : _listDashboardComponents )
        {
            DashboardService.getInstance(  ).registerDashboardComponent( entry, this );
        }
    }

    /**
     * Register Admin Dashboard Components
     * @throws LuteceInitException If an error occured
     */
    protected void registerAdminDashboardComponents(  )
        throws LuteceInitException
    {
        for ( DashboardComponentEntry entry : _listAdminDashboardComponents )
        {
            AdminDashboardService.getInstance(  ).registerDashboardComponent( entry, this );
        }
    }

    /**
     * Register RBAC Resource Types
     * @throws LuteceInitException If an error occurs
     */
    protected void registerRBACResourceTypes(  ) throws LuteceInitException
    {
        for ( RBACResourceTypeEntry entry : _listRBACResourceTypes )
        {
            ResourceIdService ris;

            try
            {
                ris = (ResourceIdService) Class.forName( entry.getClassName(  ) ).newInstance(  );
                // Each resource id service should register itself and its permissions
                ris.register(  );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Register Daemons
     * @throws LuteceInitException If an error occurs
     */
    protected void registerDaemons(  ) throws LuteceInitException
    {
        for ( DaemonEntry entry : _listDaemons )
        {
            entry.setPluginName( getName(  ) );
            AppDaemonService.registerDaemon( entry );
        }
    }

    /**
     * Installs a Plugin
     */
    public void install(  )
    {
        // Register a new right for the plugin
        registerRights(  );

        // Register a new portlets as plugin
        registerPortlets(  );

        _bIsInstalled = true;
        update(  );

        notifyListeners( PluginEvent.PLUGIN_INSTALLED );
    }

    /**
     * Uninstalls a Plugin
     */
    public void uninstall(  )
    {
        // Unregister a new right for the plugin
        unregisterRights(  );

        // Unregister a new portlets as plugin
        unregisterPortlets(  );
        _bIsInstalled = false;
        update(  );

        notifyListeners( PluginEvent.PLUGIN_UNINSTALLED );
    }

    /**
     * Notifiy Listener
     * @param nEventType The event type
     */
    private void notifyListeners( int nEventType )
    {
        PluginEvent event = new PluginEvent( this, nEventType );
        PluginService.notifyListeners( event );
    }

    /**
     * Returns the type of the plugin
     *
     * @return the plugin type as a int
     */
    public int getType(  )
    {
        // Load the Type
        int nPluginTypeFlags = 0;

        if ( ( _listXPageApplications != null ) && ( _listXPageApplications.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_APPLICATION;
        }

        if ( ( _listPortletTypes != null ) && ( _listPortletTypes.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_PORTLET;
        }

        if ( ( _listRights != null ) && ( _listRights.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_FEATURE;
        }

        if ( ( _listInsertServices != null ) && ( _listInsertServices.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_INSERTSERVICE;
        }

        if ( ( _listContentServices != null ) && ( _listContentServices.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_CONTENTSERVICE;
        }

        if ( ( _listDaemons != null ) && ( !_listDaemons.isEmpty(  ) ) )
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
    public List<InsertService> getInsertServices(  )
    {
        return _listInsertServices;
    }

    /**
     * Returns the list of Content services of the plugin
     *
     * @return the plugin list of ContentServiceEntry
     */
    public List<ContentServiceEntry> getContentServices(  )
    {
        return _listContentServices;
    }

    /**
     * Returns the list of XPage Applications of the plugin
     *
     * @return the plugin list of XPageApplicationEntry
     */
    public List<XPageApplicationEntry> getApplications(  )
    {
        return _listXPageApplications;
    }

    /**
     * Returns the list of portlet type of the plugin
     *
     * @return the plugin list of portlet type
     */
    public List<PortletType> getPortletTypes(  )
    {
        return _listPortletTypes;
    }

    /**
     * Sets the list of portlet type
     *
     * @param listPortletTypes The portlet type list
     */
    public void setPortletTypes( List<PortletType> listPortletTypes )
    {
        _listPortletTypes = listPortletTypes;
    }

    /**
     * Returns the list of portlet type of the plugin
     *
     * @return the plugin list of rights
     */
    public List<Right> getRights(  )
    {
        return _listRights;
    }

    /**
     * Sets plugin rights list
     *
     * @param listRights The rights list
     */
    public void setRights( List<Right> listRights )
    {
        _listRights = listRights;
    }

    /**
     * Returns the name of the plugin
     *
     * @return the plugin name as a String
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the plugin
     *
     * @param strName The plugin name
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
    public String getVersion(  )
    {
        return _strVersion;
    }

    /**
     * Sets the version plugin name
     * @param strVersion The version
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
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description of the plugin
     *
     * @param strDescription The description
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
    public String getProvider(  )
    {
        return _strProvider;
    }

    /**
     * Sets the provider name
     *
     * @param strProvider The provider name
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
    public String getProviderUrl(  )
    {
        return _strProviderUrl;
    }

    /**
     * Sets the provider url
     *
     * @param strProviderUrl the name of the provider
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
    public String getIconUrl(  )
    {
        return _strIconUrl;
    }

    /**
     * Sets the url of the plugin's icon
     *
     * @param strIconUrl The url of icon
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
    public String getDocumentationUrl(  )
    {
        return _strDocumentationUrl;
    }

    /**
     * Sets the url of the plugin's Documentation
     *
     * @param strDocumentationUrl The documentation Url
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
    public String getCopyright(  )
    {
        return _strCopyright;
    }

    /**
     * Sets the copyright
     *
     * @param strCopyright The copyright
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
    public String getServiceClass(  )
    {
        return _strPluginClass;
    }

    /**
     * Sets the class service of plugin
     *
     * @param strPluginClass The plugin class
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
    public boolean isInstalled(  )
    {
        return _bIsInstalled;
    }

    /**
     * Sets the boolean which shows if the plugin is installed
     *
     * @param bIsInstalled The installed boolean
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
    public String getMinCoreVersion(  )
    {
        return _strMinCoreVersion;
    }

    /**
     * Sets the the min core version compatibility for the plugin
     *
     * @param strMinCoreVersion The min core version
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
    public String getMaxCoreVersion(  )
    {
        return _strMaxCoreVersion;
    }

    /**
     * Sets the the max core version compatibility for the plugin
     *
     * @param strMaxCoreVersion The max core version
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
    public boolean isDbPoolRequired(  )
    {
        return _bDbPoolRequired;
    }

    /**
     * Sets the boolean which shows if a pool is required
     *
     * @param bDbPoolRequired The dbpool boolean
     */
    public void setIsDbPoolRequired( boolean bDbPoolRequired )
    {
        _bDbPoolRequired = bDbPoolRequired;
    }

    /**
     * Returns a Connection Service associated to the plugin
     * @return _connectionService The connection service
     */
    public PluginConnectionService getConnectionService(  )
    {
        return _connectionService;
    }

    /**
     * Sets the connection service
     *
     * @param connectionService The connection Service object
     */
    public void setConnectionService( PluginConnectionService connectionService )
    {
        _connectionService = connectionService;
    }

    /**
     * Initializes the plugin's ConnectionService
     *
     * @param strPoolName The pool name
     */
    public void initConnectionService( String strPoolName )
    {
        _connectionService = new PluginConnectionService( strPoolName );
    }

    /**
     * Gets plugin's parameters
     * @return _mapParams The hastable of parameters
     */
    public Map<String, String> getParams(  )
    {
        return _mapParams;
    }

    /**
     * Gets a parameter value for a given parameter name
     * @param strParamName The name of the parameter
     * @return null
     */
    public String getParamValue( String strParamName )
    {
        if ( !_mapParams.containsKey( strParamName ) )
        {
            return null;
        }

        return _mapParams.get( strParamName );
    }

    /**
     * Sets parameters values with an hashtable
     * @param mapParams The parameter map
     */
    public void setParams( Map<String, String> mapParams )
    {
        _mapParams = mapParams;
    }

    /**
     * Sets a parameter value for a given parameter name
     * @param strParamName The name of the parameter
     * @param strParamValue The value of the parameter
     */
    public void setParamValue( String strParamName, String strParamValue )
    {
        if ( _mapParams.containsKey( strParamName ) )
        {
            _mapParams.put( strParamName, strParamValue );
        }
    }

    /**
     * Implementation of the Comparable interface.
     * @param plugin A plugin Object
     * @return 1, 0 ou -1 according the plugin name
     */
    @Override
    public int compareTo( Plugin plugin )
    {
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

        return comparator.compare( getName(  ), plugin.getName(  ) );
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
     * @return The list of CSS Style Sheets with no mode associated
     */
    public List<String> getCssStyleSheets(  )
    {
        List<String> res = _listCssStyleSheets.get( null );

        if ( res == null )
        {
            return Collections.emptyList(  );
        }

        return res;
    }

    /**
     * Returns all CSS Style Sheets of the plugin associated with a mode
     * @param mode the mode
     * @return The list of CSS Style Sheets associated with the mode
     * @since 5.1.0
     */
    public List<String> getCssStyleSheets( int mode )
    {
        List<String> res = _listCssStyleSheets.get( mode );

        if ( res == null )
        {
            return Collections.emptyList(  );
        }

        return res;
    }

    /**
     * Returns the theme the plugin use for rendering a Xpage
     * @param request The HttpServletRequest
     * @return The theme
     */
    public Theme getXPageTheme( HttpServletRequest request )
    {
        return null;
    }

    /**
     * Add an Javascript File to the plugin definition, not associated with a mode
     * @param strJavascriptFile The Javascript File path
     */
    public void addJavascriptFile( String strJavascriptFile )
    {
        List<String> files = _listJavascriptFiles.get( null );

        if ( files == null )
        {
            files = new ArrayList<String>(  );
            _listJavascriptFiles.put( null, files );
        }

        files.add( strJavascriptFile );
    }

    /**
     * Returns all Javascript File of the plugin with no mode associated
     * @return The list of Javascript File with no mode associated
     */
    public List<String> getJavascriptFiles(  )
    {
        List<String> res = _listJavascriptFiles.get( null );

        if ( res == null )
        {
            return Collections.emptyList(  );
        }

        return res;
    }

    /**
     * Returns all Javascript File of the plugin associated with a mode
     * @param mode the mode
     * @return The list of Javascript File with associated with the mode
     * @since 5.1.0
     */
    public List<String> getJavascriptFiles( int mode )
    {
        List<String> res = _listJavascriptFiles.get( mode );

        if ( res == null )
        {
            return Collections.emptyList(  );
        }

        return res;
    }

    /**
     * Give the scope of css stylesheets
     * @return true if scope is portal otherwise false
     */
    public boolean isCssStylesheetsScopePortal(  )
    {
        return ( ( _strCssStylesheetsScope != null ) && _strCssStylesheetsScope.equalsIgnoreCase( SCOPE_PORTAL ) );
    }

    /**
     * Give the scope of css stylesheets
     * @return true if scope is portal otherwise false
     */
    public boolean isCssStylesheetsScopeXPage(  )
    {
        return ( ( _strCssStylesheetsScope != null ) && _strCssStylesheetsScope.equalsIgnoreCase( SCOPE_XPAGE ) );
    }

    /**
     * Give the scope of javascripts
     * @return true if scope is portal otherwise false
     */
    public boolean isJavascriptFilesScopePortal(  )
    {
        return ( ( _strJavascriptFilesScope != null ) && _strJavascriptFilesScope.equalsIgnoreCase( SCOPE_PORTAL ) );
    }

    /**
     * Give the scope of javascripts
     * @return true if scope is portal otherwise false
     */
    public boolean isJavascriptFilesScopeXPage(  )
    {
        return ( ( _strJavascriptFilesScope != null ) && _strJavascriptFilesScope.equalsIgnoreCase( SCOPE_XPAGE ) );
    }

    /**
     * Return the list of stylesheets that should be used in the Back Office
     * @return the list of stylesheets that should be used in the Back Office
     * @since 5.1
     */
    public List<String> getAdminCssStyleSheets(  )
    {
        return _listAdminCssStyleSheets;
    }

    /**
     * Return the list of Javascript Files that should be used in the Back Office
     * @return the list of Javascript Files that should be used in the Back Office
     * @since 5.1
     */
    public List<String> getAdminJavascriptFiles(  )
    {
        return _listAdminJavascriptFiles;
    }

    /**
     * Adds a that file that will be autoincluded in freemarker templates
     * @param strMacroFileName the file name
     */
    public void addFreemarkerMacrosFile( String strMacroFileName )
    {
        _listFreemarkerMacrosFiles.add( strMacroFileName );
    }

    /**
     * Gets the free marker macros files.
     *
     * @return the free marker macros files
     */
    public List<String> getFreeMarkerMacrosFiles(  )
    {
        return _listFreemarkerMacrosFiles;
    }

    /**
     * Useful for debugging
     *
     * @return The plugin object in String format
     */
    @Override
    public String toString(  )
    {
        return getName(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(  )
    {
        if ( getName(  ) == null )
        {
            return 0;
        }

        return getName(  ).toLowerCase(  ).hashCode(  );
    }
}
