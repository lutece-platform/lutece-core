/*
 * Copyright (c) 2002-2007, Mairie de Paris
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
 * This class provides instances management methods (create, find, ...) for Right objects
 */
public final class RightHome
{
    // Static variable pointed at the DAO instance
    private static IRightDAO _dao = (IRightDAO) SpringContextService.getBean( "rightDAO" );

    /**
     * Creates a new RightHome object.
     */
    private RightHome(  )
    {
    }

    /**
     * Creation of an instance of an admin right
     *
     * @param right An instance of an admin right which contains the informations to store
     * @return The instance of an admin right which has been created with its primary key.
     */
    public static Right create( Right right )
    {
        _dao.insert( right );

        return right;
    }

    /**
     * Update of the admin right which is specified
     *
     * @param right The instance of the admin right which contains the data to store
     * @return The instance of the admin right which has been updated
     */
    public static Right update( Right right )
    {
        _dao.store( right );

        return right;
    }

    /**
     * Remove the admin right whose identifier is specified in parameter
     *
     * @param strId The identifier of the admin right to remove
     */
    public static void remove( String strId )
    {
        _dao.delete( strId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an admin right whose identifier is specified in parameter
     *
     * @param strKey The admin right primary key
     * @return an instance of an admin right
     */
    public static Right findByPrimaryKey( String strKey )
    {
        return _dao.load( strKey );
    }

    /**
     * Loads the data of all the rights and returns them in form of a collection
     *
     * @return the collection which contains the data of all the rights
     */
    public static Collection<Right> getRightsList(  )
    {
        return _dao.selectRightsList(  );
    }

    /**
     * Loads the data of all the rights with level greater or equal than nLevel
     * and returns them in form of a collection
     *
     * @return the collection which contains the data of all the rights
     */
    public static Collection<Right> getRightsList( int nLevel )
    {
        return _dao.selectRightsList( nLevel );
    }

    /**
     * Loads the data of all the rights with the specified feature group
     * and returns them in form of a collection
     *
     * @param strFeatureGroup the name of the feature group
     * @return the collection which contains the data of all the rights
     */
    public static Collection<Right> getRightsList( String strFeatureGroup )
    {
        return _dao.selectRightsList( strFeatureGroup );
    }
}
