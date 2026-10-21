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
package fr.paris.lutece.portal.web.admin;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;

/**
 * AdminMessageJspBeanTest Test Class
 *
 */
public class AdminMessageJspBeanTest extends LuteceTestCase
{
    @Inject
    private AdminMessageJspBean instance;
    
    /**
     * Test of getMessage method, of class fr.paris.lutece.portal.web.admin.AdminMessageJspBean.
     */
	@Test
    public void testGetMessage( ) throws AccessDeniedException
    {
        System.out.println( "getMessage" );

        MockHttpServletRequest request = new MockHttpServletRequest( );

        AdminUser user = new AdminUser( );
        user.setLocale( Locale.FRANCE );
        AdminUserUtils.registerAdminUser( request, user );

        String strTitle = "WARNING_MESSAGE";
        boolean bCancelButton = false;
        Object [ ] messageArgs = {
                "WARNING_MESSAGE"
        };
        AdminMessage message = new AdminMessage( "WARNING_MESSAGE", messageArgs, strTitle, "", "", AdminMessage.TYPE_WARNING, bCancelButton );
        HttpSession session = request.getSession( true );
        session.setAttribute( "LUTECE_ADMIN_MESSAGE", message );

        assertTrue( StringUtils.isNotEmpty( instance.getMessage( request ) ) );
    }
    /**
     * The button of a message given the screen the user came from goes back in the history, and links to that screen when there is no page to go
     * back to (a new tab)
     */
    @Test
    public void testGetMessageReturningTo( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newAdminRequest( );
        AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP,
                "jsp/admin/plugins/workflow/CreateAction.jsp?id_workflow=1&plugin_name=workflow" );

        String strPage = instance.getMessage( request );

        assertTrue( strPage.contains( "href=\"jsp/admin/plugins/workflow/CreateAction.jsp?id_workflow=1&amp;plugin_name=workflow\"" ), strPage );
        assertTrue( strPage.contains( "data-lutece-action=\"back\"" ), strPage );
        assertFalse( strPage.contains( "AdminMenu.jsp" ), strPage );
    }

    /**
     * Without the screen the user came from, the button links to the admin home
     */
    @Test
    public void testGetMessageWithoutReturnUrl( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newAdminRequest( );
        AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );

        String strPage = instance.getMessage( request );

        assertTrue( strPage.contains( "href=\"jsp/admin/AdminMenu.jsp\"" ), strPage );
        assertTrue( strPage.contains( "data-lutece-action=\"back\"" ), strPage );
    }

    /**
     * A return URL with a scheme that runs script is never written in the link
     */
    @Test
    public void testGetMessageReturningToScriptUrl( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = newAdminRequest( );
        AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP, "javascript:alert(1)" );

        String strPage = instance.getMessage( request );

        assertFalse( strPage.contains( "javascript:" ), strPage );
        assertTrue( strPage.contains( "href=\"jsp/admin/AdminMenu.jsp\"" ), strPage );
    }

    private static MockHttpServletRequest newAdminRequest( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setLocale( Locale.FRANCE );
        AdminUserUtils.registerAdminUser( request, user );
        return request;
    }
}
