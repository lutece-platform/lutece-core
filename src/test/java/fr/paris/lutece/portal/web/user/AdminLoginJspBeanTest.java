/*
 * Copyright (c) 2002-2022, City of Paris
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.IAdminUserDAO;
import fr.paris.lutece.portal.business.user.PasswordUpdateMode;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;
import jakarta.inject.Inject;

public class AdminLoginJspBeanTest extends LuteceTestCase
{
    private static final String NEW_PASSWORD = "password";
    private static final String PASSWORD = "Pa55word!";
    private LuteceDefaultAdminUser user;
    private @Inject IPasswordFactory passwordFactory;
    private @Inject IAdminUserDAO adminUserDAO;
    private @Inject AdminLoginJspBean bean;
    private @Inject ISecurityTokenService _securityTokenService;

    @BeforeEach
    public void setUp( ) throws Exception
    {
        assertFalse( PASSWORD.equals( NEW_PASSWORD ) );

        String randomUsername = "user" + new SecureRandom( ).nextLong( );

        user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( PASSWORD ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );
    }

    @AfterEach
    public void tearDown( ) throws Exception
    {
        AdminUserHome.remove( user.getUserId( ) );
        AdminUserHome.removeAllPasswordHistoryForUser( user.getUserId( ) );
    }
    @Test
    public void testDoLogin( ) throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) );
        bean.doLogin( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_USER_MUST_CHANGE_PASSWORD, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }
    @Test
    public void testDoLoginNoCSRFToken( ) throws Exception
    {

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
    @Test
    public void testDoLoginBadCSRFToken( ) throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) + "b" );
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
    @Test
    public void testDoLoginDisabledLuteceUser( ) throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "lutece" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }
    @Test
    public void testDoLoginDisabledRedacUser( ) throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "redac" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }
    @Test
    public void testDoLoginDisabledValidUser( ) throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN, _securityTokenService.getToken( request, "admin/admin_login.html" ) );
        request.addParameter( Parameters.ACCESS_CODE, "valid" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }
    @Test
    public void testDoForgotPasswordNoParam( ) throws Exception
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        bean.doForgotPassword( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
    }
    @Test
    public void testDoForgotPasswordDoesNotExist( ) throws Exception
    {


        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "DOES_NOT_EXIST" );
        String url = bean.doForgotPassword( request );
        assertTrue( url != null && url.endsWith ( "AdminMessage.jsp" ) );
    }
    @Test
    public void testDoForgotPasswordNoEmail( ) throws Exception
    {
        user.setEmail( null );
        adminUserDAO.store( user );



        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, user.getAccessCode( ) );
        String url = bean.doForgotPassword( request );
        assertTrue( url != null && url.endsWith ( "AdminMessage.jsp" ) );
    }
    @Test
    public void testDoForgotPassword( ) throws Exception
    {


        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, user.getAccessCode( ) );
        bean.doForgotPassword( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( "portal.admin.message.admin_forgot_password.sendingSuccess", Locale.FRENCH ),
                message.getText( Locale.FRENCH ) );
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
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
    @Test
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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
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

    @Test
    public void testDoResetPasswordSessionLockDifferentSessions( )
    {
        boolean previousSessionLockParam = setSessionLock( true );

        try
        {
    
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
            LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
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
    @Test
    public void testDoResetPasswordShortPassword( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordChangedPassword( )
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date( );
        String token = AdminUserService.getUserPasswordResetToken( user, timestamp, request );
        request.setParameter( "ts", Long.toString( timestamp.getTime( ) ) );
        request.setParameter( "token", token );
        request.setParameter( Parameters.NEW_PASSWORD, NEW_PASSWORD );
        request.setParameter( Parameters.CONFIRM_NEW_PASSWORD, NEW_PASSWORD );

        final String changedPassword = PASSWORD + "_changed";
        assertFalse( PASSWORD.equals( changedPassword ) );
        assertFalse( NEW_PASSWORD.equals( changedPassword ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( changedPassword ) );
        adminUserDAO.store( user, PasswordUpdateMode.UPDATE );

        String res = bean.doResetPassword( request );
        assertNotNull( res );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( AdminMessage.TYPE_STOP, message.getType( ) );
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( changedPassword ) );
    }
    @Test
    public void testDoResetPasswordExpiredToken( )
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setParameter( Parameters.USER_ID, Integer.toString( user.getUserId( ) ) );
        Date timestamp = new Date(
                new Date( ).getTime( ) + 1 + ( 1000L * 60 * AdminUserService.getIntegerSecurityParameter( AdminUserService.DSKEY_RESET_TOKEN_VALIDITY ) ) );
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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordNonexistentUser( )
    {

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
    @Test
    public void testDoResetPasswordBadToken( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordDifferentPasswords( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordNoNewPassword( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordNoConfirmPassword( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordGET( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordNoTimestamp( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordNoUserId( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testDoResetPasswordNoToken( )
    {

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
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
        assertNotNull( storedUser );
        assertTrue( storedUser.getPassword( ).check( PASSWORD ) );
    }
    @Test
    public void testGetResetPasswordNoRequestParameters( )
    {

        MockHttpServletRequest request = new MockHttpServletRequest( );

        bean.getResetPassword( request );
        assertTrue( "The template failed", true );
    }
}
