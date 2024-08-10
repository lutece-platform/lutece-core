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
package fr.paris.lutece.portal.service.filter;

import java.io.IOException;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderType;
import fr.paris.lutece.portal.service.securityheader.SecurityHeaderService;

/**
 * Rest api security header filter
 * This filter is used to add security headers to the response when the requested resource is a API REST endpoint.
 * 
 */
public class RestApiSecurityHeaderFilter implements Filter
{

	private Logger _logger = LogManager.getLogger( "lutece.securityHeader" );
	
    /**
     * Initializes the filter
     * 
     * @param filterConfig
     *            The filter config
     * @throws ServletException
     *             If an error occured
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        // This would be a good place to collect a parameterized
        // default encoding type. For brevity, we're going to
        // use a hard-coded value in this example.
    }

    /**
     * Apply the filter
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     * @param filterChain
     *            The Filter Chain
     * @throws IOException
     *             If an error occured
     * @throws ServletException
     *             If an error occured
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain ) throws IOException, ServletException
    {
    	HttpServletRequest req = ( HttpServletRequest ) request;
    	HttpServletResponse resp = ( HttpServletResponse )response;
        
    	addRestApiHeaders ( req, resp );
  	
        filterChain.doFilter( req, resp );
    }

    /**
     * Adds active security headers that must be added to REST API endpoints to the response
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    private void addRestApiHeaders( HttpServletRequest request, HttpServletResponse response )
    {
    	SecurityHeaderService securityHeaderService = CDI.current().select(SecurityHeaderService.class).get();
    	for( SecurityHeader securityHeader : securityHeaderService.findActive( SecurityHeaderType.REST_API.getCode( ), null ) )
    	{
    		response.setHeader( securityHeader.getName( ), securityHeader.getValue( ) );
    		_logger.debug( "Security header added to endpoint {} - name : {}, value : {} ", request.getServletPath(), securityHeader.getName( ), securityHeader.getValue( ) );
    	}
    }
    
    /**
     * Destroy the filter
     */
    public void destroy( )
    {
        // no-op
    }
}