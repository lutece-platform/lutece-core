/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.portal.business.template;

import java.io.Serializable;

/**
 * This is the business class for the object AutoInclude
 */ 
public class AutoInclude implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String _strFilePath;
    private String _strOwner;

    /**
     * Constructor
     */
    public AutoInclude()
    {
    }
    
    /**
     * Constructor
     * @param strFilePath The autoinclude path 
     */
    public AutoInclude( String strFilePath )
    {
        _strFilePath = strFilePath;
    }
    
    /**
     * Returns the FilePath
     * @return The FilePath
     */
    public String getFilePath( )
    {
        return _strFilePath;
    }

    /**
     * Sets the FilePath
     * @param strFilePath The FilePath
     */ 
    public void setFilePath( String strFilePath )
    {
        _strFilePath = strFilePath;
    }

    /**
     * Returns the Owner
     * @return The Owner
     */
    public String getOwner( )
    {
        return _strOwner;
    }

    /**
     * Sets the Owner
     * @param strOwner The Owner
     */ 
    public void setOwner( String strOwner )
    {
        _strOwner = strOwner;
    }

}
