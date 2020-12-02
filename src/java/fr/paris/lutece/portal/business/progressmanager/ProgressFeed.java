/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.portal.business.progressmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the business class for the object ProgressFeed
 */
public class ProgressFeed implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private String _strId;
    private String _strToken;

    private int _nNbItemTotal = 0;

    private int _nNbItemSuccess = 0;

    private int _nNbItemFailure = 0;

    private List<String> _report = new ArrayList<>( );

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public String getId( )
    {
        return _strId;
    }

    /**
     * Sets the Id
     * 
     * @param strId
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the Token
     * 
     * @return The Token
     */
    public String getToken( )
    {
        return _strToken;
    }

    /**
     * Sets the Token
     * 
     * @param strToken
     */
    public void setToken( String strToken )
    {
        _strToken = strToken;
    }

    /**
     * Returns the NbItemTotal
     * 
     * @return The NbItemTotal
     */
    public int getNbItemTotal( )
    {
        return _nNbItemTotal;
    }

    /**
     * Sets the NbItemTotal
     * 
     * @param nNbItemTotal
     *            The NbItemTotal
     */
    public void setNbItemTotal( int nNbItemTotal )
    {
        _nNbItemTotal = nNbItemTotal;
    }

    /**
     * Returns the NbItemSuccess
     * 
     * @return The NbItemSuccess
     */
    public int getNbItemSuccess( )
    {
        return _nNbItemSuccess;
    }

    /**
     * Increments the NbItemSuccess
     * 
     * @param nNbItemSuccess
     *            The NbItemSuccess
     */
    public void addSuccessItems( int nNbItemSuccess )
    {
        _nNbItemSuccess += nNbItemSuccess;
    }

    /**
     * Returns the NbItemFailure
     * 
     * @return The NbItemFailure
     */
    public int getNbItemFailure( )
    {
        return _nNbItemFailure;
    }

    /**
     * Increments the NbItemFailure
     * 
     * @param nNbItemFailure
     *            The NbItemFailure
     */
    public void addFailureItems( int nNbItemFailure )
    {
        _nNbItemFailure += nNbItemFailure;
    }

    /**
     * Returns the Report
     * 
     * @return The Report
     */
    public List<String> getReportList( )
    {
        return _report;
    }

    /**
     * Returns the last Report
     * 
     * @return The Report
     */
    public String getLastReport( )
    {
        return ( _report != null && _report.size( ) > 0 ) ? _report.get( _report.size( ) - 1 ) : null;
    }

    /**
     * Add line to the Report
     * 
     * @param strReport
     */
    public void addReport( String strReport )
    {
        _report.add( strReport );
    }

    /**
     * Add lines to the Report
     * 
     * @param strReportList
     */
    public void addReport( List<String> strReportList )
    {
        _report.addAll( strReportList );
    }
}
