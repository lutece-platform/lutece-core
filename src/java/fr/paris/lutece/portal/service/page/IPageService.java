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
package fr.paris.lutece.portal.service.page;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.SiteMessageException;

import javax.servlet.http.HttpServletRequest;


/**
 * Page Service Interface
 */
public interface IPageService
{
    /**
     * Build the page content.
     *
     * @param nIdPage The page ID
     * @param nMode The current mode.
     * @param request The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    String getPageContent( int nIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException;

    /**
     * Returns the page for a given ID. The page is built using XML data of each
     * portlet or retrieved from the cache if it's enable.
     *
     * @param strIdPage The page ID
     * @param nMode The current mode.
     * @param request The HttpRequest
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    String getPage( String strIdPage, int nMode, HttpServletRequest request )
        throws SiteMessageException;

    /**
     * Returns the page for a given ID. The page is built using XML data of each
     * portlet or retrieved from the cache if it's enable.
     *
     * @param request The page ID
     * @param nMode The current mode.
     * @return The HTML code of the page as a String.
     * @throws SiteMessageException If a message shouldbe displayed
     */
    String getPage( HttpServletRequest request, int nMode )
        throws SiteMessageException;

    /**
    * Invalidate Page Content
    * @param nPageId The Page ID
    */
    void invalidateContent( int nPageId );

    /**
     * Update the page
     * @param page The page
     */
    void updatePage( Page page );

    /**
     * Remove the page
     * @param nPageId The page's id
     */
    void removePage( int nPageId );

    /**
     * Create a page
     * @param page The page to create
     */
    void createPage( Page page );

    /**
     * Check if authorized
     * @param nId The Page's ID
     * @param strPermission The given permission
     * @param user The user
     * @return True if authorized, otherwise false
     */
    boolean isAuthorizedAdminPage( int nId, String strPermission, AdminUser user );

    /**
     * Return the ressource id type
     * @return the resource type id
     */
    String getResourceTypeId(  );
}
