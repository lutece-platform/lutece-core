/*
 * Copyright (c) 2002-2010, Mairie de Paris
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

import java.util.Map;

import fr.paris.lutece.util.url.UrlItem;

/**
 * 
 * ItemNavigation provides a way to navigate between items
 *
 */
public class ItemNavigator
{
	private Map<Integer, String> _listItem;
	private int _nCurrentItemId;
	private String _strBaseUrl;
	private String _strParameterName;
	
	/**
	 * Create a new instance of ItemNavigator
	 * @param listItem the list of item
	 * @param nCurrentItemId The current map key
	 * @param strBaseUrl The base url
	 * @param strParameterName The parameter name
	 */
	public ItemNavigator( Map<Integer, String> listItem, int nCurrentItemId, String strBaseUrl, String strParameterName )
	{
		_listItem = listItem;
		_nCurrentItemId = nCurrentItemId;
		_strBaseUrl = strBaseUrl;
		_strParameterName = strParameterName;
	}
	
	/**
	 * Get the previous page link of the item
	 * @return the previous page link of the item
	 */
	public String getPreviousPageLink(  )
	{
		int nPreviousItemId = _nCurrentItemId - 1;
		UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strParameterName, "" + _listItem.get( nPreviousItemId ) );

        return url.getUrl(  );
	}
	
	/**
	 * Get the next page link of the item
	 * @return the next page link of the item
	 */
	public String getNextPageLink(  )
	{
		int nNextItemId = _nCurrentItemId + 1;
		UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strParameterName, "" + _listItem.get( nNextItemId ) );

        return url.getUrl(  );
	}
	
	/**
	 * Get the size of the item list
	 * @return the size of the item list
	 */
	public int getListItemSize(  )
	{
		return _listItem.size(  );
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
