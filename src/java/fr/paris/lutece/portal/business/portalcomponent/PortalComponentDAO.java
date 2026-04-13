/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.business.portalcomponent;

import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class provides Data Access methods for PortalComponent objects
 */
@ApplicationScoped
public final class PortalComponentDAO implements IPortalComponentDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT name FROM core_portal_component WHERE id_portal_component = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_portal_component ( id_portal_component, name ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_portal_component WHERE id_portal_component = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_portal_component SET id_portal_component = ?, name = ? " + " WHERE id_portal_component = ?";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * {@inheritDoc}
     */
    public void insert( PortalComponent portalComponent )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {

            daoUtil.setInt( 1, portalComponent.getId( ) );
            daoUtil.setString( 2, portalComponent.getName( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    public PortalComponent load( int nPortalComponentId )
    {
        PortalComponent portalComponent = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nPortalComponentId );

            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                portalComponent = new PortalComponent( );

                portalComponent.setId( nPortalComponentId );
                portalComponent.setName( daoUtil.getString( 1 ) );
            }

        }

        return portalComponent;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nPortalComponentId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nPortalComponentId );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void store( PortalComponent portalComponent )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {

            daoUtil.setInt( 1, portalComponent.getId( ) );
            daoUtil.setString( 2, portalComponent.getName( ) );
            daoUtil.setInt( 3, portalComponent.getId( ) );

            daoUtil.executeUpdate( );
        }
    }
}
