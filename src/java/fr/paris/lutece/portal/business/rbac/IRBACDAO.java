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

import java.util.Collection;


/**
 * Interface for RBAC DAO
 */
public interface IRBACDAO
{
    /**
     * Delete a record from the table
     *
     * @param nRBACId The id of RBAC object to delete
     */
    void delete( int nRBACId );

    /**
     * Remove all the entries of the given role key
     *
     * @param strRoleKey the role key of the entries to remove
     */
    void deleteForRoleKey( String strRoleKey );

    /**
     * Insert a new record in the table.
     *
     *
     * @param rBAC The rBAC object
     */
    void insert( RBAC rBAC );

    /**
     * Load the data of RBAC from the table
     *
     *
     * @param nRBACId The identifier of RBAC
     * @return the instance of the RBAC
     */
    RBAC load( int nRBACId );

    /**
     * Load the list of rBACs
     *
     * @return The Collection of the RBACs
     */
    Collection<RBAC> selectRBACList(  );

    /**
     * Find all the entries for a given role key
     *
     * @param strRoleKey the role key to search for
     * @return A collection of rBACs
     */
    Collection<RBAC> selectRBACListByRoleKey( String strRoleKey );

    /**
     *
     *
     * @param strTypeCode The type code
     * @param strId the id
     * @param strPermission th permission
     * @return listRoleKeys
     */
    Collection<String> selectRoleKeys( String strTypeCode, String strId, String strPermission );

    /**
     * Update the record in the table
     *
     * @param rBAC The reference of rBAC
     */
    void store( RBAC rBAC );

    /**
     * Update the role key of all the entries of a given role key
     *
     * @param strOldRoleKey the role key to update
     * @param strNewRoleKey the new role key
     */
    void updateRoleKey( String strOldRoleKey, String strNewRoleKey );
}
