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
package fr.paris.lutece.portal.service.event;

import fr.paris.lutece.portal.business.event.EventRessourceListener;
import fr.paris.lutece.portal.business.event.ResourceEvent;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * ResourceEventManager
 *
 */
public final class ResourceEventManager
{
    private static List<EventRessourceListener> _lstListeners = new ArrayList<EventRessourceListener>(  );

    /**
    * Private constructor - this class need not be instantiated
    */
    private ResourceEventManager(  )
    {
    }

    /**
     * Subscribe to this listener.
     *
     * @param listener the listener
     */
    public static void register( EventRessourceListener listener )
    {
        _lstListeners.add( listener );
        AppLogService.info( "New resource evnt listener registered : " + listener.getName(  ) );
    }

    /**
     * Warn subscribers that a resource has been created
     * @param event the event for the created resource
     */
    public static void fireAddedResource( ResourceEvent event )
    {
        for ( EventRessourceListener listener : _lstListeners )
        {
            listener.addedResource( event );
        }
    }

    /**
     * Warn subscribers that a resource has been updated
     * @param event the event for the updated resource
     */
    public static void fireUpdatedResource( ResourceEvent event )
    {
        for ( EventRessourceListener listener : _lstListeners )
        {
            listener.updatedResource( event );
        }
    }

    /**
     * Warn subscribers that a resource has been deleted
     * @param event the event for the deleted resource
     */
    public static void fireDeletedResource( ResourceEvent event )
    {
        for ( EventRessourceListener listener : _lstListeners )
        {
            listener.deletedResource( event );
        }
    }
}
