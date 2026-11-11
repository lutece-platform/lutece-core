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
package fr.paris.lutece.portal.web;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.web.admin.AdminMapJspBean;
import fr.paris.lutece.portal.web.admin.AdminPageJspBean;
import fr.paris.lutece.portal.web.admin.AdminPagePortletJspBean;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectorJspBean;
import fr.paris.lutece.portal.web.rbac.RoleManagementJspBean;
import fr.paris.lutece.portal.web.system.CacheJspBean;
import fr.paris.lutece.portal.web.system.PluginJspBean;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * Identifiers that are not numbers, or of no object, give a page or a message instead of an error
 */
public class InvalidRequestValuesTest extends LuteceTestCase
{
    private @Inject AdminPagePortletJspBean _portletBean;
    private @Inject PluginJspBean _pluginBean;
    private @Inject RoleManagementJspBean _roleBean;

    private MockHttpServletRequest login( String strRight )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), strRight );
        return request;
    }

    @Test
    public void testMapWithInvalidPageId( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = login( AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
        request.addParameter( Parameters.PAGE_ID, "abc" );
        AdminMapJspBean bean = new AdminMapJspBean( );
        bean.init( request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );

        assertNotNull( bean.getMap( request ) );
    }

    @Test
    public void testCacheInfosWithInvalidIndex( ) throws AccessDeniedException
    {
        for ( String strIndex : new String [ ] {
                "9999", "abc", "-1"
        } )
        {
            MockHttpServletRequest request = login( CacheJspBean.RIGHT_CACHE_MANAGEMENT );
            request.addParameter( "id_cache", strIndex );
            CacheJspBean bean = new CacheJspBean( );
            bean.init( request, CacheJspBean.RIGHT_CACHE_MANAGEMENT );

            assertNotNull( bean.getCacheInfos( request ) );
        }
    }

    @Test
    public void testPluginDescriptionOfUnknownPlugin( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = login( PluginJspBean.RIGHT_MANAGE_PLUGINS );
        request.addParameter( "plugin_name", "nope" );
        _pluginBean.init( request, PluginJspBean.RIGHT_MANAGE_PLUGINS );

        assertNotNull( _pluginBean.getPluginDescription( request ) );
    }

    @Test
    public void testInsertServicesWithoutSelectedText( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "input", "x" );

        assertNotNull( new InsertServiceSelectorJspBean( ).getServicesListPage( request ) );
    }

    @Test
    public void testAssignUsersOfUnknownRole( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = login( RoleManagementJspBean.RIGHT_MANAGE_ROLES );
        request.addParameter( "role_key", "nope" );
        _roleBean.init( request, RoleManagementJspBean.RIGHT_MANAGE_ROLES );

        assertNotNull( _roleBean.getAssignUsers( request ) );
    }

    @Test
    public void testModifyUnknownPortlet( ) throws AccessDeniedException
    {
        for ( String strPortletId : new String [ ] {
                "999999", "abc"
        } )
        {
            MockHttpServletRequest request = login( AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );
            request.addParameter( Parameters.PORTLET_ID, strPortletId );
            _portletBean.init( request, AdminPageJspBean.RIGHT_MANAGE_ADMIN_SITE );

            assertEquals( "AdminMessage.jsp", _portletBean.doModifyPortlet( request ) );
        }
    }
}
