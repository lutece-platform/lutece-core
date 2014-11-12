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

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 *
 * AdminUserFieldDAO
 *
 */
public class AdminUserFieldDAO implements IAdminUserFieldDAO
{
    // CONSTANTS
    private static final String CONSTANT_PERCENT = "%";
    private static final String CONSTANT_OPEN_BRACKET = "(";
    private static final String CONSTANT_CLOSED_BRACKET = ")";

    // NEW PK
    private static final String SQL_QUERY_NEW_PK = " SELECT max(id_user_field) FROM core_admin_user_field ";

    // SELECT
    private static final String SQL_QUERY_SELECT = " SELECT auf.id_user_field, auf.id_user, auf.id_attribute, auf.id_field, auf.id_file, auf.user_field_value, " +
        " au.access_code, au.last_name, au.first_name, au.email, au.status, au.locale, au.level_user, " +
        " a.type_class_name, a.title, a.help_message, a.is_mandatory, a.attribute_position, " +
        " af.title, af.DEFAULT_value, af.is_DEFAULT_value, af.height, af.width, af.max_size_enter, af.is_multiple, af.field_position " +
        " FROM core_admin_user_field auf " + " INNER JOIN core_admin_user au ON auf.id_user = au.id_user " +
        " INNER JOIN core_attribute a ON auf.id_attribute = a.id_attribute " +
        " INNER JOIN core_attribute_field af ON auf.id_field = af.id_field ";
    private static final String SQL_QUERY_SELECT_USER_FIELDS_BY_ID_USER_ID_ATTRIBUTE = " SELECT auf.id_user_field, auf.id_user, auf.id_attribute, auf.id_field, auf.id_file, auf.user_field_value, " +
        " a.type_class_name, a.title, a.help_message, a.is_mandatory, a.attribute_position " +
        " FROM core_admin_user_field auf " + " INNER JOIN core_attribute a ON a.id_attribute = auf.id_attribute " +
        " WHERE auf.id_user = ? AND auf.id_attribute = ? ";
    private static final String SQL_QUERY_SELECT_USERS_BY_FILTER = " SELECT DISTINCT u.id_user, u.access_code, u.last_name, u.first_name, u.email, u.status, u.locale, u.level_user " +
        " FROM core_admin_user u INNER JOIN core_admin_user_field uf ON u.id_user = uf.id_user ";
    private static final String SQL_QUERY_SELECT_ID_USER = " SELECT id_user FROM core_admin_user_field WHERE id_attribute = ? AND id_field = ? AND user_field_value LIKE ? ";

    // INSERT
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_admin_user_field (id_user_field, id_user, id_attribute, id_field, id_file, user_field_value) " +
        " VALUES (?,?,?,?,?,?) ";

    // UPDATE
    private static final String SQL_QUERY_UPDATE = " UPDATE core_admin_user_field SET user_field_value = ? WHERE id_user_field = ? ";

    // DELETE
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_admin_user_field WHERE id_user_field = ? ";
    private static final String SQL_QUERY_DELETE_FROM_ID_FIELD = " DELETE FROM core_admin_user_field WHERE id_field = ? ";
    private static final String SQL_QUERY_DELETE_FROM_ID_USER = " DELETE FROM core_admin_user_field WHERE id_user = ? ";
    private static final String SQL_QUERY_DELETE_FROM_ID_ATTRIBUTE = " DELETE FROM core_admin_user_field WHERE id_attribute = ? ";

    // FILTER
    private static final String SQL_ID_ATTRIBUTE_AND_USER_FIELD_VALUE = " WHERE id_attribute = ? AND id_field = ? AND user_field_value LIKE ? ";
    private static final String SQL_AND_ID_USER_IN = " AND id_user IN ";
    private static final String SQL_AND_ID_USER_IN_FIRST = " AND uf.id_user IN ";
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_FILTER_ID_USER_FIELD = " WHERE auf.id_user_field = ? ";
    private static final String SQL_FILTER_ID_USER = " auf.id_user = ? ";
    private static final String SQL_FILTER_ID_ATTRIBUTE = " auf.id_attribute = ? ";
    private static final String SQL_FILTER_ID_FIELD = " auf.id_field = ? ";

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
     * Load the user field
     * @param nIdUserField ID
     * @return AdminUserField
     */
    @Override
    public AdminUserField load( int nIdUserField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT + SQL_WHERE + SQL_FILTER_ID_USER_FIELD );
        daoUtil.setInt( 1, nIdUserField );
        daoUtil.executeQuery(  );

        AdminUserField userField = null;

        if ( daoUtil.next(  ) )
        {
            userField = new AdminUserField(  );
            userField.setIdUserField( daoUtil.getInt( 1 ) );
            userField.setValue( daoUtil.getString( 6 ) );

            // USER
            AdminUser user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 2 ) );
            user.setAccessCode( daoUtil.getString( 7 ) );
            user.setLastName( daoUtil.getString( 8 ) );
            user.setFirstName( daoUtil.getString( 9 ) );
            user.setEmail( daoUtil.getString( 10 ) );
            user.setStatus( daoUtil.getInt( 11 ) );
            user.setLocale( new Locale( daoUtil.getString( 12 ) ) );
            user.setUserLevel( daoUtil.getInt( 13 ) );
            userField.setUser( user );

            // ATTRIBUTE
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( 14 ) ).newInstance(  );
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

            attribute.setIdAttribute( daoUtil.getInt( 3 ) );
            attribute.setTitle( daoUtil.getString( 15 ) );
            attribute.setHelpMessage( daoUtil.getString( 16 ) );
            attribute.setMandatory( daoUtil.getBoolean( 17 ) );
            attribute.setPosition( daoUtil.getInt( 18 ) );
            attribute.setAttributeType( new Locale( daoUtil.getString( 12 ) ) );
            userField.setAttribute( attribute );

            // ATTRIBUTEFIELD
            AttributeField attributeField = new AttributeField(  );
            attributeField.setIdField( daoUtil.getInt( 4 ) );
            attributeField.setTitle( daoUtil.getString( 19 ) );
            attributeField.setValue( daoUtil.getString( 20 ) );
            attributeField.setDefaultValue( daoUtil.getBoolean( 21 ) );
            attributeField.setHeight( daoUtil.getInt( 22 ) );
            attributeField.setWidth( daoUtil.getInt( 23 ) );
            attributeField.setMaxSizeEnter( daoUtil.getInt( 24 ) );
            attributeField.setMultiple( daoUtil.getBoolean( 25 ) );
            attributeField.setPosition( daoUtil.getInt( 26 ) );
            userField.setAttributeField( attributeField );

            // FILE
            if ( daoUtil.getObject( 5 ) != null ) // f.id_file
            {
                File file = new File(  );
                file.setIdFile( daoUtil.getInt( 5 ) ); // f.id_file
                userField.setFile( file );
            }
        }

        daoUtil.free(  );

        return userField;
    }

    /**
     * Insert a new user field
     * @param userField the user field
     */
    @Override
    public void insert( AdminUserField userField )
    {
        userField.setIdUserField( newPrimaryKey(  ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, userField.getIdUserField(  ) );
        daoUtil.setInt( 2, userField.getUser(  ).getUserId(  ) );
        daoUtil.setInt( 3, userField.getAttribute(  ).getIdAttribute(  ) );
        daoUtil.setInt( 4, userField.getAttributeField(  ).getIdField(  ) );

        if ( userField.getFile(  ) != null )
        {
            daoUtil.setInt( 5, userField.getFile(  ).getIdFile(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setString( 6, userField.getValue(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update an user field
     * @param userField the adminuser field
     */
    @Override
    public void store( AdminUserField userField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( 1, userField.getValue(  ) );
        daoUtil.setInt( 2, userField.getIdUserField(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete an attribute
     * @param nIdUserField the ID of the user field
     */
    @Override
    public void delete( int nIdUserField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nIdUserField );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete all user fields from given id field
     * @param nIdField id field
     */
    @Override
    public void deleteUserFieldsFromIdField( int nIdField )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_ID_FIELD );
        daoUtil.setInt( 1, nIdField );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete all user fields from given id user
     * @param nIdUser id user
     */
    @Override
    public void deleteUserFieldsFromIdUser( int nIdUser )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_ID_USER );
        daoUtil.setInt( 1, nIdUser );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete all user fields from given id attribute
     * @param nIdAttribute the id attribute
     */
    @Override
    public void deleteUserFieldsFromIdAttribute( int nIdAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_ID_ATTRIBUTE );
        daoUtil.setInt( 1, nIdAttribute );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load all the user field by a given ID user
     * @param nIdUser the ID user
     * @param nIdAttribute the ID attribute
     * @return a list of adminuserfield
     */
    @Override
    public List<AdminUserField> selectUserFieldsByIdUserIdAttribute( int nIdUser, int nIdAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USER_FIELDS_BY_ID_USER_ID_ATTRIBUTE );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.setInt( 2, nIdAttribute );
        daoUtil.executeQuery(  );

        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>(  );

        while ( daoUtil.next(  ) )
        {
            AdminUserField userField = new AdminUserField(  );
            userField.setIdUserField( daoUtil.getInt( 1 ) );
            userField.setValue( daoUtil.getString( 6 ) );

            // FILE
            if ( daoUtil.getObject( 5 ) != null ) // f.id_file
            {
                File file = new File(  );
                file.setIdFile( daoUtil.getInt( 5 ) ); // f.id_file
                userField.setFile( file );
            }

            // USER
            AdminUser user = new AdminUser(  );
            user.setUserId( nIdUser );
            userField.setUser( user );

            // ATTRIBUTE
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( 7 ) ).newInstance(  );
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

            attribute.setIdAttribute( nIdAttribute );
            attribute.setTitle( daoUtil.getString( 8 ) );
            attribute.setHelpMessage( daoUtil.getString( 9 ) );
            attribute.setMandatory( daoUtil.getBoolean( 10 ) );
            attribute.setPosition( daoUtil.getInt( 11 ) );
            userField.setAttribute( attribute );

            // ATTRIBUTEFIELD
            AttributeField attributeField = new AttributeField(  );
            attributeField.setIdField( daoUtil.getInt( 4 ) );
            userField.setAttributeField( attributeField );

            listUserFields.add( userField );
        }

        daoUtil.free(  );

        return listUserFields;
    }

    /**
     * Load users by a given filter
     * @param auFieldFilter the filter
     * @return a list of users
     */
    @Override
    public List<AdminUser> selectUsersByFilter( AdminUserFieldFilter auFieldFilter )
    {
        List<AdminUserField> listUserFields = auFieldFilter.getListUserFields(  );

        if ( ( listUserFields == null ) || ( listUserFields.size(  ) == 0 ) )
        {
            return null;
        }

        List<AdminUser> listUsers = new ArrayList<AdminUser>(  );
        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_USERS_BY_FILTER );

        for ( int i = 1; i <= listUserFields.size(  ); i++ )
        {
            if ( i == 1 )
            {
                sbSQL.append( SQL_ID_ATTRIBUTE_AND_USER_FIELD_VALUE );
            }
            else
            {
                sbSQL.append( CONSTANT_OPEN_BRACKET + SQL_QUERY_SELECT_ID_USER );
            }

            if ( ( i != listUserFields.size(  ) ) && ( i != 1 ) )
            {
                sbSQL.append( SQL_AND_ID_USER_IN );
            }
            else if ( ( i != listUserFields.size(  ) ) && ( i == 1 ) )
            {
                sbSQL.append( SQL_AND_ID_USER_IN_FIRST );
            }
        }

        for ( int i = 2; i <= listUserFields.size(  ); i++ )
        {
            sbSQL.append( CONSTANT_CLOSED_BRACKET );
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ) );

        int nbCount = 1;

        for ( AdminUserField userField : listUserFields )
        {
            daoUtil.setInt( nbCount++, userField.getAttribute(  ).getIdAttribute(  ) );
            daoUtil.setInt( nbCount++, userField.getAttributeField(  ).getIdField(  ) );
            daoUtil.setString( nbCount++, CONSTANT_PERCENT + userField.getValue(  ) + CONSTANT_PERCENT );
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminUser user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 1 ) );
            user.setAccessCode( daoUtil.getString( 2 ) );
            user.setLastName( daoUtil.getString( 3 ) );
            user.setFirstName( daoUtil.getString( 4 ) );
            user.setEmail( daoUtil.getString( 5 ) );
            user.setStatus( daoUtil.getInt( 6 ) );

            Locale locale = new Locale( daoUtil.getString( 7 ) );
            user.setLocale( locale );
            listUsers.add( user );
        }

        daoUtil.free(  );

        return listUsers;
    }

    /**
     * select by filter.
     *
     * @param auFieldFilter the filter
     * @return the list
     */
    @Override
    public List<AdminUserField> selectByFilter( AdminUserFieldFilter auFieldFilter )
    {
        List<AdminUserField> listUserFields = new ArrayList<AdminUserField>(  );
        List<String> listFilter = new ArrayList<String>(  );

        if ( auFieldFilter.containsIdAttribute(  ) )
        {
            listFilter.add( SQL_FILTER_ID_ATTRIBUTE );
        }

        if ( auFieldFilter.containsIdUser(  ) )
        {
            listFilter.add( SQL_FILTER_ID_USER );
        }

        if ( auFieldFilter.containsIdField(  ) )
        {
            listFilter.add( SQL_FILTER_ID_FIELD );
        }

        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT );

        if ( listFilter.size(  ) > 0 )
        {
            boolean bIsFirst = true;

            for ( String filter : listFilter )
            {
                if ( bIsFirst )
                {
                    sbSQL.append( SQL_WHERE );
                    bIsFirst = false;
                }
                else
                {
                    sbSQL.append( SQL_AND );
                }

                sbSQL.append( filter );
            }
        }

        DAOUtil daoUtil = new DAOUtil( sbSQL.toString(  ) );
        int nIndex = 1;

        if ( auFieldFilter.containsIdAttribute(  ) )
        {
            daoUtil.setInt( nIndex++, auFieldFilter.getIdAttribute(  ) );
        }

        if ( auFieldFilter.containsIdUser(  ) )
        {
            daoUtil.setInt( nIndex++, auFieldFilter.getIdUser(  ) );
        }

        if ( auFieldFilter.containsIdField(  ) )
        {
            daoUtil.setInt( nIndex++, auFieldFilter.getIdField(  ) );
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AdminUserField userField = new AdminUserField(  );
            userField.setIdUserField( daoUtil.getInt( 1 ) );
            userField.setValue( daoUtil.getString( 6 ) );

            // USER
            AdminUser user = new AdminUser(  );
            user.setUserId( daoUtil.getInt( 2 ) );
            user.setAccessCode( daoUtil.getString( 7 ) );
            user.setLastName( daoUtil.getString( 8 ) );
            user.setFirstName( daoUtil.getString( 9 ) );
            user.setEmail( daoUtil.getString( 10 ) );
            user.setStatus( daoUtil.getInt( 11 ) );
            user.setLocale( new Locale( daoUtil.getString( 12 ) ) );
            user.setUserLevel( daoUtil.getInt( 13 ) );
            userField.setUser( user );

            // ATTRIBUTE
            IAttribute attribute = null;

            try
            {
                attribute = (IAttribute) Class.forName( daoUtil.getString( 14 ) ).newInstance(  );
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

            attribute.setIdAttribute( daoUtil.getInt( 3 ) );
            attribute.setTitle( daoUtil.getString( 15 ) );
            attribute.setHelpMessage( daoUtil.getString( 16 ) );
            attribute.setMandatory( daoUtil.getBoolean( 17 ) );
            attribute.setPosition( daoUtil.getInt( 18 ) );
            attribute.setAttributeType( new Locale( daoUtil.getString( 12 ) ) );
            userField.setAttribute( attribute );

            // ATTRIBUTEFIELD
            AttributeField attributeField = new AttributeField(  );
            attributeField.setIdField( daoUtil.getInt( 4 ) );
            attributeField.setTitle( daoUtil.getString( 19 ) );
            attributeField.setValue( daoUtil.getString( 20 ) );
            attributeField.setDefaultValue( daoUtil.getBoolean( 21 ) );
            attributeField.setHeight( daoUtil.getInt( 22 ) );
            attributeField.setWidth( daoUtil.getInt( 23 ) );
            attributeField.setMaxSizeEnter( daoUtil.getInt( 24 ) );
            attributeField.setMultiple( daoUtil.getBoolean( 25 ) );
            attributeField.setPosition( daoUtil.getInt( 26 ) );
            userField.setAttributeField( attributeField );

            // FILE
            if ( daoUtil.getObject( 5 ) != null ) // f.id_file
            {
                File file = new File(  );
                file.setIdFile( daoUtil.getInt( 5 ) ); // f.id_file
                userField.setFile( file );
            }

            listUserFields.add( userField );
        }

        daoUtil.free(  );

        return listUserFields;
    }
}
