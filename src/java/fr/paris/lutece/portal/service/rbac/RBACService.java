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
package fr.paris.lutece.portal.service.rbac;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.api.user.UserRole;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.rbac.RBACHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class provides the main methods to control the access to a resource depending on the user's roles
 */
public final class RBACService
{

    /**
     * ReferenceItem as an RBACResource
     */
    private static final class RBACReferenceItem implements RBACResource
    {
        private final String _strResourceType;
        private final ReferenceItem _item;

        /**
         * Constructor
         * 
         * @param strResourceType
         *            the resource type
         * @param item
         *            the reference item
         */
        public RBACReferenceItem( String strResourceType, ReferenceItem item )
        {
            _strResourceType = strResourceType;
            _item = item;
        }

        @Override
        public String getResourceTypeCode( )
        {
            return _strResourceType;
        }

        @Override
        public String getResourceId( )
        {
            return _item.getCode( );
        }

        /**
         * The reference item
         * 
         * @return the reference item
         */
        public ReferenceItem getItem( )
        {
            return _item;
        }

    }

    /**
     * Constructor
     */
    private RBACService( )
    {
    }

    /**
     * Check that a given user is allowed to access a resource for a given permission
     * 
     * @param strResourceTypeCode
     *            the key of the resource type being considered
     * @param strResourceId
     *            the id of the resource being considered
     * @param strPermission
     *            the permission needed
     * @param user
     *            the user trying to access the ressource
     * @return true if the user can access the given resource with the given permission, false otherwise
     */
    public static boolean isAuthorized( String strResourceTypeCode, String strResourceId, String strPermission, User user )
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
     * 
     * @param strResourceTypeCode
     *            the key of the resource type being considered
     * @param strResourceId
     *            the id of the resource being considered
     * @param strPermission
     *            the permission needed
     * @param user
     *            the user trying to access the ressource
     * @return true if the user can access the given resource with the given permission, false otherwise
     * @deprecated use isAuthorized( String, String, String, User )
     */
    @Deprecated
    public static boolean isAuthorized( String strResourceTypeCode, String strResourceId, String strPermission, AdminUser user )
    {
        return isAuthorized( strResourceTypeCode, strResourceId, strPermission, (User) user );
    }

    /**
     * Check that a given user is allowed to access a resource for a given permission
     * 
     * @param resource
     *            the resource object being considered
     * @param strPermission
     *            the permission needed
     * @param user
     *            the user trying to access the ressource
     * @return true if the user can access the given resource with the given permission, false otherwise
     */
    public static boolean isAuthorized( RBACResource resource, String strPermission, User user )
    {
        boolean bAuthorized = false;

        if ( resource != null )
        {
            bAuthorized = isAuthorized( resource.getResourceTypeCode( ), resource.getResourceId( ), strPermission, user );
        }

        return bAuthorized;
    }

    /**
     * Check that a given user is allowed to access a resource for a given permission
     * 
     * @param resource
     *            the resource object being considered
     * @param strPermission
     *            the permission needed
     * @param user
     *            the user trying to access the ressource
     * @return true if the user can access the given resource with the given permission, false otherwise
     * @deprecated use isAuthorized( RBACResource, String, User )
     */
    @Deprecated
    public static boolean isAuthorized( RBACResource resource, String strPermission, AdminUser user )
    {
        return isAuthorized( resource, strPermission, (User) user );
    }

    /**
     * Check that a given user is in the given role
     * 
     * @param user
     *            The user
     * @param strRole
     *            The role
     * @return true if the user has the given role, false otherwise
     */
    public static boolean isUserInRole( User user, String strRole )
    {
        Map<String, UserRole> userRoles = user.getUserRoles( );

        return userRoles.containsKey( strRole );
    }

    /**
     * Check that a given user is in the given role
     * 
     * @param user
     *            The user
     * @param strRole
     *            The role
     * @return true if the user has the given role, false otherwise
     * @deprecated use isUserInRole( User, String )
     */
    @Deprecated
    public static boolean isUserInRole( AdminUser user, String strRole )
    {
        return isUserInRole( (User) user, strRole );
    }

    /**
     * Filter a collection of resources for a given user
     * 
     * @param <E>
     *            The RBAC resource
     * @param collection
     *            The collection to filter
     * @param strPermission
     *            Permission to check
     * @param user
     *            The user
     * @return A filtered collection of resources
     */
    public static <E extends RBACResource> Collection<E> getAuthorizedCollection( Collection<E> collection, String strPermission, User user )
    {
        if ( user == null )
        {
            return Collections.emptyList( );
        }
        Map<String, Collection<RBAC>> rbacsByResourceType = new HashMap<>( );
        RBACHome.findByPermissionAndRoles( strPermission, user.getUserRoles( ).keySet( ) ).stream( ).forEach( rbac -> {
            rbacsByResourceType.computeIfAbsent( rbac.getResourceTypeKey( ), t -> new ArrayList<>( ) ).add( rbac );
        } );
        return collection.stream( )
                .filter( resource -> rbacsByResourceType
                        .getOrDefault( resource.getResourceTypeCode( ), Collections.emptyList( ) ).stream( )
                        .anyMatch( rbac -> RBAC.WILDCARD_RESOURCES_ID.equals( rbac.getResourceId( ) )
                                || resource.getResourceId( ).equals( rbac.getResourceId( ) ) ) )
                .collect( Collectors.toList( ) );
    }

    /**
     * Filter a collection of resources for a given user
     * 
     * @param <E>
     *            The RBAC resource
     * @param collection
     *            The collection to filter
     * @param strPermission
     *            Permission to check
     * @param user
     *            The user
     * @return A filtered collection of resources
     * @deprecated use getAuthorizedCollection( Collection, String, User )
     */
    @Deprecated
    public static <E extends RBACResource> Collection<E> getAuthorizedCollection( Collection<E> collection, String strPermission, AdminUser user )
    {
        return getAuthorizedCollection( collection, strPermission, (User) user );
    }

    /**
     * Filter a Reference List for a given user
     * 
     * @param listResources
     *            The list to filter
     * @param strResourceType
     *            The resource type
     * @param strPermission
     *            The permission to check
     * @param user
     *            The user
     * @return The filtered collection
     */
    public static ReferenceList getAuthorizedReferenceList( ReferenceList listResources, String strResourceType, String strPermission, User user )
    {
        return getAuthorizedCollection( listResources.stream( )
                .map( item -> new RBACReferenceItem( strResourceType, item ) ).collect( Collectors.toList( ) ),
                strPermission, user ).stream( ).map( RBACReferenceItem::getItem )
                        .collect( Collectors.toCollection( ReferenceList::new ) );
    }

    /**
     * Filter a Reference List for a given user
     * 
     * @param listResources
     *            The list to filter
     * @param strResourceType
     *            The resource type
     * @param strPermission
     *            The permission to check
     * @param user
     *            The user
     * @return The filtered collection
     * @deprecated use getAuthorizedReferenceList( ReferenceList, String, String, User )
     */
    @Deprecated
    public static ReferenceList getAuthorizedReferenceList( ReferenceList listResources, String strResourceType, String strPermission, AdminUser user )
    {
        return getAuthorizedReferenceList( listResources, strResourceType, strPermission, (User) user );
    }

    /**
     * Filter a collection of RBACAction for a given user
     * 
     * @param <E>
     *            The RBAC resource
     * @param collection
     *            The collection to filter
     * @param resource
     *            The resource
     * @param user
     *            The user
     * @return The filtered collection
     */
    public static <E extends RBACAction> Collection<E> getAuthorizedActionsCollection( Collection<E> collection, RBACResource resource, User user )
    {
        if ( collection.isEmpty( ) )
        {
            return collection;
        }
        Set<String> permissions = RBACHome
                .findByPermissionsAndRoles(
                        collection.stream( ).map( RBACAction::getPermission ).collect( Collectors.toSet( ) ),
                        user.getUserRoles( ).keySet( ) )
                .stream( ).filter( rbac -> resource.getResourceTypeCode( ).equals( rbac.getResourceTypeKey( ) ) )
                .filter( rbac -> RBAC.WILDCARD_RESOURCES_ID.equals( rbac.getResourceId( ) )
                        || resource.getResourceId( ).equals( rbac.getResourceId( ) ) )
                .map( RBAC::getPermissionKey ).collect( Collectors.toSet( ) );
        return collection.stream( ).filter( action -> permissions.contains( action.getPermission( ) )
                || permissions.contains( RBAC.WILDCARD_PERMISSIONS_KEY ) ).collect( Collectors.toList( ) );
    }

    /**
     * Filter a collection of RBACAction for a given user
     * 
     * @param <E>
     *            The RBAC resource
     * @param collection
     *            The collection to filter
     * @param resource
     *            The resource
     * @param user
     *            The user
     * @return The filtered collection
     * @deprecated use getAuthorizedActionsCollection( Collection, RBACResource, User )
     */
    @Deprecated
    public static <E extends RBACAction> Collection<E> getAuthorizedActionsCollection( Collection<E> collection, RBACResource resource, AdminUser user )
    {
        return getAuthorizedActionsCollection( collection, resource, (User) user );
    }

}
