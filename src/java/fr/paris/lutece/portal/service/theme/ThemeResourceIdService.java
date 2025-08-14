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
package fr.paris.lutece.portal.service.theme;

import fr.paris.lutece.portal.business.style.Theme;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;

/**
 *
 * ThemeResourceIdService
 *
 */
public class ThemeResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATE_THEME = "CREATE_THEME";
    public static final String PERMISSION_MODIFY_THEME = "MODIFY_THEME";
    public static final String PERMISSION_DELETE_THEME = "DELETE_THEME";
    public static final String PERMISSION_MODIFY_GLOBAL_THEME = "MODIFY_GLOBAL_THEME";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "theme.permission.label.resource_type_theme";
    private static final String PROPERTY_LABEL_CREATE_THEME = "theme.permission.label.create_theme";
    private static final String PROPERTY_LABEL_MODIFY_THEME = "theme.permission.label.modify_theme";
    private static final String PROPERTY_LABEL_DELETE_THEME = "theme.permission.label.delete_theme";
    private static final String PROPERTY_LABEL_MODIFY_GLOBAL_THEME = "theme.permission.label.modify_global_theme";

    /**
     * Create a new instance of ThemeResourceIdService
     */
    public ThemeResourceIdService(  )
    {
        
    }

    /**
    * Initializes the service
    */
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( ThemeResourceIdService.class.getName(  ) );
        rt.setResourceTypeKey( Theme.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE_THEME );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE_THEME );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY_THEME );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY_THEME );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DELETE_THEME );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE_THEME );
        rt.registerPermission( p );
        
        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY_GLOBAL_THEME );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY_GLOBAL_THEME );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
    * Returns a list of theme resource ids
    * @param locale The current locale
    * @return A list of resource ids
    */
    public ReferenceList getResourceIdList( Locale locale )
    {
        return ThemeService.getInstance(  ).getThemes(  );
    }

    /**
    * Returns the Title of a given resource
    * @param strTheme the theme key
    * @param locale The current locale
    * @return The Title of a given resource
    */
    public String getTitle( String strTheme, Locale locale )
    {
        Theme theme = ThemeService.getInstance(  ).getTheme( strTheme );

        return ( theme != null ) ? theme.getCodeTheme(  ) : "";
    }
}
