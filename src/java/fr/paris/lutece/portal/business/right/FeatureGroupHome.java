/*
 * Copyright (c) 2002-2007, Mairie de Paris
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

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for feature groups objects
 */
public final class FeatureGroupHome
{
    // Static variable pointed at the DAO instance
    private static IFeatureGroupDAO _dao = (IFeatureGroupDAO) SpringContextService.getBean( "featureGroupDAO" );

    /**
     * Creates a new FeatureGroupHome object.
     */
    private FeatureGroupHome(  )
    {
    }

    /**
     * Creation of an instance of an feature group
     *
     * @param featureGroup An instance of an feature group which contains the informations to store
     * @return The instance of an feature group which has been created with its primary key.
     */
    public static FeatureGroup create( FeatureGroup featureGroup )
    {
        _dao.insert( featureGroup );

        return featureGroup;
    }

    /**
     * Update of the feature group which is specified
     *
     * @param featureGroup The instance of the feature group which contains the data to store
     * @return The instance of the feature group which has been updated
     */
    public static FeatureGroup update( FeatureGroup featureGroup )
    {
        _dao.store( featureGroup );

        return featureGroup;
    }

    /**
     * Remove the feature group whose identifier is specified in parameter
     *
     * @param strId The identifier of the feature group to remove
     */
    public static void remove( String strId )
    {
        _dao.delete( strId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an feature group whose identifier is specified in parameter
     *
     * @param strKey The feature group primary key
     * @return an instance of an feature group
     */
    public static FeatureGroup findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey );
    }

    /**
     * Loads the data of all the feature groups and returns them in form of a collection
     *
     * @return the collection which contains the data of all the feature groups
     */
    public static List<FeatureGroup> getFeatureGroupsList(  )
    {
        return _dao.selectFeatureGroupsList(  );
    }

    /**
     * Gets the count of groups
     * @return the count of groups
     */
    public static int getFeatureGroupsCount(  )
    {
        return _dao.selectFeatureGroupsCount(  );
    }
}
