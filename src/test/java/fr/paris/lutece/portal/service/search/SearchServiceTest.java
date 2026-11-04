/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.test.LuteceTestCase;

/**
 * Labels of the search types
 */
public class SearchServiceTest extends LuteceTestCase
{
    @AfterEach
    protected void tearDown( )
    {
        SearchService.resetTypeLabels( );
    }

    @Test
    public void testDefaultTypeLabelFromBundle( )
    {
        // portal.search.type.page, the type lower-cased
        assertEquals( "Site pages", SearchService.getDefaultTypeLabel( PageIndexer.INDEX_TYPE_PAGE, Locale.ENGLISH ) );
        assertEquals( "Pages du site", SearchService.getDefaultTypeLabel( PageIndexer.INDEX_TYPE_PAGE, Locale.FRENCH ) );
    }

    @Test
    public void testDefaultTypeLabelWithoutKey( )
    {
        // no key: the type itself, first letter capitalized
        assertEquals( "Unknown_type for test", SearchService.getDefaultTypeLabel( "unknown_type for test", Locale.ENGLISH ) );
        assertEquals( "", SearchService.getDefaultTypeLabel( "", Locale.ENGLISH ) );
    }
}
