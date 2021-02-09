/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.service.insert;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager of the InsertServices (Insert + HTML). Responsible for (un)registering them.
 */
public final class InsertServiceManager
{
    // //////////////////////////////////////////////////////////////////////////
    // Class attributes

    /** Insert Service registry */
    private static Map<String, InsertService> _mapInsertServicesRegistry = new HashMap<>( );

    // //////////////////////////////////////////////////////////////////////////
    // Methods

    /**
     * Creates a new InsertServiceManager object
     */
    private InsertServiceManager( )
    {
    }

    /**
     * Registers a new Index Service
     *
     * @param is
     *            the service
     */
    public static void registerInsertService( InsertService is )
    {
        _mapInsertServicesRegistry.put( is.getId( ), is );
        AppLogService.info( "New Insert Service registered : " + is.getId( ) );
    }

    /**
     * Unregisters a new Index Service
     *
     * @param is
     *            the service
     */
    public static void unregisterInsertService( InsertService is )
    {
        _mapInsertServicesRegistry.remove( is.getId( ) );
        AppLogService.info( "Service unregistered : " + is.getId( ) );
    }

    /**
     * Returns all registered Insert services
     *
     * @return A collection containing all registered Insert services
     */
    public static Collection<InsertService> getInsertServicesList( )
    {
        ArrayList<InsertService> listServices = new ArrayList<>( );

        for ( InsertService service : _mapInsertServicesRegistry.values( ) )
        {
            if ( service.isEnabled( ) )
            {
                listServices.add( service );
            }
        }

        return listServices;
    }

    /**
     * Get a particular Insert service
     *
     * @param strId
     *            Identifier of the seeked service
     * @return the selected Insert service
     */
    public static InsertService getInsertService( String strId )
    {
        return _mapInsertServicesRegistry.get( strId );
    }

    /**
     * Returns the names of all registered Insert Service
     *
     * @return An enumeration containing the names of all registered Insert Service
     */
    public static List<String> getInsertsLabels( )
    {
        List<String> listInserts = new ArrayList<>( );

        for ( InsertService ls : getInsertServicesList( ) )
        {
            listInserts.add( ls.getLabelKey( ) );
        }

        return listInserts;
    }
}
