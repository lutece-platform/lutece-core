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

import java.util.ArrayList;
import java.util.Collection;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Theme objects
 */
public final class ThemeDAO implements IThemeDAO
{
	private static final String SQL_QUERY_SELECT = " SELECT code_theme, theme_description, path_images, path_css, theme_author, "
			+ " theme_author_url, theme_version, theme_licence FROM core_theme WHERE code_theme = ?";
	private static final String SQL_QUERY_INSERT = " INSERT INTO core_theme ( code_theme, theme_description, path_images, path_css,"
			+ " theme_author, theme_author_url, theme_version, theme_licence ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
	private static final String SQL_QUERY_DELETE = " DELETE FROM core_theme WHERE code_theme = ?";
	private static final String SQL_QUERY_UPDATE = " UPDATE core_theme SET theme_description = ?, path_images = ?, "
			+ " path_css = ? , theme_author = ?, theme_author_url = ?, theme_version = ?, " + " theme_licence = ? WHERE code_theme = ?";
	private static final String SQL_QUERY_SELECTALL = " SELECT code_theme, theme_description, path_images, path_css, theme_author, "
			+ " theme_author_url, theme_version, theme_licence FROM core_theme ORDER BY code_theme";
	private static final String SQL_QUERY_SELECT_THEME = " SELECT id_theme , description_theme FROM core_theme";

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.paris.lutece.portal.business.style.IThemeDAO#insert(Theme)
	 */
	public synchronized void insert( Theme theme )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

		daoUtil.setString( 1, theme.getCodeTheme( ) );
		daoUtil.setString( 2, theme.getThemeDescription( ) );
		daoUtil.setString( 3, theme.getPathImages( ) );
		daoUtil.setString( 4, theme.getPathCss( ) );
		daoUtil.setString( 5, theme.getThemeAuthor( ) );
		daoUtil.setString( 6, theme.getThemeAuthorUrl( ) );
		daoUtil.setString( 7, theme.getThemeVersion( ) );
		daoUtil.setString( 8, theme.getThemeLicence( ) );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.paris.lutece.portal.business.style.IThemeDAO#load(String)
	 */
	public Theme load( String strCodeTheme )
	{
		Theme theme = null;
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
		daoUtil.setString( 1, strCodeTheme );

		daoUtil.executeQuery( );

		if( daoUtil.next( ) )
		{
			theme = new Theme( );
			theme.setCodeTheme( daoUtil.getString( 1 ) );
			theme.setThemeDescription( daoUtil.getString( 2 ) );
			theme.setPathImages( daoUtil.getString( 3 ) );
			theme.setPathCss( daoUtil.getString( 4 ) );
			theme.setThemeAuthor( daoUtil.getString( 5 ) );
			theme.setThemeAuthorUrl( daoUtil.getString( 6 ) );
			theme.setThemeVersion( daoUtil.getString( 7 ) );
			theme.setThemeLicence( daoUtil.getString( 8 ) );
		}

		daoUtil.free( );

		return theme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.paris.lutece.portal.business.style.IThemeDAO#delete(String)
	 */
	public void delete( String strCodeTheme )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
		daoUtil.setString( 1, strCodeTheme );
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.paris.lutece.portal.business.style.IThemeDAO#store(fr.paris.lutece.portal.business.style.Theme)
	 */
	public void store( Theme theme )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

		daoUtil.setString( 1, theme.getThemeDescription( ) );
		daoUtil.setString( 2, theme.getPathImages( ) );
		daoUtil.setString( 3, theme.getPathCss( ) );
		daoUtil.setString( 4, theme.getThemeAuthor( ) );
		daoUtil.setString( 5, theme.getThemeAuthorUrl( ) );
		daoUtil.setString( 6, theme.getThemeVersion( ) );
		daoUtil.setString( 7, theme.getThemeLicence( ) );
		daoUtil.setString( 8, theme.getCodeTheme( ) );

		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.paris.lutece.portal.business.style.IThemeDAO#selectThemesList()
	 */
	public Collection<Theme> selectThemesList( )
	{
		Collection<Theme> themeList = new ArrayList<Theme>( );
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
		daoUtil.executeQuery( );

		while( daoUtil.next( ) )
		{
			Theme theme = new Theme( );

			theme.setCodeTheme( daoUtil.getString( 1 ) );
			theme.setThemeDescription( daoUtil.getString( 2 ) );
			theme.setPathImages( daoUtil.getString( 3 ) );
			theme.setPathCss( daoUtil.getString( 4 ) );
			theme.setThemeAuthor( daoUtil.getString( 5 ) );
			theme.setThemeAuthorUrl( daoUtil.getString( 6 ) );
			theme.setThemeVersion( daoUtil.getString( 7 ) );
			theme.setThemeLicence( daoUtil.getString( 8 ) );

			themeList.add( theme );
		}

		daoUtil.free( );

		return themeList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.paris.lutece.portal.business.style.IThemeDAO#getThemesList()
	 */
	public ReferenceList getThemesList( )
	{
		ReferenceList themesList = new ReferenceList( );
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_THEME );

		daoUtil.executeQuery( );

		while( daoUtil.next( ) )
		{
			themesList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
		}

		daoUtil.free( );

		return themesList;
	}
}
