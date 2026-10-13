/*
 * Copyright (c) 2002-2025, City of Paris
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

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import fr.paris.lutece.portal.service.html.XSSSanitizerException;
import fr.paris.lutece.portal.service.html.XSSSanitizerService;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.portal.service.util.AppLogService;
import freemarker.core.TemplateHTMLOutputModel;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Renders stored rich text content (HTML, or Markdown / BBCode for a parser declaring their prefix) to HTML that can be
 * displayed. It is the single exit point of rich content: whatever its format, the HTML it returns is always
 * sanitized, so a parser only converts and plain HTML no longer bypasses the cleaning (LUT-33153).
 */
public class RichTextContentService
{
    private static List<IRichTextContentParser> _parsers = CDI.current( ).select( IRichTextContentParser.class ).stream( ).collect( Collectors.toList( ) );

    /**
     * Private constructor: static methods only.
     */
    private RichTextContentService( )
    {
    }

    /**
     * Converts a stored rich content to HTML, then sanitizes it. A content no parser claims is HTML.
     *
     * @param content
     *            the stored content, may be null
     * @return the sanitized HTML, empty for null
     * @throws RichTextParsingException
     *             if the parser of the content fails
     */
    public static String getContent( String content ) throws RichTextParsingException
    {
        if ( content == null )
        {
            return "";
        }

        return sanitize( convert( content ) );
    }

    /**
     * Renders a stored rich content for a template: the sanitized HTML as a FreeMarker markup value, written as is by
     * <code>${content}</code>. If the content cannot be converted or sanitized, it is returned escaped: never as raw
     * HTML.
     *
     * @param content
     *            the stored content, may be null
     * @return the markup value, never null
     */
    public static TemplateHTMLOutputModel getMarkup( String content )
    {
        try
        {
            return HtmlMarkup.of( getContent( content ) );
        }
        catch( RichTextParsingException | RuntimeException e )
        {
            AppLogService.error( "Rich text content rendered as text: it could not be converted or sanitized", e );
            return HtmlMarkup.of( StringEscapeUtils.escapeHtml4( content ) );
        }
    }

    /**
     * Converts a content with the parser declaring its prefix, if any.
     *
     * @param content
     *            the stored content
     * @return the HTML
     * @throws RichTextParsingException
     *             if the parser fails
     */
    private static String convert( String content ) throws RichTextParsingException
    {
        for ( IRichTextContentParser parser : _parsers )
        {
            String strPrefix = parser.getPrefix( );
            if ( strPrefix != null && !strPrefix.isEmpty( ) && content.startsWith( strPrefix ) )
            {
                return parser.parseContent( content.substring( strPrefix.length( ) ) );
            }
        }

        return content;
    }

    /**
     * Sanitizes HTML.
     *
     * @param strHtml
     *            the HTML
     * @return the sanitized HTML
     * @throws RichTextParsingException
     *             if the sanitizer fails
     */
    private static String sanitize( String strHtml ) throws RichTextParsingException
    {
        try
        {
            return XSSSanitizerService.sanitize( strHtml );
        }
        catch( XSSSanitizerException e )
        {
            throw new RichTextParsingException( "Unable to sanitize rich text content", e );
        }
    }
}
