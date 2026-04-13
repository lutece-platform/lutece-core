/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.web.xss;

import fr.paris.lutece.portal.service.html.XSSSanitizerException;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * FieldValidationService Test Class
 */
public class FieldValidationServiceTest extends LuteceTestCase
{
    private static final boolean SANITIZE_MODE = true;
    private static final boolean BLOCK_MODE = false;
    private static final String XSS_CHARACTERS = "<>#\"&";

    /**
     * Test that a null or empty value is always accepted unchanged, whatever the mode.
     *
     * @throws XSSSanitizerException
     *             never, the values are accepted
     */
    public void testValidateNullAndEmpty( ) throws XSSSanitizerException
    {
        assertNull( FieldValidationService.validate( FieldType.URL, null, SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "", FieldValidationService.validate( FieldType.EMAIL, "", BLOCK_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test that in block mode a value containing a forbidden character raises the sanitizer exception, whatever the
     * type.
     */
    public void testBlockModeBlocksForbiddenCharacters( )
    {
        assertBlocked( FieldType.URL, "jsp/Portal.jsp?a=1&b=2" );
        assertBlocked( FieldType.HTML, "<b>x</b>" );
        assertBlocked( FieldType.TEXT, "a & b" );
    }

    /**
     * Asserts that a value is rejected in block mode by expecting the sanitizer exception.
     *
     * @param type
     *            the field type
     * @param strValue
     *            the value expected to be blocked
     */
    private void assertBlocked( FieldType type, String strValue )
    {
        try
        {
            FieldValidationService.validate( type, strValue, BLOCK_MODE, XSS_CHARACTERS );
            fail( "Expected XSSSanitizerException for value: " + strValue );
        }
        catch( XSSSanitizerException e )
        {
            assertNotNull( e );
        }
    }

    /**
     * Test that in block mode a clean value is accepted unchanged.
     *
     * @throws XSSSanitizerException
     *             never, the value is clean
     */
    public void testBlockModeKeepsCleanValue( ) throws XSSSanitizerException
    {
        assertEquals( "jsp/site/Portal.jsp", FieldValidationService.validate( FieldType.URL, "jsp/site/Portal.jsp", BLOCK_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test that in sanitize mode a legitimate URL with a query string is accepted and left unchanged.
     *
     * @throws XSSSanitizerException
     *             never, sanitize mode does not block
     */
    public void testValidateUrlValid( ) throws XSSSanitizerException
    {
        String strUrl = "jsp/site/Portal.jsp?page=map&id=42";
        assertEquals( strUrl, FieldValidationService.validate( FieldType.URL, strUrl, SANITIZE_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test that in sanitize mode a URL using a dangerous scheme is replaced by the invalid_url placeholder.
     *
     * @throws XSSSanitizerException
     *             never, sanitize mode does not block
     */
    public void testValidateUrlDangerousScheme( ) throws XSSSanitizerException
    {
        assertEquals( "invalid_url", FieldValidationService.validate( FieldType.URL, "javascript:alert(1)", SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "invalid_url", FieldValidationService.validate( FieldType.URL, "  JavaScript:alert(1)", SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "invalid_url", FieldValidationService.validate( FieldType.URL, "java\tscript:alert(1)", SANITIZE_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test that the URL validation rejects a URL carrying an injected markup payload in a query parameter value,
     * whether the payload is raw or URL-encoded, instead of rewriting it.
     */
    public void testValidateUrlRejectsMarkupInParameters( )
    {
        assertEquals( "invalid_url", FieldValidationService.validateUrl( "jsp/site/Portal.jsp?q=%3Cscript%3Ealert(1)%3C/script%3E" ) );
        assertEquals( "invalid_url", FieldValidationService.validateUrl( "jsp/site/Portal.jsp?q=<script>" ) );
    }

    /**
     * Test that the URL validation trims the value and accepts a URL whose parameters carry no markup.
     */
    public void testValidateUrlKeepsEncodedParameters( )
    {
        assertEquals( "jsp/site/Portal.jsp?q=a%20b&id=42", FieldValidationService.validateUrl( "  jsp/site/Portal.jsp?q=a%20b&id=42  " ) );
    }

    /**
     * Test that the independent parameter sanitization neutralizes an injected markup payload while preserving the
     * base path, the parameter names and the fragment.
     */
    public void testSanitizeUrlParameters( )
    {
        String strResult = FieldValidationService.sanitizeUrlParameters( "jsp/site/Portal.jsp?q=%3Cscript%3Ealert(1)%3C/script%3E&id=42#anchor" );
        assertFalse( strResult.contains( "<" ) );
        assertFalse( strResult.contains( ">" ) );
        assertFalse( strResult.contains( "%3C" ) );
        assertTrue( strResult.startsWith( "jsp/site/Portal.jsp?q=" ) );
        assertTrue( strResult.contains( "&id=42" ) );
        assertTrue( strResult.endsWith( "#anchor" ) );
    }

    /**
     * Test that the independent parameter sanitization leaves a URL without query string unchanged.
     */
    public void testSanitizeUrlParametersWithoutQuery( )
    {
        assertNull( FieldValidationService.sanitizeUrlParameters( null ) );
        assertEquals( "jsp/site/Portal.jsp", FieldValidationService.sanitizeUrlParameters( "jsp/site/Portal.jsp" ) );
    }

    /**
     * Test the CSS color validation: a valid color is kept, an invalid one is replaced by the placeholder.
     *
     * @throws XSSSanitizerException
     *             never, sanitize mode does not block
     */
    public void testValidateColor( ) throws XSSSanitizerException
    {
        assertEquals( "#fff", FieldValidationService.validate( FieldType.COLOR, "#fff", SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "rgba(0,0,0,0.5)", FieldValidationService.validate( FieldType.COLOR, "rgba(0,0,0,0.5)", SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "invalid_color", FieldValidationService.validate( FieldType.COLOR, "notacolor", SANITIZE_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test the email validation: a valid email is kept, an invalid one is replaced by the placeholder.
     *
     * @throws XSSSanitizerException
     *             never, sanitize mode does not block
     */
    public void testValidateEmail( ) throws XSSSanitizerException
    {
        assertEquals( "webmaster@mydomain.com", FieldValidationService.validate( FieldType.EMAIL, "webmaster@mydomain.com", SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "user+tag@domain.com", FieldValidationService.validate( FieldType.EMAIL, "user+tag@domain.com", SANITIZE_MODE, XSS_CHARACTERS ) );
        assertEquals( "invalid_email", FieldValidationService.validate( FieldType.EMAIL, "not-an-email", SANITIZE_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test that in sanitize mode an HTML block is cleaned and drops scripts and event handlers.
     *
     * @throws XSSSanitizerException
     *             never, sanitize mode does not block
     */
    public void testSanitizeHtml( ) throws XSSSanitizerException
    {
        String strDirty = "<h1>Title</h1><b>bold</b><img src=\"x\" onerror=\"alert(1)\"><script>alert(1)</script>";
        String strResult = FieldValidationService.validate( FieldType.HTML, strDirty, SANITIZE_MODE, XSS_CHARACTERS );
        assertFalse( strResult.contains( "<script" ) );
        assertFalse( strResult.contains( "onerror" ) );
        assertTrue( strResult.contains( "bold" ) );
    }

    /**
     * Test that in sanitize mode a plain text value is left unchanged.
     *
     * @throws XSSSanitizerException
     *             never, sanitize mode does not block
     */
    public void testValidateTextUnchanged( ) throws XSSSanitizerException
    {
        String strText = "City of Paris Co";
        assertEquals( strText, FieldValidationService.validate( FieldType.TEXT, strText, SANITIZE_MODE, XSS_CHARACTERS ) );
    }

    /**
     * Test that the two-argument method resolves the mode from the admin XSS filter configuration, which defaults to
     * sanitize mode: a dangerous URL is never blocked but replaced by the invalid_url placeholder.
     *
     * @throws XSSSanitizerException
     *             never, the configured mode is sanitize
     */
    public void testValidateResolvesModeFromConfiguration( ) throws XSSSanitizerException
    {
        assertEquals( "invalid_url", FieldValidationService.validate( FieldType.URL, "javascript:alert(1)" ) );
    }
}
