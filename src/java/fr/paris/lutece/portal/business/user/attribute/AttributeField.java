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


/**
 *
 * AttributeField
 *
 */
public class AttributeField
{
    private int _nIdField;
    private IAttribute _attribute;
    private String _strTitle;
    private String _strValue;
    private boolean _bDefaultValue;
    private int _nPosition;
    private int _nHeight;
    private int _nWidth;
    private int _nMaxSizeEnter;
    private boolean _bMultiple;

    /**
     * Set ID field
     * @param nIdField ID Field
     */
    public void setIdField( int nIdField )
    {
        _nIdField = nIdField;
    }

    /**
     * Get ID Field
     * @return ID Field
     */
    public int getIdField(  )
    {
        return _nIdField;
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
     * Get attribute
     * @return attribute
     */
    public IAttribute getAttribute(  )
    {
        return _attribute;
    }

    /**
     *
     * @return the title of the field
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * set the title of the field
     * @param title the title of the field
     */
    public void setTitle( String title )
    {
        _strTitle = title;
    }

    /**
     *
     * @return the value of the field
     */
    public String getValue(  )
    {
        return _strValue;
    }

    /**
     * set the value of the field
     * @param value the value of the field
     */
    public void setValue( String value )
    {
        _strValue = value;
    }

    /**
     *
     * @return true if the field is a default field of the entry
     */
    public boolean isDefaultValue(  )
    {
        return _bDefaultValue;
    }

    /**
     * set true if the field is a default field of the entry
     * @param defaultValue true if the field is a default field of the entry
     */
    public void setDefaultValue( boolean defaultValue )
    {
        _bDefaultValue = defaultValue;
    }

    /**
     *
     * @return the position of the field in the list of the entry's fields
     */
    public int getPosition(  )
    {
        return _nPosition;
    }

    /**
     * set the position of the field in the list of the entry's fields
     * @param position the position of the field in the list of fields
     */
    public void setPosition( int position )
    {
        _nPosition = position;
    }

    /**
     * Get height
     * @return height
     */
    public int getHeight(  )
    {
        return _nHeight;
    }

    /**
     * Set height
     * @param nHeight Height
     */
    public void setHeight( int nHeight )
    {
        _nHeight = nHeight;
    }

    /**
     * Get width
     * @return width
     */
    public int getWidth(  )
    {
        return _nWidth;
    }

    /**
     * Set width
     * @param nWidth width
     */
    public void setWidth( int nWidth )
    {
        _nWidth = nWidth;
    }

    /**
     * Get max size enter
     * @return max size enter
     */
    public int getMaxSizeEnter(  )
    {
        return _nMaxSizeEnter;
    }

    /**
     * Set max size enter
     * @param nMaxSizeEnter max size enter
     */
    public void setMaxSizeEnter( int nMaxSizeEnter )
    {
        _nMaxSizeEnter = nMaxSizeEnter;
    }

    /**
     * Check if the attribute is multiple
     * @return true if it is multiple, false otherwise
     */
    public boolean isMultiple(  )
    {
        return _bMultiple;
    }

    /**
     * Set the multiple
     * @param bMultiple mutiple
     */
    public void setMultiple( boolean bMultiple )
    {
        _bMultiple = bMultiple;
    }
}
