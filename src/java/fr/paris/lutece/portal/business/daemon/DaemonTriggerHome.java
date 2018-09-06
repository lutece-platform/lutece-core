/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.portal.business.daemon;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for DaemonTrigger objects
 */
public final class DaemonTriggerHome
{
    // Static variable pointed at the DAO instance
    private static IDaemonTriggerDAO _dao = SpringContextService.getBean( "daemonTriggerDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "daemontrigger" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DaemonTriggerHome(  )
    {
    }

    /**
     * Create an instance of the daemonTrigger class
     * @param daemonTrigger The instance of the DaemonTrigger which contains the informations to store
     * @return The  instance of daemonTrigger which has been created with its primary key.
     */
    public static DaemonTrigger create( DaemonTrigger daemonTrigger )
    {
        _dao.insert( daemonTrigger, _plugin );

        return daemonTrigger;
    }

    /**
     * Update of the daemonTrigger which is specified in parameter
     * @param daemonTrigger The instance of the DaemonTrigger which contains the data to store
     * @return The instance of the  daemonTrigger which has been updated
     */
    public static DaemonTrigger update( DaemonTrigger daemonTrigger )
    {
        _dao.store( daemonTrigger, _plugin );

        return daemonTrigger;
    }

    /**
     * Remove the daemonTrigger whose identifier is specified in parameter
     * @param nKey The daemonTrigger Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a daemonTrigger whose identifier is specified in parameter
     * @param nKey The daemonTrigger primary key
     * @return an instance of DaemonTrigger
     */
    public static DaemonTrigger findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the daemonTrigger objects and returns them as a list
     * @return the list which contains the data of all the daemonTrigger objects
     */
    public static List<DaemonTrigger> getDaemonTriggersList( )
    {
        return _dao.selectDaemonTriggersList( _plugin );
    }
}

