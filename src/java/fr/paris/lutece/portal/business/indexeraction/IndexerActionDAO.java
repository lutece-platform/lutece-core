/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.business.indexeraction;

import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Indexer Action objects
 */
public final class IndexerActionDAO implements IIndexerActionDAO
{
    // Constants
    /**
     * SQL Where constant
     */
    public static final String CONSTANT_WHERE = " WHERE ";

    /**
     * SQL And constant
     */
    public static final String CONSTANT_AND = " AND ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_action,id_document,id_task,indexer_name, id_portlet"
            + " FROM core_indexer_action WHERE id_action = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_indexer_action( id_document,id_task ,indexer_name,id_portlet)" + " VALUES(?,?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_indexer_action WHERE id_action = ? ";
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM core_indexer_action";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_indexer_action SET id_action=?,id_document=?,id_task=?,indexer_name=?,id_portlet=? WHERE id_action = ? ";
    private static final String SQL_QUERY_SELECT = "SELECT id_action,id_document,id_task,indexer_name,id_portlet" + " FROM core_indexer_action  ";
    private static final String SQL_FILTER_ID_TASK = " id_task = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( IndexerAction indexerAction )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, indexerAction.getIdDocument( ) );
            daoUtil.setInt( nIndex++, indexerAction.getIdTask( ) );
            daoUtil.setString( nIndex++, indexerAction.getIndexerName( ) );
            daoUtil.setInt( nIndex, indexerAction.getIdPortlet( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                indexerAction.setIdAction( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IndexerAction load( int nId )
    {
        IndexerAction indexerAction = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                indexerAction = new IndexerAction( );
                indexerAction.setIdAction( daoUtil.getInt( 1 ) );
                indexerAction.setIdDocument( daoUtil.getString( 2 ) );
                indexerAction.setIdTask( daoUtil.getInt( 3 ) );
                indexerAction.setIndexerName( daoUtil.getString( 4 ) );
                indexerAction.setIdPortlet( daoUtil.getInt( 5 ) );
            }

        }

        return indexerAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAll( )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL ) )
        {
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( IndexerAction indexerAction )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( 1, indexerAction.getIdAction( ) );
            daoUtil.setString( 2, indexerAction.getIdDocument( ) );
            daoUtil.setInt( 3, indexerAction.getIdTask( ) );
            daoUtil.setString( 4, indexerAction.getIndexerName( ) );
            daoUtil.setInt( 5, indexerAction.getIdPortlet( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IndexerAction> selectList( IndexerActionFilter filter )
    {
        List<IndexerAction> indexerActionList = new ArrayList<>( );
        IndexerAction indexerAction = null;
        List<String> listStrFilter = new ArrayList<>( );

        if ( filter.containsIdTask( ) )
        {
            listStrFilter.add( SQL_FILTER_ID_TASK );
        }

        String strSQL = buildRequestWithFilter( SQL_QUERY_SELECT, listStrFilter, null );

        try ( DAOUtil daoUtil = new DAOUtil( strSQL ) )
        {

            int nIndex = 1;

            if ( filter.containsIdTask( ) )
            {
                daoUtil.setInt( nIndex, filter.getIdTask( ) );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                indexerAction = new IndexerAction( );
                indexerAction.setIdAction( daoUtil.getInt( 1 ) );
                indexerAction.setIdDocument( daoUtil.getString( 2 ) );
                indexerAction.setIdTask( daoUtil.getInt( 3 ) );
                indexerAction.setIndexerName( daoUtil.getString( 4 ) );
                indexerAction.setIdPortlet( daoUtil.getInt( 5 ) );
                indexerActionList.add( indexerAction );
            }

        }

        return indexerActionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IndexerAction> selectList( )
    {
        List<IndexerAction> indexerActionList = new ArrayList<>( );
        IndexerAction indexerAction = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                indexerAction = new IndexerAction( );
                indexerAction.setIdAction( daoUtil.getInt( 1 ) );
                indexerAction.setIdDocument( daoUtil.getString( 2 ) );
                indexerAction.setIdTask( daoUtil.getInt( 3 ) );
                indexerAction.setIndexerName( daoUtil.getString( 4 ) );
                indexerAction.setIdPortlet( daoUtil.getInt( 5 ) );
                indexerActionList.add( indexerAction );
            }

        }

        return indexerActionList;
    }

    /**
     * Builds a query with filters placed in parameters
     * 
     * @param strSelect
     *            the select of the query
     * @param listStrFilter
     *            the list of filter to add in the query
     * @param strOrder
     *            the order by of the query
     * @return a query
     */
    public static String buildRequestWithFilter( String strSelect, List<String> listStrFilter, String strOrder )
    {
        StringBuilder strBuffer = new StringBuilder( );
        strBuffer.append( strSelect );

        int nCount = 0;

        for ( String strFilter : listStrFilter )
        {
            if ( ++nCount == 1 )
            {
                strBuffer.append( CONSTANT_WHERE );
            }

            strBuffer.append( strFilter );

            if ( nCount != listStrFilter.size( ) )
            {
                strBuffer.append( CONSTANT_AND );
            }
        }

        if ( strOrder != null )
        {
            strBuffer.append( strOrder );
        }

        return strBuffer.toString( );
    }
}
