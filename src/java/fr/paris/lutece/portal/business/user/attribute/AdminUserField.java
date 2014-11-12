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
package fr.paris.lutece.portal.business.user.attribute;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.user.AdminUser;


/**
 *
 * AdminUserField
 *
 */
public class AdminUserField
{
    private AdminUser _user;
    private AttributeField _attributeField;
    private IAttribute _attribute;
    private String _value;
    private int _nIdUserField;
    private File _file;

    /**
     * Get user
     * @return user
     */
    public AdminUser getUser(  )
    {
        return _user;
    }

    /**
     * Set user
     * @param user user
     */
    public void setUser( AdminUser user )
    {
        _user = user;
    }

    /**
     * Get attribute field
     * @return attribute field
     */
    public AttributeField getAttributeField(  )
    {
        return _attributeField;
    }

    /**
     * Set attribute field
     * @param attributeField attribute field
     */
    public void setAttributeField( AttributeField attributeField )
    {
        _attributeField = attributeField;
    }

    /**
     * Get attribute
     * @return attribute
     */
    public IAttribute getAttribute(  )
    {
        return _attribute;
    }

    /**
     * Set attribute
     * @param attribute attribute
     */
    public void setAttribute( IAttribute attribute )
    {
        _attribute = attribute;
    }

    /**
     * Get value
     * @return value
     */
    public String getValue(  )
    {
        return _value;
    }

    /**
     * Set value
     * @param value value
     */
    public void setValue( String value )
    {
        _value = value;
    }

    /**
     * Get Id user field
     * @return id user field
     */
    public int getIdUserField(  )
    {
        return _nIdUserField;
    }

    /**
     * Set id user field
     * @param nIdUserField id user field
     */
    public void setIdUserField( int nIdUserField )
    {
        _nIdUserField = nIdUserField;
    }

    /**
     * Get file
     * @return file
     */
    public File getFile(  )
    {
        return _file;
    }

    /**
     * Set file
     * @param file file
     */
    public void setFile( File file )
    {
        _file = file;
    }
}
