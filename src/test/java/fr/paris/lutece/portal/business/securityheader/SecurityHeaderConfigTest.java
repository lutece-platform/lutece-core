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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

public class SecurityHeaderConfigTest extends LuteceTestCase
{
	private static final String HEADER_NAME = "test_header_name";
	private static final String HEADER_VALUE = "test_header_value";
	private static final String HEADER_DESCRIPTION = "test_header_description";
	
	private static final String MODIFIED_HEADER_VALUE = "modified_test_header_value";
	private static final String MODIFIED_HEADER_DESCRIPTION = "modified_test_header_description";	
	
	private static final String HEADER_CUSTOM_VALUE = "test_custom_value";
	private static final String URL_PATTERN = "test_url_pattern";
	
	private static final String MODIFIED_HEADER_CUSTOM_VALUE = "modified_test_custom_value";
	private static final String MODIFIED_URL_PATTERN = "modified_test_url_pattern";
	
	@Test
	public void testBusinessSecurityHeader( )
    {
		// Initialize an object
    	SecurityHeader securityHeader = new SecurityHeader( );
    	securityHeader.setName( HEADER_NAME );
    	securityHeader.setValue( HEADER_VALUE );
    	securityHeader.setDescription( HEADER_DESCRIPTION );
    	securityHeader.setType( SecurityHeaderType.PAGE.getCode( ) );
    	securityHeader.setPageCategory( SecurityHeaderPageCategory.ALL.getCode( ) );
    	
    	// Create test
    	securityHeader = SecurityHeaderHome.create( securityHeader );
    	assertNotNull( securityHeader );
    	
    	// Find by Primary Key test
    	SecurityHeader securityHeaderStored = SecurityHeaderHome.findByPrimaryKey( securityHeader.getId( ) );
        assertEquals( securityHeader.getName( ), securityHeaderStored.getName( ) );
        assertEquals( securityHeader.getValue( ), securityHeaderStored.getValue( ) );
        assertEquals( securityHeader.getDescription( ), securityHeaderStored.getDescription( ) );
        assertEquals( securityHeader.getType( ), securityHeaderStored.getType( ) );
        assertEquals( securityHeader.getPageCategory( ), securityHeaderStored.getPageCategory( ) );
        assertEquals( securityHeader.isActive( ), false );
    	
    	// Update test
    	securityHeader.setValue( MODIFIED_HEADER_VALUE );
    	securityHeader.setValue( MODIFIED_HEADER_DESCRIPTION );
    	securityHeader.setType( SecurityHeaderType.REST_API.getCode( ) );
    	securityHeader.setPageCategory( null );
    	
    	SecurityHeaderHome.update( securityHeader );
    	
    	securityHeaderStored = SecurityHeaderHome.findByPrimaryKey( securityHeader.getId( ) );
        assertEquals( securityHeader.getName( ), securityHeaderStored.getName( ) );
        assertEquals( securityHeader.getDescription( ), securityHeaderStored.getDescription( ) );
        assertEquals( securityHeader.getValue( ), securityHeaderStored.getValue( ) );
        assertEquals( securityHeader.getType( ), securityHeaderStored.getType( ) );
        assertEquals( securityHeader.getPageCategory( ), securityHeaderStored.getPageCategory( ) );
        assertEquals( securityHeader.isActive( ), false );
        
        // Activation test
        SecurityHeaderHome.updateIsActive( securityHeader.getId( ), true );
        
        securityHeaderStored = SecurityHeaderHome.findByPrimaryKey( securityHeader.getId( ) );
        assertEquals( securityHeaderStored.isActive( ), true );
        
        // Find all test
        assertTrue( SecurityHeaderHome.findAll( ).size( ) > 0 );
        
        // Delete test
        SecurityHeaderHome.remove( securityHeader.getId( ) );
        securityHeaderStored = SecurityHeaderHome.findByPrimaryKey( securityHeader.getId( ) );
        assertNull( securityHeaderStored );
    }
	
	@Test
    public void testBusinessSecurityHeaderConfig( )
    {
    	// Create security header to configure
    	SecurityHeader securityHeader = new SecurityHeader( );
    	securityHeader.setName( HEADER_NAME );
    	securityHeader.setValue( HEADER_VALUE );
    	securityHeader.setType( SecurityHeaderType.PAGE.getCode( ) );
    	
    	securityHeader = SecurityHeaderHome.create( securityHeader );
    	
        // Initialize an object
    	SecurityHeaderConfigItem securityHeaderConfigItem = new SecurityHeaderConfigItem( );
    	securityHeaderConfigItem.setIdSecurityHeader( securityHeader.getId( ) );
    	securityHeaderConfigItem.setHeaderCustomValue( HEADER_CUSTOM_VALUE );
    	securityHeaderConfigItem.setUrlPattern( "test_url_pattern" );

        // Create test
    	securityHeaderConfigItem = SecurityHeaderConfigHome.create( securityHeaderConfigItem );
    	assertNotNull( securityHeaderConfigItem );

    	// find by Primary Key test
    	SecurityHeaderConfigItem securityHeaderConfigStored = SecurityHeaderConfigHome.findByPrimaryKey( securityHeaderConfigItem.getIdConfigItem( ) );
        assertEquals( securityHeaderConfigItem.getIdSecurityHeader( ), securityHeaderConfigStored.getIdSecurityHeader( ) );    	
        assertEquals( securityHeaderConfigItem.getHeaderCustomValue( ), securityHeaderConfigStored.getHeaderCustomValue( ) );
        assertEquals( securityHeaderConfigItem.getUrlPattern( ), securityHeaderConfigStored.getUrlPattern( ) );

        // find by securityHeader Id test
        assertEquals( 1, SecurityHeaderConfigHome.findBySecurityHeaderId( securityHeader.getId( ) ).size( ) );
        
        // find test
        assertEquals( 1, SecurityHeaderConfigHome.find( securityHeader.getId( ), URL_PATTERN ).size( ) );
        
        // Update test
        securityHeaderConfigItem.setHeaderCustomValue( MODIFIED_HEADER_CUSTOM_VALUE );
        securityHeaderConfigItem.setUrlPattern( MODIFIED_URL_PATTERN );

        SecurityHeaderConfigHome.update( securityHeaderConfigItem );
        
        securityHeaderConfigStored = SecurityHeaderConfigHome.findByPrimaryKey( securityHeaderConfigItem.getIdConfigItem( ) );
        assertEquals( securityHeaderConfigItem.getHeaderCustomValue( ), securityHeaderConfigStored.getHeaderCustomValue( ) );
        assertEquals( securityHeaderConfigItem.getUrlPattern( ), securityHeaderConfigStored.getUrlPattern( ) );

        // Delete test
        SecurityHeaderConfigHome.remove( securityHeaderConfigItem.getIdConfigItem( ) );
        securityHeaderConfigStored = SecurityHeaderConfigHome.findByPrimaryKey( securityHeaderConfigItem.getIdConfigItem( ) );
        assertNull( securityHeaderConfigStored );
    }
}