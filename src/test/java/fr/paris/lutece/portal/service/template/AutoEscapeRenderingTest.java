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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.util.html.Paginator;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModelEx;

/**
 * Renders, under the HTML auto-escaping, the templates and macros of the core that wrote JavaScript, entities or HTML
 * parameters as they were with the auto-escaping off: they keep the output they had.
 */
public class AutoEscapeRenderingTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String BACK_OFFICE_COMMONS = "commons_bs5_tabler.html";

    /**
     * Builds the engine as AbstractFreeMarkerTemplateService configures it.
     *
     * @param snippets
     *            the templates of the test, looked up before the files
     * @return the configuration
     * @throws IOException
     *             if the templates directory cannot be read
     */
    private static Configuration configuration( StringTemplateLoader snippets ) throws IOException
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_32 );
        cfg.setNumberFormat( "0.######" );
        cfg.setLocalizedLookup( false );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                snippets, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "i18n", (TemplateMethodModelEx) args -> String.valueOf( args.get( 0 ) ) );
        cfg.setSharedVariable( "dskey", (TemplateMethodModelEx) args -> "DS" );
        cfg.setSharedVariable( "htmlAttributes", new HtmlAttributesMethod( true ) );
        return cfg;
    }

    private static String process( Configuration cfg, String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringWriter out = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, out );
        return out.toString( );
    }

    /**
     * Renders a snippet with the back office macros auto-included.
     */
    private static String renderBackOffice( String strSnippet, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", strSnippet );
        Configuration cfg = configuration( snippets );
        cfg.setLazyAutoImports( true );
        cfg.addAutoInclude( BACK_OFFICE_COMMONS );
        return process( cfg, "snippet.ftl", model );
    }

    /**
     * Renders a snippet with every macro of the front office theme.
     */
    private static String renderFrontOffice( String strSnippet, Map<String, Object> model ) throws IOException, TemplateException
    {
        String strIncludes;
        try ( Stream<Path> paths = Files.walk( Path.of( TEMPLATES_ROOT, "skin/themes/macros" ) ) )
        {
            strIncludes = paths.filter( p -> p.toString( ).endsWith( ".ftl" ) ).sorted( )
                    .map( p -> "<#include \"" + Path.of( TEMPLATES_ROOT ).relativize( p ).toString( ).replace( '\\', '/' ) + "\">" )
                    .collect( Collectors.joining( ) );
        }
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", strIncludes + strSnippet );
        return process( configuration( snippets ), "snippet.ftl", model );
    }

    /**
     * The config of the text editor profile is a JSON object written in a script.
     */
    @Test
    public void testEditorProfileConfigIsJavaScript( ) throws Exception
    {
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "admin/util/editor/editor_test.html", "" );
        Configuration cfg = configuration( snippets );
        cfg.setSharedVariable( "richTextEditorProfile",
                (TemplateMethodModelEx) args -> Map.of( "editor", "test", "config", "{\"height\":600,\"x\":\"</script>\"}" ) );

        String strHtml = process( cfg, "admin/util/editor/editor.html", new HashMap<>( ) );

        assertTrue( strHtml.contains( "const profileConfig = {\"height\":600,\"x\":\"\\u003c/script>\"};" ), strHtml );
    }

    /**
     * The HTML inserted into an editor (mode 2) is passed as a JavaScript string, not as text of the page.
     */
    @Test
    public void testInsertIntoElement2PassesTheHtml( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "input", "content" );
        model.put( "insert", "<b>x</b>'" );

        String strHtml = process( configuration( new StringTemplateLoader( ) ), "admin/insert/insert_into_element2.html", model );

        assertTrue( strHtml.contains( "var textToInsert = '<b>x<\\/b>\\''" ), strHtml );
        assertFalse( strHtml.contains( "&lt;b&gt;" ), strHtml );
    }

    /**
     * The link of a shortcut of the home page adds its parameter with one ampersand.
     */
    @Test
    public void testHomeShortcutUrlHasOneAmpersand( ) throws Exception
    {
        String strHome = Files.readString( Path.of( TEMPLATES_ROOT, "admin/themes/tabler/pages/home/adminHome.ftl" ), StandardCharsets.UTF_8 );
        Matcher matcher = Pattern.compile( "<#macro homeShortcutUrl right>.*?</#macro>" ).matcher( strHome );
        assertTrue( matcher.find( ) );
        Map<String, Object> model = new HashMap<>( );
        model.put( "right", Map.of( "externalFeature", false, "url", "jsp/admin/Feature.jsp?a=1", "pluginName", "myplugin" ) );

        String strHtml = renderBackOffice( matcher.group( ) + "<a href=\"<@homeShortcutUrl right />\">", model );

        assertTrue( strHtml.contains( "href=\"jsp/admin/Feature.jsp?a=1&amp;plugin_name=myplugin\"" ), strHtml );
    }

    /**
     * The labels of a paginator, entities in Java, show as arrows.
     */
    @Test
    public void testPaginatorLabelsAreNotEscapedTwice( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "paginator", new Paginator<>( Collections.nCopies( 30, "x" ), 1, "jsp/admin/List.jsp", "page_index", "15" ) );

        String strHtml = renderBackOffice( "<@paginationPageLinks paginator=paginator />", model );

        assertTrue( strHtml.contains( "|&lt;" ), strHtml );
        assertFalse( strHtml.contains( "&amp;lt;" ), strHtml );
        assertFalse( strHtml.contains( "&amp;gt;" ), strHtml );
    }

    /**
     * An SVG icon given to the back office button as a capture is written as markup; a string literal too.
     */
    @Test
    public void testButtonSvgIcon( ) throws Exception
    {
        String strHtml = renderBackOffice( "<#assign svg><svg class=\"i\"></svg></#assign><@button title='T' buttonIcon=svg />", new HashMap<>( ) );
        assertTrue( strHtml.contains( "<svg class=\"i\"></svg>" ), strHtml );

        strHtml = renderBackOffice( "<@button title='T' buttonIcon='<svg class=\"j\"></svg>' />", new HashMap<>( ) );
        assertTrue( strHtml.contains( "<svg class=\"j\"></svg>" ), strHtml );
    }

    /**
     * The values of the consent services are written as JavaScript strings and numbers.
     */
    @Test
    public void testConsentServicesWriteJavaScript( ) throws Exception
    {
        String strHtml = renderFrontOffice( "<@cConsentTacServiceMatomo id='5' host=\"https://stats/?a=1&b='2'\" />", new HashMap<>( ) );
        assertTrue( strHtml.contains( "tarteaucitron.user.matomoId = 5;" ), strHtml );
        assertTrue( strHtml.contains( "tarteaucitron.user.matomoHost = 'https://stats/?a=1&b=\\'2\\'';" ), strHtml );

        strHtml = renderFrontOffice( "<@cConsentOrejimeService code='yt' title=\"L'a\" description='d \"q\"' />", new HashMap<>( ) );
        assertTrue( strHtml.contains( "title: 'L\\'a'," ), strHtml );
        assertTrue( strHtml.contains( "description: \"d \\\"q\\\"\"," ), strHtml );
    }
}
