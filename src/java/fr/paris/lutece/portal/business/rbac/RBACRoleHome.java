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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for RBACRole objects
 */
public final class RBACRoleHome
{
    // Static variable pointed at the DAO instance
    private static IRBACRoleDAO _dao = SpringContextService.getBean( "rbacRoleDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private RBACRoleHome( )
    {
    }

    /**
     * Creation of an instance of role
     *
     * @param role
     *            The instance of the role which contains the informations to store
     * @return The instance of role which has been created with its primary key.
     */
    public static RBACRole create( RBACRole role )
    {
        _dao.insert( role );

        return role;
    }

    /**
     * Update of the role which is specified in parameter
     * 
     * @param strRoleKey
     *            the key of the role to update
     * @param role
     *            The instance of the role which contains the new data to store
     * @return The instance of the role which has been updated
     */
    public static RBACRole update( String strRoleKey, RBACRole role )
    {
        _dao.store( strRoleKey, role );

        return role;
    }

    /**
     * Remove the RBACRole whose identifier is specified in parameter
     *
     * @param strRoleKey
     *            The RBACRole object to remove
     */
    public static void remove( String strRoleKey )
    {
        _dao.delete( strRoleKey );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a role whose identifier is specified in parameter
     *
     * @param strRoleKey
     *            The Primary key of the role
     * @return An instance of role
     */
    public static RBACRole findByPrimaryKey( String strRoleKey )
    {
        return _dao.load( strRoleKey );
    }

    /**
     * Returns a collection of roles objects
     *
     * @return A collection of roles
     */
    public static Collection<RBACRole> findAll( )
    {
        return _dao.selectRoleList( );
    }

    /**
     * Returns a reference of roles objects
     *
     * @return A collection of roles
     */
    public static ReferenceList getRolesList( )
    {
        ReferenceList list = new ReferenceList( );
        Collection<RBACRole> listRoles = _dao.selectRoleList( );

        for ( RBACRole role : listRoles )
        {
            list.addItem( role.getKey( ), role.getDescription( ) );
        }

        return list;
    }

    /**
     * Check that the given key points to an existing role
     *
     * @param strRoleKey
     *            The role key
     * @return true if the role exists, false otherwise
     */
    public static boolean checkExistRole( String strRoleKey )
    {
        return _dao.checkExistRole( strRoleKey );
    }
}
