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
package fr.paris.lutece.portal.business.securityheader;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SecurityHeaderConfigItem
{
	private int _nIdConfigItem;
	
	private int _nIdSecurityHeader;
	
	@NotEmpty( message = "portal.securityheader.validation.security_header_config_item.config.HeaderCustomValue.notEmpty" )
	@Size( max = 1024 , message = "portal.securityheader.validation.security_header_config_item.config.HeaderCustomValue.size" )
	private String _strHeaderCustomValue;
	
	@NotEmpty( message = "portal.securityheader.validation.security_header_config_item.config.UrlPattern.notEmpty" )
	@Size( max = 1024 , message = "portal.securityheader.validation.security_header_config_item.config.UrlPattern.size" )
	private String _strUrlPattern;
	
	
	
	/**
     * Returns the item config id
     *
     * @return The item config id
     */
	public int getIdConfigItem( ) 
	{
		return _nIdConfigItem;
	}

	/**
     * Sets the item config id
     *
     * @param nIdConfigItem
     *            The item config id
     */
	public void setIdConfigItem( int nIdConfigItem ) 
	{
		this._nIdConfigItem = nIdConfigItem;
	}
	
    
    /**
     * Returns the security header id
     *
     * @return The security header id
     */
	public int getIdSecurityHeader( ) 
	{
		return _nIdSecurityHeader;
	}

	/**
     * Sets the security header id
     *
     * @param nId
     *            The security header id
     */
	public void setIdSecurityHeader( int nIdSecurityHeader ) 
	{
		this._nIdSecurityHeader = nIdSecurityHeader;
	}
    
    /**
     * Returns the header custom value of the security header
     *
     * @return the security header value as a String
     */
    public String getHeaderCustomValue( )
    {
        return _strHeaderCustomValue;
    }

    /**
     * Sets the header value of the security header
     *
     * @param strHeaderValue
     *            The security header value url
     */
    public void setHeaderCustomValue( String strHeaderCustomValue )
    {
        _strHeaderCustomValue = strHeaderCustomValue;
    }
    
    /**
     * Returns the url pattern to which the security header custom value applies
     *
     * @return the url pattern to which the security header custom value applies as a String
     */
    public String getUrlPattern( )
    {
        return _strUrlPattern;
    }

    /**
     * Sets the url pattern to which the security header custom value applies
     *
     * @param strUrlPattern
     *            The url pattern to which the security header custom value applies
     */
    public void setUrlPattern( String strUrlPattern )
    {
    	_strUrlPattern = strUrlPattern;
    }
}