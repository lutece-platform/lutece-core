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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.editor.EditorProfile;

/**
 * Tests the choice of the editor profile, which needs neither the database nor CDI
 */
public class EditorProfileServiceTest
{
    private static final List<String> NONE = List.of( );

    /**
     * The first matching profile applies, in the order of the list; the default profile applies when none matches
     */
    @Test
    public void testFindProfileByPriority( )
    {
        EditorProfile defaultProfile = profile( "tinymce6", "default", true, NONE, NONE, NONE );
        EditorProfile publishers = profile( "tinymce6", "publishers", false, List.of( "document" ), List.of( "content_publisher" ), NONE );
        EditorProfile documents = profile( "tinymce6", "documents", false, List.of( "document", "blog" ), NONE, NONE );
        List<EditorProfile> listProfiles = List.of( publishers, documents, defaultProfile );

        assertEquals( "publishers", EditorProfileService.findProfile( listProfiles, "tinymce6", "document", List.of( "content_publisher" ), NONE ).getName( ) );
        assertEquals( "documents", EditorProfileService.findProfile( listProfiles, "tinymce6", "document", List.of( "other_role" ), NONE ).getName( ) );
        assertEquals( "documents", EditorProfileService.findProfile( listProfiles, "tinymce6", "blog", List.of( "content_publisher" ), NONE ).getName( ) );
        assertEquals( "default", EditorProfileService.findProfile( listProfiles, "tinymce6", "forms", List.of( "content_publisher" ), NONE ).getName( ) );
    }

    /**
     * Roles and workgroups are optional; when set, the user needs one of each
     */
    @Test
    public void testFindProfileByRoleAndWorkgroup( )
    {
        EditorProfile defaultProfile = profile( "tinymce6", "default", true, NONE, NONE, NONE );
        EditorProfile both = profile( "tinymce6", "both", false, List.of( "core" ), List.of( "role_a", "role_b" ), List.of( "dsin" ) );
        List<EditorProfile> listProfiles = List.of( both, defaultProfile );

        assertEquals( "both", EditorProfileService.findProfile( listProfiles, "tinymce6", "core", List.of( "role_b" ), List.of( "dsin", "other" ) ).getName( ) );
        assertEquals( "default", EditorProfileService.findProfile( listProfiles, "tinymce6", "core", List.of( "role_b" ), List.of( "other" ) ).getName( ),
                "the workgroup is missing" );
        assertEquals( "default", EditorProfileService.findProfile( listProfiles, "tinymce6", "core", NONE, List.of( "dsin" ) ).getName( ), "the role is missing" );
    }

    /**
     * Only the profiles of the editor are candidates, including for the default profile
     */
    @Test
    public void testFindProfileByEditor( )
    {
        EditorProfile default5 = profile( "othereditor", "default 5", true, NONE, NONE, NONE );
        EditorProfile default6 = profile( "tinymce6", "default 6", true, NONE, NONE, NONE );
        EditorProfile documents5 = profile( "othereditor", "documents 5", false, List.of( "document" ), NONE, NONE );
        List<EditorProfile> listProfiles = List.of( documents5, default5, default6 );

        assertEquals( "documents 5", EditorProfileService.findProfile( listProfiles, "othereditor", "document", NONE, NONE ).getName( ) );
        assertEquals( "default 6", EditorProfileService.findProfile( listProfiles, "tinymce6", "document", NONE, NONE ).getName( ),
                "a profile of another editor never applies" );
        assertNull( EditorProfileService.findProfile( listProfiles, "markdown", "document", NONE, NONE ), "an editor without profile" );
    }

    /**
     * Without any profile, there is nothing to apply
     */
    @Test
    public void testFindProfileWithoutProfiles( )
    {
        assertNull( EditorProfileService.findProfile( new ArrayList<>( ), "tinymce6", "core", NONE, NONE ) );
    }

    /**
     * The plugin is read from the path of the template, then from the URI of the request
     */
    @Test
    public void testGetPlugin( )
    {
        assertEquals( "document", EditorProfileService.getPluginFromTemplateName( "admin/plugins/document/modify_document.html" ) );
        assertEquals( "document", EditorProfileService.getPluginFromTemplateName( "/admin/plugins/document/attributes/modify_richtext.html" ) );
        assertEquals( "core", EditorProfileService.getPluginFromTemplateName( "admin/system/modify_properties.html" ) );
        assertNull( EditorProfileService.getPluginFromTemplateName( "skin/site/page_frameset.html" ) );
        assertNull( EditorProfileService.getPluginFromTemplateName( null ) );

        assertEquals( "forms", EditorProfileService.getPluginFromRequestUri( "/lutece/jsp/admin/plugins/forms/ManageForms.jsp" ) );
        assertEquals( "core", EditorProfileService.getPluginFromRequestUri( "/lutece/jsp/admin/system/ManageProperties.jsp" ) );
        assertEquals( "core", EditorProfileService.getPluginFromRequestUri( null ) );

        // the portlet forms of the plugins are included in a template of the core: the JSP names the plugin
        assertEquals( "html", EditorProfileService.getPlugin( "admin/portlet/create_portlet.html", "/lutece/jsp/admin/plugins/html/CreatePortletHtml.jsp" ) );
        assertEquals( "document", EditorProfileService.getPlugin( "admin/plugins/document/modify_document.html", "/lutece/jsp/admin/plugins/html/Any.jsp" ) );
        assertEquals( "core", EditorProfileService.getPlugin( "admin/system/modify_properties.html", "/lutece/jsp/admin/system/ManageProperties.jsp" ) );
        assertEquals( "core", EditorProfileService.getPlugin( null, null ) );
    }

    /**
     * The configuration is empty or a JSON object
     */
    @Test
    public void testIsValidConfig( )
    {
        assertTrue( EditorProfileService.isValidConfig( "" ) );
        assertTrue( EditorProfileService.isValidConfig( "{}" ) );
        assertTrue( EditorProfileService.isValidConfig( "{ \"toolbar\": \"undo redo | lutece\", \"height\": 400 }" ) );
        assertFalse( EditorProfileService.isValidConfig( "[ 1, 2 ]" ), "an array" );
        assertFalse( EditorProfileService.isValidConfig( "{ \"height\": }" ), "malformed" );
        assertFalse( EditorProfileService.isValidConfig( "toolbar: 'undo'" ), "not JSON" );
    }

    /**
     * Builds a profile
     *
     * @param strEditorName
     *            the editor
     * @param strName
     *            the name
     * @param bDefault
     *            true for the default profile
     * @param listPlugins
     *            the plugins
     * @param listRoles
     *            the roles
     * @param listWorkgroups
     *            the workgroups
     * @return the profile
     */
    private static EditorProfile profile( String strEditorName, String strName, boolean bDefault, List<String> listPlugins, List<String> listRoles,
            List<String> listWorkgroups )
    {
        EditorProfile profile = new EditorProfile( );
        profile.setEditorName( strEditorName );
        profile.setName( strName );
        profile.setDefault( bDefault );
        profile.setPlugins( new ArrayList<>( listPlugins ) );
        profile.setRoles( new ArrayList<>( listRoles ) );
        profile.setWorkgroups( new ArrayList<>( listWorkgroups ) );

        return profile;
    }
}
