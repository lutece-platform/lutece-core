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
package fr.paris.lutece.portal.service.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Rendering of the front office message page
 */
public class SiteMessageTemplateTest extends LuteceTestCase
{
    private static final String TEMPLATE_SITE_MESSAGE = "skin/site/site_message.html";

    private String render( String strTitle )
    {
        Map<String, Object> message = new HashMap<>( );
        message.put( "type", SiteMessage.TYPE_ERROR );

        Map<String, Object> model = new HashMap<>( );
        model.put( "message", message );
        model.put( "text", "Page not found" );
        if ( strTitle != null )
        {
            model.put( "title", strTitle );
        }

        return AppTemplateService.getTemplate( TEMPLATE_SITE_MESSAGE, Locale.ENGLISH, model ).getHtml( );
    }

    private static int count( String strHtml, String strText )
    {
        return strHtml.split( strText, -1 ).length - 1;
    }

    /**
     * The title of the message is written once, not after the label of its type
     */
    @Test
    public void testTitleWrittenOnce( )
    {
        String strHtml = render( "Bh-title-of-the-message" );

        assertEquals( 1, count( strHtml, "Bh-title-of-the-message" ), strHtml );
        assertFalse( strHtml.contains( "Error Bh-title" ), strHtml );
    }

    /**
     * Without title, the label of the type is the title
     */
    @Test
    public void testLabelOfTheTypeWithoutTitle( )
    {
        String strHtml = render( null );

        assertTrue( strHtml.contains( "Error" ), strHtml );
    }
}
