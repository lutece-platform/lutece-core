/*
 * Copyright (c) 2016, Mairie de Paris
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
package fr.paris.lutece.portal.business.user;

import java.security.SecureRandom;
import java.util.Date;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;

public class AdminUserHomeTest extends LuteceTestCase
{

    private static final String LEGACY_PASSWORD = "legacyPassword";
    private LuteceDefaultAdminUser user;

    @Override
    public void setUp( ) throws Exception
    {
        super.setUp( );

        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );

        user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( new IPassword( )
        {

            @Override
            public boolean isLegacy( )
            {
                return false;
            }

            @Override
            public String getStorableRepresentation( )
            {
                return "PLAINTEXT:" + LEGACY_PASSWORD;
            }

            @Override
            public boolean check( String strCleartextPassword )
            {
                return LEGACY_PASSWORD.equals( strCleartextPassword );
            }
        } );
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

    private AdminUserDAO getAdminUserDAO( )
    {
        AdminUserDAO adminUserDAO = new AdminUserDAO( );
        ApplicationContext context = SpringContextService.getContext( );
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory( );
        beanFactory.autowireBean( adminUserDAO );
        return adminUserDAO;
    }

    public void testGetUserPasswordResetTokenLegacyPassword( )
    {
        Date timestamp = new Date( );
        String strToken = AdminUserHome.getUserPasswordResetToken( user.getUserId( ), timestamp, null );
        assertNotNull( strToken );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );
        user.setPassword( passwordFactory.getPasswordFromCleartext( LEGACY_PASSWORD ) );
        getAdminUserDAO( ).store( user );
        String strTokenUpdatedPassword = AdminUserHome.getUserPasswordResetToken( user.getUserId( ), timestamp, null );
        assertNotNull( strTokenUpdatedPassword );
        assertFalse( strToken.equals( strTokenUpdatedPassword ) );

    }

}
