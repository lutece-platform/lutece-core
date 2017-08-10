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

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUserDAO;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.PasswordUpdateMode;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;

public class AdminLoginJspBeanTest extends LuteceTestCase
{
    private static final String NEW_PASSWORD = "password";
    private static final String PASSWORD = "Pa55word!";
    private LuteceDefaultAdminUser user;

    @Override
    public void setUp( ) throws Exception
    {
        super.setUp( );

        assertFalse( PASSWORD.equals( NEW_PASSWORD ) );

        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( PASSWORD ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );
    }

    @Override
    public void tearDown( ) throws Exception
    {
        AdminUserHome.remove( user.getUserId( ) );
        AdminUserHome.removeAllPasswordHistoryForUser( user.getUserId( ) );
        super.tearDown( );
    }

    public void testDoLogin( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) );
        bean.doLogin( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_USER_MUST_CHANGE_PASSWORD, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }

    public void testDoLoginNoCSRFToken( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        try
        {
            bean.doLogin( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // OK
        }
    }

    public void testDoLoginBadCSRFToken( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) + "b" );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        try
        {
            bean.doLogin( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            // OK
        }
    }

    public void testDoLoginDisabledLuteceUser( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "lutece" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }

    public void testDoLoginDisabledRedacUser( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "redac" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }

    public void testDoLoginDisabledValidUser( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, SecurityTokenService.getInstance( ).getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "valid" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }

    private AdminUserDAO getAdminUserDAO( )
    {
        AdminUserDAO adminUserDAO = new AdminUserDAO( );
        ApplicationContext context = SpringContextService.getContext( );
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory( );
        beanFactory.autowireBean( adminUserDAO );
        return adminUserDAO;
    }

    public void testDoForgotPasswordNoParam( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        bean.doForgotPassword( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }

    public void testDoForgotPasswordDoesNotExist( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "DOES_NOT_EXIST" );
        String url = bean.doForgotPassword( request );
        assertEquals( "AdminFormContact.jsp", url );
    }

    public void testDoForgotPasswordNoEmail( ) throws Exception
    {
        user.setEmail( null );
        getAdminUserDAO( ).store( user );

        AdminLoginJspBean bean = new AdminLoginJspBean( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, user.getAccessCode( ) );
        String url = bean.doForgotPassword( request );
        assertEquals( "AdminFormContact.jsp", url );
    }

    public void testDoForgotPassword( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, user.getAccessCode( ) );
        bean.doForgotPassword( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( "portal.admin.message.admin_forgot_password.sendingSuccess", Locale.FRENCH ),
                message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNoSessionLock( )
    {
        boolean previousSessionLockParam = setSessionLock( false );

        try
        {
            doResetPasswordTest( );
        }
        finally
        {
            restoreSessionLock( previousSessionLockParam );
        }
    }

    public void testDoResetPasswordSessionLock( )
    {
        boolean previousSessionLockParam = setSessionLock( true );

        try
        {
            doResetPasswordTest( );
        }
        finally
        {
            restoreSessionLock( previousSessionLockParam );
        }
    }

    private void doResetPasswordTest( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_INFO, message.getType( ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertFalse( storedUser.getPassword( ).check( PASSWORD ) );
        assertTrue( storedUser.getPassword( ).check( NEW_PASSWORD ) );
        List<IPassword> passwordHistory = AdminUserHome.selectUserPasswordHistory( user.getUserId( ) );
        boolean found = false;
        for ( IPassword password : passwordHistory )
        {
            if ( password.check( NEW_PASSWORD ) )
            {
                found = true;
                break;
            }
        }
        assertTrue( found );
    }

    public void testDoResetPasswordSessionLockDifferentSessions( )
    {
        boolean previousSessionLockParam = setSessionLock( true );

        try
        {
            AdminLoginJspBean bean = new AdminLoginJspBean( );
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.setMethod( "POST" );
            request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
            Date timestamp = new Date( );
            String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
            request.changeSessionId( );
            request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
            request.setParameter( "token", token );
            request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
            request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

            String res = bean.doResetPassword( request );
            assertNotNull( res );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
            LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
            assertNotNull( storedUser );
            assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
        }
        finally
        {
            restoreSessionLock( previousSessionLockParam );
        }
    }

    private boolean setSessionLock( boolean locked )
    {
        boolean previous = AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION );
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION, Boolean.valueOf( locked ).toString( ) );
        return previous;
    }

    private void restoreSessionLock( boolean previousSessionLockParam )
    {
        AdminUserService.updateSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION, Boolean.valueOf( previousSessionLockParam ).toString( ) );
    }

    public void testDoResetPasswordShortPassword( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        String newPassword = "p";
        request.setParameter( Parameters.NEW_PASSWORD, newPassword );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, newPassword );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordChangedPassword( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );
        final String changedPassword = PASSWORD + "_changed";
        assertFalse( PASSWORD.equals( changedPassword ) );
        assertFalse( NEW_PASSWORD.equals( changedPassword ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( changedPassword ) );
        getAdminUserDAO( ).store( user, PasswordUpdateMode.UPDATE );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( changedPassword ) );
    }

    public void testDoResetPasswordExpiredToken( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( new Date( ).getTime( ) + 1
                + ( 1000L * 60 * AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY ) ) );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNonexistentUser( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( Integer.MAX_VALUE ) );
        Date timestamp = new Date( );
        String token = AdminUserHome.getUserPasswordResetToken( Integer.MAX_VALUE, timestamp,
                AdminUserService.getBooleanSecurityParameter( AdminUserService.DSKEY_LOCK_RESET_TOKEN_TO_SESSION ) ? request.getSession( ).getId( ) : null );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
    }

    public void testDoResetPasswordBadToken( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        char [ ] tokenCharacters = token.toCharArray( );
        tokenCharacters [0] += 1;
        request.setParameter( "token", new String( tokenCharacters ) );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordDifferentPasswords( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD + "diff" );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNoNewPassword( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNoConfirmPassword( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordGET( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "GET" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        try
        {
            bean.doResetPassword( request );
            fail( "should have thrown" );
        }
        catch( AppException e )
        {
        }
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNoTimestamp( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNoUserId( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testDoResetPasswordNoToken( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = getAdminUserDAO( ).loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }

    public void testGetResetPasswordNoRequestParameters( )
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );

        bean.getResetPassword( request );
        assertTrue( "The template failed", true );
    }
}
