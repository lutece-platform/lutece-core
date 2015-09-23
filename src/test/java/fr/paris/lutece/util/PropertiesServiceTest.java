/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.util;

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.test.LuteceTestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Properties;


/**
 * PropertiesService Test Class
 *
 */
public class PropertiesServiceTest extends LuteceTestCase
{
    private static final String PATH_CONF = "WEB-INF/conf";
    private static final String PATH_CONF_PLUGINS = "WEB-INF/conf/plugins";
    private static final String FILE_CONFIG = "config.properties";
    private static final String PROPERTY_PROD_URL = "lutece.prod.url";

    /**
     * Test of addPropertiesFile method, of class fr.paris.lutece.util.PropertiesService.
     */
    public void testAddPropertiesFile(  ) throws Exception
    {
        System.out.println( "addPropertiesFile" );

        PropertiesService instance = new PropertiesService( AppPathService.getWebAppPath(  ) );

        instance.addPropertiesFile( PATH_CONF, FILE_CONFIG );
        instance.getProperty( PROPERTY_PROD_URL );

        // Test reloading
        instance.reload( FILE_CONFIG );
    }

    /**
     * Test of addPropertiesDirectory method, of class fr.paris.lutece.util.PropertiesService.
     */
    public void testAddPropertiesDirectory(  ) throws Exception
    {
        System.out.println( "addPropertiesDirectory" );

        String strRelativePath = PATH_CONF_PLUGINS;
        PropertiesService instance = new PropertiesService( AppPathService.getWebAppPath(  ) );

        instance.addPropertiesDirectory( strRelativePath );

        // Test reloading
        instance.reloadAll(  );
    }

    public void testReloadAll(  ) throws FileNotFoundException, IOException
    {
        File propsFile = File.createTempFile( "junit", ".properties" );
        propsFile.deleteOnExit(  );

        Properties props = new Properties(  );
        props.put( "test1", "test1" );
        props.put( "test2", "test2" );

        OutputStream os = new FileOutputStream( propsFile );
        props.store( os, this.getClass(  ).getName(  ) );
        os.close(  );

        PropertiesService instance = new PropertiesService( propsFile.getParent(  ) );
        instance.addPropertiesFile( "", propsFile.getName(  ) );

        for ( String key : props.stringPropertyNames(  ) )
        {
            assertNotNull( instance.getProperty( key ) );
            assertEquals( props.getProperty( key ), instance.getProperty( key ) );
        }

        props.setProperty( "test1", "test1_mod" );
        props.remove( "test2" );
        os = new FileOutputStream( propsFile );
        props.store( os, this.getClass(  ).getName(  ) );
        os.close(  );

        instance.reloadAll(  );
        assertEquals( props.getProperty( "test1" ), instance.getProperty( "test1" ) );
        assertNull( instance.getProperty( "test2" ) );
    }

    public void testReloadAllOrder(  ) throws IOException
    {
        File propsFile = File.createTempFile( "junit", ".properties" );
        propsFile.deleteOnExit(  );

        Properties props = new Properties(  );
        props.put( "key", "1" );

        OutputStream os = new FileOutputStream( propsFile );
        props.store( os, this.getClass(  ).getName(  ) );
        os.close(  );

        PropertiesService instance = new PropertiesService( propsFile.getParent(  ) );
        instance.addPropertiesFile( "", propsFile.getName(  ) );

        assertEquals( "1", instance.getProperty( "key" ) );

        for ( int i = 2; i < 10; i++ )
        {
            propsFile = File.createTempFile( "junit", ".properties" );
            propsFile.deleteOnExit(  );

            props = new Properties(  );
            props.put( "key", Integer.toString( i ) );
            os = new FileOutputStream( propsFile );
            props.store( os, this.getClass(  ).getName(  ) );
            os.close(  );

            instance.addPropertiesFile( "", propsFile.getName(  ) );

            assertEquals( Integer.toString( i ), instance.getProperty( "key" ) );

            instance.reloadAll(  );

            assertEquals( Integer.toString( i ), instance.getProperty( "key" ) );
        }
    }
}
