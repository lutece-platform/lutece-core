/*
 * Copyright (c) 2002-2024, City of Paris
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
package fr.paris.lutece.portal.web.system;

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.portal.service.cache.LuteceCacheEvent;
import fr.paris.lutece.portal.service.cache.LuteceCacheEvent.LuteceCacheEventType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class TestResetCacheObserver
{

    private Map<String, Long> _callCount = new HashMap<String, Long>( );

    public void notify( @Observes LuteceCacheEvent event )
    {
        if ( null != event.getSource( ) && LuteceCacheEventType.RESET == event.getType( ) )
        {
            if ( !_callCount.containsKey( event.getSource( ).getName( ) ) )
            {
                _callCount.put( event.getSource( ).getName( ), 0l );
            }
            _callCount.put( event.getSource( ).getName( ), _callCount.get( event.getSource( ).getName( ) ) + 1 );
        }
    }

    public void reset( )
    {
        _callCount.clear( );
        ;
    }

    public long getCallCount( )
    {
        return _callCount.size( );
    }

    public long getCallCount( String cacheName )
    {
        return _callCount.containsKey( cacheName ) ? _callCount.get( cacheName ) : 0;
    }

}
