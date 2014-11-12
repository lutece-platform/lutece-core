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
package fr.paris.lutece.portal.service.cache;

import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Default cache key service
 *
 */
public class DefaultCacheKeyService implements ICacheKeyService
{
    private List<String> _listAllowedParameters;
    private List<String> _listIgnoredParameters;

    /**
     * Calculate the cache key
     * @param mapParams A map of key/value
     * @param nMode The mode
     * @param user The LuteceUser
     * @return The calculated key
     */
    public String getKey( Map<String, String> mapParams, int nMode, LuteceUser user )
    {
        StringBuilder sbKey = new StringBuilder(  );

        for ( Entry<String, String> entry : mapParams.entrySet(  ) )
        {
            String strHtKey = entry.getKey(  );

            if ( ( ( _listAllowedParameters == null ) || _listAllowedParameters.contains( strHtKey ) ) &&
                    ( ( _listIgnoredParameters == null ) || ( !_listIgnoredParameters.contains( strHtKey ) ) ) )
            {
                sbKey.append( "[" ).append( strHtKey ).append( ":" ).append( entry.getValue(  ) ).append( "]" );
            }
        }

        String strUserName = ( user != null ) ? user.getName(  ) : "-";

        sbKey.append( "[m:" ).append( nMode ).append( "]" );
        sbKey.append( "[user:" ).append( strUserName ).append( "]" );

        return sbKey.toString(  );
    }

    /**
     * {@inheritDoc}
     */
    public void setAllowedParametersList( List<String> list )
    {
        _listAllowedParameters = list;
    }

    /**
     * {@inheritDoc}
     */
    public void setIgnoredParametersList( List<String> list )
    {
        _listIgnoredParameters = list;
    }
}
