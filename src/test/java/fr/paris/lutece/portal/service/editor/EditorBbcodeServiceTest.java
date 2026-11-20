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
package fr.paris.lutece.portal.service.editor;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * EditorBbcodeService Test Class
 */
public class EditorBbcodeServiceTest extends LuteceTestCase
{
    private static final String ERROR = "Error occurred";

    @Inject
    private IEditorBbcodeService _editorBbcodeService;

    /**
     * An address holding ":p" (mailto:paul) is not turned into an emoticon: the link is kept
     */
    @Test
    public void testNoEmoticonInsideAnAddress( )
    {
        String strHtml = _editorBbcodeService.parse( "Contact : [Paul](mailto:paul@example.org) et [Jean](mailto:jean@example.org)" );

        assertFalse( strHtml.contains( ERROR ), strHtml );
        assertTrue( strHtml.contains( "href=\"mailto:paul@example.org\"" ), strHtml );
        assertTrue( strHtml.contains( "href=\"mailto:jean@example.org\"" ), strHtml );
    }

    /**
     * A "$" in a BBCode parameter is text, not a group reference
     */
    @Test
    public void testDollarInParameter( )
    {
        String strHtml = _editorBbcodeService.parse( "[list=$]\n[*]a\n[/list]" );

        assertFalse( strHtml.contains( ERROR ), strHtml );
    }

    /**
     * Emoticons are text: no image is substituted (the images of the former emoticons are not in the webapp)
     */
    @Test
    public void testNoEmoticonReplacement( )
    {
        String strHtml = _editorBbcodeService.parse( "Hello :p :) ;) :D" );

        assertFalse( strHtml.contains( "<img" ), strHtml );
        assertTrue( strHtml.contains( "Hello :p :) ;) :D" ), strHtml );
    }
}
