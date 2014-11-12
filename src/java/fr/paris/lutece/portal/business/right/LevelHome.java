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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Level right objects
 */
public final class LevelHome
{
    // Static variable pointed at the DAO instance
    private static ILevelDAO _dao = (ILevelDAO) SpringContextService.getBean( "levelDAO" );

    /**
     * Creates a new LevelHome object.
     */
    private LevelHome(  )
    {
    }

    /**
     * Creation of an instance of a mode
     *
     * @param mode An instance of a mode which contains the informations to store
     * @return The instance of a mode which has been created with its primary key.
     */
    public static Level create( Level mode )
    {
        _dao.insert( mode );

        return mode;
    }

    /**
     * Update of the mode which is specified
     *
     * @param mode The instance of the mode which contains the data to store
     * @return The instance of the mode which has been updated
     */
    public static Level update( Level mode )
    {
        _dao.store( mode );

        return mode;
    }

    /**
     * Remove the mode whose identifier is specified in parameter
     *
     * @param nId The identifier of the mode to remove
     */
    public static void remove( int nId )
    {
        _dao.delete( nId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an mode whose identifier is specified in parameter
     *
     * @param nKey The mode primary key
     * @return an instance of a mode
     */
    public static Level findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Return the list of all the modes
     *
     * @return A collection of modes objects
     */
    public static Collection<Level> getLevelsList(  )
    {
        return _dao.selectLevelsList(  );
    }
}
