/*
 * Copyright (c) 2002-2024, City of Paris
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

import java.util.List;
import java.util.stream.StreamSupport;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.portal.service.util.AppLogService;
import jakarta.annotation.Priority;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class UninstalledPluginExtension implements Extension
{
    private static final String KEY_UNINSTALLED_PLUGIN = "plugins.uninstalled.";

    public void initSystemProperties( @Observes @Priority( value = 2 ) final BeforeBeanDiscovery bd )
    {
        AppLogService.debug( "Processing UninstalledPluginExtension" );
        Config config = ConfigProvider.getConfig( );

        List<String> listUninstalledPluginsKeys = StreamSupport.stream( config.getPropertyNames( ).spliterator( ), false )
                .filter( s -> s.startsWith( KEY_UNINSTALLED_PLUGIN ) )
                .toList( );
        for ( String key : listUninstalledPluginsKeys )
        {
            String strValue = config.getValue( key, String.class );
            System.setProperty( key, strValue );
            AppLogService.debug( "Uninstalled plugin key found and set as system property: {} / {} ", key, strValue );
        }
    }

    /**
     * Clears system properties related to uninstalled plugins that may have been set within this extension.
     */
    public static void clearSystemProperties( )
    {
        System.getProperties( ).keySet( ).stream( )
                .filter( k -> String.valueOf( k ).startsWith( UninstalledPluginExtension.KEY_UNINSTALLED_PLUGIN ) )
                .map( k -> (String) k )
                .forEach( System::clearProperty );
    }
}
