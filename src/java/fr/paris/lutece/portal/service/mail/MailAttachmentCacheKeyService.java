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
package fr.paris.lutece.portal.service.mail;

import fr.paris.lutece.portal.service.cache.ICacheKeyService;
import fr.paris.lutece.portal.service.security.LuteceUser;

import java.util.List;
import java.util.Map;


/**
 * MailAttachmentCacheKeyService
 *
 */
public class MailAttachmentCacheKeyService implements ICacheKeyService
{
    public static final String MARK_URL = "url";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey( Map<String, String> mapParams, int nMode, LuteceUser user )
    {
        StringBuilder sbKey = new StringBuilder(  );

        if ( mapParams.containsKey( MARK_URL ) )
        {
            sbKey.append( "[url:" ).append( mapParams.get( MARK_URL ).replaceAll( "/", "" ) ).append( "]" );
        }

        return sbKey.toString(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAllowedParametersList( List<String> arg0 )
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIgnoredParametersList( List<String> arg0 )
    {
    }
}
