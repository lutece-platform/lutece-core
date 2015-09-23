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
package fr.paris.lutece.portal.service.admin;

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.regularexpression.RegularExpression;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserFilter;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldFilter;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.regularexpression.RegularExpressionService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.CryptoService;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.password.PasswordUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;


/**
 * This service provides features concerning the administration users
 */
public final class AdminUserService
{
    // DataStore keys
    public static final String DSKEY_ACCOUNT_REACTIVATED_MAIL_SENDER = "core.advanced_parameters.account_reactivated_mail_sender";
    public static final String DSKEY_ACCOUNT_REACTIVATED_MAIL_SUBJECT = "core.advanced_parameters.account_reactivated_mail_subject";
    public static final String DSKEY_ACCOUNT_REACTIVATED_MAIL_BODY = "core_account_reactivated_mail";
    public static final String DSKEY_PASSWORD_DURATION = "core.advanced_parameters.password_duration";
    public static final String DSKEY_MAXIMUM_NUMBER_PASSWORD_CHANGE = "core.advanced_parameters.maximum_number_password_change";
    public static final String DSKEY_PASSWORD_HISTORY_SIZE = "core.advanced_parameters.password_history_size";
    public static final String DSKEY_TSW_SIZE_PASSWORD_CHANGE = "core.advanced_parameters.tsw_size_password_change";
    public static final String DSKEY_NOTIFY_USER_PASSWORD_EXPIRED = "core.advanced_parameters.notify_user_password_expired";
    public static final String DSKEY_BANNED_DOMAIN_NAMES = "banned_domain_names";
    public static final String DSKEY_ACCOUNT_LIFE_TIME = "core.advanced_parameters.account_life_time";
    public static final String DSKEY_TIME_BEFORE_ALERT_ACCOUNT = "core.advanced_parameters.time_before_alert_account";
    public static final String DSKEY_NB_ALERT_ACCOUNT = "core.advanced_parameters.nb_alert_account";
    public static final String DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT = "core.advanced_parameters.time_between_alerts_account";
    public static final String DSKEY_ACCES_FAILURES_MAX = "core.advanced_parameters.access_failures_max";
    public static final String DSKEY_ACCES_FAILURES_INTERVAL = "core.advanced_parameters.access_failures_interval";
    public static final String DSKEY_EMAIL_PATTERN = "core.advanced_parameters.email_pattern";
    public static final String DSKEY_EMAIL_PATTERN_VERIFY_BY = "core.advanced_parameters.email_pattern_verify_by";
    public static final String DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS = "core.advanced_parameters.password_format_special_characters";
    public static final String DSKEY_PASSWORD_FORMAT_NUMERO = "core.advanced_parameters.password_format_numero";
    public static final String DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE = "core.advanced_parameters.password_format_upper_lower_case";
    public static final String DSKEY_FORCE_CHANGE_PASSWORD_REINIT = "core.advanced_parameters.force_change_password_reinit";
    public static final String DSKEY_PASSWORD_MINIMUM_LENGTH = "core.advanced_parameters.password_minimum_length";
    public static final String DSKEY_DEFAULT_USER_STATUS = "core.advanced_parameters.default_user_status";
    public static final String DSKEY_DEFAULT_USER_LANGUAGE = "core.advanced_parameters.default_user_language";
    public static final String DSKEY_DEFAULT_USER_NOTIFICATION = "core.advanced_parameters.default_user_notification";
    public static final String DSKEY_DEFAULT_USER_LEVEL = "core.advanced_parameters.default_user_level";
    public static final String DSKEY_ENABLE_PASSWORD_ENCRYPTION = "core.advanced_parameters.enable_password_encryption";
    public static final String DSKEY_ENCRYPTION_ALGORITHM = "core.advanced_parameters.encryption_algorithm";
    public static final String DSKEY_USE_ADVANCED_SECURITY_PARAMETERS = "core.advanced_parameters.use_advanced_security_parameters";

    // Parameter
    private static final String PARAMETER_ACCESS_CODE = "access_code";
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";

    // Markers
    private static final String MARK_DEFAULT_USER_LEVEL = "default_user_level";
    private static final String MARK_DEFAULT_USER_NOTIFICATION = "default_user_notification";
    private static final String MARK_DEFAULT_USER_LANGUAGE = "default_user_language";
    private static final String MARK_DEFAULT_USER_STATUS = "default_user_status";
    private static final String MARK_LANGUAGES_LIST = "languages_list";
    private static final String MARK_USER_LEVELS_LIST = "user_levels";
    private static final String MARK_ENABLE_PASSWORD_ENCRYPTION = "enable_password_encryption";
    private static final String MARK_ENCRYPTION_ALGORITHM = "encryption_algorithm";
    private static final String MARK_ENCRYPTION_ALGORITHMS_LIST = "encryption_algorithms_list";
    private static final String MARK_SEARCH_IS_SEARCH = "search_is_search";
    private static final String MARK_SEARCH_ADMIN_USER_FILTER = "search_admin_user_filter";
    private static final String MARK_SEARCH_ADMIN_USER_FIELD_FILTER = "search_admin_user_field_filter";
    private static final String MARK_ATTRIBUTES_LIST = "attributes_list";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_SORT_SEARCH_ATTRIBUTE = "sort_search_attribute";
    private static final String MARK_MAP_ID_USER_LIST_USER_FIELDS = "map_id_user_list_user_fields";
    private static final String MARK_EMAIL_PATTERN = "email_pattern";
    private static final String MARK_AVAILABLE_REGULAREXPRESSIONS = "available_regularexpressions";
    private static final String MARK_SELECTED_REGULAREXPRESSIONS = "selected_regularexpressions";
    private static final String MARK_IS_EMAIL_PATTERN_SET_MANUALLY = "is_email_pattern_set_manually";
    private static final String MARK_PLUGIN_REGULAREXPRESSION = "plugin_regularexpression";
    private static final String MARK_FORCE_CHANGE_PASSWORD_REINIT = "force_change_password_reinit";
    private static final String MARK_PASSWORD_MINIMUM_LENGTH = "password_minimum_length";
    private static final String MARK_PASSWORD_FORMAT_UPPER_LOWER_CASE = "password_format_upper_lower_case";
    private static final String MARK_PASSWORD_FORMAT_NUMERO = "password_format_numero";
    private static final String MARK_PASSWORD_FORMAT_SPECIAL_CHARACTERS = "password_format_special_characters";
    private static final String MARK_PASSWORD_DURATION = "password_duration";
    private static final String MARK_PASSWORD_HISTORY_SIZE = "password_history_size";
    private static final String MARK_MAXIMUM_NUMBER_PASSWORD_CHANGE = "maximum_number_password_change";
    private static final String MARK_TSW_SIZE_PASSWORD_CHANGE = "tsw_size_password_change";
    private static final String MARK_USE_ADVANCED_SECURITY_PARAMETERS = "use_advanced_security_parameters";
    private static final String MARK_ACCOUNT_LIFE_TIME = "account_life_time";
    private static final String MARK_TIME_BEFORE_ALERT_ACCOUNT = "time_before_alert_account";
    private static final String MARK_NB_ALERT_ACCOUNT = "nb_alert_account";
    private static final String MARK_TIME_BETWEEN_ALERTS_ACCOUNT = "time_between_alerts_account";
    private static final String MARK_ACCES_FAILURES_MAX = "access_failures_max";
    private static final String MARK_ACCES_FAILURES_INTERVAL = "access_failures_interval";
    private static final String MARK_NAME = "name";
    private static final String MARK_FIRST_NAME = "first_name";
    private static final String MARK_DATE_VALID = "date_valid";
    private static final String MARK_BANNED_DOMAIN_NAMES = "banned_domain_names";
    private static final String MARK_NOTIFY_USER_PASSWORD_EXPIRED = "notify_user_password_expired";
    private static final String MARK_SITE_NAME = "site_name";
    private static final String MARK_USER = "user";
    private static final String MARK_SITE_LINK = "site_link";
    private static final String MARK_LOGIN_URL = "login_url";

    // Properties
    private static final String PROPERTY_ADMINISTRATOR = "right.administrator";
    private static final String PROPERTY_ENCRYPTION_ALGORITHMS_LIST = "encryption.algorithmsList";
    private static final String PROPERTY_EMAIL_PATTERN = "lutece.email.pattern";
    private static final String PROPERTY_MESSAGE_EMAIL_FORMAT = "portal.users.message.user.emailFormat";
    private static final String PROPERTY_MESSAGE_EMAIL_FORMAT_BANNED_DOMAIN_NAME = "portal.users.message.user.emailFormatBannedDomainNames";
    private static final String PROPERTY_MESSAGE_MINIMUM_PASSWORD_LENGTH = "portal.users.message.password.minimumPasswordLength";
    private static final String PROPERTY_MESSAGE_PASSWORD_FORMAT = "portal.users.message.password.format";
    private static final String PROPERTY_MESSAGE_PASSWORD_FORMAT_UPPER_LOWER_CASE = "portal.users.message.password.formatUpperLowerCase";
    private static final String PROPERTY_MESSAGE_PASSWORD_FORMAT_NUMERO = "portal.users.message.password.formatNumero";
    private static final String PROPERTY_MESSAGE_PASSWORD_FORMAT_SPECIAL_CHARACTERS = "portal.users.message.password.formatSpecialCharacters";
    private static final String PROPERTY_MESSAGE_PASSWORD_ALREADY_USED = "portal.users.message.password.passwordAlreadyUsed";
    private static final String PROPERTY_MESSAGE_MAX_PASSWORD_CHANGE = "portal.users.message.password.maxPasswordChange";
    private static final String PROPERTY_ANONYMIZATION_ENCRYPT_ALGO = "security.anonymization.encryptAlgo";
    private static final String PROPERTY_DEFAULT_PASSWORD_MINIMAL_LENGTH = "security.defaultValues.passwordMinimalLength";
    private static final String PROPERTY_DEFAULT_MAXIMUM_NUMBER_PASSWORD_CHANGE = "security.defaultValues.maximumPasswordChange";
    private static final String PROPERTY_DEFAULT_TSW_SIZE_PASSWORD_CHANGE = "security.defaultValues.maximumPasswordChangeTSWSize";
    private static final String PROPERTY_DEFAULT_HISTORY_SIZE = "security.defaultValues.passwordHistorySize";
    private static final String PROPERTY_DEFAULT_PASSWORD_DURATION = "security.defaultValues.passwordDuration";
    private static final String PROPERTY_DEFAULT_ENCRYPTION_ALGORITHM = "security.defaultValues.algorithm";

    // CONSTANTS
    private static final String CONSTANT_DEFAULT_ENCRYPT_ALGO = "SHA-256";
    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";
    private static final String AMPERSAND = "&";
    private static final String ZERO = "0";
    private static final String CONSTANT_AT = "@";
    private static final String CONSTANT_UNDERSCORE = "_";
    private static final String CONSTANT_XML_USER = "user";
    private static final String CONSTANT_XML_ACCESS_CODE = "access_code";
    private static final String CONSTANT_XML_LAST_NAME = "last_name";
    private static final String CONSTANT_XML_FIRST_NAME = "first_name";
    private static final String CONSTANT_XML_EMAIL = "email";
    private static final String CONSTANT_XML_STATUS = "status";
    private static final String CONSTANT_XML_LOCALE = "locale";
    private static final String CONSTANT_XML_LEVEL = "level";
    private static final String CONSTANT_XML_MUST_CHANGE_PASSWORD = "must_change_password";
    private static final String CONSTANT_XML_ACCESSIBILITY_MODE = "accessibility_mode";
    private static final String CONSTANT_XML_PASSWORD_MAX_VALID_DATE = "password_max_valid_date";
    private static final String CONSTANT_XML_ACCOUNT_MAX_VALID_DATE = "account_max_valid_date";
    private static final String CONSTANT_XML_DATE_LAST_LOGIN = "date_last_login";
    private static final String CONSTANT_XML_ROLES = "roles";
    private static final String CONSTANT_XML_RIGHTS = "rights";
    private static final String CONSTANT_XML_WORKGROUPS = "workgroups";
    private static final String CONSTANT_XML_ROLE = "role";
    private static final String CONSTANT_XML_RIGHT = "right";
    private static final String CONSTANT_XML_WORKGROUP = "workgroup";
    private static final String CONSTANT_XML_ATTRIBUTES = "attributes";
    private static final String CONSTANT_XML_ATTRIBUTE = "attribute";
    private static final String CONSTANT_XML_ATTRIBUTE_ID = "attribute-id";
    private static final String CONSTANT_XML_ATTRIBUTE_FIELD_ID = "attribute-field-id";
    private static final String CONSTANT_XML_ATTRIBUTE_VALUE = "attribute-value";

    /** Private constructor */
    private AdminUserService(  )
    {
    }

    /**
     * Init
     */
    public static void init(  )
    {
        AdminUser.init(  );
    }

    /**
     * Get the user in session
     * @param request The HTTP request
     * @return the user in session
     */
    public static AdminUser getAdminUser( HttpServletRequest request )
    {
        return AdminAuthenticationService.getInstance(  ).getRegisteredUser( request );
    }

    /**
     * Get the locale for the current request
     * @param request The HTTP request
     * @return the locale to use with this request
     */
    public static Locale getLocale( HttpServletRequest request )
    {
        Locale locale;
        AdminUser user = getAdminUser( request );

        if ( user != null )
        {
            // Take the locale of the current user if exists
            locale = user.getLocale(  );
        }
        else
        {
            // TODO : Add cookie search

            // Take the locale of the browser
            locale = request.getLocale(  );
        }

        return locale;
    }

    /**
     * Gets the admin right level
     *
     * @param request The HTTP request
     * @return The boolean level right
     */
    @Deprecated
    public static boolean getUserAdminRightLevel( HttpServletRequest request )
    {
        String strRightCode = AppPropertiesService.getProperty( PROPERTY_ADMINISTRATOR );

        AdminUser user = getAdminUser( request );
        boolean bLevelRight = user.checkRight( strRightCode );

        return bLevelRight;
    }

    /**
     * Get the filtered list of admin users
     * @param listUsers the initial list of users
     * @param request HttpServletRequest
     * @param model map
     * @param url URL of the current interface
     * @return The filtered list of admin users
     */
    public static List<AdminUser> getFilteredUsersInterface( Collection<AdminUser> listUsers,
        HttpServletRequest request, Map<String, Object> model, UrlItem url )
    {
        AdminUser currentUser = getAdminUser( request );

        // FILTER
        AdminUserFilter auFilter = new AdminUserFilter(  );
        List<AdminUser> listFilteredUsers = new ArrayList<AdminUser>(  );
        boolean bIsSearch = auFilter.setAdminUserFilter( request );
        boolean bIsFiltered;

        for ( AdminUser filteredUser : AdminUserHome.findUserByFilter( auFilter ) )
        {
            bIsFiltered = false;

            for ( AdminUser user : listUsers )
            {
                if ( user.getUserId(  ) == filteredUser.getUserId(  ) )
                {
                    bIsFiltered = true;

                    break;
                }
            }

            if ( bIsFiltered && ( currentUser.isParent( filteredUser ) || ( currentUser.isAdmin(  ) ) ) )
            {
                listFilteredUsers.add( filteredUser );
            }
        }

        List<AdminUser> filteredUsers = new ArrayList<AdminUser>(  );

        AdminUserFieldFilter auFieldFilter = new AdminUserFieldFilter(  );
        auFieldFilter.setAdminUserFieldFilter( request, currentUser.getLocale(  ) );

        List<AdminUser> listFilteredUsersByUserFields = AdminUserFieldHome.findUsersByFilter( auFieldFilter );

        if ( listFilteredUsersByUserFields != null )
        {
            for ( AdminUser filteredUser : listFilteredUsers )
            {
                for ( AdminUser filteredUserByUserField : listFilteredUsersByUserFields )
                {
                    if ( filteredUser.getUserId(  ) == filteredUserByUserField.getUserId(  ) )
                    {
                        filteredUsers.add( filteredUser );
                    }
                }
            }
        }
        else
        {
            filteredUsers = listFilteredUsers;
        }

        Map<String, List<AdminUserField>> map = new HashMap<String, List<AdminUserField>>(  );

        for ( AdminUser user : filteredUsers )
        {
            auFieldFilter.setIdUser( user.getUserId(  ) );

            List<AdminUserField> listAdminUserFields = AdminUserFieldHome.findByFilter( auFieldFilter );
            map.put( String.valueOf( user.getUserId(  ) ), listAdminUserFields );
        }

        List<IAttribute> listAttributes = AttributeService.getInstance(  )
                                                          .getAllAttributesWithFields( currentUser.getLocale(  ) );

        String strSortSearchAttribute = StringUtils.EMPTY;

        if ( bIsSearch )
        {
            auFilter.setUrlAttributes( url );
            strSortSearchAttribute = AMPERSAND + auFilter.getUrlAttributes(  );
            auFieldFilter.setUrlAttributes( url );
            strSortSearchAttribute = auFieldFilter.getUrlAttributes(  );
        }

        model.put( MARK_SEARCH_ADMIN_USER_FILTER, auFilter );
        model.put( MARK_SEARCH_IS_SEARCH, bIsSearch );
        model.put( MARK_SEARCH_ADMIN_USER_FIELD_FILTER, auFieldFilter );
        model.put( MARK_LOCALE, currentUser.getLocale(  ) );
        model.put( MARK_ATTRIBUTES_LIST, listAttributes );
        model.put( MARK_SORT_SEARCH_ATTRIBUTE, strSortSearchAttribute );
        model.put( MARK_MAP_ID_USER_LIST_USER_FIELDS, map );

        return filteredUsers;
    }

    /**
     * Build the advanced parameters management
     * @param user The AdminUser object
     * @return The model for the advanced parameters
     */
    public static Map<String, Object> getManageAdvancedParameters( AdminUser user )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        boolean bPermissionManageAdvancedParameters = RBACService.isAuthorized( AdminUser.RESOURCE_TYPE,
                RBAC.WILDCARD_RESOURCES_ID, AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, user );
        boolean bPermissionManageEncryptedPassword = RBACService.isAuthorized( AdminUser.RESOURCE_TYPE,
                RBAC.WILDCARD_RESOURCES_ID, AdminUserResourceIdService.PERMISSION_MANAGE_ENCRYPTED_PASSWORD, user );

        if ( bPermissionManageAdvancedParameters )
        {
            // Encryption Password
            if ( bPermissionManageEncryptedPassword )
            {
                model.put( MARK_ENABLE_PASSWORD_ENCRYPTION,
                    DefaultUserParameterHome.findByKey( DSKEY_ENABLE_PASSWORD_ENCRYPTION ) );
                model.put( MARK_ENCRYPTION_ALGORITHM, DefaultUserParameterHome.findByKey( DSKEY_ENCRYPTION_ALGORITHM ) );

                String[] listAlgorithms = AppPropertiesService.getProperty( PROPERTY_ENCRYPTION_ALGORITHMS_LIST )
                                                              .split( COMMA );

                for ( String strAlgorithm : listAlgorithms )
                {
                    strAlgorithm.trim(  );
                }

                model.put( MARK_ENCRYPTION_ALGORITHMS_LIST, listAlgorithms );
            }

            // USER LEVEL
            String strDefaultLevel = DefaultUserParameterHome.findByKey( DSKEY_DEFAULT_USER_LEVEL );
            Level defaultLevel = LevelHome.findByPrimaryKey( Integer.parseInt( strDefaultLevel ) );

            // USER NOTIFICATION
            int nDefaultUserNotification = Integer.parseInt( DefaultUserParameterHome.findByKey( 
                        DSKEY_DEFAULT_USER_NOTIFICATION ) );

            // USER LANGUAGE
            ReferenceList listLanguages = I18nService.getAdminLocales( user.getLocale(  ) );
            String strDefaultUserLanguage = DefaultUserParameterHome.findByKey( DSKEY_DEFAULT_USER_LANGUAGE );

            // USER STATUS
            int nDefaultUserStatus = Integer.parseInt( DefaultUserParameterHome.findByKey( DSKEY_DEFAULT_USER_STATUS ) );

            model.put( MARK_USER_LEVELS_LIST, LevelHome.getLevelsList(  ) );
            model.put( MARK_DEFAULT_USER_LEVEL, defaultLevel );
            model.put( MARK_DEFAULT_USER_NOTIFICATION, nDefaultUserNotification );
            model.put( MARK_LANGUAGES_LIST, listLanguages );
            model.put( MARK_DEFAULT_USER_LANGUAGE, strDefaultUserLanguage );
            model.put( MARK_DEFAULT_USER_STATUS, nDefaultUserStatus );

            // EMAIL PATTERN
            model.put( MARK_PLUGIN_REGULAREXPRESSION, RegularExpressionService.getInstance(  ).isAvailable(  ) );
            model.put( MARK_IS_EMAIL_PATTERN_SET_MANUALLY, isEmailPatternSetManually(  ) );
            model.put( MARK_EMAIL_PATTERN, getEmailPattern(  ) );
            model.put( MARK_AVAILABLE_REGULAREXPRESSIONS, getAvailableRegularExpressions(  ) );
            model.put( MARK_SELECTED_REGULAREXPRESSIONS, getSelectedRegularExpressions(  ) );

            boolean bUseAdvancesSecurityParameters = getBooleanSecurityParameter( DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );

            model.put( MARK_USE_ADVANCED_SECURITY_PARAMETERS, bUseAdvancesSecurityParameters );

            model.put( MARK_FORCE_CHANGE_PASSWORD_REINIT,
                getBooleanSecurityParameter( DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) );
            model.put( MARK_PASSWORD_MINIMUM_LENGTH, getIntegerSecurityParameter( DSKEY_PASSWORD_MINIMUM_LENGTH ) );

            if ( bUseAdvancesSecurityParameters )
            {
                // SECURITY PARAMETERS
                model.put( MARK_PASSWORD_FORMAT_UPPER_LOWER_CASE,
                    getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE ) );
                model.put( MARK_PASSWORD_FORMAT_NUMERO, getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_NUMERO ) );
                model.put( MARK_PASSWORD_FORMAT_SPECIAL_CHARACTERS,
                    getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS ) );
                model.put( MARK_PASSWORD_DURATION, getIntegerSecurityParameter( DSKEY_PASSWORD_DURATION ) );
                model.put( MARK_PASSWORD_HISTORY_SIZE, getIntegerSecurityParameter( DSKEY_PASSWORD_HISTORY_SIZE ) );
                model.put( MARK_MAXIMUM_NUMBER_PASSWORD_CHANGE,
                    getIntegerSecurityParameter( DSKEY_MAXIMUM_NUMBER_PASSWORD_CHANGE ) );
                model.put( MARK_TSW_SIZE_PASSWORD_CHANGE, getIntegerSecurityParameter( DSKEY_TSW_SIZE_PASSWORD_CHANGE ) );
                model.put( MARK_NOTIFY_USER_PASSWORD_EXPIRED,
                    getBooleanSecurityParameter( DSKEY_NOTIFY_USER_PASSWORD_EXPIRED ) );
            }

            model.put( MARK_BANNED_DOMAIN_NAMES, getLargeSecurityParameter( DSKEY_BANNED_DOMAIN_NAMES ) );
            model.put( MARK_ACCOUNT_LIFE_TIME, getIntegerSecurityParameter( DSKEY_ACCOUNT_LIFE_TIME ) );
            model.put( MARK_TIME_BEFORE_ALERT_ACCOUNT, getIntegerSecurityParameter( DSKEY_TIME_BEFORE_ALERT_ACCOUNT ) );
            model.put( MARK_NB_ALERT_ACCOUNT, getIntegerSecurityParameter( DSKEY_NB_ALERT_ACCOUNT ) );
            model.put( MARK_TIME_BETWEEN_ALERTS_ACCOUNT,
                getIntegerSecurityParameter( DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT ) );
            model.put( MARK_ACCES_FAILURES_MAX, getIntegerSecurityParameter( DSKEY_ACCES_FAILURES_MAX ) );
            model.put( MARK_ACCES_FAILURES_INTERVAL, getIntegerSecurityParameter( DSKEY_ACCES_FAILURES_INTERVAL ) );
        }

        return model;
    }

    /**
     * Check if the given email is valid or not. <br />
     * The given email is compared to the value of the parameter
     * <i>'core_user_parameter.email_pattern'</i>.
     *
     * @param strEmail the str email
     * @return true, if successful
     */
    public static boolean checkEmail( String strEmail )
    {
        boolean bIsValid = true;

        if ( isEmailPatternSetManually(  ) )
        {
            if ( StringUtils.isBlank( strEmail ) || !strEmail.matches( getEmailPattern(  ) ) )
            {
                bIsValid = false;
            }
        }
        else
        {
            for ( RegularExpression regularExpression : getSelectedRegularExpressions(  ) )
            {
                if ( !RegularExpressionService.getInstance(  ).isMatches( strEmail, regularExpression ) )
                {
                    bIsValid = false;

                    break;
                }
            }
        }

        if ( bIsValid )
        {
            String strBannedDomainNames = AdminUserService.getSecurityParameter( DSKEY_BANNED_DOMAIN_NAMES );

            if ( !StringUtils.isEmpty( strBannedDomainNames ) )
            {
                String[] strListBannedDomainNames = strBannedDomainNames.split( SEMICOLON );
                String strDomainName = strEmail.substring( strEmail.indexOf( CONSTANT_AT ) + 1 );

                if ( ( strDomainName != null ) && ( strListBannedDomainNames != null ) &&
                        ( strListBannedDomainNames.length > 0 ) )
                {
                    for ( String strDomain : strListBannedDomainNames )
                    {
                        if ( strDomainName.equals( strDomain ) )
                        {
                            bIsValid = false;

                            break;
                        }
                    }
                }
            }
        }

        return bIsValid;
    }

    /**
     * Do modify the email pattern
     * @param strEmailPattern the email pattern
     * @param bIsSetManually true if it is know set manually, false otherwise
     */
    public static void doModifyEmailPattern( String strEmailPattern, boolean bIsSetManually )
    {
        if ( bIsSetManually )
        {
            DefaultUserParameterHome.update( DSKEY_EMAIL_PATTERN, strEmailPattern );
            DefaultUserParameterHome.update( DSKEY_EMAIL_PATTERN_VERIFY_BY, StringUtils.EMPTY );
        }
        else
        {
            if ( isEmailPatternSetManually(  ) )
            {
                // If the previous email pattern is set manually, then the parameter email_pattern_verify_by is set at 0
                // This way, the interface know the email pattern is not set manually
                // Indeed, the control is set on the content of the parameter 'email_pattern_verify_by'
                DefaultUserParameterHome.update( DSKEY_EMAIL_PATTERN_VERIFY_BY, ZERO );
            }
        }
    }

    /**
     * Reset the email pattern by putting the default email pattern that is set
     * in the <b>lutece.properties</b>.
     */
    public static void doResetEmailPattern(  )
    {
        DefaultUserParameterHome.update( DSKEY_EMAIL_PATTERN, getDefaultEmailPattern(  ) );
    }

    /**
     * Get the email error message url.
     *
     * @param request the request
     * @return the error message
     */
    public static String getEmailErrorMessageUrl( HttpServletRequest request )
    {
        String strMessage;

        if ( isEmailPatternSetManually(  ) )
        {
            strMessage = getEmailPattern(  );
        }
        else
        {
            StringBuilder sbMessage = new StringBuilder(  );
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.split( COMMA );

            for ( String strRegularExpressionId : regularExpressionIds )
            {
                strRegularExpressionId.trim(  );

                if ( StringUtils.isNotBlank( strRegularExpressionId ) &&
                        StringUtils.isNumeric( strRegularExpressionId ) )
                {
                    int nRegularExpressionId = Integer.parseInt( strRegularExpressionId );
                    RegularExpression regularExpression = RegularExpressionService.getInstance(  )
                                                                                  .getRegularExpressionByKey( nRegularExpressionId );

                    if ( regularExpression != null )
                    {
                        sbMessage.append( regularExpression.getValidExemple(  ) );
                        sbMessage.append( COMMA );
                    }
                }
            }

            // Get all message except the last character which is a comma
            strMessage = sbMessage.toString(  ).substring( 0, sbMessage.length(  ) - 1 );
        }

        String strBannedDomainNames = getSecurityParameter( DSKEY_BANNED_DOMAIN_NAMES );
        String strMessageProperty;

        if ( !StringUtils.isEmpty( strBannedDomainNames ) )
        {
            strMessageProperty = PROPERTY_MESSAGE_EMAIL_FORMAT_BANNED_DOMAIN_NAME;
        }
        else
        {
            strMessageProperty = PROPERTY_MESSAGE_EMAIL_FORMAT;
        }

        Object[] param = { strMessage, strBannedDomainNames };

        return AdminMessageService.getMessageUrl( request, strMessageProperty, param, AdminMessage.TYPE_STOP );
    }

    /**
     * Do insert a regular expression
     * @param nRegularExpressionId the ID of the regular expression
     */
    public static void doInsertRegularExpression( int nRegularExpressionId )
    {
        if ( !isEmailPatternSetManually(  ) )
        {
            // Retrieve the rules from the database
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.split( COMMA );

            // Check if the ID is already inserted
            boolean bIsAlreadyInserted = false;

            for ( String strRegularExpressionId : regularExpressionIds )
            {
                strRegularExpressionId.trim(  );

                if ( StringUtils.isNotBlank( strRegularExpressionId ) &&
                        StringUtils.isNumeric( strRegularExpressionId ) )
                {
                    int nRegexId = Integer.parseInt( strRegularExpressionId );

                    if ( nRegexId == nRegularExpressionId )
                    {
                        bIsAlreadyInserted = true;

                        break;
                    }
                }
            }

            if ( !bIsAlreadyInserted )
            {
                // If it is not inserted, then it is concatened to the list of regularExpression
                String strRegularExpressionIds = emailPatternVerifyBy + COMMA + nRegularExpressionId;
                emailPatternVerifyBy = strRegularExpressionIds;
                DefaultUserParameterHome.update( DSKEY_EMAIL_PATTERN_VERIFY_BY, emailPatternVerifyBy );
            }
        }
    }

    /**
     * Do remove a regular expression
     * @param nRegularExpressionId the ID of the regularexpresion
     */
    public static void doRemoveRegularExpression( int nRegularExpressionId )
    {
        if ( !isEmailPatternSetManually(  ) )
        {
            List<Integer> listRegularExpressionIds = new ArrayList<Integer>(  );

            // Retrieve the rules from the database
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.split( COMMA );

            // Build the list of regular expression without the regular expression id to delete
            for ( String strRegularExpressionId : regularExpressionIds )
            {
                strRegularExpressionId.trim(  );

                if ( StringUtils.isNotBlank( strRegularExpressionId ) &&
                        StringUtils.isNumeric( strRegularExpressionId ) )
                {
                    int nRegexId = Integer.parseInt( strRegularExpressionId );

                    if ( nRegexId != nRegularExpressionId )
                    {
                        listRegularExpressionIds.add( nRegexId );
                    }
                }
            }

            StringBuilder sbRegularExpressionIds = new StringBuilder(  );

            for ( int i = 0; i < listRegularExpressionIds.size(  ); i++ )
            {
                sbRegularExpressionIds.append( listRegularExpressionIds.get( i ) );

                if ( i < ( listRegularExpressionIds.size(  ) - 1 ) )
                {
                    sbRegularExpressionIds.append( COMMA );
                }
            }

            DefaultUserParameterHome.update( DSKEY_EMAIL_PATTERN_VERIFY_BY, sbRegularExpressionIds.toString(  ) );
        }
    }

    /**
     * Get the default email pattern defined in the <b>lutece.properties</b>.
     * @return the default email pattern
     */
    private static String getDefaultEmailPattern(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_EMAIL_PATTERN );
    }

    /**
     * Get the AdminUser email pattern that is stored in
     * <b>'core_user_parameter.email_pattern'</b>. <br />
     * If it does not exist, then it will retrieve the value in the
     * <b>lutece.properties</b> file (parameter <b>email.pattern</b>)
     * @return the AdminUser email pattern
     */
    private static String getEmailPattern(  )
    {
        String strEmailPattern = getDefaultEmailPattern(  );
        String emailPattern = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN );

        if ( emailPattern != null )
        {
            strEmailPattern = emailPattern;
        }

        return strEmailPattern;
    }

    /**
     * Get the available rugalar expressions
     * @return a list of {@link ReferenceList}
     */
    public static ReferenceList getAvailableRegularExpressions(  )
    {
        ReferenceList regularExpressionsList = new ReferenceList(  );

        if ( !isEmailPatternSetManually(  ) )
        {
            List<Integer> listRegularExpressionIds = new ArrayList<Integer>(  );

            // Retrieve the rules from the database
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.split( COMMA );

            for ( String strRegularExpressionId : regularExpressionIds )
            {
                strRegularExpressionId.trim(  );

                if ( StringUtils.isNotBlank( strRegularExpressionId ) &&
                        StringUtils.isNumeric( strRegularExpressionId ) )
                {
                    int nRegexId = Integer.parseInt( strRegularExpressionId );
                    listRegularExpressionIds.add( nRegexId );
                }
            }

            // Fetch all regular expressions
            List<RegularExpression> listRegularExpression = RegularExpressionService.getInstance(  )
                                                                                    .getAllRegularExpression(  );

            // Get only the expressions that are not already selected
            for ( RegularExpression regularExpression : listRegularExpression )
            {
                if ( !listRegularExpressionIds.contains( regularExpression.getIdExpression(  ) ) )
                {
                    regularExpressionsList.addItem( regularExpression.getIdExpression(  ),
                        regularExpression.getTitle(  ) );
                }
            }
        }

        return regularExpressionsList;
    }

    /**
     * Get the list of selected regular expression
     * @return a list of {@link RegularExpression}
     */
    public static List<RegularExpression> getSelectedRegularExpressions(  )
    {
        List<RegularExpression> listRegularExpressions = new ArrayList<RegularExpression>(  );

        if ( !isEmailPatternSetManually(  ) )
        {
            // Retrieve the rules from the database
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.split( COMMA );

            for ( String strRegularExpressionId : regularExpressionIds )
            {
                strRegularExpressionId.trim(  );

                if ( StringUtils.isNotBlank( strRegularExpressionId ) &&
                        StringUtils.isNumeric( strRegularExpressionId ) )
                {
                    int nRegularExpressionId = Integer.parseInt( strRegularExpressionId );
                    RegularExpression expression = RegularExpressionService.getInstance(  )
                                                                           .getRegularExpressionByKey( nRegularExpressionId );

                    if ( expression != null )
                    {
                        listRegularExpressions.add( expression );
                    }
                }
            }
        }

        return listRegularExpressions;
    }

    /**
     * Check whether the email pattern is set manually or by a set of rules from
     * the plugin-regularexpression.
     * @return true if it is set manually, false otherwise
     */
    private static boolean isEmailPatternSetManually(  )
    {
        boolean bIsSetManually = true;

        if ( RegularExpressionService.getInstance(  ).isAvailable(  ) )
        {
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( DSKEY_EMAIL_PATTERN_VERIFY_BY );

            if ( StringUtils.isNotBlank( emailPatternVerifyBy ) )
            {
                bIsSetManually = false;
            }
        }

        return bIsSetManually;
    }

    /**
     * Get an integer user parameter from its key.
     * @param strParameterkey Key of the parameter
     * @return The value of the user parameter, or 0 if there is no value or an
     *         non integer value.
     */
    public static int getIntegerSecurityParameter( String strParameterkey )
    {
        String defaultUserParameter = DefaultUserParameterHome.findByKey( strParameterkey );

        if ( StringUtils.isBlank( defaultUserParameter ) )
        {
            return 0;
        }

        try
        {
            int nValue = Integer.parseInt( defaultUserParameter );

            return nValue;
        }
        catch ( NumberFormatException e )
        {
            return 0;
        }
    }

    /**
     * Get a boolean user parameter from its key.
     * @param strParameterkey Key of the parameter
     * @return The value of the user parameter, or false if there is no value or
     *         an non boolean value.
     */
    public static boolean getBooleanSecurityParameter( String strParameterkey )
    {
        String defaultUserParameter = DefaultUserParameterHome.findByKey( strParameterkey );

        return ( defaultUserParameter == null ) ? false : Boolean.parseBoolean( defaultUserParameter );
    }

    /**
     * Get a user parameter from its key.
     * @param strParameterkey Key of the parameter
     * @return The value of the user parameter.
     */
    public static String getSecurityParameter( String strParameterkey )
    {
        String defaultUserParameter = DefaultUserParameterHome.findByKey( strParameterkey );

        return ( defaultUserParameter == null ) ? null : defaultUserParameter;
    }

    /**
     * Get a user parameter from its key.
     * @param strParameterKey Key of the parameter
     * @return The value of the user parameter.
     */
    public static String getLargeSecurityParameter( String strParameterKey )
    {
        return DatastoreService.getDataValue( PluginService.getCore(  ).getName(  ) + CONSTANT_UNDERSCORE +
            strParameterKey, StringUtils.EMPTY );
    }

    /**
     * Update a security parameter value.
     * @param strParameterKey The key of the parameter
     * @param strValue The new value
     */
    public static void updateSecurityParameter( String strParameterKey, String strValue )
    {
        String strValueTmp = StringUtils.isNotBlank( strValue ) ? strValue : StringUtils.EMPTY;
        DefaultUserParameterHome.update( strParameterKey, strValueTmp );
    }

    /**
     * Update a security parameter value.
     * @param strParameterKey The key of the parameter
     * @param strValue The new value
     */
    public static void updateLargeSecurityParameter( String strParameterKey, String strValue )
    {
        DatastoreService.setDataValue( PluginService.getCore(  ).getName(  ) + CONSTANT_UNDERSCORE + strParameterKey,
            strValue );
    }

    /**
     * Check that the password respect user parameters
     * @param request The request
     * @param strPassword The password to check
     * @param nUserId The id of the modified user
     * @return Null if the password is correct, or the url of an admin message
     *         describing the error
     */
    public static String checkPassword( HttpServletRequest request, String strPassword, int nUserId )
    {
        return checkPassword( request, strPassword, nUserId, Boolean.FALSE );
    }

    /**
     * Check that the password respect user parameters
     * @param request The request
     * @param strPassword The password to check
     * @param nUserId The id of the modified user
     * @param bSkipHistoryCheck Indicates if the password history should be
     *            checked or not.
     * @return Null if the password is correct, or the url of an admin message
     *         describing the error
     */
    public static String checkPassword( HttpServletRequest request, String strPassword, int nUserId,
        boolean bSkipHistoryCheck )
    {
        // Minimum password length
        int nMinimumLength = AdminUserService.getIntegerSecurityParameter( DSKEY_PASSWORD_MINIMUM_LENGTH );

        if ( ( nMinimumLength > 0 ) && ( strPassword.length(  ) < nMinimumLength ) )
        {
            Object[] param = { nMinimumLength };

            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_MINIMUM_PASSWORD_LENGTH, param,
                AdminMessage.TYPE_STOP );
        }

        // Password format
        boolean bUserPasswordFormatUpperLowerCase = AdminUserService.getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE );
        boolean bUserPasswordFormatNumero = AdminUserService.getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_NUMERO );
        boolean bUserPasswordFormatSpecialCaracters = AdminUserService.getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS );

        if ( ( bUserPasswordFormatUpperLowerCase || bUserPasswordFormatNumero || bUserPasswordFormatSpecialCaracters ) &&
                !PasswordUtil.checkPasswordFormat( strPassword, bUserPasswordFormatUpperLowerCase,
                    bUserPasswordFormatNumero, bUserPasswordFormatSpecialCaracters ) )
        {
            StringBuffer strParam = new StringBuffer(  );

            //Add Message Upper Lower Case
            if ( bUserPasswordFormatUpperLowerCase )
            {
                strParam.append( I18nService.getLocalizedString( PROPERTY_MESSAGE_PASSWORD_FORMAT_UPPER_LOWER_CASE,
                        request.getLocale(  ) ) );
            }

            //Add Message Numero
            if ( bUserPasswordFormatNumero )
            {
                if ( bUserPasswordFormatUpperLowerCase )
                {
                    strParam.append( ", " );
                }

                strParam.append( I18nService.getLocalizedString( PROPERTY_MESSAGE_PASSWORD_FORMAT_NUMERO,
                        request.getLocale(  ) ) );
            }

            //Add Message Special Characters
            if ( bUserPasswordFormatSpecialCaracters )
            {
                if ( bUserPasswordFormatUpperLowerCase || bUserPasswordFormatNumero )
                {
                    strParam.append( ", " );
                }

                strParam.append( I18nService.getLocalizedString( PROPERTY_MESSAGE_PASSWORD_FORMAT_SPECIAL_CHARACTERS,
                        request.getLocale(  ) ) );
            }

            Object[] param = { strParam.toString(  ) };

            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_PASSWORD_FORMAT, param,
                AdminMessage.TYPE_STOP );
        }

        // Check password history
        if ( ( nUserId > 0 ) && !bSkipHistoryCheck )
        {
            int nPasswordHistorySize = AdminUserService.getIntegerSecurityParameter( DSKEY_PASSWORD_HISTORY_SIZE );

            if ( nPasswordHistorySize > 0 )
            {
                String strEncryptedPassword = encryptPassword( strPassword );
                List<String> passwordHistory = AdminUserHome.selectUserPasswordHistory( nUserId );

                if ( nPasswordHistorySize < passwordHistory.size(  ) )
                {
                    passwordHistory = passwordHistory.subList( 0, nPasswordHistorySize );
                }

                if ( passwordHistory.contains( strEncryptedPassword ) )
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_PASSWORD_ALREADY_USED,
                        AdminMessage.TYPE_STOP );
                }
            }

            int nTSWSizePasswordChange = AdminUserService.getIntegerSecurityParameter( DSKEY_TSW_SIZE_PASSWORD_CHANGE );
            int nMaximumNumberPasswordChange = AdminUserService.getIntegerSecurityParameter( DSKEY_MAXIMUM_NUMBER_PASSWORD_CHANGE );

            if ( nMaximumNumberPasswordChange > 0 )
            {
                Timestamp minDate;

                if ( nTSWSizePasswordChange > 0 )
                {
                    minDate = new Timestamp( new java.util.Date(  ).getTime(  ) -
                            DateUtil.convertDaysInMiliseconds( nTSWSizePasswordChange ) );
                }
                else
                {
                    minDate = new Timestamp( 0 );
                }

                if ( AdminUserHome.countUserPasswordHistoryFromDate( minDate, nUserId ) >= nMaximumNumberPasswordChange )
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_MAX_PASSWORD_CHANGE,
                        AdminMessage.TYPE_STOP );
                }
            }
        }

        return null;
    }

    /**
     * Generate a new random password
     * @return the new password
     */
    public static String makePassword(  )
    {
        // Password format
        boolean bUserPasswordFormatUpperLowerCase = AdminUserService.getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE );
        boolean bUserPasswordFormatNumero = AdminUserService.getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_NUMERO );
        boolean bUserPasswordFormatSpecialCaracters = AdminUserService.getBooleanSecurityParameter( DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS );
        int nMinPasswordSize = AdminUserService.getIntegerSecurityParameter( DSKEY_PASSWORD_MINIMUM_LENGTH );

        return PasswordUtil.makePassword( nMinPasswordSize, bUserPasswordFormatUpperLowerCase,
            bUserPasswordFormatNumero, bUserPasswordFormatSpecialCaracters );
    }

    /**
     * Encrypt a password with the encryption algorithm choosed by the admin
     * @param strPassword The password to encrypt
     * @return The given password encrypted
     */
    public static String encryptPassword( String strPassword )
    {
        String strPasswordTmp = strPassword;

        if ( getBooleanSecurityParameter( DSKEY_ENABLE_PASSWORD_ENCRYPTION ) )
        {
            String strAlgorithm = DefaultUserParameterHome.findByKey( DSKEY_ENCRYPTION_ALGORITHM );
            strPasswordTmp = CryptoService.encrypt( strPasswordTmp, strAlgorithm );
        }

        return strPasswordTmp;
    }

    /**
     * Enable advanced security parameters
     */
    public static void useAdvancedSecurityParameters(  )
    {
        updateSecurityParameter( DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.TRUE.toString(  ) );
        updateSecurityParameter( DSKEY_FORCE_CHANGE_PASSWORD_REINIT, Boolean.TRUE.toString(  ) );
        updateSecurityParameter( DSKEY_MAXIMUM_NUMBER_PASSWORD_CHANGE,
            AppPropertiesService.getProperty( PROPERTY_DEFAULT_MAXIMUM_NUMBER_PASSWORD_CHANGE ) );
        updateSecurityParameter( DSKEY_PASSWORD_DURATION,
            AppPropertiesService.getProperty( PROPERTY_DEFAULT_PASSWORD_DURATION ) );
        updateSecurityParameter( DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE, Boolean.TRUE.toString(  ) );
        updateSecurityParameter( DSKEY_PASSWORD_FORMAT_NUMERO, Boolean.TRUE.toString(  ) );
        updateSecurityParameter( DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS, Boolean.TRUE.toString(  ) );
        updateSecurityParameter( DSKEY_PASSWORD_HISTORY_SIZE,
            AppPropertiesService.getProperty( PROPERTY_DEFAULT_HISTORY_SIZE ) );
        updateSecurityParameter( DSKEY_TSW_SIZE_PASSWORD_CHANGE,
            AppPropertiesService.getProperty( PROPERTY_DEFAULT_TSW_SIZE_PASSWORD_CHANGE ) );

        int nMinPwdLength = getIntegerSecurityParameter( DSKEY_PASSWORD_MINIMUM_LENGTH );

        if ( nMinPwdLength <= 0 )
        {
            updateSecurityParameter( DSKEY_PASSWORD_MINIMUM_LENGTH,
                AppPropertiesService.getProperty( PROPERTY_DEFAULT_PASSWORD_MINIMAL_LENGTH ) );
        }

        updateSecurityParameter( DSKEY_ENABLE_PASSWORD_ENCRYPTION, Boolean.TRUE.toString(  ) );
        updateSecurityParameter( DSKEY_ENCRYPTION_ALGORITHM,
            AppPropertiesService.getProperty( PROPERTY_DEFAULT_ENCRYPTION_ALGORITHM ) );
        updateSecurityParameter( DSKEY_NOTIFY_USER_PASSWORD_EXPIRED, Boolean.TRUE.toString(  ) );
    }

    /**
     * Disable advances security parameters
     */
    public static void removeAdvancedSecurityParameters(  )
    {
        updateSecurityParameter( DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_MAXIMUM_NUMBER_PASSWORD_CHANGE, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_PASSWORD_DURATION, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_PASSWORD_FORMAT_NUMERO, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_PASSWORD_HISTORY_SIZE, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_TSW_SIZE_PASSWORD_CHANGE, StringUtils.EMPTY );
        updateSecurityParameter( DSKEY_NOTIFY_USER_PASSWORD_EXPIRED, StringUtils.EMPTY );
    }

    /**
     * Compute the maximum valid date of a password with the current time and
     * the parameters in the database.
     * @return The maximum valid date of a password
     */
    public static Timestamp getPasswordMaxValidDate(  )
    {
        int nbDayPasswordValid = getIntegerSecurityParameter( DSKEY_PASSWORD_DURATION );

        if ( nbDayPasswordValid <= 0 )
        {
            return null;
        }

        return PasswordUtil.getPasswordMaxValidDate( nbDayPasswordValid );
    }

    /**
     * Compute the maximum valid date of an account with the current time and
     * the parameters in the database.
     * @return The maximum valid date of an account
     */
    public static Timestamp getAccountMaxValidDate(  )
    {
        int nbMonthsAccountValid = getIntegerSecurityParameter( DSKEY_ACCOUNT_LIFE_TIME );

        if ( nbMonthsAccountValid <= 0 )
        {
            return null;
        }

        Calendar calendar = new GregorianCalendar( LocaleService.getDefault(  ) );
        calendar.add( Calendar.MONTH, nbMonthsAccountValid );

        return new Timestamp( calendar.getTimeInMillis(  ) );
    }

    /**
     * Anonymize user data from his id. His rights, roles and his passwords
     * history are also deleted.
     * @param nAdminUserId Id of the user to anonymize
     * @param locale The locale
     */
    public static void anonymizeUser( int nAdminUserId, Locale locale )
    {
        AdminUser user = AdminUserHome.findByPrimaryKey( nAdminUserId );

        String strEncryptionAlgorithme = AppPropertiesService.getProperty( PROPERTY_ANONYMIZATION_ENCRYPT_ALGO,
                CONSTANT_DEFAULT_ENCRYPT_ALGO );

        Map<String, Boolean> anonymizationStatus = AdminUserHome.getAnonymizationStatusUserStaticField(  );

        if ( anonymizationStatus.get( PARAMETER_ACCESS_CODE ) )
        {
            user.setAccessCode( CryptoService.encrypt( user.getAccessCode(  ), strEncryptionAlgorithme ) );
        }

        if ( anonymizationStatus.get( PARAMETER_FIRST_NAME ) )
        {
            user.setFirstName( CryptoService.encrypt( user.getFirstName(  ), strEncryptionAlgorithme ) );
        }

        if ( anonymizationStatus.get( PARAMETER_LAST_NAME ) )
        {
            user.setLastName( CryptoService.encrypt( user.getLastName(  ), strEncryptionAlgorithme ) );
        }

        if ( anonymizationStatus.get( PARAMETER_EMAIL ) )
        {
            user.setEmail( CryptoService.encrypt( user.getEmail(  ), strEncryptionAlgorithme ) );
        }

        user.setStatus( AdminUser.ANONYMIZED_CODE );
        AdminUserHome.removeAllRightsForUser( nAdminUserId );
        AdminUserHome.removeAllRolesForUser( nAdminUserId );
        AdminUserHome.removeAllPasswordHistoryForUser( nAdminUserId );
        AdminUserHome.update( user );

        AttributeService attributeService = AttributeService.getInstance(  );
        List<IAttribute> listAllAttributes = attributeService.getAllAttributesWithoutFields( locale );
        List<IAttribute> listAttributesText = new ArrayList<IAttribute>(  );

        for ( IAttribute attribut : listAllAttributes )
        {
            if ( attribut.isAnonymizable(  ) )
            {
                listAttributesText.add( attribut );
            }
        }

        for ( IAttribute attribute : listAttributesText )
        {
            List<AdminUserField> listAdminUserField = AdminUserFieldHome.selectUserFieldsByIdUserIdAttribute( nAdminUserId,
                    attribute.getIdAttribute(  ) );

            for ( AdminUserField adminUserField : listAdminUserField )
            {
                adminUserField.setValue( CryptoService.encrypt( adminUserField.getValue(  ), strEncryptionAlgorithme ) );
                AdminUserFieldHome.update( adminUserField );
            }
        }
    }

    /**
     * Get the list of id of expired users
     * @return the list of id of expired users
     */
    public static List<Integer> getExpiredUserIdList(  )
    {
        return AdminUserHome.findAllExpiredUserId(  );
    }

    /**
     * Update the user expiration date with new values, and notify him with an
     * email if his account was close to expire.
     * @param user The user to update
     */
    @SuppressWarnings( "deprecation" )
    public static void updateUserExpirationDate( AdminUser user )
    {
        if ( user == null )
        {
            return;
        }

        Timestamp newExpirationDate = getAccountMaxValidDate(  );
        Timestamp maxValidDate = user.getAccountMaxValidDate(  );
        // We update the user account
        AdminUserHome.updateUserExpirationDate( user.getUserId(  ), newExpirationDate );

        // We notify the user
        String strUserMail = user.getEmail(  );
        int nbDaysBeforeFirstAlert = AdminUserService.getIntegerSecurityParameter( DSKEY_TIME_BEFORE_ALERT_ACCOUNT );

        if ( maxValidDate != null )
        {
            Timestamp firstAlertMaxDate = new Timestamp( maxValidDate.getTime(  ) -
                    DateUtil.convertDaysInMiliseconds( nbDaysBeforeFirstAlert ) );
            Timestamp currentTimestamp = new Timestamp( new java.util.Date(  ).getTime(  ) );

            if ( ( currentTimestamp.getTime(  ) > firstAlertMaxDate.getTime(  ) ) &&
                    StringUtils.isNotBlank( strUserMail ) )
            {
                AdminUser completeUser = AdminUserHome.findByPrimaryKey( user.getUserId(  ) );
                String strBody = DatabaseTemplateService.getTemplateFromKey( DSKEY_ACCOUNT_REACTIVATED_MAIL_BODY );

                String defaultUserParameter = DefaultUserParameterHome.findByKey( DSKEY_ACCOUNT_REACTIVATED_MAIL_SENDER );
                String strSender = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

                defaultUserParameter = DefaultUserParameterHome.findByKey( DSKEY_ACCOUNT_REACTIVATED_MAIL_SUBJECT );

                String strSubject = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

                Map<String, String> model = new HashMap<String, String>(  );

                DateFormat dateFormat = SimpleDateFormat.getDateInstance( DateFormat.SHORT, LocaleService.getDefault(  ) );

                String accountMaxValidDate = dateFormat.format( new Date( newExpirationDate.getTime(  ) ) );

                model.put( MARK_DATE_VALID, accountMaxValidDate );
                model.put( MARK_NAME, completeUser.getLastName(  ) );
                model.put( MARK_FIRST_NAME, completeUser.getFirstName(  ) );

                HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( strBody,
                        LocaleService.getDefault(  ), model );
                MailService.sendMailHtml( strUserMail, strSender, strSender, strSubject, template.getHtml(  ) );
            }
        }
    }

    /**
     * Update the date of last login of an admin user
     * @param nIdUser Id of the user to update
     */
    public static void updateDateLastLogin( int nIdUser )
    {
        AdminUserHome.updateDateLastLogin( nIdUser, new Timestamp( new Date(  ).getTime(  ) ) );
    }

    /**
     * Notify an user by email
     * @param strBaseUrl The base URL of the webapp
     * @param user The admin user to notify
     * @param strPropertyEmailSubject the property of the subject email
     * @param strTemplate the URL of the HTML Template
     */
    public static void notifyUser( String strBaseUrl, AdminUser user, String strPropertyEmailSubject, String strTemplate )
    {
        String strSenderEmail = MailService.getNoReplyEmail(  );
        String strSiteName = PortalService.getSiteName(  );
        Locale locale = user.getLocale(  );
        String strEmailSubject = I18nService.getLocalizedString( strPropertyEmailSubject, new String[] { strSiteName },
                locale );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_USER, user );
        model.put( MARK_SITE_NAME, strSiteName );
        model.put( MARK_LOGIN_URL, strBaseUrl + AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ) );
        model.put( MARK_SITE_LINK, MailService.getSiteLink( strBaseUrl, false ) );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, locale, model );

        MailService.sendMailHtml( user.getEmail(  ), strSenderEmail, strSenderEmail, strEmailSubject,
            template.getHtml(  ) );
    }

    /**
     * Get a XML string describing a user.<br />
     * The XML is constructed as follow :<br />
     * <b>&lt;user&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;access_code&gt;</b>value<b>&lt;/value&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;last_name&gt;</b>value<b>&gt;/user&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;first_name&gt;</b>value<b>&lt;/value&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;email&gt;</b>value<b>&lt;/email&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;status&gt;</b>value<b>&lt;/status&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;locale&gt;</b>value<b>&lt;/locale&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;level&gt;</b>value<b>&lt;/level&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;must_change_password&gt;</b>value<b>&lt;
     * must_change_password&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;accessibility_mode&gt;</b>value<b>&lt;
     * accessibility_mode&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;password_max_valid_date&gt;</b>value<b>&lt;
     * password_max_valid_date&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;account_max_valid_date&gt;</b>value<b>&lt;
     * account_max_valid_date&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;date_last_login&gt;</b>value<b>&lt;/date_last_login&gt
     * ;</b><br />
     * &nbsp;&nbsp;<b>&lt;roles&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;role&gt;</b>value<b>&lt;/role&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;...<br />
     * &nbsp;&nbsp;<b>&lt;/roles&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;rights&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;right&gt;</b>value<b>&lt;/right&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;...<br />
     * &nbsp;&nbsp;<b>&lt;/rights&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;workspaces&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;workspace&gt;</b>value<b>&lt;/workspace&gt
     * ;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;...<br />
     * &nbsp;&nbsp;<b>&lt;/workspaces&gt;</b><br />
     * &nbsp;&nbsp;<b>&lt;attributes&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;attribute&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;attribute-id&gt;</b>value<b>&
     * lt;/attribute-id&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;attribute-field-id&gt;</b>
     * value<b>&lt;/attribute-id&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;attribute-value&gt;</b>value<b
     * >&lt;/attribute-value&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;<b>&lt;/attribute&gt;</b><br />
     * &nbsp;&nbsp;&nbsp;&nbsp;...<br />
     * &nbsp;&nbsp;<b>&lt;/attributes&gt;</b><br />
     * <b>&lt;/user&gt;</b><br />
     * <br />
     * Sections <b>roles</b>, <b>rights</b>, <b>workspaces</b> and
     * <b>attributes</b> are not included if data are not imported
     * @param user The user to get the XML description of.
     * @param bIncludeRoles True to include roles of the user in the XML, false
     *            otherwise.
     * @param bIncludeRights True to include rights of the user in the XML,
     *            false otherwise.
     * @param bIncludeWorkgroups True to include workgroups of the user in the
     *            XML, false otherwise.
     * @param bIncludeAttributes True to include attributes of the user in the
     *            XML, false otherwise.
     * @param listAttributes The list of attributes to include in the XML if
     *            attributes are included.
     * @return A string of XML describing the user.
     */
    public static String getXmlFromUser( AdminUser user, boolean bIncludeRoles, boolean bIncludeRights,
        boolean bIncludeWorkgroups, boolean bIncludeAttributes, List<IAttribute> listAttributes )
    {
        StringBuffer sbXml = new StringBuffer(  );
        DateFormat dateFormat = new SimpleDateFormat(  );

        XmlUtil.beginElement( sbXml, CONSTANT_XML_USER );
        XmlUtil.addElement( sbXml, CONSTANT_XML_ACCESS_CODE, user.getAccessCode(  ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_LAST_NAME, user.getLastName(  ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_FIRST_NAME, user.getFirstName(  ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_EMAIL, user.getEmail(  ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_STATUS, Integer.toString( user.getRealStatus(  ) ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_LOCALE, user.getLocale(  ).toString(  ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_LEVEL, Integer.toString( user.getUserLevel(  ) ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_MUST_CHANGE_PASSWORD, Boolean.toString( user.isPasswordReset(  ) ) );
        XmlUtil.addElement( sbXml, CONSTANT_XML_ACCESSIBILITY_MODE, Boolean.toString( user.getAccessibilityMode(  ) ) );

        String strPasswordMaxValidDate = StringUtils.EMPTY;

        if ( user.getPasswordMaxValidDate(  ) != null )
        {
            strPasswordMaxValidDate = dateFormat.format( user.getPasswordMaxValidDate(  ) );
        }

        XmlUtil.addElement( sbXml, CONSTANT_XML_PASSWORD_MAX_VALID_DATE, strPasswordMaxValidDate );

        String strAccountMaxValidDate = StringUtils.EMPTY;

        if ( user.getAccountMaxValidDate(  ) != null )
        {
            strAccountMaxValidDate = dateFormat.format( user.getAccountMaxValidDate(  ) );
        }

        XmlUtil.addElement( sbXml, CONSTANT_XML_ACCOUNT_MAX_VALID_DATE, strAccountMaxValidDate );

        String strDateLastLogin = StringUtils.EMPTY;

        if ( user.getDateLastLogin(  ) != null )
        {
            strDateLastLogin = dateFormat.format( user.getDateLastLogin(  ) );
        }

        XmlUtil.addElement( sbXml, CONSTANT_XML_DATE_LAST_LOGIN, strDateLastLogin );

        if ( bIncludeRoles )
        {
            Map<String, AdminRole> mapRoles = AdminUserHome.getRolesListForUser( user.getUserId(  ) );
            XmlUtil.beginElement( sbXml, CONSTANT_XML_ROLES );

            for ( String strRole : mapRoles.keySet(  ) )
            {
                XmlUtil.addElement( sbXml, CONSTANT_XML_ROLE, strRole );
            }

            XmlUtil.endElement( sbXml, CONSTANT_XML_ROLES );
        }

        if ( bIncludeRights )
        {
            XmlUtil.beginElement( sbXml, CONSTANT_XML_RIGHTS );

            Map<String, Right> mapRights = AdminUserHome.getRightsListForUser( user.getUserId(  ) );

            for ( String strRight : mapRights.keySet(  ) )
            {
                XmlUtil.addElement( sbXml, CONSTANT_XML_RIGHT, strRight );
            }

            XmlUtil.endElement( sbXml, CONSTANT_XML_RIGHTS );
        }

        if ( bIncludeWorkgroups )
        {
            XmlUtil.beginElement( sbXml, CONSTANT_XML_WORKGROUPS );

            ReferenceList refListWorkgroups = AdminWorkgroupHome.getUserWorkgroups( user );

            for ( ReferenceItem refItem : refListWorkgroups )
            {
                XmlUtil.addElement( sbXml, CONSTANT_XML_WORKGROUP, refItem.getCode(  ) );
            }

            XmlUtil.endElement( sbXml, CONSTANT_XML_WORKGROUPS );
        }

        if ( bIncludeAttributes )
        {
            Map<String, Object> mapAttributes = AdminUserFieldService.getAdminUserFields( listAttributes,
                    user.getUserId(  ), LocaleService.getDefault(  ) );
            XmlUtil.beginElement( sbXml, CONSTANT_XML_ATTRIBUTES );

            for ( Entry<String, Object> entry : mapAttributes.entrySet(  ) )
            {
                String strAttributeKey = entry.getKey(  );
                Object value = entry.getValue(  );

                if ( value instanceof List<?> )
                {
                    List<AdminUserField> listFields = (List<AdminUserField>) value;

                    for ( AdminUserField adminUserFields : listFields )
                    {
                        if ( adminUserFields.getIdUserField(  ) > 0 )
                        {
                            XmlUtil.beginElement( sbXml, CONSTANT_XML_ATTRIBUTE );
                            XmlUtil.addElement( sbXml, CONSTANT_XML_ATTRIBUTE_ID, strAttributeKey );
                            XmlUtil.addElement( sbXml, CONSTANT_XML_ATTRIBUTE_FIELD_ID,
                                adminUserFields.getAttributeField(  ).getIdField(  ) );
                            XmlUtil.addElement( sbXml, CONSTANT_XML_ATTRIBUTE_VALUE, adminUserFields.getValue(  ) );
                            XmlUtil.endElement( sbXml, CONSTANT_XML_ATTRIBUTE );
                        }
                    }
                }
            }

            XmlUtil.endElement( sbXml, CONSTANT_XML_ATTRIBUTES );
        }

        XmlUtil.endElement( sbXml, CONSTANT_XML_USER );

        return sbXml.toString(  );
    }
}
