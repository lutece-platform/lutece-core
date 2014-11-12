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
    public DataTableFilterType getFilterType(  )
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
    public String getParameterName(  )
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
    public String getFilterLabel(  )
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
    public ReferenceList getRefList(  )
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
    public String getValue(  )
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
