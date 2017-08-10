/*
 * Copyright (c) 2002-2017, Mairie de Paris
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
package fr.paris.lutece.portal.service.content;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mock.web.MockHttpServletRequest;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.test.LuteceTestCase;

public class XPageAppServiceTest extends LuteceTestCase
{
    public void testGetXPageApplicationsList( )
    {
        Collection<XPageApplicationEntry> listXPageApps = XPageAppService.getXPageApplicationsList( );
        // Assert default XPages are loaded
        assertTrue( listXPageApps.size( ) >= 2 );
    }

    public void testEnabledState( ) throws LuteceInitException
    {
        XPageApplicationEntry entry = new XPageApplicationEntry( );
        entry.setClassName( TestXPageApplication.class.getName( ) );
        entry.setId( "testEnableXPageApplication" );
        entry.setPluginName( "core" ); // core is an always enabled plugin

        XPageAppService.registerXPageApplication( entry );

        assertTrue( isTestXPageApplicationActive( entry ) );

        entry.setEnabled( false );
        XPageAppService.registerXPageApplication( entry );
        assertFalse( isTestXPageApplicationActive( entry ) );

        entry.setPluginName( "bogus_inexistant_plugin" );
        XPageAppService.registerXPageApplication( entry );
        assertFalse( isTestXPageApplicationActive( entry ) );

        entry.setEnabled( true );
        XPageAppService.registerXPageApplication( entry );
        assertFalse( isTestXPageApplicationActive( entry ) );
    }

    private boolean isTestXPageApplicationActive( XPageApplicationEntry entry )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( "page", entry.getId( ) );
        ContentService cs = PortalService.getInvokedContentService( request );
        try
        {
            cs.getPage( request, 0 );
            return true;
        }
        catch( UserNotSignedException | SiteMessageException e )
        {
            return false;
        }
    }

    @SuppressWarnings( "serial" )
    static class TestXPageApplication implements XPageApplication
    {

        @Override
        public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws UserNotSignedException, SiteMessageException
        {
            return new XPage( );
        }
    }
}
