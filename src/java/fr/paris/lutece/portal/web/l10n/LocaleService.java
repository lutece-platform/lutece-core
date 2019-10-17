/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.ArrayList;
import java.util.List;

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
    private static final String PROPERTY_LANG_LIST = "lutece.i18n.availableLocales";
    private static final String PROPERTY_LANG_DEFAULT = "lutece.i18n.defaultLocale";
    private static Locale _locale;
    private static List<Locale> _supportedLocales;

    /**
     * Private constructor
     */
    private LocaleService( )
    {
    }

    /**
     * Set the locale selected by the user in the front office. The user may select a language without been authenticated.
     * 
     * @param request
     *            The request
     * @param locale
     *            The locale
     */
    public static void setUserSelectedLocale( HttpServletRequest request, Locale locale )
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_SELECTED_LOCALE, locale );
    }

    /**
     * Get a Locale selected by the user in front office
     * 
     * @param request
     *            The HTTP request
     * @return The locale selected, or the default jvm locale if no locale has been selected
     */
    public static Locale getUserSelectedLocale( HttpServletRequest request )
    {
        Locale locale = LocaleService.getDefault( );
        HttpSession session = request.getSession( );

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
     * 
     * @return The locale
     */
    public static Locale getDefault( )
    {
        if ( _locale != null )
        {
            return _locale;
        }
        else
        {
            String dsLang = DatastoreService.getInstanceDataValue( DSKEY_LANGUAGE_DEFAULT, null );

            if ( dsLang != null )
            {
                for ( String strISOLang : Locale.getISOLanguages( ) )
                {
                    if ( strISOLang.equals( dsLang ) )
                    {
                        _locale = new Locale( dsLang );
                        AppLogService.info( "LocaleService : default locale set to : " + dsLang );

                        return _locale;
                    }
                }
            }

            // otherwise, get the default locale from properties
            _locale = new Locale( AppPropertiesService.getProperty( PROPERTY_LANG_DEFAULT ) );
            AppLogService.error( "LocaleService : invalid defined locale " + dsLang + " - default set to " + _locale.getLanguage( ) );

            return _locale;
        }
    }

    /**
     * Retrieve the best supported locale for the user's session Priority order : 1- selected locale session attribute 2- browser locale (if supported) 3-
     * default server locale
     * 
     * @param request
     *            The HTTP request
     * @return The locale
     */
    public static Locale getContextUserLocale( HttpServletRequest request )
    {
        Locale locale = null;

        // consider the current session locale
        HttpSession session = request.getSession( false );
        if ( session != null )
        {
            Locale userSessionLocale = (Locale) session.getAttribute( ATTRIBUTE_SELECTED_LOCALE );
            if ( userSessionLocale != null )
            {
                locale = userSessionLocale;
            }
        }

        if ( locale == null || !isSupported( locale ) )
        {
            // consider the browser language
            locale = new Locale( request.getLocale( ).getLanguage( ).substring( 0, 2 ) );

            if ( !isSupported( locale ) )
            {
                // consider the server default
                locale = getDefault( );
            }
        }

        return locale;
    }

    /**
     * check if Locale is supported according to locale list in lutece properties
     * 
     * @param locale
     * @return true if supported
     */
    public static boolean isSupported( Locale locale )
    {
        if ( _supportedLocales == null )
        {
            getSupportedLangList( );
        }

        // check if the mandatory language is supported
        for ( Locale supportedLocale : _supportedLocales )
        {
            if ( supportedLocale.equals( locale ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * get Supported Lang List
     * 
     * @return the Supported Lang List
     */
    public static List<Locale> getSupportedLangList( )
    {
        if ( _supportedLocales == null )
        {
            String [ ] strLangList = AppPropertiesService.getProperty( PROPERTY_LANG_LIST ).split( "," );
            _supportedLocales = new ArrayList<>( );
            for ( String lang : strLangList )
            {
                _supportedLocales.add( new Locale( lang ) );
            }
        }

        return _supportedLocales;
    }
}
