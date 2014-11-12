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

import fr.paris.lutece.portal.service.cache.AbstractCacheableService;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Cache service for {@link BaseUserPreferencesServiceImpl}
 */
public class BaseUserPreferencesCacheService extends AbstractCacheableService
{
    private static final String CACHE_SERVICE_NAME = "BaseUserPreferencesCacheService";
    private static final String CONSTANT_UNDERSCORE = "_";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(  )
    {
        return CACHE_SERVICE_NAME;
    }

    /**
     * Get the key in the cache of a preference of a user
     * @param strUserId The id of the user
     * @param strKey The preference key
     * @return The key in the cache of the preference of the user
     */
    public String getCacheKey( String strUserId, String strKey )
    {
        return strUserId + CONSTANT_UNDERSCORE + strKey;
    }

    /**
     * Remove every values stored in cache for a given user
     * @param strUserId The user id to remove from cache
     */
    public void removeCacheValuesOfUser( String strUserId )
    {
        if ( StringUtils.isNotEmpty( strUserId ) )
        {
            String strPrefix = strUserId + CONSTANT_UNDERSCORE;
            List<String> listKeysToRemove = new ArrayList<String>(  );

            for ( String strKey : getKeys(  ) )
            {
                if ( strKey.startsWith( strPrefix ) )
                {
                    listKeysToRemove.add( strKey );
                }
            }

            for ( String strKey : listKeysToRemove )
            {
                removeKey( strKey );
            }
        }
    }
}
