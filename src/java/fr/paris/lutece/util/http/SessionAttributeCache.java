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
package fr.paris.lutece.util.http;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Reads session attributes once per request.
 * <p>
 * With a distributed session (OpenLiberty sessionCache over Hazelcast), every getAttribute is a network call, including for an absent attribute
 * (an anonymous user has no Lutece user, no selected locale...). The services called several times while building a page (current user, locale,
 * base URL) read their attribute through this cache: the values read, absent ones included, are kept in a request attribute for the session of the
 * request. The attributes must be written through this class too, so that the requests reading them afterwards see the new value.
 * </p>
 */
public final class SessionAttributeCache
{
    private static final String REQUEST_ATTRIBUTE_CACHE = SessionAttributeCache.class.getName( );
    private static final Object ABSENT = new Object( );

    /**
     * Private constructor
     */
    private SessionAttributeCache( )
    {
    }

    /**
     * Gets an attribute of the session of the request, read once per request
     *
     * @param request
     *            The request
     * @param strName
     *            The attribute name
     * @return the attribute value, or null if there is no session or no such attribute
     */
    public static Object getAttribute( HttpServletRequest request, String strName )
    {
        if ( request == null )
        {
            return null;
        }
        HttpSession session = request.getSession( false );
        if ( session == null )
        {
            return null;
        }
        Map<String, Object> cache = getCache( request, session );
        Object value = cache.get( strName );
        if ( value == null )
        {
            value = session.getAttribute( strName );
            cache.put( strName, ( value != null ) ? value : ABSENT );
            return value;
        }
        return ( value == ABSENT ) ? null : value;
    }

    /**
     * Sets an attribute of the session of the request (created if needed)
     *
     * @param request
     *            The request
     * @param strName
     *            The attribute name
     * @param value
     *            The value, null to remove the attribute
     */
    public static void setAttribute( HttpServletRequest request, String strName, Object value )
    {
        HttpSession session = request.getSession( true );
        if ( value != null )
        {
            session.setAttribute( strName, value );
        }
        else
        {
            session.removeAttribute( strName );
        }
        getCache( request, session ).put( strName, ( value != null ) ? value : ABSENT );
    }

    /**
     * Removes an attribute of the session of the request, if there is a session
     *
     * @param request
     *            The request
     * @param strName
     *            The attribute name
     */
    public static void removeAttribute( HttpServletRequest request, String strName )
    {
        HttpSession session = request.getSession( false );
        if ( session != null )
        {
            session.removeAttribute( strName );
            getCache( request, session ).put( strName, ABSENT );
        }
    }

    /**
     * Gets the cache of the request for the given session: a new session (invalidated then created during the request) starts with an empty cache
     *
     * @param request
     *            The request
     * @param session
     *            The current session of the request
     * @return the cache
     */
    @SuppressWarnings( "unchecked" )
    private static Map<String, Object> getCache( HttpServletRequest request, HttpSession session )
    {
        Object [ ] holder = (Object [ ]) request.getAttribute( REQUEST_ATTRIBUTE_CACHE );
        if ( holder == null || !session.getId( ).equals( holder [0] ) )
        {
            holder = new Object [ ] {
                    session.getId( ), new HashMap<String, Object>( )
            };
            request.setAttribute( REQUEST_ATTRIBUTE_CACHE, holder );
        }
        return (Map<String, Object>) holder [1];
    }
}
