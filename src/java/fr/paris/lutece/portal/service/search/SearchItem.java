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

import org.apache.lucene.document.Document;


/**
 * Search Item
 */
public class SearchItem
{
    public static final String FIELD_UID = "uid";
    public static final String FIELD_CONTENTS = "contents";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUMMARY = "summary";
    public static final String FIELD_METADATA = "metadata";
    public static final String FIELD_URL = "url";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_ROLE = "role";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_DOCUMENT_PORTLET_ID = "document_portlet_id";

    // Variables declarations
    private String _strId;
    private String _strTitle;
    private String _strSummary;
    private String _strMetadata;
    private String _strUrl;
    private String _strLastModifiedDate;
    private String _strType;
    private String _strRole;
    private String _strState;
    private String _strDocPortletId;

    /**
     * Constructor
     * @param document The document retrieved by the search
     */
    public SearchItem( Document document )
    {
        _strId = document.get( FIELD_UID );
        _strTitle = document.get( FIELD_TITLE );
        _strUrl = document.get( FIELD_URL );
        _strSummary = document.get( FIELD_SUMMARY );
        _strMetadata = document.get( FIELD_METADATA );
        _strLastModifiedDate = document.get( FIELD_DATE );
        _strType = document.get( FIELD_TYPE );
        _strDocPortletId = document.get( FIELD_DOCUMENT_PORTLET_ID );
        _strRole = document.get( FIELD_ROLE );
    }

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
     * Returns the DocPortletId
     *
     * @return The DocPortletId
     */
    public String getDocPortletId(  )
    {
        return _strDocPortletId;
    }

    /**
     * Sets the DocPortletId
     *
     * @param strDocPortletId The DocPortletId
     */
    public void setDocPortletId( String strDocPortletId )
    {
        _strDocPortletId = strDocPortletId;
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
        return ( _strSummary != null ) ? _strSummary : "";
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
     * Returns the meta data
     *
     * @return The meta data
     */
    public String getMetadata(  )
    {
        return ( _strMetadata != null ) ? _strMetadata : "";
    }

    /**
     * Sets the meta data
     *
     * @param strMetadata The meta data
     */
    public void setMetadata( String strMetadata )
    {
        _strSummary = strMetadata;
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
     * Returns the LastModifiedDate
     *
     * @return The LastModifiedDate
     */
    public String getDate(  )
    {
        return ( _strLastModifiedDate != null ) ? _strLastModifiedDate : "";
    }

    /**
     * Sets the LastModifiedDate
     *
     * @param strLastModifiedDate The LastModifiedDate
     */
    public void setDate( String strLastModifiedDate )
    {
        _strLastModifiedDate = strLastModifiedDate;
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
     * Return the role of the document
     * @return the role of the document
     */
    public String getRole(  )
    {
        return _strRole;
    }

    /**
     * Sets the role of the document
     * @param role the role of the document
     */
    public void setRole( String role )
    {
        _strRole = role;
    }

    /**
     * Return the state of the document
     * @return the state of the document
     */
    public String getState(  )
    {
        return _strState;
    }

    /**
     * Sets the state of the document
     * @param state of the document
     */
    public void setState( String state )
    {
        _strState = state;
    }
}
