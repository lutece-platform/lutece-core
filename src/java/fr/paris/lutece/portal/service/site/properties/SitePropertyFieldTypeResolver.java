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
package fr.paris.lutece.portal.service.site.properties;

import fr.paris.lutece.portal.web.xss.FieldType;

/**
 * Resolves the {@link FieldType} of a site property from its datastore key, using the suffix convention of the site
 * properties form.
 *
 * <p>
 * Suffix convention (evaluated from the most specific to the most generic):
 * </p>
 * <ul>
 * <li>{@code .htmlblock} or {@code .textblock} &rarr; {@link FieldType#HTML}</li>
 * <li>contains {@code color} &rarr; {@link FieldType#COLOR}</li>
 * <li>{@code _url} or {@code .url} &rarr; {@link FieldType#URL}</li>
 * <li>{@code email} &rarr; {@link FieldType#EMAIL}</li>
 * <li>anything else (including {@code .checkbox}) &rarr; {@link FieldType#TEXT}</li>
 * </ul>
 */
public final class SitePropertyFieldTypeResolver
{
    private static final String SUFFIX_HTMLBLOCK = ".htmlblock";
    private static final String SUFFIX_TEXTBLOCK = ".textblock";
    private static final String TOKEN_COLOR = "color";
    private static final String SUFFIX_URL_UNDERSCORE = "_url";
    private static final String SUFFIX_URL_DOT = ".url";
    private static final String SUFFIX_EMAIL = "email";

    /**
     * Private constructor. Utility class.
     */
    private SitePropertyFieldTypeResolver( )
    {
    }

    /**
     * Resolves the field type of a site property from its datastore key.
     *
     * @param strKey
     *            the full datastore key of the site property
     * @return the resolved field type, {@link FieldType#TEXT} by default when no specific suffix matches or when the
     *         key is null
     */
    public static FieldType fromKey( String strKey )
    {
        if ( strKey == null )
        {
            return FieldType.TEXT;
        }

        if ( strKey.endsWith( SUFFIX_HTMLBLOCK ) || strKey.endsWith( SUFFIX_TEXTBLOCK ) )
        {
            return FieldType.HTML;
        }
        if ( strKey.contains( TOKEN_COLOR ) )
        {
            return FieldType.COLOR;
        }
        if ( strKey.endsWith( SUFFIX_URL_UNDERSCORE ) || strKey.endsWith( SUFFIX_URL_DOT ) )
        {
            return FieldType.URL;
        }
        if ( strKey.endsWith( SUFFIX_EMAIL ) )
        {
            return FieldType.EMAIL;
        }

        return FieldType.TEXT;
    }
}
