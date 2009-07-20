package fr.paris.lutece.portal.business.style;

import java.util.Collection;

import fr.paris.lutece.util.ReferenceList;

public interface IThemeDAO
{

	/**
	 * Insert a new record in the table.
	 * 
	 * @param mode The mode object
	 */
	public abstract void insert( Theme mode );

	/**
	 * load the data of Level from the table
	 * 
	 * @param nIdTheme The indentifier of the object Theme
	 * @return The Instance of the object Theme
	 */
	public abstract Theme load( String strCodeTheme );

	/**
	 * Delete a record from the table
	 * 
	 * @param nThemeId The indentifier of the object Theme
	 */
	public abstract void delete( String strCodeTheme );

	/**
	 * Update the record in the table
	 * 
	 * @param mode The instance of the Theme to update
	 */
	public abstract void store( Theme theme );

	/**
	 * Returns a list of all the themes
	 * 
	 * @return A collection of themes objects
	 */
	public abstract Collection<Theme> selectThemesList( );

	/**
	 * Returns the list of the themes in form of a reference list
	 * 
	 * @return the themes list in form of a ReferenceList object
	 */
	public abstract ReferenceList getThemesList( );

}