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
package fr.paris.lutece.portal.service.template;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.template.DatabaseTemplateHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * The templates stored in the database are FreeMarker sources: their markers must reach the rendering
 */
public class DatabaseTemplateServiceTest extends LuteceTestCase
{
    private static final String KEY = "core_first_alert_mail";
    private static final String TEMPLATE = "<p>Your account expires on ${date_valid}: <a href=\"${url}\">click here</a>.</p>";

    @Test
    public void testMarkersOfALinkAreRendered( )
    {
        String strOriginalTemplate = DatabaseTemplateService.getTemplateFromKey( KEY, true );
        DatabaseTemplateService.updateTemplate( KEY, TEMPLATE );

        try
        {
            String strTemplate = DatabaseTemplateService.getTemplateFromKey( KEY );
            assertEquals( TEMPLATE, strTemplate );

            Map<String, Object> model = new HashMap<>( );
            model.put( "date_valid", "2026-12-31" );
            model.put( "url", "https://example.org/jsp/admin/user/ReactivateAccount.jsp" );
            String strHtml = AppTemplateService.getTemplateFromStringFtl( strTemplate, Locale.ENGLISH, model ).getHtml( );
            assertTrue( strHtml.contains( "<a href=\"https://example.org/jsp/admin/user/ReactivateAccount.jsp\">" ), strHtml );
        }
        finally
        {
            DatabaseTemplateHome.updateTemplate( KEY, strOriginalTemplate );
        }
    }
}
