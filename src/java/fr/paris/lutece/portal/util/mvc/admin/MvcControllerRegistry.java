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
package fr.paris.lutece.portal.util.mvc.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Registry of admin MVC controllers, keyed by their route name.
 * <p>
 * Built once at application startup by scanning every CDI bean that extends {@link MVCAdminJspBean}
 * and carries a {@link Controller} annotation. The route name is taken from {@link Controller#name()}
 * when set, otherwise from the bean {@link Named} value. The resulting map is consumed by the admin
 * MVC front-controller servlet to resolve the target bean of a {@code /jsp/admin/mvc/&#123;name&#125;}
 * request.
 * </p>
 */
@ApplicationScoped
public class MvcControllerRegistry
{
    private final Map<String, Bean<?>> _mapControllers = new HashMap<>( );

    @Inject
    private BeanManager _beanManager;

    /**
     * Scans the CDI bean archive and builds the route name to bean map at application startup.
     *
     * @param event the application scope initialization event observed to trigger the scan
     */
    public void onStartup( @Observes @Priority( 100 ) @Initialized( ApplicationScoped.class ) Object event )
    {
        for ( Bean<?> bean : _beanManager.getBeans( MVCAdminJspBean.class, Any.Literal.INSTANCE ) )
        {
            Controller controller = bean.getBeanClass( ).getAnnotation( Controller.class );

            if ( controller == null )
            {
                continue;
            }

            String strRouteName = resolveRouteName( bean, controller );

            if ( strRouteName.isBlank( ) )
            {
                continue;
            }

            registerController( strRouteName, bean );
        }

        AppLogService.info( "Admin MVC front-controller registry initialized with {} route(s)", _mapControllers.size( ) );
    }

    /**
     * Resolves the target controller bean for a given route name.
     *
     * @param strName the route name extracted from the request path
     * @return an {@link Optional} holding the matching bean, empty when no controller is registered under this name
     */
    public Optional<Bean<?>> resolve( String strName )
    {
        return Optional.ofNullable( _mapControllers.get( strName ) );
    }

    /**
     * Computes the route name of a controller, favoring the explicit annotation value over the bean name.
     *
     * @param bean the CDI bean of the controller
     * @param controller the controller annotation carried by the bean class
     * @return the route name, or an empty string when none can be determined
     */
    private String resolveRouteName( Bean<?> bean, Controller controller )
    {
        if ( !controller.name( ).isBlank( ) )
        {
            return controller.name( );
        }

        return bean.getName( ) != null ? bean.getName( ) : "";
    }

    /**
     * Registers a controller bean under a route name, failing fast on duplicate names.
     *
     * @param strRouteName the route name
     * @param bean the controller bean to register
     */
    private void registerController( String strRouteName, Bean<?> bean )
    {
        Bean<?> previous = _mapControllers.putIfAbsent( strRouteName, bean );

        if ( previous != null )
        {
            throw new AppException( "Duplicate admin MVC controller route name '" + strRouteName + "' declared by "
                    + previous.getBeanClass( ).getName( ) + " and " + bean.getBeanClass( ).getName( ) );
        }
    }
}
