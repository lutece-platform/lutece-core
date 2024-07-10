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
package fr.paris.lutece.portal.service.init;

import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.scheduler.JobSchedulerService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.LuteceConfigSource;
import fr.paris.lutece.util.config.MapConverter;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.ServletContext;

/**
 * The initialization class of the application. This class is declared load-on-startup in the downloadFile web.xml
 */
@ApplicationScoped
public class AppInitListener 
{
    // ////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String PATH_CONF = "/WEB-INF/conf/";

    /**
     * Initialize the service of application
     * 
     * @param context
     *            the context servlet initialized event
     */
	public void initializedService(@Observes @Initialized(ApplicationScoped.class) @Priority(value=1)
	ServletContext context){		
        AppLogService.info( "Started initializing services");
        
        AppPathService.init( context );
        
        // Those lines are a workaround in case Config API impl is calling getPropertyNames before CDI AppInitExtension (WildFly)
        Config config = ConfigProvider.getConfig( );
        if (null == config.getConfigValue( "lutece.name" ).getValue( )) {
            ConfigProviderResolver resolver = ConfigProviderResolver.instance();
            ConfigBuilder builder = resolver.getBuilder();
            Config newConfig = builder.addDefaultSources( ).withSources( new LuteceConfigSource( ) ).withConverters( new MapConverter( ) ).build( );
            resolver.releaseConfig( config );
            resolver.registerConfig(newConfig, getClass( ).getClassLoader( ));
            System.setProperty( "log4j.configurationFile", "file:" + AppPathService.getWebAppPath( ) + "/WEB-INF/conf/log.properties" );
        }
        
        // Initializes properties service
	    AppInit.initPropertiesServices(PATH_CONF, AppPathService.getWebAppPath( ));
	}
	
    /**
     * Initialize the service of application
     * 
     * @param context
     *            the context servlet initialized event
     */
	public void initializedOtherService(@Observes @Initialized(ApplicationScoped.class) @Priority(value=3)
		ServletContext context){
	    // Initializes all other services
	    AppInit.initServices(context, PATH_CONF, AppPathService.getWebAppPath( ));
        AppLogService.info( "End initializing services");
	}
	
    /**
     * Shutdown the application
     * 
     * @param destroyed
     *            context event
     */
	public void contextDestroyed(@Observes @Destroyed(ApplicationScoped.class) 
		ServletContext context){
		MailService.shutdown( );
        AppDaemonService.shutdown( );
        JobSchedulerService.shutdown( );
        ShutdownServiceManager.shutdown( );
        CacheService.getInstance( ).shutdown( );
        AppConnectionService.releasePool( );
        AppLogService.info( "Application stopped" );
	}
	
    /**
     * Initialize the service of application. This method is a workaround to the Glassfish / Payara Issue https://github.com/payara/Payara/issues/5968
     * 
     * @param context
     *            the context servlet event
     */
    public void initializedServiceFallback( @Observes @Priority( value = 6 ) ServletContext context )
    {
        if ( !AppInit.isWebappSuccessfullyLoaded( ) )
        {
            initializedService( context );
            initializedOtherService( context );
        }
    }
}
