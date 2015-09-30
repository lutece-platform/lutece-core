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
package fr.paris.lutece.portal.service.prefs;

import java.util.List;


/**
 * User Preferences Interface
 */
public interface IUserPreferencesService
{
    /**
     * Get a preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param strDefault The default value
     * @return The preference's value
     */
    String get( String strUserId, String strKey, String strDefault );

    /**
     * Get an integer preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param nDefault The default value
     * @return The preference's value
     */
    int getInt( String strUserId, String strKey, int nDefault );

    /**
     * Get a boolean preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param bDefault The default value
     * @return The preference's value
     */
    boolean getBoolean( String strUserId, String strKey, boolean bDefault );

    /**
     * Get the list of users associated with a key and a value
     * @param strKey The preference's key
     * @param strValue The preference's value
     * @return The list of user id associated with a key and a value, or an
     *         empty list if no user id is associated with the given parameters.
     */
    List<String> getUsers( String strKey, String strValue );

    /**
     * Put a preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param strValue The value
     */
    void put( String strUserId, String strKey, String strValue );

    /**
     * Put an integer preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param nValue The value
     */
    void putInt( String strUserId, String strKey, int nValue );

    /**
     * Put a boolean preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     * @param bValue The value
     */
    void putBoolean( String strUserId, String strKey, boolean bValue );

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
    void clear( String strUserId );

    /**
     * Clear a preference for a given user
     * @param strUserId The user's ID
     * @param strKey The preference's key
     */
    void clearKey( String strUserId, String strKey );

    /**
     * Clear all preferences with a given prefix for a given user
     * @param strUserId The user's ID
     * @param strPrefix The keys prefix
     */
    void clearKeyPrefix( String strUserId, String strPrefix );

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
     * @param strValue The Pref Value
     * @return 1 if exists otherwise false
     */
    boolean existsValueForKey( String strKey,String strValue );
}
