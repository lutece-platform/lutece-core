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
package fr.paris.lutece.portal.service.portlet;

import fr.paris.lutece.portal.business.portlet.PortletTemplate;
import fr.paris.lutece.portal.business.portlet.PortletTemplateHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * RBAC resource type of the portlet templates : the FreeMarker templates the portlets can be rendered with
 */
public class PortletTemplateResourceIdService extends ResourceIdService
{
    /** Permission to create a template */
    public static final String PERMISSION_CREATE = "CREATE";
    /** Permission to modify a template */
    public static final String PERMISSION_MODIFY = "MODIFY";
    /** Permission to delete a template */
    public static final String PERMISSION_DELETE = "DELETE";

    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "portal.style.rbac.portletTemplate.resourceType";
    private static final String PROPERTY_LABEL_CREATE = "portal.style.rbac.portletTemplate.permission.create";
    private static final String PROPERTY_LABEL_MODIFY = "portal.style.rbac.portletTemplate.permission.modify";
    private static final String PROPERTY_LABEL_DELETE = "portal.style.rbac.portletTemplate.permission.delete";

    /**
     * {@inheritDoc }
     */
    @Override
    public void register( )
    {
        ResourceType rt = new ResourceType( );
        rt.setResourceIdServiceClass( PortletTemplateResourceIdService.class.getName( ) );
        rt.setResourceTypeKey( PortletTemplate.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission( );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        p = new Permission( );
        p.setPermissionKey( PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        return PortletTemplateHome.findReferenceList( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        if ( !StringUtils.isNumeric( strId ) )
        {
            return null;
        }

        PortletTemplate template = PortletTemplateHome.findByPrimaryKey( Integer.parseInt( strId ) );

        return ( template != null ) ? template.getDescription( ) : null;
    }
}
