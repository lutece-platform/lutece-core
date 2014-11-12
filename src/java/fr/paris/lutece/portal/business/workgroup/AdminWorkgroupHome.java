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
package fr.paris.lutece.portal.business.workgroup;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for AdminWorkgroup objects
 */
public final class AdminWorkgroupHome
{
    // Static variable pointed at the DAO instance
    private static IAdminWorkgroupDAO _dao = (IAdminWorkgroupDAO) SpringContextService.getBean( "adminWorkgroupDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AdminWorkgroupHome(  )
    {
    }

    /**
     * Creation of an instance of workgroup
     *
     * @param workgroup The instance of the workgroup which contains the informations to store
     * @return The  instance of workgroup which has been created with its primary key.
     */
    public static AdminWorkgroup create( AdminWorkgroup workgroup )
    {
        _dao.insert( workgroup );

        return workgroup;
    }

    /**
     * Update of the workgroup which is specified in parameter
     * @param workgroup The instance of the workgroup which contains the new data to store
     * @return The instance of the  workgroup which has been updated
     */
    public static AdminWorkgroup update( AdminWorkgroup workgroup )
    {
        _dao.store( workgroup );

        return workgroup;
    }

    /**
     * Remove the AdminWorkgroup whose identifier is specified in parameter
     *
     * @param strWorkgroupKey The AdminWorkgroup object to remove
     */
    public static void remove( String strWorkgroupKey )
    {
        _dao.delete( strWorkgroupKey );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a workgroup whose identifier is specified in parameter
     *
     * @param strWorkgroupKey The key of the workgroup
     * @return An instance of workgroup
     */
    public static AdminWorkgroup findByPrimaryKey( String strWorkgroupKey )
    {
        return _dao.load( strWorkgroupKey );
    }

    /**
     * Returns a collection of workgroups objects
     * @return A collection of workgroups
     */
    public static Collection<AdminWorkgroup> findAll(  )
    {
        return _dao.selectWorkgroupList(  );
    }

    /**
     * Check that the given key points to an existing workgroup
     * @param strWorkgroupKey The workgroup key
     * @return true if the workgroup exists, false otherwise
     */
    public static boolean checkExistWorkgroup( String strWorkgroupKey )
    {
        return _dao.checkExistWorkgroup( strWorkgroupKey );
    }

    /**
     * Is the user member of the workgroup
     * @param user The user
     * @param strWorkgroup The workgroup key
     * @return True if the user is in the workgroup, otherwise false
     */
    public static boolean isUserInWorkgroup( AdminUser user, String strWorkgroup )
    {
        return _dao.isUserInWorkgroup( user.getUserId(  ), strWorkgroup );
    }

    /**
     * Is the user member of the workgroup
     * @param nIdUser The user identifier
     * @return True if the user is in a workgroup, otherwise false
     */
    public static boolean checkUserHasWorkgroup( int nIdUser )
    {
        return _dao.checkUserHasWorkgroup( nIdUser );
    }

    /**
     * Returns the list of all workgroups the user is member
     * @param user The user
     * @return A reference list of all user's workgroups
     */
    public static ReferenceList getUserWorkgroups( AdminUser user )
    {
        return _dao.getUserWorkgroups( user.getUserId(  ) );
    }

    /**
      * Returns the list of all users for a workgroup
      * @param strWorkgroupKey The workgroup key
      * @return A list of all users of the workgroup
      */
    public static Collection<AdminUser> getUserListForWorkgroup( String strWorkgroupKey )
    {
        return _dao.getUsersListForWorkgroup( strWorkgroupKey );
    }

    /**
     * Add user to the workgroup
     * @param user The user
     * @param strWorkgroupKey The workgroup key
     */
    public static void addUserForWorkgroup( AdminUser user, String strWorkgroupKey )
    {
        _dao.insertUserForWorkgroup( user, strWorkgroupKey );
    }

    /**
     * Remove all users of a workgroup
     * @param strWorkgroupKey The workgroup key
     */
    public static void removeAllUsersForWorkgroup( String strWorkgroupKey )
    {
        _dao.deleteAllUsersForWorkgroup( strWorkgroupKey );
    }

    /**
     * Remove an user from a workgroup
     * @param user The user
     * @param strWorkgroupKey The workgroup key
     */
    public static void removeUserFromWorkgroup( AdminUser user, String strWorkgroupKey )
    {
        _dao.deleteUserFromWorkgroup( user.getUserId(  ), strWorkgroupKey );
    }

    /**
     * Find workgroups from a filter
     * @param awFilter the filter
     * @return the list of workgroups
     */
    public static Collection<AdminWorkgroup> findByFilter( AdminWorkgroupFilter awFilter )
    {
        return _dao.selectWorkgroupsByFilter( awFilter );
    }
}
