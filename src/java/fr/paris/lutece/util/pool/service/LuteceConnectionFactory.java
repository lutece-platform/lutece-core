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
package fr.paris.lutece.util.pool.service;

import java.lang.reflect.Proxy;

import java.sql.Connection;


/**
 *
 * Factory to create LuteceConnection
 */
public final class LuteceConnectionFactory
{
    /**
     * Supported interfaces for the proxy
     */
    @SuppressWarnings( "rawtypes" )
    private static final Class[] PROXY_INTERFACES = { LuteceConnection.class };

    /**
     * Instantiates a new lutece connection factory.
     */
    private LuteceConnectionFactory(  )
    {
    }

    /**
     * Builds a new proxied instance of the connection
     * so we can use {@link ConnectionPool} with third party libraries.
     * @param pool the connection pool to use when closing connection
     * @param connection the actual connection
     * @return a proxy implementing {@link LuteceConnection}
     */
    public static LuteceConnection newInstance( ConnectionPool pool, Connection connection )
    {
        return (LuteceConnection) Proxy.newProxyInstance( LuteceConnection.class.getClassLoader(  ), PROXY_INTERFACES,
            new LuteceConnectionProxy( pool, connection ) );
    }
}
