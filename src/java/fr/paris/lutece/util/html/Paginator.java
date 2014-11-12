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

import java.util.ArrayList;
import java.util.List;


/**
 * Paginator provides a way to display a collection of items on severals pages.
 * <strong>This implementation requires all items</strong>
 * @param <E> Item of the list
 */
public class Paginator<E> extends AbstractPaginator<E>
{
    private static final long serialVersionUID = 1531851701326259139L;

    /**
     * Creates a new instance of Paginator
     * @param list The collection to paginate
     * @param nItemPerPage Number of items to display per page
     * @param strBaseUrl The base Url for build links on each page link
     * @param strPageIndexParameterName The parameter name for the page index
     * @param strPageIndex The current page index
     */
    public Paginator( List<E> list, int nItemPerPage, String strBaseUrl, String strPageIndexParameterName,
        String strPageIndex )
    {
        _list = list;
        _nItemsCount = _list.size(  );
        _nItemPerPage = ( nItemPerPage <= 0 ) ? 1 : nItemPerPage;
        _strBaseUrl = strBaseUrl;
        _strPageIndexParameterName = strPageIndexParameterName;
        _strItemsPerPageParameterName = PARAMETER_ITEMS_PER_PAGE;

        try
        {
            _nPageCurrent = Integer.parseInt( strPageIndex );
        }
        catch ( NumberFormatException e )
        {
            // strPageIndex invalid, use 1 as default page index.
            _nPageCurrent = 1;
        }

        if ( _nPageCurrent > getPagesCount(  ) )
        {
            _nPageCurrent = 1;
        }
    }

    /**
     * Creates a new instance of Paginator
     * @param list The collection to paginate
     * @param nItemPerPage Number of items to display per page
     * @param strBaseUrl The base Url for build links on each page link
     * @param strPageIndexParameterName The parameter name for the page index
     * @param strPageIndex The current page index
     * @param strItemsPerPageParameterName The parameter name of the number
     *            items per page
     */
    public Paginator( List<E> list, int nItemPerPage, String strBaseUrl, String strPageIndexParameterName,
        String strPageIndex, String strItemsPerPageParameterName )
    {
        this( list, nItemPerPage, strBaseUrl, strPageIndexParameterName, strPageIndex );
        _strItemsPerPageParameterName = strItemsPerPageParameterName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<E> getPageItems(  )
    {
        int nStartIndex = ( _nPageCurrent - 1 ) * _nItemPerPage;
        int nMax = _nItemsCount;
        int nMaxPage = nStartIndex + _nItemPerPage;

        if ( nMaxPage < nMax )
        {
            nMax = nMaxPage;
        }

        List<E> list = new ArrayList<E>(  );

        for ( int i = nStartIndex; i < nMax; i++ )
        {
            list.add( _list.get( i ) );
        }

        return list;
    }
}
