/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.Collator;


/**
 * This class provides String utils.
 */
public final class StringUtil
{
    private static final String PROPERTY_XSS_CHARACTERS = "input.xss.characters";
    private static final String EMAIL_PATTERN = "^[\\w_.\\-]+@[\\w_.\\-]+\\.[\\w]+$";
    private static final String STRING_CODE_PATTERN = "^[\\w]+$";

    // The characters that are considered dangerous for XSS attacks
    private static char[] _aXssCharacters = null;
    private static String _xssCharactersAsString = null;

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
        StringBuffer strResult = new StringBuffer(  );
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
     * @param strInit The String to convert
     * @return The sTring converted to French non diacritics characters
     */
    public static String replaceAccent( String strInit )
    {
        char charTest;
        char[] tabTemp = strInit.toCharArray(  );
        char[] voyelle = { 'a', 'c', 'e', 'i', 'o', 'u', 'y' };

        // Collator can compare 2 strings using locale-sensitive String comparison
        Collator collator = Collator.getInstance( java.util.Locale.FRENCH );
        collator.setStrength( Collator.PRIMARY );

        for ( int i = 0; i < strInit.length(  ); i++ )
        {
            charTest = strInit.charAt( i );

            // Test character only when it's in the range [192-255]
            if ( ( charTest > 191 ) && ( charTest < 256 ) )
            {
                for ( int j = 0; j < voyelle.length; j++ )
                {
                    if ( collator.compare( String.valueOf( charTest ), String.valueOf( voyelle[j] ) ) == 0 )
                    {
                        tabTemp[i] = voyelle[j];

                        break;
                    }
                }
            }
        }

        return String.valueOf( tabTemp );
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

        boolean bContains = false;

        for ( int nIndex = 0; !bContains && ( nIndex < _aXssCharacters.length ); nIndex++ )
        {
            bContains = strValue.lastIndexOf( _aXssCharacters[nIndex] ) >= 0;
        }

        return bContains;
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
            StringBuffer sbfCharList = new StringBuffer(  );

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
     * Check a code key.<br />
     * Return true if each character of String is
     * <ul>
     *         <li>number</li>
     *  <li>lower case</li>
     *  <li>upper case</li>
     * </ul>
     *
     * @param strCodeKey The code Key
     * @return True if code key is valid
     */
    public static synchronized boolean checkCodeKey( String strCodeKey )
    {
        return ( strCodeKey == null ) ? null : strCodeKey.matches( STRING_CODE_PATTERN );
    }
}
