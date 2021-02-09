/*
 * Copyright (c) 2002-2021, City of Paris
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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class provides Data Access methods for RBAC objects
 */
public final class RBACDAO implements IRBACDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT rbac_id, role_key, resource_type, resource_id, permission FROM core_admin_role_resource WHERE rbac_id = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_admin_role_resource ( role_key, resource_type, resource_id, permission ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_admin_role_resource WHERE rbac_id = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_admin_role_resource SET rbac_id = ?, role_key = ?, resource_type = ?, resource_id = ?, permission = ? WHERE rbac_id = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT rbac_id, role_key, resource_type, resource_id, permission FROM core_admin_role_resource ";
    private static final String SQL_QUERY_SELECT_BY_ROLE = " SELECT rbac_id, role_key, resource_type, resource_id, permission FROM core_admin_role_resource WHERE role_key = ?  ORDER BY resource_type,resource_id,permission ";
    private static final String SQL_QUERY_UPDATE_ROLES = " UPDATE core_admin_role_resource SET  role_key = ? WHERE role_key = ?  ";
    private static final String SQL_QUERY_DELETE_FOR_ROLE_KEY = " DELETE FROM core_admin_role_resource WHERE role_key = ? ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE_TYPE_AND_RESOURCE_ID = " DELETE FROM core_admin_role_resource WHERE resource_type = ? AND resource_id = ? ";

    // query used to retrieve the roles associeted with a resource
    private static final String SQL_QUERY_SELECT_ROLE_KEYS = " SELECT DISTINCT role_key FROM core_admin_role_resource " + " WHERE resource_type = ? AND "
            + "( resource_id = ? OR resource_id= ? ) AND" + "( permission = ? OR permission= ? )";

    /**
     * Insert a new record in the table.
     *
     * @param rBAC
     *            The rBAC object
     */
    public void insert( RBAC rBAC )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, rBAC.getRoleKey( ) );
            daoUtil.setString( nIndex++, rBAC.getResourceTypeKey( ) );
            daoUtil.setString( nIndex++, rBAC.getResourceId( ) );
            daoUtil.setString( nIndex, rBAC.getPermissionKey( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                rBAC.setRBACId( daoUtil.getGeneratedKeyInt( 1 ) );
            }

        }
    }

    /**
     * Load the data of RBAC from the table
     *
     * @param nRBACId
     *            The identifier of RBAC
     * @return the instance of the RBAC
     */
    public RBAC load( int nRBACId )
    {
        RBAC rBAC = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nRBACId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                rBAC = new RBAC( );
                rBAC.setRBACId( daoUtil.getInt( 1 ) );
                rBAC.setRoleKey( daoUtil.getString( 2 ) );
                rBAC.setResourceTypeKey( daoUtil.getString( 3 ) );
                rBAC.setResourceId( daoUtil.getString( 4 ) );
                rBAC.setPermissionKey( daoUtil.getString( 5 ) );
            }

        }

        return rBAC;
    }

    /**
     * Delete a record from the table
     * 
     * @param nRBACId
     *            The id of RBAC object to delete
     */
    public void delete( int nRBACId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nRBACId );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Update the record in the table
     * 
     * @param rBAC
     *            The reference of rBAC
     */
    public void store( RBAC rBAC )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( 1, rBAC.getRBACId( ) );
            daoUtil.setString( 2, rBAC.getRoleKey( ) );
            daoUtil.setString( 3, rBAC.getResourceTypeKey( ) );
            daoUtil.setString( 4, rBAC.getResourceId( ) );
            daoUtil.setString( 5, rBAC.getPermissionKey( ) );
            daoUtil.setInt( 6, rBAC.getRBACId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Load the list of rBACs
     * 
     * @return The Collection of the RBACs
     */
    public Collection<RBAC> selectRBACList( )
    {
        Collection<RBAC> listRBACs = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                RBAC rBAC = new RBAC( );
                rBAC.setRBACId( daoUtil.getInt( 1 ) );
                rBAC.setRoleKey( daoUtil.getString( 2 ) );
                rBAC.setResourceTypeKey( daoUtil.getString( 3 ) );
                rBAC.setResourceId( daoUtil.getString( 4 ) );
                rBAC.setPermissionKey( daoUtil.getString( 5 ) );

                listRBACs.add( rBAC );
            }

        }

        return listRBACs;
    }

    /**
     * Find all the entries for a given role key
     * 
     * @param strRoleKey
     *            the role key to search for
     * @return A collection of rBACs
     */
    public Collection<RBAC> selectRBACListByRoleKey( String strRoleKey )
    {
        Collection<RBAC> listRBACs = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ROLE ) )
        {
            daoUtil.setString( 1, strRoleKey );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                RBAC rBAC = new RBAC( );
                rBAC.setRBACId( daoUtil.getInt( 1 ) );
                rBAC.setRoleKey( daoUtil.getString( 2 ) );
                rBAC.setResourceTypeKey( daoUtil.getString( 3 ) );
                rBAC.setResourceId( daoUtil.getString( 4 ) );
                rBAC.setPermissionKey( daoUtil.getString( 5 ) );

                listRBACs.add( rBAC );
            }

        }

        return listRBACs;
    }

    /**
     * Update the role key of all the entries of a given role key
     * 
     * @param strOldRoleKey
     *            the role key to update
     * @param strNewRoleKey
     *            the new role key
     */
    public void updateRoleKey( String strOldRoleKey, String strNewRoleKey )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ROLES ) )
        {
            daoUtil.setString( 1, strNewRoleKey );
            daoUtil.setString( 2, strOldRoleKey );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Remove all the entries of the given role key
     * 
     * @param strRoleKey
     *            the role key of the entries to remove
     */
    public void deleteForRoleKey( String strRoleKey )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FOR_ROLE_KEY ) )
        {
            daoUtil.setString( 1, strRoleKey );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * @param strTypeCode
     *            The type code
     * @param strId
     *            the id
     * @param strPermission
     *            th permission
     * @return listRoleKeys
     */
    public Collection<String> selectRoleKeys( String strTypeCode, String strId, String strPermission )
    {
        Collection<String> listRoleKeys = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ROLE_KEYS ) )
        {
            daoUtil.setString( 1, strTypeCode );

            daoUtil.setString( 2, strId );
            daoUtil.setString( 3, RBAC.WILDCARD_RESOURCES_ID );

            daoUtil.setString( 4, strPermission );
            daoUtil.setString( 5, RBAC.WILDCARD_PERMISSIONS_KEY );

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                daoUtil.getString( 1 );
                listRoleKeys.add( daoUtil.getString( 1 ) );
            }

        }

        return listRoleKeys;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteForResourceTypeAndId( String strResourceType, String strResourceId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_RESOURCE_TYPE_AND_RESOURCE_ID ) )
        {
            daoUtil.setString( 1, strResourceType );
            daoUtil.setString( 2, strResourceId );

            daoUtil.executeUpdate( );
        }
    }
}
