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
package fr.paris.lutece.portal.service.jpa;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.jpa.JPAGenericDAO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;


/**
 * Generic JPA DAO for Lutece components
 * @param <K> Type of the entity's key
 * @param <E> Type of the entity
 */
public abstract class JPALuteceDAO<K, E> extends JPAGenericDAO<K, E>
{
    private static final String BEAN_ENTITY_MANAGER_SERVICE = "entityManagerService";
    private static final String DEFAULT_PERSISTENCE_UNIT = "portal";

    /**
     * Gets the plugin name.
     * Override this method to define the plugin associated to this DAO
     * @return The plugin name
     */
    public abstract String getPluginName(  );

    /**
     * Gets the entity manager to use with this DAO
     * @return The appropriate entity manager delivered by the EntityManagerService
     */
    public EntityManagerFactory getEntityManagerFactory(  )
    {
        String strPersistenceUnit = DEFAULT_PERSISTENCE_UNIT;
        Plugin plugin = PluginService.getPlugin( getPluginName(  ) );

        if ( plugin != null )
        {
            strPersistenceUnit = plugin.getDbPoolName(  );
        }

        EntityManagerService ems = (EntityManagerService) SpringContextService.getBean( BEAN_ENTITY_MANAGER_SERVICE );

        return ems.getEntityManagerFactory( strPersistenceUnit );
    }

    /**
     *
     * @param strSQL The SQL query
     * @return query
     */
    protected Query createNativeQuery( String strSQL )
    {
        return getEM(  ).createNativeQuery( strSQL );
    }
}
