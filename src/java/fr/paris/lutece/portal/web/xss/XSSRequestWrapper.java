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
package fr.paris.lutece.portal.web.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import fr.paris.lutece.portal.service.html.XSSSanitizerException;
import fr.paris.lutece.portal.service.html.XSSSanitizerService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class XSSRequestWrapper extends HttpServletRequestWrapper
{

    public XSSRequestWrapper( HttpServletRequest request )
    {
	super ( request);
    }

    @Override
    public String getParameter( String name )
    {
	try
	{
		if ( super.getParameter( name ) == null )
		{
			return null;
		}

	    return XSSSanitizerService.sanitize ( super.getParameter( name ) );
	} 
	catch ( XSSSanitizerException e )
	{
	    AppLogService.error ( "XSS Sanitizer error", e );
	    return null;
	}
    }

    @Override
    public String[ ] getParameterValues( String name )
    {
	String[ ] values = super.getParameterValues ( name );
	if ( values == null )
	{
	    return null;
	}
	for ( int i = 0; i < values.length; i++ )
	{
	    try
	    {
	    	if ( values[ i ] != null )
	    	{
	    		values[ i ] = XSSSanitizerService.sanitize ( ( values[ i ] ) );	
	    	}
	    } 
	    catch ( XSSSanitizerException e )
	    {
		AppLogService.error ( "XSS Sanitizer error", e );
		values[ i ] = null;
	    }
	}
	return values;
    }
}
