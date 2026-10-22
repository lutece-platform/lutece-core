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
package fr.paris.lutece.portal.service.plugin;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Plugin Test Class
 */
public class PluginTest extends LuteceTestCase
{
    private static final String PROPERTY_DEFAULT_ICON_URL = "plugin.image.defaultIconUrl";

    @Test
    public void testResolveIconUrlEmpty( )
    {
        assertEquals( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL ), Plugin.resolveIconUrl( "" ) );
        assertEquals( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL ), Plugin.resolveIconUrl( null ) );
    }

    @Test
    public void testResolveIconUrlExisting( )
    {
        assertEquals( "themes/admin/shared/images/apps.svg?v=1", Plugin.resolveIconUrl( "themes/admin/shared/images/apps.svg?v=1" ) );
    }

    @Test
    public void testResolveIconUrlMissing( )
    {
        assertEquals( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL ), Plugin.resolveIconUrl( "images/admin/skin/feature_default_icon.png" ) );
    }

    @Test
    public void testResolveIconUrlDataUri( )
    {
        String strDataUri = "data:image/svg+xml;base64,PHN2Zy8+";
        assertEquals( strDataUri, Plugin.resolveIconUrl( strDataUri ) );
    }

    @Test
    public void testResolveIconUrlInvalidPath( )
    {
        assertEquals( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL ), Plugin.resolveIconUrl( "images/admin/skin/\0icon.png" ) );
    }

    @Test
    public void testResolveIconUrlAbsolute( )
    {
        assertEquals( "https://lutece.paris.fr/missing.png", Plugin.resolveIconUrl( "https://lutece.paris.fr/missing.png" ) );
    }
}
