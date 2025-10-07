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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContext;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.content.ContentPostProcessorService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.datastore.CoreDataKeys;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.fileimage.FileImageService;
import fr.paris.lutece.portal.service.filter.FilterService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.servlet.ServletService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.bean.BeanUtil;

/**
 * This class initializes all the application services
 * 
 * @since 1.1
 */
public final class AppInit
{
    private static final String FILE_PROPERTIES_DATABASE = "db.properties";
    private static final String PATH_TEMPLATES = "/WEB-INF/templates/";
    private static final String LOGGER_LUTECE_INIT = "lutece.init";
    private static final String MAIN_LOG_FILE = "log4j2.xml";
    public static final String LOG4J_CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";


    private static boolean _bInitSuccessfull;
    private static String _strLoadingFailureCause;
    private static String _strLoadingFailureDetails;
    private static long startTime;
    private static boolean _bLog4jConfigurationFileSet;

 

    /**
     * Constructor
     */
    private AppInit( )
    {
    }
    /**
     * Initializes the Log4j2 configuration by setting the `log4j.configurationFile` system property.
     * This method should be called first, before any other components or libraries are initialized, 
     * to ensure that the Log4j logging system is properly configured.
     * 
     * If the system property `log4j.configurationFile` is not already set, this method constructs 
     * the configuration file path using the default location or custom plugin log configuration files.
     * 
     * The configuration file will be located at "/WEB-INF/conf/log.xml" by default, 
     * and it can include additional log configuration files specified by plugins.
     * The constructed path will be set as the value for the `log4j.configurationFile` system property.
     * 
     * <p><strong>Usage:</strong> Call this method at the very beginning of the application startup 
     * to ensure that Log4j is configured correctly before any logging occurs.</p>
     * 
     * @see org.apache.logging.log4j.LogManager
     */
    public static void initConfigLog() {
        // Record the start time if it hasn't been set yet
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        // Check if the log4j configuration file property is already set
        String log4jConfigurationFile = System.getProperty(LOG4J_CONFIGURATION_FILE_PROPERTY);
        if (log4jConfigurationFile == null || log4jConfigurationFile.isBlank()) {
            // Retrieve configuration log files from plugins if available
            Set<String> pathLogFilePlugins = WebConfResourceLocator.getPathConfLog();
            String luteceLog4jConfigurationFile = System.getProperty("log4j.luteceConfigurationFile");

            // Verify if there are plugin paths or a lutece configuration file
            if ((luteceLog4jConfigurationFile != null && !luteceLog4jConfigurationFile.isBlank())
                    || (pathLogFilePlugins != null && !pathLogFilePlugins.isEmpty())) {

                Set<String> paths = new LinkedHashSet<>(); // Ensures insertion order and avoids duplicates
                paths.add(MAIN_LOG_FILE);
                // Add plugin configuration paths if available
                if (pathLogFilePlugins != null) {
                    paths.addAll(pathLogFilePlugins);
                }
                // Add paths specified in the luteceLog4jConfigurationFile property, if present
                if (luteceLog4jConfigurationFile != null && !luteceLog4jConfigurationFile.isBlank()) {
                    paths.addAll(Arrays.asList(luteceLog4jConfigurationFile.split(",")));
                }

                // Construct the configuration paths string and set the system property
                String logConfigPaths = paths.stream()
                                             .map(path -> path.replace("\\", "/"))
                                             .collect(Collectors.joining(","));
                                             
                System.setProperty(LOG4J_CONFIGURATION_FILE_PROPERTY, logConfigPaths);
                _bLog4jConfigurationFileSet = true;
            }
        }
    }
    /**
     * Initializes all the application services (used for junit tests)
     * 
     * @param strConfPath
     *            The relative path to the config files
     */
    public static void initServices( String strConfPath )
    { 	   
    	AppPathService.initResourceManager();
    	initServices( null, strConfPath );
    }

    /**
     * Initializes all the application services
     * 
     * @param context
     *            The servlet context
     * @param strConfPath
     *            The relative path to the config files
     * @param strRealPath
     *            The real path to the config files
     */
    public static void initServices( ServletContext context, String strConfPath)
    {    	  
    	Logger _logger = LogManager.getLogger( LOGGER_LUTECE_INIT );    	
        try
        {
            // Initialize and run StartUp services
            AppLogService.info( "Running extra startup services ..." );           
            // Initializes the template services from the servlet context information
			AppTemplateService.init( PATH_TEMPLATES, context );     
			// Initializes the Datastore Service
			DatastoreService.init( );	
			
			// BeanUtil initialization, considering Lutèce availables locales and date
			// format properties
			BeanUtil.init( );
			// Initializes the connection pools
			try {
				AppConnectionService.init( strConfPath, FILE_PROPERTIES_DATABASE, "portal" );
				AppLogService.info( "Creating connexions pool 'portal'." );			
			} catch (LuteceInitException e) {
				_logger.error("Error ininitialised service", e);
				_strLoadingFailureCause = e.getMessage( );
	            Throwable cause = e.getCause( );
	            while ( cause != null )
	            {
	                _strLoadingFailureDetails = cause.getMessage( );
	                cause = cause.getCause( );
	            }
			}
			StartUpServiceManager.initializeEarlyInitializationServices( );
            StartUpServiceManager.init( );
            // Initializes Search Engine Indexation Service
            IndexationService.init( );
            // Initializes PluginService
            AppLogService.info( "Initializing plugins ..." );
            PluginService.init( );
            // Initializes FilterService and ServletService
            AppLogService.info( "Initializing plugins filters ..." );
            FilterService.init( context );
            AppLogService.info( "Initializing plugins servlets ..." );
            ServletService.init( context );

            // Trace Contents services loading
            traceContentServicesLoading( );

            // Initializes the SecurityService
            SecurityService.init( );

            // Initializes plugins auto-includes and auto-imports - needs to be launched before the daemons
            // (indexer could fail)
            AppTemplateService.initMacros( );

            // Initializes the daemons service
            AppDaemonService.init( );

            // Initializes the admin authentication module
            AdminAuthenticationService.init( );

            // Initialize FileImageService
            FileImageService.init( );

            // Process post startup services
            AppLogService.info( "Running post startup services ..." );
            PostStartUpServiceManager.init( );

            // Initialize Content Post Processor Service
            ContentPostProcessorService.init( );

            _bInitSuccessfull = true;
            long endTime = System.currentTimeMillis(); 
            long duration = endTime - startTime; 
            logStartupTime( );
            // Start datastore's cache after all processes that may use Datastore
            DatastoreService.startCache( );
            String strBaseUrl = getBaseUrl( context );
            
            StringBuilder sbBanner = new StringBuilder( );
            sbBanner.append( AppInfo.LUTECE_BANNER_SERVER ).append( "  started successfully" )
                    .append( "\n   Front office " )
                    .append( strBaseUrl ).append(AppPathService.getPortalUrl( ))
                    .append( "\n   Back office  " )
                    .append( strBaseUrl ).append( AppPathService.getAdminMenuUrl( ) ).append( "\n" )
                    .append("   Lutece https port: ["+AppPropertiesService.getProperty("https.port","")).append("]\n" )
                    .append("   Lutece application services started in : ").append(duration/1000.0 ).append(" secondes \n");                      
            _logger.info( sbBanner.toString( ) );                                          
        }
        catch( Exception e)
        {
        	_logger.error("Error ininitialised service", e);
            _strLoadingFailureCause = e.getMessage( );
            Throwable cause = e.getCause( );
            while ( cause != null )
            {
                _strLoadingFailureDetails = cause.getMessage( );
                cause = cause.getCause( );
            }
        }
    }
    /**
     * Get a base url to display in start logs
     * 
     * @param context
     *            the servlet context
     * @return the base url
     */
    private static String getBaseUrl( ServletContext context )
    {
        StringBuilder sbBaseUrl=new StringBuilder("http(s)://");
		try {
			sbBaseUrl.append(InetAddress.getLocalHost().getCanonicalHostName( ))
					.append(":").append(AppPropertiesService.getProperty("http.port","port"));
		} catch (UnknownHostException e) {
			AppLogService.error(e.getMessage(),e);
		}
        if ( context != null )
        {
            sbBaseUrl.append( context.getContextPath( ) );
        }
        return sbBaseUrl.append( '/' ).toString( );
    }

    /**
     * Tells if Lutece Startup was successful
     * 
     * @return True, if no error, otherwise false
     */
    public static boolean isWebappSuccessfullyLoaded( )
    {
        return _bInitSuccessfull;
    }
    /**
     * Checks if the system property `log4j.configurationFile` is set.
     * This property determines the configuration file used by Log4j.
     * 
     * @return {@code true} if `log4j.configurationFile` is set, otherwise {@code false}.
     */
    public static boolean isLog4jConfigurationFileSet( )
    {
        return _bLog4jConfigurationFileSet;
    }

    /**
     * Returns the cause of the startup failure
     * 
     * @return the cause of the startup failure
     */
    public static String getLoadingFailureCause( )
    {
        return _strLoadingFailureCause;
    }

    /**
     * Returns details of the startup failure
     * 
     * @return details of the startup failure
     */
    public static String getLoadingFailureDetails( )
    {
        return _strLoadingFailureDetails;
    }

    /**
     * Traces Content Services loading
     */
    private static void traceContentServicesLoading( )
    {
        for ( ContentService cs : PortalService.getContentServicesList( ) )
        {
            AppLogService.info( "Content Service '{}' is loaded ", cs.getName( ) );
        }
    }

    /**
     * Log startup time.
     */
    private static void logStartupTime( )
    {
        String strStartupTime = DateFormat.getDateTimeInstance( ).format( new Date( ) );
        DatastoreService.setDataValue( CoreDataKeys.KEY_STARTUP_TIME, strStartupTime );
    }
    
}
