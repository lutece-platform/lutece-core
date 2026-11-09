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
 * The front office check box and radio macros render under the HTML output format: their input type is a string, not a
 * captured markup that cFormCheck could not compare.
 */
public class CheckRadioMacroTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";

    /**
     * Renders a snippet with every macro of the front office theme.
     *
     * @param strSnippet
     *            the snippet
     * @return the output
     * @throws IOException
     *             if a template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strSnippet ) throws IOException, TemplateException
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
        snippets.putTemplate( "snippet.ftl", strIncludes + strSnippet );
        cfg.setLocalizedLookup( false );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), snippets
        } ) );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "i18n", (TemplateMethodModelEx) args -> String.valueOf( args.get( 0 ) ) );
        cfg.setSharedVariable( "dskey", (TemplateMethodModelEx) args -> "DS" );
        cfg.setSharedVariable( "htmlAttributes", new HtmlAttributesMethod( true ) );
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( new HashMap<String, Object>( ), out );
        return out.toString( );
    }

    /**
     * A check box renders an input of type checkbox, a button style and a required check box too.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testCheckbox( ) throws Exception
    {
        assertTrue( render( "<@cCheckbox name='terms' label='Terms' />" ).contains( "type=\"checkbox\"" ) );
        assertTrue( render( "<@cCheckbox name='terms' label='Terms' type='button' />" ).contains( "type=\"checkbox\"" ) );
        // a required check box compares its type with 'radio' (terms of service of forms and access control)
        assertTrue( render( "<@cCheckbox name='terms' label='Terms' required=true />" ).contains( "aria-required=\"true\"" ) );
    }

    /**
     * A radio renders an input of type radio, a button style and a required radio too.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testRadio( ) throws Exception
    {
        assertTrue( render( "<@cRadio name='choice' label='Yes' value='y' />" ).contains( "type=\"radio\"" ) );
        assertTrue( render( "<@cRadio name='choice' label='Yes' value='y' type='button' />" ).contains( "type=\"radio\"" ) );
        assertTrue( render( "<@cRadio name='choice' label='Yes' value='y' required=true />" ).contains( "type=\"radio\"" ) );
    }
}
