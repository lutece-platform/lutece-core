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
package fr.paris.lutece.portal.service.portal;

import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;

public class PortalMenuServiceTest extends LuteceTestCase
{
    public void testGetMenuContent()
    {
        HttpServletRequest request = new MokeHttpServletRequest( );
        // determine a random page name
        String randomPageName = "page" + new SecureRandom( ).nextLong( );
        // get the menu
        String menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
        assertFalse( "Portal menu should not contain not yet created page with name " + randomPageName, menu.contains( randomPageName ) );
        // create the page
        Page page = new Page(  );
        page.setParentPageId( PortalService.getRootPageId(  ) );
        page.setName( randomPageName );
        IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
        pageService.createPage( page );
        // get the menu
        menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
        assertTrue( "Portal menu should contain page with name " + randomPageName, menu.contains( randomPageName ) );
        // change the page name
        randomPageName = randomPageName + "_mod";
        page.setName( randomPageName );
        pageService.updatePage( page );
        // get the menu
        menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
        assertTrue( "Portal menu should contain page with the modified name " + randomPageName, menu.contains( randomPageName ) );
        // remove the page
        pageService.removePage( page.getId( ) );
        // get the menu
        menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
        assertFalse( "Portal menu should not contain page with name " + randomPageName + " anymore", menu.contains( randomPageName ) );
    }

}
