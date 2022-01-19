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
package fr.paris.lutece.portal.business.rbac;

import fr.paris.lutece.api.user.UserRole;
import java.io.Serializable;

/**
 * This class describes a role used by the administration
 */
public class RBACRole implements Serializable, UserRole
{
    private static final long serialVersionUID = 6596841223502982731L;
    private String _strKey;
    private String _strDescription;

    /**
     * Constructor
     */
    public RBACRole( )
    {
    }

    /**
     * Constructor
     * 
     * @param strKey
     *            The Key
     * @param strDescription
     *            The description
     */
    public RBACRole( String strKey, String strDescription )
    {
        _strKey = strKey;
        _strDescription = strDescription;
    }

    /**
     * Returns the role Key
     * 
     * @return The role Key.
     */
    @Override
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * Sets the role key
     * 
     * @param strKey
     *            The Key to set.
     */
    @Override
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * Returns the role description.
     * 
     * @return The description.
     */
    @Override
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the role description
     * 
     * @param strDescription
     *            The description to set.
     */
    @Override
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }
}
