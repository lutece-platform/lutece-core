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
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Guards the HTML auto-escaping of the core templates (LUT-33153): FreeMarker escapes every interpolation, and the
 * templates only opt out of it where the value is known to be safe. Only the FreeMarker engine is needed.
 */
public class FreeMarkerAutoEscapeTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";

    /** Escaping by hand: a ParseException under auto-escaping, or a double escaping. */
    private static final String [ ] LEGACY_ESCAPING = {
            "?html", "?xhtml", "?xml", "?rtf", "?web_safe", "<#escape", "<#noescape", "<#noautoesc", "<#outputformat", "auto_esc=false"
    };

    /**
     * A variable assigned a string that holds a value: a literal interpolating it, the value converted by ?string, or a
     * concatenation (+, +=). A map, or a merge of maps ({...} outside an interpolation), is not a string.
     */
    private static final Pattern STRING_INTERPOLATION = Pattern.compile(
            "<#(?:assign|local|global)\\s+(\\w+)\\s*(?![^>]*(?<!\\$)\\{)(?:\\+=|=\\s*(?:[^>]*[\"'][^\"'>]*\\$\\{|[^>]*\\)\\?string\\b|[^>]*\\+))" );

    /** ?js_string?no_esc, right inside a script only: in HTML text or in an attribute, the value would not be escaped. */
    private static final Pattern JS_NO_ESC = Pattern.compile( "\\?js_string\\?no_esc" );

    /** A script element. */
    /**
     * An argument named like params (params, titleParams, additionalParameters...) given a string literal. searchParams
     * of cMainNav is not a set of attributes but the hidden fields of the search form.
     */
    private static final Pattern PARAMS_STRING = Pattern.compile( "\\b(?!searchParams\\b)(\\w*[Pp]arams\\w*|additionalParameters)\\s*=\\s*[\"']" );
    /** A captured block: <#assign name>...</#assign>, or #local, #global. */
    private static final Pattern CAPTURE = Pattern.compile( "<#(?:assign|local|global)\\s+(\\w+)\\s*>" );
    /** An event handler attribute, written in HTML, in a params string or capture, or as a key of an attrs map. */
    private static final Pattern EVENT_HANDLER = Pattern.compile( "(?i)(?<![\\w.-])on[a-z]+\\s*=\\s*[\"'\\\\]|['\"]on[a-z]+['\"]\\s*:\\s*['\"]" );
    /** A javascript: URL. */
    private static final Pattern JAVASCRIPT_URL = Pattern.compile( "(?i)javascript\\s*:" );
    /** The comments and the conditions of the FreeMarker directives, which write nothing. */
    private static final Pattern NOT_WRITTEN = Pattern.compile( "(?s)<#--.*?-->|<#(?:if|elseif)\\b[^>]*>" );
    private static final Pattern SCRIPT = Pattern.compile( "(?is)<script\\b.*?</script>" );

    /** Templates written into a script by their caller, where ?js_string?no_esc is right outside a script element. */
    private static final String [ ] SCRIPT_SNIPPETS = {
            // macros writing a piece of JavaScript, called inside a <script> element
            "admin/themes/tabler/javascript/charcounter/charCounter.ftl", "skin/themes/macros/components/javascript/charCounter.ftl",
            "skin/themes/macros/components/title/cSetPageTitle.ftl", "admin/util/calendar/macro_datetimepicker.html",
            "skin/themes/macros/forms/layout/cForm.ftl",
    };

    /** An HTML start tag; FreeMarker tags and quoted values are read as a whole. */
    private static final Pattern HTML_TAG = Pattern.compile( "<[a-zA-Z][\\w:-]*((?:[^<>\"']|\"[^\"]*\"|'[^']*'|<#[^>]*>|</#[^>]*>|\\$\\{[^}]*\\})*)>" );

    /** An attribute whose value is an interpolation without quotes, outside any quoted value. */
    private static final Pattern UNQUOTED_VALUE = Pattern.compile( "(?<![\\w:-])[\\w:-]+=\\$\\{(?![^}]*\\?c\\})" );

    private static final Pattern NO_ESC = Pattern.compile( "\\$\\{([^{}]*)\\?no_esc\\}|=\\s*\\(?\\s*([\\w.'()!]+?)\\s*\\)?\\?no_esc\\b" );

    /**
     * The values a template may write unescaped, each for a stated reason. A new ?no_esc must match one of them, or be
     * added here with its reason: it is the list a security review reads.
     */
    private static final Pattern [ ] UNESCAPED_ALLOWED = {
            // a value JavaScript-escaped for a <script> element, where HTML escaping is wrong
            Pattern.compile( ".*\\?js_string" ),
            // a bundle of attributes written by the template author (params): the data goes to attrs
            Pattern.compile( "params|\\w+Params|additionalParameters" ),
            // HTML, CSS or JavaScript entered in the site properties by an administrator, rendered as such
            Pattern.compile( "logoSvg|lightColors|darkColors|dtThemeOptions|themeEditorConfig|\\(?themeEditorExtraConfig!''\\)?" ),
            Pattern.compile( "dskey\\('[\\w.]+\\.htmlblock'\\)?" ),
            // JavaScript written by the template that calls the macro (macro parameters, not data)
            Pattern.compile( "insert|options|lang" ),
            // i18n messages of the application, which may hold HTML
            Pattern.compile( "groupItem\\.label|groupItem\\.help!''" ),
            // HTML of a user attribute type (core or plugin), rendered by its own template
            Pattern.compile( "attribute\\.getHtml\\w+\\(.*\\)" ),
    };

    /**
     * Lists the template files of the core.
     *
     * @return the templates
     * @throws IOException
     *             if the tree cannot be read
     */
    private static List<Path> templates( ) throws IOException
    {
        try ( Stream<Path> stream = Files.walk( Path.of( TEMPLATES_ROOT ) ) )
        {
            return stream.filter( p -> p.toString( ).endsWith( ".html" ) || p.toString( ).endsWith( ".ftl" ) ).sorted( ).collect( Collectors.toList( ) );
        }
    }

    /**
     * Every template parses with HTML auto-escaping, the configuration AbstractFreeMarkerTemplateService builds.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testEveryTemplateParsesWithAutoEscaping( ) throws IOException
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );

        List<Path> listTemplates = templates( );
        assertTrue( listTemplates.size( ) > 400, "core templates not found under " + TEMPLATES_ROOT );

        List<String> listFailures = new ArrayList<>( );
        for ( Path path : listTemplates )
        {
            try
            {
                new Template( path.toString( ), Files.readString( path, StandardCharsets.UTF_8 ), cfg );
            }
            catch( Exception e )
            {
                listFailures.add( path + " : " + e.getMessage( ).split( "\n" ) [0] );
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " template(s) do not parse:\n  " + String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * No template escapes by hand any more.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testNoTemplateEscapesByHand( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            String strSource = Files.readString( path, StandardCharsets.UTF_8 );
            for ( String strLegacy : LEGACY_ESCAPING )
            {
                if ( strSource.contains( strLegacy ) )
                {
                    listFailures.add( path + " uses " + strLegacy );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * Every value written unescaped is one of the cases listed in {@link #UNESCAPED_ALLOWED}.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testEveryUnescapedValueIsListed( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            Matcher matcher = NO_ESC.matcher( Files.readString( path, StandardCharsets.UTF_8 ) );
            while ( matcher.find( ) )
            {
                String strExpression = ( matcher.group( 1 ) != null ? matcher.group( 1 ) : matcher.group( 2 ) ).trim( );
                if ( Stream.of( UNESCAPED_ALLOWED ).noneMatch( p -> p.matcher( strExpression ).matches( ) ) )
                {
                    listFailures.add( path + " : " + matcher.group( ) );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " value(s) written unescaped without a listed reason:\n  " + String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * No attribute bundle written as is (params) is a string interpolating a value: commonAttrs writes params unescaped,
     * so the value must be passed in attrs, or the bundle be a capture (&lt;#assign x&gt;...&lt;/#assign&gt;), whose
     * interpolations are escaped.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testNoParamsInterpolateAValueInAString( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            String strSource = Files.readString( path, StandardCharsets.UTF_8 );
            Matcher matcher = STRING_INTERPOLATION.matcher( strSource );
            while ( matcher.find( ) )
            {
                if ( Pattern.compile( "\\bparams\\s*=\\s*\\(?\\s*" + matcher.group( 1 ) + "\\b" ).matcher( strSource ).find( ) )
                {
                    listFailures.add( path + " : params=" + matcher.group( 1 ) );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * Every interpolated attribute value is quoted (OWASP XSS Prevention Cheat Sheet: "Always surround attribute values
     * with double or single quotation marks"): escaping does not stop a space or an equal sign from adding an
     * attribute to an unquoted value. A number written with ?c is accepted.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testAttributeValuesAreQuoted( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            String strSource = Files.readString( path, StandardCharsets.UTF_8 ).replaceAll( "(?s)<#--.*?-->", "" );
            Matcher tag = HTML_TAG.matcher( strSource );
            while ( tag.find( ) )
            {
                String strTopLevel = tag.group( 1 ).replaceAll( "\"[^\"]*\"|'[^']*'", "\"\"" );
                Matcher unquoted = UNQUOTED_VALUE.matcher( strTopLevel );
                while ( unquoted.find( ) )
                {
                    listFailures.add( path + " : " + tag.group( ).substring( 0, Math.min( 120, tag.group( ).length( ) ) ) );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " unquoted attribute value(s):\n  " + String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * ?js_string?no_esc is only written inside a script element, or in a template that writes a piece of JavaScript
     * listed in SCRIPT_SNIPPETS: in HTML text or in an attribute, a JavaScript-escaped value is not HTML-escaped.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testJavaScriptEscapingOnlyInScripts( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            String strPath = path.toString( ).replace( '\\', '/' );
            if ( Stream.of( SCRIPT_SNIPPETS ).anyMatch( strPath::endsWith ) )
            {
                continue;
            }
            String strSource = Files.readString( path, StandardCharsets.UTF_8 ).replaceAll( "(?s)<#--.*?-->", "" );
            List<int [ ]> listScripts = new ArrayList<>( );
            Matcher script = SCRIPT.matcher( strSource );
            while ( script.find( ) )
            {
                listScripts.add( new int [ ] {
                        script.start( ), script.end( )
                } );
            }
            Matcher js = JS_NO_ESC.matcher( strSource );
            while ( js.find( ) )
            {
                int nPosition = js.start( );
                if ( listScripts.stream( ).noneMatch( range -> range [0] <= nPosition && nPosition < range [1] ) )
                {
                    listFailures.add( path + " : line " + ( strSource.substring( 0, nPosition ).split( "\n", -1 ).length ) );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " ?js_string?no_esc outside a script element:\n  " + String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * No template writes JavaScript in its HTML (LUT-33153, points 7, 15 and 20 of the review): no event handler
     * attribute (onclick, onchange, onerror...), whether written in HTML, in params or in attrs, and no javascript: URL.
     * The behaviours are declared with data-lutece-action, data-lutece-confirm and data-lutece-fallback-src, which
     * lutece-actions.js runs; so a Content Security Policy can forbid inline handlers.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testNoEventHandlerNorJavaScriptUrl( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            String strSource = NOT_WRITTEN.matcher( Files.readString( path, StandardCharsets.UTF_8 ) ).replaceAll( "" );
            for ( Pattern pattern : new Pattern [ ] {
                    EVENT_HANDLER, JAVASCRIPT_URL
            } )
            {
                Matcher matcher = pattern.matcher( strSource );
                while ( matcher.find( ) )
                {
                    listFailures.add( path + " : " + matcher.group( ) );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " event handler(s) or javascript: URL(s), use data-lutece-action instead:\n  "
                    + String.join( "\n  ", listFailures ) );
        }
    }

    /**
     * params is a map (LUT-33387): no argument named like params is given a string literal or a captured block. A
     * string or a markup given as params fails the rendering in htmlAttributes; this guard finds it before.
     *
     * @throws IOException
     *             if a template cannot be read
     */
    @Test
    public void testParamsIsAMap( ) throws IOException
    {
        List<String> listFailures = new ArrayList<>( );
        for ( Path path : templates( ) )
        {
            String strSource = Pattern.compile( "(?s)<#--.*?-->" ).matcher( Files.readString( path, StandardCharsets.UTF_8 ) ).replaceAll( "" );
            Matcher literal = PARAMS_STRING.matcher( strSource );
            while ( literal.find( ) )
            {
                listFailures.add( path + " : " + literal.group( ) );
            }
            Matcher capture = CAPTURE.matcher( strSource );
            while ( capture.find( ) )
            {
                if ( Pattern.compile( "\\b(\\w*[Pp]arams\\w*|additionalParameters)\\s*=\\s*\\(?\\s*" + capture.group( 1 ) + "\\b" ).matcher( strSource ).find( ) )
                {
                    listFailures.add( path + " : captured " + capture.group( 1 ) + " given as params" );
                }
            }
        }
        if ( !listFailures.isEmpty( ) )
        {
            fail( listFailures.size( ) + " params given as a string or a captured block, write a map:\n  " + String.join( "\n  ", listFailures ) );
        }
    }
}
