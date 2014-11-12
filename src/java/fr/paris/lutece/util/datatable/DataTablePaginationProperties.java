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


/**
 * The Class DataTablePaginationProperties.
 */
public class DataTablePaginationProperties
{
    private int _nItemsPerPage;
    private int _nCurrentPageIndex;

    /**
     * Get the number of items to display per page
     * @return The number of items to display per page
     */
    public int getItemsPerPage(  )
    {
        return _nItemsPerPage;
    }

    /**
     * Set the number of items to display per page
     * @param nItemsPerPage The number of items to display per page
     */
    public void setItemsPerPage( int nItemsPerPage )
    {
        _nItemsPerPage = nItemsPerPage;
    }

    /**
     * Get the current page index
     * @return The current page index
     */
    public int getCurrentPageIndex(  )
    {
        return _nCurrentPageIndex;
    }

    /**
     * Set the current page index
     * @param nCurrentPageIndex The current page index
     */
    public void setCurrentPageIndex( int nCurrentPageIndex )
    {
        _nCurrentPageIndex = nCurrentPageIndex;
    }
}
