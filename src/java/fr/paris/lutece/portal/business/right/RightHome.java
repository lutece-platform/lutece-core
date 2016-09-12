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

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Right objects
 */
public final class RightHome
{
    //Constants
    private static final int CONSTANT_ERROR_ORDER = -2; //this value must be negative
    private static final int CONSTANT_STEP_ORDER = 1;
    private static final int CONSTANT_FIRST_ID_ORDER = 1;

    // Static variable pointed at the DAO instance
    private static IRightDAO _dao = (IRightDAO) SpringContextService.getBean( "rightDAO" );

    /**
     * Creates a new RightHome object.
     */
    private RightHome(  )
    {
    }

    /**
     * Creation of an instance of an admin right
     *
     * @param right An instance of an admin right which contains the informations to store
     * @return The instance of an admin right which has been created with its primary key.
     */
    public static Right create( Right right )
    {
        right.setOrder( getRightsList( right.getFeatureGroup(  ) ).size(  ) + CONSTANT_STEP_ORDER );
        _dao.insert( right );

        return right;
    }

    /**
     * Update of the admin right which is specified
     *
     * @param right The instance of the admin right which contains the data to store
     * @return The instance of the admin right which has been updated
     */
    public static Right update( Right right )
    {
        Right oldRight = findByPrimaryKey( right.getId(  ) );

        if ( oldRight == null )
        {
            return null;
        }

        // The feature group have changed
        if ( ( ( right.getFeatureGroup(  ) != null ) &&
                !right.getFeatureGroup(  ).equals( oldRight.getFeatureGroup(  ) ) ) ||
                ( ( right.getFeatureGroup(  ) == null ) && ( oldRight.getFeatureGroup(  ) != null ) ) )
        {
            deleteEntryFromList( oldRight.getFeatureGroup(  ), oldRight.getOrder(  ) );
            right.setOrder( getRightsList( right.getFeatureGroup(  ) ).size(  ) + CONSTANT_STEP_ORDER );
        }

        // The order have changed
        else if ( right.getOrder(  ) != oldRight.getOrder(  ) )
        {
            right.setOrder( changeRightOrder( oldRight, right.getOrder(  ) ) );
        }

        _dao.store( right );

        return right;
    }

    /**
     * Remove the admin right whose identifier is specified in parameter
     *
     * @param strId The identifier of the admin right to remove
     */
    public static void remove( String strId )
    {
        Right oldRight = findByPrimaryKey( strId );

        if ( oldRight != null )
        {
            deleteEntryFromList( oldRight.getFeatureGroup(  ), oldRight.getOrder(  ) );
        }

        _dao.delete( strId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an admin right whose identifier is specified in parameter
     *
     * @param strKey The admin right primary key
     * @return an instance of an admin right
     */
    public static Right findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey );
    }

    /**
     * Loads the data of all the rights and returns them in form of a collection
     *
     * @return the collection which contains the data of all the rights
     */
    public static Collection<Right> getRightsList(  )
    {
        return _dao.selectRightsList(  );
    }

    /**
     * Loads the data of all the rights with level greater or equal than nLevel
     * and returns them in form of a collection
     * @param nLevel The right's level
     *
     * @return the collection which contains the data of all the rights
     */
    public static Collection<Right> getRightsList( int nLevel )
    {
        return _dao.selectRightsList( nLevel );
    }

    /**
     * Loads the data of all the rights with the specified feature group
     * and returns them in form of a collection
     *
     * @param strFeatureGroup the name of the feature group
     * @return the collection which contains the data of all the rights
     */
    public static Collection<Right> getRightsList( String strFeatureGroup )
    {
        return _dao.selectRightsList( strFeatureGroup );
    }

    /**
     * Change the order in a {@link Right}
     *
     * @param right The right to update order
     * @param nNewOrder The new place in the list or END_OF_LIST to place Right at the end
     * @return The new order
     */
    public static int changeRightOrder( Right right, int nNewOrder )
    {
        if ( right == null )
        {
            return CONSTANT_ERROR_ORDER;
        }

        if ( nNewOrder < right.getOrder(  ) )
        {
            for ( Right rightGroup : getRightsList( right.getFeatureGroup(  ) ) )
            {
                int nRightToUpdateOrder = rightGroup.getOrder(  );

                if ( ( nRightToUpdateOrder >= nNewOrder ) && ( nRightToUpdateOrder < right.getOrder(  ) ) )
                {
                    rightGroup.setOrder( nRightToUpdateOrder + CONSTANT_STEP_ORDER );
                    _dao.store( rightGroup );
                }
            }
        }
        else if ( nNewOrder > right.getOrder(  ) )
        {
            for ( Right rightGroup : getRightsList( right.getFeatureGroup(  ) ) )
            {
                int nRightToUpdateOrder = rightGroup.getOrder(  );

                if ( ( nRightToUpdateOrder <= nNewOrder ) && ( nRightToUpdateOrder > right.getOrder(  ) ) )
                {
                    rightGroup.setOrder( nRightToUpdateOrder - CONSTANT_STEP_ORDER );
                    _dao.store( rightGroup );
                }
            }
        }

        return nNewOrder;
    }

    /**
     * Delete entry (specify by nOrderId)
     * @param strFeatureGroup The {@link FeatureGroup} impacted
     * @param nOrderId The order to delete
     */
    public static void deleteEntryFromList( String strFeatureGroup, int nOrderId )
    {
        for ( Right rightGroup : getRightsList( strFeatureGroup ) )
        {
            int nRightToUpdateOrder = rightGroup.getOrder(  );

            if ( ( nRightToUpdateOrder > nOrderId ) )
            {
                rightGroup.setOrder( nRightToUpdateOrder - CONSTANT_STEP_ORDER );
                _dao.store( rightGroup );
            }
        }
    }

    /**
     * Reinitialize feature order groups
     *
     * @param strFeatureGroup The feature group key
     */
    public static void reinitFeatureOrders( String strFeatureGroup )
    {
        if ( ( strFeatureGroup == null ) || strFeatureGroup.equals( "" ) )
        {
            return;
        }

        int nOrder = CONSTANT_FIRST_ID_ORDER;

        for ( Right rightGroup : getRightsList( strFeatureGroup ) )
        {
            rightGroup.setOrder( nOrder++ );
            _dao.store( rightGroup );
        }
    }

    /**
     * Check feature orders and return false if at least one order is twice
     *
     * @param strFeatureGroup The feature group key
     * @return true if order list is ok, false else.
     */
    public static boolean checkFeatureOrders( String strFeatureGroup )
    {
        if ( ( strFeatureGroup == null ) || strFeatureGroup.equals( "" ) )
        {
            return false;
        }

        int nOrder = CONSTANT_FIRST_ID_ORDER;

        for ( Right rightGroup : getRightsList( strFeatureGroup ) )
        {
            if ( nOrder != rightGroup.getOrder(  ) )
            {
                return false;
            }

            nOrder++;
        }

        return true;
    }
    
    public static Collection<Right> getExternalRightList(  ){
        return _dao.selectExternalRightsList( 0 );
    }
}
