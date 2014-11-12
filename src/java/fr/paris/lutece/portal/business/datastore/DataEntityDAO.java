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
package fr.paris.lutece.portal.business.datastore;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Entity objects
 */
public final class DataEntityDAO implements IDataEntityDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT entity_key, entity_value FROM core_datastore WHERE entity_key = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_datastore ( entity_key, entity_value ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_datastore WHERE entity_key = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_datastore SET entity_value = ? WHERE entity_key = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT entity_key, entity_value FROM core_datastore";

    /**
     * Insert a new record in the table.
     * @param entity instance of the Entity object to insert
     */
    @Override
    public void insert( DataEntity entity )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setString( 1, entity.getKey(  ) );
        daoUtil.setString( 2, entity.getValue(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the entity from the table
     * @param strKey The identifier of the entity
     * @return the instance of the Entity
     */
    @Override
    public DataEntity load( String strKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setString( 1, strKey );
        daoUtil.executeQuery(  );

        DataEntity entity = null;

        if ( daoUtil.next(  ) )
        {
            entity = new DataEntity(  );

            entity.setKey( daoUtil.getString( 1 ) );
            entity.setValue( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return entity;
    }

    /**
     * Delete a record from the table
     * @param strKey The identifier of the entity
     */
    @Override
    public void delete( String strKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setString( 1, strKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param entity The reference of the entity
     */
    @Override
    public void store( DataEntity entity )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, entity.getValue(  ) );
        daoUtil.setString( 2, entity.getKey(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the entitys and returns them as a List
     * @return The List which contains the data of all the entitys
     */
    @Override
    public List<DataEntity> selectEntitiesList(  )
    {
        List<DataEntity> entityList = new ArrayList<DataEntity>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DataEntity entity = new DataEntity(  );

            entity.setKey( daoUtil.getString( 1 ) );
            entity.setValue( daoUtil.getString( 2 ) );

            entityList.add( entity );
        }

        daoUtil.free(  );

        return entityList;
    }
}
