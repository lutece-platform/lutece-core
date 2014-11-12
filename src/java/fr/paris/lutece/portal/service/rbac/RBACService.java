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

import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * This class provides the main methods to control the access to a resource depending on the user's roles
 */
public final class RBACService
{
    /**
     * Constructor
     */
    private RBACService(  )
    {
    }

    /**
     * Check that a given user is allowed to access a resource for a given permission
     * @param strResourceTypeCode the key of the resource type being considered
     * @param strResourceId the id of the resource being considered
     * @param strPermission the permission needed
     * @param user the user trying to access the ressource
     * @return true if the user can access the given resource with the given permission, false otherwise
     */
    public static boolean isAuthorized( String strResourceTypeCode, String strResourceId, String strPermission,
        AdminUser user )
    {
        // Check user roles
        Collection<String> colRoles = RBACHome.findRoleKeys( strResourceTypeCode, strResourceId, strPermission );

        for ( String strRole : colRoles )
        {
            if ( isUserInRole( user, strRole ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check that a given user is allowed to access a resource for a given permission
     * @param resource the resource object being considered
     * @param strPermission the permission needed
     * @param user the user trying to access the ressource
     * @return true if the user can access the given resource with the given permission, false otherwise
     */
    public static boolean isAuthorized( RBACResource resource, String strPermission, AdminUser user )
    {
        boolean bAuthorized = false;

        if ( resource != null )
        {
            bAuthorized = isAuthorized( resource.getResourceTypeCode(  ), resource.getResourceId(  ), strPermission,
                    user );
        }

        return bAuthorized;
    }

    /**
     * Check that a given user is in the given role
     * @param user The user
     * @param strRole The role
     * @return true if the user has the given role, false otherwise
     */
    public static boolean isUserInRole( AdminUser user, String strRole )
    {
        Map<String, AdminRole> userRoles = user.getRoles(  );

        if ( userRoles.containsKey( strRole ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Filter a collection of resources for a given user
     * @param <E> The RBAC resource
     * @param collection The collection to filter
     * @param strPermission Permission to check
     * @param user The user
     * @return A filtered collection of resources
     */
    public static <E extends RBACResource> Collection<E> getAuthorizedCollection( Collection<E> collection,
        String strPermission, AdminUser user )
    {
        Collection<E> list = new ArrayList<E>(  );

        for ( E resource : collection )
        {
            if ( isAuthorized( resource, strPermission, user ) )
            {
                list.add( resource );
            }
        }

        return list;
    }

    /**
     * Filter a Reference List for a given user
     * @param listResources The list to filter
     * @param strResourceType The resource type
     * @param strPermission  The permission to check
     * @param user The user
     * @return The filtered collection
     */
    public static ReferenceList getAuthorizedReferenceList( ReferenceList listResources, String strResourceType,
        String strPermission, AdminUser user )
    {
        ReferenceList list = new ReferenceList(  );

        for ( ReferenceItem item : listResources )
        {
            if ( isAuthorized( strResourceType, item.getCode(  ), strPermission, user ) )
            {
                list.addItem( item.getCode(  ), item.getName(  ) );
            }
        }

        return list;
    }

    /**
     * Filter a collection of RBACAction for a given user
     * @param <E> The RBAC resource
     * @param collection The collection to filter
     * @param resource The resource
     * @param user The user
     * @return The filtered collection
     */
    public static <E extends RBACAction> Collection<E> getAuthorizedActionsCollection( Collection<E> collection,
        RBACResource resource, AdminUser user )
    {
        Collection<E> list = new ArrayList<E>(  );

        for ( E action : collection )
        {
            if ( isAuthorized( resource, action.getPermission(  ), user ) )
            {
                list.add( action );
            }
        }

        return list;
    }
}
