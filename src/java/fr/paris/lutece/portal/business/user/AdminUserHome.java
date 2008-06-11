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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;
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
     * @return True if the the login is already in use, otherwise false
     */
    public static boolean checkAccessCodeAlreadyInUse( String strAccessCode )
    {
        return _dao.checkAccessCodeAlreadyInUse( strAccessCode );
    }

    //////////////////////////////////////////////////////////////////
    /// for no-module mode

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
}
