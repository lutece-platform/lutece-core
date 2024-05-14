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
package fr.paris.lutece.portal.service.event;

import fr.paris.lutece.plugins.workflowcore.business.event.EventAction;
import fr.paris.lutece.plugins.workflowcore.business.event.Type;
import fr.paris.lutece.portal.business.event.LuteceUserEvent;
import fr.paris.lutece.portal.business.event.ResourceEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class LegacyEventObserver
{

    /**
     * Fires the legacy LuteceEventManager#notifyListeners
     * 
     * @param event
     *            The Lutece User event
     */
    public void notifyUserEventManager( @Observes LuteceUserEvent event )
    {
        LuteceUserEventManager.getInstance( ).notifyListeners( event );
    }

    /**
     * Fires the legacy ResourceEventManager#fireAddedResource
     * 
     * @param event
     *            The Resource Event event
     */
    public void notifyAddedResourceEventManager( @Observes @Type(EventAction.CREATE) ResourceEvent event )
    {
        ResourceEventManager.fireAddedResource( event );
    }

    /**
     * Fires the legacy ResourceEventManager#fireUpdatedResource
     * 
     * @param event
     *            The Resource Event event
     */
    public void notifyUpdatedResourceEventManager( @Observes @Type(EventAction.UPDATE) ResourceEvent resourceEvent )
    {
        ResourceEventManager.fireUpdatedResource( resourceEvent );
    }

    /**
     * Fires the legacy ResourceEventManager#fireDeletedResource
     * 
     * @param event
     *            The Resource Event event
     */
    public void notifyDeletedResourceEventManager( @Observes @Type(EventAction.REMOVE) ResourceEvent resourceEvent )
    {
        ResourceEventManager.fireDeletedResource( resourceEvent );
    }
}
