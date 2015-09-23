/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * LocaleService
 */
public final class LocaleService
{
    private static final String ATTRIBUTE_SELECTED_LOCALE = "LUTECE_ATTRIBUTE_USER_SELECTED_LOCALE";
    private static final String DSKEY_LANGUAGE_DEFAULT = "portal.site.site_property.locale.default";
    private static final String LANGUAGE_DEFAULT = "en";
    private static Locale _locale;

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
        Locale locale = LocaleService.getDefault(  );
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

    /**
     * Return a Lutece defined default Locale
     * @return The locale
     */
    public static Locale getDefault(  )
    {
        if ( _locale == null )
        {
            String strCountry = DatastoreService.getInstanceDataValue( DSKEY_LANGUAGE_DEFAULT, LANGUAGE_DEFAULT );

            for ( String strISOContry : Locale.getISOCountries(  ) )
            {
                if ( strISOContry.equalsIgnoreCase( strCountry ) )
                {
                    _locale = new Locale( strCountry );
                    AppLogService.info( "LocaleService : default locale set to : " + strCountry );

                    return _locale;
                }
            }

            _locale = Locale.getDefault(  );
            AppLogService.error( "LocaleService : invalid defined locale " + strCountry + " - default set to " +
                LANGUAGE_DEFAULT );
        }

        return _locale;
    }
}
