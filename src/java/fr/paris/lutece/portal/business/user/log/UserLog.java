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
package fr.paris.lutece.portal.business.user.log;

import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * This class represents business object UserLog
 */
public class UserLog
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final int LOGIN_OK = 1;
    public static final int LOGIN_DENIED = 0;
    private static final String PROPERTY_PASSWORD_LENGHT_MAX = "password.lenght.max";
    private String _strAccessCode;
    private String _strIpAddress;
    private java.sql.Timestamp _dateLogin;
    private int _nLoginStatus;

    /**
     * Returns the access code of this user log.
     *
     * @return the user access code
     */
    public String getAccessCode(  )
    {
        return _strAccessCode;
    }

    /**
     * Sets the access code of the user log to the specified string.
     *
     * @param strAccessCode the new access code
     */
    public void setAccessCode( String strAccessCode )
    {
        int nPasswordMaxLenght = Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_PASSWORD_LENGHT_MAX ) );
        String strModifyAccessCode = strAccessCode;

        if ( strModifyAccessCode.length(  ) > nPasswordMaxLenght )
        {
            strModifyAccessCode = strModifyAccessCode.substring( 0, nPasswordMaxLenght );
        }

        _strAccessCode = strModifyAccessCode;
    }

    /**
     * Returns the ip address of this user log.
     *
     * @return the user ip address
     */
    public String getIpAddress(  )
    {
        return _strIpAddress;
    }

    /**
     * Sets the ip address of the user log to the specified string.
     *
     * @param strIpAddress the new ip address
     */
    public void setIpAddress( String strIpAddress )
    {
        _strIpAddress = strIpAddress;
    }

    /**
     * Returns the login date of this user log.
     *
     * @return the user date of login
     */
    public java.sql.Timestamp getDateLogin(  )
    {
        return _dateLogin;
    }

    /**
     * Sets the login date of the user log to the specified timestamp.
     *
     * @param dateLogin the new date of login
     */
    public void setDateLogin( java.sql.Timestamp dateLogin )
    {
        _dateLogin = dateLogin;
    }

    /**
     * Returns the login status of this user log.
     *
     * @return the user status of login
     */
    public int getLoginStatus(  )
    {
        return _nLoginStatus;
    }

    /**
     * Sets the login status of the user log to the specified integer.
     *
     * @param nLoginStatus the new status of login
     */
    public void setLoginStatus( int nLoginStatus )
    {
        _nLoginStatus = nLoginStatus;
    }
}
