/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.util.securityheader;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import fr.paris.lutece.util.http.AntPathMatcher;

import fr.paris.lutece.portal.business.securityheader.SecurityHeader;
import fr.paris.lutece.portal.business.securityheader.SecurityHeaderConfigItem;

public class SecurityHeaderUtil 
{
	public final static AntPathMatcher PATH_MATCHER = new AntPathMatcher( );
	private static final String CONSTANT_QUESTION_MARK = "?";
	
	/**
	 * Returns the value of the security header. If a custom value has been defined for the URL passed as a parameter, this custom value is returned
	 * else the default value is returned
	 * 
	 * @param request the request
	 * @param securityHeader Security header instance
	 * @return value of the header (custom or default)
	 */
	public static String getSecurityHeaderValue( HttpServletRequest request, SecurityHeader securityHeader )
    {
    	List<SecurityHeaderConfigItem> securityHeaderConfigItemList = securityHeader.getSecurityHeaderConfigItemList( );
        String value = securityHeader.getValue( );
        if( securityHeaderConfigItemList != null && !securityHeaderConfigItemList.isEmpty( ) )
        {
        	String url = getCurrentUrl( request );
        	for( SecurityHeaderConfigItem securityHeaderConfigItem : securityHeaderConfigItemList )
            {
            	if( PATH_MATCHER.match( securityHeaderConfigItem.getUrlPattern( ), url ) )
        		{
        			value = securityHeaderConfigItem.getHeaderCustomValue( );
    			    break;
        		}
    	    }
        }
        return value;
    }
	
	/**
	 * Returns current url without origin and root element
	 * 
	 * @param request the request
	 * @return current url
	 */
	private static String getCurrentUrl( HttpServletRequest request )
	{
		String currentUrl = request.getServletPath( );
		if( !StringUtils.isBlank( request.getQueryString( ) ) )
		{
			currentUrl = currentUrl + CONSTANT_QUESTION_MARK + request.getQueryString( );
		}
		
		return currentUrl;
	}
}