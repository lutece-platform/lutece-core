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

import fr.paris.lutece.portal.service.spring.SpringContextService;

public class SecurityHeaderConfigHome
{
	// Static variable pointed at the DAO instance
    private static ISecurityHeaderConfigItemDAO _dao = SpringContextService.getBean( "securityHeaderConfigItemDAO" );
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private SecurityHeaderConfigHome( )
    {
    }
    
    /**
     * Creation of an instance of security header config item
     *
     * @param securityHeaderConfigItem
     *            The instance of the security header config item which contains the informations to store
     * @return The instance of security header config item which has been created with its primary key.
     */
    public static SecurityHeaderConfigItem create( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
        _dao.insert( securityHeaderConfigItem );

        return securityHeaderConfigItem;
    }
    
    /**
     * Update of the instance of security header config item which is specified in parameter
     *
     * @param securityHeaderConfigItem
     *            The instance of the security header config item which contains the informations to update
     * @return The instance of security header config item which has been created with its primary key.
     */
    public static void update( SecurityHeaderConfigItem securityHeaderConfigItem )
    {
        _dao.store( securityHeaderConfigItem );
    }
    
    /**
     * Remove the security header config item whose identifier is specified in parameter
     *
     * @param nSecurityHeaderConfigItemId
     *            The security header config item Id
     */
    public static void remove( int nSecurityHeaderConfigItemId )
    {
        _dao.delete( nSecurityHeaderConfigItemId );
    }
    
    // /////////////////////////////////////////////////////////////////////////
    // Finders
    
    /**
     * Returns all security header config items having a specific url pattern of a security header 
     *
     * @param securityHeaderId
     *            The security header id
     * @param urlPattern
     *            The URL pattern
     * @return the list which contains the security header config items having a specific url pattern of a security header
     */
    public static List<SecurityHeaderConfigItem> find( int securityHeaderId, String urlPattern )
    {
    	return _dao.find( securityHeaderId, urlPattern );
    }
    
    /**
     * Returns all security header config items of a security header
     *
     * @param securityHeaderId
     *            The security header id
     * @return the list which contains the security header config items of a security header
     */
    public static List<SecurityHeaderConfigItem> findBySecurityHeaderId( int securityHeaderId )
    {
    	return _dao.findBySecurityHeaderId( securityHeaderId );
    }
    
    /**
     * Returns an instance of a security header config item whose identifier is specified in parameter
     *
     * @param nKey
     *            The SecurityHeaderConfigItem primary key
     * @return an instance of SecurityHeaderConfigItem
     */
    public static SecurityHeaderConfigItem findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }
}