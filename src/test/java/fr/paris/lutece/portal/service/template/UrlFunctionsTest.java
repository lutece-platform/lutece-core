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
import java.io.StringWriter;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;

/**
 * Tests the safeUrl and cssUrl functions (LUT-33153), declared for the back office and the front office: a URL read
 * from data is checked before it is written in a link or in CSS, which HTML escaping does not protect (OWASP XSS
 * Prevention Cheat Sheet). Only the FreeMarker engine is needed.
 */
public class UrlFunctionsTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String [ ] DIRECTORIES = {
            "skin/themes/macros/utilities/functions/", "admin/themes/tabler/utilities/functions/"
    };

    /**
     * Renders an expression with both functions included, as the core configures the engine.
     *
     * @param strDirectory
     *            the directory holding the functions
     * @param strExpression
     *            the expression
     * @param strValue
     *            the value bound to url
     * @return the output
     * @throws Exception
     *             if the template fails
     */
    private static String render( String strDirectory, String strExpression, String strValue ) throws Exception
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", "<#include \"" + strDirectory + "safeUrl.ftl\"><#include \"" + strDirectory + "cssUrl.ftl\">" + strExpression );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), snippets
        } ) );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( Map.of( "url", strValue ), out );
        return out.toString( );
    }

    /**
     * Relative, http(s), mailto and tel URLs are kept, escaped as any value.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testSafeUrlKeepsSafeUrls( ) throws Exception
    {
        for ( String strDirectory : DIRECTORIES )
        {
            for ( String strUrl : new String [ ] {
                    "jsp/site/Portal.jsp?page=map", "/site/x", "https://lutece.paris.fr/", "HTTP://example.org", "mailto:a@b.fr", "tel:0102",
                    "Portal.jsp?a=b:c", "#top", ""
            } )
            {
                assertEquals( strUrl, render( strDirectory, "${safeUrl(url)}", strUrl ), strDirectory );
            }
            assertEquals( "a?x=1&amp;y=2", render( strDirectory, "${safeUrl(url)}", "a?x=1&y=2" ), strDirectory );
        }
    }

    /**
     * Other schemes are replaced by the fallback, white space or control characters in the scheme included.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testSafeUrlRefusesScriptSchemes( ) throws Exception
    {
        for ( String strDirectory : DIRECTORIES )
        {
            for ( String strUrl : new String [ ] {
                    "javascript:alert(1)", " JavaScript:alert(1)", "java\tscript:alert(1)", "java\nscript:alert(1)", "data:text/html,x", "vbscript:x"
            } )
            {
                assertEquals( "#", render( strDirectory, "${safeUrl(url)}", strUrl ), strDirectory + " " + strUrl );
                assertEquals( "[]", render( strDirectory, "[${safeUrl(url, '')}]", strUrl ), strDirectory + " " + strUrl );
            }
        }
    }

    /**
     * A markup value (a capture) is returned as is when it is accepted, so it is not escaped twice.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testSafeUrlKeepsACapture( ) throws Exception
    {
        for ( String strDirectory : DIRECTORIES )
        {
            assertEquals( "a?x=1&amp;y=2", render( strDirectory, "<#assign c>${url}</#assign>${safeUrl(c)}", "a?x=1&y=2" ), strDirectory );
            assertEquals( "#", render( strDirectory, "<#assign c>${url}</#assign>${safeUrl(c)}", "javascript:alert(1)" ), strDirectory );
        }
    }

    /**
     * cssUrl keeps a URL that cannot end url("..."), data: URLs included, and refuses any other.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testCssUrl( ) throws Exception
    {
        for ( String strDirectory : DIRECTORIES )
        {
            for ( String strUrl : new String [ ] {
                    "images/logo.png", "https://cdn.example.org/a.jpg?v=2", "data:image/png;base64,iVBORw0KGgo+/="
            } )
            {
                assertEquals( strUrl, render( strDirectory, "${cssUrl(url)}", strUrl ), strDirectory );
            }
            for ( String strUrl : new String [ ] {
                    "x.png\");background:url(//evil", "x.png') ; color:red", "a b.png", "x\\\".png", "<x>"
            } )
            {
                assertEquals( "", render( strDirectory, "${cssUrl(url)}", strUrl ), strDirectory + " " + strUrl );
            }
        }
    }
}
