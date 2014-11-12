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
package fr.paris.lutece.portal.service.portlet;

import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * Resource Id service for RBAC features to control access to portlet
 */
public class PortletResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_MANAGE = "MANAGE";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "portal.site.portlet.resourceType";
    private static final String PROPERTY_LABEL_CREATE = "portal.site.portlet.permission.create";
    private static final String PROPERTY_LABEL_MANAGE = "portal.site.portlet.permission.manage";

    /**
     * Initializes the service
     */
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( PortletResourceIdService.class.getName(  ) );
        rt.setResourceTypeKey( PortletType.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MANAGE );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Returns a list of resource ids
     * @param locale The current locale
     * @return A list of resource ids
     */
    public ReferenceList getResourceIdList( Locale locale )
    {
        return PortletTypeHome.getPortletsTypesList( locale );
    }

    /**
     * Returns the Title of a given resource
     * @param strPortletTypeId The Id of the resource
     * @param locale The current locale
     * @return The Title of a given resource
     */
    public String getTitle( String strPortletTypeId, Locale locale )
    {
        PortletType portletType = PortletHome.getPortletType( strPortletTypeId );
        portletType.setLocale( locale );

        return portletType.getName(  );
    }
}
