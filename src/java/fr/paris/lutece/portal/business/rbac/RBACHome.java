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
package fr.paris.lutece.portal.business.rbac;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for RBAC objects
 */
public final class RBACHome
{
    // Static variable pointed at the DAO instance
    private static IRBACDAO _dao = (IRBACDAO) SpringContextService.getBean( "rBACDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private RBACHome(  )
    {
    }

    /**
     * Creation of an instance of rBAC
     *
     * @param rBAC The instance of the rBAC which contains the informations to store
     * @return The  instance of rBAC which has been created with its primary key.
     */
    public static RBAC create( RBAC rBAC )
    {
        _dao.insert( rBAC );

        return rBAC;
    }

    /**
     * Update of the rBAC which is specified in parameter
     *
     * @param rBAC The instance of the rBAC which contains the data to store
     * @return The instance of the  rBAC which has been updated
     */
    public static RBAC update( RBAC rBAC )
    {
        _dao.store( rBAC );

        return rBAC;
    }

    /**
     * Remove the RBAC whose identifier is specified in parameter
     *
     * @param nKey The Primary key of the rBAC to remove
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a rBAC whose identifier is specified in parameter
     *
     * @param nKey The Primary key of the rBAC
     * @return An instance of rBAC
     */
    public static RBAC findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns a collection of rBACs objects
     * @return A collection of rBACs
     */
    public static Collection<RBAC> findAll(  )
    {
        return _dao.selectRBACList(  );
    }

    /**
     * Find all the entries for a given role key
     * @param strRoleKey the role key to search for
     * @return A collection of rBACs
     */
    public static Collection<RBAC> findResourcesByCode( String strRoleKey )
    {
        return _dao.selectRBACListByRoleKey( strRoleKey );
    }

    /**
     * Update the role key of all the entries of a given role key
     * @param strOldRoleKey the role key to update
     * @param strNewRoleKey the new role key
     */
    public static void updateRoleKey( String strOldRoleKey, String strNewRoleKey )
    {
        _dao.updateRoleKey( strOldRoleKey, strNewRoleKey );
    }

    /**
     * Gets all role keys
     * @param strTypeCode The code type
     * @param strId The ID
     * @param strPermission The permission
     * @return A collection of role keys
     */
    public static Collection<String> findRoleKeys( String strTypeCode, String strId, String strPermission )
    {
        return _dao.selectRoleKeys( strTypeCode, strId, strPermission );
    }

    /**
     * Remove all the entries of the given role key
     * @param strRoleKey the role key of the entries to remove
     */
    public static void removeForRoleKey( String strRoleKey )
    {
        _dao.deleteForRoleKey( strRoleKey );
    }
}
