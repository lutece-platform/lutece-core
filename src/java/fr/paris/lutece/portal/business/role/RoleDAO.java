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
package fr.paris.lutece.portal.business.role;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Role objects
 */
public final class RoleDAO implements IRoleDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT role, role_description, workgroup_key FROM core_role WHERE role = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT role , role_description, workgroup_key FROM core_role ORDER BY role";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_role ( role, role_description, workgroup_key ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_role WHERE role = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_role SET role_description = ?, workgroup_key = ? WHERE role = ?";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     * @param role The Instance of the object Role
     */
    public void insert( Role role )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setString( 1, role.getRole(  ) );
        daoUtil.setString( 2, role.getRoleDescription(  ) );
        daoUtil.setString( 3, role.getWorkgroup(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of Role from the table
     * @param strRole The indentifier of the object Role
     * @return The Instance of the object Role
     */
    public Role load( String strRole )
    {
        Role role = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strRole );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            role = new Role(  );
            role.setRole( daoUtil.getString( 1 ) );
            role.setRoleDescription( daoUtil.getString( 2 ) );
            role.setWorkgroup( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return role;
    }

    /**
     * Delete a record from the table
     * @param strRole The indentifier of the object Role
     */
    public void delete( String strRole )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strRole );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param role The instance of the Role to update
     */
    public void store( Role role )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, role.getRoleDescription(  ) );
        daoUtil.setString( 2, role.getWorkgroup(  ) );
        daoUtil.setString( 3, role.getRole(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a list of all the right role
     * @return A ReferenceList of role objects
     */
    public ReferenceList selectRolesList(  )
    {
        ReferenceList roleList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Role role = new Role(  );
            role.setRole( daoUtil.getString( 1 ) );
            role.setRoleDescription( daoUtil.getString( 2 ) );

            roleList.addItem( role.getRole(  ), role.getRoleDescription(  ) );
        }

        daoUtil.free(  );

        return roleList;
    }

    /**
     * Load the list of roles
     * @return The Collection of the Roles
     */
    public Collection<Role> selectAll(  )
    {
        Collection<Role> listRoles = new ArrayList<Role>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Role role = new Role(  );
            role.setRole( daoUtil.getString( 1 ) );
            role.setRoleDescription( daoUtil.getString( 2 ) );
            role.setWorkgroup( daoUtil.getString( 3 ) );

            listRoles.add( role );
        }

        daoUtil.free(  );

        return listRoles;
    }
}
