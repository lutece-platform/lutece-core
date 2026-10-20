/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.portal.service.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.portal.business.right.FeatureGroup;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.prefs.AdminUserPreferencesService;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * The shortcuts of the Back Office home page: the buttons of its header, each one a menu entry. An administrator sets the default list,
 * each user may replace it with a list of their own. A list is the ids of the rights in their order, separated by commas.
 */
@ApplicationScoped
public class HomeShortcutService
{
    /** The highest number of shortcuts, so that the buttons hold on one line */
    public static final int MAX_SHORTCUTS = 8;

    private static final String PREFERENCE_KEY = "core.home.shortcuts";
    private static final String DATASTORE_KEY_DEFAULT = "core.home.shortcuts.default";
    private static final String DEFAULT_SHORTCUTS = "CORE_ADMIN_SITE,CORE_USERS_MANAGEMENT";
    private static final String SEPARATOR = ",";
    /** The site property that lets the users choose their own shortcuts; without it, everyone has the default ones */
    public static final String DATASTORE_KEY_USER_SHORTCUTS_ENABLED = "portal.site.site_property.bo.shortcuts.checkbox";

    /**
     * Returns the shortcuts of a user: their own list, or the default one, kept to the entries of their menu
     *
     * @param user
     *            the user
     * @param listFeatureGroups
     *            the menu of the user: its groups and their entries, the rights the user has and that have a URL
     * @return the rights of the shortcuts, in their order
     */
    public List<Right> getShortcuts( AdminUser user, List<FeatureGroup> listFeatureGroups )
    {
        Map<String, Right> mapFeatures = getFeatures( listFeatureGroups );
        String strShortcuts = isUserShortcutsEnabled( ) ? AdminUserPreferencesService.instance( ).get( getUserId( user ), PREFERENCE_KEY, null ) : null;

        if ( strShortcuts == null )
        {
            strShortcuts = DatastoreService.getDataValue( DATASTORE_KEY_DEFAULT, DEFAULT_SHORTCUTS );
        }

        List<Right> listShortcuts = new ArrayList<>( );

        for ( String strId : split( strShortcuts ) )
        {
            Right right = mapFeatures.get( strId );

            if ( right != null && listShortcuts.size( ) < MAX_SHORTCUTS )
            {
                listShortcuts.add( right );
            }
        }

        return listShortcuts;
    }

    /**
     * Tells whether the users may choose their own shortcuts (site property, on when it is not set)
     *
     * @return true when the users may choose their own shortcuts, false when everyone has the default ones
     */
    public boolean isUserShortcutsEnabled( )
    {
        return !"0".equals( DatastoreService.getDataValue( DATASTORE_KEY_USER_SHORTCUTS_ENABLED, "1" ) );
    }

    /**
     * Tells whether a user has a list of their own
     *
     * @param user
     *            the user
     * @return true when the user saved a list and may use it, false when they have the default one
     */
    public boolean hasOwnShortcuts( AdminUser user )
    {
        return isUserShortcutsEnabled( ) && AdminUserPreferencesService.instance( ).existsKey( getUserId( user ), PREFERENCE_KEY );
    }

    /**
     * Saves the list of a user, kept to the entries of their menu
     *
     * @param user
     *            the user
     * @param listIds
     *            the ids of the rights, in their order
     * @param listFeatureGroups
     *            the menu of the user
     */
    public void saveUserShortcuts( AdminUser user, List<String> listIds, List<FeatureGroup> listFeatureGroups )
    {
        AdminUserPreferencesService.instance( ).put( getUserId( user ), PREFERENCE_KEY, clean( listIds, getFeatures( listFeatureGroups ) ) );
    }

    /**
     * Removes the list of a user, who gets the default list back
     *
     * @param user
     *            the user
     */
    public void resetUserShortcuts( AdminUser user )
    {
        AdminUserPreferencesService.instance( ).clearKey( getUserId( user ), PREFERENCE_KEY );
    }

    /**
     * Saves the default list, kept to the entries of the menu of the administrator who saves it. The users who saved a list of their own
     * keep it.
     *
     * @param listIds
     *            the ids of the rights, in their order
     * @param listFeatureGroups
     *            the menu of the administrator
     */
    public void saveDefaultShortcuts( List<String> listIds, List<FeatureGroup> listFeatureGroups )
    {
        DatastoreService.setDataValue( DATASTORE_KEY_DEFAULT, clean( listIds, getFeatures( listFeatureGroups ) ) );
    }

    /**
     * Keeps the ids of a list that are entries of the menu, once each, up to the highest number of shortcuts
     *
     * @param listIds
     *            the ids
     * @param mapFeatures
     *            the entries of the menu, by id
     * @return the list, its ids separated by commas
     */
    private static String clean( List<String> listIds, Map<String, Right> mapFeatures )
    {
        return listIds.stream( ).filter( mapFeatures::containsKey ).distinct( ).limit( MAX_SHORTCUTS ).collect( Collectors.joining( SEPARATOR ) );
    }

    private static Map<String, Right> getFeatures( List<FeatureGroup> listFeatureGroups )
    {
        Map<String, Right> mapFeatures = new LinkedHashMap<>( );

        for ( FeatureGroup featureGroup : listFeatureGroups )
        {
            for ( Right right : featureGroup.getFeatures( ) )
            {
                mapFeatures.put( right.getId( ), right );
            }
        }

        return mapFeatures;
    }

    private static List<String> split( String strList )
    {
        return Arrays.stream( StringUtils.split( StringUtils.defaultString( strList ), SEPARATOR ) ).map( String::trim ).collect( Collectors.toList( ) );
    }

    private static String getUserId( AdminUser user )
    {
        return Integer.toString( user.getUserId( ) );
    }
}
