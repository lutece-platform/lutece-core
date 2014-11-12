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
package fr.paris.lutece.util;


/**
 * This class is the representation of a ReferenceItem composed of a code and a name. It also provides the methods to
 * access to those attributes.
 */
public class ReferenceItem
{
    private String _strCode;
    private String _strName;
    private boolean _bChecked;

    /**
     * Returns the code of this ReferenceItem object as a String
     *
     * @return The code
     */
    public String getCode(  )
    {
        return _strCode;
    }

    /**
     * Sets the code of this ReferenceItem with the String specified in parameter
     *
     * @param strCode The new String value of the code
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    /**
     * Returns the name of this ReferenceItem object as a String
     *
     * @return the name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of this ReferenceItem object with the value specified in parameter
     *
     * @param strName new String value of the name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns weither the item is checked or not
     * @return true if checked, otherwise false
     * @since 2.0
     */
    public boolean isChecked(  )
    {
        return _bChecked;
    }

    /**
     * Sets checked
     * @param bChecked The check status
     * @since 2.0
     */
    public void setChecked( boolean bChecked )
    {
        _bChecked = bChecked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(  )
    {
        return "RefItem[Name=" + this.getName(  ) + ", Code=" + this.getCode(  ) + ", Checked=" + this.isChecked(  ) +
        "]";
    }
}
