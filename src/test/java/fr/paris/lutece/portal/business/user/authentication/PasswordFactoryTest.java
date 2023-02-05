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
package fr.paris.lutece.portal.business.user.authentication;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.password.IPassword;

public class PasswordFactoryTest extends LuteceTestCase
{
    public void testGetPasswordIncorrectFormat( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        try
        {
            passwordFactory.getPassword( "FOO" );
            fail( );
        }
        catch( IllegalArgumentException e )
        {
        }
    }

    public void testGetPasswordUnknownFormat( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        try
        {
            passwordFactory.getPassword( "FOO:BAR" );
            fail( );
        }
        catch( IllegalArgumentException e )
        {
        }
    }

    public void testGetPasswordPlaintext( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        IPassword password = passwordFactory.getPassword( "PLAINTEXT:PASSWORD" );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertTrue( password.isLegacy( ) );
        try
        {
            password.getStorableRepresentation( );
            fail( );
        }
        catch( UnsupportedOperationException e )
        {
        }
    }

    public void testGetPasswordMD5( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        IPassword password = passwordFactory.getPassword( "MD5:319f4d26e3c536b5dd871bb2c52e3178" );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertTrue( password.isLegacy( ) );
        try
        {
            password.getStorableRepresentation( );
            fail( );
        }
        catch( UnsupportedOperationException e )
        {
        }
    }

    public void testGetPasswordSHA1( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        IPassword password = passwordFactory.getPassword( "SHA-1:112bb791304791ddcf692e29fd5cf149b35fea37" );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertTrue( password.isLegacy( ) );
        try
        {
            password.getStorableRepresentation( );
            fail( );
        }
        catch( UnsupportedOperationException e )
        {
        }
    }

    public void testGetPasswordSHA256( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        IPassword password = passwordFactory.getPassword( "SHA-256:0be64ae89ddd24e225434de95d501711339baeee18f009ba9b4369af27d30d60" );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertTrue( password.isLegacy( ) );
        try
        {
            password.getStorableRepresentation( );
            fail( );
        }
        catch( UnsupportedOperationException e )
        {
        }
    }

    public void testGetPasswordPBKDF2WithHmacSHA1( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        String storedPassword = "PBKDF2:40000:c2d05d21e68313aaf55cf16751c53dd9:da09ad1888f548ddf5f2cb0a0b9904aaf547e4b6722d4e04ac75dab73b87d379"
                + "be5b312a50b15c2dcdd9b745b616492c85a8e8e4a8b75e8abf1b99507680e30befb6bfdc9b3e0493dcccc43be6dcc24be3015bf966a66797047d75b938784921"
                + "710b0de6e3643cc8088ec7315e1e03c91250b5c4a65de8adb0a7351a1564bbb7";
        IPassword password = passwordFactory.getPassword( storedPassword );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertTrue( password.isLegacy( ) );
        try
        {
            password.getStorableRepresentation( );
            fail( );
        }
        catch( UnsupportedOperationException e )
        {
        }
    }

    public void testGetPasswordPBKDF2WithHmacSHA512UpgradeIterations( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        String storedPassword = "PBKDF2WITHHMACSHA512:30000:ac08c57a261dc5db09de2b689b0c55bf:96340dad8137a023c7888245c4221acf18bfaf9f69dfff4c34b"
                + "0da4f8cc5b2a8959b0552312dd3dccff002ad765fc7bef6429c4dd3760ad68b53a0323d1464d41d74271b1f0fccd80e94b99b5e4323ffc67109d5917cccf5"
                + "e74641cd7059e88671bd58ee40223fb2051968dea450cc9806546f98798c5b6ed3b3f5d44b51e03f";
        IPassword password = passwordFactory.getPassword( storedPassword );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertTrue( "Password stored with less iterations than the default should be marked as legacy so that it is upgraded", password.isLegacy( ) );
    }

    public void testGetPasswordPBKDF2WithHmacSHA512( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        String storedPassword = "PBKDF2WITHHMACSHA512:210000:f89603fe9d91a8e622a86a927ecf13db:95c00213d61d4b6b8d5200b578a9a5bdb8d70fbb249e6f956d7"
                + "6c84af02c9c37260dee41e2ec8d7fb4c51b85f36025d729ce453fe169f9a688ebaf2efeb61a7934c419e76576d885411fd87a71408517952e56a33ad03f5a1"
                + "b9cf33311cd6b4767b164fda39c4cb2f942f9d360cfcf1498dd850536dc8447e94cb815b888ae2a";
        IPassword password = passwordFactory.getPassword( storedPassword );
        assertEquals( true, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertFalse( password.isLegacy( ) );
        assertEquals( storedPassword, password.getStorableRepresentation( ) );
    }

    public void testGetPasswordFromCleartext( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        IPassword password = passwordFactory.getPasswordFromCleartext( "PASSWORD" );
        assertFalse( password.isLegacy( ) );
        assertFalse( password.getStorableRepresentation( ).equals( passwordFactory.getPasswordFromCleartext( "PASSWORD" ).getStorableRepresentation( ) ) );
    }

    public void testGetDummyPassword( )
    {
        PasswordFactory passwordFactory = new PasswordFactory( );
        IPassword password = passwordFactory.getDummyPassword( );
        assertEquals( false, password.check( "PASSWORD" ) );
        assertEquals( false, password.check( "BAR" ) );
        assertEquals( false, password.check( "\0" ) );
        assertEquals( false, password.check( "" ) );
        assertFalse( password.isLegacy( ) );
        try
        {
            password.getStorableRepresentation( );
            fail( );
        }
        catch( UnsupportedOperationException e )
        {
        }
    }
}
