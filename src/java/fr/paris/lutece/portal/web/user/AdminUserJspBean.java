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
package fr.paris.lutece.portal.web.user;

import fr.paris.lutece.portal.business.features.Level;
import fr.paris.lutece.portal.business.features.LevelHome;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.AdminRoleHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
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

    // Properties
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
    private static final String PROPERTY_MESSAGE_EMAIL_FORMAT = "portal.users.message.user.emailFormat";

    // Parameters
    private static final String PARAMETER_ACCESS_CODE = "access_code";
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_STATUS = "status";
    private static final String PARAMETER_USER_ID = "id_user";
    private static final String PARAMETER_ROLE = "roles";
    private static final String PARAMETER_RIGHT = "right";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_LANGUAGE = "language";
    private static final String PARAMETER_DELEGATE_RIGHTS = "delegate_rights";
    private static final String PARAMETER_USER_LEVEL = "user_level";
    private static final String PARAMETER_WORKGROUP = "workgroup";
    private static final String PARAMETER_SELECT = "select";
    private static final String PARAMETER_SELECT_ALL = "all";

    // Jsp url
    private static final String JSP_MANAGE_USER_RIGHTS = "ManageUserRights.jsp";
    private static final String JSP_MANAGE_USER_ROLES = "ManageUserRoles.jsp";
    private static final String JSP_MANAGE_USER = "ManageUsers.jsp";
    private static final String JSP_MANAGE_USER_WORKGROUPS = "ManageUserWorkgroups.jsp";
    private static final String JSP_URL_REMOVE_USER = "jsp/admin/user/DoRemoveUser.jsp";
    private static final String JSP_URL_CREATE_USER = "jsp/admin/user/CreateUser.jsp";
    private static final String JSP_URL_IMPORT_USER = "jsp/admin/user/ImportUser.jsp";

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

        // creation in no-module mode : no import
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            strCreateUrl = JSP_URL_CREATE_USER;
        }
        else
        {
            strCreateUrl = JSP_URL_IMPORT_USER;
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        AdminUser currentUser = getUser(  );

        List<AdminUser> listUser = (List<AdminUser>) AdminUserHome.findUserList(  );
        List<AdminUser> availableUsers = new ArrayList<AdminUser>(  );

        for ( AdminUser user : listUser )
        {
            boolean b = ( ( haveCommonWorkgroups( currentUser, user ) ) ||
                ( !AdminWorkgroupHome.checkUserHasWorkgroup( user.getUserId(  ) ) ) );

            if ( currentUser.isAdmin(  ) ||
                    ( currentUser.isParent( user ) &&
                    ( ( haveCommonWorkgroups( currentUser, user ) ) ||
                    ( !AdminWorkgroupHome.checkUserHasWorkgroup( user.getUserId(  ) ) ) ) ) )
            {
                availableUsers.add( user );
            }
        }

        Paginator paginator = new Paginator( availableUsers, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        HashMap model = new HashMap(  );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_USER_LIST, paginator.getPageItems(  ) );
        model.put( MARK_USER_CREATION_URL, strCreateUrl );

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

        HashMap model = new HashMap(  );
        Collection allImportUsers = null;

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
        if ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) )
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

        // creation in no-module mode : load empty form
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            HashMap model = new HashMap(  );

            model.put( MARK_USER_LEVELS_LIST, filteredLevels );
            model.put( MARK_CURRENT_USER, currentUser );
            model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( getLocale(  ) ) );
            model.put( MARK_CURRENT_LANGUAGE, getLocale(  ).getLanguage(  ) );
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

            HashMap model = new HashMap(  );

            if ( user != null )
            {
                model.put( MARK_USER_LEVELS_LIST, filteredLevels );
                model.put( MARK_CURRENT_USER, currentUser );
                model.put( MARK_IMPORT_USER, user );
                model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale(  ) ) );
                model.put( MARK_CURRENT_LANGUAGE, user.getLocale(  ).getLanguage(  ) );
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

        if ( !( ( strEmail == null ) || ( strEmail.trim(  ).equals( "" ) ) ) )
        {
            if ( !StringUtil.checkEmail( strEmail ) )
            {
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_EMAIL_FORMAT, AdminMessage.TYPE_STOP );
            }
        }

        // check again that access code is not in use
        if ( AdminUserHome.checkAccessCodeAlreadyInUse( strAccessCode ) )
        {
            return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_ACCESS_CODE_ALREADY_USED,
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
            String strPassword = request.getParameter( PARAMETER_PASSWORD );

            if ( ( strPassword == null ) || ( strPassword.equals( "" ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            user.setPassword( strPassword );

            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );

            user.setUserLevel( nNewUserLevel );
            AdminUserHome.create( user );
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
            AdminUserHome.create( user );
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

        HashMap model = new HashMap(  );
        HtmlTemplate template;

        AdminUser user = null;

        // creation in no-module mode : load form with password modification field and login modification field
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            user = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( nUserId );

            Level level = LevelHome.findByPrimaryKey( user.getUserLevel(  ) );
            model.put( MARK_USER, user );
            model.put( MARK_LEVEL, level );
            model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale(  ) ) );
            model.put( MARK_CURRENT_LANGUAGE, user.getLocale(  ).getLanguage(  ) );
            template = AppTemplateService.getTemplate( TEMPLATE_DEFAULT_MODIFY_USER, getLocale(  ), model );
        }
        else
        {
            user = AdminUserHome.findByPrimaryKey( nUserId );

            Level level = LevelHome.findByPrimaryKey( user.getUserLevel(  ) );
            model.put( MARK_USER, user );
            model.put( MARK_LEVEL, level );
            model.put( MARK_LANGUAGES_LIST, I18nService.getAdminLocales( user.getLocale(  ) ) );
            model.put( MARK_CURRENT_LANGUAGE, user.getLocale(  ).getLanguage(  ) );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_USER, getLocale(  ), model );
        }

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

        if ( !( ( strEmail == null ) || ( strEmail.trim(  ).equals( "" ) ) ) )
        {
            if ( !StringUtil.checkEmail( strEmail ) )
            {
                return AdminMessageService.getMessageUrl( request, PROPERTY_MESSAGE_EMAIL_FORMAT, AdminMessage.TYPE_STOP );
            }
        }

        int nUserId = Integer.parseInt( strUserId );

        // modification in no-module mode : we manage the password
        if ( AdminAuthenticationService.getInstance(  ).isDefaultModuleUsed(  ) )
        {
            LuteceDefaultAdminUser user = new LuteceDefaultAdminUser(  );

            String strPassword = request.getParameter( PARAMETER_PASSWORD );

            if ( ( strPassword == null ) || ( strPassword.equals( "" ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            user.setPassword( strPassword );

            user.setUserId( nUserId );
            user.setAccessCode( strAccessCode );
            user.setLastName( strLastName );
            user.setFirstName( strFirstName );
            user.setEmail( strEmail );
            user.setStatus( Integer.parseInt( strStatus ) );
            user.setLocale( new Locale( request.getParameter( PARAMETER_LANGUAGE ) ) );
            AdminUserHome.update( user );
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

            AdminUserHome.update( user );
        }

        return JSP_MANAGE_USER;
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
        AdminUserHome.remove( nUserId );
        AdminUserHome.removeAllRightsForUser( nUserId );
        AdminUserHome.removeAllRolesForUser( nUserId );

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

        HashMap model = new HashMap(  );
        model.put( MARK_CAN_MODIFY, getUser(  ).isParent( selectedUser ) || getUser(  ).isAdmin(  ) );
        model.put( MARK_CAN_DELEGATE, getUser(  ).getUserId(  ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_RIGHT_LIST, I18nService.localizeCollection( rightList, getLocale(  ) ) );

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

        //  ReferenceList assignableWorkgroupsList = AdminWorkgroupHome.getUserWorkgroups( selectedUser );
        HashMap model = new HashMap(  );
        model.put( MARK_CAN_MODIFY, getUser(  ).isParent( selectedUser ) || getUser(  ).isAdmin(  ) );
        model.put( MARK_CAN_DELEGATE, getUser(  ).getUserId(  ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_WORKGROUP_LIST, workgroupsList );

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

        HashMap model = new HashMap(  );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_ALL_WORKSGROUP_LIST, assignableWorkspaces );
        model.put( MARK_CAN_DELEGATE, String.valueOf( bDelegateWorkgroups ) );

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

        HashMap model = new HashMap(  );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_RIGHT_LIST, I18nService.localizeCollection( rightList, getLocale(  ) ) );
        model.put( MARK_ALL_RIGHT_LIST, I18nService.localizeCollection( allRightList, getLocale(  ) ) );
        model.put( MARK_CAN_DELEGATE, String.valueOf( bDelegateRights ) );
        model.put( MARK_SELECT_ALL, bSelectAll );

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

        HashMap model = new HashMap(  );
        model.put( MARK_CAN_MODIFY, getUser(  ).isParent( selectedUser ) || getUser(  ).isAdmin(  ) );
        model.put( MARK_CAN_DELEGATE, getUser(  ).getUserId(  ) != nUserId );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_ROLE_LIST, roleList );

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

        HashMap model = new HashMap(  );
        model.put( MARK_USER, AdminUserHome.findByPrimaryKey( nUserId ) );
        model.put( MARK_USER_ROLE_LIST, roleList );
        model.put( MARK_ALL_ROLE_LIST, assignableRoleList );

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
     * @param user1
     * @param user2
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
}
