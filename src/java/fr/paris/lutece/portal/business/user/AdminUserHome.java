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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * This class provides instances management methods (create, find, ...) for AdminUser objects
 */
public final class AdminUserHome
{
    // Static variable pointed at the DAO instance
    private static IAdminUserDAO _dao = (IAdminUserDAO) SpringContextService.getBean( "adminUserDAO" );

    /**
     * Private constructor
     */
    private AdminUserHome(  )
    {
    }

    /**
     * Get the user infos from the access code.
     * @param strUserLogin the login
     * @return user info
     */
    public static AdminUser findUserByLogin( String strUserLogin )
    {
        return _dao.selectUserByAccessCode( strUserLogin );
    }

    /**
     * Get the user access code from its email.
     * @param strEmail The email
     * @return The access code of the user with the given email, or null if no user has been found
     */
    public static String findUserByEmail( String strEmail )
    {
        return _dao.selectUserByEmail( strEmail );
    }

    /**
     * Get the user infos from user id
     * @param nUserId the user identifier
     * @return The user
     */
    public static AdminUser findByPrimaryKey( int nUserId )
    {
        return _dao.load( nUserId );
    }

    /**
     * @return the user list
     */
    public static Collection<AdminUser> findUserList(  )
    {
        return _dao.selectUserList(  );
    }

    /**
     * @param user The AdminUser
     */
    public static void create( AdminUser user )
    {
        _dao.insert( user );
    }

    /**
     * @param user The AdminUser
     */
    public static void update( AdminUser user )
    {
        _dao.store( user );
    }

    /**
     * @param nUserId the user identifier
     */
    public static void remove( int nUserId )
    {
        _dao.delete( nUserId );
    }

    /**
     * Get the right list associated to a given user id
     * @param nUserId the id of the user to retrieve rights
     * @return the right list
     */
    public static Map<String, Right> getRightsListForUser( int nUserId )
    {
        return _dao.selectRightsListForUser( nUserId );
    }

    /**
     * @param nUserId The user identifier
     * @param strRightId The right identifier
     */
    public static void createRightForUser( int nUserId, String strRightId )
    {
        _dao.insertRightsListForUser( nUserId, strRightId );
    }

    /**
     * @param nUserId The user identifier
     */
    public static void removeAllRightsForUser( int nUserId )
    {
        _dao.deleteAllRightsForUser( nUserId );
    }

    /**
     * @param user The Admin User object
     */
    public static void removeAllDelegatedRightsForUser( AdminUser user )
    {
        _dao.deleteAllDelegatedRightsForUser( user.getUserId(  ), user.getUserLevel(  ) );
    }

    /**
     * @param user The Admin User object
     */
    public static void removeAllOwnRightsForUser( AdminUser user )
    {
        _dao.deleteAllOwnRightsForUser( user.getUserId(  ), user.getUserLevel(  ) );
    }

    /**
     * Get the role list associated to a given user id
     * @param nUserId the id of the user to retrieve rights
     * @return the role list
     */
    public static Map<String, AdminRole> getRolesListForUser( int nUserId )
    {
        return _dao.selectRolesListForUser( nUserId );
    }

    /**
     * @param nUserId the id of the user
     * @param strRightId the right identifier
     */
    public static void createRoleForUser( int nUserId, String strRightId )
    {
        _dao.insertRolesListForUser( nUserId, strRightId );
    }

    /**
     * @param nUserId the user identifier
     */
    public static void removeAllRolesForUser( int nUserId )
    {
        _dao.deleteAllRolesForUser( nUserId );
    }

    /**
     * Checks wether the role is in use or not
     * @param strRoleKey the role key to check
     * @return true if the role is attributed, false otherwise
     */
    public static boolean checkRoleAttributed( String strRoleKey )
    {
        return _dao.checkRoleAttributed( strRoleKey );
    }

    /**
     * Checks if a given login is already in use
     * @param strAccessCode The login
     * @return user ID if the access code is already used by another user, -1 otherwise
     */
    public static int checkAccessCodeAlreadyInUse( String strAccessCode )
    {
        return _dao.checkAccessCodeAlreadyInUse( strAccessCode );
    }

    /**
     * Checks if a given email is already in use
     * @param strEmail The email
     * @return user ID if the email is already used by another user, -1 otherwise
     */
    public static int checkEmailAlreadyInUse( String strEmail )
    {
        return _dao.checkEmailAlreadyInUse( strEmail );
    }

    /**
     * Check if the user has the role
     * @param user The AdminUser
     * @param strRoleKey The role Key
     * @return true if the user has the role
     */
    public static boolean hasRole( AdminUser user, String strRoleKey )
    {
        return _dao.hasRole( user.getUserId(  ), strRoleKey );
    }

    /**
     * Remove role for an user
     * @param nUserId The ID of the user
     * @param strRoleKey The role key
     */
    public static void removeRoleForUser( int nUserId, String strRoleKey )
    {
        _dao.deleteRoleForUser( nUserId, strRoleKey );
    }

    // ////////////////////////////////////////////////////////////////
    // / for no-module mode

    /**
     * @param user the LuteceDefaultAdminUSer
     */
    public static void create( LuteceDefaultAdminUser user )
    {
        _dao.insert( user );
    }

    /**
     * @param user the LuteceDefaultAdminUSer
     */
    public static void update( LuteceDefaultAdminUser user )
    {
        _dao.store( user );
    }

    /**
     * Get the user infos from user id
     * @param nUserId the user identifier
     * @return the delfault admin user
     */
    public static LuteceDefaultAdminUser findLuteceDefaultAdminUserByPrimaryKey( int nUserId )
    {
        return _dao.loadDefaultAdminUser( nUserId );
    }

    /**
     * Get all users having a given role
     * @param strRoleKey The role key
     * @return A collection of AdminUser
     */
    public static Collection<AdminUser> findByRole( String strRoleKey )
    {
        return _dao.selectUsersByRole( strRoleKey );
    }

    /**
     * Get all users having a given level
     * @param nIdLevel The level
     * @return A collection of AdminUser
     */
    public static Collection<AdminUser> findByLevel( int nIdLevel )
    {
        return _dao.selectUsersByLevel( nIdLevel );
    }

    /**
     * Update role key if role key name has change
     * @param strOldRoleKey The old role key name
     * @param role The new role
     */
    public static void updateUsersRole( String strOldRoleKey, AdminRole role )
    {
        _dao.storeUsersRole( strOldRoleKey, role );
    }

    /**
     * Get all users by using a filter.
     * @param auFilter The filter
     * @return A collection of AdminUser
     */
    public static Collection<AdminUser> findUserByFilter( AdminUserFilter auFilter )
    {
        return _dao.selectUsersByFilter( auFilter );
    }

    /**
     * Get all users having a given right
     * @param strIdRight The ID right
     * @return A collection of AdminUser
     */
    public static Collection<AdminUser> findByRight( String strIdRight )
    {
        return _dao.selectUsersByRight( strIdRight );
    }

    /**
     * Check if the user has the given right
     * @param user The Admin User
     * @param strIdRight The ID right
     * @return true if the user has the right
     */
    public static boolean hasRight( AdminUser user, String strIdRight )
    {
        return _dao.hasRight( user.getUserId(  ), strIdRight );
    }

    /**
     * Remove a right for an user
     * @param nUserId The user ID
     * @param strIdRight The right ID
     */
    public static void removeRightForUser( int nUserId, String strIdRight )
    {
        _dao.deleteRightForUser( nUserId, strIdRight );
    }

    /**
     * Gets the history of password of the given user
     * @param nUserID Id of the user
     * @return The collection of recent passwords used by the user.
     */
    public static List<String> selectUserPasswordHistory( int nUserID )
    {
        return _dao.selectUserPasswordHistory( nUserID );
    }

    /**
     * Get the number of password change done by a user since the given date.
     * @param minDate Minimum date to consider.
     * @param nUserId Id of the user
     * @return The number of password change done by the user since the given date.
     */
    public static int countUserPasswordHistoryFromDate( Timestamp minDate, int nUserId )
    {
        return _dao.countUserPasswordHistoryFromDate( minDate, nUserId );
    }

    /**
     * Log a password change in the password history
     * @param strPassword New password of the user
     * @param nUserId Id of the user
     */
    public static void insertNewPasswordInHistory( String strPassword, int nUserId )
    {
        _dao.insertNewPasswordInHistory( strPassword, nUserId );
    }

    /**
     * Remove every password saved in the password history for a user.
     * @param nUserId Id of the user
     */
    public static void removeAllPasswordHistoryForUser( int nUserId )
    {
        _dao.removeAllPasswordHistoryForUser( nUserId );
    }

    /**
     * Get a map of anonymization status of a user field.
     * @return A map containing the associations of user field name and a boolean describing whether the field should be anonymized.
     */
    public static Map<String, Boolean> getAnonymizationStatusUserStaticField(  )
    {
        return _dao.selectAnonymizationStatusUserStaticField(  );
    }

    /**
     * Update the anonymization status of a user field.
     * @param strFieldName Name of the field to update
     * @param bAnonymizeFiled True if the field should be anonymize, false otherwise
     */
    public static void updateAnonymizationStatusUserStaticField( String strFieldName, boolean bAnonymizeFiled )
    {
        _dao.updateAnonymizationStatusUserStaticField( strFieldName, bAnonymizeFiled );
    }

    /**
     * Get the list of id of user with the expired status.
     * @return The list of if of user with the expired status.
     */
    public static List<Integer> findAllExpiredUserId(  )
    {
        return _dao.findAllExpiredUserId(  );
    }

    /**
     * Get the list of id of users that have an expired time life but not the expired status
     * @param currentTimestamp Timestamp describing the current time.
     * @return the list of id of users with expired time life
     */
    public static List<Integer> getIdUsersWithExpiredLifeTimeList( Timestamp currentTimestamp )
    {
        return _dao.getIdUsersWithExpiredLifeTimeList( currentTimestamp );
    }

    /**
     * Get the list of id of users that need to receive their first alert
     * @param firstAlertMaxDate The maximum expiration date to send first alert.
     * @return the list of id of users that need to receive their first alert
     */
    public static List<Integer> getIdUsersToSendFirstAlert( Timestamp firstAlertMaxDate )
    {
        return _dao.getIdUsersToSendFirstAlert( firstAlertMaxDate );
    }

    /**
     * Get the list of id of users that need to receive their first alert
     * @param alertMaxDate The maximum date to send alerts.
     * @param timeBetweenAlerts Timestamp describing the time between two alerts.
     * @param maxNumberAlerts Maximum number of alerts to send to a user
     * @return the list of id of users that need to receive their first alert
     */
    public static List<Integer> getIdUsersToSendOtherAlert( Timestamp alertMaxDate, Timestamp timeBetweenAlerts,
        int maxNumberAlerts )
    {
        return _dao.getIdUsersToSendOtherAlert( alertMaxDate, timeBetweenAlerts, maxNumberAlerts );
    }

    /**
     * Get the list of id of users that have an expired password but not the change password flag
     * @param currentTimestamp Timestamp describing the current time.
     * @return the list of id of users with expired passwords
     */
    public static List<Integer> getIdUsersWithExpiredPasswordsList( Timestamp currentTimestamp )
    {
        return _dao.getIdUsersWithExpiredPasswordsList( currentTimestamp );
    }

    /**
     * Update status of a list of user accounts
     * @param listIdUser List of user accounts to update
     * @param nNewStatus New status of the user
     */
    public static void updateUserStatus( List<Integer> listIdUser, int nNewStatus )
    {
        _dao.updateUserStatus( listIdUser, nNewStatus );
    }

    /**
     * Increment the number of alert send to users by 1
     * @param listIdUser The list of users to update
     */
    public static void updateNbAlert( List<Integer> listIdUser )
    {
        _dao.updateNbAlert( listIdUser );
    }

    /**
     * Set the "change password" flag of users to true
     * @param listIdUser The list of users to update
     */
    public static void updateChangePassword( List<Integer> listIdUser )
    {
        _dao.updateChangePassword( listIdUser );
    }

    /**
     * Update the admin user expiration date with the new values. Also update his alert account to 0
     * @param nIdUser Id of the admin user to update
     * @param newExpirationDate Id of the user to update
     */
    public static void updateUserExpirationDate( int nIdUser, Timestamp newExpirationDate )
    {
        _dao.updateUserExpirationDate( nIdUser, newExpirationDate );
    }

    /**
     * Update the admin user last login date.
     * @param nIdUser Id of the admin user to update
     * @param dateLastLogin New last login date of the user
     */
    public static void updateDateLastLogin( int nIdUser, Timestamp dateLastLogin )
    {
        _dao.updateDateLastLogin( nIdUser, dateLastLogin );
    }
}
