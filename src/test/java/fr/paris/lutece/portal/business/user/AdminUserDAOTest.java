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
package fr.paris.lutece.portal.business.user;

import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;

public class AdminUserDAOTest extends LuteceTestCase
{

    private AdminUserDAO getAdminUserDAO( )
    {
        AdminUserDAO adminUserDAO = new AdminUserDAO( );
        ApplicationContext context = SpringContextService.getContext( );
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory( );
        beanFactory.autowireBean( adminUserDAO );
        return adminUserDAO;
    }

    public void testInsertLuteceDefaultAdminUser( )
    {
        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( randomUsername ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );

        try
        {
            LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            assertEquals( randomUsername, storedUser.getAccessCode( ) );
            assertEquals( randomUsername, storedUser.getFirstName( ) );
            assertEquals( randomUsername, storedUser.getLastName( ) );
            assertEquals( randomUsername + "@lutece.fr", storedUser.getEmail( ) );
            assertTrue( storedUser.getPassword( ).check( randomUsername ) );
        }
        finally
        {
            adminUserDAO.delete( user.getUserId( ) );
        }
    }

    public void testLoadDefaultAdminUser( )
    {
        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( 1 );
        assertEquals( "admin", storedUser.getAccessCode( ) );
        assertEquals( "admin", storedUser.getFirstName( ) );
        assertEquals( "Admin", storedUser.getLastName( ) );
        assertEquals( "admin@lutece.fr", storedUser.getEmail( ) );
        assertEquals( AdminUser.ACTIVE_CODE, storedUser.getStatus( ) );
        assertTrue( storedUser.getPassword( ).check( "adminadmin" ) );
        assertEquals( Locale.FRENCH, storedUser.getLocale( ) );
        assertEquals( 0, storedUser.getUserLevel( ) );
        assertFalse( storedUser.getAccessibilityMode( ) );
        assertNull( storedUser.getAccountMaxValidDate( ) );
        assertEquals( "all", storedUser.getWorkgroupKey( ) );
    }

    public void testStoreLuteceDefaultAdminUser( )
    {
        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( randomUsername ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );

        try
        {
            LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            String changedRandomUsername = randomUsername + "_2";
            storedUser.setAccessCode( changedRandomUsername );
            storedUser.setFirstName( changedRandomUsername );
            storedUser.setLastName( changedRandomUsername );
            storedUser.setEmail( changedRandomUsername + "@lutece.fr" );
            storedUser.setPassword( passwordFactory.getPasswordFromCleartext( changedRandomUsername ) );

            adminUserDAO.store( storedUser, PasswordUpdateMode.UPDATE );

            storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );

            assertEquals( changedRandomUsername, storedUser.getAccessCode( ) );
            assertEquals( changedRandomUsername, storedUser.getFirstName( ) );
            assertEquals( changedRandomUsername, storedUser.getLastName( ) );
            assertEquals( changedRandomUsername + "@lutece.fr", storedUser.getEmail( ) );
            assertTrue( storedUser.getPassword( ).check( changedRandomUsername ) );
        }
        finally
        {
            adminUserDAO.delete( user.getUserId( ) );
        }
    }
    
    public void testStoreLuteceDefaultAdminUserIgnorePassword( )
    {
        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( randomUsername ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );

        try
        {
            LuteceDefaultAdminUser storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );
            String changedRandomUsername = randomUsername + "_2";
            storedUser.setAccessCode( changedRandomUsername );
            storedUser.setFirstName( changedRandomUsername );
            storedUser.setLastName( changedRandomUsername );
            storedUser.setEmail( changedRandomUsername + "@lutece.fr" );
            storedUser.setPassword( passwordFactory.getPasswordFromCleartext( changedRandomUsername ) );

            adminUserDAO.store( storedUser, PasswordUpdateMode.IGNORE );

            storedUser = adminUserDAO.loadDefaultAdminUser( user.getUserId( ) );

            assertEquals( changedRandomUsername, storedUser.getAccessCode( ) );
            assertEquals( changedRandomUsername, storedUser.getFirstName( ) );
            assertEquals( changedRandomUsername, storedUser.getLastName( ) );
            assertEquals( changedRandomUsername + "@lutece.fr", storedUser.getEmail( ) );
            assertTrue( storedUser.getPassword( ).check( randomUsername ) );
        }
        finally
        {
            adminUserDAO.delete( user.getUserId( ) );
        }
    }

    public void testPasswordHistory( )
    {
        AdminUserDAO adminUserDAO = getAdminUserDAO( );
        String randomUsername = "user" + new SecureRandom( ).nextLong( );
        IPasswordFactory passwordFactory = SpringContextService.getBean( IPasswordFactory.BEAN_NAME );

        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser( randomUsername, new LuteceDefaultAdminAuthentication( ) );
        user.setPassword( passwordFactory.getPasswordFromCleartext( randomUsername ) );
        user.setFirstName( randomUsername );
        user.setLastName( randomUsername );
        user.setEmail( randomUsername + "@lutece.fr" );
        adminUserDAO.insert( user );

        try
        {
            adminUserDAO.insertNewPasswordInHistory( passwordFactory.getPasswordFromCleartext( "1" ), user.getUserId( ) );
            Thread.sleep( 1000 ); // Need this because the PRIMARY KEY uses the timestamp
            adminUserDAO.insertNewPasswordInHistory( passwordFactory.getPasswordFromCleartext( "2" ), user.getUserId( ) );
            Thread.sleep( 1000 ); // Need this because the PRIMARY KEY uses the timestamp
            adminUserDAO.insertNewPasswordInHistory( passwordFactory.getPasswordFromCleartext( "3" ), user.getUserId( ) );

            List<IPassword> passwords = adminUserDAO.selectUserPasswordHistory( user.getUserId( ) );
            assertNotNull( passwords );
            assertEquals( 3, passwords.size( ) );
            assertTrue( passwords.get( 0 ).check( "3" ) );
            assertTrue( passwords.get( 1 ).check( "2" ) );
            assertTrue( passwords.get( 2 ).check( "1" ) );
        }
        catch( InterruptedException e )
        {
            throw new RuntimeException( e ); // Should not happen
        }
        finally
        {
            try
            {
                adminUserDAO.removeAllPasswordHistoryForUser( user.getUserId( ) );
            }
            finally
            {
            }
            adminUserDAO.delete( user.getUserId( ) );
        }
    }
}
