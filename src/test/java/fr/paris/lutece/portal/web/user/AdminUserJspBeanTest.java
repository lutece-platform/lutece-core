/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.regularexpression.RegularExpressionService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPasswordFactory;

public class AdminUserJspBeanTest extends LuteceTestCase
{
    public void testDoCreateAdminUser( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
        bean.doCreateAdminUser( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        String randomUserName = "User_" + new SecureRandom( ).nextLong( );
        try
        {
            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", "   " );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", "admin" );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.user.accessCodeAlreadyUsed", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", "admin@lutece.fr" );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.user.accessEmailUsed", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel1AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.USER_ACCESS_DENIED, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.addParameter( "first_password", randomUserName );
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.differentsPassword", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.addParameter( "first_password", randomUserName );
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.differentsPassword", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.addParameter( "first_password", randomUserName );
            request.addParameter( "second_password", randomUserName );
            request.addParameter( "status", Integer.toString( AdminUser.ACTIVE_CODE ) ); // NPE if absent
            request.addParameter( "language", "fr" ); // NPE if absent
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNull( message );
            AdminUser createdUser = AdminUserHome.findUserByLogin( randomUserName );
            assertNotNull( createdUser );
            LuteceDefaultAdminUser createdUserWithPassword = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( createdUser.getUserId( ) );
            assertNotNull( createdUserWithPassword );
            assertTrue( createdUserWithPassword.getPassword( ).check( randomUserName ) );
        }
        finally
        {
            AdminUser user = AdminUserHome.findUserByLogin( randomUserName );
            if ( user != null )
            {
                AdminUserHome.remove( user.getUserId( ) );
            }
        }
    }

    public void testDoCreateAdminUserInvalidToken( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String randomUserName = "User_" + new SecureRandom( ).nextLong( );
        try
        {
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.addParameter( "first_password", randomUserName );
            request.addParameter( "second_password", randomUserName );
            request.addParameter( "status", Integer.toString( AdminUser.ACTIVE_CODE ) ); // NPE if absent
            request.addParameter( "language", "fr" ); // NPE if absent
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" )
                    + "b" );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            AdminUser createdUser = AdminUserHome.findUserByLogin( randomUserName );
            assertNull( createdUser );
        }
        finally
        {
            AdminUser user = AdminUserHome.findUserByLogin( randomUserName );
            if ( user != null )
            {
                AdminUserHome.remove( user.getUserId( ) );
            }
        }
    }

    public void testDoCreateAdminUserNoToken( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String randomUserName = "User_" + new SecureRandom( ).nextLong( );
        try
        {
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            request.addParameter( "user_level", "0" );
            request.addParameter( "first_password", randomUserName );
            request.addParameter( "second_password", randomUserName );
            request.addParameter( "status", Integer.toString( AdminUser.ACTIVE_CODE ) ); // NPE if absent
            request.addParameter( "language", "fr" ); // NPE if absent
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doCreateAdminUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            AdminUser createdUser = AdminUserHome.findUserByLogin( randomUserName );
            assertNull( createdUser );
        }
        finally
        {
            AdminUser user = AdminUserHome.findUserByLogin( randomUserName );
            if ( user != null )
            {
                AdminUserHome.remove( user.getUserId( ) );
            }
        }
    }

    private AdminUser getLevel1AdminUserWithCORE_USERS_MANAGEMENTRight( )
    {
        AdminUser user = new AdminUser( );
        user.setUserLevel( 1 );
        Map<String, Right> rights = new HashMap<String, Right>( 1 );
        rights.put( "CORE_USERS_MANAGEMENT", new Right( ) );
        user.setRights( rights );
        return user;
    }

    private AdminUser getLevel0AdminUserWithCORE_USERS_MANAGEMENTRight( )
    {
        AdminUser user = new AdminUser( );
        user.setUserLevel( 0 );
        Map<String, Right> rights = new HashMap<String, Right>( 1 );
        rights.put( "CORE_USERS_MANAGEMENT", new Right( ) );
        user.setRights( rights );
        return user;
    }

    public void testDoModifyAdminUserLegacyPassword( )
    {

    }

    public void testDoModifyAdminUser( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser userToModify = getUserToModify( );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", getLevel1AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            try
            {
                bean.doModifyAdminUser( request );
                fail( "Should not be able to modify a user with a lower level" );
            }
            catch( AccessDeniedException e )
            {
            }

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            final String modifiedName = userToModify.getAccessCode( ) + "_mod";

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", "  " );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", "admin" );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", modifiedName + "@lutece.fr" );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.user.accessCodeAlreadyUsed", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", "admin@lutece.fr" );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.user.accessEmailUsed", Locale.FRENCH ), message.getText( Locale.FRENCH ) );
        }
        finally
        {
            disposeOfUser( userToModify );
        }
    }

    public void testDoModifyAdminUserSuccess( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser userToModify = getUserToModify( );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            final String modifiedName = userToModify.getAccessCode( ) + "_mod";
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", userToModify.getEmail( ) );
            request.addParameter( "status", Integer.toString( AdminUser.NOT_ACTIVE_CODE ) );
            request.addParameter( "language", Locale.KOREA.toString( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) );
            bean.doModifyAdminUser( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNull( message );
            AdminUser stored = AdminUserHome.findByPrimaryKey( userToModify.getUserId( ) );
            assertNotNull( stored );
            assertEquals( modifiedName, stored.getAccessCode( ) );
            assertEquals( modifiedName, stored.getFirstName( ) );
            assertEquals( modifiedName, stored.getLastName( ) );
            assertEquals( AdminUser.NOT_ACTIVE_CODE, stored.getStatus( ) );
            assertEquals( Locale.KOREA.toString( ).toLowerCase( ), stored.getLocale( ).toString( ).toLowerCase( ) );
        }
        finally
        {
            disposeOfUser( userToModify );
        }
    }

    public void testDoModifyAdminUserNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser userToModify = getUserToModify( );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            final String modifiedName = userToModify.getAccessCode( ) + "_mod";
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", userToModify.getEmail( ) );
            request.addParameter( "status", Integer.toString( AdminUser.NOT_ACTIVE_CODE ) );
            request.addParameter( "language", Locale.KOREA.toString( ) );
            bean.doModifyAdminUser( request );
            fail( "Should have thrown " );
        }
        catch( AccessDeniedException e )
        {
            AdminUser stored = AdminUserHome.findByPrimaryKey( userToModify.getUserId( ) );
            assertNotNull( stored );
            assertEquals( userToModify.getAccessCode( ), stored.getAccessCode( ) );
            assertEquals( userToModify.getFirstName( ), stored.getFirstName( ) );
            assertEquals( userToModify.getLastName( ), stored.getLastName( ) );
            assertEquals( AdminUser.ACTIVE_CODE, stored.getStatus( ) );
            assertEquals( LocaleService.getDefault( ), stored.getLocale( ) );
        }
        finally
        {
            disposeOfUser( userToModify );
        }
    }

    public void testDoModifyAdminUserInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser userToModify = getUserToModify( );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            final String modifiedName = userToModify.getAccessCode( ) + "_mod";
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", userToModify.getEmail( ) );
            request.addParameter( "status", Integer.toString( AdminUser.NOT_ACTIVE_CODE ) );
            request.addParameter( "language", Locale.KOREA.toString( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" )
                    + "b" );
            bean.doModifyAdminUser( request );
            fail( "Should have thrown " );
        }
        catch( AccessDeniedException e )
        {
            AdminUser stored = AdminUserHome.findByPrimaryKey( userToModify.getUserId( ) );
            assertNotNull( stored );
            assertEquals( userToModify.getAccessCode( ), stored.getAccessCode( ) );
            assertEquals( userToModify.getFirstName( ), stored.getFirstName( ) );
            assertEquals( userToModify.getLastName( ), stored.getLastName( ) );
            assertEquals( AdminUser.ACTIVE_CODE, stored.getStatus( ) );
            assertEquals( LocaleService.getDefault( ), stored.getLocale( ) );
        }
        finally
        {
            disposeOfUser( userToModify );
        }
    }

    private AdminUser getUserToModify( )
    {
        String randomName = "User_" + new SecureRandom( ).nextLong( );
        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( );
        user.setAccessCode( randomName );
        user.setFirstName( randomName );
        user.setLastName( randomName );
        user.setEmail( randomName + "@lutece.fr" );
        user.setUserLevel( 0 );
        user.setStatus( AdminUser.ACTIVE_CODE );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );
        user.setPassword( passwordFactory.getPasswordFromCleartext( "PASSWORD" ) );
        AdminUserHome.create( user );
        AdminUserHome.createRightForUser( user.getUserId( ), "CORE_USERS_MANAGEMENT" );
        AdminUserHome.createRoleForUser( user.getUserId( ), "all_site_manager" );
        return AdminUserHome.findByPrimaryKey( user.getUserId( ) );
    }

    private void disposeOfUser( AdminUser user )
    {
        AdminUserHome.removeAllRolesForUser( user.getUserId( ) );
        AdminUserHome.removeAllOwnRightsForUser( user );
        AdminUserHome.remove( user.getUserId( ) );
    }

    public void testDoUseAdvancedSecurityParametersDoNotChangePassword( ) throws AccessDeniedException
    {
        boolean bUseAdvancesSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserJspBean bean = new AdminUserJspBean( );
        try
        {
            LuteceDefaultAdminUser admin = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( 1 );
            assertTrue( admin.getPassword( ).check( "adminadmin" ) );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
            bean.doUseAdvancedSecurityParameters( request );
            admin = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( 1 );
            assertTrue( admin.getPassword( ).check( "adminadmin" ) );
        }
        finally
        {
            if ( bUseAdvancesSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testGetCreateAdminUserTEMPLATE_DEFAULT_CREATE_USER( ) throws PasswordResetException, AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", getLevel1AdminUserWithCORE_USERS_MANAGEMENTRight( ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        bean.getCreateAdminUser( request ); // should not throw
    }

    public void testGetModifyUserPasswordUserNotfound( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_user", Integer.toString( Integer.MIN_VALUE ) );
        try
        {
            bean.getModifyUserPassword( request );
            fail( "Should have thrown" );
        }
        catch( AppException e )
        {
            // OK
        }
    }

    public void testGetModifyUserPasswordNotDefaultModule( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "id_user", Integer.toString( 1 ) );
        ReflectionTestUtils.setField( AdminAuthenticationService.getInstance( ), "_bUseDefaultModule", Boolean.FALSE );
        try
        {
            bean.getModifyUserPassword( request );
            fail( "Should have thrown" );
        }
        catch( AppException e )
        {
            // OK
        }
        finally
        {
            ReflectionTestUtils.setField( AdminAuthenticationService.getInstance( ), "_bUseDefaultModule", Boolean.TRUE );
        }
    }

    public void testGetModifyUserPasswordNoRight( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", AdminUserHome.findUserByLogin( "lutece" ) );
        request.setParameter( "id_user", Integer.toString( 1 ) );
        try
        {
            bean.getModifyUserPassword( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // OK
        }
    }

    public void testGetModifyUserPassword( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( 1 ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            String html = bean.getModifyUserPassword( request );
            assertNotNull( html );
            assertTrue( html.contains( "<small>" + I18nService.getLocalizedString( "portal.users.modify_user_password.pageTitle", Locale.FRANCE ) + "</small>" ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordNoRight( )
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", AdminUserHome.findUserByLogin( "lutece" ) );
        request.setParameter( "id_user", Integer.toString( 1 ) );
        try
        {
            bean.doModifyAdminUserPassword( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // OK
        }
    }

    public void testDoModifyAdminUserPassword( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            String password = "CHANGEDCHANGED";
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "first_password", password );
            request.setParameter( "second_password", password );
            request.setParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "portal.users.modify_user_password.pageTitle" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            String url = bean.doModifyAdminUserPassword( request );
            assertEquals( "ManageUsers.jsp", url );
            assertTrue( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordNotFound( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        String password = "CHANGEDCHANGED";
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( Integer.MIN_VALUE ) );
            request.setParameter( "first_password", password );
            request.setParameter( "second_password", password );
            request.setParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "portal.users.modify_user_password.pageTitle" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            fail( "should have thrown" );
        }
        catch( AppException e )
        {
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordNoFirst( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            String password = "CHANGEDCHANGED";
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "second_password", password );
            request.setParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "portal.users.modify_user_password.pageTitle" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordNoSecond( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.setParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "portal.users.modify_user_password.pageTitle" ) );
            String password = "CHANGEDCHANGED";
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "first_password", password );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordDifferentSecond( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            String password = "CHANGEDCHANGED";
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "first_password", password );
            request.setParameter( "second_password", password + "-" );
            request.setParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "portal.users.modify_user_password.pageTitle" ) );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordWeak( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.setParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "portal.users.modify_user_password.pageTitle" ) );
            String password = "W";
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "first_password", password );
            request.setParameter( "second_password", password );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        String password = "CHANGEDCHANGED";
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "first_password", password );
            request.setParameter( "second_password", password );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserPasswordInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        String password = "CHANGEDCHANGED";
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
            request.setParameter( "first_password", password );
            request.setParameter( "second_password", password );
            request.setParameter( "token", "invalid" );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doModifyAdminUserPassword( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserRights( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            for ( Right right : RightHome.getRightsList( ) )
            {
                if ( "CORE_USERS_MANAGEMENT".equals( right.getId( ) ) )
                {
                    assertTrue( AdminUserHome.hasRight( user, right.getId( ) ) );
                }
                else
                {
                    assertFalse( AdminUserHome.hasRight( user, right.getId( ) ) );
                }
            }
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            for ( Right right : RightHome.getRightsList( ) )
            {
                request.addParameter( "right", right.getId( ) );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRights.jsp" ) );
            bean.doModifyAdminUserRights( request );
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            for ( Right right : RightHome.getRightsList( ) )
            {
                assertTrue( AdminUserHome.hasRight( stored, right.getId( ) ) );
            }
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserRightsInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            for ( Right right : RightHome.getRightsList( ) )
            {
                if ( "CORE_USERS_MANAGEMENT".equals( right.getId( ) ) )
                {
                    assertTrue( AdminUserHome.hasRight( user, right.getId( ) ) );
                }
                else
                {
                    assertFalse( AdminUserHome.hasRight( user, right.getId( ) ) );
                }
            }
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            for ( Right right : RightHome.getRightsList( ) )
            {
                request.addParameter( "right", right.getId( ) );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRights.jsp" ) + "b" );
            bean.doModifyAdminUserRights( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            for ( Right right : RightHome.getRightsList( ) )
            {
                if ( "CORE_USERS_MANAGEMENT".equals( right.getId( ) ) )
                {
                    assertTrue( AdminUserHome.hasRight( stored, right.getId( ) ) );
                }
                else
                {
                    assertFalse( AdminUserHome.hasRight( stored, right.getId( ) ) );
                }
            }
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserRightsNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            for ( Right right : RightHome.getRightsList( ) )
            {
                if ( "CORE_USERS_MANAGEMENT".equals( right.getId( ) ) )
                {
                    assertTrue( AdminUserHome.hasRight( user, right.getId( ) ) );
                }
                else
                {
                    assertFalse( AdminUserHome.hasRight( user, right.getId( ) ) );
                }
            }
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            for ( Right right : RightHome.getRightsList( ) )
            {
                request.addParameter( "right", right.getId( ) );
            }
            bean.doModifyAdminUserRights( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            for ( Right right : RightHome.getRightsList( ) )
            {
                if ( "CORE_USERS_MANAGEMENT".equals( right.getId( ) ) )
                {
                    assertTrue( AdminUserHome.hasRight( stored, right.getId( ) ) );
                }
                else
                {
                    assertFalse( AdminUserHome.hasRight( stored, right.getId( ) ) );
                }
            }
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserRoles( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            for ( Role role : RoleHome.findAll( ) )
            {
                assertFalse( AdminUserHome.hasRole( user, role.getRole( ) ) );
            }
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            assertFalse( RoleHome.findAll( ).isEmpty( ) );
            for ( Role role : RoleHome.findAll( ) )
            {
                request.addParameter( "roles", role.getRole( ) );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRoles.jsp" ) );
            bean.doModifyAdminUserRoles( request );
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            for ( Role role : RoleHome.findAll( ) )
            {
                assertTrue( AdminUserHome.hasRole( stored, role.getRole( ) ) );
            }
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserRolesInvladidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            for ( Role role : RoleHome.findAll( ) )
            {
                assertFalse( AdminUserHome.hasRole( user, role.getRole( ) ) );
            }
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            assertFalse( RoleHome.findAll( ).isEmpty( ) );
            for ( Role role : RoleHome.findAll( ) )
            {
                request.addParameter( "roles", role.getRole( ) );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRoles.jsp" ) + "b" );
            bean.doModifyAdminUserRoles( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            for ( Role role : RoleHome.findAll( ) )
            {
                assertFalse( AdminUserHome.hasRole( stored, role.getRole( ) ) );
            }
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserRolesNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            for ( Role role : RoleHome.findAll( ) )
            {
                assertFalse( AdminUserHome.hasRole( user, role.getRole( ) ) );
            }
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            assertFalse( RoleHome.findAll( ).isEmpty( ) );
            for ( Role role : RoleHome.findAll( ) )
            {
                request.addParameter( "roles", role.getRole( ) );
            }
            bean.doModifyAdminUserRoles( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            for ( Role role : RoleHome.findAll( ) )
            {
                assertFalse( AdminUserHome.hasRole( stored, role.getRole( ) ) );
            }
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyAdminUserWorkgroups( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminWorkgroup workgroup = null;
        try
        {
            workgroup = new AdminWorkgroup( );
            workgroup.setKey( user.getAccessCode( ) );
            workgroup.setDescription( user.getAccessCode( ) );
            AdminWorkgroupHome.create( workgroup );
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.setParameter( "workgroup", workgroup.getKey( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserWorkgroups.jsp" ) );
            bean.doModifyAdminUserWorkgroups( request );
            assertNull( AdminMessageService.getMessage( request ) );
            assertTrue( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) );
        }
        finally
        {
            if ( workgroup != null )
            {
                AdminWorkgroupHome.removeAllUsersForWorkgroup( workgroup.getKey( ) );
                AdminWorkgroupHome.remove( workgroup.getKey( ) );
            }
            disposeOfUser( user );
        }

    }

    public void testDoModifyAdminUserWorkgroupsInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminWorkgroup workgroup = null;
        try
        {
            workgroup = new AdminWorkgroup( );
            workgroup.setKey( user.getAccessCode( ) );
            workgroup.setDescription( user.getAccessCode( ) );
            AdminWorkgroupHome.create( workgroup );
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.setParameter( "workgroup", workgroup.getKey( ) );
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserWorkgroups.jsp" ) + "b" );
            bean.doModifyAdminUserWorkgroups( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) );
        }
        finally
        {
            if ( workgroup != null )
            {
                AdminWorkgroupHome.removeAllUsersForWorkgroup( workgroup.getKey( ) );
                AdminWorkgroupHome.remove( workgroup.getKey( ) );
            }
            disposeOfUser( user );
        }

    }

    public void testDoModifyAdminUserWorkgroupsNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminWorkgroup workgroup = null;
        try
        {
            workgroup = new AdminWorkgroup( );
            workgroup.setKey( user.getAccessCode( ) );
            workgroup.setDescription( user.getAccessCode( ) );
            AdminWorkgroupHome.create( workgroup );
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            request.setParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.setParameter( "workgroup", workgroup.getKey( ) );
            bean.doModifyAdminUserWorkgroups( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            assertFalse( AdminWorkgroupHome.isUserInWorkgroup( user, workgroup.getKey( ) ) );
        }
        finally
        {
            if ( workgroup != null )
            {
                AdminWorkgroupHome.removeAllUsersForWorkgroup( workgroup.getKey( ) );
                AdminWorkgroupHome.remove( workgroup.getKey( ) );
            }
            disposeOfUser( user );
        }

    }

    public void testGetAnonymizeAdminUser( )
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            bean.getAnonymizeAdminUser( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoAnonymizeAdminUser( ) throws AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoAnonymizeAdminUser.jsp" ) );
            bean.doAnonymizeAdminUser( request );
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertFalse( user.getAccessCode( ).equals( stored.getAccessCode( ) ) );
            assertFalse( user.getFirstName( ).equals( stored.getFirstName( ) ) );
            assertFalse( user.getLastName( ).equals( stored.getLastName( ) ) );
            assertFalse( user.getEmail( ).equals( stored.getEmail( ) ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoAnonymizeAdminUserInvalidToken( ) throws AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoAnonymizeAdminUser.jsp" ) + "b" );
            bean.doAnonymizeAdminUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertEquals( user.getAccessCode( ), stored.getAccessCode( ) );
            assertEquals( user.getFirstName( ), stored.getFirstName( ) );
            assertEquals( user.getLastName( ), stored.getLastName( ) );
            assertEquals( user.getEmail( ), stored.getEmail( ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoAnonymizeAdminUserNoToken( ) throws AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            bean.doAnonymizeAdminUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertEquals( user.getAccessCode( ), stored.getAccessCode( ) );
            assertEquals( user.getFirstName( ), stored.getFirstName( ) );
            assertEquals( user.getLastName( ), stored.getLastName( ) );
            assertEquals( user.getEmail( ), stored.getEmail( ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoChangeFieldAnonymizeAdminUsers( ) throws AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, Boolean> origStatusMap = AdminUserHome.getAnonymizationStatusUserStaticField( );
        try
        {
            for ( Entry<String, Boolean> entry : origStatusMap.entrySet( ) )
            {
                assertTrue( entry.getValue( ) );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "admin/user/field_anonymize_admin_user.html" ) );
            bean.doChangeFieldAnonymizeAdminUsers( request );
            for ( Entry<String, Boolean> entry : AdminUserHome.getAnonymizationStatusUserStaticField( ).entrySet( ) )
            {
                assertFalse( entry.getValue( ) );
            }
        }
        finally
        {
            for ( Entry<String, Boolean> entry : origStatusMap.entrySet( ) )
            {
                AdminUserHome.updateAnonymizationStatusUserStaticField( entry.getKey( ), entry.getValue( ) );
            }
        }
    }

    public void testDoChangeFieldAnonymizeAdminUsersInvalidToken( ) throws AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, Boolean> origStatusMap = AdminUserHome.getAnonymizationStatusUserStaticField( );
        try
        {
            for ( Entry<String, Boolean> entry : origStatusMap.entrySet( ) )
            {
                assertTrue( entry.getValue( ) );
            }
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "admin/user/field_anonymize_admin_user.html" ) + "b" );
            bean.doChangeFieldAnonymizeAdminUsers( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            for ( Entry<String, Boolean> entry : AdminUserHome.getAnonymizationStatusUserStaticField( ).entrySet( ) )
            {
                assertTrue( entry.getValue( ) );
            }
        }
        finally
        {
            for ( Entry<String, Boolean> entry : origStatusMap.entrySet( ) )
            {
                AdminUserHome.updateAnonymizationStatusUserStaticField( entry.getKey( ), entry.getValue( ) );
            }
        }
    }

    public void testDoChangeFieldAnonymizeAdminUsersNoToken( ) throws AccessDeniedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        Map<String, Boolean> origStatusMap = AdminUserHome.getAnonymizationStatusUserStaticField( );
        try
        {
            for ( Entry<String, Boolean> entry : origStatusMap.entrySet( ) )
            {
                assertTrue( entry.getValue( ) );
            }
            bean.doChangeFieldAnonymizeAdminUsers( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            for ( Entry<String, Boolean> entry : AdminUserHome.getAnonymizationStatusUserStaticField( ).entrySet( ) )
            {
                assertTrue( entry.getValue( ) );
            }
        }
        finally
        {
            for ( Entry<String, Boolean> entry : origStatusMap.entrySet( ) )
            {
                AdminUserHome.updateAnonymizationStatusUserStaticField( entry.getKey( ), entry.getValue( ) );
            }
        }
    }

    public void testDoConfirmRemoveAdminUser( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doConfirmRemoveAdminUser( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoRemoveAdminUser( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoRemoveUser.jsp" ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doRemoveAdminUser( request );
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertNull( stored );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoRemoveAdminUserInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoRemoveUser.jsp" ) + "b" );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doRemoveAdminUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertNotNull( stored );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoRemoveAdminUserNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            request.addParameter( "id_user", Integer.toString( user.getUserId( ) ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doRemoveAdminUser( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertNotNull( stored );
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoImportUsersFromFile( ) throws AccessDeniedException, UserNotSignedException, IOException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ImportUser.jsp" ) );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem file = new DiskFileItem( "import_file", "application/csv", true, "junit.csv", 1024, new File( System.getProperty( "java.io.tmpdir" ) ) );
        OutputStreamWriter writer = new OutputStreamWriter( file.getOutputStream( ), Charset.forName( "UTF-8" ) );
        writer.write( "test;test;test;test@test.fr;" + AdminUser.ACTIVE_CODE + ";" + Locale.FRANCE + ";0;false;false;;;" );
        writer.close( );
        fileItems.add( file );
        multipartFiles.put( "import_file", fileItems );
        Map<String, String [ ]> parameters = request.getParameterMap( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        bean.getImportUsersFromFile( request ); // initialize _importAdminUserService
        AdminUser importedUser = null;
        try
        {
            bean.doImportUsersFromFile( multipartRequest );
            importedUser = AdminUserHome.findUserByLogin( "test" );
            assertNotNull( importedUser );
            assertEquals( "test", importedUser.getAccessCode( ) );
            assertEquals( "test", importedUser.getFirstName( ) );
            assertEquals( "test", importedUser.getLastName( ) );
            assertEquals( "test@test.fr", importedUser.getEmail( ) );
            assertEquals( AdminUser.ACTIVE_CODE, importedUser.getStatus( ) );
        }
        finally
        {
            if ( importedUser != null )
            {
                disposeOfUser( importedUser );
            }
            disposeOfUser( user );
        }
    }

    public void testDoImportUsersFromFileInvalidToken( ) throws AccessDeniedException, UserNotSignedException, IOException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ImportUser.jsp" )
                + "b" );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem file = new DiskFileItem( "import_file", "application/csv", true, "junit.csv", 1024, new File( System.getProperty( "java.io.tmpdir" ) ) );
        OutputStreamWriter writer = new OutputStreamWriter( file.getOutputStream( ), Charset.forName( "UTF-8" ) );
        writer.write( "test;test;test;test@test.fr;" + AdminUser.ACTIVE_CODE + ";" + Locale.FRANCE + ";0;false;false;;;" );
        writer.close( );
        fileItems.add( file );
        multipartFiles.put( "import_file", fileItems );
        Map<String, String [ ]> parameters = request.getParameterMap( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        bean.getImportUsersFromFile( request ); // initialize _importAdminUserService
        AdminUser importedUser = null;
        try
        {
            bean.doImportUsersFromFile( multipartRequest );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            importedUser = AdminUserHome.findUserByLogin( "test" );
            assertNull( importedUser );
        }
        finally
        {
            if ( importedUser != null )
            {
                disposeOfUser( importedUser );
            }
            disposeOfUser( user );
        }
    }

    public void testDoImportUsersFromFileNoToken( ) throws AccessDeniedException, UserNotSignedException, IOException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem file = new DiskFileItem( "import_file", "application/csv", true, "junit.csv", 1024, new File( System.getProperty( "java.io.tmpdir" ) ) );
        OutputStreamWriter writer = new OutputStreamWriter( file.getOutputStream( ), Charset.forName( "UTF-8" ) );
        writer.write( "test;test;test;test@test.fr;" + AdminUser.ACTIVE_CODE + ";" + Locale.FRANCE + ";0;false;false;;;" );
        writer.close( );
        fileItems.add( file );
        multipartFiles.put( "import_file", fileItems );
        Map<String, String [ ]> parameters = request.getParameterMap( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        bean.getImportUsersFromFile( request ); // initialize _importAdminUserService
        AdminUser importedUser = null;
        try
        {
            bean.doImportUsersFromFile( multipartRequest );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            importedUser = AdminUserHome.findUserByLogin( "test" );
            assertNull( importedUser );
        }
        finally
        {
            if ( importedUser != null )
            {
                disposeOfUser( importedUser );
            }
            disposeOfUser( user );
        }
    }

    public void testDoInsertRegularExpression( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        request.setParameter( "id_expression", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        try
        {
            bean.doInsertRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
        }
        finally
        {
            AdminUserService.doRemoveRegularExpression( 1 );
            disposeOfUser( user );
        }
    }

    public void testDoInsertRegularExpressionInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        request.setParameter( "id_expression", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        try
        {
            bean.doInsertRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // OK
        }
        finally
        {
            AdminUserService.doRemoveRegularExpression( 1 );
            disposeOfUser( user );
        }
    }

    public void testDoInsertRegularExpressionNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        request.setParameter( "id_expression", "1" );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        try
        {
            bean.doInsertRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // OK
        }
        finally
        {
            AdminUserService.doRemoveRegularExpression( 1 );
            disposeOfUser( user );
        }
    }

    public void testDoModifyAccountLifeTimeEmails( ) throws AccessDeniedException
    {
        String origEmailSender = AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_sender" );
        String origEmailSubject = AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_subject" );
        String origEmailBody = DatabaseTemplateService.getTemplateFromKey( "core_first_alert_mail" );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "email_type", "first" );
        request.setParameter( "email_sender", "junit" );
        request.setParameter( "email_subject", "junit" );
        request.setParameter( "email_body", "junit" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
        try
        {
            bean.doModifyAccountLifeTimeEmails( request );
            assertEquals( "junit", AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_sender" ) );
            assertEquals( "junit", AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_subject" ) );
            assertEquals( "junit", DatabaseTemplateService.getTemplateFromKey( "core_first_alert_mail" ) );
        }
        finally
        {
            AdminUserService.updateSecurityParameter( "core.advanced_parameters.first_alert_mail_sender", origEmailSender );
            AdminUserService.updateSecurityParameter( "core.advanced_parameters.first_alert_mail_subject", origEmailSubject );
            DatabaseTemplateService.updateTemplate( "core_first_alert_mail", origEmailBody );
        }
    }

    public void testDoModifyAccountLifeTimeEmailsInvalidToken( )
    {
        String origEmailSender = AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_sender" );
        String origEmailSubject = AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_subject" );
        String origEmailBody = DatabaseTemplateService.getTemplateFromKey( "core_first_alert_mail" );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "email_type", "first" );
        request.setParameter( "email_sender", "junit" );
        request.setParameter( "email_subject", "junit" );
        request.setParameter( "email_body", "junit" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        try
        {
            bean.doModifyAccountLifeTimeEmails( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origEmailSender, AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_sender" ) );
            assertEquals( origEmailSubject, AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_subject" ) );
            assertEquals( origEmailBody, DatabaseTemplateService.getTemplateFromKey( "core_first_alert_mail" ) );
        }
        finally
        {
            AdminUserService.updateSecurityParameter( "core.advanced_parameters.first_alert_mail_sender", origEmailSender );
            AdminUserService.updateSecurityParameter( "core.advanced_parameters.first_alert_mail_subject", origEmailSubject );
            DatabaseTemplateService.updateTemplate( "core_first_alert_mail", origEmailBody );
        }
    }

    public void testDoModifyAccountLifeTimeEmailsNoToken( )
    {
        String origEmailSender = AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_sender" );
        String origEmailSubject = AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_subject" );
        String origEmailBody = DatabaseTemplateService.getTemplateFromKey( "core_first_alert_mail" );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "email_type", "first" );
        request.setParameter( "email_sender", "junit" );
        request.setParameter( "email_subject", "junit" );
        request.setParameter( "email_body", "junit" );
        try
        {
            bean.doModifyAccountLifeTimeEmails( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origEmailSender, AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_sender" ) );
            assertEquals( origEmailSubject, AdminUserService.getSecurityParameter( "core.advanced_parameters.first_alert_mail_subject" ) );
            assertEquals( origEmailBody, DatabaseTemplateService.getTemplateFromKey( "core_first_alert_mail" ) );
        }
        finally
        {
            AdminUserService.updateSecurityParameter( "core.advanced_parameters.first_alert_mail_sender", origEmailSender );
            AdminUserService.updateSecurityParameter( "core.advanced_parameters.first_alert_mail_subject", origEmailSubject );
            DatabaseTemplateService.updateTemplate( "core_first_alert_mail", origEmailBody );
        }
    }

    public void testDoModifyDefaultUserParameterValues( ) throws AccessDeniedException, UserNotSignedException
    {
        String origDefaultStatus = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS );
        String origDefaultLevel = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL );
        String origDefaultNotification = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION );
        String origDefaultLanguage = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.setParameter( "status", Integer.toString( AdminUser.ANONYMIZED_CODE ) );
        request.setParameter( "user_level", "10" );
        request.setParameter( "notify_user", "false" );
        request.setParameter( "language", Locale.CANADA_FRENCH.toString( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
        try
        {
            bean.doModifyDefaultUserParameterValues( request );
            assertEquals( Integer.toString( AdminUser.ANONYMIZED_CODE ), DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS ) );
            assertEquals( "10", DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL ) );
            assertEquals( "false", DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION ) );
            assertEquals( Locale.CANADA_FRENCH.toString( ), DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_STATUS, origDefaultStatus );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LEVEL, origDefaultLevel );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION, origDefaultNotification );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE, origDefaultLanguage );
            disposeOfUser( user );
        }
    }

    public void testDoModifyDefaultUserParameterValuesInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        String origDefaultStatus = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS );
        String origDefaultLevel = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL );
        String origDefaultNotification = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION );
        String origDefaultLanguage = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.setParameter( "status", Integer.toString( AdminUser.ANONYMIZED_CODE ) );
        request.setParameter( "user_level", "10" );
        request.setParameter( "notify_user", "false" );
        request.setParameter( "language", Locale.CANADA_FRENCH.toString( ) );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        try
        {
            bean.doModifyDefaultUserParameterValues( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origDefaultStatus, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS ) );
            assertEquals( origDefaultLevel, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL ) );
            assertEquals( origDefaultNotification, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION ) );
            assertEquals( origDefaultLanguage, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_STATUS, origDefaultStatus );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LEVEL, origDefaultLevel );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION, origDefaultNotification );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE, origDefaultLanguage );
            disposeOfUser( user );
        }
    }

    public void testDoModifyDefaultUserParameterValuesNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        String origDefaultStatus = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS );
        String origDefaultLevel = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL );
        String origDefaultNotification = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION );
        String origDefaultLanguage = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.setParameter( "status", Integer.toString( AdminUser.ANONYMIZED_CODE ) );
        request.setParameter( "user_level", "10" );
        request.setParameter( "notify_user", "false" );
        request.setParameter( "language", Locale.CANADA_FRENCH.toString( ) );
        try
        {
            bean.doModifyDefaultUserParameterValues( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origDefaultStatus, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_STATUS ) );
            assertEquals( origDefaultLevel, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LEVEL ) );
            assertEquals( origDefaultNotification, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION ) );
            assertEquals( origDefaultLanguage, DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_STATUS, origDefaultStatus );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LEVEL, origDefaultLevel );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_NOTIFICATION, origDefaultNotification );
            DefaultUserParameterHome.update( AdminUserService.DSKEY_DEFAULT_USER_LANGUAGE, origDefaultLanguage );
            disposeOfUser( user );
        }
    }

    public void testDoModifyDefaultUserSecurityValues( ) throws AccessDeniedException, UserNotSignedException
    {
        assertFalse( "Test does not account for advanced security parameters",
                AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        boolean origForceChangePasswordReinit = Boolean
                .parseBoolean( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) );
        int origPasswordMinimumLength = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH );
        int origResetTokenValidity = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY );
        boolean origLockResetTokenToSession = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION );
        int origAccountLifeTime = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME );
        int origTimeBeforeAlertAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT );
        int origNbAlertAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT );
        int origTimeBetweenAlertsAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT );
        int origAccessFailuresMax = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX );
        int origAccessFailuresInterval = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL );
        String origBannedDomainNames = AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
        request.setParameter( "force_change_password_reinit", origForceChangePasswordReinit ? Boolean.FALSE.toString( ) : Boolean.TRUE.toString( ) );
        request.setParameter( "password_minimum_length", Integer.toString( origPasswordMinimumLength + 1 ) );
        request.setParameter( "reset_token_validity", Integer.toString( origResetTokenValidity + 1 ) );
        request.setParameter( "lock_reset_token_to_session", origLockResetTokenToSession ? Boolean.FALSE.toString( ) : Boolean.TRUE.toString( ) );
        request.setParameter( "account_life_time", Integer.toString( origAccountLifeTime + 1 ) );
        request.setParameter( "time_before_alert_account", Integer.toString( origTimeBeforeAlertAccount + 1 ) );
        request.setParameter( "nb_alert_account", Integer.toString( origNbAlertAccount + 1 ) );
        request.setParameter( "time_between_alerts_account", Integer.toString( origTimeBetweenAlertsAccount + 1 ) );
        request.setParameter( "access_failures_max", Integer.toString( origAccessFailuresMax + 1 ) );
        request.setParameter( "access_failures_interval", Integer.toString( origAccessFailuresInterval + 1 ) );
        request.setParameter( "banned_domain_names", origBannedDomainNames + "b" );
        try
        {
            bean.doModifyDefaultUserSecurityValues( request );
            assertEquals( !origForceChangePasswordReinit,
                    Boolean.parseBoolean( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) ) );
            assertEquals( origPasswordMinimumLength + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH ) );
            assertEquals( origResetTokenValidity + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY ) );
            assertEquals( !origLockResetTokenToSession, AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION ) );
            assertEquals( origAccountLifeTime + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME ) );
            assertEquals( origTimeBeforeAlertAccount + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT ) );
            assertEquals( origNbAlertAccount + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT ) );
            assertEquals( origTimeBetweenAlertsAccount + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT ) );
            assertEquals( origAccessFailuresMax + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX ) );
            assertEquals( origAccessFailuresInterval + 1, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL ) );
            assertEquals( origBannedDomainNames + "b", AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT, Boolean.toString( origForceChangePasswordReinit ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH, Integer.toString( origPasswordMinimumLength ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY, Integer.toString( origResetTokenValidity ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION, Boolean.toString( origLockResetTokenToSession ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, Integer.toString( origAccountLifeTime ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT, Integer.toString( origTimeBeforeAlertAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT, Integer.toString( origNbAlertAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT, Integer.toString( origTimeBetweenAlertsAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX, Integer.toString( origAccessFailuresMax ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL, Integer.toString( origAccessFailuresInterval ) );
            AdminUserService.updateLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES, origBannedDomainNames );
            disposeOfUser( user );
        }
    }

    public void testDoModifyDefaultUserSecurityValuesInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        assertFalse( "Test does not account for advanced security parameters",
                AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        boolean origForceChangePasswordReinit = Boolean
                .parseBoolean( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) );
        int origPasswordMinimumLength = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH );
        int origResetTokenValidity = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY );
        boolean origLockResetTokenToSession = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION );
        int origAccountLifeTime = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME );
        int origTimeBeforeAlertAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT );
        int origNbAlertAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT );
        int origTimeBetweenAlertsAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT );
        int origAccessFailuresMax = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX );
        int origAccessFailuresInterval = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL );
        String origBannedDomainNames = AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        request.setParameter( "force_change_password_reinit", origForceChangePasswordReinit ? Boolean.FALSE.toString( ) : Boolean.TRUE.toString( ) );
        request.setParameter( "password_minimum_length", Integer.toString( origPasswordMinimumLength + 1 ) );
        request.setParameter( "reset_token_validity", Integer.toString( origResetTokenValidity + 1 ) );
        request.setParameter( "lock_reset_token_to_session", origLockResetTokenToSession ? Boolean.FALSE.toString( ) : Boolean.TRUE.toString( ) );
        request.setParameter( "account_life_time", Integer.toString( origAccountLifeTime + 1 ) );
        request.setParameter( "time_before_alert_account", Integer.toString( origTimeBeforeAlertAccount + 1 ) );
        request.setParameter( "nb_alert_account", Integer.toString( origNbAlertAccount + 1 ) );
        request.setParameter( "time_between_alerts_account", Integer.toString( origTimeBetweenAlertsAccount + 1 ) );
        request.setParameter( "access_failures_max", Integer.toString( origAccessFailuresMax + 1 ) );
        request.setParameter( "access_failures_interval", Integer.toString( origAccessFailuresInterval + 1 ) );
        request.setParameter( "banned_domain_names", origBannedDomainNames + "b" );
        try
        {
            bean.doModifyDefaultUserSecurityValues( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origForceChangePasswordReinit,
                    Boolean.parseBoolean( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) ) );
            assertEquals( origPasswordMinimumLength, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH ) );
            assertEquals( origResetTokenValidity, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY ) );
            assertEquals( origLockResetTokenToSession, AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION ) );
            assertEquals( origAccountLifeTime, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME ) );
            assertEquals( origTimeBeforeAlertAccount, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT ) );
            assertEquals( origNbAlertAccount, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT ) );
            assertEquals( origTimeBetweenAlertsAccount, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT ) );
            assertEquals( origAccessFailuresMax, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX ) );
            assertEquals( origAccessFailuresInterval, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL ) );
            assertEquals( origBannedDomainNames, AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT, Boolean.toString( origForceChangePasswordReinit ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH, Integer.toString( origPasswordMinimumLength ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY, Integer.toString( origResetTokenValidity ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION, Boolean.toString( origLockResetTokenToSession ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, Integer.toString( origAccountLifeTime ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT, Integer.toString( origTimeBeforeAlertAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT, Integer.toString( origNbAlertAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT, Integer.toString( origTimeBetweenAlertsAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX, Integer.toString( origAccessFailuresMax ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL, Integer.toString( origAccessFailuresInterval ) );
            AdminUserService.updateLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES, origBannedDomainNames );
            disposeOfUser( user );
        }
    }

    public void testDoModifyDefaultUserSecurityValuesNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        assertFalse( "Test does not account for advanced security parameters",
                AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        boolean origForceChangePasswordReinit = Boolean
                .parseBoolean( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) );
        int origPasswordMinimumLength = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH );
        int origResetTokenValidity = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY );
        boolean origLockResetTokenToSession = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION );
        int origAccountLifeTime = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME );
        int origTimeBeforeAlertAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT );
        int origNbAlertAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT );
        int origTimeBetweenAlertsAccount = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT );
        int origAccessFailuresMax = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX );
        int origAccessFailuresInterval = AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL );
        String origBannedDomainNames = AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.setParameter( "force_change_password_reinit", origForceChangePasswordReinit ? Boolean.FALSE.toString( ) : Boolean.TRUE.toString( ) );
        request.setParameter( "password_minimum_length", Integer.toString( origPasswordMinimumLength + 1 ) );
        request.setParameter( "reset_token_validity", Integer.toString( origResetTokenValidity + 1 ) );
        request.setParameter( "lock_reset_token_to_session", origLockResetTokenToSession ? Boolean.FALSE.toString( ) : Boolean.TRUE.toString( ) );
        request.setParameter( "account_life_time", Integer.toString( origAccountLifeTime + 1 ) );
        request.setParameter( "time_before_alert_account", Integer.toString( origTimeBeforeAlertAccount + 1 ) );
        request.setParameter( "nb_alert_account", Integer.toString( origNbAlertAccount + 1 ) );
        request.setParameter( "time_between_alerts_account", Integer.toString( origTimeBetweenAlertsAccount + 1 ) );
        request.setParameter( "access_failures_max", Integer.toString( origAccessFailuresMax + 1 ) );
        request.setParameter( "access_failures_interval", Integer.toString( origAccessFailuresInterval + 1 ) );
        request.setParameter( "banned_domain_names", origBannedDomainNames + "b" );
        try
        {
            bean.doModifyDefaultUserSecurityValues( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origForceChangePasswordReinit,
                    Boolean.parseBoolean( DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT ) ) );
            assertEquals( origPasswordMinimumLength, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH ) );
            assertEquals( origResetTokenValidity, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY ) );
            assertEquals( origLockResetTokenToSession, AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION ) );
            assertEquals( origAccountLifeTime, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME ) );
            assertEquals( origTimeBeforeAlertAccount, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT ) );
            assertEquals( origNbAlertAccount, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT ) );
            assertEquals( origTimeBetweenAlertsAccount, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT ) );
            assertEquals( origAccessFailuresMax, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX ) );
            assertEquals( origAccessFailuresInterval, AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL ) );
            assertEquals( origBannedDomainNames, AdminUserService.getLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_FORCE_CHANGE_PASSWORD_REINIT, Boolean.toString( origForceChangePasswordReinit ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_PASSWORD_MINIMUM_LENGTH, Integer.toString( origPasswordMinimumLength ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY, Integer.toString( origResetTokenValidity ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION, Boolean.toString( origLockResetTokenToSession ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, Integer.toString( origAccountLifeTime ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT, Integer.toString( origTimeBeforeAlertAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_NB_ALERT_ACCOUNT, Integer.toString( origNbAlertAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_TIME_BETWEEN_ALERTS_ACCOUNT, Integer.toString( origTimeBetweenAlertsAccount ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_MAX, Integer.toString( origAccessFailuresMax ) );
            AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_ACCES_FAILURES_INTERVAL, Integer.toString( origAccessFailuresInterval ) );
            AdminUserService.updateLargeSecurityParameter( AdminUserService.DSKEY_BANNED_DOMAIN_NAMES, origBannedDomainNames );
            disposeOfUser( user );
        }
    }

    public void testDoModifyEmailPattern( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        String origEmailPattern = getEmailPattern( );
        assertTrue( "plugin-regularexpression is not there", isEmailPatternSetManually( ) );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
        request.setParameter( "is_email_pattern_set_manually", Boolean.FALSE.toString( ) );
        request.setParameter( "email_pattern", origEmailPattern + "b" );
        try
        {
            bean.doModifyEmailPattern( request );
            assertEquals( origEmailPattern + "b", getEmailPattern( ) );
            assertTrue( isEmailPatternSetManually( ) );
        }
        finally
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern, isEmailPatternSetManually( ) );
            disposeOfUser( user );
        }
    }

    /**
     * FIXME copied from AdminUserService Get the default email pattern defined in the <b>lutece.properties</b>.
     * 
     * @return the default email pattern
     */
    private static String getDefaultEmailPattern( )
    {
        return AppPropertiesService.getProperty( "lutece.email.pattern" );
    }

    /**
     * FIXME copied from AdminUserService Get the AdminUser email pattern that is stored in <b>'core_user_parameter.email_pattern'</b>. <br>
     * If it does not exist, then it will retrieve the value in the <b>lutece.properties</b> file (parameter <b>email.pattern</b>)
     * 
     * @return the AdminUser email pattern
     */
    private static String getEmailPattern( )
    {
        String strEmailPattern = getDefaultEmailPattern( );
        String emailPattern = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_EMAIL_PATTERN );

        if ( emailPattern != null )
        {
            strEmailPattern = emailPattern;
        }

        return strEmailPattern;
    }

    /**
     * FIXME copied from AdminUserService Check whether the email pattern is set manually or by a set of rules from the plugin-regularexpression.
     * 
     * @return true if it is set manually, false otherwise
     */
    private static boolean isEmailPatternSetManually( )
    {
        boolean bIsSetManually = true;

        if ( RegularExpressionService.getInstance( ).isAvailable( ) )
        {
            String emailPatternVerifyBy = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_EMAIL_PATTERN_VERIFY_BY );

            if ( StringUtils.isNotBlank( emailPatternVerifyBy ) )
            {
                bIsSetManually = false;
            }
        }

        return bIsSetManually;
    }

    public void testDoModifyEmailPatternInvalidToken( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        String origEmailPattern = getEmailPattern( );
        assertTrue( "plugin-regularexpression is not there", isEmailPatternSetManually( ) );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        request.setParameter( "is_email_pattern_set_manually", Boolean.FALSE.toString( ) );
        request.setParameter( "email_pattern", origEmailPattern + "b" );
        try
        {
            bean.doModifyEmailPattern( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origEmailPattern, getEmailPattern( ) );
            assertTrue( isEmailPatternSetManually( ) );
        }
        finally
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern, isEmailPatternSetManually( ) );
            disposeOfUser( user );
        }
    }

    public void testDoModifyEmailPatternNoToken( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        String origEmailPattern = getEmailPattern( );
        assertTrue( "plugin-regularexpression is not there", isEmailPatternSetManually( ) );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        request.setParameter( "is_email_pattern_set_manually", Boolean.FALSE.toString( ) );
        request.setParameter( "email_pattern", origEmailPattern + "b" );
        try
        {
            bean.doModifyEmailPattern( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origEmailPattern, getEmailPattern( ) );
            assertTrue( isEmailPatternSetManually( ) );
        }
        finally
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern, isEmailPatternSetManually( ) );
            disposeOfUser( user );
        }
    }

    public void testGetChangeUseAdvancedSecurityParametersToFalse( )
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.TRUE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            bean.getChangeUseAdvancedSecurityParameters( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testGetChangeUseAdvancedSecurityParametersToTrue( )
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.FALSE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            bean.getChangeUseAdvancedSecurityParameters( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertTrue( message.getRequestParameters( ).containsKey( SecurityTokenService.PARAMETER_TOKEN ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoRemoveAdvancedSecurityParameters( ) throws AccessDeniedException
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.TRUE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
            bean.doRemoveAdvancedSecurityParameters( request );
            assertFalse( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoRemoveAdvancedSecurityParametersInvalidToken( ) throws AccessDeniedException
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.TRUE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                    + "b" );
            bean.doRemoveAdvancedSecurityParameters( request );
            fail( "should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoRemoveAdvancedSecurityParametersNoToken( ) throws AccessDeniedException
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.TRUE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            bean.doRemoveAdvancedSecurityParameters( request );
            fail( "should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertTrue( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoUseAdvancedSecurityParameters( ) throws AccessDeniedException
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.FALSE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
            bean.doUseAdvancedSecurityParameters( request );
            assertTrue( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoUseAdvancedSecurityParametersInvalidToken( ) throws AccessDeniedException
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.FALSE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                    + "b" );
            bean.doUseAdvancedSecurityParameters( request );
            fail( "should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoUseAdvancedSecurityParametersNoToken( ) throws AccessDeniedException
    {
        boolean origUseAdvancedSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS, Boolean.FALSE.toString( ) );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            bean.doUseAdvancedSecurityParameters( request );
            fail( "should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertFalse( AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS ) );
        }
        finally
        {
            if ( origUseAdvancedSecurityParameters )
            {
                AdminUserService.useAdvancedSecurityParameters( );
            }
            else
            {
                AdminUserService.removeAdvancedSecurityParameters( );
            }
        }
    }

    public void testDoRemoveRegularExpression( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            request.setParameter( "id_expression", "1" );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
            bean.doRemoveRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoRemoveRegularExpressionInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            request.setParameter( "id_expression", "1" );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                    + "b" );
            bean.doRemoveRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // ok
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoRemoveRegularExpressionNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = getUserToModify( );
        AdminAuthenticationService.getInstance( ).registerUser( request, user );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.setParameter( "id_expression", "1" );
        try
        {
            bean.doRemoveRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // ok
        }
        finally
        {
            disposeOfUser( user );
        }
    }

    public void testDoModifyEmailPatternReset( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        String origEmailPattern = getEmailPattern( );
        assertTrue( "plugin-regularexpression is not there", isEmailPatternSetManually( ) );
        AdminUser user = getUserToModify( );
        try
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern + "b", isEmailPatternSetManually( ) );
            assertEquals( origEmailPattern + "b", getEmailPattern( ) );
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) );
            request.setParameter( "reset", "reset" );
            bean.doModifyEmailPattern( request );
            assertEquals( origEmailPattern, getEmailPattern( ) );
            assertTrue( isEmailPatternSetManually( ) );
        }
        finally
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern, isEmailPatternSetManually( ) );
            disposeOfUser( user );
        }
    }

    public void testDoModifyEmailPatternResetInvalidToken( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        String origEmailPattern = getEmailPattern( );
        assertTrue( "plugin-regularexpression is not there", isEmailPatternSetManually( ) );
        AdminUserService.doModifyEmailPattern( origEmailPattern + "b", isEmailPatternSetManually( ) );
        assertEquals( origEmailPattern + "b", getEmailPattern( ) );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" )
                + "b" );
        request.setParameter( "reset", "reset" );
        try
        {
            bean.doModifyEmailPattern( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origEmailPattern + "b", getEmailPattern( ) );
        }
        finally
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern, isEmailPatternSetManually( ) );
        }
    }

    public void testDoModifyEmailPatternResetNoToken( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        String origEmailPattern = getEmailPattern( );
        assertTrue( "plugin-regularexpression is not there", isEmailPatternSetManually( ) );
        AdminUserService.doModifyEmailPattern( origEmailPattern + "b", isEmailPatternSetManually( ) );
        assertEquals( origEmailPattern + "b", getEmailPattern( ) );
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        request.setParameter( "reset", "reset" );
        try
        {
            bean.doModifyEmailPattern( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( origEmailPattern + "b", getEmailPattern( ) );
        }
        finally
        {
            AdminUserService.doModifyEmailPattern( origEmailPattern, isEmailPatternSetManually( ) );
        }
    }
}
