/*
 * Copyright (c) 2002-2008, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.test.LuteceTestCase;
import static org.junit.Assert.*;

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
        entry.setFilterClass( "" );
        entry.setMapping( "/jsp/" );

        Plugin plugin = null;
        FilterService instance = FilterService.getInstance(  );
        instance.registerFilter( entry, plugin );
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
