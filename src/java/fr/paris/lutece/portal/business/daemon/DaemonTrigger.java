/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
package fr.paris.lutece.portal.business.daemon;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This is the business class for the object DaemonTrigger
 */ 
public class DaemonTrigger implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @NotEmpty( message = "#i18n{portal.system.validation.daemontrigger.Key.notEmpty}" )
    @Size( max = 50 , message = "#i18n{portal.system.validation.daemontrigger.Key.size}" )
    private String _strKey;

    @NotEmpty( message = "#i18n{portal.system.validation.daemontrigger.Group.notEmpty}" )
    @Size( max = 50 , message = "#i18n{portal.system.validation.daemontrigger.Group.size}" )
    private String _strGroup;

    @NotEmpty( message = "#i18n{portal.system.validation.daemontrigger.CronExpression.notEmpty}" )
    @Size( max = 50 , message = "#i18n{portal.system.validation.daemontrigger.CronExpression.size}" )
    private String _strCronExpression;

    @NotEmpty( message = "#i18n{portal.system.validation.daemontrigger.DaemonKey.notEmpty}" )
    @Size( max = 50 , message = "#i18n{portal.system.validation.daemontrigger.DaemonKey.size}" )
    private String _strDaemonKey;

    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Key
     * @return The Key
     */
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * Sets the Key
     * @param strKey The Key
     */
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * Returns the Group
     * @return The Group
     */
    public String getGroup( )
    {
        return _strGroup;
    }

    /**
     * Sets the Group
     * @param strGroup The Group
     */
    public void setGroup( String strGroup )
    {
        _strGroup = strGroup;
    }

    /**
     * Returns the CronExpression
     * @return The CronExpression
     */
    public String getCronExpression( )
    {
        return _strCronExpression;
    }

    /**
     * Sets the CronExpression
     * @param strCronExpression The CronExpression
     */
    public void setCronExpression( String strCronExpression )
    {
        _strCronExpression = strCronExpression;
    }

    /**
     * Returns the DaemonKey
     * @return The DaemonKey
     */
    public String getDaemonKey( )
    {
        return _strDaemonKey;
    }

    /**
     * Sets the DaemonKey
     * @param strDaemonKey The DaemonKey
     */
    public void setDaemonKey( String strDaemonKey )
    {
        _strDaemonKey = strDaemonKey;
    }
}
