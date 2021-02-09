/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.portal.util;

import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Markers;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Connection;

import java.util.HashMap;

/**
 * This class tests the class fr.paris.lutece.portal.util.AppInit.
 */
public class AppInitTest extends LuteceTestCase
{
    /**
     * Check Template service
     */
    public void testTemplateService( )
    {
        HashMap<String, String> model = new HashMap<>( );
        model.put( "web_mail", "lutece@paris.fr" );
        model.put( Markers.PAGE_MAIN_MENU, "menu" );

        HtmlTemplate t = AppTemplateService.getTemplate( "skin/site/portal_footer.html", LocaleService.getDefault( ), model );
        assertNotNull( t );
    }

    /**
     * Check some keys from config.properties
     */
    public void testInitConfigProperties( )
    {
        assertEquals("UTF-8", AppPropertiesService.getProperty( "lutece.encoding" ) );
    }

    /**
     * Check some keys from lutece.properties
     */
    public void testInitLuteceProperties( )
    {
        assertNotNull( AppPropertiesService.getProperty( "lutece.page.root" ) );
        assertNotNull( AppPropertiesService.getProperty( "lutece.root.name" ) );
    }

    /**
     * Check pool initialization for AppConnection Service
     */
    public void testInitAppConnectionService( )
    {
        Connection connection = AppConnectionService.getConnection( );
        assertNotNull( connection );
        AppConnectionService.freeConnection( connection );
    }
}
