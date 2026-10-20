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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Data access of the editor profiles. The targets of a profile (plugins, roles, workgroups) are rows of core_editor_profile_target.
 */
@ApplicationScoped
public final class EditorProfileDAO implements IEditorProfileDAO
{
    private static final String TARGET_PLUGIN = "plugin";
    private static final String TARGET_ROLE = "role";
    private static final String TARGET_WORKGROUP = "workgroup";

    private static final String SQL_QUERY_SELECT_COLUMNS = "SELECT id_profile, name, description, editor_name, config, priority, is_default FROM core_editor_profile";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_COLUMNS + " WHERE id_profile = ?";
    private static final String SQL_QUERY_SELECTALL = SQL_QUERY_SELECT_COLUMNS + " ORDER BY editor_name, is_default, priority, id_profile";
    private static final String SQL_QUERY_INSERT = "INSERT INTO core_editor_profile ( name, description, editor_name, config, priority, is_default ) VALUES ( ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_UPDATE = "UPDATE core_editor_profile SET name = ?, description = ?, config = ? WHERE id_profile = ?";
    private static final String SQL_QUERY_UPDATE_PRIORITY = "UPDATE core_editor_profile SET priority = ? WHERE id_profile = ?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_editor_profile WHERE id_profile = ?";
    private static final String SQL_QUERY_SELECT_MAX_PRIORITY = "SELECT MAX( priority ) FROM core_editor_profile WHERE editor_name = ?";
    private static final String SQL_QUERY_SELECT_TARGETS = "SELECT id_profile, target_type, target_value FROM core_editor_profile_target WHERE id_profile = ? ORDER BY target_value";
    private static final String SQL_QUERY_SELECTALL_TARGETS = "SELECT id_profile, target_type, target_value FROM core_editor_profile_target ORDER BY target_value";
    private static final String SQL_QUERY_INSERT_TARGET = "INSERT INTO core_editor_profile_target ( id_profile, target_type, target_value ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_DELETE_TARGETS = "DELETE FROM core_editor_profile_target WHERE id_profile = ?";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( EditorProfile profile )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, profile.getName( ) );
            daoUtil.setString( nIndex++, profile.getDescription( ) );
            daoUtil.setString( nIndex++, profile.getEditorName( ) );
            daoUtil.setString( nIndex++, profile.getConfig( ) );
            daoUtil.setInt( nIndex++, profile.getPriority( ) );
            daoUtil.setBoolean( nIndex, profile.isDefault( ) );
            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                profile.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        insertTargets( profile );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( EditorProfile profile )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, profile.getName( ) );
            daoUtil.setString( nIndex++, profile.getDescription( ) );
            daoUtil.setString( nIndex++, profile.getConfig( ) );
            daoUtil.setInt( nIndex, profile.getId( ) );
            daoUtil.executeUpdate( );
        }
        deleteTargets( profile.getId( ) );
        insertTargets( profile );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdProfile )
    {
        deleteTargets( nIdProfile );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdProfile );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EditorProfile load( int nIdProfile )
    {
        EditorProfile profile = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nIdProfile );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                profile = dataToObject( daoUtil );
            }
        }

        if ( profile != null )
        {
            try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_TARGETS ) )
            {
                daoUtil.setInt( 1, nIdProfile );
                daoUtil.executeQuery( );

                while ( daoUtil.next( ) )
                {
                    addTarget( profile, daoUtil.getString( 2 ), daoUtil.getString( 3 ) );
                }
            }
        }

        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EditorProfile> selectAll( )
    {
        Map<Integer, EditorProfile> mapProfiles = new LinkedHashMap<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                EditorProfile profile = dataToObject( daoUtil );
                mapProfiles.put( profile.getId( ), profile );
            }
        }

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_TARGETS ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                EditorProfile profile = mapProfiles.get( daoUtil.getInt( 1 ) );

                if ( profile != null )
                {
                    addTarget( profile, daoUtil.getString( 2 ), daoUtil.getString( 3 ) );
                }
            }
        }

        return new ArrayList<>( mapProfiles.values( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storePriority( int nIdProfile, int nPriority )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PRIORITY ) )
        {
            daoUtil.setInt( 1, nPriority );
            daoUtil.setInt( 2, nIdProfile );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int selectMaxPriority( String strEditorName )
    {
        int nMaxPriority = 0;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_PRIORITY ) )
        {
            daoUtil.setString( 1, strEditorName );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nMaxPriority = daoUtil.getInt( 1 );
            }
        }

        return nMaxPriority;
    }

    /**
     * Inserts the targets of a profile
     *
     * @param profile
     *            the profile
     */
    private void insertTargets( EditorProfile profile )
    {
        insertTargets( profile.getId( ), TARGET_PLUGIN, profile.getPlugins( ) );
        insertTargets( profile.getId( ), TARGET_ROLE, profile.getRoles( ) );
        insertTargets( profile.getId( ), TARGET_WORKGROUP, profile.getWorkgroups( ) );
    }

    /**
     * Inserts the targets of one type
     *
     * @param nIdProfile
     *            the identifier of the profile
     * @param strTargetType
     *            the type of the targets
     * @param listValues
     *            the values
     */
    private void insertTargets( int nIdProfile, String strTargetType, List<String> listValues )
    {
        if ( listValues == null )
        {
            return;
        }

        for ( String strValue : listValues )
        {
            try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_TARGET ) )
            {
                daoUtil.setInt( 1, nIdProfile );
                daoUtil.setString( 2, strTargetType );
                daoUtil.setString( 3, strValue );
                daoUtil.executeUpdate( );
            }
        }
    }

    /**
     * Deletes the targets of a profile
     *
     * @param nIdProfile
     *            the identifier of the profile
     */
    private void deleteTargets( int nIdProfile )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_TARGETS ) )
        {
            daoUtil.setInt( 1, nIdProfile );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * Adds a target to the list of its type
     *
     * @param profile
     *            the profile
     * @param strTargetType
     *            the type of the target
     * @param strValue
     *            the value
     */
    private static void addTarget( EditorProfile profile, String strTargetType, String strValue )
    {
        switch( strTargetType )
        {
            case TARGET_PLUGIN:
                profile.getPlugins( ).add( strValue );
                break;
            case TARGET_ROLE:
                profile.getRoles( ).add( strValue );
                break;
            case TARGET_WORKGROUP:
                profile.getWorkgroups( ).add( strValue );
                break;
            default:
                break;
        }
    }

    /**
     * Builds a profile from the current row, without its targets
     *
     * @param daoUtil
     *            the result of a query on the columns of SQL_QUERY_SELECT_COLUMNS
     * @return the profile
     */
    private static EditorProfile dataToObject( DAOUtil daoUtil )
    {
        int nIndex = 1;
        EditorProfile profile = new EditorProfile( );
        profile.setId( daoUtil.getInt( nIndex++ ) );
        profile.setName( daoUtil.getString( nIndex++ ) );
        profile.setDescription( daoUtil.getString( nIndex++ ) );
        profile.setEditorName( daoUtil.getString( nIndex++ ) );
        profile.setConfig( daoUtil.getString( nIndex++ ) );
        profile.setPriority( daoUtil.getInt( nIndex++ ) );
        profile.setDefault( daoUtil.getBoolean( nIndex ) );

        return profile;
    }
}
