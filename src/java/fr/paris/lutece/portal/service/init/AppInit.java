/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.fileimage.FileImageService;
import fr.paris.lutece.portal.service.filter.FilterService;
import fr.paris.lutece.portal.service.html.XmlTransformerCacheService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.FileInputStream;
import java.io.FileWriter;

import java.util.HashMap;
import java.util.Properties;


/**
 * This class initializes all the application services
 * @since 1.1
 */
public final class AppInit
{
    private static final String PROPERTY_AUTOINIT = "autoInit";
    private static final String PROPERTY_INIT_WEBAPP_PROD_URL = "init.webapp.prod.url";
    private static final String MARK_WEBAPP_HOME = "webapp_home";
    private static final String MARK_PROD_URL = "lutece_prod_url";
    private static final String MARK_AUTOINIT = "autoinit";
    private static final String PATH_CONFIG = "/WEB-INF/conf/";
    private static final String FILE_PROPERTIES_CONFIG = "config.properties";
    private static final String FILE_PROPERTIES_DATABASE = "db.properties";
    private static final String PATH_TEMPLATES = "/WEB-INF/templates/";
    private static final String CONFIG_PROPERTIES_TEMPLATE = "admin/system/config_properties.html";
    private static boolean _bInitSuccessfull;
    private static String _strLoadingFailureCause;
    private static String _strLoadingFailureDetails;

    /**
     * Constructor
     */
    private AppInit(  )
    {
    }

    /**
     * Initializes all the application services
     * @param strConfPath The relative path to the config files
     */
    public static void initServices( String strConfPath )
    {
        initServices( strConfPath, null );
    }

    /**
     * Initializes all the application services
     * @param strConfPath The relative path to the config files
     * @param strRealPath The real path to the config files
     */
    public static void initServices( String strConfPath, String strRealPath )
    {
        try
        {
            // Initializes the log service
            AppLogService.init( strConfPath, FILE_PROPERTIES_CONFIG );
            AppLogService.info( "Starting application..." );

            // Initializes the properties download files containing the variables used by the application
            AppPropertiesService.init( strConfPath );

            // Initializes the template services from the servlet context information
            AppTemplateService.init( PATH_TEMPLATES );

            if ( strRealPath != null )
            {
                // Initializes the properties download files containing the
                // variables used by the application
                initProperties( strRealPath );
            }

            // Initializes the connection pools
            AppConnectionService.init( strConfPath, FILE_PROPERTIES_DATABASE, "portal" );
            AppLogService.info( "Creating connexions pool 'portal'." );

            // Spring ApplicationContext initialization
            SpringContextService.init(  );

            // Initialize and run StartUp services
            StartUpServiceManager.init(  );

            // XmlTransformer service cache manager
            XmlTransformerCacheService.init(  );

            AdminMailingListService.init(  );

            // Initializes Search Engine Indexation Service
            IndexationService.init(  );

            // Initializes PluginService
            PluginService.init(  );

            // Initializes FilterService
            FilterService.init(  );

            // Trace Contents services loading
            traceContentServicesLoading(  );

            // Initializes the SecurityService
            SecurityService.init(  );

            // Initializes plugins autoincludes - needs to be launched before the daemons (indexer could fail)
            AppTemplateService.initAutoIncludes(  );

            // Initializes the daemons service
            AppDaemonService.init(  );

            // Initializes the admin authentication module
            AdminAuthenticationService.init(  );

            // Initialize FileImageService
            FileImageService.init(  );

            // Initialize AdminUserService
            AdminUserService.init(  );

            // Process post startup services
            PostStartUpServiceManager.init(  );

            _bInitSuccessfull = true;
        }
        catch ( LuteceInitException e )
        {
            _strLoadingFailureCause = e.getMessage(  );

            Throwable cause = e.getCause(  );

            while ( cause != null )
            {
                _strLoadingFailureDetails = cause.getMessage(  );
                cause = cause.getCause(  );
            }
        }
    }

    /**
     * Tells if Lutece Startup was successful
     * @return True, if no error, otherwise false
     */
    public static boolean isWebappSuccessfullyLoaded(  )
    {
        return _bInitSuccessfull;
    }

    /**
     * Returns the cause of the startup failure
     * @return the cause of the startup failure
     */
    public static String getLoadingFailureCause(  )
    {
        return _strLoadingFailureCause;
    }

    /**
     * Returns details of the startup failure
     * @return details of the startup failure
     */
    public static String getLoadingFailureDetails(  )
    {
        return _strLoadingFailureDetails;
    }

    /**
     * Traces Content Services loading
     */
    private static void traceContentServicesLoading(  )
    {
        for ( ContentService cs : PortalService.getContentServicesList(  ) )
        {
            AppLogService.info( "Content Service '" + cs.getName(  ) + "' is loaded " +
                ( cs.isCacheEnable(  ) ? " [ cache enable ] " : " [ cache disable ] " ) );
        }
    }

    /**
     * Initializes the config.properties file after first installation
     *
     * @param strRealPath The real path to the configuration file
     */
    private static void initProperties( String strRealPath )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        Properties p = new Properties(  );

        try
        {
            FileInputStream fis = new FileInputStream( strRealPath + PATH_CONFIG + FILE_PROPERTIES_CONFIG );
            p.load( fis );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        if ( p.getProperty( PROPERTY_AUTOINIT ).equals( "true" ) )
        {
            model.put( MARK_WEBAPP_HOME, AppPathService.getWebAppPath(  ) );
            model.put( MARK_PROD_URL, p.getProperty( PROPERTY_INIT_WEBAPP_PROD_URL ) );
            model.put( MARK_AUTOINIT, "false" );

            HtmlTemplate configTemplate = AppTemplateService.getTemplate( CONFIG_PROPERTIES_TEMPLATE, null, model );
            // reset configuration cache to avoid configuration caching before macros are set. See LUTECE-1460
            AppTemplateService.resetConfiguration(  );

            try
            {
                FileWriter fw = new FileWriter( strRealPath + PATH_CONFIG + FILE_PROPERTIES_CONFIG );
                fw.write( configTemplate.getHtml(  ) );
                fw.close(  );
            }
            catch ( Exception io )
            {
                io.printStackTrace(  );
            }
        }
    }
}
