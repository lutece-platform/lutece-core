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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.dashboard.DashboardService;
import fr.paris.lutece.portal.service.dashboard.IDashboardComponent;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage admin features ( manage, create, modify, remove)
 */
public class AdminMenuJspBean implements Serializable
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String PROPERTY_LOGOUT_URL = "lutece.admin.logout.url";
    public static final String PROPERTY_MENU_DEFAULT_POS = "top";
    public static final String PROPERTY_MENU_DATASTORE_POS = "portal.site.site_property.menu.position";
    private static final long serialVersionUID = -8939026727319948581L;

    // Markers
    private static final String MARK_FEATURE_GROUP_LIST = "feature_group_list";
    private static final String MARK_LANGUAGES_LIST = "languages_list";
    private static final String MARK_CURRENT_LANGUAGE = "current_language";
    private static final String MARK_USER = "user";
    private static final String MARK_ADMIN_URL = "admin_url";
    private static final String MARK_ADMIN_LOGOUT_URL = "admin_logout_url";
    private static final String MARK_ADMIN_SUMMARY_DOCUMENTATION_URL = "admin_summary_documentation_url";
    private static final String MARK_SITE_NAME = "site_name";
    private static final String MARK_MENU_POS = "menu_pos";
    private static final String MARK_MODIFY_PASSWORD_URL = "url_modify_password";
    private static final String MARK_DASHBOARD_ZONE = "dashboard_zone_";
    private static final String MARK_URL_CSS = "css_url";
    private static final String MARK_JAVASCRIPT_FILE = "javascript_file";
    private static final String MARK_JAVASCRIPT_FILES = "javascript_files";
    private static final String MARK_PLUGIN_NAME = "plugin_name";
    private static final String MARK_ADMIN_AVATAR = "adminAvatar";

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
    private static final String PROPERTY_DOCUMENTATION_SUMMARY_URL = "lutece.documentation.summary.url";
    private static final String PROPERTY_DASHBOARD_ZONES = "lutece.dashboard.zones.count";
    private static final int PROPERTY_DASHBOARD_ZONES_DEFAULT = 4;
    private static final String REFERER = "referer";

    // Jsp
    private static final String PROPERTY_JSP_URL_ADMIN_LOGOUT = "lutece.admin.logout.url";
    private static final String MESSAGE_CONTROL_PASSWORD_NO_CORRESPONDING = "portal.users.message.password.confirm.error";
    private static final String PASSWORD_ERROR = "portal.users.message.password.wrong.current";
    private static final String PASSWORD_CURRENT_ERROR = "portal.users.message.password.new.equals.current";
    private static final String MESSAGE_PASSWORD_REDIRECT = "portal.users.message.password.ok.redirect";
    
    private static String _strStylesheets;
    private static String _strJavascripts;
    private boolean _bAdminAvatar = PluginService.isPluginEnable( "adminavatar" );

    /**
     * Returns the Administration header menu
     *
     * @param request The HttpServletRequest
     * @return The html code of the header
     */
    public String getAdminMenuHeader( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        String strVersion = AppInfo.getVersion(  );
        String strSiteName = PortalService.getSiteName(  );
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = user.getLocale(  );
        List<FeatureGroup> aFeaturesGroupList = getFeatureGroupsList( user );

        // Displays the menus accroding to the rights of the users
        model.put( Markers.VERSION, strVersion );
        model.put( MARK_SITE_NAME, strSiteName );
        model.put( MARK_MENU_POS,
            DatastoreService.getInstanceDataValue( PROPERTY_MENU_DATASTORE_POS, PROPERTY_MENU_DEFAULT_POS ) );
        model.put( MARK_FEATURE_GROUP_LIST, aFeaturesGroupList );
        model.put( MARK_ADMIN_URL, AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl(  ) );
        model.put( MARK_USER, user );
        model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( locale ) );
        model.put( MARK_CURRENT_LANGUAGE, locale.getLanguage(  ) );

        String strLogoutUrl = AppPropertiesService.getProperty( PROPERTY_LOGOUT_URL );
        model.put( MARK_ADMIN_LOGOUT_URL, ( strLogoutUrl == null ) ? "" : strLogoutUrl );

        String strDocumentationUrl = AppPropertiesService.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL );
        model.put( MARK_ADMIN_SUMMARY_DOCUMENTATION_URL, ( strDocumentationUrl == null ) ? null : strDocumentationUrl );

        int nZoneMax = AppPropertiesService.getPropertyInt( PROPERTY_DASHBOARD_ZONES, PROPERTY_DASHBOARD_ZONES_DEFAULT );
        setDashboardData( model, user, request, nZoneMax );
        
        model.put( MARK_ADMIN_AVATAR , _bAdminAvatar );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_MENU_HEADER, user.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the Administration footer menu
     *
     * @param request The HttpServletRequest
     * @return The html code of the header
     */
    public String getAdminMenuFooter( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );
        String strFooterVersion = AppInfo.getVersion(  );
        String strFooterSiteName = PortalService.getSiteName(  );
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = ( user != null ) ? user.getLocale(  ) : LocaleService.getDefault(  );
        model.put( Markers.VERSION, strFooterVersion );
        model.put( MARK_SITE_NAME, strFooterSiteName );
        model.put( MARK_JAVASCRIPT_FILES, getAdminJavascripts(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_MENU_FOOTER, locale, model );

        return template.getHtml(  );
    }

    /**
     * Returns the html code of the menu of the users
     * @param request The Http request
     * @return The html code of the users menu
     */
    public String getAdminMenuUser( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        Locale locale = user.getLocale(  );

        // Displays the menus according to the users rights
        List<FeatureGroup> listFeatureGroups = getFeatureGroupsList( user );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( MARK_FEATURE_GROUP_LIST, listFeatureGroups );
        model.put( MARK_USER, user );
        model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( locale ) );
        model.put( MARK_CURRENT_LANGUAGE, locale.getLanguage(  ) );
        model.put( MARK_MODIFY_PASSWORD_URL, AdminAuthenticationService.getInstance(  ).getChangePasswordPageUrl(  ) );

        setDashboardData( model, user, request );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_HOME, locale, model );

        return template.getHtml(  );
    }

    /**
     * Add dashboard data to the template's model
     * @param model The template's model
     * @param user The Admin User
     * @param request HttpServletRequest
     */
    private void setDashboardData( Map<String, Object> model, AdminUser user, HttpServletRequest request )
    {
        List<IDashboardComponent> listDashboards = DashboardService.getInstance(  ).getDashboards( user, request );
        int nZoneMax = AppPropertiesService.getPropertyInt( PROPERTY_DASHBOARD_ZONES, PROPERTY_DASHBOARD_ZONES_DEFAULT );

        if ( ( listDashboards != null ) && ( listDashboards.size(  ) > 0 ) )
        {
            int nColumnCount = DashboardService.getInstance(  ).getColumnCount(  );

            // Personnalized dashboards for the nColumnCount first zones
            for ( int i = 1; i <= nColumnCount; i++ )
            {
                model.put( MARK_DASHBOARD_ZONE + i,
                    DashboardService.getInstance(  ).getDashboardData( listDashboards, user, i, request ) );
            }

            // Default dashboards for the nColumnCount to nZoneMax zones
            for ( int i = nColumnCount + 1; i < nZoneMax; i++ )
            {
                model.put( MARK_DASHBOARD_ZONE + i,
                    DashboardService.getInstance(  ).getDashboardData( user, i, request ) );
            }
        }
        else
        {
            for ( int i = 1; i < nZoneMax; i++ )
            {
                model.put( MARK_DASHBOARD_ZONE + i,
                    DashboardService.getInstance(  ).getDashboardData( user, i, request ) );
            }
        }
    }

    /**
     * Add a specific dashboard data to the template's model
     * @param model The template's model
     * @param user The Admin User
     * @param request HttpServletRequest
     * @param nDashboardZone the dashboard zone
     */
    private void setDashboardData( Map<String, Object> model, AdminUser user, HttpServletRequest request,
        int nDashboardZone )
    {
        model.put( MARK_DASHBOARD_ZONE + nDashboardZone,
            DashboardService.getInstance(  ).getDashboardData( user, nDashboardZone, request ) );
    }

    /**
     * Returns an array that contains all feature groups corresponding to the user
     *
     * @param user The Admin user
     * @return An array of FeatureGroup objects
     */
    private List<FeatureGroup> getFeatureGroupsList( AdminUser user )
    {
        // structure that will be returned
        ArrayList<FeatureGroup> aOutFeatureGroupList = new ArrayList<FeatureGroup>(  );

        // get the list of user's features
        Map<String, Right> featuresMap = user.getRights(  );
        List<Right> features = new ArrayList<Right>( featuresMap.values(  ) );

        List<Right> rightsToDelete = new ArrayList<Right>(  );

        //delete features which have a null URL : these features does not have to be displayed in the menu
        for ( Right right : features )
        {
            if ( right.getUrl(  ) == null )
            {
                rightsToDelete.add( right );
            }
        }

        features.removeAll( rightsToDelete );

        Collections.sort( features );

        // for each group, load the features
        for ( FeatureGroup featureGroup : FeatureGroupHome.getFeatureGroupsList(  ) )
        {
            ArrayList<Right> aLeftFeatures = new ArrayList<Right>(  );

            for ( Right right : features )
            {
                right.setLocale( user.getLocale(  ) );
                right.setIconUrl( getFeatureIcon( right ) );

                String strFeatureGroup = right.getFeatureGroup(  );

                if ( featureGroup.getId(  ).equalsIgnoreCase( strFeatureGroup ) )
                {
                    featureGroup.addFeature( right );
                }
                else
                {
                    aLeftFeatures.add( right );
                }
            }

            if ( !featureGroup.isEmpty(  ) )
            {
                featureGroup.setLocale( user.getLocale(  ) );
                aOutFeatureGroupList.add( featureGroup );
            }

            features = aLeftFeatures;
        }

        // add the features with no group to the last group
        if ( aOutFeatureGroupList.size(  ) > 0 )
        {
            FeatureGroup lastFeatureGroup = aOutFeatureGroupList.get( aOutFeatureGroupList.size(  ) - 1 );

            for ( Right right : features )
            {
                lastFeatureGroup.addFeature( right );

                // FIXME ????         itFeatures.remove(  );
            }
        }

        return aOutFeatureGroupList;
    }

    /**
     * Change the current language of the user
     *
     * @param request The HTTP request
     * @return The forward Url
     */
    public String doChangeLanguage( HttpServletRequest request )
    {
        String strLanguage = request.getParameter( PARAMETER_LANGUAGE );
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = new Locale( strLanguage );
        user.setLocale( locale );
        AppPathService.getBaseUrl( request );

        return AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl(  );
    }

    /**
     * Gets the feature icon
     * @param right The right
     * @return The icon
     */
    private String getFeatureIcon( Right right )
    {
        String strIconUrl = AppPropertiesService.getProperty( PROPERTY_DEFAULT_FEATURE_ICON );

        if ( ( right.getIconUrl(  ) != null ) && ( !right.getIconUrl(  ).equals( "" ) ) )
        {
            strIconUrl = right.getIconUrl(  );
        }
        else
        {
            String strPluginName = right.getPluginName(  );
            Plugin plugin = PluginService.getPlugin( strPluginName );

            if ( plugin != null )
            {
                strIconUrl = plugin.getIconUrl(  );
            }
        }

        return strIconUrl;
    }

    /**
     * Display the modification form for user password.
     * This is used only by the default module.
     * For other modules, custom implementation should be provided.
     * @param request the http request
     * @return the form allowing the modification of the user's password
     */
    public String getModifyDefaultAdminUserPassword( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );
        Locale locale = user.getLocale(  );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PASSWORD_DEFAULT_MODULE, locale );

        return template.getHtml(  );
    }

    /**
     * Perform the user password modification.
     * This is used only by the default module.
     * For other modules, custom implementation should be provided.
     * @param request the http request
     * @return the form allowing the modification of the user's password
     */
    public String doModifyDefaultAdminUserPassword( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        String strCurrentPassword = request.getParameter( Parameters.PASSWORD_CURRENT );
        strCurrentPassword = StringUtil.replaceAccent( strCurrentPassword );

        String strNewPassword = request.getParameter( Parameters.NEW_PASSWORD );
        strNewPassword = StringUtil.replaceAccent( strNewPassword );

        String strConfirmNewPassword = request.getParameter( Parameters.CONFIRM_NEW_PASSWORD );
        strConfirmNewPassword = StringUtil.replaceAccent( strConfirmNewPassword );

        LuteceDefaultAdminUser userStored = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId(  ) );

        String strPassword = userStored.getPassword(  );
        strPassword = StringUtil.replaceAccent( strPassword );

        // Mandatory Fields
        if ( strCurrentPassword.equals( "" ) || strNewPassword.equals( "" ) || strConfirmNewPassword.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Test the difference between the two fields of new password
        if ( !strNewPassword.equals( strConfirmNewPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CONTROL_PASSWORD_NO_CORRESPONDING,
                AdminMessage.TYPE_STOP );
        }

        String strUrl = AdminUserService.checkPassword( request, strNewPassword, user.getUserId(  ) );

        if ( ( strUrl != null ) && !StringUtils.isEmpty( strUrl ) )
        {
            return strUrl;
        }

        // Encryption password
        strNewPassword = AdminUserService.encryptPassword( strNewPassword );
        strCurrentPassword = AdminUserService.encryptPassword( strCurrentPassword );

        // Test of the value of the current password
        if ( !strCurrentPassword.equals( strPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, PASSWORD_ERROR, AdminMessage.TYPE_STOP );
        }

        // Test of control of difference between the new password and the current one
        if ( strPassword.equals( strNewPassword ) )
        {
            return AdminMessageService.getMessageUrl( request, PASSWORD_CURRENT_ERROR, AdminMessage.TYPE_STOP );
        }

        // Successful tests
        userStored.setPassword( strNewPassword );
        userStored.setPasswordReset( Boolean.FALSE );
        userStored.setPasswordMaxValidDate( AdminUserService.getPasswordMaxValidDate(  ) );
        AdminUserHome.update( userStored );
        AdminUserHome.insertNewPasswordInHistory( strNewPassword, userStored.getUserId(  ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_PASSWORD_REDIRECT,
            AppPropertiesService.getProperty( PROPERTY_JSP_URL_ADMIN_LOGOUT ), AdminMessage.TYPE_INFO );
    }

    /**
     * Change the mode accessibility
     * @param request {@link HttpServletRequest}
     * @return The forward Url
     */
    public String doModifyAccessibilityMode( HttpServletRequest request )
    {
        AdminUser user = AdminUserService.getAdminUser( request );

        if ( user != null )
        {
            boolean bIsAccessible = !user.getAccessibilityMode(  );
            user.setAccessibilityMode( bIsAccessible );
            AdminUserHome.update( user );
        }

        String strReferer = request.getHeader( REFERER );

        if ( StringUtils.isNotBlank( strReferer ) )
        {
            return strReferer;
        }

        return AppPathService.getBaseUrl( request ) + AppPathService.getAdminMenuUrl(  );
    }

    /**
     * Return the stylesheets block to include in the footer
     * @return the stylesheets files block to include in the footer
     * @since 5.1
     */
    public String getAdminStyleSheets(  )
    {
        if ( _strStylesheets == null )
        {
            StringBuilder sbCssLinks = new StringBuilder(  );
            List<Plugin> listPlugins = new ArrayList<Plugin>(  );
            listPlugins.add( PluginService.getCore(  ) );
            listPlugins.addAll( PluginService.getPluginList(  ) );

            for ( Plugin plugin : listPlugins )
            {
                if ( plugin.getAdminCssStyleSheets(  ) != null )
                {
                    for ( String strStyleSheet : plugin.getAdminCssStyleSheets(  ) )
                    {
                        Map<String, Object> model = new HashMap<String, Object>(  );
                        model.put( MARK_URL_CSS, strStyleSheet );
                        model.put( MARK_PLUGIN_NAME, plugin.getName(  ) );
                        sbCssLinks.append( AppTemplateService.getTemplate( TEMPLATE_STYLESHEET_LINK,
                                LocaleService.getDefault(  ), model ).getHtml(  ) );
                    }
                }
            }

            _strStylesheets = sbCssLinks.toString(  );
        }

        return _strStylesheets;
    }

    /**
     * Return the javascript files block to include in the footer
     * @return the javascript files block to include in the footer
     * @since 5.1
     */
    private String getAdminJavascripts(  )
    {
        if ( _strJavascripts == null )
        {
            StringBuilder sbJavascripts = new StringBuilder(  );
            List<Plugin> listPlugins = new ArrayList<Plugin>(  );
            listPlugins.add( PluginService.getCore(  ) );
            listPlugins.addAll( PluginService.getPluginList(  ) );

            for ( Plugin plugin : listPlugins )
            {
                if ( plugin.getAdminJavascriptFiles(  ) != null )
                {
                    for ( String strJavascript : plugin.getAdminJavascriptFiles(  ) )
                    {
                        Map<String, Object> model = new HashMap<String, Object>(  );
                        model.put( MARK_JAVASCRIPT_FILE, strJavascript );
                        model.put( MARK_PLUGIN_NAME, plugin.getName(  ) );
                        sbJavascripts.append( AppTemplateService.getTemplate( TEMPLATE_JAVASCRIPT_FILE,
                                LocaleService.getDefault(  ), model ).getHtml(  ) );
                    }
                }
            }

            _strJavascripts = sbJavascripts.toString(  );
        }

        return _strJavascripts;
    }
}
