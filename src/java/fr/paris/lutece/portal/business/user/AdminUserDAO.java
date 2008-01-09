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
package fr.paris.lutece.portal.business.user;

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * This class porvides Data Access methods for AdminUser objects
 */
public class AdminUserDAO implements IAdminUserDAO
{
    // Constants
    private static final String SQL_QUERY_NEWPK = "SELECT max( id_user ) FROM core_admin_user ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_admin_user ( id_user , access_code, last_name , first_name, email, status, locale, level_user )  VALUES ( ? , ? , ? , ? , ? ,? , ?, ? ) ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user , access_code, last_name , first_name, email, status, locale, level_user FROM core_admin_user ORDER BY last_name ";
    private static final String SQL_QUERY_SELECT_USER_FROM_USER_ID = "SELECT id_user , access_code, last_name , first_name, email, status, password, locale, level_user FROM core_admin_user  WHERE id_user = ? ORDER BY last_name";
    private static final String SQL_QUERY_SELECT_USER_FROM_ACCESS_CODE = "SELECT id_user , access_code, last_name , first_name, email, status, locale, level_user FROM core_admin_user  WHERE access_code = ? ";
    private static final String SQL_QUERY_SELECT_RIGHTS_FROM_USER_ID = " SELECT a.id_right , a.name, a.admin_url , a.description , a.plugin_name, a.id_feature_group, a.icon_url, a.level_right " +
        " FROM core_admin_right a , core_user_right b " + " WHERE a.id_right = b.id_right " + " AND b.id_user = ? " +
        " AND a.admin_url <> '' " + " ORDER BY a.id_right ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_admin_user SET access_code = ? , last_name = ? , first_name = ?, email = ?, status = ?, locale = ? WHERE id_user = ?  ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_admin_user WHERE id_user = ? ";
    private static final String SQL_QUERY_INSERT_USER_RIGHT = "INSERT INTO core_user_right ( id_right, id_user )  VALUES ( ? , ? ) ";
    private static final String SQL_QUERY_DELETE_ALL_USER_RIGHTS = "DELETE FROM core_user_right WHERE id_user = ? ";
    private static final String SQL_QUERY_SELECT_ROLES_FROM_USER_ID = " SELECT a.role_key , a.role_description " +
        " FROM core_admin_role a , core_user_role b " + " WHERE a.role_key = b.role_key " +
        " AND b.id_user = ?  ORDER BY a.role_key ";
    private static final String SQL_QUERY_INSERT_USER_ROLE = " INSERT INTO core_user_role ( role_key, id_user )  VALUES ( ? , ? ) ";
    private static final String SQL_QUERY_DELETE_ALL_USER_ROLES = " DELETE FROM core_user_role WHERE id_user = ? ";
    private static final String SQL_CHECK_ROLE_ATTRIBUTED = " SELECT id_user FROM core_user_role WHERE role_key = ?";
    private static final String SQL_CHECK_ACCESS_CODE_IN_USE = " SELECT id_user FROM core_admin_user WHERE access_code = ?";
    private static final String SQL_QUERY_INSERT_DEFAULT_USER = " INSERT INTO core_admin_user ( id_user , access_code, last_name , first_name, email, status, password, locale, level_user )  VALUES ( ? , ? , ? , ? , ? ,? ,? ,?, ? ) ";
    private static final String SQL_QUERY_UPDATE_DEFAULT_USER = " UPDATE core_admin_user SET access_code = ? , last_name = ? , first_name = ?, email = ?, status = ?, password = ?, locale = ?  WHERE id_user = ?  ";
    private static final String SQL_QUERY_SELECT_USERS_ID_BY_ROLES = " SELECT a.id_user , a.access_code, a.last_name , a.first_name, a.email, a.status, a.locale " +
        " FROM core_admin_user a, core_user_role b WHERE a.id_user = b.id_user AND b.role_key = ? ";
    private static final String SQL_QUERY_SELECT_USER_RIGHTS_OWN = " SELECT DISTINCT b.id_right FROM core_admin_right a , core_user_right b WHERE b.id_user = ? and a.id_right = b.id_right and a.level_right >= ?";
    private static final String SQL_QUERY_SELECT_USER_RIGHTS_DELEGATED = " SELECT DISTINCT b.id_right FROM core_admin_right a , core_user_right b WHERE b.id_user = ? and a.id_right = b.id_right and a.level_right < ?";
    private static final String SQL_QUERY_DELETE_USER_RIGHTS = " DELETE FROM core_user_right WHERE id_user = ? and id_right = ?";

    /**
     * @param nUserId th user id
     * @return user
     */
    public AdminUser load( int nUserId )
    {
        AdminUser user = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_FROM_USER_ID );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 1 ) );
            user.setAccessCode( daoUtil.getString( 2 ) );
            user.setLastName( daoUtil.getString( 3 ) );
            user.setFirstName( daoUtil.getString( 4 ) );
            user.setEmail( daoUtil.getString( 5 ) );
            user.setStatus( daoUtil.getInt( 6 ) );
            user.setLocale( new Locale( daoUtil.getString( 8 ) ) );
            user.setUserLevel( daoUtil.getInt( 9 ) );
        }

        daoUtil.free(  );

        return user;
    }

    /**
     * @param strUserAccessCode th elogin
     * @return user
     */
    public AdminUser selectUserByAccessCode( String strUserAccessCode )
    {
        AdminUser user = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_FROM_ACCESS_CODE );
        daoUtil.setString( 1, strUserAccessCode );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 1 ) );
            user.setAccessCode( daoUtil.getString( 2 ) );
            user.setLastName( daoUtil.getString( 3 ) );
            user.setFirstName( daoUtil.getString( 4 ) );
            user.setEmail( daoUtil.getString( 5 ) );
            user.setStatus( daoUtil.getInt( 6 ) );
            user.setLocale( new Locale( daoUtil.getString( 7 ) ) );
            user.setUserLevel( daoUtil.getInt( 8 ) );
        }

        daoUtil.free(  );

        return user;
    }

    /**
     * @return userList
     */
    public Collection<AdminUser> selectUserList(  )
    {
        Collection<AdminUser> userList = new ArrayList<AdminUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminUser user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 1 ) );
            user.setAccessCode( daoUtil.getString( 2 ) );
            user.setLastName( daoUtil.getString( 3 ) );
            user.setFirstName( daoUtil.getString( 4 ) );
            user.setEmail( daoUtil.getString( 5 ) );
            user.setStatus( daoUtil.getInt( 6 ) );
            user.setLocale( new Locale( daoUtil.getString( 7 ) ) );
            user.setUserLevel( daoUtil.getInt( 8 ) );
            userList.add( user );
        }

        daoUtil.free(  );

        return userList;
    }

    /**
     * Generates a new primary key
         * @return nKey
     */
    public int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEWPK );
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
     * @param user The AdminUser
     */
    public void insert( AdminUser user )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        user.setUserId( newPrimaryKey(  ) );
        daoUtil.setInt( 1, user.getUserId(  ) );
        daoUtil.setString( 2, user.getAccessCode(  ) );
        daoUtil.setString( 3, user.getLastName(  ) );
        daoUtil.setString( 4, user.getFirstName(  ) );
        daoUtil.setString( 5, user.getEmail(  ) );
        daoUtil.setInt( 6, user.getStatus(  ) );
        daoUtil.setString( 7, user.getLocale(  ).toString(  ) );
        daoUtil.setInt( 8, user.getUserLevel(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
      * @param user The AdminUser
     */
    public void store( AdminUser user )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, user.getAccessCode(  ) );
        daoUtil.setString( 2, user.getLastName(  ) );
        daoUtil.setString( 3, user.getFirstName(  ) );
        daoUtil.setString( 4, user.getEmail(  ) );
        daoUtil.setInt( 5, user.getStatus(  ) );
        daoUtil.setString( 6, user.getLocale(  ).toString(  ) );

        daoUtil.setInt( 7, user.getUserId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param nUserId th user id
     */
    public void delete( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Get the right list associated to a given user id
     * @param nUserId the id of the user to retrieve rights
     * @return the right list as a collection of strings
     */
    public Map<String, Right> selectRightsListForUser( int nUserId )
    {
        Map<String, Right> rightsMap = new HashMap<String, Right>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RIGHTS_FROM_USER_ID );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Right right = new Right(  );
            right.setId( daoUtil.getString( 1 ) );
            right.setNameKey( daoUtil.getString( 2 ) );
            right.setUrl( daoUtil.getString( 3 ) );
            right.setDescriptionKey( daoUtil.getString( 4 ) );
            right.setPluginName( daoUtil.getString( 5 ) );
            right.setFeatureGroup( daoUtil.getString( 6 ) );
            right.setIconUrl( daoUtil.getString( 7 ) );
            right.setLevel( daoUtil.getInt( 8 ) );
            rightsMap.put( right.getId(  ), right );
        }

        daoUtil.free(  );

        return rightsMap;
    }

    /**
     * @param nUserId the user id
     * @param strRightId the right id
     */
    public void insertRightsListForUser( int nUserId, String strRightId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_USER_RIGHT );
        daoUtil.setString( 1, strRightId );
        daoUtil.setInt( 2, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param nUserId the user id
     */
    public void deleteAllRightsForUser( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_USER_RIGHTS );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Get the role list associated to a given user id
     * @param nUserId the id of the user to retrieve roles
     * @return the role list
     */
    public Map<String, AdminRole> selectRolesListForUser( int nUserId )
    {
        Map<String, AdminRole> rolesMap = new HashMap<String, AdminRole>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ROLES_FROM_USER_ID );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminRole role = new AdminRole(  );
            role.setKey( daoUtil.getString( 1 ) );
            role.setDescription( daoUtil.getString( 2 ) );

            rolesMap.put( role.getKey(  ), role );
        }

        daoUtil.free(  );

        return rolesMap;
    }

    /**
     * @param nUserId the user id
     * @param strRoleKey the key role
     */
    public void insertRolesListForUser( int nUserId, String strRoleKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_USER_ROLE );
        daoUtil.setString( 1, strRoleKey );
        daoUtil.setInt( 2, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param nUserId the user id
     */
    public void deleteAllRolesForUser( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_USER_ROLES );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Checks wether the role is in use or not
     * @param strRoleKey the role key to check
     * @return true if the role is attributed, false otherwise
     */
    public boolean checkRoleAttributed( String strRoleKey )
    {
        boolean bInUse = false;

        DAOUtil daoUtil = new DAOUtil( SQL_CHECK_ROLE_ATTRIBUTED );
        daoUtil.setString( 1, strRoleKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bInUse = true;
        }

        daoUtil.free(  );

        return bInUse;
    }

    /**
     * @param strAccessCode
     * @return
     */
    public boolean checkAccessCodeAlreadyInUse( String strAccessCode )
    {
        boolean bInUse = false;
        DAOUtil daoUtil = new DAOUtil( SQL_CHECK_ACCESS_CODE_IN_USE );
        daoUtil.setString( 1, strAccessCode );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bInUse = true;
        }

        daoUtil.free(  );

        return bInUse;
    }

    //////////////////////////////////////////////////////////////////
    // for no-module mode
    /**
     * Insert a new record in the table.
     * @param user The AdminUser
     *
     */
    public void insert( LuteceDefaultAdminUser user )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_DEFAULT_USER );

        user.setUserId( newPrimaryKey(  ) );
        daoUtil.setInt( 1, user.getUserId(  ) );
        daoUtil.setString( 2, user.getAccessCode(  ) );
        daoUtil.setString( 3, user.getLastName(  ) );
        daoUtil.setString( 4, user.getFirstName(  ) );
        daoUtil.setString( 5, user.getEmail(  ) );
        daoUtil.setInt( 6, user.getStatus(  ) );
        daoUtil.setString( 7, user.getPassword(  ) );
        daoUtil.setString( 8, user.getLocale(  ).toString(  ) );
        daoUtil.setInt( 9, user.getUserLevel(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param user The AdminUser
     */
    public void store( LuteceDefaultAdminUser user )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DEFAULT_USER );

        daoUtil.setString( 1, user.getAccessCode(  ) );
        daoUtil.setString( 2, user.getLastName(  ) );
        daoUtil.setString( 3, user.getFirstName(  ) );
        daoUtil.setString( 4, user.getEmail(  ) );
        daoUtil.setInt( 5, user.getStatus(  ) );
        daoUtil.setString( 6, user.getPassword(  ) );
        daoUtil.setString( 7, user.getLocale(  ).toString(  ) );

        daoUtil.setInt( 8, user.getUserId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @param nUserId th user id
     * @return user
     */
    public LuteceDefaultAdminUser loadDefaultAdminUser( int nUserId )
    {
        LuteceDefaultAdminUser user = new LuteceDefaultAdminUser(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_FROM_USER_ID );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            user.setUserId( daoUtil.getInt( 1 ) );
            user.setAccessCode( daoUtil.getString( 2 ) );
            user.setLastName( daoUtil.getString( 3 ) );
            user.setFirstName( daoUtil.getString( 4 ) );
            user.setEmail( daoUtil.getString( 5 ) );
            user.setStatus( daoUtil.getInt( 6 ) );
            user.setPassword( daoUtil.getString( 7 ) );

            Locale locale = new Locale( daoUtil.getString( 8 ) );
            user.setLocale( locale );
            user.setUserLevel( daoUtil.getInt( 9 ) );
        }

        daoUtil.free(  );

        return user;
    }

    /**
    * @return userList
    */
    public Collection<AdminUser> selectUsersByRole( String strRoleKey )
    {
        Collection<AdminUser> userList = new ArrayList<AdminUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_ID_BY_ROLES );
        daoUtil.setString( 1, strRoleKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminUser user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 1 ) );
            user.setAccessCode( daoUtil.getString( 2 ) );
            user.setLastName( daoUtil.getString( 3 ) );
            user.setFirstName( daoUtil.getString( 4 ) );
            user.setEmail( daoUtil.getString( 5 ) );
            user.setStatus( daoUtil.getInt( 6 ) );
            user.setLocale( new Locale( daoUtil.getString( 7 ) ) );
            userList.add( user );
        }

        daoUtil.free(  );

        return userList;
    }

    /**
     * Select rights by user, by user level and by type (Delegated or own)
     *
     * @param nUserId the id of the user
     * @param nUserLevel the id of the user level
     * @param bDelegated true if select concern delegated rights
     * @return collection of id rights
     */
    private Collection<String> selectIdRights( int nUserId, int nUserLevel, boolean bDelegated )
    {
        String strSqlQuery = bDelegated ? SQL_QUERY_SELECT_USER_RIGHTS_DELEGATED : SQL_QUERY_SELECT_USER_RIGHTS_OWN;
        Collection<String> idRightList = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( strSqlQuery );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setInt( 2, nUserLevel );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            idRightList.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return idRightList;
    }

    /**
     * Deletes rights by user and by id right
     *
     * @param nUserId the user id
     * @param idRightList the list of rights to delete
     */
    private void deleteRightsForUser( int nUserId, Collection<String> idRightList )
    {
        for ( String strIdRight : idRightList )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_USER_RIGHTS );
            daoUtil.setInt( 1, nUserId );
            daoUtil.setString( 2, strIdRight );
            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }

    /**
     * Deletes rights own by user ie rights with level >= userlevel
     * @param nUserId the user id
     * @param nUserLevel the user level
     */
    public void deleteAllOwnRightsForUser( int nUserId, int nUserLevel )
    {
        Collection<String> idRightList = selectIdRights( nUserId, nUserLevel, false );

        deleteRightsForUser( nUserId, idRightList );
    }

    /**
     * Deletes rights delegated by user ie rights with level < userlevel
     * @param nUserId the user id
     * @param nUserLevel the user level
     */
    public void deleteAllDelegatedRightsForUser( int nUserId, int nUserLevel )
    {
        Collection<String> idRightList = selectIdRights( nUserId, nUserLevel, true );

        deleteRightsForUser( nUserId, idRightList );
    }
}
