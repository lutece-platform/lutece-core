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
import java.util.Locale;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.business.user.AdminUserDAO;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPasswordFactory;

public class AdminLoginJspBeanTest extends LuteceTestCase
{
    public void testDoLogin( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        bean.doLogin( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        bean.doLogin( request );
        message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MESSAGE_AUTH_FAILURE, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "admin" );
        request.addParameter( Parameters.PASSWORD, "adminadmin" );
        bean.doLogin( request );
        message = AdminMessageService.getMessage( request );
        assertNull( message );
    }

    private AdminUserDAO getAdminUserDAO( )
    {
        AdminUserDAO adminUserDAO = new AdminUserDAO( );
        ApplicationContext context = SpringContextService.getContext( );
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory( );
        beanFactory.autowireBean( adminUserDAO );
        return adminUserDAO;
    }

    public void testDoForgotPassword( ) throws Exception
    {
        AdminLoginJspBean bean = new AdminLoginJspBean( );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        bean.doForgotPassword( request );
        AdminMessage message = AdminMessageService.getMessage( request );
        assertNotNull( message );
        assertEquals( I18nService.getLocalizedString( Messages.MANDATORY_FIELDS, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

        request = new MockHttpServletRequest( );
        request.addParameter( Parameters.ACCESS_CODE, "DOES_NOT_EXIST" );
        String url = bean.doForgotPassword( request );
        assertEquals( "AdminFormContact.jsp", url );

        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        String password = "Pa55word!";
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( password ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( "" );
        adminUserDAO.insert( user );
        try
        {
            request = new MockHttpServletRequest( );
            request.addParameter( Parameters.ACCESS_CODE, randomUsername );
            url = bean.doForgotPassword( request );
            assertEquals( "AdminFormContact.jsp", url );

            user.setEmail( randomUsername + "@lutece.fr" );
            adminUserDAO.store( user );
            request = new MockHttpServletRequest( );
            request.addParameter( Parameters.ACCESS_CODE, randomUsername );
            bean.doForgotPassword( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( "portal.admin.message.admin_forgot_password.sendingSuccess", Locale.FRENCH ),
                    message.getText( Locale.FRENCH ) );
            LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            assertNotNull( storedUser );
            assertFalse( storedUser.getPassword( ).check( password ) );
        }
        finally
        {
            AdminUserHome.remove( user.getUserId( ) );
        }
    }
}
