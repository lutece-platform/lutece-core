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
package fr.paris.lutece.portal.web.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.web.menu.MenuItem.MenuTreeBuilder;

/**
 * Tests the tree built from the flat list of the site map and the menus
 */
public class MenuTreeBuilderTest
{
    private static MenuItem item( int nPageId, int nParentId, int nLevel )
    {
        return MenuItem.builder( ).pageId( nPageId ).parentId( nParentId ).name( "page " + nPageId ).level( nLevel ).build( );
    }

    /**
     * The site map lists the root page (id 1, parent 0) first: its child pages must be nested under it, not listed beside it
     */
    @Test
    public void testTreeWithRootPage( )
    {
        List<MenuItem> roots = MenuTreeBuilder.buildTree( List.of( item( 1, 0, 0 ), item( 3, 1, 1 ), item( 5, 3, 2 ), item( 119, 1, 1 ) ) );

        assertEquals( 1, roots.size( ) );
        MenuItem root = roots.get( 0 );
        assertEquals( 1, root.getPageId( ) );
        assertEquals( 2, root.getChildren( ).size( ) );
        assertEquals( 3, root.getChildren( ).get( 0 ).getPageId( ) );
        assertEquals( 119, root.getChildren( ).get( 1 ).getPageId( ) );
        assertEquals( 5, root.getChildren( ).get( 0 ).getChildren( ).get( 0 ).getPageId( ) );
    }

    /**
     * A menu without the root page: the first level pages are the roots
     */
    @Test
    public void testTreeWithoutRootPage( )
    {
        List<MenuItem> roots = MenuTreeBuilder.buildTree( List.of( item( 3, 1, 1 ), item( 5, 3, 2 ), item( 119, 1, 1 ) ) );

        assertEquals( 2, roots.size( ) );
        assertEquals( 1, roots.get( 0 ).getChildren( ).size( ) );
        assertEquals( 0, roots.get( 1 ).getChildren( ).size( ) );
    }

    /**
     * A page that is its own parent is a root, not a child of itself
     */
    @Test
    public void testSelfParent( )
    {
        List<MenuItem> roots = MenuTreeBuilder.buildTree( List.of( item( 1, 1, 0 ) ) );

        assertEquals( 1, roots.size( ) );
        assertEquals( 0, roots.get( 0 ).getChildren( ).size( ) );
    }
}
