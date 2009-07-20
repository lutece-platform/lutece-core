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
package fr.paris.lutece.portal.business.style;

import java.io.Serializable;

/**
 * This class represents business objects Mode
 */
public class Theme implements Serializable
{
	private static final long serialVersionUID = -1423380460541137444L;

	private String _strCodeTheme;
	private String _strThemeDescription;
	private String _strPathImages;
	private String _strPathCss;
	private String _strThemeAuthor;
	private String _strThemeAuthorUrl;
	private String _strThemeVersion;
	private String _strThemeLicence;

	public String getCodeTheme( )
	{
		return _strCodeTheme;
	}

	public void setCodeTheme( String codeTheme )
	{
		_strCodeTheme = codeTheme;
	}

	public String getThemeDescription( )
	{
		return _strThemeDescription;
	}

	public void setThemeDescription( String themeDescription )
	{
		_strThemeDescription = themeDescription;
	}

	public String getPathImages( )
	{
		return _strPathImages;
	}

	public void setPathImages( String pathImages )
	{
		_strPathImages = pathImages;
	}

	public String getPathCss( )
	{
		return _strPathCss;
	}

	public void setPathCss( String pathCss )
	{
		_strPathCss = pathCss;
	}

	public String getThemeAuthor( )
	{
		return _strThemeAuthor;
	}

	public void setThemeAuthor( String themeAuthor )
	{
		_strThemeAuthor = themeAuthor;
	}

	public String getThemeAuthorUrl( )
	{
		return _strThemeAuthorUrl;
	}

	public void setThemeAuthorUrl( String themeAuthorUrl )
	{
		_strThemeAuthorUrl = themeAuthorUrl;
	}

	public String getThemeVersion( )
	{
		return _strThemeVersion;
	}

	public void setThemeVersion( String themeVersion )
	{
		_strThemeVersion = themeVersion;
	}

	public String getThemeLicence( )
	{
		return _strThemeLicence;
	}

	public void setThemeLicence( String themeLicence )
	{
		_strThemeLicence = themeLicence;
	}
}
