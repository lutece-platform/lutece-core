/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.web.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.user.menu.AccessibilityModeAdminUserMenuItemProvider;
import fr.paris.lutece.portal.business.user.menu.LanguageAdminUserMenuItemProvider;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.menu.AdminUserMenuService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;

/**
 * This class provides the user interface to manage admin features ( manage, create, modify, remove)
 */
public class AdminMenuJspBean implements Serializable
{
    // ///////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String ERROR_INVALID_TOKEN = "Invalid security token";
    public static final String PROPERTY_LOGOUT_URL = "lutece.admin.logout.url";
    public static final String PROPERTY_MENU_DEFAULT_POS = "top";
    public static final String PROPERTY_MENU_DATASTORE_POS = "portal.site.site_property.menu.position";
    private static final long serialVersionUID = -8939026727319948581L;

    // Markers
    private static final String MARK_FEATURE_GROUP_LIST = "feature_group_list";
    private static final String MARK_USER = "user";
    private static final String MARK_ADMIN_URL = "admin_url";
    private static final String MARK_PROD_BASE_URL = "prod_base_url";
    private static final String MARK_ADMIN_LOGOUT_URL = "admin_logout_url";
    private static final String MARK_SITE_NAME = "site_name";
    private static final String MARK_MENU_POS = "menu_pos";
    private static final String MARK_DASHBOARD_ZONE = "dashboard_zone_";
    private static final String MARK_JAVASCRIPT_FILE = "javascript_file";
    private static final String MARK_JAVASCRIPT_FILES = "javascript_files";
    private static final String MARK_PLUGIN_NAME = "plugin_name";
    private static final String MARK_PLUGINS_LIST = "plugins_list";
    private static final String MARK_ADMIN_AVATAR = "adminAvatar";
    private static final String MARK_MINIMUM_PASSWORD_SIZE = "minimumPasswordSize";
    private static final String MARK_USER_MENU_ITEMS = "userMenuItems";
    private static final String MARK_LIST_LOGGER_INFO = "listLoggersInfo";

    // Templates
    private static final String TEMPLATE_ADMIN_HOME = "admin/user/admin_home.html";
    private static final String TEMPLATE_ADMIN_MENU_HEADER = "admin/user/admin_header.html";
    private static final String TEMPLATE_ADMIN_MENU_FOOTER = "admin/user/admin_footer.html";
    private static final String TEMPLATE_MODIFY_PASSWORD_DEFAULT_MODULE = "admin/user/modify_password_default_module.html";
    private static final String TEMPLATE_STYLESHEET_LINK = "admin/stylesheet_link.html";
    private static final String TEMPLATE_JAVASCRIPT_FILE = "admin/javascript_file.html";

    // Parameter
    private static final String PARAMETER_LANGUAGE = "language";

    // Properties
    private static final String PROPERTY_DEFAULT_FEATURE_ICON = "lutece.admin.feature.default.icon";
    private static final String PROPERTY_DASHBOARD_ZONES = "lutece.dashboard.zones.count";
    private static final int PROPERTY_DASHBOARD_ZONES_DEFAULT = 4;
    private static final String REFERER = "referer";

    // Jsp
    private static final String PROPERTY_JSP_URL_ADMIN_LOGOUT = "lutece.admin.logout.url";
    private static final String MESSAGE_CONTROL_PASSWORD_NO_CORRESPONDING = "portal.users.message.password.confirm.error";
    private static final String PASSWORD_ERROR = "portal.users.message.password.wrong.current";
    private static final String PASSWORD_CURRENT_ERROR = "portal.users.message.password.new.equals.current";
    private static final String MESSAGE_PASSWORD_REDIRECT = "portal.users.message.password.ok.redirect";
    private static final String LOGGER_ACCESS = "lutece.adminaccess";

    private static String _strStylesheets;
    private static boolean _bResetAdminStylesheets;
    private static String _strJavascripts;
    private boolean _bAdminAvatar = PluginService.isPluginEnable( "adminavatar" );
    private static Logger _loggerAccess = LogManager.getLogger( LOGGER_ACCESS );

    /**
     * Returns the Administration header menu
     *
     * @param request
     *            The HttpServletRequest
     * @return The html code of the header
     */
    public String getAdminMenuHeader( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        String strSiteName = PortalService.getSiteName( );
        AdminUser user = AdminUserService.getAdminUser( request );
        List<FeatureGroup> aFeaturesGroupList = getFeatureGroupsList( user );

        // Displays the menus according to the rights of the users
        model.put( MARK_SITE_NAME, strSiteName );
        model.put( MARK_MENU_POS, DatastoreService.getInstanceDataValue( PROPERTY_MENU_DATASTORE_POS, PROPERTY_MENU_DEFAULT_POS ) );
        model.put( MARK_FEATURE_GROUP_LIST, aFeaturesGroupList );
        model.put( MARK_ADMIN_URL, AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl( ) );
        model.put( MARK_PROD_BASE_URL, AppPathService.getProdUrl( request ) );
        model.put( MARK_USER, user );
        if ( user.isAdmin( ) )
        {
        	model.put( MARK_LIST_LOGGER_INFO, AppLogService.getLoggersInfo( ) );
        }

        String strLogoutUrl = AppPropertiesService.getProperty( PROPERTY_LOGOUT_URL );
        model.put( MARK_ADMIN_LOGOUT_URL, ( strLogoutUrl == null ) ? "" : strLogoutUrl );

        int nZoneMax = AppPropertiesService.getPropertyInt( PROPERTY_DASHBOARD_ZONES, PROPERTY_DASHBOARD_ZONES_DEFAULT );
        setDashboardData( model, user, request, nZoneMax );

        model.put( MARK_ADMIN_AVATAR, _bAdminAvatar );
        AdminUserMenuService registry = SpringContextService.getBean( AdminUserMenuService.BEAN_NAME );
        model.put( MARK_USER_MENU_ITEMS, registry.getItems( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_MENU_HEADER, user.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Returns the Administration footer menu
     *
     * @param request
     *            The HttpServletRequest
     * @return The html code of the header
     */
    public String getAdminMenuFooter( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        String strFooterVersion = AppInfo.getVersion( );
        String strFooterSiteName = PortalService.getSiteName( );
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = ( user != null ) ? user.getLocale( ) : LocaleService.getDefault( );
        model.put( Markers.VERSION, strFooterVersion );
        model.put( MARK_SITE_NAME, strFooterSiteName );
        model.put( MARK_JAVASCRIPT_FILES, getAdminJavascripts( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_MENU_FOOTER, locale, model );

        traceAdminAccess( request );

        return template.getHtml( );
    }

    /**
     * Returns the html code of the menu of the users
     * 
     * @param request
     *            The Http request
     * @return The html code of the users menu
     */
    public String getAdminMenuUser( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        Map<String, Object> model = new HashMap<>( );

        setDashboardData( model, user, request );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_HOME, user.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Add dashboard data to the template's model
     * 
     * @param model
     *            The template's model
     * @param user
     *            The Admin User
     * @param request
     *            HttpServletRequest
     */
    private void setDashboardData( Map<String, Object> model, AdminUser user, HttpServletRequest request )
    {
        List<IDashboardComponent> listDashboards = DashboardService.getInstance( ).getDashboards( user, request );
        int nZoneMax = AppPropertiesService.getPropertyInt( PROPERTY_DASHBOARD_ZONES, PROPERTY_DASHBOARD_ZONES_DEFAULT );

        if ( CollectionUtils.isNotEmpty( listDashboards ) )
        {
            int nColumnCount = DashboardService.getInstance( ).getColumnCount( );

            // Personnalized dashboards for the nColumnCount first zones
            for ( int i = 1; i <= nColumnCount; i++ )
            {
                model.put( MARK_DASHBOARD_ZONE + i, DashboardService.getInstance( ).getDashboardData( listDashboards, user, i, request ) );
            }

            // Default dashboards for the nColumnCount to nZoneMax zones
            for ( int i = nColumnCount + 1; i < nZoneMax; i++ )
            {
                model.put( MARK_DASHBOARD_ZONE + i, DashboardService.getInstance( ).getDashboardData( user, i, request ) );
            }
        }
        else
        {
            for ( int i = 1; i < nZoneMax; i++ )
            {
                model.put( MARK_DASHBOARD_ZONE + i, DashboardService.getInstance( ).getDashboardData( user, i, request ) );
            }
        }
    }

    /**
     * Add a specific dashboard data to the template's model
     * 
     * @param model
     *            The template's model
     * @param user
     *            The Admin User
     * @param request
     *            HttpServletRequest
     * @param nDashboardZone
     *            the dashboard zone
     */
    private void setDashboardData( Map<String, Object> model, AdminUser user, HttpServletRequest request, int nDashboardZone )
    {
        model.put( MARK_DASHBOARD_ZONE + nDashboardZone, DashboardService.getInstance( ).getDashboardData( user, nDashboardZone, request ) );
    }

    /**
     * Returns an array that contains all feature groups corresponding to the user
     *
     * @param user
     *            The Admin user
     * @return An array of FeatureGroup objects
     */
    private List<FeatureGroup> getFeatureGroupsList( AdminUser user )
    {
        // structure that will be returned
        ArrayList<FeatureGroup> aOutFeatureGroupList = new ArrayList<>( );

        // get the list of user's features
        Map<String, Right> featuresMap = user.getRights( );
        List<Right> features = new ArrayList<>( featuresMap.values( ) );

        List<Right> rightsToDelete = new ArrayList<>( );

        // delete features which have a null URL : these features does not have to be displayed in the menu
        for ( Right right : features )
        {
            if ( right.getUrl( ) == null )
            {
                rightsToDelete.add( right );
            }
        }

        features.removeAll( rightsToDelete );

        Collections.sort( features );

        // for each group, load the features
        for ( FeatureGroup featureGroup : FeatureGroupHome.getFeatureGroupsList( ) )
        {
            ArrayList<Right> aLeftFeatures = new ArrayList<>( );

            for ( Right right : features )
            {
                right.setLocale( user.getLocale( ) );
                right.setIconUrl( getFeatureIcon( right ) );

                String strFeatureGroup = right.getFeatureGroup( );

                if ( featureGroup.getId( ).equalsIgnoreCase( strFeatureGroup ) )
                {
                    featureGroup.addFeature( right );
                }
                else
                {
                    aLeftFeatures.add( right );
                }
            }

            if ( !featureGroup.isEmpty( ) )
            {
                featureGroup.setLocale( user.getLocale( ) );
                aOutFeatureGroupList.add( featureGroup );
            }

            features = aLeftFeatures;
        }

        // add the features with no group to the last group
        if ( CollectionUtils.isNotEmpty( aOutFeatureGroupList ) )
        {
            FeatureGroup lastFeatureGroup = aOutFeatureGroupList.get( aOutFeatureGroupList.size( ) - 1 );

            for ( Right right : features )
            {
                lastFeatureGroup.addFeature( right );
            }
        }

        return aOutFeatureGroupList;
    }

    /**
     * Change the current language of the user
     *
     * @param request
     *            The HTTP request
     * @return The forward Url
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doChangeLanguage( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, LanguageAdminUserMenuItemProvider.TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strLanguage = request.getParameter( PARAMETER_LANGUAGE );
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = new Locale( strLanguage );
        user.setLocale( locale );
        AppPathService.getBaseUrl( request );

        return AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl( );
    }

    /**
     * Gets the feature icon
     * 
     * @param right
     *            The right
     * @return The icon
     */
    private String getFeatureIcon( Right right )
    {
        String strIconUrl = AppPropertiesService.getProperty( PROPERTY_DEFAULT_FEATURE_ICON );

        if ( ( right.getIconUrl( ) != null ) && ( !right.getIconUrl( ).equals( "" ) ) )
        {
            strIconUrl = right.getIconUrl( );
        }
        else
        {
            String strPluginName = right.getPluginName( );
            Plugin plugin = PluginService.getPlugin( strPluginName );

            if ( plugin != null )
            {
                strIconUrl = plugin.getIconUrl( );
            }
        }

        return strIconUrl;
    }

    /**
     * Display the modification form for user password. This is used only by the default module. For other modules, custom implementation should be provided.
     * 
     * @param request
     *            the http request
     * @return the form allowing the modification of the user's password
     */
    public String getModifyDefaultAdminUserPassword( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = user.getLocale( );
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_MINIMUM_PASSWORD_SIZE, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH ) );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MODIFY_PASSWORD_DEFAULT_MODULE ) );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PASSWORD_DEFAULT_MODULE, locale, model );

        return template.getHtml( );
    }

    /**
     * Perform the user password modification. This is used only by the default module. For other modules, custom implementation should be provided.
     * 
     * @param request
     *            the http request
     * @return the form allowing the modification of the user's password
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyDefaultAdminUserPassword( HttpServletRequest request ) throws AccessDeniedException
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        String strCurrentPassword = request.getParameter( Parameters.PASSWORD_CURRENT );
        String strNewPassword = request.getParameter( Parameters.NEW_PASSWORD );
        String strConfirmNewPassword = request.getParameter( Parameters.CONFIRM_NEW_PASSWORD );

        LuteceDefaultAdminUser userStored = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) );

        IPassword password = userStored.getPassword( );

        // Mandatory Fields
        if ( StringUtils.isEmpty( strCurrentPassword ) || StringUtils.isEmpty( strNewPassword ) || StringUtils.isEmpty( strConfirmNewPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Test the difference between the two fields of new password
        if ( !strNewPassword.equals( strConfirmNewPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CONTROL_PASSWORD_NO_CORRESPONDING, AdminMessage.TYPE_STOP );
        }

        String strUrl = AdminUserService.checkPassword( request, strNewPassword, user.getUserId( ) );

        if ( StringUtils.isNotEmpty( strUrl ) )
        {
            return strUrl;
        }

        // Test of the value of the current password
        if ( !password.check( strCurrentPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, PASSWORD_ERROR, AdminMessage.TYPE_STOP );
        }

        // Test of control of difference between the new password and the current one
        if ( strCurrentPassword.equals( strNewPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, PASSWORD_CURRENT_ERROR, AdminMessage.TYPE_STOP );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MODIFY_PASSWORD_DEFAULT_MODULE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        // Successful tests
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );
        userStored.setPassword( passwordFactory.getPasswordFromCleartext( strNewPassword ) );
        userStored.setPasswordReset( Boolean.FALSE );
        userStored.setPasswordMaxValidDate( AdminUserService.getPasswordMaxValidDate( ) );
        AdminUserHome.update( userStored );
        AdminUserHome.insertNewPasswordInHistory( userStored.getPassword( ), userStored.getUserId( ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_PASSWORD_REDIRECT, AppPropertiesService.getProperty( PROPERTY_JSP_URL_ADMIN_LOGOUT ),
                AdminMessage.TYPE_INFO );
    }

    /**
     * Change the mode accessibility
     * 
     * @param request
     *            {@link HttpServletRequest}
     * @return The forward Url
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doModifyAccessibilityMode( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, AccessibilityModeAdminUserMenuItemProvider.TEMPLATE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        AdminUser user = AdminUserService.getAdminUser( request );

        if ( user != null )
        {
            boolean bIsAccessible = !user.getAccessibilityMode( );
            user.setAccessibilityMode( bIsAccessible );
            AdminUserHome.update( user );
        }

        String strReferer = request.getHeader( REFERER );

        if ( StringUtils.isNotBlank( strReferer ) )
        {
            return strReferer;
        }

        return AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl( );
    }

    /**
     * Return the stylesheets block to include in the footer
     * 
     * @return the stylesheets files block to include in the footer
     * @since 5.1
     */
    public String getAdminStyleSheets( )
    {
        loadStylesheets( );
        return _strStylesheets;
    }

    private static synchronized void loadStylesheets( )
    {
        if ( _strStylesheets == null || _bResetAdminStylesheets )
        {
            List<Plugin> listPlugins = new ArrayList<>( );
            listPlugins.add( PluginService.getCore( ) );
            listPlugins.addAll( PluginService.getPluginList( ) );

            Map<String, Object> model = new HashMap<>( );
            model.put( MARK_PLUGINS_LIST, listPlugins );

            _strStylesheets = AppTemplateService.getTemplate( TEMPLATE_STYLESHEET_LINK, LocaleService.getDefault( ), model ).getHtml( );
            _bResetAdminStylesheets = false;
        }
    }

    public static void resetAdminStylesheets( )
    {
        _bResetAdminStylesheets = true;
    }

    /**
     * Return the javascript files block to include in the footer
     * 
     * @return the javascript files block to include in the footer
     * @since 5.1
     */
    private static synchronized String getAdminJavascripts( )
    {
        if ( _strJavascripts == null )
        {
            StringBuilder sbJavascripts = new StringBuilder( );
            List<Plugin> listPlugins = new ArrayList<>( );
            listPlugins.add( PluginService.getCore( ) );
            listPlugins.addAll( PluginService.getPluginList( ) );

            for ( Plugin plugin : listPlugins )
            {
                if ( plugin.getAdminJavascriptFiles( ) != null )
                {
                    for ( String strJavascript : plugin.getAdminJavascriptFiles( ) )
                    {
                        Map<String, Object> model = new HashMap<>( );
                        model.put( MARK_JAVASCRIPT_FILE, strJavascript );
                        model.put( MARK_PLUGIN_NAME, plugin.getName( ) );
                        sbJavascripts.append( AppTemplateService.getTemplate( TEMPLATE_JAVASCRIPT_FILE, LocaleService.getDefault( ), model ).getHtml( ) );
                    }
                }
            }

            _strJavascripts = sbJavascripts.toString( );
        }

        return _strJavascripts;
    }

    /**
     * Trace in a log file URL accessed by the current admin user
     * 
     * @param request
     *            The HTTP request
     */
    private void traceAdminAccess( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );
        if ( user != null )
        {
            StringBuilder sbAccessLog = new StringBuilder( );
            sbAccessLog.append( "USER id:" ).append( user.getUserId( ) ).append( ", name: " ).append( user.getFirstName( ) ).append( " " )
                    .append( user.getLastName( ) ).append( ", ip: " ).append( request.getRemoteAddr( ) ).append( ", url: " ).append( request.getScheme( ) )
                    .append( "://" ).append( request.getServerName( ) ).append( ':' ).append( request.getServerPort( ) ).append( request.getRequestURI( ) );
            String strQuery = request.getQueryString( );
            if ( strQuery != null )
            {
                sbAccessLog.append( "?" ).append( strQuery );
            }
            _loggerAccess.info( sbAccessLog.toString( ) );

        }
    }
}
