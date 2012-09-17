package fr.paris.lutece.util.datatable;

public class DataTablePaginationProperties
{
	private int _nItemsPerPage;
	private int _nCurrentPageIndex;

	/**
	 * Get the number of items to display per page
	 * @return The number of items to display per page
	 */
	public int getItemsPerPage( )
	{
		return _nItemsPerPage;
	}

	/**
	 * Set the number of items to display per page
	 * @param nItemsPerPage The number of items to display per page
	 */
	public void setItemsPerPage( int nItemsPerPage )
	{
		_nItemsPerPage = nItemsPerPage;
	}

	/**
	 * Get the current page index
	 * @return The current page index
	 */
	public int getCurrentPageIndex( )
	{
		return _nCurrentPageIndex;
	}

	/**
	 * Set the current page index
	 * @param nCurrentPageIndex The current page index
	 */
	public void setCurrentPageIndex( int nCurrentPageIndex )
	{
		_nCurrentPageIndex = nCurrentPageIndex;
	}

}
