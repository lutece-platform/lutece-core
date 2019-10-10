/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * The Class CryptoService.
 */
public final class CryptoService
{
    private static final int CONSTANT_CRYPTOKEY_LENGTH_BYTES = 32;
    // Properties
    private static final String PROPERTY_ENCODING = "lutece.encoding";
    static final String PROPERTY_CRYPTO_KEY = "crypto.key";
    static final String DSKEY_CRYPTO_KEY = "core." + PROPERTY_CRYPTO_KEY;

    /**
     * Private constructor
     */
    private CryptoService( )
    {
    }

    /**
     * Encrypt a data using an algorithm defined in lutece.properties
     * 
     * @param strDataToEncrypt The data to encrypt
     * @param strAlgorithm     the algorithm
     * @return The encrypted string
     */
    public static String encrypt( String strDataToEncrypt, String strAlgorithm )
    {
        String hash = null;
        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance( strAlgorithm );
        } catch ( NoSuchAlgorithmException e )
        {
            AppLogService.error( e.getMessage( ), e );
        }

        if ( md != null )
        {
            try
            {
                hash = byteToHex( md
                        .digest( strDataToEncrypt.getBytes( AppPropertiesService.getProperty( PROPERTY_ENCODING ) ) ) );
            } catch ( UnsupportedEncodingException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }
        return hash;
    }

    /**
     * Get a digest of the content of a stream
     * 
     * @param stream       the stream containing the data to digest
     * @param strAlgorithm the digest Algorithm
     * @return hex encoded digest string
     * @see MessageDigest
     * @since 6.0.0
     */
    public static String digest( InputStream stream, String strAlgorithm )
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance( strAlgorithm );
        } catch ( NoSuchAlgorithmException e )
        {
            AppLogService.error( strAlgorithm + " not found", e );
            return null;
        }
        byte[] buffer = new byte[1024];
        try
        {
            int nNumBytesRead = stream.read( buffer );
            while ( nNumBytesRead != -1 )
            {
                digest.update( buffer, 0, nNumBytesRead );
                nNumBytesRead = stream.read( buffer );
            }
        } catch ( IOException e )
        {
            AppLogService.error( "Error reading stream", e );
            return null;
        }
        return byteToHex( digest.digest( ) );
    }

    /**
     * Get the cryptographic key of the application
     * 
     * @return The cryptographic key of the application
     */
    public static String getCryptoKey( )
    {
        String strKey = DatastoreService.getDataValue( DSKEY_CRYPTO_KEY, null );
        if ( strKey == null )
        {
            // no key as been generated for this application
            strKey = AppPropertiesService.getProperty( PROPERTY_CRYPTO_KEY );
            if ( strKey == null )
            {
                // no legacy key exists. Generate a random one
                Random random = new SecureRandom( );
                byte[] bytes = new byte[CONSTANT_CRYPTOKEY_LENGTH_BYTES];
                random.nextBytes( bytes );
                strKey = byteToHex( bytes );
            }
            DatastoreService.setDataValue( DSKEY_CRYPTO_KEY, strKey );
        }
        return strKey;
    }

    /**
     * Get the HmacSHA256 of a message using the app crypto key. The UTF-8
     * representation of the key is used.
     * 
     * @param message the message. The mac is calculated from the UTF-8
     *                representation
     * @return the hmac as hex
     * @since 6.0.0
     */
    public static String hmacSHA256( String message )
    {
        byte[] keyBytes;
        try
        {
            keyBytes = getCryptoKey( ).getBytes( "UTF-8" );
        } catch ( UnsupportedEncodingException e )
        {
            throw new AppException( "UTF-8 should be supported", e );
        }
        final String strAlg = "HmacSHA256";
        SecretKeySpec key = new SecretKeySpec( keyBytes, strAlg );

        try
        {
            Mac mac = Mac.getInstance( strAlg );
            mac.init( key );

            return byteToHex( mac.doFinal( message.getBytes( "UTF-8" ) ) );
        } catch ( NoSuchAlgorithmException e )
        {
            throw new AppException( "Could not find " + strAlg + " algorithm which is supposed to be supported by Java",
                    e );
        } catch ( InvalidKeyException e )
        {
            throw new AppException( "The key should be valid", e );
        } catch ( IllegalStateException e )
        {
            throw new AppException( e.getMessage( ), e );
        } catch ( UnsupportedEncodingException e )
        {
            throw new AppException( "UTF-8 should be supported", e );
        }
    }

    /**
     * Convert byte to hex
     * 
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
        StringBuilder hex = new StringBuilder( bits.length * 2 );

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

        return hex.toString( );
    }
}
