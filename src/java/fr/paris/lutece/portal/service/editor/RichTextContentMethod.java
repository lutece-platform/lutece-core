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
package fr.paris.lutece.portal.service.editor;

import java.util.List;

import freemarker.core.HTMLOutputFormat;
import freemarker.core.TemplateHTMLOutputModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * The <code>richText( content )</code> function of the templates: renders a stored rich content (HTML, or a format a
 * parser declares) to sanitized HTML, as a markup value the template writes as is: <code>${richText( entry.value )}</code>.
 * It is the way to display rich content under HTML auto-escaping, instead of <code>?no_esc</code> on a value nobody has
 * cleaned.
 */
public class RichTextContentMethod implements TemplateMethodModelEx
{
    /**
     * Renders the content given as single argument.
     *
     * @param arguments
     *            the content to render, a string or a captured block (null or missing renders nothing)
     * @return the sanitized HTML as markup
     * @throws TemplateModelException
     *             if the argument is not a string
     */
    @Override
    public Object exec( @SuppressWarnings( "rawtypes" ) List arguments ) throws TemplateModelException
    {
        if ( arguments.size( ) != 1 )
        {
            throw new TemplateModelException( "richText( content ) takes one argument" );
        }

        Object argument = arguments.get( 0 );
        if ( argument == null )
        {
            return RichTextContentService.getMarkup( null );
        }
        if ( argument instanceof TemplateHTMLOutputModel )
        {
            return RichTextContentService.getMarkup( HTMLOutputFormat.INSTANCE.getMarkupString( (TemplateHTMLOutputModel) argument ) );
        }
        if ( !( argument instanceof TemplateScalarModel ) )
        {
            throw new TemplateModelException( "richText( content ) expects a string, not " + ( (TemplateModel) argument ).getClass( ).getSimpleName( ) );
        }

        return RichTextContentService.getMarkup( ( (TemplateScalarModel) argument ).getAsString( ) );
    }
}
