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
package fr.paris.lutece.portal.service.image;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

/**
 * Tests the identifiers of the images requested from {@link ImageServlet}
 */
public class ImageServletRequestTest extends LuteceTestCase
{
    private static final String MIME_TYPE_SVG = "image/svg+xml";

    /**
     * A servlet whose context only knows the MIME type of the default image
     */
    private static ImageServlet newServlet( ) throws Exception
    {
        ServletContext context = (ServletContext) Proxy.newProxyInstance( ImageServletRequestTest.class.getClassLoader( ), new Class<?> [ ] {
                ServletContext.class
        }, ( proxy, method, args ) -> "getMimeType".equals( method.getName( ) ) ? MIME_TYPE_SVG : null );
        ServletConfig config = (ServletConfig) Proxy.newProxyInstance( ImageServletRequestTest.class.getClassLoader( ), new Class<?> [ ] {
                ServletConfig.class
        }, ( proxy, method, args ) -> "getServletContext".equals( method.getName( ) ) ? context : null );

        ImageServlet servlet = new ImageServlet( );
        servlet.init( config );

        return servlet;
    }

    /**
     * An id that is not a number, or out of the int range, gets the default image as an unknown id
     */
    @Test
    public void testIdNotANumberGetsTheDefaultImage( ) throws Exception
    {
        ImageServlet servlet = newServlet( );

        for ( String strId : new String [ ] {
                "abc", "99999999999", "-1", "999999"
        } )
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.setParameter( ImageServlet.PARAMETER_RESOURCE_TYPE, "page_thumbnail" );
            request.setParameter( ImageServlet.PARAMETER_ID, strId );
            MockHttpServletResponse response = new MockHttpServletResponse( );

            servlet.processRequest( request, response );

            assertEquals( MIME_TYPE_SVG, response.getContentType( ), "id " + strId );
        }
    }
}
