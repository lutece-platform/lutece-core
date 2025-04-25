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
package fr.paris.lutece.portal.business.securityheader;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class provides Data Access methods for SecurityHeader objects
 */
@ApplicationScoped
public final class SecurityHeaderDAO implements ISecurityHeaderDAO
{
      // Constants
      private static final String SQL_QUERY_SELECT = "SELECT id_security_header, name, value, description, type, page_category, is_active FROM core_admin_security_header WHERE id_security_header = ?";
      private static final String SQL_QUERY_INSERT = "INSERT INTO core_admin_security_header ( name, value, description, type, page_category ) VALUES ( ?, ?, ?, ?, ? ) ";
      private static final String SQL_QUERY_DELETE = "DELETE FROM core_admin_security_header WHERE id_security_header = ? ";
      private static final String SQL_QUERY_UPDATE = "UPDATE core_admin_security_header SET value = ?, description = ?, type = ?, page_category = ? WHERE id_security_header = ?";
      private static final String SQL_QUERY_UPDATE_IS_ACTIVE = "UPDATE core_admin_security_header SET is_active = ? WHERE id_security_header = ?";
      private static final String SQL_QUERY_SELECTALL = "SELECT id_security_header, name, value, description, type, page_category, is_active FROM core_admin_security_header";

    /**
     * Insert a new record in the table.
     *
     * @param securityHeader
     *            instance of the SecurityHeader object to insert
     */
    @Override
    public void insert( SecurityHeader securityHeader )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, securityHeader.getName( ) );
            daoUtil.setString( nIndex++, securityHeader.getValue( ) );
            daoUtil.setString( nIndex++, securityHeader.getDescription( ) );
            daoUtil.setString( nIndex++, securityHeader.getType( ) );
            daoUtil.setString( nIndex, securityHeader.getPageCategory() );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
            	securityHeader.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * Loads the data of the security header from the table
     *
     * @param nId
     *            The identifier of the security header
     * @return the instance of the Security header
     */
    @Override
    public SecurityHeader load( int nId )
    {
    	SecurityHeader securityHeader = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
            	securityHeader = new SecurityHeader( );

                securityHeader.setId( daoUtil.getInt( 1 ) );
            	securityHeader.setName( daoUtil.getString( 2 ) );            	
            	securityHeader.setValue( daoUtil.getString( 3 ) );
            	securityHeader.setDescription( daoUtil.getString( 4 ) );
            	securityHeader.setType( daoUtil.getString( 5 ) );
            	securityHeader.setPageCategory(daoUtil.getString( 6 ));
            	securityHeader.setActive( daoUtil.getBoolean( 7 ) );
            }

        }

        return securityHeader;
    }

    /**
     * Delete a record from the table
     *
     * @param nSecurityHeaderId
     *            The identifier of the security header
     */
    @Override
    public void delete( int nSecurityHeaderId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nSecurityHeaderId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Update the record in the table
     *
     * @param securityHeader
     *            The reference of the security header
     */
    @Override
    public void store( SecurityHeader securityHeader )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {

            daoUtil.setString( 1, securityHeader.getValue( ) );
            daoUtil.setString( 2, securityHeader.getDescription( ) );
            daoUtil.setString( 3, securityHeader.getType( ) );
            daoUtil.setString( 4, securityHeader.getPageCategory( ) );
            daoUtil.setInt( 5, securityHeader.getId( ) );

            daoUtil.executeUpdate( );
        }
    }
    
    /**
     * Update of "is_active" column value of security header which is specified in parameter
     * 
     * @param nSecurityHeaderId
     *            The securityHeader Id
     * @param isActiveValue
     *            The value to set to is_active column
     */
    @Override
    public void updateIsActive( int nSecurityHeaderId, boolean isActiveValue )
    {
    	try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_IS_ACTIVE ) )
        {

            daoUtil.setBoolean( 1, isActiveValue );
            daoUtil.setInt( 2, nSecurityHeaderId );

            daoUtil.executeUpdate( );
        }
    }
    
    /**
     * Loads the data of all the security headers and returns them in form of a collection
     *
     * @return The Collection which contains the data of all the security headers
     */
    @Override
    public List<SecurityHeader> selectAll( )
    {
        List<SecurityHeader> securityHeadersList = new ArrayList<SecurityHeader>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
            	SecurityHeader securityHeader = new SecurityHeader( );

            	securityHeader.setId( daoUtil.getInt( 1 ) );
            	securityHeader.setName( daoUtil.getString( 2 ) );            	
            	securityHeader.setValue( daoUtil.getString( 3 ) );
            	securityHeader.setDescription( daoUtil.getString( 4 ) );
            	securityHeader.setType( daoUtil.getString( 5 ) );
            	securityHeader.setPageCategory(daoUtil.getString( 6 ) );
            	securityHeader.setActive( daoUtil.getBoolean( 7 ) );

            	securityHeadersList.add( securityHeader );
            }

        }

        return securityHeadersList;
    }
}