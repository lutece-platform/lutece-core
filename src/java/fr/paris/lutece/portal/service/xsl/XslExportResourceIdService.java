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
package fr.paris.lutece.portal.service.xsl;

import fr.paris.lutece.portal.business.xsl.XslExport;
import fr.paris.lutece.portal.business.xsl.XslExportHome;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * RBAC Resource Id Service to allow roles on AdminUser Services
 */
public class XslExportResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE = "CREATE_XSL_EXPORT";
    public static final String PERMISSION_DELETE = "DELETE_XSL_EXPORT";
    public static final String PERMISSION_MODIFY = "MODIFY_XSL_EXPORT";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "portal.xsl.permission.label.resource_type_xsl_export";
    private static final String PROPERTY_LABEL_CREATE = "portal.xsl.permission.label.create_xsl_export";
    private static final String PROPERTY_LABEL_DELETE = "portal.xsl.permission.label.delete_xsl_export";
    private static final String PROPERTY_LABEL_MODIFY = "portal.xsl.permission.label.modify_xsl_export";

    /**
     * Initializes the service
     */
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( XslExportResourceIdService.class.getName(  ) );
        rt.setResourceTypeKey( XslExport.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
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
        return null;
    }

    /**
     * Returns the Title of a given resource
     * @param strId The Id of the resource
     * @param locale The current locale
     * @return The Title of a given resource
     */
    public String getTitle( String strId, Locale locale )
    {
        int nIdExport;

        try
        {
            nIdExport = Integer.parseInt( strId );
        }
        catch ( Exception e )
        {
            return null;
        }

        XslExport export = XslExportHome.findByPrimaryKey( nIdExport );

        return ( export != null ) ? export.getTitle(  ) : null;
    }
}
