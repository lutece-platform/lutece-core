/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.web.xss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * Tests the input filter (LUT-34109): it never rewrites the parameters, and when it is activated it refuses a request
 * holding one of the configured characters.
 */
public class SafeRequestFilterTest
{
    private static final String CHARACTERS = "<>#\"&";
    private static final String TYPED = "l'école a<b \"c\" & d";
    private static final String MESSAGE_URL = "jsp/site/Portal.jsp?page=message";

    /**
     * A filter whose message page is a fixed URL.
     */
    private static final class TestFilter extends SafeRequestFilter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void init( FilterConfig config )
        {
            // Initialized by initFilter
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getMessageUrl( HttpServletRequest request, String strMessageKey, Object [ ] messageArgs, String strTitleKey )
        {
            return MESSAGE_URL;
        }
    }

    /**
     * A response that keeps the URL it is redirected to.
     */
    private static final class RedirectCapture extends HttpServletResponseWrapper
    {
        private String _strLocation;

        /**
         * Constructor.
         */
        RedirectCapture( )
        {
            super( new MockHttpServletResponse( ) );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void sendRedirect( String strLocation )
        {
            _strLocation = strLocation;
        }

        /**
         * Gives the URL the response was redirected to.
         *
         * @return the URL, or null
         */
        String getLocation( )
        {
            return _strLocation;
        }
    }

    /**
     * Not activated, the default: the request goes through as it came, its values unchanged.
     *
     * @throws IOException
     *             if an I/O error occurs
     * @throws ServletException
     *             if the chain fails
     */
    @Test
    public void testParametersAreNeverRewritten( ) throws IOException, ServletException
    {
        TestFilter filter = new TestFilter( );
        filter.initFilter( false, CHARACTERS );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "text", TYPED );
        ServletRequest [ ] passed = new ServletRequest [ 1];

        filter.doFilter( request, new MockHttpServletResponse( ), ( req, res ) -> passed [0] = req );

        assertSame( request, passed [0] );
        assertEquals( TYPED, passed [0].getParameter( "text" ) );
    }

    /**
     * Activated: a request holding one of the characters is sent to the message page and does not reach the chain.
     *
     * @throws IOException
     *             if an I/O error occurs
     * @throws ServletException
     *             if the chain fails
     */
    @Test
    public void testActivatedFilterRefusesTheCharacters( ) throws IOException, ServletException
    {
        TestFilter filter = new TestFilter( );
        filter.initFilter( true, CHARACTERS );
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "text", TYPED );
        RedirectCapture response = new RedirectCapture( );
        ServletRequest [ ] passed = new ServletRequest [ 1];

        filter.doFilter( request, response, ( req, res ) -> passed [0] = req );

        assertNull( passed [0] );
        assertEquals( MESSAGE_URL, response.getLocation( ) );
    }
}
