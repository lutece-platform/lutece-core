/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.business.style.ThemeHome;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ThemesService
 */
public final class ThemesService
{
    public static final String GLOBAL_THEME = "default";
    private static final String COOKIE_NAME = "theme";
    private static final String THEME_TEST = "theme_test";
    private static final String PROPERTY_USE_GLOBAL_THEME = "portal.style.label.useGlobalTheme";

    /**
     * Private constructor
     */
    private ThemesService(  )
    {
    }

    /**
     * Get the theme code depending of the different priorities. The priorities are :
     * <ol>
     * <li>the theme of test (in case you want to test a page with a specific theme)</li>
     * <li>the theme choosen by the user</li>
     * <li>the global theme : the one choosen in the back office for the whole site</li>
     * <li>the page theme : a theme specified for a page</li>
     * </ol>
     *
     * @param data
     * @param request
     * @return
     */
    public static Theme getTheme( PageData data, HttpServletRequest request )
    {
        String strTheme = getGlobalTheme(  );

        // The code_theme of the page
        String strPageTheme = data.getTheme(  );

        if ( ( strPageTheme != null ) && ( strPageTheme.compareToIgnoreCase( GLOBAL_THEME ) != 0 ) )
        {
            strTheme = strPageTheme;
        }

        // The theme of the user
        String strUserTheme = getUserTheme( request );

        if ( strUserTheme != null )
        {
            strTheme = strUserTheme;
        }

        // the test theme (choosen for a page to test the different theme from the backoffice theme section)
        String themeTest = request.getParameter( THEME_TEST );

        if ( themeTest != null )
        {
            strTheme = themeTest;
        }

        Theme theme = ThemeHome.findByPrimaryKey( strTheme );

        return theme;
    }

    /**
     * Gets the theme selected by the user
     *
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

                        if ( ThemeHome.isValidTheme( strTheme ) )
                        {
                            return strTheme;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Sets the users theme using a cookie
     * @param request The HTTP request
     * @param response The HTTP response
     * @param strTheme The Theme code
     */
    public static void setUserTheme( HttpServletRequest request, HttpServletResponse response, String strTheme )
    {
        Cookie cookie = new Cookie( COOKIE_NAME, strTheme );
        response.addCookie( cookie );
    }

    /**
     * Returns the global theme
     *
     * @return the global theme
     */
    public static String getGlobalTheme(  )
    {
        return ThemeHome.getGlobalTheme(  );
    }

    /**
     * Returns the global theme Object
     *
     * @return the global theme Object
     */
    public static Theme getGlobalThemeObject(  )
    {
        return ThemeHome.findByPrimaryKey( ThemeHome.getGlobalTheme(  ) );
    }

    /**
     * Sets the global theme
     *
     * @param strTheme The global theme
     */
    public static void setGlobalTheme( String strGlobalTheme )
    {
        ThemeHome.setGlobalTheme( strGlobalTheme );
    }

    /**
     * Returns a reference list which contains all the themes
     *
     * @return a reference list
     */
    public static ReferenceList getPageThemes( Locale locale )
    {
        ReferenceList listThemes = ThemeHome.getThemes(  );
        String strGlobalTheme = I18nService.getLocalizedString( PROPERTY_USE_GLOBAL_THEME, locale );
        listThemes.addItem( GLOBAL_THEME, strGlobalTheme );

        return listThemes;
    }
}
