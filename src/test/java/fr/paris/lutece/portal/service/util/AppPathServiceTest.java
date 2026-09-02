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
package fr.paris.lutece.portal.service.util;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.resource.loader.ResourceNotFoundException;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import jakarta.servlet.http.HttpServletRequest;

/**
 * AppPathService Test Class
 */
public class AppPathServiceTest extends LuteceTestCase
{
    // TODO D�comenter les virtuals host dans config.properties...
    private static final String PROPERTY_VIRTUAL_HOST_KEY_PARAMETER = "virtualHostKey.parameterName";
    private static final String PROPERTY_BASE_URL = "lutece.base.url";
    private static final String FRAGMENT_END_PATH_CONF = "/WEB-INF/conf/";
    // Declared in webapp/WEB-INF/conf/config.properties, with no value
    private static final String PROPERTY_DECLARED_WITHOUT_VALUE = "lutece.prod.url";
    private static final String PROPERTY_MISSING = "lutece.path.that.is.not.declared";

   
    /**
     * Test of getWebAppPath method, of class fr.paris.lutece.portal.service.util.AppPathService. FIXME : uncomment this method when a better way to find real
     * app path is found.
     */

    /*
     * public void testGetWebAppPath( ) { System.out.println( "getWebAppPath" );
     * 
     * String expResult = WEBAPP_PATH; String result = AppPathService.getWebAppPath( ); assertNotNull( result ); }
     */

    /**
     * Test of getResourceAsStream method, of class fr.paris.lutece.portal.service.util.AppPathService.
     */
    @Test
    public void testGetResourceAsStream( ) throws IOException, ResourceNotFoundException
    {
        System.out.println( "getResourceAsStream" );

        String strPath = FRAGMENT_END_PATH_CONF;
        String strFilename = "lutece.properties";

        InputStream is = AppPathService.getResourceStream( strPath, strFilename );
        assertNotNull( is );

        // Don't forget to close the file input stream
        if ( is != null )
        {
            is.close( );
        }
    }

    /**
     * A key declared with an empty value resolves to null just like an absent key, so getPath used to report both as not found. The exception must now name the
     * actual cause, an empty declaration being fixed in a different place than a missing one.
     */
    @Test
    public void testGetPathWhenPropertyIsDeclaredWithoutValue( )
    {
        assertNull( AppPropertiesService.getProperty( PROPERTY_DECLARED_WITHOUT_VALUE ) );
        assertTrue( AppPropertiesService.isPropertyDeclared( PROPERTY_DECLARED_WITHOUT_VALUE ),
                "The key must be known to the configuration even without a value" );

        AppException e = assertThrows( AppException.class, ( ) -> AppPathService.getPath( PROPERTY_DECLARED_WITHOUT_VALUE ) );
        assertTrue( e.getMessage( ).contains( "declared with an empty value" ), e.getMessage( ) );
    }

    /**
     * Test of getPath method with a key that no configuration source declares.
     */
    @Test
    public void testGetPathWhenPropertyIsMissing( )
    {
        assertFalse( AppPropertiesService.isPropertyDeclared( PROPERTY_MISSING ) );

        AppException e = assertThrows( AppException.class, ( ) -> AppPathService.getPath( PROPERTY_MISSING ) );
        assertTrue( e.getMessage( ).contains( "not found in the properties file" ), e.getMessage( ) );
    }

    /**
     * Test of getAbsolutePathFromRelativePath method, of class fr.paris.lutece.portal.service.util.AppPathService. FIXME : uncomment this method when a better
     * way to find real app path is found.
     */

    /*
     * public void testGetAbsolutePathFromRelativePath( ) { System.out.println( "getAbsolutePathFromRelativePath" );
     * 
     * String strDirectory = FRAGMENT_END_PATH_TEMPLATES;
     * 
     * String expResult = strDirectory; String result = AppPathService.getAbsolutePathFromRelativePath( strDirectory ); assertNotNull( result ); assertTrue(
     * result.endsWith( expResult ) ); }
     */

    /**
     * Test of getBaseUrl method, of class fr.paris.lutece.portal.service.util.AppPathService.
     */
    @Test
    public void testGetBaseUrl( )
    {
        System.out.println( "getBaseUrl" );

        HttpServletRequest request = null;

        // Test case where base url is defined in the properties. Can't test dynamic base Url.
        String expResult = AppPropertiesService.getProperty( PROPERTY_BASE_URL );

        if ( expResult != null )
        {
            expResult += "/";

            String result = AppPathService.getBaseUrl( request );
            assertEquals( expResult, result );
        }
    }

    /**
     * Test of getAvailableVirtualHosts method, of class fr.paris.lutece.portal.service.util.AppPathService.
     */
    @Test
    public void testGetAvailableVirtualHosts( )
    {
        System.out.println( "getAvailableVirtualHosts" );

        ReferenceList result = AppPathService.getAvailableVirtualHosts( );

        if ( result != null )
        {
            assertTrue( result.size( ) == 2 );
        }
    }

    /**
     * Test of getVirtualHostKey method, of class fr.paris.lutece.portal.service.util.AppPathService.
     */
    @Test
    public void testGetVirtualHostKey( )
    {
        System.out.println( "getVirtualHostKey" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strParameter = AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST_KEY_PARAMETER );
        ReferenceList listKeys = AppPathService.getAvailableVirtualHosts( );

        if ( listKeys != null )
        {
            String strKey = listKeys.get( 0 ).getCode( );
            request.addParameter( strParameter, strKey );

            String expResult = strKey;
            String result = AppPathService.getVirtualHostKey( request );
            assertEquals( expResult, result );
        }
    }
}
