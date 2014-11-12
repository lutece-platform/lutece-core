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
package fr.paris.lutece.portal.business.user.authentication;

import fr.paris.lutece.portal.business.user.AdminUser;

import java.sql.Date;


/**
 * Admin user implementation for database authentication module
 */
public class LuteceDefaultAdminUser extends AdminUser
{
    private String _strPassword;
    private Date _dateValidityPassword;
    private String _strLastPassword;

    /**
     * Constructor
     */
    public LuteceDefaultAdminUser(  )
    {
        super(  );
    }

    /**
     * Constructor
     * @param strUserName The user
     * @param authenticationService The authentication service
     */
    public LuteceDefaultAdminUser( String strUserName, AdminAuthentication authenticationService )
    {
        super( strUserName, authenticationService );
    }

    /**
     * Returns the Password
     * @return The Password.
     */
    public String getPassword(  )
    {
        return _strPassword;
    }

    /**
     * Sets the password
     * @param strPassword The Password
     */
    public void setPassword( String strPassword )
    {
        _strPassword = strPassword;
    }

    /**
     * Returns the password validity date
     * @return The password validity date
     */
    public Date getDateValidityPassword(  )
    {
        return _dateValidityPassword;
    }

    /**
     * Sets the password validity date
     * @param dateValidityPassword The password validity date
     */
    public void setDateValidityPassword( Date dateValidityPassword )
    {
        _dateValidityPassword = dateValidityPassword;
    }

    /**
     * Returns the Last Password.
     * @return The Last Password.
     */
    public String getLastPassword(  )
    {
        return _strLastPassword;
    }

    /**
     * Sets the Last Password.
     * @param strLastPassword The Last Password.
     */
    public void setLastPassword( String strLastPassword )
    {
        _strLastPassword = strLastPassword;
    }
}
