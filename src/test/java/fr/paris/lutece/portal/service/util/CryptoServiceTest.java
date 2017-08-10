/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;

public class CryptoServiceTest extends LuteceTestCase
{
    private boolean bCryptoKeyExists;
    private String strOrigCrytoKey;

    @Override
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        bCryptoKeyExists = DatastoreService.existsKey( CryptoService.DSKEY_CRYPTO_KEY );
        strOrigCrytoKey = DatastoreService.getDataValue( CryptoService.DSKEY_CRYPTO_KEY, null );
        DatastoreService.removeData( CryptoService.DSKEY_CRYPTO_KEY );
    }

    public void testGetCryptoKey( )
    {
        String strCryptoKey = CryptoService.getCryptoKey( );
        assertNotNull( strCryptoKey );
        assertFalse( strCryptoKey.equals( "A23A5C78H" ) ); // legacy key
        assertTrue( strCryptoKey.length( ) >= 64 );
        assertEquals( strCryptoKey, CryptoService.getCryptoKey( ) );
    }

    public void testGetCryptoKeyLegacy( ) throws FileNotFoundException, IOException
    {
        final String strLegacyKey = "LEGACY";
        setLegacyKey( strLegacyKey );
        try
        {
            assertEquals( strLegacyKey, CryptoService.getCryptoKey( ) );
        }
        finally
        {
            removeLegacyKey( );
        }
    }

    private void removeLegacyKey( ) throws IOException, FileNotFoundException
    {
        File luteceProperties = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
        Properties props = new Properties( );
        try( InputStream is = new FileInputStream( luteceProperties ) )
        {
            props.load( is );
        }
        props.remove( CryptoService.PROPERTY_CRYPTO_KEY );
        try( OutputStream os = new FileOutputStream( luteceProperties ) )
        {
            props.store( os, "saved for junit " + this.getClass( ).getCanonicalName( ) );
        }
        AppPropertiesService.reloadAll( );
    }

    private void setLegacyKey( final String strLegacyKey ) throws IOException, FileNotFoundException
    {
        File luteceProperties = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
        Properties props = new Properties( );
        try( InputStream is = new FileInputStream( luteceProperties ) )
        {
            props.load( is );
        }
        props.setProperty( CryptoService.PROPERTY_CRYPTO_KEY, strLegacyKey );
        try( OutputStream os = new FileOutputStream( luteceProperties ) )
        {
            props.store( os, "saved for junit " + this.getClass( ).getCanonicalName( ) );
        }
        AppPropertiesService.reloadAll( );
    }

    public void testHmacSHA256( )
    {
        System.out.println( CryptoService.hmacSHA256( "message" ) );
    }

    @Override
    protected void tearDown( ) throws Exception
    {
        if ( bCryptoKeyExists )
        {
            DatastoreService.setDataValue( CryptoService.DSKEY_CRYPTO_KEY, strOrigCrytoKey );
        }
        else
        {
            DatastoreService.removeData( CryptoService.DSKEY_CRYPTO_KEY );
        }
        super.tearDown( );
    }

}
