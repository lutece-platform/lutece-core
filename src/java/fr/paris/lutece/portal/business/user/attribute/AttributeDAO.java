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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * AttributeDAO
 *
 */
public class AttributeDAO implements IAttributeDAO
{
    // NEW POSITION
    private static final String SQL_QUERY_NEW_POSITION = "SELECT MAX(attribute_position)" + " FROM core_attribute ";

    // SELECT
    private static final String SQL_QUERY_SELECT = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position, plugin_name "
            + " FROM core_attribute WHERE id_attribute = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position, anonymize, plugin_name "
            + " FROM core_attribute ORDER BY attribute_position ASC ";
    private static final String SQL_QUERY_SELECT_PLUGIN_ATTRIBUTES = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position "
            + " FROM core_attribute WHERE plugin_name = ? ORDER BY attribute_position ASC ";
    private static final String SQL_QUERY_SELECT_CORE_ATTRIBUTES = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position "
            + " FROM core_attribute WHERE plugin_name IS NULL OR plugin_name = '' ORDER BY attribute_position ASC ";

    // INSERT
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_attribute (type_class_name, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position)"
            + " VALUES (?,?,?,?,?,?,?,?) ";

    // UPDATE
    private static final String SQL_QUERY_UPDATE = " UPDATE core_attribute SET title = ?, help_message = ?, is_mandatory = ?, is_shown_in_search = ?, is_shown_in_result_list = ?, is_field_in_line = ?, attribute_position = ? "
            + " WHERE id_attribute = ? ";
    private static final String SQL_QUERY_UPDATE_ANONYMIZATION = " UPDATE core_attribute SET anonymize = ? WHERE id_attribute = ? ";

    // DELETE
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_attribute WHERE id_attribute = ?";

    /**
     * Generates a new field position
     * 
     * @return the new entry position
     */
    private int newPosition( )
    {
        int nPos;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_POSITION ) )
        {
            daoUtil.executeQuery( );

            if ( !daoUtil.next( ) )
            {
                // if the table is empty
                nPos = 1;
            }

            nPos = daoUtil.getInt( 1 ) + 1;
        }

        return nPos;
    }

    /**
     * Load attribute
     * 
     * @param nIdAttribute
     *            ID
     * @param locale
     *            Locale
     * @return Attribute
     */
    public IAttribute load( int nIdAttribute, Locale locale )
    {
        IAttribute attribute = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nIdAttribute );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                int nIndex = 1;

                try
                {
                    attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance( );
                }
                catch( InstantiationException | IllegalAccessException | ClassNotFoundException e )
                {
                    AppLogService.error( e );
                }

                if ( attribute != null )
                {
                    attribute.setIdAttribute( daoUtil.getInt( nIndex++ ) );
                    attribute.setTitle( daoUtil.getString( nIndex++ ) );
                    attribute.setHelpMessage( daoUtil.getString( nIndex++ ) );
                    attribute.setMandatory( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInSearch( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInResultList( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setFieldInLine( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setPosition( daoUtil.getInt( nIndex++ ) );
                    attribute.setAttributeType( locale );

                    Plugin plugin = PluginService.getPlugin( daoUtil.getString( nIndex++ ) );
                    attribute.setPlugin( plugin );
                }
            }

        }

        return attribute;
    }

    /**
     * Insert a new attribute
     * 
     * @param attribute
     *            the attribute
     * @return new PK
     */
    public int insert( IAttribute attribute )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, attribute.getClass( ).getName( ) );
            daoUtil.setString( nIndex++, attribute.getTitle( ) );
            daoUtil.setString( nIndex++, attribute.getHelpMessage( ) );
            daoUtil.setBoolean( nIndex++, attribute.isMandatory( ) );
            daoUtil.setBoolean( nIndex++, attribute.isShownInSearch( ) );
            daoUtil.setBoolean( nIndex++, attribute.isShownInResultList( ) );
            daoUtil.setBoolean( nIndex++, attribute.isFieldInLine( ) );
            daoUtil.setInt( nIndex++, newPosition( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                attribute.setIdAttribute( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }

        return attribute.getIdAttribute( );
    }

    /**
     * Update an attribute
     * 
     * @param attribute
     *            the attribute
     */
    public void store( IAttribute attribute )
    {
        int nIndex = 1;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            daoUtil.setString( nIndex++, attribute.getTitle( ) );
            daoUtil.setString( nIndex++, attribute.getHelpMessage( ) );
            daoUtil.setBoolean( nIndex++, attribute.isMandatory( ) );
            daoUtil.setBoolean( nIndex++, attribute.isShownInSearch( ) );
            daoUtil.setBoolean( nIndex++, attribute.isShownInResultList( ) );
            daoUtil.setBoolean( nIndex++, attribute.isFieldInLine( ) );
            daoUtil.setInt( nIndex++, attribute.getPosition( ) );
            daoUtil.setInt( nIndex++, attribute.getIdAttribute( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Delete an attribute
     * 
     * @param nIdAttribute
     *            The Id of the attribute
     */
    public void delete( int nIdAttribute )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdAttribute );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * Load every attributes
     * 
     * @param locale
     *            locale
     * @return list of attributes
     */
    public List<IAttribute> selectAll( Locale locale )
    {
        List<IAttribute> listAttributes = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                IAttribute attribute = null;

                try
                {
                    attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance( );
                }
                catch( InstantiationException | IllegalAccessException | ClassNotFoundException e )
                {
                    AppLogService.error( e );
                }

                if ( attribute != null )
                {
                    attribute.setIdAttribute( daoUtil.getInt( nIndex++ ) );
                    attribute.setTitle( daoUtil.getString( nIndex++ ) );
                    attribute.setHelpMessage( daoUtil.getString( nIndex++ ) );
                    attribute.setMandatory( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInSearch( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInResultList( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setFieldInLine( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setPosition( daoUtil.getInt( nIndex++ ) );
                    attribute.setAnonymize( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setAttributeType( locale );

                    Plugin plugin = PluginService.getPlugin( daoUtil.getString( nIndex++ ) );
                    attribute.setPlugin( plugin );

                    listAttributes.add( attribute );
                }
            }

        }

        return listAttributes;
    }

    /**
     * Load every attributes from plugin name
     * 
     * @param strPluginName
     *            plugin name
     * @param locale
     *            locale
     * @return list of attributes
     */
    public List<IAttribute> selectPluginAttributes( String strPluginName, Locale locale )
    {
        int nIndex = 1;
        List<IAttribute> listAttributes = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PLUGIN_ATTRIBUTES ) )
        {
            daoUtil.setString( 1, strPluginName );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                nIndex = 1;

                IAttribute attribute = null;

                try
                {
                    attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance( );
                }
                catch( InstantiationException | IllegalAccessException | ClassNotFoundException e )
                {
                    AppLogService.error( e );
                }

                if ( attribute != null )
                {
                    attribute.setIdAttribute( daoUtil.getInt( nIndex++ ) );
                    attribute.setTitle( daoUtil.getString( nIndex++ ) );
                    attribute.setHelpMessage( daoUtil.getString( nIndex++ ) );
                    attribute.setMandatory( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInSearch( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInResultList( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setFieldInLine( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setPosition( daoUtil.getInt( nIndex++ ) );
                    attribute.setAttributeType( locale );

                    Plugin plugin = PluginService.getPlugin( strPluginName );
                    attribute.setPlugin( plugin );

                    listAttributes.add( attribute );
                }
            }

        }

        return listAttributes;
    }

    /**
     * Load every attributes that do not come from a plugin
     * 
     * @param locale
     *            locale
     * @return list of attributes
     */
    public List<IAttribute> selectCoreAttributes( Locale locale )
    {
        List<IAttribute> listAttributes = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CORE_ATTRIBUTES ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                int nIndex = 1;
                IAttribute attribute = null;

                try
                {
                    attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance( );
                }
                catch( InstantiationException | IllegalAccessException | ClassNotFoundException e )
                {
                    AppLogService.error( e );
                }

                if ( attribute != null )
                {
                    attribute.setIdAttribute( daoUtil.getInt( nIndex++ ) );
                    attribute.setTitle( daoUtil.getString( nIndex++ ) );
                    attribute.setHelpMessage( daoUtil.getString( nIndex++ ) );
                    attribute.setMandatory( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInSearch( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setShownInResultList( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setFieldInLine( daoUtil.getBoolean( nIndex++ ) );
                    attribute.setPosition( daoUtil.getInt( nIndex++ ) );
                    attribute.setAttributeType( locale );

                    listAttributes.add( attribute );
                }
            }

        }

        return listAttributes;
    }

    /**
     * Update the anonymization status of the attribute.
     * 
     * @param nIdAttribute
     *            Id of the attribute
     * @param bAnonymize
     *            New value of the anonymization status. True means the attribute should be anonymize, false means it doesn't.
     */
    public void updateAttributeAnonymization( int nIdAttribute, boolean bAnonymize )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ANONYMIZATION ) )
        {
            daoUtil.setBoolean( 1, bAnonymize );
            daoUtil.setInt( 2, nIdAttribute );
            daoUtil.executeUpdate( );
        }
    }
}
