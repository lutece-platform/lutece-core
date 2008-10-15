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
package fr.paris.lutece.portal.service.plugin;

import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.service.content.ContentServiceEntry;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.dashboard.DashboardComponentEntry;
import fr.paris.lutece.portal.service.filter.FilterEntry;
import fr.paris.lutece.portal.service.includes.PageIncludeEntry;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.insert.InsertService;
import fr.paris.lutece.portal.service.rbac.RBACResourceTypeEntry;
import fr.paris.lutece.portal.service.search.SearchIndexerEntry;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;

import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is the plugin file element
 */
public class PluginFile
{
    private static final String FILE_RULES = "/fr/paris/lutece/portal/service/plugin/plugin-digester-rules.xml";

    // Variables
    private String _strName;
    private String _strVersion;
    private String _strDescription;
    private String _strProvider;
    private String _strProviderUrl;
    private String _strCopyright;
    private String _strPluginClass;
    private String _strIconUrl;
    private String _strDocumentationUrl;
    private boolean _bIsInstalled;
    private boolean _bDbPoolRequired;
    private List<String> _listCssStyleSheets = new ArrayList<String>(  );
    private List<String> _listJavascriptFiles = new ArrayList<String>(  );
    private List<Right> _listRights = new ArrayList<Right>(  );
    private List<PortletType> _listPortletTypes = new ArrayList<PortletType>(  );
    private List<DaemonEntry> _listDaemons = new ArrayList<DaemonEntry>(  );
    private List<XPageApplicationEntry> _listApplications = new ArrayList<XPageApplicationEntry>(  );
    private List<FilterEntry> _listFilters = new ArrayList<FilterEntry>(  );
    private List<ContentServiceEntry> _listContentServices = new ArrayList<ContentServiceEntry>(  );
    private List<SearchIndexerEntry> _listSearchIndexers = new ArrayList<SearchIndexerEntry>(  );
    private List<InsertService> _listInsertServices = new ArrayList<InsertService>(  );
    private List<RBACResourceTypeEntry> _listRBACResourceTypes = new ArrayList<RBACResourceTypeEntry>(  );
    private List<PageIncludeEntry> _listPageIncludes = new ArrayList<PageIncludeEntry>(  );
    private List<DashboardComponentEntry> _listDashboardComponents = new ArrayList<DashboardComponentEntry>(  );
    private Map<String, String> _mapParams = new HashMap<String, String>(  );
    private String _strSearchIndexerClass;

    /**
     * Load plugin data from the XML file using Jakarta Commons Digester
     * @param strFilename The XML plugin filename
     * @throws fr.paris.lutece.portal.service.init.LuteceInitException If a problem occured during the loading
     */
    public void load( String strFilename ) throws LuteceInitException
    {
        // Configure Digester from XML ruleset
        URL rules = getClass(  ).getResource( FILE_RULES );
        Digester digester = DigesterLoader.createDigester( rules );

        // Push empty List onto Digester's Stack
        digester.push( this );
        digester.setValidating(false);

        try
        {
            InputStream input = new FileInputStream( strFilename );
            digester.parse( input );
        }
        catch ( FileNotFoundException e )
        {
            throw new LuteceInitException( "Error loading plugin file : " + strFilename, e );
        }
        catch ( SAXException e )
        {
            throw new LuteceInitException( "Error loading plugin file : " + strFilename, e );
        }
        catch ( IOException e )
        {
            throw new LuteceInitException( "Error loading plugin file : " + strFilename, e );
        }
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
     * Sets the name of the plugin to the specified string.
     *
     * @param strName The name of the plugin
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
     * Sets the version of the plugin to the specified string.
     *
     * @param strVersion The version of the plugin
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
     * Sets the description of the plugin to the specified string.
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
     * Sets the provider of the plugin to the specified string.
     *
     * @param strProvider The provider
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
     * Sets the provider url to the specified string.
     *
     * @param strProviderUrl The url of the provider
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
     * Sets the url of the icon of the plugin to the specified string.
     *
     * @param strIconUrl The url of the icon
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
     * Sets the url of the Documentation of the plugin to the specified string.
     *
     * @param strDocumentationUrl the url of the documentation
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
     * Sets the copyright of the plugin to the specified string.
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
    public String getPluginClass(  )
    {
        return _strPluginClass;
    }

    /**
     * Sets the class name of the plugin to the specified string.
     *
     * @param strPluginClass The name of the class
     */
    public void setPluginClass( String strPluginClass )
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
     * Sets the boolean wich shows if the plugin is installed
     *
     * @param bIsInstalled The installed boolean
     */
    public void setIsInstalled( boolean bIsInstalled )
    {
        _bIsInstalled = bIsInstalled;
    }

    /**
     * Add an CSS stylesheet to the plugin definition
     * @param strStyleSheet The StyleSheet path
     */
    public void addCssStyleSheet( String strStyleSheet )
    {
        _listCssStyleSheets.add( strStyleSheet );
    }

    /**
     * Returns all CSS Style Sheets of the plugin
     * @return The list of CSS Style Sheets
     */
    public List<String> getCssStyleSheets(  )
    {
        return _listCssStyleSheets;
    }

    /**
     * Add an Javascript File to the plugin definition
     * @param strJavascriptFile The Javascript File path
     */
    public void addJavascriptFile( String strJavascriptFile )
    {
        _listJavascriptFiles.add( strJavascriptFile );
    }

    /**
     * Returns all Javascript File of the plugin
     * @return The list of Javascript File
     */
    public List<String> getJavascriptFiles(  )
    {
        return _listJavascriptFiles;
    }

    /**
     * Add an AdminFeature Right to the plugin definition
     * @param right The Right to Add
     */
    public void addRight( Right right )
    {
        _listRights.add( right );
    }

    /**
     * Returns right list of the plugin
     *
     * @return the list of rights
     */
    public List<Right> getRights(  )
    {
        // Initialize plugin name attribute of all rights (not made by digester)
        for ( Right right : _listRights )
        {
            right.setPluginName( _strName );
        }

        return _listRights;
    }

    /**
     * Add an Application to the plugin definition
     * @param application The application to Add
     */
    public void addXPageApplication( XPageApplicationEntry application )
    {
        _listApplications.add( application );
    }

    /**
     * Returns application list of the plugin
     *
     * @return the list of applications
     */
    public List<XPageApplicationEntry> getXPageApplications(  )
    {
        return _listApplications;
    }

    /**
     * Add a filter to the plugin definition
     * @param entry The filter entry
     */
    public void addFilter( FilterEntry entry )
    {
        _listFilters.add( entry );
    }

    /**
     * Returns filter list of the plugin
     *
     * @return the list of filters
     */
    public List<FilterEntry> getFilters(  )
    {
        return _listFilters;
    }

    /**
     * Add a portlet type to the plugin definition
     * @param portletType a portlet type to the plugin definition
     */
    public void addPortletType( PortletType portletType )
    {
        _listPortletTypes.add( portletType );
    }

    /**
     * Returns the portlet types list of the plugin
     * @return the portlet types list
     */
    public List<PortletType> getPortletTypes(  )
    {
        // Initialize plugin name attribute of all rights (not made by digester)
        for ( PortletType portletType : _listPortletTypes )
        {
            portletType.setPluginName( _strName );
        }

        return _listPortletTypes;
    }

    /**
     * Add an Content Service to the plugin definition
     * @param entry The entry
     */
    public void addContentService( ContentServiceEntry entry )
    {
        _listContentServices.add( entry );
    }

    /**
     * Returns all Content Services of the plugin
     * @return The list of Content Services
     */
    public List<ContentServiceEntry> getContentServices(  )
    {
        return _listContentServices;
    }

    /**
     * Add an Insert Service to the plugin definition
     * @param is The Insert Service
     */
    public void addInsertService( InsertService is )
    {
        _listInsertServices.add( is );
    }

    /**
     * Returns all Insert Services of the plugin
     * @return The list of Insert Services
     */
    public List<InsertService> getInsertServices(  )
    {
        return _listInsertServices;
    }

    /**
     * Add a SearchIndexer to the plugin definition
     * @param entry The Search Indexer Entry
     */
    public void addSearchIndexer( SearchIndexerEntry entry )
    {
        _listSearchIndexers.add( entry );
    }

    /**
     * Returns all Search Indexer of the plugin
     * @return The list of Search Indexers
     */
    public List<SearchIndexerEntry> getSearchIndexers(  )
    {
        return _listSearchIndexers;
    }

    /**
     * Add an Page Include to the plugin definition
     * @param entry The Page Include Entry
     */
    public void addPageInclude( PageIncludeEntry entry )
    {
        _listPageIncludes.add( entry );
    }

    /**
     * Returns all Page Include Services of the plugin
     * @return The list of Page Include Services
     */
    public List<PageIncludeEntry> getPageIncludes(  )
    {
        return _listPageIncludes;
    }

    /**
     * Add an Dashboard Component to the plugin definition
     * @param entry The Dashboard Component Entry
     */
    public void addDashboardComponent( DashboardComponentEntry entry )
    {
        _listDashboardComponents.add( entry );
    }

    /**
     * Returns all Dashboard Component Services of the plugin
     * @return The list of Dashboard Component Services
     */
    public List<DashboardComponentEntry> getDashboardComponents(  )
    {
        return _listDashboardComponents;
    }

    /**
     * Add an RBAC Resource Type to the plugin definition
     * @param entry The RBACResourceType
     */
    public void addRBACResourceType( RBACResourceTypeEntry entry )
    {
        _listRBACResourceTypes.add( entry );
    }

    /**
     * Returns all RBAC Resource Types of the plugin
     * @return The list of RBACResourceType
     */
    public List<RBACResourceTypeEntry> getRBACResourceTypes(  )
    {
        return _listRBACResourceTypes;
    }

    /**
     * Add a Daemon to the plugin definition
     * @param daemonEntry  The daemon entry to add
     */
    public void addDaemon( DaemonEntry daemonEntry )
    {
        _listDaemons.add( daemonEntry );
    }

    /**
     * Returns all Daemons of the plugin
     * @return The list of Daemons
     */
    public List<DaemonEntry> getDaemons(  )
    {
        return _listDaemons;
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
     * Sets the boolean which shows if a pool is required for the plugin
     *
     * @param bDbPoolRequired The required boolean
     */
    public void setIsDbPoolRequired( boolean bDbPoolRequired )
    {
        _bDbPoolRequired = bDbPoolRequired;
    }

    /**
     * Gets plugin parameters defined in the XML file
     * @return The hashtable of the parameters
     */
    public Map<String, String> getParams(  )
    {
        return _mapParams;
    }

    /**
     * Add a parameter to the plugin definition
     * @param strName The parameter name
     * @param strValue The parameter value
     */
    public void addParameter( String strName, String strValue )
    {
        _mapParams.put( strName, strValue );
    }

    /**
     * Returns the SearchIndexer Class of the plugin
     *
     * @return the Class as a String
     * @since 2.0.0
     */
    public String getSearchIndexerClass(  )
    {
        return _strSearchIndexerClass;
    }

    /**
     * Sets the class service of plugin
     * @since 2.0.0
     * @param strSearchIndexerClass The PageInclude Class name
     */
    public void setSearchIndexerClass( String strSearchIndexerClass )
    {
        _strSearchIndexerClass = strSearchIndexerClass;
    }
}
