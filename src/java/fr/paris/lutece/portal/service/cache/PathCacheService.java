/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.page.PageEvent;
import fr.paris.lutece.portal.service.page.PageEventListener;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.web.constants.Parameters;

/**
 * XPage path cache service
 */
public class PathCacheService extends AbstractCacheableService implements IPathCacheService, PageEventListener
{

    /**
     * Constructor
     */
    PathCacheService( )
    {
        initCache( );
        PageService.addPageEventListener( this );
    }

    @Override
    public String getName( )
    {
        return IPathCacheService.BEAN_NAME;
    }

    @Override
    public String getKey( String strXPageName, int nMode, HttpServletRequest request )
    {
        return getKey( strXPageName, nMode, null, request );
    }

    @Override
    public String getKey( String strXPageName, int nMode, String strTitlesUrls, HttpServletRequest request )
    {
        if ( !isCacheEnable( ) )
        {
            return null;
        }
        StringBuilder builder = new StringBuilder( );
        builder.append( "[XPageName:" ).append( strXPageName ).append( ']' );
        builder.append( "[mode:" ).append( nMode ).append( ']' );
        if ( strTitlesUrls != null )
        {
            builder.append( "[titleUrls:" ).append( strTitlesUrls ).append( ']' );
        }
        if ( request != null )
        {
            builder.append( "[locale:" ).append( request.getLocale( ) ).append( ']' );
            String strPageId = request.getParameter( Parameters.PAGE_ID );
            if ( StringUtils.isNotBlank( strPageId ) )
            {
                builder.append( '[' ).append( Parameters.PAGE_ID ).append( ':' ).append( strPageId ).append( ']' );
            }
            String strPortletId = request.getParameter( Parameters.PORTLET_ID );
            if ( StringUtils.isNotBlank( strPortletId ) )
            {
                builder.append( '[' ).append( Parameters.PORTLET_ID ).append( ':' ).append( strPortletId ).append( ']' );
            }
        }
        return builder.toString( );
    }

    @Override
    public String getFromCache( String strKey )
    {
        return (String) super.getFromCache( strKey );
    }

    @Override
    public void putInCache( String strKey, String path )
    {
        super.putInCache( strKey, path );
    }

    @Override
    public void processPageEvent( PageEvent event )
    {
        if ( isCacheEnable( ) && event.getEventType( ) != PageEvent.PAGE_CREATED )
        {
            // some cached paths might contain page info that need invalidation, but not for
            // a page which was just created
            resetCache( );
        }
    }

}
