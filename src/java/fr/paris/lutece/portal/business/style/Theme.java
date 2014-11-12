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
package fr.paris.lutece.portal.business.style;

import java.io.Serializable;


/**
 * This class represents business objects Mode
 */
public class Theme implements Serializable
{
    public static final String RESOURCE_TYPE = "THEME";
    private static final long serialVersionUID = -1423380460541137444L;
    private String _strCodeTheme;
    private String _strThemeDescription;
    private String _strPathImages;
    private String _strPathCss;
    private String _strPathJs;
    private String _strThemeAuthor;
    private String _strThemeAuthorUrl;
    private String _strThemeVersion;
    private String _strThemeLicence;

    /**
     * Returns the _strCodeTheme
     *
     * @return the _strCodeTheme
     */
    public String getCodeTheme(  )
    {
        return _strCodeTheme;
    }

    /**
     * Sets the _strCodeTheme
     *
     * @param strCodeTheme the _strCodeTheme to set
     */
    public void setCodeTheme( String strCodeTheme )
    {
        _strCodeTheme = strCodeTheme;
    }

    /**
     * Returns the _strThemeDescription
     *
     * @return the _strThemeDescription
     */
    public String getThemeDescription(  )
    {
        return _strThemeDescription;
    }

    /**
     * Sets the _strThemeDescription
     *
     * @param strThemeDescription the _strThemeDescription to set
     */
    public void setThemeDescription( String strThemeDescription )
    {
        _strThemeDescription = strThemeDescription;
    }

    /**
     * Returns the _strPathImages
     *
     * @return the _strPathImages
     */
    public String getPathImages(  )
    {
        return _strPathImages;
    }

    /**
     * Sets the _strPathImages
     *
     * @param strPathImages the _strPathImages to set
     */
    public void setPathImages( String strPathImages )
    {
        _strPathImages = strPathImages;
    }

    /**
     * Returns the _strPathCss
     *
     * @return the _strPathCss
     */
    public String getPathCss(  )
    {
        return _strPathCss;
    }

    /**
     * Sets the _strPathCss
     *
     * @param strPathCss the _strPathCss to set
     */
    public void setPathCss( String strPathCss )
    {
        _strPathCss = strPathCss;
    }

    /**
     * Returns the _strPathJs
     *
     * @return the _strPathJs
     */
    public String getPathJs(  )
    {
        return _strPathJs;
    }

    /**
     * Sets the _strPathJs
     *
     * @param strPathJs the _strPathJs to set
     */
    public void setPathJs( String strPathJs )
    {
        _strPathJs = strPathJs;
    }

    /**
     * Returns the _strThemeAuthor
     *
     * @return the _strThemeAuthor
     */
    public String getThemeAuthor(  )
    {
        return _strThemeAuthor;
    }

    /**
     * Sets the _strThemeAuthor
     *
     * @param strThemeAuthor the _strThemeAuthor to set
     */
    public void setThemeAuthor( String strThemeAuthor )
    {
        _strThemeAuthor = strThemeAuthor;
    }

    /**
     * Returns the _strThemeAuthorUrl
     *
     * @return the _strThemeAuthorUrl
     */
    public String getThemeAuthorUrl(  )
    {
        return _strThemeAuthorUrl;
    }

    /**
     * Sets the _strThemeAuthorUrl
     *
     * @param strThemeAuthorUrl the _strThemeAuthorUrl to set
     */
    public void setThemeAuthorUrl( String strThemeAuthorUrl )
    {
        _strThemeAuthorUrl = strThemeAuthorUrl;
    }

    /**
     * Returns the _strThemeVersion
     *
     * @return the _strThemeVersion
     */
    public String getThemeVersion(  )
    {
        return _strThemeVersion;
    }

    /**
     * Sets the _strThemeVersion
     *
     * @param strThemeVersion the _strThemeVersion to set
     */
    public void setThemeVersion( String strThemeVersion )
    {
        _strThemeVersion = strThemeVersion;
    }

    /**
     * Returns the _strThemeLicence
     *
     * @return the _strThemeLicence
     */
    public String getThemeLicence(  )
    {
        return _strThemeLicence;
    }

    /**
     * Sets the _strThemeLicence
     *
     * @param strThemeLicence the _strThemeLicence to set
     */
    public void setThemeLicence( String strThemeLicence )
    {
        _strThemeLicence = strThemeLicence;
    }
}
