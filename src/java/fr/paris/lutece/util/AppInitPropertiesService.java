/*
 * Copyright (c) 2002-2023, City of Paris
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import fr.paris.lutece.portal.service.init.WebConfResourceLocator;



/**
 * This class provides management services for properties files.
 *
 * <p>
 * The configuration is split into two independent sources :
 * </p>
 * <ul>
 * <li>a <b>base</b> source holding every {@code .properties} file except those located under the
 * {@code override/} and {@code override/plugins} directories ;</li>
 * <li>an <b>override</b> source holding only the {@code .properties} files located under the
 * {@code override/} and {@code override/plugins} directories.</li>
 * </ul>
 * <p>
 * Each source is exposed to MicroProfile Config through a dedicated {@code ConfigSource}, the override
 * source being given a higher ordinal so that its values take precedence over the base ones.
 * </p>
 */
public final class AppInitPropertiesService
{
    private static final String FILE_PROPERTIES_CONFIG = "config.properties";
    private static final String FILE_PROPERTIES_DATABASE = "db.properties";
    private static final String FILE_PROPERTIES_LUTECE = "lutece.properties";
    private static final String FILE_PROPERTIES_SEARCH = "search.properties";
    private static final String FILE_PROPERTIES_DAEMONS = "daemons.properties";
    private static final String FILE_PROPERTIES_CACHES = "caches.properties";
    private static final String FILE_PROPERTIES_EDITORS = "editors.properties";
    private static PropertiesService _basePropertiesService;
    private static PropertiesService _overridePropertiesService;

    /**
     * Private constructor
     */
    private AppInitPropertiesService( )
    {
    }

    /**
     * Initializes the base and the override properties services.
     *
     * @param strConfPath
     *            The configuration path
     */
    public static void init( String strConfPath )
    {
    	if ( _basePropertiesService == null )
    	{
	        String confPath = strConfPath;
	        _basePropertiesService = new PropertiesService( );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_CONFIG );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_DATABASE );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_LUTECE );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_SEARCH );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_DAEMONS );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_CACHES );
	        _basePropertiesService.addPropertiesFile( confPath, FILE_PROPERTIES_EDITORS );
	        _basePropertiesService.addPropertiesPaths( WebConfResourceLocator.getPathPropertiesFileWithoutOverride( ) );

	        _overridePropertiesService = new PropertiesService( );
	        _overridePropertiesService.addPropertiesPaths( WebConfResourceLocator.getPathOverridePropertiesFile( ) );
    	}
    }

    /**
     * Returns the value of a variable defined in the base properties files (everything except the
     * override directories) as a String.
     *
     * @param strProperty
     *            The variable name
     * @return The variable value read in the base properties files
     */
    public static String getBaseProperty( String strProperty )
    {
        return _basePropertiesService.getProperty( strProperty );
    }

    /**
     * Returns the names of the variables defined in the base properties files (everything except the
     * override directories).
     *
     * @return The set of base property names
     */
    public static Set<String> getBasePropertiesName( )
    {
        return _basePropertiesService.getProperties( ).stringPropertyNames( );
    }

    /**
     * Returns the value of a variable defined in the override properties files ({@code override/} and
     * {@code override/plugins}) as a String.
     *
     * @param strProperty
     *            The variable name
     * @return The variable value read in the override properties files
     */
    public static String getOverrideProperty( String strProperty )
    {
        return _overridePropertiesService.getProperty( strProperty );
    }

    /**
     * Returns the names of the variables defined in the override properties files ({@code override/}
     * and {@code override/plugins}).
     *
     * @return The set of override property names
     */
    public static Set<String> getOverridePropertiesName( )
    {
        return _overridePropertiesService.getProperties( ).stringPropertyNames( );
    }

    /**
     * Returns the value of a variable defined in the .properties files of the application as a String.
     * The override source takes precedence over the base source.
     *
     * @param strProperty
     *            The variable name
     * @return The variable value read in the properties files
     */
    public static String getProperty( String strProperty )
    {
        String strValue = _overridePropertiesService.getProperty( strProperty );
        if ( strValue == null )
        {
            strValue = _basePropertiesService.getProperty( strProperty );
        }
        return strValue;
    }
    /**
     * Gets all properties, merging the base and the override sources, the override values taking
     * precedence.
     *
     * @return All properties
     * @since version 3.0
     */
    public static Properties getProperties( )
    {
        Properties properties = new Properties( );
        properties.putAll( _basePropertiesService.getProperties( ) );
        properties.putAll( _overridePropertiesService.getProperties( ) );
        return properties;
    }
    /**
     * Get all properties As map
     * @return Properties as map
     */
    public static Map<String, String> getPropertiesAsMap( )
    {
        Map<String, String> res = new HashMap<>( );
        Properties properties = getProperties( );

        Enumeration<?> names = properties.propertyNames( );

        while ( names.hasMoreElements( ) )
        {
            String name = (String) names.nextElement( );
            res.put( name, properties.getProperty( name ) );
        }

        return res;
    }
    /**
     * Return the union of the base and the override property names.
     * @return The set of all property names
     */
    public static Set<String> getPropertiesName( )
    {
        Set<String> setNames = new LinkedHashSet<>( _basePropertiesService.getProperties( ).stringPropertyNames( ) );
        setNames.addAll( _overridePropertiesService.getProperties( ).stringPropertyNames( ) );
        return setNames;
    }

    /**
     * Returns a list of keys that match a given prefix.
     *
     * @param strPrefix
     *            the str prefix
     * @return A list of keys that match the prefix
     * @since version 7.0
     */
    public static List<String> getKeys( String strPrefix )
    {
        List<String> listKeys = new ArrayList<>( );
        Enumeration eList = getProperties( ).keys( );

        while ( eList.hasMoreElements( ) )
        {
            String strKey = (String) eList.nextElement( );

            if ( strKey.startsWith( strPrefix ) )
            {
                listKeys.add( strKey );
            }
        }

        return listKeys;
    }
}
