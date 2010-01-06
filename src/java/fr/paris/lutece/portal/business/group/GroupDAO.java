/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.business.group;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Group objects
 */
public final class GroupDAO implements IGroupDAO
{
    // Constants
    private static final String SQL_QUERY_SELECTALL = " SELECT group_key, group_description FROM core_group ORDER BY group_key";
    private static final String SQL_QUERY_SELECT_BY_KEY = " SELECT group_key, group_description FROM core_group WHERE group_key = ? ORDER BY group_key";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_group ( group_key, group_description ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_group WHERE group_key like ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_group SET group_key = ?, group_description = ? WHERE group_key like ?";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     * @param group The Instance of the object Group
     */
    public synchronized void insert( Group group )
    {
        int nParam = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setString( ++nParam, group.getGroupKey(  ) );
        daoUtil.setString( ++nParam, group.getGroupDescription(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of Group from the table
     * @param strGroupKey The indentifier of the object Group
     * @return The Instance of the object Group
     */
    public Group load( String strGroupKey )
    {
        int nParam;
        Group group = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_KEY );
        daoUtil.setString( 1, strGroupKey );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nParam = 0;
            group = new Group(  );
            group.setGroupKey( daoUtil.getString( ++nParam ) );
            group.setGroupDescription( daoUtil.getString( ++nParam ) );
        }

        daoUtil.free(  );

        return group;
    }

    /**
     * Delete a record from the table
     * @param strGroupKey The indentifier of the object Group
     */
    public void delete( String strGroupKey )
    {
        int nParam = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( ++nParam, strGroupKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param group The instance of the Group to update
     */
    public void store( Group group )
    {
        int nParam = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( ++nParam, group.getGroupKey(  ) );
        daoUtil.setString( ++nParam, group.getGroupDescription(  ) );
        daoUtil.setString( ++nParam, group.getGroupKey(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a list of all the right group
     * @return A ReferenceList of group objects
     */
    public ReferenceList selectGroupsList(  )
    {
        int nParam;
        ReferenceList groupList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nParam = 0;

            Group group = new Group(  );
            group.setGroupKey( daoUtil.getString( ++nParam ) );
            group.setGroupDescription( daoUtil.getString( ++nParam ) );
            groupList.addItem( group.getGroupKey(  ), group.getGroupDescription(  ) );
        }

        daoUtil.free(  );

        return groupList;
    }

    /**
     * Load the list of groups
     * @return The Collection of the Groups
     */
    public Collection<Group> selectAll(  )
    {
        int nParam;
        Collection<Group> listGroups = new ArrayList<Group>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nParam = 0;

            Group group = new Group(  );
            group.setGroupKey( daoUtil.getString( ++nParam ) );
            group.setGroupDescription( daoUtil.getString( ++nParam ) );

            listGroups.add( group );
        }

        daoUtil.free(  );

        return listGroups;
    }
}
