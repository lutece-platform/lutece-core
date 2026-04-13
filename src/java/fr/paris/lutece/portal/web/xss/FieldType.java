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
package fr.paris.lutece.portal.web.xss;

/**
 * Semantic type of a user-editable field, used by {@link FieldValidationService} to apply a dedicated validation or
 * sanitization strategy.
 *
 * <p>
 * This type is independent from any particular feature: it can be reused by any form (site properties, plugin
 * configuration, admin forms, etc.) that needs a per-type validation. The mapping between a concrete field or datastore
 * key and its {@link FieldType} is the responsibility of the caller.
 * </p>
 */
public enum FieldType
{
    /** Plain text, left under the responsibility of the global XSS sanitizer filter. */
    TEXT,
    /** Email address, validated against the email format. */
    EMAIL,
    /** URL or resource path, validated structurally, on its scheme and on its query parameter values. */
    URL,
    /** CSS color value ({@code #RGB}, {@code #RRGGBB}, {@code #RRGGBBAA}, {@code rgb()}/{@code rgba()}). */
    COLOR,
    /** HTML content, sanitized with the XSS sanitizer allowlist. */
    HTML;

    /**
     * Indicates whether a field of this type must bypass the global XSS sanitizer filter of the form.
     *
     * <p>
     * The global filter strips a configured set of characters (see {@code lutece.safe.request.site.xssCharacters},
     * typically {@code <>#"&}). A type requires the bypass only when its legitimate content contains such characters:
     * a {@link #URL} carries {@code =} and {@code &} in its query string, a {@link #COLOR} starts with {@code #}, and
     * {@link #HTML} carries markup. {@link #EMAIL} and {@link #TEXT} do not require the bypass: a valid email contains
     * none of the filtered characters and is only validated, and plain text stays under the global filter.
     * </p>
     *
     * @return {@code true} for {@link #URL}, {@link #COLOR} and {@link #HTML}, {@code false} otherwise
     */
    public boolean requiresXssBypass( )
    {
        return this == URL || this == COLOR || this == HTML;
    }
}
