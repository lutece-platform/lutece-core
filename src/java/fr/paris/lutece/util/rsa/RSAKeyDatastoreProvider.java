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
    private static final int WAIT_PRIVATE_KEY_ATTEMPTS = 50;
    private static final long WAIT_PRIVATE_KEY_DELAY_MS = 100L;

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
	 * The public key is the election: only the instance that inserts it (insert if absent, atomic on the primary key of core_datastore) stores its
	 * private key. Inserting both keys independently let two instances starting together store the public key of one and the private key of the
	 * other: every encrypted link then failed. The other instances wait for the private key of the winner. A public key left without private key (an
	 * instance stopped between the two inserts) is replaced.
	 * </p>
	 *
	 * @throws GeneralSecurityException
	 *             if RSA key generation fails
	 */
	private void initKeys( ) throws GeneralSecurityException
	{
	    // Generate a candidate key pair (discarded if another instance wins the race)
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
	    keyGen.initialize( 2048 );
	    KeyPair pair = keyGen.generateKeyPair( );

	    String strPublic  = Base64.getEncoder( ).encodeToString( pair.getPublic( ).getEncoded( ) );
	    String strPrivate = Base64.getEncoder( ).encodeToString( pair.getPrivate( ).getEncoded( ) );

	    if ( DatastoreService.insertDataValueIfAbsent( DATASTORE_PUBLIC_KEY, strPublic ) )
	    {
	        DatastoreService.setDataValue( DATASTORE_PRIVATE_KEY, strPrivate );
	        return;
	    }

	    AppLogService.info( "RSA keys already initialized by another instance, using the stored pair" );

	    if ( !waitForPrivateKey( ) )
	    {
	        AppLogService.error( "RSA public key stored without private key, a new key pair is stored" );
	        DatastoreService.setDataValue( DATASTORE_PUBLIC_KEY, strPublic );
	        DatastoreService.setDataValue( DATASTORE_PRIVATE_KEY, strPrivate );
	    }
	}

	/**
	 * Waits for the private key stored by the instance that inserted the public key
	 *
	 * @return true if the private key is stored
	 * @throws GeneralSecurityException
	 *             if the wait is interrupted
	 */
	private boolean waitForPrivateKey( ) throws GeneralSecurityException
	{
	    for ( int i = 0; i < WAIT_PRIVATE_KEY_ATTEMPTS; i++ )
	    {
	        // the datastore cache may hold the key as missing
	        DatastoreService.evictCachedKey( DATASTORE_PRIVATE_KEY );

	        if ( DatastoreService.existsKey( DATASTORE_PRIVATE_KEY ) )
	        {
	            return true;
	        }

	        try
	        {
	            Thread.sleep( WAIT_PRIVATE_KEY_DELAY_MS );
	        }
	        catch( InterruptedException e )
	        {
	            Thread.currentThread( ).interrupt( );
	            throw new GeneralSecurityException( "Interrupted while waiting for the RSA private key", e );
	        }
	    }

	    return false;
	}

}
