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
 * This class reprsents business objects Mode
 */
public class Mode
{
    private static final String EMPTY_STRING = "";
    private int _nId;
    private String _strDescription;
    private String _strPath;
    private String _strOutputXslPropertyMethod;
    private String _strOutputXslPropertyVersion;
    private String _strOutputXslPropertyMediaType;
    private String _strOutputXslPropertyIndent;
    private String _strOutputXslPropertyOmitXmlDeclaration;
    private String _strOutputXslPropertyEncoding;
    private String _strOutputXslPropertyStandalone;

    /**
     * Sets the mode identifier
     *
     * @param nId the mode identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the mode identifier
     *
     * @return the mode identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the name of the mode
     *
     * @param strDescription the mode name
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the name of the mode
     *
     * @return mode name
     */
    public String getDescription(  )
    {
        return _strDescription;
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

    /**
     * @return Returns the _strOutputXslPropertyEncoding.
     */
    public String getOutputXslPropertyEncoding(  )
    {
        return _strOutputXslPropertyEncoding;
    }

    /**
     * @param strOutPutXslPropertyEncoding The _strOutputXslPropertyEncoding to set.
     */
    public void setOutputXslPropertyEncoding( String strOutPutXslPropertyEncoding )
    {
        _strOutputXslPropertyEncoding = ( strOutPutXslPropertyEncoding == null ) ? EMPTY_STRING
                                                                                 : strOutPutXslPropertyEncoding;
    }

    /**
     * @return Returns the _strOutputXslPropertyIndent.
     */
    public String getOutputXslPropertyIndent(  )
    {
        return _strOutputXslPropertyIndent;
    }

    /**
     * @param strOutPutXslPropertyIndent The _strOutputXslPropertyIndent to set.
     */
    public void setOutputXslPropertyIndent( String strOutPutXslPropertyIndent )
    {
        _strOutputXslPropertyIndent = ( strOutPutXslPropertyIndent == null ) ? EMPTY_STRING : strOutPutXslPropertyIndent;
    }

    /**
     * @return Returns the _strOutputXslPropertyMediaType.
     */
    public String getOutputXslPropertyMediaType(  )
    {
        return _strOutputXslPropertyMediaType;
    }

    /**
     * @param strOutPutXslPropertyMediaType The _strOutputXslPropertyMediaType to set.
     */
    public void setOutputXslPropertyMediaType( String strOutPutXslPropertyMediaType )
    {
        _strOutputXslPropertyMediaType = ( strOutPutXslPropertyMediaType == null ) ? EMPTY_STRING
                                                                                   : strOutPutXslPropertyMediaType;
    }

    /**
     * @return Returns the _strOutputXslPropertyMethod.
     */
    public String getOutputXslPropertyMethod(  )
    {
        return _strOutputXslPropertyMethod;
    }

    /**
     * @param strOutPutXslPropertyMethod The _strOutputXslPropertyMethod to set.
     */
    public void setOutputXslPropertyMethod( String strOutPutXslPropertyMethod )
    {
        _strOutputXslPropertyMethod = ( strOutPutXslPropertyMethod == null ) ? EMPTY_STRING : strOutPutXslPropertyMethod;
    }

    /**
     * @return Returns the _strOutputXslPropertyOmitXmlDeclaration.
     */
    public String getOutputXslPropertyOmitXmlDeclaration(  )
    {
        return _strOutputXslPropertyOmitXmlDeclaration;
    }

    /**
     * @param strOutPutXslPropertyOmitXmlDeclaration The _strOutputXslPropertyOmitXmlDeclaration to set.
     */
    public void setOutputXslPropertyOmitXmlDeclaration( String strOutPutXslPropertyOmitXmlDeclaration )
    {
        _strOutputXslPropertyOmitXmlDeclaration = ( strOutPutXslPropertyOmitXmlDeclaration == null ) ? EMPTY_STRING
                                                                                                     : strOutPutXslPropertyOmitXmlDeclaration;
    }

    /**
     * @return Returns the _strOutputXslPropertyStandalone.
     */
    public String getOutputXslPropertyStandalone(  )
    {
        return _strOutputXslPropertyStandalone;
    }

    /**
     * @param strOutPutXslPropertyStandalone The _strOutputXslPropertyStandalone to set.
     */
    public void setOutputXslPropertyStandalone( String strOutPutXslPropertyStandalone )
    {
        _strOutputXslPropertyStandalone = ( strOutPutXslPropertyStandalone == null ) ? EMPTY_STRING
                                                                                     : strOutPutXslPropertyStandalone;
    }

    /**
     * @return Returns the _strOutputXslPropertyVersion.
     */
    public String getOutputXslPropertyVersion(  )
    {
        return _strOutputXslPropertyVersion;
    }

    /**
     * @param strOutPutXslPropertyVersion The _strOutputXslPropertyVersion to set.
     */
    public void setOutputXslPropertyVersion( String strOutPutXslPropertyVersion )
    {
        _strOutputXslPropertyVersion = ( strOutPutXslPropertyVersion == null ) ? EMPTY_STRING
                                                                               : strOutPutXslPropertyVersion;
    }
}
