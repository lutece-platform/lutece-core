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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

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
    // NEW PK
    private static final String SQL_QUERY_NEW_PK = " SELECT max(id_attribute) FROM core_attribute ";

    // NEW POSITION
    private static final String SQL_QUERY_NEW_POSITION = "SELECT MAX(attribute_position)" + " FROM core_attribute ";

    // SELECT
    private static final String SQL_QUERY_SELECT = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position, plugin_name " +
        " FROM core_attribute WHERE id_attribute = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position, anonymize, plugin_name " +
        " FROM core_attribute ORDER BY attribute_position ASC ";
    private static final String SQL_QUERY_SELECT_PLUGIN_ATTRIBUTES = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position " +
        " FROM core_attribute WHERE plugin_name = ? ORDER BY attribute_position ASC ";
    private static final String SQL_QUERY_SELECT_CORE_ATTRIBUTES = " SELECT type_class_name, id_attribute, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position " +
        " FROM core_attribute WHERE plugin_name IS NULL OR plugin_name = '' ORDER BY attribute_position ASC ";

    // INSERT
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_attribute (id_attribute, type_class_name, title, help_message, is_mandatory, is_shown_in_search, is_shown_in_result_list, is_field_in_line, attribute_position)" +
        " VALUES (?,?,?,?,?,?,?,?,?) ";

    // UPDATE
    private static final String SQL_QUERY_UPDATE = " UPDATE core_attribute SET title = ?, help_message = ?, is_mandatory = ?, is_shown_in_search = ?, is_shown_in_result_list = ?, is_field_in_line = ?, attribute_position = ? " +
        " WHERE id_attribute = ? ";
    private static final String SQL_QUERY_UPDATE_ANONYMIZATION = " UPDATE core_attribute SET anonymize = ? WHERE id_attribute = ? ";

    // DELETE
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_attribute WHERE id_attribute = ?";

    // NEW PK
    /**
     * Generate a new PK
     * @return The new ID
     */
    private int newPrimaryKey(  )
    {
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_NEW_PK );
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ) );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
    * Generates a new field position
    * @return the new entry position
    */
    private int newPosition(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_POSITION );
        daoUtil.executeQuery(  );

        int nPos;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nPos = 1;
        }

        nPos = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nPos;
    }

    /**
     * Load attribute
     * @param nIdAttribute ID
     * @param locale Locale
     * @return Attribute
     */
    public IAttribute load( int nIdAttribute, Locale locale )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdAttribute );
        daoUtil.executeQuery(  );

        IAttribute attribute = null;

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance(  );
            }
            catch ( ClassNotFoundException e )
            {
                // class doesn't exist
                AppLogService.error( e );
            }
            catch ( InstantiationException e )
            {
                // Class is abstract or is an interface or haven't accessible
                // builder
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                // can't access to rhe class
                AppLogService.error( e );
            }

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

        daoUtil.free(  );

        return attribute;
    }

    /**
     * Insert a new attribute
     * @param attribute the attribute
     * @return new PK
     */
    public int insert( IAttribute attribute )
    {
        int nIndex = 1;
        int nNewPrimaryKey = newPrimaryKey(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( nIndex++, nNewPrimaryKey );
        daoUtil.setString( nIndex++, attribute.getClass(  ).getName(  ) );
        daoUtil.setString( nIndex++, attribute.getTitle(  ) );
        daoUtil.setString( nIndex++, attribute.getHelpMessage(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isMandatory(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isShownInSearch(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isShownInResultList(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isFieldInLine(  ) );
        daoUtil.setInt( nIndex++, newPosition(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return nNewPrimaryKey;
    }

    /**
     * Update an attribute
     * @param attribute the attribute
     */
    public void store( IAttribute attribute )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( nIndex++, attribute.getTitle(  ) );
        daoUtil.setString( nIndex++, attribute.getHelpMessage(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isMandatory(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isShownInSearch(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isShownInResultList(  ) );
        daoUtil.setBoolean( nIndex++, attribute.isFieldInLine(  ) );
        daoUtil.setInt( nIndex++, attribute.getPosition(  ) );
        daoUtil.setInt( nIndex++, attribute.getIdAttribute(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete an attribute
     * @param nIdAttribute The Id of the attribute
     */
    public void delete( int nIdAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdAttribute );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load every attributes
     * @param locale locale
     * @return list of attributes
     */
    public List<IAttribute> selectAll( Locale locale )
    {
        List<IAttribute> listAttributes = new ArrayList<IAttribute>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance(  );
            }
            catch ( ClassNotFoundException e )
            {
                // class doesn't exist
                AppLogService.error( e );
            }
            catch ( InstantiationException e )
            {
                // Class is abstract or is an interface or haven't accessible
                // builder
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                // can't access to the class
                AppLogService.error( e );
            }

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

        daoUtil.free(  );

        return listAttributes;
    }

    /**
     * Load every attributes from plugin name
     * @param strPluginName plugin name
     * @param locale locale
     * @return list of attributes
     */
    public List<IAttribute> selectPluginAttributes( String strPluginName, Locale locale )
    {
        int nIndex = 1;
        List<IAttribute> listAttributes = new ArrayList<IAttribute>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PLUGIN_ATTRIBUTES );
        daoUtil.setString( 1, strPluginName );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nIndex = 1;

            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance(  );
            }
            catch ( ClassNotFoundException e )
            {
                // class doesn't exist
                AppLogService.error( e );
            }
            catch ( InstantiationException e )
            {
                // Class is abstract or is an interface or haven't accessible
                // builder
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                // can't access to rhe class
                AppLogService.error( e );
            }

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

        daoUtil.free(  );

        return listAttributes;
    }

    /**
     * Load every attributes that do not come from a plugin
     * @param locale locale
     * @return list of attributes
     */
    public List<IAttribute> selectCoreAttributes( Locale locale )
    {
        List<IAttribute> listAttributes = new ArrayList<IAttribute>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CORE_ATTRIBUTES );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( nIndex++ ) ).newInstance(  );
            }
            catch ( ClassNotFoundException e )
            {
                // class doesn't exist
                AppLogService.error( e );
            }
            catch ( InstantiationException e )
            {
                // Class is abstract or is an interface or haven't accessible
                // builder
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                // can't access to rhe class
                AppLogService.error( e );
            }

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

        daoUtil.free(  );

        return listAttributes;
    }

    /**
     * Update the anonymization status of the attribute.
     * @param nIdAttribute Id of the attribute
     * @param bAnonymize New value of the anonymization status. True means the
     *            attribute should be anonymize, false means it doesn't.
     */
    public void updateAttributeAnonymization( int nIdAttribute, boolean bAnonymize )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ANONYMIZATION );
        daoUtil.setBoolean( 1, bAnonymize );
        daoUtil.setInt( 2, nIdAttribute );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
