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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.sql.Connection;


/**
 *
 * LuteceConnectionProxy : proxy implementation of {@link LuteceConnection}
 * This class should be removed when java5 support is dropped.
 *
 */
public class LuteceConnectionProxy implements InvocationHandler
{
    /**
     * Close method name
     */
    private static final String METHOD_CLOSE = "close";

    /**
     * Close connection method name (from {@link LuteceConnection}
     */
    private static final String METHOD_CLOSE_CONNECTION = "closeConnection";
    private ConnectionPool _pool;
    private Connection _connection;

    /**
     * Private constructor
     * @param pool the pool to use
     * @param connection the actual connection
     */
    LuteceConnectionProxy( ConnectionPool pool, Connection connection )
    {
        _pool = pool;
        _connection = connection;
    }

    /**
     * Catches close() and closeConnection() method.
     * close() calls _pool.freeConnection, and closeConnection() calls _connection.close().
     *
     * @param proxy the proxy
     * @param method the method
     * @param args the args
     * @return the object
     * @throws Throwable the throwable
     */
    @Override
    public Object invoke( Object proxy, Method method, Object[] args )
        throws Throwable
    {
        Object oReturn;

        if ( METHOD_CLOSE.equals( method.getName(  ) ) )
        {
            _pool.freeConnection( (Connection) proxy );
            oReturn = null;
        }
        else if ( METHOD_CLOSE_CONNECTION.equals( method.getName(  ) ) )
        {
            _connection.close(  );
            oReturn = null;
        }
        else
        {
            try {
                oReturn = method.invoke( _connection, args );
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        return oReturn;
    }
}
