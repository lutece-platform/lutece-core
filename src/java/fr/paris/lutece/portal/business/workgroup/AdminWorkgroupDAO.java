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
    private static final String CONSTANT_PERCENT = "%";
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
    private static final String SQL_QUERY_SELECT_WORKGROUP_FROM_SEARCH = " SELECT workgroup_key, workgroup_description FROM core_admin_workgroup " +
        " WHERE workgroup_key LIKE ? AND workgroup_description LIKE ? ORDER BY workgroup_key ";

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void delete( String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strWorkgroupKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
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

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
     */
    public void deleteAllUsersForWorkgroup( String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_USERS_WORKGROUP );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    public void deleteUserFromWorkgroup( int nUserId, String strWorkgroupKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_USER_FROM_WORKGROUP );
        daoUtil.setString( 1, strWorkgroupKey );
        daoUtil.setInt( 2, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Find workgroups from a filter
     * @param awFilter the filter
     * @return the list of workgroups
     */
    public Collection<AdminWorkgroup> selectWorkgroupsByFilter( AdminWorkgroupFilter awFilter )
    {
        Collection<AdminWorkgroup> listFilteredWorkgroups = new ArrayList<AdminWorkgroup>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_WORKGROUP_FROM_SEARCH );

        daoUtil.setString( 1, CONSTANT_PERCENT + awFilter.getKey(  ) + CONSTANT_PERCENT );
        daoUtil.setString( 2, CONSTANT_PERCENT + awFilter.getDescription(  ) + CONSTANT_PERCENT );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminWorkgroup workgroup = new AdminWorkgroup(  );
            workgroup.setKey( daoUtil.getString( 1 ) );
            workgroup.setDescription( daoUtil.getString( 2 ) );

            listFilteredWorkgroups.add( workgroup );
        }

        daoUtil.free(  );

        return listFilteredWorkgroups;
    }
}
