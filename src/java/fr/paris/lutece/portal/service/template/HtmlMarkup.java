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
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Turns a Java-side HTML string into a FreeMarker <em>markup output</em> value.
 *
 * <p>
 * This is the supported way of putting HTML built on the Java side into a template model. A plain
 * <code>String</code> placed in the model is escaped by FreeMarker when
 * <code>service.freemarker.templateAutoEscape</code> is <code>true</code>, and the markup ends up displayed as text on
 * the page. The historical workaround, <code>${x?no_esc}</code>, is not an option: it is a <em>parse</em> error when the
 * property is <code>false</code>, so a template using it no longer loads on a non-switched instance.
 * </p>
 *
 * <p>
 * A markup output value solves both problems at once. It renders identically in both modes:
 * </p>
 *
 * <pre>
 *                  value put in the model as String    value put in the model with HtmlMarkup
 *   autoEscape off  &lt;p&gt;rich&lt;/p&gt;                        &lt;p&gt;rich&lt;/p&gt;
 *   autoEscape on   &amp;lt;p&amp;gt;rich&amp;lt;/p&amp;gt;            &lt;p&gt;rich&lt;/p&gt;
 * </pre>
 *
 * <p>
 * The template simply writes <code>${content}</code> — no <code>?no_esc</code>, no <code>&lt;#noautoesc&gt;</code> — and
 * <code>?has_content</code> keeps working on the value.
 * </p>
 *
 * <p>
 * Security note: a markup output value tells FreeMarker &laquo; this is already safe HTML &raquo;. Use
 * {@link #ofSanitized(String)} for anything that originates from a user; {@link #of(String)} is reserved for markup
 * built by the application itself (page assembly, portlet rendering, a value that has already been sanitized upstream).
 * </p>
 *
 * @since 8.0.2
 */
public final class HtmlMarkup
{
    private HtmlMarkup( )
    {
    }

    /**
     * Wraps HTML that the application itself produced, and that must therefore be rendered as-is.
     *
     * <p>
     * Do not call this on user input. The returned value bypasses auto-escaping by design.
     * </p>
     *
     * @param strHtml
     *            the HTML markup, may be <code>null</code>
     * @return a markup output value, never <code>null</code>
     */
    public static TemplateModel of( String strHtml )
    {
        try
        {
            return HTMLOutputFormat.INSTANCE.fromMarkup( strHtml == null ? "" : strHtml );
        }
        catch( TemplateModelException e )
        {
            // HTMLOutputFormat never rejects a markup string; this cannot happen in practice
            throw new AppException( "Unable to build an HTML markup value", e );
        }
    }

    /**
     * Sanitizes untrusted HTML, then wraps the result.
     *
     * <p>
     * This is the safe entry point for rich content coming from an editor or from any other non-trusted source: the
     * markup is passed through {@link XSSSanitizerService} first, so what reaches the page is both unescaped and clean.
     * </p>
     *
     * @param strHtml
     *            the untrusted HTML markup, may be <code>null</code>
     * @return a markup output value holding the sanitized HTML, never <code>null</code>
     * @throws XSSSanitizerException
     *             if the sanitizer fails
     */
    public static TemplateModel ofSanitized( String strHtml ) throws XSSSanitizerException
    {
        if ( strHtml == null )
        {
            return of( "" );
        }

        return of( XSSSanitizerService.sanitize( strHtml ) );
    }

    /**
     * Escapes a plain-text value and wraps the result, so that it renders identically in both auto-escaping modes.
     *
     * <p>
     * Useful when Java code must hand a template a value that the template will print without knowing which mode it
     * runs in. In practice templates should just print the raw <code>String</code> and let auto-escaping do its job;
     * this method exists for the cases where the value crosses a macro boundary that would otherwise escape it twice.
     * </p>
     *
     * @param strText
     *            the plain text, may be <code>null</code>
     * @return a markup output value holding the escaped text, never <code>null</code>
     */
    public static TemplateModel ofText( String strText )
    {
        try
        {
            return HTMLOutputFormat.INSTANCE.fromPlainTextByEscaping( strText == null ? "" : strText );
        }
        catch( TemplateModelException e )
        {
            // HTMLOutputFormat never rejects a plain text value; this cannot happen in practice
            throw new AppException( "Unable to escape a text value into an HTML markup value", e );
        }
    }
}
