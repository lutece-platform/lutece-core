/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

package fr.paris.lutece.portal.service.rbac;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.business.rbac.RBACRole;

/**
 * User
 */
public interface User 
{
    /**
     * Gets RBAC roles 
     * @return The roles 
     */
     Map<String, RBACRole> getRBACRoles( );
    
     /**
      * Gets the access code of this user
      * 
      * @return the access code of this user
      */
     String getAccessCode();
     
     /**
      * Gets the email of this user.
      * 
      * @return The email
      */
     String getEmail( );
     
     /**
      * Gets the last name of this user.
      *
      * @return the user last name
      */
     String getLastName( );
     
     /**
      * Gets the first name of this user.
      *
      * @return the user first name
      */
      String getFirstName( );
    
    /**
     * Gets a user's info
     * 
     * @param key
     *            The info key
     * @param <X> the value's type stored in the user map info
     * @return The info value
     */
     <X extends Object>X getUserInfo( String key );
    
     /**
      * Get the list of workgroups for this user
      * @return a list of  workgroups for this user
      */
     List<String> getUserWorkgroups();
     /**
      * Gets the realm of this user
      * 
      * @return the realm of this user
      */
     String getRealm( );
     
}
