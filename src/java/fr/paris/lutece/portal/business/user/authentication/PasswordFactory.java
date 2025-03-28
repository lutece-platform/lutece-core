/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.CryptoService;
import fr.paris.lutece.util.password.IPassword;
import fr.paris.lutece.util.password.IPasswordFactory;

/**
 * A factory for getting storable password representation
 */
final class PasswordFactory implements IPasswordFactory
{
    // storage types
    private static final String ERROR_PASSWORD_STORAGE = "Invalid stored password ";
    private static final String PBKDF2WITHHMACSHA1_STORAGE_TYPE = "PBKDF2";
    private static final String PBKDF2WITHHMACSHA512_STORAGE_TYPE = "PBKDF2WITHHMACSHA512";
    private static final String PLAINTEXT_STORAGE_TYPE = "PLAINTEXT";
    private static final String DUMMY_STORAGE_TYPE = "\0DUMMY\0";
    private static final String DUMMY_STORED_PASSWORD = DUMMY_STORAGE_TYPE + ":\0";

    @Override
    public IPassword getPassword( String strStoredPassword )
    {
        int storageTypeSeparatorIndex = strStoredPassword.indexOf( ':' );
        if ( storageTypeSeparatorIndex == -1 )
        {
            throw new IllegalArgumentException( strStoredPassword );
        }
        String storageType = strStoredPassword.substring( 0, storageTypeSeparatorIndex );
        String password = strStoredPassword.substring( storageTypeSeparatorIndex + 1 );
        switch( storageType )
        {
            case PLAINTEXT_STORAGE_TYPE:
                return new PlaintextPassword( password );
            case PBKDF2WITHHMACSHA1_STORAGE_TYPE:
                return new PBKDF2WithHmacSHA1Password( password );
            case PBKDF2WITHHMACSHA512_STORAGE_TYPE:
                return new PBKDF2WithHmacSHA512Password( password );
            case DUMMY_STORAGE_TYPE:
                return new DummyPassword( );
            default:
                return new DigestPassword( storageType, password );
        }
    }

    @Override
    public IPassword getPasswordFromCleartext( String strUserPassword )
    {
        return new PBKDF2WithHmacSHA512Password( strUserPassword, PBKDF2Password.PASSWORD_REPRESENTATION.CLEARTEXT );
    }

    @Override
    public IPassword getDummyPassword( )
    {
        return getPassword( DUMMY_STORED_PASSWORD );
    }

    /**
     * A Password stored using PBKDF2
     */
    private abstract static class PBKDF2Password implements IPassword
    {

        /**
         * Enum to specify if the password is constructed from cleartext or hashed form
         */
        enum PASSWORD_REPRESENTATION
        {
            CLEARTEXT,
            STORABLE
        }

        /** Storage format : iterations:hex(salt):hex(hash) */
        private static final Pattern FORMAT = Pattern.compile( "^(\\d+):([a-z0-9]+):([a-z0-9]+)$", Pattern.CASE_INSENSITIVE );
        private static final Random RANDOM;

        // init the random number generator
        static
        {
            Random rand;
            try
            {
                rand = SecureRandom.getInstance( "SHA1PRNG" );
            }
            catch( NoSuchAlgorithmException e )
            {
                AppLogService.error( "SHA1PRNG is not availabled. Picking the default SecureRandom.", e );
                rand = new SecureRandom( );
            }
            RANDOM = rand;
        }

        static final String PROPERTY_PASSWORD_HASH_ITERATIONS = "password.hash.iterations";
        static final int DEFAULT_HASH_ITERATIONS = 210000;
        private static final String PROPERTY_PASSWORD_HASH_LENGTH = "password.hash.length";

        /** number of iterations */
        final int _iterations;
        /** salt */
        private final byte [ ] _salt;
        /** hashed password */
        private final byte [ ] _hash;

        /**
         * Construct a password from the stored representation
         * 
         * @param strStoredPassword
         *            the stored representation
         */
        public PBKDF2Password( String strStoredPassword )
        {
            this( strStoredPassword, PASSWORD_REPRESENTATION.STORABLE );
        }

        /**
         * Construct a password
         * 
         * @param strPassword
         *            the password text
         * @param representation
         *            representation of strPassword
         */
        public PBKDF2Password( String strPassword, PASSWORD_REPRESENTATION representation )
        {
            switch( representation )
            {
                case CLEARTEXT:
                    _iterations = AppPropertiesService.getPropertyInt( PROPERTY_PASSWORD_HASH_ITERATIONS, DEFAULT_HASH_ITERATIONS );
                    int hashLength = AppPropertiesService.getPropertyInt( PROPERTY_PASSWORD_HASH_LENGTH, 128 );
                    try
                    {
                        _salt = new byte [ 16];
                        RANDOM.nextBytes( _salt );
                        PBEKeySpec spec = new PBEKeySpec( strPassword.toCharArray( ), _salt, _iterations, hashLength * 8 );
                        SecretKeyFactory skf = SecretKeyFactory.getInstance( getAlgorithm( ) );
                        _hash = skf.generateSecret( spec ).getEncoded( );
                    }
                    catch( NoSuchAlgorithmException | InvalidKeySpecException e )
                    {
                        throw new AppException( "Invalid Algo or key", e ); // should not happen
                    }
                    break;
                case STORABLE:
                    Matcher matcher = FORMAT.matcher( strPassword );

                    if ( !matcher.matches( ) || matcher.groupCount( ) != 3 )
                    {
                        throw new IllegalArgumentException( ERROR_PASSWORD_STORAGE + strPassword );
                    }
                    _iterations = Integer.valueOf( matcher.group( 1 ) );
                    try
                    {
                        _salt = Hex.decodeHex( matcher.group( 2 ).toCharArray( ) );
                        _hash = Hex.decodeHex( matcher.group( 3 ).toCharArray( ) );
                    }
                    catch( DecoderException e )
                    {
                        throw new IllegalArgumentException( ERROR_PASSWORD_STORAGE + strPassword );
                    }
                    break;
                default:
                    throw new IllegalArgumentException( representation.toString( ) );
            }
        }

        /**
         * Get the PBKDF2 algorithm
         * 
         * @return the PBKDF2 algorithm to use
         */
        protected abstract String getAlgorithm( );

        @Override
        public boolean check( String strCleartextPassword )
        {
            PBEKeySpec spec = new PBEKeySpec( strCleartextPassword.toCharArray( ), _salt, _iterations, _hash.length * 8 );
            try
            {
                SecretKeyFactory skf = SecretKeyFactory.getInstance( getAlgorithm( ) );
                byte [ ] testHash = skf.generateSecret( spec ).getEncoded( );
                return Arrays.equals( _hash, testHash );
            }
            catch( NoSuchAlgorithmException | InvalidKeySpecException e )
            {
                throw new AppException( "Invalid Algo or key", e ); // should not happen
            }
        }

        /**
         * Get the storage type identifier
         * 
         * @return the storage type identifier
         */
        protected abstract String getStorageType( );

        @Override
        public String getStorableRepresentation( )
        {
            StringBuilder sb = new StringBuilder( );
            sb.append( getStorageType( ) ).append( ':' );
            sb.append( _iterations ).append( ':' ).append( Hex.encodeHex( _salt ) );
            sb.append( ':' ).append( Hex.encodeHex( _hash ) );
            return sb.toString( );
        }

    }

    /**
     * A Password stored using PBKDF2WithHmacSHA1
     */
    private static final class PBKDF2WithHmacSHA1Password extends PBKDF2Password
    {

        /**
         * Construct a password from the stored representation
         * 
         * @param strStoredPassword
         *            the stored representation
         */
        public PBKDF2WithHmacSHA1Password( String strStoredPassword )
        {
            super( strStoredPassword );
        }

        @Override
        public boolean isLegacy( )
        {
            return true;
        }

        @Override
        protected String getAlgorithm( )
        {
            return "PBKDF2WithHmacSHA1";
        }

        @Override
        protected String getStorageType( )
        {
            return PBKDF2WITHHMACSHA1_STORAGE_TYPE;
        }

        /**
         * Legacy passwords must not be stored.
         * 
         * @return never returns
         * @throws UnsupportedOperationException
         *             Always thrown because Legacy passwords must not be stored.
         */
        @Override
        public String getStorableRepresentation( )
        {
            throw new UnsupportedOperationException( "Must not store a legacy password" );
        }

    }

    /**
     * A Password stored using PBKDF2WithHmacSHA512
     */
    private static class PBKDF2WithHmacSHA512Password extends PBKDF2Password
    {

        /**
         * Construct a password from the stored representation
         * 
         * @param strStoredPassword
         *            the stored representation
         */
        public PBKDF2WithHmacSHA512Password( String strStoredPassword )
        {
            super( strStoredPassword );
        }

        /**
         * Construct a password
         * 
         * @param strStoredPassword
         *            the password text
         * @param representation
         *            representation of strPassword
         */
        public PBKDF2WithHmacSHA512Password( String strStoredPassword, PASSWORD_REPRESENTATION representation )
        {
            super( strStoredPassword, representation );
        }

        @Override
        public boolean isLegacy( )
        {
            int iterations = AppPropertiesService.getPropertyInt( PROPERTY_PASSWORD_HASH_ITERATIONS, DEFAULT_HASH_ITERATIONS );
            return _iterations < iterations;
        }

        @Override
        protected String getAlgorithm( )
        {
            return "PBKDF2WithHmacSHA512";
        }

        @Override
        protected String getStorageType( )
        {
            return PBKDF2WITHHMACSHA512_STORAGE_TYPE;
        }

    }

    /**
     * Dummy password which never matches a user password, but takes the same time as the PBKDF2Password to do so.
     */
    private static final class DummyPassword extends PBKDF2WithHmacSHA512Password
    {
        DummyPassword( )
        {
            // take the same time to construct as a proper PBKDF2Password
            super( "", PASSWORD_REPRESENTATION.CLEARTEXT );
        }

        @Override
        public boolean check( String strCleartextPassword )
        {
            // take the same time to check as a proper PBKDF2Password
            super.check( strCleartextPassword );
            return false;
        }

        @Override
        public String getStorableRepresentation( )
        {
            throw new UnsupportedOperationException( "Must not store a dummy password" );
        }
    }

    /**
     * Legacy password implementation super class
     */
    private abstract static class LegacyPassword implements IPassword
    {
        /**
         * Legacy passwords are legacy
         * 
         * @return <code>true</code>
         */
        @Override
        public final boolean isLegacy( )
        {
            return true;
        }

        /**
         * Legacy passwords must not be stored.
         * 
         * @return never returns
         * @throws UnsupportedOperationException
         *             Always thrown because Legacy passwords must not be stored.
         */
        @Override
        public final String getStorableRepresentation( )
        {
            throw new UnsupportedOperationException( "Passwords should not be stored without proper hashing and salting" );
        }

    }

    /**
     * Password stored as plaintext
     */
    private static final class PlaintextPassword extends LegacyPassword
    {

        /** the stored password */
        private final String _strPassword;

        /**
         * Constructor
         * 
         * @param strStoredPassword
         *            the stored password
         */
        public PlaintextPassword( String strStoredPassword )
        {
            _strPassword = strStoredPassword;
        }

        @Override
        public boolean check( String strCleartextPassword )
        {
            return _strPassword != null && _strPassword.equals( strCleartextPassword );
        }

    }

    /**
     * Password stored as {@link MessageDigest} output
     */
    private static final class DigestPassword extends LegacyPassword
    {
        /** the stored password */
        private final String _strPassword;
        /** the digest algorithm */
        private final String _strAlgorithm;

        /**
         * Constructor
         * 
         * @param strAlgorithm
         *            the digest algorithm
         * @param strStoredPassword
         *            the stored password
         */
        public DigestPassword( String strAlgorithm, String strStoredPassword )
        {
            _strPassword = strStoredPassword;
            // check for algorithm support
            try
            {
                MessageDigest.getInstance( strAlgorithm );
            }
            catch( NoSuchAlgorithmException e )
            {
                throw new IllegalArgumentException( e );
            }
            _strAlgorithm = strAlgorithm;
        }

        @Override
        public boolean check( String strCleartextPassword )
        {
            return _strPassword != null && _strPassword.equals( CryptoService.encrypt( strCleartextPassword, _strAlgorithm ) );
        }
    }
}
