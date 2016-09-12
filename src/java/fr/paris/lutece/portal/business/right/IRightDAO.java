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
package fr.paris.lutece.portal.business.right;

import java.util.Collection;


/**
 * RightDAO Interface
 */
public interface IRightDAO
{
    /**
     * Delete a record from the table
     *
     * @param strIdRight string identifier of the admin right to delete
     */
    void delete( String strIdRight );

    /**
     * Insert a new record in the table.
     *
     * @param right instance of the right to insert
     */
    void insert( Right right );

    /**
     * load the data of the right from the table
     *
     * @param strId The identifier of the admin right
     * @return The instance of the admin right
     */
    Right load( String strId );

    /**
     * Loads the data of all the rights and returns them in form of a collection
     *
     * @return the collection which contains the data of all the rights
     */
    Collection<Right> selectRightsList(  );

    /**
     * Update the record in the table
     *
     * @param right the reference of the admin right
     */
    void store( Right right );
    
    /**
     * Loads the data of all the external admin features with level greater or equal than nLevel
     * and returns them in form of a collection
     *
     * @param nLevel The right level
     * @return the collection which contains the data of all the external admin features
     */
    public Collection<Right> selectExternalRightsList( int nLevel );

    /**
     * Loads the data of all the rights with level greater or equal than nLevel
     * and returns them in form of a collection
     *
     * @param nLevel The right's level
     * @return the collection which contains the data of all the rights
     */
    Collection<Right> selectRightsList( int nLevel );

    /**
     * Loads the data of all the rights with the specified feature group
     * and returns them in form of a collection
     *
     * @param strFeatureGroup the name of the feature group
     * @return the collection which contains the data of all the rights
     */
    Collection<Right> selectRightsList( String strFeatureGroup );
}
