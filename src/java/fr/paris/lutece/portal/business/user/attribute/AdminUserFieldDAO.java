/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import java.util.Locale;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * 
 * AdminUserFieldDAO
 *
 */
public class AdminUserFieldDAO implements IAdminUserFieldDAO
{
	// NEW PK
	private static final String SQL_QUERY_NEW_PK = " SELECT max(id_user_field) FROM core_admin_user_field ";
	
	// SELECT
	private static final String SQL_QUERY_SELECT = " SELECT auf.id_user_field, auf.id_user, auf.id_attribute, auf.id_field, auf.user_field_value, " +
			" au.access_code, au.last_name, au.first_name, au.email, au.status, au.locale, au.level_user, " +
			" a.type_class_name, a.title, a.help_message, a.is_mandatory, a.attribute_position, " +
			" af.title, af.DEFAULT_value, af.is_DEFAULT_value, af.field_position " +
			" FROM core_admin_user_field auf " +
			" INNER JOIN core_admin_user au au ON auf.id_user = au.id_user " +
			" INNER JOIN core_attribute a ON auf.id_attribute = a.id_attribute " +
			" INNER JOIN core_attribute_field af ON auf.id_field = af.id_field " +
			" WHERE auf.id_user_field = ? ";
	
	// INSERT
	private static final String SQL_QUERY_INSERT = " INSERT INTO core_admin_user_field (id_user_field, id_user, id_attribute, id_field, user_field_value " +
			" VALUES (?,?,?,?,?) ";
	
	// UPDATE
	private static final String SQL_QUERY_UPDATE = " UPDATE core_admin_user_field SET user_field_value = ? WHERE id_user_field = ? ";
	
	// DELETE
	private static final String SQL_QUERY_DELETE = " DELETE FROM core_admin_user_field WHERE id_user_field = ? ";
	private static final String SQL_QUERY_DELETE_FROM_ID_FIELD = " DELETE FROM core_admin_user_field WHERE id_field = ? ";
	private static final String SQL_QUERY_DELETE_FROM_ID_USER = " DELETE FROM core_admin_user_field WHERE id_user = ? ";
	private static final String SQL_QUERY_DELETE_FROM_ID_ATTRIBUTE = " DELETE FROM core_admin_user_field WHERE id_attribute = ? ";
	
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
	public AdminUserField load( int nIdUserField )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdUserField );
        daoUtil.executeQuery(  );
        
        AdminUserField userField = null;
        if ( daoUtil.next(  ) )
        {
        	userField = new AdminUserField(  );
        	userField.setValue( daoUtil.getString( 5 ) );
        	
        	// USER
        	AdminUser user = new AdminUser(  );
        	user.setUserId( daoUtil.getInt( 2 ) );
        	user.setAccessCode( daoUtil.getString( 6 ) );
        	user.setLastName( daoUtil.getString( 7 ) );
        	user.setFirstName( daoUtil.getString( 8 ) );
        	user.setEmail( daoUtil.getString( 9 ) );
        	user.setStatus( daoUtil.getInt( 10 ) );
        	user.setLocale( new Locale( daoUtil.getString( 11 ) ) );
        	user.setUserLevel( daoUtil.getInt( 12 ) );
        	userField.setUser( user );
        	
        	// ATTRIBUTE
        	AbstractAttribute attribute = null;
        	try
            {
        		attribute = (AbstractAttribute) Class.forName( daoUtil.getString( 13 ) ).newInstance(  );
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
            attribute.setIdAttribute( daoUtil.getInt( 3 ) );
            attribute.setTitle( daoUtil.getString( 14 ) );
            attribute.setHelpMessage( daoUtil.getString( 15 ) );
            attribute.setMandatory( daoUtil.getBoolean( 16 ) );
            attribute.setPosition( daoUtil.getInt( 17 ) );
            attribute.setAttributeType( new Locale( daoUtil.getString( 11 ) ) );
            userField.setAttribute( attribute );
            
            // ATTRIBUTEFIELD
            AttributeField attributeField = new AttributeField(  );
            attributeField.setTitle( daoUtil.getString( 18 ) );
            attributeField.setValue( daoUtil.getString( 19 ) );
            attributeField.setDefaultValue( daoUtil.getBoolean( 20 ) );
            attributeField.setPosition( daoUtil.getInt( 21 ) );
            userField.setAttributeField( attributeField );
        }
        
        daoUtil.free(  );
        
        return userField;
	}
		
	/**
	 * Insert a new user field
	 * @param userField the user field
	 * @return new PK
	 */
	public void insert( AdminUserField userField )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
		daoUtil.setInt( 1, newPrimaryKey(  ) );
		daoUtil.setInt( 2, userField.getUser(  ).getUserId(  ) );
		daoUtil.setInt( 3, userField.getAttribute(  ).getIdAttribute(  ) );
		daoUtil.setInt( 4, userField.getAttributeField(  ).getIdField(  ) );
		
		daoUtil.executeUpdate(  );
		daoUtil.free(  );
	}
	
	/**
	 * Update an user field 
	 * @param attribute the attribute
	 */
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
	public void deleteUserFieldsFromIdUser( int nIdUser )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_ID_USER );
		daoUtil.setInt( 1, nIdUser );
		
		daoUtil.executeUpdate(  );
		daoUtil.free(  );
	}
	
	/**
	 * Delete all user fields from given id attribute
	 * @param nIdUser id user
	 */
	public void deleteUserFieldsFromIdAttribute( int nIdAttribute )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_ID_ATTRIBUTE );
		daoUtil.setInt( 1, nIdAttribute );
		
		daoUtil.executeUpdate(  );
		daoUtil.free(  );
	}
}
