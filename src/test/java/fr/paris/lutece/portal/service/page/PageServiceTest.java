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
package fr.paris.lutece.portal.service.page;

import java.util.List;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.business.page.Page;
import java.sql.Timestamp;
import java.security.SecureRandom;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.cache.CacheService;
import fr.paris.lutece.portal.service.cache.CacheableService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;

public class PageServiceTest extends LuteceTestCase
{
    private static final int MODE_ADMIN = 1;
    private boolean _pageCacheStatus;
    private @Inject IPageService pageService ;

    @BeforeEach
    public void setUp( ) throws Exception
    {
        // activate the page cache
        List<CacheableService<?, ?>> services = CacheService.getCacheableServicesList( );

        for ( CacheableService cs : services )
        {
            if ( cs instanceof PageCacheService )
            {
                _pageCacheStatus = cs.isCacheEnable( );
                cs.enableCache( true );

                break;
            }
        }
    }

    @AfterEach
    public void tearDown( ) throws Exception
    {
        // restore the previous page cache status
        List<CacheableService<?, ?>> services = CacheService.getCacheableServicesList( );

        for ( CacheableService cs : services )
        {
            if ( cs instanceof PageCacheService )
            {
                cs.enableCache( _pageCacheStatus );

                break;
            }
        }
    }

    /**
     * Tests that the base URL is correct when the page is cached and the URL used to access the site changes
     * 
     * @throws SiteMessageException
     */
    @Test
    public void testBaseUrl( ) throws SiteMessageException
    {
        HttpServletResponse response = new MockHttpServletResponse( );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        LocalVariables.setLocal( null, request, response );
        request.addParameter( Parameters.PAGE_ID, "1" );

        String pageContent = pageService.getPage( request, 0 );
        assertTrue( pageContent.contains( "<base href=\"http://localhost/\">" ) );

        // change server name and thus base_url
        request = new MockHttpServletRequest( );
        request.setServerName( "junit" );

        LocalVariables.setLocal( null, request, response );
        request.addParameter( Parameters.PAGE_ID, "1" );
        pageContent = pageService.getPage( request, 0 );
        assertTrue( pageContent.contains( "<base href=\"http://junit/\">" ) );
    }

    /**
     * Tests that a theme request parameter does not add a cache entry: the key holds the theme of the user only
     * 
     * @throws SiteMessageException
     */
    @Test
    public void testThemeParameterNotInKey( ) throws SiteMessageException
    {
        CacheableService<?, ?> pageCache = null;
        for ( CacheableService<?, ?> cs : CacheService.getCacheableServicesList( ) )
        {
            if ( cs instanceof PageCacheService )
            {
                pageCache = cs;
                break;
            }
        }
        assertNotNull( pageCache );
        pageCache.resetCache( );

        HttpServletResponse response = new MockHttpServletResponse( );
        for ( String strTheme : new String [ ] { "junit-theme-a", "junit-theme-b", "junit-theme-c" } )
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            LocalVariables.setLocal( null, request, response );
            request.addParameter( Parameters.PAGE_ID, "1" );
            request.addParameter( "theme", strTheme );
            pageService.getPage( request, 0 );
        }

        assertEquals( 1, pageCache.getCacheSize( ) );
    }

    /**
     * The columns of a page and its breadcrumb are HTML the core renders, handed to the page template as markup
     * (LUT-33153): with auto-escaping, a String would be written as text.
     *
     * @throws SiteMessageException
     *             if the page cannot be built
     */
    @Test
    public void testColumnsAndPathAreWrittenAsHtml( ) throws SiteMessageException
    {
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).stream( )
                .filter( template -> template.getFile( ).endsWith( "page_template2.html" ) ).findFirst( ).orElseThrow( ).getId( ) );
        page.setName( "columns" + new SecureRandom( ).nextInt( 100000 ) );
        page.setDateUpdate( new Timestamp( System.currentTimeMillis( ) ) );
        pageService.createPage( page );
        try
        {
            MockHttpServletRequest request = new MockHttpServletRequest( );
            LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );

            String strAdminPreview = pageService.getPage( String.valueOf( page.getId( ) ), MODE_ADMIN, request );
            assertTrue( strAdminPreview.contains( "id=\"lutece-column-1\"" ), "column outline missing" );
            assertFalse( strAdminPreview.contains( "&lt;div" ), "column escaped" );

            request.addParameter( Parameters.PAGE_ID, String.valueOf( page.getId( ) ) );
            String strPage = pageService.getPage( request, 0 );
            assertTrue( strPage.contains( page.getName( ) ), "page missing" );
            assertFalse( strPage.contains( "&lt;li" ) || strPage.contains( "&lt;a " ), "breadcrumb escaped" );
        }
        finally
        {
            pageService.removePage( page.getId( ) );
        }
    }
}
