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
package fr.paris.lutece.portal.service.cache;

import javax.servlet.http.HttpServletRequest;

/**
 * Service interface for XPage path caching
 */
public interface IPathCacheService
{

    /** Bean name */
    String BEAN_NAME = "pathCacheService";

    /**
     * Constructs a cache key
     * 
     * @param strXPageName
     *            the XPage name
     * @param nMode
     *            the mode
     * @param request
     *            the request
     * @return the cache key, or <code>null</code> if for instance the cache is not enabled
     */
    String getKey( String strXPageName, int nMode, HttpServletRequest request );

    /**
     * Constructs a cache key
     * 
     * @param strXPageName
     *            the XPage name
     * @param nMode
     *            the mode
     * @param strTitlesUrls
     *            list of links (url and titles)
     * @param request
     *            the request the request
     * @return the cache key, or <code>null</code> if for instance the cache is not enabled
     */
    String getKey( String strXPageName, int nMode, String strTitlesUrls, HttpServletRequest request );

    /**
     * Get the path html from cache
     * 
     * @param strKey
     *            the cache key
     * @return the path html, or <code>null</code> if it's not in cache or the cache is not enabled
     */
    String getFromCache( String strKey );

    /**
     * Put a path in cache
     * 
     * @param strKey
     *            the cache key
     * @param path
     *            the path html
     */
    void putInCache( String strKey, String path );

}
