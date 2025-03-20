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
package fr.paris.lutece.portal.service.html;

import jakarta.enterprise.inject.spi.CDI;

/**
 * This class provides management methods for cleaner
 */
public final class XSSSanitizerService
{
    /** html Cleaner */
    private static IXSSSanitizer _xssSanitizer = CDI.current().select(IXSSSanitizer.class).get( );
    private static boolean _bInit;

    /**
     * Constructor. Creates a new XSSSanitizerService object.
     */
    private XSSSanitizerService( )
    {
    }

    /**
     * Clean HTML code from XSS risks
     *
     * @param strSource
     *            The input string to clean
     * @return The cleaned string
     */
    public static String sanitize( String strSource )
    {
        init( );
        
        if ( _xssSanitizer != null )
        {
        	// use advanced implementation
        	return _xssSanitizer.sanitize( strSource );
        }
        else
        {
        	// use default
        	return cleanXSS( strSource );
        }
        
    }

    private static void init( )
    {
        // init XSSSanitizerService
        if ( !_bInit && _xssSanitizer != null )
        {
        	_xssSanitizer.init( );
            _bInit = true;
        }
    }
    
    /**
     * default xss clean (escape chars only)
     * 
     * @param value
     * @return the cleaned value
     */
    private static String cleanXSS( String value ) 
    {
        if (value != null) {
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("\"", "&quot;").replaceAll("'", "&#x27;");
            value = value.replaceAll("&", "&amp;");
            value = value.replaceAll("\\(" , "&#40;").replaceAll("\\)", "&#41;");
        }
        return value;
    }
}
