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
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Rich text editor TinyMCE: language of the editor
 */
public class EditorTinymceLanguageTest extends LuteceTestCase
{
    private static String render( Locale locale )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "commonsGlobalThemeCode", "lutece" );

        return AppTemplateService.getTemplateFromStringFtl( "<#include \"/admin/util/editor/editor_tinymce6.html\" /><@initEditor />", locale, model ).getHtml( );
    }

    /**
     * A back office in English gives an English editor, in French a French one
     */
    @Test
    public void testLanguageOfTheEditor( )
    {
        String strEnglish = render( Locale.ENGLISH );
        assertTrue( strEnglish.contains( "language: 'en'" ), strEnglish );
        assertFalse( strEnglish.contains( "langs/fr_FR.js" ), strEnglish );
        assertTrue( strEnglish.contains( "Paragraph=p" ), strEnglish );

        String strFrench = render( Locale.FRENCH );
        assertTrue( strFrench.contains( "language: 'fr_FR'" ), strFrench );
        assertTrue( strFrench.contains( "langs/fr_FR.js" ), strFrench );
    }
}
