/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.security.SecureRandom;
import java.util.Collection;

import org.junit.Assert;

import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.test.LuteceTestCase;

public class PageDAOTest extends LuteceTestCase
{
    public void testGetPagesByRoleKey( )
    {
        SecureRandom rnd = new SecureRandom( );
        Role role = null;
        Page page = null;
        try
        {
            // create a role
            String randomRoleName = "role" + rnd.nextLong( );
            role = new Role( );
            role.setRole( randomRoleName );
            role.setRoleDescription( randomRoleName );
            role.setWorkgroup( AdminWorkgroupService.ALL_GROUPS );
            RoleHome.create( role );
            // create a page
            String randomPageName = "page" + rnd.nextLong( );
            page = new Page( );
            page.setParentPageId( PortalService.getRootPageId( ) );
            page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
            page.setName( randomPageName );
            page.setRole( randomRoleName );
            IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
            pageService.createPage( page );
            // get page by role
            PageDAO dao = new PageDAO( );
            Collection<Page> pages = dao.getPagesByRoleKey( randomRoleName );
            assertNotNull( pages );
            for ( Page p : pages )
            {
                if ( p.getName( ).equals( randomPageName ) )
                {
                    return;
                }
            }
            Assert.fail( "could not find the page " + randomPageName );
        }
        finally
        {
            // cleanup
            if ( role != null )
            {
                try
                {
                    RoleHome.remove( role.getRole( ) );
                }
                finally
                {
                }
            }
            if ( page != null )
            {
                try
                {
                    PageHome.remove( page.getId( ) );
                }
                finally
                {
                }
            }
        }

    }
}
