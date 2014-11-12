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

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for feature group  objects
 */
public final class FeatureGroupDAO implements IFeatureGroupDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT id_feature_group, feature_group_description, feature_group_label, feature_group_order " +
        " FROM core_feature_group " + " WHERE id_feature_group = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_feature_group ( id_feature_group , feature_group_description, feature_group_label, feature_group_order ) " +
        " VALUES ( ?, ?, ?, ?  )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_feature_group WHERE id_feature_group = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_feature_group SET feature_group_description = ?, " +
        " feature_group_label = ? , feature_group_order = ? " + " WHERE id_feature_group = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_feature_group, feature_group_description, feature_group_label, feature_group_order " +
        "" + " FROM core_feature_group ORDER BY feature_group_order ASC";
    private static final String SQL_QUERY_COUNT_FEATUREGROUP = " SELECT COUNT(id_feature_group)FROM core_feature_group";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     * @param featureGroup instance of the feature group to insert
     */
    public void insert( FeatureGroup featureGroup )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setString( 1, featureGroup.getId(  ) );
        daoUtil.setString( 2, featureGroup.getDescriptionKey(  ) );
        daoUtil.setString( 3, featureGroup.getLabelKey(  ) );
        daoUtil.setInt( 4, featureGroup.getOrder(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of FeatureGroup from the table
     * @param strIdFeatureGroup The indentifier of the object FeatureGroup
     * @return The Instance of the object Page
     */
    public FeatureGroup load( String strIdFeatureGroup )
    {
        FeatureGroup featureGroup = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strIdFeatureGroup );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            featureGroup = new FeatureGroup(  );
            featureGroup.setId( daoUtil.getString( 1 ) );
            featureGroup.setDescriptionKey( daoUtil.getString( 2 ) );
            featureGroup.setLabelKey( daoUtil.getString( 3 ) );
            featureGroup.setOrder( daoUtil.getInt( 4 ) );
        }

        daoUtil.free(  );

        return featureGroup;
    }

    /**
     * Delete a record from the table
     * @param strIdFeatureGroup The indentifier of the object FeatureGroup
     */
    public void delete( String strIdFeatureGroup )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strIdFeatureGroup );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param featureGroup the reference of the feature group
     */
    public void store( FeatureGroup featureGroup )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, featureGroup.getDescriptionKey(  ) );
        daoUtil.setString( 2, featureGroup.getLabelKey(  ) );
        daoUtil.setInt( 3, featureGroup.getOrder(  ) );
        daoUtil.setString( 4, featureGroup.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Loads the data of all the feature groups and returns them in form of a collection
     * @return the list which contains the data of all the feature groups
     */
    public List<FeatureGroup> selectFeatureGroupsList(  )
    {
        List<FeatureGroup> featureGroupList = new ArrayList<FeatureGroup>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            FeatureGroup featureGroup = new FeatureGroup(  );

            featureGroup.setId( daoUtil.getString( 1 ) );
            featureGroup.setDescriptionKey( daoUtil.getString( 2 ) );
            featureGroup.setLabelKey( daoUtil.getString( 3 ) );
            featureGroup.setOrder( daoUtil.getInt( 4 ) );

            featureGroupList.add( featureGroup );
        }

        daoUtil.free(  );

        return featureGroupList;
    }

    /**
     * Returns the count of groups
     * @return the count of all the feature groups
     */
    public int selectFeatureGroupsCount(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_FEATUREGROUP );

        int nCount = 0;
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }
}
