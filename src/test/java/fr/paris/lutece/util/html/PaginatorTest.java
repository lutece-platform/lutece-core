/*
 * Copyright (c) 2002-2022, City of Paris
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.mocks.MockHttpServletRequest;

/**
 * Paginator Test Class
 *
 */
public class PaginatorTest
{
    private static final int NB_ITEMS_PER_PAGE = 5;
    private static final int ITEMS_COUNT = 14;
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String BASE_URL = "http://myhost/mypage.jsp";

    @Test
    public void testPaginator( )
    {
        List<String> list = new ArrayList<>( );

        for ( int i = 0; i < ITEMS_COUNT; i++ )
        {
            list.add( "Item" + i );
        }

        String strPageIndex = "1";
        Paginator<?> paginator = new Paginator<>( list, NB_ITEMS_PER_PAGE, BASE_URL, PARAMETER_PAGE_INDEX, strPageIndex );

        assertEquals( ITEMS_COUNT, paginator.getItemsCount( ) );
        assertEquals( 1, paginator.getRangeMin( ) );
        assertEquals( 5, paginator.getRangeMax( ) );
        assertEquals( 3, paginator.getPagesCount( ) );
        assertEquals( 1, paginator.getPageCurrent( ) );
        assertEquals( BASE_URL + "?" + PARAMETER_PAGE_INDEX + "=" + 2, paginator.getNextPageLink( ) );
        assertEquals( 5, paginator.getPageItems( ).size( ) );

        strPageIndex = "3";
        paginator = new Paginator<>( list, NB_ITEMS_PER_PAGE, BASE_URL, PARAMETER_PAGE_INDEX, strPageIndex );

        assertEquals( ITEMS_COUNT, paginator.getItemsCount( ) );
        assertEquals( 11, paginator.getRangeMin( ) );
        assertEquals( 14, paginator.getRangeMax( ) );
        assertEquals( 3, paginator.getPagesCount( ) );
        assertEquals( 3, paginator.getPageCurrent( ) );
        assertEquals( BASE_URL + "?" + PARAMETER_PAGE_INDEX + "=" + 2, paginator.getPreviousPageLink( ) );
        assertEquals( 4, paginator.getPageItems( ).size( ) );

        // List test
        paginator.getPagesLinks( );
    }

    /**
     * A page index of 0 or less shows the first page
     */
    @Test
    public void testPageIndexBelowOne( )
    {
        List<String> list = new ArrayList<>( );

        for ( int i = 0; i < ITEMS_COUNT; i++ )
        {
            list.add( "Item" + i );
        }

        for ( String strPageIndex : new String [ ] {
                "0", "-1", "-100"
        } )
        {
            Paginator<?> paginator = new Paginator<>( list, NB_ITEMS_PER_PAGE, BASE_URL, PARAMETER_PAGE_INDEX, strPageIndex );

            assertEquals( 1, paginator.getPageCurrent( ) );
            assertEquals( 1, paginator.getRangeMin( ) );
            assertEquals( 5, paginator.getPageItems( ).size( ) );
            assertEquals( 5, paginator.getPageItems( 0 ).size( ) );
            assertEquals( 1, paginator.getPageCurrent( ) );
        }
    }

    /**
     * The range of a page never goes past the last item
     */
    @Test
    public void testRangeMaxOfASinglePage( )
    {
        List<String> list = new ArrayList<>( );
        list.add( "Item" );

        assertEquals( 1, new Paginator<>( list, 2, BASE_URL, PARAMETER_PAGE_INDEX, "1" ).getRangeMax( ) );
        assertEquals( 1, new Paginator<>( list, 1, BASE_URL, PARAMETER_PAGE_INDEX, "1" ).getRangeMax( ) );
        assertEquals( 1, new Paginator<>( list, 10, BASE_URL, PARAMETER_PAGE_INDEX, "1" ).getRangeMax( ) );
    }

    /**
     * A number of items per page typed in the URL that is not a positive number keeps the current one, or the default
     */
    @Test
    public void testItemsPerPageNotANumber( )
    {
        for ( String strValue : new String [ ] { "abc", "0", "-5", " " } )
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.setParameter( "items_per_page", strValue );

            assertEquals( 20, AbstractPaginator.getItemsPerPage( request, "items_per_page", 20, 50 ) );
            assertEquals( 50, AbstractPaginator.getItemsPerPage( request, "items_per_page", 0, 50 ) );
        }

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setParameter( "items_per_page", "10" );
        assertEquals( 10, AbstractPaginator.getItemsPerPage( request, "items_per_page", 20, 50 ) );
    }
}
