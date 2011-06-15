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
package fr.paris.lutece.portal.web.user;

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeField;
import fr.paris.lutece.portal.business.user.attribute.AttributeFieldHome;
import fr.paris.lutece.portal.business.user.attribute.AttributeHome;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameter;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.CryptoService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.password.PasswordUtil;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage app user features ( manage, create, modify, remove, ... )
 */
public class AdminUserJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String CONSTANTE_UN = "1";
    private static final String CONSTANT_DEFAULT_ALGORITHM = "noValue";
    private static final String CONSTANT_EMPTY_STRING = "";

    // I18n message keys
    private static final String MESSAGE_EMAIL_SUBJECT = "portal.admin.admin_forgot_password.email.subject";

    // Templates
    private static final String TEMPLATE_MANAGE_USERS = "admin/user/manage_users.html";
    private static final String TEMPLATE_CREATE_USER = "admin/user/create_user.html";
    private static final String TEMPLATE_MODIFY_USER = "admin/user/modify_user.html";
    private static final String TEMPLATE_MANAGE_USER_RIGHTS = "admin/user/manage_user_rights.html";
    private static final String TEMPLATE_MODIFY_USER_RIGHTS = "admin/user/modify_user_rights.html";
    private static final String TEMPLATE_MANAGE_USER_ROLES = "admin/user/manage_user_roles.html";
    private static final String TEMPLATE_MODIFY_USER_ROLES = "admin/user/modify_user_roles.html";
    private static final String TEMPLATE_IMPORT_USER = "admin/user/import_module_user.html";
    private static final String TEMPLATE_DEFAULT_CREATE_USER = "admin/user/create_user_default_module.html";
    private static final String TEMPLATE_DEFAULT_MODIFY_USER = "admin/user/modify_user_default_module.html";
    private static final String TEMPLATE_MANAGE_USER_WORKGROUPS = "admin/user/manage_user_workgroups.html";
    private static final String TEMPLATE_MODIFY_USER_WORKGROUPS = "admin/user/modify_user_workgroups.html";
    private static final String TEMPLATE_ADMIN_EMAIL_CHANGE_STATUS = "admin/user/user_email_change_status.html";
    private static final String TEMPLATE_NOTIFY_USER = "admin/user/notify_user.html";
    private static final String TEMPLATE_MANAGE_ADVANCED_PARAMETERS = "admin/user/manage_advanced_parameters.html";
    private static final String TEMPLATE_ADMIN_EMAIL_FORGOT_PASSWORD = "admin/admin_email_forgot_password.html";

    // Messages
    private static final String PROPERTY_MANAGE_USERS_PAGETITLE = "portal.users.manage_users.pageTitle";
    private static final String PROPERTY_MODIFY_USER_PAGETITLE = "portal.users.modify_user.pageTitle";
    private static final String PROPERTY_CREATE_USER_PAGETITLE = "portal.users.create_user.pageTitle";
    private static final String PROPERTY_IMPORT_MODULE_USER_PAGETITLE = "portal.users.import_module_user.pageTitle";
    private static final String PROPERTY_MANAGE_USER_RIGHTS_PAGETITLE = "portal.users.manage_user_rights.pageTitle";
    private static final String PROPERTY_MODIFY_USER_RIGHTS_PAGETITLE = "portal.users.modify_user_rights.pageTitle";
    private static final String PROPERTY_MANAGE_USER_ROLES_PAGETITLE = "portal.users.manage_user_roles.pageTitle";
    private static final String PROPERTY_MODIFY_USER_ROLES_PAGETITLE = "portal.users.modify_user_roles.pageTitle";
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE = "portal.users.message.confirmRemoveUser";
    private static final String PROPERTY_USERS_PER_PAGE = "paginator.user.itemsPerPage";
    private static final String PROPERTY_DELEGATE_USER_RIGHTS_PAGETITLE = "portal.users.delegate_user_rights.pageTitle";
    private static final String PROPERTY_MANAGE_USER_WORKGROUPS_PAGETITLE = "portal.users.manage_user_workgroups.pageTitle";
    private static final String PROPERTY_MODIFY_USER_WORKGROUPS_PAGETITLE = "portal.users.modify_user_workgroups.pageTitle";
    private static final String PROPERTY_MESSAGE_ACCESS_CODE_ALREADY_USED = "portal.users.message.user.accessCodeAlreadyUsed";
    private static final String PROPERTY_MESSAGE_EMAIL_ALREADY_USED = "portal.users.message.user.accessEmailUsed";
    private static final String PROPERTY_MESSAGE_DIFFERENTS_PASSWORD = "portal.users.message.differentsPassword";
    private static final String PROPERTY_MESSAGE_EMAIL_SUBJECT_CHANGE_STATUS = "portal.users.user_change_status.email.subject";
    private static final String PROPERTY_MESSAGE_EMAIL_SUBJECT_NOTIFY_USER = "portal.users.notify_user.email.subject";
    private static final String PROPERTY_MANAGE_ADVANCED_PARAMETERS_PAGETITLE = "portal.users.manage_advanced_parameters.pageTitle";
    private static final String PROPERTY_MESSAGE_CONFIRM_MODIFY_PASSWORD_ENCRYPTION = "portal.users.manage_advanced_parameters.message.confirmModifyPasswordEncryption";
    private static final String PROPERTY_MESSAGE_NO_CHANGE_PASSWORD_ENCRYPTION = "portal.users.manage_advanced_parameters.message.noChangePasswordEncryption";
    private static final String PROPERTY_MESSAGE_INVALID_ENCRYPTION_ALGORITHM = "portal.users.manage_advanced_parameters.message.invalidEncryptionAlgorithm";
    private static final String PROPERTY_MESSAGE_ERROR_EMAIL_PATTERN = "portal.users.manage_advanced_parameters.message.errorEmailPattern";

    // Properties
    private static final String PROPERTY_NO_REPLY_EMAIL = "mail.noreply.email";
    private static final String PROPERTY_SITE_NAME = "lutece.name";

    // Parameters
    private static final String PARAMETER_ACCESS_CODE = "access_code";
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_NOTIFY_USER = "notify_user";
    private static final String PARAMETER_STATUS = "status";
    private static final String PARAMETER_USER_ID = "id_user";
    private static final String PARAMETER_ROLE = "roles";
    private static final String PARAMETER_RIGHT = "right";
    private static final String PARAMETER_FIRST_PASSWORD = "first_password";
    private static final String PARAMETER_SECOND_PASSWORD = "second_password";
    private static final String PARAMETER_LANGUAGE = "language";
    private static final String PARAMETER_DELEGATE_RIGHTS = "delegate_rights";
    private static final String PARAMETER_USER_LEVEL = "user_level";
    private static final String PARAMETER_WORKGROUP = "workgroup";
    private static final String PARAMETER_SELECT = "select";
    private static final String PARAMETER_SELECT_ALL = "all";
    private static final String PARAMETER_ENCRYPTION_ALGORITHM = "encryption_algorithm";
    private static final String PARAMETER_ACCESSIBILITY_MODE = "accessibility_mode";
    private static final String PARAMETER_EMAIL_PATTERN = "email_pattern";
    private static final String PARAMETER_DEFAULT_USER_STATUS = "default_user_status";
    private static final String PARAMETER_ENABLE_PASSWORD_ENCRYPTION = "enable_password_encryption";
    private static final String PARAMETER_DEFAULT_USER_LEVEL = "default_user_level";
    private static final String PARAMETER_DEFAULT_USER_NOTIFICATION = "default_user_notification";
    private static final String PARAMETER_DEFAULT_USER_LANGUAGE = "default_user_language";
    private static final String PARAMETER_IS_EMAIL_PATTERN_SET_MANUALLY = "is_email_pattern_set_manually";
    private static final String PARAMETER_ID_EXPRESSION = "id_expression";

    // Jsp url
    private static final String JSP_MANAGE_USER_RIGHTS = "ManageUserRights.jsp";
    private static final String JSP_MANAGE_USER_ROLES = "ManageUserRoles.jsp";
    private static final String JSP_MANAGE_USER = "ManageUsers.jsp";
    private static final String JSP_MANAGE_USER_WORKGROUPS = "ManageUserWorkgroups.jsp";
    private static final String JSP_MANAGE_ADVANCED_PARAMETERS = "ManageAdvancedParameters.jsp";
    private static final String JSP_URL_REMOVE_USER = "jsp/admin/user/DoRemoveUser.jsp";
    private static final String JSP_URL_CREATE_USER = "jsp/admin/user/CreateUser.jsp";
    private static final String JSP_URL_IMPORT_USER = "jsp/admin/user/ImportUser.jsp";
    private static final String JSP_URL_MANAGE_ADVANCED_PARAMETERS = "jsp/admin/user/ManageAdvancedParameters.jsp";
    private static final String JSP_URL_MODIFY_PASSWORD_ENCRYPTION = "jsp/admin/user/DoModifyPasswordEncryption.jsp";
    private static final String JSP_URL_MODIFY_USER = "jsp/admin/user/ModifyUser.jsp";
    private static final String JSP_URL_MANAGE_USER_RIGHTS = "jsp/admin/user/ManageUserRights.jsp";
    private static final String JSP_URL_MANAGE_USER_ROLES = "jsp/admin/user/ManageUserRoles.jsp";
    private static final String JSP_URL_MANAGE_USER_WORKGROUPS = "jsp/admin/user/ManageUserWorkgroups.jsp";

    // Markers
    private static final String MARK_USER_LIST = "user_list";
    private static final String MARK_IMPORT_USER_LIST = "import_user_list";
    private static final String MARK_ACCESS_CODE = "access_code";
    private static final String MARK_LAST_NAME = "last_name";
    private static final String MARK_FIRST_NAME = "first_name";
    private static final String MARK_EMAIL = "email";
    private static final String MARK_IMPORT_USER = "import_user";
    private static final String MARK_USER = "user";
    private static final String MARK_LEVEL = "level";
    private static final String MARK_USER_RIGHT_LIST = "user_right_list";
    private static final String MARK_ALL_ROLE_LIST = "all_role_list";
    private static final String MARK_USER_ROLE_LIST = "user_role_list";
    private static final String MARK_ALL_RIGHT_LIST = "all_right_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_LANGUAGES_LIST = "languages_list";
    private static final String MARK_CURRENT_LANGUAGE = "current_language";
    private static final String MARK_USER_CREATION_URL = "url_user_creation";
    private static final String MARK_CAN_DELEGATE = "can_delegate";
    private static final String MARK_CAN_MODIFY = "can_modify";
    private static final String MARK_USER_LEVELS_LIST = "user_levels";
    private static final String MARK_CURRENT_USER = "current_user";
    private static final String MARK_USER_WORKGROUP_LIST = "user_workgroup_list";
    private static final String MARK_ALL_WORKSGROUP_LIST = "all_workgroup_list";
    private static final String MARK_SELECT_ALL = "select_all";
    private static final String MARK_SITE_NAME = "site_name";
    private static final String MARK_LOGIN_URL = "login_url";
    private static final String MARK_NEW_PASSWORD = "new_password";
    private static final String MARK_PERMISSION_ADVANCED_PARAMETER = "permission_advanced_parameter";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator";
    private static final String MARK_ATTRIBUTES_LIST = "attributes_list";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_MAP_LIST_ATTRIBUTE_DEFAULT_VALUES = "map_list_attribute_default_values";
    private static final String MARK_DEFAULT_USER_LEVEL = "default_user_level";
    private static final String MARK_DEFAULT_USER_NOTIFICATION = "default_user_notification";
    private static final String MARK_DEFAULT_USER_LANGUAGE = "default_user_language";
    private static final String MARK_DEFAULT_USER_STATUS = "default_user_status";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Build the User list
     *
     * @param request Http Request
     * @return the AppUser list
     */
    public String getManageAdminUsers( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_USERS_PAGETITLE );

        String strCreateUrl;
        AdminUser currentUser = getUser(  );

        Map<String, Object> model = new HashMap<String, Object>(  );

        // creation in no-module mode : no import
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            strCreateUrl = JSP_URL_CREATE_USER;
        }
        else
        {
            strCreateUrl = JSP_URL_IMPORT_USER;
        }

        String strURL = getHomeUrl( request );
        UrlItem url = new UrlItem( strURL );

        List<AdminUser> listUsers = (List<AdminUser>) AdminUserHome.findUserList(  );
        List<AdminUser> availableUsers = AdminUserService.getFilteredUsersInterface( listUsers, request, model, url );
        listUsers = new ArrayList<AdminUser>(  );

        for ( AdminUser user : availableUsers )
        {
            if ( currentUser.isAdmin(  ) ||
                    ( currentUser.isParent( user ) &&
                    ( ( haveCommonWorkgroups( currentUser, user ) ) ||
                    ( !AdminWorkgroupHome.checkUserHasWorkgroup( user.getUserId(  ) ) ) ) ) )
            {
                listUsers.add( user );
            }
        }

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listUsers, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        // PAGINATOR
        LocalizedPaginator paginator = new LocalizedPaginator( listUsers, _nItemsPerPage, url.getUrl(  ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale(  ) );

        // USER LEVEL
        Collection<Level> filteredLevels = new ArrayList<Level>(  );

        for ( Level level : LevelHome.getLevelsList(  ) )
        {
            if ( currentUser.isAdmin(  ) || currentUser.hasRights( level.getId(  ) ) )
            {
                filteredLevels.add( level );
            }
        }

        boolean bPermissionAdvancedParameter = RBACService.isAuthorized( AdminUser.RESOURCE_TYPE,
                RBAC.WILDCARD_RESOURCES_ID, AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS,
                getUser(  ) );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_USER_LEVELS_LIST, filteredLevels );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_USER_LIST, paginator.getPageItems(  ) );
        model.put( MARK_USER_CREATION_URL, strCreateUrl );
        model.put( MARK_PERMISSION_ADVANCED_PARAMETER, bPermissionAdvancedParameter );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Display the page for user import. This page is used in module mode to link a user to its
     * code in the module (for later authentication) and to populate the creation form with the data
     * the module is able to provide.
     * @param request the http request
     * @return the html code for the import page
     */
    public String getFindImportAdminUser( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_IMPORT_MODULE_USER_PAGETITLE );

        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );

        Map<String, Object> model = new HashMap<String, Object>(  );
        Collection<?> allImportUsers = null;

        if ( !( ( strLastName == null ) && ( strFirstName == null ) && ( strEmail == null ) ) ) // at least 1 criteria check
        {
            if ( !( strLastName.equals( "" ) && strFirstName.equals( "" ) && strEmail.equals( "" ) ) )
            {
                allImportUsers = AdminAuthenticationService.getInstance(  )
                                                           .getUserListFromModule( strLastName, strFirstName, strEmail );
            }
        }

        model.put( MARK_IMPORT_USER_LIST, allImportUsers );
        model.put( MARK_ACCESS_CODE, strAccessCode );
        model.put( MARK_LAST_NAME, strLastName );
        model.put( MARK_FIRST_NAME, strFirstName );
        model.put( MARK_EMAIL, strEmail );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_USER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Performs checks on the selected user to import and redirects on the creation form.
     * This page is used in module mode.
     * @param request The HTTP Request
     * @return The Jsp URL of the creation form if check ok, an error page url otherwise
     */
    public String doSelectImportUser( HttpServletRequest request )
    {
        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );

        if ( ( strAccessCode == null ) || ( strAccessCode.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // check that access code is not in use
        if ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) != -1 )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ACCESS_CODE_ALREADY_USED,
                AdminMessage.TYPE_STOP );
        }

        return AppPathService.getBaseUrl( request ) + JSP_URL_CREATE_USER + "?" + PARAMETER_ACCESS_CODE + "=" +
        strAccessCode;
    }

    /**
     * Returns the data capture form of a new User
     *
     * @param request The HTTP Request
     * @return The HTML form
     */
    public String getCreateAdminUser( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_USER_PAGETITLE );

        HtmlTemplate template;
        AdminUser currentUser = getUser(  );
        Collection<Level> filteredLevels = new ArrayList<Level>(  );

        for ( Level level : LevelHome.getLevelsList(  ) )
        {
            if ( currentUser.isAdmin(  ) || currentUser.hasRights( level.getId(  ) ) )
            {
                filteredLevels.add( level );
            }
        }

        // Default user parameter values
        String strDefaultLevel = DefaultUserParameterHome.findByKey( PARAMETER_DEFAULT_USER_LEVEL ).getParameterValue(  );
        Level defaultLevel = LevelHome.findByPrimaryKey( Integer.parseInt( strDefaultLevel ) );
        int nDefaultUserNotification = Integer.parseInt( DefaultUserParameterHome.findByKey( 
                    PARAMETER_DEFAULT_USER_NOTIFICATION ).getParameterValue(  ) );
        String strDefaultUserLanguage = DefaultUserParameterHome.findByKey( PARAMETER_DEFAULT_USER_LANGUAGE )
                                                                .getParameterValue(  );
        int nDefaultUserStatus = Integer.parseInt( DefaultUserParameterHome.findByKey( PARAMETER_DEFAULT_USER_STATUS )
                                                                           .getParameterValue(  ) );

        // Specific attributes
        List<IAttribute> listAttributes = AttributeService.getInstance(  ).getAllAttributesWithFields( getLocale(  ) );

        // creation in no-module mode : load empty form
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            Map<String, Object> model = new HashMap<String, Object>(  );

            model.put( MARK_USER_LEVELS_LIST, filteredLevels );
            model.put( MARK_CURRENT_USER, currentUser );
            model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( getLocale(  ) ) );
            model.put( MARK_DEFAULT_USER_LEVEL, defaultLevel );
            model.put( MARK_DEFAULT_USER_NOTIFICATION, nDefaultUserNotification );
            model.put( MARK_DEFAULT_USER_LANGUAGE, strDefaultUserLanguage );
            model.put( MARK_DEFAULT_USER_STATUS, nDefaultUserStatus );
            model.put( MARK_ATTRIBUTES_LIST, listAttributes );
            model.put( MARK_LOCALE, getLocale(  ) );
            template = AppTemplateService.getTemplate( TEMPLATE_DEFAULT_CREATE_USER, getLocale(  ), model );
        }
        else // creation in module mode : populate the form with the data from the user selected for import
        {
            // parameters retrieved from the "import" action (retrieves the data from the access code)
            String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
            AdminUser user = null;

            if ( ( strAccessCode != null ) && ( !strAccessCode.equals( "" ) ) )
            {
                user = AdminAuthenticationService.getInstance(  ).getUserPublicDataFromModule( strAccessCode );
            }

            Map<String, Object> model = new HashMap<String, Object>(  );

            if ( user != null )
            {
                model.put( MARK_USER_LEVELS_LIST, filteredLevels );
                model.put( MARK_CURRENT_USER, currentUser );
                model.put( MARK_IMPORT_USER, user );
                model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale(  ) ) );
                model.put( MARK_DEFAULT_USER_LEVEL, defaultLevel );
                model.put( MARK_DEFAULT_USER_NOTIFICATION, nDefaultUserNotification );
                model.put( MARK_DEFAULT_USER_LANGUAGE, strDefaultUserLanguage );
                model.put( MARK_DEFAULT_USER_STATUS, nDefaultUserStatus );
                model.put( MARK_ATTRIBUTES_LIST, listAttributes );
                model.put( MARK_LOCALE, getLocale(  ) );
            }

            template = AppTemplateService.getTemplate( TEMPLATE_CREATE_USER, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new appUser
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     */
    public String doCreateAdminUser( HttpServletRequest request )
    {
        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strStatus = request.getParameter( PARAMETER_STATUS );
        String strUserLevel = request.getParameter( PARAMETER_USER_LEVEL );
        String strNotifyUser = request.getParameter( PARAMETER_NOTIFY_USER );
        String strAccessibilityMode = request.getParameter( PARAMETER_ACCESSIBILITY_MODE );

        if ( ( strAccessCode == null ) || ( strAccessCode.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strLastName == null ) || ( strLastName.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strFirstName == null ) || ( strFirstName.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( ( strEmail == null ) || ( strEmail.trim(  ).equals( "" ) ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !AdminUserService.checkEmail( strEmail ) )
        {
            return AdminUserService.getEmailErrorMessageUrl( request );
        }

        // check again that access code is not in use
        if ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) != -1 )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ACCESS_CODE_ALREADY_USED,
                AdminMessage.TYPE_STOP );
        }

        // check again that email is not in use
        if ( AdminUserHome.checkEmailAlreadyInUse( strEmail ) != -1 )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_EMAIL_ALREADY_USED,
                AdminMessage.TYPE_STOP );
        }

        // defines the new created user level
        int nNewUserLevel = Integer.valueOf( strUserLevel );

        // check if the user is still an admin
        if ( !( getUser(  ).hasRights( nNewUserLevel ) || getUser(  ).isAdmin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        // creation in no-module mode : we manage the password
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            LuteceDefaultAdminUser user = new LuteceDefaultAdminUser(  );
            String strFirstPassword = request.getParameter( PARAMETER_FIRST_PASSWORD );
            String strSecondPassword = request.getParameter( PARAMETER_SECOND_PASSWORD );

            if ( ( strFirstPassword == null ) || ( strFirstPassword.equals( CONSTANT_EMPTY_STRING ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            if ( !strFirstPassword.equals( strSecondPassword ) )
            {
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_DIFFERENTS_PASSWORD,
                    AdminMessage.TYPE_STOP );
            }

            // Encryption password
            if ( Boolean.valueOf( DefaultUserParameterHome.findByKey( PARAMETER_ENABLE_PASSWORD_ENCRYPTION )
                                                              .getParameterValue(  ) ) )
            {
                String strAlgorithm = DefaultUserParameterHome.findByKey( PARAMETER_ENCRYPTION_ALGORITHM )
                                                              .getParameterValue(  );
                strFirstPassword = CryptoService.encrypt( strFirstPassword, strAlgorithm );
            }

            user.setPassword( strFirstPassword );

            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );

            user.setUserLevel( nNewUserLevel );
            user.setAccessibilityMode( strAccessibilityMode != null );

            String strError = AdminUserFieldService.checkUserFields( request, getLocale(  ) );

            if ( strError != null )
            {
                return strError;
            }

            AdminUserHome.create( user );
            AdminUserFieldService.doCreateUserFields( user, request, getLocale(  ) );

            if ( ( strNotifyUser != null ) && strNotifyUser.equals( CONSTANTE_UN ) )
            {
                //Notify user for the creation of this account
                notifyUser( request, user, PROPERTY_MESSAGE_EMAIL_SUBJECT_NOTIFY_USER, TEMPLATE_NOTIFY_USER );
            }
        }
        else
        {
            AdminUser user = new AdminUser(  );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );

            user.setUserLevel( nNewUserLevel );
            user.setAccessibilityMode( strAccessibilityMode != null );

            String strError = AdminUserFieldService.checkUserFields( request, getLocale(  ) );

            if ( strError != null )
            {
                return strError;
            }

            AdminUserHome.create( user );
            AdminUserFieldService.doCreateUserFields( user, request, getLocale(  ) );
        }

        return JSP_MANAGE_USER;
    }

    /**
     * Returns the form to update info about a AppUser
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyAdminUser( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_USER_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );

        int nUserId = Integer.parseInt( strUserId );

        Map<String, Object> model = new HashMap<String, Object>(  );
        HtmlTemplate template;

        AdminUser user = null;
        String strTemplateUrl = "";

        // creation in no-module mode : load form with password modification field and login modification field
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );
            strTemplateUrl = TEMPLATE_DEFAULT_MODIFY_USER;
        }
        else
        {
            user = AdminUserHome.findByPrimaryKey( nUserId );
            strTemplateUrl = TEMPLATE_MODIFY_USER;
        }

        Level level = LevelHome.findByPrimaryKey( user.getUserLevel(  ) );

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == user.getUserId(  ) )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_USER;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        List<IAttribute> listAttributes = AttributeService.getInstance(  ).getAllAttributesWithFields( getLocale(  ) );
        Map<String, Object> map = AdminUserFieldService.getAdminUserFields( listAttributes, nUserId, getLocale(  ) );
        
        model.put( MARK_USER, user );
        model.put( MARK_LEVEL, level );
        model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale(  ) ) );
        model.put( MARK_CURRENT_LANGUAGE, user.getLocale(  ).getLanguage(  ) );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );
        model.put( MARK_ATTRIBUTES_LIST, listAttributes );
        model.put( MARK_LOCALE, getLocale(  ) );
        model.put( MARK_MAP_LIST_ATTRIBUTE_DEFAULT_VALUES, map );

        template = AppTemplateService.getTemplate( strTemplateUrl, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the change form of an appUser
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyAdminUser( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strStatus = request.getParameter( PARAMETER_STATUS );
        String strAccessibilityMode = request.getParameter( PARAMETER_ACCESSIBILITY_MODE );

        if ( ( strAccessCode == null ) || ( strAccessCode.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strLastName == null ) || ( strLastName.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strFirstName == null ) || ( strFirstName.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( ( strEmail == null ) || ( strEmail.trim(  ).equals( "" ) ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !AdminUserService.checkEmail( strEmail ) )
        {
            return AdminUserService.getEmailErrorMessageUrl( request );
        }

        int nUserId = Integer.parseInt( strUserId );

        // check again that access code is not in use
        if ( ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) != -1 ) &&
                ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) != nUserId ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ACCESS_CODE_ALREADY_USED,
                AdminMessage.TYPE_STOP );
        }

        // check again that email is not in use
        if ( ( AdminUserHome.checkEmailAlreadyInUse( strEmail ) != -1 ) &&
                ( AdminUserHome.checkEmailAlreadyInUse( strEmail ) != nUserId ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_EMAIL_ALREADY_USED,
                AdminMessage.TYPE_STOP );
        }

        // modification in no-module mode : we manage the password
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            LuteceDefaultAdminUser user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );

            String strFirstPassword = request.getParameter( PARAMETER_FIRST_PASSWORD );
            String strSecondPassword = request.getParameter( PARAMETER_SECOND_PASSWORD );

            if ( ( strFirstPassword != null ) && ( strFirstPassword.equals( "" ) ) && ( strSecondPassword != null ) &&
                    ( !strSecondPassword.equals( "" ) ) )
            {
                // First password is empty but second password is filled
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_DIFFERENTS_PASSWORD,
                    AdminMessage.TYPE_STOP );
            }

            if ( ( strSecondPassword != null ) && ( strSecondPassword.equals( "" ) ) && ( strFirstPassword != null ) &&
                    !strFirstPassword.equals( "" ) )
            {
                // First password is filled but second password is empty
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_DIFFERENTS_PASSWORD,
                    AdminMessage.TYPE_STOP );
            }

            if ( !strFirstPassword.equals( strSecondPassword ) )
            {
                // First and second password are filled but there are different
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_DIFFERENTS_PASSWORD,
                    AdminMessage.TYPE_STOP );
            }

            if ( ( strFirstPassword != null ) && !strFirstPassword.equals( "" ) )
            {
                // Encryption password
                if ( Boolean.valueOf( DefaultUserParameterHome.findByKey( PARAMETER_ENABLE_PASSWORD_ENCRYPTION )
                                                                  .getParameterValue(  ) ) )
                {
                    String strAlgorithm = DefaultUserParameterHome.findByKey( PARAMETER_ENCRYPTION_ALGORITHM )
                                                                  .getParameterValue(  );
                    strFirstPassword = CryptoService.encrypt( strFirstPassword, strAlgorithm );
                }

                user.setPassword( strFirstPassword );
            }

            user.setUserId( nUserId );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );

            int nStatus = Integer.parseInt( strStatus );

            if ( nStatus != user.getStatus(  ) )
            {
                user.setStatus( nStatus );
                notifyUser( request, user, PROPERTY_MESSAGE_EMAIL_SUBJECT_CHANGE_STATUS,
                    TEMPLATE_ADMIN_EMAIL_CHANGE_STATUS );
            }

            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );
            user.setAccessibilityMode( strAccessibilityMode != null );

            String strError = AdminUserFieldService.checkUserFields( request, getLocale(  ) );

            if ( strError != null )
            {
                return strError;
            }

            AdminUserHome.update( user );

            AdminUserFieldService.doModifyUserFields( user, request, getLocale(  ), getUser(  ) );
        }
        else
        {
            AdminUser user = new AdminUser(  );
            user.setUserId( nUserId );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );
            user.setAccessibilityMode( strAccessibilityMode != null );

            String strError = AdminUserFieldService.checkUserFields( request, getLocale(  ) );

            if ( strError != null )
            {
                return strError;
            }

            AdminUserHome.update( user );

            AdminUserFieldService.doModifyUserFields( user, request, getLocale(  ), getUser(  ) );
        }

        return JSP_MANAGE_USER;
    }

    /**
     * Notify an user by email
     * @param request Http Request}
     * @param user The admin user to notify
     * @param strPropertyEmailSubject the property of the subject email
     * @param strTemplate the URL of the HTML Template
     */
    private void notifyUser( HttpServletRequest request, AdminUser user, String strPropertyEmailSubject,
        String strTemplate )
    {
        String strSenderEmail = AppPropertiesService.getProperty( PROPERTY_NO_REPLY_EMAIL );
        String strSiteName = AppPropertiesService.getProperty( PROPERTY_SITE_NAME );

        Locale locale;

        if ( user.getLocale(  ) != null )
        {
            locale = user.getLocale(  );
        }
        else
        {
            locale = getLocale(  );
        }

        String strEmailSubject = I18nService.getLocalizedString( strPropertyEmailSubject, new String[] { strSiteName },
                locale );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_USER, user );
        model.put( MARK_SITE_NAME, strSiteName );
        model.put( MARK_LOGIN_URL,
            AppPathService.getBaseUrl( request ) + AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, locale, model );

        MailService.sendMailHtml( user.getEmail(  ), strSenderEmail, strSenderEmail, strEmailSubject,
            template.getHtml(  ) );
    }

    /**
     * Returns the page of confirmation for deleting a provider
     *
     * @param request The Http Request
     * @return the confirmation url
     */
    public String doConfirmRemoveAdminUser( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        String strUrlRemove = JSP_URL_REMOVE_USER + "?" + PARAMETER_USER_ID + "=" + nUserId;

        String strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE, strUrlRemove,
                AdminMessage.TYPE_CONFIRMATION );

        return strUrl;
    }

    /**
     * Process to the confirmation of deleting of an AppUser
     *
     * @param request The Http Request
     * @return the HTML page
     */
    public String doRemoveAdminUser( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUserFieldService.doRemoveUserFields( user, request, getLocale(  ) );
        AdminUserHome.removeAllRightsForUser( nUserId );
        AdminUserHome.removeAllRolesForUser( nUserId );
        AdminUserHome.remove( nUserId );

        return JSP_MANAGE_USER;
    }

    /**
     * Build the User right list
     *
     * @param request Http Request
     * @return the right list
     */
    public String getManageAdminUserRights( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_USER_RIGHTS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        Collection<Right> rightList = AdminUserHome.getRightsListForUser( nUserId ).values(  );

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == nUserId )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_RIGHTS;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_CAN_MODIFY, getUser(  ).isParent( selectedUser ) || getUser(  ).isAdmin(  ) );
        model.put( MARK_CAN_DELEGATE, getUser(  ).getUserId(  ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_RIGHT_LIST, I18nService.localizeCollection( rightList, getLocale(  ) ) );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USER_RIGHTS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Build the User workgroup list
     *
     * @param request Http Request
     * @return the right list
     */
    public String getManageAdminUserWorkgroups( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_USER_WORKGROUPS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        ReferenceList workgroupsList = AdminWorkgroupHome.getUserWorkgroups( selectedUser );

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == nUserId )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_WORKGROUPS;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        //  ReferenceList assignableWorkgroupsList = AdminWorkgroupHome.getUserWorkgroups( selectedUser );
        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_CAN_MODIFY, getUser(  ).isParent( selectedUser ) || getUser(  ).isAdmin(  ) );
        model.put( MARK_CAN_DELEGATE, getUser(  ).getUserId(  ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_WORKGROUP_LIST, workgroupsList );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USER_WORKGROUPS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Build the assignable workgroups list
     *
     * @param request Http Request
     * @return the right list
     */
    public String getModifyAdminUserWorkgroups( HttpServletRequest request )
    {
        boolean bDelegateWorkgroups = Boolean.valueOf( request.getParameter( PARAMETER_DELEGATE_RIGHTS ) );
        setPageTitleProperty( PROPERTY_MODIFY_USER_WORKGROUPS_PAGETITLE );

        setPageTitleProperty( bDelegateWorkgroups ? PROPERTY_DELEGATE_USER_RIGHTS_PAGETITLE
                                                  : PROPERTY_MODIFY_USER_RIGHTS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = getUser(  );

        ReferenceList userWorkspaces = AdminWorkgroupHome.getUserWorkgroups( user );
        ReferenceList assignableWorkspaces = AdminWorkgroupHome.getUserWorkgroups( currentUser );

        ArrayList<String> checkedValues = new ArrayList<String>(  );

        for ( ReferenceItem item : userWorkspaces )
        {
            checkedValues.add( item.getCode(  ) );
        }

        assignableWorkspaces.checkItems( checkedValues.toArray( new String[] {  } ) );

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == nUserId )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_WORKGROUPS;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_ALL_WORKSGROUP_LIST, assignableWorkspaces );
        model.put( MARK_CAN_DELEGATE, String.valueOf( bDelegateWorkgroups ) );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER_WORKGROUPS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Build the right list
     *
     * @param request Http Request
     * @return the right list
     */
    public String getModifyAdminUserRights( HttpServletRequest request )
    {
        boolean bDelegateRights = Boolean.valueOf( request.getParameter( PARAMETER_DELEGATE_RIGHTS ) );

        String strSelectAll = request.getParameter( PARAMETER_SELECT );
        boolean bSelectAll = ( ( strSelectAll != null ) && strSelectAll.equals( PARAMETER_SELECT_ALL ) ) ? true : false;

        setPageTitleProperty( bDelegateRights ? PROPERTY_DELEGATE_USER_RIGHTS_PAGETITLE
                                              : PROPERTY_MODIFY_USER_RIGHTS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

        Collection<Right> rightList;
        Collection<Right> allRightList = RightHome.getRightsList( user.getUserLevel(  ) );

        AdminUser currentUser = getUser(  );

        if ( bDelegateRights )
        {
            Map<String, Right> rights = AdminUserHome.getRightsListForUser( currentUser.getUserId(  ) );
            rightList = new ArrayList<Right>(  );

            for ( String strRights : rights.keySet(  ) )
            {
                // logged user can only delegate rights with level higher or equal to user level.
                if ( rights.get( strRights ).getLevel(  ) >= user.getUserLevel(  ) )
                {
                    rightList.add( rights.get( strRights ) );
                }
            }
        }
        else
        {
            rightList = AdminUserHome.getRightsListForUser( nUserId ).values(  );
        }

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == nUserId )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_RIGHTS;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_RIGHT_LIST, I18nService.localizeCollection( rightList, getLocale(  ) ) );
        model.put( MARK_ALL_RIGHT_LIST, I18nService.localizeCollection( allRightList, getLocale(  ) ) );
        model.put( MARK_CAN_DELEGATE, String.valueOf( bDelegateRights ) );
        model.put( MARK_SELECT_ALL, bSelectAll );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER_RIGHTS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the change form of an appUser rights
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyAdminUserRights( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        String[] arrayRights = request.getParameterValues( PARAMETER_RIGHT );

        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

        AdminUserHome.removeAllOwnRightsForUser( user );

        if ( arrayRights != null )
        {
            for ( int i = 0; i < arrayRights.length; i++ )
            {
                AdminUserHome.createRightForUser( nUserId, arrayRights[i] );
            }
        }

        return JSP_MANAGE_USER_RIGHTS + "?" + PARAMETER_USER_ID + "=" + nUserId;
    }

    /**
     * Build the User role list
     *
     * @param request Http Request
     * @return the right list
     */
    public String getManageAdminUserRoles( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_USER_ROLES_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        Collection<AdminRole> roleList = AdminUserHome.getRolesListForUser( nUserId ).values(  );

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == nUserId )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_ROLES;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_CAN_MODIFY, getUser(  ).isParent( selectedUser ) || getUser(  ).isAdmin(  ) );
        model.put( MARK_CAN_DELEGATE, getUser(  ).getUserId(  ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_ROLE_LIST, roleList );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USER_ROLES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Build the role list
     *
     * @param request Http Request
     * @return the right list
     */
    public String getModifyAdminUserRoles( HttpServletRequest request )
    {
        boolean bDelegateRoles = Boolean.valueOf( request.getParameter( PARAMETER_DELEGATE_RIGHTS ) );
        setPageTitleProperty( PROPERTY_MODIFY_USER_ROLES_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        Collection<AdminRole> roleList = AdminUserHome.getRolesListForUser( nUserId ).values(  );
        Collection<AdminRole> assignableRoleList;

        if ( bDelegateRoles )
        {
            // assign connected user roles
            assignableRoleList = new ArrayList<AdminRole>(  );

            AdminUser currentUser = getUser(  );

            for ( AdminRole role : AdminRoleHome.findAll(  ) )
            {
                if ( currentUser.isAdmin(  ) || RBACService.isUserInRole( currentUser, role.getKey(  ) ) )
                {
                    assignableRoleList.add( role );
                }
            }
        }
        else
        {
            // assign all available roles
            assignableRoleList = AdminRoleHome.findAll(  );
        }

        // ITEM NAVIGATION
        Map<Integer, String> listItem = new HashMap<Integer, String>(  );
        Collection<AdminUser> listAllUsers = AdminUserHome.findUserList(  );
        int nMapKey = 1;
        int nCurrentItemId = 1;

        for ( AdminUser allUser : listAllUsers )
        {
            listItem.put( nMapKey, Integer.toString( allUser.getUserId(  ) ) );

            if ( allUser.getUserId(  ) == nUserId )
            {
                nCurrentItemId = nMapKey;
            }

            nMapKey++;
        }

        String strBaseUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_ROLES;
        UrlItem url = new UrlItem( strBaseUrl );
        ItemNavigator itemNavigator = new ItemNavigator( listItem, nCurrentItemId, url.getUrl(  ), PARAMETER_USER_ID );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_ROLE_LIST, roleList );
        model.put( MARK_ALL_ROLE_LIST, assignableRoleList );
        model.put( MARK_ITEM_NAVIGATOR, itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER_ROLES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the change form of an appUser roles
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyAdminUserRoles( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        String[] arrayRoles = request.getParameterValues( PARAMETER_ROLE );

        AdminUserHome.removeAllRolesForUser( nUserId );

        if ( arrayRoles != null )
        {
            for ( int i = 0; i < arrayRoles.length; i++ )
            {
                AdminUserHome.createRoleForUser( nUserId, arrayRoles[i] );
            }
        }

        return JSP_MANAGE_USER_ROLES + "?" + PARAMETER_USER_ID + "=" + nUserId;
    }

    /**
     * Process the change form of an appUser workspaces
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyAdminUserWorkgroups( HttpServletRequest request )
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = getUser(  );
        String[] arrayWorkspaces = request.getParameterValues( PARAMETER_WORKGROUP );
        ReferenceList assignableWorkgroups = AdminWorkgroupHome.getUserWorkgroups( currentUser );

        for ( ReferenceItem item : assignableWorkgroups )
        {
            AdminWorkgroupHome.removeUserFromWorkgroup( user, item.getCode(  ) );
        }

        if ( arrayWorkspaces != null )
        {
            for ( String strWorkgroupKey : arrayWorkspaces )
            {
                AdminWorkgroupHome.addUserForWorkgroup( user, strWorkgroupKey );
            }
        }

        return JSP_MANAGE_USER_WORKGROUPS + "?" + PARAMETER_USER_ID + "=" + nUserId;
    }

    /**
     * Tell if 2 users have groups in common
     * @param user1 User1
     * @param user2 User2
     * @return true or false
     */
    private boolean haveCommonWorkgroups( AdminUser user1, AdminUser user2 )
    {
        ReferenceList workgroups = AdminWorkgroupHome.getUserWorkgroups( user1 );

        if ( workgroups.size(  ) == 0 )
        {
            return true;
        }

        for ( ReferenceItem item : workgroups )
        {
            if ( AdminWorkgroupHome.isUserInWorkgroup( user2, item.getCode(  ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Build the advanced parameters management
     * @param request HttpServletRequest
     * @return The options for the advanced parameters
     */
    public String getManageAdvancedParameters( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            return getManageAdminUsers( request );
        }

        setPageTitleProperty( PROPERTY_MANAGE_ADVANCED_PARAMETERS_PAGETITLE );

        Map<String, Object> model = AdminUserService.getManageAdvancedParameters( getUser(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ADVANCED_PARAMETERS,
                getUser(  ).getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the page of confirmation for modifying the password
     * encryption
     *
     * @param request The Http Request
     * @return the confirmation url
     */
    public String doConfirmModifyPasswordEncryption( HttpServletRequest request )
    {
        String strEnablePasswordEncryption = request.getParameter( PARAMETER_ENABLE_PASSWORD_ENCRYPTION );
        String strEncryptionAlgorithm = request.getParameter( PARAMETER_ENCRYPTION_ALGORITHM );

        if ( strEncryptionAlgorithm.equals( CONSTANT_DEFAULT_ALGORITHM ) )
        {
            strEncryptionAlgorithm = CONSTANT_EMPTY_STRING;
        }

        String strCurrentPasswordEnableEncryption = DefaultUserParameterHome.findByKey( PARAMETER_ENABLE_PASSWORD_ENCRYPTION )
                                                                            .getParameterValue(  );
        String strCurrentEncryptionAlgorithm = DefaultUserParameterHome.findByKey( PARAMETER_ENCRYPTION_ALGORITHM )
                                                                       .getParameterValue(  );

        String strUrl = "";

        if ( strEnablePasswordEncryption.equals( strCurrentPasswordEnableEncryption ) &&
                strEncryptionAlgorithm.equals( strCurrentEncryptionAlgorithm ) )
        {
            strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_NO_CHANGE_PASSWORD_ENCRYPTION,
                    JSP_URL_MANAGE_ADVANCED_PARAMETERS, AdminMessage.TYPE_INFO );
        }
        else if ( strEnablePasswordEncryption.equals( String.valueOf( Boolean.TRUE ) ) &&
                strEncryptionAlgorithm.equals( CONSTANT_EMPTY_STRING ) )
        {
            strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_INVALID_ENCRYPTION_ALGORITHM,
                    JSP_URL_MANAGE_ADVANCED_PARAMETERS, AdminMessage.TYPE_STOP );
        }
        else
        {
            if ( strEnablePasswordEncryption.equals( String.valueOf( Boolean.FALSE ) ) )
            {
                strEncryptionAlgorithm = "";
            }

            String strUrlModify = JSP_URL_MODIFY_PASSWORD_ENCRYPTION + "?" + PARAMETER_ENABLE_PASSWORD_ENCRYPTION +
                "=" + strEnablePasswordEncryption + "&" + PARAMETER_ENCRYPTION_ALGORITHM + "=" +
                strEncryptionAlgorithm;

            strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_MODIFY_PASSWORD_ENCRYPTION,
                    strUrlModify, AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }

    /**
     * Modify the password encryption
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user does not have the permission
     */
    public String doModifyPasswordEncryption( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ENCRYPTED_PASSWORD, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        String strEnablePasswordEncryption = request.getParameter( PARAMETER_ENABLE_PASSWORD_ENCRYPTION );
        String strEncryptionAlgorithm = request.getParameter( PARAMETER_ENCRYPTION_ALGORITHM );

        String strCurrentPasswordEnableEncryption = DefaultUserParameterHome.findByKey( PARAMETER_ENABLE_PASSWORD_ENCRYPTION )
                                                                            .getParameterValue(  );
        String strCurrentEncryptionAlgorithm = DefaultUserParameterHome.findByKey( PARAMETER_ENCRYPTION_ALGORITHM )
                                                                       .getParameterValue(  );

        if ( strEnablePasswordEncryption.equals( strCurrentPasswordEnableEncryption ) &&
                strEncryptionAlgorithm.equals( strCurrentEncryptionAlgorithm ) )
        {
            return JSP_MANAGE_ADVANCED_PARAMETERS;
        }

        DefaultUserParameter userParamEnablePwdEncryption = new DefaultUserParameter( PARAMETER_ENABLE_PASSWORD_ENCRYPTION,
                strEnablePasswordEncryption );
        DefaultUserParameter userParamEncryptionAlgorithm = new DefaultUserParameter( PARAMETER_ENCRYPTION_ALGORITHM,
                strEncryptionAlgorithm );

        DefaultUserParameterHome.update( userParamEnablePwdEncryption );
        DefaultUserParameterHome.update( userParamEncryptionAlgorithm );

        // Alert all users their password have been reinitialized.
        Collection<AdminUser> listUser = AdminUserHome.findUserList(  );

        for ( AdminUser user : listUser )
        {
            Locale locale = user.getLocale(  );

            if ( locale == null )
            {
                locale = Locale.getDefault(  );
            }

            // make password
            String strPassword = PasswordUtil.makePassword(  );

            // update password
            if ( ( strPassword != null ) && !strPassword.equals( CONSTANT_EMPTY_STRING ) )
            {
                // Encrypted password
                String strEncryptedPassword = strPassword;

                if ( Boolean.valueOf( DefaultUserParameterHome.findByKey( PARAMETER_ENABLE_PASSWORD_ENCRYPTION )
                                                                  .getParameterValue(  ) ) )
                {
                    String strAlgorithm = DefaultUserParameterHome.findByKey( PARAMETER_ENCRYPTION_ALGORITHM )
                                                                  .getParameterValue(  );
                    strEncryptedPassword = CryptoService.encrypt( strPassword, strAlgorithm );
                }

                LuteceDefaultAdminUser userStored = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId(  ) );
                userStored.setPassword( strEncryptedPassword );
                userStored.setPasswordReset( Boolean.TRUE );
                AdminUserHome.update( userStored );
            }

            if ( !( ( user.getEmail(  ) == null ) || user.getEmail(  ).equals( CONSTANT_EMPTY_STRING ) ) )
            {
                //send password by e-mail
                String strSenderEmail = AppPropertiesService.getProperty( PROPERTY_NO_REPLY_EMAIL );
                String strEmailSubject = I18nService.getLocalizedString( MESSAGE_EMAIL_SUBJECT, locale );
                HashMap<String, Object> model = new HashMap<String, Object>(  );
                model.put( MARK_NEW_PASSWORD, strPassword );
                model.put( MARK_LOGIN_URL,
                    AppPathService.getBaseUrl( request ) +
                    AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  ) );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADMIN_EMAIL_FORGOT_PASSWORD, locale,
                        model );

                MailService.sendMailHtml( user.getEmail(  ), strSenderEmail, strSenderEmail, strEmailSubject,
                    template.getHtml(  ) );
            }
        }

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Modify the default user parameter values.
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user does not have the permission
     */
    public String doModifyDefaultUserParameterValues( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        DefaultUserParameter userParamStatus = new DefaultUserParameter( PARAMETER_DEFAULT_USER_STATUS,
                request.getParameter( PARAMETER_STATUS ) );
        DefaultUserParameterHome.update( userParamStatus );

        DefaultUserParameter userParamUserLevel = new DefaultUserParameter( PARAMETER_DEFAULT_USER_LEVEL,
                request.getParameter( PARAMETER_USER_LEVEL ) );
        DefaultUserParameterHome.update( userParamUserLevel );

        DefaultUserParameter userParamNotifyUser = new DefaultUserParameter( PARAMETER_DEFAULT_USER_NOTIFICATION,
                request.getParameter( PARAMETER_NOTIFY_USER ) );
        DefaultUserParameterHome.update( userParamNotifyUser );

        DefaultUserParameter userParamLanguage = new DefaultUserParameter( PARAMETER_DEFAULT_USER_LANGUAGE,
                request.getParameter( PARAMETER_LANGUAGE ) );
        DefaultUserParameterHome.update( userParamLanguage );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Modify the email pattern
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user does not have the permission
     */
    public String doModifyEmailPattern( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        String strJsp = StringUtils.EMPTY;
        String strSetManually = request.getParameter( PARAMETER_IS_EMAIL_PATTERN_SET_MANUALLY );
        String strEmailPattern = request.getParameter( PARAMETER_EMAIL_PATTERN );

        if ( StringUtils.isNotBlank( strEmailPattern ) )
        {
            AdminUserService.doModifyEmailPattern( strEmailPattern, strSetManually != null );
            strJsp = JSP_MANAGE_ADVANCED_PARAMETERS;
        }
        else
        {
            strJsp = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ERROR_EMAIL_PATTERN,
                    AdminMessage.TYPE_STOP );
        }

        return strJsp;
    }

    /**
     * Reset the email pattern
     * @param request {@link HttpServletRequest}
     * @return the jsp return
     * @throws AccessDeniedException access denied if the AdminUser does not have the permission
     */
    public String doResetEmailPattern( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        AdminUserService.doResetEmailPattern(  );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Do insert a regular expression
     * @param request {@link HttpServletRequest}
     * @return the jsp return
     * @throws AccessDeniedException access denied if the AdminUser does not have the permission
     */
    public String doInsertRegularExpression( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        String strRegularExpressionId = request.getParameter( PARAMETER_ID_EXPRESSION );

        if ( StringUtils.isNotBlank( strRegularExpressionId ) && StringUtils.isNumeric( strRegularExpressionId ) )
        {
            int nRegularExpressionId = Integer.parseInt( strRegularExpressionId );
            AdminUserService.doInsertRegularExpression( nRegularExpressionId );
        }

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Do remove a regular expression
     * @param request {@link HttpServletRequest}
     * @return the jsp return
     * @throws AccessDeniedException access denied if the AdminUser does not have the permission
     */
    public String doRemoveRegularExpression( HttpServletRequest request )
        throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) )
        {
            throw new AccessDeniedException(  );
        }

        String strRegularExpressionId = request.getParameter( PARAMETER_ID_EXPRESSION );

        if ( StringUtils.isNotBlank( strRegularExpressionId ) && StringUtils.isNumeric( strRegularExpressionId ) )
        {
            int nRegularExpressionId = Integer.parseInt( strRegularExpressionId );
            AdminUserService.doRemoveRegularExpression( nRegularExpressionId );
        }

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }
}
