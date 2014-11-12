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

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for MailingList objects
 */
public final class MailingListDAO implements IMailingListDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_mailinglist ) FROM core_admin_mailinglist";
    private static final String SQL_QUERY_SELECT = "SELECT id_mailinglist, name, description, workgroup FROM core_admin_mailinglist WHERE id_mailinglist = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_admin_mailinglist ( id_mailinglist, name, description, workgroup ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_admin_mailinglist WHERE id_mailinglist = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_admin_mailinglist SET id_mailinglist = ?, name = ?, description = ?, workgroup = ? WHERE id_mailinglist = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_mailinglist, name, description, workgroup FROM core_admin_mailinglist";
    private static final String SQL_QUERY_SELECT_BY_WORKGROUP = "SELECT id_mailinglist, name, description, workgroup FROM core_admin_mailinglist WHERE workgroup = ? ";

    // filters
    private static final String SQL_QUERY_FILTERS_INSERT = "INSERT INTO core_admin_mailinglist_filter ( id_mailinglist, workgroup, role ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_FILTERS_DELETE = "DELETE FROM core_admin_mailinglist_filter WHERE id_mailinglist = ? ";
    private static final String SQL_QUERY_FILTERS_DELETE_FILTER = "DELETE FROM core_admin_mailinglist_filter WHERE id_mailinglist = ? AND workgroup = ? AND role = ? ";
    private static final String SQL_QUERY_FILTERS_SELECTALL = "SELECT id_mailinglist, workgroup, role FROM core_admin_mailinglist_filter WHERE id_mailinglist = ?";
    private static final String SQL_QUERY_FILTERS_SELECT = "SELECT id_mailinglist, workgroup, role FROM core_admin_mailinglist_filter WHERE id_mailinglist = ? AND workgroup = ? AND role = ?";

    /**
     * Generates a new primary key
     *
     * @return The new primary key
     */
    public int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     *
     * @param mailingList instance of the MailingList object to insert
     */
    @Override
    public synchronized void insert( MailingList mailingList )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        mailingList.setId( newPrimaryKey(  ) );

        daoUtil.setInt( 1, mailingList.getId(  ) );
        daoUtil.setString( 2, mailingList.getName(  ) );
        daoUtil.setString( 3, mailingList.getDescription(  ) );
        daoUtil.setString( 4, mailingList.getWorkgroup(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the mailingList from the table
     *
     * @param nId The identifier of the mailingList
     * @return the instance of the MailingList
     */
    @Override
    public MailingList load( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        MailingList mailinglist = null;

        if ( daoUtil.next(  ) )
        {
            mailinglist = new MailingList(  );

            mailinglist.setId( daoUtil.getInt( 1 ) );
            mailinglist.setName( daoUtil.getString( 2 ) );
            mailinglist.setDescription( daoUtil.getString( 3 ) );
            mailinglist.setWorkgroup( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        // load filters
        selectMailingListUsersFilters( mailinglist );

        return mailinglist;
    }

    /**
     * Delete a record from the table
     *
     * @param nMailingListId The identifier of the mailingList
     */
    @Override
    public void delete( int nMailingListId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nMailingListId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // delete filters
        deleteFilters( nMailingListId );
    }

    /**
     * Update the record in the table
     *
     * @param mailingList The reference of the mailingList
     */
    @Override
    public void store( MailingList mailingList )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setInt( 1, mailingList.getId(  ) );
        daoUtil.setString( 2, mailingList.getName(  ) );
        daoUtil.setString( 3, mailingList.getDescription(  ) );
        daoUtil.setString( 4, mailingList.getWorkgroup(  ) );
        daoUtil.setInt( 5, mailingList.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the mailingLists and returns them in form of a collection
     *
     * @return The Collection which contains the data of all the mailingLists
     */
    @Override
    public Collection<MailingList> selectAll(  )
    {
        Collection<MailingList> mailingListList = new ArrayList<MailingList>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MailingList mailingList = new MailingList(  );

            mailingList.setId( daoUtil.getInt( 1 ) );
            mailingList.setName( daoUtil.getString( 2 ) );
            mailingList.setDescription( daoUtil.getString( 3 ) );
            mailingList.setWorkgroup( daoUtil.getString( 4 ) );

            mailingListList.add( mailingList );
        }

        daoUtil.free(  );

        return mailingListList;
    }

    /**
     * Returns all mailing lists having a scope restricted to a given workgroup
     *
     * @param strWorkgroup The workgroup
     * @return the collection which contains the data of all the mailingLists
     */
    @Override
    public Collection<MailingList> selectByWorkgroup( String strWorkgroup )
    {
        Collection<MailingList> mailingListList = new ArrayList<MailingList>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_WORKGROUP );
        daoUtil.setString( 1, strWorkgroup );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MailingList mailingList = new MailingList(  );

            mailingList.setId( daoUtil.getInt( 1 ) );
            mailingList.setName( daoUtil.getString( 2 ) );
            mailingList.setDescription( daoUtil.getString( 3 ) );
            mailingList.setWorkgroup( daoUtil.getString( 4 ) );

            mailingListList.add( mailingList );
        }

        daoUtil.free(  );

        return mailingListList;
    }

    /**
     * Insert a new record in the table.
     *
     * @param nMailingListId The mailing list Id
     * @param mailingListUsersFilter instance of the MailingListUsersFilter object to insert
     */
    @Override
    public void insertFilter( MailingListUsersFilter mailingListUsersFilter, int nMailingListId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FILTERS_INSERT );

        daoUtil.setInt( 1, nMailingListId );
        daoUtil.setString( 2, mailingListUsersFilter.getWorkgroup(  ) );
        daoUtil.setString( 3, mailingListUsersFilter.getRole(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Remove an users filter from the mailing list
     * @param nMailingListId The Id of the mailing list
     * @param filter the filter to remove
     */
    @Override
    public void deleteFilter( MailingListUsersFilter filter, int nMailingListId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FILTERS_DELETE_FILTER );
        daoUtil.setInt( 1, nMailingListId );
        daoUtil.setString( 2, filter.getWorkgroup(  ) );
        daoUtil.setString( 3, filter.getRole(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nMailingListUsersFilterId The identifier of the mailingListUsersFilter
     */
    public void deleteFilters( int nMailingListUsersFilterId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FILTERS_DELETE );
        daoUtil.setInt( 1, nMailingListUsersFilterId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the mailingListUsersFilters and returns them in form of a collection
     * @param mailinglist The mailing list
     */
    public void selectMailingListUsersFilters( MailingList mailinglist )
    {
        if ( mailinglist != null )
        {
            Collection<MailingListUsersFilter> mailingListUsersFilterList = mailinglist.getFilters(  );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FILTERS_SELECTALL );
            daoUtil.setInt( 1, mailinglist.getId(  ) );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                MailingListUsersFilter mailingListUsersFilter = new MailingListUsersFilter(  );

                mailingListUsersFilter.setWorkgroup( daoUtil.getString( 2 ) );
                mailingListUsersFilter.setRole( daoUtil.getString( 3 ) );

                mailingListUsersFilterList.add( mailingListUsersFilter );
            }

            daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkFilter( MailingListUsersFilter filter, int nId )
    {
        boolean bExists = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FILTERS_SELECT );
        daoUtil.setInt( 1, nId );
        daoUtil.setString( 2, filter.getWorkgroup(  ) );
        daoUtil.setString( 3, filter.getRole(  ) );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bExists = true;
        }

        daoUtil.free(  );

        return bExists;
    }

    /**
         * {@inheritDoc}
         */
    @Override
    public List<MailingList> selectByFilter( MailingListFilter filter )
    {
        List<MailingList> mailingListList = new ArrayList<MailingList>(  );
        DAOUtil daoUtil = new DAOUtil( filter.buildSQLQuery( SQL_QUERY_SELECTALL ) );
        filter.setFilterValues( daoUtil );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            MailingList mailingList = new MailingList(  );

            mailingList.setId( daoUtil.getInt( nIndex++ ) );
            mailingList.setName( daoUtil.getString( nIndex++ ) );
            mailingList.setDescription( daoUtil.getString( nIndex++ ) );
            mailingList.setWorkgroup( daoUtil.getString( nIndex ) );

            mailingListList.add( mailingList );
        }

        daoUtil.free(  );

        return mailingListList;
    }
}
