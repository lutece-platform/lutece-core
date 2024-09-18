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
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderPageCategory;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderType;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.securityheader.SecurityHeaderService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Page security header filter
 * This filter is used to add security headers to the response when the requested resource is a jsp page except
 * for BO admin authenticated pages. For those pages, security headers adding is managed by AuthenticationFilter class.
 * When the request resource is a API REST endpoint, security headers adding is done in RestApiSecurityHeaderFilter class
 * 
 */
public class PageSecurityHeaderFilter implements javax.servlet.Filter
{

	private Logger _logger = LogManager.getLogger( "lutece.securityHeader" );
	private static final String PROPERTY_JSP_URL_PORTAL_LOGOUT = "mylutece.url.doLogout";
	
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
        
    	addAllPagesHeaders( req, resp );
    	addFoAuthenticatedPagesHeaders( req, resp );
    	addFoLogoutPageHeaders( req, resp );
    	
        filterChain.doFilter( req, resp );
    }
    
    /**
     * Adds active security headers that must be added to all pages to the response
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    private void addAllPagesHeaders( HttpServletRequest request, HttpServletResponse response )
    {
    	addActiveHeaders( request, response, SecurityHeaderType.PAGE, SecurityHeaderPageCategory.ALL );
    }
     
    /**
     * Adds active security headers that must be added to FO authenticated pages to the response
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    private void addFoAuthenticatedPagesHeaders( HttpServletRequest request, HttpServletResponse response )
    {
    	if( SecurityService.getInstance( ).getRegisteredUser( request ) != null )
    	{
    		addActiveHeaders( request, response, SecurityHeaderType.PAGE, SecurityHeaderPageCategory.AUTHENTICATED_ADMIN_FRONT_OFFICE );
    	}
    }
    
    /**
     * Adds active security headers that must be added to the logout page to the response 
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     */
    private void addFoLogoutPageHeaders( HttpServletRequest request, HttpServletResponse response )
    {
    	String logoutPage = getAbsoluteUrl( request, AppPropertiesService.getProperty( PROPERTY_JSP_URL_PORTAL_LOGOUT ) );
    	if( logoutPage != null )
    	{
    		if( getRequestedUrl( request ).equals( logoutPage ) )
    		{
    			addActiveHeaders( request, response, SecurityHeaderType.PAGE, SecurityHeaderPageCategory.LOGOUT_FO );
    		}
    	}
    }
    
    /**
     * 
     * Adds active security headers of the specified type and the specified category to the response
     * 
     * @param request
     *            The HTTP request
     * @param response
     *            The HTTP response
     * @param typePage
     *            The type of page
     * @param pageCategory
     *            The category of page (if type is equals to page)
     */
    private void addActiveHeaders( HttpServletRequest request, HttpServletResponse response, SecurityHeaderType typePage, SecurityHeaderPageCategory pageCategory )
    {
    	Collection<SecurityHeader> securityHeadersToAddList = getSecurityHeaderService( ).findActive( typePage.getCode( ), pageCategory.getCode( ) );
    	if( securityHeadersToAddList != null )
    	{
    		for( SecurityHeader securityHeader : securityHeadersToAddList )
        	{
        		response.setHeader( securityHeader.getName( ), securityHeader.getValue( ) );
        		_logger.debug( "Security header added to page {} - name : {}, value : {} ", request.getServletPath( ), securityHeader.getName( ), securityHeader.getValue( ) );
        	}
    	}   	
    }
    
    /**
     * Destroy the filter
     */
    public void destroy( )
    {
        
    }
    
    /**
     * Returns the security header service.
     * 
     * @return the security header service
     */
    private SecurityHeaderService getSecurityHeaderService( )
    {
    	return SpringContextService.getBean( "securityHeaderService" );
    }
    
    /**
     * Return the absolute representation of the requested url
     * 
     * @param request
     *            the http request (provides the base path if needed)
     * @return the requested url has a string
     *
     */
    private String getRequestedUrl( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + request.getServletPath( ).substring( 1 );
    }
    
    /**
     * Returns the absolute url corresponding to the given one, if the later was found to be relative. An url starting with "http://" is absolute. A relative
     * url should be given relatively to the webapp root.
     * 
     * @param request
     *            the http request (provides the base path if needed)
     * @param strUrl
     *            the url to transform
     * @return the corresponding absolute url
     *
     */
    private String getAbsoluteUrl( HttpServletRequest request, String strUrl )
    {
        if ( ( strUrl != null ) && !strUrl.startsWith( "http://" ) && !strUrl.startsWith( "https://" ) )
        {
            return AppPathService.getBaseUrl( request ) + strUrl;
        }

        return strUrl;
    }
}