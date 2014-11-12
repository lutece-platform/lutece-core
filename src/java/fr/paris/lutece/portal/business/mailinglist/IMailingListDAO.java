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

import java.util.Collection;
import java.util.List;


/**
 * IMailingListDAO Interface
 */
public interface IMailingListDAO
{
    /**
     * Insert a new record in the table.
     * @param mailingList instance of the MailingList object to inssert
     */
    void insert( MailingList mailingList );

    /**
     * Update the record in the table
     *
     * @param mailingList the reference of the MailingList
     */
    void store( MailingList mailingList );

    /**
     * Delete a record from the table
     *
     * @param nIdMailingList int identifier of the MailingList to delete
     */
    void delete( int nIdMailingList );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * load the data of the right from the table
     *
     * @param nKey The identifier of the mailingList
     * @return The instance of the mailingList
     */
    MailingList load( int nKey );

    /**
     * Loads the data of all the mailingLists and returns them in form of a collection
     *
     * @return the collection which contains the data of all the mailingLists
     */
    Collection<MailingList> selectAll(  );

    /**
     * Returns all mailing lists having a scope restricted to a given workgroup
     *
     * @param strWorkgroup The workgroup
     * @return the collection which contains the data of all the mailingLists
     */
    Collection<MailingList> selectByWorkgroup( String strWorkgroup );

    /**
     * Add an users filter to the mailing list
     * @param filter the filter to add
     * @param nId The Id of the mailing list
     */
    void insertFilter( MailingListUsersFilter filter, int nId );

    /**
     * Remove an users filter from the mailing list
     * @param filter the filter to remove
     * @param nId The Id of the mailing list
     */
    void deleteFilter( MailingListUsersFilter filter, int nId );

    /**
     * Check if the filter already exists or not in the mailing list
     * @param filter the filter
     * @param nId the id mailing list
     * @return true if it already exists, false otherwise
     */
    boolean checkFilter( MailingListUsersFilter filter, int nId );

    /**
     * Select by filter.
     *
     * @param filter the filter
     * @return the list
     */
    List<MailingList> selectByFilter( MailingListFilter filter );
}
