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
package fr.paris.lutece.portal.business.editor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A profile of a back office rich text editor: the configuration of the editor in the templates of some plugins, optionally only for the users holding
 * some roles or belonging to some workgroups. The profiles of an editor apply while it is the editor of the back office; its default profile applies when
 * no other of its profiles matches.
 */
public class EditorProfile implements Serializable
{
    /** The configuration of a profile that changes nothing */
    public static final String EMPTY_CONFIG = "{}";

    private static final long serialVersionUID = 4726719184364730213L;

    private int _nId;
    private String _strName;
    private String _strDescription;
    private String _strEditorName;
    private String _strConfig = EMPTY_CONFIG;
    private int _nPriority;
    private boolean _bDefault;
    private List<String> _listPlugins = new ArrayList<>( );
    private List<String> _listRoles = new ArrayList<>( );
    private List<String> _listWorkgroups = new ArrayList<>( );

    /**
     * @return the identifier
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * @param nId
     *            the identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * @return the name
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * @param strName
     *            the name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * @return the description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * @param strDescription
     *            the description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * @return the name of the editor (core_text_editor) the profile belongs to. It is set at the creation and never changed.
     */
    public String getEditorName( )
    {
        return _strEditorName;
    }

    /**
     * @param strEditorName
     *            the name of the editor
     */
    public void setEditorName( String strEditorName )
    {
        _strEditorName = strEditorName;
    }

    /**
     * @return the configuration of the editor: a JSON object whose options replace the ones of the editor template and of the calling template
     */
    public String getConfig( )
    {
        return _strConfig;
    }

    /**
     * @param strConfig
     *            the configuration, a JSON object
     */
    public void setConfig( String strConfig )
    {
        _strConfig = strConfig;
    }

    /**
     * @return the priority among the profiles of the editor: the matching profile of lowest priority applies
     */
    public int getPriority( )
    {
        return _nPriority;
    }

    /**
     * @param nPriority
     *            the priority
     */
    public void setPriority( int nPriority )
    {
        _nPriority = nPriority;
    }

    /**
     * @return true for the default profile of the editor, which can be modified but not removed, and has no target
     */
    public boolean isDefault( )
    {
        return _bDefault;
    }

    /**
     * @param bDefault
     *            true for the default profile
     */
    public void setDefault( boolean bDefault )
    {
        _bDefault = bDefault;
    }

    /**
     * @return the names of the plugins whose templates use the profile (core for the templates of the core)
     */
    public List<String> getPlugins( )
    {
        return _listPlugins;
    }

    /**
     * @param listPlugins
     *            the names of the plugins
     */
    public void setPlugins( List<String> listPlugins )
    {
        _listPlugins = listPlugins;
    }

    /**
     * @return the keys of the roles, one of which the user must hold, empty for any user
     */
    public List<String> getRoles( )
    {
        return _listRoles;
    }

    /**
     * @param listRoles
     *            the keys of the roles
     */
    public void setRoles( List<String> listRoles )
    {
        _listRoles = listRoles;
    }

    /**
     * @return the keys of the workgroups, one of which the user must belong to, empty for any user
     */
    public List<String> getWorkgroups( )
    {
        return _listWorkgroups;
    }

    /**
     * @param listWorkgroups
     *            the keys of the workgroups
     */
    public void setWorkgroups( List<String> listWorkgroups )
    {
        _listWorkgroups = listWorkgroups;
    }
}
