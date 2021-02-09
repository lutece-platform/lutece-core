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
package fr.paris.lutece.portal.business.portlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for AliasPortlet objects
 */
public final class AliasPortletDAO implements IAliasPortletDAO
{
    // sql queries
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_portlet_alias ( id_portlet , id_alias ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_portlet_alias WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id_alias FROM core_portlet_alias WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_portlet_alias SET id_alias=? WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECT_PORTLETS_BY_TYPE = "SELECT  id_portlet, name FROM core_portlet WHERE id_portlet_type = ? ORDER BY name";
    private static final String SQL_QUERY_SELECT_ALIAS_ID = "SELECT id_alias FROM core_portlet_alias WHERE id_portlet= ? ";
    private static final String SQL_QUERY_SELECT_ACCEPT_ALIAS_PORTLET_LIST = "SELECT id_portlet, name FROM core_portlet WHERE accept_alias = 1 ";
    private static final String SQL_QUERY_SELECT_ACCEPT_ALIAS_PORTLET_LIST_DETAIL = "SELECT id_portlet, id_page, name FROM core_portlet WHERE accept_alias = 1 ORDER BY id_page";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * {@inheritDoc}
     */
    public synchronized void insert( Portlet portlet )
    {
        AliasPortlet alias = (AliasPortlet) portlet;

        // insert into the table portlet_alias
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            daoUtil.setInt( 1, alias.getId( ) );
            daoUtil.setInt( 2, alias.getAliasId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nPortletId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nPortletId );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    public Portlet load( int nIdPortlet )
    {
        AliasPortlet portlet = new AliasPortlet( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nIdPortlet );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                portlet.setAliasId( daoUtil.getInt( 1 ) );
            }

        }

        return portlet;
    }

    /**
     * {@inheritDoc}
     */
    public void store( Portlet portlet )
    {
        AliasPortlet r = (AliasPortlet) portlet;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( 1, r.getAliasId( ) );
            daoUtil.setInt( 2, portlet.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList selectPortletsByTypeList( String strPortletTypeId )
    {
        ReferenceList list = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLETS_BY_TYPE ) )
        {
            daoUtil.setString( 1, strPortletTypeId );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public int selectAliasId( int nIdPortlet )
    {
        int nAliasId = 0;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALIAS_ID ) )
        {
            daoUtil.setInt( 1, nIdPortlet );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nAliasId = daoUtil.getInt( 1 );
            }

        }

        return nAliasId;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList selectAcceptAliasPortletRefList( )
    {
        ReferenceList list = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACCEPT_ALIAS_PORTLET_LIST ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

        }

        return list;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Portlet> selectAcceptAliasPortletList( )
    {
        List<Portlet> list = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACCEPT_ALIAS_PORTLET_LIST_DETAIL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                PortletImpl portlet = new PortletImpl( );
                portlet.setId( daoUtil.getInt( 1 ) );
                portlet.setPageId( daoUtil.getInt( 2 ) );
                portlet.setName( daoUtil.getString( 3 ) );
                list.add( portlet );
            }

        }

        return list;
    }

}
