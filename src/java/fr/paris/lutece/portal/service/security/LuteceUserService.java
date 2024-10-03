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
package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.portal.service.util.AppLogService;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Service to access user management functionalities. This class provide
 *
 */
public final class LuteceUserService
{
	private static LuteceUserCacheService _luteceUserCacheService= CDI.current( ).select( LuteceUserCacheService.class ).get( );

    /**
     * Private constructor
     */
    private LuteceUserService( )
    {
        // Do nothing
    }

    /**
     * Get the LuteceUser with the given name
     * 
     * @param strName
     *            The name of the LuteceUser to get
     * @return The LuteceUser, or null if no LuteceUser has the given name
     */
    public static LuteceUser getLuteceUserFromName( String strName )
    {
        // TODO : add separation between user management implementations
        LuteceUser user = _luteceUserCacheService.get( strName );

        if ( user != null )
        {
            try
            {
                return (LuteceUser) user.clone( );
            }
            catch( CloneNotSupportedException e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }

        for ( ILuteceUserProviderService luteceUserProviderService : CDI.current().select(ILuteceUserProviderService.class).stream().toList( ) )
        {
            user = luteceUserProviderService.getLuteceUserFromName( strName );

            if ( user != null )
            {
                if ( luteceUserProviderService.canUsersBeCached( ) )
                {
                    try
                    {
                        _luteceUserCacheService.put( strName, (LuteceUser) user.clone( ) );
                    }
                    catch( CloneNotSupportedException e )
                    {
                        AppLogService.error( e.getMessage( ), e );
                    }
                }

                return user;
            }
        }

        return null;
    }

    /**
     * Declares that a user was modified. This method must be used every time a user is updated to keep the cache up to date. Note that if the user name of a
     * user is changed, this method does not need to be called
     * 
     * @param strUserName
     *            The name of the updated user.
     */
    public static void userAttributesChanged( String strUserName )
    {
    	_luteceUserCacheService.remove( strUserName );
    }
}
