/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.business.workgroup;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for AdminWorkgroup objects
 */
public final class AdminWorkgroupDAO implements IAdminWorkgroupDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT workgroup_key, workgroup_description FROM core_admin_workgroup WHERE workgroup_key = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_admin_workgroup ( workgroup_key, workgroup_description ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_admin_workgroup WHERE workgroup_key = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_admin_workgroup SET workgroup_description = ? WHERE workgroup_key = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT workgroup_key, workgroup_description FROM core_admin_workgroup ORDER BY workgroup_key";
    private static final String SQL_QUERY_SELECT_USER_WORKGROUP = " SELECT workgroup_key FROM core_admin_workgroup_user WHERE id_user = ? AND workgroup_key = ? ";
    private static final String SQL_QUERY_USER_IN_WORKGROUP = " SELECT id_user FROM core_admin_workgroup_user WHERE id_user = ? ";
    private static final String SQL_QUERY_SELECT_USER_WORKGROUPS = " SELECT a.workgroup_key, a.workgroup_description " +
        " FROM core_admin_workgroup a, core_admin_workgroup_user b " +
        " WHERE a.workgroup_key = b.workgroup_key AND b.id_user = ?  ";
    private static final String SQL_QUERY_SELECT_USERS_LIST_FOR_WORKGROUP = " SELECT b.id_user " +
        " FROM core_admin_workgroup a, core_admin_workgroup_user b " +
        " WHERE a.workgroup_key = b.workgroup_key AND a.workgroup_key = ?";
    private static final String SQL_QUERY_DELETE_ALL_USERS_WORKGROUP = " DELETE FROM core_admin_workgroup_user WHERE workgroup_key = ?  ";
    private static final String SQL_QUERY_INSERT_USER_WORKGROUP = " INSERT INTO core_admin_workgroup_user ( workgroup_key, id_user ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_USER_FROM_WORKGROUP = " DELETE FROM core_admin_workgroup_user WHERE workgroup_key = ?  AND id_user = ?";

    /**
     * Insert a new record in the table.
     *
     * @param workgroup The workgroup object
     */
    public void insert( AdminWorkgroup workgroup )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setString( 1, workgroup.getKey(  ) );
        daoUtil.setString( 2, workgroup.getDescription(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of AdminWorkgroup from the table
     *
     * @param nWorkgroupId The identifier of AdminWorkgroup
     * @return the instance of the AdminWorkgroup
     */
    public AdminWorkgroup load( String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.executeQuery(  );

        AdminWorkgroup workgroup = null;

        if ( daoUtil.next(  ) )
        {
            workgroup = new AdminWorkgroup(  );
            workgroup.setKey( daoUtil.getString( 1 ) );
            workgroup.setDescription( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return workgroup;
    }

    /**
     * Delete a record from the table
     * @param workgroup The AdminWorkgroup object
     */
    public void delete( String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strWorkgroupKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record identified by the given workgroup key with the given workgroup in the table
     * @param workgroup The reference of workgroup to be the new one
     */
    public void store( AdminWorkgroup workgroup )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( 1, workgroup.getDescription(  ) );
        daoUtil.setString( 2, workgroup.getKey(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of workgroups
     * @return The Collection of the Workgroups
     */
    public Collection<AdminWorkgroup> selectWorkgroupList(  )
    {
        Collection<AdminWorkgroup> listWorkgroups = new ArrayList<AdminWorkgroup>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminWorkgroup workgroup = new AdminWorkgroup(  );
            workgroup.setKey( daoUtil.getString( 1 ) );
            workgroup.setDescription( daoUtil.getString( 2 ) );

            listWorkgroups.add( workgroup );
        }

        daoUtil.free(  );

        return listWorkgroups;
    }

    /**
     * Check that the given key points to an existing workgroup
     * @param strNewWorkgroupKey
     * @return true if the workgroup exists, false otherwise
     */
    public boolean checkExistWorkgroup( String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }
        else
        {
            daoUtil.free(  );

            return false;
        }
    }

    /**
     * Is the user member of the workgroup
     * @param nIdUser The user Id
     * @param strWorkgroup The workgroup key
     */
    public boolean isUserInWorkgroup( int nIdUser, String strWorkgroupKey )
    {
        boolean bInWorkgroup = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_WORKGROUP );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.setString( 2, strWorkgroupKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bInWorkgroup = true;
        }

        daoUtil.free(  );

        return bInWorkgroup;
    }

    /**
     * Is the user member of the workgroup
     * @param nIdUser The user Id
     */
    public boolean checkUserHasWorkgroup( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_USER_IN_WORKGROUP );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }
        else
        {
            daoUtil.free(  );

            return false;
        }
    }

    /**
     * Returns the list of all workgroups the user is member
     * @param nIdUser The user Id
     */
    public ReferenceList getUserWorkgroups( int nIdUser )
    {
        ReferenceList listWorkgroups = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_WORKGROUPS );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listWorkgroups.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return listWorkgroups;
    }

    public Collection<AdminUser> getUsersListForWorkgroup( String strWorkgroupKey )
    {
        Collection<AdminUser> listUsers = new ArrayList<AdminUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_LIST_FOR_WORKGROUP );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.executeQuery(  );

        AdminUser adminUser = null;

        while ( daoUtil.next(  ) )
        {
            adminUser = AdminUserHome.findByPrimaryKey( daoUtil.getInt( 1 ) );

            if ( adminUser != null )
            {
                listUsers.add( adminUser );
            }
        }

        daoUtil.free(  );

        return listUsers;
    }

    /**
     * @param nUserId the user id
     */
    public void deleteAllUsersForWorkgroup( String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_USERS_WORKGROUP );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param nUserId the user id
     * @param strWorkgroupKey the key workgroup
     */
    public void insertUserForWorkgroup( AdminUser user, String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_USER_WORKGROUP );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.setInt( 2, user.getUserId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param nUserId the user id
     * @param strWorkgroupKey the key workgroup
     */
    public void deleteUserFromWorkgroup( int nUserId, String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_USER_FROM_WORKGROUP );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.setInt( 2, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
