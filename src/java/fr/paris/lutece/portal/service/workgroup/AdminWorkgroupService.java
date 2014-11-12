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
package fr.paris.lutece.portal.service.workgroup;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;


/**
 * AdminWorkgroupService
 */
public final class AdminWorkgroupService
{
    public static final String ALL_GROUPS = "all";
    private static final String PROPERTY_LABEL_ALL_GROUPS = "portal.workgroup.labelAllGroups";

    /**
     * Private constructor
     */
    private AdminWorkgroupService(  )
    {
    }

    /**
     * Check if a resource should be visible to the user according its workgroup
     * @param resource The resource to check
     * @param user The current user
     * @return true if authorized, otherwise false
     */
    public static boolean isAuthorized( AdminWorkgroupResource resource, AdminUser user )
    {
        String strWorkgroup = normalizeWorkgroupKey( resource.getWorkgroup(  ) );

        if ( !strWorkgroup.equals( ALL_GROUPS ) )
        {
            return AdminWorkgroupHome.isUserInWorkgroup( user, strWorkgroup );
        }

        return true;
    }

    /**
     * Filter a collection of resources for a given user
     *
     * @return A filtered collection of resources
     * @param <E> The workgroup resource
     * @param collection The collection to filter
     * @param user The user
     */
    public static <E extends AdminWorkgroupResource> Collection<E> getAuthorizedCollection( Collection<E> collection,
        AdminUser user )
    {
        ArrayList<E> list = new ArrayList<E>(  );

        for ( E resource : collection )
        {
            if ( isAuthorized( resource, user ) )
            {
                list.add( resource );
            }
        }

        return list;
    }

    /**
     * Gets all workgroups of the user
     * @param user The current user
     * @param locale The Locale
     * @return A list of workgroup
     */
    public static ReferenceList getUserWorkgroups( AdminUser user, Locale locale )
    {
        ReferenceList list = AdminWorkgroupHome.getUserWorkgroups( user );
        list.addItem( ALL_GROUPS, I18nService.getLocalizedString( PROPERTY_LABEL_ALL_GROUPS, locale ) );

        return list;
    }

    /**
     * Normalized an empty or null workgroup key
     * @param strAdminWorkgroup The workgroup key to normalize
     * @return  The workgroup key normalized
     */
    public static String normalizeWorkgroupKey( String strAdminWorkgroup )
    {
        String strNormalized = strAdminWorkgroup;

        if ( ( strAdminWorkgroup == null ) || ( strAdminWorkgroup.equals( "" ) ) )
        {
            strNormalized = AdminWorkgroupService.ALL_GROUPS;
        }

        return strNormalized;
    }
}
