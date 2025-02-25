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
package fr.paris.lutece.portal.business.right;

import jakarta.enterprise.inject.spi.CDI;

import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for Level right objects
 */
public final class LevelHome
{
    // Static variable pointed at the DAO instance
    private static ILevelDAO _dao = CDI.current().select( ILevelDAO.class ).get( );

    /**
     * Creates a new LevelHome object.
     */
    private LevelHome( )
    {
    }

    /**
     * Creation of an instance of a level
     *
     * @param level
     *            An instance of a level which contains the informations to store
     * @return The instance of a level which has been created with its primary key.
     */
    public static Level create( Level level )
    {
        _dao.insert( level );

        return level;
    }

    /**
     * Update of the level which is specified
     *
     * @param level
     *            The instance of the level which contains the data to store
     * @return The instance of the level which has been updated
     */
    public static Level update( Level level )
    {
        _dao.store( level );

        return level;
    }

    /**
     * Remove the level whose identifier is specified in parameter
     *
     * @param nId
     *            The identifier of the level to remove
     */
    public static void remove( int nId )
    {
        _dao.delete( nId );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an level whose identifier is specified in parameter
     *
     * @param nKey
     *            The level primary key
     * @return an instance of a level
     */
    public static Level findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Return the list of all the levels
     *
     * @return A collection of levels objects
     */
    public static Collection<Level> getLevelsList( )
    {
        return _dao.selectLevelsList( );
    }
    
    /**
     * Generates a new primary key
     * 
     * @return The new primary key
     */
    public static int newPrimaryKey( )
    {
        return _dao.newPrimaryKey( );
    }
}
