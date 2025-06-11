/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.portal.web.xpages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.MokeLuteceAuthentication;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.util.AppInitPropertiesService;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/**
 * SiteMap Test Class
 */
public class SiteMapAppTest extends LuteceTestCase
{
    private static final String ROLE1 = "ROLE1";
    private static final String ROLE2 = "ROLE2";
    private static final String SERVICE_NAME = "SiteMapService";
    private @Inject IPageService pageService;

    /**
     * Test of getPage method, of class fr.paris.lutece.portal.web.xpages.SiteMapApp.
     */
    @Test
    public void testGetPage( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );

        int nMode = 0;
        Plugin plugin = null;
        SiteMapApp instance = CDI.current( ).select( SiteMapApp.class ).get( );

        XPage result = instance.getPage( request, nMode, plugin );
        assertNotNull( result );
    }

    @Test
    public void testGetPageMod( )
    {
        HttpServletRequest request = new MockHttpServletRequest( );

        // determine a random page name
        String randomPageName = "page" + new SecureRandom( ).nextLong( );

        // get the site map
        SiteMapApp instance = CDI.current( ).select( SiteMapApp.class ).get( );
        
        XPage sitemap = instance.getPage( request, 0, null );
        assertFalse( "Site map should not contain not yet created page with name " + randomPageName, sitemap.getContent( ).contains( randomPageName ) );

        // create the page
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );
        page.setName( randomPageName );

        pageService.createPage( page );
        // get the site map
        sitemap = instance.getPage( request, 0, null );
        assertTrue( "Site map should contain page with name " + randomPageName, sitemap.getContent( ).contains( randomPageName ) );
        // change the page name
        randomPageName = randomPageName + "_mod";
        page.setName( randomPageName );
        pageService.updatePage( page );
        // get the site map
        sitemap = instance.getPage( request, 0, null );
        assertTrue( "Site map should contain page with the modified name " + randomPageName, sitemap.getContent( ).contains( randomPageName ) );
        // remove the page
        pageService.removePage( page.getId( ) );
        // get the site map
        sitemap = instance.getPage( request, 0, null );
        assertFalse( "Site map should not contain page with name " + randomPageName + " anymore", sitemap.getContent( ).contains( randomPageName ) );
    }

    @Test
    public void testPageVisibility( ) throws IOException, LuteceInitException
    {
        // create pages
        final Random rand = new SecureRandom( );
        Page pageNoRole = createPage( "page." + rand.nextLong( ) );
        Page pageRole1 = createPage( "page.role1." + rand.nextLong( ), ROLE1 );
        Page pageRole2 = createPage( "page.role2." + rand.nextLong( ), ROLE2 );

        boolean authStatus = enableAuthentication( );
        boolean cacheStatus = enableSiteMapCacheService( );

        try
        {
            SiteMapApp instance = CDI.current( ).select( SiteMapApp.class ).get( );

            // test twice to test the cache
            for ( int i = 0; i < 2; i++ )
            {
                // test menu content with no user
                HttpServletRequest request = new MockHttpServletRequest( );
                XPage sitemap = instance.getPage( request, 0, null );
                assertTrue( "Site map should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageNoRole.getName( ) ) );
                assertFalse( "Site map should not contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole1.getName( ) ) );
                assertFalse( "Site map should not contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole2.getName( ) ) );

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
                sitemap = instance.getPage( request, 0, null );
                assertTrue( "Site map should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageNoRole.getName( ) ) );
                assertFalse( "Site map should not contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole1.getName( ) ) );
                assertFalse( "Site map should not contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole2.getName( ) ) );
                // test menu content with ROLE1
                user.setRoles( Arrays.asList( ROLE1 ) );
                sitemap = instance.getPage( request, 0, null );
                assertTrue( "Site map should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageNoRole.getName( ) ) );
                assertTrue( "Site map should contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole1.getName( ) ) );
                assertFalse( "Site map should not contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole2.getName( ) ) );

                // test menu content with ROLE2
                user.setRoles( Arrays.asList( ROLE2 ) );
                sitemap = instance.getPage( request, 0, null );
                assertTrue( "Site map should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageNoRole.getName( ) ) );
                assertFalse( "Site map should not contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole1.getName( ) ) );
                assertTrue( "Site map should contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole2.getName( ) ) );

                // test menu content with ROLE1 and ROLE2
                user.setRoles( Arrays.asList( ROLE1, ROLE2 ) );
                sitemap = instance.getPage( request, 0, null );
                assertTrue( "Site map should contain page not associated with a role named " + pageNoRole.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageNoRole.getName( ) ) );
                assertTrue( "Site map should contain page associated with role " + ROLE1 + " named " + pageRole1.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole1.getName( ) ) );
                assertTrue( "Site map should contain page associated with role " + ROLE2 + " named " + pageRole2.getName( ) + " (call " + ( i + 1 ) + ")",
                        sitemap.getContent( ).contains( pageRole2.getName( ) ) );

                // test menu content with ROLE2 and ROLE1
                user.setRoles( Arrays.asList( ROLE2, ROLE1 ) );

                XPage sitemap2 = instance.getPage( request, 0, null );
                assertTrue( "Role order should not matter to the cache (call " + ( i + 1 ) + ")", sitemap.getContent( ).equals(sitemap2.getContent( )) );
            }
        }
        finally
        {
            // cleanup
            restoreAuthentication( authStatus );
            restoreSiteMapCacheService( cacheStatus );

            pageService.removePage( pageNoRole.getId( ) );
            pageService.removePage( pageRole1.getId( ) );
            pageService.removePage( pageRole2.getId( ) );
        }
    }

    private void restoreSiteMapCacheService( boolean status )
    {
        List<CacheableService<?, ?>> caches = CacheService.getCacheableServicesList( );
        for ( CacheableService cacheService : caches )
        {
            if ( SERVICE_NAME.equals( cacheService.getName( ) ) )
            {
                cacheService.enableCache( status );
                break;
            }
        }
    }

    private boolean enableSiteMapCacheService( )
    {
        boolean status = false;
        List<CacheableService<?, ?>> caches = CacheService.getCacheableServicesList( );
        for ( CacheableService cacheService : caches )
        {
            if ( SERVICE_NAME.equals( cacheService.getName( ) ) )
            {
                status = cacheService.isCacheEnable( );
                cacheService.enableCache( true );
                break;
            }
        }
        return status;
    }

    private void restoreAuthentication( boolean status ) throws IOException, LuteceInitException
    {
        if ( !status )
        {
            System.getProperties( ).remove( "mylutece.authentication.enable");
            System.getProperties( ).remove( "mylutece.authentication.class" );
            SecurityService.init( );
        }
    }

    private boolean enableAuthentication( ) throws IOException, LuteceInitException
    {
        boolean status = SecurityService.isAuthenticationEnable( );
        if ( !status )
        {
            System.setProperty( "mylutece.authentication.enable", "true" );
            System.setProperty( "mylutece.authentication.class", MokeLuteceAuthentication.class.getName( ) );
            SecurityService.init( );
        }
        return status;
    }

    private Page createPage( String pageName, String role )
    {
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
        page.setName( pageName );
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).get( 0 ).getId( ) );

        if ( role != null )
        {
            page.setRole( role );
        }

        pageService.createPage( page );

        return page;
    }

    private Page createPage( String pageName )
    {
        return createPage( pageName, null );
    }
}
