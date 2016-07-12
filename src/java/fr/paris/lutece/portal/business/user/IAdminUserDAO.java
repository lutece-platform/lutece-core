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

import java.sql.Timestamp;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * AdminUserDAO Interface
 */
public interface IAdminUserDAO
{
    /**
     * Checks the availibility of an access code
     * @param strAccessCode The access code
     * @return user ID if the access code is already used by another user, -1 otherwise
     */
    int checkAccessCodeAlreadyInUse( String strAccessCode );

    /**
     * Checks the availibility of an email
     * @param strEmail The email
     * @return True if the email is already used by another user
     */
    int checkEmailAlreadyInUse( String strEmail );

    /**
     * Checks wether the role is in use or not
     * @param strRoleKey the role key to check
     * @return user ID if the emaile is already used by another user, -1 otherwise
     */
    boolean checkRoleAttributed( String strRoleKey );

    /**
     * Delete an user
     * @param nUserId the user id
     */
    void delete( int nUserId );

    /**
     * Deletes rights delegated by user ie rights with level < userlevel
     * @param nUserId the user id
     * @param nUserLevel the user level
     */
    void deleteAllDelegatedRightsForUser( int nUserId, int nUserLevel );

    /**
     * Deletes all rights owned by user ie rights with level >= userlevel
     * @param nUserId the user id
     * @param nUserLevel the user level
     */
    void deleteAllOwnRightsForUser( int nUserId, int nUserLevel );

    /**
     * Delete all rights owned by an user
     * @param nUserId the user id
     */
    void deleteAllRightsForUser( int nUserId );

    /**
     * Remove all rights from an user
     * @param nUserId the user id
     */
    void deleteAllRolesForUser( int nUserId );

    /**
     * Insert a new record in the table.
     * @param user The AdminUser
     */
    void insert( AdminUser user );

    /**
     * Insert a new record in the table.
     * @param user The AdminUser
     */
    void insert( LuteceDefaultAdminUser user );

    /**
     * Add a right to an user
     * @param nUserId the user id
     * @param strRightId the right id
     */
    void insertRightsListForUser( int nUserId, String strRightId );

    /**
     * Gives a role to an user
     * @param nUserId the user id
     * @param strRoleKey the key role
     */
    void insertRolesListForUser( int nUserId, String strRoleKey );

    /**
     * Load an AdminUser
     * @param nUserId the user id
     * @return user
     */
    AdminUser load( int nUserId );

    /**
     * Load a default AdminUser
     * @param nUserId the user id
     * @return user
     */
    LuteceDefaultAdminUser loadDefaultAdminUser( int nUserId );

    /**
     * Generates a new primary key
     * @return nKey
     */
    int newPrimaryKey(  );

    /**
     * Get the right list associated to a given user id
     * @param nUserId the id of the user to retrieve rights
     * @return the right list as a collection of strings
     */
    Map<String, Right> selectRightsListForUser( int nUserId );

    /**
     * Get the role list associated to a given user id
     * @param nUserId the id of the user to retrieve roles
     * @return the role list
     */
    Map<String, AdminRole> selectRolesListForUser( int nUserId );

    /**
     * Get an user by its access code (login)
     * @param strUserAccessCode the login
     * @return The user found, otherwise null
     */
    AdminUser selectUserByAccessCode( String strUserAccessCode );

    /**
     * Get the user access code from its email.
     * @param strEmail The email
     * @return The access code of the user with the given email, or null if no user has been found
     */
    String selectUserByEmail( String strEmail );

    /**
     * Gets the collection of all AdminUsers
     * @return The user list
     */
    Collection<AdminUser> selectUserList(  );

    /**
     * Gets a collection of AdminUser that share a given role
     * @param strRoleKey The role key
     * @return The user List
     */
    Collection<AdminUser> selectUsersByRole( String strRoleKey );

    /**
     * Update AdminUser data
     * @param user The AdminUser
     */
    void store( AdminUser user );

    /**
     * Update AdminUser data
     * @param user The AdminUser
     */
    void store( LuteceDefaultAdminUser user );

    /**
     * Select all user that own a given level
     * @param nIdLevel The level
     * @return userList The user's list
     */
    Collection<AdminUser> selectUsersByLevel( int nIdLevel );

    /**
     * Update role key if role key name has change
     * @param strOldRoleKey The old role key name
     * @param role The new role
     */
    void storeUsersRole( String strOldRoleKey, AdminRole role );

    /**
     * Check if the user has the role
     * @param nUserId The ID of the user
     * @param strRoleKey The role Key
     * @return true if the user has the role
     */
    boolean hasRole( int nUserId, String strRoleKey );

    /**
     * Remove role for an user
     * @param nUserId The ID of the user
     * @param strRoleKey The role key
     */
    void deleteRoleForUser( int nUserId, String strRoleKey );

    /**
     * Select users by filter
     * @param auFilter the filter
     * @return a list of AdminUser
     */
    Collection<AdminUser> selectUsersByFilter( AdminUserFilter auFilter );

    /**
     * Get all users having a given right
     * @param strIdRight The ID right
     * @return A collection of AdminUser
     */
    Collection<AdminUser> selectUsersByRight( String strIdRight );

    /**
     * Check if the user has the given right
     * @param nUserId The ID of the user
     * @param strIdRight The ID right
     * @return true if the user has the right
     */
    boolean hasRight( int nUserId, String strIdRight );

    /**
     * Remove a right for an user
     * @param nUserId The user ID
     * @param strIdRight The right ID
     */
    void deleteRightForUser( int nUserId, String strIdRight );

    /**
     * Gets the history of password of the given user
     * @param nUserID Id of the user
     * @return The collection of recent passwords used by the user.
     */
    List<String> selectUserPasswordHistory( int nUserID );

    /**
     * Get the number of password change done by a user since the given date.
     * @param minDate Minimum date to consider.
     * @param nUserId Id of the user
     * @return The number of password change done by the user since the given date.
     */
    int countUserPasswordHistoryFromDate( Timestamp minDate, int nUserId );

    /**
     * Log a password change in the password history
     * @param strPassword New password of the user
     * @param nUserId Id of the user
     */
    void insertNewPasswordInHistory( String strPassword, int nUserId );

    /**
     * Remove every password saved in the password history for a given user.
     * @param nUserId Id of the user
     */
    void removeAllPasswordHistoryForUser( int nUserId );

    /**
     * Get a map of anonymization status of a user field.
     * @return A map containing the associations of user field name and a boolean describing whether the field should be anonymized.
     */
    Map<String, Boolean> selectAnonymizationStatusUserStaticField(  );

    /**
     * Update the anonymization status of a user field.
     * @param strFieldName Name of the field to update
     * @param bAnonymizeFiled True if the field should be anonymized, false otherwise
     */
    void updateAnonymizationStatusUserStaticField( String strFieldName, boolean bAnonymizeFiled );

    /**
     * Get the list of id of user with the expired status.
     * @return The list of if of user with the expired status.
     */
    List<Integer> findAllExpiredUserId(  );

    /**
     * Get the list of id of users that have an expired time life but not the expired status
     * @param currentTimestamp Timestamp describing the current time.
     * @return the list of id of users with expired time life
     */
    List<Integer> getIdUsersWithExpiredLifeTimeList( Timestamp currentTimestamp );

    /**
     * Get the list of id of users that need to receive their first alert
     * @param alertMaxDate The maximum date to send alerts.
     * @return the list of id of users that need to receive their first alert
     */
    List<Integer> getIdUsersToSendFirstAlert( Timestamp alertMaxDate );

    /**
     * Get the list of id of users that need to receive their first alert
     * @param alertMaxDate The maximum date to send alerts.
     * @param timeBetweenAlerts Timestamp describing the time between two alerts.
     * @param maxNumberAlerts Maximum number of alerts to send to a user
     * @return the list of id of users that need to receive their first alert
     */
    List<Integer> getIdUsersToSendOtherAlert( Timestamp alertMaxDate, Timestamp timeBetweenAlerts, int maxNumberAlerts );

    /**
     * Get the list of id of users that have an expired password but not the change password flag
     * @param currentTimestamp Timestamp describing the current time.
     * @return the list of id of users with expired passwords
     */
    List<Integer> getIdUsersWithExpiredPasswordsList( Timestamp currentTimestamp );

    /**
     * Update status of a list of user accounts
     * @param listIdUser List of user accounts to update
     * @param nNewStatus New status of the user
     */
    void updateUserStatus( List<Integer> listIdUser, int nNewStatus );

    /**
     * Increment the number of alert send to users by 1
     * @param listIdUser The list of users to update
     */
    void updateNbAlert( List<Integer> listIdUser );

    /**
     * Set the "change password" flag of users to true
     * @param listIdUser The list of users to update
     */
    void updateChangePassword( List<Integer> listIdUser );

    /**
     * Update the admin user expiration date with the new values. Also update his alert account to 0
     * @param nIdUser Id of the admin user to update
     * @param newExpirationDate New expiration date of the user
     */
    void updateUserExpirationDate( int nIdUser, Timestamp newExpirationDate );

    /**
     * Update the admin user last login date.
     * @param nIdUser Id of the admin user to update
     * @param dateLastLogin New last login date of the user
     */
    void updateDateLastLogin( int nIdUser, Timestamp dateLastLogin );
}
