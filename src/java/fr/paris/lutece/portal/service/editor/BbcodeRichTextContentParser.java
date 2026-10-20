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

import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * The BBCode format of the rich text contents (LUT-33153): a content stored with the prefix of this parser is converted
 * from BBCode to HTML by EditorBbcodeService before richText() sanitizes it. The rendering of a rich text content does
 * not depend on the editor that wrote it: an editor storing another format brings its own parser, declared as a CDI
 * bean, and the templates keep writing <code>${richText( content )}</code>. The parser is inactive while its prefix,
 * the property editors.parser.bbcode.richTextPrefix, is not set: a content without a prefix is HTML.
 */
@ApplicationScoped
public class BbcodeRichTextContentParser implements IRichTextContentParser
{
    private static final String NAME = "bbcode";

    @Inject
    @ConfigProperty( name = "editors.parser.bbcode.richTextPrefix" )
    private Optional<String> _configuredPrefix;

    @Inject
    private IEditorBbcodeService _editorBbcodeService;

    private String _strPrefix;
    private String _strName = NAME;

    /**
     * Reads the prefix from the configuration.
     */
    @PostConstruct
    void init( )
    {
        _strPrefix = _configuredPrefix.orElse( "" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix( )
    {
        return _strPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix( String strPrefix )
    {
        _strPrefix = strPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return _strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Converts BBCode to HTML.
     *
     * @param content
     *            the BBCode, without the prefix
     * @return the HTML, sanitized afterwards by RichTextContentService
     * @throws RichTextParsingException
     *             never: the BBCode parser keeps what it does not recognize as text
     */
    @Override
    public String parseContent( String content ) throws RichTextParsingException
    {
        return _editorBbcodeService.parse( content );
    }
}
