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
package fr.paris.lutece.portal.service.util;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * The Class CryptoService.
 */
public final class CryptoService
{
    // Properties
    private static final String PROPERTY_ENCODING = "lutece.encoding";
    private static final String PROPERTY_CRYPTO_KEY = "crypto.key";

    /**
     * Private constructor
     */
    private CryptoService(  )
    {
    }

    /**
    * Encrypt a data using an algorithm defined in lutece.properties
    * @param strDataToEncrypt The data to encrypt
    * @param strAlgorithm the algorithm
    * @return The encrypted string
    */
    public static String encrypt( String strDataToEncrypt, String strAlgorithm )
    {
        String hash = null;
        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance( strAlgorithm );
        }
        catch ( NoSuchAlgorithmException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        try
        {
            hash = byteToHex( md.digest( strDataToEncrypt.getBytes( AppPropertiesService.getProperty( PROPERTY_ENCODING ) ) ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return hash;
    }

    /**
     * Get the cryptographic key of the application
     * @return The cryptographic key of the application
     */
    public static String getCryptoKey(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_CRYPTO_KEY );
    }

    /**
     * Convert byte to hex
     * @param bits the byte to convert
     * @return the hex
     */
    private static String byteToHex( byte[] bits )
    {
        if ( bits == null )
        {
            return null;
        }

        // encod(1_bit) => 2 digits
        StringBuffer hex = new StringBuffer( bits.length * 2 );

        for ( int i = 0; i < bits.length; i++ )
        {
            if ( ( (int) bits[i] & 0xff ) < 0x10 )
            {
                // 0 < .. < 9
                hex.append( "0" );
            }

            // [(bit+256)%256]^16
            hex.append( Integer.toString( (int) bits[i] & 0xff, 16 ) );
        }

        return hex.toString(  );
    }
}
