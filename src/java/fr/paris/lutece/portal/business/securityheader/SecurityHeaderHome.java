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
package fr.paris.lutece.portal.business.securityheader;


import java.util.Collection;

import jakarta.enterprise.inject.spi.CDI;

/**
 * This class provides instances management methods (create, find, ...) for SecurityHeader objects
 */
public final class SecurityHeaderHome
{
    // Static variable pointed at the DAO instance
    private static ISecurityHeaderDAO _dao = CDI.current().select(ISecurityHeaderDAO.class).get();

    /**
     * Private constructor - this class need not be instantiated
     */
    private SecurityHeaderHome( )
    {
    }

    /**
     * Creation of an instance of Security header
     *
     * @param securityHeader
     *            The instance of the security header which contains the informations to store
     * @return The instance of security header which has been created with its primary key.
     */
    public static SecurityHeader create( SecurityHeader securityheader )
    {
        _dao.insert( securityheader );

        return securityheader;
    }

    /**
     * Update of the security header which is specified in parameter
     *
     * @param securityHeader
     *            The instance of the Security header which contains the data to store
     * @return The instance of the security header which has been updated
     */
    public static void update( SecurityHeader securityHeader )
    {
        _dao.store( securityHeader );
    }

    /**
     * Update of the is_active column value of the security header which is specified in parameter
     * 
     * @param nSecurityHeaderId
     *            The securityHeader Id
     * @param isActiveValue
     *            The value to set to is_active column
     */
    public static void updateIsActive( int nSecurityHeaderId, boolean isActiveValue )
    {
    	_dao.updateIsActive( nSecurityHeaderId, isActiveValue );
    }
    
    /**
     * Remove the security header whose identifier is specified in parameter
     *
     * @param nSecurityHeaderId
     *            The securityHeader Id
     */
    public static void remove( int nSecurityHeaderId )
    {
        _dao.delete( nSecurityHeaderId );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a security header whose identifier is specified in parameter
     *
     * @param nKey
     *            The security header primary key
     * @return an instance of SecurityHeader
     */
    public static SecurityHeader findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Loads the data of all the security headers and returns them in form of a collection.
     *
     * @return the collection which contains the data of all the security headers
     */
    public static Collection<SecurityHeader> findAll( )
    {
        return _dao.selectAll( );
    }
}