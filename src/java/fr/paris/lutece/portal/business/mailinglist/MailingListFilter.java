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
package fr.paris.lutece.portal.business.mailinglist;

import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;


/**
 *
 * MailingListFilter
 *
 */
public class MailingListFilter implements Serializable
{
    private static final long serialVersionUID = -2176789297248798592L;
    private static final String SQL_FILTER_NAME = " name LIKE ? ";
    private static final String SQL_FILTER_DESCRIPTION = " description LIKE ? ";
    private static final String SQL_FILTER_WORKGROUP = " workgroup = ? ";
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_OR = " OR ";
    private static final String PERCENT = "%";
    private String _strName;
    private String _strDescription;
    private String _strWorkgroup;
    private boolean _bIsWideSearch;

    /**
     * Instantiates a new mailing list filter.
     */
    public MailingListFilter(  )
    {
    }

    /**
     * Instantiates a new mailing list filter.
     *
     * @param filter the filter
     */
    public MailingListFilter( MailingListFilter filter )
    {
        _strName = filter.getName(  );
        _strDescription = filter.getDescription(  );
        _strWorkgroup = filter.getWorkgroup(  );
        _bIsWideSearch = filter.isWideSearch(  );
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Contains name.
     *
     * @return true, if successful
     */
    public boolean containsName(  )
    {
        return StringUtils.isNotBlank( _strName );
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Contains description.
     *
     * @return true, if successful
     */
    public boolean containsDescription(  )
    {
        return StringUtils.isNotBlank( _strDescription );
    }

    /**
     * Returns the Workgroup
     *
     * @return The Workgroup
     */
    public String getWorkgroup(  )
    {
        return _strWorkgroup;
    }

    /**
     * Sets the Workgroup
     *
     * @param strWorkgroup The Workgroup
     */
    public void setWorkgroup( String strWorkgroup )
    {
        _strWorkgroup = strWorkgroup;
    }

    /**
     * Contains workgroup.
     *
     * @return true, if successful
     */
    public boolean containsWorkgroup(  )
    {
        return StringUtils.isNotBlank( _strWorkgroup );
    }

    /**
     * Set true if the search is wide, false otherwise
     * @param isWideSearch true if the search is wide, false otherwise
     */
    public void setWideSearch( boolean isWideSearch )
    {
        _bIsWideSearch = isWideSearch;
    }

    /**
     * Return true if the search is wide, false otherwise
     * @return true if the search is wide, false otherwise
     */
    public boolean isWideSearch(  )
    {
        return _bIsWideSearch;
    }

    /**
     * Builds the sql query.
     *
     * @param strSQL the str sql
     * @return the string
     */
    public String buildSQLQuery( String strSQL )
    {
        StringBuilder sbSQL = new StringBuilder( strSQL );
        int nIndex = 1;

        nIndex = buildFilter( sbSQL, containsName(  ), SQL_FILTER_NAME, nIndex );
        nIndex = buildFilter( sbSQL, containsDescription(  ), SQL_FILTER_DESCRIPTION, nIndex );
        buildFilter( sbSQL, containsWorkgroup(  ), SQL_FILTER_WORKGROUP, nIndex );

        return sbSQL.toString(  );
    }

    /**
     * Sets the filter values.
     *
     * @param daoUtil the new filter values
     */
    public void setFilterValues( DAOUtil daoUtil )
    {
        int nIndex = 1;

        if ( containsName(  ) )
        {
            daoUtil.setString( nIndex++, PERCENT + getName(  ) + PERCENT );
        }

        if ( containsDescription(  ) )
        {
            daoUtil.setString( nIndex++, PERCENT + getDescription(  ) + PERCENT );
        }

        if ( containsWorkgroup(  ) )
        {
            daoUtil.setString( nIndex, getWorkgroup(  ) );
        }
    }

    /**
     * Builds the filter.
     *
     * @param sbSQL the sb sql
     * @param bAddFilter the b add filter
     * @param strSQL the str sql
     * @param nIndex the n index
     * @return the int
     */
    private int buildFilter( StringBuilder sbSQL, boolean bAddFilter, String strSQL, int nIndex )
    {
        int nIndexTmp = nIndex;

        if ( bAddFilter )
        {
            nIndexTmp = addSQLWhereOr( isWideSearch(  ), sbSQL, nIndex );
            sbSQL.append( strSQL );
        }

        return nIndexTmp;
    }

    /**
     * Add a <b>WHERE</b> or a <b>OR</b> depending of the index.
     * <br/>
     * <ul>
     * <li>if <code>nIndex</code> == 1, then we add a <b>WHERE</b></li>
     * <li>if <code>nIndex</code> != 1, then we add a <b>OR</b> or a <b>AND</b> depending of the wide search characteristic</li>
     * </ul>
     * @param bIsWideSearch true if it is a wide search, false otherwise
     * @param sbSQL the SQL query
     * @param nIndex the index
     * @return the new index
     */
    private int addSQLWhereOr( boolean bIsWideSearch, StringBuilder sbSQL, int nIndex )
    {
        if ( nIndex == 1 )
        {
            sbSQL.append( SQL_WHERE );
        }
        else
        {
            sbSQL.append( bIsWideSearch ? SQL_OR : SQL_AND );
        }

        return nIndex + 1;
    }
}
