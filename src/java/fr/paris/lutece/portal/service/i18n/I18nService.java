/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
package fr.paris.lutece.portal.service.i18n;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;


/**
 * This class provides services for internationalization (i18n)
 * @since v1.4.1
 */
public final class I18nService
{
    private static final String FORMAT_PACKAGE_PORTAL_RESOURCES_LOCATION = "fr.paris.lutece.portal.resources.{0}_messages";
    private static final String FORMAT_PACKAGE_PLUGIN_RESOURCES_LOCATION = "fr.paris.lutece.plugins.{0}.resources.{0}_messages";
    private static final String FORMAT_PACKAGE_MODULE_RESOURCES_LOCATION = "fr.paris.lutece.plugins.{0}.modules.{1}.resources.{1}_messages";
    private static final String MARK_LOCALIZED_KEY_BEGIN = "#i18n{";
    private static final String MARK_LOCALIZED_KEY_END = "}";
    private static final Locale LOCALE_DEFAULT = new Locale( "", "", "" );
    private static final String PROPERTY_AVAILABLES_LOCALES = "lutece.i18n.availableLocales";

    /**
     * This method localize a string. It scans for localization keys and replace
     * them by localized values.<br>
     * The localization key structure is : #{bundle.key}.<br>
     * bundle's values should be 'portal' or a plugin name.
     * @param strSource The string that contains localization keys
     * @param locale The locale
     * @return The localized string
     */
    public static String localize( String strSource, Locale locale )
    {
        StringBuffer sbReturn = new StringBuffer(  );
        int nPos = strSource.indexOf( MARK_LOCALIZED_KEY_BEGIN );
        int nLastPos = -1;

        while ( nPos != -1 )
        {
            sbReturn.append( strSource.substring( nLastPos + 1, nPos ) );
            nLastPos = strSource.indexOf( MARK_LOCALIZED_KEY_END, nPos );

            String strKey = strSource.substring( nPos + MARK_LOCALIZED_KEY_BEGIN.length(  ), nLastPos );
            sbReturn.append( getLocalizedString( strKey, locale ) );
            nPos = strSource.indexOf( MARK_LOCALIZED_KEY_BEGIN, nLastPos );
        }

        sbReturn.append( strSource.substring( nLastPos + 1 ) );

        return sbReturn.toString(  );
    }

    /**
     * Returns the string corresponding to a given key for a given locale <br>
     * <ul>
     * <li> Core key structure :<br>
     * <code>portal.[admin, features, insert, rbac, search, site, style, system, users, util].key</code></li>
     * <li> Plugin key structure :<br>
     * <code>[plugin].key </code></li>
     * <li> Module key structure :<br>
     * <code>module.[plugin].[module].key </code></li>
     * </ul>
     * @param strKey The key of the string
     * @param locale The locale
     * @return The string corresponding to the key
     */
    public static String getLocalizedString( String strKey, Locale locale )
    {
        String strReturn = "";

        try
        {
            int nPos = strKey.indexOf( '.' );

            if ( nPos != -1 )
            {
                String strBundleKey = strKey.substring( 0, nPos );
                String strStringKey = strKey.substring( nPos + 1 );

                String strBundle = FORMAT_PACKAGE_PORTAL_RESOURCES_LOCATION;

                if ( !strBundleKey.equals( "portal" ) )
                {
                    if ( strBundleKey.equals( "module" ) )
                    {
                        // module resource
                        nPos = strStringKey.indexOf( '.' );

                        String strPlugin = strStringKey.substring( 0, nPos );
                        strStringKey = strStringKey.substring( nPos + 1 );
                        nPos = strStringKey.indexOf( '.' );

                        String strModule = strStringKey.substring( 0, nPos );
                        strStringKey = strStringKey.substring( nPos + 1 );

                        Object[] params = { strPlugin, strModule };
                        MessageFormat format = new MessageFormat( FORMAT_PACKAGE_MODULE_RESOURCES_LOCATION );
                        strBundle = format.format( params );
                    }
                    else
                    {
                        // plugin resource
                        Object[] params = { strBundleKey };
                        MessageFormat format = new MessageFormat( FORMAT_PACKAGE_PLUGIN_RESOURCES_LOCATION );
                        strBundle = format.format( params );
                    }
                }
                else
                {
                    nPos = strStringKey.indexOf( '.' );

                    String strElement = strStringKey.substring( 0, nPos );
                    strStringKey = strStringKey.substring( nPos + 1 );

                    Object[] params = { strElement };
                    MessageFormat format = new MessageFormat( FORMAT_PACKAGE_PORTAL_RESOURCES_LOCATION );
                    strBundle = format.format( params );
                }

                // if language is english use a special locale to force using default 
                // bundle instead of the bundle of default locale.
                if ( locale.getLanguage(  ).equals( Locale.ENGLISH.getLanguage(  ) ) )
                {
                    locale = LOCALE_DEFAULT;
                }

                ResourceBundle rbLabels = ResourceBundle.getBundle( strBundle, locale );
                strReturn = rbLabels.getString( strStringKey );
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return strReturn;
    }

    /**
     * Returns the string corresponding to a given key for a given locale that use a
     * MessageFormat pattern with arguments.
     * @return The string corresponding to the key
     * @param arguments The arguments used as values by the formatter
     * @param strKey The key of the string that contains the pattern
     * @param locale The locale
     */
    public static String getLocalizedString( String strKey, Object[] arguments, Locale locale )
    {
        String strMessagePattern = getLocalizedString( strKey, locale );

        return MessageFormat.format( strMessagePattern, arguments );
    }

    /**
     * Format a date according to the given locale
     * @param date The date to format
     * @param locale The locale
     * @param nDateFormat A DateFormat constant corresponding to the expected format. (ie: DateFormat.FULL)
     * @return The formatted date
     */
    public static String getLocalizedDate( Date date, Locale locale, int nDateFormat )
    {
        DateFormat dateFormatter = DateFormat.getDateInstance( nDateFormat, locale );
        String strDate = dateFormatter.format( date );

        return strDate;
    }

    /**
     * Format a date according to the given locale
     * @param date The date to format
     * @param locale The locale
     * @param nDateFormat A DateFormat constant corresponding to the expected format. (ie: DateFormat.FULL)
     * @param nTimeFormat A TimeFormat constant corresponding to the expected format. (ie: DateFormat.SHORT)
     * @return The formatted date
     */
    public static String getLocalizedDateTime( Date date, Locale locale, int nDateFormat, int nTimeFormat )
    {
        DateFormat dateFormatter = DateFormat.getDateTimeInstance( nDateFormat, nTimeFormat, locale );
        String strDate = dateFormatter.format( date );

        return strDate;
    }

    /**
     * Returns supported locales for Lutece backoffice
     * @return A list of locales
     */
    public static List<Locale> getAdminAvailableLocales(  )
    {
        String strAvailableLocales = AppPropertiesService.getProperty( PROPERTY_AVAILABLES_LOCALES );
        StringTokenizer strTokens = new StringTokenizer( strAvailableLocales, "," );
        List<Locale> list = new ArrayList<Locale>(  );

        while ( strTokens.hasMoreTokens(  ) )
        {
            String strLanguage = strTokens.nextToken(  );
            Locale locale = new Locale( strLanguage );
            list.add( locale );
        }

        return list;
    }

    /**
     * Returns a ReferenceList of available locales
     * @param locale The locale to display available languages
     * @return A ReferenceList of available locales
     */
    public static ReferenceList getAdminLocales( Locale locale )
    {
        ReferenceList list = new ReferenceList(  );

        for ( Locale l : getAdminAvailableLocales(  ) )
        {
            list.addItem( l.getLanguage(  ), l.getDisplayLanguage( l ) );
        }

        return list;
    }

    /**
     * Localize all items of a list
     * @param list The list to localize
     * @param locale The locale
     * @return The localized collection
     */
    public static Collection localizeCollection( Collection<?extends Localizable> collection, Locale locale )
    {
        for ( Localizable object : collection )
        {
            object.setLocale( locale );
        }

        return collection;
    }

    /**
     * Localize all items of a list
     * @param list The list to localize
     * @param locale The locale
     * @return The localized collection
     */
    public static List localizeCollection( List<?extends Localizable> list, Locale locale )
    {
        for ( Localizable object : list )
        {
            object.setLocale( locale );
        }

        return list;
    }
}
