/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.datastore.LocalizedData;
import fr.paris.lutece.portal.service.datastore.LocalizedDataGroup;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.PathNotFoundException;

/**
 * Lets a front office theme choose which theme site properties ( <code>portal.theme.site_property.*</code> ) are offered in the site properties
 * management screen. The theme declares it in an optional file <code>themes/skin/&lt;theme code&gt;/theme.json</code>:
 *
 * <pre>
 * {
 *   "siteProperties": {
 *     "groups": { "consent": false },
 *     "keys": { "portal.theme.site_property.layout.darkmode.checkbox": false }
 *   }
 * }
 * </pre>
 *
 * Only <code>false</code> hides something: a property that is not listed, or a theme without the file, keeps every property available. A group is the
 * value of the <code>.group</code> resource of the property. A key hides the property with that key and every property under it ( the key followed by
 * a dot ), so hiding a <code>.select</code> also hides its <code>.select.options</code> companion. A hidden property keeps its stored value: the save
 * only writes the properties that are posted.
 */
public final class ThemeSitePropertiesFilter
{
    /** The name of the theme configuration file, in the theme skin directory */
    public static final String THEME_CONFIG_FILE = "theme.json";

    private static final String PATH_THEMES_SKIN = "/themes/skin/";
    private static final String THEME_SITE_PROPERTIES_PREFIX = "portal.theme.site_property.";
    private static final String SUFFIX_OPTIONS = ".options";
    private static final String NODE_SITE_PROPERTIES = "siteProperties";
    private static final String NODE_GROUPS = "groups";
    private static final String NODE_KEYS = "keys";
    private static final String SEPARATOR = ".";
    private static final Pattern PATTERN_THEME_CODE = Pattern.compile( "[A-Za-z0-9_-]+" );
    private static final ObjectMapper MAPPER = new ObjectMapper( );

    /**
     * Private constructor
     */
    private ThemeSitePropertiesFilter( )
    {
    }

    /**
     * Removes from the groups the theme site properties that the theme configuration file hides
     *
     * @param listGroups
     *            The groups of site properties, modified in place
     * @param theme
     *            The front office theme in use
     */
    public static void filter( List<LocalizedDataGroup> listGroups, Theme theme )
    {
        JsonNode config = readConfig( theme );

        if ( config != null )
        {
            for ( LocalizedDataGroup group : listGroups )
            {
                filter( group.getLocalizedDataList( ), config );
            }
        }
    }

    /**
     * Removes from a list of properties the theme site properties that a configuration hides
     *
     * @param listData
     *            The properties, modified in place
     * @param config
     *            The content of the theme configuration file
     */
    static void filter( List<LocalizedData> listData, JsonNode config )
    {
        JsonNode siteProperties = config.path( NODE_SITE_PROPERTIES );
        JsonNode groups = siteProperties.path( NODE_GROUPS );
        JsonNode keys = siteProperties.path( NODE_KEYS );

        // The .options companion of a property hidden by its group has no group: it follows its property
        List<String> listHiddenByGroup = listData.stream( )
                .filter( data -> isThemeProperty( data ) && isFalse( groups, data.getGroup( ) ) )
                .map( LocalizedData::getKey )
                .toList( );

        listData.removeIf( data -> isThemeProperty( data )
                && ( isFalse( groups, data.getGroup( ) ) || isHiddenByKey( keys, data.getKey( ) ) || isOptionsOf( data.getKey( ), listHiddenByGroup ) ) );
    }

    /**
     * Reads the configuration file of a theme
     *
     * @param theme
     *            The theme
     * @return The content of the file, or null when the theme has none or it can't be read
     */
    private static JsonNode readConfig( Theme theme )
    {
        if ( theme == null || theme.getCodeTheme( ) == null || !PATTERN_THEME_CODE.matcher( theme.getCodeTheme( ) ).matches( ) )
        {
            return null;
        }

        File file;

        try
        {
            file = new File( AppPathService.getAbsolutePathFromRelativePath( PATH_THEMES_SKIN + theme.getCodeTheme( ) + "/" + THEME_CONFIG_FILE ) );
        }
        catch( PathNotFoundException e )
        {
            // The file is optional: a theme without it keeps every property
            return null;
        }

        if ( !file.isFile( ) )
        {
            return null;
        }

        try
        {
            return MAPPER.readTree( file );
        }
        catch( IOException e )
        {
            AppLogService.error( "Unable to read the theme configuration file {} : {}", file.getPath( ), e.getMessage( ), e );

            return null;
        }
    }

    private static boolean isThemeProperty( LocalizedData data )
    {
        return data.getKey( ) != null && data.getKey( ).startsWith( THEME_SITE_PROPERTIES_PREFIX );
    }

    private static boolean isFalse( JsonNode map, String strName )
    {
        if ( strName == null || strName.isBlank( ) )
        {
            return false;
        }

        JsonNode value = map.get( strName.trim( ) );

        return value != null && value.isBoolean( ) && !value.booleanValue( );
    }

    private static boolean isHiddenByKey( JsonNode keys, String strKey )
    {
        for ( var it = keys.properties( ).iterator( ); it.hasNext( ); )
        {
            var entry = it.next( );

            if ( entry.getValue( ).isBoolean( ) && !entry.getValue( ).booleanValue( )
                    && ( strKey.equals( entry.getKey( ) ) || strKey.startsWith( entry.getKey( ) + SEPARATOR ) ) )
            {
                return true;
            }
        }

        return false;
    }

    private static boolean isOptionsOf( String strKey, List<String> listHiddenKeys )
    {
        return strKey.endsWith( SUFFIX_OPTIONS ) && listHiddenKeys.contains( strKey.substring( 0, strKey.length( ) - SUFFIX_OPTIONS.length( ) ) );
    }
}
