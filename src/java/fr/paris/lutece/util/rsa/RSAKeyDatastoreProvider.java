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
import fr.paris.lutece.portal.service.util.AppLogService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RSAKeyDatastoreProvider implements IRSAKeyProvider 
{

	private static final String DATASTORE_PUBLIC_KEY = "lutece.rsa.key.public";
    private static final String DATASTORE_PRIVATE_KEY = "lutece.rsa.key.private";

	@Override
	public PublicKey getPublicKey( ) throws GeneralSecurityException 
	{
		if ( !DatastoreService.existsKey( DATASTORE_PUBLIC_KEY ) )
		{ 
			initKeys( );
		}
		
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(
                    Base64.getDecoder( ).decode( DatastoreService.getDataValue( DATASTORE_PUBLIC_KEY, "" ).getBytes( ) ) );
            
        KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

        return keyFactory.generatePublic( keySpecPublic );
        
	}
	
	@Override
	public PrivateKey getPrivateKey( ) throws GeneralSecurityException 
	{
		if ( !DatastoreService.existsKey( DATASTORE_PRIVATE_KEY ) )
        {
			initKeys();
        }

        PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(
                Base64.getDecoder( ).decode( DatastoreService.getDataValue( DATASTORE_PRIVATE_KEY, "" ).getBytes( ) ) );

        KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

        return keyFactory.generatePrivate( keySpecPrivate );
	}

	/**
	 * Initializes the RSA key pair in the datastore if missing.
	 * <p>
	 * Uses {@link DatastoreService#insertDataValueIfAbsent(String, String)} so
	 * that concurrent initialization from multiple application instances is
	 * safe: only the first instance wins, the others see their generated pair
	 * silently discarded and will read the stored pair on the next call.
	 * </p>
	 *
	 * @throws GeneralSecurityException
	 *             if RSA key generation fails
	 */
	private void initKeys( ) throws GeneralSecurityException
	{
		// Generate a candidate key pair (will be discarded if another instance wins the race)
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
	    keyGen.initialize( 2048 );
	    KeyPair pair = keyGen.generateKeyPair( );
	    PrivateKey privateKey = pair.getPrivate( );
	    PublicKey publicKey = pair.getPublic( );

	    String strPublic  = Base64.getEncoder( ).encodeToString( publicKey.getEncoded( ) );
	    String strPrivate = Base64.getEncoder( ).encodeToString( privateKey.getEncoded( ) );

	    // Atomic conditional insert — relies on the primary key uniqueness
	    // constraint of core_datastore.entity_key. Portable across MariaDB,
	    // PostgreSQL, Oracle, H2, etc.
	    boolean bPublicInserted  = DatastoreService.insertDataValueIfAbsent( DATASTORE_PUBLIC_KEY,  strPublic  );
	    boolean bPrivateInserted = DatastoreService.insertDataValueIfAbsent( DATASTORE_PRIVATE_KEY, strPrivate );

	    if ( !bPublicInserted || !bPrivateInserted )
	    {
	        AppLogService.info( "RSA keys already initialized by another instance — using stored pair" );
	    }
	}

}
