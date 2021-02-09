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
