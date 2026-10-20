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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Tests the htmlAttributes( macroAttributes, params ) function (LUT-33387) under the HTML auto-escaping the core runs with
 * (LUT-33153). Only the FreeMarker engine is needed: no Lutece context.
 */
public class HtmlAttributesMethodTest
{
    private static final String [ ] REFUSED_NAMES = {
            "onclick", "OnClick", "ONMOUSEOVER", "href", "src", "action", "formaction", "style", "srcdoc", ":href", "x-on:click", "v-on:click",
            "@click", "hx-on", "xlink:href", "data-bs-sanitize", "data-bs-allow-list", "addons", "x\"y"
    };

    /**
     * Renders a snippet with the function shared as the core shares it.
     *
     * @param bStrict
     *            the mode of the function
     * @param strSnippet
     *            the template
     * @param model
     *            the data model
     * @return the rendering
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( boolean bStrict, String strSnippet, Map<String, Object> model ) throws IOException, TemplateException
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_34 );
        StringTemplateLoader loader = new StringTemplateLoader( );
        loader.putTemplate( "snippet.ftl", strSnippet );
        cfg.setTemplateLoader( loader );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "htmlAttributes", new HtmlAttributesMethod( bStrict ) );
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( model, out );
        return out.toString( );
    }

    /**
     * Renders a snippet with an empty model.
     *
     * @param bStrict
     *            the mode of the function
     * @param strSnippet
     *            the template
     * @return the rendering
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( boolean bStrict, String strSnippet ) throws IOException, TemplateException
    {
        return render( bStrict, strSnippet, new HashMap<>( ) );
    }

    /**
     * Each kind of value is written its own way, each attribute preceded by a space, the id and the class first.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testValuesAreWrittenByType( ) throws Exception
    {
        assertEquals( "<div id=\"main\" class=\"card\" role=\"region\" hidden data-count=\"1000\" data-ratio=\"1.5\" title=\"\">",
                render( true, "<div${htmlAttributes( {'role': 'region', 'id': 'main', 'class': 'card'}, "
                        + "{'hidden': true, 'disabled': false, 'data-count': 1000, 'data-ratio': 1.50, 'title': ''} )}>" ) );
    }

    /**
     * Nothing is written for empty maps, for an empty string as params, and for an empty or missing value of the macro.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNothingIsWrittenForNoAttribute( ) throws Exception
    {
        assertEquals( "<p>", render( true, "<p${htmlAttributes( {} )}>" ) );
        assertEquals( "<p>", render( true, "<p${htmlAttributes( {}, '' )}>" ) );
        assertEquals( "<p>", render( true, "<p${htmlAttributes( {'id': missing!, 'role': '', 'class': ' '}, {} )}>" ) );
        assertEquals( "<p alt=\"\">", render( true, "<p${htmlAttributes( {}, {'alt': missing!} )}>" ) );
    }

    /**
     * A value can never close its attribute nor add another one, a captured markup included.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testValuesAreAlwaysEscaped( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "data", "\" onmouseover=\"alert(1)" );
        model.put( "html", HtmlMarkup.of( "x\" onclick=\"alert(1)" ) );

        String strHtml = render( true, "<#assign capture>A &amp; ${data}</#assign><a${htmlAttributes( {'title': data, 'aria-label': capture, "
                + "'data-x': html} )}>", model );

        assertEquals( "<a title=\"&quot; onmouseover=&quot;alert(1)\" aria-label=\"A &amp; &quot; onmouseover=&quot;alert(1)\" "
                + "data-x=\"x&quot; onclick=&quot;alert(1)\">", strHtml );
    }

    /**
     * A caller can never pass an event handler, a URL, a style, a framework binding nor an unknown name through params: in
     * strict mode the rendering fails, otherwise the attribute is left out.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testParamsNamesOutOfTheAllowListAreRefused( ) throws Exception
    {
        for ( String strName : REFUSED_NAMES )
        {
            String strSnippet = "<a${htmlAttributes( {'" + strName.replace( "\"", "\\\"" ) + "': 'x', 'title': 't'} )}>";
            assertThrows( TemplateException.class, ( ) -> render( true, strSnippet ), strName );
            assertEquals( "<a title=\"t\">", render( false, strSnippet ), strName );
        }
    }

    /**
     * The attributes a caller usually passes are allowed, whatever their case.
     */
    @Test
    public void testUsualParamsNamesAreAllowed( )
    {
        for ( String strName : new String [ ] {
                "aria-label", "aria-hidden", "data-bs-toggle", "data-portlet-id", "title", "name", "value", "role", "tabindex", "autocomplete",
                "enctype", "formnovalidate", "target", "rel", "hidden", "required", "autofocus", "accesskey", "draggable", "class", "Title"
        } )
        {
            assertTrue( HtmlAttributesMethod.isAllowed( strName ), strName );
        }
        assertFalse( HtmlAttributesMethod.isAllowed( "aria-" ) );
        assertFalse( HtmlAttributesMethod.isAllowed( "data-" ) );
    }

    /**
     * The classes of the macro and of the caller are gathered without duplicates; any other attribute both give is the
     * macro's.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testMacroAttributesWinOverParams( ) throws Exception
    {
        String strSnippet = "<div${htmlAttributes( {'class': 'btn btn-primary', 'role': 'status'}, {'class': 'mt-3 btn', 'role': 'alert'} )}>";

        assertThrows( TemplateException.class, ( ) -> render( true, strSnippet ) );
        assertEquals( "<div class=\"btn btn-primary mt-3\" role=\"status\">", render( false, strSnippet ) );
    }

    /**
     * params is a map: a string or a markup bundle of attributes always fails the rendering, whatever the mode.
     */
    @Test
    public void testParamsMustBeAMap( )
    {
        for ( boolean bStrict : new boolean [ ] {
                true, false
        } )
        {
            assertThrows( TemplateException.class, ( ) -> render( bStrict, "<p${htmlAttributes( {}, 'hidden title=\"x\"' )}>" ) );
            assertThrows( TemplateException.class, ( ) -> render( bStrict, "<#assign p>title=\"x\"</#assign><p${htmlAttributes( {}, p )}>" ) );
        }
    }

    /**
     * A value that is neither a string, a number nor a boolean is refused.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testSequenceValueIsRefused( ) throws Exception
    {
        String strSnippet = "<p${htmlAttributes( {'title': ['a', 'b'], 'role': 'note'} )}>";

        assertThrows( TemplateException.class, ( ) -> render( true, strSnippet ) );
        assertEquals( "<p role=\"note\">", render( false, strSnippet ) );
    }
}
