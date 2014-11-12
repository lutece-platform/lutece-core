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
package fr.paris.lutece.portal.service.rbac;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.Localizable;

import java.util.Locale;


/**
 * This class defines the permission element for resource control
 */
public class Permission implements Localizable
{
    private String _strPermissionKey;
    private String _strPermissionTitle;
    private Locale _locale;

    /**
     * Implements Localizable
     * @param locale The current locale
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * @return Returns the Permission Key.
     */
    public String getPermissionKey(  )
    {
        return _strPermissionKey;
    }

    /**
     * Sets the permission
     * @param strPermissionKey The Permission Key to set.
     */
    public void setPermissionKey( String strPermissionKey )
    {
        this._strPermissionKey = strPermissionKey;
    }

    /**
     * @return Returns the Permission Title.
     */
    public String getPermissionTitle(  )
    {
        return I18nService.getLocalizedString( _strPermissionTitle, _locale );
    }

    /**
     * Sets the permission title
     * @param strPermissionTitle The Permission Title to set.
     */
    public void setPermissionTitleKey( String strPermissionTitle )
    {
        this._strPermissionTitle = strPermissionTitle;
    }
}
