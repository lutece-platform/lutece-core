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
package fr.paris.lutece.portal.service.message;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;


/**
 * AdminMessageService Test Class
 *
 */
public class AdminMessageServiceTest extends LuteceTestCase
{
    /**
     * Test of getMessageUrl method, of class fr.paris.lutece.portal.service.message.AdminMessageService.
     */
    public void testGetMessageUrl(  )
    {
        System.out.println( "getMessageUrl" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        String strMessageKey = Messages.MANDATORY_FIELDS;
        String strButtonUrl = "url";
        String strTarget = "target";
        Object[] args = { "arg1", "arg2" };

        AdminMessageService.getMessageUrl( request, strMessageKey );
        AdminMessageService.getMessageUrl( request, strMessageKey, AdminMessage.TYPE_STOP );
        AdminMessageService.getMessageUrl( request, strMessageKey, strButtonUrl, AdminMessage.TYPE_STOP );
        AdminMessageService.getMessageUrl( request, strMessageKey, strButtonUrl, strTarget );
        AdminMessageService.getMessageUrl( request, strMessageKey, strButtonUrl, strTarget, AdminMessage.TYPE_STOP );
        AdminMessageService.getMessageUrl( request, strMessageKey, args, AdminMessage.TYPE_STOP );
        AdminMessageService.getMessageUrl( request, strMessageKey, args, strButtonUrl, AdminMessage.TYPE_STOP );
    }

    /**
     * Test of getMessage method, of class fr.paris.lutece.portal.service.message.AdminMessageService.
     */
    public void testGetMessage(  )
    {
        System.out.println( "getMessage" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        String strMessageKey = Messages.MANDATORY_FIELDS;
        String strButtonUrl = "url";
        String strTarget = "target";
        AdminMessageService.getMessageUrl( request, strMessageKey, strButtonUrl, strTarget, AdminMessage.TYPE_STOP );

        AdminMessage message = AdminMessageService.getMessage( request );
        assertEquals( message.getText( LocaleService.getDefault(  ) ),
            I18nService.getLocalizedString( strMessageKey, LocaleService.getDefault(  ) ) );
        assertEquals( message.getUrl(  ), strButtonUrl );
        assertEquals( message.getTarget(  ), strTarget );
        message.getTitle( LocaleService.getDefault(  ) );
        message.isCancel(  );
    }
}
