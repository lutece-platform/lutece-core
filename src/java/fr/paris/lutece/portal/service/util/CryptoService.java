package fr.paris.lutece.portal.service.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.paris.lutece.portal.business.user.parameter.DefaultUserParameterHome;

public class CryptoService 
{
	// Properties
    private static final String PROPERTY_ENCODING = "lutece.encoding"; 
	
	/**
     * Encrypt a data using an algorithm defined in lutece.properties 
     * @param strDataToEncrypt The data to encrypt
     * @return The encrypted string
     */
    public static String encrypt( String strDataToEncrypt, String strAlgorithm )
    {
        String hash = null;
        MessageDigest md = null;

        try
        {
            md = MessageDigest.getInstance( strAlgorithm );
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace(  );
        }

        try
        {
            hash = byteToHex( md.digest( strDataToEncrypt.getBytes( 
            		AppPropertiesService.getProperty( PROPERTY_ENCODING ) ) ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace(  );
        }

        return hash;
    }

    /**
     * Convert byte to hex
     * @param bits the byte to convert
     * @return the hex
     */
    private static String byteToHex( byte[] bits )
    {
        if ( bits == null )
        {
            return null;
        }

        // encod(1_bit) => 2 digits
        StringBuffer hex = new StringBuffer( bits.length * 2 ); 

        for ( int i = 0; i < bits.length; i++ )
        {
            if ( ( (int) bits[i] & 0xff ) < 0x10 )
            {
            	// 0 < .. < 9
                hex.append( "0" );
            }
            // [(bit+256)%256]^16
            hex.append( Integer.toString( (int) bits[i] & 0xff, 16 ) ); 
        }

        return hex.toString(  );
    }

}
