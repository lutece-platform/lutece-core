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
package fr.paris.lutece.portal.business.stylesheet;


/**
 * This class represents business StyleSheet objects
 */
public class StyleSheet
{
    private static final String EMPTY_STRING = "";
    private int _nId;
    private int _nStyleId;
    private int _nModeId;
    private String _strDescription;
    private String _strFile;
    private byte[] _strSource;
    private String _strPath;

    /**
     * Returns the identifier of this StyleSheet.
     *
     * @return this StyleSheet identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets this identifier to the int value specified in parameter.
     *
     * @param nId The new identifier value
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the identifier of the style of this StyleSheet.
     *
     * @return the Style identifier of this StyleSheet
     */
    public int getStyleId(  )
    {
        return _nStyleId;
    }

    /**
     * Sets the Style identifier of this StyleSheet to the int value specified in parameter.
     *
     * @param nStyleId The new identifier value
     */
    public void setStyleId( int nStyleId )
    {
        _nStyleId = nStyleId;
    }

    /**
     * Returns the identifier of the mode of this StyleSheet.
     *
     * @return the Mode identifier of this StyleSheet
     */
    public int getModeId(  )
    {
        return _nModeId;
    }

    /**
     * Sets the Mode identifier of this StyleSheet to the int value specified in parameter.
     *
     * @param nModeId The new identifier value
     */
    public void setModeId( int nModeId )
    {
        _nModeId = nModeId;
    }

    /**
     * Returns the description of this StyleSheet.
     *
     * @return the description of this StyleSheet
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description of this StyleSheet to the String value specified in parameter.
     *
     * @param strDescription The new description value
     */
    public void setDescription( String strDescription )
    {
        _strDescription = ( strDescription == null ) ? EMPTY_STRING : strDescription;
    }

    /**
     * Returns the file name of this StyleSheet.
     *
     * @return the file name of this StyleSheet
     */
    public String getFile(  )
    {
        return _strFile;
    }

    /**
     * Sets the file name of this StyleSheet to the String value specified in parameter, "" if null.
     *
     * @param strFile The new file name value
     */
    public void setFile( String strFile )
    {
        _strFile = ( strFile == null ) ? EMPTY_STRING : strFile;
    }

    /**
     * Returns the file source of this StyleSheet.
     *
     * @return the file source of this StyleSheet
     */
    public byte[] getSource(  )
    {
        return _strSource;
    }

    /**
     * Sets the file source of this StyleSheet to the String value specified in parameter.
     *
     * @param strSource The new file source value
     */
    public void setSource( byte[] strSource )
    {
        _strSource = strSource;
    }

    /**
     * Sets the path of the stylesheet according to their mode
     *
     * @param strPath the path
     */
    public void setPath( String strPath )
    {
        _strPath = strPath;
    }

    /**
     * Returns the path of the stylesheet according to their mode
     *
     * @return path
     */
    public String getPath(  )
    {
        return _strPath;
    }
}
