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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;

import fr.paris.lutece.util.AppInitPropertiesService;

/**
 * this class provides management services for properties files
 */
public final class AppPropertiesService
{
   
	private static Config _config;
    /**
     * Private constructor
     */
    private AppPropertiesService( )
    {
    }

    /**
     * Initializes the service
     * 
     * @param strConfPath
     *            The configuration path
     */
    public static void init( String strConfPath )
    {
    	AppInitPropertiesService.init(strConfPath);
    	
		try {
			_config= ConfigProvider.getConfig();
			
		} catch (Exception e) {
			AppLogService.error(e.getMessage( ), e);
			throw e;
		}	
		
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty
     *            The variable name
     * @return The variable value read in the properties file
     */
    public static String getProperty( String strProperty )
    {
    	return _config.getOptionalValue(strProperty, String.class).orElse(null);
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty
     *            The variable name
     * @param strDefault
     *            The default value which is returned if no value is found for the variable in the .properties file.
     * @return The variable value read in the properties file
     */
    public static String getProperty( String strProperty, String strDefault )
    {
    	return _config.getOptionalValue(strProperty, String.class).orElse( strDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as an int
     *
     * @param strProperty
     *            The variable name
     * @param nDefault
     *            The default value which is returned if no value is found for the variable in the le downloadFile .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public static int getPropertyInt( String strProperty, int nDefault )
    {
        return _config.getOptionalValue(strProperty, Integer.class).orElse( nDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as an long
     *
     * @param strProperty
     *            The variable name
     * @param lDefault
     *            The default value which is returned if no value is found for the variable in the le downloadFile .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public static long getPropertyLong( String strProperty, long lDefault )
    {
        return _config.getOptionalValue(strProperty, Long.class).orElse( lDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a boolean
     *
     * @param strProperty
     *            The variable name
     * @param bDefault
     *            The default value which is returned if no value is found for the variable in the le downloadFile .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public static boolean getPropertyBoolean( String strProperty, boolean bDefault )
    {
        return _config.getOptionalValue(strProperty, Boolean.class).orElse( bDefault );
    }

    /**
     * Return the resolved property value with the specified type for the specified property name from the underlying
     * {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive to compute;
     * therefore, if the returned value is intended to be frequently used, callers should consider storing rather than
     * recomputing it.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T>
     *            The property type
     * @param propertyName
     *            The configuration property name
     * @param propertyType
     *            The type into which the resolved property value should be converted
     * @return The resolved property value as an {@code Optional} wrapping the requested type
     *
     * @throws IllegalArgumentException
     *             if the property cannot be converted to the specified type
     */
    public <T> Optional<T> getOptionalValue(String name, Class<T> aClass) 
    {
    	return _config.getOptionalValue(name, aClass );
    }
    /**
     * Reloads all the properties files
     */
    public static void reloadAll( )
    {
    	AppInitPropertiesService.reloadAll( );
    }

    /**
     * Reloads a given properties file
     * 
     * @param strFilename
     *            The file name
     */
    public static void reload( String strFilename )
    {
    	AppInitPropertiesService.reload( strFilename );
    }

    /**
     * Gets properties
     * 
     * @return All properties
     * @since version 3.0
     */
    public static Properties getProperties( )
    {
        return AppInitPropertiesService.getProperties();
    }

    /**
     * Gets all properties as a Map
     * 
     * @return a Map of all properties
     * @since version 5.0
     */
    public static Map<String, String> getPropertiesAsMap( )
    {
       return AppInitPropertiesService.getPropertiesAsMap();
    }

    /**
     * Returns a list of keys that match a given prefix.
     *
     * @param strPrefix
     *            the str prefix
     * @return A list of keys that match the prefix
     * @since version 3.0
     */
    public static List<String> getKeys( String strPrefix )
    {
        return AppInitPropertiesService.getKeys( strPrefix );
    }
}
