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
package fr.paris.lutece.portal.service.prefs;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;

/**
 * AdminUser Preferences Service
 */
public final class AdminUserPreferencesService
{
    //private static final String BEAN_USER_PREFERENCE_SERVICE = "adminUserPreferencesService";
    private static IUserPreferencesService _singleton;

    /** private constructor */
    private AdminUserPreferencesService( )
    {
    }

    /**
     * Return the unique instance
     *
     * @return The instance
     */
    public static synchronized IUserPreferencesService instance( )
    {
        if ( _singleton == null )
        {
            // BaseUserPreferencesServiceImpl is the admin-scoped service : it uses the @AdminUserPreferences DAO,
            // backed by the core_admin_user_preferences table. PortalUserPreferenceServiceImpl extends it (and is
            // backed by core_user_preferences instead), so a plain select( BaseUserPreferencesServiceImpl.class ) is
            // ambiguous. Resolve the exact base bean, never the subclass.
            Instance<BaseUserPreferencesServiceImpl> instances = CDI.current( ).select( BaseUserPreferencesServiceImpl.class );

            for ( Instance.Handle<BaseUserPreferencesServiceImpl> handle : instances.handles( ) )
            {
                if ( BaseUserPreferencesServiceImpl.class.equals( handle.getBean( ).getBeanClass( ) ) )
                {
                    _singleton = handle.get( );
                    break;
                }
            }
        }

        return _singleton;
    }
}
