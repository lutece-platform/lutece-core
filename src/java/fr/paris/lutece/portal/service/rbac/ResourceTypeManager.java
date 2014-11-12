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
package fr.paris.lutece.portal.service.rbac;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Manager of the ResourceType. Responsible for (un)registering them.
 */
public final class ResourceTypeManager
{
    /** resource type registry */
    private static Map<String, ResourceType> _mapResourceTypes = new HashMap<String, ResourceType>(  );

    ////////////////////////////////////////////////////////////////////////////
    // Methods

    /**
     * Creates a new ResourceTypeManager object
     */
    private ResourceTypeManager(  )
    {
    }

    /**
     * Registers a new resource type
     *
     * @param rt the resource type to register
     */
    public static void registerResourceType( ResourceType rt )
    {
        _mapResourceTypes.put( rt.getResourceTypeKey(  ), rt );
        AppLogService.info( "New RBAC resource type registered : " + rt.getResourceTypeKey(  ) );
    }

    /**
     * Returns all registered resource types
     *
     * @return A collection containing all registered resource types
     */
    public static Collection<ResourceType> getResourceTypeList(  )
    {
        return _mapResourceTypes.values(  );
    }

    /**
     * Get a particular resource type
     *
     * @param strId Identifier of the seeked resource type
     * @return the selected resource type
     */
    public static ResourceType getResourceType( String strId )
    {
        return _mapResourceTypes.get( strId );
    }

    /**
     * Registers a collection of resource type
     *
     * @param resourceTypeList The resource type list
     */
    public static void registerList( Collection<ResourceType> resourceTypeList )
    {
        registerList( resourceTypeList, null );
    }

    /**
     * Registers a collection of resource type
     *
     * @param listResourcesType  The resource type list
     * @param strPluginName The plugin name
     */
    public static void registerList( Collection<ResourceType> listResourcesType, String strPluginName )
    {
        for ( ResourceType resourceType : listResourcesType )
        {
            resourceType.setPluginName( strPluginName );
            registerResourceType( resourceType );
        }
    }

    /**
     * Gets a localized list of permission for a given resource type
     * @param strResourceType The resource type
     * @param locale The Locale
     * @return A list of permission
     */
    public static ReferenceList getPermissionsList( String strResourceType, Locale locale )
    {
        ReferenceList list = new ReferenceList(  );
        ResourceType resourceType = getResourceType( strResourceType );
        Collection<Permission> listPermissions = resourceType.getPermissionList(  );
        I18nService.localizeCollection( listPermissions, locale );

        for ( Permission permission : listPermissions )
        {
            list.addItem( permission.getPermissionKey(  ), permission.getPermissionTitle(  ) );
        }

        return list;
    }
}
