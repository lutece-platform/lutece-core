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
package fr.paris.lutece.util.datatable;

import fr.paris.lutece.util.ReferenceList;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


/**
 * Class to manage filter panel
 */
public class FilterPanel implements Serializable
{
    public static final String PARAM_FILTER_PANEL_PREFIX = "filter_panel_";
    public static final String PARAM_UPDATE_FILTERS = "updateFilters";
    public static final String PARAM_RESET_FILTERS = "resetFilters";
    private static final long serialVersionUID = 761740458592056772L;
    private String _strFormUrl;
    private ArrayList<DataTableFilter> _listFilter = new ArrayList<DataTableFilter>(  );

    /**
     * Instantiates a new filter panel.
     */
    protected FilterPanel(  )
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
        _listFilter.add( new DataTableFilter( filterType, strParameterName, strFilterLabel ) );
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
        _listFilter.add( filter );
    }

    /**
     * Get the url of the action of the form
     * @return The url of the action of the form
     */
    public String getFormUrl(  )
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
    public List<DataTableFilter> getListFilter(  )
    {
        return _listFilter;
    }
}
