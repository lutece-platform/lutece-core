package fr.paris.lutece.portal.service.security;

import static org.junit.Assert.assertNotEquals;

import java.security.GeneralSecurityException;

import fr.paris.lutece.test.LuteceTestCase;

public class RsaServiceTest extends LuteceTestCase
{

    public void testEncryptDecrypt( ) throws GeneralSecurityException
    {
        String message = "This is the text to encrypt";
        
        String encrypted = RsaService.encryptRsa( message );
        assertNotEquals( message, encrypted );
        
        String decrypted = RsaService.decryptRsa( encrypted );
        assertEquals( message, decrypted );
    }
}
