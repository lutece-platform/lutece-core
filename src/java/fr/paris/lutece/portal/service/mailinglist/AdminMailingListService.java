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
package fr.paris.lutece.portal.service.mailinglist;

import fr.paris.lutece.portal.business.mailinglist.MailingList;
import fr.paris.lutece.portal.business.mailinglist.MailingListFilter;
import fr.paris.lutece.portal.business.mailinglist.MailingListHome;
import fr.paris.lutece.portal.business.mailinglist.MailingListUsersFilter;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * AdminMailingListService
 */
public final class AdminMailingListService
{
    public static final String ALL_ROLES = "*";

    /**
     * Private COnstructor
     */
    private AdminMailingListService(  )
    {
    }

    /**
     * Initialize
     */
    public static void init(  )
    {
        // Initialize mailing list
        MailingList.init(  );
    }

    /**
     * Returns a list of all mailing list visible by the user
     * @param user The user
     * @return The list as a ReferenceList
     */
    public static ReferenceList getMailingLists( AdminUser user )
    {
        ReferenceList list = new ReferenceList(  );

        for ( MailingList mailinglist : getUserMailingLists( user ) )
        {
            list.addItem( mailinglist.getId(  ), mailinglist.getName(  ) );
        }

        return list;
    }

    /**
     * Returns a list of all mailing list visible by the user
     * @param user The user
     * @return The list as a mailinglist Collection
     */
    public static Collection<MailingList> getUserMailingLists( AdminUser user )
    {
        Collection<MailingList> listMailinglists = new ArrayList<MailingList>(  );

        // Add all global mailing lists
        listMailinglists.addAll( MailingListHome.findByWorkgroup( AdminWorkgroupService.ALL_GROUPS ) );

        // Add mailing list of the user's workgroups
        ReferenceList listWorkgroups = AdminWorkgroupHome.getUserWorkgroups( user );

        for ( ReferenceItem workgroup : listWorkgroups )
        {
            listMailinglists.addAll( MailingListHome.findByWorkgroup( workgroup.getCode(  ) ) );
        }

        return listMailinglists;
    }

    /**
     * Gets the user mailing lists by filter.
     *
     * @param user the user
     * @param filter the filter
     * @return the user mailing lists by filter
     */
    public static List<MailingList> getUserMailingListsByFilter( AdminUser user, MailingListFilter filter )
    {
        MailingListFilter mailingListFilter = new MailingListFilter( filter );
        // First get mailinglist by workgroup 'all'
        mailingListFilter.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );

        List<MailingList> listMailingLists = MailingListHome.findsByFilter( mailingListFilter );

        // Add mailing list of the user's workgroups
        ReferenceList listWorkgroups = AdminWorkgroupHome.getUserWorkgroups( user );

        for ( ReferenceItem workgroup : listWorkgroups )
        {
            mailingListFilter = new MailingListFilter( filter );
            mailingListFilter.setWorkgroup( workgroup.getCode(  ) );
            listMailingLists.addAll( MailingListHome.findsByFilter( mailingListFilter ) );
        }

        return listMailingLists;
    }

    /**
     * Returns all the recipient of a given mailing list
     * @param nIdMailingList The mailing list Id
     * @return The list
     */
    public static Collection<Recipient> getRecipients( int nIdMailingList )
    {
        Collection<Recipient> listRecipients = new ArrayList<Recipient>(  );
        MailingList mailinglist = MailingListHome.findByPrimaryKey( nIdMailingList );

        if ( mailinglist != null )
        {
            for ( MailingListUsersFilter filter : mailinglist.getFilters(  ) )
            {
                listRecipients.addAll( getRecipients( filter.getWorkgroup(  ), filter.getRole(  ) ) );
            }
        }

        return listRecipients;
    }

    /**
     * Gets all recipients corresponding to a filter based on a Workgroup and a role
     * @param strWorkgroup The workgroup
     * @param strRole The role
     * @return A collection of recipient
     */
    public static Collection<Recipient> getRecipients( String strWorkgroup, String strRole )
    {
        Collection<Recipient> listRecipients = new ArrayList<Recipient>(  );
        Collection<AdminUser> listUsers;

        if ( ( strWorkgroup != null ) && ( !strWorkgroup.equals( AdminWorkgroupService.ALL_GROUPS ) ) )
        {
            listUsers = AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroup );
        }
        else
        {
            listUsers = AdminUserHome.findUserList(  );
        }

        for ( AdminUser user : listUsers )
        {
            if ( ( strRole != null ) && ( !strRole.equals( ALL_ROLES ) ) )
            {
                if ( !user.isInRole( strRole ) )
                {
                    // skip this user if it isn't in the role
                    continue;
                }
            }

            Recipient recipient = new Recipient(  );
            recipient.setName( user.getFirstName(  ) + " " + user.getLastName(  ) );
            recipient.setEmail( user.getEmail(  ) );
            listRecipients.add( recipient );
        }

        return listRecipients;
    }

    /**
     * Check if the filter already exists or not in a mailing list
     * @param filter the filter
     * @param nIdMailingList the id mailing list
     * @return true if it already exists, false otherwise
     */
    public static boolean checkFilter( MailingListUsersFilter filter, int nIdMailingList )
    {
        return MailingListHome.checkFilter( filter, nIdMailingList );
    }
}
