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
package fr.paris.lutece.util.url;


/**
 * This class provides utility methods to manipulate an url parameter
 */
public class UrlParameterItem
{
    ////////////////////////////////////////////////////////////////////////////
    // Attributes

    /** the name of the parameter. */
    private String _strName;

    /** the value of the parameter. */
    private String _strValue;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Constructs a UrlParameterItem.
     * @param strName The name of the parameter.
     * @param strValue The value of the parameter.
     */
    public UrlParameterItem( String strName, String strValue )
    {
        _strName = strName;
        _strValue = strValue;
    }

    /**
     * Constructs a UrlParameterItem.
     * @param strName The name of the parameter.
     * @param nValue The value of the parameter.
     */
    public UrlParameterItem( String strName, int nValue )
    {
        _strName = strName;
        _strValue = Integer.toString( nValue );
    }

    ////////////////////////////////////////////////////////////////////////////
    // public method

    /**
     * Return the string to insert the parameter in the url.
     *
     * @param bFirst True if this is the first url parameter.
     * @return String The string to insert in the url.
     */
    public String getCode( boolean bFirst )
    {
        return ( ( bFirst ) ? "?" : "&" ) + _strName + "=" + _strValue;
    }

    /**
     * Return the string to insert the parameter in the url.
     *
     * @param bFirst True if this is the first url parameter.
     * @return String The string to insert in the url.
     */
    public String getCodeEntity( boolean bFirst )
    {
        return ( ( bFirst ) ? "?" : "&#38;" ) + _strName + "=" + _strValue;
    }
}
