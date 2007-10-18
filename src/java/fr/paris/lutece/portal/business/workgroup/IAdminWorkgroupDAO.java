/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * Interface for AdminWorkgroup DAO
 *
 */
public interface IAdminWorkgroupDAO
{
    /**
     * Check that the given key points to an existing workgroup
     *
     * @param strNewWorkgroupKey
     * @return true if the workgroup exists, false otherwise
     */
    boolean checkExistWorkgroup( String strWorkgroupKey );

    /**
     * Delete a record from the table
     *
     * @param workgroup The AdminWorkgroup object
     */
    void delete( String strWorkgroupKey );

    /**
     * Insert a new record in the table.
     *
     *
     * @param workgroup The workgroup object
     */
    void insert( AdminWorkgroup workgroup );

    /**
     * Load the data of AdminWorkgroup from the table
     *
     *
     * @param nWorkgroupId The identifier of AdminWorkgroup
     * @return the instance of the AdminWorkgroup
     */
    AdminWorkgroup load( String strWorkgroupKey );

    /**
     * Load the list of workgroups
     *
     * @return The Collection of the Workgroups
     */
    Collection<AdminWorkgroup> selectWorkgroupList(  );

    /**
     * Update the record identified by the given workgroup key with the given workgroup in the table
     * @param workgroup The reference of workgroup to be the new one
     */
    void store( AdminWorkgroup workgroup );

    /**
     * Is the user member of the workgroup
     * @param nIdUser The user Id
     * @param strWorkgroup The workgroup key
     */
    boolean isUserInWorkgroup( int nIdUser, String strWorkgroup );

    /**
     * Is the user member of the workgroup
     * @param nIdUser The user Id
     */
    boolean checkUserHasWorkgroup( int nIdUser );

    /**
     * Returns the list of all workgroups the user is member
     * @param nIdUser The user Id
     */
    ReferenceList getUserWorkgroups( int nIdUser );

    /**
     * Returns the list of all users for a workgroup
     * @param nIdUser The workgroup key
     */
    Collection<AdminUser> getUsersListForWorkgroup( String strWorkgroupKey );

    /**
     * @param nUserId the user id
     */
    public void deleteAllUsersForWorkgroup( String strWorkgroupKey );

    /**
     * @param nUserId the user id
     * @param strWorkgroupKey the key workgroup
     */
    void insertUserForWorkgroup( AdminUser user, String strWorkgroupKey );

    /**
     * @param userId
     * @param strWorkgroupKey
     */
    void deleteUserFromWorkgroup( int userId, String strWorkgroupKey );
}
