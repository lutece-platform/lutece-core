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

import java.nio.file.Files;
import java.nio.file.Paths;

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.cdi.mvc.RedirectScopeContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

/**
 * The initialization service of the application.
 */
public class AppInitExtension implements Extension
{
    private static final String PATH_CONF = "/WEB-INF/conf/";

    protected void initPropertiesServices( @Observes @Priority(value=1) final BeforeBeanDiscovery bd )
    {
    	if ( !Files.isReadable( Paths.get( AppPathService.getWebAppPath( ) + PATH_CONF ) ) )
        {
	    	String _strResourcesDir = getClass().getResource( "/" ).toString( ).replaceFirst( "file:", "" )
	                .replaceFirst( "test-classes", "lutece" )
	                .replaceFirst( "classes", "lutece" );
	        AppPathService.init( _strResourcesDir );
        }
    	AppInit.initConfigLog( );
    }
    /**
     * After bean discovery.
     *
     * @param event the event.
     * @param beanManager the bean manager.
     */
    public void registerRedirectScope(@Observes AfterBeanDiscovery event ) {
        event.addContext(new RedirectScopeContext());
    }
}
