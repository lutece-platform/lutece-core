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
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.service.util.AppLogService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryListenersService
 * 
 * @deprecated Use CDI events and @Observes pattern instead. This class will be removed in future versions.
 * 
 */
@Deprecated(forRemoval = true, since = "8.0")
@ApplicationScoped
public class QueryListenersService
{
    private static List<QueryEventListener> _listListeners = new ArrayList<>( );

    QueryListenersService( )
    {
        // Ctor
    }

    /**
     * Returns the unique instance of the {@link QueryListenersService} service.
     * 
     * <p>This method is deprecated and is provided for backward compatibility only. 
     * For new code, use dependency injection with {@code @Inject} to obtain the 
     * {@link QueryListenersService} instance instead.</p>
     * 
     * @return The unique instance of {@link QueryListenersService}.
     * 
     * @deprecated Use {@code @Inject} to obtain the {@link QueryListenersService} 
     * instance. This method will be removed in future versions.
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public static QueryListenersService getInstance( )
    {
        return CDI.current( ).select( QueryListenersService.class ).get( );
    }

    /**
     * Register a new listener
     * 
     * @deprecated Use CDI events and @Observes pattern instead. This method will be removed in future versions.
     * 
     * @param listener
     *            The listener to register
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public void registerQueryListener( QueryEventListener listener )
    {
        _listListeners.add( listener );
        AppLogService.info( "New Query Event Listener registered : {}", listener.getClass( ).getName( ) );
    }

    /**
     * Notify all registered listeners
     * 
     * @deprecated Use CDI events to notify others. This method will be removed in future versions.
     * 
     * @param event
     *            The query event to notify
     */
    @Deprecated( since = "8.0", forRemoval = true )
    public void notifyListeners( QueryEvent event )
    {
        for ( QueryEventListener listener : _listListeners )
        {
            try
            {
                listener.processQueryEvent( event );
            }
            catch( Exception e )
            {
                // Catch any exception in order to stay in the loop to notify all listeners
                AppLogService.error( "Error while processing query event : {}", e.getMessage( ), e );
            }
        }
    }
}
