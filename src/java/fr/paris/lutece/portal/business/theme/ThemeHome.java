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
package fr.paris.lutece.portal.business.theme;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import jakarta.enterprise.inject.spi.CDI;


/**
 * This class provides instances management methods (create, find, ...) for Theme objects
 */
public final class ThemeHome
{
    // Static variable pointed at the DAO instance
    private static IThemeDAO _dao = CDI.current( ).select( IThemeDAO.class ).get( );

    /**
     * Creates a new ThemeHome object.
     */
    private ThemeHome(  )
    {
    }

    /**
     * Returns an instance of an theme whose identifier is specified in parameter
     *
     * @param strCodeTheme The theme primary key
     * @return an instance of a theme
     */
    public static Theme findByPrimaryKey( String strCodeTheme )
    {
        return _dao.load( strCodeTheme );
    }

    /**
     * Return the list of all the themes
     *
     * @return A collection of themes objects
     */
    public static Collection<Theme> getThemesList(  )
    {
        return _dao.selectThemesList( );
    }

    /**
     * Returns a reference list which contains all the themes
     *
     * @return a reference list
     */
    public static ReferenceList getThemes(  )
    {
        return _dao.getThemesList( );
    }

    /**
     * Checks if the theme is among existing themes
     *
     * @param strCodeTheme The theme to check
     * @return True if the theme is valid
     */
    public static boolean isValidTheme( String strCodeTheme )
    {
        Theme theme = ThemeHome.findByPrimaryKey( strCodeTheme );

        if ( theme != null )
        {
            return true;
        }

        return false;
    }

    /**
     * Returns the global theme
     *
     * @return The Global Theme
     */
    public static Theme getGlobalTheme(  )
    {
        return _dao.getGlobalTheme( );
    }
}
