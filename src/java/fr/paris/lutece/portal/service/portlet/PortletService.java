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
package fr.paris.lutece.portal.service.portlet;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;
import java.util.Collection;


/**
 * class PortletService
 */
public final class PortletService
{
    private static PortletService _singleton;
    private static IPageService _pageService = (IPageService) SpringContextService.getBean( "pageService" );

    /**
     * Constructor for class PortletService
     *
     */
    private PortletService(  )
    {
    }

    /**
        * Get the unique instance of the service
        *
        * @return The unique instance
        */
    public static synchronized PortletService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new PortletService(  );
        }

        return _singleton;
    }

    /**
     * Filter a collection of portlet  associated to a given user
     * @param collectionPortlet the The collection to filter
     * @param user the current user
     * @return  a collection of portlet associated to a given user
     */
    public Collection<Portlet> getAuthorizedPortletCollection( Collection<Portlet> collectionPortlet, AdminUser user )
    {
        Collection<Portlet> collectionPortletAuthorized = new ArrayList<Portlet>(  );

        for ( Portlet portlet : collectionPortlet )
        {
            if ( _pageService.isAuthorizedAdminPage( portlet.getPageId(  ), PageResourceIdService.PERMISSION_VIEW, user ) )
            {
                collectionPortletAuthorized.add( portlet );
            }
        }

        return collectionPortletAuthorized;
    }

    /**
         * Check if a portlet should be visible to the user
         * @param idPortlet the id of the portlet
         * @param user the current user
         * @return true if authorized, otherwise false
         */
    public boolean isAuthorized( int idPortlet, AdminUser user )
    {
        Page page = PageHome.getPageByIdPortlet( idPortlet );

        return _pageService.isAuthorizedAdminPage( page.getId(  ), PageResourceIdService.PERMISSION_VIEW, user );
    }

    /**
           * Check if a portlet should be visible to the user
           * @param portlet the portlet
           * @param user the current user
           * @return true if authorized, otherwise false
           */
    public boolean isAuthorized( Portlet portlet, AdminUser user )
    {
        Page page = PageHome.findByPrimaryKey( portlet.getPageId(  ) );

        return _pageService.isAuthorizedAdminPage( page.getId(  ), PageResourceIdService.PERMISSION_VIEW, user );
    }
}
