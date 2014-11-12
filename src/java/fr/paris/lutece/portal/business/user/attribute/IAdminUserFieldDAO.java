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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;

import java.util.List;


/**
 *
 * IAdminUserFieldDAO
 *
 */
public interface IAdminUserFieldDAO
{
    /**
     * Load the user field
     * @param nIdUserField ID
     * @return AdminUserField
     */
    AdminUserField load( int nIdUserField );

    /**
     * Insert a new user field
     * @param userField the user field
     */
    void insert( AdminUserField userField );

    /**
     * Update an user field
     * @param userField the user field
     */
    void store( AdminUserField userField );

    /**
     * Delete an attribute
     * @param nIdUserField the ID of the user field
     */
    void delete( int nIdUserField );

    /**
     * Delete all user fields from given id field
     * @param nIdField id field
     */
    void deleteUserFieldsFromIdField( int nIdField );

    /**
     * Delete all user fields from given id user
     * @param nIdUser id user
     */
    void deleteUserFieldsFromIdUser( int nIdUser );

    /**
     * Delete all user fields from given id attribute
     * @param nIdAttribute id attribute
     */
    void deleteUserFieldsFromIdAttribute( int nIdAttribute );

    /**
     * Load all the user field by a given ID user
     * @param nIdUser the ID user
     * @param nIdAttribute the ID attribute
     * @return a list of adminuserfield
     */
    List<AdminUserField> selectUserFieldsByIdUserIdAttribute( int nIdUser, int nIdAttribute );

    /**
     * Load users by a given filter
     * @param auFieldFilter the filter
     * @return a list of users
     */
    List<AdminUser> selectUsersByFilter( AdminUserFieldFilter auFieldFilter );

    /**
     * Select by filter.
     *
     * @param auFieldFilter the filter
     * @return the list
     */
    List<AdminUserField> selectByFilter( AdminUserFieldFilter auFieldFilter );
}
