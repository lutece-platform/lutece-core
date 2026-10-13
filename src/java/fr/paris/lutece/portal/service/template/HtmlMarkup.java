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

import fr.paris.lutece.portal.service.html.XSSSanitizerException;
import fr.paris.lutece.portal.service.html.XSSSanitizerService;
import fr.paris.lutece.portal.service.util.AppException;

import freemarker.core.HTMLOutputFormat;
import freemarker.core.TemplateHTMLOutputModel;
import freemarker.template.TemplateModelException;

/**
 * Turns HTML held in a Java String into a FreeMarker HTML markup value.
 * <p>
 * With HTML auto-escaping, a String put in a template model is escaped where the template prints it: right for data,
 * wrong for HTML the application has built (a page body, a portlet column, the content of an admin feature). Put such
 * HTML in the model as a markup value: the template writes <code>${content}</code> and gets it as is, with no
 * <code>?no_esc</code>, and the theme templates that override it need no change either.
 * </p>
 * <p>
 * A markup value tells FreeMarker "this is safe HTML". Use {@link #of(String)} only for HTML the application built from
 * its own templates, whose data was escaped when it was rendered. HTML coming from a user (rich text) goes through
 * {@link #ofSanitized(String)}.
 * </p>
 */
public final class HtmlMarkup
{
    /**
     * Private constructor: static methods only.
     */
    private HtmlMarkup( )
    {
    }

    /**
     * Marks HTML built by the application as markup.
     *
     * @param strHtml
     *            HTML rendered by the application, may be null
     * @return the markup value, empty for null
     */
    public static TemplateHTMLOutputModel of( String strHtml )
    {
        try
        {
            return HTMLOutputFormat.INSTANCE.fromMarkup( strHtml == null ? "" : strHtml );
        }
        catch( TemplateModelException e )
        {
            throw new AppException( "Unable to build an HTML markup value", e );
        }
    }

    /**
     * Sanitizes HTML coming from a user, then marks it as markup.
     *
     * @param strHtml
     *            HTML from a user, may be null
     * @return the sanitized markup value, empty for null
     */
    public static TemplateHTMLOutputModel ofSanitized( String strHtml )
    {
        try
        {
            return of( strHtml == null ? "" : XSSSanitizerService.sanitize( strHtml ) );
        }
        catch( XSSSanitizerException e )
        {
            throw new AppException( "Unable to sanitize HTML", e );
        }
    }
}
