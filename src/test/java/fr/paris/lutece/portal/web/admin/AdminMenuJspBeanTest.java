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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserDAO;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.business.user.menu.AccessibilityModeAdminUserMenuItemProvider;
import fr.paris.lutece.portal.business.user.menu.LanguageAdminUserMenuItemProvider;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.Utils;
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
        MockHttpServletRequest request = new MockHttpServletRequest( );
        getUser( request );
        Utils.registerAdminUser( request, _user );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        assertNotNull( instance.getAdminMenuHeader( request ) );
    }

    /**
     * Test of getAdminMenuUser method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testGetAdminMenuUser( ) throws AccessDeniedException
    {
        System.out.println( "getAdminMenuUser" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        getUser( request );
        Utils.registerAdminUser( request, _user );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        instance.getAdminMenuUser( request );
    }

    /**
     * Test of doChangeLanguage method, of class fr.paris.lutece.portal.web.admin.AdminMenuJspBean.
     */
    public void testDoChangeLanguage( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_LANGUAGE, TEST_LANGUAGE );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, LanguageAdminUserMenuItemProvider.TEMPLATE ) );

        getUser( request );
        Utils.registerAdminUser( request, _user );
        _user.setLocale( Locale.FRANCE );

        Locale localeSTored = _user.getLocale( );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        instance.doChangeLanguage( request );
        assertNotSame( localeSTored.getLanguage( ), _user.getLocale( ).getLanguage( ) );
    }

    public void testDoChangeLanguageInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_LANGUAGE, TEST_LANGUAGE );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, LanguageAdminUserMenuItemProvider.TEMPLATE ) + "b" );

        getUser( request );
        Utils.registerAdminUser( request, _user );
        _user.setLocale( Locale.FRANCE );

        Locale localeSTored = _user.getLocale( );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        try
        {
            instance.doChangeLanguage( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertSame( localeSTored.getLanguage( ), _user.getLocale( ).getLanguage( ) );
        }
    }

    public void testDoChangeLanguageNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_LANGUAGE, TEST_LANGUAGE );

        getUser( request );
        Utils.registerAdminUser( request, _user );
        _user.setLocale( Locale.FRANCE );

        Locale localeSTored = _user.getLocale( );

        AdminMenuJspBean instance = new AdminMenuJspBean( );
        try
        {
            instance.doChangeLanguage( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertSame( localeSTored.getLanguage( ), _user.getLocale( ).getLanguage( ) );
        }
    }

    private void getUser( MockHttpServletRequest request )
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

    public void testDoModifyDefaultAdminUserPassword( ) throws AccessDeniedException
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
            request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                    SecurityTokenService.getInstance( ).getToken( request, "admin/user/modify_password_default_module.html" ) );
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

    public void testDoModifyDefaultAdminUserPasswordInvalidToken( ) throws AccessDeniedException
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
        AdminMenuJspBean instance = new AdminMenuJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", user );
        request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", user );
        request.addParameter( Parameters.PASSWORD_CURRENT, password );
        request.addParameter( Parameters.NEW_PASSWORD, password + "_mod" );
        request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, "admin/user/modify_password_default_module.html" ) + "b" );
        try
        {
            instance.doModifyDefaultAdminUserPassword( request );
            fail( "Shoulf have thrown" );
        }
        catch( AccessDeniedException e )
        {
            List<IPassword> history = AdminUserHome.selectUserPasswordHistory( user.getUserId( ) );
            assertEquals( 0, history.size( ) );
            LuteceDefaultAdminUser stored = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            assertTrue( stored.getPassword( ).check( password ) );
        }
    }

    public void testDoModifyDefaultAdminUserPasswordNoToken( ) throws AccessDeniedException
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
        AdminMenuJspBean instance = new AdminMenuJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", user );
        request = new MockHttpServletRequest( );
        request.getSession( true ).setAttribute( "lutece_admin_user", user );
        request.addParameter( Parameters.PASSWORD_CURRENT, password );
        request.addParameter( Parameters.NEW_PASSWORD, password + "_mod" );
        request.addParameter( Parameters.CONFIRM_NEW_PASSWORD, password + "_mod" );

        try
        {
            instance.doModifyDefaultAdminUserPassword( request );
            fail( "Shoulf have thrown" );
        }
        catch( AccessDeniedException e )
        {
            List<IPassword> history = AdminUserHome.selectUserPasswordHistory( user.getUserId( ) );
            assertEquals( 0, history.size( ) );
            LuteceDefaultAdminUser stored = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            assertTrue( stored.getPassword( ).check( password ) );
        }
    }

    public void testDoModifyAccessibilityMode( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AccessibilityModeAdminUserMenuItemProvider.TEMPLATE ) );

        getUser( request );
        Utils.registerAdminUser( request, _user );
        boolean bAccessibilityMode = _user.getAccessibilityMode( );
        try
        {
            AdminMenuJspBean instance = new AdminMenuJspBean( );
            instance.doModifyAccessibilityMode( request );
            assertEquals( !bAccessibilityMode, _user.getAccessibilityMode( ) );
        }
        finally
        {
            _user.setAccessibilityMode( bAccessibilityMode );
            AdminUserHome.update( _user );
        }
    }

    public void testDoModifyAccessibilityModeInvalidToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( SecurityTokenService.PARAMETER_TOKEN,
                SecurityTokenService.getInstance( ).getToken( request, AccessibilityModeAdminUserMenuItemProvider.TEMPLATE ) + "b" );

        getUser( request );
        Utils.registerAdminUser( request, _user );
        boolean bAccessibilityMode = _user.getAccessibilityMode( );
        try
        {
            AdminMenuJspBean instance = new AdminMenuJspBean( );
            instance.doModifyAccessibilityMode( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( bAccessibilityMode, _user.getAccessibilityMode( ) );
        }
        finally
        {
            _user.setAccessibilityMode( bAccessibilityMode );
            AdminUserHome.update( _user );
        }
    }

    public void testDoModifyAccessibilityModeNoToken( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );

        getUser( request );
        Utils.registerAdminUser( request, _user );
        boolean bAccessibilityMode = _user.getAccessibilityMode( );
        try
        {
            AdminMenuJspBean instance = new AdminMenuJspBean( );
            instance.doModifyAccessibilityMode( request );
            fail( "Should have thrown" );
        }
        catch( AccessDeniedException e )
        {
            assertEquals( bAccessibilityMode, _user.getAccessibilityMode( ) );
        }
        finally
        {
            _user.setAccessibilityMode( bAccessibilityMode );
            AdminUserHome.update( _user );
        }
    }
}
