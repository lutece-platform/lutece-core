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

import java.io.Serializable;


/**
 * Data table column
 */
public class DataTableColumn implements Serializable
{
    private static final long serialVersionUID = 7018948403682749643L;
    private String _strTitleKey;
    private String _strParameterName;
    private boolean _bSortable;
    private DataTableColumnType _columnType;
    private String _strLabelTrue;
    private String _strLabelFalse;

    /**
     * Creates a new DataTableColumn
     * @param strTitleKey I18n key of the title of this column
     * @param strParameterName Name of the object to insert into this cells of this column
     * @param bSortable True of the column is sortable, false otherwise
     * @param typeColumn The type of data displayed by the column
     */
    public DataTableColumn( String strTitleKey, String strParameterName, boolean bSortable,
        DataTableColumnType typeColumn )
    {
        _strTitleKey = strTitleKey;
        _strParameterName = strParameterName;
        _bSortable = bSortable;
        _columnType = typeColumn;
    }

    /**
     * Creates a new boolean DataTableColumn
     * @param strTitleKey I18n key of the title of this column
     * @param strParameterName Name of the object to insert into this cells of this column
     * @param bSortable True of the column is sortable, false otherwise
     * @param columnType The type of data displayed by the column
     * @param strLabelTrue The label to display in a cell if the value is true
     * @param strLabelFalse The label to display in a cell if the value is false
     */
    public DataTableColumn( String strTitleKey, String strParameterName, boolean bSortable,
        DataTableColumnType columnType, String strLabelTrue, String strLabelFalse )
    {
        _strTitleKey = strTitleKey;
        _strParameterName = strParameterName;
        _bSortable = bSortable;
        _columnType = columnType;
        _strLabelTrue = strLabelTrue;
        _strLabelFalse = strLabelFalse;
    }

    /**
     * Get the I18n key of the title of this column
     * @return The I18n key of the title of this column
     */
    public String getTitleKey(  )
    {
        return _strTitleKey;
    }

    /**
     * Set the I18n key of the title of this column
     * @param strTitleKey The I18n key of the title of this column
     */
    public void setTitleKey( String strTitleKey )
    {
        _strTitleKey = strTitleKey;
    }

    /**
     * Get the name of the object to insert into this cells of this column
     * @return The name of the object to insert into this cells of this column
     */
    public String getParameterName(  )
    {
        return _strParameterName;
    }

    /**
     * Set the name of the object to insert into this cells of this column
     * @param strParameterName The name of the object to insert into this cells of this column
     */
    public void setParameterName( String strParameterName )
    {
        _strParameterName = strParameterName;
    }

    /**
     * Get the sortable boolean of this column
     * @return true if the column is sortable, false otherwise
     */
    public boolean getSortable(  )
    {
        return _bSortable;
    }

    /**
     * Set the sortable boolean of this column
     * @param bSortable true if the column is sortable, false otherwise
     */
    public void setSortable( boolean bSortable )
    {
        _bSortable = bSortable;
    }

    /**
     * Get the type of the column
     * @return The type of the column
     */
    public DataTableColumnType getTypeColumn(  )
    {
        return _columnType;
    }

    /**
     * Set the type of the column
     * @param columnType The type of the column
     */
    public void setTypeColumn( DataTableColumnType columnType )
    {
        _columnType = columnType;
    }

    /**
     * Get the label to print in a cell if the boolean value is true
     * @return The label to print in a cell if the boolean value is true
     */
    public String getLabelTrue(  )
    {
        return _strLabelTrue;
    }

    /**
     * Set the label to print in a cell if the boolean value is true
     * @param strLabelTrue The label to print in a cell if the boolean value is true
     */
    public void setLabelTrue( String strLabelTrue )
    {
        _strLabelTrue = strLabelTrue;
    }

    /**
     * Get the label to print in a cell if the boolean value is false
     * @return The label to print in a cell if the boolean value is false
     */
    public String getLabelFalse(  )
    {
        return _strLabelFalse;
    }

    /**
     * Set the label to print in a cell if the boolean value is false
     * @param strLabelFalse The label to print in a cell if the boolean value is false
     */
    public void setLabelFalse( String strLabelFalse )
    {
        _strLabelFalse = strLabelFalse;
    }
}
