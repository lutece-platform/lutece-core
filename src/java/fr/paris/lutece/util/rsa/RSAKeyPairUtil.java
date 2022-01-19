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
package fr.paris.lutece.util.rsa;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import fr.paris.lutece.portal.service.datastore.DatastoreService;

public class RSAKeyPairUtil
{
    private static final String DATASTORE_PUBLIC_KEY = "lutece.rsa.key.public";
    private static final String DATASTORE_PRIVATE_KEY = "lutece.rsa.key.private";

    private static RSAKeyPairUtil _instance;

    private PrivateKey _privateKey;
    private PublicKey _publicKey;

    private RSAKeyPairUtil( ) throws GeneralSecurityException
    {
        readKeys( );
    }

    public static RSAKeyPairUtil getInstance( ) throws GeneralSecurityException
    {
        if ( _instance == null )
        {
            _instance = new RSAKeyPairUtil( );
        }
        return _instance;
    }

    /**
     * @return the privateKey
     */
    public PrivateKey getPrivateKey( )
    {
        return _privateKey;
    }

    /**
     * @return the publicKey
     */
    public PublicKey getPublicKey( )
    {
        return _publicKey;
    }

    private void readKeys( ) throws GeneralSecurityException
    {
        if ( DatastoreService.existsKey( DATASTORE_PUBLIC_KEY ) && DatastoreService.existsKey( DATASTORE_PRIVATE_KEY ) )
        {
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(
                    Base64.getDecoder( ).decode( DatastoreService.getDataValue( DATASTORE_PUBLIC_KEY, "" ).getBytes( ) ) );
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(
                    Base64.getDecoder( ).decode( DatastoreService.getDataValue( DATASTORE_PRIVATE_KEY, "" ).getBytes( ) ) );

            KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

            this._publicKey = keyFactory.generatePublic( keySpecPublic );
            this._privateKey = keyFactory.generatePrivate( keySpecPrivate );
        }
        else
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
            keyGen.initialize( 2048 );
            KeyPair pair = keyGen.generateKeyPair( );
            this._privateKey = pair.getPrivate( );
            this._publicKey = pair.getPublic( );

            DatastoreService.setDataValue( DATASTORE_PUBLIC_KEY, Base64.getEncoder( ).encodeToString( _publicKey.getEncoded( ) ) );
            DatastoreService.setDataValue( DATASTORE_PRIVATE_KEY, Base64.getEncoder( ).encodeToString( _privateKey.getEncoded( ) ) );
        }
    }
}
