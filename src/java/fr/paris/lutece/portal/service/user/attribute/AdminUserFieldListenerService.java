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
package fr.paris.lutece.portal.service.user.attribute;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.attribute.AdminUserField;
import fr.paris.lutece.portal.business.user.attribute.AdminUserFieldListener;
import fr.paris.lutece.portal.business.user.attribute.SimpleAdminUserFieldListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * AdminUserFieldListenerService
 *
 */
public class AdminUserFieldListenerService
{
    private List<AdminUserFieldListener> _listRegisteredListeners = new ArrayList<AdminUserFieldListener>(  );

    /**
     * Register a new Removal listener
     * @param listener The listener to register
     */
    public void registerListener( AdminUserFieldListener listener )
    {
        _listRegisteredListeners.add( listener );
    }

    /**
     * Create user fields
     * @param user AdminUser
     * @param request HttpServletRequest
     * @param locale locale
     */
    public void doCreateUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
        for ( AdminUserFieldListener listener : _listRegisteredListeners )
        {
            listener.doCreateUserFields( user, request, locale );
        }
    }

    /**
     * Modify user fields
     * @param user AdminUser
     * @param request HttpServletRequest
     * @param locale locale
     * @param currentUser current user
     */
    public void doModifyUserFields( AdminUser user, HttpServletRequest request, Locale locale, AdminUser currentUser )
    {
        for ( AdminUserFieldListener listener : _listRegisteredListeners )
        {
            listener.doModifyUserFields( user, request, locale, currentUser );
        }
    }

    /**
     * Remove user fields
     * @param user Adminuser
     * @param request HttpServletRequest
     * @param locale locale
     */
    public void doRemoveUserFields( AdminUser user, HttpServletRequest request, Locale locale )
    {
        for ( AdminUserFieldListener listener : _listRegisteredListeners )
        {
            listener.doRemoveUserFields( user, request, locale );
        }
    }

    /**
     * Create user fields
     * @param user AdminUser
     * @param listUserFields The list of user fields to create
     * @param locale locale
     */
    public void doCreateUserFields( AdminUser user, List<AdminUserField> listUserFields, Locale locale )
    {
        for ( AdminUserFieldListener listener : _listRegisteredListeners )
        {
            if ( listener instanceof SimpleAdminUserFieldListener )
            {
                ( (SimpleAdminUserFieldListener) listener ).doCreateUserFields( user, listUserFields, locale );
            }
        }
    }

    /**
     * Modify user fields
     * @param user AdminUser
     * @param listUserFields The list of user fields to modify
     * @param locale locale
     * @param currentUser current user
     */
    public void doModifyUserFields( AdminUser user, List<AdminUserField> listUserFields, Locale locale,
        AdminUser currentUser )
    {
        for ( AdminUserFieldListener listener : _listRegisteredListeners )
        {
            if ( listener instanceof SimpleAdminUserFieldListener )
            {
                ( (SimpleAdminUserFieldListener) listener ).doModifyUserFields( user, listUserFields, locale,
                    currentUser );
            }
        }
    }

    /**
     * Remove user fields
     * @param user Adminuser
     * @param locale locale
     */
    public void doRemoveUserFields( AdminUser user, Locale locale )
    {
        for ( AdminUserFieldListener listener : _listRegisteredListeners )
        {
            if ( listener instanceof SimpleAdminUserFieldListener )
            {
                ( (SimpleAdminUserFieldListener) listener ).doRemoveUserFields( user, locale );
            }
        }
    }
}
