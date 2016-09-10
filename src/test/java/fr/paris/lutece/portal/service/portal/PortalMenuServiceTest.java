/*
 * Copyright (c) 2002-2015, Mairie de Paris
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

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.MokeLuteceAuthentication;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.security.SecureRandom;

import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class PortalMenuServiceTest extends LuteceTestCase
{
    private static final String ROLE1 = "ROLE1";
    private static final String ROLE2 = "ROLE2";

    public void testGetMenuContent( )
    {
        HttpServletRequest request = new MokeHttpServletRequest( );

        // determine a random page name
        String randomPageName = "page" + new SecureRandom( ).nextLong( );

        // get the menu
        String menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
        assertFalse( "Portal menu should not contain not yet created page with name " + randomPageName, menu.contains( randomPageName ) );

        // create the page
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
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

    public void testPageVisibility( ) throws IOException, LuteceInitException
    {
        // create pages
        final Random rand = new SecureRandom( );
        Page pageNoRole = createPage( "page." + rand.nextLong( ) );
        Page pageRole1 = createPage( "page.role1." + rand.nextLong( ), ROLE1 );
        Page pageRole2 = createPage( "page.role2." + rand.nextLong( ), ROLE2 );

        boolean authStatus = enableAuthentication( );
        boolean cacheStatus = enablePortalMenuServiceCache( );

        try
        {
            // test twice to test the cache
            for ( int i = 0; i < 2; i++ )
            {
                // test menu content with no user
                HttpServletRequest request = new MokeHttpServletRequest( );
                String menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
                assertTrue( "Portal menu should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageNoRole.getName( ) ) );
                assertFalse( "Portal menu should not contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 )
                        + ")", menu.contains( pageRole1.getName( ) ) );
                assertFalse( "Portal menu should not contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 )
                        + ")", menu.contains( pageRole2.getName( ) ) );

                // test menu content with no role
                @SuppressWarnings( "serial" )
                LuteceUser user = new LuteceUser( "junit", SecurityService.getInstance( ).getAuthenticationService( ) )
                {
                    @Override
                    public String getName( )
                    {
                        // user name is different on each call
                        return "user" + rand.nextLong( );
                    }
                };

                request.getSession( ).setAttribute( "lutece_user", user );
                menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
                assertTrue( "Portal menu should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageNoRole.getName( ) ) );
                assertFalse( "Portal menu should not contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 )
                        + ")", menu.contains( pageRole1.getName( ) ) );
                assertFalse( "Portal menu should not contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 )
                        + ")", menu.contains( pageRole2.getName( ) ) );
                // test menu content with ROLE1
                user.setRoles( Arrays.asList( ROLE1 ) );
                menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
                assertTrue( "Portal menu should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageNoRole.getName( ) ) );
                assertTrue( "Portal menu should contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageRole1.getName( ) ) );
                assertFalse( "Portal menu should not contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 )
                        + ")", menu.contains( pageRole2.getName( ) ) );

                // test menu content with ROLE2
                user.setRoles( Arrays.asList( ROLE2 ) );
                menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
                assertTrue( "Portal menu should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageNoRole.getName( ) ) );
                assertFalse( "Portal menu should not contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 )
                        + ")", menu.contains( pageRole1.getName( ) ) );
                assertTrue( "Portal menu should contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageRole2.getName( ) ) );

                // test menu content with ROLE1 and ROLE2
                user.setRoles( Arrays.asList( ROLE1, ROLE2 ) );
                menu = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
                assertTrue( "Portal menu should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageNoRole.getName( ) ) );
                assertTrue( "Portal menu should contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageRole1.getName( ) ) );
                assertTrue( "Portal menu should contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        menu.contains( pageRole2.getName( ) ) );

                // test menu content with ROLE2 and ROLE1
                user.setRoles( Arrays.asList( ROLE2, ROLE1 ) );

                String menu2 = PortalMenuService.getInstance( ).getMenuContent( 0, PortalMenuService.MODE_NORMAL, PortalMenuService.MENU_MAIN, request );
                assertTrue( "Role order should not matter to the cache (call " + ( i + 1 ) + ")", menu == menu2 );
            }
        }
        finally
        {
            // cleanup
            restoreAuthentication( authStatus );
            restorePortalMenuServiceCache( cacheStatus );

            IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
            pageService.removePage( pageNoRole.getId( ) );
            pageService.removePage( pageRole1.getId( ) );
            pageService.removePage( pageRole2.getId( ) );
        }
    }

    private void restorePortalMenuServiceCache( boolean status )
    {
        PortalMenuService.getInstance( ).enableCache( status );
    }

    private boolean enablePortalMenuServiceCache( )
    {
        boolean status = PortalMenuService.getInstance( ).isCacheEnable( );
        PortalMenuService.getInstance( ).enableCache( true );

        return status;
    }

    private void restoreAuthentication( boolean status ) throws IOException, LuteceInitException
    {
        if ( !status )
        {
            File luteceProperties = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
            Properties props = new Properties( );
            InputStream is = new FileInputStream( luteceProperties );
            props.load( is );
            is.close( );
            props.remove( "mylutece.authentication.enable" );
            props.remove( "mylutece.authentication.class" );

            OutputStream os = new FileOutputStream( luteceProperties );
            props.store( os, "saved for junit " + this.getClass( ).getCanonicalName( ) );
            os.close( );
            AppPropertiesService.reloadAll( );
            SecurityService.init( );
        }
    }

    private boolean enableAuthentication( ) throws IOException, LuteceInitException
    {
        boolean status = SecurityService.isAuthenticationEnable( );

        if ( !status )
        {
            File luteceProperties = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
            Properties props = new Properties( );
            InputStream is = new FileInputStream( luteceProperties );
            props.load( is );
            is.close( );
            props.setProperty( "mylutece.authentication.enable", "true" );
            props.setProperty( "mylutece.authentication.class", MokeLuteceAuthentication.class.getName( ) );

            OutputStream os = new FileOutputStream( luteceProperties );
            props.store( os, "saved for junit " + this.getClass( ).getCanonicalName( ) );
            os.close( );
            AppPropertiesService.reloadAll( );
            SecurityService.init( );
        }

        return status;
    }

    private Page createPage( String pageName, String role )
    {
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
        page.setName( pageName );

        if ( role != null )
        {
            page.setRole( role );
        }

        IPageService pageService = (IPageService) SpringContextService.getBean( "pageService" );
        pageService.createPage( page );

        return page;
    }

    private Page createPage( String pageName )
    {
        return createPage( pageName, null );
    }
}
