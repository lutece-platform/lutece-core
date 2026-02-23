/*
 * Copyright (c) 2002-2026, City of Paris
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

import java.util.List;

public interface ISecurityHeaderConfigItemDAO
{
	/**
     * Insert a new record in the table.
     *
     * @param securityHeaderConfigItem
     *            instance of the SecurityHeaderConfigItem object to insert
     */
    void insert( SecurityHeaderConfigItem securityHeaderConfigItem );
	
	/**
     * Delete records from the table which security header id is equals to nSecurityHeaderId
     *
     * @param nSecurityHeaderId
     *            The identifier of the security header
     */
    void deleteForSecurityHeaderId( int nSecurityHeaderId );
    
    /**
     * Update the record in the table
     *
     * @param securityHeaderConfigItem
     *            The reference of the security header config item
     */
    void store( SecurityHeaderConfigItem securityHeaderConfigItem );
    
    /**
     * Delete a record from the table.
     *
     * @param nSecurityHeaderConfigItemId
     *            int identifier of the SecurityHeaderConfigItem to delete
     */
    void delete( int nSecurityHeaderConfigItemId );
    
    // /////////////////////////////////////////////////////////////////////////
    // Finders
    
    /**
     * load the data of the security header config item from the table
     *
     * @param nKey
     *            The identifier of the security header config item
     * @return The instance of the security header config item
     */
    SecurityHeaderConfigItem load( int nKey );
    
    /**
     * Returns all security header config items having a specific url pattern of a security header 
     *
     * @param securityHeaderId
     *            The security header id
     * @param urlPattern
     *            The URL pattern
     * @return the list which contains the security header config items having a specific url pattern of a security header
     */
    List<SecurityHeaderConfigItem> find( int securityHeaderId, String urlPattern );
	
    /**
     * Returns all security header config items of a security header
     *
     * @param securityHeaderId
     *            The security header id
     * @return the list which contains the security header config items of a security header
     */
	List<SecurityHeaderConfigItem> findBySecurityHeaderId( int securityHeaderId );
}