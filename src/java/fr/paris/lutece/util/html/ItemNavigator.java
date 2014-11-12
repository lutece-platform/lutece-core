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
package fr.paris.lutece.util.html;

import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.util.List;


/**
 *
 * ItemNavigation provides a way to navigate between items
 *
 */
public class ItemNavigator implements Serializable
{
    private static final long serialVersionUID = -6240496590042563143L;
    private List<String> _listItems;
    private int _nCurrentItemId;
    private String _strBaseUrl;
    private String _strParameterName;

    /**
     * Create a new instance of ItemNavigator
     * @param listItems the list of item
     * @param nCurrentItemId The current map key
     * @param strBaseUrl The base url
     * @param strParameterName The parameter name
     */
    public ItemNavigator( List<String> listItems, int nCurrentItemId, String strBaseUrl, String strParameterName )
    {
        _listItems = listItems;
        _nCurrentItemId = nCurrentItemId;
        _strBaseUrl = strBaseUrl;
        _strParameterName = strParameterName;
    }

    /**
     * Set the current item id given the content of the item
     * @param strItem the item
     */
    public void setCurrentItemId( String strItem )
    {
        int nCurrentItemId = _nCurrentItemId;

        if ( ( strItem != null ) && ( _listItems != null ) && !_listItems.isEmpty(  ) &&
                !strItem.equals( _listItems.get( _nCurrentItemId ) ) )
        {
            int nPreviousItemId = _nCurrentItemId - 1;
            int nNextItemId = _nCurrentItemId + 1;

            if ( ( nPreviousItemId >= 0 ) && strItem.equals( _listItems.get( nPreviousItemId ) ) )
            {
                // Check if it is the previous item
                nCurrentItemId = nPreviousItemId;
            }
            else if ( ( nNextItemId < _listItems.size(  ) ) && strItem.equals( _listItems.get( nNextItemId ) ) )
            {
                // Check if it is the next item
                nCurrentItemId = nNextItemId;
            }
            else
            {
                // No choice, have to check every items
                for ( int nIndex = 0; nIndex < _listItems.size(  ); nIndex++ )
                {
                    if ( strItem.equals( _listItems.get( nIndex ) ) )
                    {
                        nCurrentItemId = nIndex;

                        break;
                    }
                }
            }
        }

        _nCurrentItemId = nCurrentItemId;
    }

    /**
     * Get the previous page link of the item
     * @return the previous page link of the item
     */
    public String getPreviousPageLink(  )
    {
        int nPreviousItemId = _nCurrentItemId - 1;

        if ( ( _listItems != null ) && !_listItems.isEmpty(  ) && ( nPreviousItemId >= 0 ) )
        {
            UrlItem url = new UrlItem( _strBaseUrl );
            url.addParameter( _strParameterName, _listItems.get( nPreviousItemId ) );

            return url.getUrl(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Get the next page link of the item
     * @return the next page link of the item
     */
    public String getNextPageLink(  )
    {
        int nNextItemId = _nCurrentItemId + 1;

        if ( ( _listItems != null ) && !_listItems.isEmpty(  ) && ( nNextItemId < _listItems.size(  ) ) )
        {
            UrlItem url = new UrlItem( _strBaseUrl );
            url.addParameter( _strParameterName, _listItems.get( nNextItemId ) );

            return url.getUrl(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Get the Url of he navigator
     * @return The Url of the navigator
     */
    public String getBaseUrl(  )
    {
        return _strBaseUrl;
    }

    /**
     * Set the Url of he navigator
     * @param strBaseUrl The new Url of the navigator
     */
    public void setBaseUrl( String strBaseUrl )
    {
        _strBaseUrl = strBaseUrl;
    }

    /**
     * Get the size of the item list
     * @return the size of the item list
     */
    public int getListItemSize(  )
    {
        if ( _listItems != null )
        {
            return _listItems.size(  );
        }

        return 0;
    }

    /**
     * Get the current item map key
     * @return the current item map key
     */
    public int getCurrentItemId(  )
    {
        return _nCurrentItemId;
    }
}
