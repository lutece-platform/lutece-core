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

import jakarta.enterprise.inject.spi.CDI;


/**
 * Service pour gérer le contenu de texte enrichi.
 */
public class RichTextContentService
{

    private static List<IRichTextContentParser> _parsers = CDI.current().select(IRichTextContentParser.class).stream().collect(Collectors.toList());

    /**
     * Récupère le contenu en fonction de son type (Markdown, BBCode ou texte brut).
     *
     * @param content
     *            Le contenu à traiter.
     * @return Le contenu converti en HTML si nécessaire, ou le contenu original.
     */
    public static String getContent( String content ) throws RichTextParsingException
    {
        if ( content == null )
        {
            return "";
        }

        for ( IRichTextContentParser _parser : _parsers )
        {
            if ( content.length( ) >= _parser.getPrefix( ).length( ) && content.startsWith( _parser.getPrefix( ) ) )
            {
                return _parser.parseContent( content.substring( _parser.getPrefix( ).length( ) ) );
            }
        }
        return content;
    }
}
