/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.test.LuteceTestCase;

/**
 *
 */
public class SecurityUtilTest extends LuteceTestCase
{
    /**
     * Test of containsCleanParameters method, of class SecurityUtil.
     */
    @Test
    public void testContainsCleanParameters( )
    {
        System.out.println( "containsCleanParameters" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "param1", "AZ" );
        request.setParameter( "param2", "09" );
        request.setParameter( "param3", "az" );
        request.setParameter( "param4", "/" );

        assertTrue( SecurityUtil.containsCleanParameters( request ) );

        request.setParameter( "param4", "%" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", ">" );
        assertFalse( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "<" );
        assertFalse( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "#" );
        assertFalse( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "\"" );
        assertFalse( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", ";" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "&" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "[" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "]" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", ";" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "{" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
        request.setParameter( "param4", "}" );
        assertTrue( SecurityUtil.containsCleanParameters( request ) );
    }

    /**
     * Test of isRedirectUrlSafe method, of class SecurityUtil, to avoid open redirect
     */
    @Test
    public void testIsRedirectUrlSafe( )
    {
        System.out.println( "isRedirectUrlSafe" );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        // Assert False
        String strUrl = "http://anothersite.com";
        request.setParameter( "url", strUrl );
        assertFalse( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "//anothersite.com";
        request.setParameter( "url", strUrl );
        assertFalse( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "file://my.txt";
        request.setParameter( "url", strUrl );
        assertFalse( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "javascript:alert('hello');";
        request.setParameter( "url", strUrl );
        assertFalse( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "opera-http://anothersite.com";
        request.setParameter( "url", strUrl );
        assertFalse( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "http://another.subdomain.mylutece.com";
        request.setParameter( "url", strUrl );
        String strUrlPatterns = "http://**.lutece.com,https://**.lutece.com";
        assertFalse( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request, strUrlPatterns ) );

        // Assert True
        strUrl = null;
        assertTrue( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "";
        assertTrue( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "/jsp/site/Portal.jsp";
        request.setParameter( "url", strUrl );
        assertTrue( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "Another.jsp";
        request.setParameter( "url", strUrl );
        assertTrue( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "http://localhost/myapp/jsp/site/Portal.jsp";
        request.setParameter( "url", strUrl );
        assertTrue( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request ) );

        strUrl = "http://another.subdomain.lutece.com";
        request.setParameter( "url", strUrl );
        strUrlPatterns = "http://**.lutece.com/**,https://**.lutece.com/**";
        assertTrue( SecurityUtil.isInternalRedirectUrlSafe( strUrl, request, strUrlPatterns ) );

    }
}
