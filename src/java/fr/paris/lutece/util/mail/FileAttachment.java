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
package fr.paris.lutece.util.mail;

import java.io.Serializable;


/**
 *
 * File Attachement Object
 *
 */
public class FileAttachment implements Serializable
{
    private static final long serialVersionUID = 9072808576472163734L;
    private String _strFileName; //file-name
    private byte[] _data; // data
    private String _strType; // Content-type

    /**
    * Creates a new FileAttachement object.
    *
    * @param fileName the file name
    * @param data The data
    * @param type The file Content-type
    */
    public FileAttachment( String fileName, byte[] data, String type )
    {
        _strFileName = fileName;
        _data = data;
        _strType = type;
    }

    /**
     *
     * @return the content of the fileAttachement object
     */
    public byte[] getData(  )
    {
        return _data;
    }

    /**
     * set the content of the fileAttachement object
     * @param data the content of the fileAttachement object
     */
    public void setData( byte[] data )
    {
        _data = data;
    }

    /**
     *
     * @return the file name
     */
    public String getFileName(  )
    {
        return _strFileName;
    }

    /**
     * set the file name
     * @param fileName  the file name
     */
    public void setFileName( String fileName )
    {
        _strFileName = fileName;
    }

    /**
     * return the file content-type
     * @return Content-type
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * set the file content-type
     * @param type the file content-type
     */
    public void setType( String type )
    {
        _strType = type;
    }
}
