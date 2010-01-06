/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.business.group;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Group right objects
 */
public final class GroupHome
{
    // Static variable pointed at the DAO instance
    private static IGroupDAO _dao = (IGroupDAO) SpringContextService.getBean( "groupDAO" );

    /**
     * Creates a new GroupHome object.
     */
    private GroupHome(  )
    {
    }

    /**
     * Creation of an instance of a mode
     *
     * @param group An instance of a group which contains the informations to create
     * @return The instance of a mode which has been created with its primary key.
     */
    public static Group create( Group group )
    {
        _dao.insert( group );

        return group;
    }

    /**
     * Update of the mode which is specified
     *
     * @param group The instance of the group which contains the data to store
     * @return The instance of the mode which has been updated
     */
    public static Group update( Group group )
    {
        _dao.store( group );

        return group;
    }

    /**
     * Remove the mode whose identifier is specified in parameter
     *
     * @param strGroupKey The identifier of the group to remove
     */
    public static void remove( String strGroupKey )
    {
        _dao.delete( strGroupKey );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an group whose identifier is specified in parameter
     *
     * @param strGroupKey The mode primary key
     * @return an instance of a group
     */
    public static Group findByPrimaryKey( String strGroupKey )
    {
        return _dao.load( strGroupKey );
    }

    /**
     * Return the list of all groups
     *
     * @return A ReferenceList of groups
     */
    public static ReferenceList getGroupsList(  )
    {
        ReferenceList groupList = _dao.selectGroupsList(  );

        return groupList;
    }

    /**
     * Returns the groups list
     *
     * @return Collection of Group
     */
    public static Collection<Group> findAll(  )
    {
        Collection<Group> groupList = _dao.selectAll(  );

        return groupList;
    }
}
