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
package fr.paris.lutece.util.filesystem;

import fr.paris.lutece.util.string.StringUtil;


/**
 * This class provides utilities for uploaded files
 */
public final class UploadUtil
{
    /**
     * Creates a new UploadUtil object.
     */
    private UploadUtil(  )
    {
    }

    /**
     * Removes special characters contained in the file name
     *
     * @param strName the file name
     * @return strClearName the cleaned file name
     */
    public static String cleanFileName( String strName )
    {
        return cleanString( strName, false );
    }

    /**
     * Removes special characters contained in a file path. This method performs
     * the same transformation as cleanFileName but leaves out the slashes.
     *
     * @param strPath The path to clean
     * @return The cleaned path
     */
    public static String cleanFilePath( String strPath )
    {
        return cleanString( strPath, true );
    }

    /**
     * Helper method for cleanFileName and cleanFilePath.
     *
     * @param strValue The string literal to clean
     * @param bKeepSlashes Set to true to keep slashes, false to replace them
     * @return The cleaned string literal
     */
    private static String cleanString( String strValue, boolean bKeepSlashes )
    {
        // First, replace the accents :
        String strNoAccents = StringUtil.replaceAccent( strValue );

        // Convert to an array of characters
        char[] characters = new char[strNoAccents.length(  )];
        strNoAccents.getChars( 0, strNoAccents.length(  ), characters, 0 );

        // Iterate to process one character at a time
        for ( int i = 0; i < strNoAccents.length(  ); i++ )
        {
            char current = characters[i];

            // Valid characters are : '.', alphanumeric (a-zA-Z0-9),
            // and '/' if bKeepSlashes is true.
            boolean bValid = ( ( current == '.' ) || ( ( current >= '0' ) && ( current <= '9' ) ) ||
                ( ( current >= 'a' ) && ( current <= 'z' ) ) || ( ( current >= 'A' ) && ( current <= 'Z' ) ) ||
                ( bKeepSlashes && ( current == '/' ) ) );

            // Other characters are replaced by underscore.
            if ( !bValid )
            {
                characters[i] = '_';
            }
        }

        // Return result as a string
        return String.valueOf( characters );
    }
}
