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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Locale;


/**
 * This class provides instances management methods (create, find, ...) for feature groups objects
 */
public final class FeatureGroupHome
{
    // Static variable pointed at the DAO instance
    private static IFeatureGroupDAO _dao = (IFeatureGroupDAO) SpringContextService.getBean( "featureGroupDAO" );
    private static final int CONSTANT_ERROR_ORDER = -2; //this value must be negative
    private static final int CONSTANT_STEP_ORDER = 1;

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
        featureGroup.setOrder( getFeatureGroupsCount(  ) + CONSTANT_STEP_ORDER );
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
        FeatureGroup oldFeatureGroup = findByPrimaryKey( featureGroup.getId(  ) );

        if ( oldFeatureGroup == null )
        {
            return null;
        }

        // The order have changed
        else if ( featureGroup.getOrder(  ) != oldFeatureGroup.getOrder(  ) )
        {
            featureGroup.setOrder( changeRightOrder( oldFeatureGroup, featureGroup.getOrder(  ) ) );
        }

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
        FeatureGroup oldFeature = findByPrimaryKey( strId );

        if ( oldFeature != null )
        {
            deleteEntryFromList( oldFeature.getOrder(  ) );
        }

        _dao.delete( strId );
    }

    /**
     * Delete entry (specify by nOrderId)
     * @param nOrderId The order to delete
     */
    public static void deleteEntryFromList( int nOrderId )
    {
        for ( FeatureGroup featureGroupChange : getFeatureGroupsList(  ) )
        {
            int nFeatureGroupToUpdateOrder = featureGroupChange.getOrder(  );

            if ( ( nFeatureGroupToUpdateOrder > nOrderId ) )
            {
                featureGroupChange.setOrder( nFeatureGroupToUpdateOrder - CONSTANT_STEP_ORDER );
                _dao.store( featureGroupChange );
            }
        }
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
     * Loads the data of all the feature groups and returns them in form of a reference list
     *
     * @return the reference list of all the feature groups
     */
    public static ReferenceList getFeatureGroupsReferenceList(  )
    {
        ReferenceList featuresGroupsReferenceList = new ReferenceList(  );
        for ( FeatureGroup featureGroup : getFeatureGroupsList(  ) )
        {
            featuresGroupsReferenceList.add( featureGroup.getReferenceItem(  ) );
        }
        
        return featuresGroupsReferenceList;
    }
    

    /**
     * Gets the count of groups
     * @return the count of groups
     */
    public static int getFeatureGroupsCount(  )
    {
        return _dao.selectFeatureGroupsCount(  );
    }

    /**
     * Change the order in a {@link Right}
     *
     * @param featureGroup The {@link FeatureGroup}
     * @param nNewOrder The new place in the list or END_OF_LIST to place Right at the end
     * @return The new order
     */
    public static int changeRightOrder( FeatureGroup featureGroup, int nNewOrder )
    {
        if ( featureGroup == null )
        {
            return CONSTANT_ERROR_ORDER;
        }

        if ( nNewOrder < featureGroup.getOrder(  ) )
        {
            for ( FeatureGroup featureGroupChange : getFeatureGroupsList(  ) )
            {
                int nFeatureGroupToUpdateOrder = featureGroupChange.getOrder(  );

                if ( ( nFeatureGroupToUpdateOrder >= nNewOrder ) &&
                        ( nFeatureGroupToUpdateOrder < featureGroup.getOrder(  ) ) )
                {
                    featureGroupChange.setOrder( nFeatureGroupToUpdateOrder + CONSTANT_STEP_ORDER );
                    _dao.store( featureGroupChange );
                }
            }
        }
        else if ( nNewOrder > featureGroup.getOrder(  ) )
        {
            for ( FeatureGroup featureGroupChange : getFeatureGroupsList(  ) )
            {
                int nFeatureGroupToUpdateOrder = featureGroupChange.getOrder(  );

                if ( ( nFeatureGroupToUpdateOrder <= nNewOrder ) &&
                        ( nFeatureGroupToUpdateOrder > featureGroup.getOrder(  ) ) )
                {
                    featureGroupChange.setOrder( nFeatureGroupToUpdateOrder - CONSTANT_STEP_ORDER );
                    _dao.store( featureGroupChange );
                }
            }
        }

        return nNewOrder;
    }
}
