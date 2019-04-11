/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.portal.business.user.menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.test.LuteceTestCase;

public class DocumentationAdminUserMenuItemProviderTest extends LuteceTestCase
{
    private static final String PROPERTY_DOCUMENTATION_SUMMARY_URL = "lutece.documentation.summary.url";

    public void testIsItemProviderInvoked( )
    {
        assertNotNull( AppPropertiesService.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL ) );
        assertTrue( new DocumentationAdminUserMenuItemProvider( ).isItemProviderInvoked( new MockHttpServletRequest( ) ) );
    }

    public void testIsItemProviderInvokedNullDocURL( ) throws IOException
    {
        String documentationUrl = changeDocumentationURL( null );
        try
        {
            assertNull( AppPropertiesService.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL ) );
            assertFalse( new DocumentationAdminUserMenuItemProvider( ).isItemProviderInvoked( new MockHttpServletRequest( ) ) );
        }
        finally
        {
            changeDocumentationURL( documentationUrl );
        }
    }

    private String changeDocumentationURL( String url ) throws IOException
    {
        File file = new File( getResourcesDir( ), "WEB-INF/conf/lutece.properties" );
        try( FileInputStream is = new FileInputStream( file ) )
        {
            Properties properties = new Properties( );
            properties.load( is );
            String previous = properties.getProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL );
            if ( url == null )
            {
                properties.remove( PROPERTY_DOCUMENTATION_SUMMARY_URL );
            }
            else
            {
                properties.setProperty( PROPERTY_DOCUMENTATION_SUMMARY_URL, url );
            }
            try( FileOutputStream os = new FileOutputStream( file ) )
            {
                properties.store( os, "Junit" );
            }
            AppPropertiesService.reloadAll( );
            return previous;
        }
    }
}
