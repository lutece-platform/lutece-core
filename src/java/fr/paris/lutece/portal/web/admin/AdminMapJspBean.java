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

import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.menu.MenuItem;
import fr.paris.lutece.portal.web.menu.MenuItem.MenuTreeBuilder;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

/**
 * This class provides the map of the pages on the site
 */
@RequestScoped
@Named
public class AdminMapJspBean extends AdminFeaturesPageJspBean
{
    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    private static final long serialVersionUID = 2871979154306491511L;
    private static final int NO_PARENT = 0;

    // Markers
    private static final String MARKER_MAP_SITE = "map_site";
    private static final String MARK_PAGE = "page";
    private static final String MARK_MAP_ITEMS = "mapItems";

    // Templates
    private static final String TEMPLATE_MAP_SITE = "admin/site/site_map.html";
    private static final String TEMPLATE_MAP_TREE = "admin/site/admin_site_map_admin.html";

    // Parameters
    private static final String PARAMETER_PAGE_ID = "page_id";

    // Properties
    private static final String PROPERTY_ADMIN_PATH = "lutece.admin.path";

    public String getMap( HttpServletRequest request )
    {
        String map = getMapTemplate( request );

        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = ( strPageId != null ) ? Integer.parseInt( strPageId ) : 1;
        Page page = PageHome.getPage( nPageId );

        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_PAGE, page );
        model.put( MARKER_MAP_SITE, HtmlMarkup.of( map ) );
        // The map is a fragment without any <base>: the module of the tree is imported from the base url, not from the url of the jsp
        model.put( Markers.BASE_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MAP_SITE, getLocale( ), model );

        return t.getHtml();
    }

    /**
     * Build or get in the cache the page which contains the site map depending on the mode
     *
     * @param request
     *            The Http request
     * @return The content of the site map
     */
    public String getMapTemplate( HttpServletRequest request )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( MARK_MAP_ITEMS, MenuTreeBuilder.buildTree( getMapItems( request ) ) );
        model.put( "site_path", AppPropertiesService.getProperty( PROPERTY_ADMIN_PATH ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MAP_TREE, getLocale( ), model );
        
        return t.getHtml();
    }
    
    /**
     * The pages of the site the user may view, in the order of the tree: each page followed by its child pages, with its level
     *
     * @param request
     *            the request, its page_id the current page
     * @return the pages, flat
     */
    public List<MenuItem> getMapItems( HttpServletRequest request )
    {
        String strCurrentPageId = request.getParameter( PARAMETER_PAGE_ID );
        Integer nCurrentPageId = null;

        try
        {
            nCurrentPageId = ( strCurrentPageId != null ) ? Integer.valueOf( strCurrentPageId ) : null;
        }
        catch( NumberFormatException e )
        {
            // no current page
        }

        return getMapItems( AdminUserService.getAdminUser( request ), nCurrentPageId );
    }

    /**
     * The pages of the site a user may view, in the order of the tree: each page followed by its child pages, with its level. The pages
     * are read in one query, ordered by parent then by order, and the tree is walked in memory; a page the user may not view is left
     * out, its child pages are not
     *
     * @param user
     *            the user
     * @param nCurrentPageId
     *            the current page, or null
     * @return the pages, flat
     */
    public static List<MenuItem> getMapItems( User user, Integer nCurrentPageId )
    {
        Map<Integer, Page> mapPages = new HashMap<>( );
        Map<Integer, List<Page>> mapChildren = new HashMap<>( );

        for ( Page page : PageHome.getAllPagesMinimalData( ) )
        {
            mapPages.put( page.getId( ), page );
            mapChildren.computeIfAbsent( page.getParentPageId( ), k -> new ArrayList<>( ) ).add( page );
        }

        List<MenuItem> mapItems = new ArrayList<>( );
        Page root = mapPages.get( PortalService.getRootPageId( ) );

        if ( root != null )
        {
            buildMenu( user, mapItems, root, NO_PARENT, 0, nCurrentPageId, mapPages, mapChildren, new HashSet<>( ) );
        }

        return mapItems;
    }

    /**
     * Adds a page, if the user may view it, then its child pages, depth first; a loop in the tree stops at the page seen again. The parent of
     * an item is the page through which it was reached, so that the root of the map stays a root even when its stored parent is one of its
     * descendants; the child pages of a page the user may not view keep that page as parent, which is not listed, so they are roots of the map
     *
     * @param user
     *            the user
     * @param flatMenu
     *            the pages listed
     * @param page
     *            the page
     * @param nParentId
     *            the page through which this page was reached, NO_PARENT for the root
     * @param nLevel
     *            its depth, 0 for the root
     * @param nCurrentPageId
     *            the current page, or null
     * @param mapPages
     *            the pages of the site by identifier
     * @param mapChildren
     *            the child pages of each page, in their order
     * @param setVisited
     *            the pages already seen
     */
    private static void buildMenu( User user, List<MenuItem> flatMenu, Page page, int nParentId, int nLevel, Integer nCurrentPageId, Map<Integer, Page> mapPages,
            Map<Integer, List<Page>> mapChildren, Set<Integer> setVisited )
    {
        if ( !setVisited.add( page.getId( ) ) )
        {
            return;
        }

        boolean bAuthorizationPage = ( page.getId( ) == PortalService.getRootPageId( ) ) || RBACService.isAuthorized( Page.RESOURCE_TYPE,
                Integer.toString( getAuthorizationNode( page, mapPages ) ), PageResourceIdService.PERMISSION_VIEW, user );

        if ( bAuthorizationPage )
        {
            // Add page
            MenuItem menuItem = MenuItem.builder( ).pageId( page.getId( ) ).parentId( nParentId )
                    .name( page.getName( ) ).description( page.getDescription( ) )
                    .currentPageId( nCurrentPageId ).level( nLevel ).role( page.getRole( ) )
                    .build( );
            flatMenu.add( menuItem );
        }

        for ( Page pageChild : mapChildren.getOrDefault( page.getId( ), Collections.emptyList( ) ) )
        {
            buildMenu( user, flatMenu, pageChild, page.getId( ), nLevel + 1, nCurrentPageId, mapPages, mapChildren, setVisited );
        }
    }

    /**
     * The page whose rights a page follows: itself when it is an authorization node (node_status 0), else the closest such ancestor
     *
     * @param page
     *            the page
     * @param mapPages
     *            the pages of the site by identifier
     * @return the identifier of the authorization node
     */
    private static int getAuthorizationNode( Page page, Map<Integer, Page> mapPages )
    {
        Page node = page;
        Set<Integer> setVisited = new HashSet<>( );

        while ( node.getNodeStatus( ) != 0 && setVisited.add( node.getId( ) ) )
        {
            Page parent = mapPages.get( node.getParentPageId( ) );

            if ( parent == null )
            {
                break;
            }

            node = parent;
        }

        return node.getId( );
    }
}
