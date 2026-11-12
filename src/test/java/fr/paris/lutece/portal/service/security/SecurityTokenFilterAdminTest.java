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
package fr.paris.lutece.portal.service.security;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.ReflectionTestUtils;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * A back office action sent without its security token is refused with a 403, not an error
 */
public class SecurityTokenFilterAdminTest extends LuteceTestCase
{
    private static final String ADMIN_CONTROLLER = "/jsp/admin/system/ManageSecurityHeaders.jsp";
    private static final String ACTION = "removeSecurityHeader";

    @Inject
    private SecurityTokenHandler _handler;
    @Inject
    private SecurityTokenService _tokenService;

    private SecurityTokenFilterAdmin _filter;

    /**
     * A response that keeps the status sent as an error
     */
    private static final class ErrorResponse extends HttpServletResponseWrapper
    {
        private int _nError;

        ErrorResponse( )
        {
            super( new MockHttpServletResponse( ) );
        }

        @Override
        public void sendError( int nStatus ) throws IOException
        {
            _nError = nStatus;
        }
    }

    @BeforeEach
    protected void createFilter( )
    {
        _handler.registerAdminControllers( null );
        _filter = new SecurityTokenFilterAdmin( );
        ReflectionTestUtils.setField( _filter, "_securityTokenHandler", _handler );
    }

    private MockHttpServletRequest post( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setServletPath( ADMIN_CONTROLLER );
        request.setParameter( "action", ACTION );
        return request;
    }

    @Test
    public void testActionWithoutTokenIsForbidden( ) throws IOException, ServletException
    {
        boolean [ ] bChained = {
                false
        };
        ErrorResponse response = new ErrorResponse( );

        _filter.doFilter( post( ), response, ( req, resp ) -> bChained [0] = true );

        assertEquals( 403, response._nError );
        assertFalse( bChained [0] );
    }

    @Test
    public void testActionWithTokenPasses( ) throws IOException, ServletException
    {
        boolean [ ] bChained = {
                false
        };
        MockHttpServletRequest request = post( );
        request.setParameter( SecurityTokenHandler.MARK_CSRF_TOKEN, _tokenService.getToken( request, ACTION ) );
        ErrorResponse response = new ErrorResponse( );

        _filter.doFilter( request, response, ( req, resp ) -> bChained [0] = true );

        assertEquals( 0, response._nError );
        assertTrue( bChained [0] );
    }
}
