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
package fr.paris.lutece.portal.service.mailinglist;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.mailinglist.MailingList;
import fr.paris.lutece.portal.business.mailinglist.MailingListHome;
import fr.paris.lutece.portal.business.mailinglist.MailingListUsersFilter;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroup;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Recipients of the admin mailing lists
 */
public class AdminMailingListServiceTest extends LuteceTestCase
{
    private String _strName;
    private final List<AdminUser> _listUsers = new ArrayList<>( );
    private final List<String> _listWorkgroups = new ArrayList<>( );
    private MailingList _mailingList;

    @BeforeEach
    public void createMailingList( )
    {
        _strName = "junit" + Math.abs( new SecureRandom( ).nextInt( ) );
        _mailingList = new MailingList( );
        _mailingList.setName( _strName );
        _mailingList.setDescription( _strName );
        _mailingList.setWorkgroup( "all" );
        MailingListHome.create( _mailingList );
    }

    @AfterEach
    public void removeAll( )
    {
        MailingListHome.remove( _mailingList.getId( ) );

        for ( String strWorkgroup : _listWorkgroups )
        {
            AdminWorkgroupHome.removeAllUsersForWorkgroup( strWorkgroup );
            AdminWorkgroupHome.remove( strWorkgroup );
        }

        for ( AdminUser user : _listUsers )
        {
            AdminUserHome.remove( user.getUserId( ) );
        }
    }

    /**
     * A user matched by two filters of a mailing list is one recipient
     */
    @Test
    public void testUserMatchedByTwoFiltersIsOneRecipient( )
    {
        AdminUser user = createUser( "both", AdminUser.ACTIVE_CODE );
        String strWorkgroup1 = createWorkgroup( "1", user );
        String strWorkgroup2 = createWorkgroup( "2", user );
        addFilter( strWorkgroup1 );
        addFilter( strWorkgroup2 );

        Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( _mailingList.getId( ) );

        assertEquals( 1, listRecipients.stream( ).filter( r -> user.getEmail( ).equals( r.getEmail( ) ) ).count( ) );
        assertEquals( 1, listRecipients.size( ) );
    }

    /**
     * The disabled, expired and anonymized accounts are not recipients, whatever the filter
     */
    @Test
    public void testOnlyActiveAccountsAreRecipients( )
    {
        AdminUser active = createUser( "on", AdminUser.ACTIVE_CODE );
        AdminUser disabled = createUser( "off", AdminUser.NOT_ACTIVE_CODE );
        AdminUser expired = createUser( "exp", AdminUser.EXPIRED_CODE );
        AdminUser anonymized = createUser( "ano", AdminUser.ANONYMIZED_CODE );
        String strWorkgroup = createWorkgroup( "w", active, disabled, expired, anonymized );
        addFilter( strWorkgroup );

        List<String> listEmails = AdminMailingListService.getRecipients( _mailingList.getId( ) ).stream( ).map( Recipient::getEmail ).toList( );
        assertEquals( List.of( active.getEmail( ) ), listEmails );

        List<String> listAllEmails = AdminMailingListService.getRecipients( "all", AdminMailingListService.ALL_ROLES ).stream( ).map( Recipient::getEmail )
                .toList( );
        assertTrue( listAllEmails.contains( active.getEmail( ) ) );
        assertFalse( listAllEmails.contains( disabled.getEmail( ) ) );
        assertFalse( listAllEmails.contains( expired.getEmail( ) ) );
        assertFalse( listAllEmails.contains( anonymized.getEmail( ) ) );
    }

    private AdminUser createUser( String strSuffix, int nStatus )
    {
        AdminUser user = new AdminUser( );
        String strAccessCode = _strName + strSuffix;
        user.setAccessCode( strAccessCode );
        user.setLastName( strAccessCode );
        user.setFirstName( strAccessCode );
        user.setEmail( strAccessCode + "@lutece.fr" );
        user.setStatus( nStatus );
        AdminUserHome.create( user );
        _listUsers.add( user );

        return user;
    }

    private String createWorkgroup( String strSuffix, AdminUser... users )
    {
        AdminWorkgroup workgroup = new AdminWorkgroup( );
        workgroup.setKey( _strName + strSuffix );
        workgroup.setDescription( _strName );
        AdminWorkgroupHome.create( workgroup );
        _listWorkgroups.add( workgroup.getKey( ) );

        for ( AdminUser user : users )
        {
            AdminWorkgroupHome.addUserForWorkgroup( user, workgroup.getKey( ) );
        }

        return workgroup.getKey( );
    }

    private void addFilter( String strWorkgroup )
    {
        MailingListUsersFilter filter = new MailingListUsersFilter( );
        filter.setWorkgroup( strWorkgroup );
        filter.setRole( AdminMailingListService.ALL_ROLES );
        MailingListHome.addFilterToMailingList( filter, _mailingList.getId( ) );
    }
}
