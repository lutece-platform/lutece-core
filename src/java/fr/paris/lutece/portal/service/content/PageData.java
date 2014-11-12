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
package fr.paris.lutece.portal.service.content;


/**
 * This class provides a structure to build portal pages.
 */
public class PageData
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String EMPTY_STRING = "";
    private String _strName;
    private String _strFavourite;
    private String _strCssUrl;
    private String _strCustomizeCssUrl;
    private String _strPluginsCssUrl;
    private String _strMetaAuthor;
    private String _strMetaCopyright;
    private String _strMetaKeywords;
    private String _strMetaDescription;
    private String _strHeader;
    private String _strMenu;
    private String _strPagePath;
    private String _strContent;
    private String _strFavicon;
    private String _strTreeMenu;
    private String _strTheme;
    private boolean _bIsHomePage;

    /**
     * Returns the name of the page
     *
     * @return The name of the page as a string.
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the page to the specified string.
     *
     * @param strName The new name of the page.
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the favourite of the page
     *
     * @return The favourite of the page as a string.
     */
    public String getFavourite(  )
    {
        return _strFavourite;
    }

    /**
     * Sets the favourite of the page to the specified string.
     *
     * @param strFavourite The new favourite of the page.
     */
    public void setFavourite( String strFavourite )
    {
        _strFavourite = strFavourite;
    }

    /**
     * Returns the URL of the Cascading Style Sheet associated to this page
     *
     * @return the URL of the Cascading Style Sheet associated to this page as a String.
     */
    public String getCssUrl(  )
    {
        return _strCssUrl;
    }

    /**
     * Sets the URL of the Cascading Style Sheet associated to this page
     *
     * @param strCssUrl  Sets the URL of the Cascading Style Sheet associated to this page to the specified string.
     */
    public void setCssUrl( String strCssUrl )
    {
        _strCssUrl = strCssUrl;
    }

    /**
     * Returns the URL of the Customize Cascading Style Sheet associated to this page
     *
     * @return the URL of the Customize Cascading Style Sheet associated to this page as a String.
     */
    public String getCustomizeCssUrl(  )
    {
        return _strCustomizeCssUrl;
    }

    /**
     * Sets the URL of the Customize Cascading Style Sheet associated to this page
     *
     * @param strCustomizeCssUrl  Sets the URL of the Customize Cascading Style Sheet associated to this page to the
     *        specified string.
     */
    public void setCustomizeCssUrl( String strCustomizeCssUrl )
    {
        _strCustomizeCssUrl = strCustomizeCssUrl;
    }

    /**
     * Returns the URL of the Plugins Cascading Style Sheet associated to this page
     *
     * @return the URL of the Plugins Cascading Style Sheet associated to this page as a String.
     */
    public String getPluginsCssUrl(  )
    {
        return _strPluginsCssUrl;
    }

    /**
     * Sets the URL of the Plugins Cascading Style Sheet associated to this page
     *
     * @param strPluginsCssUrl  Sets the URL of the Plugins Cascading Style Sheet associated to this page to the
     *        specified string.
     */
    public void setPluginsCssUrl( String strPluginsCssUrl )
    {
        _strPluginsCssUrl = strPluginsCssUrl;
    }

    /**
     * Returns Author to mention in the META tags of the page.
     *
     * @return Author to mention in the META tags of the page as a String.
     */
    public String getMetaAuthor(  )
    {
        return _strMetaAuthor;
    }

    /**
     * Sets Author to mention in the META tags of the page.
     *
     * @param strMetaAuthor The Author to mention in the META tags of the page
     */
    public void setMetaAuthor( String strMetaAuthor )
    {
        _strMetaAuthor = strMetaAuthor;
    }

    /**
     * Returns Copyright to mention in the META tags of the page.
     *
     * @return Copyright to mention in the META tags of the page as a String.
     */
    public String getMetaCopyright(  )
    {
        return _strMetaCopyright;
    }

    /**
     * Sets Copyright to mention in the META tags of the page.
     *
     * @param strMetaCopyright The Copyright to mention in the META tags of the page
     */
    public void setMetaCopyright( String strMetaCopyright )
    {
        _strMetaCopyright = strMetaCopyright;
    }

    /**
     * Returns Keywords to mention in the META tags of the page.
     *
     * @return Keywords to mention in the META tags of the page as a String.
     */
    public String getMetaKeywords(  )
    {
        return _strMetaKeywords;
    }

    /**
     * Sets Keywords to mention in the META tags of the page.
     *
     * @param strMetaKeywords The Keywords to mention in the META tags of the page.
     */
    public void setMetaKeywords( String strMetaKeywords )
    {
        _strMetaKeywords = strMetaKeywords;
    }

    /**
     * Returns Description to mention in the META tags of the page.
     *
     * @return Description to mention in the META tags of the page as a String.
     */
    public String getMetaDescription(  )
    {
        return _strMetaDescription;
    }

    /**
     * Sets Description to mention in the META tags of the page.
     *
     * @param strMetaDescription The Description to mention in the META tags of the page.
     */
    public void setMetaDescription( String strMetaDescription )
    {
        _strMetaDescription = strMetaDescription;
    }

    /**
     * Returns the header to display at the top of the page.
     *
     * @return The header HTML code as a String.
     */
    public String getHeader(  )
    {
        return _strHeader;
    }

    /**
     * Sets the header to display at the top of the page.
     *
     * @param strHeader Sets the header to display at the top of the page.
     */
    public void setHeader( String strHeader )
    {
        _strHeader = strHeader;
    }

    /**
     * Returns the menu associated to the page
     *
     * @return The HTML code of the menu associated to the page as a String
     */
    public String getMenu(  )
    {
        return _strMenu;
    }

    /**
     * Sets the menu associated to the page
     *
     * @param strMenu The HTML code of the menu to associate to the page as a String
     */
    public void setMenu( String strMenu )
    {
        _strMenu = strMenu;
    }

    /**
     * Returns the page path.
     *
     * @return the page path.
     */
    public String getPagePath(  )
    {
        return _strPagePath;
    }

    /**
     * Set the page path.
     *
     * @param strPagePath the page path
     */
    public void setPagePath( String strPagePath )
    {
        _strPagePath = strPagePath;
    }

    /**
     * Returns the page path.
     *
     * @return the page path.
     */
    public String getTreeMenu(  )
    {
        return _strTreeMenu;
    }

    /**
     * Set the page path.
     *
     * @param strTreeMenu the page path
     */
    public void setTreeMenu( String strTreeMenu )
    {
        _strTreeMenu = ( strTreeMenu == null ) ? EMPTY_STRING : strTreeMenu;
    }

    /**
     * Returns the page content.
     *
     * @return The HTML code of the page content as a String.
     */
    public String getContent(  )
    {
        return _strContent;
    }

    /**
     * Sets the page content.
     *
     * @param strContent The HTML code of the page content as a String.
     */
    public void setContent( String strContent )
    {
        _strContent = strContent;
    }

    /**
     * Returns the favicon of the page
     *
     * @return The favicon of the page as a string.
     */
    public String getFavicon(  )
    {
        return _strFavicon;
    }

    /**
     * Sets the Favicon of the page to the specified string.
     *
     * @param strFavicon The new Favicon of the page.
     */
    public void setFavicon( String strFavicon )
    {
        _strFavicon = strFavicon;
    }

    /**
     * Returns the theme of the page
     *
     * @return The theme of the page as a string.
     */
    public String getTheme(  )
    {
        return _strTheme;
    }

    /**
     * Sets the Theme of the page to the specified string.
     *
     * @param strTheme The new Theme of the page.
     */
    public void setTheme( String strTheme )
    {
        _strTheme = strTheme;
    }

    /**
     * Returns weither the current page is an homepage or not.
     *
     * @return true if the page is an homepage, otherwise false.
     */
    public boolean isHomePage(  )
    {
        return _bIsHomePage;
    }

    /**
     * Sets the homepage indicator.
     *
     * @param bHomePage Should be true if the page is an homepage, otherwise false.
     */
    public void setHomePage( boolean bHomePage )
    {
        _bIsHomePage = bHomePage;
    }
}
