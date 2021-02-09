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
package fr.paris.lutece.portal.business.stylesheet;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class provides Data Access methods for StyleSheet objects
 */
public final class StyleSheetDAO implements IStyleSheetDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT a.description , a.file_name , a.source , b.id_style , b.id_mode " + " FROM core_stylesheet a "
            + " LEFT JOIN core_style_mode_stylesheet b ON a.id_stylesheet = b.id_stylesheet " + " WHERE a.id_stylesheet = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_stylesheet ( description , file_name, source ) " + " VALUES ( ? ,?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_stylesheet WHERE id_stylesheet = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_stylesheet SET id_stylesheet = ?, description = ?, file_name = ?, source = ? WHERE id_stylesheet = ?  ";
    private static final String SQL_QUERY_SELECT_MODEID = " SELECT a.id_mode FROM core_mode a , core_style_mode_stylesheet b  "
            + " WHERE a.id_mode = b.id_mode AND b.id_stylesheet = ?";
    private static final String SQL_QUERY_COUNT_STYLESHEET = " SELECT count(*) FROM core_style_mode_stylesheet WHERE id_style = ? AND id_mode = ? ";
    private static final String SQL_QUERY_INSERT_STYLEMODESTYLESHEET = " INSERT INTO core_style_mode_stylesheet ( id_style , id_mode , id_stylesheet ) "
            + " VALUES ( ?, ? ,? )";
    private static final String SQL_QUERY_UPDATE_STYLEMODESTYLESHEET = " UPDATE core_style_mode_stylesheet SET id_style = ? , id_mode = ?  "
            + " WHERE id_stylesheet = ? ";
    private static final String SQL_QUERY_DELETEE_STYLEMODESTYLESHEET = " DELETE FROM core_style_mode_stylesheet WHERE id_stylesheet = ? ";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * Insert a new record in the table.
     * 
     * @param stylesheet
     *            The StyleSheet object
     */
    public void insert( StyleSheet stylesheet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {

            int nIndex = 1;
            daoUtil.setString( nIndex++, stylesheet.getDescription( ) );
            daoUtil.setString( nIndex++, stylesheet.getFile( ) );
            daoUtil.setBytes( nIndex, stylesheet.getSource( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                stylesheet.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }

            // Update of the table style_mode_stylesheet in the database
            insertStyleModeStyleSheet( stylesheet );
        }
    }

    /**
     * Load the data of Stylesheet from the table
     * 
     * @param nIdStylesheet
     *            the identifier of the Stylesheet to load
     * @return stylesheet
     */
    public StyleSheet load( int nIdStylesheet )
    {
        StyleSheet stylesheet = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nIdStylesheet );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                stylesheet = new StyleSheet( );
                stylesheet.setId( nIdStylesheet );
                stylesheet.setDescription( daoUtil.getString( 1 ) );
                stylesheet.setFile( daoUtil.getString( 2 ) );
                stylesheet.setSource( daoUtil.getBytes( 3 ) );
                stylesheet.setStyleId( daoUtil.getInt( 4 ) );
                stylesheet.setModeId( daoUtil.getInt( 5 ) );
            }

        }

        return stylesheet;
    }

    /**
     * Delete the StyleSheet from the database whose identifier is specified in parameter
     * 
     * @param nIdStylesheet
     *            the identifier of the StyleSheet to delete
     */
    public void delete( int nIdStylesheet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdStylesheet );
            daoUtil.executeUpdate( );
        }

        // delete also into style_mode_stylesheet
        deleteStyleModeStyleSheet( nIdStylesheet );
    }

    /**
     * Load the list of stylesheet
     * 
     * @param nModeId
     *            The Mode identifier
     * @return the list of the StyleSheet in form of a collection of StyleSheet objects
     */
    public Collection<StyleSheet> selectStyleSheetList( int nModeId )
    {
        Collection<StyleSheet> stylesheetList = new ArrayList<>( );

        String strSelect = " SELECT a.id_stylesheet , a.description , a.file_name ";
        String strFrom = " FROM core_stylesheet a ";

        if ( nModeId != -1 )
        {
            strFrom = " FROM  core_stylesheet a , core_style_mode_stylesheet b " + " WHERE a.id_stylesheet = b.id_stylesheet " + " AND b.id_mode = ? ";
        }

        strFrom += " ORDER BY a.description ";

        String strSQL = strSelect + strFrom;

        try ( DAOUtil daoUtil = new DAOUtil( strSQL ) )
        {

            if ( nModeId != -1 )
            {
                daoUtil.setInt( 1, nModeId );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                StyleSheet stylesheet = new StyleSheet( );

                stylesheet.setId( daoUtil.getInt( 1 ) );
                stylesheet.setDescription( daoUtil.getString( 2 ) );
                stylesheet.setFile( daoUtil.getString( 3 ) );
                stylesheetList.add( stylesheet );
            }

        }

        return stylesheetList;
    }

    /**
     * Update the record in the table
     * 
     * @param stylesheet
     *            The stylesheet
     */
    public void store( StyleSheet stylesheet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setInt( 1, stylesheet.getId( ) );
            daoUtil.setString( 2, stylesheet.getDescription( ) );
            daoUtil.setString( 3, stylesheet.getFile( ) );
            daoUtil.setBytes( 4, stylesheet.getSource( ) );
            daoUtil.setInt( 5, stylesheet.getId( ) );

            daoUtil.executeUpdate( );
        }

        // update the table style_mode_stylesheet
        updateStyleModeStyleSheet( stylesheet );
    }

    /**
     * Insert a new record in the table style_mode_stylesheet
     * 
     * @param stylesheet
     *            the instance of StyleSheet to insert in the table
     */
    private void insertStyleModeStyleSheet( StyleSheet stylesheet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_STYLEMODESTYLESHEET ) )
        {

            daoUtil.setInt( 1, stylesheet.getStyleId( ) );
            daoUtil.setInt( 2, stylesheet.getModeId( ) );
            daoUtil.setInt( 3, stylesheet.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Updates the table style_mode_stylesheet with the data of the StyleShhet instance specified in parameter
     * 
     * @param stylesheet
     *            the instance of the stylesheet to update
     */
    private void updateStyleModeStyleSheet( StyleSheet stylesheet )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_STYLEMODESTYLESHEET ) )
        {

            daoUtil.setInt( 1, stylesheet.getStyleId( ) );
            daoUtil.setInt( 2, stylesheet.getModeId( ) );
            daoUtil.setInt( 3, stylesheet.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Deletes the data in the table style_mode_stylesheet
     *
     * @param nStyleSheetId
     *            the identifier of the stylesheet
     */
    private void deleteStyleModeStyleSheet( int nStyleSheetId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETEE_STYLEMODESTYLESHEET ) )
        {
            daoUtil.setInt( 1, nStyleSheetId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Returns the number of stylesheets associated to the style and the mode specified in parameter
     * 
     * @param nStyleId
     *            the style id
     * @param nModeId
     *            the mode id
     * @return the number of stylesheet associated
     */
    public int selectStyleSheetNbPerStyleMode( int nStyleId, int nModeId )
    {
        int nCount;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_STYLESHEET ) )
        {

            daoUtil.setInt( 1, nStyleId );
            daoUtil.setInt( 2, nModeId );

            daoUtil.executeQuery( );

            if ( !daoUtil.next( ) )
            {
                daoUtil.free( );
                throw new AppException( DAOUtil.MSG_EXCEPTION_SELECT_ERROR + nModeId + " StyleId " + nStyleId );
            }

            nCount = ( daoUtil.getInt( 1 ) );

        }

        return nCount;
    }

    /**
     * Returns the identifier of the mode of the stylesheet whose identifier is specified in parameter
     * 
     * @param nIdStylesheet
     *            the identifier of the stylesheet
     * @return the identifier of the mode
     */
    public int selectModeId( int nIdStylesheet )
    {
        int nModeId;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MODEID ) )
        {

            daoUtil.setInt( 1, nIdStylesheet );
            daoUtil.executeQuery( );

            if ( !daoUtil.next( ) )
            {
                daoUtil.free( );
                throw new AppException( DAOUtil.MSG_EXCEPTION_SELECT_ERROR + nIdStylesheet );
            }

            nModeId = ( daoUtil.getInt( 1 ) );

        }

        return nModeId;
    }
}
