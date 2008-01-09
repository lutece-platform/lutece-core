/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.util.crypto;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * This class provides some simple functions to encrypt or decrypt data.
 * This implementation is based on the javax.crypto.* API and the Sun implementation.
 * The algoritm used is the DES algorithm (symetric)
 *
 * @since 1.2.1
 */
public class CryptoUtil
{
    private static final String ALGORITHM_DES = "DES";
    private static final String ENCODING_UTF8 = "UTF8";

    /**
     * This function encrypt a given string using the DES algorithm.
     * @param strDataToEncrypt The String to encrypt
     * @param strKey The generated key used to encrypt
     * @return The encrypted string
     */
    public static String encrypt( String strDataToEncrypt, String strKey )
    {
        byte[] key = strKey.getBytes(  );

        // Get the KeyGenerator
        Provider sunJCE = new com.sun.crypto.provider.SunJCE(  );
        Security.addProvider( sunJCE );

        String strAlgorithm = ALGORITHM_DES; //On utilise un algorithme DES
        SecretKeySpec keySpec = null;
        DESKeySpec deskey = null;
        String strResult = "";

        try
        {
            // Prepare the key
            deskey = new DESKeySpec( key );
            keySpec = new SecretKeySpec( deskey.getKey(  ), ALGORITHM_DES );

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance( strAlgorithm );

            // Encrypt data
            cipher.init( Cipher.ENCRYPT_MODE, keySpec );

            // Encode the string into bytes using utf-8
            byte[] utf8 = strDataToEncrypt.getBytes( ENCODING_UTF8 ); // FIXME ?

            // Encrypt
            byte[] enc = cipher.doFinal( utf8 );

            // Encode bytes to base64 to get a string
            strResult = new sun.misc.BASE64Encoder(  ).encode( enc );
        }
        catch ( Exception e )
        {
            AppLogService.error( "Data encryption error", e );
        }

        return strResult;
    }

    /**
     * This function decrypt a given string using the DES algorithm.
     * @param strDataToDecrypt The String to decrypt
     * @param strKey The generated key used to decrypt
     * @return The encrypted string
     */
    public static String decrypt( String strDataToDecrypt, String strKey )
    {
        byte[] key = strKey.getBytes(  );

        // Get the KeyGenerator
        Provider sunJCE = new com.sun.crypto.provider.SunJCE(  );
        Security.addProvider( sunJCE );

        String strAlgorithm = ALGORITHM_DES;
        SecretKeySpec keySpec = null;
        DESKeySpec deskey = null;
        String strResult = "";

        try
        {
            // Prepare the key
            deskey = new DESKeySpec( key );
            keySpec = new SecretKeySpec( deskey.getKey(  ), ALGORITHM_DES );

            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance( strAlgorithm );
            cipher.init( Cipher.DECRYPT_MODE, keySpec );

            // Decrypt data
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder(  ).decodeBuffer( strDataToDecrypt );

            // Decrypt
            byte[] utf8 = cipher.doFinal( dec );

            // Decode using utf-8
            return new String( utf8, ENCODING_UTF8 );
        }

        catch ( Exception e )
        {
            AppLogService.error( "Data decryption error", e );
        }

        return strResult;
    }
}
