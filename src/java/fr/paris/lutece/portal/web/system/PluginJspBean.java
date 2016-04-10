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
package fr.paris.lutece.portal.web.system;

import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage the lutece plugins
 * (install, enable, disable)
 */
public class PluginJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RIGHT_MANAGE_PLUGINS = "CORE_PLUGINS_MANAGEMENT";
    private static final long serialVersionUID = -9058113426951331118L;
    private static final String TEMPLATE_MANAGE_PLUGINS = "admin/system/manage_plugins.html";
    private static final String MARK_PLUGINS_LIST = "plugins_list";
    private static final String MARK_CORE = "core";
    private static final String MARK_POOLS_LIST = "pools_list";
    private static final String MARK_FILTER_LIST = "filter_list";
    private static final String MARK_CURRENT_FILTER = "current_filter";
    private static final String PROPERTY_PLUGIN_MESSAGE = "portal.system.message.confirmDisable";
    private static final String PROPERTY_PLUGIN_PORTLET_EXIST_MESSAGE = "portal.system.message.portletExist";
    private static final String PROPERTY_PLUGIN_NO_CORE_COMPATIBILITY_MESSAGE = "portal.system.message.noCoreCompatibility";
    private static final String PROPERTY_PLUGIN_INSTALL_ERROR = "portal.system.message.installError";
    private static final String PARAM_PLUGIN_NAME = "plugin_name";
    private static final String PARAM_PLUGIN_TYPE = "plugin_type";
    private static final String PARAM_DB_POOL_NAME = "db_pool_name";
    private static final String PARAM_PLUGIN_TYPE_ALL = "all";
    private static final String PARAM_PLUGIN_TYPE_PORTLET = "portlet";
    private static final String PARAM_PLUGIN_TYPE_APPLICATION = "application";
    private static final String PARAM_PLUGIN_TYPE_FEATURE = "feature";
    private static final String PARAM_PLUGIN_TYPE_INSERTSERVICE = "insertservice";
    private static final String PARAM_PLUGIN_TYPE_CONTENTSERVICE = "contentservice";
    private static final String PROPERTY_PLUGIN_TYPE_NAME_ALL = "portal.system.pluginType.name.all";
    private static final String PROPERTY_PLUGIN_TYPE_NAME_APPLICATION = "portal.system.pluginType.name.application";
    private static final String PROPERTY_PLUGIN_TYPE_NAME_PORTLET = "portal.system.pluginType.name.portlet";
    private static final String PROPERTY_PLUGIN_TYPE_NAME_FEATURE = "portal.system.pluginType.name.feature";
    private static final String PROPERTY_PLUGIN_TYPE_NAME_INSERTSERVICE = "portal.system.pluginType.name.insertService";
    private static final String PROPERTY_PLUGIN_TYPE_NAME_CONTENTSERVICE = "portal.system.pluginType.name.contentService";
    private static final String TEMPLATE_PLUGIN_DETAILS = "/admin/system/view_plugin.html";

    /**
     * Returns the plugins management page
     *
     * @param request The Http request
     * @return Html page
     */
    public String getManagePlugins( HttpServletRequest request )
    {
        Locale locale = AdminUserService.getLocale( request );
        String strPluginTypeFilter = request.getParameter( PARAM_PLUGIN_TYPE );
        Collection<Plugin> listPlugins = PluginService.getPluginList(  );
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_PLUGINS_LIST, filterPluginsList( listPlugins, strPluginTypeFilter ) );
        model.put( MARK_CORE, PluginService.getCore( ) );
        model.put( MARK_POOLS_LIST, getPoolsList(  ) );
        model.put( MARK_FILTER_LIST, getPluginTypeFilterList( locale ) );
        model.put( MARK_CURRENT_FILTER, ( strPluginTypeFilter != null ) ? strPluginTypeFilter : "" );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_PLUGINS, locale, model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Install a plugin
     *
     * @param request The Http request
     * @param context The servlet context
     * @return the url of the page containing a log essage
     */
    public String doInstallPlugin( HttpServletRequest request, ServletContext context )
    {
        try
        {
            String strPluginName = request.getParameter( PARAM_PLUGIN_NAME );
            Plugin plugin = PluginService.getPlugin( strPluginName );

            if ( verifyCoreCompatibility( plugin ) )
            {
                plugin.install(  );
            }
            else
            {
                Object[] args = { plugin.getMinCoreVersion(  ), plugin.getMaxCoreVersion(  ) };

                return AdminMessageService.getMessageUrl( request, PROPERTY_PLUGIN_NO_CORE_COMPATIBILITY_MESSAGE, args,
                    AdminMessage.TYPE_STOP );
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );

            return AdminMessageService.getMessageUrl( request, PROPERTY_PLUGIN_INSTALL_ERROR, AdminMessage.TYPE_STOP );
        }

        return getHomeUrl( request );
    }

    /**
     * Uninstall a plugin
     *
     * @param request The Http request
     * @param context The servlet context
     * @return the url of the page containing a log essage
     */
    public String doUninstallPlugin( HttpServletRequest request, ServletContext context )
    {
        try
        {
            String strPluginName = request.getParameter( PARAM_PLUGIN_NAME );
            Plugin plugin = PluginService.getPlugin( strPluginName );
            plugin.uninstall(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return getHomeUrl( request );
    }

    /**
     * Returns the page of confirmation for uninstalling a plugin
     *
     * @param request The Http Request
     * @return the HTML page
     */
    public String getConfirmUninstallPlugin( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAM_PLUGIN_NAME );
        Plugin plugin = PluginService.getPlugin( strPluginName );
        Collection<PortletType> listPortletTypes = plugin.getPortletTypes(  );
        String strMessageKey = PROPERTY_PLUGIN_MESSAGE;
        String strUrl = "jsp/admin/system/DoUninstallPlugin.jsp?plugin_name=" + strPluginName;
        String strAdminMessageUrl = AdminMessageService.getMessageUrl( request, strMessageKey, strUrl, "",
                AdminMessage.TYPE_CONFIRMATION );

        for ( PortletType portletType : listPortletTypes )
        {
            String strPluginHomeClass = portletType.getHomeClass(  );

            if ( ( plugin.getType(  ) & Plugin.PLUGIN_TYPE_PORTLET ) != 0 )
            {
                if ( isPortletExists( strPluginHomeClass ) )
                {
                    strMessageKey = PROPERTY_PLUGIN_PORTLET_EXIST_MESSAGE;
                    strAdminMessageUrl = AdminMessageService.getMessageUrl( request, strMessageKey,
                            AdminMessage.TYPE_CONFIRMATION );
                }
            }
        }

        return strAdminMessageUrl;
    }

    /**
     * Defines the database connection pool to be used by the plugin
     * @param request The http request
     * @return the URL to redirect after this action
     */
    public String doModifyPluginPool( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAM_PLUGIN_NAME );
        String strDbPoolName = request.getParameter( PARAM_DB_POOL_NAME );

        try
        {
            Plugin plugin = PluginService.getPlugin( strPluginName );
            plugin.updatePoolName( strDbPoolName );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return getHomeUrl( request );
    }

    /**
     * Displays a plugin's description
     * @param request The HTTP request
     * @return The popup HTML code
     */
    public String getPluginDescription( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAM_PLUGIN_NAME );
        Plugin plugin;
        if ( PluginService.getCore( ).getName( ).equals( strPluginName ) )
        {
            plugin = PluginService.getCore( );
        } else
        {
            plugin = PluginService.getPlugin( strPluginName );
        }

        // set the locale for the feature labels
        I18nService.localizeCollection( plugin.getRights(  ), getLocale(  ) );
        // set the locale for the portlet types labels
        I18nService.localizeCollection( plugin.getPortletTypes(  ), getLocale(  ) );
        // set the locale for the  link services labels
        I18nService.localizeCollection( plugin.getInsertServices(  ), getLocale(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_DETAILS, getLocale(  ), plugin );

        return getAdminPage( template.getHtml(  ) );
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Return a filter list of plugins
     * @param listPlugins the COllection of plugins
     * @param strPluginTypeFilter The filter
     * @return list The list of plugins
     */
    private Collection<Plugin> filterPluginsList( Collection<Plugin> listPlugins, String strPluginTypeFilter )
    {
        Collection<Plugin> list = new ArrayList<Plugin>(  );

        for ( Plugin plugin : listPlugins )
        {
            // Checks for filtering the plugin list
            if ( strPluginTypeFilter != null )
            {
                if ( strPluginTypeFilter.equals( PARAM_PLUGIN_TYPE_APPLICATION ) &&
                        ( ( plugin.getType(  ) & Plugin.PLUGIN_TYPE_APPLICATION ) == 0 ) )
                {
                    // skip this plugin
                    continue;
                }

                if ( strPluginTypeFilter.equals( PARAM_PLUGIN_TYPE_PORTLET ) &&
                        ( ( plugin.getType(  ) & Plugin.PLUGIN_TYPE_PORTLET ) == 0 ) )
                {
                    // skip this plugin
                    continue;
                }

                if ( strPluginTypeFilter.equals( PARAM_PLUGIN_TYPE_FEATURE ) &&
                        ( ( plugin.getType(  ) & Plugin.PLUGIN_TYPE_FEATURE ) == 0 ) )
                {
                    // skip this plugin
                    continue;
                }

                if ( strPluginTypeFilter.equals( PARAM_PLUGIN_TYPE_INSERTSERVICE ) &&
                        ( ( plugin.getType(  ) & Plugin.PLUGIN_TYPE_INSERTSERVICE ) == 0 ) )
                {
                    // skip this plugin
                    continue;
                }

                if ( strPluginTypeFilter.equals( PARAM_PLUGIN_TYPE_CONTENTSERVICE ) &&
                        ( ( plugin.getType(  ) & Plugin.PLUGIN_TYPE_CONTENTSERVICE ) == 0 ) )
                {
                    // skip this plugin
                    continue;
                }
            }

            list.add( plugin );
        }

        return list;
    }

    /**
     * Create a ReferenceList containing all Plugin types
     * @param locale The Locale
     * @return A ReferenceList containing all Plugin types
     */
    private ReferenceList getPluginTypeFilterList( Locale locale )
    {
        ReferenceList list = new ReferenceList(  );
        list.addItem( PARAM_PLUGIN_TYPE_ALL, I18nService.getLocalizedString( PROPERTY_PLUGIN_TYPE_NAME_ALL, locale ) );
        list.addItem( PARAM_PLUGIN_TYPE_APPLICATION,
            I18nService.getLocalizedString( PROPERTY_PLUGIN_TYPE_NAME_APPLICATION, locale ) );
        list.addItem( PARAM_PLUGIN_TYPE_PORTLET,
            I18nService.getLocalizedString( PROPERTY_PLUGIN_TYPE_NAME_PORTLET, locale ) );
        list.addItem( PARAM_PLUGIN_TYPE_FEATURE,
            I18nService.getLocalizedString( PROPERTY_PLUGIN_TYPE_NAME_FEATURE, locale ) );
        list.addItem( PARAM_PLUGIN_TYPE_INSERTSERVICE,
            I18nService.getLocalizedString( PROPERTY_PLUGIN_TYPE_NAME_INSERTSERVICE, locale ) );
        list.addItem( PARAM_PLUGIN_TYPE_CONTENTSERVICE,
            I18nService.getLocalizedString( PROPERTY_PLUGIN_TYPE_NAME_CONTENTSERVICE, locale ) );

        return list;
    }

    /**
     * Return a list of pools
     * @return listPools the list of pools
     */
    private ReferenceList getPoolsList(  )
    {
        ReferenceList listPools = new ReferenceList(  );
        listPools.addItem( AppConnectionService.NO_POOL_DEFINED, " " );
        AppConnectionService.getPoolList( listPools );

        return listPools;
    }

    /**
     * Returns the status of the existence of a portlet on the site
     *
     * @param strPluginHomeClass The home class of the plugin
     * @return The existence status as a boolean
     */
    private boolean isPortletExists( String strPluginHomeClass )
    {
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strPluginHomeClass );

        return ( PortletTypeHome.getNbPortletTypeByPortlet( strPortletTypeId ) != 0 );
    }

    /**
     * Verify the core compatibility for a plugin
     *
     * @param plugin The plugin
     * @return true if compatible with the current core version
     */
    private boolean verifyCoreCompatibility( Plugin plugin )
    {
        String strCoreVersion = AppInfo.getVersion(  );

        // Remove version qualifier (-SNAPSHOT, -RC-XX, ...)
        int nPos = strCoreVersion.indexOf( "-" );

        if ( nPos > 0 )
        {
            strCoreVersion = strCoreVersion.substring( 0, nPos );
        }

        String[] coreVersion = strCoreVersion.split( "\\." );

        String strMinCoreVersion = ( plugin.getMinCoreVersion(  ) == null ) ? "" : plugin.getMinCoreVersion(  );
        String strMaxCoreVersion = ( plugin.getMaxCoreVersion(  ) == null ) ? "" : plugin.getMaxCoreVersion(  );

        // test the min core version
        boolean bMin = ( strMinCoreVersion == null ) || strMinCoreVersion.trim(  ).equals( "" );

        if ( ( strMinCoreVersion != null ) && !strMinCoreVersion.trim(  ).equals( "" ) )
        {
            String[] minCoreVersion = strMinCoreVersion.split( "\\." );

            if ( checkCoreMinCompatibility( minCoreVersion, coreVersion ) )
            {
                AppLogService.debug( "Min core version ok : " + plugin.getMinCoreVersion(  ) );
                bMin = true;
            }
        }

        // test the max core version
        boolean bMax = ( strMaxCoreVersion == null ) || strMaxCoreVersion.trim(  ).equals( "" );

        if ( ( strMaxCoreVersion != null ) && !strMaxCoreVersion.trim(  ).equals( "" ) )
        {
            String[] maxCoreVersion = strMaxCoreVersion.split( "\\." );

            if ( checkCoreMaxCompatibility( maxCoreVersion, coreVersion ) )
            {
                AppLogService.debug( "Max core version ok : " + plugin.getMaxCoreVersion(  ) );
                bMax = true;
            }
        }

        return bMin && bMax;
    }

    /**
     * Checks the compatibility
     * @param minCoreVersion The min core version
     * @param coreVersion The current core version
     * @return true if compatible with the current core version
     */
    private boolean checkCoreMinCompatibility( String[] minCoreVersion, String[] coreVersion )
    {
        for ( int i = 0; i < Math.min( minCoreVersion.length, coreVersion.length ); ++i )
        {
            if ( ( Integer.parseInt( minCoreVersion[i] ) ) < ( Integer.parseInt( coreVersion[i] ) ) )
            {
                return true;
            }

            if ( ( Integer.parseInt( minCoreVersion[i] ) ) > ( Integer.parseInt( coreVersion[i] ) ) )
            {
                return false;
            }
        }

        return true; //inclusive
    }

    /**
     * Checks the compatibility
     * @param maxCoreVersion The max core version
     * @param coreVersion The current core version
     * @return true if compatible with the current core version
     */
    private boolean checkCoreMaxCompatibility( String[] maxCoreVersion, String[] coreVersion )
    {
        for ( int i = 0; i < Math.min( maxCoreVersion.length, coreVersion.length ); ++i )
        {
            if ( ( Integer.parseInt( maxCoreVersion[i] ) ) > ( Integer.parseInt( coreVersion[i] ) ) )
            {
                return true;
            }

            if ( ( Integer.parseInt( maxCoreVersion[i] ) ) < ( Integer.parseInt( coreVersion[i] ) ) )
            {
                return false;
            }
        }

        return false; //exclusive
    }
}
