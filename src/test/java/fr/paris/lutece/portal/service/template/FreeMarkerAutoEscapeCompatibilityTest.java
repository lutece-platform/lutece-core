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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Guards the compatibility of the core FreeMarker templates with BOTH values of the
 * <code>service.freemarker.templateAutoEscape</code> property (see LUT-33153).
 *
 * <p>
 * The property is read by <code>FreeMarkerTemplateService</code> and defaults to <code>false</code>. When it is
 * <code>true</code>, <code>AbstractFreeMarkerTemplateService</code> installs
 * <code>HTMLOutputFormat.INSTANCE</code> plus <code>ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY</code>. That single switch
 * changes the type produced by block-capture assignments (<code>&lt;#assign x&gt;...&lt;/#assign&gt;</code>) from
 * <code>String</code> to markup output, and makes a family of built-ins illegal in one mode or the other.
 * </p>
 *
 * <p>
 * Two guarantees are enforced here:
 * </p>
 * <ol>
 * <li>{@link #everyCoreTemplateParsesInBothModes()} — every template shipped in <code>webapp/WEB-INF/templates</code>
 * must PARSE under both configurations. This catches <code>?no_esc</code> / <code>?esc</code> (ParseException when
 * auto-escaping is off) and <code>?html</code> / <code>?xhtml</code> (ParseException when it is on) across the whole
 * tree, at build time, with no per-template work.</li>
 * <li>The idiom tests — the vocabulary that plugins are asked to use must render identically in both modes.</li>
 * </ol>
 *
 * <p>
 * This test deliberately does not extend <code>LuteceTestCase</code>: it needs no container, no datasource and no
 * Lutece context, only the FreeMarker engine, so that it stays fast enough to run on every build.
 * </p>
 */
public class FreeMarkerAutoEscapeCompatibilityTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";

    /** Built-ins that are a ParseException when auto-escaping is OFF (the output format is not a markup one). */
    private static final String [ ] BANNED_WHEN_OFF = {
            "?no_esc", "?esc"
    };

    /** Built-ins that are a ParseException when auto-escaping is ON (legacy escaping). */
    private static final String [ ] BANNED_WHEN_ON = {
            "?html", "?xhtml"
    };

    // ------------------------------------------------------------------------------------------------
    // Engine plumbing — mirrors AbstractFreeMarkerTemplateService.buildConfiguration()
    // ------------------------------------------------------------------------------------------------

    private static Configuration newConfiguration( boolean bAutoEscape )
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_31 );
        cfg.setTemplateLoader( new StringTemplateLoader( ) );
        cfg.setLocalizedLookup( false );

        if ( bAutoEscape )
        {
            cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
            cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        }

        return cfg;
    }

    /**
     * Renders a set of templates, the first one being the entry point.
     *
     * @param bAutoEscape
     *            the mode to render under
     * @param model
     *            the data model
     * @param namesAndSources
     *            name, source, name, source...
     * @return the rendered output, trimmed
     */
    private static String render( boolean bAutoEscape, Map<String, Object> model, String... namesAndSources ) throws Exception
    {
        StringTemplateLoader loader = new StringTemplateLoader( );

        for ( int i = 0; i < namesAndSources.length; i += 2 )
        {
            loader.putTemplate( namesAndSources [i], namesAndSources [i + 1] );
        }

        Configuration cfg = newConfiguration( bAutoEscape );
        cfg.setTemplateLoader( loader );

        StringWriter out = new StringWriter( );
        cfg.getTemplate( namesAndSources [0] ).process( model == null ? new HashMap<>( ) : model, out );

        return out.toString( ).trim( );
    }

    /**
     * Asserts that a template set renders to the very same output in both modes, and returns that output.
     */
    private String assertSameInBothModes( String strMessage, Map<String, Object> model, String... namesAndSources ) throws Exception
    {
        String strOff = render( false, model, namesAndSources );
        String strOn = render( true, model, namesAndSources );

        assertEquals( strOff, strOn, strMessage + " — rendering differs between autoescape=false and autoescape=true" );

        return strOff;
    }

    private static Map<String, Object> model( Object... keysAndValues )
    {
        Map<String, Object> model = new HashMap<>( );

        for ( int i = 0; i < keysAndValues.length; i += 2 )
        {
            model.put( (String) keysAndValues [i], keysAndValues [i + 1] );
        }

        return model;
    }

    // ------------------------------------------------------------------------------------------------
    // 1. The whole template tree must parse under both configurations
    // ------------------------------------------------------------------------------------------------

    @Test
    @DisplayName( "Every core template parses with auto-escaping both on and off" )
    public void everyCoreTemplateParsesInBothModes( ) throws IOException
    {
        List<Path> listTemplates = listCoreTemplates( );

        assertTrue( listTemplates.size( ) > 100,
                "Expected to find the core template tree under " + TEMPLATES_ROOT + ", found " + listTemplates.size( ) + " files" );

        List<String> listFailures = new ArrayList<>( );

        for ( Path path : listTemplates )
        {
            String strSource = new String( Files.readAllBytes( path ), StandardCharsets.UTF_8 );
            String strName = path.toString( );

            for ( boolean bAutoEscape : new boolean [ ] {
                    false, true
            } )
            {
                try
                {
                    new Template( strName, strSource, newConfiguration( bAutoEscape ) );
                }
                catch( Exception e )
                {
                    listFailures.add( strName + " [autoEscape=" + bAutoEscape + "] : " + firstLine( e.getMessage( ) ) );
                }
            }
        }

        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " template(s) do not parse in both auto-escaping modes:\n  " + String.join( "\n  ", listFailures ) );
        }
    }

    @Test
    @DisplayName( "No template uses a built-in that is illegal in one of the two auto-escaping modes" )
    public void noModeSpecificBuiltInIsUsed( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );

        for ( Path path : listCoreTemplates( ) )
        {
            String strSource = new String( Files.readAllBytes( path ), StandardCharsets.UTF_8 );

            for ( String strBuiltIn : BANNED_WHEN_OFF )
            {
                if ( strSource.contains( strBuiltIn ) )
                {
                    listFailures.add( path + " uses " + strBuiltIn + " — ParseException when auto-escaping is off; use <#noautoesc>${x}</#noautoesc>" );
                }
            }

            for ( String strBuiltIn : BANNED_WHEN_ON )
            {
                if ( strSource.contains( strBuiltIn ) )
                {
                    listFailures.add(
                            path + " uses " + strBuiltIn + " — ParseException when auto-escaping is on; use <#outputformat \"HTML\">${x}</#outputformat>" );
                }
            }
        }

        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " mode-specific built-in usage(s):\n  " + String.join( "\n  ", listFailures ) );
        }
    }

    private static List<Path> listCoreTemplates( ) throws IOException
    {
        Path root = Paths.get( TEMPLATES_ROOT );

        if ( !Files.isDirectory( root ) )
        {
            root = Paths.get( "." + File.separator + TEMPLATES_ROOT );
        }

        try ( Stream<Path> stream = Files.walk( root ) )
        {
            return stream.filter( Files::isRegularFile ).filter( p -> {
                String strName = p.getFileName( ).toString( );
                return strName.endsWith( ".ftl" ) || strName.endsWith( ".html" );
            } ).collect( Collectors.toList( ) );
        }
    }

    private static String firstLine( String strMessage )
    {
        if ( strMessage == null )
        {
            return "(no message)";
        }

        String strFirst = strMessage.split( "\n" ) [0].trim( );

        return strFirst.length( ) > 160 ? strFirst.substring( 0, 160 ) + "..." : strFirst;
    }

    // ------------------------------------------------------------------------------------------------
    // 2. The bi-compatible vocabulary renders identically in both modes
    // ------------------------------------------------------------------------------------------------

    @Test
    @DisplayName( "<#noautoesc> leaves the value unescaped in both modes" )
    public void noAutoEscDirectiveIsBiCompatible( ) throws Exception
    {
        String strOutput = assertSameInBothModes( "<#noautoesc>", model( "html", "<p>rich</p>" ), "t.ftl", "<#noautoesc>${html}</#noautoesc>" );

        assertEquals( "<p>rich</p>", strOutput );
    }

    @Test
    @DisplayName( "<#outputformat \"HTML\"> escapes explicitly in both modes" )
    public void outputFormatDirectiveIsBiCompatible( ) throws Exception
    {
        String strOutput = assertSameInBothModes( "<#outputformat>", model( "u", "Tom & <b>J</b>" ), "t.ftl",
                "<#outputformat \"HTML\">${u}</#outputformat>" );

        assertEquals( "Tom &amp; &lt;b&gt;J&lt;/b&gt;", strOutput );
    }

    @Test
    @DisplayName( "?has_content behaves like != '' on strings and also works on captured blocks" )
    public void hasContentIsBiCompatible( ) throws Exception
    {
        String strOutput = assertSameInBothModes( "?has_content", model( "full", "abc", "empty", "" ), "t.ftl",
                "<#macro t><#local cap>x</#local><#local ws>   </#local>" + "[${full?has_content?c}|${( full != '' )?c}]" + "[${empty?has_content?c}|${( empty != '' )?c}]"
                        + "[${cap?has_content?c}][${ws?has_content?c}]</#macro><@t/>" );

        // full -> true, empty -> false, captured block -> true, whitespace-only -> true
        assertEquals( "[true|true][false|false][true][true]", strOutput );
    }

    @Test
    @DisplayName( "A captured block passed as an attribute bundle keeps the delimiting quotes in both modes" )
    public void capturedAttributeBundleIsBiCompatible( ) throws Exception
    {
        Map<String, Object> model = model( "u", "Tom & <b>J</b>" );
        String [ ] templates = {
                "caller.ftl", "<#import 'btn.ftl' as c><#macro t><#local p>title=\"${u}\"</#local><@c.button params=p /></#macro><@t/>", "btn.ftl",
                "<#macro button params=''><btn<#if params?has_content> ${params}</#if>></#macro>"
        };

        // The markup structure is identical in both modes: the delimiting quotes survive. Only the interpolated
        // value differs, and that difference is exactly what auto-escaping is for.
        assertEquals( "<btn title=\"Tom & <b>J</b>\">", render( false, model, templates ) );
        assertEquals( "<btn title=\"Tom &amp; &lt;b&gt;J&lt;/b&gt;\">", render( true, model, templates ) );
    }

    @Test
    @DisplayName( "A string literal attribute bundle loses its delimiting quotes — the capture form does not" )
    public void capturedAttributeBundleBeatsStringLiteral( ) throws Exception
    {
        Map<String, Object> model = model( "u", "v" );
        String strMacro = "<#macro button params=''><btn<#if params?has_content> ${params}</#if>></#macro>";

        String strLiteral = render( true, model, "caller.ftl",
                "<#import 'btn.ftl' as c><#macro t><#local p = 'title=\"${u}\"'><@c.button params=p /></#macro><@t/>", "btn.ftl", strMacro );
        String strCapture = render( true, model, "caller.ftl",
                "<#import 'btn.ftl' as c><#macro t><#local p>title=\"${u}\"</#local><@c.button params=p /></#macro><@t/>", "btn.ftl", strMacro );

        assertEquals( "<btn title=&quot;v&quot;>", strLiteral, "string literal form is expected to break the attribute" );
        assertEquals( "<btn title=\"v\">", strCapture, "capture form is expected to keep the attribute intact" );
    }

    @Test
    @DisplayName( "A markup-output value coming from the Java model renders raw in both modes" )
    public void markupOutputFromModelIsBiCompatible( ) throws Exception
    {
        String strHtml = "<p>sanitized <b>content</b></p>";

        String strOutput = assertSameInBothModes( "markup output from model", model( "content", HTMLOutputFormat.INSTANCE.fromMarkup( strHtml ) ), "t.ftl",
                "${content}" );

        assertEquals( strHtml, strOutput );
    }

    @Test
    @DisplayName( "A markup-output value from the model supports ?has_content" )
    public void markupOutputFromModelSupportsHasContent( ) throws Exception
    {
        String strOutput = assertSameInBothModes( "markup output ?has_content",
                model( "content", HTMLOutputFormat.INSTANCE.fromMarkup( "<p>x</p>" ), "blank", HTMLOutputFormat.INSTANCE.fromMarkup( "" ) ), "t.ftl",
                "[${content?has_content?c}][${blank?has_content?c}]" );

        assertEquals( "[true][false]", strOutput );
    }

    // ------------------------------------------------------------------------------------------------
    // 3. The constructs we are migrating away from — kept as executable documentation of WHY
    // ------------------------------------------------------------------------------------------------

    @Test
    @DisplayName( "A String from the model is escaped when auto-escaping is on — this is why the service returns markup" )
    public void stringFromModelIsEscapedWhenOn( ) throws Exception
    {
        Map<String, Object> model = model( "content", "<p>x</p>" );

        assertEquals( "<p>x</p>", render( false, model, "t.ftl", "${content}" ) );
        assertEquals( "&lt;p&gt;x&lt;/p&gt;", render( true, model, "t.ftl", "${content}" ) );
    }

    @Test
    @DisplayName( "A string literal holding attributes gets its delimiting quotes escaped when auto-escaping is on" )
    public void stringLiteralAttributeBundleBreaksWhenOn( ) throws Exception
    {
        Map<String, Object> model = model( "u", "v" );
        String strTemplate = "<#assign p = 'title=\"${u}\"'><btn ${p}>";

        assertEquals( "<btn title=\"v\">", render( false, model, "t.ftl", strTemplate ) );
        assertEquals( "<btn title=&quot;v&quot;>", render( true, model, "t.ftl", strTemplate ) );
    }

    @Test
    @DisplayName( "Comparing a captured block with '' fails when auto-escaping is on" )
    public void comparingCapturedBlockFailsWhenOn( ) throws Exception
    {
        String strTemplate = "<#macro t><#local c>x</#local><#if c != ''>full</#if></#macro><@t/>";

        assertEquals( "full", render( false, null, "t.ftl", strTemplate ) );

        try
        {
            render( true, null, "t.ftl", strTemplate );
            fail( "Expected a template exception when comparing a markup output with a string" );
        }
        catch( Exception e )
        {
            assertTrue( String.valueOf( e.getMessage( ) ).contains( "compare" ),
                    "Expected a comparison error, got: " + firstLine( e.getMessage( ) ) );
        }
    }
}
