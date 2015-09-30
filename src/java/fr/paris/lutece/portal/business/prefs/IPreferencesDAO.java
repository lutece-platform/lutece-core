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
package fr.paris.lutece.portal.business.prefs;

import java.util.List;


/**
 * Preferences DAO interface
 */
public interface IPreferencesDAO
{
    /**
     * Get a preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param strDefault The default value
     * @return The preference's value
     */
    String load( String strUserId, String strKey, String strDefault );

    /**
     * Get the list of user id associated with the given key and value
     * @param strKey The preference's key
     * @param strValue The preference's value
     * @return The list of user id associated with the given key and value. If
     *         there is no user id associated with the parameters, then an empty
     *         list is returned
     */
    List<String> getUserId( String strKey, String strValue );

    /**
     * Store a preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param strValue The value
     */
    void store( String strUserId, String strKey, String strValue );

    /**
     * Get all preference keys for a given user
     * @param strUserId The user's ID
     * @return The keys
     */
    List<String> keys( String strUserId );

    /**
     * Clear all preferences for a given user
     * @param strUserId The user's ID
     */
    void remove( String strUserId );

    /**
     * Clear all preferences for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     */
    void removeKey( String strUserId, String strKey );

    /**
     * Clear all preferences for a given user
     * @param strUserId The user's ID
     * @param strKeyPrefix The key prefix
     */
    void removeKeyPrefix( String strUserId, String strKeyPrefix );

    /**
     * Cheks if a preference key exists
     * @param strUserId The User ID
     * @param strKey The Pref key
     * @return 1 if exists otherwise false
     */
    boolean existsKey( String strUserId, String strKey );
    
    /**
     * Checks if a value is already used for a preference given key 
     * @param strKey The Pref key
     * @param strValue The Pref value
     * @return 1 if exists otherwise false
     */
    boolean existsValueForKey( String strKey,String strValue );
    
    
}
