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
package fr.paris.lutece.portal.service.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * Discards the body written after a redirect.
 * <p>
 * The JSP of a MVC controller still renders its content, header and footer once the controller has redirected. OpenLiberty then fails with
 * "Message already sent" and closes the persistent connection without announcing it: the next request sent on that connection by a reverse proxy
 * (nginx, Kubernetes ingress) gets no response, a 502 for the user.
 * </p>
 */
public class RedirectResponseWrapper extends HttpServletResponseWrapper
{
    private boolean _bRedirected;
    private PrintWriter _discardWriter;
    private ServletOutputStream _discardOutputStream;

    /**
     * Constructor
     *
     * @param response
     *            The response to wrap
     */
    public RedirectResponseWrapper( HttpServletResponse response )
    {
        super( response );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendRedirect( String strLocation ) throws IOException
    {
        super.sendRedirect( strLocation );
        _bRedirected = true;
    }

    /**
     * Tells whether a redirect has been sent
     *
     * @return true if the response has been redirected
     */
    public boolean isRedirected( )
    {
        return _bRedirected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrintWriter getWriter( ) throws IOException
    {
        if ( _bRedirected )
        {
            if ( _discardWriter == null )
            {
                _discardWriter = new PrintWriter( Writer.nullWriter( ) );
            }
            return _discardWriter;
        }
        return super.getWriter( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServletOutputStream getOutputStream( ) throws IOException
    {
        if ( _bRedirected )
        {
            if ( _discardOutputStream == null )
            {
                _discardOutputStream = new DiscardOutputStream( );
            }
            return _discardOutputStream;
        }
        return super.getOutputStream( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flushBuffer( ) throws IOException
    {
        if ( !_bRedirected )
        {
            super.flushBuffer( );
        }
    }

    /**
     * Output stream that drops what is written
     */
    private static final class DiscardOutputStream extends ServletOutputStream
    {
        @Override
        public void write( int b )
        {
            // The response has been redirected: the body is ignored
        }

        @Override
        public void write( byte [ ] b, int off, int len )
        {
            // The response has been redirected: the body is ignored
        }

        @Override
        public boolean isReady( )
        {
            return true;
        }

        @Override
        public void setWriteListener( WriteListener writeListener )
        {
            // Nothing is ever written
        }
    }
}
