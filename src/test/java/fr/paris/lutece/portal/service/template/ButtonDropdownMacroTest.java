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
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import freemarker.core.HTMLOutputFormat;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Tests the dropdown menu of the back office button macro: items given as list items are kept in a list, plain links
 * in a div. A list item in a div would close the list item that encloses the menu (a question of the forms question
 * list), and the rest of the list would leave it. Only the FreeMarker engine is needed: no Lutece context.
 */
public class ButtonDropdownMacroTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String BACK_OFFICE_COMMONS = "commons_bs5_tabler.html";
    private static final String SNIPPET_LIST_ITEMS = "<@button title='Actions' id='actions' dropdownMenu=true><li><a class=\"dropdown-item\" href=\"a.jsp\">A</a></li></@button>";
    private static final String SNIPPET_LINKS = "<@button title='Actions' id='actions' dropdownMenu=true><a class=\"dropdown-item\" href=\"a.jsp\">A</a></@button>";

    /**
     * Renders a snippet with the back office macros auto-included, with the HTML auto-escaping the core runs with.
     *
     * @param strSnippet
     *            the snippet
     * @return the rendered HTML
     * @throws IOException
     *             if a template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strSnippet ) throws IOException, TemplateException
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_32 );
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", strSnippet );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), snippets
        } ) );
        cfg.setLocalizedLookup( false );
        cfg.setLazyAutoImports( true );
        cfg.addAutoInclude( BACK_OFFICE_COMMONS );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "htmlAttributes", new HtmlAttributesMethod( true ) );
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( new HashMap<String, Object>( ), out );
        return out.toString( );
    }

    /**
     * Menu items given as list items are written in a ul.
     *
     * @throws IOException
     *             if a template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    @Test
    public void testListItemsStayInAList( ) throws IOException, TemplateException
    {
        String strHtml = render( SNIPPET_LIST_ITEMS );

        assertTrue( strHtml.contains( "<ul class=\"dropdown-menu dropdown-menu-end\" id=\"actions-content\"" ), strHtml );
        assertTrue( strHtml.contains( "<li><a class=\"dropdown-item\" href=\"a.jsp\">A</a></li>" ), strHtml );
        assertFalse( strHtml.contains( "<div class=\"dropdown-menu" ), strHtml );
    }

    /**
     * Menu items given as plain links are written in a div.
     *
     * @throws IOException
     *             if a template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    @Test
    public void testLinksSitInADiv( ) throws IOException, TemplateException
    {
        String strHtml = render( SNIPPET_LINKS );

        assertTrue( strHtml.contains( "<div class=\"dropdown-menu dropdown-menu-end\" id=\"actions-content\"" ), strHtml );
        assertFalse( strHtml.contains( "<ul class=\"dropdown-menu" ), strHtml );
    }
}
