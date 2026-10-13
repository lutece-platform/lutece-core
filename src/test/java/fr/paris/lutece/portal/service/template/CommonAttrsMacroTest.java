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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Tests the commonAttrs macro (LUT-33387), declared for the back office and for the front office, with and without
 * auto-escaping (service.freemarker.templateAutoEscape). Only the FreeMarker engine is needed: no Lutece context.
 */
public class CommonAttrsMacroTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String BACK_OFFICE = "admin/themes/tabler/utilities/attributes/commonAttrs.ftl";
    private static final String FRONT_OFFICE = "skin/themes/macros/utilities/functions/commonAttrs.ftl";
    private static final String [ ] DECLARATIONS = {
            BACK_OFFICE, FRONT_OFFICE
    };
    private static final String MACRO_START = "<#macro commonAttrs";

    /**
     * Renders a snippet with commonAttrs auto-included, as AbstractFreeMarkerTemplateService configures the engine.
     */
    private static String render( String strDeclaration, boolean bAutoEscape, String strSnippet ) throws IOException, TemplateException
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", strSnippet );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), snippets
        } ) );
        cfg.setLocalizedLookup( false );
        cfg.addAutoInclude( strDeclaration );
        if ( bAutoEscape )
        {
            cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
            cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        }
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( new HashMap<String, Object>( ), out );
        return out.toString( );
    }

    /**
     * Asserts that a snippet renders the expected output with both declarations and in both escaping modes.
     */
    private static void assertRendersInBothModes( String strExpected, String strSnippet ) throws IOException, TemplateException
    {
        for ( String strDeclaration : DECLARATIONS )
        {
            for ( boolean bAutoEscape : new boolean [ ] {
                    false, true
            } )
            {
                assertEquals( strExpected, render( strDeclaration, bAutoEscape, strSnippet ), strDeclaration + ", autoEscape=" + bAutoEscape );
            }
        }
    }

    /**
     * The back office and front office declarations of the macro must stay identical.
     */
    @Test
    public void testBackAndFrontOfficeDeclarationsAreIdentical( ) throws IOException
    {
        String strBackOffice = Files.readString( Path.of( TEMPLATES_ROOT, BACK_OFFICE ), StandardCharsets.UTF_8 );
        String strFrontOffice = Files.readString( Path.of( TEMPLATES_ROOT, FRONT_OFFICE ), StandardCharsets.UTF_8 );

        assertEquals( strBackOffice.substring( strBackOffice.indexOf( MACRO_START ) ), strFrontOffice.substring( strFrontOffice.indexOf( MACRO_START ) ) );
    }

    /**
     * Empty values write no attribute.
     */
    @Test
    public void testNothingIsWrittenForEmptyValues( ) throws IOException, TemplateException
    {
        assertRendersInBothModes( "", "<@commonAttrs />" );
        assertRendersInBothModes( "", "<@commonAttrs id='' class='' classBase=' ' classExtra='  ' params='' />" );
    }

    /**
     * id, class and params are written in this order, each preceded by a space.
     */
    @Test
    public void testIdClassAndParams( ) throws IOException, TemplateException
    {
        assertRendersInBothModes( " id=\"main\" class=\"row\" data-x", "<@commonAttrs id='main' class='row' params='data-x' />" );
    }

    /**
     * classBase, class and classExtra make one class attribute, in this order, without extra spaces.
     */
    @Test
    public void testClassesAreJoinedInOrder( ) throws IOException, TemplateException
    {
        assertRendersInBothModes( " class=\"btn btn-primary\"", "<@commonAttrs classBase=' btn  btn-primary ' />" );
        assertRendersInBothModes( " class=\"collapse d-flex\"", "<@commonAttrs classExtra=' collapse   d-flex ' />" );
        assertRendersInBothModes( " class=\"card mine collapse\"", "<@commonAttrs classBase='card' class='mine' classExtra=' collapse' />" );
    }

    /**
     * A string or number is written as a value, true as a valueless attribute, false not at all, an empty string as an
     * empty value.
     */
    @Test
    public void testAttrs( ) throws IOException, TemplateException
    {
        assertRendersInBothModes( " aria-hidden=\"true\" data-n=\"1234.5\" disabled alt=\"\"",
                "<@commonAttrs attrs={'aria-hidden': 'true', 'data-n': 1234.5, 'disabled': true, 'hidden': false, 'alt': ''} />" );
    }

    /**
     * A name that is not a valid attribute name is ignored.
     */
    @Test
    public void testAnInvalidAttributeNameIsIgnored( ) throws IOException, TemplateException
    {
        assertRendersInBothModes( " title=\"t\"", "<@commonAttrs attrs={'x onclick=\"y\"': 'z', 'title': 't'} />" );
    }

    /**
     * With auto-escaping, a value cannot close its attribute; without it, the value is written as is, like any other value
     * of the page.
     */
    @Test
    public void testAValueCannotBreakItsAttributeWhenAutoEscaping( ) throws IOException, TemplateException
    {
        String strSnippet = "<#assign value='x\" onmouseover=\"alert(1)' /><@commonAttrs attrs={'title': value} />";
        for ( String strDeclaration : DECLARATIONS )
        {
            assertEquals( " title=\"x&quot; onmouseover=&quot;alert(1)\"", render( strDeclaration, true, strSnippet ), strDeclaration );
            assertEquals( " title=\"x\" onmouseover=\"alert(1)\"", render( strDeclaration, false, strSnippet ), strDeclaration );
        }
    }

    /**
     * With auto-escaping, a markup value (a captured block) is written as is, in class and in attrs.
     */
    @Test
    public void testAMarkupValueIsKeptAsIs( ) throws IOException, TemplateException
    {
        for ( String strDeclaration : DECLARATIONS )
        {
            assertEquals( " class=\"x <b>c</b>\" title=\"a<b>c</b>\"",
                    render( strDeclaration, true, "<#assign c><b>c</b></#assign><#assign t>a<b>c</b></#assign><@commonAttrs classBase='x' class=c attrs={'title': t} />" ),
                    strDeclaration );
        }
    }
}
