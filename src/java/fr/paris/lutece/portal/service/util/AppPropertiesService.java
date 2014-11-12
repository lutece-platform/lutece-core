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
package fr.paris.lutece.portal.service.util;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.util.PropertiesService;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * this class provides management services for properties files
 */
public final class AppPropertiesService
{
    private static final String FILE_PROPERTIES_CONFIG = "config.properties";
    private static final String FILE_PROPERTIES_DATABASE = "db.properties";
    private static final String FILE_PROPERTIES_LUTECE = "lutece.properties";
    private static final String FILE_PROPERTIES_SEARCH = "search.properties";
    private static final String FILE_PROPERTIES_DAEMONS = "daemons.properties";
    private static final String FILE_PROPERTIES_CACHES = "caches.properties";
    private static final String FILE_PROPERTIES_EDITORS = "editors.properties";
    private static final String PATH_PLUGINS = "plugins/";
    private static final String PATH_OVERRIDE_CORE = "override/";
    private static final String PATH_OVERRIDE_PLUGINS = "override/plugins";
    private static PropertiesService _propertiesService;
    private static String _strConfPath;

    /**
     * Private constructor
     */
    private AppPropertiesService(  )
    {
    }

    /**
     * Initializes the service
     * @param strConfPath The configuration path
     * @throws LuteceInitException If an error occured
     */
    public static void init( String strConfPath ) throws LuteceInitException
    {
        _strConfPath = strConfPath;
        _propertiesService = new PropertiesService( AppPathService.getWebAppPath(  ) );

        try
        {
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_CONFIG );
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_DATABASE );
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_LUTECE );
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_SEARCH );
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_DAEMONS );
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_CACHES );
            _propertiesService.addPropertiesFile( _strConfPath, FILE_PROPERTIES_EDITORS );
            _propertiesService.addPropertiesDirectory( _strConfPath + PATH_PLUGINS );
            _propertiesService.addPropertiesDirectory( _strConfPath + PATH_OVERRIDE_CORE );
            _propertiesService.addPropertiesDirectory( _strConfPath + PATH_OVERRIDE_PLUGINS );
        }
        catch ( FileNotFoundException e )
        {
            throw new LuteceInitException( "AppPropertiesService failed to load : " + e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            throw new LuteceInitException( "AppPropertiesService failed to load : " + e.getMessage(  ), e );
        }
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty The variable name
     * @return The variable value read in the properties file
     */
    public static String getProperty( String strProperty )
    {
        return _propertiesService.getProperty( strProperty );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty The variable name
     * @param strDefault The default value which is returned if no value is found for the variable in the .properties
     *        file.
     * @return The variable value read in the properties file
     */
    public static String getProperty( String strProperty, String strDefault )
    {
        return _propertiesService.getProperty( strProperty, strDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as an int
     *
     * @param strProperty The variable name
     * @param nDefault The default value which is returned if no value is found for the variable in the le downloadFile
     *        .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public static int getPropertyInt( String strProperty, int nDefault )
    {
        return _propertiesService.getPropertyInt( strProperty, nDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as an long
     *
     * @param strProperty The variable name
     * @param lDefault The default value which is returned if no value is found for the variable in the le downloadFile
     *        .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public static long getPropertyLong( String strProperty, long lDefault )
    {
        return _propertiesService.getPropertyLong( strProperty, lDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a boolean
     *
     * @param strProperty The variable name
     * @param bDefault The default value which is returned if no value is found for the variable in the le downloadFile
     *        .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public static boolean getPropertyBoolean( String strProperty, boolean bDefault )
    {
        return _propertiesService.getPropertyBoolean( strProperty, bDefault );
    }

    /**
     * Reloads all the properties files
     */
    public static void reloadAll(  )
    {
        try
        {
            _propertiesService.reloadAll(  );
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Reloads a given properties file
     * @param strFilename The file name
     */
    public static void reload( String strFilename )
    {
        try
        {
            _propertiesService.reload( strFilename );
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Gets properties
     * @return All properties
     * @since version 3.0
     */
    public static Properties getProperties(  )
    {
        // Return a copy of all properties
        return new Properties( _propertiesService.getProperties(  ) );
    }

    /**
     * Gets all properties as a Map
     * @return a Map of all properties
     * @since version 5.0
     */
    public static Map<String, String> getPropertiesAsMap(  )
    {
        Map<String, String> res = new HashMap<String, String>(  );
        Properties properties = _propertiesService.getProperties(  );

        // enumerate over property names to get all properties, including one which are defaults
        Enumeration<?> names = properties.propertyNames(  );

        while ( names.hasMoreElements(  ) )
        {
            String name = (String) names.nextElement(  );
            res.put( name, properties.getProperty( name ) );
            ;
        }

        return res;
    }

    /**
     * Returns a list of keys that match a given prefix.
     *
     * @param strPrefix the str prefix
     * @return A list of keys that match the prefix
     * @since version 3.0
     */
    public static List<String> getKeys( String strPrefix )
    {
        List<String> listKeys = new ArrayList<String>(  );
        Enumeration eList = _propertiesService.getProperties(  ).keys(  );

        while ( eList.hasMoreElements(  ) )
        {
            String strKey = (String) eList.nextElement(  );

            if ( strKey.startsWith( strPrefix ) )
            {
                listKeys.add( strKey );
            }
        }

        return listKeys;
    }
}
