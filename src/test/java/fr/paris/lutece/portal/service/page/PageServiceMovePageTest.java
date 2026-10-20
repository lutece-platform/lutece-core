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
package fr.paris.lutece.portal.service.page;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * PageService.movePage: the place of a page among the child pages of its parent, and its authorization node.
 * The pages of the test sit under a page of their own, P, so that a move renumbers none of the pages of the site:
 *
 * <pre>
 * P
 * ├ A    (its own authorization node)
 * │ ├ a1, a2, a3
 * └ B    (its own authorization node)
 *   └ b1
 *     └ b1x
 * </pre>
 */
public class PageServiceMovePageTest extends LuteceTestCase
{
    private static final int NODE_OWN = 0;
    private static final int NODE_INHERITED = 1;

    private @Inject IPageService _pageService;
    private final List<Page> _listCreated = new ArrayList<>( );
    private String _strPrefix;
    private Page _p;
    private Page _a;
    private Page _a1;
    private Page _a2;
    private Page _a3;
    private Page _b;
    private Page _b1;
    private Page _b1x;

    @BeforeEach
    public void createTree( )
    {
        _strPrefix = "movepage" + Math.abs( new SecureRandom( ).nextInt( ) ) + "_";
        int nRootPageId = PortalService.getRootPageId( );
        _p = createPage( "P", nRootPageId, PageHome.getNewChildPageOrder( nRootPageId ), NODE_OWN );
        _a = createPage( "A", _p.getId( ), 1, NODE_OWN );
        _a1 = createPage( "a1", _a.getId( ), 1, NODE_INHERITED );
        _a2 = createPage( "a2", _a.getId( ), 2, NODE_INHERITED );
        _a3 = createPage( "a3", _a.getId( ), 3, NODE_INHERITED );
        _b = createPage( "B", _p.getId( ), 2, NODE_OWN );
        _b1 = createPage( "b1", _b.getId( ), 1, NODE_INHERITED );
        _b1x = createPage( "b1x", _b1.getId( ), 1, NODE_INHERITED );
    }

    @AfterEach
    public void removeTree( )
    {
        // The deepest pages first: a page goes once it has no child page left
        List<Page> listLeft = new ArrayList<>( _listCreated );

        while ( !listLeft.isEmpty( ) )
        {
            List<Page> listLeaves = listLeft.stream( ).filter( page -> PageHome.getChildPagesMinimalData( page.getId( ) ).isEmpty( ) ).toList( );

            if ( listLeaves.isEmpty( ) )
            {
                break;
            }

            listLeaves.forEach( page -> _pageService.removePage( page.getId( ) ) );
            listLeft.removeAll( listLeaves );
        }
    }

    @Test
    public void testReorderAmongSisterPages( )
    {
        _pageService.movePage( reload( _a3 ), _a.getId( ), 1 );
        assertChildren( _a, _a3, _a1, _a2 );

        _pageService.movePage( reload( _a3 ), _a.getId( ), 3 );
        assertChildren( _a, _a1, _a2, _a3 );

        _pageService.movePage( reload( _a1 ), _a.getId( ), 2 );
        assertChildren( _a, _a2, _a1, _a3 );
    }

    @Test
    public void testPlaceOutOfTheList( )
    {
        // Beyond the last one: last; before the first one: first
        _pageService.movePage( reload( _a1 ), _a.getId( ), 99 );
        assertChildren( _a, _a2, _a3, _a1 );

        _pageService.movePage( reload( _a1 ), _a.getId( ), 0 );
        assertChildren( _a, _a1, _a2, _a3 );
    }

    @Test
    public void testMoveToAnotherParent( )
    {
        Page a2 = reload( _a2 );
        _pageService.movePage( a2, _b.getId( ), 1 );

        // The parent it leaves closes the gap, the one it joins makes room
        assertChildren( _a, _a1, _a3 );
        assertChildren( _b, _a2, _b1 );
        assertEquals( _b.getId( ), reload( _a2 ).getParentPageId( ) );
        assertEquals( _b.getId( ), a2.getParentPageId( ), "the page moved carries its new parent" );
        assertEquals( 1, a2.getOrder( ), "the page moved carries its new order" );
    }

    @Test
    public void testAuthorizationNodeFollowsTheNewParent( )
    {
        // b1 and b1x take the node of B from it; moved under A, they take the node of A
        assertEquals( _b.getId( ), node( _b1 ) );
        assertEquals( _b.getId( ), node( _b1x ) );

        _pageService.movePage( reload( _b1 ), _a.getId( ), 4 );

        assertChildren( _a, _a1, _a2, _a3, _b1 );
        assertChildren( _b );
        assertEquals( _a.getId( ), node( _b1 ), "the page moved takes the node of its new parent" );
        assertEquals( _a.getId( ), node( _b1x ), "so do its descendants" );
    }

    @Test
    public void testOwnAuthorizationNodeStays( )
    {
        // B is a node of its own: under A it keeps it, and so do its descendants
        _pageService.movePage( reload( _b ), _a.getId( ), 1 );

        assertChildren( _p, _a );
        assertChildren( _a, _b, _a1, _a2, _a3 );
        assertEquals( _b.getId( ), node( _b ) );
        assertEquals( _b.getId( ), node( _b1 ) );
        assertEquals( _b.getId( ), node( _b1x ) );
    }

    /**
     * Creates a test page, its authorization node its own one or the one of its parent
     *
     * @param strName
     *            the name, after the prefix of the test
     * @param nParentPageId
     *            the parent page
     * @param nOrder
     *            the order among the child pages of the parent
     * @param nNodeStatus
     *            0 for a node of its own, 1 for the node of its parent
     * @return the page
     */
    private Page createPage( String strName, int nParentPageId, int nOrder, int nNodeStatus )
    {
        Page page = new Page( );
        page.setParentPageId( nParentPageId );
        page.setOrder( nOrder );
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
        page.setName( _strPrefix + strName );
        page.setDescription( _strPrefix + strName );
        page.setMetaKeywords( "" );
        page.setMetaDescription( "" );
        page.setNodeStatus( nNodeStatus );
        page.setDateUpdate( new Timestamp( System.currentTimeMillis( ) ) );
        _pageService.createPage( page );
        _listCreated.add( page );

        Integer nNode = ( nNodeStatus == NODE_OWN ) ? Integer.valueOf( page.getId( ) ) : PageHome.getPage( nParentPageId ).getIdAuthorizationNode( );
        PageHome.updateAuthorizationNode( page.getId( ), nNode );

        return reload( page );
    }

    /**
     * The page as stored
     *
     * @param page
     *            the page
     * @return the page read again
     */
    private static Page reload( Page page )
    {
        return PageHome.getPageWithoutImageContent( page.getId( ) );
    }

    /**
     * The authorization node of a page, as stored
     *
     * @param page
     *            the page
     * @return its authorization node
     */
    private static int node( Page page )
    {
        return reload( page ).getIdAuthorizationNode( );
    }

    /**
     * Checks the child pages of a page, as stored: these ones, in this order, with the orders 1 to n
     *
     * @param parent
     *            the parent page
     * @param expected
     *            the child pages expected, in their order
     */
    private static void assertChildren( Page parent, Page... expected )
    {
        List<String> listExpected = new ArrayList<>( );

        for ( int i = 0; i < expected.length; i++ )
        {
            listExpected.add( expected [i].getId( ) + "@" + ( i + 1 ) );
        }

        List<String> listActual = new ArrayList<>( );

        for ( Page child : PageHome.getChildPagesMinimalData( parent.getId( ) ) )
        {
            listActual.add( child.getId( ) + "@" + reload( child ).getOrder( ) );
        }

        assertEquals( listExpected, listActual, "page id@order of the child pages of " + parent.getName( ) );
    }
}
