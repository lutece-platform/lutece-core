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
package fr.paris.lutece.portal.business.user;

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.authentication.LuteceDefaultAdminUser;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * This class porvides Data Access methods for AdminUser objects
 */
public class AdminUserDAO implements IAdminUserDAO
{
    // Constants
    private static final String CONSTANT_AND_STATUS = " AND status = ?";
    private static final String CONSTANT_AND_USER_LEVEL = " AND level_user = ?";
    private static final String CONSTANT_ORDER_BY_LAST_NAME = " ORDER BY last_name ";
    private static final String CONSTANT_PERCENT = "%";
    private static final String SQL_QUERY_NEWPK = "SELECT max( id_user ) FROM core_admin_user ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_admin_user ( id_user , access_code, last_name , first_name, email, status, locale, level_user, accessibility_mode, password_max_valid_date, account_max_valid_date )  VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user , access_code, last_name , first_name, email, status, locale, level_user, accessibility_mode, reset_password, password_max_valid_date, account_max_valid_date, last_login, workgroup_key FROM core_admin_user ORDER BY last_name ";
    private static final String SQL_QUERY_SELECT_USER_FROM_USER_ID = "SELECT id_user , access_code, last_name , first_name, email, status, password, locale, level_user, reset_password, accessibility_mode, password_max_valid_date, account_max_valid_date, workgroup_key FROM core_admin_user WHERE id_user = ? ";
    private static final String SQL_QUERY_SELECT_USER_FROM_ACCESS_CODE = "SELECT id_user, access_code, last_name, first_name, email, status, locale, level_user, reset_password, accessibility_mode, password_max_valid_date, last_login FROM core_admin_user  WHERE access_code = ? ";
    private static final String SQL_QUERY_SELECT_USER_FROM_EMAIL = "SELECT access_code FROM core_admin_user  WHERE email = ? ";
    private static final String SQL_QUERY_SELECT_RIGHTS_FROM_USER_ID = " SELECT a.id_right , a.name, a.admin_url , a.description , a.plugin_name, a.id_feature_group, a.icon_url, a.level_right, a.documentation_url, a.id_order, a.is_external_feature " +
        " FROM core_admin_right a , core_user_right b " + " WHERE a.id_right = b.id_right " + " AND b.id_user = ? " +
        " ORDER BY a.id_order ASC, a.id_right ASC ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_admin_user SET access_code = ? , last_name = ? , first_name = ?, email = ?, status = ?, locale = ?, reset_password = ?, accessibility_mode = ?, password_max_valid_date = ? WHERE id_user = ?  ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_admin_user WHERE id_user = ? ";
    private static final String SQL_QUERY_INSERT_USER_RIGHT = "INSERT INTO core_user_right ( id_right, id_user )  VALUES ( ? , ? ) ";
    private static final String SQL_QUERY_DELETE_ALL_USER_RIGHTS = "DELETE FROM core_user_right WHERE id_user = ? ";
    private static final String SQL_QUERY_SELECT_ROLES_FROM_USER_ID = " SELECT a.role_key , a.role_description " +
        " FROM core_admin_role a , core_user_role b WHERE a.role_key = b.role_key " +
        " AND b.id_user = ?  ORDER BY a.role_key ";
    private static final String SQL_QUERY_INSERT_USER_ROLE = " INSERT INTO core_user_role ( role_key, id_user )  VALUES ( ? , ? ) ";
    private static final String SQL_QUERY_DELETE_ALL_USER_ROLES = " DELETE FROM core_user_role WHERE id_user = ? ";
    private static final String SQL_CHECK_ROLE_ATTRIBUTED = " SELECT id_user FROM core_user_role WHERE role_key = ?";
    private static final String SQL_CHECK_ACCESS_CODE_IN_USE = " SELECT id_user FROM core_admin_user WHERE access_code = ?";
    private static final String SQL_CHECK_EMAIL_IN_USE = " SELECT id_user FROM core_admin_user WHERE email = ?";
    private static final String SQL_QUERY_INSERT_DEFAULT_USER = " INSERT INTO core_admin_user ( id_user, access_code, last_name, first_name, email, status, password, locale, level_user, accessibility_mode, reset_password, password_max_valid_date, account_max_valid_date, last_login, workgroup_key )  VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE_DEFAULT_USER = " UPDATE core_admin_user SET access_code = ?, last_name = ?, first_name = ?, email = ?, status = ?, password = ?, locale = ?, reset_password = ?, accessibility_mode = ?, password_max_valid_date = ?, workgroup_key = ? WHERE id_user = ?  ";
    private static final String SQL_QUERY_SELECT_USERS_ID_BY_ROLES = " SELECT a.id_user , a.access_code, a.last_name , a.first_name, a.email, a.status, a.locale, a.accessibility_mode, a.password_max_valid_date " +
        " FROM core_admin_user a, core_user_role b WHERE a.id_user = b.id_user AND b.role_key = ? ";
    private static final String SQL_QUERY_SELECT_USER_RIGHTS_OWN = " SELECT DISTINCT b.id_right FROM core_admin_right a , core_user_right b WHERE b.id_user = ? and a.id_right = b.id_right and a.level_right >= ?";
    private static final String SQL_QUERY_SELECT_USER_RIGHTS_DELEGATED = " SELECT DISTINCT b.id_right FROM core_admin_right a , core_user_right b WHERE b.id_user = ? and a.id_right = b.id_right and a.level_right < ?";
    private static final String SQL_QUERY_DELETE_USER_RIGHTS = " DELETE FROM core_user_right WHERE id_user = ? and id_right = ?";
    private static final String SQL_QUERY_SELECT_USERS_BY_LEVEL = " SELECT a.id_user, a.access_code, a.last_name, a.first_name, a.email, a.status, a.locale, a.accessibility_mode " +
        " FROM core_admin_user a WHERE a.level_user = ? ";
    private static final String SQL_QUERY_UPDATE_USERS_ROLE = "UPDATE core_user_role SET role_key = ? WHERE role_key = ?";
    private static final String SQL_QUERY_SELECT_USER_ROLE = " SELECT id_user FROM core_user_role WHERE id_user = ? AND role_key = ? ";
    private static final String SQL_QUERY_DELETE_ROLE_FOR_USER = " DELETE FROM core_user_role WHERE id_user = ? AND role_key = ? ";
    private static final String SQL_QUERY_SELECT_USER_FROM_SEARCH = " SELECT id_user, access_code, last_name, first_name, email, status, locale, level_user, accessibility_mode " +
        " FROM core_admin_user WHERE access_code LIKE ? AND last_name LIKE ? AND email LIKE ? ";
    private static final String SQL_QUERY_SELECT_USERS_BY_RIGHT = " SELECT  u.id_user , u.access_code, u.last_name , u.first_name, u.email, u.status, u.locale, u.level_user, u.accessibility_mode " +
        " FROM core_admin_user u INNER JOIN core_user_right r ON u.id_user = r.id_user WHERE r.id_right = ? ";
    private static final String SQL_QUERY_SELECT_USER_RIGHT = " SELECT id_user FROM core_user_right WHERE id_user = ? AND id_right = ? ";
    private static final String SQL_SELECT_USER_PASSWORD_HISTORY = "SELECT password FROM core_user_password_history WHERE id_user = ? ORDER BY date_password_change desc";
    private static final String SQL_COUNT_USER_PASSWORD_HISTORY = "SELECT COUNT(*) FROM core_user_password_history WHERE id_user = ? AND date_password_change > ?";
    private static final String SQL_INSERT_PASSWORD_HISTORY = "INSERT INTO core_user_password_history (id_user, password) VALUES ( ?, ? ) ";
    private static final String SQL_DELETE_PASSWORD_HISTORY = "DELETE FROM core_user_password_history WHERE id_user = ?";
    private static final String SQL_SELECT_ANONYMIZATION_STATUS_USER_FILED = "SELECT field_name, anonymize from core_admin_user_anonymize_field";
    private static final String SQL_UPDATE_ANONYMIZATION_STATUS_USER_FILED = "UPDATE core_admin_user_anonymize_field  SET anonymize = ? WHERE field_name = ? ";
    private static final String SQL_QUERY_SELECT_EXPIRED_USER_ID = "SELECT id_user FROM core_admin_user WHERE status = ?";
    private static final String SQL_QUERY_SELECT_EXPIRED_LIFE_TIME_USER_ID = "SELECT id_user FROM core_admin_user WHERE account_max_valid_date < ? and status < ? ";
    private static final String SQL_QUERY_SELECT_USER_ID_FIRST_ALERT = "SELECT id_user FROM core_admin_user WHERE nb_alerts_sent = 0 and status < ? and account_max_valid_date < ? ";
    private static final String SQL_QUERY_SELECT_USER_ID_OTHER_ALERT = "SELECT id_user FROM core_admin_user " +
        "WHERE nb_alerts_sent > 0 and nb_alerts_sent <= ? and status < ? and (account_max_valid_date + nb_alerts_sent * ?) < ? ";
    private static final String SQL_QUERY_SELECT_USER_ID_PASSWORD_EXPIRED = " SELECT id_user FROM core_admin_user WHERE password_max_valid_date < ? AND reset_password = 0 ";
    private static final String SQL_QUERY_UPDATE_STATUS = " UPDATE core_admin_user SET status = ? WHERE id_user IN ( ";
    private static final String SQL_QUERY_UPDATE_NB_ALERT = " UPDATE core_admin_user SET nb_alerts_sent = nb_alerts_sent + 1 WHERE id_user IN ( ";
    private static final String SQL_QUERY_UPDATE_RESET_PASSWORD_LIST_ID = " UPDATE core_admin_user SET reset_password = 1 WHERE id_user IN ( ";
    private static final String SQL_QUERY_UPDATE_REACTIVATE_ACCOUNT = " UPDATE core_admin_user SET nb_alerts_sent = 0, account_max_valid_date = ? WHERE id_user = ? ";
    private static final String SQL_QUERY_UPDATE_DATE_LAST_LOGIN = " UPDATE core_admin_user SET last_login = ? WHERE id_user = ? ";
    private static final String CONSTANT_CLOSE_PARENTHESIS = " ) ";
    private static final String CONSTANT_COMMA = ", ";

    /**
     * {@inheritDoc}
     */
    @Override
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
            user.setPasswordReset( daoUtil.getBoolean( 10 ) );
            user.setAccessibilityMode( daoUtil.getBoolean( 11 ) );
            user.setPasswordMaxValidDate( daoUtil.getTimestamp( 12 ) );

            long accountTime = daoUtil.getLong( 13 );

            if ( accountTime > 0 )
            {
                user.setAccountMaxValidDate( new Timestamp( accountTime ) );
            }
        }

        daoUtil.free(  );

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            user.setPasswordReset( daoUtil.getBoolean( 9 ) );
            user.setAccessibilityMode( daoUtil.getBoolean( 10 ) );
            user.setPasswordMaxValidDate( daoUtil.getTimestamp( 11 ) );

            Timestamp dateLastLogin = daoUtil.getTimestamp( 12 );

            if ( ( dateLastLogin != null ) && !dateLastLogin.equals( AdminUser.DEFAULT_DATE_LAST_LOGIN ) )
            {
                user.setDateLastLogin( dateLastLogin );
            }
        }

        daoUtil.free(  );

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String selectUserByEmail( String strEmail )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_FROM_EMAIL );
        daoUtil.setString( 1, strEmail );
        daoUtil.executeQuery(  );

        String strAccessCode = null;

        if ( daoUtil.next(  ) )
        {
            strAccessCode = daoUtil.getString( 1 );
        }

        daoUtil.free(  );

        return strAccessCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            user.setAccessibilityMode( daoUtil.getBoolean( 9 ) );
            user.setPasswordReset( daoUtil.getBoolean( 10 ) );
            user.setPasswordMaxValidDate( daoUtil.getTimestamp( 11 ) );

            long accountTime = daoUtil.getLong( 12 );

            if ( accountTime > 0 )
            {
                user.setAccountMaxValidDate( new Timestamp( accountTime ) );
            }

            Timestamp dateLastLogin = daoUtil.getTimestamp( 13 );

            if ( ( dateLastLogin != null ) && !dateLastLogin.equals( AdminUser.DEFAULT_DATE_LAST_LOGIN ) )
            {
                user.setDateLastLogin( dateLastLogin );
            }

            user.setWorkgroupKey( daoUtil.getString( 14 ) );
            userList.add( user );
        }

        daoUtil.free(  );

        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( AdminUser user )
    {
        user.setUserId( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, user.getUserId(  ) );
        daoUtil.setString( 2, user.getAccessCode(  ) );
        daoUtil.setString( 3, user.getLastName(  ) );
        daoUtil.setString( 4, user.getFirstName(  ) );
        daoUtil.setString( 5, user.getEmail(  ) );
        daoUtil.setInt( 6, user.getStatus(  ) );
        daoUtil.setString( 7, user.getLocale(  ).toString(  ) );
        daoUtil.setInt( 8, user.getUserLevel(  ) );
        daoUtil.setBoolean( 9, user.getAccessibilityMode(  ) );
        daoUtil.setTimestamp( 10, user.getPasswordMaxValidDate(  ) );

        if ( user.getAccountMaxValidDate(  ) == null )
        {
            daoUtil.setLongNull( 11 );
        }
        else
        {
            daoUtil.setLong( 11, user.getAccountMaxValidDate(  ).getTime(  ) );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( AdminUser user )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, user.getAccessCode(  ) );
        daoUtil.setString( 2, user.getLastName(  ) );
        daoUtil.setString( 3, user.getFirstName(  ) );
        daoUtil.setString( 4, user.getEmail(  ) );
        daoUtil.setInt( 5, user.getStatus(  ) );
        daoUtil.setString( 6, user.getLocale(  ).toString(  ) );
        daoUtil.setBoolean( 7, user.isPasswordReset(  ) );
        daoUtil.setBoolean( 8, user.getAccessibilityMode(  ) );
        daoUtil.setTimestamp( 9, user.getPasswordMaxValidDate(  ) );

        daoUtil.setInt( 10, user.getUserId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            right.setDocumentationUrl( daoUtil.getString( 9 ) );
            right.setOrder( daoUtil.getInt( 10 ) );
            right.setExternalFeature( daoUtil.getBoolean( 11 ) );
            rightsMap.put( right.getId(  ), right );
        }

        daoUtil.free(  );

        return rightsMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertRightsListForUser( int nUserId, String strRightId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_USER_RIGHT );
        daoUtil.setString( 1, strRightId );
        daoUtil.setInt( 2, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllRightsForUser( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_USER_RIGHTS );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public void insertRolesListForUser( int nUserId, String strRoleKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_USER_ROLE );
        daoUtil.setString( 1, strRoleKey );
        daoUtil.setInt( 2, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllRolesForUser( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_USER_ROLES );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
    public int checkAccessCodeAlreadyInUse( String strAccessCode )
    {
        int nIdUser = -1;
        DAOUtil daoUtil = new DAOUtil( SQL_CHECK_ACCESS_CODE_IN_USE );
        daoUtil.setString( 1, strAccessCode );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nIdUser = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nIdUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int checkEmailAlreadyInUse( String strEmail )
    {
        int nIdUser = -1;
        DAOUtil daoUtil = new DAOUtil( SQL_CHECK_EMAIL_IN_USE );
        daoUtil.setString( 1, strEmail );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nIdUser = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nIdUser;
    }

    // ////////////////////////////////////////////////////////////////
    // for no-module mode
    /**
     * {@inheritDoc}
     */
    @Override
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
        daoUtil.setBoolean( 10, user.getAccessibilityMode(  ) );
        daoUtil.setBoolean( 11, user.isPasswordReset(  ) );
        daoUtil.setTimestamp( 12, user.getPasswordMaxValidDate(  ) );

        if ( user.getAccountMaxValidDate(  ) == null )
        {
            daoUtil.setLongNull( 13 );
        }
        else
        {
            daoUtil.setLong( 13, user.getAccountMaxValidDate(  ).getTime(  ) );
        }

        daoUtil.setTimestamp( 14, user.getDateLastLogin(  ) );
        daoUtil.setString( 15, user.getWorkgroupKey(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        daoUtil.setBoolean( 8, user.isPasswordReset(  ) );
        daoUtil.setBoolean( 9, user.getAccessibilityMode(  ) );
        daoUtil.setTimestamp( 10, user.getPasswordMaxValidDate(  ) );
        daoUtil.setString( 11, user.getWorkgroupKey(  ) );

        daoUtil.setInt( 12, user.getUserId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            user.setPasswordReset( daoUtil.getBoolean( 10 ) );
            user.setAccessibilityMode( daoUtil.getBoolean( 11 ) );
            user.setWorkgroupKey( daoUtil.getString( 14 ) );
        }

        daoUtil.free(  );

        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            user.setAccessibilityMode( daoUtil.getBoolean( 8 ) );
            user.setPasswordMaxValidDate( daoUtil.getTimestamp( 9 ) );
            userList.add( user );
        }

        daoUtil.free(  );

        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AdminUser> selectUsersByLevel( int nIdLevel )
    {
        Collection<AdminUser> userList = new ArrayList<AdminUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_BY_LEVEL );
        daoUtil.setInt( 1, nIdLevel );
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
            user.setAccessibilityMode( daoUtil.getBoolean( 8 ) );
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
     * {@inheritDoc}
     */
    @Override
    public void deleteAllOwnRightsForUser( int nUserId, int nUserLevel )
    {
        Collection<String> idRightList = selectIdRights( nUserId, nUserLevel, false );

        deleteRightsForUser( nUserId, idRightList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAllDelegatedRightsForUser( int nUserId, int nUserLevel )
    {
        Collection<String> idRightList = selectIdRights( nUserId, nUserLevel, true );

        deleteRightsForUser( nUserId, idRightList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeUsersRole( String strOldRoleKey, AdminRole role )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_USERS_ROLE );
        daoUtil.setString( 1, role.getKey(  ) );
        daoUtil.setString( 2, strOldRoleKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRole( int nUserId, String strRoleKey )
    {
        boolean bHasRole = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_ROLE );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setString( 2, strRoleKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bHasRole = true;
        }

        daoUtil.free(  );

        return bHasRole;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRoleForUser( int nUserId, String strRoleKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ROLE_FOR_USER );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setString( 2, strRoleKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AdminUser> selectUsersByFilter( AdminUserFilter auFilter )
    {
        Collection<AdminUser> userList = new ArrayList<AdminUser>(  );
        DAOUtil daoUtil;

        String query = SQL_QUERY_SELECT_USER_FROM_SEARCH;

        if ( auFilter.getStatus(  ) != -1 )
        {
            query += CONSTANT_AND_STATUS;
        }

        if ( auFilter.getUserLevel(  ) != -1 )
        {
            query += CONSTANT_AND_USER_LEVEL;
        }

        query += CONSTANT_ORDER_BY_LAST_NAME;

        daoUtil = new DAOUtil( query );
        daoUtil.setString( 1, CONSTANT_PERCENT + auFilter.getAccessCode(  ) + CONSTANT_PERCENT );
        daoUtil.setString( 2, CONSTANT_PERCENT + auFilter.getLastName(  ) + CONSTANT_PERCENT );
        daoUtil.setString( 3, CONSTANT_PERCENT + auFilter.getEmail(  ) + CONSTANT_PERCENT );

        if ( auFilter.getStatus(  ) != -1 )
        {
            daoUtil.setInt( 4, auFilter.getStatus(  ) );

            if ( auFilter.getUserLevel(  ) != -1 )
            {
                daoUtil.setInt( 5, auFilter.getUserLevel(  ) );
            }
        }
        else
        {
            if ( auFilter.getUserLevel(  ) != -1 )
            {
                daoUtil.setInt( 4, auFilter.getUserLevel(  ) );
            }
        }

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
            user.setAccessibilityMode( daoUtil.getBoolean( 9 ) );
            userList.add( user );
        }

        daoUtil.free(  );

        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<AdminUser> selectUsersByRight( String strIdRight )
    {
        Collection<AdminUser> userList = new ArrayList<AdminUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS_BY_RIGHT );
        daoUtil.setString( 1, strIdRight );
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
            user.setAccessibilityMode( daoUtil.getBoolean( 9 ) );
            userList.add( user );
        }

        daoUtil.free(  );

        return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRight( int nUserId, String strIdRight )
    {
        boolean bHasRight = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_RIGHT );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setString( 2, strIdRight );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bHasRight = true;
        }

        daoUtil.free(  );

        return bHasRight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRightForUser( int nUserId, String strIdRight )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_USER_RIGHTS );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setString( 2, strIdRight );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> selectUserPasswordHistory( int nUserID )
    {
        List<String> listPasswordHistory = new ArrayList<String>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_USER_PASSWORD_HISTORY );
        daoUtil.setInt( 1, nUserID );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listPasswordHistory.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return listPasswordHistory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int countUserPasswordHistoryFromDate( Timestamp minDate, int nUserId )
    {
        int nNbRes = 0;

        DAOUtil daoUtil = new DAOUtil( SQL_COUNT_USER_PASSWORD_HISTORY );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setTimestamp( 2, minDate );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nNbRes = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nNbRes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertNewPasswordInHistory( String strPassword, int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_INSERT_PASSWORD_HISTORY );
        daoUtil.setInt( 1, nUserId );
        daoUtil.setString( 2, strPassword );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllPasswordHistoryForUser( int nUserId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_DELETE_PASSWORD_HISTORY );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Boolean> selectAnonymizationStatusUserStaticField(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_SELECT_ANONYMIZATION_STATUS_USER_FILED );
        daoUtil.executeQuery(  );

        Map<String, Boolean> resultMap = new HashMap<String, Boolean>(  );

        while ( daoUtil.next(  ) )
        {
            resultMap.put( daoUtil.getString( 1 ), daoUtil.getBoolean( 2 ) );
        }

        daoUtil.free(  );

        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAnonymizationStatusUserStaticField( String strFieldName, boolean bAnonymizeFiled )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_UPDATE_ANONYMIZATION_STATUS_USER_FILED );
        daoUtil.setBoolean( 1, bAnonymizeFiled );
        daoUtil.setString( 2, strFieldName );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findAllExpiredUserId(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_EXPIRED_USER_ID );
        daoUtil.setInt( 1, AdminUser.EXPIRED_CODE );

        List<Integer> listIdExpiredUser = new ArrayList<Integer>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listIdExpiredUser.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return listIdExpiredUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdUsersWithExpiredLifeTimeList( Timestamp currentTimestamp )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_EXPIRED_LIFE_TIME_USER_ID );
        daoUtil.setLong( 1, currentTimestamp.getTime(  ) );
        daoUtil.setInt( 2, AdminUser.EXPIRED_CODE );

        List<Integer> listIdExpiredUser = new ArrayList<Integer>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listIdExpiredUser.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return listIdExpiredUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdUsersToSendFirstAlert( Timestamp alertMaxDate )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_ID_FIRST_ALERT );
        daoUtil.setInt( 1, AdminUser.EXPIRED_CODE );
        daoUtil.setLong( 2, alertMaxDate.getTime(  ) );

        List<Integer> listIdUserFirstAlert = new ArrayList<Integer>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listIdUserFirstAlert.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return listIdUserFirstAlert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdUsersToSendOtherAlert( Timestamp alertMaxDate, Timestamp timeBetweenAlerts,
        int maxNumberAlerts )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_ID_OTHER_ALERT );
        daoUtil.setInt( 1, maxNumberAlerts );
        daoUtil.setInt( 2, AdminUser.EXPIRED_CODE );
        daoUtil.setLong( 3, timeBetweenAlerts.getTime(  ) );
        daoUtil.setLong( 4, alertMaxDate.getTime(  ) );

        List<Integer> listIdUserFirstAlert = new ArrayList<Integer>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listIdUserFirstAlert.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return listIdUserFirstAlert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdUsersWithExpiredPasswordsList( Timestamp currentTimestamp )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_ID_PASSWORD_EXPIRED );
        daoUtil.setTimestamp( 1, currentTimestamp );

        List<Integer> idUserPasswordExpiredlist = new ArrayList<Integer>(  );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            idUserPasswordExpiredlist.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return idUserPasswordExpiredlist;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserStatus( List<Integer> listIdUser, int nNewStatus )
    {
        if ( ( listIdUser != null ) && ( listIdUser.size(  ) > 0 ) )
        {
            StringBuilder sbSQL = new StringBuilder(  );
            sbSQL.append( SQL_QUERY_UPDATE_STATUS );

            for ( int i = 0; i < listIdUser.size(  ); i++ )
            {
                if ( i > 0 )
                {
                    sbSQL.append( CONSTANT_COMMA );
                }

                sbSQL.append( listIdUser.get( i ) );
            }

            sbSQL.append( CONSTANT_CLOSE_PARENTHESIS );

            DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ) );
            daoUtil.setInt( 1, nNewStatus );
            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNbAlert( List<Integer> listIdUser )
    {
        if ( ( listIdUser != null ) && ( listIdUser.size(  ) > 0 ) )
        {
            StringBuilder sbSQL = new StringBuilder(  );
            sbSQL.append( SQL_QUERY_UPDATE_NB_ALERT );

            for ( int i = 0; i < listIdUser.size(  ); i++ )
            {
                if ( i > 0 )
                {
                    sbSQL.append( CONSTANT_COMMA );
                }

                sbSQL.append( listIdUser.get( i ) );
            }

            sbSQL.append( CONSTANT_CLOSE_PARENTHESIS );

            DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ) );
            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChangePassword( List<Integer> listIdUser )
    {
        if ( ( listIdUser != null ) && ( listIdUser.size(  ) > 0 ) )
        {
            StringBuilder sbSQL = new StringBuilder(  );
            sbSQL.append( SQL_QUERY_UPDATE_RESET_PASSWORD_LIST_ID );

            for ( int i = 0; i < listIdUser.size(  ); i++ )
            {
                if ( i > 0 )
                {
                    sbSQL.append( CONSTANT_COMMA );
                }

                sbSQL.append( listIdUser.get( i ) );
            }

            sbSQL.append( CONSTANT_CLOSE_PARENTHESIS );

            DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ) );
            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUserExpirationDate( int nIdUser, Timestamp newExpirationDate )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_REACTIVATE_ACCOUNT );

        if ( newExpirationDate == null )
        {
            daoUtil.setLongNull( 1 );
        }
        else
        {
            daoUtil.setLong( 1, newExpirationDate.getTime(  ) );
        }

        daoUtil.setInt( 2, nIdUser );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDateLastLogin( int nIdUser, Timestamp dateLastLogin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_DATE_LAST_LOGIN );
        daoUtil.setTimestamp( 1, dateLastLogin );
        daoUtil.setInt( 2, nIdUser );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
