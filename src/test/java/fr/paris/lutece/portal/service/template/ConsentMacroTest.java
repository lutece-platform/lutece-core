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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModelEx;

/**
 * The cConsent macro writes the tracking script that the plugin-matomo page include puts in the page model as a
 * script: a String from a plugin not migrated to the auto-escaping yet, or an HtmlMarkup.
 */
public class ConsentMacroTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String SCRIPT = "var u=((\"https:\" == document.location.protocol) ? \"https://stats/\" : \"http://stats/\");";

    /**
     * Renders the consent macro with every macro of the front office theme.
     *
     * @param model
     *            the model
     * @return the output
     * @throws IOException
     *             if a template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( Map<String, Object> model ) throws IOException, TemplateException
    {
        String strIncludes;
        try ( Stream<Path> paths = Files.walk( Path.of( TEMPLATES_ROOT, "skin/themes/macros" ) ) )
        {
            strIncludes = paths.filter( p -> p.toString( ).endsWith( ".ftl" ) ).sorted( )
                    .map( p -> "<#include \"" + Path.of( TEMPLATES_ROOT ).relativize( p ).toString( ).replace( '\\', '/' ) + "\">" )
                    .collect( Collectors.joining( ) );
        }
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_28 );
        // as AbstractFreeMarkerTemplateService configures the engine
        cfg.setNumberFormat( "0.######" );
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", strIncludes + "<@cConsent />" );
        cfg.setLocalizedLookup( false );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), snippets
        } ) );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "i18n", (TemplateMethodModelEx) args -> String.valueOf( args.get( 0 ) ) );
        cfg.setSharedVariable( "dskey", (TemplateMethodModelEx) args -> "DS" );
        cfg.setSharedVariable( "htmlAttributes", new HtmlAttributesMethod( true ) );
        // the theme paths that the page model gives
        model.putIfAbsent( "commonsSharedThemePath", "themes/shared/" );
        model.putIfAbsent( "commonsSiteJsPath", "themes/skin/js/" );
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( model, out );
        return out.toString( );
    }

    /**
     * The matomo script put as a String by the page include is written as a script, not escaped.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testMatomoStringIsWrittenAsScript( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "matomo", SCRIPT );
        String strOutput = render( model );
        assertTrue( strOutput.contains( SCRIPT ), strOutput );
    }

    /**
     * The matomo script put as markup is written as it is.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testMatomoMarkupIsWrittenAsScript( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "matomo", HtmlMarkup.of( SCRIPT ) );
        String strOutput = render( model );
        assertTrue( strOutput.contains( SCRIPT ), strOutput );
    }

    /**
     * Without the matomo include, the consent renders.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testWithoutMatomo( ) throws Exception
    {
        assertTrue( render( new HashMap<>( ) ).contains( "matomohightrack" ) );
    }
}
