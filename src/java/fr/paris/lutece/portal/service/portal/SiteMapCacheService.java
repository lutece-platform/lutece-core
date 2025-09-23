/*
 * Copyright (c) 2002-2025, City of Paris
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
package fr.paris.lutece.portal.service.portal;

import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;
import fr.paris.lutece.portal.service.page.PageEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class SiteMapCacheService
{

    private static final String CACHE_NAME = "SiteMapService";

    @Inject
    @LuteceCache( cacheName = CACHE_NAME, keyType = String.class, valueType = String.class, enable = true )
    private Lutece107Cache<String, String> _cacheSiteMap;

    public String get( String strKey )
    {
        String strCachedPage = _cacheSiteMap.isCacheEnable( ) && !_cacheSiteMap.isClosed( ) ? _cacheSiteMap.get( strKey ) : null;
        return strCachedPage;
    }

    public void put( String strKey, String strPage )
    {
        if ( _cacheSiteMap.isCacheEnable( ) && !_cacheSiteMap.isClosed( ) )
        {
            synchronized( strKey )
            {
                _cacheSiteMap.put( strKey, strPage );
            }
        }
    }

    /**
     * Process a page event
     *
     * @param event
     *            The event to process
     */
    public void processPageEvent( @Observes PageEvent event )
    {
        // page was added, removed or updated; clear cache
        _cacheSiteMap.resetCache( );
    }
}
