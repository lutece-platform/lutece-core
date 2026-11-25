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

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.style.PageTemplateHome;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginDefaultImplementation;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.MokeLuteceAuthentication;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import fr.paris.lutece.portal.web.LocalVariables;

import jakarta.inject.Inject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * A page restricted to a role the visitor does not hold shows the login template, without the extenders of the page (plugin extend):
 * the extenders would render the page's feedback form and count a hit for a visitor who cannot see the page.
 */
public class PageExtendersVisibilityTest extends LuteceTestCase
{
    private static final String EXTEND = "extend";

    /** Test authentication with the templates the portal writes in place of a restricted page. */
    public static class RestrictedPageAuthentication extends MokeLuteceAuthentication
    {
        @Override
        public String getAccessDeniedTemplate( )
        {
            return "skin/site/statistical_include.html";
        }

        @Override
        public String getAccessControledTemplate( )
        {
            return "skin/site/statistical_include.html";
        }
    }

    @Inject
    private IPageService _pageService;

    @Test
    public void testExtendersOfARestrictedPageAreNotInserted( ) throws Exception
    {
        boolean bAuthentication = enableAuthentication( );
        Map<String, Plugin> mapPlugins = plugins( );
        Plugin extendBefore = mapPlugins.get( EXTEND );
        PluginDefaultImplementation extend = new PluginDefaultImplementation( );
        extend.setName( EXTEND );
        extend.setStatus( true );
        mapPlugins.put( EXTEND, extend );
        Page restricted = createPage( "e2e_role_ex14c" );
        Page open = createPage( Page.ROLE_NONE );

        try
        {
            String strRestricted = anonymousPage( restricted );
            assertFalse( strRestricted.contains( "@Extender[" + restricted.getId( ) + ",PAGE" ), "extenders of the restricted page inserted" );

            String strOpen = anonymousPage( open );
            assertTrue( strOpen.contains( "@Extender[" + open.getId( ) + ",PAGE,all]@" ), "extenders of a public page missing" );
        }
        finally
        {
            _pageService.removePage( restricted.getId( ) );
            _pageService.removePage( open.getId( ) );

            if ( extendBefore != null )
            {
                mapPlugins.put( EXTEND, extendBefore );
            }
            else
            {
                mapPlugins.remove( EXTEND );
            }

            restoreAuthentication( bAuthentication );
        }
    }

    private String anonymousPage( Page page ) throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        LocalVariables.setLocal( null, request, new MockHttpServletResponse( ) );
        request.addParameter( Parameters.PAGE_ID, String.valueOf( page.getId( ) ) );

        return _pageService.getPage( request, 0 );
    }

    private Page createPage( String strRole )
    {
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
        page.setPageTemplateId( PageTemplateHome.getPageTemplatesList( ).stream( )
                .filter( template -> template.getFile( ).endsWith( "page_template2.html" ) ).findFirst( ).orElseThrow( ).getId( ) );
        page.setName( "ex14c" + new SecureRandom( ).nextInt( 100000 ) );
        page.setRole( strRole );
        page.setDateUpdate( new Timestamp( System.currentTimeMillis( ) ) );
        _pageService.createPage( page );

        return page;
    }

    @SuppressWarnings( "unchecked" )
    private static Map<String, Plugin> plugins( ) throws ReflectiveOperationException
    {
        Field field = PluginService.class.getDeclaredField( "_mapPlugins" );
        field.setAccessible( true );

        return (Map<String, Plugin>) field.get( null );
    }

    private static boolean enableAuthentication( ) throws IOException, LuteceInitException
    {
        boolean bStatus = SecurityService.isAuthenticationEnable( );

        if ( !bStatus )
        {
            System.setProperty( "mylutece.authentication.enable", "true" );
            System.setProperty( "mylutece.authentication.class", RestrictedPageAuthentication.class.getName( ) );
            SecurityService.init( );
        }

        return bStatus;
    }

    private static void restoreAuthentication( boolean bStatus ) throws IOException, LuteceInitException
    {
        if ( !bStatus )
        {
            System.getProperties( ).remove( "mylutece.authentication.enable" );
            System.getProperties( ).remove( "mylutece.authentication.class" );
            SecurityService.init( );
        }
    }
}
