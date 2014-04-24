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

import fr.paris.lutece.test.LuteceTestCase;

import java.util.ArrayList;
import java.util.List;


/**
 * Paginator Test Class
 *
 */
public class PaginatorTest extends LuteceTestCase
{
    private static final int NB_ITEMS_PER_PAGE = 5;
    private static final int ITEMS_COUNT = 14;
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String BASE_URL = "http://myhost/mypage.jsp";

    public void testPaginator(  )
    {
        List list = new ArrayList(  );

        for ( int i = 0; i < ITEMS_COUNT; i++ )
        {
            list.add( "Item" + i );
        }

        String strPageIndex = "1";
        Paginator paginator = new Paginator( list, NB_ITEMS_PER_PAGE, BASE_URL, PARAMETER_PAGE_INDEX, strPageIndex );

        assertEquals( ITEMS_COUNT, paginator.getItemsCount(  ) );
        assertEquals( 1, paginator.getRangeMin(  ) );
        assertEquals( 5, paginator.getRangeMax(  ) );
        assertEquals( 3, paginator.getPagesCount(  ) );
        assertEquals( 1, paginator.getPageCurrent(  ) );
        assertEquals( BASE_URL + "?" + PARAMETER_PAGE_INDEX + "=" + 2, paginator.getNextPageLink(  ) );
        assertEquals( 5, paginator.getPageItems(  ).size(  ) );

        strPageIndex = "3";
        paginator = new Paginator( list, NB_ITEMS_PER_PAGE, BASE_URL, PARAMETER_PAGE_INDEX, strPageIndex );

        assertEquals( ITEMS_COUNT, paginator.getItemsCount(  ) );
        assertEquals( 11, paginator.getRangeMin(  ) );
        //        assertEquals( 14 , paginator.getRangeMax() );
        assertEquals( 3, paginator.getPagesCount(  ) );
        assertEquals( 3, paginator.getPageCurrent(  ) );
        assertEquals( BASE_URL + "?" + PARAMETER_PAGE_INDEX + "=" + 2, paginator.getPreviousPageLink(  ) );
        assertEquals( 4, paginator.getPageItems(  ).size(  ) );

        // List test
        paginator.getPagesLinks(  );
    }
}
