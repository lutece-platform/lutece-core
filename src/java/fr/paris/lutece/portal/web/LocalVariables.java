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
package fr.paris.lutece.portal.web;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Tools class to transmit config, request and response servlet parameters
 * using {@link java.lang.ThreadLocal}.
 */
public final class LocalVariables
{
    private static ThreadLocal<ServletConfig> _tlConfig = new ThreadLocal<ServletConfig>(  );
    private static ThreadLocal<HttpServletRequest> _tlRequest = new ThreadLocal<HttpServletRequest>(  );
    private static ThreadLocal<HttpServletResponse> _tlResponse = new ThreadLocal<HttpServletResponse>(  );

    /**
     * Utility classes have no constructor
     */
    private LocalVariables(  )
    {
    }

    /**
     * Initialize thread locals variables
     *
     * @param config Current <code>ServletConfig</code> to associate to the thread
     * @param request Current <code>HttpServletRequest</code> to associate to the thread
     * @param response Current <code>HttpServletResponse</code> to associate to the thread
     */
    public static void setLocal( ServletConfig config, HttpServletRequest request, HttpServletResponse response )
    {
        _tlConfig.set( config );
        _tlRequest.set( request );
        _tlResponse.set( response );
    }

    /**
     * Read <code>ServletConfig</code> associate to the current thread
     *
     * @return the <code>ServletConfig</code> associate to the current thread
     */
    public static ServletConfig getConfig(  )
    {
        return _tlConfig.get(  );
    }

    /**
     * Read <code>HttpServletRequest</code> associate to the current thread
     *
     * @return the <code>HttpServletRequest</code> associate to the current thread
     */
    public static HttpServletRequest getRequest(  )
    {
        return _tlRequest.get(  );
    }

    /**
     * Read <code>HttpServletResponse</code> associate to the current thread
     *
     * @return the <code>HttpServletResponse</code> associate to the current thread
     */
    public static HttpServletResponse getResponse(  )
    {
        return _tlResponse.get(  );
    }
}
