/*
 * Copyright (c) 2002-2016, Mairie de Paris
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
package fr.paris.lutece.util.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * JsonUtil Test Class
 *
 */
public class JsonUtilTest extends LuteceTestCase
{
    /**
     * Test of code and message error responses
     */
    public void testErrorCodeMessageResponse( )
    {
        System.out.println( "errorMessageResponse" );
        AbstractJsonResponse response = new ErrorJsonResponse( "codeValue", "messageValue" );
        String strJson = JsonUtil.buildJsonResponse( response );
        System.out.println( strJson );

        String strRefJson = "{\"errorCode\":\"codeValue\",\"message\":\"messageValue\",\"status\":\"ERROR\"}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef;
        JsonNode objectNodeJson;
        try
        {
            objectNodeRef = objectMapper.readTree( strRefJson );
            objectNodeJson = objectMapper.readTree( strJson );
        }
        catch( Exception e )
        {
            throw new AssertionError( "Should not happen" );
        }

        assertEquals( objectNodeRef, objectNodeJson );
    }

    /**
     * Test of code only error responses
     */
    public void testErrorCodeResponse( )
    {
        System.out.println( "errorResponse" );
        AbstractJsonResponse response = new ErrorJsonResponse( "codeValue" );
        String strJson = JsonUtil.buildJsonResponse( response );
        System.out.println( strJson );

        String strRefJson = "{\"errorCode\":\"codeValue\",\"message\":null,\"status\":\"ERROR\"}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef;
        JsonNode objectNodeJson;
        try
        {
            objectNodeRef = objectMapper.readTree( strRefJson );
            objectNodeJson = objectMapper.readTree( strJson );
        }
        catch( Exception e )
        {
            throw new AssertionError( "Should not happen" );
        }

        assertEquals( objectNodeRef, objectNodeJson );
    }

    /**
     * Test of replaceAccent method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testResponseTRUE( )
    {
        System.out.println( "response" );

        AbstractJsonResponse response = new JsonResponse( Boolean.TRUE );
        String strJson = JsonUtil.buildJsonResponse( response );
        System.out.println( strJson );

        String strRefJson = "{\"result\":true,\"status\":\"OK\"}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef;
        JsonNode objectNodeJson;
        try
        {
            objectNodeRef = objectMapper.readTree( strRefJson );
            objectNodeJson = objectMapper.readTree( strJson );
        }
        catch( Exception e )
        {
            throw new AssertionError( "Should not happen" );
        }

        assertEquals( objectNodeRef, objectNodeJson );

    }

    /**
     * Test of replaceAccent method, of class fr.paris.lutece.util.string.StringUtil.
     */
    public void testResponseBEAN( )
    {
        System.out.println( "response" );

        AbstractJsonResponse response = new JsonResponse( new Object( )
        {
            @SuppressWarnings( "unused" )
            public String beanstringfield = "Foo";
            @SuppressWarnings( "unused" )
            public int beanintfield = 37;
        } );
        String strJson = JsonUtil.buildJsonResponse( response );
        System.out.println( strJson );

        String strRefJson = "{\"result\":{\"beanstringfield\":\"Foo\", \"beanintfield\":37},\"status\":\"OK\"}";
        ObjectMapper objectMapper = new ObjectMapper( );
        JsonNode objectNodeRef;
        JsonNode objectNodeJson;
        try
        {
            objectNodeRef = objectMapper.readTree( strRefJson );
            objectNodeJson = objectMapper.readTree( strJson );
        }
        catch( Exception e )
        {
            throw new AssertionError( "Should not happen" );
        }

        assertEquals( objectNodeRef, objectNodeJson );

    }

}
