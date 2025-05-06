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

/**
 * ISecurityHeaderDAO Interface
 */
public interface ISecurityHeaderDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param securityheader
     *            instance of the SecurityHeader object to insert
     */
    void insert( SecurityHeader securityheader );

    /**
     * Update the record in the table.
     *
     * @param securityheader
     *            the reference of the SecurityHeader
     */
    void store( SecurityHeader securityheader );

    /**
     * Delete a record from the table.
     *
     * @param nSecurityHeaderId
     *            int identifier of the SecurityHeader to delete
     */
    void delete( int nSecurityHeaderId );
    
    /**
     * Update of the is_active column value of securityHeader which is specified in parameter.
     * 
     * @param nSecurityHeaderId
     *            The securityHeader Id
     * @param isActiveValue
     *            The value to set to is_active column
     */
    void updateIsActive( int nSecurityHeaderId, boolean isActiveValue );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * load the data of the security header from the table
     *
     * @param nKey
     *            The identifier of the securityHeader
     * @return The instance of the securityHeader
     */
    SecurityHeader load( int nKey );

    /**
     * Loads the data of all the security headers and returns them in form of a collection
     *
     * @return the collection which contains the data of all the security headers
     */
    Collection<SecurityHeader> selectAll( );
}