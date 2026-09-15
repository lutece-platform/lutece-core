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
package fr.paris.lutece.portal.service.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Checks that {@link HtmlMarkup} really produces a value that survives both auto-escaping modes (LUT-33153).
 *
 * <p>
 * The point of the class is a single behaviour: HTML built on the Java side must reach the page unescaped whether
 * <code>service.freemarker.templateAutoEscape</code> is on or off, with the template writing a plain
 * <code>${x}</code>. These tests render a real template with a real FreeMarker configuration under both settings and
 * compare, rather than asserting on the wrapper type.
 * </p>
 *
 * <p>
 * {@link HtmlMarkup#ofSanitized(String)} is not covered here: it goes through <code>XSSSanitizerService</code>, which
 * resolves its implementation through CDI and therefore needs a container. It is exercised by the integration run.
 * </p>
 */
public class HtmlMarkupTest
{
    private static final String RICH_HTML = "<p>rich <b>content</b> &amp; more</p>";

    private static String render( boolean bAutoEscape, String strTemplate, Map<String, Object> model ) throws Exception
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_31 );

        if ( bAutoEscape )
        {
            cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
            cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        }

        StringWriter out = new StringWriter( );
        new Template( "test", new StringReader( strTemplate ), cfg ).process( model, out );

        return out.toString( );
    }

    private static Map<String, Object> singleton( String strKey, Object value )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( strKey, value );

        return model;
    }

    @Test
    @DisplayName( "of() renders the markup unescaped in both modes" )
    public void ofRendersUnescapedInBothModes( ) throws Exception
    {
        Map<String, Object> model = singleton( "content", HtmlMarkup.of( RICH_HTML ) );

        assertEquals( RICH_HTML, render( false, "${content}", model ) );
        assertEquals( RICH_HTML, render( true, "${content}", model ) );
    }

    @Test
    @DisplayName( "A raw String is escaped when auto-escaping is on — the regression of() prevents" )
    public void rawStringIsEscapedWhenAutoEscapingIsOn( ) throws Exception
    {
        Map<String, Object> model = singleton( "content", RICH_HTML );

        assertEquals( RICH_HTML, render( false, "${content}", model ) );
        assertEquals( "&lt;p&gt;rich &lt;b&gt;content&lt;/b&gt; &amp;amp; more&lt;/p&gt;", render( true, "${content}", model ) );
    }

    @Test
    @DisplayName( "of() needs no ?no_esc — which would not even parse when auto-escaping is off" )
    public void ofNeedsNoNoEscBuiltIn( ) throws Exception
    {
        Map<String, Object> model = singleton( "content", HtmlMarkup.of( RICH_HTML ) );

        // the template is the plain form, and it loads under both configurations
        assertNotNull( render( false, "${content}", model ) );
        assertNotNull( render( true, "${content}", model ) );
    }

    @Test
    @DisplayName( "of() supports ?has_content, so macro guards keep working" )
    public void ofSupportsHasContent( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "filled", HtmlMarkup.of( RICH_HTML ) );
        model.put( "blank", HtmlMarkup.of( "" ) );

        String strTemplate = "[${filled?has_content?c}][${blank?has_content?c}]";

        assertEquals( "[true][false]", render( false, strTemplate, model ) );
        assertEquals( "[true][false]", render( true, strTemplate, model ) );
    }

    @Test
    @DisplayName( "of() maps null to an empty value instead of failing" )
    public void ofAcceptsNull( ) throws Exception
    {
        Map<String, Object> model = singleton( "content", HtmlMarkup.of( null ) );

        assertEquals( "", render( false, "${content}", model ) );
        assertEquals( "", render( true, "${content}", model ) );
    }

    @Test
    @DisplayName( "ofText() escapes the value identically in both modes" )
    public void ofTextEscapesInBothModes( ) throws Exception
    {
        Map<String, Object> model = singleton( "label", HtmlMarkup.ofText( "Tom & <b>Jerry</b>" ) );

        assertEquals( "Tom &amp; &lt;b&gt;Jerry&lt;/b&gt;", render( false, "${label}", model ) );
        assertEquals( "Tom &amp; &lt;b&gt;Jerry&lt;/b&gt;", render( true, "${label}", model ) );
    }

    @Test
    @DisplayName( "A markup value passed through a macro is not escaped a second time" )
    public void markupSurvivesAMacroBoundary( ) throws Exception
    {
        Map<String, Object> model = singleton( "content", HtmlMarkup.of( RICH_HTML ) );
        String strTemplate = "<#macro box body=''><div<#if body?has_content> class=\"filled\"</#if>>${body}</div></#macro>" + "<@box body=content />";

        String strExpected = "<div class=\"filled\">" + RICH_HTML + "</div>";

        assertEquals( strExpected, render( false, strTemplate, model ) );
        assertEquals( strExpected, render( true, strTemplate, model ) );
    }
}
