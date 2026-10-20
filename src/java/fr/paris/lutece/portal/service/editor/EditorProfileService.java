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
package fr.paris.lutece.portal.service.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.portal.business.editor.EditorProfile;
import fr.paris.lutece.portal.business.editor.EditorProfileHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Chooses the profile of the back office rich text editor. Only the profiles of the editor of the back office are candidates. A profile targets plugins,
 * and optionally roles and workgroups: it matches the templates of one of its plugins, for a user holding one of its roles (any user when it has none) and
 * belonging to one of its workgroups (any user when it has none). The first matching profile, in the order of priority, applies; the default profile of
 * the editor applies when none matches.
 */
@ApplicationScoped
public class EditorProfileService
{
    /** The name standing for the templates of the core */
    public static final String PLUGIN_CORE = "core";

    // admin/plugins/document/modify_document.html -> document
    private static final Pattern PATTERN_PLUGIN_TEMPLATE = Pattern.compile( "^/?admin/plugins/([^/]+)/" );
    private static final Pattern PATTERN_CORE_TEMPLATE = Pattern.compile( "^/?admin/" );
    // /lutece/jsp/admin/plugins/document/ModifyDocument.jsp -> document
    private static final Pattern PATTERN_PLUGIN_URI = Pattern.compile( "/jsp/admin/plugins/([^/]+)/" );
    private static final ObjectMapper MAPPER = new ObjectMapper( );

    /**
     * Gives the profile of an editor for the templates of a plugin and a user
     *
     * @param strEditorName
     *            the name of the editor
     * @param strPlugin
     *            the name of the plugin, core for the templates of the core
     * @param user
     *            the user, null when unknown: only the profiles without role nor workgroup match
     * @return the matching profile, or the default profile of the editor, null when the editor has no profile
     */
    public EditorProfile getProfile( String strEditorName, String strPlugin, AdminUser user )
    {
        Collection<String> listRoles = ( user != null && user.getRoles( ) != null ) ? user.getRoles( ).keySet( ) : new ArrayList<>( );
        Collection<String> listWorkgroups = ( user != null && user.getUserWorkgroups( ) != null ) ? user.getUserWorkgroups( ) : new ArrayList<>( );

        return findProfile( EditorProfileHome.findAll( ), strEditorName, strPlugin, listRoles, listWorkgroups );
    }

    /**
     * Finds the profile of an editor applying to a plugin and to the roles and workgroups of a user
     *
     * @param listProfiles
     *            the profiles, by priority
     * @param strEditorName
     *            the name of the editor
     * @param strPlugin
     *            the name of the plugin
     * @param listRoles
     *            the keys of the roles of the user
     * @param listWorkgroups
     *            the keys of the workgroups of the user
     * @return the first matching profile of the editor, or its default profile, null when the editor has no profile
     */
    public static EditorProfile findProfile( List<EditorProfile> listProfiles, String strEditorName, String strPlugin, Collection<String> listRoles,
            Collection<String> listWorkgroups )
    {
        EditorProfile defaultProfile = null;

        for ( EditorProfile profile : listProfiles )
        {
            if ( !StringUtils.equals( profile.getEditorName( ), strEditorName ) )
            {
                continue;
            }

            if ( profile.isDefault( ) )
            {
                defaultProfile = profile;
            }
            else if ( profile.getPlugins( ).contains( strPlugin ) && matches( profile.getRoles( ), listRoles )
                    && matches( profile.getWorkgroups( ), listWorkgroups ) )
            {
                return profile;
            }
        }

        return defaultProfile;
    }

    /**
     * An empty target list matches any user, otherwise the user must have one of the values
     *
     * @param listTargets
     *            the values targeted by the profile
     * @param listUserValues
     *            the values of the user
     * @return true if the profile matches
     */
    private static boolean matches( List<String> listTargets, Collection<String> listUserValues )
    {
        return listTargets.isEmpty( ) || listTargets.stream( ).anyMatch( listUserValues::contains );
    }

    /**
     * Guesses the plugin of a template from its path: admin/plugins/[plugin]/... for a plugin, admin/... for the core
     *
     * @param strTemplateName
     *            the path of the template
     * @return the name of the plugin, core, or null when the path tells nothing
     */
    public static String getPluginFromTemplateName( String strTemplateName )
    {
        if ( strTemplateName == null )
        {
            return null;
        }

        Matcher matcher = PATTERN_PLUGIN_TEMPLATE.matcher( strTemplateName );

        if ( matcher.find( ) )
        {
            return matcher.group( 1 );
        }

        return PATTERN_CORE_TEMPLATE.matcher( strTemplateName ).find( ) ? PLUGIN_CORE : null;
    }

    /**
     * Guesses the plugin of a request from its URI: .../jsp/admin/plugins/[plugin]/... for a plugin, the core otherwise
     *
     * @param strRequestUri
     *            the URI of the request
     * @return the name of the plugin, or core
     */
    public static String getPluginFromRequestUri( String strRequestUri )
    {
        if ( strRequestUri != null )
        {
            Matcher matcher = PATTERN_PLUGIN_URI.matcher( strRequestUri );

            if ( matcher.find( ) )
            {
                return matcher.group( 1 );
            }
        }

        return PLUGIN_CORE;
    }

    /**
     * Chooses the plugin of a page from its rendered template and its request. A template of a plugin names it. A template of the core may render the form
     * of a plugin (the portlet forms of the plugins are included in admin/portlet/create_portlet.html): a JSP of a plugin then names it. Otherwise the page
     * belongs to the core.
     *
     * @param strTemplateName
     *            the path of the rendered template, may be null
     * @param strRequestUri
     *            the URI of the request, may be null
     * @return the name of the plugin, or core
     */
    public static String getPlugin( String strTemplateName, String strRequestUri )
    {
        String strPlugin = getPluginFromTemplateName( strTemplateName );

        if ( strPlugin != null && !PLUGIN_CORE.equals( strPlugin ) )
        {
            return strPlugin;
        }

        return getPluginFromRequestUri( strRequestUri );
    }

    /**
     * Checks the configuration of a profile
     *
     * @param strConfig
     *            the configuration
     * @return true when it is empty or a JSON object
     */
    public static boolean isValidConfig( String strConfig )
    {
        if ( StringUtils.isBlank( strConfig ) )
        {
            return true;
        }

        try
        {
            JsonNode node = MAPPER.readTree( strConfig );

            return node != null && node.isObject( );
        }
        catch( JsonProcessingException e )
        {
            return false;
        }
    }

    /**
     * Creates the default profile of each editor that has none, so that every editor keeps one
     *
     * @param listEditorNames
     *            the names of the editors of the back office
     * @param strName
     *            the name given to a created default profile
     */
    public void createMissingDefaultProfiles( List<String> listEditorNames, String strName )
    {
        List<EditorProfile> listProfiles = EditorProfileHome.findAll( );

        for ( String strEditorName : listEditorNames )
        {
            if ( listProfiles.stream( ).noneMatch( profile -> profile.isDefault( ) && strEditorName.equals( profile.getEditorName( ) ) ) )
            {
                EditorProfile profile = new EditorProfile( );
                profile.setName( strName );
                profile.setEditorName( strEditorName );
                profile.setDefault( true );
                EditorProfileHome.create( profile );
            }
        }
    }

    /**
     * Moves a profile one step before or after in the order of priority of its editor. The default profile stays last.
     *
     * @param profile
     *            the profile
     * @param bUp
     *            true to move it before the previous one, false after the next one
     */
    public void move( EditorProfile profile, boolean bUp )
    {
        int nIdProfile = profile.getId( );
        List<EditorProfile> listProfiles = new ArrayList<>( EditorProfileHome.findAll( ) );
        listProfiles.removeIf( other -> other.isDefault( ) || !StringUtils.equals( other.getEditorName( ), profile.getEditorName( ) ) );

        int nIndex = -1;

        for ( int i = 0; i < listProfiles.size( ); i++ )
        {
            if ( listProfiles.get( i ).getId( ) == nIdProfile )
            {
                nIndex = i;
            }
        }

        int nOther = bUp ? nIndex - 1 : nIndex + 1;

        if ( nIndex < 0 || nOther < 0 || nOther >= listProfiles.size( ) )
        {
            return;
        }

        listProfiles.add( nOther, listProfiles.remove( nIndex ) );

        // the priorities are renumbered from 1, which also repairs duplicates
        for ( int i = 0; i < listProfiles.size( ); i++ )
        {
            EditorProfileHome.updatePriority( listProfiles.get( i ).getId( ), i + 1 );
        }
    }
}
