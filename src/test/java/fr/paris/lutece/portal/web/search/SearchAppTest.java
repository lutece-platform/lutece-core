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
package fr.paris.lutece.portal.web.search;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.search.SearchParameterHome;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.ReferenceItem;
import jakarta.inject.Inject;

/**
 * SearchApp Test Class
 *
 */
public class SearchAppTest extends LuteceTestCase
{
    private @Inject SearchApp instance;

    /**
     * Test of getPage method, of class fr.paris.lutece.portal.web.search.SearchApp.
     * 
     * @throws SiteMessageException
     */
    @Test
    public void testGetPage( ) throws SiteMessageException
    {
        System.out.println( "getPage" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "query", "lutece" );
        request.addParameter( "items_per_page", "5" );

        int nMode = 0;
        Plugin plugin = null;

        assertNotNull( instance.getPage( request, nMode, plugin ) );
    }

    /**
     * A number of results per page that is not a number gives the default one
     */
    @Test
    public void testGetPageInvalidItemsPerPage( ) throws SiteMessageException
    {
        for ( String strItemsPerPage : new String [ ] {
                "abc", "-1", "0"
        } )
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "query", "lutece" );
            request.addParameter( "items_per_page", strItemsPerPage );

            assertNotNull( instance.getPage( request, 0, null ) );
        }
    }

    /**
     * A query with double quotes (a phrase of the Lucene syntax, or an unclosed quote) is searched, not refused as invalid; the other
     * characters refused in the inputs still refuse the query
     */
    @Test
    public void testGetPageQueryWithDoubleQuotes( ) throws SiteMessageException
    {
        for ( String strQuery : new String [ ] {
                "\"lutece portal\"", "\"lutece"
        } )
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "query", strQuery );
            String strContent = instance.getPage( request, 0, null ).getContent( );
            assertFalse( strContent.contains( "Invalid search term" ) || strContent.contains( "Terme(s) de recherche invalide(s)" ), strQuery );
        }

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "query", "<b>lutece" );
        String strContent = instance.getPage( request, 0, null ).getContent( );
        assertTrue( strContent.contains( "Invalid search term" ) || strContent.contains( "Terme(s) de recherche invalide(s)" ) );
    }

    /**
     * The date pickers of the date filter post the date in the format the server parses (not the ISO default of the picker)
     */
    @Test
    public void testDateFilterPostsTheServerFormat( ) throws SiteMessageException
    {
        ReferenceItem dateFilter = SearchParameterHome.findByKey( "date_filter" );
        String strSaved = dateFilter.getName( );

        try
        {
            dateFilter.setName( "1" );
            SearchParameterHome.update( dateFilter );

            MockHttpServletRequest request = new MockHttpServletRequest( );
            request.addParameter( "query", "lutece" );
            request.addParameter( "date_after", "01/01/2020" );

            String strContent = instance.getPage( request, 0, null ).getContent( );

            assertTrue( strContent.contains( "name=\"date_after\"" ), "the date filter should be shown" );
            assertFalse( strContent.contains( "let dataFormat='yyyy-m-d 00:00:00'" ), "the pickers should post the server format" );
        }
        finally
        {
            dateFilter.setName( strSaved );
            SearchParameterHome.update( dateFilter );
        }
    }
}
