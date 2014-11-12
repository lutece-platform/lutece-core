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
package fr.paris.lutece.portal.service.page;

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.concurrent.ConcurrentHashMap;


/**
 * Page Cache Service
 */
public class PageCacheService extends AbstractCacheableService
{
    private static final String SERVICE_NAME = "Page Cache Service";

    // Performance patch
    private static ConcurrentHashMap<String, String> _keyMemory = new ConcurrentHashMap<String, String>(  );

    /**
     * {@inheritDoc }
     */
    public String getName(  )
    {
        return SERVICE_NAME;
    }

    /**
     * Get a memory key
     * @param strKey The key
     * @return The key
     */
    String getKey( String strKey )
    {
        String keyInMemory = _keyMemory.putIfAbsent( strKey, strKey );

        if ( keyInMemory != null )
        {
            return keyInMemory;
        }

        return strKey;
    }

    /**
     * @see net.sf.ehcache.event.CacheEventListener#notifyElementEvicted(net.sf.ehcache.Ehcache,
     *      net.sf.ehcache.Element)
     * @param cache The Ehcache object
     * @param element The Element object
     */
    @Override
    public void notifyElementEvicted( Ehcache cache, Element element )
    {
        removeKeyFromMap( element );
    }

    /**
     * @see net.sf.ehcache.event.CacheEventListener#notifyElementExpired(net.sf.ehcache.Ehcache,
     *      net.sf.ehcache.Element)
     * @param cache The Ehcache object
     * @param element The Element object
     */
    @Override
    public void notifyElementExpired( Ehcache cache, Element element )
    {
        removeKeyFromMap( element );
    }

    /**
     * @see net.sf.ehcache.event.CacheEventListener#notifyElementRemoved(net.sf.ehcache.Ehcache,
     *      net.sf.ehcache.Element)
     * @param cache The Ehcache object
     * @param element The Element object
     */
    @Override
    public void notifyElementRemoved( Ehcache cache, Element element )
    {
        removeKeyFromMap( element );
    }

    /**
     * @see net.sf.ehcache.event.CacheEventListener#notifyRemoveAll(net.sf.ehcache.Ehcache)
     * @param cache The Ehcache object
     */
    @Override
    public void notifyRemoveAll( Ehcache cache )
    {
        _keyMemory.clear(  );
    }

    /**
     * @param element The Element object
     */
    public void removeKeyFromMap( Element element )
    {
        _keyMemory.remove( (String) element.getKey(  ) );
    }

    /**
     * @see java.lang.Object#clone()
     * @return the instance
     */
    @Override
    public Object clone(  )
    {
        throw new RuntimeException( "This class shouldn't be cloned" );
    }
}
