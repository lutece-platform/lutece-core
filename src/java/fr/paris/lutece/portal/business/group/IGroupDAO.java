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
package fr.paris.lutece.portal.business.group;

import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 *
 *
 */
public interface IGroupDAO
{
    /**
     * Delete a record from the table
     *
     * @param strGroupKey The indentifier of the object Group
     */
    void delete( String strGroupKey );

    /**
     * Insert a new record in the table.
     *
     * @param group The Instance of the object Group
     */
    void insert( Group group );

    /**
     * load the data of Group from the table
     *
     * @param strGroupKey The indentifier of the object Group
     * @return The Instance of the object Group
     */
    Group load( String strGroupKey );

    /**
     * Returns a list of all the right group
     *
     * @return A ReferenceList of group objects
     */
    ReferenceList selectGroupsList(  );

    /**
     * Load the list of groups
     * @return The Collection of the Groups
     */
    Collection<Group> selectAll(  );

    /**
     * Update the record in the table
     *
     * @param group The instance of the Group to update
     */
    void store( Group group );
}
