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
package fr.paris.lutece.util;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.status.StatusLogger;

import fr.paris.lutece.portal.service.init.WebConfResourceLocator;
import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.apache.logging.log4j.Logger;

/**
 * This class provides utility methods to read values of the properties stored in the .properties file of the application.
 */

public class PropertiesService
{
    // Static variables
   // private String _strRootPath;
    private Properties _properties = new Properties( );
    private Map<String, String> _mapPropertiesFiles = new LinkedHashMap<>( );

    private final String RSA_KEY_PREFIX = "PROTECTED::RSA::";
    private final String MESSAGE_CIPHERED_PROPERTY_SECURITY_EXCEPTION = "A ciphered property security exception occured." ;
    // Use of a static logger because Lutece loggers are loaded after the ServletContext initialization, whereas this class is loaded beforehand.
    final Logger LOGGER = StatusLogger.getLogger();
    /**
     * Constructor should define the base root path for properties files
     * 
     * @param strRootPath
     *            The root path
     */
    public PropertiesService(  )
    {
    }

    /**
     * Add properties from a properties file
     * 
     * @param strRelativePath
     *            Relative path from the root path
     * @param strFilename
     *            The filename of the properties file (ie: config.properties)
     */
    public void addPropertiesFile( String strRelativePath, String strFilename )
    {
        String strPath =  ( ( strRelativePath.endsWith( "/" ) ) ? strRelativePath : ( strRelativePath + "/" ) ) + strFilename;
        _mapPropertiesFiles.put( strFilename, strPath );
        loadFile( strPath );
    }

    /**
     * Add properties from all files found in a given directory
     * 
     * @param strRelativePath
     *            Relative path
     */
    public void addPropertiesDirectory(String ... strRelativePaths)  {
    	loadProperties(WebConfResourceLocator.getPathPropertiesFile( ));
    }

	private void loadProperties( Set<String> listPath) {
		
		listPath.forEach(pathResource -> {
			_mapPropertiesFiles.put( getFileName( pathResource), pathResource );
			loadFile( pathResource, _properties );
		}
		);
	}
	
	private String getFileName(String filePath) {
        int lastSeparatorIndex = filePath.lastIndexOf('/');
        if (lastSeparatorIndex >= 0) {
            return filePath.substring(lastSeparatorIndex + 1);
        } else {
            return filePath;
        }
    }

    /**
     * Load properties of a file
     * 
     * @param strRelativePath
     *            The relative path of the properties file
     */
    private void loadFile( String strRelativePath )
    {
        loadFile( strRelativePath, _properties );
    }

    /**
     * Load properties of a file
     * 
     * @param strFullPath
     *            The absolute path of the properties file
     * @param props
     *            properties to load into
     * @throws java.io.IOException
     *             If an error occurs reading the file
     */
    private void loadFile( String strRelatvePath, Properties props )
    { 
    	 try {
    	    InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(strRelatvePath);
            if(is == null) { 
            	is = Files.newInputStream(Paths.get(AppPathService.getWebAppPath(), strRelatvePath));
            }
    	    props.load( is );
        } catch (Exception ex) {
        	LOGGER.error( "Error loading property file, path: {}", strRelatvePath, ex );
		} 
      
    }

    /**
     * Reload a properties file .
     * 
     * @param strFilename
     *            The filename of the properties file
     */
    public void reload( String strFilename )
    {
        String strFullPath = _mapPropertiesFiles.get( strFilename );
        loadFile( strFullPath );
    }

    /**
     * Reload all properties files
     * 
     */
    public void reloadAll( )
    {
        Properties newProperties = new Properties( );

        for ( String strFullPath : _mapPropertiesFiles.values( ) )
        {
            loadFile( strFullPath, newProperties );
        }

        _properties = newProperties;
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty
     *            The variable name
     * @return The variable value read in the properties file
     */
    public String getProperty( String strProperty )
    {
    	String strValue = _properties.getProperty( strProperty ) ;
    	if ( strValue != null && strValue.startsWith( RSA_KEY_PREFIX ) )
		{
			try 
			{
				return RsaService.decryptRsa( strValue.substring( RSA_KEY_PREFIX.length( ) ) );
			} 
			catch ( GeneralSecurityException e ) 
			{
				LOGGER.error( MESSAGE_CIPHERED_PROPERTY_SECURITY_EXCEPTION, e );
			}
		}

   		return strValue;
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
    public String getProperty( String strProperty, String strDefault )
    {
        return _properties.getProperty( strProperty, strDefault );
    }
    /**
     * Gets properties
     * 
     * @return All properties
     */
    public Properties getProperties( )
    {
        return _properties;
    }
}
