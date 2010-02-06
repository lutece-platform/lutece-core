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
package fr.paris.lutece.portal.business.rss;


/**
 *
 * class ResourceRssType
 *
 */
public class ResourceRssType implements IResourceRssType
{
    private String _strTitleI18nKey;
    private String _strTitle;
    private String _strClassName;
    private String _strKey;

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRssType#getKey()
     */
    public String getKey(  )
    {
        return _strKey;
    }

    /* (non-Javadoc)
    * @see fr.paris.lutece.portal.business.rss.IResourceRssType#setKey(java.lang.String)
    */
    public void setKey( String key )
    {
        _strKey = key;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRssType#getTitleI18nKey()
     */
    public String getTitleI18nKey(  )
    {
        return _strTitleI18nKey;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRssType#setTitleI18nKey(java.lang.String)
     */
    public void setTitleI18nKey( String title )
    {
        _strTitleI18nKey = title;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.rss.IResourceRssType#getClassName()
         */
    public String getClassName(  )
    {
        return _strClassName;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.rss.IResourceRssType#setClassName(java.lang.String)
         */
    public void setClassName( String className )
    {
        _strClassName = className;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRssType#getTitle()
     */
    public String getTitle(  )
    {
        // TODO Auto-generated method stub
        return _strTitle;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.portal.business.rss.IResourceRssType#setTitle(java.lang.String)
     */
    public void setTitle( String title )
    {
        _strTitle = title;
    }
}
