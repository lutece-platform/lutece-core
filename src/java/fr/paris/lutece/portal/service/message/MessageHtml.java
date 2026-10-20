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

import java.io.Serializable;

/**
 * An argument of a message that is HTML the application built, such as the list AdminMessageService.getFormattedList
 * returns. Any other argument of a message is escaped when the message is written in a page; this one is kept as HTML,
 * then sanitized with the whole message. It is serializable: messages are kept in the session.
 */
public final class MessageHtml implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String _strHtml;

    /**
     * Creates an argument.
     *
     * @param strHtml
     *            the HTML
     */
    private MessageHtml( String strHtml )
    {
        _strHtml = strHtml;
    }

    /**
     * Marks HTML the application built as an HTML argument of a message. Never use it on a value a user typed.
     *
     * @param strHtml
     *            the HTML
     * @return the argument
     */
    public static MessageHtml of( String strHtml )
    {
        return new MessageHtml( ( strHtml == null ) ? "" : strHtml );
    }

    /**
     * Returns the HTML.
     *
     * @return the HTML
     */
    public String getHtml( )
    {
        return _strHtml;
    }

    /**
     * Returns the HTML, so that a message formatted as text (getText) is unchanged.
     *
     * @return the HTML
     */
    @Override
    public String toString( )
    {
        return _strHtml;
    }
}
