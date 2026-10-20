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
package fr.paris.lutece.portal.service.mail;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The link to the site in the e-mails of the back office (LUT-33153, point 8 of the review): the template writes it
 * from the site name and getSiteUrl, so a site name holding HTML is shown as text.
 */
public class MailSiteUrlTest extends LuteceTestCase
{
    private static final String [ ] TEMPLATES = {
            "admin/admin_email_forgot_login.html", "admin/admin_email_forgot_password.html", "admin/user/user_email_change_status.html",
            "admin/user/notify_user_account_created.html"
    };

    /**
     * getSiteUrl joins the base URL and the path of the back office or the front office.
     */
    @Test
    public void testSiteUrl( )
    {
        assertTrue( MailService.getSiteUrl( "https://site.example/", false ).startsWith( "https://site.example/" ) );
        assertTrue( MailService.getSiteUrl( null, true ).length( ) >= 0 );
    }

    /**
     * A site name holding HTML is written as text in the link of each e-mail.
     */
    @Test
    public void testSiteNameIsEscapedInTheLink( )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "site_name", "Site \"><img src=x onerror=alert(1)>" );
        model.put( "site_url", "https://site.example/jsp/admin/AdminMenu.jsp" );
        model.put( "login", "login" );
        model.put( "login_url", "https://site.example/jsp/admin/AdminLogin.jsp" );
        model.put( "timestamp", 1L );
        model.put( "token", "token" );
        model.put( "user_id", 1 );
        model.put( "lock_reset_token_to_session", false );
        model.put( "reset_password_validity", new Date( ) );
        model.put( "password", "password" );
        AdminUser user = new AdminUser( );
        user.setAccessCode( "code" );
        user.setFirstName( "first" );
        user.setLastName( "last" );
        user.setStatus( 0 );
        model.put( "user", user );
        for ( String strTemplate : TEMPLATES )
        {
            String strHtml = AppTemplateService.getTemplate( strTemplate, Locale.ENGLISH, model ).getHtml( );
            assertTrue( strHtml.contains( "href=\"https://site.example/jsp/admin/AdminMenu.jsp\"" ), strTemplate + "\n" + strHtml );
            assertFalse( strHtml.contains( "<img" ), strTemplate + "\n" + strHtml );
            assertTrue( strHtml.contains( "&lt;img" ), strTemplate + "\n" + strHtml );
        }
    }
}
