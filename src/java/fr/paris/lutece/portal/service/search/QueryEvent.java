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
package fr.paris.lutece.portal.service.search;

import javax.servlet.http.HttpServletRequest;


/**
 * QueryEvent
 */
public class QueryEvent
{
    // Variables declarations
    private String _strQuery;
    private int _nResultsCount;
    private HttpServletRequest _request;

    /**
     * Returns the Query
     *
     * @return The Query
     */
    public String getQuery(  )
    {
        return _strQuery;
    }

    /**
     * Sets the Query
     *
     * @param strQuery The Query
     */
    public void setQuery( String strQuery )
    {
        _strQuery = strQuery;
    }

    /**
     * Returns the ResultsCount
     *
     * @return The ResultsCount
     */
    public int getResultsCount(  )
    {
        return _nResultsCount;
    }

    /**
     * Sets the ResultsCount
     *
     * @param nResultsCount The ResultsCount
     */
    public void setResultsCount( int nResultsCount )
    {
        _nResultsCount = nResultsCount;
    }

    /**
     * Returns the request
     *
     * @return The request
     */
    public HttpServletRequest getRequest(  )
    {
        return _request;
    }

    /**
     * Sets the request
     *
     * @param request The request
     */
    public void setRequest( HttpServletRequest request )
    {
        _request = request;
    }
}
