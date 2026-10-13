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
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateMethodModelEx;

/**
 * Renders the cSessionTimeout macro once enabled (LUT-33153): disabled by default, it is not reached by the macro
 * harness, and its URLs, computed by captures, were refused by ?js_string under auto-escaping. Only the FreeMarker
 * engine is needed.
 */
public class SessionTimeoutMacroTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String MACRO = "skin/themes/macros/components/javascript/cSessionTimeout.ftl";
    private static final String KEY_PREFIX = "portal.theme.site_property.sessiontimeout.";

    /**
     * Renders the macro with the given datastore values, as AbstractFreeMarkerTemplateService configures the engine.
     *
     * @param mapDatastore
     *            the datastore values read by dskey( )
     * @param model
     *            the model
     * @return the output
     * @throws Exception
     *             if the template fails
     */
    private static String render( Map<String, String> mapDatastore, Map<String, Object> model ) throws Exception
    {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        StringTemplateLoader snippets = new StringTemplateLoader( );
        snippets.putTemplate( "snippet.ftl", "<#macro deprecatedWarning args></#macro><#include \"" + MACRO + "\"><@cSessionTimeout />" );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), snippets
        } ) );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setSharedVariable( "dskey", (TemplateMethodModelEx) args -> mapDatastore.getOrDefault( String.valueOf( args.get( 0 ) ), "DS" ) );
        cfg.setSharedVariable( "i18n", (TemplateMethodModelEx) args -> String.valueOf( args.get( 0 ) ) );
        StringWriter out = new StringWriter( );
        cfg.getTemplate( "snippet.ftl" ).process( model, out );
        return out.toString( );
    }

    /**
     * Enabled with the default URLs, the macro renders them, JavaScript-escaped.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testDefaultUrls( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "base_url", "http://site/" );
        model.put( "commonsSiteSharedPath", "themes/skin/shared/" );

        String strOutput = render( Map.of( KEY_PREFIX + "enabled.checkbox", "1" ), model );

        assertTrue( strOutput.contains( "keepAliveUrl: \"http://site/jsp/site/Portal.jsp\"" ), strOutput );
        assertTrue( strOutput.contains( "loginUrl: \"jsp/site/Portal.jsp?page=mylutece&action=doLogin\"" ), strOutput );
    }

    /**
     * URLs entered in the site properties cannot end the JavaScript string they are written in.
     *
     * @throws Exception
     *             if the template fails
     */
    @Test
    public void testUrlsAreJavaScriptEscaped( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "urlAuth", "x\"</script><script>alert(1)</script>" );
        model.put( "commonsSiteSharedPath", "" );

        String strOutput = render( Map.of( KEY_PREFIX + "enabled.checkbox", "1", KEY_PREFIX + "keepAliveUrl", "y\";alert(2);//" ), model );

        assertFalse( strOutput.contains( "</script><script>alert(1)" ), strOutput );
        assertFalse( Pattern.compile( "[^\\\\]\";alert\\(2\\)" ).matcher( strOutput ).find( ), strOutput );
        assertTrue( strOutput.contains( "keepAliveUrl: \"y\\\";alert(2);//\"" ), strOutput );
    }
}
