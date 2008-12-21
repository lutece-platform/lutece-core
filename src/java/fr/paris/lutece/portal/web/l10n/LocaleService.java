/*
 * Copyright (c) 2002-2008, Mairie de Paris
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
package fr.paris.lutece.portal.web.l10n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * LocaleService
 */
public final class LocaleService
{
    private static final String ATTRIBUTE_SELECTED_LOCALE = "LUTECE_ATTRIBUTE_USER_SELECTED_LOCALE";

    /**
     * Private constructor
     */
    private LocaleService(  )
    {
    }

    /**
     * Set the locale selected by the user in the front office. The user may select a language
     * without been authenticated.
     * @param request The request
     * @param locale The locale
     */
    public static void setUserSelectedLocale( HttpServletRequest request, Locale locale )
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_SELECTED_LOCALE, locale );
    }

    /**
     * Get a Locale selected by the user in front office
     * @param request The HTTP request
     * @return The locale selected, or the default jvm locale if no locale has been selected
     */
    public static Locale getUserSelectedLocale( HttpServletRequest request )
    {
        Locale locale = Locale.getDefault(  );
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            Locale localeSession = (Locale) session.getAttribute( ATTRIBUTE_SELECTED_LOCALE );

            if ( localeSession != null )
            {
                locale = localeSession;
            }
        }

        return locale;
    }
}
