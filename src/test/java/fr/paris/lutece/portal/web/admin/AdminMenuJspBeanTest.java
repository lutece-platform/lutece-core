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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserDAO;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;

import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * AdminMenuJspBeanTest Test Class
 *
 */
public class AdminMenuJspBeanTest extends LuteceTestCase
{
    private static final String PARAMETER_LANGUAGE = "language";
    private static final String TEST_USER_ACCESS_CODE = "admin";
    private static final String TEST_USER_PASSWORD = "admin";
    private static final String TEST_LANGUAGE = "en";
    AdminUser _user = new AdminUser( );

    {
        _user.setFirstName( "first_name" );
        _user.setLastName( "last_name" );
    }

    /**
     * Test of getAdminMenuHeader method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testGetAdminMenuHeader( ) throws AccessDeniedException
    {
        System.out.println( "getAdminMenuHeader" );

        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        getUser( request );
        request.registerAdminUser( _user );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        instance.getAdminMenuHeader( request );
    }

    /**
     * Test of getAdminMenuUser method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testGetAdminMenuUser( ) throws AccessDeniedException
    {
        System.out.println( "getAdminMenuUser" );

        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        getUser( request );
        request.registerAdminUser( _user );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        instance.getAdminMenuUser( request );
    }

    /**
     * Test of doChangeLanguage method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testDoChangeLanguage( ) throws AccessDeniedException
    {
        System.out.println( "doChangeLanguage" );

        MokeHttpServletRequest request = new MokeHttpServletRequest( );
        request.addMokeParameters( PARAMETER_LANGUAGE, TEST_LANGUAGE );

        getUser( request );
        request.registerAdminUser( _user );
        _user.setLocale( Locale.FRANCE );

        Locale localeSTored = _user.getLocale( );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        instance.doChangeLanguage( request );
        assertNotSame( localeSTored.getLanguage( ), _user.getLocale( ).getLanguage( ) );
    }

    private void getUser( MokeHttpServletRequest request )
    {
        try
        {
            AdminAuthenticationService.getInstance( ).loginUser( request, TEST_USER_ACCESS_CODE, TEST_USER_PASSWORD );
            _user = AdminUserService.getAdminUser( request );
        }
        catch( FailedLoginException ex )
        {
            String strReturn = "../../" + AdminAuthenticationService.getInstance( ).getLoginPageUrl( );
        }
        catch( LoginException ex )
        {
            String strReturn = "../../" + AdminAuthenticationService.getInstance( ).getLoginPageUrl( );
        }
    }

    private AdminUserDAO getAdminUserDAO( )
    {
        AdminUserDAO adminUserDAO = new AdminUserDAO( );
        ApplicationContext context = SpringContextService.getContext( );
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory( );
        beanFactory.autowireBean( adminUserDAO );
        return adminUserDAO;
    }

    public void testDoModifyDefaultAdminUserPassword( )
    {
        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        String password = "Pa55word!";
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( password ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );
        try
        {
            AdminMenuJspBean instance = new AdminMenuJspBean( );
            // no args
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            instance.doModifyDefaultAdminUserPassword( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, password );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, password );
            request.addParameter( Parameters.NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, password );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.NEW_PASSWORD, password );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.NEW_PASSWORD, password );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, password );
            request.addParameter( Parameters.NEW_PASSWORD, password );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.password.confirm.error", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, "BOGUS" );
            request.addParameter( Parameters.NEW_PASSWORD, password + "_mod" );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.password.wrong.current", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, password );
            request.addParameter( Parameters.NEW_PASSWORD, password );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.users.message.password.new.equals.current", Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            request = new MockHttpServletRequest( );
            request.getSession( true ).setAttribute( "lutece_admin_user", user );
            request.addParameter( Parameters.PASSWORD_CURRENT, password );
            request.addParameter( Parameters.NEW_PASSWORD, password + "_mod" );
            request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
            instance.doModifyDefaultAdminUserPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( AppPropertiesService.getProperty( "lutece.admin.logout.url" ), message.getUrl( ) );

            List<IPassword> history = AdminUserHome.selectUserPasswordHistory( user.getUserId( ) );
            assertEquals( 1, history.size( ) );
            assertTrue( history.get( 0 ).check( password + "_mod" ) );
        }
        finally
        {
            adminUserDAO.delete( user.getUserId( ) );
            AdminUserHome.removeAllPasswordHistoryForUser( user.getUserId( ) );
        }

    }
}
