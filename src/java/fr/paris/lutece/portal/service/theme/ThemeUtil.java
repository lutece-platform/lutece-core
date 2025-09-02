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


/**
 *
 * ThemeConstants
 *
 */
public final class ThemeUtil
{
    // CONSTANTS
    public static final String COOKIE_NAME = "theme";

    // PROPERTIES

    // MARKS 
    public static final String MARK_THEMES_LIST = "themes_list";
    public static final String MARK_THEME_DEFAULT = "theme_default";
    public static final String MARK_THEME = "theme";
    public static final String MARK_BASE_URL = "base_url";
    public static final String MARK_PERMISSION_MODIFY_GLOBAL_THEME = "permission_modify_global_theme";

    // PARAMETERS
    public static final String PARAMETER_THEME = "theme";
    public static final String PARAMETER_URL = "url";
    public static final String PARAMETER_CODE_THEME = "code_theme";
    public static final String PARAMETER_THEME_LICENCE = "theme_licence";
    public static final String PARAMETER_THEME_VERSION = "theme_version";
    public static final String PARAMETER_THEME_AUTHOR_URL = "theme_author_url";
    public static final String PARAMETER_THEME_AUTHOR = "theme_author";
    public static final String PARAMETER_PATH_CSS = "path_css";
    public static final String PARAMETER_PATH_JS = "path_js";
    public static final String PARAMETER_PATH_IMAGES = "path_images";
    public static final String PARAMETER_THEME_DESCRIPTION = "theme_description";
    
    // PROPERTIES
    public static final String PROPERTY_MANAGE_THEMES_PAGE_TITLE = "theme.manage_themes.page_title";

    // MESSAGES
    public static final String MESSAGE_OBJECT_NOT_FOUND = "theme.message.object_not_found";

    /**
     * Private constructor
     */
    private ThemeUtil(  )
    {
    }
}
