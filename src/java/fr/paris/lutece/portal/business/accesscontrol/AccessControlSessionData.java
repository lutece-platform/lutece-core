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
package fr.paris.lutece.portal.business.accesscontrol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccessControlSessionData implements Serializable
{
    private static final long serialVersionUID = 6559917546035608330L;

    private static final String SESSION_KEY = "access_control_data";

    private final int _nIdResource;
    private final String _strTypeResource;
    private String _strReturnQueryString;
    private boolean _bAccessControlResult;
    private Map<Integer, Serializable> _persistentData = new HashMap<>( );

    
    /**
     * Constructor
     * @param nIdResource
     * @param strTypeResource
     */
    public AccessControlSessionData( int nIdResource, String strTypeResource )
    {
        _nIdResource = nIdResource;
        _strTypeResource = strTypeResource;
    }

    /**
     * @return the strReturnQueryString
     */
    public String getReturnQueryString( )
    {
        return _strReturnQueryString;
    }

    /**
     * @param strReturnQueryString
     *            the strReturnQueryString to set
     */
    public void setReturnQueryString( String strReturnQueryString )
    {
        _strReturnQueryString = strReturnQueryString;
    }

    /**
     * @return the bAccessControlResult
     */
    public boolean isAccessControlResult( )
    {
        return _bAccessControlResult;
    }

    /**
     * @param bAccessControlResult
     *            the bAccessControlResult to set
     */
    public void setAccessControlResult( boolean bAccessControlResult )
    {
        _bAccessControlResult = bAccessControlResult;
    }
    
    /**
     * Add param to persistent data map.
     * @param key the id of the controller that handles the persistent data
     * @param value
     */
    public void addPersistentParam( int key, Serializable value )
    {
        _persistentData.put( key, value );
    }
    
    /**
     * Get persistent data
     * @return
     */
    public Map<Integer, Serializable> getPersistentData( )
    {
        return _persistentData;
    }

    /**
     * @return the nIdResource
     */
    public int getIdResource( )
    {
        return _nIdResource;
    }

    /**
     * @return the strTypeResource
     */
    public String getTypeResource( )
    {
        return _strTypeResource;
    }
    
    /**
     * Get the session key for this ession data
     * @return
     */
    public String getSessionKey( )
    {
        return getSessionKey( _nIdResource, _strTypeResource );
    }
    
    /**
     * Get the session key for the given parameters
     * @param nIdResource
     * @param strResourceType
     * @return
     */
    public static final String getSessionKey( int nIdResource, String strResourceType )
    {
        return SESSION_KEY + "_" + nIdResource + "_" + strResourceType;
    }
}
