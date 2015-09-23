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
package fr.paris.lutece.portal.service.i18n;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.text.DateFormat;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class provides services for internationalization (i18n)
 * @since v1.4.1
 */
public final class I18nService
{
    private static final String FORMAT_PACKAGE_PORTAL_RESOURCES_LOCATION = "fr.paris.lutece.portal.resources.{0}_messages";
    private static final String FORMAT_PACKAGE_PLUGIN_RESOURCES_LOCATION = "fr.paris.lutece.plugins.{0}.resources.{0}_messages";
    private static final String FORMAT_PACKAGE_MODULE_RESOURCES_LOCATION = "fr.paris.lutece.plugins.{0}.modules.{1}.resources.{1}_messages";
    private static final Pattern PATTERN_LOCALIZED_KEY = Pattern.compile( "#i18n\\{(.*?)\\}" );
    private static final String PROPERTY_AVAILABLES_LOCALES = "lutece.i18n.availableLocales";
    private static final Locale LOCALE_DEFAULT = new Locale( "", "", "" );
    private static final String PROPERTY_DEFAULT_LOCALE = "lutece.i18n.defaultLocale";
    private static final String PROPERTY_FORMAT_DATE_SHORT_LIST = "lutece.format.date.short";
    private static Map<String, String> _pluginBundleNames = Collections.synchronizedMap( new HashMap<String, String>(  ) );
    private static Map<String, String> _moduleBundleNames = Collections.synchronizedMap( new HashMap<String, String>(  ) );
    private static Map<String, String> _portalBundleNames = Collections.synchronizedMap( new HashMap<String, String>(  ) );
    private static final String PROPERTY_PATH_OVERRIDE = "path.i18n.override";
    private static final ClassLoader _overrideLoader;
    private static final Map<String, ResourceBundle> _resourceBundleCache = Collections.synchronizedMap( new HashMap<String, ResourceBundle>(  ) );

    static
    {
        File overridePath = null;

        try
        {
            overridePath = new File( AppPathService.getPath( PROPERTY_PATH_OVERRIDE ) );
        }
        catch ( AppException e )
        {
            // the key is unknown. Message override will be deactivated
            AppLogService.error( "property " + PROPERTY_PATH_OVERRIDE +
                " is undefined. Message overriding will be disabled." );
        }

        URL[] overrideURL = null;

        if ( overridePath != null )
        {
            try
            {
                overrideURL = new URL[] { overridePath.toURI(  ).toURL(  ) };
            }
            catch ( MalformedURLException e )
            {
                AppLogService.error( "Error initializing message overriding: " + e.getMessage(  ), e );
            }
        }

        if ( overrideURL != null )
        {
            _overrideLoader = new URLClassLoader( overrideURL, null );
        }
        else
        {
            _overrideLoader = null;
        }
    }

    /**
     * Private constructor
     */
    private I18nService(  )
    {
    }

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
        String result = strSource;

        if ( strSource != null )
        {
            Matcher matcher = PATTERN_LOCALIZED_KEY.matcher( strSource );

            if ( matcher.find(  ) )
            {
                StringBuffer sb = new StringBuffer(  );

                do
                {
                    matcher.appendReplacement( sb, getLocalizedString( matcher.group( 1 ), locale ) );
                }
                while ( matcher.find(  ) );

                matcher.appendTail( sb );
                result = sb.toString(  );
            }
        }

        return result;
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
     * @param theLocale The locale
     * @return The string corresponding to the key
     */
    public static String getLocalizedString( String strKey, Locale theLocale )
    {
        Locale locale = theLocale;
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

                        strBundle = getModuleBundleName( strPlugin, strModule );
                    }
                    else
                    {
                        // plugin resource
                        strBundle = getPluginBundleName( strBundleKey );
                    }
                }
                else
                {
                    nPos = strStringKey.indexOf( '.' );

                    String strElement = strStringKey.substring( 0, nPos );
                    strStringKey = strStringKey.substring( nPos + 1 );

                    strBundle = getPortalBundleName( strElement );
                }

                // if language is english use a special locale to force using default 
                // bundle instead of the bundle of default locale.
                if ( locale.getLanguage(  ).equals( Locale.ENGLISH.getLanguage(  ) ) )
                {
                    locale = LOCALE_DEFAULT;
                }

                ResourceBundle rbLabels = getResourceBundle( locale, strBundle );
                strReturn = rbLabels.getString( strStringKey );
            }
        }
        catch ( Exception e )
        {
            String strErrorMessage = "Error localizing key : '" + strKey + "' - " + e.getMessage(  );

            if ( e.getCause(  ) != null )
            {
                strErrorMessage += ( " - cause : " + e.getCause(  ).getMessage(  ) );
            }

            AppLogService.error( strErrorMessage );
        }

        return strReturn;
    }

    /**
     * Get resource bundle name for plugin
     * @param strBundleKey the plugin key
     * @return resource bundle name
     */
    private static String getPluginBundleName( String strBundleKey )
    {
        String strBundle = _pluginBundleNames.get( strBundleKey );

        if ( strBundle == null )
        {
            Object[] params = { strBundleKey };
            MessageFormat format = new MessageFormat( FORMAT_PACKAGE_PLUGIN_RESOURCES_LOCATION );
            strBundle = format.format( params );
            _pluginBundleNames.put( strBundleKey, strBundle );
        }

        return strBundle;
    }

    /**
     * Get resource bundle name for module
     * @param strPlugin the plugin key
     * @param strModule the module key
     * @return resource bundle name
     */
    private static String getModuleBundleName( String strPlugin, String strModule )
    {
        String key = strPlugin + strModule;
        String strBundle = _moduleBundleNames.get( key );

        if ( strBundle == null )
        {
            Object[] params = { strPlugin, strModule };
            MessageFormat format = new MessageFormat( FORMAT_PACKAGE_MODULE_RESOURCES_LOCATION );
            strBundle = format.format( params );
            _moduleBundleNames.put( key, strBundle );
        }

        return strBundle;
    }

    /**
     * Get resource bundle name for core element
     * @param strElement element name
     * @return resource bundle name
     */
    private static String getPortalBundleName( String strElement )
    {
        String strBundle = _portalBundleNames.get( strElement );

        if ( strBundle == null )
        {
            Object[] params = { strElement };
            MessageFormat format = new MessageFormat( FORMAT_PACKAGE_PORTAL_RESOURCES_LOCATION );
            strBundle = format.format( params );
            _portalBundleNames.put( strElement, strBundle );
        }

        return strBundle;
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
     * Get the default Locale specified in properties file
     * @return The default Locale
     */
    public static Locale getDefaultLocale(  )
    {
        String strDefaultLocale = AppPropertiesService.getProperty( PROPERTY_DEFAULT_LOCALE );

        return new Locale( strDefaultLocale );
    }

    /**
     * Get the short date format specified by a locale
     * @param locale The locale
     * @return The localized short date pattern or null else
     */
    public static String getDateFormatShortPattern( Locale locale )
    {
        String strAvailableLocales = AppPropertiesService.getProperty( PROPERTY_FORMAT_DATE_SHORT_LIST );

        if ( ( locale != null ) && ( strAvailableLocales != null ) && !strAvailableLocales.equals( "" ) )
        {
            StringTokenizer strTokens = new StringTokenizer( strAvailableLocales, "," );
            String strToken = null;

            for ( Locale adminLocale : getAdminAvailableLocales(  ) )
            {
                if ( ( strTokens != null ) && strTokens.hasMoreTokens(  ) )
                {
                    strToken = strTokens.nextToken(  );
                }

                if ( adminLocale.getLanguage(  ).equals( locale.getLanguage(  ) ) )
                {
                    return strToken;
                }
            }
        }

        return null;
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
     * @param collection The list to localize
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

    /**
     * get the resource bundle, possibly with its override
     * @param locale the locale
     * @param strBundle the bundle name
     * @return the resource bundle
     */
    private static ResourceBundle getResourceBundle( Locale locale, String strBundle )
    {
        String key = strBundle + locale.toString(  );
        ResourceBundle rbLabels = _resourceBundleCache.get( key );

        if ( rbLabels == null )
        {
            rbLabels = ResourceBundle.getBundle( strBundle, locale );

            if ( _overrideLoader != null )
            {
                ResourceBundle overrideBundle = null;

                try
                {
                    overrideBundle = ResourceBundle.getBundle( strBundle, locale, _overrideLoader );
                }
                catch ( MissingResourceException e )
                {
                    // no override for this resource
                    _resourceBundleCache.put( key, rbLabels );

                    return rbLabels;
                }

                ResourceBundle res = new CombinedResourceBundle( overrideBundle, rbLabels );
                _resourceBundleCache.put( key, res );

                return res;
            }

            _resourceBundleCache.put( key, rbLabels );
        }

        return rbLabels;
    }

    /**
     * Reset the caches
     * @since 5.1
     */
    public static void resetCache(  )
    {
        ResourceBundle.clearCache(  );

        if ( _overrideLoader != null )
        {
            ResourceBundle.clearCache( _overrideLoader );
        }

        _resourceBundleCache.clear(  );
    }
}
