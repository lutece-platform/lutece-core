/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.portal.web.encoding;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


/**
 * Encoding filter
 */
public class EncodingFilter implements javax.servlet.Filter
{
    private static final String REQUEST_ENCODING = "UTF-8";

    /**
     * Initializes the filter
     * @param filterConfig The filter config
     * @throws ServletException If an error occured
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        // This would be a good place to collect a parameterized
        // default encoding type.  For brevity, we're going to
        // use a hard-coded value in this example.
    }

    /**
     * Apply the filter
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The Filter Chain
     * @throws IOException If an error occured
     * @throws ServletException If an error occured
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain )
        throws IOException, ServletException
    {
        // Wrap the response object.  You should create a mechanism
        // to ensure the response object only gets wrapped once.
        // In this example, the response object will inappropriately
        // get wrapped multiple times during a forward.
        response = new EncodingServletResponse( (HttpServletResponse) response );

        // Specify the encoding to assume for the request so
        // the parameters can be properly decoded/.
        request.setCharacterEncoding( REQUEST_ENCODING );

        filterChain.doFilter( request, response );
    }

    /**
     * Destroy the filter
     */
    public void destroy(  )
    {
        // no-op
    }
}
