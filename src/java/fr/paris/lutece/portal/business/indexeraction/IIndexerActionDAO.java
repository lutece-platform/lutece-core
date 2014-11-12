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
package fr.paris.lutece.portal.business.indexeraction;

import java.util.List;


/**
 *
 * IIndexerActionDAO
 *
 */
public interface IIndexerActionDAO
{
    /**
     * Generates a new primary key
     *
     * @return The new primary key
     */
    int newPrimaryKey(  );

    /**
     * Insert a new record in the table.
     *
     * @param indexerAction instance of the IndexerAction object to insert
     */
    void insert( IndexerAction indexerAction );

    /**
     * Load the data of the IndexerAction  from the table
     *
     * @param nId The identifier of the action
     * @return the instance of the  IndexerAction
     */
    IndexerAction load( int nId );

    /**
     * Delete a record from the table
     *
     * @param nId The identifier of the action
     */
    void delete( int nId );

    /**
     * Delete a record from the table
     *
     */
    void deleteAll(  );

    /**
     * Update the indexerAction in the table
     *
     * @param indexerAction instance of the IndexerAction object to update
     */
    void store( IndexerAction indexerAction );

    /**
     * Load the data of all indexerAction and returns them in a list
     * @param filter the search filter
     * @return The List which contains the data of all action
     */
    List<IndexerAction> selectList( IndexerActionFilter filter );

    /**
     * Load the data of all indexerAction
     *
     * @return The List which contains the data of all action
     */
    List<IndexerAction> selectList(  );
}
