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
package fr.paris.lutece.portal.service.admin;

import java.security.SecureRandom;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;
import fr.paris.lutece.portal.service.template.DatabaseTemplateService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Renewal of the account life time at login and its « account reactivated » mail
 */
public class AdminUserServiceExpirationTest extends LuteceTestCase
{
    /**
     * Account life time disabled: the default body needs date_valid, no mail is sent and the login is not broken
     */
    @Test
    public void testNoLifeTimeDoesNotBreakTheRenewal( )
    {
        String strLifeTime = DefaultUserParameterHome.findByKey( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME );
        AdminUser user = createUserInAlertWindow( );
        try
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, "0" );

            AdminUserService.updateUserExpirationDate( AdminUserHome.findByPrimaryKey( user.getUserId( ) ) );

            assertNull( AdminUserHome.findByPrimaryKey( user.getUserId( ) ).getAccountMaxValidDate( ) );
        }
        finally
        {
            DefaultUserParameterHome.update( AdminUserService.DSKEY_ACCOUNT_LIFE_TIME, strLifeTime );
            AdminUserHome.remove( user.getUserId( ) );
        }
    }

    /**
     * The markers offered by the screen of the mail (last_name) are given, and a body that cannot be rendered does not break the renewal
     */
    @Test
    public void testReactivatedMailMarkers( )
    {
        String strBody = DatabaseTemplateService.getTemplateFromKey( AdminUserService.DSKEY_ACCOUNT_REACTIVATED_MAIL_BODY );
        try
        {
            for ( String strNewBody : new String [ ] {
                    "${first_name} ${last_name} ${date_valid}", "${unknown_marker}"
            } )
            {
                DatabaseTemplateService.updateTemplate( AdminUserService.DSKEY_ACCOUNT_REACTIVATED_MAIL_BODY, strNewBody );
                AdminUser user = createUserInAlertWindow( );
                try
                {
                    AdminUserService.updateUserExpirationDate( AdminUserHome.findByPrimaryKey( user.getUserId( ) ) );

                    Timestamp date = AdminUserHome.findByPrimaryKey( user.getUserId( ) ).getAccountMaxValidDate( );
                    assertTrue( date.getTime( ) > System.currentTimeMillis( ), "account renewed" );
                }
                finally
                {
                    AdminUserHome.remove( user.getUserId( ) );
                }
            }
        }
        finally
        {
            DatabaseTemplateService.updateTemplate( AdminUserService.DSKEY_ACCOUNT_REACTIVATED_MAIL_BODY, strBody );
        }
    }

    private static AdminUser createUserInAlertWindow( )
    {
        String strName = "user" + new SecureRandom( ).nextLong( );
        AdminUser user = new AdminUser( );
        user.setAccessCode( strName );
        user.setLastName( strName );
        user.setFirstName( strName );
        user.setEmail( strName + "@lutece.fr" );
        user.setStatus( AdminUser.ACTIVE_CODE );
        AdminUserHome.create( user );
        // expires in 5 days: inside the alert window
        AdminUserHome.updateUserExpirationDate( user.getUserId( ), new Timestamp( System.currentTimeMillis( ) + 5L * 24 * 3600 * 1000 ) );
        return user;
    }
}
