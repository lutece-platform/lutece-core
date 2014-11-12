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
package fr.paris.lutece.portal.web.system;

import java.util.Date;


/**
 * Utility class for SystemJspBean
 */
public class SystemFile
{
    private String _strName;
    private String _strDescription;
    private String _strDirectory;
    private Date _date;
    private int _size;

    /**
     * Gets the name
     * @return The name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name
     * @param strName The name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Gets the description
     * @return The description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description
     * @param strDescription The description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Gets the directory
     * @return The directory
     */
    public String getDirectory(  )
    {
        return _strDirectory;
    }

    /**
     * Sets the directory
     * @param strDirectory The directory
     */
    public void setDirectory( String strDirectory )
    {
        _strDirectory = strDirectory;
    }

    /**
     * Gets the date
     * @return The date
     */
    public Date getDate(  )
    {
        return _date;
    }

    /**
     * Sets the date
     * @param date The date
     */
    public void setDate( Date date )
    {
        _date = date;
    }

    /**
     * Gets the size
     * @return The size
     */
    public int getSize(  )
    {
        return _size;
    }

    /**
     * Sets the size
     * @param size The size
     */
    public void setSize( int size )
    {
        _size = size;
    }
}
