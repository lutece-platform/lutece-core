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

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Entity objects
 */
public final class DataEntityHome
{
    // Static variable pointed at the DAO instance
    private static IDataEntityDAO _dao = new DataEntityDAO(  );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DataEntityHome(  )
    {
    }

    /**
     * Create an instance of the entity class
     * @param entity The instance of the Entity which contains the informations to store
     * @return The  instance of entity which has been created with its primary key.
     */
    public static DataEntity create( DataEntity entity )
    {
        _dao.insert( entity );

        return entity;
    }

    /**
     * Update of the entity data specified in parameter
     * @param entity The instance of the Entity which contains the data to store
     * @return The instance of the  entity which has been updated
     */
    public static DataEntity update( DataEntity entity )
    {
        _dao.store( entity );

        return entity;
    }

    /**
     * Remove the entity whose identifier is specified in parameter
     * @param strKey The entity Id
     */
    public static void remove( String strKey )
    {
        _dao.delete( strKey );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a entity whose identifier is specified in parameter
     * @param strKey The entity primary key
     * @return an instance of Entity
     */
    public static DataEntity findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey );
    }

    /**
     * Load the data of all the entity objects and returns them in form of a collection
     * @return the list which contains the data of all the entity objects
     */
    public static List<DataEntity> findAll(  )
    {
        return _dao.selectEntitiesList(  );
    }
}
