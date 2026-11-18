/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.web.portlet;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.AliasPortlet;
import fr.paris.lutece.portal.business.portlet.AliasPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenHandler;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;

/**
 * AliasPortletJspBean Test Class
 */
public class AliasPortletJspBeanTest extends LuteceTestCase
{
    private static final String MESSAGE_PORTLET_TYPE_NOT_FOUND = "portal.site.message.portletTypeNotFound";

    @Inject
    private AliasPortletJspBean _bean;

    @Test
    public void testGetCreateWithoutPortletType( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        _bean.init( request, AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE );

        String strHtml = _bean.getCreate( request );

        assertTrue( strHtml.contains( I18nService.getLocalizedString( MESSAGE_PORTLET_TYPE_NOT_FOUND, AdminUserService.getLocale( request ) ) ) );
        assertFalse( strHtml.contains( "form-portlet" ) );
    }

    @Inject
    private ISecurityTokenService _securityTokenService;

    private AliasPortlet createAlias( String strName, int nAliasId )
    {
        AliasPortlet alias = new AliasPortlet( );
        alias.setName( strName );
        alias.setPageId( PortalService.getRootPageId( ) );
        alias.setColumn( 1 );
        alias.setOrder( 1 );
        alias.setAliasId( nAliasId );
        alias.setRole( Page.ROLE_NONE );
        AliasPortletHome.getInstance( ).create( alias );

        return alias;
    }

    private MockHttpServletRequest newRequest( AliasPortlet target, String strName, int nPageId ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        _bean.init( request, AliasPortletJspBean.RIGHT_MANAGE_ADMIN_SITE );
        request.setParameter( "portlet_name", strName );
        request.setParameter( "order", "1" );
        request.setParameter( "column", "1" );
        request.setParameter( "accept_alias", "0" );
        request.setParameter( Parameters.DISPLAY_PORTLET_TITLE, "0" );
        request.setParameter( "alias_id", Integer.toString( target.getId( ) ) );
        request.setParameter( "page_id", Integer.toString( nPageId ) );

        return request;
    }

    /**
     * The creation and the modification of an alias portlet need the token of the common form; the modification stores the page, the role
     * and the devices of the form
     */
    @Test
    public void testCreateAndModifyCheckTheTokenAndStoreTheCommonData( ) throws AccessDeniedException
    {
        AliasPortlet target = createAlias( "junit alias target", 0 );
        AliasPortlet alias = createAlias( "junit alias", target.getId( ) );
        Page page = new Page( );
        page.setParentPageId( PortalService.getRootPageId( ) );
        page.setName( "junit alias page" );
        PageHome.create( page );

        try
        {
            // creation without token: refused
            MockHttpServletRequest request = newRequest( target, "junit alias created", PortalService.getRootPageId( ) );
            _bean.doCreate( request );
            AdminMessage message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.USER_ACCESS_DENIED, Locale.FRENCH ), message.getText( Locale.FRENCH ) );

            // modification without token: refused, nothing changed
            request = newRequest( target, "junit alias modified", page.getId( ) );
            request.setParameter( "portlet_id", Integer.toString( alias.getId( ) ) );
            _bean.doModify( request );
            message = AdminMessageService.getMessage( request );
            assertNotNull( message );
            assertEquals( I18nService.getLocalizedString( Messages.USER_ACCESS_DENIED, Locale.FRENCH ), message.getText( Locale.FRENCH ) );
            assertEquals( "junit alias", PortletHome.findByPrimaryKey( alias.getId( ) ).getName( ) );

            // modification with the token of the modification form: the page, the role and the devices are stored
            request = newRequest( target, "junit alias modified", page.getId( ) );
            request.setParameter( "portlet_id", Integer.toString( alias.getId( ) ) );
            request.setParameter( Parameters.ROLE, "junit_role" );
            request.setParameter( Parameters.DISPLAY_ON_SMALL_DEVICE, "1" );
            request.setParameter( SecurityTokenHandler.MARK_CSRF_TOKEN, _securityTokenService.getToken( request, "admin/portlet/modify_portlet.html" ) );
            _bean.doModify( request );

            Portlet stored = PortletHome.findByPrimaryKey( alias.getId( ) );
            assertEquals( "junit alias modified", stored.getName( ) );
            assertEquals( page.getId( ), stored.getPageId( ) );
            assertEquals( "junit_role", stored.getRole( ) );
            assertEquals( Portlet.FLAG_DISPLAY_ON_SMALL_DEVICE, stored.getDeviceDisplayFlags( ) );
        }
        finally
        {
            AliasPortletHome.getInstance( ).remove( alias );
            AliasPortletHome.getInstance( ).remove( target );
            PageHome.remove( page.getId( ) );
        }
    }
}
