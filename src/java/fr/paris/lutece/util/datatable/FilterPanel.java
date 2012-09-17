package fr.paris.lutece.util.datatable;

import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.List;


/**
 * Class to manage filter panel
 */
public class FilterPanel
{
	public static final String PARAM_FILTER_PANEL_PREFIX = "filter_panel_";
	public static final String PARAM_UPDATE_FILTERS = "updateFilters";
	public static final String PARAM_RESET_FILTERS = "resetFilters";

	private String _strFormUrl;
	private List<DataTableFilter> listFilter = new ArrayList<DataTableFilter>( );

	protected FilterPanel( )
	{

	}

	/**
	 * Creates a new FilterPanel.
	 * @param strFormUrl The url to validate the form of filters
	 */
	public FilterPanel( String strFormUrl )
	{
		_strFormUrl = strFormUrl;
	}

	/**
	 * Add a filter to this filter panel
	 * @param filterType data type of the filter. For drop down list, use {@link FilterPanel#addDropDownListFilter(String, String, ReferenceList) addDropDownListFilter} instead
	 * @param strParameterName Name of the parameter of the object to filter.<br/>
	 * For example, if this filter should be applied on the parameter "title" of a class named "Data", then the value of the parameter <i>strParameterName</i> should be "title".
	 * @param strFilterLabel Label describing the filter
	 */
	public void addFilter( DataTableFilterType filterType, String strParameterName, String strFilterLabel )
	{
		listFilter.add( new DataTableFilter( filterType, strParameterName, strFilterLabel ) );
	}

	/**
	 * Add a drop down list filter.
	 * @param strParameterName Name of the parameter of the object to filter.<br/>
	 * For example, if this filter should be applied on the parameter "title" of a class named "Data", then the value of the parameter <i>strParameterName</i> should be "title".
	 * @param strFilterLabel Label describing the filter
	 * @param refList Reference list containing data of the drop down list
	 */
	public void addDropDownListFilter( String strParameterName, String strFilterLabel, ReferenceList refList )
	{
		DataTableFilter filter = new DataTableFilter( DataTableFilterType.DROPDOWNLIST, strParameterName, strFilterLabel );
		filter.setRefList( refList );
		listFilter.add( filter );
	}

	/**
	 * Get the url of the action of the form
	 * @return The url of the action of the form
	 */
	public String getFormUrl( )
	{
		return _strFormUrl;
	}

	/**
	 * Set the url of the action of the form
	 * @param strFormUrl The url of the action of the form
	 */
	public void setFormUrl( String strFormUrl )
	{
		_strFormUrl = strFormUrl;
	}

	/**
	 * Get the list of filters of the FilterPanel
	 * @return The list of filters of the FilterPanel
	 */
	public List<DataTableFilter> getListFilter( )
	{
		return listFilter;
	}

}
