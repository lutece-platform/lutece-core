/*
 * Copyright (c) 2002-2026, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import jakarta.enterprise.inject.spi.CDI;
import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * Rich text content is always sanitized when it is rendered (LUT-33153), plain HTML included.
 */
public class RichTextContentServiceTest extends LuteceTestCase
{
    /** XSS vectors a rich text editor could store (OWASP XSS filter evasion cheat sheet) */
    private static final String [ ] PAYLOADS = {
            "<script>alert(1)</script>", "<img src=x onerror=alert(1)>", "<svg onload=alert(1)>", "<a href=\"javascript:alert(1)\">x</a>",
            "<a href=\"JaVaScRiPt:alert(1)\">x</a>", "<iframe src=\"https://evil.example\"></iframe>", "<p onmouseover=\"alert(1)\">x</p>",
            "<img src=\"data:image/svg+xml;base64,PHN2Zz48L3N2Zz4=\">", "<div style=\"background:url(javascript:alert(1))\">x</div>",
            "<math><mtext><table><mglyph><style><img src=x onerror=alert(1)>", "<form action=\"https://evil.example\"><button>x</button></form>",
            "<object data=\"x.swf\"></object>", "<base href=\"https://evil.example/\">", "<meta http-equiv=\"refresh\" content=\"0;url=https://evil.example\">"
    };

    /**
     * No XSS vector survives the rendering of HTML content.
     *
     * @throws RichTextParsingException
     *             if the rendering fails
     */
    @Test
    public void testHtmlContentIsSanitized( ) throws RichTextParsingException
    {
        for ( String strPayload : PAYLOADS )
        {
            String strHtml = RichTextContentService.getContent( "<p>before</p>" + strPayload + "<p>after</p>" ).toLowerCase( );
            assertFalse( strHtml.contains( "<script" ), strPayload + " -> " + strHtml );
            assertFalse( strHtml.matches( "(?s).*\\son[a-z]+\\s*=.*" ), strPayload + " -> " + strHtml );
            assertFalse( strHtml.contains( "javascript:" ), strPayload + " -> " + strHtml );
            assertFalse( strHtml.matches( "(?s).*<(iframe|object|form|base|meta|svg|math|style)\\b.*" ), strPayload + " -> " + strHtml );
            assertTrue( strHtml.startsWith( "<p>before</p>" ), strPayload + " -> " + strHtml );
        }
    }

    /**
     * The formatting a rich text editor produces is kept.
     *
     * @throws RichTextParsingException
     *             if the rendering fails
     */
    @Test
    public void testFormattingIsKept( ) throws RichTextParsingException
    {
        String strHtml = "<p><strong>a</strong> <em>b</em> <a href=\"https://lutece.paris.fr\">c</a></p><ul><li>d</li></ul>";
        String strRendered = RichTextContentService.getContent( strHtml );
        assertTrue( strRendered.contains( "<strong>a</strong>" ) && strRendered.contains( "<em>b</em>" ) && strRendered.contains( "<li>d</li>" ),
                strRendered );
        assertTrue( strRendered.contains( "href=\"https://lutece.paris.fr\"" ), strRendered );
    }

    /**
     * The markup handed to a template holds the sanitized HTML, so it can be written as is.
     *
     * @throws TemplateModelException
     *             if the markup cannot be read
     * @throws RichTextParsingException
     *             if the rendering fails
     */
    @Test
    public void testMarkupHoldsTheSanitizedHtml( ) throws TemplateModelException, RichTextParsingException
    {
        String strContent = "<p>x</p><script>alert(1)</script>";
        assertEquals( RichTextContentService.getContent( strContent ),
                HTMLOutputFormat.INSTANCE.getMarkupString( RichTextContentService.getMarkup( strContent ) ) );
        assertEquals( "", HTMLOutputFormat.INSTANCE.getMarkupString( RichTextContentService.getMarkup( null ) ) );
    }

    /**
     * In an auto-escaping template, richText( x ) writes the sanitized HTML, while ${x} writes the escaped text.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testRichTextFunctionInATemplate( ) throws Exception
    {
        StringTemplateLoader loader = new StringTemplateLoader( );
        loader.putTemplate( "t", "[${richText( value )}][${value}][<#assign c><b>${value}</b></#assign>${richText( c )}]" );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setTemplateLoader( loader );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "richText", new RichTextContentMethod( ) );

        StringWriter out = new StringWriter( );
        cfg.getTemplate( "t" ).process( Map.of( "value", "<p>x</p><img src=x onerror=alert(1)>" ), out );

        assertEquals( "[<p>x</p><img src=\"x\" />][&lt;p&gt;x&lt;/p&gt;&lt;img src=x onerror=alert(1)&gt;][<b>&lt;p&gt;x&lt;/p&gt;&lt;img src&#61;x onerror&#61;alert(1)&gt;</b>]",
                out.toString( ) );
    }

    /**
     * A content stored as BBCode is converted by the BBCode parser when its prefix is set, then sanitized; the templates
     * do not change when the editor changes its format.
     *
     * @throws RichTextParsingException
     *             if the rendering fails
     */
    @Test
    public void testBbcodeContentIsConvertedThenSanitized( ) throws RichTextParsingException
    {
        BbcodeRichTextContentParser parser = CDI.current( ).select( BbcodeRichTextContentParser.class ).get( );
        String strPrefix = parser.getPrefix( );
        parser.setPrefix( "[bbcode]" );
        try
        {
            String strHtml = RichTextContentService.getContent( "[bbcode][b]bold[/b] <script>alert(1)</script>" );
            assertTrue( strHtml.contains( "<b>bold</b>" ) || strHtml.contains( "<strong>bold</strong>" ), strHtml );
            assertFalse( strHtml.contains( "<script" ), strHtml );
            assertEquals( "[b]bold[/b]", RichTextContentService.getContent( "[b]bold[/b]" ), "a content without the prefix is HTML" );
        }
        finally
        {
            parser.setPrefix( strPrefix );
        }
    }

    /**
     * The plain text of a rich content, for a text message or a plain text e-mail: no tag, the entities decoded, a line
     * per paragraph, and nothing of a script.
     *
     * @throws RichTextParsingException
     *             if the rendering fails
     */
    @Test
    public void testPlainText( ) throws RichTextParsingException
    {
        String strText = RichTextContentService.getText( "<p>Hello <b>A &amp; B</b></p><p>second<br>line</p><script>alert(1)</script>" );
        assertEquals( "Hello A & B\nsecond\nline", strText );
        assertEquals( "", RichTextContentService.getText( null ) );
    }
}
