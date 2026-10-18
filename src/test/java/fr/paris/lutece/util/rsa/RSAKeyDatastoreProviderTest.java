/*
 * Copyright (c) 2002-2026, City of Paris
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
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * RSAKeyDatastoreProvider tests
 */
public class RSAKeyDatastoreProviderTest extends LuteceTestCase
{
    private static final String DATASTORE_PUBLIC_KEY = "lutece.rsa.key.public";
    private static final String DATASTORE_PRIVATE_KEY = "lutece.rsa.key.private";

    private String _strPublic;
    private String _strPrivate;

    @BeforeEach
    protected void saveKeys( )
    {
        _strPublic = DatastoreService.getDataValue( DATASTORE_PUBLIC_KEY, null );
        _strPrivate = DatastoreService.getDataValue( DATASTORE_PRIVATE_KEY, null );
        DatastoreService.removeData( DATASTORE_PUBLIC_KEY );
        DatastoreService.removeData( DATASTORE_PRIVATE_KEY );
    }

    @AfterEach
    protected void restoreKeys( )
    {
        DatastoreService.removeData( DATASTORE_PUBLIC_KEY );
        DatastoreService.removeData( DATASTORE_PRIVATE_KEY );
        if ( _strPublic != null )
        {
            DatastoreService.setDataValue( DATASTORE_PUBLIC_KEY, _strPublic );
        }
        if ( _strPrivate != null )
        {
            DatastoreService.setDataValue( DATASTORE_PRIVATE_KEY, _strPrivate );
        }
    }

    /**
     * The keys created on first use are a pair
     * 
     * @throws GeneralSecurityException
     *             should not happen
     */
    @Test
    public void testInitStoresAPair( ) throws GeneralSecurityException
    {
        RSAKeyDatastoreProvider provider = new RSAKeyDatastoreProvider( );

        RSAPrivateKey privateKey = (RSAPrivateKey) provider.getPrivateKey( );
        RSAPublicKey publicKey = (RSAPublicKey) provider.getPublicKey( );

        assertEquals( publicKey.getModulus( ), privateKey.getModulus( ) );
    }

    /**
     * A public key stored by another instance without its private key (instance stopped between the two inserts) is replaced by a pair
     * 
     * @throws GeneralSecurityException
     *             should not happen
     */
    @Test
    public void testOrphanPublicKeyReplaced( ) throws GeneralSecurityException
    {
        RSAKeyDatastoreProvider provider = new RSAKeyDatastoreProvider( );
        provider.getPublicKey( );
        String strOrphanPublic = DatastoreService.getDataValue( DATASTORE_PUBLIC_KEY, null );
        DatastoreService.removeData( DATASTORE_PRIVATE_KEY );

        RSAPrivateKey privateKey = (RSAPrivateKey) provider.getPrivateKey( );
        RSAPublicKey publicKey = (RSAPublicKey) provider.getPublicKey( );

        assertNotEquals( strOrphanPublic, DatastoreService.getDataValue( DATASTORE_PUBLIC_KEY, null ) );
        assertEquals( publicKey.getModulus( ), privateKey.getModulus( ) );
    }
}
