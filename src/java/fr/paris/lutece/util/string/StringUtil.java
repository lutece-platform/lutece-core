/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.util.string;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.Normalizer;


/**
 * This class provides String utils.
 */
public final class StringUtil
{
    private static final String PROPERTY_XSS_CHARACTERS = "input.xss.characters";
    private static final String EMAIL_PATTERN = "^[\\w_.\\-]+@[\\w_.\\-]+\\.[\\w]+$";
    private static final String STRING_CODE_PATTERN = "^[\\w]+$";
    private static final String CONSTANT_AT = "@";

    // The characters that are considered dangerous for XSS attacks
    private static char[] _aXssCharacters;
    private static String _xssCharactersAsString;

    /**
     * Constructor with no parameter
    */
    private StringUtil(  )
    {
    }

    /**
     * This function substitutes all occurences of a given bookmark by a given value
     *
     * @param strSource The input string that contains bookmarks to replace
     * @param strValue    The value to substitute to the bookmark
     * @param strBookmark    The bookmark name
     * @return The output string.
     */
    public static String substitute( String strSource, String strValue, String strBookmark )
    {
        StringBuilder strResult = new StringBuilder(  );
        int nPos = strSource.indexOf( strBookmark );
        String strModifySource = strSource;

        while ( nPos != -1 )
        {
            strResult.append( strModifySource.substring( 0, nPos ) );
            strResult.append( strValue );
            strModifySource = strModifySource.substring( nPos + strBookmark.length(  ) );
            nPos = strModifySource.indexOf( strBookmark );
        }

        strResult.append( strModifySource );

        return strResult.toString(  );
    }

    /**
     * This function converts French diacritics characters into non diacritics.
     *
     * @param strSource The String to convert
     * @return The sTring converted to French non diacritics characters
     */
    public static String replaceAccent( String strSource )
    {
        String strNormalized = Normalizer.normalize( strSource, Normalizer.Form.NFKD );
        strNormalized = strNormalized.replaceAll( "\\p{M}", "" );

        return strNormalized;
    }

    /**
     * Checks if a string literal contains any HTML special characters
     * (&, ", ' <, >).
     * @param strValue The string literal to check
     * @return True if the string literal contains any special character
     */
    public static boolean containsHtmlSpecialCharacters( String strValue )
    {
        return ( ( strValue.indexOf( '"' ) > -1 ) || ( strValue.indexOf( '&' ) > -1 ) ||
        ( strValue.indexOf( '<' ) > -1 ) || ( strValue.indexOf( '>' ) > -1 ) );
    }

    /**
     * Checks if a String contains characters that could be used for a
     * cross-site scripting attack.
     *
     * @param strValue a character String
     * @return true if the String contains illegal characters
     */
    public static synchronized boolean containsXssCharacters( String strValue )
    {
        // Read XSS characters from properties file if not already initialized
        if ( _aXssCharacters == null )
        {
            _aXssCharacters = AppPropertiesService.getProperty( PROPERTY_XSS_CHARACTERS ).toCharArray(  );
        }

        return containsXssCharacters( strValue, _aXssCharacters );
    }

    /**
     * Checks if a String contains characters that could be used for a
     * cross-site scripting attack.
     *
     * @param strValue a character String
     * @param aXssCharacters a Xss characters tab to check in strValue
     * @return true if the String contains illegal characters
     */
    public static synchronized boolean containsXssCharacters( String strValue, char[] aXssCharacters )
    {
        // Read XSS characters from properties file if not already initialized
        boolean bContains = false;

        if ( aXssCharacters != null )
        {
            for ( int nIndex = 0; !bContains && ( nIndex < aXssCharacters.length ); nIndex++ )
            {
                bContains = strValue.lastIndexOf( aXssCharacters[nIndex] ) >= 0;
            }
        }

        return bContains;
    }

    /**
     * Checks if a String contains characters that could be used for a
     * cross-site scripting attack.
     *
     * @param strValue a character String
     * @param strXssCharacters a String wich contain a list of Xss characters to check in strValue
     * @return true if the String contains illegal characters
     */
    public static synchronized boolean containsXssCharacters( String strValue, String strXssCharacters )
    {
        // Read XSS characters from properties file if not already initialized
        if ( strXssCharacters != null )
        {
            return containsXssCharacters( strValue, strXssCharacters.toCharArray(  ) );
        }

        return false;
    }

    /**
     * Simple convenience method to return the XSS characters as a string, to
     * include it in error messages.
     *
     * @return a String containing a comma-separated list of the XSS characters
     */
    public static synchronized String getXssCharactersAsString(  )
    {
        // Read XSS characters from properties file if not already initialized
        if ( _aXssCharacters == null )
        {
            _aXssCharacters = AppPropertiesService.getProperty( PROPERTY_XSS_CHARACTERS ).toCharArray(  );
        }

        if ( _xssCharactersAsString == null )
        {
            StringBuilder sbfCharList = new StringBuilder(  );

            int iIndex;

            for ( iIndex = 0; iIndex < ( _aXssCharacters.length - 1 ); iIndex++ )
            {
                sbfCharList.append( _aXssCharacters[iIndex] );
                sbfCharList.append( ", " );
            }

            // Append last character outside of the loop to avoid trailing comma
            sbfCharList.append( _aXssCharacters[iIndex] );
            _xssCharactersAsString = sbfCharList.toString(  );
        }

        return _xssCharactersAsString;
    }

    /**
     * This function checks if an email is in a valid format Returns true if the
     * email is valid
     *
     * @param strEmail  The mail to check
     * @return boolean true if strEmail is valid
     */
    public static synchronized boolean checkEmail( String strEmail )
    {
        return strEmail.matches( EMAIL_PATTERN );
    }

    /**
     * This function checks if an email is in a valid format, and is not in a banned domain names list. Returns true if the email is valid
     *
     * @param strEmail The mail to check
     * @param strBannedDomainNames The list of banned domain names. Domain names may start with a '@' or not.
     * @return boolean true if strEmail is valid, false otherwise
     */
    public static synchronized boolean checkEmailAndDomainName( String strEmail, String[] strBannedDomainNames )
    {
        boolean bIsValid = strEmail.matches( EMAIL_PATTERN );

        return bIsValid && checkEmailDomainName( strEmail, strBannedDomainNames );
    }

    /**
     * Check if a domain name of an email address is not in a banned domain names list.
     * @param strEmail Email addresse to check
     * @param strBannedDomainNames List of banned domain names
     * @return True if the email address is correct, false otherwise
     */
    public static synchronized boolean checkEmailDomainName( String strEmail, String[] strBannedDomainNames )
    {
        if ( ( strBannedDomainNames != null ) && ( strBannedDomainNames.length > 0 ) )
        {
            int nOffset;

            if ( strBannedDomainNames[0].contains( CONSTANT_AT ) )
            {
                nOffset = 0;
            }
            else
            {
                nOffset = 1;
            }

            int nIndex = strEmail.indexOf( CONSTANT_AT );

            if ( ( nIndex >= 0 ) && ( ( nIndex + nOffset ) < strEmail.length(  ) ) )
            {
                String strDomainName = strEmail.substring( nIndex + nOffset );

                for ( String strDomain : strBannedDomainNames )
                {
                    if ( strDomainName.equals( strDomain ) )
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Check a code key.<br />
     * Return true if each character of String is
     * <ul>
     * <li>number</li>
     * <li>lower case</li>
     * <li>upper case</li>
     * </ul>
     *
     * @param strCodeKey The code Key
     * @return True if code key is valid
     */
    public static synchronized boolean checkCodeKey( String strCodeKey )
    {
        return ( strCodeKey == null ) ? false : strCodeKey.matches( STRING_CODE_PATTERN );
    }

    /**
     * Converts <code>strValue</code> to an int value.
     * @param strValue the value to convert
     * @param nDefaultValue the default returned value
     * @return <code>strValue</code> int value, <code>nDefaultValue</code> if strValue is not an Integer.
     */
    public static int getIntValue( String strValue, int nDefaultValue )
    {
        try
        {
            return Integer.parseInt( strValue );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe.getMessage(  ), nfe );
        }

        return nDefaultValue;
    }
}
