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
package fr.paris.lutece.portal.business.rss;


/**
 *
 * Class ResourceRssType that provides the resources type for rss
 *
 */
public class ResourceRssType implements IResourceRssType
{
    private String _strTitleI18nKey;
    private String _strTitle;
    private String _strClassName;
    private String _strKey;

    /**
     * Get resource rss type
     * @return  key of the resourceRss type
     */
    public String getKey(  )
    {
        return _strKey;
    }

    /**
     * Set the  key  of the resourceRss type
     * @param key title the key of the text type
     */
    public void setKey( String key )
    {
        _strKey = key;
    }

    /**
     * Get title key of the resourceRss type
     * @return the I18n title key of the resourceRss type
     */
    public String getTitleI18nKey(  )
    {
        return _strTitleI18nKey;
    }

    /**
     * Set the I18n title key  of the resourceRss type
     * @param title the title of the resourceRss type
     */
    public void setTitleI18nKey( String title )
    {
        _strTitleI18nKey = title;
    }

    /**
     * Get the path for acces to the Class resourceRss
     * @return the path for acces to the Classe resourceRss
     */
    public String getClassName(  )
    {
        return _strClassName;
    }

    /**
     * set the path for acces to the Class resourceRss
     * @param className the path for acces to the Class resourceRss
     */
    public void setClassName( String className )
    {
        _strClassName = className;
    }

    /**
    * Get the  title of the resourceRss type
    * @return the title of the resourceRss type
    */
    public String getTitle(  )
    {
        // TODO Auto-generated method stub
        return _strTitle;
    }

    /**
     * Set the  title of the resourceRss type
     * @param title the title of the resourceRss type
     */
    public void setTitle( String title )
    {
        _strTitle = title;
    }
}
