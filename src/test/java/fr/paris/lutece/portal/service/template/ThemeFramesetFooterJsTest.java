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

import java.util.HashMap;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Footer scripts of the front office frameset
 */
public class ThemeFramesetFooterJsTest extends LuteceTestCase
{
    private static final String KEY_BROWSER_WARNING = "portal.theme.site_property.bannerBrowserWarning.checkbox";

    /**
     * The old browser banner does not redefine the XSS characters of the forms (they are defined by page_frameset.html)
     */
    @Test
    public void testBrowserWarningKeepsTheXssCharacters( )
    {
        String strSaved = DatastoreService.getDataValue( KEY_BROWSER_WARNING, "0" );

        try
        {
            DatastoreService.setDataValue( KEY_BROWSER_WARNING, "1" );

            String strHtml = AppTemplateService.getTemplate( "skin/site/theme_frameset_footer_js.html", Locale.FRENCH, new HashMap<String, Object>( ) )
                    .getHtml( );

            assertTrue( strHtml.contains( "bowser.getParser" ), strHtml );
            assertFalse( strHtml.contains( "var xssChars" ), strHtml );
            assertFalse( strHtml.contains( "DS Value Missing" ), strHtml );
        }
        finally
        {
            DatastoreService.setDataValue( KEY_BROWSER_WARNING, strSaved );
        }
    }
}
