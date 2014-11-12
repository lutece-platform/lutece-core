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

import java.sql.Connection;


/**
 * Wraps a connection to use {@link ConnectionPool} when closing with ({@link #close()}).
 * The actual close is done by {@link #closeConnection()}. <br>
 * Lutece needs the {@link #close()} method to return the connection to the pool.
 * Lutece classes (like DAOUtil uses {@link LuteceConnectionService#freeConnection(Connection)} ),
 * but external libraries (such as hibernate) closes connection directly.
 * This interface is a workaround for Connection incompatibilities from java5 to java6.
 * It should be remove as soon as Lutece drop java5 compatibility.
 * No wrapper delegation is possible implementing Connection interface and wrapping one connection.
 * Actually, implementing java5 java.sql.Connection will cause compilation error with jdk6 (not overriding methods), while
 * implementing java6 java.sql.Connection will also cause compilation errors with jdk5 (no such class found).
 * @see #closeConnection()
 * @see <a href="http://dev.lutece.paris.fr/jira/browse/LUTECE-1267">LUTECE-1267</a>
 */
public interface LuteceConnection extends Connection
{
    /**
     * Actual connection close.
     */
    void closeConnection(  );
}
