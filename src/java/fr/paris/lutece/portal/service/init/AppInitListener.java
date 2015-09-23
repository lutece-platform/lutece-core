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
package fr.paris.lutece.portal.service.init;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.scheduler.JobSchedulerService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * The initialization servlet of the application. This servlet is declared load-on-startup in the downloadFile web.xml
 */
public class AppInitListener implements ServletContextListener
{
    //////////////////////////////////////////////////////////////////////////////////
    //Constants
    private static final String PATH_CONF = "/WEB-INF/conf/";

    /**
     * Initialize the application
     * @param sce context event
     */
    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        ServletContext context = sce.getServletContext(  );

        // Initializes the PathService that give Absolute paths or URL to other services
        AppPathService.init( context );

        // Initializes all other services
        AppInit.initServices( context, PATH_CONF, AppPathService.getWebAppPath(  ) );
    }

    /**
     * Shutdown the application
     * @param sce context event
     */
    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
        MailService.shutdown(  );
        AppDaemonService.shutdown(  );
        JobSchedulerService.shutdown(  );
        ShutdownServiceManager.shutdown(  );
        CacheService.getInstance(  ).shutdown(  );
        AppConnectionService.releasePool(  );
        SpringContextService.shutdown(  );
        AppLogService.info( "Application stopped" );
    }
}
