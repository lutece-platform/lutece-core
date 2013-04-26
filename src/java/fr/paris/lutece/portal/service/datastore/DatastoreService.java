/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.portal.service.datastore;

import fr.paris.lutece.portal.business.datastore.DataEntity;
import fr.paris.lutece.portal.business.datastore.DataEntityHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;


/**
 * Datastore Service
 */
public final class DatastoreService 
{
    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";
    private static AbstractCacheableService _cache = new DatastoreCacheService();

    /**
     * Private constructor
     */
    private DatastoreService(  )
    {
    }


    /**
     * Get entity
     * @param strKey The entity's key
     * @param strDefault The default value
     * @return The value
     */
    public static String getDataValue( String strKey, String strDefault )
    {
        DataEntity entity = (DataEntity) _cache.getFromCache(strKey);
        if( entity == null )
        {    
            entity = DataEntityHome.findByPrimaryKey( strKey );
            if( entity == null )
            {
                return strDefault;
            }
            _cache.putInCache( strKey, entity );
        }
        return entity.getValue(  );
    }

    /**
     * Get entity
     * @param strKey The entity's key
     * @param strValue The value
     */
    public static void setDataValue( String strKey, String strValue )
    {
        DataEntity p = new DataEntity( strKey, strValue );
        DataEntity entity = DataEntityHome.findByPrimaryKey( strKey );

        if ( entity != null )
        {
            DataEntityHome.update( p );
        }
        else
        {
            DataEntityHome.create( p );
        }
    }

}
