package fr.paris.lutece.util.datatable;

import fr.paris.lutece.util.ReferenceList;


/**
 * Class to filter data with a DataTableManager
 */
public class DataTableFilter
{
	private DataTableFilterType _filterType;
	private String _strParameterName;
	private String _strFilterLabel;
	private String _strValue;
	private ReferenceList _refList;

	/**
	 * Creates a new filter
	 * @param filterType The type of the filter
	 * @param strParameterName The name of the parameter to filter
	 * @param strFilterLabel The label of the filter
	 */
	protected DataTableFilter( DataTableFilterType filterType, String strParameterName, String strFilterLabel )
	{
		_filterType = filterType;
		_strParameterName = strParameterName;
		_strFilterLabel = strFilterLabel;
	}

	/**
	 * Get the type of the filter
	 * @return The type of the filter
	 */
	public DataTableFilterType getFilterType( )
	{
		return _filterType;
	}

	/**
	 * Set the type of the filter
	 * @param filterType The type of the filter
	 */
	protected void setFilterType( DataTableFilterType filterType )
	{
		_filterType = filterType;
	}

	/**
	 * Get the name of the parameter to filter
	 * @return The name of the parameter to filter
	 */
	public String getParameterName( )
	{
		return _strParameterName;
	}

	/**
	 * Set the name of the parameter to filter
	 * @param strParameterName The name of the parameter to filter
	 */
	protected void setParameterName( String strParameterName )
	{
		_strParameterName = strParameterName;
	}

	/**
	 * Get the label of the filter
	 * @return The label of the filter
	 */
	public String getFilterLabel( )
	{
		return _strFilterLabel;
	}

	/**
	 * Set the label of the filter
	 * @param strFilterLabel The label of the filter
	 */
	protected void setFilterLabel( String strFilterLabel )
	{
		_strFilterLabel = strFilterLabel;
	}

	/**
	 * Get the reference list of this filter
	 * @return The reference list of this filter
	 */
	public ReferenceList getRefList( )
	{
		return _refList;
	}

	/**
	 * Set the reference list of this filter
	 * @param refList The reference list of this filter
	 */
	protected void setRefList( ReferenceList refList )
	{
		_refList = refList;
	}

	/**
	 * Get the current value of the filter
	 * @return The current value of the filter
	 */
	public String getValue( )
	{
		return _strValue;
	}

	/**
	 * Set the current value of the filter
	 * @param strValue The current value of the filter
	 */
	protected void setValue( String strValue )
	{
		_strValue = strValue;
	}
}
