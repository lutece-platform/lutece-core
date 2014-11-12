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

import java.io.Serializable;

import java.util.List;


/**
 * Handles paging
 * @param <E> the type
 */
public interface IPaginator<E> extends Serializable
{
    /** Default value for Page Index Parameter */
    String PARAMETER_PAGE_INDEX = "page_index";

    /** Default value for Items Per Page Parameter */
    String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    String LABEL_FIRST = "|&lt;";
    String LABEL_PREVIOUS = "&lt;";
    String LABEL_NEXT = "&gt;";
    String LABEL_LAST = "&gt;|";

    /**
     * Gets the number of pages
     * @return the number of pages
     */
    int getPagesCount(  );

    /**
     * Returns the List
     *
     * @return The List
     */
    List<E> getPageItems(  );

    /**
     * Returns the current page index
     *
     * @return The current page index
     */
    int getPageCurrent(  );

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    String getFirstPageLink(  );

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    String getPreviousPageLink(  );

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    String getLastPageLink(  );

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    String getNextPageLink(  );

    /**
     * Returns Pages Links
     *
     * @return Pages Links
     */
    List<PaginatorPage> getPagesLinks(  );

    /**
     * Returns the index of the first item of the current page range
     * @return The index of the first item of the current page range
     */
    int getRangeMin(  );

    /**
     * Returns the index of the last item of the current page range
     * @return The index of the last item of the current page range
     */
    int getRangeMax(  );

    /**
     * Returns the number of items in the collection
     * @return The number of items in the collection
     */
    int getItemsCount(  );

    /**
     * Get First label
     * @return The Label
     */
    String getLabelFirst(  );

    /**
     * Get Previous label
     * @return The Label
     */
    String getLabelPrevious(  );

    /**
     * Get Next label
     * @return The Label
     */
    String getLabelNext(  );

    /**
     * Get First label
     * @return The Label
     */
    String getLabelLast(  );

    /**
     * Get Item Count label
     * @return The Label
     */
    String getLabelItemCount(  );

    /**
     * Get Item Count per page label
     * @return The Label
     */
    String getLabelItemCountPerPage(  );

    /**
     * Get the parameter name of the <code>items_per_page</code>
     * @return the parameter name
     */
    String getItemsPerPageParameterName(  );

    /**
     * Set the parameter name of the <code>items_per_page</code>
     * @param strItemsPerPageParameterName the parameter name
     */
    void setItemsPerPageParameterName( String strItemsPerPageParameterName );

    /**
     * Gets the items count per page.
     * @return items count per page.
     */
    int getItemsPerPage(  );
}
