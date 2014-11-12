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
package fr.paris.lutece.portal.business.page;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.resource.ExtendableResourceRemovalListenerService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Page objects
 */
public final class PageHome
{
    // Static variable pointed at the DAO instance
    private static IPageDAO _dao = (IPageDAO) SpringContextService.getBean( "pageDAO" );

    /**
     * Creates a new PageHome object.
     */
    private PageHome(  )
    {
    }

    /**
     * Creates an instance of page
     *
     * @param page An instance of page which contains the informations to store
     * @return The  instance of page which has been created with its primary key.
     */
    public static Page create( Page page )
    {
        _dao.insert( page );
        PortalService.resetCache(  );

        return page;
    }

    /**
     * Removes a page and all its contents (the portlets and theirs contents)
     *
     * @param nPageId The page identifier
     */
    public static void remove( int nPageId )
    {
        Page page = findByPrimaryKey( nPageId );

        // remove portlets
        for ( Portlet portlet : page.getPortlets(  ) )
        {
            portlet.remove(  );
        }

        _dao.delete( nPageId );
        // We remove extensions of the removed page if any
        ExtendableResourceRemovalListenerService.doRemoveResourceExtentions( Page.RESOURCE_TYPE,
            Integer.toString( nPageId ) );

        PortalService.resetCache(  );
    }

    /**
     * update of the page which is specified in parameter
     *
     * @param page the instance of the page which contains the data to store
     */
    public static void update( Page page )
    {
        _dao.store( page );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of un page whose identifier is specified in parameter
     *
     * @param nKey the primary key of the page
     * @return an instance of the class
     */
    public static Page findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, true );
    }

    /**
     * Loads a page without portlets from its identifier
     *
     * @param nPageId the page identifier
     * @return an instance a the class Page
     */
    public static Page getPage( int nPageId )
    {
        return _dao.load( nPageId, false );
    }

    /**
     * Loads a page without portlets from its identifier without image content
     *
     * @param nPageId the page identifier
     * @return an instance a the class Page
     */
    public static Page getPageWithoutImageContent( int nPageId )
    {
        return _dao.loadWithoutImageContent( nPageId, false );
    }

    /**
     * Loads a page associated to a portlet
     * @param nPorletId The indentifier of the object portlet associate to the page
     * @return The Instance of the object Page
     */
    public static Page getPageByIdPortlet( int nPorletId )
    {
        return _dao.loadPageByIdPortlet( nPorletId );
    }

    /**
     * Returns the list of the child pages from the current parent page identifier
     *
     * @param nParentPageId the current page identifier, parent of childs pages
     * @return a collection of pages
     */
    public static Collection<Page> getChildPages( int nParentPageId )
    {
        return _dao.selectChildPages( nParentPageId );
    }

    /**
     * Returns the list of the child pages from the current parent page identifier
     *
     * @param nParentPageId the ParentPageId identifier
     * @return page collection
     */
    public static Collection<Page> getChildPagesMinimalData( int nParentPageId )
    {
        return _dao.selectChildPagesMinimalData( nParentPageId );
    }

    /**
     * Return the list of all the pages from a portal identifier
     *
     * @return a collection of pages
     */
    public static List<Page> getAllPages(  )
    {
        return _dao.selectAllPages(  );
    }

    /**
     * Returns the list of page
     *
     * @return the list of pages
     */
    public static ReferenceList getPagesList(  )
    {
        return _dao.getPagesList(  );
    }

    /**
     * Return the list of all the pages filtered by Lutece Role specified in parameter
     *
     * @param strRoleKey The Lutece Role key
     * @return a collection of pages
     */
    public static Collection<Page> getPagesByRoleKey( String strRoleKey )
    {
        return _dao.getPagesByRoleKey( strRoleKey );
    }

    /**
     * Gets an image resource
     * @param nPageId The page ID
     * @return ImageResource
     */
    public static ImageResource getImageResource( int nPageId )
    {
        return _dao.loadImageResource( nPageId );
    }

    /**
     * Select the max child page order and create the new order for new child page
     * @param nParentPageId The parent page Id
     * @return the new child page order
     */
    public static int getNewChildPageOrder( int nParentPageId )
    {
        return _dao.selectNewChildPageOrder( nParentPageId );
    }

    /**
     * Check if the page exists
     * @param nPageId The Page ID
     * @return True if the page exists, otherwise false
     */
    public static boolean checkPageExist( int nPageId )
    {
        return _dao.checkPageExist( nPageId );
    }

    /**
     * Get the last modified page
     * @return the last modified {@link Page}
     */
    public static Page getLastModifiedPage(  )
    {
        return _dao.loadLastModifiedPage(  );
    }

    /**
     * get list of children Pages Which Must Change  their authorization node
     * @param nIdParentPage the id of the parent page
     * @return an id list
     */
    public static List<Integer> getPagesWhichMustChangeAuthorizationNode( int nIdParentPage )
    {
        return _dao.selectPageForChangeAutorisationNode( nIdParentPage );
    }

    /**
     * Update the authorization node of the  page
     * @param nIdPage the page id
     * @param nIdAuthorizationNode  the authorization node id
     */
    public static void updateAuthorizationNode( int nIdPage, Integer nIdAuthorizationNode )
    {
        _dao.updateAutorisationNode( nIdPage, nIdAuthorizationNode );
    }
}
