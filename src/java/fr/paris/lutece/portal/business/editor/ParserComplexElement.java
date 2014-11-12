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
package fr.paris.lutece.portal.business.editor;


/**
 *
 * ParserComplexElementClass
 *
 */
public class ParserComplexElement
{
    private String _strTagName;
    private String _strOpenSubstWithParam;
    private String _strCloseSubstWithParam;
    private String _strOpenSubstWithoutParam;
    private String _strCloseSubstWithoutParam;
    private String _strInternalSubst;
    private boolean _bProcessInternalTags;
    private boolean _bAcceptParam;
    private boolean _bRequiresQuotedParam;

    /**
     *
     * @param strTagName strTagName
     * @param strOpenSubstWithParam strOpenSubstWithParam
     * @param strCloseSubstWithParam strCloseSubstWithParam
     * @param strOpenSubstWithoutParam strOpenSubstWithoutParam
     * @param strCloseSubstWithoutParam strCloseSubstWithoutParam
     * @param strInternalSubst strInternalSubst
     * @param bProcessInternalTags bProcessInternalTags
     * @param bAcceptParam bAcceptParam
     * @param bRequiresQuotedParam bRequiresQuotedParam
     */
    public ParserComplexElement( String strTagName, String strOpenSubstWithParam, String strCloseSubstWithParam,
        String strOpenSubstWithoutParam, String strCloseSubstWithoutParam, String strInternalSubst,
        boolean bProcessInternalTags, boolean bAcceptParam, boolean bRequiresQuotedParam )
    {
        _strTagName = strTagName;
        _strOpenSubstWithParam = strOpenSubstWithParam;
        _strCloseSubstWithParam = strCloseSubstWithParam;
        _strOpenSubstWithoutParam = strOpenSubstWithoutParam;
        _strCloseSubstWithoutParam = strCloseSubstWithoutParam;
        _strInternalSubst = strInternalSubst;
        _bProcessInternalTags = bProcessInternalTags;
        _bAcceptParam = bAcceptParam;
        _bRequiresQuotedParam = bRequiresQuotedParam;
    }

    /**
     *
     * @return return tag name
     */
    public String getTagName(  )
    {
        return _strTagName;
    }

    /**
     * set tag name
     * @param strTagName strTagName
     */
    public void setTagName( String strTagName )
    {
        _strTagName = strTagName;
    }

    /**
     *OpenSubstWithParam(
     * @return OpenSubstWithParam(
     */
    public String getOpenSubstWithParam(  )
    {
        return _strOpenSubstWithParam;
    }

    /**
     *
     * @param strOpenSubstWithParam strOpenSubstWithParam
     */
    public void setOpenSubstWithParam( String strOpenSubstWithParam )
    {
        _strOpenSubstWithParam = strOpenSubstWithParam;
    }

    /**
     *
     * @return CloseSubstWithParam
     */
    public String getCloseSubstWithParam(  )
    {
        return _strCloseSubstWithParam;
    }

    /**
     *
     * @param strCloseSubstWithParam trCloseSubstWithParam
     */
    public void setCloseSubstWithParam( String strCloseSubstWithParam )
    {
        _strCloseSubstWithParam = strCloseSubstWithParam;
    }

    /**
     *
     * @return OpenSubstWithoutParam
     */
    public String getOpenSubstWithoutParam(  )
    {
        return _strOpenSubstWithoutParam;
    }

    /**
     *
     * @param strOpenSubstWithoutParam strOpenSubstWithoutParam
     */
    public void setOpenSubstWithoutParam( String strOpenSubstWithoutParam )
    {
        _strOpenSubstWithoutParam = strOpenSubstWithoutParam;
    }

    /**
     *
     * @return CloseSubstWithoutParam
     */
    public String getCloseSubstWithoutParam(  )
    {
        return _strCloseSubstWithoutParam;
    }

    /**
     *
     * @param strCloseSubstWithoutParam strCloseSubstWithoutParam
     */
    public void setCloseSubstWithoutParam( String strCloseSubstWithoutParam )
    {
        _strCloseSubstWithoutParam = strCloseSubstWithoutParam;
    }

    /**
     *
     * @return InternalSubst
     */
    public String getInternalSubst(  )
    {
        return _strInternalSubst;
    }

    /**
     *
     * @param strInternalSubst strInternalSubst
     */
    public void setInternalSubst( String strInternalSubst )
    {
        _strInternalSubst = strInternalSubst;
    }

    /**
     *
     * @return isProcessInternalTags
     */
    public boolean isProcessInternalTags(  )
    {
        return _bProcessInternalTags;
    }

    /**
     *
     * @param bProcessInternalTags bProcessInternalTags
     */
    public void setProcessInternalTags( boolean bProcessInternalTags )
    {
        _bProcessInternalTags = bProcessInternalTags;
    }

    /**
     *
     * @return isAcceptParam
     */
    public boolean isAcceptParam(  )
    {
        return _bAcceptParam;
    }

    /**
     *
     * @param bAcceptParam bAcceptParam
     */
    public void setAcceptParam( boolean bAcceptParam )
    {
        _bAcceptParam = bAcceptParam;
    }

    /**
     *
     * @return RequiresQuotedParam
     */
    public boolean isRequiresQuotedParam(  )
    {
        return _bRequiresQuotedParam;
    }

    /**
     *
     * @param bRequiresQuotedParam bRequiresQuotedParam
     */
    public void setRequiresQuotedParam( boolean bRequiresQuotedParam )
    {
        _bRequiresQuotedParam = bRequiresQuotedParam;
    }
}
