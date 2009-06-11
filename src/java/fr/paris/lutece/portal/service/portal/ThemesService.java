/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.service.portal;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ThemesService
 */
public final class ThemesService
{
    public static final String DEFAULT_THEME = "default";
    private static final String PROPERTY_PREFIX = "themes.";
    private static final String PROPERTY_SUFFIX_CSS = ".css";
    private static final String PROPERTY_SUFFIX_IMAGES = ".images";
    private static final String PROPERTY_SUFFIX_NAME = ".name";
    private static final String PROPERTY_THEMES_LIST = "themes.list";
    private static final String COOKIE_NAME = "theme";
    private static String _strGlobalTheme = DEFAULT_THEME;

    /**
     * Private constructor
     */
    private ThemesService()
    {
    }

    /**
     * Returns the list of the code_theme of the page
     *
     * @return the list of the page Code_theme in form of ReferenceList
     */
    public static ReferenceList getThemesList(  )
    {
        // recovers themes list from the includes.list entry in the properties download file
        String strThemesList = AppPropertiesService.getProperty( PROPERTY_THEMES_LIST );

        // extracts each item (separated by a comma) from the includes list
        StringTokenizer strTokens = new StringTokenizer( strThemesList, "," );

        ReferenceList listThemes = new ReferenceList(  );

        while ( strTokens.hasMoreTokens(  ) )
        {
            String strTheme = (String) strTokens.nextToken(  );
            String strThemeName = AppPropertiesService.getProperty( PROPERTY_PREFIX + strTheme + PROPERTY_SUFFIX_NAME );
            listThemes.addItem( strTheme, strThemeName );
        }

        return listThemes;
    }

    /**
     * Gets the theme selected by the user
     * @param request The HTTP request
     * @return The theme if available otherwise null
     */
    public static String getUserTheme( HttpServletRequest request )
    {
        if ( request != null )
        {
            Cookie[] cookies = request.getCookies(  );

            if ( cookies != null )
            {
                for ( int i = 0; i < cookies.length; i++ )
                {
                    Cookie cookie = cookies[i];

                    if ( cookie.getName(  ).equalsIgnoreCase( COOKIE_NAME ) )
                    {
                        String strTheme = cookie.getValue(  );

                        if ( isValidTheme( strTheme ) )
                        {
                            return strTheme;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static void setUserTheme( HttpServletRequest request, HttpServletResponse response, String strTheme )
    {
        Cookie cookie = new Cookie( COOKIE_NAME, strTheme );
        response.addCookie( cookie );
    }

    /**
     * Checks if the theme is among existing themes
     * @param strTheme The theme to check
     * @return True if the theme is valid
     */
    private static boolean isValidTheme( String strTheme )
    {
        Iterator i = getThemesList(  ).iterator(  );

        while ( i.hasNext(  ) )
        {
            ReferenceItem item = (ReferenceItem) i.next(  );

            if ( item.getCode(  ).equals( strTheme ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the global theme
     * @return the global theme
     */
    public static String getGlobalTheme(  )
    {
        return _strGlobalTheme;
    }

    /**
     * Sets the global theme
     * @param strTheme The global theme
     */
    public static void setGlobalTheme( String strTheme )
    {
        _strGlobalTheme = strTheme;
    }

    /**
     * Returns the url of the CSS of the given theme
     * @param strTheme The theme
     * @return The CSS url
     */
    public static String getThemeCSS( String strTheme )
    {
        return AppPropertiesService.getProperty( PROPERTY_PREFIX + strTheme + PROPERTY_SUFFIX_CSS );
    }

    /**
     * Returns the url of the images directory of the given theme
     * @param strTheme The theme
     * @return The images directory url
     */
    public static String getThemeImages( String strTheme )
    {
        return AppPropertiesService.getProperty( PROPERTY_PREFIX + strTheme + PROPERTY_SUFFIX_IMAGES );
    }
}
