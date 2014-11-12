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
package fr.paris.lutece.portal.business.style;


/**
 * This class reprsents business objects PageTemplate
 */
public class PageTemplate
{
    private int _nId;
    private String _strDescription;
    private String _strFile;
    private String _strPicture;

    /**
     * Returns the page template identifier
     *
     * @return the page template identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the page template identifier
     *
     * @param nId the page template identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the description of the page template
     *
     * @return page template description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description of the page template
     *
     * @param strDescription the page template description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the name of the html page which manages this type of page template
     *
     * @return the name of the html page
     */
    public String getFile(  )
    {
        return _strFile;
    }

    /**
     * Sets the name of the html page which manages this type of page template
     *
     * @param strFile The file name
     */
    public void setFile( String strFile )
    {
        _strFile = strFile;
    }

    /**
     * Returns the name of the picture associated to the page template type in the administration unit
     *
     * @return The name of the picture as a string
     */
    public String getPicture(  )
    {
        return _strPicture;
    }

    /**
     * Sets the name of the picture associated to the page template type in the administration unit
     *
     * @param strPicture The picture name
     */
    public void setPicture( String strPicture )
    {
        _strPicture = strPicture;
    }
}
