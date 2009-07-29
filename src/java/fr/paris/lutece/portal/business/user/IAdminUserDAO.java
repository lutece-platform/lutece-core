/*
 * Copyright (c) 2002-2009, Mairie de Paris
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

import java.util.Collection;
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
}
