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
package fr.paris.lutece.util.env;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EnvUtil
 */
public final class EnvUtil
{
    public static final String PREFIX_ENV = "env.";
    private static final String SUFFIX_FILE = "_FILE";
    private static final String PATTERN_MARKER = "\\$\\{(.+?)\\}";

    private static Pattern _pattern = Pattern.compile( PATTERN_MARKER );

    private static Map<String, String> _mapEnv = System.getenv( );

    // Private constructor
    private EnvUtil( )
    {
    }

    /**
     * Evaluate a string that may contain a reference to an environement variable enclosed by ${...}
     * 
     * @param strSource
     *            The source string
     * @return The source string or the env variable's value if found.
     */
    public static String evaluate( String strSource )
    {
        return evaluate( strSource, "" );
    }

    /**
     * Evaluate a string that may contain a reference to an environement variable enclosed by ${...}. If the environement variable has a suffix _FILE the
     * evaluation will take the content of the file as value.
     * 
     * @param strSource
     *            The source string
     * @param strEnvPrefix
     *            A prefix of the environment
     * @return The source string or the env variable's value if found.
     */
    public static String evaluate( String strSource, String strEnvPrefix )
    {
        String strOutput = ( strSource != null ) ? strSource : "";
        Matcher matcher = _pattern.matcher( strOutput );
        int offset = 0;

        while ( matcher.find( ) )
        {
            String strMarker = matcher.group( );
            String strEnvVariable = strMarker.substring( 2 + strEnvPrefix.length( ), strMarker.length( ) - 1 );
            String strValue = _mapEnv.get( strEnvVariable );
            if ( strEnvVariable.endsWith( SUFFIX_FILE ) )
            {
                strValue = getFileContent( strValue );
            }
            strValue = strValue == null ? "" : strValue;
            strOutput = strOutput.substring( 0, matcher.start( ) + offset ) + strValue + strOutput.substring( matcher.end( ) + offset );
            offset += strValue.length( ) - strMarker.length( );
        }
        return strOutput;
    }

    /**
     * Mock Env map for unit testing
     * 
     * @param mapEnv
     *            a mock map
     */
    static void setMockMapEnv( Map<String, String> mapEnv )
    {
        _mapEnv = mapEnv;
    }

    /**
     * Gets the content of a file as a String
     * 
     * @param strFilePath
     *            The file path
     * @return The file content or the error message while reading the message
     */
    private static String getFileContent( String strFilePath )
    {
        String strData;
        try
        {
            strData = new String( Files.readAllBytes( Paths.get( strFilePath ) ) );
        }
        catch( IOException e )
        {
            return e.getMessage( );
        }
        return strData;
    }

}
