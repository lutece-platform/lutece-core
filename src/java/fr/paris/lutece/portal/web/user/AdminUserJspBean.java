/*
 * Copyright (c) 2002-2020, City of Paris
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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.rbac.RBACRoleHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.right.Level;
import fr.paris.lutece.portal.business.right.LevelHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.PasswordUpdateMode;
import fr.paris.lutece.portal.business.user.attribute.IAttribute;
import fr.paris.lutece.portal.business.user.attribute.ISimpleValuesAttributes;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.admin.ImportAdminUserService;
import fr.paris.lutece.portal.service.csv.CSVMessageDescriptor;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.service.user.attribute.AdminUserFieldService;
import fr.paris.lutece.portal.service.user.attribute.AttributeService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.service.xsl.XslExportService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.dashboard.AdminDashboardJspBean;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.filesystem.FileSystemUtil;
import fr.paris.lutece.util.html.AbstractPaginator;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.ItemNavigator;
import fr.paris.lutece.util.password.PasswordUtil;
import fr.paris.lutece.util.sort.AttributeComparator;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlUtil;

/**
 * This class provides the user interface to manage app user features ( manage,
 * create, modify, remove, ... )
 */
public class AdminUserJspBean extends AdminFeaturesPageJspBean
{
    private static final String ATTRIBUTE_IMPORT_USERS_LIST_MESSAGES = "importUsersListMessages";
    private static final long serialVersionUID = -6323157489236186522L;

    // //////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String CONSTANTE_UN = "1";
    private static final String CONSTANT_USER_MSG = "User ";
    private static final String CONSTANT_NOT_AUTHORIZED = " is not authorized to permission ";
    private static final String CONSTANT_ADVANCED_PARAMS = "core.advanced_parameters.";

    // Beans
    private static final String BEAN_IMPORT_ADMIN_USER_SERVICE = "adminUserImportService";

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
    private static final String TEMPLATE_DEFAULT_MODIFY_USER_PASSWORD = "admin/user/modify_user_password_default_module.html";
    private static final String TEMPLATE_MANAGE_USER_WORKGROUPS = "admin/user/manage_user_workgroups.html";
    private static final String TEMPLATE_MODIFY_USER_WORKGROUPS = "admin/user/modify_user_workgroups.html";
    private static final String TEMPLATE_ADMIN_EMAIL_CHANGE_STATUS = "admin/user/user_email_change_status.html";
    private static final String TEMPLATE_NOTIFY_USER = "admin/user/notify_user_account_created.html";
    private static final String TEMPLATE_FIELD_ANONYMIZE_ADMIN_USER = "admin/user/field_anonymize_admin_user.html";
    private static final String TEMPLATE_ACCOUNT_LIFE_TIME_EMAIL = "admin/user/account_life_time_email.html";
    private static final String TEMPLATE_EXPORT_USERS_FROM_FILE = "admin/user/export_users.html";

    // Messages
    private static final String PROPERTY_MANAGE_USERS_PAGETITLE = "portal.users.manage_users.pageTitle";
    private static final String PROPERTY_MODIFY_USER_PAGETITLE = "portal.users.modify_user.pageTitle";
    private static final String PROPERTY_MODIFY_USER_PASSWORD_PAGETITLE = "portal.users.modify_user_password.pageTitle";
    private static final String PROPERTY_LABEL_FIRST_PASSWORD = "portal.users.modify_password_default_module.form.password.new";
    private static final String PROPERTY_CREATE_USER_PAGETITLE = "portal.users.create_user.pageTitle";
    private static final String PROPERTY_IMPORT_MODULE_USER_PAGETITLE = "portal.users.import_module_user.pageTitle";
    private static final String PROPERTY_MANAGE_USER_RIGHTS_PAGETITLE = "portal.users.manage_user_rights.pageTitle";
    private static final String PROPERTY_MODIFY_USER_RIGHTS_PAGETITLE = "portal.users.modify_user_rights.pageTitle";
    private static final String PROPERTY_MANAGE_USER_ROLES_PAGETITLE = "portal.users.manage_user_roles.pageTitle";
    private static final String PROPERTY_MODIFY_USER_ROLES_PAGETITLE = "portal.users.modify_user_roles.pageTitle";
    private static final String PROPERTY_IMPORT_USERS_FROM_FILE_PAGETITLE = "portal.users.import_users_from_file.pageTitle";
    private static final String PROPERTY_EXPORT_USERS_PAGETITLE = "portal.users.export_users.pageTitle";
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
    private static final String PROPERTY_MESSAGE_ERROR_EMAIL_PATTERN = "portal.users.manage_advanced_parameters.message.errorEmailPattern";
    private static final String PROPERTY_MESSAGE_CONFIRM_USE_ASP = "portal.users.manage_advanced_parameters.message.confirmUseAdvancedSecurityParameters";
    private static final String PROPERTY_MESSAGE_CONFIRM_REMOVE_ASP = "portal.users.manage_advanced_parameters.message.confirmRemoveAdvancedSecurityParameters";
    private static final String PROPERTY_MESSAGE_NO_ADMIN_USER_SELECTED = "portal.users.message.noUserSelected";
    private static final String PROPERTY_MESSAGE_CONFIRM_ANONYMIZE_USER = "portal.users.message.confirmAnonymizeUser";
    private static final String PROPERTY_MESSAGE_TITLE_CHANGE_ANONYMIZE_USER = "portal.users.anonymize_user.titleAnonymizeUser";
    private static final String PROPERTY_MESSAGE_NO_ACCOUNT_TO_REACTIVATED = "portal.users.message.noAccountToReactivate";
    private static final String PROPERTY_MESSAGE_ACCOUNT_REACTIVATED = "portal.users.message.messageAccountReactivated";
    private static final String PROPERTY_FIRST_EMAIL = "portal.users.accountLifeTime.labelFirstEmail";
    private static final String PROPERTY_OTHER_EMAIL = "portal.users.accountLifeTime.labelOtherEmail";
    private static final String PROPERTY_ACCOUNT_DEACTIVATES_EMAIL = "portal.users.accountLifeTime.labelAccountDeactivatedEmail";
    private static final String PROPERTY_ACCOUNT_UPDATED_EMAIL = "portal.users.accountLifeTime.labelAccountUpdatedEmail";
    private static final String PROPERTY_NOTIFY_PASSWORD_EXPIRED = "portal.users.accountLifeTime.labelPasswordExpired";
    private static final String PROPERTY_MESSAGE_USER_ERROR_SESSION = "portal.users.message.user.error.session";
    private static final String MESSAGE_NOT_AUTHORIZED = "Action not permited to current user";
    private static final String MESSAGE_MANDATORY_FIELD = "portal.util.message.mandatoryField";
    private static final String MESSAGE_ERROR_CSV_FILE_IMPORT = "portal.users.import_users_from_file.error_csv_file_import";
    private static final String FIELD_IMPORT_USERS_FILE = "portal.users.import_users_from_file.labelImportFile";
    private static final String FIELD_XSL_EXPORT = "portal.users.export_users.labelXslt";

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
    private static final String PARAMETER_ACCESSIBILITY_MODE = "accessibility_mode";
    private static final String PARAMETER_WORKGROUP_KEY = "workgroup_key";
    private static final String PARAMETER_EMAIL_PATTERN = "email_pattern";
    private static final String PARAMETER_IS_EMAIL_PATTERN_SET_MANUALLY = "is_email_pattern_set_manually";
    private static final String PARAMETER_ID_EXPRESSION = "id_expression";
    private static final String PARAMETER_FORCE_CHANGE_PASSWORD_REINIT = "force_change_password_reinit";
    private static final String PARAMETER_PASSWORD_MINIMUM_LENGTH = "password_minimum_length";
    private static final String PARAMETER_PASSWORD_FORMAT_UPPER_LOWER_CASE = "password_format_upper_lower_case";
    private static final String PARAMETER_PASSWORD_FORMAT_NUMERO = "password_format_numero";
    private static final String PARAMETER_PASSWORD_FORMAT_SPECIAL_CHARACTERS = "password_format_special_characters";
    private static final String PARAMETER_PASSWORD_DURATION = "password_duration";
    private static final String PARAMETER_PASSWORD_HISTORY_SIZE = "password_history_size";
    private static final String PARAMETER_MAXIMUM_NUMBER_PASSWORD_CHANGE = "maximum_number_password_change";
    private static final String PARAMETER_TSW_SIZE_PASSWORD_CHANGE = "tsw_size_password_change";
    private static final String PARAMETER_ACCOUNT_LIFE_TIME = "account_life_time";
    private static final String PARAMETER_TIME_BEFORE_ALERT_ACCOUNT = "time_before_alert_account";
    private static final String PARAMETER_NB_ALERT_ACCOUNT = "nb_alert_account";
    private static final String PARAMETER_TIME_BETWEEN_ALERTS_ACCOUNT = "time_between_alerts_account";
    private static final String PARAMETER_ATTRIBUTE = "attribute_";
    private static final String PARAMETER_EMAIL_TYPE = "email_type";
    private static final String PARAMETER_FIRST_ALERT_MAIL_SENDER = "first_alert_mail_sender";
    private static final String PARAMETER_OTHER_ALERT_MAIL_SENDER = "other_alert_mail_sender";
    private static final String PARAMETER_EXPIRED_ALERT_MAIL_SENDER = "expired_alert_mail_sender";
    private static final String PARAMETER_REACTIVATED_ALERT_MAIL_SENDER = "account_reactivated_mail_sender";
    private static final String PARAMETER_FIRST_ALERT_MAIL_SUBJECT = "first_alert_mail_subject";
    private static final String PARAMETER_OTHER_ALERT_MAIL_SUBJECT = "other_alert_mail_subject";
    private static final String PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT = "expired_alert_mail_subject";
    private static final String PARAMETER_REACTIVATED_ALERT_MAIL_SUBJECT = "account_reactivated_mail_subject";
    private static final String PARAMETER_FIRST_ALERT_MAIL = "core_first_alert_mail";
    private static final String PARAMETER_OTHER_ALERT_MAIL = "core_other_alert_mail";
    private static final String PARAMETER_EXPIRATION_MAIL = "core_expiration_mail";
    private static final String PARAMETER_ACCOUNT_REACTIVATED = "core_account_reactivated_mail";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_NOTIFY_USER_PASSWORD_EXPIRED = "notify_user_password_expired";
    private static final String PARAMETER_PASSWORD_EXPIRED_MAIL_SENDER = "password_expired_mail_sender";
    private static final String PARAMETER_PASSWORD_EXPIRED_MAIL_SUBJECT = "password_expired_mail_subject";
    private static final String PARAMETER_NOTIFY_PASSWORD_EXPIRED = "core_password_expired";
    private static final String PARAMETER_IMPORT_USERS_FILE = "import_file";
    private static final String PARAMETER_SKIP_FIRST_LINE = "ignore_first_line";
    private static final String PARAMETER_UPDATE_USERS = "update_existing_users";
    private static final String PARAMETER_XSL_EXPORT_ID = "xsl_export_id";
    private static final String PARAMETER_EXPORT_ROLES = "export_roles";
    private static final String PARAMETER_EXPORT_ATTRIBUTES = "export_attributes";
    private static final String PARAMETER_EXPORT_RIGHTS = "export_rights";
    private static final String PARAMETER_EXPORT_WORKGROUPS = "export_workgroups";
    private static final String PARAMETER_RESET_TOKEN_VALIDITY = "reset_token_validity";
    private static final String PARAMETER_LOCK_RESET_TOKEN_TO_SESSION = "lock_reset_token_to_session";
    private static final String PARAMETER_RESET = "reset";

    // Jsp url
    private static final String JSP_MANAGE_USER_RIGHTS = "ManageUserRights.jsp";
    private static final String JSP_MANAGE_USER_ROLES = "ManageUserRoles.jsp";
    private static final String JSP_MANAGE_USER = "ManageUsers.jsp";
    private static final String JSP_MANAGE_USER_WORKGROUPS = "ManageUserWorkgroups.jsp";
    private static final String JSP_URL_REMOVE_USER = "jsp/admin/user/DoRemoveUser.jsp";
    private static final String JSP_URL_CREATE_USER = "jsp/admin/user/CreateUser.jsp";
    private static final String JSP_URL_IMPORT_USER = "jsp/admin/user/ImportUser.jsp";
    private static final String JSP_URL_MODIFY_USER = "jsp/admin/user/ModifyUser.jsp";
    private static final String JSP_URL_MANAGE_USER_RIGHTS = "jsp/admin/user/ManageUserRights.jsp";
    private static final String JSP_URL_MANAGE_USER_ROLES = "jsp/admin/user/ManageUserRoles.jsp";
    private static final String JSP_URL_MANAGE_USER_WORKGROUPS = "jsp/admin/user/ManageUserWorkgroups.jsp";
    private static final String JSP_URL_USE_ADVANCED_SECUR_PARAM = "jsp/admin/user/DoUseAdvancedSecurityParameters.jsp";
    private static final String JSP_URL_REMOVE_ADVANCED_SECUR_PARAM = "jsp/admin/user/DoRemoveAdvancedSecurityParameters.jsp";
    private static final String JSP_URL_ANONYMIZE_ADMIN_USER = "jsp/admin/user/DoAnonymizeAdminUser.jsp";

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
    private static final String MARK_PERMISSION_ADVANCED_PARAMETER = "permission_advanced_parameter";
    private static final String MARK_PERMISSION_IMPORT_EXPORT_USERS = "permission_import_export_users";
    private static final String MARK_ITEM_NAVIGATOR = "item_navigator";
    private static final String MARK_ATTRIBUTES_LIST = "attributes_list";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_MAP_LIST_ATTRIBUTE_DEFAULT_VALUES = "map_list_attribute_default_values";
    private static final String MARK_DEFAULT_USER_LEVEL = "default_user_level";
    private static final String MARK_DEFAULT_USER_NOTIFICATION = "default_user_notification";
    private static final String MARK_DEFAULT_USER_LANGUAGE = "default_user_language";
    private static final String MARK_DEFAULT_USER_STATUS = "default_user_status";
    private static final String MARK_ACCESS_FAILURES_MAX = "access_failures_max";
    private static final String MARK_ACCESS_FAILURES_INTERVAL = "access_failures_interval";
    private static final String MARK_BANNED_DOMAIN_NAMES = "banned_domain_names";
    private static final String MARK_EMAIL_SENDER = "email_sender";
    private static final String MARK_EMAIL_SUBJECT = "email_subject";
    private static final String MARK_EMAIL_BODY = "email_body";
    private static final String MARK_EMAIL_LABEL = "emailLabel";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LIST_MESSAGES = "csv_messages";
    private static final String MARK_CSV_SEPARATOR = "csv_separator";
    private static final String MARK_CSV_ESCAPE = "csv_escape";
    private static final String MARK_ATTRIBUTES_SEPARATOR = "attributes_separator";
    private static final String MARK_LIST_XSL_EXPORT = "refListXsl";
    private static final String MARK_DEFAULT_VALUE_WORKGROUP_KEY = "workgroup_key_default_value";
    private static final String MARK_WORKGROUP_KEY_LIST = "workgroup_key_list";
    private static final String MARK_ADMIN_AVATAR = "adminAvatar";
    private static final String MARK_RANDOM_PASSWORD_SIZE = "randomPasswordSize";
    private static final String MARK_MINIMUM_PASSWORD_SIZE = "minimumPasswordSize";
    private static final String MARK_DEFAULT_MODE_USED = "defaultModeUsed";

    private static final String CONSTANT_EMAIL_TYPE_FIRST = "first";
    private static final String CONSTANT_EMAIL_TYPE_OTHER = "other";
    private static final String CONSTANT_EMAIL_TYPE_EXPIRED = "expired";
    private static final String CONSTANT_EMAIL_TYPE_REACTIVATED = "reactivated";
    private static final String CONSTANT_EMAIL_PASSWORD_EXPIRED = "password_expired";
    private static final String CONSTANT_EXTENSION_CSV_FILE = ".csv";
    private static final String CONSTANT_EXTENSION_XML_FILE = ".xml";
    private static final String CONSTANT_MIME_TYPE_CSV = "application/csv";
    private static final String CONSTANT_MIME_TYPE_XML = "application/xml";
    private static final String CONSTANT_MIME_TYPE_TEXT_CSV = "text/csv";
    private static final String CONSTANT_MIME_TYPE_OCTETSTREAM = "application/octet-stream";
    private static final String CONSTANT_EXPORT_USERS_FILE_NAME = "users";
    private static final String CONSTANT_POINT = ".";
    private static final String CONSTANT_QUOTE = "\"";
    private static final String CONSTANT_ATTACHEMENT_FILE_NAME = "attachement; filename=\"";
    private static final String CONSTANT_ATTACHEMENT_DISPOSITION = "Content-Disposition";
    private static final String CONSTANT_XML_USERS = "users";

    private static final String TOKEN_TECHNICAL_ADMIN = AdminDashboardJspBean.TEMPLATE_MANAGE_DASHBOARDS;
    private static final String JSP_MANAGE_ADVANCED_PARAMETERS = "../AdminTechnicalMenu.jsp?#users_advanced_parameters";

    private transient ImportAdminUserService _importAdminUserService;
    private boolean _bAdminAvatar = PluginService.isPluginEnable( "adminavatar" );

    private int _nItemsPerPage;
    private String _strCurrentPageIndex;
    private ItemNavigator _itemNavigator;

    /**
     * Build the User list
     *
     * @param request Http Request
     * @return the AppUser list
     */
    public String getManageAdminUsers( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_USERS_PAGETITLE );

        // Reinit session
        reinitItemNavigator( );

        String strCreateUrl;
        AdminUser currentUser = getUser( );

        Map<String, Object> model = new HashMap<>( );

        // creation in no-module mode : no import
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            strCreateUrl = JSP_URL_CREATE_USER;
        }
        else
        {
            strCreateUrl = JSP_URL_IMPORT_USER;
        }

        String strURL = getHomeUrl( request );
        UrlItem url = new UrlItem( strURL );

        Collection<AdminUser> listUsers = AdminUserHome.findUserList( );
        listUsers = AdminWorkgroupService.getAuthorizedCollection( listUsers, getUser( ) );

        List<AdminUser> availableUsers = AdminUserService.getFilteredUsersInterface( listUsers, request, model, url );
        List<AdminUser> listDisplayUsers = new ArrayList<>( );

        for ( AdminUser user : availableUsers )
        {
            if ( isUserAuthorizedToModifyUser( currentUser, user ) )
            {
                listDisplayUsers.add( user );
            }
        }

        // SORT
        String strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        String strAscSort = null;

        if ( strSortedAttributeName != null )
        {
            strAscSort = request.getParameter( Parameters.SORTED_ASC );

            boolean bIsAscSort = Boolean.parseBoolean( strAscSort );

            Collections.sort( listDisplayUsers, new AttributeComparator( strSortedAttributeName, bIsAscSort ) );
        }

        _strCurrentPageIndex = AbstractPaginator.getPageIndex( request, AbstractPaginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );
        int defaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = AbstractPaginator.getItemsPerPage( request, AbstractPaginator.PARAMETER_ITEMS_PER_PAGE,
                _nItemsPerPage, defaultItemsPerPage );

        if ( strSortedAttributeName != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, strSortedAttributeName );
        }

        if ( strAscSort != null )
        {
            url.addParameter( Parameters.SORTED_ASC, strAscSort );
        }

        // PAGINATOR
        LocalizedPaginator<AdminUser> paginator = new LocalizedPaginator<>( listDisplayUsers, _nItemsPerPage,
                url.getUrl( ), AbstractPaginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        // USER LEVEL
        Collection<Level> filteredLevels = new ArrayList<>( );

        for ( Level level : LevelHome.getLevelsList( ) )
        {
            if ( currentUser.isAdmin( ) || currentUser.hasRights( level.getId( ) ) )
            {
                filteredLevels.add( level );
            }
        }

        boolean bPermissionAdvancedParameter = RBACService.isAuthorized( AdminUser.RESOURCE_TYPE,
                RBAC.WILDCARD_RESOURCES_ID, AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS,
                getUser( ) );
        boolean bPermissionImportExportUsers = RBACService.isAuthorized( AdminUser.RESOURCE_TYPE,
                RBAC.WILDCARD_RESOURCES_ID, AdminUserResourceIdService.PERMISSION_IMPORT_EXPORT_USERS, getUser( ) );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_USER_LEVELS_LIST, filteredLevels );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_USER_LIST, paginator.getPageItems( ) );
        model.put( MARK_USER_CREATION_URL, strCreateUrl );
        model.put( MARK_PERMISSION_ADVANCED_PARAMETER, bPermissionAdvancedParameter );
        model.put( MARK_PERMISSION_IMPORT_EXPORT_USERS, bPermissionImportExportUsers );
        model.put( MARK_ADMIN_AVATAR, _bAdminAvatar );
        model.put( MARK_DEFAULT_MODE_USED, AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USERS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Display the page for user import. This page is used in module mode to link a
     * user to its code in the module (for later authentication) and to populate the
     * creation form with the data the module is able to provide.
     * 
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

        Map<String, Object> model = new HashMap<>( );
        Collection<?> allImportUsers = null;

        if ( StringUtils.isNotEmpty( strLastName ) || StringUtils.isNotEmpty( strFirstName )
                || StringUtils.isNotEmpty( strEmail ) )
        {
            allImportUsers = AdminAuthenticationService.getInstance( ).getUserListFromModule( strLastName, strFirstName,
                    strEmail );
        }

        model.put( MARK_IMPORT_USER_LIST, allImportUsers );
        model.put( MARK_ACCESS_CODE, strAccessCode );
        model.put( MARK_LAST_NAME, strLastName );
        model.put( MARK_FIRST_NAME, strFirstName );
        model.put( MARK_EMAIL, strEmail );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_USER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Performs checks on the selected user to import and redirects on the creation
     * form. This page is used in module mode.
     * 
     * @param request The HTTP Request
     * @return The Jsp URL of the creation form if check ok, an error page url
     *         otherwise
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

        return AppPathService.getBaseUrl( request ) + JSP_URL_CREATE_USER + "?" + PARAMETER_ACCESS_CODE + "="
                + strAccessCode;
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
        AdminUser currentUser = getUser( );
        Collection<Level> filteredLevels = new ArrayList<>( );

        for ( Level level : LevelHome.getLevelsList( ) )
        {
            if ( currentUser.isAdmin( ) || currentUser.hasRights( level.getId( ) ) )
            {
                filteredLevels.add( level );
            }
        }

        // Default user parameter values
        String strDefaultLevel = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL );
        Level defaultLevel = LevelHome.findByPrimaryKey( Integer.parseInt( strDefaultLevel ) );
        int nDefaultUserNotification = Integer
                .parseInt( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION ) );
        String strDefaultUserLanguage = DefaultUserParameterHome
                .findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE );
        int nDefaultUserStatus = Integer
                .parseInt( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS ) );

        // Specific attributes
        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithFields( getLocale( ) );

        // creation in no-module mode : load empty form
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            Map<String, Object> model = new HashMap<>( );

            model.put( MARK_USER_LEVELS_LIST, filteredLevels );
            model.put( MARK_CURRENT_USER, currentUser );
            model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( getLocale( ) ) );
            model.put( MARK_DEFAULT_USER_LEVEL, defaultLevel );
            model.put( MARK_DEFAULT_USER_NOTIFICATION, nDefaultUserNotification );
            model.put( MARK_DEFAULT_USER_LANGUAGE, strDefaultUserLanguage );
            model.put( MARK_DEFAULT_USER_STATUS, nDefaultUserStatus );
            model.put( MARK_ATTRIBUTES_LIST, listAttributes );
            model.put( MARK_LOCALE, getLocale( ) );
            model.put( MARK_DEFAULT_VALUE_WORKGROUP_KEY, AdminWorkgroupService.ALL_GROUPS );
            model.put( MARK_WORKGROUP_KEY_LIST, AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
            model.put( MARK_RANDOM_PASSWORD_SIZE, AppPropertiesService.getPropertyInt(
                    PasswordUtil.PROPERTY_PASSWORD_SIZE, PasswordUtil.CONSTANT_DEFAULT_RANDOM_PASSWORD_SIZE ) );
            model.put( MARK_MINIMUM_PASSWORD_SIZE,
                    AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH ) );
            model.put( SecurityTokenService.MARK_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_USER ) );

            template = AppTemplateService.getTemplate( TEMPLATE_DEFAULT_CREATE_USER, getLocale( ), model );
        }
        else
        // creation in module mode : populate the form with the data from the user
        // selected for import
        {
            // parameters retrieved from the "import" action (retrieves the data from the
            // access code)
            String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
            AdminUser user = null;

            if ( ( strAccessCode != null ) && ( !strAccessCode.equals( "" ) ) )
            {
                user = AdminAuthenticationService.getInstance( ).getUserPublicDataFromModule( strAccessCode );
            }

            Map<String, Object> model = new HashMap<>( );

            if ( user != null )
            {
                model.put( MARK_USER_LEVELS_LIST, filteredLevels );
                model.put( MARK_CURRENT_USER, currentUser );
                model.put( MARK_IMPORT_USER, user );
                model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale( ) ) );
                model.put( MARK_DEFAULT_USER_LEVEL, defaultLevel );
                model.put( MARK_DEFAULT_USER_NOTIFICATION, nDefaultUserNotification );
                model.put( MARK_DEFAULT_USER_LANGUAGE, strDefaultUserLanguage );
                model.put( MARK_DEFAULT_USER_STATUS, nDefaultUserStatus );
                model.put( MARK_ATTRIBUTES_LIST, listAttributes );
                model.put( MARK_LOCALE, getLocale( ) );
                model.put( MARK_DEFAULT_VALUE_WORKGROUP_KEY, AdminWorkgroupService.ALL_GROUPS );
                model.put( MARK_WORKGROUP_KEY_LIST,
                        AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
                model.put( SecurityTokenService.MARK_TOKEN,
                        SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_CREATE_USER ) );
            }

            template = AppTemplateService.getTemplate( TEMPLATE_CREATE_USER, getLocale( ), model );
        }

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the data capture form of a new appUser
     *
     * @param request The HTTP Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the security token is invalid
     */
    public String doCreateAdminUser( HttpServletRequest request ) throws AccessDeniedException
    {
        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strStatus = request.getParameter( PARAMETER_STATUS );
        String strUserLevel = request.getParameter( PARAMETER_USER_LEVEL );
        String strNotifyUser = request.getParameter( PARAMETER_NOTIFY_USER );
        String strAccessibilityMode = request.getParameter( PARAMETER_ACCESSIBILITY_MODE );
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );

        String message = checkParameters( request, TEMPLATE_CREATE_USER );

        if ( message != null )
        {
            return message;
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
        int nNewUserLevel = Integer.parseInt( strUserLevel );

        // check if the user is still an admin
        if ( !( getUser( ).hasRights( nNewUserLevel ) || getUser( ).isAdmin( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        // creation in no-module mode : we manage the password
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( );
            String strFirstPassword = request.getParameter( PARAMETER_FIRST_PASSWORD );
            String strSecondPassword = request.getParameter( PARAMETER_SECOND_PASSWORD );

            if ( StringUtils.isEmpty( strFirstPassword ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            if ( !strFirstPassword.equals( strSecondPassword ) )
            {
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_DIFFERENTS_PASSWORD,
                        AdminMessage.TYPE_STOP );
            }

            String strUrl = AdminUserService.checkPassword( request, strFirstPassword, 0 );

            if ( StringUtils.isNotEmpty( strUrl ) )
            {
                return strUrl;
            }

            user.setPassword( AdminUserService.encryptPassword( strFirstPassword ) );

            user.setPasswordMaxValidDate( AdminUserService.getPasswordMaxValidDate( ) );
            user.setAccountMaxValidDate( AdminUserService.getAccountMaxValidDate( ) );

            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );

            user.setUserLevel( nNewUserLevel );
            user.setPasswordReset( Boolean.TRUE );
            user.setAccessibilityMode( strAccessibilityMode != null );
            user.setWorkgroupKey( strWorkgroupKey );

            AdminUserHome.create( user );
            AdminUserFieldService.doCreateUserFields( user, request, getLocale( ) );

            if ( CONSTANTE_UN.equals( strNotifyUser ) )
            {
                // Notify user for the creation of this account
                AdminUserService.notifyUser( AppPathService.getBaseUrl( request ), user, strFirstPassword,
                        PROPERTY_MESSAGE_EMAIL_SUBJECT_NOTIFY_USER, TEMPLATE_NOTIFY_USER );
            }
        }
        else
        {
            AdminUser user = new AdminUser( );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );

            user.setUserLevel( nNewUserLevel );
            user.setAccessibilityMode( strAccessibilityMode != null );
            user.setWorkgroupKey( strWorkgroupKey );

            AdminUserHome.create( user );
            AdminUserFieldService.doCreateUserFields( user, request, getLocale( ) );
        }

        return JSP_MANAGE_USER;
    }

    private String checkParameters( HttpServletRequest request, String jspUrl ) throws AccessDeniedException
    {
        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );

        if ( StringUtils.isEmpty( strAccessCode ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( StringUtils.isEmpty( strLastName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( StringUtils.isEmpty( strFirstName ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( StringUtils.isBlank( strEmail ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !AdminUserService.checkEmail( strEmail ) )
        {
            return AdminUserService.getEmailErrorMessageUrl( request );
        }

        String strError = AdminUserFieldService.checkUserFields( request, getLocale( ) );

        if ( strError != null )
        {
            return strError;
        }

        if ( !SecurityTokenService.getInstance( ).validate( request, jspUrl ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        return null;
    }

    /**
     * Returns the form to update info about a AppUser
     *
     * @param request The Http request
     * @return The HTML form to update info
     * @throws AccessDeniedException If the current user is not authorized to modify
     *                               the user
     */
    public String getModifyAdminUser( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MODIFY_USER_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );

        int nUserId = Integer.parseInt( strUserId );

        Map<String, Object> model = new HashMap<>( );
        HtmlTemplate template;

        AdminUser user;
        String strTemplateUrl;

        // creation in no-module mode : load form with password modification field and
        // login modification field
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );
            strTemplateUrl = TEMPLATE_DEFAULT_MODIFY_USER;
        }
        else
        {
            user = AdminUserHome.findByPrimaryKey( nUserId );
            strTemplateUrl = TEMPLATE_MODIFY_USER;
        }

        if ( ( user == null ) || ( user.getUserId( ) == 0 ) )
        {
            return getManageAdminUsers( request );
        }

        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        Level level = LevelHome.findByPrimaryKey( user.getUserLevel( ) );

        // ITEM NAVIGATION
        setItemNavigator( user.getUserId( ), AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_USER );

        List<IAttribute> listAttributes = AttributeService.getInstance( ).getAllAttributesWithFields( getLocale( ) );
        Map<String, Object> map = AdminUserFieldService.getAdminUserFields( listAttributes, nUserId, getLocale( ) );

        model.put( MARK_USER, user );
        model.put( MARK_LEVEL, level );
        model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale( ) ) );
        model.put( MARK_CURRENT_LANGUAGE, user.getLocale( ).getLanguage( ) );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_ATTRIBUTES_LIST, listAttributes );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( MARK_MAP_LIST_ATTRIBUTE_DEFAULT_VALUES, map );
        model.put( MARK_WORKGROUP_KEY_LIST, AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_MODIFY_USER ) );

        template = AppTemplateService.getTemplate( strTemplateUrl, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the change form of an appUser
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the current user is not authorized to modify
     *                               the user
     */
    public String doModifyAdminUser( HttpServletRequest request ) throws AccessDeniedException
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        String strAccessCode = request.getParameter( PARAMETER_ACCESS_CODE );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strStatus = request.getParameter( PARAMETER_STATUS );
        String strAccessibilityMode = request.getParameter( PARAMETER_ACCESSIBILITY_MODE );
        String strWorkgroupKey = request.getParameter( PARAMETER_WORKGROUP_KEY );

        int nUserId = Integer.parseInt( strUserId );

        AdminUser userToModify = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, userToModify ) )
        {
            throw new AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        String message = checkParameters( request, JSP_URL_MODIFY_USER );

        if ( message != null )
        {
            return message;
        }

        int checkCode = AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode );

        // check again that access code is not in use
        if ( ( checkCode != -1 ) && ( checkCode != nUserId ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ACCESS_CODE_ALREADY_USED,
                    AdminMessage.TYPE_STOP );
        }

        checkCode = AdminUserHome.checkEmailAlreadyInUse( strEmail );

        // check again that email is not in use
        if ( ( checkCode != -1 ) && ( checkCode != nUserId ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_EMAIL_ALREADY_USED,
                    AdminMessage.TYPE_STOP );
        }

        // modification in no-module mode : we manage the password
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            LuteceDefaultAdminUser user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );

            user.setUserId( nUserId );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setWorkgroupKey( strWorkgroupKey );

            int nStatus = Integer.parseInt( strStatus );

            if ( nStatus != user.getStatus( ) )
            {
                user.setStatus( nStatus );
                AdminUserService.notifyUser( AppPathService.getBaseUrl( request ), user,
                        PROPERTY_MESSAGE_EMAIL_SUBJECT_CHANGE_STATUS, TEMPLATE_ADMIN_EMAIL_CHANGE_STATUS );
            }

            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );
            user.setAccessibilityMode( strAccessibilityMode != null );

            AdminUserHome.update( user, PasswordUpdateMode.IGNORE );

            AdminUserFieldService.doModifyUserFields( user, request, getLocale( ), getUser( ) );
        }
        else
        {
            AdminUser user = new AdminUser( );
            user.setUserId( nUserId );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );
            user.setAccessibilityMode( strAccessibilityMode != null );
            user.setWorkgroupKey( strWorkgroupKey );

            String strError = AdminUserFieldService.checkUserFields( request, getLocale( ) );

            if ( strError != null )
            {
                return strError;
            }

            AdminUserHome.update( user );

            AdminUserFieldService.doModifyUserFields( user, request, getLocale( ), getUser( ) );
        }

        return JSP_MANAGE_USER;
    }

    /**
     * Returns the form to update password of AppUser
     *
     * @param request The Http request
     * @return The HTML form to update info
     * @throws AccessDeniedException If the current user is not authorized to modify
     *                               the user
     */
    public String getModifyUserPassword( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MODIFY_USER_PASSWORD_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );

        int nUserId = Integer.parseInt( strUserId );

        Map<String, Object> model = new HashMap<>( );
        HtmlTemplate template;

        AdminUser user = null;
        String strTemplateUrl = "";

        // creation in no-module mode : load form with password modification field and
        // login modification field
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );
            strTemplateUrl = TEMPLATE_DEFAULT_MODIFY_USER_PASSWORD;
        }
        else
        {
            throw new AppException( "Cannot modify password when not in DeafultModuleUsed mode" );
        }

        if ( ( user == null ) || ( user.getUserId( ) == 0 ) )
        {
            throw new AppException( "User to modify password for not found" );
        }

        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        // ITEM NAVIGATION
        setItemNavigator( user.getUserId( ), AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_USER );

        model.put( MARK_USER, user );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_MINIMUM_PASSWORD_SIZE,
                AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, PROPERTY_MODIFY_USER_PASSWORD_PAGETITLE ) );

        template = AppTemplateService.getTemplate( strTemplateUrl, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the change form of an appUser password
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the current user is not authorized to modify
     *                               the user
     */
    public String doModifyAdminUserPassword( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, PROPERTY_MODIFY_USER_PASSWORD_PAGETITLE ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strUserId = request.getParameter( PARAMETER_USER_ID );

        int nUserId = Integer.parseInt( strUserId );

        AdminUser userToModify = AdminUserHome.findByPrimaryKey( nUserId );

        if ( userToModify == null )
        {
            throw new AppException( CONSTANT_USER_MSG + strUserId + " not found" );
        }

        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, userToModify ) )
        {
            throw new AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        // modification in no-module mode : we manage the password
        if ( AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) )
        {
            LuteceDefaultAdminUser user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );

            String strFirstPassword = request.getParameter( PARAMETER_FIRST_PASSWORD );
            String strSecondPassword = request.getParameter( PARAMETER_SECOND_PASSWORD );

            if ( StringUtils.isEmpty( strFirstPassword ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, new String[]
                { I18nService.getLocalizedString( PROPERTY_LABEL_FIRST_PASSWORD, getLocale( ) ) },
                        AdminMessage.TYPE_STOP );
            }

            if ( !StringUtils.equals( strFirstPassword, strSecondPassword ) )
            {
                // First and second password are filled but there are different
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_DIFFERENTS_PASSWORD,
                        AdminMessage.TYPE_STOP );
            }

            String strUrl = AdminUserService.checkPassword( request, strFirstPassword, nUserId, Boolean.TRUE );

            if ( StringUtils.isNotEmpty( strUrl ) )
            {
                return strUrl;
            }

            user.setPassword( AdminUserService.encryptPassword( strFirstPassword ) );
            user.setPasswordReset( Boolean.FALSE );
            user.setPasswordMaxValidDate( AdminUserService.getPasswordMaxValidDate( ) );

            AdminUserHome.update( user, PasswordUpdateMode.UPDATE );

        }

        return JSP_MANAGE_USER;
    }

    /**
     * Get a page to import users from a CSV file.
     * 
     * @param request The request
     * @return The HTML content
     */
    public String getImportUsersFromFile( HttpServletRequest request )
    {
        _importAdminUserService = SpringContextService.getBean( BEAN_IMPORT_ADMIN_USER_SERVICE );
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            return getManageAdminUsers( request );
        }

        setPageTitleProperty( PROPERTY_IMPORT_USERS_FROM_FILE_PAGETITLE );

        Map<String, Object> model = new HashMap<>( );

        model.put( MARK_LIST_MESSAGES, request.getAttribute( ATTRIBUTE_IMPORT_USERS_LIST_MESSAGES ) );

        String strCsvSeparator = StringUtils.EMPTY + _importAdminUserService.getCSVSeparator( );
        String strCsvEscapeCharacter = StringUtils.EMPTY + _importAdminUserService.getCSVEscapeCharacter( );
        String strAttributesSeparator = StringUtils.EMPTY + _importAdminUserService.getAttributesSeparator( );
        model.put( MARK_CSV_SEPARATOR, strCsvSeparator );
        model.put( MARK_CSV_ESCAPE, strCsvEscapeCharacter );
        model.put( MARK_ATTRIBUTES_SEPARATOR, strAttributesSeparator );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_IMPORT_USER ) );

        String strTemplate = _importAdminUserService.getImportFromFileTemplate( );
        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, AdminUserService.getLocale( request ),
                model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Do import users from a CSV file
     * 
     * @param request The request
     * @return A DefaultPluginActionResult with the URL of the page to display, or
     *         the HTML content
     * @throws AccessDeniedException if the security token is invalid
     */
    public DefaultPluginActionResult doImportUsersFromFile( HttpServletRequest request ) throws AccessDeniedException
    {
        DefaultPluginActionResult result = new DefaultPluginActionResult( );

        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            result.setHtmlContent( getManageAdminUsers( request ) );

            return result;
        }

        if ( request instanceof MultipartHttpServletRequest )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            FileItem fileItem = multipartRequest.getFile( PARAMETER_IMPORT_USERS_FILE );
            String strMimeType = FileSystemUtil.getMIMEType( FileUploadService.getFileNameOnly( fileItem ) );

            if ( !( ( fileItem != null ) && !StringUtils.EMPTY.equals( fileItem.getName( ) ) ) )
            {
                Object[] tabRequiredFields =
                { I18nService.getLocalizedString( FIELD_IMPORT_USERS_FILE, getLocale( ) ) };
                result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD,
                        tabRequiredFields, AdminMessage.TYPE_STOP ) );

                return result;
            }

            if ( ( !strMimeType.equals( CONSTANT_MIME_TYPE_CSV )
                    && !strMimeType.equals( CONSTANT_MIME_TYPE_OCTETSTREAM )
                    && !strMimeType.equals( CONSTANT_MIME_TYPE_TEXT_CSV ) )
                    || !fileItem.getName( ).toLowerCase( Locale.ENGLISH ).endsWith( CONSTANT_EXTENSION_CSV_FILE ) )
            {
                result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CSV_FILE_IMPORT,
                        AdminMessage.TYPE_STOP ) );

                return result;
            }
            if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_IMPORT_USER ) )
            {
                throw new AccessDeniedException( ERROR_INVALID_TOKEN );
            }

            String strSkipFirstLine = multipartRequest.getParameter( PARAMETER_SKIP_FIRST_LINE );
            boolean bSkipFirstLine = StringUtils.isNotEmpty( strSkipFirstLine );
            String strUpdateUsers = multipartRequest.getParameter( PARAMETER_UPDATE_USERS );
            boolean bUpdateUsers = StringUtils.isNotEmpty( strUpdateUsers );

            _importAdminUserService.setUpdateExistingUsers( bUpdateUsers );

            List<CSVMessageDescriptor> listMessages = _importAdminUserService.readCSVFile( fileItem, 0, false, false,
                    bSkipFirstLine, AdminUserService.getLocale( request ), AppPathService.getBaseUrl( request ) );

            request.setAttribute( ATTRIBUTE_IMPORT_USERS_LIST_MESSAGES, listMessages );

            String strHtmlResult = getImportUsersFromFile( request );
            result.setHtmlContent( strHtmlResult );
        }
        else
        {
            Object[] tabRequiredFields =
            { I18nService.getLocalizedString( FIELD_IMPORT_USERS_FILE, getLocale( ) ) };
            result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields,
                    AdminMessage.TYPE_STOP ) );
        }

        return result;
    }

    /**
     * Get a page to export users
     * 
     * @param request The request
     * @return The html content
     */
    public String getExportUsers( HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            return getManageAdminUsers( request );
        }

        setPageTitleProperty( PROPERTY_EXPORT_USERS_PAGETITLE );

        Map<String, Object> model = new HashMap<>( );

        ReferenceList refListXsl = XslExportHome.getRefListByPlugin( PluginService.getCore( ) );

        model.put( MARK_LIST_XSL_EXPORT, refListXsl );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_EXPORT_USERS_FROM_FILE,
                AdminUserService.getLocale( request ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Do export users
     * 
     * @param request  The request
     * @param response The response
     * @return A DefaultPluginActionResult containing the result, or null if the
     *         file download has been initialized
     * @throws IOException If an IOException occurs
     */
    public DefaultPluginActionResult doExportUsers( HttpServletRequest request, HttpServletResponse response )
            throws IOException
    {
        DefaultPluginActionResult result = new DefaultPluginActionResult( );

        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_IMPORT_EXPORT_USERS, getUser( ) ) )
        {
            result.setHtmlContent( getManageAdminUsers( request ) );

            return result;
        }

        String strXslExportId = request.getParameter( PARAMETER_XSL_EXPORT_ID );
        String strExportAttributes = request.getParameter( PARAMETER_EXPORT_ATTRIBUTES );
        String strExportRoles = request.getParameter( PARAMETER_EXPORT_ROLES );
        String strExportRights = request.getParameter( PARAMETER_EXPORT_RIGHTS );
        String strExportWorkgroups = request.getParameter( PARAMETER_EXPORT_WORKGROUPS );
        boolean bExportAttributes = StringUtils.isNotEmpty( strExportAttributes );
        boolean bExportRoles = StringUtils.isNotEmpty( strExportRoles );
        boolean bExportRights = StringUtils.isNotEmpty( strExportRights );
        boolean bExportWorkgroups = StringUtils.isNotEmpty( strExportWorkgroups );

        if ( StringUtils.isBlank( strXslExportId ) )
        {
            Object[] tabRequiredFields =
            { I18nService.getLocalizedString( FIELD_XSL_EXPORT, getLocale( ) ) };
            result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_MANDATORY_FIELD, tabRequiredFields,
                    AdminMessage.TYPE_STOP ) );

            return result;
        }

        int nIdXslExport = Integer.parseInt( strXslExportId );

        XslExport xslExport = XslExportHome.findByPrimaryKey( nIdXslExport );

        Collection<AdminUser> listUsers = AdminUserHome.findUserList( );

        List<IAttribute> listAttributes = AttributeService.getInstance( )
                .getAllAttributesWithFields( LocaleService.getDefault( ) );
        List<IAttribute> listAttributesFiltered = new ArrayList<>( );

        for ( IAttribute attribute : listAttributes )
        {
            if ( attribute instanceof ISimpleValuesAttributes )
            {
                listAttributesFiltered.add( attribute );
            }
        }

        StringBuffer sbXml = new StringBuffer( XmlUtil.getXmlHeader( ) );
        XmlUtil.beginElement( sbXml, CONSTANT_XML_USERS );

        for ( AdminUser user : listUsers )
        {
            if ( !user.isStatusAnonymized( ) )
            {
                sbXml.append( AdminUserService.getXmlFromUser( user, bExportRoles, bExportRights, bExportWorkgroups,
                        bExportAttributes, listAttributesFiltered ) );
            }
        }

        XmlUtil.endElement( sbXml, CONSTANT_XML_USERS );

        String strXml = StringUtil.replaceAccent( sbXml.toString( ) );
        String strExportedUsers = XslExportService.exportXMLWithXSL( nIdXslExport, strXml );

        if ( CONSTANT_MIME_TYPE_CSV.contains( xslExport.getExtension( ) ) )
        {
            response.setContentType( CONSTANT_MIME_TYPE_CSV );
        }
        else if ( CONSTANT_EXTENSION_XML_FILE.contains( xslExport.getExtension( ) ) )
        {
            response.setContentType( CONSTANT_MIME_TYPE_XML );
        }
        else
        {
            response.setContentType( CONSTANT_MIME_TYPE_OCTETSTREAM );
        }

        String strFileName = CONSTANT_EXPORT_USERS_FILE_NAME + CONSTANT_POINT + xslExport.getExtension( );
        response.setHeader( CONSTANT_ATTACHEMENT_DISPOSITION,
                CONSTANT_ATTACHEMENT_FILE_NAME + strFileName + CONSTANT_QUOTE );

        PrintWriter out = response.getWriter( );
        out.write( strExportedUsers );
        out.flush( );
        out.close( );

        return null;
    }

    /**
     * Returns the page of confirmation for deleting a provider
     *
     * @param request The Http Request
     * @return the confirmation url
     * @throws AccessDeniedException When not authorized
     */
    public String doConfirmRemoveAdminUser( HttpServletRequest request ) throws AccessDeniedException
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

        if ( user == null )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_USER_ERROR_SESSION,
                    AdminMessage.TYPE_ERROR );
        }

        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        String strUrlRemove = JSP_URL_REMOVE_USER;
        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( PARAMETER_USER_ID, strUserId );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_REMOVE_USER ) );

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE, new Object[]
        { user.getFirstName( ), user.getLastName( ), user.getAccessCode( ) }, null, strUrlRemove, null,
                AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Process to the confirmation of deleting of an AppUser
     *
     * @param request The Http Request
     * @return the HTML page
     * @throws AccessDeniedException If the user is not authorized
     */
    public String doRemoveAdminUser( HttpServletRequest request ) throws AccessDeniedException
    {
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

        if ( user == null )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_USER_ERROR_SESSION,
                    AdminMessage.TYPE_ERROR );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_REMOVE_USER ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        AdminUserFieldService.doRemoveUserFields( user, request, getLocale( ) );
        AdminUserHome.removeAllRightsForUser( nUserId );
        AdminUserHome.removeAllRolesForUser( nUserId );
        AdminUserHome.removeAllPasswordHistoryForUser( nUserId );
        AdminUserHome.remove( nUserId );

        return JSP_MANAGE_USER;
    }

    /**
     * Build the User right list
     *
     * @param request Http Request
     * @return the right list
     * @throws AccessDeniedException If the user is not authorized
     */
    public String getManageAdminUserRights( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_USER_RIGHTS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );

        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, selectedUser ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        Collection<Right> rightList = AdminUserHome.getRightsListForUser( nUserId ).values( );

        // ITEM NAVIGATION
        setItemNavigator( selectedUser.getUserId( ),
                AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_RIGHTS );

        HashMap<String, Object> model = new HashMap<>( );
        model.put( MARK_CAN_MODIFY, getUser( ).isParent( selectedUser ) || getUser( ).isAdmin( ) );
        model.put( MARK_CAN_DELEGATE, getUser( ).getUserId( ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_RIGHT_LIST, I18nService.localizeCollection( rightList, getLocale( ) ) );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USER_RIGHTS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Build the User workgroup list
     *
     * @param request Http Request
     * @return the right list
     * @throws AccessDeniedException If the user is not authorized
     */
    public String getManageAdminUserWorkgroups( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_USER_WORKGROUPS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( currentUser, selectedUser ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        ReferenceList workgroupsList = AdminWorkgroupHome.getUserWorkgroups( selectedUser );

        // ITEM NAVIGATION
        setItemNavigator( nUserId, AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_WORKGROUPS );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CAN_MODIFY, getUser( ).isParent( selectedUser ) || getUser( ).isAdmin( ) );
        model.put( MARK_CAN_DELEGATE, getUser( ).getUserId( ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_WORKGROUP_LIST, workgroupsList );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USER_WORKGROUPS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Build the assignable workgroups list
     *
     * @param request Http Request
     * @return the right list
     * @throws AccessDeniedException If the user is not authorized
     */
    public String getModifyAdminUserWorkgroups( HttpServletRequest request ) throws AccessDeniedException
    {
        boolean bDelegateWorkgroups = Boolean.parseBoolean( request.getParameter( PARAMETER_DELEGATE_RIGHTS ) );

        setPageTitleProperty( bDelegateWorkgroups ? PROPERTY_DELEGATE_USER_RIGHTS_PAGETITLE
                : PROPERTY_MODIFY_USER_WORKGROUPS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = getUser( );

        if ( ( user == null ) || ( user.getUserId( ) == 0 ) )
        {
            return getManageAdminUsers( request );
        }

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        ReferenceList userWorkspaces = AdminWorkgroupHome.getUserWorkgroups( user );
        ReferenceList assignableWorkspaces = AdminWorkgroupHome.getUserWorkgroups( currentUser );

        ArrayList<String> checkedValues = new ArrayList<>( );

        for ( ReferenceItem item : userWorkspaces )
        {
            checkedValues.add( item.getCode( ) );
        }

        assignableWorkspaces.checkItems( checkedValues.toArray( new String[checkedValues.size( )] ) );

        // ITEM NAVIGATION
        setItemNavigator( nUserId, AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_WORKGROUPS );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_ALL_WORKSGROUP_LIST, assignableWorkspaces );
        model.put( MARK_CAN_DELEGATE, String.valueOf( bDelegateWorkgroups ) );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_DEFAULT_MODE_USED, AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_MANAGE_USER_WORKGROUPS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER_WORKGROUPS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Build the right list
     *
     * @param request Http Request
     * @return the right list
     * @throws AccessDeniedException If the user is not authorized
     */
    public String getModifyAdminUserRights( HttpServletRequest request ) throws AccessDeniedException
    {
        boolean bDelegateRights = Boolean.parseBoolean( request.getParameter( PARAMETER_DELEGATE_RIGHTS ) );

        String strSelectAll = request.getParameter( PARAMETER_SELECT );
        boolean bSelectAll = ( ( strSelectAll != null ) && strSelectAll.equals( PARAMETER_SELECT_ALL ) );

        setPageTitleProperty(
                bDelegateRights ? PROPERTY_DELEGATE_USER_RIGHTS_PAGETITLE : PROPERTY_MODIFY_USER_RIGHTS_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = getUser( );

        if ( ( user == null ) || ( user.getUserId( ) == 0 ) )
        {
            return getManageAdminUsers( request );
        }

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        Collection<Right> rightList;
        Collection<Right> allRightList = RightHome.getRightsList( user.getUserLevel( ) );

        if ( bDelegateRights )
        {
            Map<String, Right> rights = AdminUserHome.getRightsListForUser( currentUser.getUserId( ) );
            rightList = new ArrayList<>( );

            for ( Right right : rights.values( ) )
            {
                // logged user can only delegate rights with level higher or equal to user
                // level.
                if ( right.getLevel( ) >= user.getUserLevel( ) )
                {
                    rightList.add( right );
                }
            }
        }
        else
        {
            rightList = AdminUserHome.getRightsListForUser( nUserId ).values( );
        }

        // ITEM NAVIGATION
        setItemNavigator( nUserId, AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_RIGHTS );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_RIGHT_LIST, I18nService.localizeCollection( rightList, getLocale( ) ) );
        model.put( MARK_ALL_RIGHT_LIST, I18nService.localizeCollection( allRightList, getLocale( ) ) );
        model.put( MARK_CAN_DELEGATE, String.valueOf( bDelegateRights ) );
        model.put( MARK_SELECT_ALL, bSelectAll );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_DEFAULT_MODE_USED, AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_MANAGE_USER_RIGHTS ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER_RIGHTS, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the change form of an appUser rights
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user is not authorized
     */
    public String doModifyAdminUserRights( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_MANAGE_USER_RIGHTS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        String[] arrayRights = request.getParameterValues( PARAMETER_RIGHT );

        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser userCurrent = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( userCurrent, user ) )
        {
            throw new AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        AdminUserHome.removeAllOwnRightsForUser( user );

        if ( arrayRights != null )
        {
            for ( String strRight : arrayRights )
            {
                AdminUserHome.createRightForUser( nUserId, strRight );
            }
        }

        if ( user.getUserId( ) == userCurrent.getUserId( ) )
        {
            try
            {
                AdminAuthenticationService.getInstance( ).registerUser( request, user );
            }
            catch ( AccessDeniedException | UserNotSignedException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }

        return JSP_MANAGE_USER_RIGHTS + "?" + PARAMETER_USER_ID + "=" + nUserId;
    }

    /**
     * Build the User role list
     *
     * @param request Http Request
     * @return the right list
     * @throws AccessDeniedException If the user is not authorized
     */
    public String getManageAdminUserRoles( HttpServletRequest request ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_USER_ROLES_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser userCurrent = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( userCurrent, selectedUser ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        Collection<RBACRole> roleList = AdminUserHome.getRolesListForUser( nUserId ).values( );

        // ITEM NAVIGATION
        setItemNavigator( nUserId, AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_ROLES );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_CAN_MODIFY, getUser( ).isParent( selectedUser ) || getUser( ).isAdmin( ) );
        model.put( MARK_CAN_DELEGATE, getUser( ).getUserId( ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_ROLE_LIST, roleList );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USER_ROLES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Build the role list
     *
     * @param request Http Request
     * @return the right list
     * @throws AccessDeniedException IF the user is not authorized
     */
    public String getModifyAdminUserRoles( HttpServletRequest request ) throws AccessDeniedException
    {
        boolean bDelegateRoles = Boolean.parseBoolean( request.getParameter( PARAMETER_DELEGATE_RIGHTS ) );
        setPageTitleProperty( PROPERTY_MODIFY_USER_ROLES_PAGETITLE );

        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );

        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser userCurrent = AdminUserService.getAdminUser( request );

        if ( ( selectedUser == null ) || ( selectedUser.getUserId( ) == 0 ) )
        {
            return getManageAdminUsers( request );
        }

        if ( !isUserAuthorizedToModifyUser( userCurrent, selectedUser ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        Collection<RBACRole> roleList = AdminUserHome.getRolesListForUser( nUserId ).values( );
        Collection<RBACRole> assignableRoleList;

        if ( bDelegateRoles )
        {
            // assign connected user roles
            assignableRoleList = new ArrayList<>( );

            AdminUser currentUser = getUser( );

            for ( RBACRole role : RBACRoleHome.findAll( ) )
            {
                if ( currentUser.isAdmin( ) || RBACService.isUserInRole( currentUser, role.getKey( ) ) )
                {
                    assignableRoleList.add( role );
                }
            }
        }
        else
        {
            // assign all available roles
            assignableRoleList = RBACRoleHome.findAll( );
        }

        // ITEM NAVIGATION
        setItemNavigator( nUserId, AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USER_ROLES );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_ROLE_LIST, roleList );
        model.put( MARK_ALL_ROLE_LIST, assignableRoleList );
        model.put( MARK_ITEM_NAVIGATOR, _itemNavigator );
        model.put( MARK_DEFAULT_MODE_USED, AdminAuthenticationService.getInstance( ).isDefaultModuleUsed( ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_MANAGE_USER_ROLES ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER_ROLES, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Process the change form of an appUser roles
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException IF the user is not authorized
     */
    public String doModifyAdminUserRoles( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_MANAGE_USER_ROLES ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser selectedUser = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser userCurrent = AdminUserService.getAdminUser( request );

        if ( !isUserAuthorizedToModifyUser( userCurrent, selectedUser ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        String[] arrayRoles = request.getParameterValues( PARAMETER_ROLE );

        AdminUserHome.removeAllRolesForUser( nUserId );

        if ( arrayRoles != null )
        {
            for ( String strRole : arrayRoles )
            {
                AdminUserHome.createRoleForUser( nUserId, strRole );
            }
        }

        return JSP_MANAGE_USER_ROLES + "?" + PARAMETER_USER_ID + "=" + nUserId;
    }

    /**
     * Process the change form of an appUser workspaces
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user is not authorized
     */
    public String doModifyAdminUserWorkgroups( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_MANAGE_USER_WORKGROUPS ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strUserId = request.getParameter( PARAMETER_USER_ID );
        int nUserId = Integer.parseInt( strUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );
        AdminUser currentUser = getUser( );

        if ( !isUserAuthorizedToModifyUser( currentUser, user ) )
        {
            throw new fr.paris.lutece.portal.service.admin.AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }

        String[] arrayWorkspaces = request.getParameterValues( PARAMETER_WORKGROUP );
        ReferenceList assignableWorkgroups = AdminWorkgroupHome.getUserWorkgroups( currentUser );

        for ( ReferenceItem item : assignableWorkgroups )
        {
            AdminWorkgroupHome.removeUserFromWorkgroup( user, item.getCode( ) );
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
     * 
     * @param user1 User1
     * @param user2 User2
     * @return true or false
     */
    private boolean haveCommonWorkgroups( AdminUser user1, AdminUser user2 )
    {
        ReferenceList workgroups = AdminWorkgroupHome.getUserWorkgroups( user1 );

        if ( workgroups.isEmpty( ) )
        {
            return true;
        }

        for ( ReferenceItem item : workgroups )
        {
            if ( AdminWorkgroupHome.isUserInWorkgroup( user2, item.getCode( ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Modify the default user parameter values.
     * 
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user does not have the permission
     */
    public String doModifyDefaultUserParameterValues( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            throw new AccessDeniedException( CONSTANT_USER_MSG + getUser( ) + CONSTANT_NOT_AUTHORIZED
                    + AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS );
        }

        DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_STATUS,
                request.getParameter( PARAMETER_STATUS ) );
        DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LEVEL,
                request.getParameter( PARAMETER_USER_LEVEL ) );
        DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION,
                request.getParameter( PARAMETER_NOTIFY_USER ) );
        DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE,
                request.getParameter( PARAMETER_LANGUAGE ) );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Modify the default user parameter security values.
     * 
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user does not have the permission
     */
    public String doModifyDefaultUserSecurityValues( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            throw new AccessDeniedException( CONSTANT_USER_MSG + getUser( ) + CONSTANT_NOT_AUTHORIZED
                    + AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS );
        }

        String strForceChangePasswordValue = request.getParameter( PARAMETER_FORCE_CHANGE_PASSWORD_REINIT );
        strForceChangePasswordValue = StringUtils.isNotBlank( strForceChangePasswordValue )
                ? strForceChangePasswordValue
                : StringUtils.EMPTY;

        DefaultUserParameterHome.update( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT,
                strForceChangePasswordValue );

        // Parameter password length
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH,
                request.getParameter( PARAMETER_PASSWORD_MINIMUM_LENGTH ) );

        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY,
                request.getParameter( PARAMETER_RESET_TOKEN_VALIDITY ) );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION,
                request.getParameter( PARAMETER_LOCK_RESET_TOKEN_TO_SESSION ) );

        boolean bUseAdvancedSecurityParameter = AdminUserService
                .getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );

        if ( bUseAdvancedSecurityParameter )
        {
            // Parameter format
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_FORMAT_UPPER_LOWER_CASE,
                    request.getParameter( PARAMETER_PASSWORD_FORMAT_UPPER_LOWER_CASE ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_FORMAT_NUMERO,
                    request.getParameter( PARAMETER_PASSWORD_FORMAT_NUMERO ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_FORMAT_SPECIAL_CHARACTERS,
                    request.getParameter( PARAMETER_PASSWORD_FORMAT_SPECIAL_CHARACTERS ) );
            // Parameter password duration
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_DURATION,
                    request.getParameter( PARAMETER_PASSWORD_DURATION ) );

            // Password history size
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_HISTORY_SIZE,
                    request.getParameter( PARAMETER_PASSWORD_HISTORY_SIZE ) );

            // maximum number of password change
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_MAXIMUM_NUMBER_PASSWORD_CHANGE,
                    request.getParameter( PARAMETER_MAXIMUM_NUMBER_PASSWORD_CHANGE ) );

            // maximum number of password change
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TSW_SIZE_PASSWORD_CHANGE,
                    request.getParameter( PARAMETER_TSW_SIZE_PASSWORD_CHANGE ) );

            // Notify user when his password expires
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_NOTIFY_USER_PASSWORD_EXPIRED,
                    request.getParameter( PARAMETER_NOTIFY_USER_PASSWORD_EXPIRED ) );
        }

        // Time of life of accounts
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME,
                request.getParameter( PARAMETER_ACCOUNT_LIFE_TIME ) );

        // Time before the first alert when an account will expire
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT,
                request.getParameter( PARAMETER_TIME_BEFORE_ALERT_ACCOUNT ) );

        // Number of alerts sent to a user when his account will expire
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT,
                request.getParameter( PARAMETER_NB_ALERT_ACCOUNT ) );

        // Time between alerts
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT,
                request.getParameter( PARAMETER_TIME_BETWEEN_ALERTS_ACCOUNT ) );

        // Max access failure
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX,
                request.getParameter( MARK_ACCESS_FAILURES_MAX ) );

        // Access failure interval
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL,
                request.getParameter( MARK_ACCESS_FAILURES_INTERVAL ) );

        // Banned domain names
        AdminUserService.updateLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES,
                request.getParameter( MARK_BANNED_DOMAIN_NAMES ) );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Modify the email pattern
     * 
     * @param request HttpServletRequest
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException If the user does not have the permission
     */
    public String doModifyEmailPattern( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            throw new AccessDeniedException( CONSTANT_USER_MSG + getUser( ) + CONSTANT_NOT_AUTHORIZED
                    + AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS );
        }
        if ( PARAMETER_RESET.equals( request.getParameter( PARAMETER_RESET ) ) )
        {
            if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
            {
                throw new AccessDeniedException( ERROR_INVALID_TOKEN );
            }
            return doResetEmailPattern( );
        }
        String strSetManually = request.getParameter( PARAMETER_IS_EMAIL_PATTERN_SET_MANUALLY );
        String strEmailPattern = request.getParameter( PARAMETER_EMAIL_PATTERN );

        if ( StringUtils.isNotBlank( strEmailPattern ) )
        {
            if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
            {
                throw new AccessDeniedException( ERROR_INVALID_TOKEN );
            }
            AdminUserService.doModifyEmailPattern( strEmailPattern, strSetManually != null );
            return JSP_MANAGE_ADVANCED_PARAMETERS;
        }
        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ERROR_EMAIL_PATTERN,
                AdminMessage.TYPE_STOP );
    }

    /**
     * Reset the email pattern
     * 
     * @return the jsp return
     */
    private String doResetEmailPattern( )
    {
        AdminUserService.doResetEmailPattern( );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Do insert a regular expression
     * 
     * @param request {@link HttpServletRequest}
     * @return the jsp return
     * @throws AccessDeniedException access denied if the AdminUser does not have
     *                               the permission
     */
    public String doInsertRegularExpression( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            throw new AccessDeniedException( CONSTANT_USER_MSG + getUser( ) + CONSTANT_NOT_AUTHORIZED
                    + AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS );
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
     * 
     * @param request {@link HttpServletRequest}
     * @return the jsp return
     * @throws AccessDeniedException access denied if the AdminUser does not have
     *                               the permission
     */
    public String doRemoveRegularExpression( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
            throw new AccessDeniedException( CONSTANT_USER_MSG + getUser( ) + CONSTANT_NOT_AUTHORIZED
                    + AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS );
        }

        String strRegularExpressionId = request.getParameter( PARAMETER_ID_EXPRESSION );

        if ( StringUtils.isNotBlank( strRegularExpressionId ) && StringUtils.isNumeric( strRegularExpressionId ) )
        {
            int nRegularExpressionId = Integer.parseInt( strRegularExpressionId );
            AdminUserService.doRemoveRegularExpression( nRegularExpressionId );
        }

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Get the admin message to confirm the enabling or the disabling of the
     * advanced security parameters
     * 
     * @param request The request
     * @return The url of the admin message
     */
    public String getChangeUseAdvancedSecurityParameters( HttpServletRequest request )
    {
        Map<String, Object> parameters = new HashMap<>( 1 );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TOKEN_TECHNICAL_ADMIN ) );

        if ( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_REMOVE_ASP,
                    JSP_URL_REMOVE_ADVANCED_SECUR_PARAM, AdminMessage.TYPE_CONFIRMATION, parameters );
        }

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_USE_ASP,
                JSP_URL_USE_ADVANCED_SECUR_PARAM, AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Enable advanced security parameters
     * 
     * @param request The request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException if the security token is invalid
     */
    public String doUseAdvancedSecurityParameters( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        AdminUserService.useAdvancedSecurityParameters( );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Disable advanced security parameters
     * 
     * @param request The request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException if the security token is invalid
     */
    public String doRemoveAdvancedSecurityParameters( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TOKEN_TECHNICAL_ADMIN ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        AdminUserService.removeAdvancedSecurityParameters( );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Get the page with the list of every anonymizable attribute
     * 
     * @param request The request
     * @return The admin page
     */
    public String getChangeFieldAnonymizeAdminUsers( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );

        List<IAttribute> listAllAttributes = AttributeService.getInstance( )
                .getAllAttributesWithoutFields( getLocale( ) );
        List<IAttribute> listAttributesText = new ArrayList<>( );

        for ( IAttribute attribut : listAllAttributes )
        {
            if ( attribut.isAnonymizable( ) )
            {
                listAttributesText.add( attribut );
            }
        }

        model.put( MARK_ATTRIBUTES_LIST, listAttributesText );

        model.putAll( AdminUserHome.getAnonymizationStatusUserStaticField( ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TOKEN_TECHNICAL_ADMIN ) );

        setPageTitleProperty( PROPERTY_MESSAGE_TITLE_CHANGE_ANONYMIZE_USER );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FIELD_ANONYMIZE_ADMIN_USER, getLocale( ),
                model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Change the anonymization status of user parameters.
     * 
     * @param request The request
     * @return the Jsp URL of the process result
     * @throws AccessDeniedException If the security token is invalid
     */
    public String doChangeFieldAnonymizeAdminUsers( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( request.getParameter( PARAMETER_CANCEL ) != null )
        {
            return JSP_MANAGE_ADVANCED_PARAMETERS;
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_FIELD_ANONYMIZE_ADMIN_USER ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        AdminUserHome.updateAnonymizationStatusUserStaticField( PARAMETER_ACCESS_CODE,
                Boolean.valueOf( request.getParameter( PARAMETER_ACCESS_CODE ) ) );
        AdminUserHome.updateAnonymizationStatusUserStaticField( PARAMETER_FIRST_NAME,
                Boolean.valueOf( request.getParameter( PARAMETER_FIRST_NAME ) ) );
        AdminUserHome.updateAnonymizationStatusUserStaticField( PARAMETER_LAST_NAME,
                Boolean.valueOf( request.getParameter( PARAMETER_LAST_NAME ) ) );
        AdminUserHome.updateAnonymizationStatusUserStaticField( PARAMETER_EMAIL,
                Boolean.valueOf( request.getParameter( PARAMETER_EMAIL ) ) );

        AttributeService attributeService = AttributeService.getInstance( );

        List<IAttribute> listAllAttributes = attributeService.getAllAttributesWithoutFields( getLocale( ) );
        List<IAttribute> listAttributesText = new ArrayList<>( );

        for ( IAttribute attribut : listAllAttributes )
        {
            if ( attribut.isAnonymizable( ) )
            {
                listAttributesText.add( attribut );
            }
        }

        for ( IAttribute attribute : listAttributesText )
        {
            Boolean bNewValue = Boolean.valueOf(
                    request.getParameter( PARAMETER_ATTRIBUTE + Integer.toString( attribute.getIdAttribute( ) ) ) );
            attributeService.updateAnonymizationStatusUserField( attribute.getIdAttribute( ), bNewValue );
        }

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Get the confirmation page before anonymizing a user.
     * 
     * @param request The request
     * @return The URL of the confirmation page
     */
    public String getAnonymizeAdminUser( HttpServletRequest request )
    {
        String strAdminUserId = request.getParameter( PARAMETER_USER_ID );

        if ( !StringUtils.isNumeric( strAdminUserId ) || strAdminUserId.isEmpty( ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_NO_ADMIN_USER_SELECTED,
                    AdminMessage.TYPE_STOP );
        }

        int nUserId = Integer.parseInt( strAdminUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

        if ( user == null )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_USER_ERROR_SESSION,
                    AdminMessage.TYPE_ERROR );
        }

        String strUrl = JSP_URL_ANONYMIZE_ADMIN_USER;
        Map<String, Object> parameters = new HashMap<>( );
        parameters.put( PARAMETER_USER_ID, strAdminUserId );
        parameters.put( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, JSP_URL_ANONYMIZE_ADMIN_USER ) );

        return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_CONFIRM_ANONYMIZE_USER, new Object[]
        { user.getFirstName( ), user.getLastName( ), user.getAccessCode( ) }, null, strUrl, null,
                AdminMessage.TYPE_CONFIRMATION, parameters );
    }

    /**
     * Anonymize a user
     * 
     * @param request The request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException in case of invalid security token
     */
    public String doAnonymizeAdminUser( HttpServletRequest request ) throws AccessDeniedException
    {
        String strAdminUserId = request.getParameter( PARAMETER_USER_ID );

        if ( !StringUtils.isNumeric( strAdminUserId ) || strAdminUserId.isEmpty( ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_NO_ADMIN_USER_SELECTED,
                    AdminMessage.TYPE_STOP );
        }

        int nUserId = Integer.parseInt( strAdminUserId );
        AdminUser user = AdminUserHome.findByPrimaryKey( nUserId );

        if ( user == null )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_USER_ERROR_SESSION,
                    AdminMessage.TYPE_ERROR );
        }
        if ( !SecurityTokenService.getInstance( ).validate( request, JSP_URL_ANONYMIZE_ADMIN_USER ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }

        AdminUserService.anonymizeUser( nUserId, getLocale( ) );

        return JSP_MANAGE_USER;
    }

    /**
     * Update a user account life time
     * 
     * @param request The request
     * @return The Jsp URL of the process result
     */
    public String reactivateAccount( HttpServletRequest request )
    {
        AdminUser user = AdminUserHome.findByPrimaryKey( AdminUserService.getAdminUser( request ).getUserId( ) );
        String strUrl;
        int nbDaysBeforeFirstAlert = AdminUserService
                .getIntegerSecurityParameter( PARAMETER_TIME_BEFORE_ALERT_ACCOUNT );
        Timestamp firstAlertMaxDate = new Timestamp(
                new java.util.Date( ).getTime( ) + DateUtil.convertDaysInMiliseconds( nbDaysBeforeFirstAlert ) );

        if ( user.getAccountMaxValidDate( ) != null )
        {
            // If the account is close to expire but has not expired yet
            if ( ( user.getAccountMaxValidDate( ).getTime( ) < firstAlertMaxDate.getTime( ) )
                    && ( user.getStatus( ) < AdminUser.EXPIRED_CODE ) )
            {
                AdminUserService.updateUserExpirationDate( user );
            }

            strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ACCOUNT_REACTIVATED,
                    AppPathService.getAdminMenuUrl( ), AdminMessage.TYPE_INFO );
        }
        else
        {
            strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_NO_ACCOUNT_TO_REACTIVATED,
                    AppPathService.getAdminMenuUrl( ), AdminMessage.TYPE_ERROR );
        }

        return strUrl;
    }

    /**
     * Get the modify account life time emails page
     * 
     * @param request The request
     * @return The html to display
     */
    public String getModifyAccountLifeTimeEmails( HttpServletRequest request )
    {
        String strEmailType = request.getParameter( PARAMETER_EMAIL_TYPE );

        Map<String, Object> model = new HashMap<>( );
        String strSenderKey = StringUtils.EMPTY;
        String strSubjectKey = StringUtils.EMPTY;
        String strBodyKey = StringUtils.EMPTY;
        String strTitle = StringUtils.EMPTY;

        if ( CONSTANT_EMAIL_TYPE_FIRST.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_FIRST_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_FIRST_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_FIRST_ALERT_MAIL;
            strTitle = PROPERTY_FIRST_EMAIL;
        }
        else if ( CONSTANT_EMAIL_TYPE_OTHER.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_OTHER_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_OTHER_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_OTHER_ALERT_MAIL;
            strTitle = PROPERTY_OTHER_EMAIL;
        }
        else if ( CONSTANT_EMAIL_TYPE_EXPIRED.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_EXPIRED_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_EXPIRATION_MAIL;
            strTitle = PROPERTY_ACCOUNT_DEACTIVATES_EMAIL;
        }
        else if ( CONSTANT_EMAIL_TYPE_REACTIVATED.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_REACTIVATED_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_REACTIVATED_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_ACCOUNT_REACTIVATED;
            strTitle = PROPERTY_ACCOUNT_UPDATED_EMAIL;
        }
        else if ( CONSTANT_EMAIL_PASSWORD_EXPIRED.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_PASSWORD_EXPIRED_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_PASSWORD_EXPIRED_MAIL_SUBJECT;
            strBodyKey = PARAMETER_NOTIFY_PASSWORD_EXPIRED;
            strTitle = PROPERTY_NOTIFY_PASSWORD_EXPIRED;
        }

        String defaultUserParameter = DefaultUserParameterHome.findByKey( strSenderKey );
        String strSender = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

        defaultUserParameter = DefaultUserParameterHome.findByKey( strSubjectKey );

        String strSubject = ( defaultUserParameter == null ) ? StringUtils.EMPTY : defaultUserParameter;

        model.put( PARAMETER_EMAIL_TYPE, strEmailType );
        model.put( MARK_EMAIL_SENDER, strSender );
        model.put( MARK_EMAIL_SUBJECT, strSubject );
        model.put( MARK_EMAIL_BODY, DatabaseTemplateService.getTemplateFromKey( strBodyKey ) );
        model.put( MARK_EMAIL_LABEL, strTitle );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale( ) );
        model.put( SecurityTokenService.MARK_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_ACCOUNT_LIFE_TIME_EMAIL ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ACCOUNT_LIFE_TIME_EMAIL, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Update an account life time email
     * 
     * @param request The request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException if the security token is invalid
     */
    public String doModifyAccountLifeTimeEmails( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_ACCOUNT_LIFE_TIME_EMAIL ) )
        {
            throw new AccessDeniedException( ERROR_INVALID_TOKEN );
        }
        String strEmailType = request.getParameter( PARAMETER_EMAIL_TYPE );

        String strSenderKey = StringUtils.EMPTY;
        String strSubjectKey = StringUtils.EMPTY;
        String strBodyKey = StringUtils.EMPTY;

        if ( CONSTANT_EMAIL_TYPE_FIRST.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_FIRST_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_FIRST_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_FIRST_ALERT_MAIL;
        }
        else if ( CONSTANT_EMAIL_TYPE_OTHER.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_OTHER_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_OTHER_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_OTHER_ALERT_MAIL;
        }
        else if ( CONSTANT_EMAIL_TYPE_EXPIRED.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_EXPIRED_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_EXPIRED_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_EXPIRATION_MAIL;
        }
        else if ( CONSTANT_EMAIL_TYPE_REACTIVATED.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_REACTIVATED_ALERT_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_REACTIVATED_ALERT_MAIL_SUBJECT;
            strBodyKey = PARAMETER_ACCOUNT_REACTIVATED;
        }
        else if ( CONSTANT_EMAIL_PASSWORD_EXPIRED.equalsIgnoreCase( strEmailType ) )
        {
            strSenderKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_PASSWORD_EXPIRED_MAIL_SENDER;
            strSubjectKey = CONSTANT_ADVANCED_PARAMS + PARAMETER_PASSWORD_EXPIRED_MAIL_SUBJECT;
            strBodyKey = PARAMETER_NOTIFY_PASSWORD_EXPIRED;
        }

        AdminUserService.updateSecurityParameter( strSenderKey, request.getParameter( MARK_EMAIL_SENDER ) );
        AdminUserService.updateSecurityParameter( strSubjectKey, request.getParameter( MARK_EMAIL_SUBJECT ) );
        DatabaseTemplateService.updateTemplate( strBodyKey, request.getParameter( MARK_EMAIL_BODY ) );

        return JSP_MANAGE_ADVANCED_PARAMETERS;
    }

    /**
     * Get the item navigator
     * 
     * @param nIdAdminUser the admin user id
     * @param strUrl       the url
     */
    private void setItemNavigator( int nIdAdminUser, String strUrl )
    {
        if ( _itemNavigator == null )
        {
            List<String> listIdsRight = new ArrayList<>( );
            int nCurrentItemId = 0;
            int nIndex = 0;

            AdminUser currentUser = getUser( );

            for ( AdminUser adminUser : AdminUserHome.findUserList( ) )
            {
                if ( ( adminUser != null ) && isUserAuthorizedToModifyUser( currentUser, adminUser ) )
                {
                    listIdsRight.add( Integer.toString( adminUser.getUserId( ) ) );

                    if ( adminUser.getUserId( ) == nIdAdminUser )
                    {
                        nCurrentItemId = nIndex;
                    }

                    nIndex++;
                }
            }

            _itemNavigator = new ItemNavigator( listIdsRight, nCurrentItemId, strUrl, PARAMETER_USER_ID );
        }
        else
        {
            _itemNavigator.setCurrentItemId( Integer.toString( nIdAdminUser ) );
            _itemNavigator.setBaseUrl( strUrl );
        }
    }

    /**
     * Reinit the item navigator
     */
    private void reinitItemNavigator( )
    {
        _itemNavigator = null;
    }

    /**
     * Check if a user is authorized to modify another user
     * 
     * @param currentUser  The current user
     * @param userToModify The user to modify
     * @return True if the current user can modify the other user, false otherwise
     */
    private boolean isUserAuthorizedToModifyUser( AdminUser currentUser, AdminUser userToModify )
    {
        if ( currentUser == null || userToModify == null )
        {
            return false;
        }
        return currentUser.isAdmin( )
                || ( currentUser.isParent( userToModify ) && ( ( haveCommonWorkgroups( currentUser, userToModify ) )
                        || ( !AdminWorkgroupHome.checkUserHasWorkgroup( userToModify.getUserId( ) ) ) ) );
    }
}
