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
package fr.paris.lutece.portal.web.admin;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;

import java.util.Locale;

import javax.servlet.http.HttpSession;


/**
 * AdminMessageJspBeanTest Test Class
 *
 */
public class AdminMessageJspBeanTest extends LuteceTestCase
{
    /**
     * Test of getMessage method, of class fr.paris.lutece.portal.web.admin.AdminMessageJspBean.
     */
    public void testGetMessage(  ) throws AccessDeniedException
    {
        System.out.println( "getMessage" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );

        AdminUser user = new AdminUser(  );
        user.setLocale( Locale.FRANCE );
        request.registerAdminUser( user );

        String strTitle = "WARNING_MESSAGE";
        boolean bCancelButton = false;
        Object[] messageArgs = { "WARNING_MESSAGE" };
        AdminMessage message = new AdminMessage( "WARNING_MESSAGE", messageArgs, strTitle, "", "",
                AdminMessage.TYPE_WARNING, bCancelButton );
        HttpSession session = request.getSession( true );
        session.setAttribute( "LUTECE_ADMIN_MESSAGE", message );

        AdminMessageJspBean instance = new AdminMessageJspBean(  );
        instance.getMessage( request );
    }
}
