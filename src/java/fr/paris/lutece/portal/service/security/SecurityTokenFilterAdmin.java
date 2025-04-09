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
package fr.paris.lutece.portal.service.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.portal.service.admin.AdminTokenAccessDeniedException;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.web.LocalVariables;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to manage the CSRF token validation for MVCAdminJspBean requests.
 */
public class SecurityTokenFilterAdmin implements Filter
{
    private static final String EXCLUDED_RESOURCES_CONFIG = ".securityTokenFilterAdmin.exclude";

    private Set<String> _excludedResources = new HashSet<String>( );
    @Inject
    private SecurityTokenHandler _securityTokenHandler;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        Config config = ConfigProvider.getConfig( );
        _excludedResources = StreamSupport.stream( config.getPropertyNames( ).spliterator( ), false )
                .filter( n -> n.endsWith( EXCLUDED_RESOURCES_CONFIG ) )
                .flatMap( n -> Arrays.stream( config.getValue( n, String [ ].class ) ) )
                .collect( Collectors.toSet( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        LocalVariables.setLocal( null, httpServletRequest, httpServletResponse );
        _securityTokenHandler.handle( httpServletRequest );

        if ( _securityTokenHandler.shouldNotFilter( httpServletRequest )
                || isResourceExcluded( httpServletRequest ) )
        {
            chain.doFilter( request, response );
            return;
        }

        String action = MVCUtils.getAction( httpServletRequest );
        if ( !_securityTokenHandler.validate( httpServletRequest, action ) )
        {
            throw new ServletException( new AdminTokenAccessDeniedException( "Invalid security token for action: " + action ) );
        }

        chain.doFilter( request, response );
    }

    /**
     * Checks if the requested URI is excluded from security token validation.
     * 
     * @param request
     *            The request
     * @return
     *         True is the resource is excluded for the token validation.
     */
    private boolean isResourceExcluded( HttpServletRequest request )
    {
        boolean bExcluded = false;
        for ( String strExcluded : _excludedResources )
        {
            if ( request.getServletPath( ).contains( strExcluded ) )
            {
                bExcluded = true;
                break;
            }
        }
        return bExcluded;
    }

}
