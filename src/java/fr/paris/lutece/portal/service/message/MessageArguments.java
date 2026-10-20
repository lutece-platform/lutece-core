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

import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import freemarker.core.HTMLOutputFormat;
import freemarker.core.TemplateHTMLOutputModel;

/**
 * Formats the text of a message for a page (LUT-33153): the arguments are escaped, but the HTML ones (MessageHtml), so
 * that a value a user typed is shown as text and never as a link or a tag; the i18n text of the message, which may hold
 * formatting, is sanitized with the result.
 */
final class MessageArguments
{
    /**
     * Static class.
     */
    private MessageArguments( )
    {
    }

    /**
     * Returns the text of a message as markup for a page.
     *
     * @param strTextKey
     *            the i18n key of the text
     * @param args
     *            the arguments, or null
     * @param locale
     *            the locale
     * @return the sanitized text, with its arguments escaped
     */
    static TemplateHTMLOutputModel toMarkup( String strTextKey, Object [ ] args, Locale locale )
    {
        String strText = I18nService.getLocalizedString( strTextKey, locale );
        if ( args != null )
        {
            strText = MessageFormat.format( strText, escape( args ) );
        }
        return HtmlMarkup.ofSanitized( strText );
    }

    /**
     * Escapes the arguments of a message: a MessageHtml is kept as its HTML, a number or a date is kept for the formats
     * of MessageFormat, any other value is escaped as text.
     *
     * @param args
     *            the arguments
     * @return the arguments to format
     */
    static Object [ ] escape( Object [ ] args )
    {
        Object [ ] escaped = new Object [ args.length];
        for ( int i = 0; i < args.length; i++ )
        {
            Object arg = args [i];
            if ( arg == null || arg instanceof Number || arg instanceof Date )
            {
                escaped [i] = arg;
            }
            else
                if ( arg instanceof MessageHtml )
                {
                    escaped [i] = ( (MessageHtml) arg ).getHtml( );
                }
                else
                {
                    escaped [i] = HTMLOutputFormat.INSTANCE.escapePlainText( arg.toString( ) );
                }
        }
        return escaped;
    }
}
