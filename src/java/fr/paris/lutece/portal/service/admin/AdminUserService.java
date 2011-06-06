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
package fr.paris.lutece.portal.service.admin;

import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.regularexpression.RegularExpression;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserFilter;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldFilter;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameter;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.regularexpression.RegularExpressionService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This service provides features concerning the administration users
 */
public final class AdminUserService
{
    // Parameter
    private static final String PARAMETER_ENABLE_PASSWORD_ENCRYPTION = "enable_password_encryption";
    private static final String PARAMETER_ENCRYPTION_ALGORITHM = "encryption_algorithm";
    private static final String PARAMETER_DEFAULT_USER_LEVEL = "default_user_level";
    private static final String PARAMETER_DEFAULT_USER_NOTIFICATION = "default_user_notification";
    private static final String PARAMETER_DEFAULT_USER_LANGUAGE = "default_user_language";
    private static final String PARAMETER_DEFAULT_USER_STATUS = "default_user_status";
    private static final String PARAMETER_EMAIL_PATTERN = "email_pattern";
    private static final String PARAMETER_EMAIL_PATTERN_VERIFY_BY = "email_pattern_verify_by";

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

    // Properties
    private static final String PROPERTY_ADMINISTRATOR = "right.administrator";
    private static final String PROPERTY_ENCRYPTION_ALGORITHMS_LIST = "encryption.algorithmsList";
    private static final String PROPERTY_EMAIL_PATTERN = "lutece.email.pattern";
    private static final String PROPERTY_MESSAGE_EMAIL_FORMAT = "portal.users.message.user.emailFormat";

    // CONSTANTS
    private static final String COMMA = ",";
    private static final String AMPERSAND = "&";
    private static final String ZERO = "0";

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
        // Default value is JVM locale
        Locale locale = Locale.getDefault(  );
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

    // TODO : move somewhere else or could be removed ?
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
    public static List<AdminUser> getFilteredUsersInterface( List<AdminUser> listUsers, HttpServletRequest request,
        Map<String, Object> model, UrlItem url )
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

        List<IAttribute> listAttributes = AttributeHome.findAll( currentUser.getLocale(  ) );

        for ( IAttribute attribute : listAttributes )
        {
            List<AttributeField> listAttributeFields = AttributeFieldHome.selectAttributeFieldsByIdAttribute( attribute.getIdAttribute(  ) );
            attribute.setListAttributeFields( listAttributeFields );
        }

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
                    DefaultUserParameterHome.findByKey( PARAMETER_ENABLE_PASSWORD_ENCRYPTION ).getParameterValue(  ) );
                model.put( MARK_ENCRYPTION_ALGORITHM,
                    DefaultUserParameterHome.findByKey( PARAMETER_ENCRYPTION_ALGORITHM ).getParameterValue(  ) );

                String[] listAlgorithms = AppPropertiesService.getProperty( PROPERTY_ENCRYPTION_ALGORITHMS_LIST )
                                                              .split( COMMA );

                for ( String strAlgorithm : listAlgorithms )
                {
                    strAlgorithm.trim(  );
                }

                model.put( MARK_ENCRYPTION_ALGORITHMS_LIST, listAlgorithms );
            }

            // USER LEVEL 
            String strDefaultLevel = DefaultUserParameterHome.findByKey( PARAMETER_DEFAULT_USER_LEVEL )
                                                             .getParameterValue(  );
            Level defaultLevel = LevelHome.findByPrimaryKey( Integer.parseInt( strDefaultLevel ) );

            // USER NOTIFICATION
            int nDefaultUserNotification = Integer.parseInt( DefaultUserParameterHome.findByKey( 
                        PARAMETER_DEFAULT_USER_NOTIFICATION ).getParameterValue(  ) );

            // USER LANGUAGE
            ReferenceList listLanguages = I18nService.getAdminLocales( user.getLocale(  ) );
            String strDefaultUserLanguage = DefaultUserParameterHome.findByKey( PARAMETER_DEFAULT_USER_LANGUAGE )
                                                                    .getParameterValue(  );

            // USER STATUS
            int nDefaultUserStatus = Integer.parseInt( DefaultUserParameterHome.findByKey( 
                        PARAMETER_DEFAULT_USER_STATUS ).getParameterValue(  ) );

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
        }

        return model;
    }

    /**
     * Check if the given email is valid or not.
     * <br />
     * The given email is compared to the value of the parameter <i>'core_user_parameter.email_pattern'</i>.
     * @param strEmail
     * @return
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
            DefaultUserParameter emailPattern = new DefaultUserParameter( PARAMETER_EMAIL_PATTERN, strEmailPattern );
            DefaultUserParameter emailPatternVerifyBy = new DefaultUserParameter( PARAMETER_EMAIL_PATTERN_VERIFY_BY,
                    StringUtils.EMPTY );
            DefaultUserParameterHome.update( emailPattern );
            DefaultUserParameterHome.update( emailPatternVerifyBy );
        }
        else
        {
            if ( isEmailPatternSetManually(  ) )
            {
                // If the previous email pattern is set manually, then the parameter email_pattern_verify_by is set at 0
                // This way, the interface know the email pattern is not set manually
                // Indeed, the control is set on the content of the parameter 'email_pattern_verify_by'
                DefaultUserParameter emailPatternVerifyBy = new DefaultUserParameter( PARAMETER_EMAIL_PATTERN_VERIFY_BY,
                        ZERO );
                DefaultUserParameterHome.update( emailPatternVerifyBy );
            }
        }
    }

    /**
     * Reset the email pattern by putting the default email pattern
     * that is set in the <b>lutece.properties</b>.
     */
    public static void doResetEmailPattern(  )
    {
        DefaultUserParameter emailPattern = new DefaultUserParameter( PARAMETER_EMAIL_PATTERN,
                getDefaultEmailPattern(  ) );
        DefaultUserParameterHome.update( emailPattern );
    }

    /**
     * Get the email error message url
     * @return the error message
     */
    public static String getEmailErrorMessageUrl( HttpServletRequest request )
    {
        String strMessage = StringUtils.EMPTY;

        if ( isEmailPatternSetManually(  ) )
        {
            strMessage = getEmailPattern(  );
        }
        else
        {
            StringBuilder sbMessage = new StringBuilder(  );
            DefaultUserParameter emailPatternVerifyBy = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.getParameterValue(  ).split( COMMA );

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

        Object[] param = { strMessage };

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_EMAIL_FORMAT, param, AdminMessage.TYPE_STOP );
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
            DefaultUserParameter emailPatternVerifyBy = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.getParameterValue(  ).split( COMMA );

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
                String strRegularExpressionIds = emailPatternVerifyBy.getParameterValue(  ) + COMMA +
                    nRegularExpressionId;
                emailPatternVerifyBy.setParameterValue( strRegularExpressionIds );
                DefaultUserParameterHome.update( emailPatternVerifyBy );
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
            DefaultUserParameter emailPatternVerifyBy = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.getParameterValue(  ).split( COMMA );

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

            emailPatternVerifyBy.setParameterValue( sbRegularExpressionIds.toString(  ) );
            DefaultUserParameterHome.update( emailPatternVerifyBy );
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
     * Get the AdminUser email pattern that is stored in <b>'core_user_parameter.email_pattern'</b>.
     * <br />
     * If it does not exist, then it will retrieve the value in the <b>lutece.properties</b>
     * file (parameter <b>email.pattern</b>)
     * @return the AdminUser email pattern
     */
    private static String getEmailPattern(  )
    {
        String strEmailPattern = getDefaultEmailPattern(  );
        DefaultUserParameter emailPattern = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN );

        if ( emailPattern != null )
        {
            strEmailPattern = emailPattern.getParameterValue(  );
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
            DefaultUserParameter emailPatternVerifyBy = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.getParameterValue(  ).split( COMMA );

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
            DefaultUserParameter emailPatternVerifyBy = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN_VERIFY_BY );
            String[] regularExpressionIds = emailPatternVerifyBy.getParameterValue(  ).split( COMMA );

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
            DefaultUserParameter emailPatternVerifyBy = DefaultUserParameterHome.findByKey( PARAMETER_EMAIL_PATTERN_VERIFY_BY );

            if ( ( emailPatternVerifyBy != null ) &&
                    StringUtils.isNotBlank( emailPatternVerifyBy.getParameterValue(  ) ) )
            {
                bIsSetManually = false;
            }
        }

        return bIsSetManually;
    }
}
