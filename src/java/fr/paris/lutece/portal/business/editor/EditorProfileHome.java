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

import java.util.List;

import jakarta.enterprise.inject.spi.CDI;

/**
 * Home of the editor profiles
 */
public final class EditorProfileHome
{
    private static IEditorProfileDAO _dao = CDI.current( ).select( IEditorProfileDAO.class ).get( );

    /**
     * Private constructor
     */
    private EditorProfileHome( )
    {
    }

    /**
     * Creates a profile, after the existing profiles of its editor in the order of priority
     *
     * @param profile
     *            the profile
     * @return the profile, with its identifier
     */
    public static EditorProfile create( EditorProfile profile )
    {
        profile.setPriority( _dao.selectMaxPriority( profile.getEditorName( ) ) + 1 );
        _dao.insert( profile );

        return profile;
    }

    /**
     * Updates a profile and its targets. The editor, the priority and the default flag are not changed.
     *
     * @param profile
     *            the profile
     * @return the profile
     */
    public static EditorProfile update( EditorProfile profile )
    {
        _dao.store( profile );

        return profile;
    }

    /**
     * Removes a profile
     *
     * @param nIdProfile
     *            the identifier of the profile
     */
    public static void remove( int nIdProfile )
    {
        _dao.delete( nIdProfile );
    }

    /**
     * @param nIdProfile
     *            the identifier of the profile
     * @return the profile with its targets, null if it does not exist
     */
    public static EditorProfile findByPrimaryKey( int nIdProfile )
    {
        return _dao.load( nIdProfile );
    }

    /**
     * @return the profiles with their targets, by editor, then by priority with the default profile of each editor last
     */
    public static List<EditorProfile> findAll( )
    {
        return _dao.selectAll( );
    }

    /**
     * Updates the priority of a profile
     *
     * @param nIdProfile
     *            the identifier of the profile
     * @param nPriority
     *            the priority
     */
    public static void updatePriority( int nIdProfile, int nPriority )
    {
        _dao.storePriority( nIdProfile, nPriority );
    }
}
