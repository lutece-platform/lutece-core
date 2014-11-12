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
package fr.paris.lutece.portal.service.search;

import java.util.Date;
import java.util.List;


/**
 * SearchResult
 */
public class SearchResult
{
    // Variables declarations
    private String _strId;
    private Date _date;
    private String _strUrl;
    private String _strTitle;
    private String _strSummary;
    private String _strType;
    private List<String> _listRoles;

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the Id
     *
     * @param strId The Id
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the Date
     *
     * @return The Date
     */
    public Date getDate(  )
    {
        return _date;
    }

    /**
     * Sets the Date
     *
     * @param date The Date
     */
    public void setDate( Date date )
    {
        _date = date;
    }

    /**
     * Returns the Url
     *
     * @return The Url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     *
     * @param strUrl The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the Title
     *
     * @return The Title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Sets the Title
     *
     * @param strTitle The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the Summary
     *
     * @return The Summary
     */
    public String getSummary(  )
    {
        return _strSummary;
    }

    /**
     * Sets the Summary
     *
     * @param strSummary The Summary
     */
    public void setSummary( String strSummary )
    {
        _strSummary = strSummary;
    }

    /**
     * Returns the Type
     *
     * @return The Type
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * Sets the Type
     *
     * @param strType The Type
     */
    public void setType( String strType )
    {
        _strType = strType;
    }

    /**
     * Return the list of roles
     * @return the list of roles
     */
    public List<String> getRoles(  )
    {
        return _listRoles;
    }

    /**
     * Sets the list of roles
     * @param listRoles the list of roles
     */
    public void setRole( List<String> listRoles )
    {
        _listRoles = listRoles;
    }
}
