package fr.paris.lutece.util.datatable;

public class DataTableSort
{
	private String _strSortKey;
	private boolean _bAscSort;

	/**
	 * Private constructor
	 */
	protected DataTableSort( )
	{

	}

	/**
	 * Creates a new DataTableSort
	 * @param strSortKey The key of the sort
	 * @param bAscSort True if the sort is ascending, false otherwise
	 */
	public DataTableSort( String strSortKey, boolean bAscSort )
	{
		_strSortKey = strSortKey;
		_bAscSort = bAscSort;
	}

	/**
	 * Get the sort key
	 * @return The sort key
	 */
	public String getSortKey( )
	{
		return _strSortKey;
	}

	/**
	 * Set the sort key
	 * @param strSortKey The sort key
	 */
	public void setSortKey( String strSortKey )
	{
		_strSortKey = strSortKey;
	}

	/**
	 * Check if the sort is ascending
	 * @return True if the sort is ascending, false otherwise
	 */
	public boolean getAscSort( )
	{
		return _bAscSort;
	}

	/**
	 * Set the ascending sort boolean
	 * @param bAscSort True if the sort is ascending, false otherwise
	 */
	public void setAscSort( boolean bAscSort )
	{
		_bAscSort = bAscSort;
	}

}
