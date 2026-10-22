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
package fr.paris.lutece.portal.web.admin;

import java.security.SecureRandom;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.menu.MenuItem;
import fr.paris.lutece.portal.web.menu.MenuItem.MenuTreeBuilder;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * The back office site map when the stored parent of the root page is one of its descendants (top, middle, bottom, the parent of top being
 * bottom): the root stays the root of the tree, with its descendants under it
 */
public class AdminMapJspBeanCycleTest extends LuteceTestCase
{
    private static final int ID_ADMIN = 1;
    private static final String PROPERTY_ROOT_PAGE = "lutece.page.root";

    private Page _top;
    private Page _middle;
    private Page _bottom;
    private int _nInitialRootId;
    @Inject
    private IPageService _pageService;

    /**
     * Creates the pages top, middle and bottom, makes bottom the parent of top, and top the root page
     */
    @BeforeEach
    protected void createCycle( )
    {
        _top = createPage( null );
        _middle = createPage( _top.getId( ) );
        _bottom = createPage( _middle.getId( ) );
        _top.setParentPageId( _bottom.getId( ) );
        _pageService.updatePage( _top );
        _nInitialRootId = PortalService.getRootPageId( );
        System.setProperty( PROPERTY_ROOT_PAGE, Integer.toString( _top.getId( ) ) );
    }

    /**
     * Removes the pages and restores the root page
     */
    @AfterEach
    protected void removeCycle( )
    {
        for ( Page page : new Page [ ] {
                _bottom, _middle, _top
        } )
        {
            try
            {
                _pageService.removePage( page.getId( ) );
            }
            catch( RuntimeException e )
            {
                AppLogService.error( "Could not remove page {}", page.getId( ), e );
            }
        }
        System.setProperty( PROPERTY_ROOT_PAGE, Integer.toString( _nInitialRootId ) );
    }

    /**
     * Creates a page
     *
     * @param nParentId
     *            the parent page, or null
     * @return the page
     */
    private Page createPage( Integer nParentId )
    {
        Page page = new Page( );
        if ( nParentId != null )
        {
            page.setParentPageId( nParentId );
        }
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
        page.setName( "page" + new SecureRandom( ).nextLong( ) );
        _pageService.createPage( page );
        return page;
    }

    /**
     * The tree of the back office site map has the root page as its only root, with middle and bottom under it
     */
    @Test
    public void testRootStaysRootInACycle( )
    {
        AdminUser admin = AdminUserHome.findByPrimaryKey( ID_ADMIN );
        admin.setRoles( AdminUserHome.getRolesListForUser( ID_ADMIN ) );

        List<MenuItem> tree = MenuTreeBuilder.buildTree( AdminMapJspBean.getMapItems( admin, null ) );

        assertEquals( 1, tree.size( ) );
        MenuItem root = tree.get( 0 );
        assertEquals( _top.getId( ), root.getPageId( ) );
        assertEquals( 1, root.getChildren( ).size( ) );
        MenuItem middle = root.getChildren( ).get( 0 );
        assertEquals( _middle.getId( ), middle.getPageId( ) );
        assertEquals( _bottom.getId( ), middle.getChildren( ).get( 0 ).getPageId( ) );
    }
}
