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
package fr.paris.lutece.data.dao;

import java.util.Collection;

/**
 * Generic Data Access Object (DAO) interface for CRUD operations on entities.
 * <p>
 * Provides methods to insert, update, delete, load, and select all entities of type {@code T} identified by {@code ID}.
 *
 * @param <T>
 *            the entity type
 * @param <ID>
 *            the type of the entity's identifier
 */
public interface IGenericDAO<T, ID>
{
    /**
     * Inserts a new entity into the data store.
     *
     * @param paramT
     *            the entity to insert
     */
    void insert( T paramT );

    /**
     * Updates an existing entity in the data store.
     *
     * @param paramT
     *            the entity to update
     */
    void store( T paramT );

    /**
     * Deletes an entity by its identifier.
     *
     * @param paramID
     *            the identifier of the entity to delete
     */
    void delete( ID paramID );

    /**
     * Loads an entity by its identifier.
     *
     * @param paramID
     *            the identifier of the entity to load
     * @return the entity if found, or {@code null} if not found
     */
    T load( ID paramID );

    /**
     * Selects all entities from the data store.
     *
     * @return a collection of all entities
     */
    Collection<T> selectAll( );
}
