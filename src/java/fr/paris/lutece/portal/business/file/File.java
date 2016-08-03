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
package fr.paris.lutece.portal.business.file;

import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 *
 * class File
 *
 */
public class File implements Serializable
{
    private static final long serialVersionUID = -2014847000871390972L;
    private int _nIdFile;
    private PhysicalFile _physicalFile;
    private String _strTitle;
    private int _nSize;
    private String _strExtension;
    private String _strMimeType;
    private Timestamp _dateCreation;

    /**
     *
     * @return the id of the file
     */
    public int getIdFile(  )
    {
        return _nIdFile;
    }

    /**
     * set the id of the file
     * @param idFile id of the file
     */
    public void setIdFile( int idFile )
    {
        _nIdFile = idFile;
    }

    /**
     *
     * @return the title of the file
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * set the title of the file
     * @param title the title of the file
     */
    public void setTitle( String title )
    {
        _strTitle = title;
    }

    /**
     *
     * @return the size of the file
     */
    public int getSize(  )
    {
        return _nSize;
    }

    /**
     * set the size of the file
     * @param size the size of the file
     */
    public void setSize( int size )
    {
        _nSize = size;
    }

    /**
     *
     * @return the extension of the file
     */
    public String getExtension(  )
    {
        return _strExtension;
    }

    /**
     * set the extension of the file
     * @param extension the title of the file
     */
    public void setExtension( String extension )
    {
        _strExtension = extension;
    }

    /**
     *
     * @return the extension of the file
     */
    public String getMimeType(  )
    {
        return _strMimeType;
    }

    /**
     * set the mime type of the file
     * @param mimeType the mime type of the file
     */
    public void setMimeType( String mimeType )
    {
        _strMimeType = mimeType;
    }

    /**
     *
     * @return the PhysicalFile associate to the file
     */
    public PhysicalFile getPhysicalFile(  )
    {
        return _physicalFile;
    }

    /**
     * set the PhysicalFile associate to the file
     * @param file PhysicalFile
     */
    public void setPhysicalFile( PhysicalFile file )
    {
        _physicalFile = file;
    }
    
    /**
     *
     * @return the creation date of the file
     */
    public Timestamp getDateCreation(  )
    {
        return _dateCreation;
    }

    /**
     * set the creation date of the file
     * @param dateCreation the creation date of the file
     */
    public void setDateCreation( Timestamp dateCreation )
    {
        _dateCreation = dateCreation;
    }
    
    
}
