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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * AttributeFieldDAO
 *
 */
public class AttributeFieldDAO implements IAttributeFieldDAO
{
    // NEW PK
    private static final String SQL_QUERY_NEW_PK = " SELECT max(id_field) FROM core_attribute_field ";

    // NEW POSITION
    private static final String SQL_QUERY_NEW_POSITION = "SELECT MAX(field_position)" + " FROM core_attribute_field ";

    // SELECT
    private static final String SQL_QUERY_SELECT = " SELECT id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position " +
        " FROM core_attribute_field WHERE id_field = ? ";
    private static final String SQL_QUERY_SELECT_ATTRIBUTE_BY_ID_FIELD = " SELECT a.type_class_name, a.id_attribute, a.title, a.help_message, a.is_mandatory, a.attribute_position " +
        " FROM core_attribute a INNER JOIN core_attribute_field af ON a.id_attribute = af.id_attribute " +
        " WHERE af.id_field = ? ";
    private static final String SQL_QUERY_SELECT_ATTRIBUTE_FIELDS_BY_ID_ATTRIBUTE = " SELECT id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position " +
        " FROM core_attribute_field WHERE id_attribute = ? ORDER BY field_position ";

    // INSERT
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_attribute_field (id_field, id_attribute, title, DEFAULT_value, is_DEFAULT_value, height, width, max_size_enter, is_multiple, field_position) " +
        " VALUES(?,?,?,?,?,?,?,?,?,?) ";

    // UPDATE
    private static final String SQL_QUERY_UPDATE = " UPDATE core_attribute_field SET title = ?, DEFAULT_value = ?, is_DEFAULT_value = ?, height = ?, width = ?, max_size_enter = ?, is_multiple = ?, field_position = ? " +
        " WHERE id_field = ? ";

    // DELETE
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_attribute_field WHERE id_field = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID_ATTRIBUTE = " DELETE FROM core_attribute_field WHERE id_attribute = ? ";

    // NEW PK
    /**
     * Generate a new PK
     * @return The new ID
     */
    private int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
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
     * Load attribute field
     * @param nIdField ID Field
     * @return Attribute Field
     */
    @Override
    public AttributeField load( int nIdField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdField );
        daoUtil.executeQuery(  );

        AttributeField attributeField = null;

        if ( daoUtil.next(  ) )
        {
            attributeField = new AttributeField(  );
            attributeField.setIdField( daoUtil.getInt( 1 ) );

            IAttribute attribute = selectAttributeByIdField( nIdField );
            attributeField.setAttribute( attribute );
            attributeField.setTitle( daoUtil.getString( 3 ) );
            attributeField.setValue( daoUtil.getString( 4 ) );
            attributeField.setDefaultValue( daoUtil.getBoolean( 5 ) );
            attributeField.setHeight( daoUtil.getInt( 6 ) );
            attributeField.setWidth( daoUtil.getInt( 7 ) );
            attributeField.setMaxSizeEnter( daoUtil.getInt( 8 ) );
            attributeField.setMultiple( daoUtil.getBoolean( 9 ) );
            attributeField.setPosition( daoUtil.getInt( 10 ) );
        }

        daoUtil.free(  );

        return attributeField;
    }

    /**
     * Select attribute by id field
     * @param nIdField id field
     * @return user attribute
     */
    @Override
    public IAttribute selectAttributeByIdField( int nIdField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATTRIBUTE_BY_ID_FIELD );
        daoUtil.setInt( 1, nIdField );
        daoUtil.executeQuery(  );

        IAttribute attribute = null;

        if ( daoUtil.next(  ) )
        {
            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( 1 ) ).newInstance(  );
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

            attribute.setIdAttribute( daoUtil.getInt( 2 ) );
            attribute.setTitle( daoUtil.getString( 3 ) );
            attribute.setHelpMessage( daoUtil.getString( 4 ) );
            attribute.setMandatory( daoUtil.getBoolean( 5 ) );
            attribute.setPosition( daoUtil.getInt( 6 ) );
        }

        daoUtil.free(  );

        return attribute;
    }

    /**
     * Load the lists of attribute field associated to an attribute
     * @param nIdAttribute the ID attribute
     * @return the list of attribute fields
     */
    @Override
    public List<AttributeField> selectAttributeFieldsByIdAttribute( int nIdAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATTRIBUTE_FIELDS_BY_ID_ATTRIBUTE );
        daoUtil.setInt( 1, nIdAttribute );
        daoUtil.executeQuery(  );

        List<AttributeField> listAttributeFields = new ArrayList<AttributeField>(  );

        while ( daoUtil.next(  ) )
        {
            AttributeField attributeField = new AttributeField(  );
            attributeField.setIdField( daoUtil.getInt( 1 ) );

            IAttribute attribute = selectAttributeByIdField( attributeField.getIdField(  ) );
            attributeField.setAttribute( attribute );
            attributeField.setTitle( daoUtil.getString( 3 ) );
            attributeField.setValue( daoUtil.getString( 4 ) );
            attributeField.setDefaultValue( daoUtil.getBoolean( 5 ) );
            attributeField.setHeight( daoUtil.getInt( 6 ) );
            attributeField.setWidth( daoUtil.getInt( 7 ) );
            attributeField.setMaxSizeEnter( daoUtil.getInt( 8 ) );
            attributeField.setMultiple( daoUtil.getBoolean( 9 ) );
            attributeField.setPosition( daoUtil.getInt( 10 ) );
            listAttributeFields.add( attributeField );
        }

        daoUtil.free(  );

        return listAttributeFields;
    }

    /**
     * Insert a new attribute field
     * @param attributeField the attribute field
     * @return new PK
     */
    @Override
    public int insert( AttributeField attributeField )
    {
        int nNewPrimaryKey = newPrimaryKey(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, nNewPrimaryKey );
        daoUtil.setInt( 2, attributeField.getAttribute(  ).getIdAttribute(  ) );
        daoUtil.setString( 3, attributeField.getTitle(  ) );
        daoUtil.setString( 4, attributeField.getValue(  ) );
        daoUtil.setBoolean( 5, attributeField.isDefaultValue(  ) );
        daoUtil.setInt( 6, attributeField.getHeight(  ) );
        daoUtil.setInt( 7, attributeField.getWidth(  ) );
        daoUtil.setInt( 8, attributeField.getMaxSizeEnter(  ) );
        daoUtil.setBoolean( 9, attributeField.isMultiple(  ) );
        daoUtil.setInt( 10, newPosition(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return nNewPrimaryKey;
    }

    /**
     * Update an attribute field
     * @param attributeField the attribute field
     */
    @Override
    public void store( AttributeField attributeField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( 1, attributeField.getTitle(  ) );
        daoUtil.setString( 2, attributeField.getValue(  ) );
        daoUtil.setBoolean( 3, attributeField.isDefaultValue(  ) );
        daoUtil.setInt( 4, attributeField.getHeight(  ) );
        daoUtil.setInt( 5, attributeField.getWidth(  ) );
        daoUtil.setInt( 6, attributeField.getMaxSizeEnter(  ) );
        daoUtil.setBoolean( 7, attributeField.isMultiple(  ) );
        daoUtil.setInt( 8, attributeField.getPosition(  ) );
        daoUtil.setInt( 9, attributeField.getIdField(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete an attribute field
     * @param nIdField The id field
     */
    @Override
    public void delete( int nIdField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdField );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete all attribute field from an attribute id
     * @param nIdAttribute the ID attribute
     */
    @Override
    public void deleteAttributeFieldsFromIdAttribute( int nIdAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_ATTRIBUTE );
        daoUtil.setInt( 1, nIdAttribute );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
