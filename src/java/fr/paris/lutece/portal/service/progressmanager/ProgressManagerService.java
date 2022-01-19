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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.portal.service.progressmanager;

import fr.paris.lutece.portal.business.progressmanager.ProgressFeed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author sleridon
 */
public final class ProgressManagerService
{

    private static ProgressManagerService _singleton;
    private static Map<String, ProgressFeed> _progressFeeds;

    /**
     * Private constructor
     */
    private ProgressManagerService( )
    {
    }

    /**
     * Returns the unique instance of the service
     * 
     * @return The instance of the service
     */
    public static synchronized ProgressManagerService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new ProgressManagerService( );
            _progressFeeds = new HashMap<>( );
        }
        return _singleton;
    }

    /**
     * ge progress feeds
     * 
     * @return the progress feeds
     */
    public Map<String, ProgressFeed> getProgressFeeds( )
    {
        Map<String, ProgressFeed> feedMap = new HashMap<>( );
        feedMap.putAll( _progressFeeds );

        return feedMap;
    }

    /**
     * register new progress feed returns a generated token to identify the feed for rest webservice
     * 
     * @param strProgressFeedName
     * @param nTotalItems
     * @return the generated token
     */
    public String registerFeed( String strProgressFeedName, int nTotalItems )
    {
        ProgressFeed feed = new ProgressFeed( );
        feed.setId( strProgressFeedName );
        feed.setNbItemTotal( nTotalItems );

        String strToken = UUID.randomUUID( ).toString( );
        feed.setToken( strToken );

        _progressFeeds.put( strToken, feed );

        return feed.getToken( );
    }

    /**
     * unregister progress feed
     * 
     * @param strFeedToken
     */
    public void unRegisterFeed( String strFeedToken )
    {
        _progressFeeds.remove( strFeedToken );
    }

    /**
     * register new progress feed
     * 
     * @param strFeedToken
     * @return
     */
    public boolean isRegistred( String strFeedToken )
    {
        return ( _progressFeeds.get( strFeedToken ) != null );
    }

    /**
     * increment nb of success items
     * 
     * @param strFeedToken
     * @param nSuccessItems
     */
    public synchronized void incrementSuccess( String strFeedToken, int nSuccessItems )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            _progressFeeds.get( strFeedToken ).addSuccessItems( nSuccessItems );
        }
    }

    /**
     * increment nb of success items
     * 
     * @param strFeedToken
     * @param nFailureItems
     */
    public synchronized void incrementFailure( String strFeedToken, int nFailureItems )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            _progressFeeds.get( strFeedToken ).addFailureItems( nFailureItems );
        }
    }

    /**
     * add report line
     * 
     * @param strFeedToken
     * @param strReportLine
     */
    public void addReport( String strFeedToken, String strReportLine )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            _progressFeeds.get( strFeedToken ).addReport( strReportLine );
        }
    }

    /**
     * get the progress status in pourcent
     * 
     * @param strFeedToken
     * @return
     */
    public int getProgressStatus( String strFeedToken )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            ProgressFeed feed = _progressFeeds.get( strFeedToken );
            if ( feed.getNbItemTotal( ) > 0 )
            {
                if ( feed.getNbItemTotal( ) <= feed.getNbItemSuccess( ) + feed.getNbItemFailure( ) )
                {
                    return 100;
                }
                else
                {
                    return (int) ( ( feed.getNbItemSuccess( ) + feed.getNbItemFailure( ) ) * 100.0 / feed.getNbItemTotal( ) + 0.5 );
                }
            }
        }

        return -1;
    }

    /**
     * get the success nb
     * 
     * @param strFeedToken
     * @return the success nb
     */
    public int getSuccessNb( String strFeedToken )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            return _progressFeeds.get( strFeedToken ).getNbItemSuccess( );
        }

        return -1;
    }

    /**
     * get the failure nb
     * 
     * @param strFeedToken
     * @return the failure nb
     */
    public int getFailureNb( String strFeedToken )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            return _progressFeeds.get( strFeedToken ).getNbItemFailure( );
        }

        return -1;
    }

    /**
     * get the report list
     * 
     * @param strFeedToken
     * @return the failure nb
     */
    public List<String> getReport( String strFeedToken )
    {
        return getReport( strFeedToken, -1 );
    }

    /**
     * get the report list
     * 
     * @param strFeedToken
     * @param iFromLine
     * @return the failure nb
     */
    public List<String> getReport( String strFeedToken, int iFromLine )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            List<String> reportList = _progressFeeds.get( strFeedToken ).getReportList( );

            if ( iFromLine > 0 && iFromLine < reportList.size( ) )
            {
                // clone to avoid ConcurrentModificationException
                ArrayList<String> clonedReport = (ArrayList<String>) ( (ArrayList<String>) reportList ).clone( );
                return clonedReport.subList( iFromLine - 1, clonedReport.size( ) - 1 );
            }
            else
                if ( iFromLine >= reportList.size( ) )
                {
                    return new ArrayList<String>( );
                }
                else
                {
                    return reportList;
                }
        }

        return null;
    }

    /**
     * update feed total item number
     *
     * @param strFeedToken
     * @param nTotalItems
     * @return true if the feed exists
     */
    public boolean initFeed( String strFeedToken, int nTotalItems )
    {
        if ( _progressFeeds.get( strFeedToken ) != null )
        {
            _progressFeeds.get( strFeedToken ).setNbItemTotal( nTotalItems );
            return true;
        }
        else
        {
            return false;
        }
    }
}
