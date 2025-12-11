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
package fr.paris.lutece.portal.service.init;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * StartUpServiceManager
 */
public final class StartUpServiceManager
{
    /**
     * Private constructor
     */
    private StartUpServiceManager( )
    {
    }
    /**
     * Initializes early initialization services.
     * <p>
     * This method loads and processes services implementing the {@link IEarlyInitializationService} interface
     * before starting the Spring context.
     * </p>
     */
    public static void initializeEarlyInitializationServices() {
        List<IEarlyInitializationService> serviceList = new ArrayList<>();

        // Load early initialization services using ServiceLoader
        ServiceLoader<IEarlyInitializationService> providers = ServiceLoader.load(IEarlyInitializationService.class);
        providers.forEach(serviceList::add);

        // Sort services by order before processing
        serviceList.stream()
                .sorted(Comparator.comparingInt(IEarlyInitializationService::getOrder))
                .forEach(service -> {
                    String serviceName = service.getClass().getName();
                    try {
                        // Display an informational message before processing the service
                        AppLogService.info("Processing StartUp service: {} before starting Spring Context", serviceName);
                        // Call the service processing method
                        service.process();
                        // Display a success message after processing the service
                        AppLogService.info("StartUp service processed successfully: {}", serviceName);
                    } catch (Exception e) {

                        // In case of error, display an error message and throw exception if the service is marked as critical
                        AppLogService.error("Error while processing StartUp service: {}", serviceName, e);
                        if(service.isCriticalService())throw e;
                    }
                });
    }
    /**
     * Runs all StartUp Services
     */
    public static void init( )
    {
        // Get all beans from the global ApplicationContext
        List<StartUpService> listServices = SpringContextService.getBeansOfType( StartUpService.class );

        // Process all services
        for ( StartUpService service : listServices )
        {
            try
            {
                AppLogService.info( "Processing StartUp service : {}", service.getName( ) );
                service.process( );
            }
            catch( Exception e )
            {
                AppLogService.error( "Error while processing StartUp service : {}", service.getName( ), e );
            }
        }
    }
}
