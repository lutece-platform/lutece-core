/*
 * Copyright (c) 2002-2026, City of Paris
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
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
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
package fr.paris.lutece.portal.service.daemon;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminAuthentication;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPasswordFactory;
import jakarta.inject.Inject;

/**
 * Tests of the resynchronization of the account life time from the last login date by {@link AccountLifeTimeDaemon}
 */
public class AccountLifeTimeDaemonTest extends LuteceTestCase
{
    private static final int ACCOUNT_LIFE_TIME_MONTHS = 6;
    private static final long ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L;

    @Inject
    private IPasswordFactory _passwordFactory;

    private LuteceDefaultAdminUser _user;
    private String _strPreviousLifeTime;
    private String _strPreviousTimeBeforeAlert;

    @BeforeEach
    public void setUp( ) throws Exception
    {
        _strPreviousLifeTime = getDatastoreValue( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME );
        _strPreviousTimeBeforeAlert = getDatastoreValue( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT );
        DatastoreService.setDataValue( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, Integer.toString( ACCOUNT_LIFE_TIME_MONTHS ) );
        DatastoreService.setDataValue( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT, "0" );

        String strRandomUsername = "user" + new SecureRandom( ).nextLong( );
        _user = new LuteceDefaultAdminUser( strRandomUsername, new LuteceDefaultAdminAuthentication( ) );
        _user.setPassword( _passwordFactory.getPasswordFromCleartext( strRandomUsername ) );
        _user.setFirstName( strRandomUsername );
        _user.setLastName( strRandomUsername );
        // no email : the daemon must not try to send alerts to this user
        _user.setEmail( "" );
        AdminUserHome.create( _user );
    }

    @AfterEach
    public void tearDown( ) throws Exception
    {
        AdminUserHome.remove( _user.getUserId( ) );
        AdminUserHome.removeAllPasswordHistoryForUser( _user.getUserId( ) );
        restoreDatastoreValue( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, _strPreviousLifeTime );
        restoreDatastoreValue( AdminUserService.DSKEY_TIME_BEFORE_ALERT_ACCOUNT, _strPreviousTimeBeforeAlert );
    }

    @Test
    public void testActiveAccountWithRecentLoginIsResyncedInsteadOfExpired( )
    {
        Timestamp lastLogin = monthsAgo( 1 );
        setAccountState( AdminUser.ACTIVE_CODE, daysAgo( 1 ), lastLogin );

        new AccountLifeTimeDaemon( ).run( );

        AdminUser user = AdminUserHome.findByPrimaryKey( _user.getUserId( ) );
        assertEquals( AdminUser.ACTIVE_CODE, user.getRealStatus( ) );
        assertEquals( AdminUserService.getAccountMaxValidDate( lastLogin ), user.getAccountMaxValidDate( ) );
        assertTrue( user.getAccountMaxValidDate( ).after( new Timestamp( System.currentTimeMillis( ) ) ) );
    }

    @Test
    public void testExpiredAccountWithRecentLoginIsReactivated( )
    {
        Timestamp lastLogin = monthsAgo( 1 );
        setAccountState( AdminUser.EXPIRED_CODE, daysAgo( 1 ), lastLogin );

        new AccountLifeTimeDaemon( ).run( );

        AdminUser user = AdminUserHome.findByPrimaryKey( _user.getUserId( ) );
        assertEquals( AdminUser.ACTIVE_CODE, user.getRealStatus( ) );
        assertEquals( AdminUserService.getAccountMaxValidDate( lastLogin ), user.getAccountMaxValidDate( ) );
    }

    @Test
    public void testAccountThatNeverLoggedInExpires( )
    {
        Timestamp maxValidDate = daysAgo( 1 );
        setAccountState( AdminUser.ACTIVE_CODE, maxValidDate, AdminUser.getDefaultDateLastLogin( ) );

        new AccountLifeTimeDaemon( ).run( );

        AdminUser user = AdminUserHome.findByPrimaryKey( _user.getUserId( ) );
        assertEquals( AdminUser.EXPIRED_CODE, user.getRealStatus( ) );
        assertEquals( maxValidDate, user.getAccountMaxValidDate( ) );
    }

    @Test
    public void testInactiveAccountExpires( )
    {
        Timestamp maxValidDate = daysAgo( 1 );
        setAccountState( AdminUser.ACTIVE_CODE, maxValidDate, monthsAgo( ACCOUNT_LIFE_TIME_MONTHS + 1 ) );

        new AccountLifeTimeDaemon( ).run( );

        AdminUser user = AdminUserHome.findByPrimaryKey( _user.getUserId( ) );
        assertEquals( AdminUser.EXPIRED_CODE, user.getRealStatus( ) );
        assertEquals( maxValidDate, user.getAccountMaxValidDate( ) );
    }

    @Test
    public void testNoResyncWhenAccountLifeTimeIsNotSet( )
    {
        DatastoreService.setDataValue( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, "0" );
        Timestamp maxValidDate = daysAgo( 1 );
        setAccountState( AdminUser.ACTIVE_CODE, maxValidDate, monthsAgo( 1 ) );

        new AccountLifeTimeDaemon( ).run( );

        AdminUser user = AdminUserHome.findByPrimaryKey( _user.getUserId( ) );
        assertEquals( maxValidDate, user.getAccountMaxValidDate( ) );
        assertEquals( AdminUser.EXPIRED_CODE, user.getRealStatus( ) );
    }

    @Test
    public void testAccountWithFutureDateIsNotModified( )
    {
        Timestamp maxValidDate = new Timestamp( System.currentTimeMillis( ) + ( 30L * ONE_DAY_MILLIS ) );
        setAccountState( AdminUser.ACTIVE_CODE, maxValidDate, monthsAgo( 1 ) );

        new AccountLifeTimeDaemon( ).run( );

        AdminUser user = AdminUserHome.findByPrimaryKey( _user.getUserId( ) );
        assertEquals( AdminUser.ACTIVE_CODE, user.getRealStatus( ) );
        assertEquals( maxValidDate, user.getAccountMaxValidDate( ) );
    }

    private void setAccountState( int nStatus, Timestamp maxValidDate, Timestamp lastLogin )
    {
        AdminUserHome.updateUserExpirationDate( _user.getUserId( ), maxValidDate );
        AdminUserHome.updateDateLastLogin( _user.getUserId( ), lastLogin );
        AdminUserHome.updateUserStatus( Collections.singletonList( _user.getUserId( ) ), nStatus );
    }

    private static Timestamp daysAgo( int nDays )
    {
        return new Timestamp( System.currentTimeMillis( ) - ( nDays * ONE_DAY_MILLIS ) );
    }

    private static Timestamp monthsAgo( int nMonths )
    {
        Calendar calendar = Calendar.getInstance( );
        calendar.add( Calendar.MONTH, -nMonths );
        return new Timestamp( calendar.getTimeInMillis( ) );
    }

    private static String getDatastoreValue( String strKey )
    {
        return DatastoreService.existsKey( strKey ) ? DatastoreService.getDataValue( strKey, null ) : null;
    }

    private static void restoreDatastoreValue( String strKey, String strValue )
    {
        if ( strValue == null )
        {
            DatastoreService.removeData( strKey );
        }
        else
        {
            DatastoreService.setDataValue( strKey, strValue );
        }
    }
}
