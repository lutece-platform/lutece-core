/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

/**
 * AppPathService Test Class
 */
public class AppPathServiceTest extends LuteceTestCase
{
    // TODO D�comenter les virtuals host dans config.properties...
    private static final String PROPERTY_VIRTUAL_HOST_KEY_PARAMETER = "virtualHostKey.parameterName";
    private static final String PROPERTY_BASE_URL = "lutece.base.url";
    private static final String FRAGMENT_END_PATH_XSL = "/WEB-INF/xsl/";
    private static final String FRAGMENT_END_PATH_CONF = "/WEB-INF/conf/";
    private static final String FRAGMENT_END_PATH_TEMPLATES = "/WEB-INF/templates";

    // TODO WebApp Path en dur...
    // private static final String WEBAPP_PATH = "C:/Java/projets/lutece/core/target/lutece/";

    /**
     * Test of getPath method, of class fr.paris.lutece.portal.service.util.AppPathService.
     */
    public void testGetPath( )
    {
        System.out.println( "getPath" );

        String strKey = "path.stylesheet";
        String expResult = FRAGMENT_END_PATH_XSL;
        String result = AppPathService.getPath( strKey );
        assertNotNull( expResult );
        assertTrue( result.endsWith( expResult ) );

        strKey = "dummy";

        String strException = null;

        try
        {
            AppPathService.getPath( strKey );
        }
        catch( AppException e )
        {
            strException = e.getMessage( );
        }

        assertNotNull( strException );
    }

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
    public void testGetResourceAsStream( ) throws IOException
    {
        System.out.println( "getResourceAsStream" );

        String strPath = FRAGMENT_END_PATH_CONF;
        String strFilename = "lutece.properties";

        FileInputStream fis = AppPathService.getResourceAsStream( strPath, strFilename );
        assertNotNull( fis );

        // Don't forget to close the file input stream
        if ( fis != null )
        {
            fis.close( );
        }
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
