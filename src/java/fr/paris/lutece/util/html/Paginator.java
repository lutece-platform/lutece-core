/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Paginator provides a way to display a collection of items on severals pages.
 * @param <E> Item of the list
 */
public class Paginator<E>
{
    /** Default value for Page Index Parameter */
    public static final String PARAMETER_PAGE_INDEX = "page_index";

    /** Default value for Items Per Page Parameter */
    public static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    public static final String LABEL_FIRST = "|&lt;";
    public static final String LABEL_PREVIOUS = "&lt;";
    public static final String LABEL_NEXT = "&gt;";
    public static final String LABEL_LAST = "&gt;|";

    // Variables declarations
    private String _strPageIndexParameterName;
    private int _nItemPerPage;
    private String _strBaseUrl;
    private List<E> _list;
    private int _nPageCurrent;
    private String _strItemsPerPageParameterName;

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
        _nItemPerPage = nItemPerPage;
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
         * @param strItemsPerPageParameterName The parameter name of the number items per page
     */
    public Paginator( List<E> list, int nItemPerPage, String strBaseUrl, String strPageIndexParameterName,
        String strPageIndex, String strItemsPerPageParameterName )
    {
        this( list, nItemPerPage, strBaseUrl, strPageIndexParameterName, strPageIndex );
        _strItemsPerPageParameterName = strItemsPerPageParameterName;
    }

    /**
    * Gets the number of pages
    * @return the number of pages
    */

    // Declared as final because used by the constructor (PMD issue)
    public final int getPagesCount(  )
    {
        return ( ( _list.size(  ) - 1 ) / _nItemPerPage ) + 1;
    }

    /**
     * Returns the List
     *
     * @return The List
     */
    public List<E> getPageItems(  )
    {
        int nStartIndex = ( _nPageCurrent - 1 ) * _nItemPerPage;
        int nMax = _list.size(  );
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

    /**
     * Returns the current page index
     *
     * @return The current page index
     */
    public int getPageCurrent(  )
    {
        return _nPageCurrent;
    }

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    public String getFirstPageLink(  )
    {
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + 1 );

        return url.getUrl(  );
    }

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    public String getPreviousPageLink(  )
    {
        int nPreviousIndex = _nPageCurrent - 1;
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + nPreviousIndex );

        return url.getUrl(  );
    }

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    public String getLastPageLink(  )
    {
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + getPagesCount(  ) );

        return url.getUrl(  );
    }

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    public String getNextPageLink(  )
    {
        int nNextIndex = _nPageCurrent + 1;
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + nNextIndex );

        return url.getUrl(  );
    }

    /**
     * Returns Pages Links
     *
     * @return Pages Links
     */
    public List<PaginatorPage> getPagesLinks(  )
    {
        List<PaginatorPage> list = new ArrayList<PaginatorPage>(  );

        // Number of link pages in total
        int nNbLinkPages = getPagesCount(  );

        // Max number of link pages displayed
        int nNbLinkPagesToDisplay = 10;

        // Number of previous link pages to display 
        int nOffsetPrev = nNbLinkPagesToDisplay / 2;

        // Number of next link pages to display
        int nOffsetNext = nNbLinkPagesToDisplay / 2;

        // Set the offsets
        if ( _nPageCurrent <= ( nNbLinkPagesToDisplay - nOffsetPrev ) )
        {
            // If the current page is within the first 5 pages
            nOffsetPrev = _nPageCurrent - 1;
            nOffsetNext = nNbLinkPagesToDisplay - nOffsetPrev;
        }
        else if ( ( _nPageCurrent + nOffsetNext ) > nNbLinkPages )
        {
            // If the current page is within the last 5 pages
            nOffsetNext = nNbLinkPages - _nPageCurrent;
            nOffsetPrev = nNbLinkPagesToDisplay - nOffsetNext;
        }

        // Index of the last number of the link page to display
        int nMax = nNbLinkPages;

        // Index of the first number of the link page to display
        int nMin = 1;

        int nTmpMin = _nPageCurrent - nOffsetPrev;
        int nTmpMax = _nPageCurrent + nOffsetNext;

        if ( nTmpMax < nMax )
        {
            nMax = nTmpMax;
        }

        if ( nTmpMin > 0 )
        {
            nMin = nTmpMin;
        }

        for ( int i = nMin; i <= nMax; i++ )
        {
            PaginatorPage page = new PaginatorPage(  );
            String strIndex = "" + i;
            UrlItem url = new UrlItem( _strBaseUrl );
            url.addParameter( _strPageIndexParameterName, strIndex );
            page.setUrl( url.getUrl(  ) );
            page.setName( strIndex );
            page.setIndex( i );
            list.add( page );
        }

        return list;
    }

    /**
     * Returns the index of the first item of the current page range
     * @return The index of the first item of the current page range
     */
    public int getRangeMin(  )
    {
        return ( !_list.isEmpty(  ) ) ? ( ( _nItemPerPage * ( _nPageCurrent - 1 ) ) + 1 ) : 0;
    }

    /**
     * Returns the index of the last item of the current page range
     * @return The index of the last item of the current page range
     */
    public int getRangeMax(  )
    {
        return ( _list.size(  ) < ( ( _nItemPerPage * _nPageCurrent ) - 1 ) ) ? _list.size(  )
                                                                              : ( _nItemPerPage * _nPageCurrent );
    }

    /**
     * Returns the number of items in the collection
     * @return The number of items in the collection
     */
    public int getItemsCount(  )
    {
        return _list.size(  );
    }

    /**
     * Gets the number of items per page from a request parameter
     * @param request The HTTP request
     * @param strParameter The request parameter name
     * @param nCurrent The current number of items
     * @param nDefault The default number of items
     * @return The new number of items
     */
    public static int getItemsPerPage( HttpServletRequest request, String strParameter, int nCurrent, int nDefault )
    {
        int nItemsPerPage;
        String strItemsPerPage = request.getParameter( strParameter );

        if ( strItemsPerPage != null )
        {
            nItemsPerPage = Integer.parseInt( strItemsPerPage );
        }
        else
        {
            if ( nCurrent != 0 )
            {
                nItemsPerPage = nCurrent;
            }
            else
            {
                nItemsPerPage = nDefault;
            }
        }

        return nItemsPerPage;
    }

    /**
     * Gets the new page index from a request parameter
     * @param request The HTTP request
     * @param strParameter The request parameter name
     * @param strCurrentPageIndex The current page index
     * @return The new page index
     */
    public static String getPageIndex( HttpServletRequest request, String strParameter, String strCurrentPageIndex )
    {
        String strPageIndex = request.getParameter( strParameter );
        strPageIndex = ( strPageIndex != null ) ? strPageIndex : strCurrentPageIndex;

        return strPageIndex;
    }

    /**
     * Get First label
     * @return The Label
     */
    public String getLabelFirst(  )
    {
        return LABEL_FIRST;
    }

    /**
     * Get Previous label
     * @return The Label
     */
    public String getLabelPrevious(  )
    {
        return LABEL_PREVIOUS;
    }

    /**
     * Get Next label
     * @return The Label
     */
    public String getLabelNext(  )
    {
        return LABEL_NEXT;
    }

    /**
     * Get First label
     * @return The Label
     */
    public String getLabelLast(  )
    {
        return LABEL_LAST;
    }

    /**
     * Get Item Count label
     * @return The Label
     */
    public String getLabelItemCount(  )
    {
        return "";
    }

    /**
     * Get Item Count per page label
     * @return The Label
     */
    public String getLabelItemCountPerPage(  )
    {
        return "";
    }

    /**
     * Get the parameter name of the <code>items_per_page</code>
     * @return the parameter name
     */
    public String getItemsPerPageParameterName(  )
    {
        return _strItemsPerPageParameterName;
    }

    /**
     * Set the parameter name of the <code>items_per_page</code>
     * @param strItemsPerPageParameterName the parameter name
     */
    public void setItemsPerPageParameterName( String strItemsPerPageParameterName )
    {
        _strItemsPerPageParameterName = strItemsPerPageParameterName;
    }
}
