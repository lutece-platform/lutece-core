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

/**
 * Data access interface of the editor profiles
 */
public interface IEditorProfileDAO
{
    /**
     * Inserts a profile with its targets, and sets its generated identifier
     *
     * @param profile
     *            the profile
     */
    void insert( EditorProfile profile );

    /**
     * Updates a profile and replaces its targets. Its editor is not changed.
     *
     * @param profile
     *            the profile
     */
    void store( EditorProfile profile );

    /**
     * Deletes a profile and its targets
     *
     * @param nIdProfile
     *            the identifier of the profile
     */
    void delete( int nIdProfile );

    /**
     * Loads a profile with its targets
     *
     * @param nIdProfile
     *            the identifier of the profile
     * @return the profile, null if it does not exist
     */
    EditorProfile load( int nIdProfile );

    /**
     * Loads the profiles with their targets, by editor, then by priority with the default profile of each editor last
     *
     * @return the profiles
     */
    List<EditorProfile> selectAll( );

    /**
     * Updates the priority of a profile
     *
     * @param nIdProfile
     *            the identifier of the profile
     * @param nPriority
     *            the priority
     */
    void storePriority( int nIdProfile, int nPriority );

    /**
     * @param strEditorName
     *            the name of an editor
     * @return the highest priority of the profiles of the editor, 0 when there are none
     */
    int selectMaxPriority( String strEditorName );
}
