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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Abstract Paginator
 * @param <E> the type
 */
public abstract class AbstractPaginator<E> implements IPaginator<E>
{
    /** Default value for Page Index Parameter */
    public static final String PARAMETER_PAGE_INDEX = "page_index";

    /** Default value for Items Per Page Parameter */
    public static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    public static final String LABEL_FIRST = "|&lt;";
    public static final String LABEL_PREVIOUS = "&lt;";
    public static final String LABEL_NEXT = "&gt;";
    public static final String LABEL_LAST = "&gt;|";
    private static final long serialVersionUID = -2795417742672443863L;

    // Variables declarations
    /** page index parameter name */
    protected String _strPageIndexParameterName;

    /** items per page */
    protected int _nItemPerPage;

    /** base url */
    protected String _strBaseUrl;

    /** the actual list */
    protected List<E> _list;

    /** the current page */
    protected int _nPageCurrent;

    /** items per page parameter name */
    protected String _strItemsPerPageParameterName;

    /** element count */
    protected int _nItemsCount;

    /**
     *{@inheritDoc}
     */
    @Override
    public int getPageCurrent(  )
    {
        return _nPageCurrent;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getFirstPageLink(  )
    {
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + 1 );

        return url.getUrl(  );
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getPreviousPageLink(  )
    {
        int nPreviousIndex = _nPageCurrent - 1;
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + nPreviousIndex );

        return url.getUrl(  );
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getLastPageLink(  )
    {
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + getPagesCount(  ) );

        return url.getUrl(  );
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public int getItemsPerPage(  )
    {
        return _nItemPerPage;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getNextPageLink(  )
    {
        int nNextIndex = _nPageCurrent + 1;
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + nNextIndex );

        return url.getUrl(  );
    }

    /**
     *{@inheritDoc}
     */
    @Override
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
     *{@inheritDoc}
     */
    @Override
    public String getLabelFirst(  )
    {
        return LABEL_FIRST;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getLabelPrevious(  )
    {
        return LABEL_PREVIOUS;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getLabelNext(  )
    {
        return LABEL_NEXT;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getLabelLast(  )
    {
        return LABEL_LAST;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getLabelItemCount(  )
    {
        return "";
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getLabelItemCountPerPage(  )
    {
        return "";
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getItemsPerPageParameterName(  )
    {
        return _strItemsPerPageParameterName;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void setItemsPerPageParameterName( String strItemsPerPageParameterName )
    {
        _strItemsPerPageParameterName = strItemsPerPageParameterName;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int getPagesCount(  )
    {
        return ( ( _nItemsCount - 1 ) / _nItemPerPage ) + 1;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int getRangeMin(  )
    {
        return ( !_list.isEmpty(  ) ) ? ( ( _nItemPerPage * ( _nPageCurrent - 1 ) ) + 1 ) : 0;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int getRangeMax(  )
    {
        return ( _nItemsCount < ( ( _nItemPerPage * _nPageCurrent ) - 1 ) ) ? _nItemsCount : ( _nItemPerPage * _nPageCurrent );
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int getItemsCount(  )
    {
        return _nItemsCount;
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
}
