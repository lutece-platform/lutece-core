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
package fr.paris.lutece.portal.web.encoding;

import javax.servlet.http.HttpServletResponse;


/**
 * This class provides a Wrapper of an Http response that manage a correct UTF-8 encoding
 *
 */
public class EncodingServletResponse extends javax.servlet.http.HttpServletResponseWrapper
{
    private static final String RESPONSE_ENCODING = "UTF-8";
    private boolean _bEncodingSpecified;

    /**
     * Constructor
     * @param response The HTTP response
     */
    public EncodingServletResponse( HttpServletResponse response )
    {
        super( response );
    }

    /**
     * Sets the content type
     * @param type The content type
     */
    public void setContentType( String type )
    {
        String explicitType = type;

        // If a specific encoding has not already been set by the app,
        // let's see if this is a call to specify it.  If the content
        // type doesn't explicitly set an encoding, make it UTF-8.
        if ( !_bEncodingSpecified )
        {
            String lowerType = type.toLowerCase(  );

            // See if this is a call to explicitly set the character encoding.
            if ( lowerType.indexOf( "charset" ) < 0 )
            {
                // If no character encoding is specified, we still need to
                // ensure the app is specifying text content.
                if ( lowerType.startsWith( "text/" ) )
                {
                    // App is sending a text response, but no encoding
                    // is specified, so we'll force it to UTF-8.
                    explicitType = type + "; charset=" + RESPONSE_ENCODING;
                }
            }
            else
            {
                // App picked a specific encoding, so let's make
                // sure we don't override it.
                _bEncodingSpecified = true;
            }
        }

        // Delegate to supertype to record encoding.
        super.setContentType( explicitType );
    }
}
