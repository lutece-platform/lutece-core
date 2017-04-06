/*
 * Copyright (c) 2015, Mairie de Paris
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
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
            bean.init( request, "CORE_USERS_MANAGEMENT" ) ;
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
            bean.init( request, "CORE_USERS_MANAGEMENT" ) ;
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
            bean.init( request, "CORE_USERS_MANAGEMENT" ) ;
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
            bean.init( request, "CORE_USERS_MANAGEMENT" ) ;
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
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/CreateUser.jsp" ) + "b" );
            bean.init( request, "CORE_USERS_MANAGEMENT" ) ;
            bean.doCreateAdminUser( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
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
            bean.init( request, "CORE_USERS_MANAGEMENT" ) ;
            bean.doCreateAdminUser( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
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
        Map<String, Right> rights = new HashMap<String, Right>(1);
        rights.put( "CORE_USERS_MANAGEMENT", new Right( ) );
        user.setRights( rights );
        return user;
    }

    public void testDoModifyAdminUserLegacyPassword( )
    {
        
    }
    
    public void testDoModifyAdminUser(  ) throws AccessDeniedException, UserNotSignedException
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
            AdminUserHome.removeAllOwnRightsForUser( userToModify ); 
            AdminUserHome.remove( userToModify.getUserId( ) );
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
            AdminUserHome.removeAllOwnRightsForUser( userToModify ); 
            AdminUserHome.remove( userToModify.getUserId( ) );
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
            fail( "Should have thrown ");
        }
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( userToModify ); 
            AdminUserHome.remove( userToModify.getUserId( ) );
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
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ModifyUser.jsp" ) + "b" );
            bean.doModifyAdminUser( request );
            fail( "Should have thrown ");
        }
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( userToModify ); 
            AdminUserHome.remove( userToModify.getUserId( ) );
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
        return AdminUserHome.findByPrimaryKey( user.getUserId( ) );
    }

    // FIXME : this only tests that passwords are unchanged
    public void testDoUseAdvancedSecurityParameters( )
    {
        boolean bUseAdvancesSecurityParameters = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_USE_ADVANCED_SECURITY_PARAMETERS );
        AdminUserJspBean bean = new AdminUserJspBean( );
        try
        {
            LuteceDefaultAdminUser admin = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( 1 );
            assertTrue( admin.getPassword( ).check( "adminadmin" ) );
            bean.doUseAdvancedSecurityParameters( new MockHttpServletRequest( ) );
            admin = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( 1 );
            assertTrue( admin.getPassword( ).check( "adminadmin" ) );
        }
        finally
        {
            if ( !bUseAdvancesSecurityParameters )
            {
                bean.doRemoveAdvancedSecurityParameters( new MockHttpServletRequest( ) );
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
        catch ( AppException e )
        {
            //OK
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
        catch ( AppException e )
        {
            //OK
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
        catch ( AccessDeniedException e )
        {
            //OK
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
        {
            //OK
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AppException e)
        {
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
        {
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
        {
            assertFalse( AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( user.getUserId( ) ).getPassword( ).check( password ) );
        }
        finally
        {
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRights.jsp" ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRights.jsp" ) + "b" );
            bean.doModifyAdminUserRights( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRoles.jsp" ) );
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
            AdminUserHome.removeAllRolesForUser( user.getUserId( ) );
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserRoles.jsp" ) + "b" );
            bean.doModifyAdminUserRoles( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllRolesForUser( user.getUserId( ) );
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllRolesForUser( user.getUserId( ) );
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserWorkgroups.jsp" ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.setParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ManageUserWorkgroups.jsp" ) + "b" );
            bean.doModifyAdminUserWorkgroups( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoAnonymizeAdminUser.jsp" ) );
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoAnonymizeAdminUser.jsp" ) + "b" );
            bean.doAnonymizeAdminUser( request );
            fail( "Should have thrown" );
        }
        catch (AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch (AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/user/field_anonymize_admin_user.html" ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/user/field_anonymize_admin_user.html" ) + "b" );
            bean.doChangeFieldAnonymizeAdminUsers( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
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
        catch ( AccessDeniedException e )
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
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoRemoveUser.jsp" ) );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doRemoveAdminUser( request );
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertNull( stored );
        }
        finally
        {
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/DoRemoveUser.jsp" ) + "b" );
            AdminAuthenticationService.getInstance( ).registerUser( request, user );
            bean.init( request, "CORE_USERS_MANAGEMENT" );
            bean.doRemoveAdminUser( request );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertNotNull( stored );
        }
        finally
        {
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
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
        catch ( AccessDeniedException e )
        {
            assertNull( AdminMessageService.getMessage( request ) );
            AdminUser stored = AdminUserHome.findByPrimaryKey( user.getUserId( ) );
            assertNotNull( stored );
        }
        finally
        {
            AdminUserHome.removeAllOwnRightsForUser( user ); 
            AdminUserHome.remove( user.getUserId( ) );
        }  
    }

    public void testDoImportUsersFromFile( ) throws AccessDeniedException, UserNotSignedException, IOException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ImportUser.jsp" ) );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem file = new DiskFileItem( "import_file", "application/csv", true, "junit.csv", 1024, new File( System.getProperty( "java.io.tmpdir" ) ) );
        OutputStreamWriter writer = new OutputStreamWriter( file.getOutputStream( ), Charset.forName( "UTF-8" ) );
        writer.write( "test;test;test;test@test.fr;" + AdminUser.ACTIVE_CODE + ";" + Locale.FRANCE + ";0;false;false;;;" );
        writer.close( );
        fileItems.add( file );
        multipartFiles.put( "import_file", fileItems );
        Map<String, String[ ]> parameters = request.getParameterMap( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        bean.getImportUsersFromFile( request ); // initialize _importAdminUserService
        AdminUser user = null;
        try
        {
            bean.doImportUsersFromFile( multipartRequest );
            user = AdminUserHome.findUserByLogin( "test" );
            assertNotNull( user );
            assertEquals( "test", user.getAccessCode( ) );
            assertEquals( "test", user.getFirstName( ) );
            assertEquals( "test", user.getLastName( ) );
            assertEquals( "test@test.fr", user.getEmail( ) );
            assertEquals( AdminUser.ACTIVE_CODE, user.getStatus( ) );
        }
        finally
        {
            if ( user != null )
            {
                AdminUserHome.removeAllOwnRightsForUser( user ); 
                AdminUserHome.remove( user.getUserId( ) );
            }
        }
    }

    public void testDoImportUsersFromFileInvalidToken( ) throws AccessDeniedException, UserNotSignedException, IOException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "jsp/admin/user/ImportUser.jsp" ) + "b" );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem file = new DiskFileItem( "import_file", "application/csv", true, "junit.csv", 1024, new File( System.getProperty( "java.io.tmpdir" ) ) );
        OutputStreamWriter writer = new OutputStreamWriter( file.getOutputStream( ), Charset.forName( "UTF-8" ) );
        writer.write( "test;test;test;test@test.fr;" + AdminUser.ACTIVE_CODE + ";" + Locale.FRANCE + ";0;false;false;;;" );
        writer.close( );
        fileItems.add( file );
        multipartFiles.put( "import_file", fileItems );
        Map<String, String[ ]> parameters = request.getParameterMap( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        bean.getImportUsersFromFile( request ); // initialize _importAdminUserService
        AdminUser user = null;
        try
        {
            bean.doImportUsersFromFile( multipartRequest );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            user = AdminUserHome.findUserByLogin( "test" );
            assertNull( user );
        }
        finally
        {
            if ( user != null )
            {
                AdminUserHome.removeAllOwnRightsForUser( user ); 
                AdminUserHome.remove( user.getUserId( ) );
            }
        }
    }

    public void testDoImportUsersFromFileNoToken( ) throws AccessDeniedException, UserNotSignedException, IOException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        Map<String, List<FileItem>> multipartFiles = new HashMap<>( );
        List<FileItem> fileItems = new ArrayList<>( );
        FileItem file = new DiskFileItem( "import_file", "application/csv", true, "junit.csv", 1024, new File( System.getProperty( "java.io.tmpdir" ) ) );
        OutputStreamWriter writer = new OutputStreamWriter( file.getOutputStream( ), Charset.forName( "UTF-8" ) );
        writer.write( "test;test;test;test@test.fr;" + AdminUser.ACTIVE_CODE + ";" + Locale.FRANCE + ";0;false;false;;;" );
        writer.close( );
        fileItems.add( file );
        multipartFiles.put( "import_file", fileItems );
        Map<String, String[ ]> parameters = request.getParameterMap( );
        MultipartHttpServletRequest multipartRequest = new MultipartHttpServletRequest( request, multipartFiles, parameters );
        bean.getImportUsersFromFile( request ); // initialize _importAdminUserService
        AdminUser user = null;
        try
        {
            bean.doImportUsersFromFile( multipartRequest );
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            user = AdminUserHome.findUserByLogin( "test" );
            assertNull( user );
        }
        finally
        {
            if ( user != null )
            {
                AdminUserHome.removeAllOwnRightsForUser( user ); 
                AdminUserHome.remove( user.getUserId( ) );
            }
        }
    }

    public void testDoInsertRegularExpression( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
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
        }
    }

    public void testDoInsertRegularExpressionInvalidToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        request.setParameter( "id_expression", "1" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "ManageAdvancedParameters.jsp" ) + "b" );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        try
        {
            bean.doInsertRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            // OK
        }
        finally
        {
            AdminUserService.doRemoveRegularExpression( 1 );
        }
    }

    public void testDoInsertRegularExpressionNoToken( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
        request.setParameter( "id_expression", "1" );
        bean.init( request, "CORE_USERS_MANAGEMENT" );
        try
        {
            bean.doInsertRegularExpression( request ); // FIXME not really testing this plugin-regularexpression is not there
            fail( "Should have thrown" );
        }
        catch ( AccessDeniedException e )
        {
            // OK
        }
        finally
        {
            AdminUserService.doRemoveRegularExpression( 1 );
        }
    }
}
