/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.business.workflow;


/**
 *
 * Icon
 *
 */
public class Icon implements IReferenceItem
{
    private int _nId;
    private String _strName;
    private byte[] _byValue;
    private String _strMimeType;
    private int _nWidth;
    private int _nHeight;

    /**
    *
    * @return the id of the icon
    */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * set the id of the icon
     * @param idIcon the id of the icon
     */
    public void setId( int idIcon )
    {
        _nId = idIcon;
    }

    /**
     * Returns the icon name
     *
     * @return the icon name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Set the icon name
     *
     * @param strName the icon name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * get the icon file value
     * @return the icon file value
     */
    public byte[] getValue(  )
    {
        return _byValue;
    }

    /**
     * set the icon file value
     * @param value the file value
     */
    public void setValue( byte[] value )
    {
        _byValue = value;
    }

    /**
     * the icon mime type
     * @return the icon mime type
     */
    public String getMimeType(  )
    {
        return _strMimeType;
    }

    /**
     * set the icon mime type
     * @param mimeType the icon mime type
     */
    public void setMimeType( String mimeType )
    {
        _strMimeType = mimeType;
    }

    /**
         * @return the icon height
         */
    public int getHeight(  )
    {
        return _nHeight;
    }

    /**
     * @param height the  icon height
     */
    public void setHeight( int height )
    {
        _nHeight = height;
    }

    /**
     * @return the icon width
     */
    public int getWidth(  )
    {
        return _nWidth;
    }

    /**
     * @param width the icon width
     */
    public void setWidth( int width )
    {
        _nWidth = width;
    }
}
