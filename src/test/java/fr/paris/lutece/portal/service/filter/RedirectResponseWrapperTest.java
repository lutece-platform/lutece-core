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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.mocks.MockHttpServletResponse;

/**
 * Tests that the body written after a redirect is discarded.
 */
public class RedirectResponseWrapperTest
{
    /**
     * Before a redirect, the body reaches the response.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testBodyWrittenWithoutRedirect( ) throws IOException
    {
        MockHttpServletResponse response = new MockHttpServletResponse( );
        RedirectResponseWrapper wrapper = new RedirectResponseWrapper( response );

        PrintWriter writer = wrapper.getWriter( );
        writer.write( "content" );
        writer.flush( );

        assertFalse( wrapper.isRedirected( ) );
        assertEquals( "content", response.getContentAsString( ) );
    }

    /**
     * After a redirect, the content, header and footer of the JSP are discarded.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @Test
    public void testBodyDiscardedAfterRedirect( ) throws IOException
    {
        MockHttpServletResponse response = new MockHttpServletResponse( );
        RedirectResponseWrapper wrapper = new RedirectResponseWrapper( response );

        wrapper.sendRedirect( "jsp/admin/AdminMenu.jsp" );
        PrintWriter writer = wrapper.getWriter( );
        writer.write( "\n\n<html>header</html>" );
        writer.flush( );
        wrapper.getOutputStream( ).write( "footer".getBytes( ) );
        wrapper.flushBuffer( );

        assertTrue( wrapper.isRedirected( ) );
        assertEquals( "", response.getContentAsString( ) );
    }
}
