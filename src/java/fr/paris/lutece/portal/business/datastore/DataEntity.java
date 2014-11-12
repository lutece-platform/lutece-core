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
package fr.paris.lutece.portal.business.datastore;

import java.io.Serializable;


/**
 * This is the business class for the object Entity
 */
public class DataEntity implements Serializable
{
    private static final long serialVersionUID = -1891572928576705478L;

    // Variables declarations 
    private String _strKey;
    private String _strValue;

    /**
     * Constructor
     */
    public DataEntity(  )
    {
    }

    /**
     * Constructor
     * @param strKey The key
     * @param strValue The value
     */
    public DataEntity( String strKey, String strValue )
    {
        _strKey = strKey;
        _strValue = strValue;
    }

    /**
     * Returns the Key
     * @return The Key
     */
    public String getKey(  )
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
     * Returns the Value
     * @return The Value
     */
    public String getValue(  )
    {
        return _strValue;
    }

    /**
     * Sets the Value
     * @param strValue The Value
     */
    public void setValue( String strValue )
    {
        _strValue = strValue;
    }
}
