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
package fr.paris.lutece.portal.service.filter;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.List;


/**
 * FilterService Test
 */
public class FilterServiceTest extends LuteceTestCase
{
    /**
     * Test of getInstance method, of class FilterService.
     */
    public void testGetInstance(  )
    {
        System.out.println( "getInstance" );

        FilterService result = FilterService.getInstance(  );
        assertNotNull( result );
    }

    /**
     * Test of registerFilter method, of class FilterService.
     */
    public void testRegisterFilter(  )
    {
        System.out.println( "registerFilter" );

        FilterEntry entry = new FilterEntry(  );
        entry.setName( "filter" );
        entry.setFilterClass( "fr.paris.lutece.portal.service.filter.MainFilter" );
        entry.setMappingUrlPattern( "/jsp/" );

        Plugin plugin = null;
        FilterService instance = FilterService.getInstance(  );
        instance.registerFilter( entry, plugin );
    }

    public void testOrder(  ) throws LuteceInitException
    {
        System.out.println( "order" );

        FilterService.getInstance(  ).getFilters(  ).clear(  );

        FilterEntry entry2 = new FilterEntry(  );
        entry2.setName( "filter2" );
        entry2.setFilterClass( "fr.paris.lutece.portal.service.filter.MainFilter" );
        entry2.setMappingUrlPattern( "/jsp/" );
        entry2.setOrder( 2 );

        FilterEntry entry1 = new FilterEntry(  );
        entry1.setName( "filter1" );
        entry1.setFilterClass( "fr.paris.lutece.portal.service.filter.MainFilter" );
        entry1.setMappingUrlPattern( "/jsp/" );
        entry1.setOrder( 1 );

        FilterEntry entry0 = new FilterEntry(  );
        entry0.setName( "filter0" );
        entry0.setFilterClass( "fr.paris.lutece.portal.service.filter.MainFilter" );
        entry0.setMappingUrlPattern( "/jsp/" );
        entry0.setOrder( 0 );

        FilterEntry entry = new FilterEntry(  );
        entry.setName( "filter" );
        entry.setFilterClass( "fr.paris.lutece.portal.service.filter.MainFilter" );
        entry.setMappingUrlPattern( "/jsp/" );
        /// default order
        FilterService.getInstance(  ).registerFilter( entry1, null );
        FilterService.getInstance(  ).registerFilter( entry, null );
        FilterService.getInstance(  ).registerFilter( entry2, null );
        FilterService.getInstance(  ).registerFilter( entry0, null );

        FilterService.sortFilters(  );

        List<LuteceFilter> listFilters = FilterService.getInstance(  ).getFilters(  );

        for ( LuteceFilter filter : listFilters )
        {
            System.out.println( filter.getName(  ) );
        }

        assertTrue( listFilters.get( 0 ).getName(  ).equals( "filter2" ) );
        assertTrue( listFilters.get( 1 ).getName(  ).equals( "filter1" ) );
        assertTrue( listFilters.get( 2 ).getName(  ).equals( "filter0" ) );
        assertTrue( listFilters.get( 3 ).getName(  ).equals( "filter" ) );
    }

    /**
     * Test of getFilters method, of class FilterService.
     */
    public void testGetFilters(  )
    {
        System.out.println( "getFilters" );

        FilterService instance = FilterService.getInstance(  );
        List<LuteceFilter> result = instance.getFilters(  );
        assertNotNull( result );
    }
}
