/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.business.datastore;

import java.util.List;

/**
 * IDataEntityDAO Interface
 */
public interface IDataEntityDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param entity
     *            instance of the Entity object to insert
     */
    void insert( DataEntity entity );

    /**
     * Update the record in the table
     * 
     * @param entity
     *            the reference of the Entity
     */
    void store( DataEntity entity );

    /**
     * Delete a record from the table
     * 
     * @param strKey
     *            The identifier of the Entity to delete
     */
    void delete( String strKey );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param strKey
     *            The identifier of the property
     * @return The instance of the property
     */
    DataEntity load( String strKey );

    /**
     * Load the data of all entities and returns them as a List
     * 
     * @return The List which contains the data of all the property objects
     */
    List<DataEntity> selectEntitiesList( );

    /**
     * Load the data of all the entity objects whose key share a prefix
     * 
     * @param strPrefix
     *            the prefix
     * @return the list which contains the data of all the entity objects whose
     *         key share a prefix
     * @since 7.0.17
     */
    List<DataEntity> selectEntitiesByPrefix( String strPrefix );
}
