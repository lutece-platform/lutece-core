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

import java.util.List;


/**
 *
 * @author LEVY
 */
public interface IFeatureGroupDAO
{
    /**
     * Delete a record from the table
     *
     * @param strIdFeatureGroup The indentifier of the object FeatureGroup
     */
    void delete( String strIdFeatureGroup );

    /**
     * Insert a new record in the table.
     *
     * @param featureGroup instance of the feature group to insert
     */
    void insert( FeatureGroup featureGroup );

    /**
     * load the data of FeatureGroup from the table
     *
     * @param strIdFeatureGroup The indentifier of the object FeatureGroup
     * @return The Instance of the object Page
     */
    FeatureGroup load( String strIdFeatureGroup );

    /**
     * Returns the count of groups
     *
     * @return the count of all the feature groups
     */
    int selectFeatureGroupsCount(  );

    /**
     * Loads the data of all the feature groups and returns them in form of a collection
     *
     * @return the list which contains the data of all the feature groups
     */
    List<FeatureGroup> selectFeatureGroupsList(  );

    /**
     * Update the record in the table
     *
     * @param featureGroup the reference of the feature group
     */
    void store( FeatureGroup featureGroup );
}
