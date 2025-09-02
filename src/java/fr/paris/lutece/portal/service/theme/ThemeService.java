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
package fr.paris.lutece.portal.service.theme;

import fr.paris.lutece.portal.business.theme.ThemeHome;
import fr.paris.lutece.portal.business.style.Theme;

import fr.paris.lutece.portal.service.portal.IThemeService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * ThemeService
 *
 */
public class ThemeService implements IThemeService
{
    private static ThemeService _singleton;

    /**
     * Return the ThemeService singleton
     *
     * @return the ThemeService singleton
     */
    public static ThemeService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new ThemeService( );
        }

        return _singleton;
    }

    /**
     * Init
     */
    public void init( )
    {

    }


    /**
    * Get the list of themes
    * @return a list of themes
    */
    public ReferenceList getThemes( )
    {
        return ThemeHome.getThemes( );
    }

    /**
     * Get the theme with a given code theme
     * @param strCodeTheme the code theme
     * @return the theme
     */
    public Theme getTheme( String strCodeTheme )
    {
        return ThemeHome.findByPrimaryKey( strCodeTheme );
    }

    /**
     * Get the global theme
     * @return the global theme
     */
    public Theme getGlobalTheme( )
    {
        return ThemeHome.getGlobalTheme( );
    }

    /**
    * Gets the theme selected by the user
    *
    * @param request The HTTP request
    * @return The theme if available otherwise null
    */
    public String getUserTheme( HttpServletRequest request )
    {
        if ( request != null )
        {
            Cookie[] cookies = request.getCookies( );

            if ( cookies != null )
            {
                for ( int i = 0; i < cookies.length; i++ )
                {
                    Cookie cookie = cookies[i];

                    if ( cookie.getName(  ).equalsIgnoreCase( ThemeUtil.COOKIE_NAME ) )
                    {
                        String strTheme = cookie.getValue( );

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
     * Return the list of all the themes
     *
     * @return A collection of themes objects
     */
    public Collection<Theme> getThemesList(  )
    {
        return ThemeHome.getThemesList(  );
    }


    /**
     * Set the global theme
     * @param strGlobalTheme the code theme of the global theme
     */
     public void setGlobalTheme( String strGlobalTheme, String strGlobalThemeVersion  )
    {
        ThemeHome.setGlobalTheme( strGlobalTheme );
    }

    /**
    * Creation of an instance of a theme
    *
    * @param theme An instance of a theme which contains the informations to store
    * @return The instance of a theme which has been created with its primary key.
    */
    public Theme create( Theme theme )
    {
        return ThemeHome.create( theme );
    }

    /**
    * Update of the theme which is specified
    *
    * @param theme The instance of the theme which contains the data to store
    * @return The instance of the theme which has been updated
    */
    public Theme update( Theme theme )
    {
        return ThemeHome.update( theme );
    }

    /**
    * Remove the theme whose identifier is specified in parameter
    *
    * @param strCodeTheme The identifier of the theme to remove
    */
    public void remove( String strCodeTheme )
    {
        ThemeHome.remove( strCodeTheme );
    }

    /**
    * Checks if the theme is among existing themes
    *
    * @param strCodeTheme The theme to check
    * @return True if the theme is valid
    */
    public boolean isValidTheme( String strCodeTheme )
    {
        return ThemeHome.isValidTheme( strCodeTheme );
    }
}
