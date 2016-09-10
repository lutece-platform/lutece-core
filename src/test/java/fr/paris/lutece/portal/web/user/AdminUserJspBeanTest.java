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

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.admin.PasswordResetException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPasswordFactory;

public class AdminUserJspBeanTest extends LuteceTestCase
{
    public void testDoCreateAdminUser( ) throws PasswordResetException, AccessDeniedException, UserNotSignedException
    {
        AdminUserJspBean bean = new AdminUserJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        bean.doCreateAdminUser( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        String randomUserName = "User_" + new SecureRandom( ).nextLong( );
        try
        {
            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", "   " );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", "admin" );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", randomUserName + "@lutece.fr" );
            bean.doCreateAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.user.accessCodeAlreadyUsed", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.addParameter( "access_code", randomUserName );
            request.addParameter( "last_name", randomUserName );
            request.addParameter( "first_name", randomUserName );
            request.addParameter( "email", "admin@lutece.fr" );
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
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
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
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
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
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
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
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
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

    private AdminUser getLevel1AdminUserWithCORE_USERS_MANAGEMENTRight( )
    {
        AdminUser user = new AdminUser( );
        user.setUserLevel( 1 );
        Map<String, Right> rights = new HashMap<String, Right>( 1 );
        rights.put( "CORE_USERS_MANAGEMENT", new Right( ) );
        user.setRights( rights );
        return user;
    }

    public void testDoModifyAdminUser( ) throws AccessDeniedException, UserNotSignedException
    {
        AdminUser userToModify = getUserToModify( );
        try
        {
            AdminUserJspBean bean = new AdminUserJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "lutece" ) );
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
            bean.doModifyAdminUser( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            final String modifiedName = userToModify.getAccessCode( ) + "_mod";

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
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
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.user.accessEmailUsed", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", modifiedName + "@lutece.fr" );
            request.addParameter( "second_password", modifiedName );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.differentsPassword", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", modifiedName + "@lutece.fr" );
            request.addParameter( "first_password", modifiedName );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.differentsPassword", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", modifiedName + "@lutece.fr" );
            request.addParameter( "first_password", modifiedName );
            request.addParameter( "second_password", modifiedName + "diff" );
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.differentsPassword", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            AdminAuthenticationService.getInstance( ).registerUser( request, AdminUserHome.findUserByLogin( "admin" ) );
            request.addParameter( "id_user", Integer.toString( userToModify.getUserId( ) ) );
            request.addParameter( "access_code", modifiedName );
            request.addParameter( "last_name", modifiedName );
            request.addParameter( "first_name", modifiedName );
            request.addParameter( "email", modifiedName + "@lutece.fr" );
            request.addParameter( "first_password", modifiedName );
            request.addParameter( "second_password", modifiedName );
            request.addParameter( "status", Integer.toString( AdminUser.ACTIVE_CODE ) ); // NPE if absent
            request.addParameter( "language", "fr" ); // NPE if absent
            bean.doModifyAdminUser( request );
            message = AdminMessageService.getMessage( request );
            assertNull( message );
            LuteceDefaultAdminUser modifiedUser = AdminUserHome.findLuteceDefaultAdminUserByPrimaryKey( userToModify.getUserId( ) );
            assertNotNull( modifiedUser );
            assertEquals( modifiedName, modifiedUser.getAccessCode( ) );
            assertEquals( modifiedName, modifiedUser.getLastName( ) );
            assertEquals( modifiedName, modifiedUser.getFirstName( ) );
            assertEquals( modifiedName + "@lutece.fr", modifiedUser.getEmail( ) );
            assertTrue( modifiedUser.getPassword( ).check( modifiedName ) );
        }
        finally
        {
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
}
