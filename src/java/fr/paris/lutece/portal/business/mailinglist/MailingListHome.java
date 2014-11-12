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
package fr.paris.lutece.portal.business.mailinglist;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for MailingList objects
 */
public final class MailingListHome
{
    // Static variable pointed at the DAO instance
    private static IMailingListDAO _dao = (IMailingListDAO) SpringContextService.getBean( "mailingListDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private MailingListHome(  )
    {
    }

    /**
     * Creation of an instance of mailingList
     *
     * @param mailingList The instance of the MailingList which contains the informations to store
     * @return The  instance of mailingList which has been created with its primary key.
     */
    public static MailingList create( MailingList mailingList )
    {
        _dao.insert( mailingList );

        return mailingList;
    }

    /**
     * Update of the mailingList which is specified in parameter
     *
     * @param mailingList The instance of the MailingList which contains the data to store
     * @return The instance of the  mailingList which has been updated
     */
    public static MailingList update( MailingList mailingList )
    {
        _dao.store( mailingList );

        return mailingList;
    }

    /**
     * Remove the mailingList whose identifier is specified in parameter
     *
     * @param nMailingListId The mailingList Id
     */
    public static void remove( int nMailingListId )
    {
        _dao.delete( nMailingListId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a mailingList whose identifier is specified in parameter
     *
     * @param nKey The mailingList primary key
     * @return an instance of MailingList
     */
    public static MailingList findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Loads the data of all the mailingLists and returns them in form of a collection
     *
     * @return the collection which contains the data of all the mailingLists
     */
    public static Collection<MailingList> findAll(  )
    {
        return _dao.selectAll(  );
    }

    /**
     * Find all mailing lists visible by a workgroup
     * @param strWorkgroup The workgroup
     * @return A mailing list collection
     */
    public static Collection<MailingList> findByWorkgroup( String strWorkgroup )
    {
        return _dao.selectByWorkgroup( strWorkgroup );
    }

    /**
     * Add an new user filter to a mailing list
     * @param filter The filter to add
     * @param nId The Id of the mailing list
     */
    public static void addFilterToMailingList( MailingListUsersFilter filter, int nId )
    {
        _dao.insertFilter( filter, nId );
    }

    /**
     * Remove an user filter from a mailing list
     * @param filter The filter to remove
     * @param nId The Id of the mailing list
     */
    public static void deleteFilterToMailingList( MailingListUsersFilter filter, int nId )
    {
        _dao.deleteFilter( filter, nId );
    }

    /**
     * Check if the filter already exists or not in a mailing list
     * @param filter the filter
     * @param nId the id mailing list
     * @return true if it already exists, false otherwise
     */
    public static boolean checkFilter( MailingListUsersFilter filter, int nId )
    {
        return _dao.checkFilter( filter, nId );
    }

    /**
     * Find by filter.
     *
     * @param filter the filter
     * @return the list
     */
    public static List<MailingList> findsByFilter( MailingListFilter filter )
    {
        return _dao.selectByFilter( filter );
    }
}
