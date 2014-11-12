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
package fr.paris.lutece.portal.business.rbac;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for AdminRole objects
 */
public final class AdminRoleDAO implements IAdminRoleDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT role_key, role_description FROM core_admin_role WHERE role_key = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_admin_role ( role_key, role_description ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_admin_role WHERE role_key = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_admin_role SET role_key = ?, role_description = ? WHERE role_key = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT role_key, role_description FROM core_admin_role ORDER BY role_key";

    /**
     * Insert a new record in the table.
     *
     * @param role The role object
     */
    public void insert( AdminRole role )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setString( 1, role.getKey(  ) );
        daoUtil.setString( 2, role.getDescription(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of AdminRole from the table
     *
     * @param strRoleKey The identifier of AdminRole
     * @return the instance of the AdminRole
     */
    public AdminRole load( String strRoleKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strRoleKey );
        daoUtil.executeQuery(  );

        AdminRole role = null;

        if ( daoUtil.next(  ) )
        {
            role = new AdminRole(  );
            role.setKey( daoUtil.getString( 1 ) );
            role.setDescription( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return role;
    }

    /**
     * Delete a record from the table
     * @param strRoleKey The role key
     */
    public void delete( String strRoleKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strRoleKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record identified by the given role key with the given role in the table
     * @param strRoleKey the key of the role to modify
     * @param role The reference of role to be the new one
     */
    public void store( String strRoleKey, AdminRole role )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( 1, role.getKey(  ) );
        daoUtil.setString( 2, role.getDescription(  ) );
        daoUtil.setString( 3, strRoleKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of roles
     * @return The Collection of the Roles
     */
    public Collection<AdminRole> selectRoleList(  )
    {
        Collection<AdminRole> listRoles = new ArrayList<AdminRole>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminRole role = new AdminRole(  );
            role.setKey( daoUtil.getString( 1 ) );
            role.setDescription( daoUtil.getString( 2 ) );

            listRoles.add( role );
        }

        daoUtil.free(  );

        return listRoles;
    }

    /**
     * Check that the given key points to an existing role
     * @param strRoleKey the role key
     * @return true if the role exists, false otherwise
     */
    public boolean checkExistRole( String strRoleKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strRoleKey );
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
}
