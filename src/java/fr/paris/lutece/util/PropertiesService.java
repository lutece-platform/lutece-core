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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * This class provides utility methods to read values of the properties stored in the .properties file of the application.
 */
public class PropertiesService
{
    // Static variables
    private String _strRootPath;
    private volatile Properties _properties = new Properties( );
    private Set<String> _setPropertiesFiles = new LinkedHashSet<>( );

    public final String RSA_KEY_PREFIX = "PROTECTED::RSA::";
    private final String MESSAGE_CIPHERED_PROPERTY_SECURITY_EXCEPTION = "A ciphered property security exception occured." ;
    
    /**
     * Constructor should define the base root path for properties files
     * 
     * @param strRootPath
     *            The root path
     */
    public PropertiesService( String strRootPath )
    {
        _strRootPath = ( strRootPath.endsWith( "/" ) ) ? strRootPath : ( strRootPath + "/" );
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
        String strFullPath = _strRootPath + ( ( strRelativePath.endsWith( "/" ) ) ? strRelativePath : ( strRelativePath + "/" ) ) + strFilename;
        _setPropertiesFiles.add( strFullPath );
        loadFile( strFullPath );
    }

    /**
     * Add properties from all files found in a given directory
     * 
     * @param strRelativePath
     *            Relative path from the root path
     */
    public void addPropertiesDirectory( String strRelativePath )
    {
        File directory = new File( _strRootPath + strRelativePath );

        if ( directory.exists( ) )
        {
            File [ ] listFile = directory.listFiles( );

            if ( ArrayUtils.isNotEmpty( listFile ) )
            {
                for ( File file : listFile )
                {
                    if ( file.getName( ).endsWith( ".properties" ) )
                    {
                        String strFullPath = file.getAbsolutePath( );
                        _setPropertiesFiles.add( strFullPath );
                        loadFile( strFullPath );
                    }
                }
            }
        }
    }

    /**
     * Load properties of a file
     * 
     * @param strFullPath
     *            The absolute path of the properties file
     */
    private void loadFile( String strFullPath )
    {
        loadFile( strFullPath, _properties );
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
    private void loadFile( String strFullPath, Properties props )
    {
        try ( FileInputStream fis = new FileInputStream( new File( strFullPath ) ) )
        {
            props.load( fis );
        }
        catch( IOException ex )
        {
            AppLogService.error( "Error loading property file : {}", ex.getMessage( ), ex );
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
        List<String> listFullPaths = getFullPaths( strFilename );

        if ( listFullPaths.isEmpty( ) )
        {
            AppLogService.error( "Unable to reload the properties file {} : this file has not been registered", strFilename );

            return;
        }

        for ( String strFullPath : listFullPaths )
        {
            loadFile( strFullPath );
        }
    }

    /**
     * Returns the full paths of all the registered properties files having the given filename, in the order they have been registered. Several files may share
     * the same filename, typically a plugin properties file and its counterpart in the override directory.
     *
     * @param strFilename
     *            The filename of the properties file (ie: config.properties)
     * @return The list of matching full paths, empty if none has been registered
     */
    private List<String> getFullPaths( String strFilename )
    {
        return _setPropertiesFiles.stream( ).filter( strFullPath -> new File( strFullPath ).getName( ).equals( strFilename ) ).collect( Collectors.toList( ) );
    }

    /**
     * Reload all properties files
     *
     */
    public void reloadAll( )
    {
        Properties newProperties = new Properties( );

        for ( String strFullPath : _setPropertiesFiles )
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
				AppLogService.error( MESSAGE_CIPHERED_PROPERTY_SECURITY_EXCEPTION, e );
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
     * Returns the value of a variable defined in the .properties file of the application as an int
     *
     * @param strProperty
     *            The variable name
     * @param nDefault
     *            The default value which is returned if no value is found for the variable in the .properties file, or if the value is not numeric
     * @return The variable value read in the properties file
     */
    public int getPropertyInt( String strProperty, int nDefault )
    {
        String strValue = AppPropertiesService.getProperty( strProperty );
        int nValue = nDefault;

        try
        {
            if ( strValue != null )
            {
                nValue = Integer.parseInt( strValue );
            }
        }
        catch( NumberFormatException e )
        {
            AppLogService.info( e.getMessage( ), e );
        }

        return nValue;
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
    public long getPropertyLong( String strProperty, long lDefault )
    {
        String strValue = AppPropertiesService.getProperty( strProperty );
        long lValue = lDefault;

        try
        {
            if ( strValue != null )
            {
                lValue = Long.parseLong( strValue );
            }
        }
        catch( NumberFormatException e )
        {
            AppLogService.info( e.getMessage( ), e );
        }

        return lValue;
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as an boolean
     *
     * @param strProperty
     *            The variable name
     * @param bDefault
     *            The default value which is returned if no value is found for the variable in the le downloadFile .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public boolean getPropertyBoolean( String strProperty, boolean bDefault )
    {
        String strValue = AppPropertiesService.getProperty( strProperty );
        boolean bValue = bDefault;

        if ( strValue != null )
        {
            bValue = strValue.equalsIgnoreCase( "true" );
        }

        return bValue;
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
