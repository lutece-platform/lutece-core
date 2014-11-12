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
package fr.paris.lutece.portal.business.physicalfile;

import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * This class provides instances management methods (create, find, ...) for physical file objects
 */
public final class PhysicalFileHome
{
    // Static variable pointed at the DAO instance
    private static IPhysicalFileDAO _dao = (IPhysicalFileDAO) SpringContextService.getBean( "physicalFileDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private PhysicalFileHome(  )
    {
    }

    /**
     * Creation of an instance of record physical file
     *
     * @param physicalFile The instance of the physical file which contains the informations to store
     *
     * @return the id of the file after creation
     *
     */
    public static int create( PhysicalFile physicalFile )
    {
        return _dao.insert( physicalFile );
    }

    /**
     * Update of physical file which is specified in parameter
     *
     * @param  physicalFile The instance of the  record physicalFile which contains the informations to update
     *
     */
    public static void update( PhysicalFile physicalFile )
    {
        _dao.store( physicalFile );
    }

    /**
     *Delete the physical file whose identifier is specified in parameter
     *
     * @param nIdPhysicalFile The identifier of the record physical file
     */
    public static void remove( int nIdPhysicalFile )
    {
        _dao.delete( nIdPhysicalFile );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a physical file whose identifier is specified in parameter
     *
     * @param nKey The file  primary key
     * @return an instance of physical file
     */
    public static PhysicalFile findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }
}
