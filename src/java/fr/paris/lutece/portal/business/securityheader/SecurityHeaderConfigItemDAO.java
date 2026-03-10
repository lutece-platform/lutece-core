/*
 * Copyright (c) 2002-2026, City of Paris
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

@ApplicationScoped
public class SecurityHeaderConfigItemDAO implements ISecurityHeaderConfigItemDAO
{
	// Constants
	private static final String SQL_QUERY_SELECT = "SELECT id_config_item, id_security_header, header_custom_value, url_pattern FROM core_admin_security_header_config_item WHERE id_config_item = ?";
    private static final String SQL_QUERY_SELECT_BY_SECURITY_HEADER_ID = "SELECT id_config_item, id_security_header, header_custom_value, url_pattern FROM core_admin_security_header_config_item WHERE id_security_header = ?";
    private static final String SQL_QUERY_SELECT_BY_SECURITY_HEADER_ID_AND_URL_PATTERN = "SELECT id_config_item, id_security_header, header_custom_value, url_pattern FROM core_admin_security_header_config_item WHERE id_security_header = ? AND url_pattern = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_admin_security_header_config_item ( id_security_header, header_custom_value, url_pattern ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_admin_security_header_config_item SET url_pattern = ?, header_custom_value = ? WHERE id_config_item = ?";    
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_admin_security_header_config_item WHERE id_config_item = ?";
    private static final String SQL_QUERY_DELETE_FOR_SECURITY_HEADER_ID = "DELETE FROM core_admin_security_header_config_item WHERE id_security_header = ?";
	
    /** {@inheritDoc} */
    public void insert( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, securityHeaderConfigItem.getIdSecurityHeader( ) );
            daoUtil.setString( nIndex++, securityHeaderConfigItem.getHeaderCustomValue( ) );
            daoUtil.setString( nIndex++, securityHeaderConfigItem.getUrlPattern( ) );                   

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
            	securityHeaderConfigItem.setIdConfigItem( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }   
    
    /** {@inheritDoc} */
    public void deleteForSecurityHeaderId( int nSecurityHeaderId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FOR_SECURITY_HEADER_ID ) )
        {
            daoUtil.setInt( 1, nSecurityHeaderId );
            daoUtil.executeUpdate( );
        }
    }
    
    /** {@inheritDoc} */
    public void store( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {

            daoUtil.setString( 1, securityHeaderConfigItem.getUrlPattern( ) );
            daoUtil.setString(2, securityHeaderConfigItem.getHeaderCustomValue( ) );
            daoUtil.setInt(3, securityHeaderConfigItem.getIdConfigItem( ) );

            daoUtil.executeUpdate( );
        }
    }
    
    /** {@inheritDoc} */
    public void delete( int nSecurityHeaderConfigItemId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nSecurityHeaderConfigItemId );
            daoUtil.executeUpdate( );
        }
    }
    
    /** {@inheritDoc} */
    public SecurityHeaderConfigItem load( int nId )
    {
    	SecurityHeaderConfigItem securityHeaderConfigItem = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
            	securityHeaderConfigItem = new SecurityHeaderConfigItem( );

            	securityHeaderConfigItem.setIdConfigItem( daoUtil.getInt( 1 ) );
            	securityHeaderConfigItem.setIdSecurityHeader( daoUtil.getInt( 2 ) );
            	securityHeaderConfigItem.setHeaderCustomValue( daoUtil.getString( 3 ) );
            	securityHeaderConfigItem.setUrlPattern( daoUtil.getString( 4 ) );        	
            }
        }

        return securityHeaderConfigItem;
    }
    
    /** {@inheritDoc} */
    public List<SecurityHeaderConfigItem> find( int securityHeaderId, String urlPattern )
	{
		List<SecurityHeaderConfigItem> securityHeaderConfigList = new ArrayList<SecurityHeaderConfigItem>( );
		try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SECURITY_HEADER_ID_AND_URL_PATTERN ) )
		{
			daoUtil.setInt( 1, securityHeaderId );
			daoUtil.setString( 2, urlPattern );			
        	daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
            	SecurityHeaderConfigItem securityHeaderConfigItem = new SecurityHeaderConfigItem( );

            	securityHeaderConfigItem.setIdConfigItem( daoUtil.getInt( 1 ) );
            	securityHeaderConfigItem.setIdSecurityHeader( daoUtil.getInt( 2 ) );
            	securityHeaderConfigItem.setHeaderCustomValue( daoUtil.getString( 3 ) );
            	securityHeaderConfigItem.setUrlPattern( daoUtil.getString( 4 ) );            	
            	        	
            	securityHeaderConfigList.add( securityHeaderConfigItem );
            }
		}
		
		return securityHeaderConfigList;
	}
    
    /** {@inheritDoc} */
	public List<SecurityHeaderConfigItem> findBySecurityHeaderId( int securityHeaderId )
	{
		List<SecurityHeaderConfigItem> securityHeaderConfigList = new ArrayList<SecurityHeaderConfigItem>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_SECURITY_HEADER_ID ) )
        {
        	daoUtil.setInt( 1, securityHeaderId );
        	daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
            	SecurityHeaderConfigItem securityHeaderConfigItem = new SecurityHeaderConfigItem( );

            	securityHeaderConfigItem.setIdConfigItem( daoUtil.getInt( 1 ) );
            	securityHeaderConfigItem.setIdSecurityHeader( daoUtil.getInt( 2 ) );
            	securityHeaderConfigItem.setHeaderCustomValue( daoUtil.getString( 3 ) );
            	securityHeaderConfigItem.setUrlPattern( daoUtil.getString( 4 ) );            	           	        	

            	securityHeaderConfigList.add( securityHeaderConfigItem );
            }
        }

        return securityHeaderConfigList;
	}
}