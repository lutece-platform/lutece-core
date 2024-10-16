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
package fr.paris.lutece.portal.service.init;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

/**
 * The initialization service of the application.
 */
public class AppInitExtension implements Extension
{
    private static final String PATH_CONF = "/WEB-INF/conf/";
    private static final String FILE_PROPERTIES_CONFIG = "config.properties";

    protected void initPropertiesServices( @Observes final BeforeBeanDiscovery bd )
    {       
        Thread.currentThread( ).setName( "Lutece-MainThread" );
        
        if ( Files.notExists( Paths.get( AppPathService.getWebAppPath( ) + PATH_CONF ) ) )
        {
        	String _strResourcesDir = getClass( ).getResource( "/" ).toString( ).replaceFirst( "file:", "" )
                    .replaceFirst( "test-classes", "lutece" )
                    .replaceFirst( "classes", "lutece" );
            AppPathService.init( _strResourcesDir );
        }
        
        if ( Files.exists( Paths.get( AppPathService.getWebAppPath( ) + PATH_CONF + FILE_PROPERTIES_CONFIG ) ) )
        {
        	String log4jConfigurationFile = System.getProperty( "log4j.configurationFile" );
            if ( StringUtils.isEmpty( log4jConfigurationFile ) )
            {
                String pluginLogsConfigFiles = StringUtils.EMPTY;
                try
                {
                    pluginLogsConfigFiles = Files.walk( Paths.get( AppPathService.getWebAppPath( ) + PATH_CONF ) )
                        .filter( p -> p.toString( ).endsWith( "_log.xml" ) )
                        .map( Path::toString ).collect( Collectors.joining( ",file:", "file:", "," ) );
                }
                catch( IOException e )
                {
                    throw new AppException( "Error loading plugins log4j config files", e );
                }
                
                log4jConfigurationFile = pluginLogsConfigFiles.replace("\\", "/") + ( "file:" + AppPathService.getWebAppPath( ) + "/WEB-INF/conf/log.xml" );
                System.setProperty( "log4j.configurationFile", log4jConfigurationFile );
            }
            AppLogService.info( " {} {} {} ...\n", AppInfo.LUTECE_BANNER_VERSION, "Starting  version", AppInfo.getVersion( ) );
            AppInit.initPropertiesServices( PATH_CONF, AppPathService.getWebAppPath( ) );
        }
    }

}
