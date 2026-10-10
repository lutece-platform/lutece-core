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
package fr.paris.lutece.util.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpSession;
import jakarta.servlet.http.HttpSession;

/**
 * Tests the session attributes read once per request.
 */
public class SessionAttributeCacheTest
{
    private static final String NAME = "attribute";

    /**
     * Session counting the reads, with a settable id
     */
    private static final class CountingSession extends MockHttpSession
    {
        private int _nReads;
        private final String _strId;

        CountingSession( String strId )
        {
            _strId = strId;
        }

        @Override
        public Object getAttribute( String strName )
        {
            _nReads++;
            return super.getAttribute( strName );
        }

        @Override
        public String getId( )
        {
            return _strId;
        }
    }

    /**
     * Request whose session can be replaced
     */
    private static final class Request extends MockHttpServletRequest
    {
        private HttpSession _session;

        @Override
        public HttpSession getSession( boolean bCreate )
        {
            if ( _session == null && bCreate )
            {
                _session = new CountingSession( "created" );
            }
            return _session;
        }
    }

    /**
     * An attribute, present or absent, is read from the session once per request.
     */
    @Test
    public void testReadOncePerRequest( )
    {
        Request request = new Request( );
        CountingSession session = new CountingSession( "s1" );
        request._session = session;
        session.setAttribute( "present", "value" );

        for ( int i = 0; i < 5; i++ )
        {
            assertNull( SessionAttributeCache.getAttribute( request, NAME ) );
            assertEquals( "value", SessionAttributeCache.getAttribute( request, "present" ) );
        }
        assertEquals( 2, session._nReads );
    }

    /**
     * A value written through the cache is read back in the same request; removing it too.
     */
    @Test
    public void testWriteThenRead( )
    {
        Request request = new Request( );
        assertNull( SessionAttributeCache.getAttribute( request, NAME ) );

        SessionAttributeCache.setAttribute( request, NAME, "user" );
        assertEquals( "user", SessionAttributeCache.getAttribute( request, NAME ) );
        assertEquals( "user", request.getSession( false ).getAttribute( NAME ) );

        SessionAttributeCache.removeAttribute( request, NAME );
        assertNull( SessionAttributeCache.getAttribute( request, NAME ) );
        assertNull( request.getSession( false ).getAttribute( NAME ) );
    }

    /**
     * A new session during the request (invalidated then created) is read again.
     */
    @Test
    public void testNewSessionReadAgain( )
    {
        Request request = new Request( );
        CountingSession first = new CountingSession( "s1" );
        first.setAttribute( NAME, "first" );
        request._session = first;
        assertEquals( "first", SessionAttributeCache.getAttribute( request, NAME ) );

        CountingSession second = new CountingSession( "s2" );
        request._session = second;
        assertNull( SessionAttributeCache.getAttribute( request, NAME ) );

        request._session = null;
        assertNull( SessionAttributeCache.getAttribute( request, NAME ) );
    }
}
