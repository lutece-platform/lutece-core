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
package fr.paris.lutece.portal.service.portal;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.ArrayList;
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
    private static final String THEME_PLUGIN_NAME = "theme";
    private static final String BEAN_THEME_SERVICE = "theme.themeService";
    private static final String COOKIE_NAME = "theme";
    private static final String THEME_TEST = "theme_test";

    // PROPERTIES
    private static final String PROPERTY_USE_GLOBAL_THEME = "portal.style.label.useGlobalTheme";
    private static final String PROPERTY_DEFAULT_CODE_THEME = "themes.default.code";
    private static final String PROPERTY_DEFAULT_PATH_CSS = "themes.default.css";
    private static final String PROPERTY_DEFAULT_PATH_JS = "themes.default.js";
    private static final String PROPERTY_DEFAULT_PATH_IMAGE = "themes.default.images";
    private static final String PROPERTY_DEFAULT_AUTHOR_URL = "themes.default.author_url";
    private static final String PROPERTY_DEFAULT_AUTHOR = "themes.default.author";
    private static final String PROPERTY_DEFAULT_LICENCE = "themes.default.licence";
    private static final String PROPERTY_DEFAULT_DESCRIPTION = "themes.default.name";
    private static final String PROPERTY_DEFAULT_VERSION = "themes.default.version";

    // MESSAGES
    private static final String MESSAGE_THEME_NOT_AVAILABLE = "Theme service not available.";

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
     * @param data The PageData object
     * @param request The HttpServletRequest
     * @return the theme
     */
    public static Theme getTheme( PageData data, HttpServletRequest request )
    {
        String strTheme = StringUtils.EMPTY;

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

        Theme theme = getGlobalTheme( strTheme );

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
        try
        {
            IThemeService themeService = getThemeService(  );

            return themeService.getUserTheme( request );
        }
        catch ( ThemeNotAvailableException e )
        {
            return null;
        }
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
        Theme theme = getGlobalTheme( StringUtils.EMPTY );

        return theme.getCodeTheme(  );
    }

    /**
     * Returns the global theme Object
     *
     * @return the global theme Object
     */
    public static Theme getGlobalThemeObject(  )
    {
        return getGlobalTheme( StringUtils.EMPTY );
    }

    /**
     * Returns the global theme
     * @param strTheme The theme
     * @return the global theme
     */
    public static Theme getGlobalTheme( String strTheme )
    {
        Theme theme = null;

        try
        {
            IThemeService themeService = getThemeService(  );

            if ( StringUtils.isBlank( strTheme ) )
            {
                theme = themeService.getGlobalTheme(  );
            }
            else
            {
                theme = themeService.getTheme( strTheme );
            }
        }
        catch ( ThemeNotAvailableException e )
        {
            theme = getDefaultTheme(  );
        }

        return theme;
    }

    /**
     * Sets the global theme
     *
     * @param strGlobalTheme The global theme
     */
    public static void setGlobalTheme( String strGlobalTheme )
    {
        IThemeService themeService;

        try
        {
            themeService = getThemeService(  );
            themeService.setGlobalTheme( strGlobalTheme );
        }
        catch ( ThemeNotAvailableException e )
        {
            AppLogService.info( MESSAGE_THEME_NOT_AVAILABLE );
        }
    }

    /**
     * Returns a reference list which contains all the themes
     * @param locale The Locale
     * @return a reference list
     */
    public static ReferenceList getPageThemes( Locale locale )
    {
        ReferenceList listThemes = new ReferenceList(  );

        try
        {
            IThemeService themeService = getThemeService(  );
            listThemes = themeService.getThemes(  );
        }
        catch ( ThemeNotAvailableException e )
        {
            AppLogService.info( MESSAGE_THEME_NOT_AVAILABLE );
        }

        String strGlobalTheme = I18nService.getLocalizedString( PROPERTY_USE_GLOBAL_THEME, locale );
        listThemes.addItem( GLOBAL_THEME, strGlobalTheme );

        return listThemes;
    }

    /**
     * Get the theme service
     * @return the theme service
     * @throws ThemeNotAvailableException If the theme is not available
     */
    private static IThemeService getThemeService(  ) throws ThemeNotAvailableException
    {
        IThemeService themeService = null;

        if ( !isAvailable(  ) )
        {
            throw new ThemeNotAvailableException(  );
        }

        try
        {
            themeService = SpringContextService.getBean( BEAN_THEME_SERVICE );
        }
        catch ( BeanDefinitionStoreException e )
        {
            throw new ThemeNotAvailableException(  );
        }
        catch ( NoSuchBeanDefinitionException e )
        {
            throw new ThemeNotAvailableException(  );
        }
        catch ( CannotLoadBeanClassException e )
        {
            throw new ThemeNotAvailableException(  );
        }

        return themeService;
    }

    /**
     * Return the default theme in properties
     * @return the default theme
     */
    private static Theme getDefaultTheme(  )
    {
        Theme theme = new Theme(  );
        String strCodeTheme = AppPropertiesService.getProperty( PROPERTY_DEFAULT_CODE_THEME );
        String strPathCss = AppPropertiesService.getProperty( PROPERTY_DEFAULT_PATH_CSS );
        String strPathImages = AppPropertiesService.getProperty( PROPERTY_DEFAULT_PATH_IMAGE );
        String strPathJs = AppPropertiesService.getProperty( PROPERTY_DEFAULT_PATH_JS );
        String strThemeAuthor = AppPropertiesService.getProperty( PROPERTY_DEFAULT_AUTHOR );
        String strThemeAuthorUrl = AppPropertiesService.getProperty( PROPERTY_DEFAULT_AUTHOR_URL );
        String strThemeDescription = AppPropertiesService.getProperty( PROPERTY_DEFAULT_DESCRIPTION );
        String strThemeLicence = AppPropertiesService.getProperty( PROPERTY_DEFAULT_LICENCE );
        String strThemeVersion = AppPropertiesService.getProperty( PROPERTY_DEFAULT_VERSION );

        theme.setCodeTheme( strCodeTheme );
        theme.setPathCss( strPathCss );
        theme.setPathImages( strPathImages );
        theme.setPathJs( strPathJs );
        theme.setThemeAuthor( strThemeAuthor );
        theme.setThemeAuthorUrl( strThemeAuthorUrl );
        theme.setThemeDescription( strThemeDescription );
        theme.setThemeLicence( strThemeLicence );
        theme.setThemeVersion( strThemeVersion );

        return theme;
    }

    /**
     * Check if the theme service is available. It must have the following requirement to be
     * available :
     * <ul>
     * <li>The <code>plugin-theme</code> is activated</li>
     * <li>The pool of the <code>plugin-theme</code> is defined</li>
     * </ul>
     * @return true if it is available, false otherwise
     */
    public static boolean isAvailable(  )
    {
        Plugin pluginTheme = PluginService.getPlugin( THEME_PLUGIN_NAME );

        return PluginService.isPluginEnable( THEME_PLUGIN_NAME ) && ( pluginTheme.getDbPoolName(  ) != null ) &&
        !AppConnectionService.NO_POOL_DEFINED.equals( pluginTheme.getDbPoolName(  ) );
    }

    /**
     * Create a new theme
     * @param theme the theme
     * @return The theme
     */
    public static Theme create( Theme theme )
    {
        try
        {
            IThemeService themeService = getThemeService(  );

            return themeService.create( theme );
        }
        catch ( ThemeNotAvailableException e )
        {
            AppLogService.info( MESSAGE_THEME_NOT_AVAILABLE );

            return null;
        }
    }

    /**
     * Update a theme
     * @param theme the theme to update
     * @return The updated theme
     */
    public static Theme update( Theme theme )
    {
        try
        {
            IThemeService themeService = getThemeService(  );

            return themeService.update( theme );
        }
        catch ( ThemeNotAvailableException e )
        {
            AppLogService.info( MESSAGE_THEME_NOT_AVAILABLE );

            return null;
        }
    }

    /**
     * Remove a theme
     * @param strCodeTheme the code theme
     */
    public static void remove( String strCodeTheme )
    {
        try
        {
            IThemeService themeService = getThemeService(  );
            themeService.remove( strCodeTheme );
        }
        catch ( ThemeNotAvailableException e )
        {
            AppLogService.info( MESSAGE_THEME_NOT_AVAILABLE );
        }
    }

    /**
     * Get a collection of themes
     * @return a collection of themes
     */
    public static Collection<Theme> getThemesList(  )
    {
        Collection<Theme> listThemes = new ArrayList<Theme>(  );

        try
        {
            IThemeService themeService = getThemeService(  );
            listThemes = themeService.getThemesList(  );
        }
        catch ( ThemeNotAvailableException e )
        {
            Theme theme = getDefaultTheme(  );
            listThemes.add( theme );
        }

        return listThemes;
    }

    /**
     * Get the list of themes as a {@link ReferenceList}
     * @return a {@link ReferenceList}
     */
    public static ReferenceList getThemes(  )
    {
        ReferenceList listThemes = new ReferenceList(  );

        try
        {
            IThemeService themeService = getThemeService(  );
            listThemes = themeService.getThemes(  );
        }
        catch ( ThemeNotAvailableException e )
        {
            Theme theme = getDefaultTheme(  );
            listThemes.addItem( theme.getCodeTheme(  ), theme.getThemeDescription(  ) );
        }

        return listThemes;
    }

    /**
     * Check if the theme is valid
     * @param strCodeTheme the code theme
     * @return true if it is valid, false otherwise
     */
    public static boolean isValidTheme( String strCodeTheme )
    {
        boolean bIsValidTheme = false;

        try
        {
            IThemeService themeService = getThemeService(  );
            bIsValidTheme = themeService.isValidTheme( strCodeTheme );
        }
        catch ( ThemeNotAvailableException e )
        {
            Theme theme = getDefaultTheme(  );
            bIsValidTheme = theme.getCodeTheme(  ).equals( strCodeTheme );
        }

        return bIsValidTheme;
    }
}
