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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * EnvUtilTest
 */
public class EnvUtilTest
{
    private static final String ENV_LUTECE_DB_HOST_VAR = "LUTECE_HOST";
    private static final String ENV_LUTECE_DB_HOST_VALUE = "mysql";
    private static final String ENV_LUTECE_DB_USER_VAR = "LUTECE_DB_USER";
    private static final String ENV_LUTECE_DB_USER_VALUE = "lutece_user";
    private static final String ENV_LUTECE_DB_NAME_VAR = "LUTECE_DATABASE";
    private static final String ENV_LUTECE_DB_NAME_VALUE = "lutece";
    private static final String ENV_LUTECE_DB_PWD_FILE_VAR = "LUTECE_DB_PWD_FILE";
    private static final String ENV_LUTECE_DB_PWD_FILE_VALUE = "./fr/paris/lutece/util/env/password.txt";
    private static final String PASSWORD = "${LUTECE_DB_PWD_FILE}";
    private static final String PASSWORD_EXPECTED = "change me";
    private static final String URL = "jdbc:mysql://${LUTECE_HOST}/${LUTECE_DATABASE}?autoReconnect=true&useUnicode=yes&characterEncoding=utf8";
    private static final String URL_EXPECTED = "jdbc:mysql://mysql/lutece?autoReconnect=true&useUnicode=yes&characterEncoding=utf8";

    /**
     * Test of evaluate method, of class EnvUtil.
     * 
     * @throws java.net.URISyntaxException
     */
    @Test
    public void testEvaluate( ) throws URISyntaxException
    {
        System.out.println( "testEvaluate" );

        Map<String, String> mapEnv = new HashMap<>( );
        mapEnv.put( ENV_LUTECE_DB_USER_VAR, ENV_LUTECE_DB_USER_VALUE );
        mapEnv.put( ENV_LUTECE_DB_NAME_VAR, ENV_LUTECE_DB_NAME_VALUE );
        mapEnv.put( ENV_LUTECE_DB_HOST_VAR, ENV_LUTECE_DB_HOST_VALUE );
        URL url = getClass( ).getClassLoader( ).getResource( ENV_LUTECE_DB_PWD_FILE_VALUE );
        File file = Paths.get( url.toURI( ) ).toFile( );
        mapEnv.put( ENV_LUTECE_DB_PWD_FILE_VAR, file.getAbsolutePath( ) );

        // Try a real env variable
        String strSource = "${JAVA_HOME}";
        String result = EnvUtil.evaluate( strSource );
        System.out.println( strSource + ":" + result );

        EnvUtil.setMockMapEnv( mapEnv );
        strSource = "${" + ENV_LUTECE_DB_USER_VAR + "}";
        result = EnvUtil.evaluate( strSource );
        System.out.println( strSource + ":" + result );
        assertEquals( ENV_LUTECE_DB_USER_VALUE, result );
        strSource = "${" + EnvUtil.PREFIX_ENV + ENV_LUTECE_DB_USER_VAR + "}";
        result = EnvUtil.evaluate( strSource, EnvUtil.PREFIX_ENV );
        System.out.println( strSource + ":" + result );
        assertEquals( ENV_LUTECE_DB_USER_VALUE, result );
        result = EnvUtil.evaluate( ENV_LUTECE_DB_USER_VAR );
        assertEquals( ENV_LUTECE_DB_USER_VAR, result );
        result = EnvUtil.evaluate( URL );
        assertEquals( URL_EXPECTED, result );
        result = EnvUtil.evaluate( PASSWORD );
        System.out.println( result );
        assertEquals( PASSWORD_EXPECTED, result );

        System.out.println( EnvUtil.evaluate( null ) );
        System.out.println( EnvUtil.evaluate( "${DUMMY}" ) );

    }

}
