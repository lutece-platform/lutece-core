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
package fr.paris.lutece.portal.web.includes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginEvent;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

public class LinksIncludeCacheServiceTest extends LuteceTestCase
{
    private @Inject LinksIncludeCacheService service;
    private boolean bOrigCacheStatus;

    @BeforeEach
    protected void setUp( ) throws Exception
    {
        bOrigCacheStatus = service.isCacheEnable( );
        service.enableCache( true );
    }

    @AfterEach
    protected void tearDown( ) throws Exception
    {
        service.enableCache( bOrigCacheStatus );
    }
    @Test
    public void testGetCacheKey( )
    {
        Set<String> keys = new HashSet<>( );
        for ( int nMode : new int [ ] {
                0, 1
        } )
        {
            for ( String strPage : new String [ ] {
                    null, "test", "test2"
            } )
            {
                for ( Locale locale : new Locale [ ] {
                        null, Locale.FRANCE, Locale.FRENCH
                } )
                {
                    String key = service.getCacheKey( nMode, strPage, locale );
                    assertTrue( "key " + key + " was already generated", keys.add( key ) );
                }
            }
        }
    }
    @Test
    public void testProcessPluginEvent( )
    {
        Map<String, Object> value = new HashMap<String, Object>( );
        value.put( "key1", "value1" );
        service.put( "key", value );
        
        assertEquals( "value1", service.get( "key" ).get( "key1" ) );
        
        
        service.processPluginEvent( null );
        assertNull( service.get( "key" ) );
        for ( Plugin plugin : new Plugin [ ] {
                null, PluginService.getCore( )
        } )
        {
            for ( int eventType : new int [ ] {
                    PluginEvent.PLUGIN_INSTALLED, PluginEvent.PLUGIN_POOL_CHANGED, PluginEvent.PLUGIN_UNINSTALLED
            } )
            {
                service.put( "key", value );
                assertEquals( "value1", service.get( "key" ).get( "key1" ) );
                PluginEvent event = new PluginEvent( plugin, eventType );
                service.processPluginEvent( event );
                assertNull( service.get( "key" ) );
            }
        }
    }

}
