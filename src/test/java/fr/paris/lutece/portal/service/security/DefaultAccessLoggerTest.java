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

import fr.paris.lutece.portal.service.security.impl.DefaultAccessLogger;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * This class provides writing services in the application logs files
 */
public final class DefaultAccessLoggerTest extends LuteceTestCase
{

    private final String TEST_MESSAGE_TRUE = "09/04/21 15:59:13 INFO  [http-nio-8080-exec-9] lutece.accessLogger - |ffa2bcacdcc721223d1a7211f273ec15|<the application code>|CONNECT|user.loginAdminUser|BO:admin||";
    private final String TEST_MESSAGE_FALSE = "09/04/21 15:59:13 INFO  [http-nio-8080-exec-9] lutece.accessLogger - |badhashhhhhhhhhhhhhhhhhhhhhhhhhh|<the application code>|CONNECT|user.loginAdminUser|BO:admin||";

    /**
     * test valid hash
     *
     */
    public void testValidHashMessage( )
    {
        DefaultAccessLogger logger = new DefaultAccessLogger( );

        assertTrue( logger.verifyMessageHash( TEST_MESSAGE_TRUE ) );
    }

    /**
     * test wrong hash
     *
     */
    public void testWrongHashMessage( )
    {
        DefaultAccessLogger logger = new DefaultAccessLogger( );

        assertFalse( logger.verifyMessageHash( TEST_MESSAGE_FALSE ) );
    }
}
