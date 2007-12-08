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
package fr.paris.lutece.portal.web.includes;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides  the list of the path associated by the topics of the page
 */
public class ThemesInclude implements PageInclude
{
    // Constants
    private static final String MARK_THEME_CSS = "theme_css";
    private static final String MARK_THEME_IMAGES = "theme_images";
    private static final String PROPERTY_PREFIX = "themes.";
    private static final String PROPERTY_SUFFIX_NAME = ".name";
    private static final String PROPERTY_SUFFIX_CSS = ".css";
    private static final String PROPERTY_SUFFIX_IMAGES = ".images";
    private static final String PROPERTY_THEMES_LIST = "themes.list";
    private static final String DEFAULT_THEME = "default";

    /**
     * Substitue specific Freemarker markers in the page template.
     * @param rootModel the HashMap containing markers to substitute
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    public void fillTemplate( Map<String, String> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        // The code_theme of the page
        String strTheme = data.getTheme(  );

        // If code_theme is null, used the default files ( css and images )
        if ( ( strTheme == null ) || ( strTheme.equals( "" ) ) )
        {
            strTheme = DEFAULT_THEME;
        }

        String strCss = AppPropertiesService.getProperty( PROPERTY_PREFIX + strTheme + PROPERTY_SUFFIX_CSS );
        rootModel.put( MARK_THEME_CSS, strCss );

        String strImages = AppPropertiesService.getProperty( PROPERTY_PREFIX + strTheme + PROPERTY_SUFFIX_IMAGES );
        rootModel.put( MARK_THEME_IMAGES, strImages );
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
}
