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
package fr.paris.lutece.portal.business.group;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Group objects
 */
public final class GroupRoleDAO implements IGroupRoleDAO
{
    public static final String SQL_QUERY_FIND_ROLES_FROM_GROUP_ID = "SELECT role_key FROM core_group_role WHERE group_key like ? ";
    public static final String SQL_QUERY_FIND_GROUPS_FROM_ROLE_ID = "SELECT group_key FROM core_group_role WHERE role_key = ? ";
    private static final String SQL_QUERY_DELETE_ROLES_FOR_GROUP = "DELETE FROM core_group_role WHERE group_key like ?";
    private static final String SQL_QUERY_INSERT_ROLE_FOR_GROUP = "INSERT INTO core_group_role ( group_key, role_key ) VALUES ( ?, ? ) ";

    /** This class implements the Singleton design pattern. */
    private static GroupRoleDAO _dao = new GroupRoleDAO(  );

    /**
     * Returns the unique instance of the singleton.
     *
     * @return the instance
     */
    static GroupRoleDAO getInstance(  )
    {
        return _dao;
    }

    /**
     * Find group's roles
     *
     * @param strGroupKey the group key
     * @return ArrayList the roles key list corresponding to the group
     */
    public List<String> selectGroupRoles( String strGroupKey )
    {
        int nParam = 0;
        ArrayList<String> arrayRoles = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ROLES_FROM_GROUP_ID );
        daoUtil.setString( ++nParam, strGroupKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nParam = 0;
            arrayRoles.add( daoUtil.getString( ++nParam ) );
        }

        daoUtil.free(  );

        return arrayRoles;
    }

    /**
     * Find group's roles
     *
     * @param strRoleKey The Role key
     * @return ArrayList the groups key list corresponding to the role
     */
    public List<String> selectGroupRolesByRoleKey( String strRoleKey )
    {
        int nParam = 0;
        ArrayList<String> arrayGroup = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_GROUPS_FROM_ROLE_ID );
        daoUtil.setString( ++nParam, strRoleKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nParam = 0;
            arrayGroup.add( daoUtil.getString( ++nParam ) );
        }

        daoUtil.free(  );

        return arrayGroup;
    }

    /**
     * Delete roles for a group
     * @param strGroupKey The key of the group
     */
    public void deleteRoles( String strGroupKey )
    {
        int nParam = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ROLES_FOR_GROUP );
        daoUtil.setString( ++nParam, strGroupKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Assign a role to group
     * @param strGroupKey The key of the group
     * @param strRoleKey The key of the role
     */
    public void createRole( String strGroupKey, String strRoleKey )
    {
        int nParam = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ROLE_FOR_GROUP );
        daoUtil.setString( ++nParam, strGroupKey );
        daoUtil.setString( ++nParam, strRoleKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
