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
import java.util.Collection;


/**
 * This class provides Data Access methods for right objects
 */
public final class RightDAO implements IRightDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT id_right, name, level_right, " +
        " admin_url, description, plugin_name, id_feature_group, icon_url, documentation_url, id_order, is_external_feature " +
        " FROM core_admin_right " + " WHERE id_right = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_admin_right ( id_right , name, level_right, admin_url , " +
        " description, plugin_name, id_feature_group, icon_url, documentation_url, id_order, is_external_feature ) " +
        " VALUES ( ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_admin_right SET name = ?, admin_url = ? , description = ? , " +
        " plugin_name = ?, id_feature_group = ?, icon_url = ?, level_right = ?, documentation_url = ?, id_order = ?, is_external_feature = ? WHERE id_right = ?";
    private static final String SQL_QUERY_SELECTALL_EXTERNAL_FEATURES = " SELECT id_right, name, level_right, admin_url, description, plugin_name, id_feature_group, icon_url, documentation_url, id_order, is_external_feature " +
        " FROM core_admin_right WHERE level_right >= ? AND is_external_feature = 1 ORDER BY id_order ASC, id_right ASC";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_right, name, level_right, admin_url, description, plugin_name, id_feature_group, icon_url, documentation_url, id_order, is_external_feature " +
        " FROM core_admin_right WHERE level_right >= ? ORDER BY id_order ASC, id_right ASC";
    private static final String SQL_QUERY_SELECTALL_FOR_FEATUREGROUP = " SELECT id_right, name, level_right, admin_url, description, plugin_name, id_feature_group, icon_url, documentation_url, id_order, is_external_feature " +
        " FROM core_admin_right WHERE id_feature_group = ? ORDER BY id_order ASC, id_right ASC";
    private static final String SQL_QUERY_SELECTALL_FOR_FEATUREGROUP_IS_NULL = " SELECT id_right, name, level_right, admin_url, description, plugin_name, id_feature_group, icon_url, documentation_url, id_order, is_external_feature " +
        " FROM core_admin_right WHERE id_feature_group IS null ORDER BY id_order ASC, id_right ASC";
    private static final String SQL_QUERY_DELETE_USERRIGHT = " DELETE FROM core_user_right WHERE id_right = ?";
    private static final String SQL_QUERY_DELETE_ADMINRIGHT = " DELETE FROM core_admin_right WHERE id_right = ?";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     * @param right instance of the right to insert
     */
    public void insert( Right right )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setString( 1, right.getId(  ) );
        daoUtil.setString( 2, right.getNameKey(  ) );
        daoUtil.setInt( 3, right.getLevel(  ) );
        daoUtil.setString( 4, right.getUrl(  ) );
        daoUtil.setString( 5, right.getDescriptionKey(  ) );
        daoUtil.setString( 6, right.getPluginName(  ) );
        daoUtil.setString( 7, right.getFeatureGroup(  ) );
        daoUtil.setString( 8, right.getIconUrl(  ) );
        daoUtil.setString( 9, right.getDocumentationUrl(  ) );
        daoUtil.setInt( 10, right.getOrder(  ) );
        daoUtil.setBoolean( 11, right.isExternalFeature(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of the right from the table
     * @param strId The identifier of the admin right
     * @return The instance of the admin right
     */
    public Right load( String strId )
    {
        Right right = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            right = new Right(  );
            right.setId( daoUtil.getString( 1 ) );
            right.setNameKey( daoUtil.getString( 2 ) );
            right.setLevel( daoUtil.getInt( 3 ) );
            right.setUrl( daoUtil.getString( 4 ) );
            right.setDescriptionKey( daoUtil.getString( 5 ) );
            right.setPluginName( daoUtil.getString( 6 ) );
            right.setFeatureGroup( daoUtil.getString( 7 ) );
            right.setIconUrl( daoUtil.getString( 8 ) );
            right.setDocumentationUrl( daoUtil.getString( 9 ) );
            right.setOrder( daoUtil.getInt( 10 ) );
            right.setExternalFeature( daoUtil.getBoolean( 11 ));
        }

        daoUtil.free(  );

        return right;
    }

    /**
     * Delete a record from the table
     * @param strIdRight string identifier of the admin right to delete
     */
    public void delete( String strIdRight )
    {
        // Clear user right
        DAOUtil daoUtilUser = new DAOUtil( SQL_QUERY_DELETE_USERRIGHT );
        daoUtilUser.setString( 1, strIdRight );
        daoUtilUser.executeUpdate(  );
        daoUtilUser.free(  );

        // Clear admin right
        DAOUtil daoUtilAdmin = new DAOUtil( SQL_QUERY_DELETE_ADMINRIGHT );
        daoUtilAdmin.setString( 1, strIdRight );
        daoUtilAdmin.executeUpdate(  );
        daoUtilAdmin.free(  );
    }

    /**
     * Update the record in the table
     * @param right the reference of the admin right
     */
    public void store( Right right )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, right.getNameKey(  ) );
        daoUtil.setString( 2, right.getUrl(  ) );
        daoUtil.setString( 3, right.getDescriptionKey(  ) );
        daoUtil.setString( 4, right.getPluginName(  ) );
        daoUtil.setString( 5, right.getFeatureGroup(  ) );
        daoUtil.setString( 6, right.getIconUrl(  ) );
        daoUtil.setInt( 7, right.getLevel(  ) );
        daoUtil.setString( 8, right.getDocumentationUrl(  ) );
        daoUtil.setInt( 9, right.getOrder(  ) );
        daoUtil.setBoolean( 10, right.isExternalFeature(  ) );
        daoUtil.setString( 11, right.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Loads the data of all the rights and returns them in form of a collection
     * @return the collection which contains the data of all the rights
     */
    public Collection<Right> selectRightsList(  )
    {
        return selectRightsList( 0 );
    }

    /**
     * Loads the data of all the rights with level greater or equal than nLevel
     * and returns them in form of a collection
     *
     * @param nLevel The right level
     * @return the collection which contains the data of all the rights
     */
    public Collection<Right> selectRightsList( int nLevel )
    {
        Collection<Right> rightList = new ArrayList<Right>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.setInt( 1, nLevel );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Right right = new Right(  );

            right.setId( daoUtil.getString( 1 ) );
            right.setNameKey( daoUtil.getString( 2 ) );
            right.setLevel( daoUtil.getInt( 3 ) );
            right.setUrl( daoUtil.getString( 4 ) );
            right.setDescriptionKey( daoUtil.getString( 5 ) );
            right.setPluginName( daoUtil.getString( 6 ) );
            right.setFeatureGroup( daoUtil.getString( 7 ) );
            right.setIconUrl( daoUtil.getString( 8 ) );
            right.setDocumentationUrl( daoUtil.getString( 9 ) );
            right.setOrder( daoUtil.getInt( 10 ) );
            right.setExternalFeature( daoUtil.getBoolean( 11 ) );

            rightList.add( right );
        }

        daoUtil.free(  );

        return rightList;
    }
    
    /**
     * Loads the data of all the external admin features and returns them in form of a collection
     * @return the collection which contains the data of all the external admin features
     */
    public Collection<Right> selectExternalRightsList(  )
    {
        return selectExternalRightsList( 0 );
    }
    

    /**
     * Loads the data of all the external admin features with level greater or equal than nLevel
     * and returns them in form of a collection
     *
     * @param nLevel The right level
     * @return the collection which contains the data of all the external admin features
     */
    @Override
    public Collection<Right> selectExternalRightsList( int nLevel )
    {
        Collection<Right> rightList = new ArrayList<Right>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_EXTERNAL_FEATURES );
        daoUtil.setInt( 1, nLevel );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Right right = new Right(  );

            right.setId( daoUtil.getString( 1 ) );
            right.setNameKey( daoUtil.getString( 2 ) );
            right.setLevel( daoUtil.getInt( 3 ) );
            right.setUrl( daoUtil.getString( 4 ) );
            right.setDescriptionKey( daoUtil.getString( 5 ) );
            right.setPluginName( daoUtil.getString( 6 ) );
            right.setFeatureGroup( daoUtil.getString( 7 ) );
            right.setIconUrl( daoUtil.getString( 8 ) );
            right.setDocumentationUrl( daoUtil.getString( 9 ) );
            right.setOrder( daoUtil.getInt( 10 ) );
            right.setExternalFeature( daoUtil.getBoolean( 11 ) );

            rightList.add( right );
        }

        daoUtil.free(  );

        return rightList;
    }

    /**
     * Loads the data of all the rights with the specified feature group
     * and returns them in form of a collection
     *
     * @param strFeatureGroup the name of the feature group
     * @return the collection which contains the data of all the rights
     */
    public Collection<Right> selectRightsList( String strFeatureGroup )
    {
        Collection<Right> rightList = new ArrayList<Right>(  );
        String strQuery = SQL_QUERY_SELECTALL_FOR_FEATUREGROUP;

        if ( strFeatureGroup == null )
        {
            strQuery = SQL_QUERY_SELECTALL_FOR_FEATUREGROUP_IS_NULL;
        }

        DAOUtil daoUtil = new DAOUtil( strQuery );

        if ( strFeatureGroup != null )
        {
            daoUtil.setString( 1, strFeatureGroup );
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Right right = new Right(  );

            right.setId( daoUtil.getString( 1 ) );
            right.setNameKey( daoUtil.getString( 2 ) );
            right.setLevel( daoUtil.getInt( 3 ) );
            right.setUrl( daoUtil.getString( 4 ) );
            right.setDescriptionKey( daoUtil.getString( 5 ) );
            right.setPluginName( daoUtil.getString( 6 ) );
            right.setFeatureGroup( daoUtil.getString( 7 ) );
            right.setIconUrl( daoUtil.getString( 8 ) );
            right.setDocumentationUrl( daoUtil.getString( 9 ) );
            right.setOrder( daoUtil.getInt( 10 ) );
            right.setExternalFeature( daoUtil.getBoolean( 11 ) );

            rightList.add( right );
        }

        daoUtil.free(  );

        return rightList;
    }
}
