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
package fr.paris.lutece.portal.service.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;

import fr.paris.lutece.util.rsa.RSAKeyPairUtil;

/**
 * Service for encrypting/decrypting data using RSA.
 */
public final class RsaService
{
    private static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";

    private RsaService( )
    {
    }

    /**
     * Encrypt the data
     * 
     * @param data
     * @return encrypted data
     * @throws GeneralSecurityException
     */
    public static final String encryptRsa( String data ) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance( RSA_PADDING );
        cipher.init( Cipher.ENCRYPT_MODE, RSAKeyPairUtil.getInstance( ).getPublicKey( ) );
        return Base64.getUrlEncoder( ).encodeToString( cipher.doFinal( data.getBytes( StandardCharsets.UTF_8 ) ) );
    }

    /**
     * decrypt the data
     * 
     * @param data
     * @return decrypted data
     * @throws GeneralSecurityException
     */
    public static final String decryptRsa( String data ) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance( RSA_PADDING );
        cipher.init( Cipher.DECRYPT_MODE, RSAKeyPairUtil.getInstance( ).getPrivateKey( ) );

        return new String( cipher.doFinal( Base64.getUrlDecoder( ).decode( data.getBytes( StandardCharsets.UTF_8 ) ) ) );
    }
}
