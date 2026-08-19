/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.nio.file.Files;
import java.nio.file.Path;

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
    private static final String DIR_PLUGINS = "plugins";
    private static final String DIR_OVERRIDE_PLUGINS = "plugins/override";
    private static final String FILE_PLUGIN = "myplugin.properties";
    private static final String KEY_1 = "myplugin.key1";
    private static final String KEY_2 = "myplugin.key2";
    private static final String KEY_3 = "myplugin.key3";
    private static final String VALUE_BASE_1 = "base1";
    private static final String VALUE_BASE_2 = "base2";
    private static final String VALUE_BASE_3 = "base3";
    private static final String VALUE_OVERRIDE_2 = "override2";

    /**
     * Test of addPropertiesFile method, of class fr.paris.lutece.util.PropertiesService.
     */
    public void testAddPropertiesFile( ) throws Exception
    {
        System.out.println( "addPropertiesFile" );

        PropertiesService instance = new PropertiesService( AppPathService.getWebAppPath( ) );

        instance.addPropertiesFile( PATH_CONF, FILE_CONFIG );
        instance.getProperty( PROPERTY_PROD_URL );

        // Test reloading
        instance.reload( FILE_CONFIG );
    }

    /**
     * Test of addPropertiesDirectory method, of class fr.paris.lutece.util.PropertiesService.
     */
    public void testAddPropertiesDirectory( ) throws Exception
    {
        System.out.println( "addPropertiesDirectory" );

        String strRelativePath = PATH_CONF_PLUGINS;
        PropertiesService instance = new PropertiesService( AppPathService.getWebAppPath( ) );

        instance.addPropertiesDirectory( strRelativePath );

        // Test reloading
        instance.reloadAll( );
    }

    public void testReloadAll( ) throws FileNotFoundException, IOException
    {
        File propsFile = File.createTempFile( "junit", ".properties" );
        propsFile.deleteOnExit( );

        Properties props = new Properties( );
        props.put( "test1", "test1" );
        props.put( "test2", "test2" );

        OutputStream os = new FileOutputStream( propsFile );
        props.store( os, this.getClass( ).getName( ) );
        os.close( );

        PropertiesService instance = new PropertiesService( propsFile.getParent( ) );
        instance.addPropertiesFile( "", propsFile.getName( ) );

        for ( String key : props.stringPropertyNames( ) )
        {
            assertNotNull( instance.getProperty( key ) );
            assertEquals( props.getProperty( key ), instance.getProperty( key ) );
        }

        props.setProperty( "test1", "test1_mod" );
        props.remove( "test2" );
        os = new FileOutputStream( propsFile );
        props.store( os, this.getClass( ).getName( ) );
        os.close( );

        instance.reloadAll( );
        assertEquals( props.getProperty( "test1" ), instance.getProperty( "test1" ) );
        assertNull( instance.getProperty( "test2" ) );
    }

    public void testReloadAllOrder( ) throws IOException
    {
        File propsFile = File.createTempFile( "junit", ".properties" );
        propsFile.deleteOnExit( );

        Properties props = new Properties( );
        props.put( "key", "1" );

        OutputStream os = new FileOutputStream( propsFile );
        props.store( os, this.getClass( ).getName( ) );
        os.close( );

        PropertiesService instance = new PropertiesService( propsFile.getParent( ) );
        instance.addPropertiesFile( "", propsFile.getName( ) );

        assertEquals( "1", instance.getProperty( "key" ) );

        for ( int i = 2; i < 10; i++ )
        {
            propsFile = File.createTempFile( "junit", ".properties" );
            propsFile.deleteOnExit( );

            props = new Properties( );
            props.put( "key", Integer.toString( i ) );
            os = new FileOutputStream( propsFile );
            props.store( os, this.getClass( ).getName( ) );
            os.close( );

            instance.addPropertiesFile( "", propsFile.getName( ) );

            assertEquals( Integer.toString( i ), instance.getProperty( "key" ) );

            instance.reloadAll( );

            assertEquals( Integer.toString( i ), instance.getProperty( "key" ) );
        }
    }

    /**
     * Test that reloadAll does not lose the properties of a file sharing its filename with another registered file, which is the case of a plugin properties
     * file having a counterpart in the override directory.
     *
     * @throws IOException
     *             If an error occurs writing the temporary properties files
     */
    public void testReloadAllWithOverriddenFilename( ) throws IOException
    {
        PropertiesService instance = buildServiceWithOverriddenFilename( );

        assertEquals( VALUE_BASE_1, instance.getProperty( KEY_1 ) );
        assertEquals( VALUE_OVERRIDE_2, instance.getProperty( KEY_2 ) );
        assertEquals( VALUE_BASE_3, instance.getProperty( KEY_3 ) );

        instance.reloadAll( );

        assertEquals( VALUE_BASE_1, instance.getProperty( KEY_1 ) );
        assertEquals( VALUE_OVERRIDE_2, instance.getProperty( KEY_2 ) );
        assertEquals( VALUE_BASE_3, instance.getProperty( KEY_3 ) );
    }

    /**
     * Test that reload reloads every registered file sharing the given filename, so that the override file keeps precedence over the base file.
     *
     * @throws IOException
     *             If an error occurs writing the temporary properties files
     */
    public void testReloadWithOverriddenFilename( ) throws IOException
    {
        PropertiesService instance = buildServiceWithOverriddenFilename( );

        instance.reload( FILE_PLUGIN );

        assertEquals( VALUE_BASE_1, instance.getProperty( KEY_1 ) );
        assertEquals( VALUE_OVERRIDE_2, instance.getProperty( KEY_2 ) );
        assertEquals( VALUE_BASE_3, instance.getProperty( KEY_3 ) );
    }

    /**
     * Test that reloading an unregistered filename does not throw.
     *
     * @throws IOException
     *             If an error occurs writing the temporary properties files
     */
    public void testReloadUnknownFile( ) throws IOException
    {
        PropertiesService instance = buildServiceWithOverriddenFilename( );

        instance.reload( "unknown.properties" );

        assertEquals( VALUE_BASE_1, instance.getProperty( KEY_1 ) );
    }

    /**
     * Builds a PropertiesService holding two registered properties files sharing the same filename : a base one declaring three keys and an override one
     * redefining the second key.
     *
     * @return The PropertiesService to test
     * @throws IOException
     *             If an error occurs writing the temporary properties files
     */
    private PropertiesService buildServiceWithOverriddenFilename( ) throws IOException
    {
        Path pathRoot = Files.createTempDirectory( "junit" );
        pathRoot.toFile( ).deleteOnExit( );

        File fileDirPlugins = new File( pathRoot.toFile( ), DIR_PLUGINS );
        File fileDirOverride = new File( pathRoot.toFile( ), DIR_OVERRIDE_PLUGINS );
        assertTrue( fileDirOverride.mkdirs( ) );
        fileDirPlugins.deleteOnExit( );
        fileDirOverride.deleteOnExit( );

        Properties propertiesBase = new Properties( );
        propertiesBase.put( KEY_1, VALUE_BASE_1 );
        propertiesBase.put( KEY_2, VALUE_BASE_2 );
        propertiesBase.put( KEY_3, VALUE_BASE_3 );
        storeProperties( new File( fileDirPlugins, FILE_PLUGIN ), propertiesBase );

        Properties propertiesOverride = new Properties( );
        propertiesOverride.put( KEY_2, VALUE_OVERRIDE_2 );
        storeProperties( new File( fileDirOverride, FILE_PLUGIN ), propertiesOverride );

        PropertiesService instance = new PropertiesService( pathRoot.toString( ) );
        instance.addPropertiesDirectory( DIR_PLUGINS );
        instance.addPropertiesDirectory( DIR_OVERRIDE_PLUGINS );

        return instance;
    }

    /**
     * Writes the given properties into the given file, which is deleted on exit.
     *
     * @param file
     *            The file to write
     * @param properties
     *            The properties to store
     * @throws IOException
     *             If an error occurs writing the file
     */
    private void storeProperties( File file, Properties properties ) throws IOException
    {
        file.deleteOnExit( );

        try ( OutputStream os = new FileOutputStream( file ) )
        {
            properties.store( os, this.getClass( ).getName( ) );
        }
    }
}
