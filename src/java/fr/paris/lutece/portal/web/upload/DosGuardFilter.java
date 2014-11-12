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
package fr.paris.lutece.portal.web.upload;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * A rewrite of the multipart filter from the com.oreilly.servlet package. The
 * rewrite allows us to use initialization parameters specified in the Lutece
 * configuration files.
 */
public class DosGuardFilter implements Filter
{
    // Initial capacity of the HashMap
    private static final int INITIAL_CAPACITY = 100;
    private FilterConfig _filterConfig;

    // The size under which requests are allowed systematically
    private int _nMinContentLength;

    // The minimum interval allowed between two requests from the same client
    private int _nMinInterval;

    // The HashMap used to store IP/time entries
    private Map<String, Long> _mapLastRequestTimes;

    // The LinkedList used to store entries in their order of arrival (to speed
    // up cleaning the HashMap)
    private LinkedList<Entry> _listOrderedRequests;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig config ) throws ServletException
    {
        _filterConfig = config;
        _mapLastRequestTimes = new HashMap<String, Long>( INITIAL_CAPACITY );
        _listOrderedRequests = new LinkedList<Entry>(  );

        try
        {
            String paramValue = _filterConfig.getInitParameter( "minContentLength" );

            if ( paramValue != null )
            {
                _nMinContentLength = Integer.parseInt( paramValue );
            }

            paramValue = _filterConfig.getInitParameter( "minInterval" );

            if ( paramValue != null )
            {
                _nMinInterval = Integer.parseInt( paramValue );
            }
        }
        catch ( NumberFormatException ex )
        {
            ServletException servletEx = new ServletException( ex.getMessage(  ) );
            servletEx.initCause( ex );
            throw servletEx;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy(  )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        // DOS check
        if ( this.isAllowed( request.getRemoteAddr(  ), request.getContentLength(  ) ) )
        {
            chain.doFilter( request, response );
        }
        else
        {
            throw new ServletException( "DOS Guard : Too many upload from the same IP !" );
        }
    }

    /**
     * Checks if a client is allowed to make a request at the present time.
     *
     * @param strRemoteAddr the IP address of the client
     * @param iContentLength the size of the request
     * @return true if allowed, false otherwize
     */
    public synchronized boolean isAllowed( String strRemoteAddr, int iContentLength )
    {
        AppLogService.debug( "DosGuard : isAllowed(" + strRemoteAddr + ", " + iContentLength + ")" );

        // Ignore the requests under the minimum size
        if ( iContentLength < _nMinContentLength )
        {
            AppLogService.debug( "ContentLength is below minimum, ignored" );

            return true;
        }

        // Record the time of this request
        long lRequestTime = System.currentTimeMillis(  );
        AppLogService.debug( "Request time : " + lRequestTime );

        // Test if IP was previously recorded
        Long previousRequestTime = _mapLastRequestTimes.get( strRemoteAddr );
        AppLogService.debug( "Previous request time : " + previousRequestTime );

        if ( previousRequestTime != null )
        {
            AppLogService.debug( "IP is in the map" );

            // Test if IP is allowed to make a new request
            if ( lRequestTime > ( previousRequestTime.longValue(  ) + _nMinInterval ) )
            {
                AppLogService.debug( "IP is allowed to make a new request" );

                // Clean up
                this.cleanExpiredEntries(  );

                // Update the map with the new time
                _mapLastRequestTimes.put( strRemoteAddr, Long.valueOf( lRequestTime ) );

                // Add a new entry in the list
                _listOrderedRequests.addFirst( new Entry( strRemoteAddr, lRequestTime ) );

                return true;
            }

            AppLogService.debug( "IP is not allowed to make a new request" );

            return false;
        }

        AppLogService.debug( "IP is not in the map" );

        // Clean up
        this.cleanExpiredEntries(  );

        // Add the IP and the time to the map
        _mapLastRequestTimes.put( strRemoteAddr, Long.valueOf( lRequestTime ) );

        // Add a new entry in the list
        _listOrderedRequests.addFirst( new Entry( strRemoteAddr, lRequestTime ) );

        return true;
    }

    /**
     * Cleans the internal map from expired entries.
     */
    private void cleanExpiredEntries(  )
    {
        AppLogService.debug( "DosGuard.class : cleanExpiredEntries()" );

        if ( _listOrderedRequests.size(  ) != 0 )
        {
            // Expired entries are those where the IP can't be blocked anymore
            long lMinTime = System.currentTimeMillis(  ) - _nMinInterval;

            AppLogService.debug( "Min time : " + lMinTime );

            // Read entries from the list, remove them as long as they are expired
            boolean bDone = false;

            while ( !bDone && ( _listOrderedRequests.size(  ) > 0 ) )
            {
                // The list is ordered by arrival time, so the last one is the
                // oldest
                Entry lastEntry = _listOrderedRequests.getLast(  );

                if ( lastEntry.getRequestTime(  ) < lMinTime )
                {
                    // The entry is expired, remove it from the map and the list
                    _mapLastRequestTimes.remove( lastEntry.getRemoteAddr(  ) );
                    _listOrderedRequests.removeLast(  );

                    AppLogService.debug( "Removing [" + lastEntry.getRemoteAddr(  ) + ", " +
                        lastEntry.getRequestTime(  ) + "]" );
                }
                else
                {
                    bDone = true;
                }
            }
        }
    }

    /**
     * Utility class used to store entries in the list.
     */
    private static class Entry
    {
        private String _strRemoteAddr;
        private long _lRequestTime;

        /**
         * Constructor
         * @param strRemoteAddr The remote address
         * @param lRequestTime The request time
         */
        public Entry( String strRemoteAddr, long lRequestTime )
        {
            this._strRemoteAddr = strRemoteAddr;
            this._lRequestTime = lRequestTime;
        }

        /**
         * Gets the remote address
         * @return The remote address
         */
        public String getRemoteAddr(  )
        {
            return _strRemoteAddr;
        }

        /**
         * Gets the request time
         * @return The request time
         */
        public long getRequestTime(  )
        {
            return _lRequestTime;
        }
    }
}
