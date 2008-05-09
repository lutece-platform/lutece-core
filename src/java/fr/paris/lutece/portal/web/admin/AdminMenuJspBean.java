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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.FeatureGroupHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.init.AppInfo;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage admin features ( manage, create, modify, remove)
 */
public class AdminMenuJspBean
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants

    // Markers
    private static final String MARK_FEATURE_LIST = "feature_list";
    private static final String MARK_FEATURE_GROUP_LIST = "feature_group_list";
    private static final String MARK_LANGUAGES_LIST = "languages_list";
    private static final String MARK_CURRENT_LANGUAGE = "current_language";
    private static final String MARK_USER_FIRSTNAME = "user_firstname";
    private static final String MARK_USER_LASTNAME = "user_lastname";
    private static final String MARK_ADMIN_URL = "admin_url";
    private static final String MARK_ADMIN_LOGOUT_URL = "admin_logout_url";
    private static final Object MARK_MODIFY_PASSWORD_URL = "url_modify_password";
    private static final String MARK_ADMIN_SUMMARY_DOCUMENTATION_URL = "admin_summary_documentation_url";

    // Templates
    private static final String TEMPLATE_ADMIN_HOME = "admin/user/admin_home.html";
    private static final String TEMPLATE_ADMIN_MENU_HEADER = "admin/user/admin_header.html";
    private static final String TEMPLATE_MODIFY_PASSWORD_DEFAULT_MODULE = "admin/user/modify_password_default_module.html";

    // Parameter
    private static final String PARAMETER_LANGUAGE = "language";

    // Properties
    private static final String PROPERTY_DEFAULT_FEATURE_ICON = "lutece.admin.feature.default.icon";
    private static final String PROPERTY_LOGOUT_URL = "lutece.admin.logout.url";
    private static final String PROPERTY_DOCUMENTATION_SUMMARY_URL = "lutece.documentation.summary.url";

    // Jsp
    private static final String JSP_URL_ADMIN_MENU = "jsp/admin/AdminMenu.jsp";
    private static final String MESSAGE_CONTROL_PASSWORD_NO_CORRESPONDING = "portal.users.message.password.confirm.error";
    private static final String PASSWORD_ERROR = "portal.users.message.password.wrong.current";
    private static final String PASSWORD_CURRENT_ERROR = "portal.users.message.password.new.equals.current";
    private static final String MESSAGE_PASSWORD_REDIRECT = "portal.users.message.password.ok.redirect";

    /**
     * Returns the Administration header menu
     *
     * @param request The HttpServletRequest
     * @return The html code of the header
     */
    public String getAdminMenuHeader( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        String strVersion = AppInfo.getVersion(  );
        AdminUser user = AdminUserService.getAdminUser( request );

        ArrayList aFeaturesGroupList = getFeatureGroupsList( user );

        // Displays the menus accroding to the rights of the users
        model.put( Markers.VERSION, strVersion );
        model.put( MARK_FEATURE_GROUP_LIST, aFeaturesGroupList );
        model.put( MARK_ADMIN_URL, AppPathService.getBaseUrl( request ) + JSP_URL_ADMIN_MENU );

        String strLogoutUrl = AppPropertiesService.getProperty( PROPERTY_LOGOUT_URL );
        model.put( MARK_ADMIN_LOGOUT_URL, ( strLogoutUrl == null ) ? "" : strLogoutUrl );

        String strDocumentationUrl = AppPropertiesService.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL );
        model.put( MARK_ADMIN_SUMMARY_DOCUMENTATION_URL, ( strDocumentationUrl == null ) ? null : strDocumentationUrl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_MENU_HEADER, user.getLocale(  ), model );

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

        //Displays the menus according to the users rights
        ArrayList listFeatureGroups = getFeatureGroupsList( user );
        HashMap model = new HashMap(  );

        model.put( MARK_FEATURE_GROUP_LIST, listFeatureGroups );
        model.put( MARK_USER_LASTNAME, user.getLastName(  ) );
        model.put( MARK_USER_FIRSTNAME, user.getFirstName(  ) );
        model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( locale ) );
        model.put( MARK_CURRENT_LANGUAGE, locale.getLanguage(  ) );
        model.put( MARK_MODIFY_PASSWORD_URL, AdminAuthenticationService.getInstance(  ).getChangePasswordPageUrl(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_HOME, locale, model );

        return template.getHtml(  );
    }

    /**
     * Returns an array that contains all feature groups corresponding to the user
     * 
     * @param user The Admin user
     * @return An array of FeatureGroup objects
     */
    private ArrayList getFeatureGroupsList( AdminUser user )
    {
        // structure that will be returned
        ArrayList<FeatureGroup> aOutFeatureGroupList = new ArrayList<FeatureGroup>(  );

        // get the list of user's features
        Map<String, Right> featuresMap = user.getRights(  );
        Collection<Right> features = featuresMap.values(  );

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

        return AppPathService.getBaseUrl( request ) + JSP_URL_ADMIN_MENU;
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

        // Test of control length of the password value
        /*if ( strNewPassword.length(  ) < nNbMinChar )
        {
            return PASSWORD_TOO_SMALL_ERROR;
        }*/

        // TODO : test size of new password

        // Successful tests
        userStored.setPassword( strNewPassword );
        AdminUserHome.update( userStored );

        return AdminMessageService.getMessageUrl( request, MESSAGE_PASSWORD_REDIRECT, "jsp/admin/DoAdminLogout.jsp",
            AdminMessage.TYPE_INFO );
    }
}
