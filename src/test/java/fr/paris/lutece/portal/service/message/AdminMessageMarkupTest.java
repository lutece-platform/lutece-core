/*
 * Copyright (c) 2002-2022, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.TemplateModelException;

/**
 * The text of a message handed to a template (LUT-33153): the HTML of the i18n message is kept, an argument is escaped
 * unless it is HTML the application built (MessageHtml).
 */
public class AdminMessageMarkupTest extends LuteceTestCase
{
    private static final String KEY_WITH_HTML = "portal.system.message.confirmToggleCache";

    /**
     * The formatting of the i18n message is kept, an argument is shown as text.
     *
     * @throws TemplateModelException
     *             if the markup cannot be read
     */
    @Test
    public void testMarkupKeepsTheMessageHtmlAndRemovesAScript( ) throws TemplateModelException
    {
        AdminMessage message = new AdminMessage( KEY_WITH_HTML, new Object [ ] {
                "cache\"><img src=x onerror=alert(1)><script>alert(2)</script>"
        }, null, "", "", AdminMessage.TYPE_CONFIRMATION, false );

        String strHtml = HTMLOutputFormat.INSTANCE.getMarkupString( message.getMarkup( Locale.ENGLISH ) );

        assertTrue( strHtml.contains( "<b>" ), strHtml );
        assertFalse( strHtml.contains( "<script" ), strHtml );
        assertFalse( strHtml.contains( "<img" ), strHtml );
        assertTrue( strHtml.contains( "&lt;img" ), strHtml );
    }

    /**
     * A link in an argument a user typed is shown as text: the sanitizer would keep an https link, the escaping does not.
     *
     * @throws TemplateModelException
     *             if the markup cannot be read
     */
    @Test
    public void testLinkInAnArgumentIsText( ) throws TemplateModelException
    {
        String strArgument = "<a href=\"https://example.org/\">click</a>";
        AdminMessage adminMessage = new AdminMessage( KEY_WITH_HTML, new Object [ ] {
                strArgument
        }, null, "", "", AdminMessage.TYPE_CONFIRMATION, false );
        SiteMessage siteMessage = new SiteMessage( KEY_WITH_HTML, new Object [ ] {
                strArgument
        }, null, "", "", SiteMessage.TYPE_INFO, SiteMessage.TYPE_BUTTON_HIDDEN, null, null );

        for ( String strHtml : new String [ ] {
                HTMLOutputFormat.INSTANCE.getMarkupString( adminMessage.getMarkup( Locale.ENGLISH ) ),
                HTMLOutputFormat.INSTANCE.getMarkupString( siteMessage.getMarkup( Locale.ENGLISH ) )
        } )
        {
            assertFalse( strHtml.contains( "<a " ), strHtml );
            assertTrue( strHtml.contains( "&lt;a href" ), strHtml );
        }
    }

    /**
     * A message with an HTML argument is kept in the session: it must be serializable, and its text unchanged.
     *
     * @throws IOException
     *             if the message cannot be serialized
     */
    @Test
    public void testMessageWithAnHtmlArgumentIsSerializable( ) throws IOException
    {
        AdminMessage message = new AdminMessage( KEY_WITH_HTML, new Object [ ] {
                MessageHtml.of( "<ul><li>x</li></ul>" )
        }, null, "", "", AdminMessage.TYPE_CONFIRMATION, false );

        try ( ObjectOutputStream out = new ObjectOutputStream( new ByteArrayOutputStream( ) ) )
        {
            out.writeObject( message );
        }
        assertTrue( message.getText( Locale.ENGLISH ).contains( "<ul><li>x</li></ul>" ) );
    }

    /**
     * A list formatted by AdminMessageService.getFormattedList, passed as an argument, stays a list whose items are text.
     *
     * @throws TemplateModelException
     *             if the markup cannot be read
     */
    @Test
    public void testFormattedListArgumentStaysAList( ) throws TemplateModelException
    {
        MessageHtml list = AdminMessageService.getFormattedList( List.of( "first <i>item</i>", "<svg onload=alert(1)>" ), Locale.ENGLISH );
        AdminMessage message = new AdminMessage( KEY_WITH_HTML, new Object [ ] {
                list
        }, null, "", "", AdminMessage.TYPE_CONFIRMATION, false );

        String strHtml = HTMLOutputFormat.INSTANCE.getMarkupString( message.getMarkup( Locale.ENGLISH ) );

        assertTrue( strHtml.contains( "<li" ), strHtml );
        assertFalse( strHtml.contains( "<svg" ), strHtml );
        assertTrue( strHtml.contains( "&lt;svg" ), strHtml );
    }
}
