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
package fr.paris.lutece.portal.service.upload;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;

/**
 * The parameters of the query string of a multipart request are kept with the fields of its body
 */
public class MultipartHandlerQueryStringTest
{
    @Test
    public void testQueryStringParametersAreKept( ) throws Exception
    {
        HttpServletRequestWrapper request = new HttpServletRequestWrapper( new MockHttpServletRequest( ) )
        {
            @Override
            public String getQueryString( )
            {
                return "page=appointment&ref=a%20b&multi=1";
            }

            @Override
            public String getCharacterEncoding( )
            {
                return "UTF-8";
            }

            @Override
            public Collection<Part> getParts( )
            {
                return List.of( textPart( "action", "doProcessWorkflowAction" ), textPart( "multi", "2" ) );
            }
        };

        MultipartHttpServletRequest multipart = new DefaultMultipartHandler( ).handle( request, false );

        assertEquals( "appointment", multipart.getParameter( "page" ) );
        assertEquals( "a b", multipart.getParameter( "ref" ) );
        assertEquals( "doProcessWorkflowAction", multipart.getParameter( "action" ) );
        // query string values first, then the values of the body, as for a request that is not multipart
        assertArrayEquals( new String [ ] {
                "1", "2"
        }, multipart.getParameterValues( "multi" ) );
    }

    private static Part textPart( String strName, String strValue )
    {
        return (Part) java.lang.reflect.Proxy.newProxyInstance( Part.class.getClassLoader( ), new Class<?> [ ] {
                Part.class
        }, ( proxy, method, args ) -> {
            switch( method.getName( ) )
            {
                case "getName":
                    return strName;
                case "getSubmittedFileName":
                    return null;
                case "getInputStream":
                    InputStream stream = new ByteArrayInputStream( strValue.getBytes( StandardCharsets.UTF_8 ) );
                    return stream;
                default:
                    return null;
            }
        } );
    }
}
